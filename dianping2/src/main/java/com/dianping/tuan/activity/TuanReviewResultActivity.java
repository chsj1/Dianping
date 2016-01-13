package com.dianping.tuan.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.base.ugc.widget.RecommendDealView;
import com.dianping.base.widget.MeasuredListView;
import com.dianping.base.widget.RightTitleButton;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;

public class TuanReviewResultActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  TextView contentView;
  String dealTitle;
  MyAdapter mAdapter;
  MApiRequest remindReViewRequest;
  TextView remindReviewTitleView;
  int shopId;
  String successMsg;
  MeasuredListView tableView;
  TextView titleView;
  Button uploadPicBtn;

  private void gotoMe()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://me"));
    localIntent.addFlags(67108864);
    startActivity(localIntent);
    finish();
  }

  private void setTitleBar()
  {
    getTitleBar().setLeftView(-1, null);
    RightTitleButton localRightTitleButton = new RightTitleButton(this);
    localRightTitleButton.setText("关闭");
    getTitleBar().addRightViewItem(localRightTitleButton, "close", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TuanReviewResultActivity.this.gotoMe();
      }
    });
  }

  private void setupView()
  {
    this.titleView = ((TextView)findViewById(R.id.title));
    this.contentView = ((TextView)findViewById(R.id.content));
    this.tableView = ((MeasuredListView)findViewById(R.id.tableView));
    this.remindReviewTitleView = ((TextView)findViewById(R.id.remind_review_title));
    this.uploadPicBtn = ((Button)findViewById(R.id.toupload));
    this.uploadPicBtn.setVisibility(8);
    this.uploadPicBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        UploadPhotoUtil.uploadShopPhoto(TuanReviewResultActivity.this, TuanReviewResultActivity.this.shopId);
      }
    });
    this.titleView.setText(this.dealTitle);
    SpannableStringBuilder localSpannableStringBuilder = com.dianping.util.TextUtils.jsonParseText(this.successMsg);
    if (!android.text.TextUtils.isEmpty(localSpannableStringBuilder))
      this.contentView.setText(localSpannableStringBuilder);
    this.mAdapter = new MyAdapter();
    this.tableView.setAdapter(this.mAdapter);
    this.mAdapter.notifyDataSetChanged();
  }

  protected void getRemindReview()
  {
    if (this.remindReViewRequest != null)
      return;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://app.t.dianping.com/");
    localStringBuilder.append("remindreviewsgn.bin");
    localStringBuilder.append("?token=").append(accountService().token());
    localStringBuilder.append("&cityid=").append(city().id());
    this.remindReViewRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.remindReViewRequest, this);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.dealTitle = getStringParam("dealtitle");
    this.successMsg = getStringParam("successmsg");
    this.shopId = getIntParam("shopid");
    setContentView(R.layout.tuan_review_result);
    setTitleBar();
    setupView();
  }

  protected void onDestroy()
  {
    if (this.remindReViewRequest != null)
    {
      mapiService().abort(this.remindReViewRequest, this, true);
      this.remindReViewRequest = null;
    }
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      gotoMe();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.remindReViewRequest)
    {
      Toast.makeText(this, paramMApiResponse.message().toString(), 0).show();
      this.remindReViewRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.remindReViewRequest)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
        this.mAdapter.appendList((DPObject)paramMApiResponse.result(), null);
      this.remindReViewRequest = null;
    }
  }

  class MyAdapter extends BasicAdapter
  {
    protected String errorMsg;
    boolean isEnd;
    ArrayList<DPObject> reviewInfos = new ArrayList();

    MyAdapter()
    {
    }

    private void appendList(DPObject paramDPObject, String paramString)
    {
      if (paramDPObject != null)
      {
        this.reviewInfos.clear();
        this.reviewInfos.addAll(Arrays.asList(paramDPObject.getArray("List")));
        this.isEnd = paramDPObject.getBoolean("IsEnd");
      }
      this.errorMsg = paramString;
      if (this.reviewInfos.size() == 0)
        TuanReviewResultActivity.this.remindReviewTitleView.setVisibility(8);
      notifyDataSetChanged();
    }

    public int getCount()
    {
      if (this.isEnd)
        return this.reviewInfos.size();
      return this.reviewInfos.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.reviewInfos.size())
        return this.reviewInfos.get(paramInt);
      if (!this.isEnd)
      {
        if (this.reviewInfos.size() == 0)
          return LOADING;
        return LAST_EXTRA;
      }
      return null;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        if ((paramView != null) && ((paramView instanceof RecommendDealView)));
        for (paramView = (RecommendDealView)paramView; ; paramView = null)
        {
          paramViewGroup = paramView;
          if (paramView == null)
            paramViewGroup = (RecommendDealView)LayoutInflater.from(TuanReviewResultActivity.this).inflate(R.layout.recommend_deal_view, null, false);
          paramViewGroup.setClickable(true);
          paramInt = ((DPObject)localObject).getInt("RateId");
          paramViewGroup.setOnClickListener(new TuanReviewResultActivity.MyAdapter.1(this, paramInt));
          paramViewGroup.getReviewBtn().setOnClickListener(new TuanReviewResultActivity.MyAdapter.2(this, paramInt));
          paramViewGroup.setDeal((DPObject)localObject, false);
          return paramViewGroup;
        }
      }
      if (localObject == LOADING)
      {
        TuanReviewResultActivity.this.getRemindReview();
        return getLoadingView(paramViewGroup, paramView);
      }
      if (localObject == LAST_EXTRA)
      {
        paramView = LayoutInflater.from(TuanReviewResultActivity.this).inflate(R.layout.tuan_to_review_more_item, null, false);
        paramView.setOnClickListener(new TuanReviewResultActivity.MyAdapter.3(this));
        return paramView;
      }
      return null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanReviewResultActivity
 * JD-Core Version:    0.6.0
 */
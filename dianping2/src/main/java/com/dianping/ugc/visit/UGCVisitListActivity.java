package com.dianping.ugc.visit;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;

public class UGCVisitListActivity extends NovaActivity
{
  private boolean isFirstFriendList = true;
  private boolean isFirstWebberList = true;
  private boolean isFriendListRequest = true;
  private int mWebberTitleIndex;
  private int shopId;

  public void generateGrowthContentView(String[] paramArrayOfString, LinearLayout paramLinearLayout)
  {
    int i = 0;
    while (i < paramArrayOfString.length)
    {
      TextView localTextView = new TextView(this);
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
      if (i != paramArrayOfString.length - 1)
        localLayoutParams.bottomMargin = ViewUtils.dip2px(this, 9.0F);
      localLayoutParams.leftMargin = ViewUtils.dip2px(this, 54.0F);
      localTextView.setTextColor(getResources().getColor(R.color.text_hint_light_gray));
      localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
      TextUtils.setJsonText(paramArrayOfString[i], localTextView);
      paramLinearLayout.addView(localTextView, localLayoutParams);
      i += 1;
    }
  }

  public String getPageName()
  {
    return "checkinlist";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void initView()
  {
    setTitle("来过");
    Object localObject1 = (DPObject)getIntent().getParcelableExtra("checkinsuccmsg");
    ListView localListView = (ListView)findViewById(R.id.ugc_hasvisit_list);
    if (localObject1 != null)
    {
      NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)LayoutInflater.from(this).inflate(R.layout.ugc_hasvisit_list_header_layout, null, false);
      Object localObject2 = (TextView)localNovaLinearLayout.findViewById(R.id.ugc_checkin_success_tip);
      TextUtils.setJsonText(((DPObject)localObject1).getString("Notice"), (TextView)localObject2);
      localObject2 = ((DPObject)localObject1).getObject("Level");
      if (localObject2 != null)
      {
        TextView localTextView = (TextView)localNovaLinearLayout.findViewById(R.id.ugc_checkin_growth_tip);
        TextUtils.setJsonText(((DPObject)localObject2).getString("ToLevel"), localTextView);
      }
      localObject1 = ((DPObject)localObject1).getStringArray("Content");
      if (localObject1 != null)
      {
        localObject2 = (LinearLayout)localNovaLinearLayout.findViewById(R.id.ugc_hasvisit_growth_layout);
        ((LinearLayout)localObject2).setVisibility(0);
        generateGrowthContentView(localObject1, (LinearLayout)localObject2);
      }
      addGAView(localNovaLinearLayout, 0);
      localListView.addHeaderView(localNovaLinearLayout, null, false);
    }
    localListView.setAdapter(new hasVisitAdapter(this));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.ugc_hasvisit_layout);
    this.shopId = getIntParam("shopid");
    initView();
  }

  class hasVisitAdapter extends BasicLoadAdapter
  {
    private static final String FRIEND_REVIEW = "friendreview";
    private static final String WEBBER_REVIEW = "webberreview";
    private Context context;

    public hasVisitAdapter(Context arg2)
    {
      super();
      this.context = localContext;
    }

    private NovaTextView getTitleView()
    {
      NovaTextView localNovaTextView = new NovaTextView(this.context);
      localNovaTextView.setBackgroundResource(R.drawable.ugc_hasvisit_list_title);
      localNovaTextView.setTextColor(UGCVisitListActivity.this.getResources().getColor(R.color.deep_gray));
      localNovaTextView.setTextSize(0, UGCVisitListActivity.this.getResources().getDimensionPixelSize(R.dimen.text_size_15));
      localNovaTextView.setPadding(ViewUtils.dip2px(this.context, 15.0F), ViewUtils.dip2px(this.context, 25.0F), 0, ViewUtils.dip2px(this.context, 15.0F));
      return localNovaTextView;
    }

    public MApiRequest createRequest(int paramInt)
    {
      Uri.Builder localBuilder;
      if (UGCVisitListActivity.this.isFriendListRequest)
      {
        localBuilder = Uri.parse("http://m.api.dianping.com/friendship/getshopfriendfeedlist.bin").buildUpon();
        localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
      }
      while (true)
      {
        localBuilder.appendQueryParameter("shopid", String.valueOf(UGCVisitListActivity.this.shopId));
        return UGCVisitListActivity.this.mapiGet(this, localBuilder.toString(), CacheType.DISABLED);
        localBuilder = Uri.parse("http://m.api.dianping.com/review/getshortfeedlist.bin").buildUpon();
        if (UGCVisitListActivity.this.isFirstWebberList)
        {
          localBuilder.appendQueryParameter("start", "0");
          continue;
        }
        localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
      }
    }

    public int getCount()
    {
      if ((this.mIsEnd) && (this.mData.size() == 0))
        return 0;
      return super.getCount();
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = super.getView(paramInt, paramView, paramViewGroup);
      if (getItem(paramInt) == EMPTY)
        paramView.setVisibility(8);
      return paramView;
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getTitleView();
      if (paramDPObject.isClass("friendreview"))
      {
        ((NovaTextView)localObject).setText("好友来过");
        ((NovaTextView)localObject).setId(R.id.friendbeen);
        UGCVisitListActivity.this.addGAView((View)localObject, 0);
        return localObject;
      }
      if (paramDPObject.isClass("webberreview"))
      {
        ((NovaTextView)localObject).setText("网友来过");
        return localObject;
      }
      if ((paramView instanceof RelativeLayout));
      for (paramView = (NovaRelativeLayout)paramView; ; paramView = (NovaRelativeLayout)LayoutInflater.from(this.context).inflate(R.layout.ugc_hasvisit_list_item, paramViewGroup, false))
      {
        paramViewGroup = paramDPObject.getObject("User");
        if (paramViewGroup != null)
        {
          localObject = (DPNetworkImageView)paramView.findViewById(R.id.ugc_hasvisit_avater);
          ((DPNetworkImageView)localObject).setImage(paramViewGroup.getString("Avatar"));
          ((DPNetworkImageView)localObject).setGAString("profile");
          NovaTextView localNovaTextView = (NovaTextView)paramView.findViewById(R.id.ugc_hasvisit_user_petname);
          localNovaTextView.setGAString("profile");
          localNovaTextView.setText(paramViewGroup.getString("Nick"));
          UGCVisitListActivity.hasVisitAdapter.1 local1 = new UGCVisitListActivity.hasVisitAdapter.1(this, paramViewGroup);
          ((DPNetworkImageView)localObject).setOnClickListener(local1);
          localNovaTextView.setOnClickListener(local1);
          ((DPNetworkImageView)paramView.findViewById(R.id.ugc_hasvisit_user_level)).setImage(paramViewGroup.getString("UserLevel"));
          paramView.setOnClickListener(new UGCVisitListActivity.hasVisitAdapter.2(this, paramDPObject));
        }
        ((TextView)paramView.findViewById(R.id.ugc_hasvisit_add_time)).setText(paramDPObject.getString("AddTime"));
        ((TextView)paramView.findViewById(R.id.ugc_hasvisit_add_type_content)).setText(paramDPObject.getString("Content"));
        return paramView;
      }
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (UGCVisitListActivity.this.isFriendListRequest)
      {
        UGCVisitListActivity.access$002(UGCVisitListActivity.this, false);
        reset();
        return;
      }
      super.onRequestFailed(paramMApiRequest, paramMApiResponse);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
      if (UGCVisitListActivity.this.isFriendListRequest)
      {
        if ((this.mData != null) && (this.mData.size() > 0))
        {
          if (UGCVisitListActivity.this.isFirstFriendList)
          {
            this.mData.add(0, new DPObject("friendreview"));
            UGCVisitListActivity.access$302(UGCVisitListActivity.this, false);
            notifyDataSetChanged();
          }
          UGCVisitListActivity.access$402(UGCVisitListActivity.this, this.mData.size());
        }
        if (this.mIsEnd)
        {
          UGCVisitListActivity.access$002(UGCVisitListActivity.this, false);
          this.mIsEnd = false;
        }
      }
      do
      {
        loadNewPage();
        do
          return;
        while (!UGCVisitListActivity.this.isFirstWebberList);
        paramMApiRequest = null;
        if (!(paramMApiResponse.result() instanceof DPObject))
          continue;
        paramMApiRequest = ((DPObject)paramMApiResponse.result()).getArray("List");
      }
      while ((paramMApiRequest == null) || (paramMApiRequest.length <= 0));
      this.mData.add(UGCVisitListActivity.this.mWebberTitleIndex, new DPObject("webberreview"));
      UGCVisitListActivity.access$102(UGCVisitListActivity.this, false);
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.visit.UGCVisitListActivity
 * JD-Core Version:    0.6.0
 */
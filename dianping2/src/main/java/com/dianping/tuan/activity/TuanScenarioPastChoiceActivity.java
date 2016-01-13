package com.dianping.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.DealListItem;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.util.ArrayList;
import java.util.Arrays;

public class TuanScenarioPastChoiceActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>, PullToRefreshListView.OnRefreshListener
{
  protected static final int LIMIT = 25;
  public static final Object RECOMMEND_IMAGE = new Object();
  protected Context context;
  protected DPObject dpRecommendResult = null;
  protected String emptyMsg;
  protected String errorMsg;
  protected int eventsceneid = 1;
  protected boolean isEnd;
  protected boolean isFirstPage = true;
  protected FrameLayout layerEmpty;
  protected ListView lvPastChoice;
  protected int nextStartIndex;
  protected PastChoiceAdapter pastChoiceAdapter;
  protected MApiRequest pastChoiceRequest;
  protected PullToRefreshListView ptrlv;
  protected int recordCount;
  protected int tabamount = 0;
  protected TextView tvEmpty;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.context = this;
    paramBundle = getIntent().getData();
    if (paramBundle.getQueryParameter("eventsceneid") == null)
      return;
    this.eventsceneid = Integer.parseInt(paramBundle.getQueryParameter("eventsceneid"));
    if (paramBundle.getQueryParameter("tabamount") == null);
    for (this.tabamount = 0; ; this.tabamount = Integer.parseInt(paramBundle.getQueryParameter("tabamount")))
    {
      setContentView(R.layout.tuan_scenario_past_choice_activity);
      this.ptrlv = ((PullToRefreshListView)findViewById(R.id.tuanscenario_pastchoicelist_ptr));
      this.ptrlv.setOnRefreshListener(this);
      this.lvPastChoice = this.ptrlv;
      this.lvPastChoice.setDivider(getResources().getDrawable(R.drawable.list_divider_right_inset));
      this.layerEmpty = ((FrameLayout)findViewById(R.id.tuanscenario_pastchoicelist_empty));
      this.lvPastChoice.setEmptyView(this.tvEmpty);
      this.pastChoiceAdapter = new PastChoiceAdapter();
      this.lvPastChoice.setAdapter(this.pastChoiceAdapter);
      return;
    }
  }

  protected void onDestroy()
  {
    if (this.pastChoiceRequest != null)
    {
      mapiService().abort(this.pastChoiceRequest, this, true);
      this.pastChoiceRequest = null;
    }
    super.onDestroy();
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    this.pastChoiceAdapter.refresh();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.pastChoiceRequest)
    {
      this.ptrlv.onRefreshComplete();
      this.errorMsg = paramMApiResponse.toString();
      this.pastChoiceAdapter.notifyDataSetChanged();
      this.pastChoiceRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.pastChoiceRequest)
    {
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "RecommendResult"))
      {
        this.ptrlv.onRefreshComplete();
        this.dpRecommendResult = ((DPObject)paramMApiResponse);
        this.pastChoiceAdapter.appendPastChoice(this.dpRecommendResult.getObject("DealList"));
      }
      this.pastChoiceRequest = null;
    }
  }

  protected void requestPastChoice()
  {
    if (this.pastChoiceRequest != null)
      return;
    if (this.nextStartIndex == 0)
      this.isFirstPage = true;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://app.t.dianping.com/");
    localStringBuilder.append("scenariopastchoicegn.bin");
    localStringBuilder.append("?eventsceneid=" + this.eventsceneid);
    if (this.tabamount > 0)
      localStringBuilder.append("&tabamount=" + this.tabamount);
    localStringBuilder.append("&limit=25");
    localStringBuilder.append("&start=" + this.nextStartIndex);
    localStringBuilder.append("&cityid=" + city().id());
    if (isLogined())
      localStringBuilder.append("&token=" + accountService().token());
    if (location() != null)
    {
      localStringBuilder.append("&lat=" + location().latitude());
      localStringBuilder.append("&lng=" + location().longitude());
    }
    localStringBuilder.append("&dpid=").append(preferences(this.context).getString("dpid", ""));
    this.pastChoiceRequest = new BasicMApiRequest(localStringBuilder.toString(), "GET", null, CacheType.DISABLED, false, null);
    mapiService().exec(this.pastChoiceRequest, this);
  }

  class PastChoiceAdapter extends BasicAdapter
  {
    protected ArrayList<DPObject> dpPastChoiceList = new ArrayList();
    protected String imageUrl = "";

    PastChoiceAdapter()
    {
    }

    protected void appendPastChoice(DPObject paramDPObject)
    {
      if ((TuanScenarioPastChoiceActivity.this.isFirstPage) && (!TextUtils.isEmpty(TuanScenarioPastChoiceActivity.this.dpRecommendResult.getString("ImageUrl"))))
        this.imageUrl = TuanScenarioPastChoiceActivity.this.dpRecommendResult.getString("ImageUrl");
      TuanScenarioPastChoiceActivity.this.isFirstPage = false;
      DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
      if (arrayOfDPObject != null)
      {
        if (TuanScenarioPastChoiceActivity.this.nextStartIndex != 0)
          break label212;
        this.dpPastChoiceList.clear();
        this.dpPastChoiceList.addAll(Arrays.asList(arrayOfDPObject));
      }
      while (true)
      {
        TuanScenarioPastChoiceActivity.this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
        TuanScenarioPastChoiceActivity.this.isEnd = paramDPObject.getBoolean("IsEnd");
        TuanScenarioPastChoiceActivity.this.emptyMsg = paramDPObject.getString("EmptyMsg");
        TuanScenarioPastChoiceActivity.this.recordCount = paramDPObject.getInt("RecordCount");
        if ((!TuanScenarioPastChoiceActivity.this.isEnd) && (arrayOfDPObject.length == 0))
          TuanScenarioPastChoiceActivity.this.isEnd = true;
        if ((TuanScenarioPastChoiceActivity.this.isEnd) && (this.dpPastChoiceList.size() <= 0) && (TuanScenarioPastChoiceActivity.this.layerEmpty != null))
          TuanScenarioPastChoiceActivity.this.layerEmpty.removeAllViews();
        notifyDataSetChanged();
        return;
        label212: this.dpPastChoiceList.addAll(Arrays.asList(arrayOfDPObject));
      }
    }

    public int getCount()
    {
      if (TextUtils.isEmpty(this.imageUrl))
      {
        if (TuanScenarioPastChoiceActivity.this.isEnd)
          return this.dpPastChoiceList.size();
        return this.dpPastChoiceList.size() + 1;
      }
      if (TuanScenarioPastChoiceActivity.this.isEnd)
        return this.dpPastChoiceList.size() + 1;
      return this.dpPastChoiceList.size() + 1 + 1;
    }

    public Object getItem(int paramInt)
    {
      if (TextUtils.isEmpty(this.imageUrl))
      {
        if (paramInt < this.dpPastChoiceList.size())
          return this.dpPastChoiceList.get(paramInt);
        if (TuanScenarioPastChoiceActivity.this.errorMsg == null)
          return LOADING;
        return ERROR;
      }
      if (paramInt == 0)
        return TuanScenarioPastChoiceActivity.RECOMMEND_IMAGE;
      if ((paramInt > 0) && (paramInt <= this.dpPastChoiceList.size()))
        return this.dpPastChoiceList.get(paramInt - 1);
      if (TuanScenarioPastChoiceActivity.this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DealListItem localDealListItem = null;
      Object localObject = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(localObject, "Deal"))
      {
        localObject = (DPObject)localObject;
        if ((paramView instanceof DealListItem))
          localDealListItem = (DealListItem)paramView;
        paramView = localDealListItem;
        if (localDealListItem == null)
          paramView = (DealListItem)LayoutInflater.from(TuanScenarioPastChoiceActivity.this.context).inflate(R.layout.deal_list_item, paramViewGroup, false);
        if (TuanScenarioPastChoiceActivity.this.location() != null)
          paramView.setDeal((DPObject)localObject, TuanScenarioPastChoiceActivity.this.location().offsetLatitude(), TuanScenarioPastChoiceActivity.this.location().offsetLongitude(), NovaConfigUtils.isShowImageInMobileNetwork(), 1, true, paramInt);
        while (true)
        {
          paramView.setTag(localObject);
          paramView.setOnClickListener(new TuanScenarioPastChoiceActivity.PastChoiceAdapter.1(this));
          return paramView;
          paramView.setDeal((DPObject)localObject, 0.0D, 0.0D, NovaConfigUtils.isShowImageInMobileNetwork(), 1, true, paramInt);
        }
      }
      if (localObject == LOADING)
      {
        if (TuanScenarioPastChoiceActivity.this.errorMsg == null)
          loadNewPage();
        return getLoadingView(paramViewGroup, paramView);
      }
      if (localObject == TuanScenarioPastChoiceActivity.RECOMMEND_IMAGE)
      {
        paramView = LayoutInflater.from(TuanScenarioPastChoiceActivity.this.context).inflate(R.layout.tuanscenario_headimage_view, null);
        ((NetworkImageView)paramView.findViewById(R.id.tuanscenario_image)).setImage(TuanScenarioPastChoiceActivity.this.dpRecommendResult.getString("ImageUrl"));
        return paramView;
      }
      return (View)getFailedView(TuanScenarioPastChoiceActivity.this.errorMsg, new TuanScenarioPastChoiceActivity.PastChoiceAdapter.2(this), paramViewGroup, paramView);
    }

    protected boolean loadNewPage()
    {
      if (TuanScenarioPastChoiceActivity.this.isEnd);
      do
        return false;
      while (TuanScenarioPastChoiceActivity.this.pastChoiceRequest != null);
      TuanScenarioPastChoiceActivity.this.errorMsg = null;
      TuanScenarioPastChoiceActivity.this.requestPastChoice();
      return true;
    }

    public void refresh()
    {
      if (TuanScenarioPastChoiceActivity.this.pastChoiceRequest != null)
      {
        TuanScenarioPastChoiceActivity.this.mapiService().abort(TuanScenarioPastChoiceActivity.this.pastChoiceRequest, null, true);
        TuanScenarioPastChoiceActivity.this.pastChoiceRequest = null;
      }
      this.imageUrl = "";
      TuanScenarioPastChoiceActivity.this.isEnd = false;
      TuanScenarioPastChoiceActivity.this.recordCount = 0;
      TuanScenarioPastChoiceActivity.this.nextStartIndex = 0;
      TuanScenarioPastChoiceActivity.this.errorMsg = null;
      TuanScenarioPastChoiceActivity.this.emptyMsg = null;
      loadNewPage();
    }

    public void reset()
    {
      if (TuanScenarioPastChoiceActivity.this.pastChoiceRequest != null)
      {
        TuanScenarioPastChoiceActivity.this.mapiService().abort(TuanScenarioPastChoiceActivity.this.pastChoiceRequest, null, true);
        TuanScenarioPastChoiceActivity.this.pastChoiceRequest = null;
      }
      this.imageUrl = "";
      this.dpPastChoiceList.clear();
      TuanScenarioPastChoiceActivity.this.isEnd = false;
      TuanScenarioPastChoiceActivity.this.recordCount = 0;
      TuanScenarioPastChoiceActivity.this.nextStartIndex = 0;
      TuanScenarioPastChoiceActivity.this.errorMsg = null;
      TuanScenarioPastChoiceActivity.this.emptyMsg = null;
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanScenarioPastChoiceActivity
 * JD-Core Version:    0.6.0
 */
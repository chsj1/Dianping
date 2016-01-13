package com.dianping.main.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AdapterCellAgent.BasicCellAgentAdapter;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.FindRecommendListItem;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.main.find.FindRecommendSimpleListItem;
import com.dianping.model.City;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Arrays;

public class RecommendListAgent extends AdapterCellAgent
  implements CityConfig.SwitchListener
{
  Adapter adapter;
  HeaderAdapter headerAdapter;
  DPObject mResponse;
  protected String recommendHeaderTag = "31RECOMMENT_HEADER.";
  protected String recommendListTag = "32RECOMMENT_LIST.";
  String title;
  private TextView titleView;

  public RecommendListAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createContentHeaderCell()
  {
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.find_recommend_header, null);
    this.titleView = ((TextView)localView.findViewById(R.id.title));
    if (TextUtils.isEmpty(this.title))
    {
      this.titleView.setText("趣味生活");
      return localView;
    }
    this.titleView.setText(this.title);
    return localView;
  }

  private View createContentHeaderDivider()
  {
    View localView = new View(getContext());
    localView.setLayoutParams(new ViewGroup.LayoutParams(-1, 1));
    localView.setBackgroundColor(-2631721);
    return localView;
  }

  public MApiRequest createRequest(int paramInt)
  {
    return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/operating/findbbslist.bin").buildUpon().appendQueryParameter("cityid", cityId() + "").appendQueryParameter("start", Integer.toString(paramInt)).toString(), CacheType.DISABLED);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.adapter.notifyDataSetChanged();
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    if (paramCity2.id() != paramCity1.id())
    {
      this.mResponse = null;
      this.headerAdapter.reset();
      this.adapter.reset();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    DPApplication.instance().cityConfig().addListener(this);
    this.headerAdapter = new HeaderAdapter(null);
    addCell(this.recommendHeaderTag, this.headerAdapter);
    this.adapter = new Adapter(null);
    addCell(this.recommendListTag, this.adapter);
  }

  public void onDestroy()
  {
    DPApplication.instance().cityConfig().removeListener(this);
    super.onDestroy();
  }

  protected void setContentHeader(String paramString)
  {
    this.title = paramString;
  }

  public void setItemClickListener(View paramView, DPObject paramDPObject)
  {
    paramView.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        paramView = this.val$dpDeal.getString("Url");
        RecommendListAgent.this.statisticsEvent("search5", "search5_forum", this.val$dpDeal.getString("Title"), 0);
        if (!TextUtils.isEmpty(paramView))
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
          RecommendListAgent.this.getFragment().startActivity(paramView);
        }
      }
    });
  }

  private class Adapter extends AdapterCellAgent.BasicCellAgentAdapter
    implements RequestHandler<MApiRequest, MApiResponse>
  {
    final Object MORE = new Object();
    ArrayList<DPObject> itemList = new ArrayList();
    protected String mErrorMsg;
    protected boolean mIsEnd;
    protected int mNextStartIndex;
    protected MApiRequest mReq;
    private int mRetryCount = 0;
    protected MApiService mapiService;

    private Adapter()
    {
      super();
    }

    protected void appendData(DPObject paramDPObject)
    {
      if (paramDPObject != null)
      {
        this.mIsEnd = paramDPObject.getBoolean("IsEnd");
        this.mNextStartIndex = paramDPObject.getInt("NextStartIndex");
        paramDPObject = paramDPObject.getArray("List");
        if ((paramDPObject != null) && (paramDPObject.length > 0))
          this.itemList.addAll(Arrays.asList(paramDPObject));
        notifyDataSetChanged();
      }
    }

    FindRecommendListItem createListItem(boolean paramBoolean)
    {
      MyResources localMyResources = RecommendListAgent.this.res;
      Context localContext = RecommendListAgent.this.getContext();
      if (paramBoolean);
      for (int i = R.layout.find_tab_recommend_list_item; ; i = R.layout.find_recommend_list_item)
        return (FindRecommendListItem)localMyResources.inflate(localContext, i, null, false);
    }

    public int getCount()
    {
      if ((RecommendListAgent.this.getCity().isForeignCity()) || ((RecommendListAgent.this.mResponse != null) && (this.itemList != null) && (this.itemList.size() == 0)));
      do
        return 0;
      while ((this.mIsEnd) && (this.itemList.size() == 0));
      return this.itemList.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.itemList.size())
        return this.itemList.get(paramInt);
      if (this.mIsEnd)
        return this.MORE;
      if (this.mErrorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
        return itemViewWithData((DPObject)localObject, paramInt, paramView, paramViewGroup);
      if (localObject == LOADING)
      {
        if (this.mErrorMsg == null)
          loadNewPage();
        localObject = new NovaLinearLayout(RecommendListAgent.this.getContext());
        AbsListView.LayoutParams localLayoutParams = new AbsListView.LayoutParams(-1, -2);
        ((NovaLinearLayout)localObject).setOrientation(1);
        ((NovaLinearLayout)localObject).setLayoutParams(localLayoutParams);
        if ((this.itemList == null) || (this.itemList.size() == 0))
          ((NovaLinearLayout)localObject).addView(RecommendListAgent.this.createDividerBlock());
        ((NovaLinearLayout)localObject).addView(RecommendListAgent.this.createContentHeaderDivider());
        ((NovaLinearLayout)localObject).addView(getLoadingView(paramViewGroup, paramView, ViewUtils.dip2px(RecommendListAgent.this.getContext(), -1.0F)));
        return localObject;
      }
      if (localObject == this.MORE)
      {
        paramView = (NovaLinearLayout)RecommendListAgent.this.res.inflate(RecommendListAgent.this.getContext(), R.layout.foot_view, paramViewGroup, false);
        paramViewGroup = (TextView)paramView.findViewById(R.id.text);
        if (paramViewGroup != null)
          paramViewGroup.setText("查看更多");
        paramView.setGAString("explore_topic", "查看更多");
        paramView.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            if (RecommendListAgent.this.mResponse != null);
            for (paramView = RecommendListAgent.this.mResponse.getString("Url"); ; paramView = "")
            {
              if (!TextUtils.isEmpty(paramView))
                RecommendListAgent.this.getFragment().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView)));
              return;
            }
          }
        });
        return paramView;
      }
      paramView = getFailedView(this.mErrorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          RecommendListAgent.Adapter.this.mErrorMsg = null;
          RecommendListAgent.Adapter.this.loadNewPage();
        }
      }
      , paramViewGroup, paramView, ViewUtils.dip2px(RecommendListAgent.this.getContext(), -1.0F));
      paramViewGroup = new NovaLinearLayout(RecommendListAgent.this.getContext());
      localObject = new AbsListView.LayoutParams(-1, -2);
      paramViewGroup.setOrientation(1);
      paramViewGroup.setLayoutParams((ViewGroup.LayoutParams)localObject);
      if ((this.itemList == null) || (this.itemList.size() == 0))
        paramViewGroup.addView(RecommendListAgent.this.createDividerBlock());
      paramViewGroup.addView(RecommendListAgent.this.createContentHeaderDivider());
      paramViewGroup.addView(paramView);
      return (View)paramViewGroup;
    }

    public View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      int i = 1;
      boolean bool;
      if (RecommendListAgent.this.getCity() == null)
      {
        bool = false;
        if ((!bool) || (paramInt <= 0))
          break label154;
        paramInt = i;
        label28: if (paramInt == 0)
          break label164;
        if (!(paramView instanceof FindRecommendSimpleListItem))
          break label159;
      }
      label154: label159: for (paramView = (FindRecommendSimpleListItem)paramView; ; paramView = null)
      {
        paramViewGroup = paramView;
        if (paramView == null)
          paramViewGroup = (FindRecommendSimpleListItem)RecommendListAgent.this.res.inflate(RecommendListAgent.this.getContext(), R.layout.find_recommend_simple_item, null, false);
        paramViewGroup.setDeal(paramDPObject);
        paramViewGroup.setMinimumHeight(ViewUtils.dip2px(RecommendListAgent.this.getContext(), 60.0F));
        paramViewGroup.setGAString("explore_topic", RecommendListAgent.this.title);
        RecommendListAgent.this.setItemClickListener(paramViewGroup, paramDPObject);
        return paramViewGroup;
        if (!RecommendListAgent.this.getCity().isForeignCity())
        {
          bool = true;
          break;
        }
        bool = false;
        break;
        paramInt = 0;
        break label28;
      }
      label164: if ((paramView instanceof FindRecommendListItem));
      for (paramView = (FindRecommendListItem)paramView; ; paramView = null)
      {
        paramViewGroup = paramView;
        if (paramView == null)
          paramViewGroup = createListItem(bool);
        paramViewGroup.setDeal(paramDPObject);
        RecommendListAgent.this.setItemClickListener(paramViewGroup, paramDPObject);
        return paramViewGroup;
      }
    }

    protected boolean loadNewPage()
    {
      if (this.mIsEnd)
        return false;
      if ((this.mReq == null) && (DPApplication.instance().cityConfig().currentCity() != null) && (RecommendListAgent.this.getFragment() != null) && (RecommendListAgent.this.cityId() > 0))
      {
        this.mErrorMsg = null;
        this.mReq = RecommendListAgent.this.createRequest(this.mNextStartIndex);
        if (this.mReq != null)
        {
          if (this.mapiService == null)
            this.mapiService = RecommendListAgent.this.getFragment().mapiService();
          if (this.mapiService != null)
          {
            GAHelper.instance().contextStatisticsEvent(RecommendListAgent.this.getContext(), "topicpage", "", 0, "view");
            this.mapiService.exec(this.mReq, this);
          }
        }
        notifyDataSetChanged();
        return true;
      }
      if (this.mReq == null)
        this.mIsEnd = true;
      notifyDataSetChanged();
      return false;
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (this.mReq == paramMApiRequest)
      {
        this.mRetryCount += 1;
        this.mReq = null;
        if (this.mRetryCount >= 3)
        {
          this.mRetryCount = 0;
          setErrorMsg("网络连接失败 点击重新加载");
        }
      }
      else
      {
        return;
      }
      loadNewPage();
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (this.mReq == paramMApiRequest)
      {
        this.mReq = null;
        RecommendListAgent.this.mResponse = ((DPObject)paramMApiResponse.result());
        if (!RecommendListAgent.this.getCity().isForeignCity())
          RecommendListAgent.this.title = RecommendListAgent.this.mResponse.getString("Title");
        appendData(RecommendListAgent.this.mResponse);
      }
    }

    public void reset()
    {
      this.itemList.clear();
      this.mNextStartIndex = 0;
      this.mRetryCount = 0;
      this.mIsEnd = false;
      this.mErrorMsg = null;
      notifyDataSetChanged();
    }

    public void setErrorMsg(String paramString)
    {
      this.mErrorMsg = paramString;
      notifyDataSetChanged();
    }
  }

  private class HeaderAdapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    NovaLinearLayout header;

    private HeaderAdapter()
    {
      super();
    }

    public int getCount()
    {
      if (RecommendListAgent.this.adapter.getCount() > 1)
        return 1;
      return 0;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (this.header == null)
      {
        this.header = new NovaLinearLayout(RecommendListAgent.this.getContext());
        paramView = new AbsListView.LayoutParams(-1, -2);
        this.header.setOrientation(1);
        this.header.setLayoutParams(paramView);
        this.header.addView(RecommendListAgent.this.createContentHeaderCell());
        this.header.addView(RecommendListAgent.this.createContentHeaderDivider());
      }
      return this.header;
    }

    public void reset()
    {
      this.header = null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.RecommendListAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.tuan.dialog.filter.NaviFilterDialog;
import com.dianping.base.tuan.utils.DistanceUtils;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.model.Location;
import com.dianping.tuan.fragment.MallSearchFragment;
import com.dianping.util.TextUtils;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaFrameLayout;

public class MallListActivity extends BaseTuanActivity
  implements FilterBar.OnItemClickListener, FilterDialog.OnFilterListener, AbstractSearchFragment.OnSearchFragmentListener
{
  public static final String MAPI_MALL_LIST = "searchmallgn.bin";
  protected CustomImageButton clearBtn;
  protected FilterBar filterBar;
  protected FilterDialog filterBarDialog;
  protected boolean intentParsed = false;
  protected boolean isFirstPage = true;
  protected AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      if (paramAdapterView != MallListActivity.this.listView);
      do
      {
        return;
        paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
      }
      while (!DPObjectUtils.isDPObjectof(paramAdapterView, "ViewItem"));
      paramAdapterView = ((DPObject)paramAdapterView).getString("Link");
      try
      {
        paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse(paramAdapterView));
        MallListActivity.this.startActivity(paramAdapterView);
        return;
      }
      catch (Exception paramAdapterView)
      {
        paramAdapterView.printStackTrace();
      }
    }
  };
  protected String keyword;
  protected CustomImageButton leftBtn;
  protected ListAdapter listAdapter = new ListAdapter(getBaseContext());
  protected PullToRefreshListView listView;
  protected DPObject[] naviFilterData;
  protected PullToRefreshListView.OnRefreshListener refreshListener = new PullToRefreshListView.OnRefreshListener()
  {
    public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
    {
      MallListActivity.this.listAdapter.pullToReset(true);
    }
  };
  protected String regionEnName;
  protected int regionId;
  protected CustomImageButton rightBtn;
  protected DPObject[] screeningData;
  protected EditText searchEdit;
  protected LinearLayout searchPannel;
  protected int sortId;
  protected View titleBar;
  protected TextView titleBarText;
  protected View.OnClickListener titleBtnOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (paramView == MallListActivity.this.leftBtn)
        MallListActivity.this.onBackPressed();
      do
      {
        return;
        if (paramView == MallListActivity.this.rightBtn)
        {
          MallListActivity.this.callUpMallSearchFragment("");
          return;
        }
        if (paramView != MallListActivity.this.clearBtn)
          continue;
        MallListActivity.this.callUpMallSearchFragment("");
        return;
      }
      while (paramView != MallListActivity.this.searchEdit);
      MallListActivity.this.callUpMallSearchFragment(MallListActivity.this.keyword);
    }
  };

  public void addGAInfo(NovaFrameLayout paramNovaFrameLayout, DPObject paramDPObject, int paramInt)
  {
    GAUserInfo localGAUserInfo = paramNovaFrameLayout.getGAUserInfo();
    localGAUserInfo.query_id = paramDPObject.getString("QueryId");
    localGAUserInfo.index = Integer.valueOf(paramInt);
    paramNovaFrameLayout.setGAString("clickmarket");
  }

  public void attachData(ViewHolder paramViewHolder, DPObject paramDPObject)
  {
    paramDPObject = paramDPObject.getObject("Shop");
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "Shop"));
    label257: label267: 
    while (true)
    {
      return;
      if (this.listAdapter.isImageOn())
      {
        paramViewHolder.imageView.setImage(paramDPObject.getString("DefaultPic"));
        paramViewHolder.imageFrame.setVisibility(0);
        paramViewHolder.titleView.setText(paramDPObject.getString("Name"));
        paramViewHolder.subtitleView.setText(paramDPObject.getString("RegionName"));
        Object localObject = paramDPObject.getString("MatchText");
        paramViewHolder.descriptionView.setVisibility(8);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          localObject = TextUtils.jsonParseText((String)localObject);
          if (localObject != null)
          {
            paramViewHolder.descriptionView.setText((CharSequence)localObject);
            paramViewHolder.descriptionView.setVisibility(0);
          }
        }
        localObject = paramDPObject.getString("Desc");
        paramViewHolder.discountView.setVisibility(8);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          localObject = TextUtils.jsonParseText((String)localObject);
          if (localObject != null)
          {
            paramViewHolder.discountView.setText((CharSequence)localObject);
            paramViewHolder.discountView.setVisibility(0);
          }
        }
        if ((paramViewHolder.descriptionView.getVisibility() != 0) || (paramViewHolder.discountView.getVisibility() != 0))
          break label257;
        paramViewHolder.divider.setVisibility(0);
      }
      while (true)
      {
        if (location() == null)
          break label267;
        paramDPObject = DistanceUtils.getDistance(paramDPObject.getDouble("Latitude"), paramDPObject.getDouble("Longitude"), location().latitude(), location().longitude());
        paramViewHolder.distanceView.setText(paramDPObject);
        return;
        paramViewHolder.imageFrame.setVisibility(8);
        break;
        paramViewHolder.divider.setVisibility(8);
      }
    }
  }

  protected void callUpMallSearchFragment(String paramString)
  {
    MallSearchFragment localMallSearchFragment = MallSearchFragment.createAndAdd(this, null, null, true, getPageName());
    localMallSearchFragment.setKeyword(paramString);
    localMallSearchFragment.setOnSearchFragmentListener(this);
  }

  protected DPObject findSelectedNavi(DPObject paramDPObject)
  {
    DPObject localDPObject;
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "Navi"))
      localDPObject = null;
    int k;
    DPObject[] arrayOfDPObject;
    do
    {
      return localDPObject;
      k = 0;
      arrayOfDPObject = paramDPObject.getArray("Subs");
      localDPObject = paramDPObject;
    }
    while (arrayOfDPObject == null);
    int m = arrayOfDPObject.length;
    int i = 0;
    while (true)
    {
      int j = k;
      localDPObject = paramDPObject;
      if (i < m)
      {
        localDPObject = arrayOfDPObject[i];
        if (localDPObject.getBoolean("Selected"))
          j = 1;
      }
      else
      {
        paramDPObject = localDPObject;
        if (j != 0)
          break;
        return localDPObject;
      }
      i += 1;
    }
  }

  protected Mode getModel()
  {
    if (TextUtils.isEmpty(this.keyword))
      return Mode.LIST;
    return Mode.SEARCH;
  }

  public String getPageName()
  {
    return "marketlist";
  }

  public ViewHolder getViewHolder(View paramView)
  {
    if (paramView == null)
      return null;
    ViewHolder localViewHolder = new ViewHolder();
    localViewHolder.imageFrame = ((FrameLayout)paramView.findViewById(R.id.mall_list_item_icon_frame));
    localViewHolder.imageView = ((DPNetworkImageView)paramView.findViewById(R.id.mall_list_item_icon));
    localViewHolder.imageStatusView = ((ImageView)paramView.findViewById(R.id.mall_list_item_status));
    localViewHolder.statusNoPic = ((ImageView)paramView.findViewById(R.id.mall_list_item_status_nopic));
    localViewHolder.infoRl = ((RelativeLayout)paramView.findViewById(R.id.mall_list_item_info));
    localViewHolder.titleView = ((TextView)paramView.findViewById(R.id.mall_list_item_title));
    localViewHolder.subtitleView = ((TextView)paramView.findViewById(R.id.mall_list_item_subtitle));
    localViewHolder.distanceView = ((TextView)paramView.findViewById(R.id.mall_list_item_distance));
    localViewHolder.descriptionView = ((TextView)paramView.findViewById(R.id.mall_list_item_description));
    localViewHolder.divider = paramView.findViewById(R.id.mall_list_item_divider);
    localViewHolder.discountView = ((TextView)paramView.findViewById(R.id.mall_list_item_discount));
    return localViewHolder;
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if (!DPObjectUtils.isDPObjectof(paramObject, "Navi"))
      return;
    paramObject = (DPObject)paramObject;
    this.filterBarDialog = new NaviFilterDialog(this, paramObject, this);
    this.filterBarDialog.setTag(findSelectedNavi(paramObject));
    this.filterBarDialog.show(paramView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.mall_list_activity);
    hideTitleBar();
    if (this.listView == null)
      setupView();
    parseIntent();
    updateView();
  }

  public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
  {
    if (!DPObjectUtils.isDPObjectof(paramObject, "Navi"))
    {
      paramFilterDialog.dismiss();
      return;
    }
    DPObject localDPObject = (DPObject)paramObject;
    paramObject = null;
    if (DPObjectUtils.isDPObjectof(paramFilterDialog.getTag(), "Navi"))
      paramObject = (DPObject)(DPObject)paramFilterDialog.getTag();
    int i;
    if (paramObject != localDPObject)
    {
      i = localDPObject.getInt("Type");
      if (i != 4)
        break label94;
      this.sortId = localDPObject.getInt("ID");
    }
    while (true)
    {
      paramFilterDialog.setTag(localDPObject);
      this.listAdapter.reset();
      paramFilterDialog.dismiss();
      return;
      label94: if (i != 2)
        continue;
      this.regionEnName = localDPObject.getString("EnName");
      this.regionId = localDPObject.getInt("ID");
    }
  }

  public void onSearchFragmentDetach()
  {
  }

  protected void parseIntent()
  {
    if (this.intentParsed)
      return;
    Uri localUri = getIntent().getData();
    if (localUri == null)
      finish();
    String str2 = localUri.getQueryParameter("region");
    String str1 = str2;
    if (TextUtils.isEmpty(str2))
      str1 = localUri.getQueryParameter("regionid");
    if (!TextUtils.isEmpty(str1));
    try
    {
      this.regionId = Integer.parseInt(str1);
      this.regionEnName = localUri.getQueryParameter("regionenname");
      str1 = localUri.getQueryParameter("sort");
      if (TextUtils.isEmpty(str1));
    }
    catch (Exception localException2)
    {
      try
      {
        this.sortId = Integer.parseInt(str1);
        this.keyword = localUri.getQueryParameter("keyword");
        this.intentParsed = true;
        return;
        localException1 = localException1;
        localException1.printStackTrace();
      }
      catch (Exception localException2)
      {
        while (true)
          localException2.printStackTrace();
      }
    }
  }

  protected void setupView()
  {
    this.titleBar = findViewById(R.id.title_bar);
    this.filterBar = ((FilterBar)findViewById(R.id.filter_bar));
    this.listView = ((PullToRefreshListView)findViewById(R.id.list_view));
    this.leftBtn = ((CustomImageButton)findViewById(R.id.left_btn));
    this.titleBarText = ((TextView)findViewById(R.id.title_bar_text));
    this.rightBtn = ((CustomImageButton)findViewById(R.id.right_btn));
    this.rightBtn.setGAString("marketsearch");
    this.searchPannel = ((LinearLayout)findViewById(R.id.search_pannel));
    this.searchEdit = ((EditText)findViewById(R.id.search_edit));
    this.clearBtn = ((CustomImageButton)findViewById(R.id.clear_btn));
    this.leftBtn.setOnClickListener(this.titleBtnOnClickListener);
    this.rightBtn.setOnClickListener(this.titleBtnOnClickListener);
    this.clearBtn.setOnClickListener(this.titleBtnOnClickListener);
    this.searchEdit.setOnClickListener(this.titleBtnOnClickListener);
    this.filterBar.setOnItemClickListener(this);
    this.filterBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.mall_list_filter_bar_bg));
    this.listView.setOnRefreshListener(this.refreshListener);
    this.listView.setAdapter(this.listAdapter);
    this.listView.setOnItemClickListener(this.itemClickListener);
  }

  public void startSearch(DPObject paramDPObject)
  {
    Object localObject2 = paramDPObject.getString("Url");
    Object localObject1 = paramDPObject.getString("Keyword");
    if (!TextUtils.isEmpty((CharSequence)localObject2))
    {
      startActivity((String)localObject2);
      return;
    }
    if (getModel() == Mode.SEARCH)
    {
      this.keyword = ((String)localObject1);
      this.regionId = 0;
      this.regionEnName = null;
      this.sortId = 0;
      this.searchEdit.setText(this.keyword);
      this.listAdapter.reset();
      return;
    }
    localObject2 = Uri.parse("dianping://malllist").buildUpon();
    if (!TextUtils.isEmpty((CharSequence)localObject1))
      ((Uri.Builder)localObject2).appendQueryParameter("keyword", (String)localObject1);
    localObject1 = new Intent("android.intent.action.VIEW", Uri.parse(((Uri.Builder)localObject2).toString()));
    paramDPObject = paramDPObject.getString("Value");
    if (!TextUtils.isEmpty(paramDPObject))
      ((Intent)localObject1).putExtra("value", paramDPObject);
    startActivity((Intent)localObject1);
  }

  protected void updateFilterBar()
  {
    if (this.naviFilterData == null);
    while (true)
    {
      return;
      this.filterBar.removeAllViews();
      DPObject[] arrayOfDPObject = this.naviFilterData;
      int j = arrayOfDPObject.length;
      int i = 0;
      while (i < j)
      {
        DPObject localDPObject1 = arrayOfDPObject[i];
        DPObject localDPObject2 = findSelectedNavi(localDPObject1);
        if (DPObjectUtils.isDPObjectof(localDPObject2, "Navi"))
          this.filterBar.addItem(localDPObject1, localDPObject2.getString("Name"));
        i += 1;
      }
    }
  }

  protected void updateTitleBar()
  {
    if (getModel() == Mode.SEARCH)
    {
      this.searchPannel.setVisibility(0);
      this.searchEdit.setText(this.keyword);
      this.searchEdit.setSelection(this.keyword.length());
      this.titleBarText.setVisibility(8);
      this.rightBtn.setVisibility(8);
      return;
    }
    this.searchPannel.setVisibility(8);
    this.titleBarText.setVisibility(0);
    this.rightBtn.setVisibility(0);
  }

  protected void updateView()
  {
    updateTitleBar();
    updateFilterBar();
  }

  class ListAdapter extends BasicLoadAdapter
  {
    private boolean shouldShowImage = DPActivity.preferences().getBoolean("isShowListImage", true);

    public ListAdapter(Context arg2)
    {
      super();
    }

    public MApiRequest createRequest(int paramInt)
    {
      Object localObject = MallListActivity.this;
      boolean bool;
      StringBuilder localStringBuilder1;
      StringBuilder localStringBuilder2;
      if (paramInt == 0)
      {
        bool = true;
        ((MallListActivity)localObject).isFirstPage = bool;
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append("http://app.t.dianping.com/");
        localStringBuilder1.append("searchmallgn.bin");
        localStringBuilder1.append("?cityid=").append(MallListActivity.this.cityId());
        if ((MallListActivity.this.accountService() != null) && (MallListActivity.this.accountService().token() != null))
          localStringBuilder1.append("&token=").append(MallListActivity.this.accountService().token());
        if (MallListActivity.this.location() != null)
        {
          localStringBuilder1.append("&lat=").append(MallListActivity.this.location().latitude());
          localStringBuilder1.append("&lng=").append(MallListActivity.this.location().longitude());
        }
        localStringBuilder2 = localStringBuilder1.append("&network=");
        if (!NetworkUtils.isWIFIConnection(MallListActivity.this.getBaseContext()))
          break label312;
      }
      label312: for (localObject = "wifi"; ; localObject = "mobile")
      {
        localStringBuilder2.append((String)localObject);
        localStringBuilder1.append("&start=").append(paramInt);
        if (!TextUtils.isEmpty(MallListActivity.this.keyword))
          localStringBuilder1.append("&keyword=").append(MallListActivity.this.keyword);
        if (!TextUtils.isEmpty(MallListActivity.this.regionEnName))
          localStringBuilder1.append("&regionenname=").append(MallListActivity.this.regionEnName);
        localStringBuilder1.append("&regionid=").append(MallListActivity.this.regionId);
        localStringBuilder1.append("&filter=").append(MallListActivity.this.sortId);
        return MallListActivity.this.mapiGet(this, localStringBuilder1.toString(), CacheType.DISABLED);
        bool = false;
        break;
      }
    }

    protected boolean isImageOn()
    {
      return (this.shouldShowImage) || (NetworkUtils.isWIFIConnection(DPApplication.instance()));
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramDPObject = paramView;
      if (paramView == null)
      {
        paramDPObject = View.inflate(MallListActivity.this.getBaseContext(), R.layout.mall_list_item_view, null);
        paramDPObject.setTag(MallListActivity.this.getViewHolder(paramDPObject));
      }
      paramView = getItem(paramInt);
      if (!DPObjectUtils.isDPObjectof(paramView, "ViewItem"))
        return null;
      paramViewGroup = (MallListActivity.ViewHolder)paramDPObject.getTag();
      MallListActivity.this.attachData(paramViewGroup, (DPObject)paramView);
      MallListActivity.this.addGAInfo((NovaFrameLayout)paramDPObject, (DPObject)paramView, paramInt);
      return paramDPObject;
    }

    protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      MallListActivity.this.listView.onRefreshComplete();
      super.onRequestComplete(paramBoolean, paramMApiRequest, paramMApiResponse);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        DPObject localDPObject = (DPObject)paramMApiResponse.result();
        if (MallListActivity.this.isFirstPage)
        {
          MallListActivity.this.naviFilterData = localDPObject.getArray("NaviBars");
          MallListActivity.this.screeningData = localDPObject.getArray("Screenings");
          MallListActivity.this.updateView();
        }
      }
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
    }
  }

  public static enum Mode
  {
    static
    {
      $VALUES = new Mode[] { LIST, SEARCH };
    }
  }

  class ViewHolder
  {
    public TextView descriptionView;
    public TextView discountView;
    public TextView distanceView;
    public View divider;
    public FrameLayout imageFrame;
    public ImageView imageStatusView;
    public DPNetworkImageView imageView;
    public RelativeLayout infoRl;
    public ImageView statusNoPic;
    public TextView subtitleView;
    public TextView titleView;

    ViewHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.MallListActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.Location;
import com.dianping.tuan.widget.TuanRankListItem;
import com.dianping.tuan.widget.TuanRankListItem.TuanRankListItemOnClickListener;
import com.dianping.tuan.widget.TuanTagNaviGridBar;
import com.dianping.tuan.widget.TuanTagNaviGridBar.OnCategorySelectChangeListener;
import com.dianping.util.DeviceUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TuanRankListActivity extends BaseTuanActivity
  implements TuanTagNaviGridBar.OnCategorySelectChangeListener
{
  protected static final int DEFAULT_RANK_CATEGORY = 1;
  protected static final String RANK_LIST_BIN = "findgoodshopgn.bin";
  protected RankDealListAdapter mAdapter;
  protected TuanTagNaviGridBar mCategoryHeaderBar;
  protected LinearLayout mHeaderLayout;
  protected boolean mIsCategoryLoad = false;
  protected boolean mIsFirstPage = true;
  protected PullToRefreshListView mListView;
  protected boolean mLoading = false;
  protected PullToRefreshListView.OnRefreshListener mRefreshListener = new PullToRefreshListView.OnRefreshListener()
  {
    public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
    {
      TuanRankListActivity.this.mAdapter.pullToReset(true);
    }
  };
  protected int mSelectedCategoryID = 1;
  protected String mSelectedEnName = "";

  private void getURIData()
  {
    Object localObject = getIntent().getData();
    if (localObject == null)
      return;
    String str = ((Uri)localObject).getQueryParameter("categoryid");
    localObject = ((Uri)localObject).getQueryParameter("categoryenname");
    try
    {
      if (!TextUtils.isEmpty(str))
        this.mSelectedCategoryID = Integer.valueOf(str).intValue();
      this.mSelectedEnName = ((String)localObject);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  private void setupView()
  {
    setContentView(R.layout.tuan_rank_list_layout);
    this.mListView = ((PullToRefreshListView)findViewById(R.id.tuan_rank_list_recommend_table));
    this.mCategoryHeaderBar = new TuanTagNaviGridBar(getApplicationContext());
    this.mCategoryHeaderBar.setOnCategorySelectChangeListener(this);
    this.mCategoryHeaderBar.setColumnNum(4);
    this.mHeaderLayout = new LinearLayout(this);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
    localLayoutParams.topMargin = ViewUtils.dip2px(this, 14.0F);
    localLayoutParams.bottomMargin = ViewUtils.dip2px(this, 9.0F);
    localLayoutParams.rightMargin = ViewUtils.dip2px(this, 5.0F);
    localLayoutParams.leftMargin = ViewUtils.dip2px(this, 5.0F);
    this.mHeaderLayout.setBackgroundColor(getResources().getColor(R.color.white));
    this.mHeaderLayout.addView(this.mCategoryHeaderBar, 0, localLayoutParams);
    this.mHeaderLayout.setVisibility(8);
    this.mListView.addHeaderView(this.mHeaderLayout);
    this.mAdapter = new RankDealListAdapter(getApplicationContext());
    this.mListView.setOnRefreshListener(this.mRefreshListener);
    this.mListView.setAdapter(this.mAdapter);
    this.mListView.setBackgroundColor(getResources().getColor(R.color.common_bk_color));
    getURIData();
  }

  public void onCategorySelectChangeListener(int paramInt1, int paramInt2, Map<String, Object> paramMap)
  {
    if ((this.mAdapter != null) && (paramMap != null));
    try
    {
      paramInt2 = ((Integer)paramMap.get("ID")).intValue();
      if ((paramInt2 != this.mSelectedCategoryID) || ((!this.mIsCategoryLoad) && (!this.mLoading)))
      {
        this.mSelectedEnName = ((String)paramMap.get("EnName"));
        this.mSelectedCategoryID = paramInt2;
        paramMap = (String)paramMap.get("Name");
        GAHelper.instance().contextStatisticsEvent(this, "selectcategory", paramMap, paramInt1, "tap");
        this.mIsCategoryLoad = false;
        this.mLoading = true;
        this.mAdapter.reset();
      }
      return;
    }
    catch (Exception paramMap)
    {
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setupView();
  }

  protected void onDestroy()
  {
    if (this.mAdapter != null)
      this.mAdapter.cancelLoad();
    super.onDestroy();
  }

  protected class RankDealListAdapter extends BasicLoadAdapter
    implements TuanRankListItem.TuanRankListItemOnClickListener
  {
    LayoutInflater mInflater;

    public RankDealListAdapter(Context arg2)
    {
      super();
      this.mInflater = ((LayoutInflater)localContext.getSystemService("layout_inflater"));
    }

    public void appendData(DPObject paramDPObject)
    {
      if ((paramDPObject != null) && (DPObjectUtils.isDPObjectof(paramDPObject, "ViewList")))
      {
        if (TuanRankListActivity.this.mIsFirstPage)
        {
          DPObject[] arrayOfDPObject = paramDPObject.getArray("TagNavis");
          if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
          {
            TuanRankListActivity.this.mHeaderLayout.setVisibility(0);
            ArrayList localArrayList = new ArrayList(arrayOfDPObject.length);
            int i = 0;
            while (i < arrayOfDPObject.length)
            {
              HashMap localHashMap = new HashMap(4);
              localHashMap.put("ID", Integer.valueOf(arrayOfDPObject[i].getInt("ID")));
              localHashMap.put("Name", arrayOfDPObject[i].getString("Name"));
              localHashMap.put("EnName", arrayOfDPObject[i].getString("EnName"));
              localHashMap.put("Selected", Boolean.valueOf(arrayOfDPObject[i].getBoolean("Selected")));
              if (arrayOfDPObject[i].getBoolean("Selected"))
              {
                TuanRankListActivity.this.mSelectedCategoryID = arrayOfDPObject[i].getInt("ID");
                TuanRankListActivity.this.mSelectedEnName = arrayOfDPObject[i].getString("EnName");
              }
              localHashMap.put("Value", arrayOfDPObject[i]);
              localArrayList.add(localHashMap);
              i += 1;
            }
            TuanRankListActivity.this.mCategoryHeaderBar.setNaviDatas(localArrayList);
            TuanRankListActivity.this.mIsCategoryLoad = true;
          }
        }
        super.appendData(paramDPObject);
      }
    }

    public MApiRequest createRequest(int paramInt)
    {
      if (this.mReq != null)
        return null;
      Object localObject = TuanRankListActivity.this;
      if (paramInt == 0);
      for (boolean bool = true; ; bool = false)
      {
        ((TuanRankListActivity)localObject).mIsFirstPage = bool;
        localObject = UrlBuilder.createBuilder("http://app.t.dianping.com/");
        ((UrlBuilder)localObject).appendPath("findgoodshopgn.bin");
        ((UrlBuilder)localObject).addParam("cityid", Integer.valueOf(TuanRankListActivity.this.cityId()));
        if (!TextUtils.isEmpty(TuanRankListActivity.this.accountService().token()))
          ((UrlBuilder)localObject).addParam("token", TuanRankListActivity.this.accountService().token());
        ((UrlBuilder)localObject).addParam("dpid", DeviceUtils.dpid());
        if (TuanRankListActivity.this.location() != null)
          ((UrlBuilder)localObject).addParam("lat", Double.valueOf(TuanRankListActivity.this.location().latitude())).addParam("lng", Double.valueOf(TuanRankListActivity.this.location().longitude()));
        ((UrlBuilder)localObject).addParam("start", Integer.valueOf(paramInt));
        ((UrlBuilder)localObject).addParam("limit", Integer.valueOf(20));
        ((UrlBuilder)localObject).addParam("categoryid", Integer.valueOf(TuanRankListActivity.this.mSelectedCategoryID));
        ((UrlBuilder)localObject).addParam("categoryenname", TuanRankListActivity.this.mSelectedEnName);
        this.mReq = TuanRankListActivity.this.mapiGet(this, ((UrlBuilder)localObject).buildUrl(), CacheType.DISABLED);
        return this.mReq;
      }
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramDPObject = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"))
      {
        DPObject localDPObject = (DPObject)paramDPObject;
        if ((paramView instanceof TuanRankListItem))
        {
          paramDPObject = (TuanRankListItem)paramView;
          paramView = paramDPObject;
          if (paramDPObject == null)
          {
            paramView = (TuanRankListItem)this.mInflater.inflate(R.layout.tuan_rank_list_item, null);
            paramView.setOnItemClick(this);
          }
          if (TuanRankListActivity.this.location() != null)
            break label130;
          paramView.setTuanDeal(localDPObject, 0.0D, 0.0D, paramInt + 1);
          label80: paramView.setGAString("rankingmenu", null, paramInt);
          paramDPObject = paramView.findViewById(R.id.tuan_rank_divider);
          if (paramDPObject != null)
          {
            if (paramInt != 0)
              break label162;
            paramDPObject.setVisibility(0);
          }
        }
        while (true)
        {
          if (localDPObject.getInt("Type") == 9)
            break label171;
          return paramView;
          paramDPObject = null;
          break;
          label130: paramView.setTuanDeal(localDPObject, TuanRankListActivity.this.location().latitude(), TuanRankListActivity.this.location().longitude(), paramInt + 1);
          break label80;
          label162: paramDPObject.setVisibility(8);
        }
        label171: return getEmptyView(emptyMessage(), "暂时没有你要找的哦，看看别的吧", paramViewGroup, null);
      }
      return new View(TuanRankListActivity.this);
    }

    public void onItemClick(DPObject paramDPObject, String paramString)
    {
      if (paramDPObject == null)
        return;
      if (!TextUtils.isEmpty(paramString))
      {
        paramDPObject = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
        TuanRankListActivity.this.startActivity(paramDPObject);
        return;
      }
      paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
      paramString.putExtra("deal", paramDPObject);
      TuanRankListActivity.this.startActivity(paramString);
    }

    protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestComplete(paramBoolean, paramMApiRequest, paramMApiResponse);
      TuanRankListActivity.this.mListView.onRefreshComplete();
      TuanRankListActivity.this.mLoading = false;
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFailed(paramMApiRequest, paramMApiResponse);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanRankListActivity
 * JD-Core Version:    0.6.0
 */
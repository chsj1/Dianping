package com.dianping.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.adapter.SubFilterAdapter;
import com.dianping.base.app.NovaLoadActivity;
import com.dianping.base.basic.TabPagerFragment;
import com.dianping.base.shoplist.data.DefaultShopListDataSource;
import com.dianping.base.shoplist.fragment.ShopListFragment;
import com.dianping.base.shoplist.fragment.ShopListFragment.OnShopItemClickListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NRankActivity extends NovaLoadActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener, ShopListFragment.OnShopItemClickListener
{
  private static final String SHOPLIST_FRAGMENT_TAG = "shoplist_fragment";
  private static final String TAG = NRankActivity.class.getSimpleName();
  protected Animation animPushTopIn;
  protected Animation animPushTopOut;
  private DPObject curMainPair;
  private DPObject[] curPairs;
  private DPObject[] curPrimaryPairs;
  private String errorMsg;
  private ListView filterListView;
  private LinearLayout filterPanel;
  private List<DefaultShopListDataSource> listDataSource = new ArrayList();
  private SubFilterAdapter mFilterAdapter;
  private MApiRequest mRankIndexRequest;
  private DPObject[] mainPairs;
  private DPObject[] pairs;
  private SparseArray<MApiRequest> requestMap = new SparseArray();
  private HashMap<DPObject, DPObject[]> subPairs;
  private TabPagerFragment tabPagerFragment;
  private ImageView titleArrow;
  private View titleMain;

  private void dismissFilter(boolean paramBoolean)
  {
    this.titleArrow.setImageResource(R.drawable.navibar_arrow_down);
    if (paramBoolean)
      this.filterPanel.startAnimation(this.animPushTopOut);
    this.filterPanel.setVisibility(8);
  }

  private DPObject[] getPrimaryPairs(DPObject[] paramArrayOfDPObject)
  {
    if (paramArrayOfDPObject == null)
      return null;
    ArrayList localArrayList = new ArrayList();
    int j = paramArrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      if (!TextUtils.isEmpty(localDPObject.getString("ID")))
      {
        localArrayList.add(localDPObject);
        if (localArrayList.size() == 3)
        {
          if (paramArrayOfDPObject.length > 3)
            localArrayList.add(new DPObject("Pair").edit().putString("ID", "-1").putString("Name", "更多排行").generate());
          return (DPObject[])localArrayList.toArray(new DPObject[0]);
        }
      }
      i += 1;
    }
    return (DPObject[])localArrayList.toArray(new DPObject[0]);
  }

  private DPObject[] make(DPObject[] paramArrayOfDPObject, Map<DPObject, DPObject[]> paramMap)
  {
    Object localObject = null;
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    int j = paramArrayOfDPObject.length;
    int i = 0;
    if (i < j)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      if (localDPObject.getInt("Type") == 0)
      {
        if (localObject != null)
        {
          localArrayList1.add(localObject);
          if (!localArrayList2.isEmpty())
            paramMap.put(localObject, localArrayList2.toArray(new DPObject[0]));
        }
        localObject = localDPObject;
        localArrayList2.clear();
      }
      while (true)
      {
        i += 1;
        break;
        localArrayList2.add(localDPObject);
      }
    }
    if (localObject != null)
    {
      localArrayList1.add(localObject);
      if (!localArrayList2.isEmpty())
        paramMap.put(localObject, localArrayList2.toArray(new DPObject[0]));
    }
    return (DPObject[])localArrayList1.toArray(new DPObject[0]);
  }

  private int parsePairIntId(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return 0;
    try
    {
      int i = Integer.parseInt(paramDPObject.getString("ID"));
      return i;
    }
    catch (java.lang.Exception paramDPObject)
    {
    }
    return 0;
  }

  private void requestRankIndex()
  {
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
    localStringBuilder.append("rankfullindex.bin?");
    localStringBuilder.append("cityid=").append(cityId());
    this.mRankIndexRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.mRankIndexRequest, this);
    showLoading(null);
  }

  private void showFilter()
  {
    this.titleArrow.setImageResource(R.drawable.navibar_arrow_up);
    this.mFilterAdapter.notifyDataSetChanged();
    this.filterPanel.setVisibility(0);
    this.filterPanel.startAnimation(this.animPushTopIn);
  }

  private void updateCurPairs(DPObject paramDPObject, int paramInt)
  {
    this.curMainPair = paramDPObject;
    this.curPairs = ((DPObject[])this.subPairs.get(this.curMainPair));
    if (this.curPairs == null)
      this.curPairs = this.pairs;
    this.curPrimaryPairs = getPrimaryPairs(this.curPairs);
    setTitle(this.curMainPair.getString("Name"));
    if (parsePairIntId(paramDPObject) <= -999)
    {
      this.titleArrow.setVisibility(8);
      this.titleMain.setOnClickListener(null);
    }
    while (true)
    {
      updateFragment();
      showContent();
      return;
      this.mFilterAdapter.setDataSet(Arrays.asList(this.mainPairs), this.curMainPair);
      this.mFilterAdapter.setSelectItem(paramInt);
      this.titleArrow.setVisibility(0);
      this.titleMain.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (NRankActivity.this.mainPairs == null)
            return;
          if (NRankActivity.this.filterPanel.getVisibility() == 0)
          {
            NRankActivity.this.dismissFilter(true);
            return;
          }
          NRankActivity.this.showFilter();
        }
      });
    }
  }

  private void updateFragment()
  {
    int j = R.id.pager;
    int i = 0;
    while (i <= 4)
    {
      String str = "android:switcher:" + j + ":" + i;
      if (getSupportFragmentManager().findFragmentByTag(str) != null)
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag(str)).commit();
      i += 1;
    }
    this.tabPagerFragment = new RankPagerFragment();
    getSupportFragmentManager().beginTransaction().replace(R.id.viewer, this.tabPagerFragment).commitAllowingStateLoss();
    this.listDataSource.clear();
    this.requestMap.clear();
    this.titleMain.post(new Runnable()
    {
      public void run()
      {
        NRankActivity.this.setTitle(NRankActivity.this.curMainPair.getString("Name"));
        if (NRankActivity.this.curPrimaryPairs == null);
        do
        {
          return;
          int i = 0;
          while ((i < NRankActivity.this.curPrimaryPairs.length) && (i < 3))
          {
            localObject = new RankShopListFragment();
            DefaultShopListDataSource localDefaultShopListDataSource = new DefaultShopListDataSource(new NRankActivity.3.1(this, i));
            localDefaultShopListDataSource.setIsRank(true);
            localDefaultShopListDataSource.setShowDistance(true);
            Location localLocation = NRankActivity.this.location();
            if (localLocation != null)
              localDefaultShopListDataSource.setOffsetGPS(localLocation.offsetLatitude(), localLocation.offsetLongitude());
            NRankActivity.this.listDataSource.add(localDefaultShopListDataSource);
            ((ShopListFragment)localObject).setShopListDataSource(localDefaultShopListDataSource);
            NRankActivity.this.tabPagerFragment.addTab(NRankActivity.this.curPrimaryPairs[i].getString("Name"), R.layout.rank_tab_indicator, (Fragment)localObject, null);
            i += 1;
          }
        }
        while (NRankActivity.this.curPrimaryPairs.length <= 3);
        Object localObject = new RankMoreFragment();
        new Bundle().putParcelableArray("curPairs", NRankActivity.this.curPairs);
        NRankActivity.this.tabPagerFragment.addTab("更多排行", R.layout.rank_tab_indicator, (Fragment)localObject, null);
      }
    });
  }

  protected View getLoadingView()
  {
    return getLayoutInflater().inflate(R.layout.loading_item_fullscreen, null);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 3);
  }

  public void onBackPressed()
  {
    if (this.filterPanel.getVisibility() == 0)
    {
      dismissFilter(true);
      return;
    }
    super.onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.titleMain = findViewById(R.id.title_main);
    this.titleArrow = ((ImageView)findViewById(R.id.title_arrow));
    this.titleArrow.setVisibility(8);
    this.animPushTopIn = AnimationUtils.loadAnimation(this, R.anim.push_top_in);
    this.animPushTopOut = AnimationUtils.loadAnimation(this, R.anim.push_top_out);
    this.filterPanel = ((LinearLayout)findViewById(R.id.filter_panel));
    this.filterListView = ((ListView)findViewById(R.id.filter_list));
    this.filterPanel.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        NRankActivity.this.dismissFilter(true);
      }
    });
    this.mFilterAdapter = new SubFilterAdapter(null, this);
    this.mFilterAdapter.setNormalTextColor(-16777216);
    this.filterListView.setAdapter(this.mFilterAdapter);
    this.filterListView.setOnItemClickListener(this);
  }

  protected void onDestroy()
  {
    if (this.mRankIndexRequest != null)
      mapiService().abort(this.mRankIndexRequest, this, true);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramAdapterView == this.filterListView)
    {
      paramAdapterView = this.mFilterAdapter.getItem(paramInt);
      if (((paramAdapterView instanceof DPObject)) && (((DPObject)paramAdapterView).isClass("Pair")))
      {
        updateCurPairs((DPObject)paramAdapterView, paramInt);
        dismissFilter(true);
        statisticsEvent("rank5", "rank5_switch_select", ((DPObject)paramAdapterView).getString("Name"), 0);
      }
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    Object localObject;
    if (paramMApiRequest == this.mRankIndexRequest)
    {
      this.mRankIndexRequest = null;
      localObject = paramMApiResponse.message();
      if (localObject != null)
      {
        Log.i(TAG, ((SimpleMsg)localObject).toString());
        this.errorMsg = ((SimpleMsg)localObject).toString();
      }
      setError(this.errorMsg);
    }
    if ((this.curPrimaryPairs != null) && (this.curPrimaryPairs.length > 0))
    {
      int i = 0;
      while ((i < this.curPrimaryPairs.length) && (i < 3))
      {
        if (paramMApiRequest == this.requestMap.get(i))
        {
          this.requestMap.put(i, null);
          localObject = "错误";
          SimpleMsg localSimpleMsg = paramMApiResponse.message();
          if (localSimpleMsg != null)
          {
            Log.i(TAG, localSimpleMsg.toString());
            localObject = localSimpleMsg.toString();
          }
          if (this.listDataSource.get(i) != null)
            ((DefaultShopListDataSource)this.listDataSource.get(i)).setError((String)localObject);
        }
        i += 1;
      }
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    int i;
    if (paramMApiRequest == this.mRankIndexRequest)
    {
      this.mRankIndexRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject[]))
      {
        setPairs((DPObject[])(DPObject[])paramMApiResponse.result());
        this.errorMsg = null;
      }
    }
    else if ((this.curPrimaryPairs != null) && (this.curPrimaryPairs.length > 0))
    {
      i = 0;
    }
    while (true)
    {
      if ((i < this.curPrimaryPairs.length) && (i < 3))
      {
        if (paramMApiRequest != this.requestMap.get(i))
          break label192;
        this.requestMap.put(i, null);
        if (((paramMApiResponse.result() instanceof DPObject)) && (this.listDataSource.get(i) != null))
          ((DefaultShopListDataSource)this.listDataSource.get(i)).appendShops((DPObject)paramMApiResponse.result());
      }
      else
      {
        return;
        setError(this.errorMsg);
        break;
      }
      if (this.listDataSource.get(i) != null)
        ((DefaultShopListDataSource)this.listDataSource.get(i)).setError("错误");
      label192: i += 1;
    }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    if (isRetrieved())
    {
      ArrayList localArrayList = paramBundle.getParcelableArrayList("pairs");
      if (localArrayList != null)
        setPairs((DPObject[])localArrayList.toArray(new DPObject[0]));
      paramBundle = paramBundle.getString("error");
      if (paramBundle != null)
        setError(paramBundle);
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    if (isRetrieved())
    {
      paramBundle.putBoolean("retrieved", true);
      if (this.pairs != null)
        paramBundle.putParcelableArrayList("pairs", new ArrayList(Arrays.asList(this.pairs)));
      if (this.errorMsg != null)
        paramBundle.putString("error", this.errorMsg);
    }
    super.onSaveInstanceState(paramBundle);
  }

  public void onShopItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong, DPObject paramDPObject)
  {
    statisticsEvent("rank5", "rank5_item", "" + paramDPObject.getInt("ID"), 0);
    paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramDPObject.getInt("ID")));
    paramAdapterView.putExtra("shopId", paramDPObject.getInt("ID"));
    paramAdapterView.putExtra("shop", paramDPObject);
    startActivity(paramAdapterView);
  }

  protected void reloadContent()
  {
    requestRankIndex();
  }

  public void setError(String paramString)
  {
    this.pairs = null;
    this.mainPairs = null;
    this.subPairs = null;
    this.errorMsg = paramString;
    showError(this.errorMsg);
  }

  public void setPairs(DPObject[] paramArrayOfDPObject)
  {
    this.pairs = paramArrayOfDPObject;
    this.subPairs = new HashMap();
    this.mainPairs = make(this.pairs, this.subPairs);
    this.errorMsg = null;
    if (this.mainPairs.length > 0);
    for (paramArrayOfDPObject = this.mainPairs[0]; ; paramArrayOfDPObject = new DPObject("Pair").edit().putString("ID", "-999").putString("Name", "排行榜").generate())
    {
      updateCurPairs(paramArrayOfDPObject, 0);
      return;
    }
  }

  protected void setupView()
  {
    super.setContentView(R.layout.rank);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.NRankActivity
 * JD-Core Version:    0.6.0
 */
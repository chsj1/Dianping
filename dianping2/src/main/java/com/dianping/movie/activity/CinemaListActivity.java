package com.dianping.movie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPApplication;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.base.widget.dialogfilter.CinemaListFilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.movie.view.CinemaFilterBar;
import com.dianping.movie.view.CinemaListItem;
import com.dianping.movie.view.CinemaListItemWithSelectedMovie;
import com.dianping.movie.view.MovieBannerView;
import com.dianping.movie.view.MovieBannerView.OnBannerClickGA;
import com.dianping.movie.view.MovieDiscountItemView;
import com.dianping.movie.view.MovieInfoView;
import com.dianping.util.CollectionUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.GAHelper;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CinemaListActivity extends MovieBaseActivity
  implements RequestHandler<MApiRequest, MApiResponse>, FilterBar.OnItemClickListener, LocationListener, AdapterView.OnItemClickListener, PullToRefreshListView.OnRefreshListener, AbsListView.OnScrollListener
{
  public static final String HIDE_HEADER_YES = "1";
  private DPObject DEFAULT_FILTER = new DPObject("Pair").edit().putString("ID", "0").putString("Name", "筛选").putInt("Type", 0).generate();
  private final int DEFAULT_MAXNUMBER_MOVIE_DISCOUNT_SCHEDULE = 2;
  private DPObject DEFAULT_REGION = new DPObject("Region").edit().putInt("ID", 0).putString("Name", "全部地区").putInt("ParentID", 0).putString("EnName", "0").putString("ParentEnName", "0").putInt("Count", -1).generate();
  private DPObject DEFAULT_SORT = new DPObject("Pair").edit().putString("ID", "1").putString("Name", "默认排序").putInt("Type", 2).generate();
  private MApiRequest bannerRequest;
  private MovieBannerView bannerView;
  private CinemaAdapter cinemaAdapter;
  private ArrayList<DPObject> cinemaList = new ArrayList();
  private CinemaFilterBar cinemaListFilterBarFix;
  private CinemaFilterBar cinemaListFilterBarFloat;
  private ListView cinemaListView;
  private Map<Integer, DPObject> cinemaMovieShowExpressMap = new HashMap();
  private MApiRequest cinemaRequest;
  private Context context;
  private DPObject currentFilter;
  private DPObject currentRegion;
  private DPObject currentSort;
  private String discountId;
  private FilterDialog dlg;
  private DPObject[] dpFilterNavs;
  private DPObject dpMovie = null;
  private DPObject[] dpRegionNavs;
  private DPObject[] dpSortNavs;
  private String emptyMsg;
  private String errorMsg;
  private String filterId;
  final FilterDialog.OnFilterListener filterListener = new FilterDialog.OnFilterListener()
  {
    public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
    {
      if ("region".equals(paramFilterDialog.getTag()))
        if (CinemaListActivity.this.dpRegionNavs != null);
      while (true)
      {
        do
          return;
        while (!CinemaListActivity.this.isDPObjectof(paramObject, "Region"));
        if (CinemaListActivity.this.currentRegion != paramObject)
        {
          CinemaListActivity.access$202(CinemaListActivity.this, (DPObject)paramObject);
          GAHelper.instance().contextStatisticsEvent(CinemaListActivity.this, "navi_region", null, 0, "tap");
        }
        if ("sort".equals(paramFilterDialog.getTag()))
        {
          if ((CinemaListActivity.this.dpSortNavs == null) || (!CinemaListActivity.this.isDPObjectof(paramObject, "Pair")))
            continue;
          if (!CinemaListActivity.this.checkSortable((DPObject)paramObject))
          {
            paramFilterDialog.dismiss();
            return;
          }
          if (CinemaListActivity.this.currentSort != paramObject)
          {
            CinemaListActivity.access$602(CinemaListActivity.this, (DPObject)paramObject);
            GAHelper.instance().contextStatisticsEvent(CinemaListActivity.this, "navi_sort", null, 0, "tap");
          }
        }
        if (!"filter".equals(paramFilterDialog.getTag()))
          break;
        if ((CinemaListActivity.this.dpFilterNavs == null) || (!CinemaListActivity.this.isDPObjectof(paramObject, "Pair")))
          continue;
        CinemaListActivity.access$902(CinemaListActivity.this, true);
        if (CinemaListActivity.this.currentFilter == paramObject)
          break;
        CinemaListActivity.access$1002(CinemaListActivity.this, (DPObject)paramObject);
        GAHelper.instance().contextStatisticsEvent(CinemaListActivity.this, "navi_filter", null, 0, "tap");
      }
      CinemaListActivity.this.updateNavs(CinemaListActivity.this.currentRegion, CinemaListActivity.this.currentSort, CinemaListActivity.this.currentFilter);
      paramFilterDialog.dismiss();
      CinemaListActivity.this.cinemaAdapter.refresh();
    }
  };
  private int flag = 0;
  private boolean hasMovieShow = false;
  private View headerView;
  private View headerView2;
  private boolean isEnd;
  private boolean isFilterSelected = false;
  private boolean isFirstPage = true;
  private boolean isHiddenHeaderview = false;
  private boolean isLocated = false;
  private boolean isPullToRefresh;
  private LinearLayout layerMovieDiscountDesc;
  private LinearLayout layerMovieInfo;
  private MApiRequest movieDetailRequest;
  private int movieId = 0;
  private MovieInfoView movieInfoView;
  private MApiRequest movieShowExpressRequest;
  private int myAccuracy = 0;
  private double myLatitude = 0.0D;
  private double myLongitude = 0.0D;
  private int nextStartIndex;
  private PullToRefreshListView ptrlv;
  private int recordCount;
  private int regionId = -1;
  private boolean showCinemaFilterBarFix = false;
  private boolean showGrade = false;
  private String sortId;
  private View tableHeader;

  private void appendCinemas(DPObject paramDPObject)
  {
    Object localObject1;
    DPObject localDPObject;
    Object localObject2;
    int i;
    if (this.isFirstPage)
    {
      DPObject[] arrayOfDPObject1 = paramDPObject.getArray("RegionNavs");
      DPObject[] arrayOfDPObject2 = paramDPObject.getArray("SortNavs");
      DPObject[] arrayOfDPObject3 = paramDPObject.getArray("FilterNavs");
      localObject1 = paramDPObject.getObject("CurrentRegion");
      localDPObject = paramDPObject.getObject("CurrentSort");
      localObject2 = paramDPObject.getObject("CurrentFilter");
      if (localObject1 == null)
      {
        i = -1;
        if (localDPObject != null)
          break label259;
        localObject1 = "";
        label75: if (localObject2 != null)
          break label270;
        localObject2 = "";
        label83: setSelectedNavs(i, (String)localObject1, (String)localObject2);
        setNavs(arrayOfDPObject1, arrayOfDPObject2, arrayOfDPObject3);
      }
    }
    else
    {
      this.isFirstPage = false;
      localObject1 = paramDPObject.getArray("List");
      if (localObject1 != null)
      {
        if (this.nextStartIndex != 0)
          break label280;
        this.cinemaList.clear();
        this.cinemaList.addAll(Arrays.asList(localObject1));
      }
    }
    while (true)
    {
      this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
      this.isEnd = paramDPObject.getBoolean("IsEnd");
      this.emptyMsg = paramDPObject.getString("EmptyMsg");
      this.recordCount = paramDPObject.getInt("RecordCount");
      if ((!this.isEnd) && (localObject1.length == 0))
        this.isEnd = true;
      if ((this.isEnd) && (this.cinemaList.size() <= 0))
      {
        this.isEnd = false;
        this.nextStartIndex = 0;
        this.errorMsg = "您请求的数据不存在";
      }
      this.cinemaAdapter.notifyDataSetChanged();
      return;
      i = ((DPObject)localObject1).getInt("ID");
      break;
      label259: localObject1 = localDPObject.getString("ID");
      break label75;
      label270: localObject2 = ((DPObject)localObject2).getString("ID");
      break label83;
      label280: this.cinemaList.addAll(Arrays.asList(localObject1));
    }
  }

  private boolean checkSortable(DPObject paramDPObject)
  {
    if ((paramDPObject != null) && ("2".equals(paramDPObject.getString("ID"))) && (location() == null))
    {
      Toast.makeText(this, "正在定位，此功能暂不可用", 0).show();
      return false;
    }
    return true;
  }

  private void getMyLocation()
  {
    Object localObject2 = DPApplication.instance().locationService().location();
    Object localObject1;
    if (localObject2 != null)
      localObject1 = null;
    try
    {
      localObject2 = (Location)((DPObject)localObject2).decodeToObject(Location.DECODER);
      localObject1 = localObject2;
      if (localObject1 != null)
      {
        this.isLocated = true;
        this.myLatitude = localObject1.offsetLatitude();
        this.myLongitude = localObject1.offsetLongitude();
        this.myAccuracy = localObject1.accuracy();
      }
      return;
    }
    catch (ArchiveException localArchiveException)
    {
      while (true)
        localArchiveException.printStackTrace();
    }
  }

  private boolean isLocated()
  {
    return this.isLocated;
  }

  private boolean loadNewPage()
  {
    if (this.isEnd);
    do
      return false;
    while (this.cinemaRequest != null);
    this.errorMsg = null;
    requestCinemaList();
    return true;
  }

  private void requestBanner()
  {
    if (this.bannerRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/newmoviebannermv.bin?").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    localBuilder.appendQueryParameter("source", "cinemalist");
    this.bannerRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.bannerRequest, this);
  }

  private void requestCinemaList()
  {
    if (this.cinemaRequest != null)
      return;
    if (this.nextStartIndex == 0)
      this.isFirstPage = true;
    Object localObject = Uri.parse("http://app.movie.dianping.com/cinemalistmv.bin?").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("start", String.valueOf(this.nextStartIndex));
    ((Uri.Builder)localObject).appendQueryParameter("limit", String.valueOf(25));
    ((Uri.Builder)localObject).appendQueryParameter("cityid", String.valueOf(cityId()));
    if (isLogined())
      ((Uri.Builder)localObject).appendQueryParameter("token", accountService().token());
    if (isLocated())
    {
      ((Uri.Builder)localObject).appendQueryParameter("lat", Location.FMT.format(this.myLatitude));
      ((Uri.Builder)localObject).appendQueryParameter("lng", Location.FMT.format(this.myLongitude));
      ((Uri.Builder)localObject).appendQueryParameter("accuracy", String.valueOf(this.myAccuracy));
    }
    ((Uri.Builder)localObject).appendQueryParameter("regionid", String.valueOf(this.currentRegion.getInt("ID")));
    ((Uri.Builder)localObject).appendQueryParameter("parentregionid", String.valueOf(this.currentRegion.getInt("ParentID")));
    ((Uri.Builder)localObject).appendQueryParameter("regiontype", String.valueOf(this.currentRegion.getInt("RegionType")));
    ((Uri.Builder)localObject).appendQueryParameter("sort", this.currentSort.getString("ID"));
    ((Uri.Builder)localObject).appendQueryParameter("filterid", this.currentFilter.getString("ID"));
    ((Uri.Builder)localObject).appendQueryParameter("movieid", String.valueOf(this.movieId));
    if (!TextUtils.isEmpty(this.from))
      ((Uri.Builder)localObject).appendQueryParameter("from", this.from);
    if (!TextUtils.isEmpty(this.discountId))
      ((Uri.Builder)localObject).appendQueryParameter("discountid", this.discountId);
    String str = ((Uri.Builder)localObject).toString();
    if (this.isPullToRefresh);
    for (localObject = CacheType.DISABLED; ; localObject = CacheType.NORMAL)
    {
      this.cinemaRequest = BasicMApiRequest.mapiGet(str, (CacheType)localObject);
      mapiService().exec(this.cinemaRequest, this);
      this.cinemaListFilterBarFix.setEnabled(false);
      this.cinemaListFilterBarFloat.setEnabled(false);
      return;
    }
  }

  private void requestMovieDetail()
  {
    if (this.movieDetailRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/moviedetailmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("id", String.valueOf(this.movieId));
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    this.movieDetailRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.movieDetailRequest, this);
  }

  private void requestMovieShowExpress(List<String> paramList)
  {
    if (this.movieShowExpressRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/cinemalistmovieshowexpressmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("movieid", String.valueOf(this.movieId));
    localBuilder.appendQueryParameter("showtime", String.valueOf(new Date().getTime()));
    localBuilder.appendQueryParameter("cinemaidlist", CollectionUtils.list2Str(paramList, ","));
    this.movieShowExpressRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.movieShowExpressRequest, this);
  }

  private void setMovieData()
  {
    if ((this.dpMovie == null) || (this.isHiddenHeaderview));
    ArrayList localArrayList;
    do
    {
      do
      {
        return;
        setTitle(this.dpMovie.getString("Name") + " 购票");
        this.headerView.setVisibility(0);
        this.tableHeader.setVisibility(0);
        this.layerMovieInfo.setVisibility(0);
        this.movieInfoView.setVisibility(0);
        this.movieInfoView.setMovieInfo(this.dpMovie, this.showGrade, MovieInfoView.FROM_CINEMATLIST);
      }
      while (this.dpMovie.getArray("DiscountDescList") == null);
      localArrayList = new ArrayList();
      localArrayList.addAll(Arrays.asList(this.dpMovie.getArray("DiscountDescList")));
    }
    while (localArrayList.size() == 0);
    this.layerMovieDiscountDesc.removeAllViews();
    int i = 0;
    Object localObject;
    if (i < localArrayList.size())
    {
      localObject = (MovieDiscountItemView)LayoutInflater.from(this).inflate(R.layout.movie_discount_item_view, this.layerMovieDiscountDesc, false);
      ((MovieDiscountItemView)localObject).setMovieDiscount((DPObject)localArrayList.get(i));
      ((MovieDiscountItemView)localObject).setTag(localArrayList.get(i));
      if (i < 2)
      {
        ((MovieDiscountItemView)localObject).setVisibility(0);
        if (i == localArrayList.size() - 1)
          ((MovieDiscountItemView)localObject).findViewById(R.id.line).setVisibility(8);
      }
      while (true)
      {
        this.layerMovieDiscountDesc.addView((View)localObject);
        i += 1;
        break;
        ((MovieDiscountItemView)localObject).setVisibility(8);
      }
    }
    if (localArrayList.size() > 2)
    {
      localObject = LayoutInflater.from(this).inflate(R.layout.movie_info_expand_view, this.layerMovieDiscountDesc, false);
      TextView localTextView = (TextView)((View)localObject).findViewById(R.id.expand_hint);
      ImageView localImageView = (ImageView)((View)localObject).findViewById(R.id.expand_arrow);
      ((View)localObject).findViewById(R.id.line).setVisibility(8);
      localTextView.setText("查看更多" + (localArrayList.size() - 2) + "条优惠");
      localImageView.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down_shop));
      ((View)localObject).setTag("TOEXPAND");
      ((View)localObject).setVisibility(0);
      ((View)localObject).setOnClickListener(new View.OnClickListener(localTextView, localImageView, (View)localObject, localArrayList)
      {
        public void onClick(View paramView)
        {
          paramView = (String)paramView.getTag();
          int i;
          if (paramView.equals("TOEXPAND"))
          {
            i = 0;
            while (i < CinemaListActivity.this.layerMovieDiscountDesc.getChildCount())
            {
              CinemaListActivity.this.layerMovieDiscountDesc.getChildAt(i).setVisibility(0);
              if (i == CinemaListActivity.this.layerMovieDiscountDesc.getChildCount() - 1)
              {
                this.val$tvExpandHint.setText("收起");
                this.val$ivExpandArrow.setImageDrawable(CinemaListActivity.this.getResources().getDrawable(R.drawable.arrow_up_shop));
                this.val$expandView.setTag("EXPANDED");
              }
              i += 1;
            }
          }
          if (paramView.equals("EXPANDED"))
          {
            i = 0;
            if (i < CinemaListActivity.this.layerMovieDiscountDesc.getChildCount())
            {
              paramView = CinemaListActivity.this.layerMovieDiscountDesc.getChildAt(i);
              if (i < 2)
                paramView.setVisibility(0);
              while (true)
              {
                i += 1;
                break;
                if (i == CinemaListActivity.this.layerMovieDiscountDesc.getChildCount() - 1)
                {
                  this.val$tvExpandHint.setText("查看更多" + (this.val$discountDescList.size() - 2) + "条优惠");
                  this.val$ivExpandArrow.setImageDrawable(CinemaListActivity.this.getResources().getDrawable(R.drawable.arrow_down_shop));
                  this.val$expandView.setTag("TOEXPAND");
                  continue;
                }
                paramView.setVisibility(8);
              }
            }
          }
        }
      });
      this.layerMovieDiscountDesc.addView((View)localObject);
    }
    this.layerMovieDiscountDesc.setVisibility(0);
  }

  private void setNavs(DPObject[] paramArrayOfDPObject1, DPObject[] paramArrayOfDPObject2, DPObject[] paramArrayOfDPObject3)
  {
    DPObject[] arrayOfDPObject = paramArrayOfDPObject1;
    if (paramArrayOfDPObject1 == null)
      arrayOfDPObject = new DPObject[0];
    paramArrayOfDPObject1 = paramArrayOfDPObject2;
    if (paramArrayOfDPObject2 == null)
      paramArrayOfDPObject1 = new DPObject[0];
    paramArrayOfDPObject2 = paramArrayOfDPObject3;
    if (paramArrayOfDPObject3 == null)
      paramArrayOfDPObject2 = new DPObject[0];
    this.dpRegionNavs = arrayOfDPObject;
    this.dpSortNavs = paramArrayOfDPObject1;
    this.dpFilterNavs = paramArrayOfDPObject2;
    if ((this.dpRegionNavs.length > 0) && (!isDPObjectof(this.dpRegionNavs[0], "Region")))
      throw new IllegalArgumentException("argument {0} must be Region array");
    if ((this.dpSortNavs.length > 0) && (!isDPObjectof(this.dpSortNavs[0], "Pair")))
      throw new IllegalArgumentException("argument {1} must be Pair array");
    if ((this.dpFilterNavs.length > 0) && (!isDPObjectof(this.dpFilterNavs[0], "Pair")))
      throw new IllegalArgumentException("argument {1} must be Pair array");
    if (this.regionId < 0)
      this.regionId = getCurrentRegion().getInt("ID");
    int j;
    int i;
    if (this.regionId >= 0)
    {
      j = arrayOfDPObject.length;
      i = 0;
      if (i < j)
      {
        paramArrayOfDPObject3 = arrayOfDPObject[i];
        if (this.regionId == paramArrayOfDPObject3.getInt("ID"))
        {
          this.currentRegion = paramArrayOfDPObject3;
          this.regionId = -1;
        }
      }
      else
      {
        if (this.regionId >= 0)
          this.currentRegion = this.DEFAULT_REGION;
        this.regionId = -1;
      }
    }
    else
    {
      if (TextUtils.isEmpty(this.sortId))
        this.sortId = getCurrentSort().getString("ID");
      if (!TextUtils.isEmpty(this.sortId))
      {
        j = paramArrayOfDPObject1.length;
        i = 0;
        label284: if (i < j)
        {
          paramArrayOfDPObject3 = paramArrayOfDPObject1[i];
          if (!paramArrayOfDPObject3.getString("ID").equals(this.sortId))
            break label472;
          this.currentSort = paramArrayOfDPObject3;
          this.sortId = null;
        }
        if (!TextUtils.isEmpty(this.sortId))
          this.currentSort = this.DEFAULT_SORT;
        this.sortId = null;
      }
      if (TextUtils.isEmpty(this.filterId))
        this.filterId = getCurrentFilter().getString("ID");
      if (!TextUtils.isEmpty(this.filterId))
      {
        j = paramArrayOfDPObject2.length;
        i = 0;
      }
    }
    while (true)
    {
      if (i < j)
      {
        paramArrayOfDPObject1 = paramArrayOfDPObject2[i];
        if (paramArrayOfDPObject1.getString("ID").equals(this.filterId))
        {
          this.currentFilter = paramArrayOfDPObject1;
          this.filterId = null;
        }
      }
      else
      {
        if (!TextUtils.isEmpty(this.filterId))
          this.currentFilter = this.DEFAULT_FILTER;
        this.filterId = null;
        updateNavs(this.currentRegion, this.currentSort, this.currentFilter);
        return;
        i += 1;
        break;
        label472: i += 1;
        break label284;
      }
      i += 1;
    }
  }

  public DPObject getCurrentFilter()
  {
    if (!TextUtils.isEmpty(this.filterId))
      return new DPObject("Pair").edit().putString("ID", this.filterId).generate();
    if (this.currentFilter == null)
      return this.DEFAULT_FILTER;
    return this.currentFilter;
  }

  public DPObject getCurrentRegion()
  {
    if (this.regionId > 0)
      return new DPObject("Region").edit().putInt("ID", this.regionId).putInt("ParentID", 0).generate();
    if (this.currentRegion == null)
      return this.DEFAULT_REGION;
    return this.currentRegion;
  }

  public DPObject getCurrentSort()
  {
    if (!TextUtils.isEmpty(this.sortId))
      return new DPObject("Pair").edit().putString("ID", this.sortId).generate();
    if (this.currentSort == null)
      return this.DEFAULT_SORT;
    return this.currentSort;
  }

  public String getPageName()
  {
    if (this.movieId == 0)
      return "cinemalist";
    return "moviecinemalist";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public boolean locationCare()
  {
    return true;
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if ("region".equals(paramObject))
      if (this.dpRegionNavs != null);
    do
      while (true)
      {
        return;
        this.dlg = new CinemaListFilterDialog(this, true);
        this.dlg.setTag(paramObject);
        ((CinemaListFilterDialog)this.dlg).setItems(this.dpRegionNavs);
        ((CinemaListFilterDialog)this.dlg).setSelectedItem(this.currentRegion);
        this.dlg.setOnFilterListener(this.filterListener);
        this.dlg.show(paramView);
        GAHelper.instance().contextStatisticsEvent(this, "navi_region", null, 0, "tap");
        return;
        if (!"sort".equals(paramObject))
          break;
        if (this.dpSortNavs == null)
          continue;
        this.dlg = new ListFilterDialog(this);
        this.dlg.setTag(paramObject);
        ((ListFilterDialog)this.dlg).setItems(this.dpSortNavs);
        ((ListFilterDialog)this.dlg).setSelectedItem(this.currentSort);
        this.dlg.setOnFilterListener(this.filterListener);
        this.dlg.show(paramView);
        GAHelper.instance().contextStatisticsEvent(this, "navi_sort", null, 0, "tap");
        return;
      }
    while ((!"filter".equals(paramObject)) || (this.dpFilterNavs == null));
    this.dlg = new ListFilterDialog(this);
    this.dlg.setTag(paramObject);
    ((ListFilterDialog)this.dlg).setItems(this.dpFilterNavs);
    ((ListFilterDialog)this.dlg).setSelectedItem(this.currentFilter);
    this.dlg.setOnFilterListener(this.filterListener);
    this.dlg.show(paramView);
    GAHelper.instance().contextStatisticsEvent(this, "navi_filter", null, 0, "tap");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040019");
    this.context = this;
    setContentView(R.layout.cinema_list_activity);
    if (paramBundle != null)
    {
      this.movieId = paramBundle.getInt("movieId");
      this.dpMovie = ((DPObject)paramBundle.getParcelable("dpMovie"));
      this.flag = paramBundle.getInt("flag");
      this.isHiddenHeaderview = paramBundle.getBoolean("isHiddenHeaderview");
      this.hasMovieShow = paramBundle.getBoolean("hasMovieShow");
      this.showGrade = paramBundle.getBoolean("showGrade");
      this.discountId = paramBundle.getString("discountId");
      this.currentRegion = ((DPObject)paramBundle.getParcelable("currentRegion"));
      this.currentSort = ((DPObject)paramBundle.getParcelable("currentSort"));
      this.currentFilter = ((DPObject)paramBundle.getParcelable("currentFilter"));
      this.isFilterSelected = paramBundle.getBoolean("isFilterSelected");
      if (this.currentRegion == null)
      {
        paramBundle = this.DEFAULT_REGION;
        this.currentRegion = paramBundle;
        if (this.currentSort != null)
          break label718;
        paramBundle = this.DEFAULT_REGION;
        label190: this.currentSort = paramBundle;
        if (this.currentFilter != null)
          break label726;
        paramBundle = this.DEFAULT_REGION;
        label207: this.currentFilter = paramBundle;
        this.cinemaListFilterBarFix = ((CinemaFilterBar)findViewById(R.id.cinemalist_filterbar_fix));
        this.cinemaListFilterBarFix.addItem("region", this.currentRegion.getString("Name"));
        this.cinemaListFilterBarFix.addItem("sort", this.currentSort.getString("Name"));
        this.cinemaListFilterBarFix.addItem("filter", this.currentFilter.getString("Name"));
        this.cinemaListFilterBarFix.setOnItemClickListener(this);
        this.ptrlv = ((PullToRefreshListView)findViewById(R.id.cinemalist_ptr));
        this.ptrlv.setOnRefreshListener(this);
        this.ptrlv.setOnScrollListener(this);
        this.cinemaListView = this.ptrlv;
        this.cinemaListView.setDivider(null);
        this.cinemaListView.setOnItemClickListener(this);
        this.headerView = LayoutInflater.from(this.context).inflate(R.layout.cinema_ptrlv_headerview, this.ptrlv, false);
        this.ptrlv.addHeaderView(this.headerView, null, false);
        this.layerMovieInfo = ((LinearLayout)this.headerView.findViewById(R.id.layer_movie_info));
        this.movieInfoView = ((MovieInfoView)this.headerView.findViewById(R.id.movie_info_view));
        this.layerMovieDiscountDesc = ((LinearLayout)this.headerView.findViewById(R.id.layer_moviediscountdesc));
        this.layerMovieInfo.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            if (CinemaListActivity.this.dpMovie != null)
              CinemaListActivity.this.startActivity("dianping://moviedetail?movieid=" + CinemaListActivity.this.dpMovie.getInt("ID"));
          }
        });
        this.headerView2 = LayoutInflater.from(this.context).inflate(R.layout.cinema_ptrlv_headerview2, this.ptrlv, false);
        this.ptrlv.addHeaderView(this.headerView2, null, false);
        this.cinemaListFilterBarFloat = ((CinemaFilterBar)this.headerView2.findViewById(R.id.cinemalist_filterbar_float));
        this.cinemaListFilterBarFloat.addItem("region", this.currentRegion.getString("Name"));
        this.cinemaListFilterBarFloat.addItem("sort", this.currentSort.getString("Name"));
        this.cinemaListFilterBarFloat.addItem("filter", this.currentFilter.getString("Name"));
        this.cinemaListFilterBarFloat.setOnItemClickListener(this);
        this.tableHeader = this.headerView2.findViewById(R.id.separator);
        this.headerView.setVisibility(8);
        this.tableHeader.setVisibility(8);
        this.layerMovieInfo.setVisibility(8);
        this.movieInfoView.setVisibility(8);
        this.layerMovieDiscountDesc.setVisibility(8);
        if ((this.isHiddenHeaderview) || (this.movieId == 0))
        {
          this.cinemaListFilterBarFloat.setVisibility(0);
          this.showCinemaFilterBarFix = true;
        }
        getMyLocation();
        this.cinemaAdapter = new CinemaAdapter();
        this.cinemaListView.setAdapter(this.cinemaAdapter);
        if (this.movieId == 0)
          break label1077;
        requestMovieDetail();
        showProgressDialog("正在获取影片信息...");
      }
    }
    while (true)
    {
      this.cinemaMovieShowExpressMap.clear();
      return;
      paramBundle = this.currentRegion;
      break;
      label718: paramBundle = this.currentSort;
      break label190;
      label726: paramBundle = this.currentFilter;
      break label207;
      paramBundle = getIntent();
      Uri localUri = getIntent().getData();
      label766: int i;
      if (localUri != null)
      {
        if (localUri.getQueryParameter("movieid") == null)
        {
          this.movieId = 0;
          if (localUri.getQueryParameter("flag") != null)
            break label1031;
          this.flag = 0;
          label781: this.isHiddenHeaderview = "1".equals(localUri.getQueryParameter("ishiddenheaderview"));
          this.hasMovieShow = true;
          if (!this.hasMovieShow)
            break label1048;
          bool = true;
          label812: this.showGrade = bool;
          if ((this.showGrade) && (this.flag == 2))
            this.showGrade = false;
          this.discountId = localUri.getQueryParameter("discountid");
        }
      }
      else if (this.movieId == 0)
      {
        this.dpMovie = ((DPObject)paramBundle.getParcelableExtra("movie"));
        this.flag = paramBundle.getIntExtra("flag", 2);
        if (paramBundle.getIntExtra("hasmovieshow", 0) != 1)
          break label1054;
        bool = true;
        label897: this.hasMovieShow = bool;
        if (paramBundle.getIntExtra("ishiddenheaderview", 0) != 1)
          break label1060;
        bool = true;
        label918: this.isHiddenHeaderview = bool;
        if (this.dpMovie == null)
          break label1066;
        i = this.dpMovie.getInt("ID");
        label941: this.movieId = i;
        if (!this.hasMovieShow)
          break label1071;
      }
      label1031: label1048: label1054: label1060: label1066: label1071: for (boolean bool = true; ; bool = false)
      {
        this.showGrade = bool;
        if ((this.showGrade) && (this.flag == 2))
          this.showGrade = false;
        this.currentRegion = this.DEFAULT_REGION;
        this.currentSort = this.DEFAULT_SORT;
        this.currentFilter = this.DEFAULT_FILTER;
        this.isFilterSelected = false;
        break;
        this.movieId = Integer.parseInt(localUri.getQueryParameter("movieid"));
        break label766;
        this.flag = Integer.parseInt(localUri.getQueryParameter("flag"));
        break label781;
        bool = false;
        break label812;
        bool = false;
        break label897;
        bool = false;
        break label918;
        i = 0;
        break label941;
      }
      label1077: setTitle(city().name() + "电影院");
      requestBanner();
    }
  }

  protected void onDestroy()
  {
    if (this.cinemaRequest != null)
    {
      mapiService().abort(this.cinemaRequest, this, true);
      this.cinemaRequest = null;
    }
    while (true)
    {
      super.onDestroy();
      return;
      if (this.movieDetailRequest != null)
      {
        mapiService().abort(this.movieDetailRequest, this, true);
        this.movieDetailRequest = null;
        continue;
      }
      if (this.bannerRequest != null)
      {
        mapiService().abort(this.bannerRequest, this, true);
        this.bannerRequest = null;
        continue;
      }
      if (this.movieShowExpressRequest == null)
        continue;
      mapiService().abort(this.movieShowExpressRequest, this, true);
      this.movieShowExpressRequest = null;
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramInt > 0)
    {
      paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
      if (DPObjectUtils.isDPObjectof(paramAdapterView, "Cinema"))
      {
        paramAdapterView = (DPObject)paramAdapterView;
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + paramAdapterView.getInt("ID") + "&movieid=" + this.movieId)));
      }
    }
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    getMyLocation();
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    if (paramPullToRefreshListView.getId() == R.id.cinemalist_ptr)
    {
      this.isPullToRefresh = true;
      this.cinemaAdapter.refresh();
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    SimpleMsg localSimpleMsg = paramMApiResponse.message();
    if (paramMApiRequest == this.bannerRequest)
      this.bannerRequest = null;
    do
    {
      return;
      if (paramMApiRequest == this.movieDetailRequest)
      {
        Toast.makeText(this.context, localSimpleMsg.content(), 1).show();
        this.movieDetailRequest = null;
        return;
      }
      if (paramMApiRequest != this.movieShowExpressRequest)
        continue;
      this.movieShowExpressRequest = null;
      return;
    }
    while (paramMApiRequest != this.cinemaRequest);
    this.cinemaListFilterBarFix.setEnabled(true);
    this.cinemaListFilterBarFloat.setEnabled(true);
    this.ptrlv.onRefreshComplete();
    this.errorMsg = paramMApiResponse.message().toString();
    this.cinemaAdapter.notifyDataSetChanged();
    this.cinemaRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.bannerRequest)
    {
      if ((paramMApiResponse instanceof DPObject))
      {
        this.headerView.setVisibility(0);
        paramMApiRequest = (ViewStub)this.headerView.findViewById(R.id.banner_stub);
        if (paramMApiRequest != null)
          paramMApiRequest.inflate();
        this.bannerView = ((MovieBannerView)this.headerView.findViewById(R.id.movie_banner_view));
        this.bannerView.setBtnOnCloseListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            CinemaListActivity.this.bannerView.setVisibility(8);
          }
        });
        this.bannerView.setOnBannerClickGA(new MovieBannerView.OnBannerClickGA()
        {
          public void onBannerClick(DPObject paramDPObject)
          {
          }
        });
        this.bannerView.setCloseDrawable(R.drawable.banner_close);
        this.bannerView.setNavigationDotNormalDrawable(R.drawable.home_serve_dot);
        this.bannerView.setNavigationDotPressedDrawable(R.drawable.home_serve_dot_pressed);
        paramMApiRequest = ((DPObject)paramMApiResponse).getArray("List");
        if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
        {
          this.bannerView.setVisibility(0);
          this.bannerView.setBanner(paramMApiRequest);
          this.bannerView.showCloseBtn();
        }
      }
      this.bannerRequest = null;
    }
    label430: label495: 
    do
    {
      int i;
      Object localObject;
      while (true)
      {
        return;
        if (paramMApiRequest == this.movieDetailRequest)
        {
          if (DPObjectUtils.isDPObjectof(paramMApiResponse, "Movie"))
          {
            this.dpMovie = ((DPObject)paramMApiResponse);
            setMovieData();
          }
          this.movieDetailRequest = null;
          return;
        }
        if (paramMApiRequest == this.movieShowExpressRequest)
        {
          this.movieShowExpressRequest = null;
          if (!DPObjectUtils.isDPObjectof(paramMApiResponse, "CinemaMovieShowExpressList"))
            continue;
          paramMApiRequest = ((DPObject)paramMApiResponse).getArray("List");
          this.cinemaMovieShowExpressMap.clear();
          if ((paramMApiRequest == null) || (paramMApiRequest.length <= 0))
            continue;
          i = 0;
          while (i < paramMApiRequest.length)
          {
            this.cinemaMovieShowExpressMap.put(Integer.valueOf(paramMApiRequest[i].getInt("CinemaId")), paramMApiRequest[i]);
            i += 1;
          }
          this.cinemaAdapter.notifyDataSetChanged();
          return;
        }
        if (paramMApiRequest != this.cinemaRequest)
          continue;
        this.cinemaListFilterBarFix.setEnabled(true);
        this.cinemaListFilterBarFloat.setEnabled(true);
        this.cinemaRequest = null;
        if (!DPObjectUtils.isDPObjectof(paramMApiResponse, "CinemaList"))
          continue;
        if (this.nextStartIndex != 0)
          break;
        i = 1;
        appendCinemas((DPObject)paramMApiResponse);
        if (this.isPullToRefresh)
          this.ptrlv.onRefreshComplete();
        if ((this.movieId == 0) || (i == 0))
          continue;
        paramMApiRequest = ((DPObject)paramMApiResponse).getArray("List");
        paramMApiResponse = new ArrayList();
        i = 0;
        if (i >= paramMApiRequest.length)
          break label514;
        localObject = paramMApiRequest[i];
        if (((localObject.getBoolean("FavShop")) || (localObject.getBoolean("LastConsumed"))) && (!paramMApiResponse.contains(String.valueOf(localObject.getInt("ID")))))
          break label495;
      }
      while (true)
      {
        i += 1;
        break label430;
        i = 0;
        break;
        paramMApiResponse.add(String.valueOf(localObject.getInt("ID")));
      }
    }
    while (paramMApiResponse.size() <= 0);
    label514: requestMovieShowExpress(paramMApiResponse);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("currentRegion", this.currentRegion);
    paramBundle.putParcelable("currentSort", this.currentSort);
    paramBundle.putParcelable("currentFilter", this.currentFilter);
    paramBundle.putString("discountId", this.discountId);
    paramBundle.putBoolean("showGrade", this.showGrade);
    paramBundle.putBoolean("hasMovieShow", this.hasMovieShow);
    paramBundle.putBoolean("isHiddenHeaderview", this.isHiddenHeaderview);
    paramBundle.putInt("flag", this.flag);
    paramBundle.putParcelable("dpMovie", this.dpMovie);
    paramBundle.putInt("movieId", this.movieId);
    paramBundle.putBoolean("isFilterSelected", this.isFilterSelected);
  }

  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 < this.cinemaListView.getHeaderViewsCount() - 1)
      if (this.showCinemaFilterBarFix)
      {
        this.showCinemaFilterBarFix = false;
        this.cinemaListFilterBarFix.setVisibility(8);
        this.cinemaListFilterBarFloat.setVisibility(0);
      }
    do
      return;
    while (this.showCinemaFilterBarFix);
    this.showCinemaFilterBarFix = true;
    this.cinemaListFilterBarFix.setVisibility(0);
    this.cinemaListFilterBarFloat.setVisibility(4);
  }

  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
  }

  public void setCurrentFilter(DPObject paramDPObject)
  {
    if (!isDPObjectof(paramDPObject, "Pair"))
      throw new IllegalArgumentException("argument must be pair");
    this.currentFilter = paramDPObject;
  }

  public void setCurrentRegion(DPObject paramDPObject)
  {
    if (!isDPObjectof(paramDPObject, "Region"))
      throw new IllegalArgumentException("argument must be region");
    this.currentRegion = paramDPObject;
  }

  public void setCurrentSort(DPObject paramDPObject)
  {
    if (!isDPObjectof(paramDPObject, "Pair"))
      throw new IllegalArgumentException("argument must be pair");
    this.currentSort = paramDPObject;
  }

  public boolean setSelectedNavs(int paramInt, String paramString1, String paramString2)
  {
    int i = 0;
    int j = i;
    if (paramInt >= 0)
    {
      j = i;
      if (paramInt != getCurrentRegion().getInt("ID"))
      {
        this.regionId = paramInt;
        j = 1;
      }
    }
    i = j;
    if (!TextUtils.isEmpty(paramString1))
    {
      i = j;
      if (!paramString1.equals(getCurrentSort().getString("ID")))
      {
        if (!"2".equals(paramString1))
          break label138;
        i = j;
        if (location() != null)
          this.sortId = paramString1;
      }
    }
    for (i = 1; ; i = 1)
    {
      j = i;
      if (!TextUtils.isEmpty(paramString2))
      {
        j = i;
        if (!paramString2.equals(getCurrentFilter().getString("ID")))
        {
          this.filterId = paramString2;
          j = 1;
        }
      }
      return j;
      label138: this.sortId = paramString1;
    }
  }

  public void updateNavs(DPObject paramDPObject1, DPObject paramDPObject2, DPObject paramDPObject3)
  {
    if (paramDPObject1 != null)
    {
      this.currentRegion = paramDPObject1;
      if (paramDPObject2 == null)
        break label69;
      this.currentSort = paramDPObject2;
      label18: if ((paramDPObject3 == null) || (!this.isFilterSelected))
        break label80;
    }
    label69: label80: for (this.currentFilter = paramDPObject3; ; this.currentFilter = this.DEFAULT_FILTER)
    {
      if (isDPObjectof(this.currentRegion, "Region"))
        break label91;
      throw new IllegalArgumentException("argument {0} must be Region");
      this.currentRegion = this.DEFAULT_REGION;
      break;
      this.currentSort = this.DEFAULT_SORT;
      break label18;
    }
    label91: if (!isDPObjectof(this.currentSort, "Pair"))
      throw new IllegalArgumentException("argument {1} must be Pair");
    if (!isDPObjectof(this.currentFilter, "Pair"))
      throw new IllegalArgumentException("argument {1} must be Pair");
    this.cinemaListFilterBarFix.setItem("region", this.currentRegion.getString("Name"));
    this.cinemaListFilterBarFix.setItem("sort", this.currentSort.getString("Name"));
    this.cinemaListFilterBarFix.setItem("filter", this.currentFilter.getString("Name"));
    this.cinemaListFilterBarFloat.setItem("region", this.currentRegion.getString("Name"));
    this.cinemaListFilterBarFloat.setItem("sort", this.currentSort.getString("Name"));
    this.cinemaListFilterBarFloat.setItem("filter", this.currentFilter.getString("Name"));
  }

  class CinemaAdapter extends BasicAdapter
  {
    CinemaAdapter()
    {
    }

    public int getCount()
    {
      if (CinemaListActivity.this.isEnd)
        return CinemaListActivity.this.cinemaList.size();
      return CinemaListActivity.this.cinemaList.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < CinemaListActivity.this.cinemaList.size())
        return CinemaListActivity.this.cinemaList.get(paramInt);
      if (CinemaListActivity.this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject2 = null;
      Object localObject1 = null;
      Object localObject3 = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(localObject3, "Cinema"))
      {
        paramInt = Integer.valueOf(CinemaListActivity.this.getCurrentFilter().getString("ID")).intValue();
        if (CinemaListActivity.this.movieId == 0)
        {
          if ((paramView instanceof CinemaListItem))
            localObject1 = (CinemaListItem)paramView;
          paramView = (View)localObject1;
          if (localObject1 == null)
            paramView = (CinemaListItem)LayoutInflater.from(CinemaListActivity.this.context).inflate(R.layout.movie_cinema_list_item, paramViewGroup, false);
          if (CinemaListActivity.this.isLocated)
          {
            paramView.setCinema((DPObject)localObject3, CinemaListActivity.this.myLatitude, CinemaListActivity.this.myLongitude, paramInt);
            paramViewGroup = paramView;
          }
        }
        do
        {
          return paramViewGroup;
          paramView.setCinema((DPObject)localObject3, 0.0D, 0.0D, paramInt);
          return paramView;
          localObject1 = localObject2;
          if ((paramView instanceof CinemaListItemWithSelectedMovie))
            localObject1 = (CinemaListItemWithSelectedMovie)paramView;
          paramView = (View)localObject1;
          if (localObject1 == null)
            paramView = (CinemaListItemWithSelectedMovie)LayoutInflater.from(CinemaListActivity.this.context).inflate(R.layout.movie_cinema_list_item_withselectedmovie, paramViewGroup, false);
          paramViewGroup = paramView;
        }
        while (localObject3 == null);
        paramViewGroup = (DPObject)localObject3;
        localObject1 = (DPObject)CinemaListActivity.this.cinemaMovieShowExpressMap.get(Integer.valueOf(paramViewGroup.getInt("ID")));
        if (CinemaListActivity.this.isLocated)
        {
          paramView.setCinemaWithMovie(paramViewGroup, CinemaListActivity.this.myLatitude, CinemaListActivity.this.myLongitude, paramInt, (DPObject)localObject1, CinemaListActivity.this.movieId, CinemaListActivity.this.dpMovie);
          return paramView;
        }
        paramView.setCinemaWithMovie(paramViewGroup, 0.0D, 0.0D, paramInt, (DPObject)localObject1, CinemaListActivity.this.movieId, CinemaListActivity.this.dpMovie);
        return paramView;
      }
      if (localObject3 == LOADING)
      {
        if (CinemaListActivity.this.errorMsg == null)
          CinemaListActivity.this.loadNewPage();
        return getLoadingView(paramViewGroup, paramView);
      }
      return (View)getFailedView(CinemaListActivity.this.errorMsg, new CinemaListActivity.CinemaAdapter.1(this), paramViewGroup, paramView);
    }

    public void refresh()
    {
      if (CinemaListActivity.this.cinemaRequest != null)
      {
        CinemaListActivity.this.mapiService().abort(CinemaListActivity.this.cinemaRequest, null, true);
        CinemaListActivity.access$1502(CinemaListActivity.this, null);
      }
      CinemaListActivity.access$1602(CinemaListActivity.this, false);
      CinemaListActivity.access$1702(CinemaListActivity.this, 0);
      CinemaListActivity.access$1802(CinemaListActivity.this, 0);
      CinemaListActivity.access$1902(CinemaListActivity.this, null);
      CinemaListActivity.access$2002(CinemaListActivity.this, null);
      CinemaListActivity.this.loadNewPage();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.CinemaListActivity
 * JD-Core Version:    0.6.0
 */
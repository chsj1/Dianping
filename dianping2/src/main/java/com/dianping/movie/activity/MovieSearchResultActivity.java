package com.dianping.movie.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPApplication;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.movie.view.CinemaListItem;
import com.dianping.movie.view.MovieOnInfoItem;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaFrameLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class MovieSearchResultActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, LocationListener, PullToRefreshListView.OnRefreshListener, AdapterView.OnItemClickListener
{
  public static final DPObject CINEAM_REC;
  private static final int DEFAULT_DISPLAY_COUNT = 3;
  private static final int LIMIT = 15;
  public static final DPObject MOVIE_MORE = new DPObject().edit().putString("__name", "MovieMore").generate();
  public static final DPObject MOVIE_REC;
  private ArrayList<DPObject> cinemaList = new ArrayList();
  private int cinemaListOffset;
  private String cinemaListTitle = "";
  private View clearBtn;
  private View contentLayout;
  private String errorMsg = null;
  private boolean isCinemaAdded;
  private boolean isCinemaEnd = true;
  private boolean isLocated = false;
  private boolean isMovieEnd = true;
  private boolean isMovieExpended;
  private String keyWord = "";
  private View loadingLayout;
  private SearchAdapter mAdapter;
  private int mPosition = -1;
  private View mainTitleView;
  private ArrayList<DPObject> movieList = new ArrayList();
  private int movieListOffset;
  private String movieListTitle = "";
  private MApiRequest movieSearchRequest;
  private int myAccuracy = 0;
  private double myLatitude = 0.0D;
  private double myLongitude = 0.0D;
  private MApiRequest pageMoreRequest;
  private PullToRefreshListView ptrlv;
  private ArrayList<DPObject> resultList = new ArrayList();
  private LinearLayout retryLayout;
  private int scope = 0;
  private boolean targetIsHit = true;
  private String targetNotHitTitle = "";
  private TextView titleContentTv;
  private TextView unHitTv;

  static
  {
    CINEAM_REC = new DPObject().edit().putString("__name", "CinemaRec").generate();
    MOVIE_REC = new DPObject().edit().putString("__name", "MovieRec").generate();
  }

  private void appendPageSearchResult(DPObject paramDPObject, int paramInt)
  {
    DPObject[] arrayOfDPObject;
    if (this.scope == 1)
    {
      paramDPObject = paramDPObject.getObject("MovieList");
      arrayOfDPObject = paramDPObject.getArray("List");
      this.isMovieEnd = paramDPObject.getBoolean("IsEnd");
      if ((this.isMovieEnd) && (this.isMovieExpended))
      {
        if (paramInt != -1)
          this.resultList.remove(paramInt);
        if (arrayOfDPObject != null)
        {
          if (paramInt == -1)
          {
            this.resultList.addAll(Arrays.asList(arrayOfDPObject));
            this.movieListOffset = paramDPObject.getInt("NextStartIndex");
          }
        }
        else
          label91: if ((this.isMovieEnd) && (this.resultList.size() <= 0))
          {
            this.isMovieEnd = false;
            this.movieListOffset = 0;
            this.errorMsg = "您请求的数据不存在";
          }
      }
    }
    while (true)
    {
      this.mAdapter.notifyDataSetChanged();
      return;
      this.resultList.addAll(paramInt, Arrays.asList(arrayOfDPObject));
      break;
      if (arrayOfDPObject == null)
        break label91;
      if (paramInt == -1)
        this.resultList.addAll(Arrays.asList(arrayOfDPObject));
      while (true)
      {
        this.movieListOffset = paramDPObject.getInt("NextStartIndex");
        if ((this.isMovieEnd) || (arrayOfDPObject.length != 0))
          break;
        this.isMovieEnd = true;
        break;
        this.resultList.addAll(paramInt, Arrays.asList(arrayOfDPObject));
      }
      if (this.scope != 2)
        continue;
      paramDPObject = paramDPObject.getObject("CinemaList");
      arrayOfDPObject = paramDPObject.getArray("List");
      if (arrayOfDPObject != null)
      {
        this.resultList.addAll(Arrays.asList(arrayOfDPObject));
        this.cinemaListOffset = paramDPObject.getInt("NextStartIndex");
        this.isCinemaEnd = paramDPObject.getBoolean("IsEnd");
        if ((!this.isCinemaEnd) && (arrayOfDPObject.length == 0))
          this.isCinemaEnd = true;
      }
      if ((!this.isCinemaEnd) || (this.resultList.size() > 0))
        continue;
      this.isCinemaEnd = false;
      this.cinemaListOffset = 0;
      this.errorMsg = "您请求的数据不存在";
    }
  }

  private void fillResultList(DPObject paramDPObject)
  {
    DPObject localDPObject;
    if (this.targetIsHit)
    {
      this.unHitTv.setVisibility(8);
      localDPObject = paramDPObject.getObject("CinemaList");
      if ((localDPObject != null) && (localDPObject.getArray("List") != null) && (localDPObject.getArray("List").length > 0))
      {
        this.cinemaList.addAll(Arrays.asList(localDPObject.getArray("List")));
        this.cinemaListOffset = localDPObject.getInt("NextStartIndex");
        this.isCinemaEnd = localDPObject.getBoolean("IsEnd");
        paramDPObject = paramDPObject.getObject("MovieList");
        if ((paramDPObject == null) || (paramDPObject.getArray("List") == null) || (paramDPObject.getArray("List").length <= 0))
          break label283;
        this.movieList.addAll(Arrays.asList(paramDPObject.getArray("List")));
        this.movieListOffset = paramDPObject.getInt("NextStartIndex");
        this.isMovieEnd = paramDPObject.getBoolean("IsEnd");
        label150: if (this.movieList.size() > 0)
        {
          if ((3 >= this.movieList.size()) || (this.cinemaList.size() <= 0))
            break label291;
          this.resultList.add(MOVIE_REC);
          this.resultList.addAll(this.movieList.subList(0, 3));
          this.resultList.add(MOVIE_MORE);
        }
        label220: if (this.cinemaList.size() <= 0)
          break label322;
        this.resultList.add(CINEAM_REC);
        this.resultList.addAll(this.cinemaList);
        this.isCinemaAdded = true;
      }
    }
    while (true)
    {
      this.loadingLayout.setVisibility(8);
      this.mAdapter.notifyDataSetChanged();
      return;
      this.isCinemaEnd = true;
      break;
      label283: this.isMovieEnd = true;
      break label150;
      label291: this.resultList.add(MOVIE_REC);
      this.resultList.addAll(this.movieList);
      this.isMovieExpended = true;
      break label220;
      label322: this.isCinemaAdded = false;
      continue;
      this.unHitTv.setVisibility(0);
      this.unHitTv.setText(this.targetNotHitTitle);
      localDPObject = paramDPObject.getObject("MovieList");
      if ((localDPObject != null) && (localDPObject.getArray("List") != null) && (localDPObject.getArray("List").length > 0))
      {
        this.resultList.addAll(Arrays.asList(localDPObject.getArray("List")));
        continue;
      }
      paramDPObject = paramDPObject.getObject("CinemaList");
      if ((paramDPObject == null) || (paramDPObject.getArray("List") == null) || (paramDPObject.getArray("List").length <= 0))
        continue;
      this.resultList.addAll(Arrays.asList(paramDPObject.getArray("List")));
    }
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
    if ((this.isCinemaEnd) && (this.isMovieEnd));
    do
      return false;
    while (this.pageMoreRequest != null);
    this.errorMsg = null;
    if (this.pageMoreRequest != null)
      return true;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/moviesearchmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    localBuilder.appendQueryParameter("keyword", this.keyWord);
    localBuilder.appendQueryParameter("limit", String.valueOf(15));
    switch (this.scope)
    {
    default:
      localBuilder.appendQueryParameter("scope", String.valueOf(0));
    case 2:
    case 1:
    }
    while (true)
    {
      if (isLocated())
      {
        localBuilder.appendQueryParameter("lat", Location.FMT.format(this.myLatitude));
        localBuilder.appendQueryParameter("lng", Location.FMT.format(this.myLongitude));
        localBuilder.appendQueryParameter("accuracy", String.valueOf(this.myAccuracy));
      }
      this.pageMoreRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      mapiService().exec(this.pageMoreRequest, this);
      return true;
      localBuilder.appendQueryParameter("scope", String.valueOf(2));
      localBuilder.appendQueryParameter("offset", String.valueOf(this.cinemaListOffset));
      continue;
      localBuilder.appendQueryParameter("scope", String.valueOf(1));
      localBuilder.appendQueryParameter("offset", String.valueOf(this.movieListOffset));
    }
  }

  private void sendSearchRequest()
  {
    if (this.movieSearchRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/moviesearchmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    localBuilder.appendQueryParameter("keyword", this.keyWord);
    localBuilder.appendQueryParameter("limit", String.valueOf(15));
    switch (this.scope)
    {
    default:
      localBuilder.appendQueryParameter("scope", String.valueOf(0));
    case 2:
    case 1:
    }
    while (true)
    {
      if (isLocated())
      {
        localBuilder.appendQueryParameter("lat", Location.FMT.format(this.myLatitude));
        localBuilder.appendQueryParameter("lng", Location.FMT.format(this.myLongitude));
        localBuilder.appendQueryParameter("accuracy", String.valueOf(this.myAccuracy));
      }
      this.movieSearchRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      mapiService().exec(this.movieSearchRequest, this);
      return;
      localBuilder.appendQueryParameter("scope", String.valueOf(2));
      localBuilder.appendQueryParameter("offset", String.valueOf(this.cinemaListOffset));
      continue;
      localBuilder.appendQueryParameter("scope", String.valueOf(1));
      localBuilder.appendQueryParameter("offset", String.valueOf(this.movieListOffset));
    }
  }

  private void setupMainTitleView(String paramString)
  {
    if (this.mainTitleView == null)
    {
      this.mainTitleView = getLayoutInflater().inflate(R.layout.movie_search_result_title_keyword, null);
      this.titleContentTv = ((TextView)this.mainTitleView.findViewById(R.id.movie_search_tv));
      this.clearBtn = this.mainTitleView.findViewById(R.id.clear_button);
    }
    this.titleContentTv.setText(paramString);
    this.titleContentTv.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent(MovieSearchResultActivity.this, MovieSearchActivity.class);
        paramView.putExtra("keyword", MovieSearchResultActivity.this.keyWord);
        paramView.putExtra("scope", Integer.valueOf(MovieSearchResultActivity.this.scope));
        paramView.setFlags(67108864);
        MovieSearchResultActivity.this.startActivity(paramView);
        GAHelper.instance().contextStatisticsEvent(MovieSearchResultActivity.this, "movie_search", MovieSearchResultActivity.this.keyWord, 0, "tap");
      }
    });
    this.clearBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent(MovieSearchResultActivity.this, MovieSearchActivity.class);
        paramView.putExtra("keyword", "");
        paramView.putExtra("scope", Integer.valueOf(MovieSearchResultActivity.this.scope));
        paramView.setFlags(67108864);
        MovieSearchResultActivity.this.startActivity(paramView);
      }
    });
    getTitleBar().setCustomContentView(this.mainTitleView);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040018");
    super.setContentView(R.layout.movie_search_result_activity);
    paramBundle = getIntent();
    try
    {
      this.scope = Integer.valueOf(getStringParam("scope")).intValue();
      this.keyWord = paramBundle.getStringExtra("keyword");
      if (this.keyWord == null)
        this.keyWord = getStringParam("keyword");
      this.loadingLayout = findViewById(R.id.status);
      this.contentLayout = findViewById(R.id.content);
      this.retryLayout = ((LinearLayout)findViewById(R.id.loading_retry_layer));
      this.unHitTv = ((TextView)findViewById(R.id.unhit_tv));
      this.ptrlv = ((PullToRefreshListView)findViewById(R.id.result_list_ptr));
      this.ptrlv.setOnRefreshListener(this);
      this.ptrlv.setPullRefreshEnable(1);
      this.ptrlv.setPullLoadEnable(0);
      setupMainTitleView(this.keyWord);
      getMyLocation();
      this.mAdapter = new SearchAdapter();
      this.ptrlv.setAdapter(this.mAdapter);
      this.ptrlv.setOnItemClickListener(this);
      paramBundle = getLayoutInflater().inflate(R.layout.error_item, this.retryLayout, false);
      if ((paramBundle instanceof LoadingErrorView))
        ((LoadingErrorView)paramBundle).setCallBack(new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            MovieSearchResultActivity.this.loadingLayout.setVisibility(0);
            MovieSearchResultActivity.this.sendSearchRequest();
          }
        });
      this.retryLayout.addView(paramBundle);
      this.retryLayout.setVisibility(8);
      sendSearchRequest();
      this.isMovieExpended = false;
      return;
    }
    catch (Exception localException)
    {
      while (true)
        this.scope = 0;
    }
  }

  protected void onDestroy()
  {
    if (this.movieSearchRequest != null)
    {
      mapiService().abort(this.movieSearchRequest, this, true);
      this.movieSearchRequest = null;
    }
    if (this.pageMoreRequest != null)
    {
      mapiService().abort(this.pageMoreRequest, this, true);
      this.pageMoreRequest = null;
    }
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if (DPObjectUtils.isDPObjectof(paramAdapterView, "Cinema"))
    {
      paramAdapterView = (DPObject)paramAdapterView;
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + paramAdapterView.getInt("ID"))));
    }
    do
      return;
    while (!DPObjectUtils.isDPObjectof(paramAdapterView, "MovieOnInfo"));
    paramAdapterView = ((DPObject)paramAdapterView).getObject("Movie");
    startActivity("dianping://moviedetail?movieid=" + paramAdapterView.getInt("ID"));
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    getMyLocation();
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.movieSearchRequest)
      if ((paramMApiResponse != null) && (paramMApiResponse.message() != null))
      {
        this.loadingLayout.setVisibility(8);
        this.contentLayout.setVisibility(8);
        this.retryLayout.setVisibility(0);
        this.movieSearchRequest = null;
      }
    do
    {
      return;
      paramMApiRequest = Toast.makeText(this, "网络不给力哦，请稍后再试", 0);
      paramMApiRequest.setGravity(17, 0, 0);
      paramMApiRequest.show();
      break;
    }
    while (paramMApiRequest != this.pageMoreRequest);
    if ((paramMApiResponse != null) && (paramMApiResponse.message() != null))
    {
      this.errorMsg = paramMApiResponse.message().toString();
      this.mAdapter.notifyDataSetChanged();
    }
    while (true)
    {
      this.pageMoreRequest = null;
      return;
      paramMApiRequest = Toast.makeText(this, "网络不给力哦，请稍后再试", 0);
      paramMApiRequest.setGravity(17, 0, 0);
      paramMApiRequest.show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.movieSearchRequest)
    {
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieSearchResult"))
      {
        this.contentLayout.setVisibility(0);
        this.retryLayout.setVisibility(8);
        paramMApiRequest = (DPObject)paramMApiResponse;
        this.movieListTitle = paramMApiRequest.getString("MovieListTitle");
        this.cinemaListTitle = paramMApiRequest.getString("CinemaListTitle");
        this.targetNotHitTitle = paramMApiRequest.getString("TargetNotHitTitle");
        this.targetIsHit = paramMApiRequest.getBoolean("TargetIsHit");
        fillResultList(paramMApiRequest);
      }
      this.movieSearchRequest = null;
    }
    do
      return;
    while (paramMApiRequest != this.pageMoreRequest);
    if (DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieSearchResult"))
      appendPageSearchResult((DPObject)paramMApiResponse, this.mPosition);
    this.pageMoreRequest = null;
  }

  class SearchAdapter extends BasicAdapter
  {
    SearchAdapter()
    {
    }

    public int getCount()
    {
      if (((MovieSearchResultActivity.this.isMovieExpended) && (!MovieSearchResultActivity.this.isMovieEnd) && (!MovieSearchResultActivity.this.isCinemaAdded)) || (!MovieSearchResultActivity.this.isCinemaEnd))
        return MovieSearchResultActivity.this.resultList.size() + 1;
      return MovieSearchResultActivity.this.resultList.size();
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < MovieSearchResultActivity.this.resultList.size())
        return MovieSearchResultActivity.this.resultList.get(paramInt);
      if (MovieSearchResultActivity.this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public int getItemViewType(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(localObject, "Cinema"))
        return 0;
      if (DPObjectUtils.isDPObjectof(localObject, "MovieOnInfo"))
        return 1;
      if (localObject == MovieSearchResultActivity.MOVIE_REC)
        return 2;
      if (localObject == MovieSearchResultActivity.CINEAM_REC)
        return 3;
      if (localObject == MovieSearchResultActivity.MOVIE_MORE)
        return 4;
      if (localObject == LOADING)
        return 5;
      return 6;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject2 = null;
      Object localObject3 = null;
      Object localObject4 = null;
      Object localObject5 = null;
      Object localObject1 = null;
      Object localObject6 = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(localObject6, "Cinema"))
      {
        if ((paramView instanceof CinemaListItem))
          localObject1 = (CinemaListItem)paramView;
        paramView = (View)localObject1;
        if (localObject1 == null)
        {
          paramView = (CinemaListItem)MovieSearchResultActivity.this.getLayoutInflater().inflate(R.layout.movie_cinema_list_item, paramViewGroup, false);
          paramView.showBottomDividerLine(false);
        }
        if (MovieSearchResultActivity.this.isLocated)
        {
          paramView.setCinema((DPObject)localObject6, MovieSearchResultActivity.this.myLatitude, MovieSearchResultActivity.this.myLongitude, 0);
          return paramView;
        }
        paramView.setCinema((DPObject)localObject6, 0.0D, 0.0D, 0);
        return paramView;
      }
      if (DPObjectUtils.isDPObjectof(localObject6, "MovieOnInfo"))
      {
        localObject1 = localObject2;
        if ((paramView instanceof MovieOnInfoItem))
          localObject1 = (MovieOnInfoItem)paramView;
        paramView = (View)localObject1;
        if (localObject1 == null)
          paramView = (MovieOnInfoItem)MovieSearchResultActivity.this.getLayoutInflater().inflate(R.layout.movie_on_info_item, paramViewGroup, false);
        paramView.setMovieOnInfo((DPObject)localObject6, 1, paramInt);
        return paramView;
      }
      if (localObject6 == MovieSearchResultActivity.MOVIE_REC)
      {
        localObject1 = localObject3;
        if ((paramView instanceof TextView))
          localObject1 = (TextView)paramView;
        paramView = (View)localObject1;
        if (localObject1 == null)
          paramView = (TextView)MovieSearchResultActivity.this.getLayoutInflater().inflate(R.layout.rec_title_layout, paramViewGroup, false);
        paramView.setText(MovieSearchResultActivity.this.movieListTitle);
        return paramView;
      }
      if (localObject6 == MovieSearchResultActivity.CINEAM_REC)
      {
        localObject1 = localObject4;
        if ((paramView instanceof TextView))
          localObject1 = (TextView)paramView;
        paramView = (View)localObject1;
        if (localObject1 == null)
          paramView = (TextView)MovieSearchResultActivity.this.getLayoutInflater().inflate(R.layout.rec_title_layout, paramViewGroup, false);
        paramView.setText(MovieSearchResultActivity.this.cinemaListTitle);
        return paramView;
      }
      if (localObject6 == MovieSearchResultActivity.MOVIE_MORE)
      {
        localObject1 = localObject5;
        if ((paramView instanceof NovaFrameLayout))
          localObject1 = (NovaFrameLayout)paramView;
        paramView = (View)localObject1;
        if (localObject1 == null)
          paramView = (NovaFrameLayout)MovieSearchResultActivity.this.getLayoutInflater().inflate(R.layout.movie_display_more, paramViewGroup, false);
        paramView.setClickable(true);
        ((TextView)paramView.findViewById(R.id.display_more_count)).setText("查看更多影片");
        paramView.findViewById(R.id.moviemore_layout).setVisibility(0);
        paramView.findViewById(R.id.loading_layout).setVisibility(8);
        paramView.setOnClickListener(new MovieSearchResultActivity.SearchAdapter.1(this, paramInt));
        return paramView;
      }
      if (localObject6 == LOADING)
      {
        if (MovieSearchResultActivity.this.errorMsg == null)
        {
          if (!DPObjectUtils.isDPObjectof(getItem(paramInt - 1), "MovieOnInfo"))
            break label491;
          MovieSearchResultActivity.access$302(MovieSearchResultActivity.this, 1);
        }
        while (true)
        {
          MovieSearchResultActivity.this.loadNewPage();
          return getLoadingView(paramViewGroup, paramView);
          label491: MovieSearchResultActivity.access$302(MovieSearchResultActivity.this, 2);
        }
      }
      return (View)getFailedView(MovieSearchResultActivity.this.errorMsg, new MovieSearchResultActivity.SearchAdapter.2(this), paramViewGroup, paramView);
    }

    public int getViewTypeCount()
    {
      return 7;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieSearchResultActivity
 * JD-Core Version:    0.6.0
 */
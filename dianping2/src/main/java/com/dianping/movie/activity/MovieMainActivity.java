package com.dianping.movie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.TitleBar;
import com.dianping.cache.DPCache;
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
import com.dianping.model.UserProfile;
import com.dianping.movie.view.MovieBannerView;
import com.dianping.movie.view.MovieBannerView.OnBannerClickGA;
import com.dianping.movie.view.MovieFloatBadgeButton;
import com.dianping.movie.view.MovieMainSlidesView;
import com.dianping.movie.view.MovieMainToolBarView;
import com.dianping.movie.view.RecommendCinemaListView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import com.dianping.widget.view.GAHelper;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class MovieMainActivity extends MovieBaseActivity
  implements AccountListener, RequestHandler<MApiRequest, MApiResponse>, LocationListener
{
  private static final int LIMIT = 25;
  private static final int MESSAGE_REQUEST_UNREADMSGCOUNT = 1;
  private final int LOGINFLAG_FROM_MYMOVIETICKET = 1;
  private final int LOGINFLAG_FROM_UNKNOWN = 0;
  private MApiRequest bannerRequest;
  private MovieBannerView bannerView;
  private RecommendCinemaListView cinemaListView;
  private MApiRequest cinemaRequest;
  private boolean cinemaRequestReturned = true;
  private Context context;
  private MovieFloatBadgeButton floatButton;
  private MApiRequest homeEntriesRequest;
  private boolean isLocated = false;
  private boolean isPullToRefresh = false;
  private int loginFlag = 0;
  protected Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      while (true)
        try
        {
          switch (paramMessage.what)
          {
          case 1:
            super.handleMessage(paramMessage);
            return;
            MovieMainActivity.this.requestUnReadMsgCount();
            sendEmptyMessageDelayed(1, 60000L);
            continue;
          }
        }
        catch (Exception paramMessage)
        {
          paramMessage.printStackTrace();
          return;
        }
    }
  };
  private MApiRequest movieListRequest;
  private boolean movieListRequestReturned = true;
  private MovieMainSlidesView movieMainSlidesView;
  private int myAccuracy = 0;
  private double myLatitude = 0.0D;
  private double myLongitude = 0.0D;
  private PullToRefreshScrollView scrollReview;
  private MovieMainToolBarView toolBarView;
  private MApiRequest unReadMsgCountRequest;
  private boolean unReadMsgCountRequestReturned = true;

  private void checkPullToRefreshFinished()
  {
    if ((this.cinemaRequestReturned) && (this.movieListRequestReturned) && (this.unReadMsgCountRequestReturned))
      this.scrollReview.onRefreshComplete();
  }

  private List<DPObject> generateMovieMainEntriesObjFromDisk(int paramInt)
  {
    return DPCache.getInstance().getParcelableArray(String.valueOf(paramInt) + Environment.versionCode(), "moviemainentries", 31539600000L, DPObject.CREATOR);
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

  private void requestBanner()
  {
    if (this.bannerRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/moviebannermv.bin?").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    localBuilder.appendQueryParameter("position", "dpapp_movieindex_banner");
    this.bannerRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.bannerRequest, this);
  }

  private void requestCinemaList()
  {
    if (this.cinemaRequest != null)
      return;
    Object localObject = Uri.parse("http://app.movie.dianping.com/recommendcinemalistmv.bin?").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("cityid", String.valueOf(cityId()));
    if (isLogined())
      ((Uri.Builder)localObject).appendQueryParameter("token", accountService().token());
    if (isLocated())
    {
      ((Uri.Builder)localObject).appendQueryParameter("lat", Location.FMT.format(this.myLatitude));
      ((Uri.Builder)localObject).appendQueryParameter("lng", Location.FMT.format(this.myLongitude));
      ((Uri.Builder)localObject).appendQueryParameter("accuracy", String.valueOf(this.myAccuracy));
    }
    if (!TextUtils.isEmpty(this.from))
      ((Uri.Builder)localObject).appendQueryParameter("from", this.from);
    String str = ((Uri.Builder)localObject).toString();
    if (this.isPullToRefresh);
    for (localObject = CacheType.DISABLED; ; localObject = CacheType.NORMAL)
    {
      this.cinemaRequest = BasicMApiRequest.mapiGet(str, (CacheType)localObject);
      mapiService().exec(this.cinemaRequest, this);
      return;
    }
  }

  private void requestHomeEntries()
  {
    if (this.homeEntriesRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/moviehomeentriesmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    this.homeEntriesRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.homeEntriesRequest, this);
  }

  private void requestMovieList()
  {
    if (this.movieListRequest != null)
      return;
    Object localObject = Uri.parse("http://app.movie.dianping.com/movieoninfolistmv.bin?").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("start", String.valueOf(0));
    ((Uri.Builder)localObject).appendQueryParameter("filter", String.valueOf(1));
    ((Uri.Builder)localObject).appendQueryParameter("cityid", String.valueOf(cityId()));
    ((Uri.Builder)localObject).appendQueryParameter("limit", String.valueOf(15));
    ((Uri.Builder)localObject).appendQueryParameter("source", "moviemain");
    String str = ((Uri.Builder)localObject).toString();
    if (this.isPullToRefresh);
    for (localObject = CacheType.DISABLED; ; localObject = CacheType.NORMAL)
    {
      this.movieListRequest = BasicMApiRequest.mapiGet(str, (CacheType)localObject);
      mapiService().exec(this.movieListRequest, this);
      return;
    }
  }

  private void requestUnReadMsgCount()
  {
    if (!isLogined());
    do
      return;
    while (this.unReadMsgCountRequest != null);
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/getmessagecounttokenmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("token", accountService().token());
    this.unReadMsgCountRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.unReadMsgCountRequest, this);
  }

  private void saveMovieMainEntriesObjRawData(DPObject[] paramArrayOfDPObject)
  {
    if (paramArrayOfDPObject == null)
      return;
    DPCache.getInstance().put(String.valueOf(cityId()) + Environment.versionCode(), "moviemainentries", paramArrayOfDPObject, 31539600000L);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public boolean locationCare()
  {
    return true;
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
    if (isLogined())
      this.mHandler.sendEmptyMessage(1);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040003");
    this.context = this;
    setContentView(R.layout.movie_main_activity);
    setTitle("看电影");
    paramBundle = new ImageView(this.context);
    paramBundle.setImageDrawable(getResources().getDrawable(R.drawable.movie_titlebar_icon_search));
    getTitleBar().addRightViewItem(paramBundle, "moviekeyword", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent(MovieMainActivity.this, MovieSearchActivity.class);
        MovieMainActivity.this.startActivity(paramView);
        GAHelper.instance().contextStatisticsEvent(MovieMainActivity.this, "search", null, 0, "tap");
      }
    });
    this.scrollReview = ((PullToRefreshScrollView)findViewById(R.id.moviemain_scrollview));
    this.scrollReview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshBase<ScrollView> paramPullToRefreshBase)
      {
        MovieMainActivity.access$102(MovieMainActivity.this, true);
        MovieMainActivity.this.requestMovieList();
        MovieMainActivity.this.requestCinemaList();
        if (MovieMainActivity.this.isLogined())
          MovieMainActivity.this.requestUnReadMsgCount();
      }
    });
    this.bannerView = ((MovieBannerView)findViewById(R.id.moviemain_bannerview));
    this.bannerView.setVisibility(8);
    this.bannerView.setOnBannerClickGA(new MovieBannerView.OnBannerClickGA()
    {
      public void onBannerClick(DPObject paramDPObject)
      {
        GAHelper.instance().contextStatisticsEvent(MovieMainActivity.this, "banner", paramDPObject.getString("Title"), 0, "tap");
      }
    });
    this.bannerView.setCloseDrawable(R.drawable.banner_close);
    this.bannerView.hideCloseButton();
    this.bannerView.setNavigationDotNormalDrawable(R.drawable.home_serve_dot);
    this.bannerView.setNavigationDotPressedDrawable(R.drawable.home_serve_dot_pressed);
    this.toolBarView = ((MovieMainToolBarView)findViewById(R.id.moviemain_toolbar));
    this.movieMainSlidesView = ((MovieMainSlidesView)findViewById(R.id.moviemain_movieslides));
    this.cinemaListView = ((RecommendCinemaListView)findViewById(R.id.moviemain_cinemalist));
    this.floatButton = ((MovieFloatBadgeButton)findViewById(R.id.mymovie));
    this.floatButton.setVisibility(8);
    getMyLocation();
    requestBanner();
    requestHomeEntries();
  }

  protected void onDestroy()
  {
    if (this.bannerRequest != null)
    {
      mapiService().abort(this.bannerRequest, this, true);
      this.bannerRequest = null;
    }
    if (this.homeEntriesRequest != null)
    {
      mapiService().abort(this.homeEntriesRequest, this, true);
      this.homeEntriesRequest = null;
    }
    if (this.movieListRequest != null)
    {
      mapiService().abort(this.movieListRequest, this, true);
      this.movieListRequest = null;
    }
    if (this.cinemaRequest != null)
    {
      mapiService().abort(this.cinemaRequest, this, true);
      this.cinemaRequest = null;
    }
    if (this.unReadMsgCountRequest != null)
    {
      mapiService().abort(this.unReadMsgCountRequest, this, true);
      this.unReadMsgCountRequest = null;
    }
    this.mHandler.removeMessages(1);
    super.onDestroy();
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    getMyLocation();
  }

  public void onLoginCancel()
  {
    this.loginFlag = 0;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.bannerRequest)
      this.bannerRequest = null;
    do
      while (true)
      {
        if (this.isPullToRefresh)
          checkPullToRefreshFinished();
        return;
        if (paramMApiRequest == this.movieListRequest)
        {
          this.movieMainSlidesView.setVisibility(8);
          this.movieListRequest = null;
          this.movieListRequestReturned = true;
          continue;
        }
        if (paramMApiRequest == this.cinemaRequest)
        {
          this.cinemaRequest = null;
          this.cinemaRequestReturned = true;
          this.cinemaListView.setVisibility(8);
          if (paramMApiResponse.message() == null);
          for (paramMApiRequest = "请求失败，请稍后再试"; ; paramMApiRequest = paramMApiResponse.message().content())
          {
            Toast.makeText(this, paramMApiRequest, 0).show();
            break;
          }
        }
        if (paramMApiRequest != this.unReadMsgCountRequest)
          break;
        this.unReadMsgCountRequest = null;
        this.unReadMsgCountRequestReturned = true;
      }
    while (paramMApiRequest != this.homeEntriesRequest);
    this.homeEntriesRequest = null;
    paramMApiRequest = generateMovieMainEntriesObjFromDisk(cityId());
    this.toolBarView.setButtons(paramMApiRequest);
    if ((paramMApiRequest != null) && (paramMApiRequest.size() == 5))
    {
      this.floatButton.setButtonContent((DPObject)paramMApiRequest.get(4));
      this.floatButton.setVisibility(0);
    }
    while (true)
    {
      this.toolBarView.setVisibility(0);
      requestMovieList();
      requestCinemaList();
      if (!isLogined())
        break;
      this.mHandler.sendEmptyMessage(1);
      break;
      this.floatButton.setVisibility(8);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.bannerRequest)
    {
      if ((paramMApiResponse instanceof DPObject))
      {
        paramMApiRequest = ((DPObject)paramMApiResponse).getArray("List");
        if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
        {
          this.bannerView.setVisibility(0);
          this.bannerView.setBanner(paramMApiRequest);
        }
      }
      this.bannerRequest = null;
    }
    label139: label211: 
    do
      while (true)
      {
        if (this.isPullToRefresh)
          checkPullToRefreshFinished();
        return;
        if (paramMApiRequest == this.movieListRequest)
        {
          if ((paramMApiResponse instanceof DPObject))
          {
            paramMApiRequest = ((DPObject)paramMApiResponse).getArray("List");
            if ((paramMApiRequest == null) || (paramMApiRequest.length == 0))
              break label139;
            this.movieMainSlidesView.setMovieList(paramMApiRequest);
            this.movieMainSlidesView.setVisibility(0);
          }
          while (true)
          {
            this.movieListRequestReturned = true;
            this.movieListRequest = null;
            break;
            this.movieMainSlidesView.setVisibility(8);
          }
        }
        if (paramMApiRequest == this.cinemaRequest)
        {
          if (DPObjectUtils.isDPObjectof(paramMApiResponse, "RecommendCinemaList"))
          {
            paramMApiRequest = ((DPObject)paramMApiResponse).getArray("List");
            if ((paramMApiRequest != null) && (paramMApiRequest.length != 0))
              break label211;
            this.cinemaListView.setVisibility(8);
          }
          while (true)
          {
            this.cinemaRequestReturned = true;
            this.cinemaRequest = null;
            break;
            this.cinemaListView.setCinemas(paramMApiRequest, this.myLatitude, this.myLongitude);
            this.cinemaListView.setVisibility(0);
          }
        }
        if (paramMApiRequest != this.unReadMsgCountRequest)
          break;
        if (DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieMessageCount"))
        {
          paramMApiRequest = ((DPObject)paramMApiResponse).getString("UnReadMsgCount");
          this.toolBarView.updateBadge(paramMApiRequest);
          this.floatButton.updateBadge(paramMApiRequest);
        }
        this.unReadMsgCountRequest = null;
        this.unReadMsgCountRequestReturned = true;
      }
    while (paramMApiRequest != this.homeEntriesRequest);
    this.homeEntriesRequest = null;
    if (DPObjectUtils.isDPObjectof(paramMApiResponse, "CellList"))
    {
      saveMovieMainEntriesObjRawData(((DPObject)paramMApiResponse).getArray("List"));
      paramMApiRequest = Arrays.asList(((DPObject)paramMApiResponse).getArray("List"));
      this.toolBarView.setButtons(paramMApiRequest);
      label355: if ((paramMApiRequest == null) || (paramMApiRequest.size() != 5))
        break label449;
      this.floatButton.setButtonContent((DPObject)paramMApiRequest.get(4));
      this.floatButton.setVisibility(0);
    }
    while (true)
    {
      this.toolBarView.setVisibility(0);
      requestMovieList();
      requestCinemaList();
      if (!isLogined())
        break;
      this.mHandler.sendEmptyMessage(1);
      break;
      paramMApiRequest = generateMovieMainEntriesObjFromDisk(cityId());
      this.toolBarView.setButtons(paramMApiRequest);
      break label355;
      label449: this.floatButton.setVisibility(8);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieMainActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.SimpleMsg;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.movie.view.MovieShowBlockListView;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import org.json.JSONObject;

public class MovieAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_MOVIE = "0400Movie.";
  private static final String URL = "http://app.movie.dianping.com/movieshowblockwithnavimv.bin?";
  private String errorMsg;
  private boolean isRequestFailed;
  private boolean mIsMovieBookingDisabled = true;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.movie.REFRESH_MOVIESHOWSCHEDULE"))
        MovieAgent.this.requestMovieInfo();
    }
  };
  private DPObject mResultObj;
  MApiRequest movieInfoReq;
  private MovieShowBlockListView movieShowBlockListView;
  private int movieid;

  public MovieAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (this.mIsMovieBookingDisabled);
    do
    {
      do
        return;
      while ((getFragment() == null) || (getShopStatus() != 0) || (!getShop().getBoolean("MovieBookable")));
      if (this.isRequestFailed)
      {
        paramBundle = (MovieShowBlockListView)this.res.inflate(getContext(), R.layout.movie_show_block_list_view, getParentView(), false);
        paramBundle.showLoadingRetryView(this.errorMsg, new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            MovieAgent.this.requestMovieInfo();
          }
        });
        addCell("0400Movie.", paramBundle, 0);
        return;
      }
      if (this.mResultObj != null)
        continue;
      requestMovieInfo();
      return;
    }
    while ((this.mResultObj == null) || (this.mResultObj.getArray("MovieNaviList") == null) || (this.mResultObj.getArray("MovieNaviList").length <= 0));
    this.movieShowBlockListView = ((MovieShowBlockListView)this.res.inflate(getContext(), R.layout.movie_show_block_list_view, getParentView(), false));
    MovieShowBlockListView localMovieShowBlockListView = this.movieShowBlockListView;
    DPObject localDPObject1 = this.mResultObj;
    DPObject localDPObject2 = getShop();
    int i = this.movieid;
    if (isLogined());
    for (paramBundle = accountService().token(); ; paramBundle = null)
    {
      localMovieShowBlockListView.initFirstMovieShowBlock(localDPObject1, localDPObject2, 1, null, i, paramBundle, String.valueOf(shopId()), getFragment().mapiService());
      addCell("0400Movie.", this.movieShowBlockListView, "moviebooking", 0);
      return;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = new IntentFilter("com.dianping.movie.REFRESH_MOVIESHOWSCHEDULE");
    getFragment().registerReceiver(this.mReceiver, paramBundle);
    if (getFragment() == null);
    do
    {
      return;
      paramBundle = ConfigHelper.movieBookingConfig;
    }
    while (paramBundle == null);
    try
    {
      this.mIsMovieBookingDisabled = new JSONObject(paramBundle).optBoolean("disableMovieBooking");
      return;
    }
    catch (org.json.JSONException paramBundle)
    {
      this.mIsMovieBookingDisabled = true;
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.movieShowBlockListView != null)
      this.movieShowBlockListView.abortRequest();
    if (this.movieInfoReq != null)
    {
      getFragment().mapiService().abort(this.movieInfoReq, this, true);
      this.movieInfoReq = null;
    }
    if (this.mReceiver != null)
      getFragment().unregisterReceiver(this.mReceiver);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.movieInfoReq = null;
    this.isRequestFailed = true;
    if (paramMApiResponse.message() == null);
    for (paramMApiRequest = "请求失败，请稍后再试"; ; paramMApiRequest = paramMApiResponse.message().content())
    {
      this.errorMsg = paramMApiRequest;
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.movieInfoReq)
    {
      this.isRequestFailed = false;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse.result(), "MovieShowBlockNew"))
      {
        this.mResultObj = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
      this.movieInfoReq = null;
    }
  }

  public void requestMovieInfo()
  {
    if ((getFragment() == null) || (getContext() == null));
    do
      return;
    while (this.movieInfoReq != null);
    int i = shopId();
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/movieshowblockwithnavimv.bin?").buildUpon().appendQueryParameter("shopid", Integer.toString(i));
    this.movieid = ((DPActivity)getContext()).getIntParam("movieid");
    if (this.movieid > 0)
      localBuilder.appendQueryParameter("movieid", Integer.toString(this.movieid));
    if (isLogined())
      localBuilder.appendQueryParameter("token", accountService().token());
    this.movieInfoReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.movieInfoReq, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.MovieAgent
 * JD-Core Version:    0.6.0
 */
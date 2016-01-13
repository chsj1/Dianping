package com.dianping.movie.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.cache.DPCache;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.movie.view.MovieMineEntriesView;
import com.dianping.movie.view.MovieUserBehaviorFeedsItem;
import com.dianping.movie.view.MovieUserProfileView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.util.Arrays;
import java.util.List;

public class MyMovieActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AbsListView.OnScrollListener, PullToRefreshListView.OnRefreshListener
{
  private MovieUserBehaviorAdapter adapter;
  private View bottomUpIcon;
  private MovieMineEntriesView entriesView;
  private boolean firstLoad = true;
  private LinearLayout headerLayer;
  private PullToRefreshListView listView;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("movie:movieuser_profile_modified"))
        MyMovieActivity.this.loadMovieUserProfile();
    }
  };
  private MApiRequest mineEntriesRequest;
  private MApiRequest movieUserRequest;
  private View seperatorBetweenEntryAndFeeds;
  private MApiRequest unReadMsgCountRequest;
  private MovieUserProfileView userProfileView;

  private List<DPObject> generateMovieMineEntriesObjFromDisk(int paramInt)
  {
    return DPCache.getInstance().getParcelableArray(String.valueOf(paramInt) + Environment.versionCode(), "moviemineentries", 31539600000L, DPObject.CREATOR);
  }

  private void loadMovieMineEntries()
  {
    if (this.mineEntriesRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/movieuserentriesmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    localBuilder.appendQueryParameter("token", accountService().token());
    this.mineEntriesRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.mineEntriesRequest, this);
  }

  private void loadMovieUserProfile()
  {
    if (this.movieUserRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/movieusermv.bin?").buildUpon();
    localBuilder.appendQueryParameter("token", accountService().token());
    this.movieUserRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.movieUserRequest, this);
  }

  private void requestUnReadMsgCount()
  {
    if (this.unReadMsgCountRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/getmessagecounttokenmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("token", accountService().token());
    this.unReadMsgCountRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.unReadMsgCountRequest, this);
  }

  private void saveMovieMineEntriesObjRawData(DPObject[] paramArrayOfDPObject)
  {
    if (paramArrayOfDPObject == null)
      return;
    DPCache.getInstance().put(String.valueOf(cityId()) + Environment.versionCode(), "moviemineentries", paramArrayOfDPObject, 31539600000L);
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040005");
    setContentView(R.layout.movie_mine_activity);
    this.listView = ((PullToRefreshListView)findViewById(R.id.feeds_list));
    this.headerLayer = ((LinearLayout)LayoutInflater.from(this).inflate(R.layout.movie_mine_header_view, this.listView, false));
    this.listView.addHeaderView(this.headerLayer);
    this.listView.setOnScrollListener(this);
    this.userProfileView = ((MovieUserProfileView)this.headerLayer.findViewById(R.id.profile));
    this.entriesView = ((MovieMineEntriesView)this.headerLayer.findViewById(R.id.movie_mine_entries));
    this.seperatorBetweenEntryAndFeeds = this.headerLayer.findViewById(R.id.separator_bewteen_entries_and_feeds);
    this.seperatorBetweenEntryAndFeeds.setVisibility(8);
    this.adapter = new MovieUserBehaviorAdapter(this);
    this.listView.setAdapter(this.adapter);
    this.listView.setOnRefreshListener(this);
    this.bottomUpIcon = findViewById(R.id.icon_bottom_up);
    this.bottomUpIcon.setVisibility(8);
    this.bottomUpIcon.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MyMovieActivity.this.adapter.notifyDataSetChanged();
        MyMovieActivity.this.listView.setSelection(0);
        MyMovieActivity.this.bottomUpIcon.setVisibility(8);
      }
    });
    loadMovieUserProfile();
    loadMovieMineEntries();
    paramBundle = new IntentFilter("movie:movieuser_profile_modified");
    registerReceiver(this.mReceiver, paramBundle);
  }

  public void onDestroy()
  {
    if (this.movieUserRequest != null)
    {
      mapiService().abort(this.movieUserRequest, this, true);
      this.movieUserRequest = null;
    }
    if (this.mineEntriesRequest != null)
    {
      mapiService().abort(this.mineEntriesRequest, this, true);
      this.mineEntriesRequest = null;
    }
    if (this.unReadMsgCountRequest != null)
    {
      mapiService().abort(this.unReadMsgCountRequest, this, true);
      this.unReadMsgCountRequest = null;
    }
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    super.onDestroy();
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    if (paramPullToRefreshListView.getId() == R.id.feeds_list)
    {
      this.adapter.pullToReset(true);
      requestUnReadMsgCount();
      loadMovieUserProfile();
      this.firstLoad = false;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.movieUserRequest)
    {
      this.movieUserRequest = null;
      if (this.firstLoad)
        this.userProfileView.setVisibility(8);
    }
    do
    {
      return;
      if (paramMApiRequest != this.mineEntriesRequest)
        continue;
      this.mineEntriesRequest = null;
      this.entriesView.setEntries(generateMovieMineEntriesObjFromDisk(cityId()));
      this.entriesView.setVisibility(0);
      requestUnReadMsgCount();
      this.seperatorBetweenEntryAndFeeds.setVisibility(0);
      return;
    }
    while (paramMApiRequest != this.unReadMsgCountRequest);
    this.unReadMsgCountRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.mineEntriesRequest)
    {
      this.mineEntriesRequest = null;
      this.entriesView.removeAllViews();
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "CellList"))
      {
        saveMovieMineEntriesObjRawData(((DPObject)paramMApiResponse).getArray("List"));
        this.entriesView.setEntries(Arrays.asList(((DPObject)paramMApiResponse).getArray("List")));
        this.entriesView.setVisibility(0);
        this.seperatorBetweenEntryAndFeeds.setVisibility(0);
        requestUnReadMsgCount();
      }
    }
    do
    {
      return;
      this.entriesView.setEntries(generateMovieMineEntriesObjFromDisk(cityId()));
      break;
      if (paramMApiRequest != this.movieUserRequest)
        continue;
      this.movieUserRequest = null;
      this.userProfileView.setMovieUser((DPObject)paramMApiResponse);
      this.userProfileView.setVisibility(0);
      return;
    }
    while (paramMApiRequest != this.unReadMsgCountRequest);
    if (DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieMessageCount"))
    {
      paramMApiRequest = ((DPObject)paramMApiResponse).getString("UnReadMsgCount");
      this.entriesView.updateBadge(paramMApiRequest);
    }
    this.unReadMsgCountRequest = null;
  }

  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 > this.listView.getHeaderViewsCount() - 1)
    {
      this.bottomUpIcon.setVisibility(0);
      return;
    }
    this.bottomUpIcon.setVisibility(8);
  }

  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
  }

  class MovieUserBehaviorAdapter extends BasicLoadAdapter
  {
    private Context context;

    public MovieUserBehaviorAdapter(Context arg2)
    {
      super();
      this.context = localContext;
    }

    public MApiRequest createRequest(int paramInt)
    {
      Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/movieuserbehaviorlistmv.bin?").buildUpon();
      localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
      localBuilder.appendQueryParameter("limit", String.valueOf(25));
      localBuilder.appendQueryParameter("token", MyMovieActivity.this.accountService().token());
      localBuilder.appendQueryParameter("cityid", String.valueOf(MyMovieActivity.this.cityId()));
      return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    }

    protected TextView getEmptyView(String paramString1, String paramString2, ViewGroup paramViewGroup, View paramView)
    {
      paramString1 = null;
      if (paramView == null);
      while (true)
      {
        paramString2 = paramString1;
        if (paramString1 == null)
        {
          paramString2 = (TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.simple_list_item_18, paramViewGroup, false);
          paramString2.setTag(EMPTY);
        }
        return paramString2;
        if (paramView.getTag() != EMPTY)
          continue;
        paramString1 = (TextView)paramView;
      }
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramDPObject = null;
      Object localObject1 = null;
      Object localObject2 = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(localObject2, "MovieUserBehavior"))
      {
        paramDPObject = localObject1;
        if ((paramView instanceof MovieUserBehaviorFeedsItem))
          paramDPObject = (MovieUserBehaviorFeedsItem)paramView;
        paramView = paramDPObject;
        if (paramDPObject == null)
          paramView = (MovieUserBehaviorFeedsItem)LayoutInflater.from(this.context).inflate(R.layout.movie_userbehavior_feeds_item, paramViewGroup, false);
        paramView.setUserBehavior((DPObject)localObject2, paramInt);
        paramDPObject = paramView;
      }
      return paramDPObject;
    }

    protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      MyMovieActivity.this.listView.onRefreshComplete();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MyMovieActivity
 * JD-Core Version:    0.6.0
 */
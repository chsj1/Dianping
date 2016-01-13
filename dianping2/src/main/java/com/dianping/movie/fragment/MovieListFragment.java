package com.dianping.movie.fragment;

import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.movie.view.MovieBannerView;
import com.dianping.movie.view.MovieOnInfoItem;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends NovaFragment
  implements AdapterView.OnItemClickListener, PullToRefreshListView.OnRefreshListener, RequestHandler<MApiRequest, MApiResponse>
{
  private MApiRequest bannerRequest;
  private MovieBannerView bannerView;
  private FrameLayout emptyView;
  private int filter;
  private String from;
  private View headerView;
  private MApiRequest hotMovieRequest;
  private boolean isPullToRefresh;
  private View loadingLayout;
  private List<Integer> mostAttractiveMovieIds = new ArrayList();
  private NovaLinearLayout mostAttractiveMovies;
  private MovieListFragment.MovieOnInfoAdapter movieOnInfoAdapter;
  private PullToRefreshListView movieOnInfoListView;

  private View createDynamicMovieItem(DPObject paramDPObject, int paramInt)
  {
    MovieOnInfoItem localMovieOnInfoItem = (MovieOnInfoItem)LayoutInflater.from(getActivity()).inflate(R.layout.movie_on_info_item, this.mostAttractiveMovies, false);
    localMovieOnInfoItem.setDynamicMovie(paramDPObject, paramInt);
    localMovieOnInfoItem.setOnClickListener(new MovieListFragment.3(this, paramDPObject, paramInt));
    return localMovieOnInfoItem;
  }

  private void requestBanner()
  {
    if (this.bannerRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/newmoviebannermv.bin?").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    localBuilder.appendQueryParameter("source", "movielist_" + this.filter);
    this.bannerRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.bannerRequest, this);
  }

  private void requestMostAttractiveUpComingMovie()
  {
    if (this.hotMovieRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/hotmovielistmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    localBuilder.appendQueryParameter("from", this.from);
    this.hotMovieRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.hotMovieRequest, this);
  }

  public boolean getAllowReturnTransitionOverlap()
  {
    return super.getAllowReturnTransitionOverlap();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    paramBundle = getArguments();
    this.mostAttractiveMovieIds.clear();
    this.isPullToRefresh = false;
    if (paramBundle != null)
    {
      this.filter = paramBundle.getInt("filter");
      this.from = paramBundle.getString("from");
    }
    this.movieOnInfoAdapter = new MovieListFragment.MovieOnInfoAdapter(this, getActivity());
    if (this.filter == 2)
    {
      this.loadingLayout.setVisibility(0);
      requestMostAttractiveUpComingMovie();
    }
    while (true)
    {
      requestBanner();
      return;
      this.movieOnInfoListView.setAdapter(this.movieOnInfoAdapter);
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(R.layout.movie_list_fragment, paramViewGroup, false);
  }

  public void onDestroyView()
  {
    super.onDestroyView();
    if (this.bannerRequest != null)
    {
      mapiService().abort(this.bannerRequest, this, true);
      this.bannerRequest = null;
    }
    if (this.hotMovieRequest != null)
    {
      mapiService().abort(this.hotMovieRequest, this, true);
      this.hotMovieRequest = null;
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramInt > 0)
    {
      paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
      if (DPObjectUtils.isDPObjectof(paramAdapterView, "MovieOnInfo"))
      {
        paramAdapterView = ((DPObject)paramAdapterView).getObject("Movie");
        startActivity("dianping://moviedetail?movieid=" + paramAdapterView.getInt("ID"));
      }
    }
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    if (paramPullToRefreshListView.getId() == R.id.movieoninfo_list)
    {
      this.isPullToRefresh = true;
      if (this.filter == 2)
      {
        this.mostAttractiveMovieIds.clear();
        requestMostAttractiveUpComingMovie();
      }
    }
    else
    {
      return;
    }
    this.movieOnInfoAdapter.pullToReset(true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.bannerRequest)
      this.bannerRequest = null;
    do
      return;
    while (paramMApiRequest != this.hotMovieRequest);
    if (this.isPullToRefresh)
      this.movieOnInfoAdapter.pullToReset(true);
    while (true)
    {
      this.isPullToRefresh = false;
      this.hotMovieRequest = null;
      return;
      this.loadingLayout.setVisibility(8);
      this.movieOnInfoListView.setAdapter(this.movieOnInfoAdapter);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.bannerRequest)
    {
      if ((paramMApiResponse instanceof DPObject))
      {
        paramMApiRequest = (ViewStub)this.headerView.findViewById(R.id.banner_stub);
        if (paramMApiRequest != null)
          paramMApiRequest.inflate();
        this.bannerView = ((MovieBannerView)this.headerView.findViewById(R.id.movie_banner_view));
        this.bannerView.setBtnOnCloseListener(new MovieListFragment.1(this));
        this.bannerView.setOnBannerClickGA(new MovieListFragment.2(this));
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
    do
      return;
    while (paramMApiRequest != this.hotMovieRequest);
    if (DPObjectUtils.isDPObjectof(paramMApiResponse, "DynamicMovieList"))
    {
      paramMApiRequest = (ViewStub)this.headerView.findViewById(R.id.mostattractive_movie_stub);
      if (paramMApiRequest != null)
        paramMApiRequest.inflate();
      this.mostAttractiveMovies = ((NovaLinearLayout)this.headerView.findViewById(R.id.mostattractive_movies_view).findViewById(R.id.most_attractive_movies));
      paramMApiRequest = ((DPObject)paramMApiResponse).getArray("List");
      if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
      {
        this.mostAttractiveMovies.removeAllViews();
        int i = 0;
        while (i < paramMApiRequest.length)
        {
          this.mostAttractiveMovieIds.add(Integer.valueOf(paramMApiRequest[i].getInt("ID")));
          this.mostAttractiveMovies.addView(createDynamicMovieItem(paramMApiRequest[i], i));
          if (i != paramMApiRequest.length - 1)
          {
            paramMApiResponse = new View(getActivity());
            LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
            localLayoutParams.setMargins(ViewUtils.dip2px(getActivity(), 10.0F), 0, 0, 0);
            paramMApiResponse.setLayoutParams(localLayoutParams);
            paramMApiResponse.setBackgroundColor(getResources().getColor(R.color.wm_border_gray));
            this.mostAttractiveMovies.addView(paramMApiResponse);
          }
          i += 1;
        }
        this.mostAttractiveMovies.setVisibility(0);
      }
    }
    else
    {
      if (!this.isPullToRefresh)
        break label442;
      this.movieOnInfoAdapter.pullToReset(true);
    }
    while (true)
    {
      this.hotMovieRequest = null;
      this.isPullToRefresh = false;
      return;
      this.mostAttractiveMovies.setVisibility(8);
      break;
      label442: this.loadingLayout.setVisibility(8);
      this.movieOnInfoListView.setAdapter(this.movieOnInfoAdapter);
    }
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.movieOnInfoListView = ((PullToRefreshListView)paramView.findViewById(R.id.movieoninfo_list));
    this.loadingLayout = paramView.findViewById(R.id.loading_layout);
    this.headerView = LayoutInflater.from(getActivity()).inflate(R.layout.movie_list_header, this.movieOnInfoListView, false);
    this.movieOnInfoListView.addHeaderView(this.headerView);
    this.emptyView = ((FrameLayout)paramView.findViewById(R.id.movieoninfo_empty));
    this.movieOnInfoListView.setEmptyView(this.emptyView);
    this.movieOnInfoListView.setOnItemClickListener(this);
    this.movieOnInfoListView.setOnRefreshListener(this);
    this.movieOnInfoListView.setHeaderDividersEnabled(false);
    this.movieOnInfoListView.setDivider(getResources().getDrawable(R.drawable.moviechannel_listview_divider_right_inset));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.fragment.MovieListFragment
 * JD-Core Version:    0.6.0
 */
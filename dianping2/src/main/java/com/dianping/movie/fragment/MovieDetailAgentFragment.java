package com.dianping.movie.fragment;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.ToolbarButton;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.movie.config.MovieDetailAgentListConfig;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.view.NovaTextView;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MovieDetailAgentFragment extends GroupAgentFragment
  implements LoginResultListener, AccountListener, RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final int LOGIN_STEP_COMMENT = 2;
  private static final int LOGIN_STEP_WISH = 1;
  public MApiRequest addWishMovieRequest;
  private TextView allReviewTitle;
  private String allReviewUrl;
  private View allReviewsLayer;
  private ToolbarButton commentBtn;
  public ViewGroup contentView;
  public DPObject dpMovie;
  public PullToRefreshListView listView;
  private View loadingLayout;
  private int loginStep = 0;
  public View mFragmentView;
  public MApiRequest movieDetailRequest;
  public int movieId;
  private boolean movieWished = false;
  public MApiRequest movieWishedOrNotRequest;
  public MApiRequest removeWishMovieRequest;
  private LinearLayout retryLayout;
  private MovieDetailAgentFragment.ReviewAdapter reviewAdapter;
  private View seperatedLine;
  private View shareView;
  private NovaTextView ticketBook;
  private ViewGroup titleBar;
  private TextView titleTv;
  private ToolbarButton wishBtn;

  private void gotoAddComment()
  {
    String str = "http://m.dianping.com/movie/h5/addcomment?movieId=" + this.movieId + "&version=*&token=!&dpshare=0";
    try
    {
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + URLEncoder.encode(str, "UTF-8"))));
      return;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
  }

  private void sendAddWishMovieRequest()
  {
    if (this.addWishMovieRequest != null)
      return;
    this.addWishMovieRequest = BasicMApiRequest.mapiPost("http://app.movie.dianping.com/addwishmoviemv.bin", new String[] { "token", accountService().token(), "movieid", "" + this.movieId });
    mapiService().exec(this.addWishMovieRequest, this);
  }

  private void sendMovieDetailRequest()
  {
    if (this.movieDetailRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/moviedetailmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("id", String.valueOf(this.movieId));
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    this.movieDetailRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.movieDetailRequest, this);
  }

  private void sendMovieWishedOrNotRequest()
  {
    if (this.movieWishedOrNotRequest != null);
    do
      return;
    while (!((NovaActivity)getActivity()).isLogined());
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/moviewishedornotmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("token", accountService().token());
    localBuilder.appendQueryParameter("movieid", String.valueOf(this.movieId));
    this.movieWishedOrNotRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.movieWishedOrNotRequest, this);
  }

  private void sendRemoveWishMovieRequest()
  {
    if (this.removeWishMovieRequest != null)
      return;
    this.removeWishMovieRequest = BasicMApiRequest.mapiPost("http://app.movie.dianping.com/removewishmoviemv.bin", new String[] { "token", accountService().token(), "movieids", "" + this.movieId });
    mapiService().exec(this.removeWishMovieRequest, this);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new MovieDetailAgentListConfig());
    return localArrayList;
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    if (paramAccountService.profile() == null)
    {
      paramAccountService = new Bundle();
      paramAccountService.putInt("loginEventType", 2);
      dispatchAgentChanged(null, paramAccountService);
      return;
    }
    paramAccountService = new Bundle();
    paramAccountService.putInt("loginEventType", 1);
    dispatchAgentChanged(null, paramAccountService);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    sendMovieDetailRequest();
    sendMovieWishedOrNotRequest();
    super.onActivityCreated(paramBundle);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.collect)
      if (((NovaActivity)getActivity()).isLogined())
        if (this.movieWished)
          sendRemoveWishMovieRequest();
    do
    {
      return;
      sendAddWishMovieRequest();
      return;
      accountService().login(this);
      this.loginStep = 1;
      return;
      if (i == R.id.addcomment)
      {
        if (((NovaActivity)getActivity()).isLogined())
        {
          gotoAddComment();
          return;
        }
        accountService().login(this);
        this.loginStep = 2;
        return;
      }
      if (i != R.id.buyticket)
        continue;
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://cinemalist?movieid=" + this.movieId + "&ishiddenheaderview=1")));
      return;
    }
    while (i != R.id.share);
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.title = this.dpMovie.getString("Name");
    localShareHolder.imageUrl = this.dpMovie.getString("Image");
    localShareHolder.webUrl = ("http://m.dianping.com/movie/h5/moviedetail?movieid=" + this.movieId + "&from=share");
    StringBuilder localStringBuilder = new StringBuilder().append(this.dpMovie.getString("Grade")).append("分，");
    if (this.dpMovie.getString("Msg") == null);
    for (paramView = "一起来看吧"; ; paramView = this.dpMovie.getString("Msg"))
    {
      localShareHolder.desc = paramView;
      ShareUtil.gotoShareTo(getActivity(), ShareType.WEB, localShareHolder, "", "", 55);
      return;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.movieId = getIntParam("movieid");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mFragmentView = paramLayoutInflater.inflate(R.layout.movie_detail_layout, null, false);
    this.listView = ((PullToRefreshListView)this.mFragmentView.findViewById(R.id.review_list_view));
    this.listView.setDivider(null);
    paramLayoutInflater = LayoutInflater.from(getActivity()).inflate(R.layout.movie_detail_listheader, this.listView, false);
    this.allReviewTitle = ((TextView)paramLayoutInflater.findViewById(R.id.allreview_title));
    this.allReviewsLayer = paramLayoutInflater.findViewById(R.id.allreviews);
    this.seperatedLine = paramLayoutInflater.findViewById(R.id.seperated_line);
    this.contentView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.content));
    this.wishBtn = ((ToolbarButton)this.mFragmentView.findViewById(R.id.collect));
    this.commentBtn = ((ToolbarButton)this.mFragmentView.findViewById(R.id.addcomment));
    this.shareView = this.mFragmentView.findViewById(R.id.share);
    this.ticketBook = ((NovaTextView)this.mFragmentView.findViewById(R.id.buyticket));
    this.titleTv = ((TextView)this.mFragmentView.findViewById(R.id.title_bar_title));
    this.wishBtn.setOnClickListener(this);
    this.commentBtn.setOnClickListener(this);
    this.ticketBook.setOnClickListener(this);
    this.listView.addHeaderView(paramLayoutInflater);
    this.listView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.titleBar = ((ViewGroup)this.mFragmentView.findViewById(R.id.titlebar));
    this.loadingLayout = this.mFragmentView.findViewById(R.id.status);
    this.retryLayout = ((LinearLayout)this.mFragmentView.findViewById(R.id.loading_retry_layer));
    paramLayoutInflater = LayoutInflater.from(getActivity()).inflate(R.layout.error_item, this.retryLayout, false);
    if ((paramLayoutInflater instanceof LoadingErrorView))
      ((LoadingErrorView)paramLayoutInflater).setCallBack(new MovieDetailAgentFragment.1(this));
    this.retryLayout.addView(paramLayoutInflater);
    this.retryLayout.setVisibility(8);
    this.titleBar.findViewById(R.id.left_view).setOnClickListener(new MovieDetailAgentFragment.2(this));
    setAgentContainerView(this.contentView);
    this.reviewAdapter = new MovieDetailAgentFragment.ReviewAdapter(this, getActivity());
    return this.mFragmentView;
  }

  public void onDestroy()
  {
    if (this.movieDetailRequest != null)
    {
      mapiService().abort(this.movieDetailRequest, this, true);
      this.movieDetailRequest = null;
    }
    if (this.movieWishedOrNotRequest != null)
    {
      mapiService().abort(this.movieWishedOrNotRequest, this, true);
      this.movieWishedOrNotRequest = null;
    }
    if (this.addWishMovieRequest != null)
    {
      mapiService().abort(this.addWishMovieRequest, this, true);
      this.addWishMovieRequest = null;
    }
    if (this.removeWishMovieRequest != null)
    {
      mapiService().abort(this.removeWishMovieRequest, this, true);
      this.removeWishMovieRequest = null;
    }
    super.onDestroy();
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    if (this.loginStep == 1)
      sendAddWishMovieRequest();
    do
      return;
    while (this.loginStep != 2);
    gotoAddComment();
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.movieDetailRequest)
    {
      this.movieDetailRequest = null;
      this.loadingLayout.setVisibility(8);
      this.retryLayout.setVisibility(0);
    }
    do
    {
      do
        while (true)
        {
          return;
          if (paramMApiRequest == this.movieWishedOrNotRequest)
          {
            this.movieWishedOrNotRequest = null;
            return;
          }
          if (paramMApiRequest != this.addWishMovieRequest)
            break;
          this.wishBtn.setIcon(getResources().getDrawable(R.drawable.movie_collection_gray));
          this.movieWished = false;
          this.addWishMovieRequest = null;
          if (paramMApiResponse == null)
            continue;
          Toast.makeText(getActivity(), paramMApiResponse.content(), 0).show();
          return;
        }
      while (paramMApiRequest != this.removeWishMovieRequest);
      this.wishBtn.setIcon(getResources().getDrawable(R.drawable.movie_collection_red));
      this.movieWished = true;
      this.removeWishMovieRequest = null;
    }
    while (paramMApiResponse == null);
    Toast.makeText(getActivity(), paramMApiResponse.content(), 0).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.movieDetailRequest)
    {
      int i;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "Movie"))
      {
        this.dpMovie = ((DPObject)paramMApiResponse);
        this.shareView.setVisibility(0);
        this.shareView.setOnClickListener(this);
        this.loadingLayout.setVisibility(8);
        this.retryLayout.setVisibility(8);
        resetAgents(null);
        this.listView.setAdapter(this.reviewAdapter);
        i = this.dpMovie.getInt("BuyTicketButtonStatus");
        paramMApiResponse = this.dpMovie.getString("BuyTicketButtonText");
        paramMApiRequest = paramMApiResponse;
        if (TextUtils.isEmpty(paramMApiResponse))
          paramMApiRequest = "选座购票";
        this.ticketBook.setText(paramMApiRequest);
      }
      switch (i)
      {
      default:
        this.titleTv.setText(this.dpMovie.getString("Name"));
        this.movieDetailRequest = null;
      case 0:
      }
    }
    label246: 
    do
    {
      return;
      this.ticketBook.setClickable(false);
      this.ticketBook.setEnabled(false);
      break;
      if (paramMApiRequest == this.movieWishedOrNotRequest)
      {
        if (DPObjectUtils.isDPObjectof(paramMApiResponse, "SimpleMsg"))
        {
          if (1 != ((DPObject)paramMApiResponse).getInt("Flag"))
            break label246;
          this.wishBtn.setIcon(getResources().getDrawable(R.drawable.movie_collection_red));
        }
        for (this.movieWished = true; ; this.movieWished = false)
        {
          this.movieWishedOrNotRequest = null;
          return;
        }
      }
      if (paramMApiRequest != this.addWishMovieRequest)
        continue;
      this.wishBtn.setIcon(getResources().getDrawable(R.drawable.movie_collection_red));
      this.movieWished = true;
      this.addWishMovieRequest = null;
      return;
    }
    while (paramMApiRequest != this.removeWishMovieRequest);
    this.wishBtn.setIcon(getResources().getDrawable(R.drawable.movie_collection_gray));
    this.movieWished = false;
    this.removeWishMovieRequest = null;
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.fragment.MovieDetailAgentFragment
 * JD-Core Version:    0.6.0
 */
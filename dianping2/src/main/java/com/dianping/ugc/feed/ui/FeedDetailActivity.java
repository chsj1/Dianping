package com.dianping.ugc.feed.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.ugc.feed.model.FeedItem;
import com.dianping.ugc.feed.model.FeedUser;
import com.dianping.ugc.feed.view.FeedItemView;
import com.dianping.ugc.feed.view.FeedItemView.OnCommentListener;
import com.dianping.ugc.widget.CommentInputView;
import com.dianping.ugc.widget.CommentInputView.OnCommentSendListener;
import com.dianping.ugc.widget.CommentInputView.OnViewRemovedListener;
import com.dianping.ugc.widget.FeedGridPhotoView.Style;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;

public class FeedDetailActivity extends NovaActivity
  implements FullRequestHandle<MApiRequest, MApiResponse>
{
  private static final int FEED_TYPE_CHECKIN = 3;
  private static final int FEED_TYPE_INVALID = -1;
  private static final int FEED_TYPE_REVIEW = 1;
  private static final int FEED_TYPE_SHOPPHOTO = 2;
  static final String TAG = "FeedDetailActivity";
  private MApiRequest getFeedDetailRequest;
  private CommentInputView mCommentInputView;
  private int mCommentViewMarginBottom = -1;
  private TextView mEmptyView;
  private View mErrorView;
  private String mFeedId;
  private FeedItemView mFeedItemView;
  private int mFeedType = -1;
  private boolean mIsCommiting = true;
  private View mLoadingView;
  private final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.REVIEWDELETE".equals(paramIntent.getAction()))
      {
        paramContext = paramIntent.getStringExtra("feedId");
        if ((FeedDetailActivity.this.mFeedId != null) && (FeedDetailActivity.this.mFeedId.equals(paramContext)))
          FeedDetailActivity.this.finish();
      }
    }
  };
  private FrameLayout mRoot;
  private ScrollView mScrollView;

  private void initViews(Bundle paramBundle)
  {
    setContentView(R.layout.activity_feed_detail_layout);
    this.mRoot = ((FrameLayout)findViewById(R.id.feed_layout));
    this.mLoadingView = findViewById(R.id.feed_loading_layout);
    this.mErrorView = findViewById(R.id.feed_error_layout);
    this.mErrorView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        FeedDetailActivity.this.requestFeedDetail();
      }
    });
    this.mRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        Rect localRect = new Rect();
        FeedDetailActivity.this.mRoot.getWindowVisibleDisplayFrame(localRect);
        if ((FeedDetailActivity.this.mRoot.getRootView().getHeight() > localRect.bottom) && (FeedDetailActivity.this.mScrollView != null) && (FeedDetailActivity.this.mCommentViewMarginBottom != -1))
        {
          int i = ViewUtils.dip2px(FeedDetailActivity.this, 48.0F);
          int j = FeedDetailActivity.this.mCommentViewMarginBottom;
          int k = localRect.bottom;
          FeedDetailActivity.this.mScrollView.smoothScrollBy(0, j - k + i);
          FeedDetailActivity.access$402(FeedDetailActivity.this, -1);
        }
      }
    });
    setFeedView(null);
    if (this.mFeedType == 3)
      setTitle(getString(R.string.ugc_checkindetail));
    do
    {
      return;
      if (this.mFeedType != 1)
        continue;
      setTitle(getString(R.string.ugc_reviewdetail));
      return;
    }
    while (this.mFeedType != 2);
    setTitle(getString(R.string.ugc_shopphotodetail));
  }

  private void processParams()
  {
    this.mFeedType = getIntParam("type", -1);
    this.mFeedId = getStringParam("id");
    this.mIsCommiting = getBooleanParam("commit");
  }

  private void requestFeedDetail()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/getfeeddetail.bin").buildUpon();
    localBuilder.appendQueryParameter("feedtype", String.valueOf(this.mFeedType));
    localBuilder.appendQueryParameter("mainid", this.mFeedId);
    if (location() != null)
    {
      localBuilder.appendQueryParameter("lat", String.valueOf(location().latitude()));
      localBuilder.appendQueryParameter("lng", String.valueOf(location().longitude()));
    }
    this.getFeedDetailRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.getFeedDetailRequest, this);
  }

  private void setFeedView(FeedItem paramFeedItem)
  {
    if (this.mFeedItemView == null)
    {
      this.mScrollView = new ScrollView(this);
      FrameLayout.LayoutParams localLayoutParams1 = new FrameLayout.LayoutParams(-1, -1);
      this.mScrollView.setBackgroundColor(getResources().getColor(R.color.white));
      FrameLayout.LayoutParams localLayoutParams2 = new FrameLayout.LayoutParams(-1, -1);
      this.mCommentInputView = new CommentInputView(this);
      this.mCommentInputView.setEnableRemoveIsSelf(false);
      this.mCommentInputView.setOnViewRemovedListener(new CommentInputView.OnViewRemovedListener()
      {
        public void OnViewRemoved()
        {
          ((FrameLayout.LayoutParams)FeedDetailActivity.this.mScrollView.getLayoutParams()).bottomMargin = 0;
        }
      });
      this.mCommentInputView.setOnCommentInputListener(new CommentInputView.OnCommentSendListener()
      {
        public void onCommentSend(String paramString)
        {
          FeedDetailActivity.access$402(FeedDetailActivity.this, -1);
          FeedDetailActivity.this.mFeedItemView.addComment(FeedDetailActivity.this.mFeedId, null, null, null, paramString);
          FeedDetailActivity.this.mScrollView.fullScroll(130);
        }
      });
      if (this.mIsCommiting)
        this.mCommentInputView.setRequestFocus();
      this.mFeedItemView = ((FeedItemView)LayoutInflater.from(this).inflate(R.layout.ugc_reviewlist_item, null, false));
      this.mFeedItemView.enableCommentSummary(false);
      this.mFeedItemView.enableExtraOpts(true);
      this.mFeedItemView.enableExpandContent(false);
      this.mFeedItemView.setContentMaxLines(2147483647);
      this.mFeedItemView.enableFullCommentList(true);
      this.mFeedItemView.setPhotoStyle(FeedGridPhotoView.Style.SQUARED);
      this.mFeedItemView.setOnCommentListener(new FeedItemView.OnCommentListener()
      {
        public void onComment(int paramInt, View paramView, String paramString1, String paramString2, String paramString3, FeedUser paramFeedUser)
        {
          Object localObject = new int[2];
          paramView.getLocationOnScreen(localObject);
          FeedDetailActivity.access$402(FeedDetailActivity.this, localObject[1] + paramView.getHeight());
          FeedDetailActivity.this.mCommentInputView.setRequestFocus();
          localObject = FeedDetailActivity.this.mCommentInputView;
          if (paramFeedUser == null);
          for (paramView = ""; ; paramView = "回复" + paramFeedUser.username)
          {
            ((CommentInputView)localObject).setCommentInputHint(paramView);
            FeedDetailActivity.this.mCommentInputView.setOnCommentInputListener(new FeedDetailActivity.6.1(this, paramString1, paramString2, paramString3, paramFeedUser));
            return;
          }
        }
      });
      this.mScrollView.addView(this.mFeedItemView, localLayoutParams2);
      localLayoutParams1.bottomMargin = ViewUtils.dip2px(this, 48.0F);
      this.mRoot.addView(this.mScrollView, localLayoutParams1);
      this.mRoot.addView(this.mCommentInputView);
    }
    if (paramFeedItem != null)
    {
      this.mFeedItemView.setData(paramFeedItem);
      this.mScrollView.setVisibility(0);
      this.mCommentInputView.setVisibility(0);
      return;
    }
    this.mScrollView.setVisibility(4);
    this.mCommentInputView.setVisibility(4);
  }

  private void showEmpty(boolean paramBoolean)
  {
    if (this.mEmptyView == null)
    {
      this.mEmptyView = new TextView(this);
      this.mEmptyView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.empty_page_search, 0);
      this.mEmptyView.setText("抱歉，出错啦\n");
      Object localObject = new SpannableString("点小评没有找到这条内容");
      ((SpannableString)localObject).setSpan(new ForegroundColorSpan(R.color.light_gray), 0, ((SpannableString)localObject).length(), 18);
      ((SpannableString)localObject).setSpan(new RelativeSizeSpan(0.8F), 0, ((SpannableString)localObject).length(), 18);
      this.mEmptyView.append((CharSequence)localObject);
      localObject = new FrameLayout.LayoutParams(-1, -1);
      this.mRoot.addView(this.mEmptyView, (ViewGroup.LayoutParams)localObject);
    }
    if (paramBoolean)
    {
      this.mEmptyView.setVisibility(0);
      return;
    }
    this.mEmptyView.setVisibility(8);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    processParams();
    initViews(paramBundle);
    requestFeedDetail();
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.REVIEWDELETE");
    LocalBroadcastManager.getInstance(this).registerReceiver(this.mReceiver, paramBundle);
  }

  public void onDestroy()
  {
    super.onDestroy();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mReceiver);
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    super.onLoginSuccess(paramAccountService);
    if (paramAccountService != null)
      requestFeedDetail();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getFeedDetailRequest)
    {
      this.getFeedDetailRequest = null;
      this.mLoadingView.setVisibility(8);
      this.mErrorView.setVisibility(0);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getFeedDetailRequest)
    {
      this.getFeedDetailRequest = null;
      this.mLoadingView.setVisibility(8);
      if ((paramMApiResponse.result() instanceof DPObject))
        setFeedView(new FeedItem((DPObject)paramMApiResponse.result()));
    }
    else
    {
      return;
    }
    this.mErrorView.setVisibility(0);
  }

  public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
  {
  }

  public void onRequestStart(MApiRequest paramMApiRequest)
  {
    if (paramMApiRequest == this.getFeedDetailRequest)
    {
      this.mLoadingView.setVisibility(0);
      this.mErrorView.setVisibility(8);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.feed.ui.FeedDetailActivity
 * JD-Core Version:    0.6.0
 */
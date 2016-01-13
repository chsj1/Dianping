package com.dianping.ugc.review.list.ui;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView.LayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.NovaFragment;
import com.dianping.v1.R.color;

public class ReviewListFragment extends NovaFragment
  implements AccountListener
{
  static final DPObject DEFAULT_SORT = new DPObject("ReviewFilter").edit().putInt("ID", 400).putString("Name", "默认排序").generate();
  static final DPObject DEFAULT_STAR;
  public static final String TAG = "ReviewListFragment";
  static final DPObject TIME_SORT = new DPObject("ReviewFilter").edit().putInt("ID", 300).putString("Name", "时间倒序").generate();
  private boolean isHotelReview;
  private ReviewListFragment.ReviewListAdapter mAdapter;
  private int mCommentViewMarginBottom;
  DPObject mCurrentSort = DEFAULT_SORT;
  DPObject mCurrentStar = DEFAULT_STAR;
  private String mDealId;
  private int mFilterId;
  private boolean mHeaderAdded = false;
  private int mHotelLabelId;
  private String mKeyword;
  int mLastSelectedTagPosition = 0;
  private boolean mNeedFilter = true;
  private final BroadcastReceiver mReceiver = new ReviewListFragment.1(this);
  private ListView mReviewList;
  private LinearLayout mReviewListHeader;
  private ViewGroup mRoot;
  private String mSelectedReviewId;
  private int mShopId;
  private int mUserId = 0;

  static
  {
    DEFAULT_STAR = new DPObject("ReviewFilter").edit().putInt("ID", 400).putString("Name", "全部星级").generate();
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    reset();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    String str = getActivity().getIntent().getData().getHost();
    if (("hotelreview".equals(str)) || ("hoteltuanreview".equals(str)) || ("hoteloverseareview".equals(str)));
    for (boolean bool = true; ; bool = false)
    {
      this.isHotelReview = bool;
      if ("hoteloverseareview".equals(str))
        setNeedFilter(false);
      if (paramBundle != null)
      {
        this.mShopId = paramBundle.getInt("shopId");
        this.mUserId = paramBundle.getInt("userId");
        this.mKeyword = paramBundle.getString("keyword");
        this.mDealId = paramBundle.getString("dealId");
        this.mFilterId = paramBundle.getInt("filterId");
        this.mHotelLabelId = paramBundle.getInt("hotelLabelId");
      }
      this.mAdapter = new ReviewListFragment.ReviewListAdapter(this, getActivity());
      this.mReviewList.setAdapter(this.mAdapter);
      return;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    accountService().addListener(this);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mRoot = new FrameLayout(paramLayoutInflater.getContext());
    this.mRoot.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
    this.mReviewList = new ListView(paramLayoutInflater.getContext());
    this.mReviewList.setCacheColorHint(getResources().getColor(R.color.transparent));
    this.mReviewList.setSelector(new ColorDrawable(getResources().getColor(R.color.transparent)));
    this.mReviewList.setHeaderDividersEnabled(false);
    this.mReviewListHeader = new LinearLayout(paramLayoutInflater.getContext());
    this.mReviewListHeader.setGravity(17);
    this.mReviewListHeader.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
    this.mReviewList.addHeaderView(this.mReviewListHeader);
    this.mRoot.addView(this.mReviewList, new FrameLayout.LayoutParams(-1, -1));
    this.mRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ReviewListFragment.2(this));
    return this.mRoot;
  }

  public void onDestroy()
  {
    super.onDestroy();
    accountService().removeListener(this);
    try
    {
      LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mReceiver);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public void onPause()
  {
    super.onPause();
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.dianping.REVIEWREFRESH");
    localIntentFilter.addAction("com.dianping.REVIEWDELETE");
    localIntentFilter.addAction("com.dianping.REFRESHLIKE");
    localIntentFilter.addAction("com.dianping.ADDCOMMENT");
    LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mReceiver, localIntentFilter);
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("shopId", this.mShopId);
    paramBundle.putInt("userId", this.mUserId);
    paramBundle.putString("keyword", this.mKeyword);
    paramBundle.putString("dealId", this.mDealId);
    paramBundle.putInt("filterId", this.mFilterId);
    paramBundle.putInt("hotelLabelId", this.mHotelLabelId);
  }

  public void onStart()
  {
    super.onStart();
    try
    {
      LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mReceiver);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public void reset()
  {
    this.mAdapter.reset();
  }

  public void setDealId(String paramString)
  {
    this.mDealId = paramString;
  }

  public void setFilterId(int paramInt)
  {
    this.mFilterId = paramInt;
  }

  public void setHotelLabelId(int paramInt)
  {
    this.mHotelLabelId = paramInt;
  }

  public void setKeyword(String paramString)
  {
    this.mKeyword = paramString;
  }

  public void setNeedFilter(boolean paramBoolean)
  {
    this.mNeedFilter = paramBoolean;
    if (this.mReviewListHeader != null)
      this.mReviewListHeader.removeAllViews();
    this.mHeaderAdded = false;
  }

  public void setSelectedReviewId(String paramString)
  {
    this.mSelectedReviewId = paramString;
  }

  public void setShopId(int paramInt)
  {
    this.mShopId = paramInt;
  }

  public void setUserId(int paramInt)
  {
    this.mUserId = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.list.ui.ReviewListFragment
 * JD-Core Version:    0.6.0
 */
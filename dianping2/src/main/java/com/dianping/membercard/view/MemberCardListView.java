package com.dianping.membercard.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.dianping.archive.DPObject;
import com.dianping.membercard.MemberCardListAdapter;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.NovaFrameLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MemberCardListView extends NovaFrameLayout
  implements AdapterView.OnItemClickListener, View.OnClickListener
{
  private static final int LIST_ANIM_TIME = 500;
  private MemberCardListAdapter mAdapter;
  private MemberCardListItem mBottomImg1;
  private MemberCardListItem mBottomImg2;
  private View mBottomLay;
  private int mBottomLayDisToTop;
  private MemberCardItem mCardInfo;
  private int mCardInfoDisToTop;
  private CouponEntryView mCouponEntryHeaderView;
  private Interpolator mDecelerateInterpolator;
  private boolean mIsClose;
  private PullToRefreshListView mListView;
  private View mMockView;
  private View mNextNextRealView;
  private View mNextRealView;
  private View.OnClickListener mOnCardInfoClickListener;
  private OnItemClickWithAnimListener mOnItemClickWithAnimListener;
  private View mRealView;

  public MemberCardListView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MemberCardListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mDecelerateInterpolator = AnimationUtils.loadInterpolator(paramContext, 17432582);
    initView();
  }

  private void playCardRevertAnim()
  {
    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(this.mBottomLay, "y", new float[] { getBottom(), this.mBottomLayDisToTop }).setDuration(500L);
    localObjectAnimator1.setInterpolator(this.mDecelerateInterpolator);
    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(this.mBottomLay, "scaleY", new float[] { 0.0F, 1.0F }).setDuration(500L);
    ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(this.mCardInfo, "y", new float[] { 0.0F, this.mCardInfoDisToTop }).setDuration(500L);
    localObjectAnimator3.setInterpolator(this.mDecelerateInterpolator);
    AnimatorSet localAnimatorSet = new AnimatorSet();
    localAnimatorSet.addListener(new Animator.AnimatorListener()
    {
      public void onAnimationCancel(Animator paramAnimator)
      {
      }

      public void onAnimationEnd(Animator paramAnimator)
      {
        MemberCardListView.this.mMockView.setVisibility(8);
        MemberCardListView.this.mRealView.setVisibility(0);
        if (MemberCardListView.this.mNextRealView != null)
          MemberCardListView.this.mNextRealView.setVisibility(0);
        if (MemberCardListView.this.mNextNextRealView != null)
          MemberCardListView.this.mNextNextRealView.setVisibility(0);
      }

      public void onAnimationRepeat(Animator paramAnimator)
      {
      }

      public void onAnimationStart(Animator paramAnimator)
      {
        MemberCardListView.this.mMockView.setVisibility(0);
        MemberCardListView.this.mRealView.setVisibility(4);
        if (MemberCardListView.this.mNextRealView != null)
          MemberCardListView.this.mNextRealView.setVisibility(4);
        if (MemberCardListView.this.mNextNextRealView != null)
          MemberCardListView.this.mNextNextRealView.setVisibility(4);
      }
    });
    localAnimatorSet.playTogether(new Animator[] { localObjectAnimator1, localObjectAnimator2, localObjectAnimator3 });
    localAnimatorSet.start();
  }

  private void setupCouponEntryView()
  {
    this.mCouponEntryHeaderView = ((CouponEntryView)LayoutInflater.from(getContext()).inflate(R.layout.membercard_coupon_entry_view, this.mListView, false));
    this.mCouponEntryHeaderView.setVisibility(8);
    this.mListView.addHeaderView(this.mCouponEntryHeaderView, null, false);
  }

  private void showBottomLay(DPObject paramDPObject1, DPObject paramDPObject2)
  {
    this.mBottomImg1.setData(paramDPObject1);
    this.mBottomImg2.setData(paramDPObject2);
    View localView1 = this.mBottomImg1.findViewById(R.id.card_line);
    View localView2 = this.mBottomImg2.findViewById(R.id.card_line);
    if (paramDPObject1 == null)
    {
      localView1.setVisibility(8);
      localView2.setVisibility(8);
      return;
    }
    localView1.setVisibility(0);
    if (paramDPObject2 == null)
    {
      localView2.setVisibility(8);
      return;
    }
    localView2.setVisibility(0);
  }

  public void closeList(boolean paramBoolean, Animator.AnimatorListener paramAnimatorListener)
  {
    if (this.mIsClose)
      return;
    if (paramBoolean)
    {
      ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(this.mListView, "y", new float[] { 0.0F, getBottom() }).setDuration(500L);
      localObjectAnimator1.setInterpolator(this.mDecelerateInterpolator);
      ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(this.mListView, "scaleY", new float[] { 1.0F, 0.0F }).setDuration(500L);
      ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(this.mBottomLay, "y", new float[] { this.mBottomLayDisToTop, getBottom() }).setDuration(500L);
      ObjectAnimator localObjectAnimator4 = ObjectAnimator.ofFloat(this.mCardInfo, "y", new float[] { this.mCardInfoDisToTop, 0.0F }).setDuration(500L);
      AnimatorSet localAnimatorSet = new AnimatorSet();
      localAnimatorSet.addListener(paramAnimatorListener);
      localAnimatorSet.playTogether(new Animator[] { localObjectAnimator1, localObjectAnimator2, localObjectAnimator3, localObjectAnimator4 });
      localAnimatorSet.start();
      this.mIsClose = true;
      return;
    }
    paramAnimatorListener = this.mListView.getLayoutParams();
    paramAnimatorListener.height = 0;
    this.mListView.setLayoutParams(paramAnimatorListener);
    this.mIsClose = true;
  }

  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mIsClose)
      return false;
    return super.dispatchTouchEvent(paramMotionEvent);
  }

  protected void initView()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.card_list, this, true);
    this.mListView = ((PullToRefreshListView)findViewById(R.id.list));
    this.mListView.setOnItemClickListener(this);
    this.mMockView = findViewById(R.id.mock_item);
    this.mCardInfo = ((MemberCardItem)findViewById(R.id.img_u));
    this.mCardInfo.setOnClickListener(this);
    this.mBottomLay = findViewById(R.id.lay_d);
    this.mBottomImg1 = ((MemberCardListItem)findViewById(R.id.img_d_1));
    this.mBottomImg2 = ((MemberCardListItem)findViewById(R.id.img_d_2));
    setupCouponEntryView();
  }

  public boolean isClose()
  {
    return this.mIsClose;
  }

  public void notifyDataSetChanged()
  {
    this.mAdapter.notifyDataSetChanged();
  }

  public void onClick(View paramView)
  {
    if ((paramView.getId() == R.id.img_u) && (this.mOnCardInfoClickListener != null))
      this.mOnCardInfoClickListener.onClick(paramView);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    Adapter localAdapter = paramAdapterView.getAdapter();
    Object localObject3 = localAdapter.getItem(paramInt);
    Object localObject2 = null;
    if (paramInt + 1 < localAdapter.getCount())
      localObject2 = localAdapter.getItem(paramInt + 1);
    Object localObject1 = null;
    if (paramInt + 2 < localAdapter.getCount())
      localObject1 = paramAdapterView.getAdapter().getItem(paramInt + 2);
    if (!(localObject3 instanceof DPObject));
    do
    {
      return;
      if (this.mOnItemClickWithAnimListener != null)
        this.mOnItemClickWithAnimListener.onItemClickBeforAnim(paramAdapterView, paramView, paramInt, paramLong);
      localObject3 = (DPObject)localObject3;
    }
    while (MemberCard.isThirdPartyCard((DPObject)localObject3));
    if ((localObject2 instanceof DPObject))
    {
      localObject2 = (DPObject)localObject2;
      if (!(localObject1 instanceof DPObject))
        break label245;
    }
    label245: for (localObject1 = (DPObject)localObject1; ; localObject1 = null)
    {
      this.mCardInfo.setData((DPObject)localObject3);
      showBottomLay((DPObject)localObject2, (DPObject)localObject1);
      this.mCardInfoDisToTop = paramView.getTop();
      this.mBottomLayDisToTop = (paramView.getBottom() - ViewUtils.dip2px(getContext(), 10.0F));
      this.mRealView = paramView;
      this.mNextRealView = paramAdapterView.getChildAt(paramInt + 1);
      this.mNextNextRealView = paramAdapterView.getChildAt(paramInt + 2);
      closeList(true, new Animator.AnimatorListener(paramAdapterView, paramView, paramInt, paramLong)
      {
        public void onAnimationCancel(Animator paramAnimator)
        {
        }

        public void onAnimationEnd(Animator paramAnimator)
        {
          if (MemberCardListView.this.mOnItemClickWithAnimListener != null)
            MemberCardListView.this.mOnItemClickWithAnimListener.onItemClickAfterAnim(this.val$parent, this.val$view, this.val$position, this.val$id);
          MemberCardListView.this.mMockView.setVisibility(8);
          MemberCardListView.this.mRealView.setVisibility(0);
          if (MemberCardListView.this.mNextRealView != null)
            MemberCardListView.this.mNextRealView.setVisibility(0);
          if (MemberCardListView.this.mNextNextRealView != null)
            MemberCardListView.this.mNextNextRealView.setVisibility(0);
        }

        public void onAnimationRepeat(Animator paramAnimator)
        {
        }

        public void onAnimationStart(Animator paramAnimator)
        {
          MemberCardListView.this.mMockView.setVisibility(0);
          MemberCardListView.this.mRealView.setVisibility(4);
          if (MemberCardListView.this.mNextRealView != null)
            MemberCardListView.this.mNextRealView.setVisibility(4);
          if (MemberCardListView.this.mNextNextRealView != null)
            MemberCardListView.this.mNextNextRealView.setVisibility(4);
        }
      });
      return;
      localObject2 = null;
      break;
    }
  }

  public void onRefreshComplete()
  {
    this.mListView.onRefreshComplete();
  }

  public void openList()
  {
    openList(false);
  }

  public void openList(boolean paramBoolean)
  {
    if (!this.mIsClose)
      return;
    ViewPropertyAnimator.animate(this.mListView).translationY(0.0F).scaleY(1.0F).setDuration(500L).setInterpolator(this.mDecelerateInterpolator).setListener(null).start();
    this.mIsClose = false;
    playCardRevertAnim();
  }

  public void setAdapter(MemberCardListAdapter paramMemberCardListAdapter)
  {
    this.mAdapter = paramMemberCardListAdapter;
    this.mListView.setAdapter(paramMemberCardListAdapter);
  }

  public void setCouponEntryViewListener(CouponEntryView.OnCouponEntryViewClickListener paramOnCouponEntryViewClickListener)
  {
    this.mCouponEntryHeaderView.setListener(paramOnCouponEntryViewClickListener);
  }

  public void setEmptyView(View paramView)
  {
    this.mListView.setEmptyView(paramView);
  }

  public void setOnCardInfoClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mOnCardInfoClickListener = paramOnClickListener;
  }

  public void setOnItemClickWithAnimListener(OnItemClickWithAnimListener paramOnItemClickWithAnimListener)
  {
    this.mOnItemClickWithAnimListener = paramOnItemClickWithAnimListener;
  }

  public void setOnRefreshListener(PullToRefreshListView.OnRefreshListener paramOnRefreshListener)
  {
    this.mListView.setOnRefreshListener(paramOnRefreshListener);
  }

  public void setSelection(int paramInt)
  {
    this.mListView.setSelection(paramInt);
  }

  public void showCardInfo(int paramInt)
  {
    new Handler().postDelayed(new Runnable(paramInt)
    {
      public void run()
      {
        MemberCardListView.this.onItemClick(MemberCardListView.this.mListView, MemberCardListView.this.mListView.getChildAt(this.val$postion), this.val$postion, 0L);
      }
    }
    , 100L);
  }

  public void trigerRefreshing()
  {
    this.mListView.setRefreshing();
  }

  public void updateCouponEntryHeaderViewContents(String paramString1, String paramString2)
  {
    this.mCouponEntryHeaderView.updateCouponEntry(paramString1, paramString2);
  }

  public void updateCouponEntryHeaderViewVisibility(int paramInt)
  {
    this.mCouponEntryHeaderView.setVisibility(paramInt);
  }

  public void updateCurrrentUserName(String paramString)
  {
    if (this.mCardInfo != null)
    {
      this.mCardInfo.updateUserNameOnly(paramString);
      this.mCardInfo.invalidate();
    }
  }

  public static abstract interface OnItemClickWithAnimListener
  {
    public abstract void onItemClickAfterAnim(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong);

    public abstract void onItemClickBeforAnim(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.MemberCardListView
 * JD-Core Version:    0.6.0
 */
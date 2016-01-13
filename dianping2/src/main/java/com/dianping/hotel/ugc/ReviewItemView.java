package com.dianping.hotel.ugc;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.ShopPower;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.model.UserProfile;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import com.dianping.util.LoginUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingItem;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaTextView;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReviewItemView extends FrameLayout
  implements View.OnClickListener
{
  public static final int DEFAULT_MAX_LINE = 8;
  public static final int MAX_PHOTOS = 4;
  private static final String TAG = ReviewItemView.class.getSimpleName().toString();
  private static final String URL_ADD_APPROVE = "http://m.api.dianping.com/review/ugcfavor.bin";
  private static final String URL_APPROVE_LIST = "http://m.api.dianping.com/review/getfavorlist.bin";
  private static final String URL_COMMENT_LIST = "http://m.api.dianping.com/review/getugccommentlist.bin";
  private static final String URL_DELETE_OWN_REVIEW = "http://m.api.dianping.com/delreview.bin";
  private static final String URL_REPORT = "http://m.api.dianping.com/review/reportugcfeed.bin";
  private View mAddApproveAnimView;
  private TextView mAllFriendsReviewsView;
  private View mAllReviewsView;
  private TextView mApproveCount;
  private TextView mAvgPrice;
  private TextView mCommentCount;
  private OnCommentListener mCommentListener;
  private View mCommentView;
  private ViewGroup mCommentsLayout;
  private TextView mContent;
  private ImageView mContentExpandView;
  private TextView mCreatedTime;
  private ReviewItem mData;
  private OnDeleteOwnReviewListener mDeleteListener;
  private View mDeleteView;
  private View mEditView;
  private OnExpandFriendsListener mExpandFriendsListener;
  private NetworkImageView mHonorView;
  public String mID;
  private int mIndex;
  private View mMoreView;
  private OnNotifyUpdateListener mNotifyListener;
  private ViewGroup mPhotosView;
  private ShopPower mShopPower;
  private PopupWindow mShowMorePopupWindow;
  private int mShowMorePopupWindowHeight;
  private int mShowMorePopupWindowWidth;
  private TextView mSource;
  private NovaTextView mTranslate;
  private View mTranslateLayout;
  private LinearLayout mUserLabels;
  private View mUserLayout;
  private View.OnClickListener mUserListener;
  private TextView mUsername;

  public ReviewItemView(Context paramContext)
  {
    super(paramContext);
  }

  public ReviewItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void addApprove()
  {
    if (this.mData.hasOwnApprove)
    {
      Toast.makeText(getContext(), "你已称赞过有用", 1).show();
      return;
    }
    if (((DPActivity)getContext()).accountService().id() == this.mData.userId)
    {
      Toast.makeText(getContext(), "不能称赞自己", 1).show();
      return;
    }
    Object localObject = ((NovaActivity)getContext()).getAccount();
    if (localObject != null)
      this.mData.addApprove(((UserProfile)localObject).nickName());
    localObject = new ArrayList();
    ((ArrayList)localObject).add("originuserid");
    ((ArrayList)localObject).add(String.valueOf(DPApplication.instance().accountService().id()));
    ((ArrayList)localObject).add("actiontype");
    ((ArrayList)localObject).add("1");
    ((ArrayList)localObject).add("mainid");
    ((ArrayList)localObject).add(this.mData.reviewId);
    ((ArrayList)localObject).add("feedtype");
    ((ArrayList)localObject).add("1");
    localObject = (BasicMApiRequest)BasicMApiRequest.mapiPost("http://m.api.dianping.com/review/ugcfavor.bin", (String[])((ArrayList)localObject).toArray(new String[((ArrayList)localObject).size()]));
    ((NovaActivity)getContext()).mapiService().exec((Request)localObject, null);
    localObject = new AnimationSet(true);
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
    TranslateAnimation localTranslateAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, -0.5F);
    localAlphaAnimation.setDuration(1000L);
    localTranslateAnimation.setDuration(1000L);
    ((AnimationSet)localObject).addAnimation(localAlphaAnimation);
    ((AnimationSet)localObject).addAnimation(localTranslateAnimation);
    ((AnimationSet)localObject).setInterpolator(new DecelerateInterpolator());
    ((AnimationSet)localObject).setAnimationListener(new Animation.AnimationListener()
    {
      public void onAnimationEnd(Animation paramAnimation)
      {
        ReviewItemView.this.mAddApproveAnimView.setVisibility(8);
      }

      public void onAnimationRepeat(Animation paramAnimation)
      {
      }

      public void onAnimationStart(Animation paramAnimation)
      {
      }
    });
    this.mAddApproveAnimView.setVisibility(0);
    this.mAddApproveAnimView.startAnimation((Animation)localObject);
    this.mData.hasOwnApprove = true;
  }

  private void deleteOwnReview(int paramInt)
  {
    Object localObject = new ArrayList();
    ((ArrayList)localObject).add("token");
    ((ArrayList)localObject).add(DPApplication.instance().accountService().token());
    ((ArrayList)localObject).add("reviewid");
    ((ArrayList)localObject).add(this.mData.reviewId);
    ((ArrayList)localObject).add("type");
    ((ArrayList)localObject).add(String.valueOf(this.mData.reviewType));
    localObject = BasicMApiRequest.mapiPost("http://m.api.dianping.com/delreview.bin", (String[])((ArrayList)localObject).toArray(new String[((ArrayList)localObject).size()]));
    DPApplication.instance().mapiService().exec((Request)localObject, new RequestHandler(paramInt)
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        ((NovaActivity)ReviewItemView.this.getContext()).dismissDialog();
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        ((NovaActivity)ReviewItemView.this.getContext()).dismissDialog();
        if ((this.val$position == ReviewItemView.this.mIndex) && (ReviewItemView.this.mDeleteListener != null))
        {
          ReviewItemView.this.mDeleteListener.onDelete(ReviewItemView.this.mIndex);
          if ((paramMApiResponse.result() instanceof DPObject))
          {
            int i = ((DPObject)paramMApiResponse.result()).getInt("StatusCode");
            paramMApiRequest = ((DPObject)paramMApiResponse.result()).getString("Content");
            if ((i == 200) && (!TextUtils.isEmpty(paramMApiRequest)))
              Toast.makeText(DPApplication.instance(), paramMApiRequest, 1).show();
          }
        }
      }
    });
    ((NovaActivity)getContext()).showProgressDialog(getResources().getString(R.string.ugc_review_deleting));
  }

  private void requestApprove(String paramString, int paramInt, ReviewItem paramReviewItem)
  {
    paramReviewItem.approveStr = "";
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/getfavorlist.bin").buildUpon();
    localBuilder.appendQueryParameter("mainid", String.valueOf(paramReviewItem.reviewId));
    localBuilder.appendQueryParameter("feedtype", "1");
    localBuilder.appendQueryParameter("start", "0");
    ((DPActivity)getContext()).mapiService().exec(BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED), new FullRequestHandle(paramReviewItem, paramString, paramInt)
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        this.val$data.approveState = ReviewItem.COMMENT_STATE.COMMENT_ERROR;
        if (ReviewItemView.this.mID.equals(this.val$id))
          ReviewItemView.this.updateLoadingApprove(true);
        do
          return;
        while (ReviewItemView.this.mNotifyListener == null);
        ReviewItemView.this.mNotifyListener.onNotify(this.val$position, this.val$id);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if ((paramMApiResponse.result() instanceof DPObject))
        {
          this.val$data.approveState = ReviewItem.COMMENT_STATE.COMMENT_IDLE;
          paramMApiRequest = ((DPObject)paramMApiResponse.result()).getArray("List");
          if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
          {
            paramMApiResponse = new ArrayList(paramMApiRequest.length);
            int i = 0;
            while (i < paramMApiRequest.length)
            {
              paramMApiResponse.add(new FavorUser(paramMApiRequest[i]));
              i += 1;
            }
            this.val$data.setApprove(paramMApiResponse);
            if (ReviewItemView.this.mID.equals(this.val$id))
              ReviewItemView.this.updateComment();
          }
          if (ReviewItemView.this.mID.equals(this.val$id))
            ReviewItemView.this.updateComment();
        }
        do
        {
          do
            return;
          while (ReviewItemView.this.mNotifyListener == null);
          ReviewItemView.this.mNotifyListener.onNotify(this.val$position, this.val$id);
          return;
          this.val$data.approveState = ReviewItem.COMMENT_STATE.COMMENT_ERROR;
          if (!ReviewItemView.this.mID.equals(this.val$id))
            continue;
          ReviewItemView.this.updateLoadingApprove(true);
          return;
        }
        while (ReviewItemView.this.mNotifyListener == null);
        ReviewItemView.this.mNotifyListener.onNotify(this.val$position, this.val$id);
      }

      public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
      {
      }

      public void onRequestStart(MApiRequest paramMApiRequest)
      {
        this.val$data.approveState = ReviewItem.COMMENT_STATE.COMMENT_LOADING;
        if (ReviewItemView.this.mID.equals(this.val$id))
          ReviewItemView.this.updateLoadingApprove(false);
      }
    });
  }

  private void requestComments(String paramString, int paramInt, ReviewItem paramReviewItem)
  {
    paramReviewItem.comments.clear();
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/getugccommentlist.bin").buildUpon();
    localBuilder.appendQueryParameter("mainid", String.valueOf(paramReviewItem.reviewId));
    localBuilder.appendQueryParameter("feedtype", "1");
    localBuilder.appendQueryParameter("start", "0");
    ((DPActivity)getContext()).mapiService().exec(BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED), new FullRequestHandle(paramReviewItem, paramString, paramInt)
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        this.val$data.commentState = ReviewItem.COMMENT_STATE.COMMENT_ERROR;
        if (ReviewItemView.this.mID.equals(this.val$id))
          ReviewItemView.this.updateLoadingComment(true);
        do
          return;
        while (ReviewItemView.this.mNotifyListener == null);
        ReviewItemView.this.mNotifyListener.onNotify(this.val$position, this.val$id);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        Log.d(ReviewItemView.TAG, "requestComments onRequestFinish position: " + ReviewItemView.this.mIndex + ", currentId: " + ReviewItemView.this.mID + ", requestId: " + this.val$id);
        if ((paramMApiResponse.result() instanceof DPObject))
        {
          this.val$data.commentState = ReviewItem.COMMENT_STATE.COMMENT_IDLE;
          paramMApiRequest = ((DPObject)paramMApiResponse.result()).getArray("List");
          if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
          {
            paramMApiResponse = new ArrayList(paramMApiRequest.length);
            int i = 0;
            while (i < paramMApiRequest.length)
            {
              paramMApiResponse.add(new ReviewComment(paramMApiRequest[i]));
              i += 1;
            }
            this.val$data.setComment(paramMApiResponse);
          }
          if (ReviewItemView.this.mID.equals(this.val$id))
            ReviewItemView.this.updateComment();
        }
        do
        {
          do
            return;
          while (ReviewItemView.this.mNotifyListener == null);
          ReviewItemView.this.mNotifyListener.onNotify(this.val$position, this.val$id);
          return;
          this.val$data.commentState = ReviewItem.COMMENT_STATE.COMMENT_ERROR;
          if (!ReviewItemView.this.mID.equals(this.val$id))
            continue;
          ReviewItemView.this.updateLoadingComment(true);
          return;
        }
        while (ReviewItemView.this.mNotifyListener == null);
        ReviewItemView.this.mNotifyListener.onNotify(this.val$position, this.val$id);
      }

      public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
      {
      }

      public void onRequestStart(MApiRequest paramMApiRequest)
      {
        this.val$data.commentState = ReviewItem.COMMENT_STATE.COMMENT_LOADING;
        if (ReviewItemView.this.mID.equals(this.val$id))
          ReviewItemView.this.updateLoadingComment(false);
      }
    });
  }

  private void setContentMaxLine(int paramInt)
  {
    Log.d(TAG, "setContentMaxLine maxLine = " + paramInt);
    this.mContent.setMaxLines(paramInt);
    this.mContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
    {
      public boolean onPreDraw()
      {
        ReviewItemView.this.mContent.getViewTreeObserver().removeOnPreDrawListener(this);
        Log.d(ReviewItemView.TAG, "setContentMaxLine onPreDraw");
        int i;
        if (ReviewItemView.this.mContent.getLineCount() > 8)
        {
          ReviewItemView.this.mContentExpandView.setVisibility(0);
          ImageView localImageView = ReviewItemView.this.mContentExpandView;
          if (ReviewItemView.this.mData.isContentExpanded)
          {
            i = R.drawable.mini_arrow_up;
            localImageView.setImageResource(i);
          }
        }
        while (true)
        {
          return true;
          i = R.drawable.mini_arrow_down;
          break;
          ReviewItemView.this.mContentExpandView.setVisibility(8);
        }
      }
    });
  }

  private void setID(String paramString)
  {
    this.mID = paramString;
  }

  private void setUserLabels(String[] paramArrayOfString)
  {
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      this.mUserLabels.removeAllViews();
      this.mUserLabels.setVisibility(0);
      this.mUserLayout.measure(View.MeasureSpec.makeMeasureSpec(ViewUtils.getScreenWidthPixels(getContext()), 1073741824), 0);
      FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, ViewUtils.dip2px(getContext(), 16));
      int j = paramArrayOfString.length;
      int i = 0;
      if (i < j)
      {
        String str = paramArrayOfString[i];
        if (TextUtils.isEmpty(str));
        while (true)
        {
          i += 1;
          break;
          DPNetworkImageView localDPNetworkImageView = new DPNetworkImageView(getContext());
          localDPNetworkImageView.setLayoutParams(localLayoutParams);
          localDPNetworkImageView.setPadding(0, 0, ViewUtils.dip2px(getContext(), 3), 0);
          localDPNetworkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
          localDPNetworkImageView.setAdjustViewBounds(true);
          localDPNetworkImageView.setImage(str);
          this.mUserLabels.addView(localDPNetworkImageView);
        }
      }
    }
    else
    {
      this.mUserLabels.setVisibility(8);
    }
  }

  private void showMore(View paramView)
  {
    if (this.mShowMorePopupWindow == null)
    {
      View localView = LayoutInflater.from(getContext()).inflate(R.layout.hotel_temp_ugc_review_item_more, null, false);
      localView.measure(0, 0);
      this.mShowMorePopupWindowWidth = localView.getMeasuredWidth();
      this.mShowMorePopupWindowHeight = localView.getMeasuredHeight();
      this.mShowMorePopupWindow = new PopupWindow(localView, -2, -2);
      this.mShowMorePopupWindow.setBackgroundDrawable(new BitmapDrawable());
      this.mShowMorePopupWindow.setOutsideTouchable(true);
      this.mShowMorePopupWindow.setFocusable(true);
      this.mShowMorePopupWindow.setTouchable(true);
      localView = this.mShowMorePopupWindow.getContentView();
      localView.findViewById(R.id.add_approve).setOnClickListener(this);
      localView.findViewById(R.id.add_comment).setOnClickListener(this);
      localView.findViewById(R.id.review_report).setOnClickListener(this);
    }
    if (this.mShowMorePopupWindow.isShowing())
    {
      this.mShowMorePopupWindow.dismiss();
      return;
    }
    int i = paramView.getHeight();
    this.mShowMorePopupWindow.showAsDropDown(paramView, -this.mShowMorePopupWindowWidth, -(this.mShowMorePopupWindowHeight + i) / 2);
  }

  private void updateComment()
  {
    if (!this.mData.hasApprove())
    {
      this.mApproveCount.setVisibility(8);
      if (this.mData.hasComments())
        break label124;
      this.mCommentCount.setVisibility(8);
      label38: if (!this.mData.isApproveExpanded)
        break label363;
      if (this.mData.approveState != ReviewItem.COMMENT_STATE.COMMENT_LOADING)
        break label181;
      updateLoadingApprove(false);
    }
    label124: 
    do
    {
      return;
      this.mApproveCount.setVisibility(0);
      this.mApproveCount.setText(getResources().getString(R.string.ugc_review_approve) + " " + this.mData.approveCount);
      break;
      this.mCommentCount.setVisibility(0);
      this.mCommentCount.setText(getResources().getString(R.string.ugc_review_comment) + " " + this.mData.commentCount);
      break label38;
      if (this.mData.approveState != ReviewItem.COMMENT_STATE.COMMENT_ERROR)
        continue;
      updateLoadingApprove(true);
      return;
    }
    while ((this.mData.approveState != ReviewItem.COMMENT_STATE.COMMENT_IDLE) || (!this.mData.hasApproveContent()));
    label181: this.mCommentsLayout.removeAllViews();
    this.mApproveCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.comments_bg_top);
    this.mApproveCount.setSelected(true);
    this.mCommentCount.setCompoundDrawables(null, null, null, null);
    this.mCommentCount.setSelected(false);
    Object localObject = new TextView(getContext());
    ((TextView)localObject).setLayoutParams(new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(-2, -2)));
    ((TextView)localObject).setTextSize(16.0F);
    ((TextView)localObject).setLineSpacing(3.0F, 1.5F);
    ((TextView)localObject).setText(this.mData.approveStr);
    ((TextView)localObject).setTextColor(getResources().getColor(R.color.light_gray));
    this.mCommentsLayout.addView((View)localObject);
    this.mCommentsLayout.setVisibility(0);
    return;
    label363: if (this.mData.isCommentExpanded)
    {
      if (this.mData.commentState == ReviewItem.COMMENT_STATE.COMMENT_LOADING)
      {
        updateLoadingComment(false);
        return;
      }
      if (this.mData.commentState == ReviewItem.COMMENT_STATE.COMMENT_ERROR)
      {
        updateLoadingComment(true);
        return;
      }
      if ((this.mData.commentState == ReviewItem.COMMENT_STATE.COMMENT_IDLE) && (this.mData.hasCommentContent()))
      {
        this.mCommentsLayout.removeAllViews();
        this.mCommentCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.comments_bg_top);
        this.mCommentCount.setSelected(true);
        this.mApproveCount.setCompoundDrawables(null, null, null, null);
        this.mApproveCount.setSelected(false);
        localObject = this.mData.comments.iterator();
        while (((Iterator)localObject).hasNext())
        {
          ReviewComment localReviewComment = (ReviewComment)((Iterator)localObject).next();
          TextView localTextView = new TextView(getContext());
          localTextView.setLayoutParams(new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(-1, -2)));
          localTextView.setTextSize(16.0F);
          localTextView.setLineSpacing(3.0F, 1.5F);
          localTextView.setText(localReviewComment.getComment());
          localTextView.setTag(localReviewComment);
          localTextView.setOnClickListener(this);
          this.mCommentsLayout.addView(localTextView);
        }
        this.mCommentsLayout.setVisibility(0);
        return;
      }
      this.mCommentsLayout.removeAllViews();
      this.mCommentCount.setCompoundDrawables(null, null, null, null);
      this.mCommentCount.setSelected(false);
      this.mCommentsLayout.setVisibility(8);
      return;
    }
    this.mApproveCount.setCompoundDrawables(null, null, null, null);
    this.mApproveCount.setSelected(false);
    this.mCommentCount.setCompoundDrawables(null, null, null, null);
    this.mCommentCount.setSelected(false);
    this.mCommentsLayout.setVisibility(8);
  }

  private void updateLoadingApprove(boolean paramBoolean)
  {
    if (!this.mData.isApproveExpanded)
      return;
    this.mCommentsLayout.removeAllViews();
    this.mApproveCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.comments_bg_top);
    this.mApproveCount.setSelected(true);
    this.mCommentCount.setCompoundDrawables(null, null, null, null);
    this.mCommentCount.setSelected(false);
    Object localObject;
    if (paramBoolean)
    {
      localObject = (LoadingErrorView)LayoutInflater.from(getContext()).inflate(R.layout.error_item, this.mCommentsLayout, false);
      ((LoadingErrorView)localObject).setLayoutParams(new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(-1, ViewUtils.dip2px(getContext(), 50.0F))));
      ((LoadingErrorView)localObject).setId(R.id.approve_retry);
      ((LoadingErrorView)localObject).setOnClickListener(this);
    }
    while (true)
    {
      this.mCommentsLayout.setVisibility(0);
      this.mCommentsLayout.addView((View)localObject);
      return;
      localObject = (LoadingItem)LayoutInflater.from(getContext()).inflate(R.layout.loading_item, this.mCommentsLayout, false);
      ((LoadingItem)localObject).setLayoutParams(new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(-1, ViewUtils.dip2px(getContext(), 50.0F))));
    }
  }

  private void updateLoadingComment(boolean paramBoolean)
  {
    if (!this.mData.isCommentExpanded)
      return;
    this.mCommentsLayout.removeAllViews();
    this.mCommentCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.comments_bg_top);
    this.mCommentCount.setSelected(true);
    this.mApproveCount.setCompoundDrawables(null, null, null, null);
    this.mApproveCount.setSelected(false);
    Object localObject;
    if (paramBoolean)
    {
      localObject = (LoadingErrorView)LayoutInflater.from(getContext()).inflate(R.layout.error_item, this.mCommentsLayout, false);
      ((LoadingErrorView)localObject).setLayoutParams(new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(-1, ViewUtils.dip2px(getContext(), 50.0F))));
      ((LoadingErrorView)localObject).setId(R.id.comment_retry);
      ((LoadingErrorView)localObject).setOnClickListener(this);
    }
    while (true)
    {
      this.mCommentsLayout.setVisibility(0);
      this.mCommentsLayout.addView((View)localObject);
      return;
      localObject = (LoadingItem)LayoutInflater.from(getContext()).inflate(R.layout.loading_item, this.mCommentsLayout, false);
      ((LoadingItem)localObject).setLayoutParams(new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(-1, ViewUtils.dip2px(getContext(), 50.0F))));
    }
  }

  private void updatePhotos(ReviewItem paramReviewItem)
  {
    int j = 0;
    this.mPhotosView.removeAllViews();
    if (paramReviewItem.thumbnailsPhotos == null);
    do
      return;
    while (paramReviewItem.thumbnailsPhotos.length == 0);
    int k = (ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 11.0F) * 3 - ViewUtils.dip2px(getContext(), 17.0F) * 2) / 4;
    int m = Math.min(4, paramReviewItem.thumbnailsPhotos.length);
    int i = 0;
    Object localObject;
    if (i < m)
    {
      if ((i != 3) || (paramReviewItem.thumbnailsPhotos.length <= 4))
        break label227;
      if (paramReviewItem.sourceType != 1)
      {
        localObject = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.hotel_temp_review_item_more_photo_tv, this.mPhotosView, false);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("共").append(Integer.toString(paramReviewItem.thumbnailsPhotos.length)).append("张");
        ((TextView)localObject).setText(localStringBuilder.toString());
        ((TextView)localObject).setOnClickListener(this);
        ((TextView)localObject).setVisibility(0);
        ((TextView)localObject).getLayoutParams().width = k;
        ((TextView)localObject).getLayoutParams().height = k;
        this.mPhotosView.addView((View)localObject);
      }
    }
    paramReviewItem = this.mPhotosView;
    if (m > 0);
    for (i = j; ; i = 8)
    {
      paramReviewItem.setVisibility(i);
      return;
      label227: localObject = (NetworkImageView)LayoutInflater.from(getContext()).inflate(R.layout.review_item_photo, this.mPhotosView, false);
      ((NetworkImageView)localObject).getLayoutParams().width = k;
      ((NetworkImageView)localObject).getLayoutParams().height = k;
      ((NetworkImageView)localObject).setImage(paramReviewItem.thumbnailsPhotos[i]);
      ((NetworkImageView)localObject).setOnClickListener(this);
      ((NetworkImageView)localObject).setTag(Integer.valueOf(i));
      this.mPhotosView.addView((View)localObject);
      i += 1;
      break;
    }
  }

  public void addComment(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    Object localObject;
    boolean bool;
    if (this.mData.isApproveExpanded)
    {
      localObject = this.mData;
      if (this.mData.isApproveExpanded)
        break label96;
      bool = true;
    }
    while (true)
    {
      ((ReviewItem)localObject).isApproveExpanded = bool;
      localObject = new ReviewComment();
      ((ReviewComment)localObject).type = paramString1;
      ((ReviewComment)localObject).commentUsername = paramString2;
      try
      {
        if ("1".equals(paramString1))
          ((ReviewComment)localObject).content = paramString4;
        while (true)
        {
          this.mData.addComment((ReviewComment)localObject);
          this.mData.isCommentExpanded = true;
          updateComment();
          return;
          label96: bool = false;
          break;
          if (!"2".equals(paramString1))
            continue;
          paramString1 = new JSONArray();
          paramString2 = new JSONObject();
          paramString2.put("text", getResources().getString(R.string.ugc_review_comment_tip) + paramString3 + "：");
          paramString1.put(paramString2);
          paramString2 = new JSONObject();
          paramString2.put("text", paramString4);
          paramString1.put(paramString2);
          ((ReviewComment)localObject).content = paramString1.toString();
        }
      }
      catch (JSONException paramString1)
      {
        while (true)
          paramString1.printStackTrace();
      }
    }
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.review_user_layout)
    {
      i = this.mData.sourceType;
      if (i == 0)
        if (DPApplication.instance().accountService().id() == this.mData.userId)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user"));
          getContext().startActivity(paramView);
          ((DPActivity)getContext()).statisticsEvent("shopinfo5", "shopinfo5_review_user", "", 0);
        }
    }
    label695: label760: 
    do
    {
      do
      {
        return;
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user?userid=" + this.mData.userId));
        break;
      }
      while ((i != 1) || (TextUtils.isEmpty(this.mData.sourceUrl)));
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.mData.sourceUrl));
      getContext().startActivity(paramView);
      return;
      if (i == R.id.more_photos)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://reviewpicturegridlist"));
        paramView.putExtra("shopId", this.mData.shopId);
        paramView.putExtra("shopname", this.mData.shopName);
        paramView.putExtra("userId", this.mData.userId);
        if ((getContext() instanceof Activity))
          getContext().startActivity(paramView);
        if ((getContext() instanceof Activity))
        {
          ((DPActivity)getContext()).statisticsEvent("shopinfo5", "shopinfo5_review_pic", "更多", 0);
          return;
        }
        ((DPActivity)getContext()).statisticsEvent("shopinfo5", "shopinfo5_review_pic", "更多", 0);
        return;
      }
      Object localObject1;
      Object localObject2;
      Object localObject3;
      if ((paramView instanceof NetworkImageView))
      {
        localObject1 = new ArrayList();
        localObject2 = this.mData.photos;
        int j = localObject2.length;
        i = 0;
        while (i < j)
        {
          localObject3 = localObject2[i];
          ((ArrayList)localObject1).add(new DPObject().edit().putString("Url", (String)localObject3).generate());
          i += 1;
        }
        localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showcheckinphoto"));
        if (this.mData.shopName != null)
          ((Intent)localObject2).putExtra("shopname", this.mData.shopName);
        i = ((Integer)paramView.getTag()).intValue();
        ((Intent)localObject2).putExtra("position", i);
        ((Intent)localObject2).putExtra("fromActivity", "ReviewPictureGridListActiviy");
        try
        {
          paramView = ((BitmapDrawable)((NetworkImageView)paramView).getDrawable()).getBitmap();
          localObject3 = new ByteArrayOutputStream();
          paramView.compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject3);
          ((Intent)localObject2).putExtra("currentbitmap", ((ByteArrayOutputStream)localObject3).toByteArray());
          ((Intent)localObject2).putParcelableArrayListExtra("pageList", (ArrayList)localObject1);
          if ((getContext() instanceof Activity))
            getContext().startActivity((Intent)localObject2);
          if ((getContext() instanceof Activity))
          {
            ((DPActivity)getContext()).statisticsEvent("shopinfo5", "shopinfo5_review_pic", String.valueOf(i + 1), 0);
            return;
          }
        }
        catch (Exception paramView)
        {
          while (true)
            paramView.printStackTrace();
          ((DPActivity)getContext()).statisticsEvent("shopinfo5", "shopinfo5_review_pic", String.valueOf(i + 1), 0);
          return;
        }
      }
      boolean bool;
      if ((i == R.id.review_content) || (i == R.id.review_content_expand))
      {
        paramView = this.mData;
        if (!this.mData.isContentExpanded)
        {
          bool = true;
          paramView.isContentExpanded = bool;
          if (!this.mData.isContentExpanded)
            break label695;
        }
        for (i = 2147483647; ; i = 8)
        {
          setContentMaxLine(i);
          return;
          bool = false;
          break;
        }
      }
      if (i == R.id.review_content_translate)
      {
        paramView = this.mData;
        if (!this.mData.isOriginalContent)
        {
          bool = true;
          paramView.isOriginalContent = bool;
          localObject1 = this.mTranslate;
          if (!this.mData.isOriginalContent)
            break label845;
          paramView = getResources().getString(R.string.ugc_review_translate_to_chinese);
          ((NovaTextView)localObject1).setText(paramView);
          localObject1 = this.mContent;
          if (!this.mData.isOriginalContent)
            break label859;
          paramView = this.mData.content;
          ((TextView)localObject1).setText(paramView);
          if (!this.mData.isContentExpanded)
            break label870;
          i = 2147483647;
          setContentMaxLine(i);
          localObject1 = this.mTranslate;
          if (!this.mData.isOriginalContent)
            break label877;
        }
        for (paramView = "translate"; ; paramView = "origin")
        {
          ((NovaTextView)localObject1).setGAString(paramView);
          return;
          bool = false;
          break;
          paramView = getResources().getString(R.string.ugc_review_translate_show_original);
          break label760;
          paramView = this.mData.translatedContent;
          break label788;
          i = 8;
          break label808;
        }
      }
      if (i == R.id.review_approve_count)
      {
        paramView = this.mData;
        if (!this.mData.isApproveExpanded)
        {
          bool = true;
          paramView.isApproveExpanded = bool;
          if (!this.mData.isApproveExpanded)
            break label989;
          if (this.mData.isCommentExpanded)
          {
            paramView = this.mData;
            if (this.mData.isCommentExpanded)
              break label983;
          }
        }
        for (bool = true; ; bool = false)
        {
          paramView.isCommentExpanded = bool;
          requestApprove(this.mID, this.mIndex, this.mData);
          return;
          bool = false;
          break;
        }
        this.mApproveCount.setCompoundDrawables(null, null, null, null);
        this.mApproveCount.setSelected(false);
        this.mCommentsLayout.setVisibility(8);
        return;
      }
      if (i == R.id.review_comment_count)
      {
        paramView = this.mData;
        if (!this.mData.isCommentExpanded)
        {
          bool = true;
          paramView.isCommentExpanded = bool;
          if (!this.mData.isCommentExpanded)
            break label1123;
          if (this.mData.isApproveExpanded)
          {
            paramView = this.mData;
            if (this.mData.isApproveExpanded)
              break label1117;
          }
        }
        for (bool = true; ; bool = false)
        {
          paramView.isApproveExpanded = bool;
          requestComments(this.mID, this.mIndex, this.mData);
          return;
          bool = false;
          break;
        }
        this.mCommentCount.setCompoundDrawables(null, null, null, null);
        this.mCommentCount.setSelected(false);
        this.mCommentsLayout.setVisibility(8);
        return;
      }
      if (i == R.id.comment_retry)
      {
        requestComments(this.mID, this.mIndex, this.mData);
        return;
      }
      if (i == R.id.approve_retry)
      {
        requestApprove(this.mID, this.mIndex, this.mData);
        return;
      }
      if (i == R.id.review_more)
      {
        showMore(this.mMoreView);
        return;
      }
      if (i == R.id.add_approve)
      {
        if ((((DPActivity)getContext()).accountService().token() != null) && (this.mShowMorePopupWindow != null))
          this.mShowMorePopupWindow.dismiss();
        ((DPActivity)getContext()).statisticsEvent("shopinfo5", "shopinfo5_review_like", "", 0);
        if (((DPActivity)getContext()).accountService().token() == null)
        {
          LoginUtils.setLoginGASource(getContext(), "rev_useful");
          ((DPActivity)getContext()).accountService().login(null);
          return;
        }
        addApprove();
        updateComment();
        return;
      }
      if ((i == R.id.add_comment) || (i == R.id.review_comment_btn) || (((paramView instanceof TextView)) && ((paramView.getTag() instanceof ReviewComment))))
      {
        if ((((DPActivity)getContext()).accountService().token() != null) && (this.mShowMorePopupWindow != null))
          this.mShowMorePopupWindow.dismiss();
        if (((DPActivity)getContext()).accountService().token() == null)
        {
          ((DPActivity)getContext()).accountService().login(null);
          return;
        }
        localObject1 = "0";
        localObject2 = "1";
        localObject3 = "";
        if ((paramView.getTag() instanceof ReviewComment))
        {
          localObject1 = ((ReviewComment)paramView.getTag()).commentId;
          localObject2 = "2";
          localObject3 = ((ReviewComment)paramView.getTag()).commentUsername;
        }
        if (this.mCommentListener != null)
        {
          this.mCommentListener.onComment(this.mIndex, this.mData.reviewId, (String)localObject1, (String)localObject2, (String)localObject3);
          return;
        }
      }
      if (i == R.id.review_report)
      {
        if ((((DPActivity)getContext()).accountService().token() != null) && (this.mShowMorePopupWindow != null))
          this.mShowMorePopupWindow.dismiss();
        if (((DPActivity)getContext()).accountService().token() == null)
        {
          ((DPActivity)getContext()).accountService().login(null);
          return;
        }
        paramView = new AlertDialog.Builder(getContext());
        paramView.setTitle("举报类型");
        localObject1 = new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            paramDialogInterface = Uri.parse("http://m.api.dianping.com/review/reportugcfeed.bin").buildUpon();
            ArrayList localArrayList = new ArrayList();
            localArrayList.add("cx");
            localArrayList.add(DeviceUtils.cxInfo("shopreport"));
            localArrayList.add("feedtype");
            localArrayList.add("1");
            localArrayList.add("causetype");
            localArrayList.add(String.valueOf(paramInt + 1));
            localArrayList.add("mainid");
            localArrayList.add(String.valueOf(ReviewItemView.this.mData.reviewId));
            paramDialogInterface = BasicMApiRequest.mapiPost(paramDialogInterface.toString(), (String[])localArrayList.toArray(new String[localArrayList.size()]));
            DPApplication.instance().mapiService().exec(paramDialogInterface, new RequestHandler()
            {
              public void onRequestFailed(Request paramRequest, Response paramResponse)
              {
              }

              public void onRequestFinish(Request paramRequest, Response paramResponse)
              {
                Toast.makeText(DPApplication.instance(), "举报已提交！", 0).show();
              }
            });
          }
        };
        paramView.setItems(new String[] { "广告", "反动", "色情", "灌水" }, (DialogInterface.OnClickListener)localObject1);
        paramView.setNegativeButton("取消", null);
        paramView.show();
        return;
      }
      if (i == R.id.all_reviews)
      {
        paramView = Uri.parse("dianping://additionalreview").buildUpon();
        paramView.appendQueryParameter("id", String.valueOf(this.mData.shopId));
        paramView.appendQueryParameter("userid", String.valueOf(this.mData.userId));
        paramView = new Intent("android.intent.action.VIEW", paramView.build());
        ((NovaActivity)getContext()).startActivity(paramView);
        return;
      }
      if (i == R.id.review_edit_btn)
      {
        paramView = Uri.parse("dianping://ugcaddreview").buildUpon();
        paramView.appendQueryParameter("shopid", String.valueOf(this.mData.shopId));
        paramView.appendQueryParameter("reviewID", this.mData.reviewId);
        paramView = new Intent("android.intent.action.VIEW", paramView.build());
        getContext().startActivity(paramView);
        return;
      }
      if ((i != R.id.all_friends_review) || (this.mExpandFriendsListener == null))
        continue;
      this.mExpandFriendsListener.onExpand();
    }
    while (i != R.id.review_delete_btn);
    label788: label808: label845: label859: label870: label877: paramView = new AlertDialog.Builder(getContext());
    label983: label989: label1123: paramView.setTitle("提示").setMessage(getResources().getString(R.string.ugc_temp_review_delete_prompt)).setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        ReviewItemView.this.deleteOwnReview(ReviewItemView.this.mIndex);
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
      }
    });
    label1117: paramView.show();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mUserLayout = findViewById(R.id.review_user_layout);
    this.mUsername = ((TextView)findViewById(R.id.review_user_name));
    this.mUserLabels = ((LinearLayout)findViewById(R.id.review_user_labels));
    this.mSource = ((TextView)findViewById(R.id.review_source));
    this.mHonorView = ((NetworkImageView)findViewById(R.id.review_honour));
    this.mShopPower = ((ShopPower)findViewById(R.id.shop_power));
    this.mAvgPrice = ((TextView)findViewById(R.id.shop_average_price));
    this.mCreatedTime = ((TextView)findViewById(R.id.review_created_time));
    this.mContent = ((TextView)findViewById(R.id.review_content));
    this.mContent.setOnClickListener(this);
    this.mContentExpandView = ((ImageView)findViewById(R.id.review_content_expand));
    this.mContentExpandView.setOnClickListener(this);
    this.mTranslateLayout = findViewById(R.id.review_content_translate_layout);
    this.mTranslate = ((NovaTextView)findViewById(R.id.review_content_translate));
    this.mTranslate.setOnClickListener(this);
    this.mPhotosView = ((ViewGroup)findViewById(R.id.review_photos));
    this.mApproveCount = ((TextView)findViewById(R.id.review_approve_count));
    this.mApproveCount.setOnClickListener(this);
    this.mCommentCount = ((TextView)findViewById(R.id.review_comment_count));
    this.mCommentCount.setOnClickListener(this);
    this.mCommentsLayout = ((ViewGroup)findViewById(R.id.review_comments));
    this.mDeleteView = findViewById(R.id.review_delete_btn);
    this.mEditView = findViewById(R.id.review_edit_btn);
    this.mCommentView = findViewById(R.id.review_comment_btn);
    this.mMoreView = findViewById(R.id.review_more);
    this.mMoreView.setOnClickListener(this);
    this.mDeleteView = findViewById(R.id.review_delete_btn);
    this.mDeleteView.setOnClickListener(this);
    this.mEditView = findViewById(R.id.review_edit_btn);
    this.mEditView.setOnClickListener(this);
    this.mCommentView = findViewById(R.id.review_comment_btn);
    this.mCommentView.setOnClickListener(this);
    this.mAddApproveAnimView = findViewById(R.id.new_approve);
    this.mAllReviewsView = findViewById(R.id.all_reviews);
    this.mAllReviewsView.setOnClickListener(this);
    this.mAllFriendsReviewsView = ((TextView)findViewById(R.id.all_friends_review));
    this.mAllFriendsReviewsView.setOnClickListener(this);
  }

  public void setData(ReviewItem paramReviewItem)
  {
    this.mData = paramReviewItem;
    setID(paramReviewItem.ID);
    label87: Object localObject1;
    if (this.mUserListener == null)
    {
      this.mUserLayout.setOnClickListener(this);
      this.mUsername.setText(paramReviewItem.username);
      setUserLabels(paramReviewItem.userLabels);
      this.mSource.setText(paramReviewItem.sourceName);
      if (TextUtils.isEmpty(paramReviewItem.honourUrl))
        break label393;
      this.mHonorView.setVisibility(0);
      this.mHonorView.setImage(paramReviewItem.honourUrl);
      this.mShopPower.setPower(paramReviewItem.shopPower);
      this.mAvgPrice.setText(paramReviewItem.avgPrice);
      this.mCreatedTime.setText(paramReviewItem.createdAt);
      Object localObject2 = this.mContent;
      if (!paramReviewItem.isOriginalContent)
        break label405;
      localObject1 = paramReviewItem.content;
      label137: ((TextView)localObject2).setText((CharSequence)localObject1);
      if (!paramReviewItem.isContentExpanded)
        break label413;
      i = 2147483647;
      label154: setContentMaxLine(i);
      if (TextUtils.isEmpty(paramReviewItem.translatedContent))
        break label441;
      this.mTranslateLayout.setVisibility(0);
      localObject2 = this.mTranslate;
      if (!paramReviewItem.isOriginalContent)
        break label420;
      localObject1 = getResources().getString(R.string.ugc_review_translate_to_chinese);
      label201: ((NovaTextView)localObject2).setText((CharSequence)localObject1);
      localObject2 = this.mTranslate;
      if (!paramReviewItem.isOriginalContent)
        break label434;
      localObject1 = "translate";
      label222: ((NovaTextView)localObject2).setGAString((String)localObject1);
      label227: if ((paramReviewItem.thumbnailsPhotos == null) || (paramReviewItem.photos == null))
        break label453;
      this.mPhotosView.setVisibility(0);
      updatePhotos(paramReviewItem);
      label254: updateComment();
      if (paramReviewItem.sourceType != 1)
        break label465;
      this.mMoreView.setVisibility(8);
      this.mDeleteView.setVisibility(8);
      this.mEditView.setVisibility(8);
      this.mCommentView.setVisibility(8);
      label302: paramReviewItem = this.mAllReviewsView;
      if (!this.mData.hasMoreReviews())
        break label567;
    }
    label393: label405: label413: label420: label434: label567: for (int i = 0; ; i = 8)
    {
      paramReviewItem.setVisibility(i);
      if (!this.mData.hasMoreFriendReviews())
        break label574;
      this.mAllFriendsReviewsView.setVisibility(0);
      this.mAllFriendsReviewsView.setText(getResources().getString(R.string.ugc_review_more_friend_review, new Object[] { Integer.valueOf(this.mData.friendCount) }));
      return;
      this.mUserLayout.setOnClickListener(this.mUserListener);
      break;
      this.mHonorView.setVisibility(8);
      break label87;
      localObject1 = paramReviewItem.translatedContent;
      break label137;
      i = 8;
      break label154;
      localObject1 = getResources().getString(R.string.ugc_review_translate_show_original);
      break label201;
      localObject1 = "origin";
      break label222;
      label441: this.mTranslateLayout.setVisibility(8);
      break label227;
      label453: this.mPhotosView.setVisibility(8);
      break label254;
      label465: if (paramReviewItem.sourceType != 0)
        break label302;
      if ((this.mData.belongType == 0) || (this.mData.belongType == 2))
      {
        this.mMoreView.setVisibility(0);
        this.mDeleteView.setVisibility(8);
        this.mEditView.setVisibility(8);
        this.mCommentView.setVisibility(8);
        break label302;
      }
      this.mMoreView.setVisibility(8);
      this.mDeleteView.setVisibility(0);
      this.mEditView.setVisibility(0);
      this.mCommentView.setVisibility(0);
      break label302;
    }
    label574: this.mAllFriendsReviewsView.setVisibility(8);
  }

  public void setIndex(int paramInt)
  {
    this.mIndex = paramInt;
  }

  public void setOnCommentListener(OnCommentListener paramOnCommentListener)
  {
    this.mCommentListener = paramOnCommentListener;
  }

  public void setOnDeleteOwnReviewListener(OnDeleteOwnReviewListener paramOnDeleteOwnReviewListener)
  {
    this.mDeleteListener = paramOnDeleteOwnReviewListener;
  }

  public void setOnExpandFriendsListener(OnExpandFriendsListener paramOnExpandFriendsListener)
  {
    this.mExpandFriendsListener = paramOnExpandFriendsListener;
  }

  public void setOnNotifyUpdateListener(OnNotifyUpdateListener paramOnNotifyUpdateListener)
  {
    this.mNotifyListener = paramOnNotifyUpdateListener;
  }

  public void setUserListener(View.OnClickListener paramOnClickListener)
  {
    this.mUserListener = paramOnClickListener;
  }

  public void update(ReviewItem paramReviewItem)
  {
    this.mAvgPrice.setText(paramReviewItem.avgPrice);
    this.mShopPower.setPower(paramReviewItem.shopPower);
    this.mContent.setText(paramReviewItem.content);
    updatePhotos(paramReviewItem);
  }

  public static abstract interface OnCommentListener
  {
    public abstract void onComment(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4);
  }

  public static abstract interface OnDeleteOwnReviewListener
  {
    public abstract void onDelete(int paramInt);
  }

  public static abstract interface OnExpandFriendsListener
  {
    public abstract void onExpand();
  }

  public static abstract interface OnNotifyUpdateListener
  {
    public abstract void onNotify(int paramInt, String paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.ugc.ReviewItemView
 * JD-Core Version:    0.6.0
 */
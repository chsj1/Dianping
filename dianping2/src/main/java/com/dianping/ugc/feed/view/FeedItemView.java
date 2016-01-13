package com.dianping.ugc.feed.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.ShopPower;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.model.UserProfile;
import com.dianping.ugc.feed.model.FeedItem;
import com.dianping.ugc.feed.model.FeedUser;
import com.dianping.ugc.widget.FeedCommentView;
import com.dianping.ugc.widget.FeedCommentView.OnCommentListener;
import com.dianping.ugc.widget.FeedGridPhotoView;
import com.dianping.ugc.widget.FeedGridPhotoView.Style;
import com.dianping.ugc.widget.FeedPoiView;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaFrameLayout;
import com.dianping.widget.view.NovaTextView;

public class FeedItemView extends NovaFrameLayout
  implements View.OnClickListener, FeedCommentView.OnCommentListener
{
  public static final int DEFAULT_MAX_LINE = 6;
  private static final String TAG = FeedItemView.class.getSimpleName().toString();
  private TextView mAllFriendsReviewsView;
  private NovaTextView mAllReviewsView;
  private TextView mAvgPrice;
  private OnCommentListener mCommentListener;
  private FeedCommentView mCommentView;
  TextView mContent;
  NovaTextView mContentExpandView;
  private TextView mCreatedTime;
  FeedItem mData;
  private FeedItemView.OnExpandFriendsListener mExpandFriendsListener;
  private LinearLayout mFeedMainLayout;
  private FeedPoiView mFeedPoiView;
  private TextView mFeedSource;
  private DPNetworkImageView mHonorView;
  public String mID;
  public int mIndex = -1;
  int mMaxLines = 6;
  private FeedGridPhotoView mPhotosView;
  private TextView mScore;
  private ShopPower mShopPower;
  private TextView mShopPowerHint;
  boolean mShowContentExpand = true;
  private NovaTextView mTranslate;
  private View mTranslateLayout;
  private DPNetworkImageView mUserAvatar;
  private View mUserAvatarLayout;
  View mUserInfo;
  private LinearLayout mUserLabels;
  int mUserLabelsWidth;
  private DPNetworkImageView mUserLevel;
  private View.OnClickListener mUserListener;
  private TextView mUserSource;
  TextView mUsername;

  public FeedItemView(Context paramContext)
  {
    super(paramContext);
  }

  public FeedItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void setContentMaxLine(int paramInt)
  {
    Log.d(TAG, "setContentMaxLine maxLine = " + paramInt);
    this.mContent.setMaxLines(paramInt);
    this.mContent.getViewTreeObserver().addOnPreDrawListener(new FeedItemView.2(this));
  }

  private void setID(String paramString)
  {
    this.mID = paramString;
  }

  private void setUserLabels(String[] paramArrayOfString)
  {
    this.mUserLabels.removeAllViews();
    this.mUserLabelsWidth = 0;
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      this.mUserLabels.setVisibility(0);
      FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, ViewUtils.dip2px(getContext(), 16.0F));
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
          localDPNetworkImageView.setImageSize(0, ViewUtils.dip2px(getContext(), 16.0F));
          localDPNetworkImageView.setPadding(0, 0, ViewUtils.dip2px(getContext(), 3.0F), 0);
          localDPNetworkImageView.setImage(str);
          localDPNetworkImageView.setLoadChangeListener(new FeedItemView.1(this, localDPNetworkImageView));
          this.mUserLabels.addView(localDPNetworkImageView);
        }
      }
    }
    else
    {
      this.mUserLabels.setVisibility(8);
    }
  }

  private void updatePhotos(FeedItem paramFeedItem)
  {
    this.mPhotosView.setPhotos(paramFeedItem.thumbnailsPhotos, paramFeedItem.photos);
  }

  public void addComment(FeedUser paramFeedUser, String paramString)
  {
    UserProfile localUserProfile = ((NovaActivity)getContext()).getAccount();
    if (localUserProfile != null)
      this.mCommentView.addComment(new FeedUser(String.valueOf(localUserProfile.id()), localUserProfile.nickName()), paramFeedUser, paramString);
  }

  public void addComment(String paramString1, String paramString2, String paramString3, FeedUser paramFeedUser, String paramString4)
  {
    UserProfile localUserProfile = ((NovaActivity)getContext()).getAccount();
    if (localUserProfile != null)
      this.mCommentView.addComment(paramString1, paramString2, paramString3, new FeedUser(String.valueOf(localUserProfile.id()), localUserProfile.nickName()), paramFeedUser, paramString4);
  }

  public void addLike(boolean paramBoolean, int paramInt)
  {
    UserProfile localUserProfile = ((NovaActivity)getContext()).getAccount();
    if (localUserProfile != null)
      this.mCommentView.addLike(paramBoolean, paramInt, new FeedUser(String.valueOf(localUserProfile.id()), localUserProfile.nickName()));
  }

  public void enableCommentSummary(boolean paramBoolean)
  {
    this.mCommentView.enableCommentSummary(paramBoolean);
  }

  public void enableExpandContent(boolean paramBoolean)
  {
    this.mShowContentExpand = paramBoolean;
    if (!this.mShowContentExpand)
      this.mContent.setEllipsize(TextUtils.TruncateAt.END);
  }

  public void enableExtraOpts(boolean paramBoolean)
  {
    this.mCommentView.enableExtraOpts(paramBoolean);
  }

  public void enableFullCommentList(boolean paramBoolean)
  {
    this.mCommentView.enableFullCommentList(paramBoolean);
  }

  public void onClick(View paramView)
  {
    int i = 2147483647;
    boolean bool2 = true;
    boolean bool1 = true;
    int j = paramView.getId();
    if ((j == R.id.feed_user_avatar_layout) || (j == R.id.feed_user_name))
      if ((this.mData.reviewType == 0) || (this.mData.reviewType == 1))
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user?userid=" + this.mData.feedUser.userid));
        getContext().startActivity(paramView);
        GAHelper.instance().contextStatisticsEvent(getContext(), "profile", null, "tap");
      }
    label215: label361: label375: 
    do
    {
      return;
      if (j == R.id.review_item)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.mData.detailUrl));
        getContext().startActivity(paramView);
        return;
      }
      if (j == R.id.review_content_expand)
      {
        paramView = this.mData;
        if (!this.mData.isContentExpanded)
        {
          paramView.isContentExpanded = bool1;
          if (!this.mData.isContentExpanded)
            break label215;
        }
        for (i = 2147483647; ; i = this.mMaxLines)
        {
          setContentMaxLine(i);
          return;
          bool1 = false;
          break;
        }
      }
      if (j == R.id.review_content_translate)
      {
        paramView = this.mData;
        Object localObject;
        if (!this.mData.isOriginalContent)
        {
          bool1 = bool2;
          paramView.isOriginalContent = bool1;
          localObject = this.mTranslate;
          if (!this.mData.isOriginalContent)
            break label361;
          paramView = getResources().getString(R.string.ugc_review_translate_to_chinese);
          ((NovaTextView)localObject).setText(paramView);
          localObject = this.mContent;
          if (!this.mData.isOriginalContent)
            break label375;
          paramView = this.mData.getContentWithRecommends();
          ((TextView)localObject).setText(paramView);
          if (!this.mData.isContentExpanded)
            break label386;
          setContentMaxLine(i);
          localObject = this.mTranslate;
          if (!this.mData.isOriginalContent)
            break label394;
        }
        for (paramView = "translate"; ; paramView = "origin")
        {
          ((NovaTextView)localObject).setGAString(paramView);
          return;
          bool1 = false;
          break;
          paramView = getResources().getString(R.string.ugc_review_translate_show_original);
          break label282;
          paramView = this.mData.translatedContent;
          break label310;
          i = this.mMaxLines;
          break label325;
        }
      }
      if (j != R.id.all_reviews)
        continue;
      paramView = Uri.parse("dianping://additionalreview").buildUpon();
      paramView.appendQueryParameter("id", String.valueOf(this.mData.shopId));
      paramView.appendQueryParameter("userid", String.valueOf(this.mData.feedUser.userid));
      paramView = new Intent("android.intent.action.VIEW", paramView.build());
      getContext().startActivity(paramView);
      return;
    }
    while ((j != R.id.all_friends_review) || (this.mExpandFriendsListener == null));
    label282: label310: label325: this.mExpandFriendsListener.onExpand();
    label386: label394: return;
  }

  public void onComment(View paramView, String paramString1, String paramString2, FeedUser paramFeedUser)
  {
    if (this.mCommentListener != null)
    {
      this.mCommentListener.onComment(this.mIndex, paramView, this.mData.feedId, paramString1, paramString2, paramFeedUser);
      return;
    }
    try
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.mData.detailUrl));
      paramView.putExtra("commit", true);
      getContext().startActivity(paramView);
      return;
    }
    catch (Exception paramView)
    {
      paramView.printStackTrace();
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    setGAString("reviewdetail");
    this.mUserAvatarLayout = findViewById(R.id.feed_user_avatar_layout);
    this.mUserAvatar = ((DPNetworkImageView)findViewById(R.id.feed_user_avatar));
    this.mUserLevel = ((DPNetworkImageView)findViewById(R.id.feed_user_level));
    this.mFeedMainLayout = ((LinearLayout)findViewById(R.id.feed_main_layout));
    this.mUserInfo = findViewById(R.id.feed_user_info);
    this.mUsername = ((TextView)findViewById(R.id.feed_user_name));
    this.mUserLabels = ((LinearLayout)findViewById(R.id.feed_user_labels));
    this.mCreatedTime = ((TextView)findViewById(R.id.feed_created_time));
    this.mFeedSource = ((TextView)findViewById(R.id.feed_source));
    this.mHonorView = ((DPNetworkImageView)findViewById(R.id.review_honour));
    this.mShopPowerHint = ((TextView)findViewById(R.id.shop_power_hint));
    this.mShopPower = ((ShopPower)findViewById(R.id.shop_power));
    this.mAvgPrice = ((TextView)findViewById(R.id.shop_average_price));
    this.mUserSource = ((TextView)findViewById(R.id.user_source));
    this.mScore = ((TextView)findViewById(R.id.feed_score));
    this.mContent = ((TextView)findViewById(R.id.review_content));
    this.mContentExpandView = ((NovaTextView)findViewById(R.id.review_content_expand));
    this.mContentExpandView.setGAString("fulltext");
    this.mContentExpandView.setOnClickListener(this);
    this.mTranslateLayout = findViewById(R.id.review_content_translate_layout);
    this.mTranslate = ((NovaTextView)findViewById(R.id.review_content_translate));
    this.mTranslate.setGAString("translate");
    this.mTranslate.setOnClickListener(this);
    this.mPhotosView = ((FeedGridPhotoView)findViewById(R.id.feed_photos));
    this.mFeedPoiView = ((FeedPoiView)findViewById(R.id.feed_poi));
    this.mCommentView = ((FeedCommentView)findViewById(R.id.feed_comment));
    this.mCommentView.setCommentListener(this);
    this.mAllReviewsView = ((NovaTextView)findViewById(R.id.all_reviews));
    this.mAllReviewsView.setGAString("reviewother");
    this.mAllReviewsView.setOnClickListener(this);
    this.mAllFriendsReviewsView = ((TextView)findViewById(R.id.all_friends_review));
    this.mAllFriendsReviewsView.setOnClickListener(this);
  }

  public void setContentMaxLines(int paramInt)
  {
    this.mMaxLines = paramInt;
  }

  public void setData(FeedItem paramFeedItem)
  {
    this.mData = paramFeedItem;
    setID(paramFeedItem.ID);
    label101: label124: Object localObject1;
    label277: label294: label323: label341: if (!TextUtils.isEmpty(this.mData.detailUrl))
    {
      setOnClickListener(this);
      this.mUserAvatar.setImage(paramFeedItem.feedUser.avatar);
      if (TextUtils.isEmpty(paramFeedItem.feedUser.userLevel))
        break label747;
      this.mUserLevel.setImage(paramFeedItem.feedUser.userLevel);
      this.mUserLevel.setImageSize(0, this.mUserLevel.getLayoutParams().height);
      this.mUserLevel.setVisibility(0);
      if (this.mUserListener != null)
        break label759;
      this.mUserAvatarLayout.setOnClickListener(this);
      this.mUsername.setOnClickListener(this);
      this.mUsername.setText(paramFeedItem.feedUser.username);
      setUserLabels(paramFeedItem.feedUser.userTags);
      this.mUserSource.setText(paramFeedItem.feedUser.source);
      this.mFeedSource.setText(paramFeedItem.feedSource);
      this.mCreatedTime.setText(paramFeedItem.createdAt);
      if (TextUtils.isEmpty(paramFeedItem.honourUrl))
        break label784;
      this.mHonorView.setVisibility(0);
      this.mHonorView.setImage(paramFeedItem.honourUrl);
      label215: switch (this.mData.feedType)
      {
      default:
        label248: if (TextUtils.isEmpty(paramFeedItem.scoreText))
          break;
        this.mScore.setText(paramFeedItem.scoreText);
        this.mScore.setVisibility(0);
        Object localObject2 = this.mContent;
        if (paramFeedItem.isOriginalContent)
        {
          localObject1 = paramFeedItem.getContentWithRecommends();
          ((TextView)localObject2).setText((CharSequence)localObject1);
          localObject1 = this.mContent;
          if (this.mContent.getText().length() != 0)
            break label974;
          i = 8;
          ((TextView)localObject1).setVisibility(i);
          if (!paramFeedItem.isContentExpanded)
            break label980;
          i = 2147483647;
          setContentMaxLine(i);
          if (TextUtils.isEmpty(paramFeedItem.translatedContent))
            break label1010;
          this.mTranslateLayout.setVisibility(0);
          localObject2 = this.mTranslate;
          if (!paramFeedItem.isOriginalContent)
            break label989;
          localObject1 = getResources().getString(R.string.ugc_review_translate_to_chinese);
          ((NovaTextView)localObject2).setText((CharSequence)localObject1);
          localObject2 = this.mTranslate;
          if (!paramFeedItem.isOriginalContent)
            break label1003;
          localObject1 = "translate";
          ((NovaTextView)localObject2).setGAString((String)localObject1);
          updatePhotos(paramFeedItem);
          if ((TextUtils.isEmpty(paramFeedItem.poiName)) && (TextUtils.isEmpty(paramFeedItem.poiPic)) && (TextUtils.isEmpty(paramFeedItem.poiDistance)) && (TextUtils.isEmpty(paramFeedItem.poiPrice)) && (TextUtils.isEmpty(paramFeedItem.poiRegion)))
            break label1022;
          this.mFeedPoiView.setVisibility(0);
          this.mFeedPoiView.setShopName(paramFeedItem.poiName);
          this.mFeedPoiView.setShopPhoto(paramFeedItem.poiPic);
          this.mFeedPoiView.setShopPoiDistance(paramFeedItem.poiDistance);
          this.mFeedPoiView.setShopAverage(paramFeedItem.poiPrice);
          this.mFeedPoiView.setShopPoi(paramFeedItem.poiRegion);
          this.mFeedPoiView.setJumpUrl(paramFeedItem.poiJumpUrl);
          this.mFeedPoiView.setGAString("poi");
          if (paramFeedItem.reviewType != 2)
            break label1034;
          this.mCommentView.setVisibility(8);
          if (!this.mData.hasMoreReviews())
            break label1076;
          paramFeedItem = this.mAllReviewsView;
          localObject1 = getResources();
          if (this.mData.belongType != 1)
            break label1068;
          i = R.string.all_reviews_owner;
          label606: paramFeedItem.setText(((Resources)localObject1).getString(i));
          this.mAllReviewsView.setVisibility(0);
          label624: if (!this.mData.hasMoreFriendReviews())
            break label1088;
          this.mAllFriendsReviewsView.setVisibility(0);
          this.mAllFriendsReviewsView.setText(getResources().getString(R.string.ugc_review_more_friend_review, new Object[] { Integer.valueOf(this.mData.friendCount) }));
          if (!this.mData.hasMoreFriendReviews())
            break label1100;
        }
      case 1:
      case 3:
      case 2:
      }
    }
    label388: label409: label414: label553: label570: label1088: label1100: for (int i = this.mAllFriendsReviewsView.getLayoutParams().height; ; i = 0)
    {
      if (((FrameLayout.LayoutParams)this.mFeedMainLayout.getLayoutParams()).bottomMargin != i)
      {
        ((FrameLayout.LayoutParams)this.mFeedMainLayout.getLayoutParams()).bottomMargin = i;
        this.mFeedMainLayout.requestLayout();
      }
      return;
      setOnClickListener(null);
      break;
      label747: this.mUserLevel.setVisibility(8);
      break label101;
      label759: this.mUserAvatarLayout.setOnClickListener(this.mUserListener);
      this.mUsername.setOnClickListener(this.mUserListener);
      break label124;
      this.mHonorView.setVisibility(8);
      break label215;
      this.mShopPowerHint.setText(getResources().getString(R.string.feed_power_hint));
      this.mShopPower.setPower(paramFeedItem.shopPower);
      this.mAvgPrice.setText(paramFeedItem.avgPrice);
      this.mShopPowerHint.setVisibility(0);
      this.mShopPower.setVisibility(0);
      this.mAvgPrice.setVisibility(0);
      break label248;
      this.mShopPowerHint.setText(getResources().getString(R.string.checkin_success));
      this.mShopPowerHint.setVisibility(0);
      this.mShopPower.setVisibility(8);
      this.mAvgPrice.setVisibility(8);
      break label248;
      this.mShopPowerHint.setText(getResources().getString(R.string.upload_photo_success));
      this.mShopPowerHint.setVisibility(0);
      this.mShopPower.setVisibility(8);
      this.mAvgPrice.setVisibility(8);
      break label248;
      this.mScore.setVisibility(8);
      break label277;
      localObject1 = paramFeedItem.translatedContent;
      break label294;
      label974: i = 0;
      break label323;
      label980: i = this.mMaxLines;
      break label341;
      label989: localObject1 = getResources().getString(R.string.ugc_review_translate_show_original);
      break label388;
      label1003: localObject1 = "origin";
      break label409;
      label1010: this.mTranslateLayout.setVisibility(8);
      break label414;
      label1022: this.mFeedPoiView.setVisibility(8);
      break label553;
      if ((paramFeedItem.reviewType != 0) && (paramFeedItem.reviewType != 1))
        break label570;
      this.mCommentView.setVisibility(0);
      this.mCommentView.setFeedData(paramFeedItem);
      break label570;
      i = R.string.all_reviews_guest;
      break label606;
      this.mAllReviewsView.setVisibility(8);
      break label624;
      this.mAllFriendsReviewsView.setVisibility(8);
      break label676;
    }
  }

  public void setIndex(int paramInt)
  {
    this.mIndex = paramInt;
  }

  public void setMaxPhotoCount(int paramInt)
  {
    this.mPhotosView.setMaxPhotoCount(paramInt);
  }

  public void setOnCommentListener(OnCommentListener paramOnCommentListener)
  {
    this.mCommentListener = paramOnCommentListener;
  }

  public void setOnExpandFriendsListener(FeedItemView.OnExpandFriendsListener paramOnExpandFriendsListener)
  {
    this.mExpandFriendsListener = paramOnExpandFriendsListener;
  }

  public void setPhotoStyle(FeedGridPhotoView.Style paramStyle)
  {
    this.mPhotosView.setStyle(paramStyle);
  }

  public void setUserListener(View.OnClickListener paramOnClickListener)
  {
    this.mUserListener = paramOnClickListener;
  }

  public void showCommentList(boolean paramBoolean)
  {
    this.mCommentView.showCommentList(paramBoolean);
  }

  public void update(FeedItem paramFeedItem)
  {
    this.mAvgPrice.setText(paramFeedItem.avgPrice);
    this.mShopPower.setPower(paramFeedItem.shopPower);
    this.mContent.setText(paramFeedItem.getContentWithRecommends());
    updatePhotos(paramFeedItem);
  }

  public static abstract interface OnCommentListener
  {
    public abstract void onComment(int paramInt, View paramView, String paramString1, String paramString2, String paramString3, FeedUser paramFeedUser);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.feed.view.FeedItemView
 * JD-Core Version:    0.6.0
 */
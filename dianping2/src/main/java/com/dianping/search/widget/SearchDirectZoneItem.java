package com.dianping.search.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.search.shoplist.data.model.DirectZoneItemModel;
import com.dianping.search.shoplist.data.model.DisplayContent;
import com.dianping.search.shoplist.data.model.SearchDirectZoneModel;
import com.dianping.search.view.SearchBlurImageView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SearchDirectZoneItem extends NovaLinearLayout
{
  private static DirectZoneItemModel EMPTY = new DirectZoneItemModel();
  public static final int MOVIE_3D = 1;
  public static final int MOVIE_4D = 2;
  public static final int MOVIE_IMAX = 3;
  public static final int MOVIE_IMAX3D = 4;
  public static final int MOVIE_NONE = 0;
  public static final int RIGHT_TEXT_MAX_WIDTH_DP = 150;
  private List<View> holderViews = new ArrayList();
  private SearchBlurImageView mBlurBg;
  private LinearLayout mBottomLine;
  private NovaRelativeLayout mDirectZone;
  private SearchDirectZoneModel mDirectZoneModel = null;
  private View mDivider;
  private View mGradeHint;
  private NetworkThumbView mIcon;
  private NetworkThumbView mIconImg;
  private DirectZonePartItem mLeftBottomItem;
  private DirectZonePartItem mLeftTopItem;
  private View mLineDivider;
  private LinearLayout mLinkContent;
  private LinearLayout mMovieContent;
  private ImageView mMovieEditionState;
  private TextView mMovieTitle;
  private TextView mPicLabel;
  private TextView mProperty;
  private ImageView mRightArrow;
  private DirectZonePartItem mRightBottomItem;
  private DirectZonePartItem mRightTopItem;
  private ImageView mRoundCornerMask;
  private ImageView mRoundMask;
  private TextView mScore;
  private NovaRelativeLayout mSearchOneMovieView;
  private TextView mTag;
  private TextView mTitle;
  private LinearLayout mTopLine;
  private View.OnClickListener onExtClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (((paramView.getTag() instanceof String)) && (!TextUtils.isEmpty((String)paramView.getTag())) && ((SearchDirectZoneItem.this.getContext() instanceof Activity)))
        SearchDirectZoneItem.this.sendAdGA(paramView);
      try
      {
        SearchDirectZoneItem.this.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView.getTag().toString())));
        return;
      }
      catch (Exception paramView)
      {
        paramView.printStackTrace();
      }
    }
  };

  public SearchDirectZoneItem(Context paramContext)
  {
    super(paramContext);
  }

  public SearchDirectZoneItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public SearchDirectZoneItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private void sendAdGA(View paramView)
  {
    if ((this.mDirectZoneModel == null) || (paramView == null) || (TextUtils.isEmpty(this.mDirectZoneModel.mFeedback)))
      return;
    String str = "direct_zone";
    if ((paramView instanceof NovaLinearLayout))
      str = ((NovaLinearLayout)paramView).getGAString();
    while (true)
    {
      paramView = new HashMap();
      paramView.put("act", "2");
      paramView.put("adidx", String.valueOf(this.mDirectZoneModel.index + 1));
      paramView.put("module", str);
      AdClientUtils.report(this.mDirectZoneModel.mFeedback, paramView);
      Log.d("debug_AdGA", "Click-GA-DirectZone:" + str + "-index:" + String.valueOf(this.mDirectZoneModel.index + 1));
      return;
      if ((paramView instanceof NovaTextView))
      {
        str = ((NovaTextView)paramView).getGAString();
        continue;
      }
      if (!(paramView instanceof NovaRelativeLayout))
        continue;
      str = ((NovaRelativeLayout)paramView).getGAString();
    }
  }

  private void setDirectZoneItemMargin(DirectZonePartItem paramDirectZonePartItem, int paramInt)
  {
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)paramDirectZonePartItem.getLayoutParams();
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), paramInt);
    paramDirectZonePartItem.setLayoutParams(localLayoutParams);
  }

  private void setIconSizeDp(int paramInt1, int paramInt2)
  {
    ViewGroup.LayoutParams localLayoutParams = this.mIcon.getLayoutParams();
    localLayoutParams.height = ViewUtils.dip2px(getContext(), paramInt2);
    localLayoutParams.width = ViewUtils.dip2px(getContext(), paramInt1);
  }

  private void setOneMovieViewData(SearchDirectZoneModel paramSearchDirectZoneModel)
  {
    Object localObject = (RelativeLayout.LayoutParams)this.mBlurBg.getLayoutParams();
    ((RelativeLayout.LayoutParams)localObject).width = ViewUtils.getScreenHeightPixels(getContext());
    ((RelativeLayout.LayoutParams)localObject).height = ViewUtils.getViewHeight(this.mSearchOneMovieView);
    this.mBlurBg.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.mBlurBg.setImage(paramSearchDirectZoneModel.picUrl);
    this.mIconImg.setImage(paramSearchDirectZoneModel.picUrl);
    this.mMovieTitle.setText(paramSearchDirectZoneModel.title);
    if (TextUtils.isEmpty(paramSearchDirectZoneModel.mScore))
    {
      this.mGradeHint.setVisibility(4);
      this.mScore.setText("暂无评分");
      switch (paramSearchDirectZoneModel.getMovieLabelType())
      {
      default:
        this.mMovieEditionState.setVisibility(8);
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      }
    }
    View localView;
    while (true)
    {
      localObject = this.holderViews.iterator();
      while (((Iterator)localObject).hasNext())
      {
        localView = (View)((Iterator)localObject).next();
        this.mMovieContent.removeView(localView);
      }
      this.mScore.setText(paramSearchDirectZoneModel.mScore);
      this.mGradeHint.setVisibility(0);
      break;
      this.mMovieEditionState.setVisibility(8);
      continue;
      this.mMovieEditionState.setImageDrawable(getResources().getDrawable(R.drawable.search_movie_editionflag_3d));
      this.mMovieEditionState.setVisibility(0);
      continue;
      this.mMovieEditionState.setImageDrawable(getResources().getDrawable(R.drawable.search_movie_editionflag_4d));
      this.mMovieEditionState.setVisibility(0);
      continue;
      this.mMovieEditionState.setImageDrawable(getResources().getDrawable(R.drawable.search_movie_editionflag_imax));
      this.mMovieEditionState.setVisibility(0);
      continue;
      this.mMovieEditionState.setImageDrawable(getResources().getDrawable(R.drawable.search_movie_editionflag_imax3d));
      this.mMovieEditionState.setVisibility(0);
    }
    int j = Math.max(paramSearchDirectZoneModel.mRightItemContents.length, paramSearchDirectZoneModel.mLeftItemContents.length);
    int i = 0;
    while (i < j)
    {
      localView = LayoutInflater.from(getContext()).inflate(R.layout.search_two_directzoneitem, null);
      this.holderViews.add(localView);
      DirectZonePartItem localDirectZonePartItem2 = (DirectZonePartItem)localView.findViewById(R.id.right_item);
      DirectZonePartItem localDirectZonePartItem1 = (DirectZonePartItem)localView.findViewById(R.id.left_item);
      localDirectZonePartItem2.initColor(R.color.white, R.drawable.search_label_bg_lightred);
      localDirectZonePartItem1.initColor(R.color.white, R.drawable.search_label_bg_lightred);
      localObject = EMPTY;
      if (i < paramSearchDirectZoneModel.mRightItemContents.length)
        localObject = paramSearchDirectZoneModel.mRightItemContents[i];
      localDirectZonePartItem2.setDirectZoneItem((DirectZoneItemModel)localObject);
      localObject = EMPTY;
      if (i < paramSearchDirectZoneModel.mLeftItemContents.length)
        localObject = paramSearchDirectZoneModel.mLeftItemContents[i];
      localDirectZonePartItem1.setDirectZoneItem((DirectZoneItemModel)localObject);
      this.mMovieContent.addView(localView);
      i += 1;
    }
    this.mSearchOneMovieView.setGAString("direct_zone");
    this.mSearchOneMovieView.gaUserInfo.index = Integer.valueOf(paramSearchDirectZoneModel.index);
    this.mSearchOneMovieView.gaUserInfo.query_id = paramSearchDirectZoneModel.queryId;
    this.mSearchOneMovieView.gaUserInfo.keyword = paramSearchDirectZoneModel.keyword;
    this.mSearchOneMovieView.setTag(paramSearchDirectZoneModel.clickUrl);
    this.mSearchOneMovieView.setOnClickListener(this.onExtClickListener);
  }

  private void setOtherDirectZoneData(SearchDirectZoneModel paramSearchDirectZoneModel, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mIcon.setImage(paramSearchDirectZoneModel.picUrl);
    label104: Object localObject1;
    label133: label218: label351: int n;
    if ((paramSearchDirectZoneModel.displayType == 0) || (paramSearchDirectZoneModel.displayType == 3) || (paramSearchDirectZoneModel.displayType == 4))
    {
      setIconSizeDp(56, 56);
      this.mRoundMask.setVisibility(0);
      this.mRoundCornerMask.setVisibility(8);
      if (TextUtils.isEmpty(paramSearchDirectZoneModel.picLabel))
        break label729;
      if (paramSearchDirectZoneModel.displayType != 0)
        break label698;
      this.mTag.setText(paramSearchDirectZoneModel.picLabel);
      this.mPicLabel.setVisibility(8);
      this.mTag.setVisibility(0);
      if (TextUtils.isEmpty(paramSearchDirectZoneModel.property))
        break label750;
      this.mProperty.setText(paramSearchDirectZoneModel.property);
      this.mProperty.setVisibility(0);
      this.mTitle.setText(paramSearchDirectZoneModel.title);
      this.mTitle.requestLayout();
      if ((paramSearchDirectZoneModel.rightTopModel != null) || (paramSearchDirectZoneModel.leftBottomModel != null) || (paramSearchDirectZoneModel.rightBottomModel != null) || (paramSearchDirectZoneModel.leftTopModel == null) || (!TextUtils.isEmpty(paramSearchDirectZoneModel.leftTopModel.iconUrl)) || (paramSearchDirectZoneModel.leftTopModel.type != 0))
        break label762;
      this.mLeftTopItem.setDoubleLine();
      this.mBottomLine.setVisibility(8);
      if (paramSearchDirectZoneModel.rightTopModel != null)
      {
        this.mRightTopItem.setTextMaxWidth(ViewUtils.dip2px(getContext(), 150.0F));
        localObject1 = (RelativeLayout.LayoutParams)this.mTopLine.getLayoutParams();
        if (paramSearchDirectZoneModel.rightTopModel.align != 1)
          break label780;
        ((RelativeLayout.LayoutParams)localObject1).addRule(0, R.id.right_arrow);
        ((RelativeLayout.LayoutParams)localObject1).alignWithParent = true;
        label280: this.mTopLine.setLayoutParams((ViewGroup.LayoutParams)localObject1);
      }
      if (paramSearchDirectZoneModel.rightBottomModel != null)
      {
        this.mRightBottomItem.setTextMaxWidth(ViewUtils.dip2px(getContext(), 150.0F));
        localObject1 = (RelativeLayout.LayoutParams)this.mBottomLine.getLayoutParams();
        if (paramSearchDirectZoneModel.rightBottomModel.align != 1)
          break label796;
        ((RelativeLayout.LayoutParams)localObject1).addRule(0, R.id.right_arrow);
        ((RelativeLayout.LayoutParams)localObject1).alignWithParent = true;
        this.mBottomLine.setLayoutParams((ViewGroup.LayoutParams)localObject1);
      }
      this.mLeftTopItem.setDirectZoneItem(paramSearchDirectZoneModel.leftTopModel);
      this.mLeftTopItem.requestLayout();
      this.mRightTopItem.setDirectZoneItem(paramSearchDirectZoneModel.rightTopModel);
      this.mLeftBottomItem.setDirectZoneItem(paramSearchDirectZoneModel.leftBottomModel);
      this.mLeftBottomItem.requestLayout();
      this.mRightBottomItem.setDirectZoneItem(paramSearchDirectZoneModel.rightBottomModel);
      if (paramSearchDirectZoneModel.leftTopModel != null)
        break label812;
      setDirectZoneItemMargin(this.mRightTopItem, 0);
      label434: if (paramSearchDirectZoneModel.leftBottomModel != null)
        break label824;
      setDirectZoneItemMargin(this.mRightBottomItem, 0);
      label450: this.mLinkContent.removeAllViews();
      localObject1 = new DisplayMetrics();
      ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics((DisplayMetrics)localObject1);
      i = ((DisplayMetrics)localObject1).widthPixels;
      n = ViewUtils.dip2px(getContext(), 7.0F);
      switch (paramSearchDirectZoneModel.displayType)
      {
      case 2:
      default:
        this.mLinkContent.setVisibility(8);
        label553: localObject1 = this.mRightArrow;
        if (!paramBoolean2)
          break;
        i = 0;
        label566: ((ImageView)localObject1).setVisibility(i);
        localObject1 = this.mLineDivider;
        if (!paramBoolean1);
      case 3:
      case 1:
      case 0:
      case 4:
      }
    }
    for (int i = 0; ; i = 8)
    {
      ((View)localObject1).setVisibility(i);
      this.mDirectZone.setGAString("direct_zone");
      this.mDirectZone.gaUserInfo.index = Integer.valueOf(paramSearchDirectZoneModel.index);
      this.mDirectZone.gaUserInfo.query_id = paramSearchDirectZoneModel.queryId;
      this.mDirectZone.gaUserInfo.keyword = paramSearchDirectZoneModel.keyword;
      this.mDirectZone.setTag(paramSearchDirectZoneModel.clickUrl);
      this.mDirectZone.setOnClickListener(this.onExtClickListener);
      return;
      setIconSizeDp(102, 74);
      this.mRoundCornerMask.setVisibility(0);
      this.mRoundMask.setVisibility(8);
      break;
      label698: this.mPicLabel.setText(paramSearchDirectZoneModel.picLabel);
      this.mPicLabel.setVisibility(0);
      this.mTag.setVisibility(8);
      break label104;
      label729: this.mPicLabel.setVisibility(8);
      this.mTag.setVisibility(8);
      break label104;
      label750: this.mProperty.setVisibility(8);
      break label133;
      label762: this.mBottomLine.setVisibility(0);
      this.mLeftTopItem.setSingleLine();
      break label218;
      label780: ((RelativeLayout.LayoutParams)localObject1).addRule(0, 0);
      ((RelativeLayout.LayoutParams)localObject1).alignWithParent = false;
      break label280;
      label796: ((RelativeLayout.LayoutParams)localObject1).addRule(0, 0);
      ((RelativeLayout.LayoutParams)localObject1).alignWithParent = false;
      break label351;
      label812: setDirectZoneItemMargin(this.mRightTopItem, 4);
      break label434;
      label824: setDirectZoneItemMargin(this.mRightBottomItem, 4);
      break label450;
      this.mLinkContent.setOrientation(0);
      this.mLinkContent.setWeightSum(1.0F);
      localObject1 = (NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.shoplist_brand_image, null);
      int j = ViewUtils.dip2px(getContext(), 3.0F);
      Object localObject2 = new LinearLayout.LayoutParams(0, (i - n * 2) * 13 / 48, 1.0F);
      ((LinearLayout.LayoutParams)localObject2).setMargins(j, 0, j, 0);
      ((NovaLinearLayout)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      ((NetworkImageView)((NovaLinearLayout)localObject1).findViewById(R.id.image)).setImage(paramSearchDirectZoneModel.displayContents[0].picUrl);
      ((NovaLinearLayout)localObject1).setTag(paramSearchDirectZoneModel.displayContents[0].titleUrl);
      ((NovaLinearLayout)localObject1).setOnClickListener(this.onExtClickListener);
      ((NovaLinearLayout)localObject1).setGAString("direct_zone_1");
      ((NovaLinearLayout)localObject1).gaUserInfo.query_id = paramSearchDirectZoneModel.queryId;
      this.mLinkContent.addView((View)localObject1);
      this.mLinkContent.setVisibility(0);
      break label553;
      j = paramSearchDirectZoneModel.displayContents.length;
      this.mLinkContent.setWeightSum(j);
      i = ViewUtils.dip2px(getContext(), 5.0F);
      n = ViewUtils.dip2px(getContext(), 3.0F);
      localObject2 = new LinearLayout.LayoutParams(0, -2, 1.0F);
      ((LinearLayout.LayoutParams)localObject2).setMargins(i, 0, i, 0);
      Object localObject3 = new LinearLayout.LayoutParams(0, -2, 1.0F);
      ((LinearLayout.LayoutParams)localObject3).setMargins(n, 0, i, 0);
      Object localObject4 = new LinearLayout.LayoutParams(0, -2, 1.0F);
      ((LinearLayout.LayoutParams)localObject4).setMargins(i, 0, n, 0);
      i = 0;
      if (i < j)
      {
        NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.shoplist_ext_btn, null);
        label1165: TextView localTextView;
        NetworkThumbView localNetworkThumbView;
        if (i == 0)
        {
          localNovaLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject3);
          localObject1 = (TextView)localNovaLinearLayout.findViewById(R.id.content);
          localTextView = (TextView)localNovaLinearLayout.findViewById(R.id.num);
          localNetworkThumbView = (NetworkThumbView)localNovaLinearLayout.findViewById(R.id.icon);
          ((TextView)localObject1).setText(paramSearchDirectZoneModel.displayContents[i].title);
          n = paramSearchDirectZoneModel.displayContents[i].count;
          localObject1 = String.valueOf(n);
          if (n > 99)
            localObject1 = "99+";
          if (n <= 0)
            break label1407;
          localTextView.setText((CharSequence)localObject1);
          localTextView.setVisibility(0);
        }
        while (true)
        {
          localNetworkThumbView.setImage(paramSearchDirectZoneModel.displayContents[i].iconUrl);
          localNovaLinearLayout.setTag(paramSearchDirectZoneModel.displayContents[i].titleUrl);
          localNovaLinearLayout.setOnClickListener(this.onExtClickListener);
          localNovaLinearLayout.setGAString("direct_zone_" + (i + 1));
          localNovaLinearLayout.gaUserInfo.query_id = paramSearchDirectZoneModel.queryId;
          localNovaLinearLayout.gaUserInfo.index = Integer.valueOf(i);
          this.mLinkContent.addView(localNovaLinearLayout);
          i += 1;
          break;
          if (i == j - 1)
          {
            localNovaLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject4);
            break label1165;
          }
          localNovaLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject2);
          break label1165;
          label1407: localTextView.setVisibility(8);
        }
      }
      this.mLinkContent.setVisibility(0);
      break label553;
      int k = paramSearchDirectZoneModel.displayContents.length;
      this.mLinkContent.setWeightSum(k);
      int i1 = ViewUtils.dip2px(getContext(), 3.0F);
      localObject1 = new LinearLayout.LayoutParams(0, ((i - n * 2) / k - i1 * 2) * 3 / 4, 1.0F);
      ((LinearLayout.LayoutParams)localObject1).setMargins(i1, 0, i1, 0);
      i = 0;
      while (i < k)
      {
        localObject2 = (NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.shoplist_brand_image, null);
        localObject3 = (NetworkThumbView)((NovaLinearLayout)localObject2).findViewById(R.id.image);
        ((NetworkThumbView)localObject3).setRoundPixels(4);
        localObject4 = (TextView)((NovaLinearLayout)localObject2).findViewById(R.id.intro);
        ((NovaLinearLayout)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject1);
        ((NetworkThumbView)localObject3).setImage(paramSearchDirectZoneModel.displayContents[i].picUrl);
        ((TextView)localObject4).setText(paramSearchDirectZoneModel.displayContents[i].title);
        ((TextView)localObject4).setVisibility(0);
        ((NovaLinearLayout)localObject2).setTag(paramSearchDirectZoneModel.displayContents[i].titleUrl);
        ((NovaLinearLayout)localObject2).setOnClickListener(this.onExtClickListener);
        ((NovaLinearLayout)localObject2).setGAString("direct_zone_" + (i + 1));
        ((NovaLinearLayout)localObject2).gaUserInfo.query_id = paramSearchDirectZoneModel.queryId;
        ((NovaLinearLayout)localObject2).gaUserInfo.index = Integer.valueOf(i);
        this.mLinkContent.addView((View)localObject2);
        i += 1;
      }
      this.mLinkContent.setVisibility(0);
      break label553;
      this.mLinkContent.setOrientation(1);
      int m = ViewUtils.dip2px(getContext(), 3.0F);
      n = paramSearchDirectZoneModel.displayContents.length;
      i = 0;
      if (i < n)
      {
        localObject1 = (NovaTextView)LayoutInflater.from(getContext()).inflate(R.layout.shoplist_brand_text, null);
        if (i < n - 1)
        {
          localObject2 = new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 36.0F));
          ((LinearLayout.LayoutParams)localObject2).setMargins(m, 0, m, ViewUtils.dip2px(getContext(), 10.0F));
          ((NovaTextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
        }
        while (true)
        {
          ((NovaTextView)localObject1).setText(paramSearchDirectZoneModel.displayContents[i].title);
          ((NovaTextView)localObject1).setTag(paramSearchDirectZoneModel.displayContents[i].titleUrl);
          ((NovaTextView)localObject1).setOnClickListener(this.onExtClickListener);
          ((NovaTextView)localObject1).setGAString("direct_zone_" + (i + 1));
          ((NovaTextView)localObject1).gaUserInfo.query_id = paramSearchDirectZoneModel.queryId;
          ((NovaTextView)localObject1).gaUserInfo.index = Integer.valueOf(i);
          this.mLinkContent.addView((View)localObject1);
          i += 1;
          break;
          localObject2 = new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 36.0F));
          ((LinearLayout.LayoutParams)localObject2).setMargins(m, 0, m, 0);
          ((NovaTextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
        }
      }
      this.mLinkContent.setVisibility(0);
      break label553;
      i = 8;
      break label566;
    }
  }

  public void fetchDisplayTypeView(int paramInt)
  {
    if (paramInt == 6)
    {
      this.mDirectZone.setVisibility(8);
      this.mLinkContent.setVisibility(8);
      this.mSearchOneMovieView.setVisibility(0);
      return;
    }
    this.mSearchOneMovieView.setVisibility(8);
    this.mDirectZone.setVisibility(0);
    this.mLinkContent.setVisibility(0);
  }

  public void findOneMovieViews()
  {
    this.mSearchOneMovieView = ((NovaRelativeLayout)findViewById(R.id.search_one_movie_view));
    this.mIconImg = ((NetworkThumbView)findViewById(R.id.movie_icon_img));
    this.mMovieEditionState = ((ImageView)findViewById(R.id.movie_edition));
    this.mMovieContent = ((LinearLayout)findViewById(R.id.movie_content));
    this.mMovieTitle = ((TextView)findViewById(R.id.movie_title));
    this.mScore = ((TextView)findViewById(R.id.score));
    this.mGradeHint = findViewById(R.id.grade_hint);
    this.mBlurBg = ((SearchBlurImageView)findViewById(R.id.blur_bg));
  }

  public void findViews()
  {
    this.mIcon = ((NetworkThumbView)findViewById(R.id.icon));
    this.mRoundMask = ((ImageView)findViewById(R.id.mask_round));
    this.mRoundCornerMask = ((ImageView)findViewById(R.id.mask_round_corner));
    this.mPicLabel = ((TextView)findViewById(R.id.pic_label));
    this.mTitle = ((TextView)findViewById(R.id.title));
    this.mLeftTopItem = ((DirectZonePartItem)findViewById(R.id.left_top));
    this.mRightTopItem = ((DirectZonePartItem)findViewById(R.id.right_top));
    this.mLeftBottomItem = ((DirectZonePartItem)findViewById(R.id.left_bottom));
    this.mRightBottomItem = ((DirectZonePartItem)findViewById(R.id.right_bottom));
    this.mDirectZone = ((NovaRelativeLayout)findViewById(R.id.direct_zone));
    this.mLinkContent = ((LinearLayout)findViewById(R.id.link_content));
    this.mDivider = findViewById(R.id.divider);
    this.mTopLine = ((LinearLayout)findViewById(R.id.top_line));
    this.mBottomLine = ((LinearLayout)findViewById(R.id.bottom_line));
    this.mTag = ((TextView)findViewById(R.id.tag));
    this.mProperty = ((TextView)findViewById(R.id.property));
    this.mRightArrow = ((ImageView)findViewById(R.id.right_arrow));
    this.mLineDivider = findViewById(R.id.line_divider);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    findViews();
    findOneMovieViews();
  }

  public void setDirectZone(SearchDirectZoneModel paramSearchDirectZoneModel, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    fetchDisplayTypeView(paramSearchDirectZoneModel.displayType);
    this.mDirectZoneModel = paramSearchDirectZoneModel;
    if (paramSearchDirectZoneModel.displayType == 6)
      setOneMovieViewData(paramSearchDirectZoneModel);
    while (paramBoolean1)
    {
      setDividerVisibility(0);
      return;
      setOtherDirectZoneData(paramSearchDirectZoneModel, paramBoolean2, paramBoolean3);
    }
    setDividerVisibility(8);
  }

  public void setDividerVisibility(int paramInt)
  {
    this.mDivider.setVisibility(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.SearchDirectZoneItem
 * JD-Core Version:    0.6.0
 */
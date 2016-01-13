package com.dianping.base.shoplist.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.ShopAndPromoListActivity;
import com.dianping.base.basic.ShowOrHideImage;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.ShopPower;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.model.GPSCoordinate;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.HashMap;
import java.util.Map;

public class ShopListItem extends NovaRelativeLayout
  implements ShowOrHideImage
{
  public static final int AD_TYPE_BG = 19;
  public static final int AD_TYPE_BG_1 = 20;
  public static final int AD_TYPE_BRAND = 16;
  public static final int AD_TYPE_FEW_RESULT = 23;
  public static final int AD_TYPE_HOT = 24;
  public static final int AD_TYPE_NORMAL = 17;
  public static final int AD_TYPE_NOT_AD = 0;
  public static final int AD_TYPE_REASON = 25;
  public static final int AD_TYPE_THUMB = 21;
  public static final int AD_TYPE_THUMB_1 = 22;
  private static final String CONST_STRING_HTTP = "http";
  private static final String CONST_STRING_WIFI = "已连接";
  public static final int LABEL_TYPE_BLUE_BORDER = 2;
  public static final int LABEL_TYPE_GRAY_BG = 5;
  public static final int LABEL_TYPE_GRAY_BORDER = 3;
  public static final int LABEL_TYPE_GREEN_BG = 4;
  public static final int LABEL_TYPE_ORANGE_BG = 6;
  public static final int LABEL_TYPE_ORANGE_BORDER = 1;
  public static final int LABEL_TYPE_TEXT = 0;
  private static final String SEARCH_LIST_WIFI = "search_list_wifi";
  public static final Integer SHOP_SIMPLE_STYLE;
  public static final Integer SHOP_TITLE_STYLE = Integer.valueOf(1);
  static Map<String, Integer> resIdMap;
  private int mAdType = 0;
  private TextView mBrandTag;
  private View mCertified;
  protected CheckBox mCheckBox;
  private TextView mFiveView;
  private TextView mFoodAuthorityTags;
  private ShopListItemIcon mIcons;
  private View mInnerLine;
  private View mItemDividerLine;
  private LinearLayout mLayoutLabel;
  private LinearLayout mLayoutWifi;
  private LinearLayout mLinearExtendedList;
  private LinearLayout mLinearTags;
  private TextView mMainTitle;
  private int mMargin;
  private ShopDataModel mModel;
  private ShopListItemNewIcon mNewIcons;
  private TextView mOtherName;
  private View mOverseaLine;
  private LinearLayout mOverseaTags;
  private ShopPower mPower;
  public DPObject mShop;
  private LinearLayout mShopTitleLine;
  private ShopListItemTags mTags;
  private TextView mTextAltName;
  private DPNetworkImageView mThumb;
  private View mThumbFrame;
  private NetworkThumbView mThumbLabel;
  private NetworkThumbView mThumbWifi;
  private ImageView mTitleUserinfo;
  private View mTopDivider;
  private TextView mTvAd;
  private TextView mTvCertifiedHairDresserInfo;
  private TextView mTvEvent;
  private TextView mTvLabel;
  private TextView mTvScore;
  private TextView mTvShopAd;
  private TextView mTvShopConsume;
  private TextView mTvShopDistance;
  private TextView mTvShopMatch;
  private TextView mTvShopTitle;
  private TextView mTvWifi;
  private View mViewLabel;
  private View mWedSelective;

  static
  {
    SHOP_SIMPLE_STYLE = Integer.valueOf(2);
    resIdMap = new HashMap();
    resIdMap.put("search_list_wifi", Integer.valueOf(R.drawable.ic_wifi));
  }

  public ShopListItem(Context paramContext)
  {
    super(paramContext);
  }

  public ShopListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void customizeStyle(Object paramObject)
  {
    int i;
    if ((paramObject != null) && (paramObject == SHOP_TITLE_STYLE))
    {
      paramObject = getResources();
      i = paramObject.getColor(R.color.shop_text_color);
      this.mTvShopTitle.setTextColor(i);
      this.mTvShopMatch.setTextColor(i);
      this.mTvShopDistance.setTextColor(i);
      this.mTvShopConsume.setTextColor(i);
      this.mOtherName.setTextColor(paramObject.getColor(R.color.tuan_common_orange));
      this.mTvShopTitle.setTextSize(0, paramObject.getDimensionPixelSize(R.dimen.text_medium_3));
      paramObject = (RelativeLayout.LayoutParams)this.mItemDividerLine.getLayoutParams();
      i = ViewUtils.dip2px(getContext(), 8.0F);
      if ((paramObject.leftMargin != this.mMargin) || (paramObject.topMargin != i) || (paramObject.height != 2))
      {
        paramObject.height = 2;
        paramObject.setMargins(this.mMargin, i, this.mMargin, 0);
      }
    }
    while (true)
    {
      return;
      if ((paramObject == null) || (paramObject != SHOP_SIMPLE_STYLE))
        break;
      paramObject = getResources();
      i = paramObject.getColor(R.color.shop_text_color);
      this.mTvShopMatch.setTextColor(i);
      this.mTvShopDistance.setTextColor(i);
      this.mTvShopTitle.setTextColor(paramObject.getColor(R.color.deep_gray));
      this.mTvShopConsume.setTextColor(paramObject.getColor(R.color.deep_gray));
      this.mOtherName.setTextColor(paramObject.getColor(R.color.tuan_common_orange));
      this.mTvShopTitle.setTextSize(0, paramObject.getDimensionPixelSize(R.dimen.basesearch_shoplist_title_text_size));
      paramObject = (RelativeLayout.LayoutParams)this.mItemDividerLine.getLayoutParams();
      if ((paramObject.leftMargin == this.mMargin) && (paramObject.topMargin == 0) && (paramObject.height == 2))
        continue;
      paramObject.height = 2;
      paramObject.setMargins(this.mMargin, 0, this.mMargin, 0);
      return;
    }
    setItemDividerLine(true, this.mModel.viewType);
  }

  private void measureShopMatch()
  {
    int k = getViewWidth(this.mTvShopDistance);
    int m = getViewWidth(this.mTvShopMatch);
    int i = ViewUtils.getViewWidth(this.mThumbFrame);
    int j = ((RelativeLayout.LayoutParams)this.mThumbFrame.getLayoutParams()).leftMargin;
    int n = ((RelativeLayout.LayoutParams)this.mThumbFrame.getLayoutParams()).rightMargin;
    n = ViewUtils.getScreenWidthPixels(getContext()) - (i + j + n);
    if (17 == this.mModel.adType)
    {
      i = getViewWidth(this.mTvAd);
      if ((this.mModel.adType != 25) || (android.text.TextUtils.isEmpty(this.mModel.adReason)))
        break label286;
    }
    label286: for (j = getViewWidth(this.mTvShopAd); ; j = 0)
    {
      if (k + m + i + ViewUtils.dip2px(getContext(), 20.0F) + j < n)
        break label291;
      m = ViewUtils.dip2px(getContext(), 20.0F);
      int i1 = (int)this.mTvShopMatch.getPaint().measureText(this.mModel.matchText) / this.mModel.matchText.length();
      i = (n - (k + i + m + j) - (int)this.mTvShopMatch.getPaint().measureText("...")) / i1;
      if ((i >= 0) && (i < this.mModel.matchText.length()))
        this.mTvShopMatch.setText(this.mModel.matchText.substring(0, i) + "...");
      return;
      i = 0;
      break;
    }
    label291: this.mTvShopMatch.setText(this.mModel.matchText);
  }

  private void setDistanceText(double paramDouble1, double paramDouble2, int paramInt)
  {
    Object localObject2 = null;
    double d = ConfigHelper.configDistanceFactor;
    Object localObject1;
    if (d <= 0.0D)
    {
      localObject1 = localObject2;
      this.mTvShopDistance.setText((CharSequence)localObject1);
      if (android.text.TextUtils.isEmpty((CharSequence)localObject1))
        break label358;
    }
    label358: for (boolean bool = true; ; bool = false)
    {
      showDistanceText(bool);
      return;
      localObject1 = localObject2;
      if (paramDouble1 == 0.0D)
        break;
      localObject1 = localObject2;
      if (paramDouble2 == 0.0D)
        break;
      localObject1 = localObject2;
      if (this.mShop.getDouble("Latitude") == 0.0D)
        break;
      localObject1 = localObject2;
      if (this.mShop.getDouble("Longitude") == 0.0D)
        break;
      paramDouble1 = new GPSCoordinate(paramDouble1, paramDouble2).distanceTo(new GPSCoordinate(this.mShop.getDouble("Latitude"), this.mShop.getDouble("Longitude"))) * d;
      localObject1 = localObject2;
      if (Double.isNaN(paramDouble1))
        break;
      localObject1 = localObject2;
      if (paramDouble1 <= 0.0D)
        break;
      int i = (int)Math.round(paramDouble1 / 10.0D) * 10;
      if (i <= 100)
      {
        localObject1 = "<100m";
        break;
      }
      if ((paramInt == 20) && (i > 1000))
      {
        localObject1 = ">1km";
        break;
      }
      if (i > 100000)
      {
        localObject1 = ">100km";
        break;
      }
      if (i >= 10000)
      {
        localObject1 = i / 1000 + "km";
        break;
      }
      if (i < 1000)
      {
        localObject1 = i + "m";
        break;
      }
      paramInt = i / 100;
      localObject1 = paramInt / 10 + "." + paramInt % 10 + "km";
      break;
    }
  }

  private void setExtendView(ShopDataModel paramShopDataModel)
  {
    this.mLinearExtendedList.removeAllViews();
    if (paramShopDataModel.viewType != 1)
      this.mInnerLine.setVisibility(8);
    while (true)
    {
      return;
      if ((paramShopDataModel.shopDealInfos == null) || (paramShopDataModel.shopDealInfos.length == 0))
      {
        this.mInnerLine.setVisibility(8);
        return;
      }
      this.mInnerLine.setVisibility(0);
      paramShopDataModel = paramShopDataModel.shopDealInfos;
      int j = paramShopDataModel.length;
      int i = 0;
      while (i < j)
      {
        DPObject localDPObject = paramShopDataModel[i];
        DealExtendedListItem localDealExtendedListItem = (DealExtendedListItem)LayoutInflater.from(getContext()).inflate(R.layout.deal_extended_item, this, false);
        localDealExtendedListItem.setData(localDPObject);
        this.mLinearExtendedList.addView(localDealExtendedListItem);
        i += 1;
      }
    }
  }

  private void setFourContentView(Object paramObject)
  {
    int j = 1;
    this.mLinearTags.setVisibility(8);
    this.mMainTitle.setVisibility(8);
    this.mOtherName.setVisibility(8);
    int i;
    if (!android.text.TextUtils.isEmpty(this.mModel.searchReason))
    {
      this.mOtherName.setText(com.dianping.util.TextUtils.highLightShow(getContext(), this.mModel.searchReason, R.color.tuan_common_orange));
      this.mOtherName.setCompoundDrawables(null, null, null, null);
      this.mOtherName.setVisibility(0);
      if ((paramObject == null) || (paramObject != SHOP_TITLE_STYLE))
        break label317;
      i = 1;
      label98: if ((paramObject == null) || (paramObject != SHOP_SIMPLE_STYLE))
        break label322;
      label109: if (i != 0)
      {
        if (android.text.TextUtils.isEmpty(this.mModel.searchReason))
          break label327;
        this.mMainTitle.setText(this.mModel.searchReason);
        this.mMainTitle.setVisibility(0);
      }
    }
    while (true)
    {
      this.mOtherName.setVisibility(8);
      if ((i != 0) || (j != 0))
      {
        if (android.text.TextUtils.isEmpty(this.mModel.searchReasonContent))
          break label365;
        this.mFiveView.setText(this.mModel.searchReasonContent);
        this.mFiveView.setVisibility(0);
      }
      return;
      if (!android.text.TextUtils.isEmpty(this.mModel.friendVisitInfo))
      {
        this.mOtherName.setText(this.mModel.friendVisitInfo);
        this.mOtherName.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.list_icon_friends), null, null, null);
        this.mOtherName.setCompoundDrawablePadding(ViewUtils.dip2px(getContext(), 4.0F));
        this.mOtherName.setVisibility(0);
        break;
      }
      if ((this.mModel.tagTextList == null) || (this.mModel.tagTextList.length <= 0))
        break;
      this.mLinearTags.setVisibility(0);
      this.mOtherName.setVisibility(8);
      break;
      label317: i = 0;
      break label98;
      label322: j = 0;
      break label109;
      label327: if (android.text.TextUtils.isEmpty(this.mModel.friendVisitInfo))
        continue;
      this.mMainTitle.setText(this.mModel.friendVisitInfo);
      this.mMainTitle.setVisibility(0);
    }
    label365: this.mFiveView.setVisibility(8);
  }

  private void setLabelLayout(NetworkThumbView paramNetworkThumbView, LinearLayout paramLinearLayout, TextView paramTextView)
  {
    if (!android.text.TextUtils.isEmpty(this.mModel.shopStatusIcon))
      if (android.text.TextUtils.substring(this.mModel.shopStatusIcon, 0, 4).equals("http"))
      {
        ViewGroup.LayoutParams localLayoutParams = paramNetworkThumbView.getLayoutParams();
        localLayoutParams.width = ViewUtils.dip2px(getContext(), 15.0F);
        localLayoutParams.height = ViewUtils.dip2px(getContext(), 15.0F);
        paramNetworkThumbView.placeholderLoading = R.drawable.placeholder_loading;
        paramNetworkThumbView.setLayoutParams(localLayoutParams);
        paramNetworkThumbView.setImage(this.mModel.shopStatusIcon);
        paramNetworkThumbView.setVisibility(0);
        label98: paramTextView.setText(this.mModel.shopStatusText);
        paramTextView.setTextColor(getResources().getColor(R.color.white));
        switch (this.mModel.shopStatusType)
        {
        default:
          paramLinearLayout.setBackgroundResource(R.color.transparent);
          paramTextView.setTextColor(getResources().getColor(R.color.shop_text_color));
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        }
      }
    while (true)
    {
      paramLinearLayout.setVisibility(0);
      return;
      paramNetworkThumbView.setLocalDrawable(getResources().getDrawable(((Integer)resIdMap.get(this.mModel.shopStatusIcon)).intValue()));
      break;
      paramNetworkThumbView.setVisibility(8);
      break label98;
      paramLinearLayout.setBackgroundResource(R.drawable.label_item_background_orange_border);
      paramTextView.setTextColor(getResources().getColor(R.color.shop_item_orange_label));
      continue;
      paramLinearLayout.setBackgroundResource(R.drawable.label_item_background_blue_border);
      paramTextView.setTextColor(getResources().getColor(R.color.shop_item_blue_label));
      continue;
      paramLinearLayout.setBackgroundResource(R.drawable.label_item_background_gray_border);
      paramTextView.setTextColor(getResources().getColor(R.color.shop_text_color));
      continue;
      paramLinearLayout.setBackgroundResource(R.drawable.label_item_background_green);
      continue;
      paramLinearLayout.setBackgroundResource(R.drawable.label_item_background_gray);
      continue;
      paramLinearLayout.setBackgroundResource(R.drawable.label_item_background_orange);
    }
  }

  private void setOtherView()
  {
    this.mTvShopTitle.requestLayout();
    if (!android.text.TextUtils.isEmpty(this.mModel.matchText))
    {
      this.mTvShopMatch.setText(this.mModel.matchText);
      if (android.text.TextUtils.isEmpty(this.mModel.certifiedHairDresserInfo))
        break label229;
      this.mTvCertifiedHairDresserInfo.setText(this.mModel.certifiedHairDresserInfo);
      this.mCertified.setVisibility(0);
      this.mViewLabel.setVisibility(0);
    }
    while (true)
    {
      Object localObject = this.mTvShopConsume;
      ((TextView)localObject).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.basesearch_shoplist_info_text_size));
      ((TextView)localObject).setText(this.mModel.priceText);
      if (!android.text.TextUtils.isEmpty(this.mModel.priceText));
      return;
      localObject = new StringBuilder();
      if (!android.text.TextUtils.isEmpty(this.mModel.regionName))
      {
        ((StringBuilder)localObject).append(this.mModel.regionName);
        if (!android.text.TextUtils.isEmpty(this.mModel.categoryName))
          ((StringBuilder)localObject).append(" ").append(this.mModel.categoryName);
      }
      while (true)
      {
        this.mTvShopMatch.setText(((StringBuilder)localObject).toString());
        break;
        if (android.text.TextUtils.isEmpty(this.mModel.categoryName))
          continue;
        ((StringBuilder)localObject).append(this.mModel.categoryName);
      }
      label229: this.mCertified.setVisibility(8);
      this.mViewLabel.setVisibility(8);
      this.mTvShopMatch.setVisibility(0);
      this.mTvCertifiedHairDresserInfo.setVisibility(8);
    }
  }

  private void setOverseaView(boolean paramBoolean)
  {
    this.mOverseaTags.setVisibility(8);
    label123: int i;
    if ((paramBoolean) && (!android.text.TextUtils.isEmpty(this.mModel.altName)))
    {
      this.mTextAltName.setVisibility(0);
      this.mTextAltName.setText("别名：" + this.mModel.altName);
      if ((android.text.TextUtils.isEmpty(this.mModel.friendVisitInfo)) && (android.text.TextUtils.isEmpty(this.mModel.searchReason)))
      {
        if (!paramBoolean)
          break label249;
        this.mTags = new ShopListItemTags(this.mOverseaTags);
        this.mTags.appendTagList(this.mModel);
      }
      TextView localTextView = this.mOtherName;
      Resources localResources = getResources();
      if (!paramBoolean)
        break label278;
      i = R.color.text_gray;
      label142: localTextView.setTextColor(localResources.getColor(i));
      if ((!paramBoolean) || ((this.mOverseaTags.getVisibility() != 0) && (this.mOtherName.getVisibility() != 0)))
        break label286;
      this.mOverseaLine.setVisibility(0);
    }
    while (true)
    {
      i = ViewUtils.getScreenWidthPixels(getContext());
      int j = ViewUtils.getViewWidth(this.mThumbFrame);
      int k = ViewUtils.dip2px(getContext(), 26.0F);
      if (this.mTags != null)
        this.mTags.setWidth(i - j - k);
      return;
      this.mTextAltName.setVisibility(8);
      break;
      label249: this.mTags = new ShopListItemTags(this.mLinearTags);
      this.mTags.appendTagList(this.mModel);
      break label123;
      label278: i = R.color.shop_text_color;
      break label142;
      label286: this.mOverseaLine.setVisibility(8);
    }
  }

  private void setSecondView()
  {
    this.mTvShopAd.setVisibility(8);
    if (!android.text.TextUtils.isEmpty(this.mModel.adReason))
    {
      this.mTvShopAd.setText(this.mModel.adReason);
      this.mTvShopAd.setVisibility(0);
      this.mTvShopAd.setMinWidth(getResources().getDimensionPixelOffset(R.dimen.basesearch_shop_adreason_minwidth));
      this.mLayoutLabel.setVisibility(8);
      this.mThumbWifi.setVisibility(8);
      this.mTvScore.setVisibility(8);
      this.mTvEvent.setVisibility(8);
      this.mLayoutWifi.setVisibility(8);
      if (android.text.TextUtils.isEmpty(this.mModel.scoreText))
        break label218;
      if (!"0".equals(this.mModel.scoreText))
        break label193;
      this.mTvScore.setVisibility(8);
      label144: this.mLayoutLabel.setVisibility(8);
    }
    label193: label218: 
    do
    {
      return;
      this.mTvShopAd.setText("");
      this.mTvShopAd.setMinWidth(getResources().getDimensionPixelOffset(R.dimen.basesearch_shop_adreason_empty_minwidth));
      this.mTvShopAd.setVisibility(8);
      break;
      this.mTvScore.setVisibility(0);
      this.mTvScore.setText(this.mModel.scoreText);
      break label144;
      if (android.text.TextUtils.isEmpty(this.mModel.shopStatusText))
        continue;
      if (this.mModel.shopStatusText.equals("已连接"))
      {
        setLabelLayout(this.mThumbWifi, this.mLayoutWifi, this.mTvWifi);
        this.mTvShopDistance.setVisibility(8);
        return;
      }
      setLabelLayout(this.mThumbLabel, this.mLayoutLabel, this.mTvLabel);
      this.mTvShopDistance.setVisibility(0);
      return;
    }
    while ((android.text.TextUtils.isEmpty(this.mModel.eventText)) || (this.mModel.viewType == 1));
    this.mTvEvent.setVisibility(0);
    this.mTvEvent.setText(this.mModel.eventText);
  }

  private void setTagMaxLength()
  {
    if (this.mModel.viewType == 1)
    {
      this.mFoodAuthorityTags.setMaxWidth(getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_thumb_height));
      this.mBrandTag.setMaxWidth(getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_thumb_height));
      return;
    }
    this.mFoodAuthorityTags.setMaxWidth(getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_tag_maxwidth));
    this.mBrandTag.setMaxWidth(getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_tag_maxwidth));
  }

  private void setTagView()
  {
    int i;
    if (this.mFoodAuthorityTags != null)
    {
      if (android.text.TextUtils.isEmpty(this.mModel.authorityLabel))
        break label367;
      this.mFoodAuthorityTags.setVisibility(0);
      localObject = (RelativeLayout.LayoutParams)this.mFoodAuthorityTags.getLayoutParams();
      ((RelativeLayout.LayoutParams)localObject).leftMargin = getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_margin_lr);
      ((RelativeLayout.LayoutParams)localObject).topMargin = getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_thumb_tag_margin_top);
      this.mFoodAuthorityTags.setText(this.mModel.authorityLabel);
      this.mFoodAuthorityTags.setTextSize(0, getResources().getDimension(R.dimen.basesearch_shoplist_thumb_tag_text_size));
      i = this.mModel.authorityLabelType;
      if (i == 1)
      {
        this.mFoodAuthorityTags.setBackgroundResource(R.drawable.basesearch_tag_michelin);
        this.mFoodAuthorityTags.setLayoutParams((ViewGroup.LayoutParams)localObject);
      }
    }
    else
    {
      label130: setTagMaxLength();
      if (this.mModel.viewType != 1)
        break label379;
      this.mIcons.setVisibility(0);
      this.mNewIcons.setVisibility(this.mModel.iconVisibility);
      setThumbSize(getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_thumb_height), getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_thumb_height));
    }
    while (true)
    {
      if ((this.mModel.viewType == 1) || ((this.mModel.iconVisibility & 0x8000) == 0))
        break label428;
      localObject = (LinearLayout.LayoutParams)this.mShopTitleLine.getLayoutParams();
      ((LinearLayout.LayoutParams)localObject).topMargin = (getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_margin_top) - ViewUtils.dip2px(getContext(), 5.0F));
      this.mShopTitleLine.setLayoutParams((ViewGroup.LayoutParams)localObject);
      return;
      if (i == 2)
      {
        this.mFoodAuthorityTags.setBackgroundResource(R.drawable.basesearch_tag_lp);
        break;
      }
      if (i == 3)
      {
        this.mFoodAuthorityTags.setBackgroundResource(R.drawable.basesearch_tag_traveling);
        break;
      }
      this.mFoodAuthorityTags.setBackgroundResource(R.drawable.hotel_shoplist_photo_toplist_tag);
      ((RelativeLayout.LayoutParams)localObject).height = ViewUtils.dip2px(getContext(), 25.0F);
      ((RelativeLayout.LayoutParams)localObject).leftMargin = (getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_margin_lr) - ViewUtils.dip2px(getContext(), 4.0F));
      this.mFoodAuthorityTags.setTextSize(0, getResources().getDimension(R.dimen.basesearch_shoplist_tag_text_size));
      break;
      label367: this.mFoodAuthorityTags.setVisibility(8);
      break label130;
      label379: this.mIcons.setVisibility(this.mModel.iconVisibility);
      this.mNewIcons.setVisibility(0);
      setThumbSize(getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_thumb_width), getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_thumb_height));
    }
    label428: Object localObject = (LinearLayout.LayoutParams)this.mShopTitleLine.getLayoutParams();
    ((LinearLayout.LayoutParams)localObject).topMargin = getResources().getDimensionPixelOffset(R.dimen.basesearch_shoplist_margin_top);
    this.mShopTitleLine.setLayoutParams((ViewGroup.LayoutParams)localObject);
  }

  private void setThumbSize(int paramInt1, int paramInt2)
  {
    ViewGroup.LayoutParams localLayoutParams = this.mThumb.getLayoutParams();
    localLayoutParams.width = paramInt1;
    localLayoutParams.height = paramInt2;
    if (paramInt1 == paramInt2);
    for (this.mThumb.placeholderLoading = R.drawable.placeholder_loading; ; this.mThumb.placeholderLoading = R.drawable.placeholder_loading_b)
    {
      this.mThumb.setLayoutParams(localLayoutParams);
      return;
    }
  }

  private void showCertifiedView()
  {
    int k = getViewWidth(this.mTvShopDistance);
    int n = getViewWidth(this.mTvCertifiedHairDresserInfo);
    int m = getViewWidth(this.mCertified);
    int i1 = getViewWidth(this.mTvShopMatch);
    int i2 = getViewWidth(this.mViewLabel);
    int i = ViewUtils.getViewWidth(this.mThumbFrame);
    int j = ((RelativeLayout.LayoutParams)this.mThumbFrame.getLayoutParams()).leftMargin;
    int i3 = ((RelativeLayout.LayoutParams)this.mThumbFrame.getLayoutParams()).rightMargin;
    i3 = ViewUtils.getScreenWidthPixels(getContext()) - (i + j + i3);
    if (17 == this.mModel.adType)
    {
      j = getViewWidth(this.mTvAd);
      if (this.mModel.adType != 25)
        break label403;
      if (Build.VERSION.SDK_INT < 16)
        break label354;
      i = this.mTvShopAd.getMinWidth();
      label151: if (k + n + m + i1 + j + i2 + ViewUtils.dip2px(getContext(), 20.0F) + i < i3)
        break label408;
      n = ViewUtils.dip2px(getContext(), 20.0F);
      i1 = (int)this.mTvCertifiedHairDresserInfo.getPaint().measureText(this.mModel.certifiedHairDresserInfo) / this.mModel.certifiedHairDresserInfo.length();
      i = (int)((i3 - (k + j + n + i) - (int)this.mTvCertifiedHairDresserInfo.getPaint().measureText("...") - m) / i1 - 0.5D);
      if ((i >= 0) && (i < this.mModel.certifiedHairDresserInfo.length()))
        this.mTvCertifiedHairDresserInfo.setText(this.mModel.certifiedHairDresserInfo.substring(0, i) + "...");
      this.mTvShopMatch.setVisibility(8);
      this.mViewLabel.setVisibility(8);
    }
    while (true)
    {
      this.mTvCertifiedHairDresserInfo.setVisibility(0);
      return;
      j = 0;
      break;
      label354: i = BitmapFactory.decodeResource(getResources(), R.drawable.ic_search_ad_rest).getWidth();
      i = (int)this.mTvShopAd.getPaint().measureText(this.mModel.adReason) + i + ViewUtils.dip2px(getContext(), 2.0F) * 2;
      break label151;
      label403: i = 0;
      break label151;
      label408: this.mTvShopMatch.setVisibility(0);
      this.mViewLabel.setVisibility(0);
    }
  }

  public int getViewWidth(View paramView)
  {
    int i = ViewUtils.getViewWidth(paramView);
    int j = ((LinearLayout.LayoutParams)paramView.getLayoutParams()).leftMargin;
    return ((LinearLayout.LayoutParams)paramView.getLayoutParams()).rightMargin + (i + j);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mThumb = ((DPNetworkImageView)findViewById(R.id.shop_pic_thumb));
    this.mThumbFrame = findViewById(R.id.thumb_frame);
    this.mPower = ((ShopPower)findViewById(R.id.shop_power));
    this.mIcons = new ShopListItemIcon(this);
    this.mNewIcons = new ShopListItemNewIcon(this);
    this.mShopTitleLine = ((LinearLayout)findViewById(R.id.shop_title));
    this.mTvShopTitle = ((TextView)findViewById(R.id.tv_shop_title));
    this.mTvShopConsume = ((TextView)findViewById(R.id.tv_shop_consume));
    this.mTvShopMatch = ((TextView)findViewById(R.id.tv_shop_match));
    this.mTvShopDistance = ((TextView)findViewById(R.id.tv_shop_distance));
    this.mOtherName = ((TextView)findViewById(R.id.other_name));
    this.mFoodAuthorityTags = ((TextView)findViewById(R.id.food_authority_tags));
    this.mTvScore = ((TextView)findViewById(R.id.tv_score));
    this.mTvEvent = ((TextView)findViewById(R.id.tv_discount));
    this.mTvAd = ((TextView)findViewById(R.id.tv_ad));
    this.mBrandTag = ((TextView)findViewById(R.id.ad_brand_tag));
    this.mTextAltName = ((TextView)findViewById(R.id.text_alt_name));
    this.mOverseaTags = ((LinearLayout)findViewById(R.id.oversea_tags));
    this.mOverseaLine = findViewById(R.id.oversea_line);
    this.mLinearTags = ((LinearLayout)findViewById(R.id.linear_tags));
    this.mInnerLine = findViewById(R.id.inner_line);
    this.mLinearExtendedList = ((LinearLayout)findViewById(R.id.extended_list));
    this.mItemDividerLine = findViewById(R.id.item_divider_line);
    this.mTvCertifiedHairDresserInfo = ((TextView)findViewById(R.id.tv_shop_certifiedHairDresser));
    this.mCertified = findViewById(R.id.ic_certified);
    this.mViewLabel = findViewById(R.id.view_label);
    this.mLayoutLabel = ((LinearLayout)findViewById(R.id.layout_label));
    this.mThumbLabel = ((NetworkThumbView)findViewById(R.id.iv_label));
    this.mTvLabel = ((TextView)findViewById(R.id.tv_label));
    this.mTitleUserinfo = ((ImageView)findViewById(R.id.iv_title_userinfo));
    this.mCheckBox = ((CheckBox)findViewById(R.id.check));
    this.mTopDivider = findViewById(R.id.top_divider);
    this.mThumbWifi = ((NetworkThumbView)findViewById(R.id.iv_wifi));
    this.mTvWifi = ((TextView)findViewById(R.id.tv_wifi));
    this.mLayoutWifi = ((LinearLayout)findViewById(R.id.layout_wifi));
    this.mTvShopAd = ((TextView)findViewById(R.id.tv_shop_ad));
    this.mWedSelective = findViewById(R.id.iv_shop_selective);
    this.mMainTitle = ((TextView)findViewById(R.id.shop_main_title));
    this.mFiveView = ((TextView)findViewById(R.id.shop_five_content));
    this.mMargin = getResources().getDimensionPixelSize(R.dimen.basesearch_shoplist_margin_lr);
  }

  public void refreshDistance(double paramDouble1, double paramDouble2)
  {
    if (this.mShop == null)
      return;
    refreshDistance(paramDouble1, paramDouble2, this.mShop.getString("DistanceText"));
  }

  public void refreshDistance(double paramDouble1, double paramDouble2, String paramString)
  {
    if (!android.text.TextUtils.isEmpty(paramString))
    {
      this.mTvShopDistance.setText(paramString);
      if (!android.text.TextUtils.isEmpty(paramString));
      for (boolean bool = true; ; bool = false)
      {
        showDistanceText(bool);
        return;
      }
    }
    setDistanceText(paramDouble1, paramDouble2, this.mAdType);
  }

  public void setAdShop(int paramInt)
  {
    this.mBrandTag.setText("品牌");
    this.mBrandTag.setBackgroundResource(R.drawable.hotel_list_icon_michelin);
    this.mTvAd.setVisibility(8);
    switch (paramInt)
    {
    default:
      this.mBrandTag.setVisibility(8);
      setAdText("推广");
      this.mTvShopMatch.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
      return;
    case 16:
      this.mBrandTag.setVisibility(0);
      setAdText("");
      this.mTvShopMatch.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
      return;
    case 0:
    case 19:
    case 20:
      this.mBrandTag.setVisibility(8);
      setAdText("");
      this.mTvShopMatch.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
      return;
    case 21:
    case 22:
      this.mBrandTag.setVisibility(8);
      setAdText("");
      if (!android.text.TextUtils.isEmpty(this.mModel.certifiedHairDresserInfo))
      {
        this.mTvCertifiedHairDresserInfo.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.search_ad_icon_rest), null);
        this.mTvCertifiedHairDresserInfo.setCompoundDrawablePadding(ViewUtils.dip2px(getContext(), 6.0F));
        this.mTvShopMatch.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        return;
      }
      this.mTvShopMatch.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.search_ad_icon_rest), null);
      this.mTvShopMatch.setCompoundDrawablePadding(ViewUtils.dip2px(getContext(), 6.0F));
      return;
    case 23:
      this.mBrandTag.setVisibility(8);
      setAdText("");
      this.mTvShopMatch.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
      return;
    case 24:
      this.mBrandTag.setVisibility(8);
      setAdText("");
      this.mTvShopMatch.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.search_ad_icon_hot), null);
      this.mTvShopMatch.setCompoundDrawablePadding(ViewUtils.dip2px(getContext(), 7.0F));
      return;
    case 25:
    }
    this.mTvShopAd.setVisibility(0);
    this.mTvShopAd.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_search_ad_rest), null);
    this.mTvShopAd.setCompoundDrawablePadding(ViewUtils.dip2px(getContext(), 2.0F));
  }

  public void setAdText(String paramString)
  {
    if (this.mTvAd != null)
    {
      if (android.text.TextUtils.isEmpty(paramString))
        this.mTvAd.setVisibility(8);
    }
    else
      return;
    this.mTvAd.setText(paramString);
    this.mTvAd.setVisibility(0);
  }

  public void setChecked(int paramInt)
  {
    CheckBox localCheckBox = this.mCheckBox;
    if (paramInt > 0);
    for (boolean bool = true; ; bool = false)
    {
      localCheckBox.setChecked(bool);
      return;
    }
  }

  public void setEditable(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mCheckBox.setVisibility(0);
      return;
    }
    this.mCheckBox.setVisibility(8);
  }

  public void setItemDividerLine(boolean paramBoolean)
  {
    if (this.mItemDividerLine == null)
      return;
    if (paramBoolean)
    {
      this.mItemDividerLine.setVisibility(0);
      return;
    }
    this.mItemDividerLine.setVisibility(8);
  }

  public void setItemDividerLine(boolean paramBoolean, int paramInt)
  {
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)this.mItemDividerLine.getLayoutParams();
    if (paramInt == 1)
    {
      paramInt = ViewUtils.dip2px(getContext(), 4.0F);
      if (localLayoutParams.height != paramInt)
      {
        localLayoutParams.height = paramInt;
        this.mItemDividerLine.setLayoutParams(localLayoutParams);
      }
      this.mItemDividerLine.setBackgroundResource(R.color.common_bk_color);
    }
    while (true)
    {
      setItemDividerLine(paramBoolean);
      return;
      if (localLayoutParams.height != 1)
      {
        localLayoutParams.height = 1;
        this.mItemDividerLine.setLayoutParams(localLayoutParams);
      }
      this.mItemDividerLine.setBackgroundResource(R.color.line_gray);
    }
  }

  public void setShop(DPObject paramDPObject, int paramInt, double paramDouble1, double paramDouble2, Activity paramActivity)
  {
    if (((paramActivity instanceof ShopAndPromoListActivity)) && (NovaConfigUtils.isShowShopImg));
    for (boolean bool = true; ; bool = false)
    {
      setShop(paramDPObject, paramInt, paramDouble1, paramDouble2, bool);
      return;
    }
  }

  public void setShop(DPObject paramDPObject, int paramInt, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    setShop(paramDPObject, paramInt, paramDouble1, paramDouble2, paramBoolean, true);
  }

  public void setShop(DPObject paramDPObject, int paramInt, double paramDouble1, double paramDouble2, boolean paramBoolean1, boolean paramBoolean2)
  {
    setShop(new ShopDataModel(paramDPObject), paramInt, paramDouble1, paramDouble2, paramBoolean1, paramBoolean2);
  }

  public void setShop(ShopDataModel paramShopDataModel, int paramInt, double paramDouble1, double paramDouble2, boolean paramBoolean1, boolean paramBoolean2)
  {
    setShop(paramShopDataModel, paramInt, paramDouble1, paramDouble2, paramBoolean1, paramBoolean2, null);
  }

  public void setShop(ShopDataModel paramShopDataModel, int paramInt, double paramDouble1, double paramDouble2, boolean paramBoolean1, boolean paramBoolean2, Object paramObject)
  {
    this.mShop = paramShopDataModel.shopObj;
    this.mModel = paramShopDataModel;
    this.mAdType = paramShopDataModel.adType;
    boolean bool;
    label49: String str;
    if (paramShopDataModel.viewType == 2)
    {
      bool = true;
      if (!paramBoolean1)
        break label250;
      this.mThumb.setImage(paramShopDataModel.defaultPic);
      this.mPower.setPower(paramShopDataModel.shopPower);
      TextView localTextView = this.mTvShopTitle;
      if (paramInt <= 0)
        break label264;
      str = paramInt + ". " + paramShopDataModel.fullName;
      label99: localTextView.setText(str);
      this.mWedSelective.setVisibility(8);
      if (paramShopDataModel.isWedSelectiveShop)
        this.mWedSelective.setVisibility(0);
      this.mTitleUserinfo.setVisibility(8);
      if (!paramShopDataModel.isWished)
        break label273;
      this.mTitleUserinfo.setImageResource(R.drawable.ic_wished);
      this.mTitleUserinfo.setVisibility(0);
      label164: setTagView();
      setOtherView();
      setFourContentView(paramObject);
      setOverseaView(bool);
      setExtendView(paramShopDataModel);
      if (!paramBoolean2)
        break label313;
      refreshDistance(paramDouble1, paramDouble2, this.mModel.distanceText);
      label208: setSecondView();
      setAdShop(paramShopDataModel.adType);
      if (android.text.TextUtils.isEmpty(this.mModel.certifiedHairDresserInfo))
        break label363;
      showCertifiedView();
    }
    while (true)
    {
      customizeStyle(paramObject);
      return;
      bool = false;
      break;
      label250: this.mThumb.setImage("");
      break label49;
      label264: str = paramShopDataModel.fullName;
      break label99;
      label273: if (paramShopDataModel.isArrived)
      {
        this.mTitleUserinfo.setImageResource(R.drawable.ic_arrived);
        this.mTitleUserinfo.setVisibility(0);
        break label164;
      }
      this.mTitleUserinfo.setVisibility(8);
      break label164;
      label313: if (!android.text.TextUtils.isEmpty(this.mModel.distanceText))
      {
        this.mTvShopDistance.setText(this.mModel.distanceText);
        this.mTvShopDistance.setVisibility(0);
        break label208;
      }
      this.mTvShopDistance.setVisibility(8);
      break label208;
      label363: if (android.text.TextUtils.isEmpty(this.mModel.matchText))
        continue;
      measureShopMatch();
    }
  }

  public void showDistanceText(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mTvShopDistance.setVisibility(0);
      return;
    }
    this.mTvShopDistance.setVisibility(8);
  }

  public void showOrHideShopImg(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mThumb.setImage(this.mShop.getString("DefaultPic"));
      this.mThumb.setVisibility(0);
      return;
    }
    this.mThumb.setVisibility(8);
  }

  public void useTopLine()
  {
    this.mTopDivider.setVisibility(0);
    setItemDividerLine(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.widget.ShopListItem
 * JD-Core Version:    0.6.0
 */
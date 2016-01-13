package com.dianping.search.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.TextView;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.ShopPower;
import com.dianping.search.shoplist.data.model.DisplayContent;
import com.dianping.search.shoplist.data.model.ExtSearchModel;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;
import java.util.HashMap;
import java.util.Map;

public class ShopListExtItem extends NovaLinearLayout
{
  Context context;
  View divider;
  LinearLayout mContent;
  TextView mDesc;
  TextView mDiscount;
  TextView mDistanceText;
  ExtSearchModel mExtModel;
  TextView mHeat;
  NovaRelativeLayout mInfoLayout;
  NetworkThumbView mLabelIcon;
  View mLine1;
  View mLine2;
  View mLine3;
  NovaLinearLayout mLinkContent;
  NetworkThumbView mLogo;
  TextView mPriceText;
  TextView mRankInfo;
  TextView mRegionText;
  View mRightArrow;
  ShopPower mStar;
  TextView mTag;
  TextView mTitle;
  TextView mTopInfo;
  TextView mTypeTag;
  View mask;
  View mask1;
  private View.OnClickListener onExtItemClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (((paramView.getTag() instanceof String)) && (!android.text.TextUtils.isEmpty((String)paramView.getTag())) && ((ShopListExtItem.this.context instanceof Activity)))
      {
        ShopListExtItem.this.sendAdGA(paramView);
        ShopListExtItem.this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView.getTag().toString())));
      }
    }
  };

  public ShopListExtItem(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
  }

  public ShopListExtItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
  }

  public ShopListExtItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.context = paramContext;
  }

  private boolean lineCountLargerThanOne(String paramString, float paramFloat, TextView paramTextView)
  {
    Paint localPaint = new Paint();
    localPaint.setTextSize(paramTextView.getTextSize());
    localPaint.setTypeface(Typeface.DEFAULT_BOLD);
    return localPaint.measureText(paramString) >= paramFloat;
  }

  private void sendAdGA(View paramView)
  {
    if ((this.mExtModel == null) || (paramView == null) || (android.text.TextUtils.isEmpty(this.mExtModel.feedback)))
      return;
    String str = "direct_zone";
    if ((paramView instanceof NovaLinearLayout))
      str = ((NovaLinearLayout)paramView).getGAString();
    while (true)
    {
      paramView = new HashMap();
      paramView.put("act", "2");
      paramView.put("adidx", String.valueOf(this.mExtModel.index + 1));
      paramView.put("module", str);
      AdClientUtils.report(this.mExtModel.feedback, paramView);
      Log.d("debug_AdGA", "Click-GA-Ext:" + str + "" + String.valueOf(this.mExtModel.index + 1));
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

  private void setLogoSizeDp(int paramInt1, int paramInt2)
  {
    ViewGroup.LayoutParams localLayoutParams = this.mLogo.getLayoutParams();
    localLayoutParams.height = ViewUtils.dip2px(this.context, paramInt2);
    localLayoutParams.width = ViewUtils.dip2px(this.context, paramInt1);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mLogo = ((NetworkThumbView)findViewById(R.id.logo));
    this.mLogo.setRoundPixels(4);
    this.mTitle = ((TextView)findViewById(R.id.title));
    this.mTag = ((TextView)findViewById(R.id.tag));
    this.mTopInfo = ((TextView)findViewById(R.id.top_info));
    this.mDesc = ((TextView)findViewById(R.id.desc));
    this.mDiscount = ((TextView)findViewById(R.id.discount));
    this.mLinkContent = ((NovaLinearLayout)findViewById(R.id.link_content));
    this.divider = findViewById(R.id.divider);
    this.mask = findViewById(R.id.mask);
    this.mRightArrow = findViewById(R.id.right_arrow);
    this.mLabelIcon = ((NetworkThumbView)findViewById(R.id.label_icon));
    this.mHeat = ((TextView)findViewById(R.id.heat));
    this.mRankInfo = ((TextView)findViewById(R.id.rank_info));
    this.mInfoLayout = ((NovaRelativeLayout)findViewById(R.id.info_layout));
    this.mask1 = findViewById(R.id.mask1);
    this.mContent = ((LinearLayout)findViewById(R.id.content));
    this.mLine1 = findViewById(R.id.line_1);
    this.mLine2 = findViewById(R.id.line_2);
    this.mLine3 = findViewById(R.id.line_3);
    this.mStar = ((ShopPower)findViewById(R.id.shop_power));
    this.mPriceText = ((TextView)findViewById(R.id.price_text));
    this.mDistanceText = ((TextView)findViewById(R.id.distance_text));
    this.mRegionText = ((TextView)findViewById(R.id.region_text));
    this.mTypeTag = ((TextView)findViewById(R.id.type_tag));
  }

  public void setDividerVisibility(int paramInt)
  {
    this.divider.setVisibility(paramInt);
  }

  public void setShopExt(ExtSearchModel paramExtSearchModel, boolean paramBoolean)
  {
    int i;
    label137: label172: label193: label230: label381: int j;
    label278: label307: label344: label481: label624: Object localObject1;
    label519: int n;
    if (!android.text.TextUtils.isEmpty(paramExtSearchModel.type))
      if ((paramExtSearchModel.displayType == 3) || (paramExtSearchModel.displayType == 4) || (paramExtSearchModel.displayType == 6))
      {
        this.mTypeTag.setText(paramExtSearchModel.type);
        this.mTypeTag.setVisibility(0);
        this.mTag.setVisibility(8);
        this.mExtModel = paramExtSearchModel;
        this.mLogo.setImage(paramExtSearchModel.logoUrl);
        this.mTitle.setText(paramExtSearchModel.title);
        i = 0;
        if (android.text.TextUtils.isEmpty(paramExtSearchModel.topInfo))
          break label906;
        this.mTopInfo.setText(com.dianping.util.TextUtils.highLightShow(getContext(), paramExtSearchModel.topInfo, R.color.tuan_common_orange));
        this.mTopInfo.setVisibility(0);
        i = 0 + 1;
        if (android.text.TextUtils.isEmpty(paramExtSearchModel.tagInfo))
          break label918;
        this.mDiscount.setText(paramExtSearchModel.tagInfo);
        this.mDiscount.setVisibility(0);
        i += 1;
        if (i <= 0)
          break label930;
        this.mDesc.setSingleLine(true);
        this.mDesc.setMaxLines(1);
        if (paramExtSearchModel.displayType != 5)
        {
          if (android.text.TextUtils.isEmpty(paramExtSearchModel.desc))
            break label949;
          this.mDesc.setText(paramExtSearchModel.desc);
          this.mDesc.setVisibility(0);
        }
        if ((paramExtSearchModel.displayType != 0) && (paramExtSearchModel.displayType != 1) && (paramExtSearchModel.displayType != 2))
          break label961;
        this.mask.setVisibility(0);
        this.mask1.setVisibility(8);
        setLogoSizeDp(56, 56);
        if (android.text.TextUtils.isEmpty(paramExtSearchModel.labelIconUrl))
          break label989;
        this.mLabelIcon.setImage(paramExtSearchModel.labelIconUrl);
        this.mLabelIcon.setVisibility(0);
        if (android.text.TextUtils.isEmpty(paramExtSearchModel.heat))
          break label1001;
        this.mLine1.setVisibility(0);
        this.mHeat.setText(paramExtSearchModel.heat);
        this.mHeat.setVisibility(0);
        if (android.text.TextUtils.isEmpty(paramExtSearchModel.rankInfo))
          break label1043;
        this.mLine1.setVisibility(0);
        this.mRankInfo.setText(paramExtSearchModel.rankInfo);
        this.mRankInfo.setVisibility(0);
        if (paramExtSearchModel.displayType != 5)
          break label1107;
        this.mRightArrow.setVisibility(8);
        this.mTitle.setSingleLine(false);
        this.mTitle.setMaxLines(2);
        this.mDesc.setSingleLine(true);
        this.mDesc.setMaxLines(1);
        i = ViewUtils.getScreenWidthPixels(this.context);
        j = ViewUtils.dip2px(this.context, 132.0F);
        if (!lineCountLargerThanOne(paramExtSearchModel.title, i - j, this.mTitle))
          break label1064;
        this.mDesc.setVisibility(8);
        this.mLinkContent.setPadding(ViewUtils.dip2px(this.context, 7.0F), ViewUtils.dip2px(this.context, 5.0F), ViewUtils.dip2px(this.context, 7.0F), 0);
        if (paramExtSearchModel.displayType != 6)
          break label1181;
        this.mLine1.setVisibility(8);
        this.mDiscount.setVisibility(8);
        this.mDesc.setVisibility(8);
        this.mTopInfo.setVisibility(8);
        this.mLine2.setVisibility(0);
        this.mLine3.setVisibility(0);
        this.mStar.setPower(paramExtSearchModel.star);
        this.mPriceText.setText(paramExtSearchModel.priceText);
        this.mRegionText.setText(paramExtSearchModel.tagInfo);
        this.mDistanceText.setText(paramExtSearchModel.distanceText);
        localObject1 = new DisplayMetrics();
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics((DisplayMetrics)localObject1);
        i = ((DisplayMetrics)localObject1).widthPixels;
        n = ViewUtils.dip2px(getContext(), 7.0F);
        this.mLinkContent.removeAllViews();
        if ((paramExtSearchModel.displayContents == null) || (paramExtSearchModel.displayContents.length <= 0))
          break label2532;
        switch (paramExtSearchModel.displayType)
        {
        case 4:
        default:
          this.mLinkContent.setVisibility(8);
          label745: if (!paramBoolean)
            break;
          setDividerVisibility(0);
        case 0:
        case 1:
        case 2:
        case 3:
        case 6:
        case 5:
        }
      }
    while (true)
    {
      this.mLinkContent.setClickable(true);
      this.mInfoLayout.setClickable(true);
      this.mInfoLayout.setGAString("direct_zone");
      this.divider.setClickable(true);
      this.mInfoLayout.setTag(paramExtSearchModel.titleUrl);
      this.mInfoLayout.setOnClickListener(this.onExtItemClickListener);
      return;
      if ((paramExtSearchModel.displayType == 0) || (paramExtSearchModel.displayType == 1) || (paramExtSearchModel.displayType == 2))
      {
        this.mTag.setText(paramExtSearchModel.type);
        this.mTag.setVisibility(0);
        this.mTypeTag.setVisibility(8);
        break;
      }
      this.mTag.setVisibility(8);
      this.mTypeTag.setVisibility(8);
      break;
      this.mTag.setVisibility(8);
      this.mTypeTag.setVisibility(8);
      break;
      label906: this.mTopInfo.setVisibility(8);
      break label137;
      label918: this.mDiscount.setVisibility(8);
      break label172;
      label930: this.mDesc.setSingleLine(false);
      this.mDesc.setMaxLines(2);
      break label193;
      label949: this.mDesc.setVisibility(8);
      break label230;
      label961: this.mask.setVisibility(8);
      this.mask1.setVisibility(0);
      setLogoSizeDp(102, 74);
      break label278;
      label989: this.mLabelIcon.setVisibility(8);
      break label307;
      label1001: if (!android.text.TextUtils.isEmpty(paramExtSearchModel.rankInfo))
      {
        this.mHeat.setVisibility(4);
        break label344;
      }
      this.mLine1.setVisibility(8);
      this.mHeat.setVisibility(8);
      break label344;
      label1043: this.mLine1.setVisibility(8);
      this.mRankInfo.setVisibility(8);
      break label381;
      label1064: if (!android.text.TextUtils.isEmpty(paramExtSearchModel.desc))
      {
        this.mDesc.setText(paramExtSearchModel.desc);
        this.mDesc.setVisibility(0);
        break label481;
      }
      this.mDesc.setVisibility(4);
      break label481;
      label1107: this.mRightArrow.setVisibility(0);
      this.mTitle.setSingleLine(true);
      this.mTitle.setMaxLines(1);
      this.mLinkContent.setPadding(ViewUtils.dip2px(this.context, 7.0F), ViewUtils.dip2px(this.context, 5.0F), ViewUtils.dip2px(this.context, 7.0F), ViewUtils.dip2px(this.context, 10.0F));
      break label519;
      label1181: this.mLine3.setVisibility(8);
      this.mLine2.setVisibility(8);
      break label624;
      this.mLinkContent.setOrientation(0);
      this.mLinkContent.setWeightSum(1.0F);
      localObject1 = (NovaLinearLayout)LayoutInflater.from(this.context).inflate(R.layout.shoplist_brand_image, null);
      j = ViewUtils.dip2px(getContext(), 3.0F);
      Object localObject2 = new LinearLayout.LayoutParams(0, (i - n * 2) * 13 / 48, 1.0F);
      ((LinearLayout.LayoutParams)localObject2).setMargins(j, 0, j, 0);
      ((NovaLinearLayout)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      ((NetworkImageView)((NovaLinearLayout)localObject1).findViewById(R.id.image)).setImage(paramExtSearchModel.displayContents[0].picUrl);
      ((NovaLinearLayout)localObject1).setTag(paramExtSearchModel.displayContents[0].titleUrl);
      ((NovaLinearLayout)localObject1).setOnClickListener(this.onExtItemClickListener);
      ((NovaLinearLayout)localObject1).setGAString("direct_zone_1");
      ((NovaLinearLayout)localObject1).gaUserInfo.query_id = paramExtSearchModel.queryid;
      this.mLinkContent.addView((View)localObject1);
      this.mLinkContent.setVisibility(0);
      break label745;
      this.mLinkContent.setOrientation(0);
      j = paramExtSearchModel.displayContents.length;
      this.mLinkContent.setWeightSum(j);
      int i2 = ViewUtils.dip2px(getContext(), 3.0F);
      localObject1 = new LinearLayout.LayoutParams(0, ((i - n * 2) / j - i2 * 2) * 3 / 4, 1.0F);
      ((LinearLayout.LayoutParams)localObject1).setMargins(i2, 0, i2, 0);
      i = 0;
      while (i < j)
      {
        localObject2 = (NovaLinearLayout)LayoutInflater.from(this.context).inflate(R.layout.shoplist_brand_image, null);
        localObject3 = (NetworkImageView)((NovaLinearLayout)localObject2).findViewById(R.id.image);
        ((NetworkImageView)localObject3).setRoundPixels(4);
        localObject4 = (TextView)((NovaLinearLayout)localObject2).findViewById(R.id.intro);
        ((NovaLinearLayout)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject1);
        ((NetworkImageView)localObject3).setImage(paramExtSearchModel.displayContents[i].picUrl);
        ((TextView)localObject4).setText(paramExtSearchModel.displayContents[i].title);
        ((TextView)localObject4).setVisibility(0);
        ((NovaLinearLayout)localObject2).setTag(paramExtSearchModel.displayContents[i].titleUrl);
        ((NovaLinearLayout)localObject2).setOnClickListener(this.onExtItemClickListener);
        ((NovaLinearLayout)localObject2).setGAString("direct_zone_" + (i + 1));
        ((NovaLinearLayout)localObject2).gaUserInfo.query_id = paramExtSearchModel.queryid;
        ((NovaLinearLayout)localObject2).gaUserInfo.index = Integer.valueOf(i);
        this.mLinkContent.addView((View)localObject2);
        i += 1;
      }
      this.mLinkContent.setVisibility(0);
      break label745;
      this.mLinkContent.setOrientation(1);
      int k = ViewUtils.dip2px(getContext(), 3.0F);
      n = paramExtSearchModel.displayContents.length;
      i = 0;
      if (i < n)
      {
        localObject1 = (NovaTextView)LayoutInflater.from(this.context).inflate(R.layout.shoplist_brand_text, null);
        if (i < n - 1)
        {
          localObject2 = new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 36.0F));
          ((LinearLayout.LayoutParams)localObject2).setMargins(k, 0, k, ViewUtils.dip2px(getContext(), 10.0F));
          ((NovaTextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
        }
        while (true)
        {
          ((NovaTextView)localObject1).setText(paramExtSearchModel.displayContents[i].title);
          ((NovaTextView)localObject1).setTag(paramExtSearchModel.displayContents[i].titleUrl);
          ((NovaTextView)localObject1).setOnClickListener(this.onExtItemClickListener);
          ((NovaTextView)localObject1).setGAString("direct_zone_" + (i + 1));
          ((NovaTextView)localObject1).gaUserInfo.query_id = paramExtSearchModel.queryid;
          ((NovaTextView)localObject1).gaUserInfo.index = Integer.valueOf(i);
          this.mLinkContent.addView((View)localObject1);
          i += 1;
          break;
          localObject2 = new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 36.0F));
          ((LinearLayout.LayoutParams)localObject2).setMargins(k, 0, k, 0);
          ((NovaTextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
        }
      }
      this.mLinkContent.setVisibility(0);
      break label745;
      this.mLinkContent.setOrientation(0);
      k = paramExtSearchModel.displayContents.length;
      this.mLinkContent.setWeightSum(k);
      i = ViewUtils.dip2px(getContext(), 5.0F);
      int i1 = ViewUtils.dip2px(getContext(), 3.0F);
      localObject2 = new LinearLayout.LayoutParams(0, -2, 1.0F);
      ((LinearLayout.LayoutParams)localObject2).setMargins(i, 0, i, 0);
      Object localObject3 = new LinearLayout.LayoutParams(0, -2, 1.0F);
      ((LinearLayout.LayoutParams)localObject3).setMargins(i1, 0, i, 0);
      Object localObject4 = new LinearLayout.LayoutParams(0, -2, 1.0F);
      ((LinearLayout.LayoutParams)localObject4).setMargins(i, 0, i1, 0);
      i = 0;
      if (i < k)
      {
        NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)LayoutInflater.from(this.context).inflate(R.layout.shoplist_ext_btn, null);
        label2080: TextView localTextView;
        NetworkThumbView localNetworkThumbView;
        if (i == 0)
        {
          localNovaLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject3);
          localObject1 = (TextView)localNovaLinearLayout.findViewById(R.id.content);
          localTextView = (TextView)localNovaLinearLayout.findViewById(R.id.num);
          localNetworkThumbView = (NetworkThumbView)localNovaLinearLayout.findViewById(R.id.icon);
          ((TextView)localObject1).setText(paramExtSearchModel.displayContents[i].title);
          i1 = paramExtSearchModel.displayContents[i].count;
          localObject1 = String.valueOf(i1);
          if (i1 > 99)
            localObject1 = "99+";
          if (i1 <= 0)
            break label2317;
          localTextView.setText((CharSequence)localObject1);
          localTextView.setVisibility(0);
        }
        while (true)
        {
          localNetworkThumbView.setImage(paramExtSearchModel.displayContents[i].iconUrl);
          localNovaLinearLayout.setTag(paramExtSearchModel.displayContents[i].titleUrl);
          localNovaLinearLayout.setOnClickListener(this.onExtItemClickListener);
          localNovaLinearLayout.setGAString("direct_zone_" + (i + 1));
          localNovaLinearLayout.gaUserInfo.query_id = paramExtSearchModel.queryid;
          localNovaLinearLayout.gaUserInfo.index = Integer.valueOf(i);
          this.mLinkContent.addView(localNovaLinearLayout);
          i += 1;
          break;
          if (i == k - 1)
          {
            localNovaLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject4);
            break label2080;
          }
          localNovaLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject2);
          break label2080;
          label2317: localTextView.setVisibility(8);
        }
      }
      this.mLinkContent.setVisibility(0);
      break label745;
      this.mLinkContent.setOrientation(1);
      int m = paramExtSearchModel.displayContents.length;
      i = 0;
      while (i < m)
      {
        localObject1 = new ImageView(this.context);
        ((ImageView)localObject1).setLayoutParams(new ViewGroup.LayoutParams(-1, 1));
        ((ImageView)localObject1).setBackgroundResource(R.drawable.divider_gray_horizontal_line);
        this.mLinkContent.addView((View)localObject1);
        localObject1 = (ShopListRankContentItem)LayoutInflater.from(this.context).inflate(R.layout.shoplist_rank_content_item, null);
        ((ShopListRankContentItem)localObject1).setContent(paramExtSearchModel.displayContents[i]);
        ((ShopListRankContentItem)localObject1).setTag(paramExtSearchModel.displayContents[i].titleUrl);
        ((ShopListRankContentItem)localObject1).setOnClickListener(this.onExtItemClickListener);
        ((ShopListRankContentItem)localObject1).setGAString("direct_zone_" + (i + 1));
        ((ShopListRankContentItem)localObject1).gaUserInfo.query_id = paramExtSearchModel.queryid;
        ((ShopListRankContentItem)localObject1).gaUserInfo.index = Integer.valueOf(i);
        this.mLinkContent.addView((View)localObject1);
        i += 1;
      }
      this.mLinkContent.setVisibility(0);
      break label745;
      label2532: this.mLinkContent.setVisibility(8);
      break label745;
      setDividerVisibility(8);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.ShopListExtItem
 * JD-Core Version:    0.6.0
 */
package com.dianping.search.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.search.shoplist.data.model.DisplayContent;
import com.dianping.search.shoplist.data.model.ShopRankModel;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;

public class ShopListRankItem extends NovaLinearLayout
{
  TextView mDesc;
  NovaRelativeLayout mDirectZone;
  View mDivider;
  TextView mHeat;
  NetworkThumbView mLabelIcon;
  View mLine;
  LinearLayout mLinkContent;
  NetworkThumbView mLogo;
  View mMask;
  TextView mRankInfo;
  TextView mTag;
  TextView mTitle;
  private View.OnClickListener onRankClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (((paramView.getTag() instanceof String)) && (!TextUtils.isEmpty((String)paramView.getTag())) && ((ShopListRankItem.this.getContext() instanceof Activity)))
        ShopListRankItem.this.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView.getTag().toString())));
    }
  };

  public ShopListRankItem(Context paramContext)
  {
    super(paramContext);
  }

  public ShopListRankItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ShopListRankItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private boolean lineCountLargerThanOne(String paramString, float paramFloat, TextView paramTextView)
  {
    Paint localPaint = new Paint();
    localPaint.setTextSize(paramTextView.getTextSize());
    localPaint.setTypeface(Typeface.DEFAULT_BOLD);
    return localPaint.measureText(paramString) >= paramFloat;
  }

  public void onFinishInflate()
  {
    super.onFinishInflate();
    this.mDirectZone = ((NovaRelativeLayout)findViewById(R.id.direct_zone));
    this.mLogo = ((NetworkThumbView)findViewById(R.id.logo));
    this.mLabelIcon = ((NetworkThumbView)findViewById(R.id.label_icon));
    this.mMask = findViewById(R.id.mask);
    this.mTitle = ((TextView)findViewById(R.id.title));
    this.mTag = ((TextView)findViewById(R.id.tag));
    this.mDesc = ((TextView)findViewById(R.id.desc));
    this.mHeat = ((TextView)findViewById(R.id.heat));
    this.mRankInfo = ((TextView)findViewById(R.id.rank_info));
    this.mLine = findViewById(R.id.line);
    this.mDivider = findViewById(R.id.divider);
  }

  public void setDividerVisibility(int paramInt)
  {
    this.mLine.setVisibility(paramInt);
    this.mDivider.setVisibility(paramInt);
  }

  public void setRank(ShopRankModel paramShopRankModel, boolean paramBoolean)
  {
    label66: label95: int i;
    int j;
    if (!TextUtils.isEmpty(paramShopRankModel.logoUrl))
    {
      this.mLogo.setImage(paramShopRankModel.logoUrl);
      this.mLogo.setVisibility(0);
      this.mMask.setVisibility(0);
      if (TextUtils.isEmpty(paramShopRankModel.labelIconUrl))
        break label435;
      this.mLabelIcon.setImage(paramShopRankModel.labelIconUrl);
      this.mLabelIcon.setVisibility(0);
      if (TextUtils.isEmpty(paramShopRankModel.title))
        break label447;
      this.mTitle.setText(paramShopRankModel.title);
      this.mTitle.setVisibility(0);
      i = ViewUtils.getScreenWidthPixels(getContext());
      j = ViewUtils.dip2px(getContext(), 132.0F);
      if (!lineCountLargerThanOne(paramShopRankModel.title, i - j, this.mTitle))
        break label459;
      this.mDesc.setVisibility(8);
      label145: if (TextUtils.isEmpty(paramShopRankModel.heat))
        break label502;
      this.mHeat.setText(paramShopRankModel.heat);
      this.mHeat.setVisibility(0);
      label174: if (TextUtils.isEmpty(paramShopRankModel.rankInfo))
        break label514;
      this.mRankInfo.setText(paramShopRankModel.rankInfo);
      this.mRankInfo.setVisibility(0);
    }
    while (true)
    {
      this.mDirectZone.setTag(paramShopRankModel.titleUrl);
      this.mDirectZone.setGAString("direct_zone");
      this.mDirectZone.setOnClickListener(this.onRankClickListener);
      setClickable(true);
      j = paramShopRankModel.displayContents.length;
      i = 0;
      while (i < j)
      {
        Object localObject = new ImageView(getContext());
        ((ImageView)localObject).setLayoutParams(new ViewGroup.LayoutParams(-1, 1));
        ((ImageView)localObject).setBackgroundResource(R.drawable.divider_gray_horizontal_line);
        this.mLinkContent.addView((View)localObject);
        localObject = (ShopListRankContentItem)LayoutInflater.from(getContext()).inflate(R.layout.shoplist_rank_content_item, null);
        ((ShopListRankContentItem)localObject).setContent(paramShopRankModel.displayContents[i]);
        ((ShopListRankContentItem)localObject).setTag(paramShopRankModel.displayContents[i].titleUrl);
        ((ShopListRankContentItem)localObject).setOnClickListener(this.onRankClickListener);
        ((ShopListRankContentItem)localObject).setGAString("direct_zone_" + (i + 1));
        ((ShopListRankContentItem)localObject).gaUserInfo.query_id = paramShopRankModel.queryid;
        ((ShopListRankContentItem)localObject).gaUserInfo.index = Integer.valueOf(i);
        this.mLinkContent.addView((View)localObject);
        i += 1;
      }
      this.mLogo.setVisibility(8);
      this.mMask.setVisibility(8);
      break;
      label435: this.mLabelIcon.setVisibility(8);
      break label66;
      label447: this.mTitle.setVisibility(8);
      break label95;
      label459: if (!TextUtils.isEmpty(paramShopRankModel.desc))
      {
        this.mDesc.setText(paramShopRankModel.desc);
        this.mDesc.setVisibility(0);
        break label145;
      }
      this.mDesc.setVisibility(4);
      break label145;
      label502: this.mHeat.setVisibility(8);
      break label174;
      label514: this.mRankInfo.setVisibility(8);
    }
    this.mLinkContent.setVisibility(0);
    if (paramBoolean)
    {
      setDividerVisibility(0);
      return;
    }
    setDividerVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.ShopListRankItem
 * JD-Core Version:    0.6.0
 */
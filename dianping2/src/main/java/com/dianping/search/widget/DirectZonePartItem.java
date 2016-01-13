package com.dianping.search.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.ShopPower;
import com.dianping.search.shoplist.data.model.DirectZoneItemModel;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.integer;
import com.dianping.widget.view.NovaLinearLayout;

public class DirectZonePartItem extends NovaLinearLayout
{
  private NetworkThumbView mIcon;
  private ShopPower mPower;
  private TextView mText;
  private int mTextBgRes = R.drawable.background_round_textview_lightred;
  private int mTextColor = R.color.shop_text_color;

  public DirectZonePartItem(Context paramContext)
  {
    super(paramContext);
  }

  public DirectZonePartItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public DirectZonePartItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public void initColor(int paramInt1, int paramInt2)
  {
    if (paramInt1 > 0)
      this.mTextColor = paramInt1;
    if (paramInt2 > 0)
      this.mTextBgRes = paramInt2;
  }

  public void onFinishInflate()
  {
    super.onFinishInflate();
    this.mIcon = ((NetworkThumbView)findViewById(R.id.icon));
    this.mPower = ((ShopPower)findViewById(R.id.power));
    this.mText = ((TextView)findViewById(R.id.text));
  }

  public void setDirectZoneItem(DirectZoneItemModel paramDirectZoneItemModel)
  {
    DirectZoneItemModel localDirectZoneItemModel = paramDirectZoneItemModel;
    if (paramDirectZoneItemModel == null)
      localDirectZoneItemModel = new DirectZoneItemModel();
    if (!android.text.TextUtils.isEmpty(localDirectZoneItemModel.text))
    {
      this.mText.setText(com.dianping.util.TextUtils.highLightShow(getContext(), localDirectZoneItemModel.text, R.color.tuan_common_orange));
      this.mText.setVisibility(0);
    }
    while (true)
      switch (localDirectZoneItemModel.type)
      {
      default:
        return;
        this.mText.setText("");
        this.mText.setVisibility(4);
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      }
    setVisibility(0);
    setIconImage(localDirectZoneItemModel.iconUrl);
    this.mPower.setVisibility(8);
    this.mText.setBackgroundResource(R.color.transparent);
    this.mText.setTextColor(getResources().getColor(this.mTextColor));
    this.mText.setTextSize(0, getResources().getDimension(R.dimen.basesearch_shoplist_info_text_size));
    return;
    setVisibility(0);
    setIconImage(localDirectZoneItemModel.iconUrl);
    this.mPower.setVisibility(8);
    this.mText.setBackgroundResource(this.mTextBgRes);
    this.mText.setTextColor(getResources().getColor(R.color.light_red));
    this.mText.setTextSize(0, getResources().getDimension(R.dimen.basesearch_shoplist_tag_text_size));
    return;
    setVisibility(0);
    this.mIcon.setVisibility(8);
    if ((!android.text.TextUtils.isEmpty(localDirectZoneItemModel.iconUrl)) && (android.text.TextUtils.isDigitsOnly(localDirectZoneItemModel.iconUrl)))
    {
      this.mPower.setPower(Integer.parseInt(localDirectZoneItemModel.iconUrl));
      this.mPower.setVisibility(0);
    }
    this.mText.setBackgroundResource(R.color.transparent);
    return;
    setVisibility(0);
    setIconImage(localDirectZoneItemModel.iconUrl);
    this.mPower.setVisibility(8);
    this.mText.setBackgroundResource(R.drawable.background_round_textview_lightred_solid);
    this.mText.setTextColor(getResources().getColor(R.color.white));
    this.mText.setTextSize(0, getResources().getDimension(R.dimen.basesearch_shoplist_tag_text_size));
    return;
    setVisibility(4);
    return;
    setVisibility(0);
    this.mPower.setVisibility(8);
    this.mText.setBackgroundResource(R.drawable.ic_text_ribbon);
    this.mText.setTextColor(getResources().getColor(R.color.white));
    this.mText.setTextSize(0, getResources().getDimension(R.dimen.text_size_11));
  }

  public void setDoubleLine()
  {
    this.mText.setSingleLine(false);
    this.mText.setMaxLines(getResources().getInteger(R.integer.search_direct_zone_multi_line));
  }

  protected void setIconImage(String paramString)
  {
    if (!android.text.TextUtils.isEmpty(paramString))
    {
      this.mIcon.setImage(paramString);
      this.mIcon.setVisibility(0);
      return;
    }
    this.mIcon.setVisibility(8);
  }

  public void setSingleLine()
  {
    this.mText.setSingleLine(true);
    this.mText.setMaxLines(1);
  }

  public void setTextMaxWidth(int paramInt)
  {
    this.mText.setMaxWidth(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.DirectZonePartItem
 * JD-Core Version:    0.6.0
 */
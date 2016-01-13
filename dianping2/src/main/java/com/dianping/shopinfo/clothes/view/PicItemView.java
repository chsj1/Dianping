package com.dianping.shopinfo.clothes.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaRelativeLayout;

public class PicItemView extends NovaRelativeLayout
{
  public NetworkImageView iconView;

  public PicItemView(Context paramContext)
  {
    super(paramContext);
  }

  public PicItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public static PicItemView createView(Context paramContext, ViewGroup paramViewGroup)
  {
    return (PicItemView)LayoutInflater.from(paramContext).inflate(R.layout.shopinfo_clothes_recommendpic_item, paramViewGroup, false);
  }

  public void init(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
      this.iconView.setImage(paramString);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.iconView = ((NetworkImageView)findViewById(R.id.item_photo));
    int i = (ViewUtils.getScreenWidthPixels(getContext()) - 100) / 3;
    this.iconView.getLayoutParams().width = i;
    this.iconView.getLayoutParams().height = i;
    ((NovaRelativeLayout)findViewById(R.id.recommend_lay)).setBackgroundColor(getResources().getColor(R.color.inner_divider));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.clothes.view.PicItemView
 * JD-Core Version:    0.6.0
 */
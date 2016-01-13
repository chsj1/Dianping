package com.dianping.movie.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.BaseBannerView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.widget.NavigationDot;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieBannerView extends BaseBannerView
  implements View.OnClickListener
{
  private final int DEFAULT_HEIGHT = ViewUtils.dip2px(getContext(), 50.0F);
  private int bannerHeight = 0;
  private final View.OnClickListener defaultOnBtnCloseClickListener = new MovieBannerView.1(this);
  private OnBannerClickGA onBannerClickGA = new MovieBannerView.2(this);

  public MovieBannerView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieBannerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setBtnOnCloseListener(this.defaultOnBtnCloseClickListener);
    setNaviDotGravity(81);
    hideCloseButton();
  }

  public void onClick(View paramView)
  {
    try
    {
      paramView = (DPObject)paramView.getTag();
      switch (paramView.getInt("CellType"))
      {
      case 2:
      case 1:
      case 3:
      case 4:
      }
      while (this.onBannerClickGA != null)
      {
        this.onBannerClickGA.onBannerClick(paramView);
        return;
        int i = paramView.getInt("CellID");
        Object localObject = paramView.getString("CellData");
        localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuanhome?category=" + i + "&screening=" + (String)localObject));
        ((Intent)localObject).addFlags(67108864);
        getContext().startActivity((Intent)localObject);
        continue;
        localObject = paramView.getString("Extra");
        localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + URLEncoder.encode((String)localObject)));
        getContext().startActivity((Intent)localObject);
        continue;
        localObject = paramView.getString("Extra");
        if ((TextUtils.isEmpty((CharSequence)localObject)) || (!((String)localObject).startsWith("dianping://")))
          continue;
        getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)localObject)));
        continue;
      }
      return;
    }
    catch (java.lang.Exception paramView)
    {
    }
  }

  public void onPageScrollStateChanged(int paramInt)
  {
  }

  public void onPageSelected(int paramInt)
  {
    this.mNaviDot.setCurrentIndex(paramInt);
  }

  public void setBanner(DPObject[] paramArrayOfDPObject)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0))
      return;
    int i = 0;
    Object localObject1 = paramArrayOfDPObject;
    if (paramArrayOfDPObject.length == 2)
    {
      localObject1 = new ArrayList();
      ((ArrayList)localObject1).addAll(Arrays.asList(paramArrayOfDPObject));
      localObject1 = (DPObject[])((ArrayList)localObject1).toArray(new DPObject[0]);
      i = 1;
    }
    stopAutoFlip();
    this.mImageViews.clear();
    int k = 0;
    int m = localObject1.length;
    int j = 0;
    if (j < m)
    {
      Object localObject3 = localObject1[j];
      Object localObject2 = localObject3.getString("ImageUrl");
      paramArrayOfDPObject = localObject3.getString("Title");
      FrameLayout.LayoutParams localLayoutParams;
      if ((!TextUtils.isEmpty((CharSequence)localObject2)) && (((String)localObject2).startsWith("http://")))
      {
        paramArrayOfDPObject = new MovieBannerView.BannerNetworkImageView(this, getContext());
        localLayoutParams = new FrameLayout.LayoutParams(-1, -1, 17);
        ((MovieBannerView.BannerNetworkImageView)paramArrayOfDPObject).setLayoutParams(localLayoutParams);
        ((MovieBannerView.BannerNetworkImageView)paramArrayOfDPObject).setScaleType(ImageView.ScaleType.FIT_XY);
        ((MovieBannerView.BannerNetworkImageView)paramArrayOfDPObject).setImage((String)localObject2);
      }
      while (true)
      {
        paramArrayOfDPObject.setTag(localObject3);
        paramArrayOfDPObject.setOnClickListener(this);
        this.mImageViews.add(paramArrayOfDPObject);
        k += 1;
        j += 1;
        break;
        localObject2 = new TextView(getContext());
        localLayoutParams = new FrameLayout.LayoutParams(-1, -1, 17);
        localLayoutParams.setMargins(ViewUtils.dip2px(getContext(), 5.0F), 0, ViewUtils.dip2px(getContext(), 5.0F), 0);
        ((TextView)localObject2).setLayoutParams(localLayoutParams);
        ((TextView)localObject2).setGravity(17);
        ((TextView)localObject2).setTextColor(getResources().getColor(R.color.text_color_gray));
        ((TextView)localObject2).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_info));
        ((TextView)localObject2).setText(paramArrayOfDPObject + "");
        paramArrayOfDPObject = (DPObject)localObject2;
        if (this.bannerHeight >= this.DEFAULT_HEIGHT)
          continue;
        this.bannerHeight = this.DEFAULT_HEIGHT;
        this.mPager.setLayoutParams(new FrameLayout.LayoutParams(-1, this.bannerHeight, 1));
        paramArrayOfDPObject = (DPObject)localObject2;
      }
    }
    if (i != 0);
    this.mNaviDot.setTotalDot(k);
    if (k == 1)
      ViewUtils.hideView(this.mNaviDot, true);
    while (true)
    {
      this.mPager.getAdapter().notifyDataSetChanged();
      if (k <= 1)
        break;
      startAutoFlip();
      return;
      ViewUtils.showView(this.mNaviDot);
    }
  }

  public void setNaviDotGravity(int paramInt)
  {
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, -2, paramInt);
    localLayoutParams.setMargins(0, 0, 0, ViewUtils.dip2px(getContext(), 5.0F));
    this.mNaviDot.setLayoutParams(localLayoutParams);
  }

  public void setOnBannerClickGA(OnBannerClickGA paramOnBannerClickGA)
  {
    this.onBannerClickGA = paramOnBannerClickGA;
  }

  public void showCloseBtn()
  {
    ViewUtils.showView(this.mBtnClose);
  }

  public static abstract interface OnBannerClickGA
  {
    public abstract void onBannerClick(DPObject paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.MovieBannerView
 * JD-Core Version:    0.6.0
 */
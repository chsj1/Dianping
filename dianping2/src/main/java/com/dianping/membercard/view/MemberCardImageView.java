package com.dianping.membercard.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import com.dianping.cache.DPCache;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.Response;
import com.dianping.membercard.utils.RecyclingBitmapDrawable;
import com.dianping.membercard.utils.RecyclingNetworkImageView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.styleable;
import java.io.PrintStream;

public class MemberCardImageView extends RecyclingNetworkImageView
{
  private boolean isHalf;
  private boolean isPrepaidCard;

  public MemberCardImageView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MemberCardImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public MemberCardImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.isPhoto = true;
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MemberCardImageView);
    this.isHalf = paramContext.getBoolean(R.styleable.MemberCardImageView_isHalf, false);
    this.isPrepaidCard = paramContext.getBoolean(R.styleable.MemberCardImageView_isPrepaidCard, false);
    paramContext.recycle();
  }

  private Bitmap cropHalfBitmap(Bitmap paramBitmap)
  {
    return Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight() * 2 / 5, null, false);
  }

  private Bitmap decodeBitmap(int paramInt)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inPreferredConfig = Bitmap.Config.ALPHA_8;
    localOptions.inScaled = true;
    return BitmapFactory.decodeResource(getContext().getResources(), paramInt, localOptions);
  }

  private int findDrawable(String paramString)
  {
    int j = 0;
    int i = paramString.lastIndexOf("/");
    if (i < 0)
      i = R.drawable.mc_card_bg2_default;
    while (true)
    {
      return i;
      paramString = paramString.substring(i + 1);
      paramString = paramString.substring(0, paramString.indexOf(".")).replace("mc_card_bg_", "").replace("-half", "");
      try
      {
        int k = Integer.valueOf(paramString).intValue();
        i = j;
        if (k > 8)
          continue;
        i = j;
        if (k == 0)
          continue;
        switch (k)
        {
        default:
          return 0;
        case 1:
          return R.drawable.mc_card_bg2_1;
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        }
      }
      catch (java.lang.Exception paramString)
      {
        return 0;
      }
    }
    return R.drawable.mc_card_bg2_2;
    return R.drawable.mc_card_bg2_3;
    return R.drawable.mc_card_bg2_4;
    return R.drawable.mc_card_bg2_5;
    return R.drawable.mc_card_bg2_6;
    return R.drawable.mc_card_bg2_7;
    return R.drawable.mc_card_bg2_8;
  }

  private Bitmap scaleBitmap(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
      localObject = null;
    int i;
    int j;
    int k;
    do
    {
      return localObject;
      if (paramBitmap.isRecycled())
        return null;
      localObject = getResources().getDisplayMetrics();
      f1 = ((DisplayMetrics)localObject).density;
      i = paramBitmap.getWidth();
      j = paramBitmap.getHeight();
      k = (int)((((DisplayMetrics)localObject).widthPixels - 30.0F * f1) / 481.0D * 502.0D);
      localObject = paramBitmap;
    }
    while (k == i);
    int m = j * k / i;
    float f1 = k / i;
    float f2 = m / j;
    Object localObject = new Matrix();
    ((Matrix)localObject).postScale(f1, f2);
    return (Bitmap)Bitmap.createBitmap(paramBitmap, 0, 0, i, j, (Matrix)localObject, false);
  }

  private BitmapDrawable transBitmapToDrawable(Bitmap paramBitmap)
  {
    if (Build.VERSION.SDK_INT >= 11)
      return new BitmapDrawable(getResources(), paramBitmap);
    return new RecyclingBitmapDrawable(getResources(), paramBitmap);
  }

  public boolean isHalf()
  {
    return this.isHalf;
  }

  public boolean isPrepaidCard()
  {
    return this.isPrepaidCard;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    try
    {
      super.onDraw(paramCanvas);
      return;
    }
    catch (java.lang.Exception paramCanvas)
    {
      System.out.println("MyImageView  -> onDraw() Canvas: trying to use a recycled bitmap");
    }
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    Bitmap localBitmap;
    if ((Boolean.FALSE == this.imageRetrieve) && (paramRequest == this.request))
    {
      paramResponse = scaleBitmap((Bitmap)paramResponse.result());
      localBitmap = cropHalfBitmap(paramResponse);
      DPCache.getInstance().put(paramRequest.url(), null, transBitmapToDrawable(paramResponse), 31539600000L);
      DPCache.getInstance().put(paramRequest.url() + "-half", null, transBitmapToDrawable(localBitmap), 31539600000L);
      if (!this.isHalf)
        break label132;
    }
    label132: for (paramRequest = localBitmap; ; paramRequest = paramResponse)
    {
      setImageDrawable(transBitmapToDrawable(paramRequest));
      this.imageRetrieve = Boolean.valueOf(true);
      this.request = null;
      return;
    }
  }

  public void setImage(String paramString)
  {
    if (paramString == null)
    {
      super.setImageDrawable(null);
      return;
    }
    String str = paramString;
    if (!this.isPrepaidCard)
      str = paramString.replace("/bg/", "/bg2/");
    if (this.isHalf);
    Object localObject;
    for (paramString = str + "-half"; ; paramString = str)
    {
      localObject = DPCache.getInstance().getDrawable(paramString, null, 31539600000L);
      if (localObject == null)
        break;
      setImageDrawable((Drawable)localObject);
      this.url = paramString;
      this.imageRetrieve = Boolean.valueOf(true);
      return;
    }
    int i = findDrawable(paramString);
    if (i != 0)
    {
      paramString = decodeBitmap(i);
      if (paramString == null)
      {
        setImageDrawable(null);
        return;
      }
      paramString = scaleBitmap(paramString);
      localObject = cropHalfBitmap(paramString);
      if (this.isHalf)
        paramString = (String)localObject;
      while (true)
      {
        setImageDrawable(transBitmapToDrawable(paramString));
        this.imageRetrieve = Boolean.valueOf(true);
        this.url = str;
        return;
      }
    }
    super.setImage(str);
  }

  public void setIsHalf(boolean paramBoolean)
  {
    this.isHalf = paramBoolean;
  }

  public void setPlaceHolder(int paramInt)
  {
    if (paramInt <= 0)
    {
      super.setPlaceHolder(paramInt);
      return;
    }
    Object localObject = decodeBitmap(paramInt);
    if (localObject == null)
    {
      setImageResource(R.drawable.mc_card_bg2_default);
      this.imageRetrieve = Boolean.valueOf(true);
      return;
    }
    localObject = scaleBitmap((Bitmap)localObject);
    Bitmap localBitmap = cropHalfBitmap((Bitmap)localObject);
    if (this.isHalf)
      localObject = localBitmap;
    while (true)
    {
      setImageDrawable(transBitmapToDrawable((Bitmap)localObject));
      this.currentPlaceholder = true;
      return;
    }
  }

  public void setPrepaidCard(boolean paramBoolean)
  {
    this.isPrepaidCard = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.MemberCardImageView
 * JD-Core Version:    0.6.0
 */
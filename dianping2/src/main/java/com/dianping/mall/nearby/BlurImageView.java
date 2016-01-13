package com.dianping.mall.nearby;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import com.dianping.base.widget.NetworkThumbView;
import java.lang.reflect.Array;

public class BlurImageView extends NetworkThumbView
{
  public BlurImageView(Context paramContext)
  {
    super(paramContext);
  }

  public BlurImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public BlurImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public Bitmap doBlur(Bitmap paramBitmap, int paramInt)
  {
    if (paramBitmap == null)
      return null;
    if (Build.VERSION.SDK_INT >= 17)
    {
      localObject1 = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
      localObject2 = RenderScript.create(getContext());
      localObject3 = ScriptIntrinsicBlur.create((RenderScript)localObject2, Element.U8_4((RenderScript)localObject2));
      localObject4 = Allocation.createFromBitmap((RenderScript)localObject2, paramBitmap);
      localObject5 = Allocation.createFromBitmap((RenderScript)localObject2, (Bitmap)localObject1);
      ((ScriptIntrinsicBlur)localObject3).setRadius(paramInt);
      ((ScriptIntrinsicBlur)localObject3).setInput((Allocation)localObject4);
      ((ScriptIntrinsicBlur)localObject3).forEach((Allocation)localObject5);
      ((Allocation)localObject5).copyTo((Bitmap)localObject1);
      paramBitmap.recycle();
      ((RenderScript)localObject2).destroy();
      return localObject1;
    }
    paramBitmap = paramBitmap.copy(paramBitmap.getConfig(), true);
    if (paramInt < 1)
      return null;
    int i10 = paramBitmap.getWidth();
    int i11 = paramBitmap.getHeight();
    Object localObject1 = new int[i10 * i11];
    paramBitmap.getPixels(localObject1, 0, i10, 0, 0, i10, i11);
    int i15 = i10 - 1;
    int i12 = i11 - 1;
    int i = i10 * i11;
    int i13 = paramInt + paramInt + 1;
    Object localObject2 = new int[i];
    Object localObject3 = new int[i];
    Object localObject4 = new int[i];
    Object localObject5 = new int[Math.max(i10, i11)];
    i = i13 + 1 >> 1;
    int j = i * i;
    int k = j * 256;
    int[] arrayOfInt1 = new int[k];
    i = 0;
    while (i < k)
    {
      arrayOfInt1[i] = (i / j);
      i += 1;
    }
    int i7 = 0;
    int i5 = 0;
    int[][] arrayOfInt = (int[][])Array.newInstance(Integer.TYPE, new int[] { i13, 3 });
    int i14 = paramInt + 1;
    int i6 = 0;
    int i1;
    int i3;
    int i4;
    int n;
    int i2;
    int m;
    int i8;
    int i9;
    int[] arrayOfInt2;
    int i17;
    int i16;
    while (i6 < i11)
    {
      i1 = 0;
      i3 = 0;
      i4 = 0;
      j = 0;
      n = 0;
      i2 = 0;
      i = 0;
      k = 0;
      m = 0;
      i8 = -paramInt;
      if (i8 <= paramInt)
      {
        i9 = localObject1[(Math.min(i15, Math.max(i8, 0)) + i7)];
        arrayOfInt2 = arrayOfInt[(i8 + paramInt)];
        arrayOfInt2[0] = ((0xFF0000 & i9) >> 16);
        arrayOfInt2[1] = ((0xFF00 & i9) >> 8);
        arrayOfInt2[2] = (i9 & 0xFF);
        i9 = i14 - Math.abs(i8);
        i4 += arrayOfInt2[0] * i9;
        i3 += arrayOfInt2[1] * i9;
        i1 += arrayOfInt2[2] * i9;
        if (i8 > 0)
        {
          m += arrayOfInt2[0];
          k += arrayOfInt2[1];
          i += arrayOfInt2[2];
        }
        while (true)
        {
          i8 += 1;
          break;
          i2 += arrayOfInt2[0];
          n += arrayOfInt2[1];
          j += arrayOfInt2[2];
        }
      }
      i9 = paramInt;
      i8 = 0;
      while (i8 < i10)
      {
        localObject2[i7] = arrayOfInt1[i4];
        localObject3[i7] = arrayOfInt1[i3];
        localObject4[i7] = arrayOfInt1[i1];
        arrayOfInt2 = arrayOfInt[((i9 - paramInt + i13) % i13)];
        int i18 = arrayOfInt2[0];
        i17 = arrayOfInt2[1];
        i16 = arrayOfInt2[2];
        if (i6 == 0)
          localObject5[i8] = Math.min(i8 + paramInt + 1, i15);
        int i19 = localObject1[(localObject5[i8] + i5)];
        arrayOfInt2[0] = ((0xFF0000 & i19) >> 16);
        arrayOfInt2[1] = ((0xFF00 & i19) >> 8);
        arrayOfInt2[2] = (i19 & 0xFF);
        m += arrayOfInt2[0];
        k += arrayOfInt2[1];
        i += arrayOfInt2[2];
        i4 = i4 - i2 + m;
        i3 = i3 - n + k;
        i1 = i1 - j + i;
        i9 = (i9 + 1) % i13;
        arrayOfInt2 = arrayOfInt[(i9 % i13)];
        i2 = i2 - i18 + arrayOfInt2[0];
        n = n - i17 + arrayOfInt2[1];
        j = j - i16 + arrayOfInt2[2];
        m -= arrayOfInt2[0];
        k -= arrayOfInt2[1];
        i -= arrayOfInt2[2];
        i7 += 1;
        i8 += 1;
      }
      i5 += i10;
      i6 += 1;
    }
    i = 0;
    while (i < i10)
    {
      i2 = 0;
      i4 = 0;
      i5 = 0;
      k = 0;
      i1 = 0;
      i3 = 0;
      j = 0;
      m = 0;
      n = 0;
      i6 = -paramInt * i10;
      i7 = -paramInt;
      if (i7 <= paramInt)
      {
        i8 = Math.max(0, i6) + i;
        arrayOfInt2 = arrayOfInt[(i7 + paramInt)];
        arrayOfInt2[0] = localObject2[i8];
        arrayOfInt2[1] = localObject3[i8];
        arrayOfInt2[2] = localObject4[i8];
        i9 = i14 - Math.abs(i7);
        i5 += localObject2[i8] * i9;
        i4 += localObject3[i8] * i9;
        i2 += localObject4[i8] * i9;
        if (i7 > 0)
        {
          n += arrayOfInt2[0];
          m += arrayOfInt2[1];
          j += arrayOfInt2[2];
        }
        while (true)
        {
          i8 = i6;
          if (i7 < i12)
            i8 = i6 + i10;
          i7 += 1;
          i6 = i8;
          break;
          i3 += arrayOfInt2[0];
          i1 += arrayOfInt2[1];
          k += arrayOfInt2[2];
        }
      }
      i7 = i;
      i8 = paramInt;
      i6 = 0;
      while (i6 < i11)
      {
        localObject1[i7] = (0xFF000000 & localObject1[i7] | arrayOfInt1[i5] << 16 | arrayOfInt1[i4] << 8 | arrayOfInt1[i2]);
        arrayOfInt2 = arrayOfInt[((i8 - paramInt + i13) % i13)];
        i16 = arrayOfInt2[0];
        i15 = arrayOfInt2[1];
        i9 = arrayOfInt2[2];
        if (i == 0)
          localObject5[i6] = (Math.min(i6 + i14, i12) * i10);
        i17 = i + localObject5[i6];
        arrayOfInt2[0] = localObject2[i17];
        arrayOfInt2[1] = localObject3[i17];
        arrayOfInt2[2] = localObject4[i17];
        n += arrayOfInt2[0];
        m += arrayOfInt2[1];
        j += arrayOfInt2[2];
        i5 = i5 - i3 + n;
        i4 = i4 - i1 + m;
        i2 = i2 - k + j;
        i8 = (i8 + 1) % i13;
        arrayOfInt2 = arrayOfInt[i8];
        i3 = i3 - i16 + arrayOfInt2[0];
        i1 = i1 - i15 + arrayOfInt2[1];
        k = k - i9 + arrayOfInt2[2];
        n -= arrayOfInt2[0];
        m -= arrayOfInt2[1];
        j -= arrayOfInt2[2];
        i7 += i10;
        i6 += 1;
      }
      i += 1;
    }
    paramBitmap.setPixels(localObject1, 0, i10, 0, 0, i10, i11);
    return (Bitmap)(Bitmap)(Bitmap)(Bitmap)(Bitmap)paramBitmap;
  }

  public void setImageBitmap(Bitmap paramBitmap)
  {
    if (this.savedScaleType != null)
      setScaleType(this.savedScaleType);
    Object localObject = paramBitmap;
    if (this.direction != 0)
    {
      localObject = new Matrix();
      ((Matrix)localObject).setRotate(this.direction, paramBitmap.getWidth() / 2.0F, paramBitmap.getHeight() / 2.0F);
      localObject = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), (Matrix)localObject, false);
    }
    this.currentPlaceholder = false;
    try
    {
      paramBitmap = doBlur((Bitmap)localObject, 3);
      super.setImageBitmap(paramBitmap);
      return;
    }
    catch (java.lang.Exception paramBitmap)
    {
      while (true)
        paramBitmap = (Bitmap)localObject;
    }
  }

  public void setImageResource(int paramInt)
  {
    setImageBitmap(((BitmapDrawable)getContext().getResources().getDrawable(paramInt)).getBitmap());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.mall.nearby.BlurImageView
 * JD-Core Version:    0.6.0
 */
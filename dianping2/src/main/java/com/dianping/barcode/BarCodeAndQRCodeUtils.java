package com.dianping.barcode;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.R.dimen;
import com.google.zxing.client.android.encode.QRCodeEncoder;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.EnumMap;
import java.util.Map;

public class BarCodeAndQRCodeUtils
{
  public static Bitmap createBarcode(Context paramContext, String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = dip2px(paramContext, 30.0F);
    BarcodeFormat localBarcodeFormat = BarcodeFormat.CODE_128;
    if (paramBoolean)
      return mixtureBitmap(encodeAsBitmap(paramContext, paramString, localBarcodeFormat, paramInt1, paramInt2, null), createCodeBitmap(paramContext, paramString, paramInt1, i), new PointF(0.0F, paramInt2), paramContext);
    return encodeAsBitmap(paramContext, paramString, localBarcodeFormat, paramInt1, paramInt2, null);
  }

  protected static Bitmap createCodeBitmap(Context paramContext, String paramString, int paramInt1, int paramInt2)
  {
    TextView localTextView = new TextView(paramContext);
    localTextView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
    localTextView.setTextSize(0, paramContext.getResources().getDimensionPixelSize(R.dimen.text_size_18));
    localTextView.setText(getDecodeString(new StringBuilder(paramString)));
    localTextView.setHeight(paramInt2);
    localTextView.setGravity(81);
    localTextView.setWidth(paramInt1);
    localTextView.setDrawingCacheEnabled(true);
    localTextView.setTextColor(-16777216);
    localTextView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    localTextView.layout(0, 0, localTextView.getMeasuredWidth(), localTextView.getMeasuredHeight());
    localTextView.buildDrawingCache();
    return localTextView.getDrawingCache();
  }

  public static Bitmap createQRImage(Context paramContext, String paramString, int paramInt1, int paramInt2)
  {
    EnumMap localEnumMap = new EnumMap(EncodeHintType.class);
    localEnumMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    localEnumMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    localEnumMap.put(EncodeHintType.MARGIN, new Integer(0));
    return encodeAsBitmap(paramContext, paramString, BarcodeFormat.QR_CODE, paramInt1, paramInt2, localEnumMap);
  }

  public static int dip2px(Context paramContext, float paramFloat)
  {
    if (paramContext == null)
      return (int)paramFloat;
    return (int)(paramFloat * paramContext.getResources().getDisplayMetrics().density + 0.5F);
  }

  protected static Bitmap encodeAsBitmap(Context paramContext, String paramString, BarcodeFormat paramBarcodeFormat, int paramInt1, int paramInt2, Map<EncodeHintType, Object> paramMap)
  {
    Intent localIntent = new Intent("com.google.zxing.client.android.ENCODE");
    localIntent.putExtra("ENCODE_FORMAT", paramBarcodeFormat.toString());
    localIntent.putExtra("ENCODE_DATA", paramString);
    localIntent.putExtra("ENCODE_TYPE", "TEXT_TYPE");
    try
    {
      paramContext = new QRCodeEncoder(paramContext, localIntent, paramInt1, paramInt2, false, paramMap).encodeAsBitmap();
      return paramContext;
    }
    catch (WriterException paramContext)
    {
      paramContext.printStackTrace();
    }
    return null;
  }

  private static StringBuilder getDecodeString(StringBuilder paramStringBuilder)
  {
    int j = paramStringBuilder.length() / 4;
    int i = 0;
    while ((i < j) && (i < 3))
    {
      paramStringBuilder.insert((i + 1) * 4 + i, " ");
      i += 1;
    }
    return paramStringBuilder;
  }

  protected static Bitmap mixtureBitmap(Bitmap paramBitmap1, Bitmap paramBitmap2, PointF paramPointF, Context paramContext)
  {
    if ((paramBitmap1 == null) || (paramBitmap2 == null) || (paramPointF == null))
      return null;
    paramContext = Bitmap.createBitmap(paramBitmap1.getWidth(), paramBitmap1.getHeight() + paramBitmap2.getHeight(), Bitmap.Config.ARGB_4444);
    Canvas localCanvas = new Canvas(paramContext);
    localCanvas.drawBitmap(paramBitmap1, 0.0F, 0.0F, null);
    localCanvas.drawBitmap(paramBitmap2, paramPointF.x, paramPointF.y, null);
    localCanvas.save(31);
    localCanvas.restore();
    paramBitmap1.recycle();
    paramBitmap2.recycle();
    return paramContext;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.barcode.BarCodeAndQRCodeUtils
 * JD-Core Version:    0.6.0
 */
package com.dianping.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.view.View;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class BitmapUtils
{
  public static final int DEFAULT_DENSITY = 480;

  public static byte[] bmpToByteArray(Bitmap paramBitmap, boolean paramBoolean)
  {
    if (paramBitmap == null)
      return null;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
    if (paramBoolean)
      paramBitmap.recycle();
    paramBitmap = localByteArrayOutputStream.toByteArray();
    try
    {
      localByteArrayOutputStream.close();
      return paramBitmap;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return paramBitmap;
  }

  private static int calculateInSampleSize(BitmapFactory.Options paramOptions, int paramInt1, int paramInt2)
  {
    int i;
    if (paramOptions == null)
      i = -1;
    int k;
    int m;
    int j;
    do
    {
      return i;
      k = paramOptions.outHeight;
      m = paramOptions.outWidth;
      j = 1;
      i = 1;
    }
    while ((k <= paramInt2) && (m <= paramInt1));
    k /= 2;
    m /= 2;
    while (true)
    {
      i = j;
      if (k / j <= paramInt2)
        break;
      i = j;
      if (m / j <= paramInt1)
        break;
      j *= 2;
    }
  }

  public static void copyExif(String paramString1, String paramString2, int paramInt)
  {
    while (true)
    {
      int i;
      try
      {
        paramString1 = new ExifInterface(paramString1);
        paramString2 = new ExifInterface(paramString2);
        Field[] arrayOfField = paramString1.getClass().getFields();
        int j = arrayOfField.length;
        i = 0;
        if (i >= j)
          continue;
        Object localObject = arrayOfField[i];
        if (((Field)localObject).getName().startsWith("TAG_"))
        {
          localObject = ((Field)localObject).get(paramString1).toString();
          String str = paramString1.getAttribute((String)localObject);
          if (!TextUtils.isEmpty(str))
          {
            paramString2.setAttribute((String)localObject, str);
            break label139;
            paramString2.setAttribute("Orientation", String.valueOf(paramInt));
            paramString2.saveAttributes();
            return;
          }
        }
      }
      catch (Exception paramString1)
      {
        Log.e("copyExif:" + paramString1.toString());
        return;
      }
      label139: i += 1;
    }
  }

  public static void copyExif(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    while (true)
    {
      int i;
      try
      {
        paramString1 = new ExifInterface(paramString1);
        paramString2 = new ExifInterface(paramString2);
        Field[] arrayOfField = paramString1.getClass().getFields();
        int j = arrayOfField.length;
        i = 0;
        if (i >= j)
          continue;
        Object localObject = arrayOfField[i];
        if (((Field)localObject).getName().startsWith("TAG_"))
        {
          localObject = ((Field)localObject).get(paramString1).toString();
          String str = paramString1.getAttribute((String)localObject);
          if (!TextUtils.isEmpty(str))
          {
            paramString2.setAttribute((String)localObject, str);
            break label152;
            paramString2.setAttribute("ImageWidth", String.valueOf(paramInt1));
            paramString2.setAttribute("ImageLength", String.valueOf(paramInt2));
            paramString2.saveAttributes();
            return;
          }
        }
      }
      catch (Exception paramString1)
      {
        Log.e("copyExif:" + paramString1.toString());
        return;
      }
      label152: i += 1;
    }
  }

  // ERROR //
  public static Bitmap createUploadImage(String paramString, int paramInt)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: getstatic 145	com/dianping/configservice/impl/ConfigHelper:scaleImageSizeWidth	I
    //   6: istore 7
    //   8: getstatic 148	com/dianping/configservice/impl/ConfigHelper:scaleImageSizeHeight	I
    //   11: istore 8
    //   13: new 48	android/graphics/BitmapFactory$Options
    //   16: dup
    //   17: invokespecial 149	android/graphics/BitmapFactory$Options:<init>	()V
    //   20: astore 5
    //   22: aload 5
    //   24: iconst_1
    //   25: putfield 153	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   28: aload_0
    //   29: aload 5
    //   31: invokestatic 159	android/graphics/BitmapFactory:decodeFile	(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   34: pop
    //   35: aload 5
    //   37: getfield 54	android/graphics/BitmapFactory$Options:outWidth	I
    //   40: aload 5
    //   42: getfield 51	android/graphics/BitmapFactory$Options:outHeight	I
    //   45: invokestatic 165	java/lang/Math:max	(II)I
    //   48: istore 10
    //   50: iload 10
    //   52: iload 8
    //   54: if_icmple +87 -> 141
    //   57: iload 10
    //   59: iload 8
    //   61: idiv
    //   62: istore 6
    //   64: iload 6
    //   66: bipush 8
    //   68: if_icmpgt +125 -> 193
    //   71: aload 4
    //   73: ifnonnull +120 -> 193
    //   76: new 48	android/graphics/BitmapFactory$Options
    //   79: dup
    //   80: invokespecial 149	android/graphics/BitmapFactory$Options:<init>	()V
    //   83: astore 5
    //   85: aload 5
    //   87: getstatic 171	android/graphics/Bitmap$Config:RGB_565	Landroid/graphics/Bitmap$Config;
    //   90: putfield 174	android/graphics/BitmapFactory$Options:inPreferredConfig	Landroid/graphics/Bitmap$Config;
    //   93: aload 5
    //   95: iconst_1
    //   96: putfield 177	android/graphics/BitmapFactory$Options:inPurgeable	Z
    //   99: aload 5
    //   101: iconst_1
    //   102: putfield 180	android/graphics/BitmapFactory$Options:inInputShareable	Z
    //   105: aload 5
    //   107: iload 6
    //   109: putfield 183	android/graphics/BitmapFactory$Options:inSampleSize	I
    //   112: aload_0
    //   113: aload 5
    //   115: invokestatic 159	android/graphics/BitmapFactory:decodeFile	(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   118: astore 5
    //   120: iload 7
    //   122: istore 9
    //   124: iload 6
    //   126: iconst_2
    //   127: imul
    //   128: istore 6
    //   130: aload 5
    //   132: astore 4
    //   134: iload 9
    //   136: istore 7
    //   138: goto -74 -> 64
    //   141: iconst_1
    //   142: istore 6
    //   144: goto -80 -> 64
    //   147: astore_0
    //   148: aconst_null
    //   149: areturn
    //   150: astore 5
    //   152: aload 4
    //   154: astore 5
    //   156: iload 7
    //   158: istore 9
    //   160: iload 7
    //   162: sipush 700
    //   165: if_icmple -41 -> 124
    //   168: sipush 700
    //   171: istore 9
    //   173: sipush 700
    //   176: istore 8
    //   178: iload 10
    //   180: sipush 700
    //   183: idiv
    //   184: istore 6
    //   186: aload 4
    //   188: astore 5
    //   190: goto -66 -> 124
    //   193: aload 4
    //   195: ifnonnull +5 -> 200
    //   198: aconst_null
    //   199: areturn
    //   200: aload 4
    //   202: invokevirtual 187	android/graphics/Bitmap:getWidth	()I
    //   205: aload 4
    //   207: invokevirtual 190	android/graphics/Bitmap:getHeight	()I
    //   210: invokestatic 193	java/lang/Math:min	(II)I
    //   213: i2f
    //   214: fstore_3
    //   215: aload 4
    //   217: invokevirtual 187	android/graphics/Bitmap:getWidth	()I
    //   220: aload 4
    //   222: invokevirtual 190	android/graphics/Bitmap:getHeight	()I
    //   225: invokestatic 165	java/lang/Math:max	(II)I
    //   228: i2f
    //   229: fstore_2
    //   230: iload 7
    //   232: i2f
    //   233: fload_3
    //   234: fdiv
    //   235: fstore_3
    //   236: iload 8
    //   238: i2f
    //   239: fload_2
    //   240: fdiv
    //   241: fload_3
    //   242: invokestatic 196	java/lang/Math:min	(FF)F
    //   245: fstore_2
    //   246: aload 4
    //   248: astore_0
    //   249: fload_2
    //   250: fconst_1
    //   251: fcmpg
    //   252: ifge +28 -> 280
    //   255: aload 4
    //   257: aload 4
    //   259: invokevirtual 187	android/graphics/Bitmap:getWidth	()I
    //   262: i2f
    //   263: fload_2
    //   264: fmul
    //   265: f2i
    //   266: aload 4
    //   268: invokevirtual 190	android/graphics/Bitmap:getHeight	()I
    //   271: i2f
    //   272: fload_2
    //   273: fmul
    //   274: f2i
    //   275: iconst_1
    //   276: invokestatic 200	android/graphics/Bitmap:createScaledBitmap	(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
    //   279: astore_0
    //   280: aload_0
    //   281: astore 4
    //   283: iload_1
    //   284: ifeq +17 -> 301
    //   287: aload_0
    //   288: astore 4
    //   290: aload_0
    //   291: ifnull +10 -> 301
    //   294: aload_0
    //   295: iload_1
    //   296: invokestatic 204	com/dianping/util/BitmapUtils:rotateImg	(Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
    //   299: astore 4
    //   301: aload 4
    //   303: areturn
    //   304: astore_0
    //   305: aload 4
    //   307: sipush 700
    //   310: sipush 700
    //   313: iconst_1
    //   314: invokestatic 200	android/graphics/Bitmap:createScaledBitmap	(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
    //   317: astore_0
    //   318: goto -38 -> 280
    //
    // Exception table:
    //   from	to	target	type
    //   13	50	147	java/lang/Exception
    //   57	64	147	java/lang/Exception
    //   76	120	150	java/lang/Throwable
    //   200	246	304	java/lang/Throwable
    //   255	280	304	java/lang/Throwable
  }

  public static Bitmap cropCenterBitmap(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    if ((i < paramInt1) && (j < paramInt2))
      return paramBitmap;
    Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, Math.max((i - paramInt1) / 2, 0), Math.max((j - paramInt2) / 2, 0), Math.min(i, paramInt1), Math.min(j, paramInt2));
    if (localBitmap != paramBitmap)
      paramBitmap.recycle();
    return localBitmap;
  }

  public static Bitmap cropCenterSquareBitmap(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    return cropSquareBitmap(cropCenterBitmap(paramBitmap, paramInt1, paramInt2));
  }

  public static Bitmap cropSquareBitmap(Bitmap paramBitmap)
  {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    if (i == j)
      return paramBitmap;
    if (i >= j);
    for (Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, i / 2 - j / 2, 0, j, j); ; localBitmap = Bitmap.createBitmap(paramBitmap, 0, j / 2 - i / 2, i, i))
    {
      if (localBitmap != paramBitmap)
        paramBitmap.recycle();
      return localBitmap;
    }
  }

  public static Bitmap decodeSampledBitmapFromResource(Resources paramResources, int paramInt1, int paramInt2, int paramInt3)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inJustDecodeBounds = true;
    localOptions.inPreferredConfig = Bitmap.Config.RGB_565;
    localOptions.inPurgeable = true;
    localOptions.inInputShareable = true;
    localOptions.inSampleSize = calculateInSampleSize(localOptions, paramInt2, paramInt3);
    localOptions.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource(paramResources, paramInt1, localOptions);
  }

  public static Bitmap decodeSampledBitmapFromStream(Bitmap.Config paramConfig, InputStream paramInputStream, int paramInt1, int paramInt2)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inJustDecodeBounds = true;
    localOptions.inPreferredConfig = paramConfig;
    localOptions.inPurgeable = true;
    localOptions.inInputShareable = true;
    localOptions.inSampleSize = calculateInSampleSize(localOptions, paramInt1, paramInt2);
    localOptions.inJustDecodeBounds = false;
    return BitmapFactory.decodeStream(paramInputStream, null, localOptions);
  }

  public static void fixBackgroundRepeat(View paramView)
  {
    if (paramView == null);
    do
    {
      return;
      paramView = paramView.getBackground();
    }
    while ((paramView == null) || (!(paramView instanceof BitmapDrawable)));
    paramView = (BitmapDrawable)paramView;
    paramView.mutate();
    paramView.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
  }

  // ERROR //
  public static BitmapDrawable getBitmapDrawable(Context paramContext, InputStream paramInputStream)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull +5 -> 6
    //   4: aconst_null
    //   5: areturn
    //   6: aconst_null
    //   7: astore_2
    //   8: new 241	android/graphics/drawable/BitmapDrawable
    //   11: dup
    //   12: aload_1
    //   13: invokespecial 261	android/graphics/drawable/BitmapDrawable:<init>	(Ljava/io/InputStream;)V
    //   16: astore_3
    //   17: aload_3
    //   18: aload_0
    //   19: invokevirtual 267	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   22: invokevirtual 273	android/content/res/Resources:getDisplayMetrics	()Landroid/util/DisplayMetrics;
    //   25: getfield 278	android/util/DisplayMetrics:densityDpi	I
    //   28: aload_0
    //   29: invokevirtual 267	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   32: invokevirtual 273	android/content/res/Resources:getDisplayMetrics	()Landroid/util/DisplayMetrics;
    //   35: getfield 278	android/util/DisplayMetrics:densityDpi	I
    //   38: imul
    //   39: sipush 480
    //   42: idiv
    //   43: invokevirtual 282	android/graphics/drawable/BitmapDrawable:setTargetDensity	(I)V
    //   46: aload_1
    //   47: invokevirtual 285	java/io/InputStream:close	()V
    //   50: aload_3
    //   51: areturn
    //   52: astore_1
    //   53: aload_2
    //   54: astore_0
    //   55: aload_1
    //   56: invokevirtual 286	java/io/IOException:printStackTrace	()V
    //   59: aload_0
    //   60: areturn
    //   61: astore_1
    //   62: aload_3
    //   63: astore_0
    //   64: goto -9 -> 55
    //
    // Exception table:
    //   from	to	target	type
    //   8	17	52	java/io/IOException
    //   17	50	61	java/io/IOException
  }

  public static BitmapDrawable getBitmapDrawable(Context paramContext, String paramString)
  {
    return getBitmapDrawable(paramContext, getFileStream(paramContext, paramString));
  }

  public static Drawable getBitmapDrawable(Context paramContext, String paramString, int paramInt)
  {
    paramString = getFileStream(paramContext, paramString);
    if (paramString != null)
      return getBitmapDrawable(paramContext, paramString);
    return paramContext.getResources().getDrawable(paramInt);
  }

  public static int[] getBitmapSize(String paramString)
  {
    int[] arrayOfInt = new int[2];
    int[] tmp5_4 = arrayOfInt;
    tmp5_4[0] = -1;
    int[] tmp9_5 = tmp5_4;
    tmp9_5[1] = -1;
    tmp9_5;
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(paramString, localOptions);
    arrayOfInt[0] = localOptions.outWidth;
    arrayOfInt[1] = localOptions.outHeight;
    return arrayOfInt;
  }

  public static InputStream getFileStream(Context paramContext, String paramString)
  {
    if (paramString.startsWith("/"))
      try
      {
        paramContext = new FileInputStream(paramString);
        return paramContext;
      }
      catch (IOException paramContext)
      {
        paramContext.printStackTrace();
        return null;
      }
    paramContext = paramContext.getResources().getAssets();
    try
    {
      paramContext = paramContext.open(paramString);
      return paramContext;
    }
    catch (IOException paramContext)
    {
      paramContext.printStackTrace();
    }
    return null;
  }

  public static Drawable getStateListDrawable(Context paramContext, String paramString1, String paramString2, int paramInt)
  {
    paramString1 = getFileStream(paramContext, paramString1);
    paramString2 = getFileStream(paramContext, paramString2);
    if ((paramString1 != null) || (paramString2 != null))
      return getStateListDrawable(paramContext, paramString1, paramString2);
    return paramContext.getResources().getDrawable(paramInt);
  }

  private static StateListDrawable getStateListDrawable(Context paramContext, InputStream paramInputStream1, InputStream paramInputStream2)
  {
    paramInputStream1 = getBitmapDrawable(paramContext, paramInputStream1);
    paramContext = getBitmapDrawable(paramContext, paramInputStream2);
    paramInputStream2 = new StateListDrawable();
    paramInputStream2.addState(new int[] { 16842919 }, paramContext);
    paramInputStream2.addState(new int[] { 16842908 }, paramContext);
    paramInputStream2.addState(new int[] { 16842913 }, paramContext);
    paramInputStream2.addState(new int[0], paramInputStream1);
    return paramInputStream2;
  }

  public static StateListDrawable getStateListDrawable(Context paramContext, String paramString1, String paramString2)
  {
    return getStateListDrawable(paramContext, getFileStream(paramContext, paramString1), getFileStream(paramContext, paramString2));
  }

  public static double[] readPictureCoordinate(String paramString)
  {
    double[] arrayOfDouble = new double[2];
    while (true)
    {
      int k;
      try
      {
        paramString = new ExifInterface(paramString);
        Object localObject2 = paramString.getAttribute("GPSLatitude");
        Object localObject1 = paramString.getAttribute("GPSLongitude");
        if (localObject2 == null)
          continue;
        localObject2 = ((String)localObject2).split(",");
        int i = 0;
        if (i >= localObject2.length)
          continue;
        double d = Double.parseDouble(localObject2[i].substring(0, localObject2[i].indexOf("/"))) / Double.parseDouble(localObject2[i].substring(localObject2[i].indexOf("/") + 1, localObject2[i].length()));
        arrayOfDouble[0] += d / Math.pow(60.0D, i);
        if (!"S".equals(paramString.getAttribute("GPSLatitudeRef")))
          break label304;
        arrayOfDouble[0] *= -1.0D;
        break label304;
        if (localObject1 != null)
        {
          localObject1 = ((String)localObject1).split(",");
          int j = 0;
          if (j < localObject1.length)
          {
            d = Double.parseDouble(localObject1[j].substring(0, localObject1[j].indexOf("/"))) / Double.parseDouble(localObject1[j].substring(localObject1[j].indexOf("/") + 1, localObject1[j].length()));
            arrayOfDouble[1] += d / Math.pow(60.0D, j);
            if (!"W".equals(paramString.getAttribute("GPSLongitudeRef")))
              continue;
            arrayOfDouble[1] *= -1.0D;
            j += 1;
            continue;
          }
        }
      }
      catch (IOException paramString)
      {
        paramString.printStackTrace();
      }
      return arrayOfDouble;
      label304: k += 1;
    }
  }

  public static int readPictureDegree(String paramString)
  {
    try
    {
      int i = new ExifInterface(paramString).getAttributeInt("Orientation", 1);
      switch (i)
      {
      case 4:
      case 5:
      case 7:
      default:
        return 0;
      case 6:
        return 90;
      case 3:
        return 180;
      case 8:
      }
      return 270;
    }
    catch (IOException paramString)
    {
      paramString.printStackTrace();
    }
    return 0;
  }

  public static Bitmap rotateImg(Bitmap paramBitmap, int paramInt)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled()))
      localObject = null;
    do
    {
      return localObject;
      localObject = paramBitmap;
    }
    while (paramInt == 0);
    Object localObject = new Matrix();
    ((Matrix)localObject).setRotate(paramInt, paramBitmap.getWidth() / 2.0F, paramBitmap.getHeight() / 2.0F);
    return (Bitmap)Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), (Matrix)localObject, false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.BitmapUtils
 * JD-Core Version:    0.6.0
 */
package com.dianping.base.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;
import com.dianping.app.DPActivity;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.image.ImageService;
import com.dianping.dataservice.image.impl.ImageRequest;
import com.dianping.util.Log;
import com.dianping.util.PermissionCheckHelper;
import com.dianping.util.PermissionCheckHelper.PermissionCallbackListener;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.string;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class ImageUtils
{
  private static final int BLACK = -16777216;
  public static final int CAMERA_WITH_DATA = 3023;
  private static final int CLEAR = 0;
  public static final int PHOTO_CROP_WITH_DATA = 3025;
  public static final int PHOTO_MAX_NUM = 9;
  public static final int PHOTO_PICKED_WITH_DATA = 3021;

  public static boolean checkPhotoSize(String paramString)
  {
    try
    {
      paramString = new FileInputStream(paramString);
      BitmapFactory.Options localOptions = new BitmapFactory.Options();
      localOptions.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(paramString, null, localOptions);
      paramString.close();
      if (localOptions.outWidth >= 100)
      {
        int i = localOptions.outHeight;
        if (i >= 100)
          return true;
      }
      return false;
    }
    catch (Exception paramString)
    {
      paramString.printStackTrace();
    }
    return true;
  }

  public static ByteArrayInputStream compressBitmap(Bitmap paramBitmap, int paramInt)
  {
    if (paramBitmap == null)
      return null;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
    if ((paramInt > 0) && (paramBitmap.getRowBytes() * paramBitmap.getHeight() / 1024 > paramInt))
    {
      int i = 70;
      localByteArrayOutputStream.reset();
      paramBitmap.compress(Bitmap.CompressFormat.JPEG, 70, localByteArrayOutputStream);
      if (localByteArrayOutputStream.toByteArray().length / 1024 > paramInt)
      {
        i = (int)(paramInt / (localByteArrayOutputStream.toByteArray().length / 1024) * 70);
        localByteArrayOutputStream.reset();
        paramBitmap.compress(Bitmap.CompressFormat.JPEG, i, localByteArrayOutputStream);
      }
      while ((localByteArrayOutputStream.toByteArray().length / 1024 > paramInt + 10) && (i > 0))
      {
        int j = i - 10;
        i = j;
        if (j < 0)
          i = 0;
        localByteArrayOutputStream.reset();
        paramBitmap.compress(Bitmap.CompressFormat.JPEG, i, localByteArrayOutputStream);
      }
    }
    return new ByteArrayInputStream(localByteArrayOutputStream.toByteArray());
  }

  // ERROR //
  public static Bitmap decodeShowImage(Context paramContext, String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_0
    //   3: invokestatic 113	com/dianping/util/ViewUtils:getScreenHeightPixels	(Landroid/content/Context;)I
    //   6: istore 5
    //   8: aload_0
    //   9: invokestatic 116	com/dianping/util/ViewUtils:getScreenWidthPixels	(Landroid/content/Context;)I
    //   12: istore 6
    //   14: aload_0
    //   15: ldc 117
    //   17: invokestatic 121	com/dianping/util/ViewUtils:dip2px	(Landroid/content/Context;F)I
    //   20: istore 7
    //   22: iload 5
    //   24: iload 6
    //   26: if_icmple +105 -> 131
    //   29: new 44	android/graphics/BitmapFactory$Options
    //   32: dup
    //   33: invokespecial 45	android/graphics/BitmapFactory$Options:<init>	()V
    //   36: astore 4
    //   38: aload 4
    //   40: iconst_1
    //   41: putfield 49	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   44: aload_1
    //   45: aload 4
    //   47: invokestatic 125	android/graphics/BitmapFactory:decodeFile	(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   50: pop
    //   51: aload 4
    //   53: getfield 61	android/graphics/BitmapFactory$Options:outWidth	I
    //   56: aload 4
    //   58: getfield 64	android/graphics/BitmapFactory$Options:outHeight	I
    //   61: if_icmple +77 -> 138
    //   64: aload 4
    //   66: getfield 61	android/graphics/BitmapFactory$Options:outWidth	I
    //   69: istore 6
    //   71: iload 6
    //   73: iload 5
    //   75: idiv
    //   76: istore 5
    //   78: iload 5
    //   80: iconst_1
    //   81: iadd
    //   82: istore 5
    //   84: iload 5
    //   86: bipush 8
    //   88: if_icmpgt +74 -> 162
    //   91: aload_3
    //   92: ifnonnull +70 -> 162
    //   95: new 44	android/graphics/BitmapFactory$Options
    //   98: dup
    //   99: invokespecial 45	android/graphics/BitmapFactory$Options:<init>	()V
    //   102: astore 4
    //   104: aload 4
    //   106: iload 5
    //   108: putfield 128	android/graphics/BitmapFactory$Options:inSampleSize	I
    //   111: aload_1
    //   112: aload 4
    //   114: invokestatic 125	android/graphics/BitmapFactory:decodeFile	(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   117: astore 4
    //   119: aload 4
    //   121: astore_3
    //   122: iload 5
    //   124: iconst_2
    //   125: imul
    //   126: istore 5
    //   128: goto -44 -> 84
    //   131: iload 6
    //   133: istore 5
    //   135: goto -106 -> 29
    //   138: aload 4
    //   140: getfield 64	android/graphics/BitmapFactory$Options:outHeight	I
    //   143: istore 6
    //   145: goto -74 -> 71
    //   148: astore_0
    //   149: aconst_null
    //   150: areturn
    //   151: astore 4
    //   153: invokestatic 133	java/lang/System:gc	()V
    //   156: goto -34 -> 122
    //   159: astore_0
    //   160: aconst_null
    //   161: areturn
    //   162: aload_3
    //   163: ifnonnull +15 -> 178
    //   166: aload_0
    //   167: ldc 135
    //   169: iconst_1
    //   170: invokestatic 141	android/widget/Toast:makeText	(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
    //   173: invokevirtual 144	android/widget/Toast:show	()V
    //   176: aconst_null
    //   177: areturn
    //   178: aload_3
    //   179: invokevirtual 147	android/graphics/Bitmap:getWidth	()I
    //   182: aload_3
    //   183: invokevirtual 91	android/graphics/Bitmap:getHeight	()I
    //   186: if_icmpge +43 -> 229
    //   189: aload_3
    //   190: invokevirtual 147	android/graphics/Bitmap:getWidth	()I
    //   193: i2f
    //   194: fstore_2
    //   195: iload 7
    //   197: i2f
    //   198: fload_2
    //   199: fdiv
    //   200: fstore_2
    //   201: fload_2
    //   202: f2i
    //   203: iconst_1
    //   204: if_icmpge +38 -> 242
    //   207: aload_3
    //   208: aload_3
    //   209: invokevirtual 147	android/graphics/Bitmap:getWidth	()I
    //   212: i2f
    //   213: fload_2
    //   214: fmul
    //   215: f2i
    //   216: aload_3
    //   217: invokevirtual 91	android/graphics/Bitmap:getHeight	()I
    //   220: i2f
    //   221: fload_2
    //   222: fmul
    //   223: f2i
    //   224: iconst_1
    //   225: invokestatic 151	android/graphics/Bitmap:createScaledBitmap	(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
    //   228: areturn
    //   229: aload_3
    //   230: invokevirtual 91	android/graphics/Bitmap:getHeight	()I
    //   233: istore 5
    //   235: iload 5
    //   237: i2f
    //   238: fstore_2
    //   239: goto -44 -> 195
    //   242: fconst_1
    //   243: fstore_2
    //   244: goto -37 -> 207
    //   247: astore_0
    //   248: aconst_null
    //   249: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   29	71	148	java/lang/Exception
    //   71	78	148	java/lang/Exception
    //   138	145	148	java/lang/Exception
    //   95	119	151	java/lang/OutOfMemoryError
    //   95	119	159	java/lang/Exception
    //   178	195	247	java/lang/Exception
    //   195	201	247	java/lang/Exception
    //   207	229	247	java/lang/Exception
    //   229	235	247	java/lang/Exception
  }

  public static void doCropPhoto(Activity paramActivity, String paramString)
  {
    if (!TextUtils.isEmpty(paramString));
    try
    {
      paramString = getCropImageIntent(Uri.fromFile(new File(paramString)));
      if (paramActivity != null)
        paramActivity.startActivityForResult(paramString, 3025);
      return;
    }
    catch (Exception paramActivity)
    {
      paramActivity.printStackTrace();
    }
  }

  public static Bitmap encodeAsBitmap(String paramString, int paramInt1, int paramInt2)
    throws WriterException
  {
    Object localObject = null;
    String str = guessAppropriateEncoding(paramString);
    if (str != null)
    {
      localObject = new Hashtable(2);
      ((Hashtable)localObject).put(EncodeHintType.CHARACTER_SET, str);
      ((Hashtable)localObject).put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    }
    localObject = new QRCodeWriter().encode(paramString, BarcodeFormat.QR_CODE, paramInt1, paramInt2, (Map)localObject);
    int j = ((BitMatrix)localObject).getWidth();
    int k = ((BitMatrix)localObject).getHeight();
    paramString = new int[j * k];
    paramInt1 = 0;
    while (paramInt1 < k)
    {
      paramInt2 = 0;
      if (paramInt2 < j)
      {
        if (((BitMatrix)localObject).get(paramInt2, paramInt1));
        for (int i = -16777216; ; i = 0)
        {
          paramString[(paramInt1 * j + paramInt2)] = i;
          paramInt2 += 1;
          break;
        }
      }
      paramInt1 += 1;
    }
    localObject = Bitmap.createBitmap(j, k, Bitmap.Config.ARGB_8888);
    ((Bitmap)localObject).setPixels(paramString, 0, j, 0, 0, j, k);
    return (Bitmap)localObject;
  }

  private static Intent getCropImageIntent(Uri paramUri)
  {
    Intent localIntent = new Intent("com.android.camera.action.CROP");
    localIntent.setDataAndType(paramUri, "image/*");
    localIntent.putExtra("crop", "true");
    localIntent.putExtra("aspectX", 1);
    localIntent.putExtra("aspectY", 1);
    localIntent.putExtra("outputX", outputX());
    localIntent.putExtra("outputY", outputY());
    localIntent.putExtra("return-data", true);
    return localIntent;
  }

  public static String getImgPath(Context paramContext, int paramInt1, int paramInt2, Intent paramIntent)
  {
    String str2 = DPActivity.preferences().getString("uploadImgPath", null);
    String str1 = str2;
    if (paramInt1 == 3023)
    {
      str1 = str2;
      if (paramInt2 == -1)
      {
        if (TextUtils.isEmpty(str2))
          break label71;
        str1 = str2;
        if (!new File(str2).exists())
          str1 = parseImgPath(paramContext, paramIntent);
      }
    }
    return str1;
    label71: return parseImgPath(paramContext, paramIntent);
  }

  public static Bitmap getThumbnail(Context paramContext, String paramString)
  {
    Object localObject = null;
    ExecutorService localExecutorService = Executors.newSingleThreadExecutor();
    paramString = new FutureTask(new Callable(paramString, paramContext)
    {
      public Bitmap call()
        throws Exception
      {
        Object localObject2 = null;
        Object localObject1 = localObject2;
        if (!TextUtils.isEmpty(this.val$imageUrl))
        {
          localObject1 = localObject2;
          if ((this.val$context instanceof DPActivity))
          {
            Response localResponse = ((ImageService)((DPActivity)this.val$context).getService("image")).execSync(new ImageRequest(this.val$imageUrl, 1, false));
            localObject1 = localObject2;
            if ((localResponse.result() instanceof Bitmap))
              localObject1 = (Bitmap)localResponse.result();
          }
        }
        return (Bitmap)localObject1;
      }
    });
    localExecutorService.submit(paramString);
    try
    {
      paramString = (Bitmap)paramString.get(2000L, TimeUnit.MILLISECONDS);
      localExecutorService.shutdown();
      localObject = paramString;
      if (paramString == null)
        localObject = BitmapFactory.decodeResource(paramContext.getResources(), R.drawable.dp_icon_60);
      return localObject;
    }
    catch (Exception paramString)
    {
      while (true)
      {
        Log.e(paramString.toString());
        paramString = (String)localObject;
      }
    }
  }

  private static String guessAppropriateEncoding(CharSequence paramCharSequence)
  {
    int i = 0;
    while (i < paramCharSequence.length())
    {
      if (paramCharSequence.charAt(i) > 'ÿ')
        return "UTF-8";
      i += 1;
    }
    return null;
  }

  private static int outputX()
  {
    return 144;
  }

  private static int outputY()
  {
    return 144;
  }

  private static String parseImgPath(Context paramContext, Intent paramIntent)
  {
    if (paramContext == null);
    while (true)
    {
      return null;
      Object localObject2 = null;
      Object localObject1 = localObject2;
      if (paramIntent != null)
      {
        paramIntent = paramIntent.getData();
        localObject1 = localObject2;
        if (paramIntent != null)
          if ("file".equals(paramIntent.getScheme()))
            paramContext = null;
      }
      try
      {
        paramIntent = URLDecoder.decode(paramIntent.toString().replaceFirst("file://", ""), "utf-8");
        paramContext = paramIntent;
        label62: return paramContext;
        Cursor localCursor = paramContext.getContentResolver().query(paramIntent, null, null, null, null);
        if (localCursor == null)
          continue;
        try
        {
          localCursor.moveToFirst();
          paramIntent = localCursor.getString(localCursor.getColumnIndex("_data"));
          paramContext = paramIntent;
          localObject1 = paramContext;
          if (localCursor != null)
          {
            localCursor.close();
            localObject1 = paramContext;
          }
          return localObject1;
        }
        catch (Exception paramIntent)
        {
          while (true)
          {
            Toast.makeText(paramContext, "请换一个文件夹试试！", 0).show();
            paramIntent.printStackTrace();
            localObject1 = localObject2;
            if (localCursor == null)
              continue;
            localCursor.close();
            localObject1 = localObject2;
          }
        }
        finally
        {
          if (localCursor != null)
            localCursor.close();
        }
      }
      catch (java.io.UnsupportedEncodingException paramIntent)
      {
        break label62;
      }
    }
  }

  private static void savePhotoIfPermissionGranted(ImageView paramImageView, Context paramContext)
  {
    if (!Environment.getExternalStorageState().equals("mounted"))
      Toast.makeText(paramContext, "您手机里没有内存卡，无法保存图片", 1).show();
    do
      return;
    while (!(paramImageView.getDrawable() instanceof BitmapDrawable));
    paramImageView = ((BitmapDrawable)paramImageView.getDrawable()).getBitmap();
    if (paramImageView != null)
    {
      new AsyncTask(paramContext)
      {
        public Boolean doInBackground(Bitmap[] paramArrayOfBitmap)
        {
          Object localObject2 = paramArrayOfBitmap[0];
          try
          {
            Object localObject1 = new File(Environment.getExternalStorageDirectory(), "dianping");
            if ((!((File)localObject1).exists()) && (!((File)localObject1).mkdirs()))
              return Boolean.valueOf(false);
            paramArrayOfBitmap = "promphoto" + System.currentTimeMillis() + ".jpg";
            localObject1 = new File((File)localObject1, paramArrayOfBitmap);
            FileOutputStream localFileOutputStream = new FileOutputStream((File)localObject1);
            ((Bitmap)localObject2).compress(Bitmap.CompressFormat.JPEG, 100, localFileOutputStream);
            localFileOutputStream.close();
            localObject2 = new ContentValues(7);
            ((ContentValues)localObject2).put("title", paramArrayOfBitmap);
            ((ContentValues)localObject2).put("_display_name", paramArrayOfBitmap);
            ((ContentValues)localObject2).put("datetaken", Long.valueOf(System.currentTimeMillis()));
            ((ContentValues)localObject2).put("mime_type", "image/jpeg");
            ((ContentValues)localObject2).put("_data", ((File)localObject1).getAbsolutePath());
            paramArrayOfBitmap = this.val$context.getContentResolver();
            localObject1 = paramArrayOfBitmap.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data=?", new String[] { ((File)localObject1).getAbsolutePath() }, null);
            if (((Cursor)localObject1).moveToFirst())
            {
              long l = ((Cursor)localObject1).getLong(((Cursor)localObject1).getColumnIndex("_id"));
              paramArrayOfBitmap.update(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + l), (ContentValues)localObject2, null, null);
            }
            while (true)
            {
              ((Cursor)localObject1).close();
              return Boolean.valueOf(true);
              paramArrayOfBitmap.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, (ContentValues)localObject2);
            }
          }
          catch (Exception paramArrayOfBitmap)
          {
            paramArrayOfBitmap.printStackTrace();
          }
          return (Boolean)(Boolean)Boolean.valueOf(false);
        }

        public void onPostExecute(Boolean paramBoolean)
        {
          if (paramBoolean.booleanValue())
          {
            Toast.makeText(this.val$context, "保存成功", 0).show();
            return;
          }
          Toast.makeText(this.val$context, "保存失败", 0).show();
        }
      }
      .execute(new Bitmap[] { paramImageView });
      return;
    }
    Toast.makeText(paramContext, "保存失败", 0).show();
  }

  public static void savePhotoToAlbum(ImageView paramImageView, Context paramContext)
  {
    paramImageView = new PermissionCheckHelper.PermissionCallbackListener(paramImageView, paramContext)
    {
      public void onPermissionCheckCallback(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt)
      {
        if (paramArrayOfInt[0] == 0)
        {
          ImageUtils.access$000(this.val$view, this.val$context);
          return;
        }
        Toast.makeText(this.val$context, "请在手机的“设置->应用->大众点评->权限”选项中，允许大众点评访问您的存储空间", 0).show();
      }
    };
    String str = paramContext.getResources().getString(R.string.rationale_camera);
    PermissionCheckHelper.instance().requestPermissions(paramContext, 0, new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, new String[] { str }, paramImageView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.util.ImageUtils
 * JD-Core Version:    0.6.0
 */
package com.dianping.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.app.DPApplication;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class FileUtils
{
  private static final char[] HEX_DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };

  public static Bitmap getBitmap(File paramFile)
  {
    return ConvertUtils.bytesToBitmap(getBytes(paramFile));
  }

  // ERROR //
  public static byte[] getBytes(File paramFile)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: new 52	java/io/RandomAccessFile
    //   7: dup
    //   8: aload_0
    //   9: ldc 54
    //   11: invokespecial 57	java/io/RandomAccessFile:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   14: astore_0
    //   15: aload_0
    //   16: invokevirtual 61	java/io/RandomAccessFile:length	()J
    //   19: l2i
    //   20: newarray byte
    //   22: astore_1
    //   23: aload_0
    //   24: aload_1
    //   25: invokevirtual 65	java/io/RandomAccessFile:read	([B)I
    //   28: pop
    //   29: aload_0
    //   30: ifnull +7 -> 37
    //   33: aload_0
    //   34: invokevirtual 68	java/io/RandomAccessFile:close	()V
    //   37: aload_1
    //   38: areturn
    //   39: astore_0
    //   40: aload_0
    //   41: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   44: goto -7 -> 37
    //   47: astore_1
    //   48: aload_2
    //   49: astore_0
    //   50: aload_1
    //   51: astore_2
    //   52: aload_0
    //   53: astore_1
    //   54: aload_2
    //   55: invokevirtual 72	java/lang/Exception:printStackTrace	()V
    //   58: aload_0
    //   59: ifnull +7 -> 66
    //   62: aload_0
    //   63: invokevirtual 68	java/io/RandomAccessFile:close	()V
    //   66: aconst_null
    //   67: areturn
    //   68: astore_0
    //   69: aload_0
    //   70: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   73: goto -7 -> 66
    //   76: astore_0
    //   77: aload_1
    //   78: ifnull +7 -> 85
    //   81: aload_1
    //   82: invokevirtual 68	java/io/RandomAccessFile:close	()V
    //   85: aload_0
    //   86: athrow
    //   87: astore_1
    //   88: aload_1
    //   89: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   92: goto -7 -> 85
    //   95: astore_2
    //   96: aload_0
    //   97: astore_1
    //   98: aload_2
    //   99: astore_0
    //   100: goto -23 -> 77
    //   103: astore_2
    //   104: goto -52 -> 52
    //
    // Exception table:
    //   from	to	target	type
    //   33	37	39	java/io/IOException
    //   4	15	47	java/lang/Exception
    //   62	66	68	java/io/IOException
    //   4	15	76	finally
    //   54	58	76	finally
    //   81	85	87	java/io/IOException
    //   15	29	95	finally
    //   15	29	103	java/lang/Exception
  }

  public static Drawable getDrawable(File paramFile)
  {
    return ConvertUtils.bytesToDrawable(getBytes(paramFile));
  }

  public static <T> T getParcelable(File paramFile, Parcelable.Creator<T> paramCreator)
  {
    return ParcelableUtils.unmarshall(getBytes(paramFile), paramCreator);
  }

  public static <T> List<T> getParcelableArray(File paramFile, Parcelable.Creator<T> paramCreator)
  {
    paramFile = getBytes(paramFile);
    if (paramFile == null)
      return null;
    return ParcelableUtils.unmarshallArray(paramFile, paramCreator);
  }

  // ERROR //
  public static Object getSerializable(File paramFile)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 42	com/dianping/util/FileUtils:getBytes	(Ljava/io/File;)[B
    //   4: astore_0
    //   5: aload_0
    //   6: ifnull +102 -> 108
    //   9: aconst_null
    //   10: astore_1
    //   11: aconst_null
    //   12: astore 5
    //   14: aconst_null
    //   15: astore_2
    //   16: aconst_null
    //   17: astore_3
    //   18: aconst_null
    //   19: astore 4
    //   21: new 97	java/io/ByteArrayInputStream
    //   24: dup
    //   25: aload_0
    //   26: invokespecial 100	java/io/ByteArrayInputStream:<init>	([B)V
    //   29: astore_0
    //   30: new 102	java/io/ObjectInputStream
    //   33: dup
    //   34: aload_0
    //   35: invokespecial 105	java/io/ObjectInputStream:<init>	(Ljava/io/InputStream;)V
    //   38: astore_1
    //   39: aload_1
    //   40: invokevirtual 109	java/io/ObjectInputStream:readObject	()Ljava/lang/Object;
    //   43: astore_2
    //   44: aload_0
    //   45: ifnull +7 -> 52
    //   48: aload_0
    //   49: invokevirtual 110	java/io/ByteArrayInputStream:close	()V
    //   52: aload_1
    //   53: ifnull +7 -> 60
    //   56: aload_1
    //   57: invokevirtual 111	java/io/ObjectInputStream:close	()V
    //   60: aload_2
    //   61: areturn
    //   62: astore_0
    //   63: aload_0
    //   64: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   67: goto -15 -> 52
    //   70: astore_0
    //   71: aload_0
    //   72: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   75: aload_2
    //   76: areturn
    //   77: astore_3
    //   78: aload 5
    //   80: astore_0
    //   81: aload_0
    //   82: astore_1
    //   83: aload 4
    //   85: astore_2
    //   86: aload_3
    //   87: invokevirtual 72	java/lang/Exception:printStackTrace	()V
    //   90: aload_0
    //   91: ifnull +7 -> 98
    //   94: aload_0
    //   95: invokevirtual 110	java/io/ByteArrayInputStream:close	()V
    //   98: aload 4
    //   100: ifnull +8 -> 108
    //   103: aload 4
    //   105: invokevirtual 111	java/io/ObjectInputStream:close	()V
    //   108: aconst_null
    //   109: areturn
    //   110: astore_0
    //   111: aload_0
    //   112: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   115: goto -17 -> 98
    //   118: astore_0
    //   119: aload_0
    //   120: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   123: goto -15 -> 108
    //   126: astore_0
    //   127: aload_1
    //   128: ifnull +7 -> 135
    //   131: aload_1
    //   132: invokevirtual 110	java/io/ByteArrayInputStream:close	()V
    //   135: aload_2
    //   136: ifnull +7 -> 143
    //   139: aload_2
    //   140: invokevirtual 111	java/io/ObjectInputStream:close	()V
    //   143: aload_0
    //   144: athrow
    //   145: astore_1
    //   146: aload_1
    //   147: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   150: goto -15 -> 135
    //   153: astore_1
    //   154: aload_1
    //   155: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   158: goto -15 -> 143
    //   161: astore 4
    //   163: aload_0
    //   164: astore_1
    //   165: aload_3
    //   166: astore_2
    //   167: aload 4
    //   169: astore_0
    //   170: goto -43 -> 127
    //   173: astore_3
    //   174: aload_1
    //   175: astore_2
    //   176: aload_0
    //   177: astore_1
    //   178: aload_3
    //   179: astore_0
    //   180: goto -53 -> 127
    //   183: astore_3
    //   184: goto -103 -> 81
    //   187: astore_3
    //   188: aload_1
    //   189: astore 4
    //   191: goto -110 -> 81
    //
    // Exception table:
    //   from	to	target	type
    //   48	52	62	java/io/IOException
    //   56	60	70	java/io/IOException
    //   21	30	77	java/lang/Exception
    //   94	98	110	java/io/IOException
    //   103	108	118	java/io/IOException
    //   21	30	126	finally
    //   86	90	126	finally
    //   131	135	145	java/io/IOException
    //   139	143	153	java/io/IOException
    //   30	39	161	finally
    //   39	44	173	finally
    //   30	39	183	java/lang/Exception
    //   39	44	187	java/lang/Exception
  }

  public static String getString(File paramFile)
  {
    paramFile = getBytes(paramFile);
    if (paramFile != null)
      return new String(paramFile, Charset.forName("UTF-8"));
    return null;
  }

  public static String md5sum(String paramString)
  {
    byte[] arrayOfByte = new byte[1024];
    MessageDigest localMessageDigest;
    try
    {
      paramString = new FileInputStream(paramString);
      localMessageDigest = MessageDigest.getInstance("MD5");
      while (true)
      {
        int i = paramString.read(arrayOfByte);
        if (i <= 0)
          break;
        localMessageDigest.update(arrayOfByte, 0, i);
      }
    }
    catch (java.lang.Exception paramString)
    {
      System.out.println("error");
      return null;
    }
    paramString.close();
    paramString = toHexString(localMessageDigest.digest());
    return paramString;
  }

  public static boolean put(File paramFile, Bitmap paramBitmap)
  {
    return put(paramFile, ConvertUtils.bitmapToBytes(paramBitmap));
  }

  public static boolean put(File paramFile, Drawable paramDrawable)
  {
    return put(paramFile, ConvertUtils.drawableToBytes(paramDrawable));
  }

  public static boolean put(File paramFile, Parcelable paramParcelable)
  {
    return put(paramFile, ParcelableUtils.marshall(paramParcelable));
  }

  // ERROR //
  public static boolean put(File paramFile, java.io.Serializable paramSerializable)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore 4
    //   5: aconst_null
    //   6: astore_3
    //   7: new 192	java/io/ByteArrayOutputStream
    //   10: dup
    //   11: invokespecial 193	java/io/ByteArrayOutputStream:<init>	()V
    //   14: astore 5
    //   16: new 195	java/io/ObjectOutputStream
    //   19: dup
    //   20: aload 5
    //   22: invokespecial 198	java/io/ObjectOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   25: astore_2
    //   26: aload_2
    //   27: aload_1
    //   28: invokevirtual 202	java/io/ObjectOutputStream:writeObject	(Ljava/lang/Object;)V
    //   31: aload_0
    //   32: aload 5
    //   34: invokevirtual 205	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   37: invokestatic 179	com/dianping/util/FileUtils:put	(Ljava/io/File;[B)Z
    //   40: istore 6
    //   42: aload_2
    //   43: ifnull +7 -> 50
    //   46: aload_2
    //   47: invokevirtual 206	java/io/ObjectOutputStream:close	()V
    //   50: iload 6
    //   52: ireturn
    //   53: astore_0
    //   54: aload_0
    //   55: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   58: goto -8 -> 50
    //   61: astore_0
    //   62: aload_3
    //   63: astore_1
    //   64: aload_1
    //   65: astore_2
    //   66: aload_0
    //   67: invokevirtual 72	java/lang/Exception:printStackTrace	()V
    //   70: aload_1
    //   71: ifnull +7 -> 78
    //   74: aload_1
    //   75: invokevirtual 206	java/io/ObjectOutputStream:close	()V
    //   78: iconst_0
    //   79: ireturn
    //   80: astore_0
    //   81: aload_0
    //   82: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   85: goto -7 -> 78
    //   88: astore_0
    //   89: aload_2
    //   90: ifnull +7 -> 97
    //   93: aload_2
    //   94: invokevirtual 206	java/io/ObjectOutputStream:close	()V
    //   97: aload_0
    //   98: athrow
    //   99: astore_1
    //   100: aload_1
    //   101: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   104: goto -7 -> 97
    //   107: astore_0
    //   108: aload 4
    //   110: astore_2
    //   111: goto -22 -> 89
    //   114: astore_0
    //   115: goto -26 -> 89
    //   118: astore_0
    //   119: aload_3
    //   120: astore_1
    //   121: goto -57 -> 64
    //   124: astore_0
    //   125: aload_2
    //   126: astore_1
    //   127: goto -63 -> 64
    //
    // Exception table:
    //   from	to	target	type
    //   46	50	53	java/io/IOException
    //   7	16	61	java/lang/Exception
    //   74	78	80	java/io/IOException
    //   7	16	88	finally
    //   66	70	88	finally
    //   93	97	99	java/io/IOException
    //   16	26	107	finally
    //   26	42	114	finally
    //   16	26	118	java/lang/Exception
    //   26	42	124	java/lang/Exception
  }

  public static boolean put(File paramFile, String paramString)
  {
    try
    {
      boolean bool = put(paramFile, paramString.getBytes("UTF-8"));
      return bool;
    }
    catch (UnsupportedEncodingException paramFile)
    {
      paramFile.printStackTrace();
    }
    return false;
  }

  // ERROR //
  public static boolean put(File paramFile, byte[] paramArrayOfByte)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: new 215	java/io/FileOutputStream
    //   7: dup
    //   8: aload_0
    //   9: invokespecial 218	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   12: astore_0
    //   13: aload_0
    //   14: aload_1
    //   15: invokevirtual 221	java/io/FileOutputStream:write	([B)V
    //   18: aload_0
    //   19: invokevirtual 224	java/io/FileOutputStream:flush	()V
    //   22: aload_0
    //   23: ifnull +7 -> 30
    //   26: aload_0
    //   27: invokevirtual 225	java/io/FileOutputStream:close	()V
    //   30: iconst_1
    //   31: ireturn
    //   32: astore_0
    //   33: aload_0
    //   34: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   37: goto -7 -> 30
    //   40: astore_1
    //   41: aload_3
    //   42: astore_0
    //   43: aload_0
    //   44: astore_2
    //   45: aload_1
    //   46: invokevirtual 72	java/lang/Exception:printStackTrace	()V
    //   49: aload_0
    //   50: ifnull +7 -> 57
    //   53: aload_0
    //   54: invokevirtual 225	java/io/FileOutputStream:close	()V
    //   57: iconst_0
    //   58: ireturn
    //   59: astore_0
    //   60: aload_0
    //   61: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   64: goto -7 -> 57
    //   67: astore_0
    //   68: aload_2
    //   69: ifnull +7 -> 76
    //   72: aload_2
    //   73: invokevirtual 225	java/io/FileOutputStream:close	()V
    //   76: aload_0
    //   77: athrow
    //   78: astore_1
    //   79: aload_1
    //   80: invokevirtual 71	java/io/IOException:printStackTrace	()V
    //   83: goto -7 -> 76
    //   86: astore_1
    //   87: aload_0
    //   88: astore_2
    //   89: aload_1
    //   90: astore_0
    //   91: goto -23 -> 68
    //   94: astore_1
    //   95: goto -52 -> 43
    //
    // Exception table:
    //   from	to	target	type
    //   26	30	32	java/io/IOException
    //   4	13	40	java/lang/Exception
    //   53	57	59	java/io/IOException
    //   4	13	67	finally
    //   45	49	67	finally
    //   72	76	78	java/io/IOException
    //   13	22	86	finally
    //   13	22	94	java/lang/Exception
  }

  public static boolean put(File paramFile, Parcelable[] paramArrayOfParcelable)
  {
    return put(paramFile, ParcelableUtils.marshallArray(paramArrayOfParcelable));
  }

  public static void removeAllFiles(File paramFile)
  {
    if (paramFile != null)
    {
      File[] arrayOfFile = paramFile.listFiles();
      if (arrayOfFile != null)
      {
        int j = arrayOfFile.length;
        int i = 0;
        if (i < j)
        {
          File localFile = arrayOfFile[i];
          if (localFile.isFile())
            localFile.delete();
          while (true)
          {
            i += 1;
            break;
            if (!localFile.isDirectory())
              continue;
            removeAllFiles(localFile);
          }
        }
      }
      paramFile.delete();
    }
  }

  public static String toHexString(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramArrayOfByte.length * 2);
    int i = 0;
    while (i < paramArrayOfByte.length)
    {
      localStringBuilder.append(HEX_DIGITS[((paramArrayOfByte[i] & 0xF0) >>> 4)]);
      localStringBuilder.append(HEX_DIGITS[(paramArrayOfByte[i] & 0xF)]);
      i += 1;
    }
    return localStringBuilder.toString();
  }

  private static class ConvertUtils
  {
    static byte[] bitmapToBytes(Bitmap paramBitmap)
    {
      if (paramBitmap == null)
        return null;
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
      return localByteArrayOutputStream.toByteArray();
    }

    private static Drawable bitmapToDrawable(Bitmap paramBitmap)
    {
      return new BitmapDrawable(DPApplication.instance().getResources(), paramBitmap);
    }

    static Bitmap bytesToBitmap(byte[] paramArrayOfByte)
    {
      if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0))
        return null;
      return BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    static Drawable bytesToDrawable(byte[] paramArrayOfByte)
    {
      if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0))
        return null;
      return bitmapToDrawable(bytesToBitmap(paramArrayOfByte));
    }

    private static Bitmap drawableToBitmap(Drawable paramDrawable)
    {
      if (paramDrawable == null)
        return null;
      if ((paramDrawable instanceof BitmapDrawable))
        return ((BitmapDrawable)paramDrawable).getBitmap();
      Bitmap localBitmap = Bitmap.createBitmap(paramDrawable.getIntrinsicWidth(), paramDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      paramDrawable.setBounds(0, 0, localCanvas.getWidth(), localCanvas.getHeight());
      paramDrawable.draw(localCanvas);
      return localBitmap;
    }

    static byte[] drawableToBytes(Drawable paramDrawable)
    {
      if (paramDrawable == null)
        return null;
      return bitmapToBytes(drawableToBitmap(paramDrawable));
    }
  }

  public static class ParcelableUtils
  {
    static byte[] marshall(Parcelable paramParcelable)
    {
      Parcel localParcel = Parcel.obtain();
      paramParcelable.writeToParcel(localParcel, 0);
      paramParcelable = localParcel.marshall();
      localParcel.recycle();
      return paramParcelable;
    }

    static byte[] marshallArray(Parcelable[] paramArrayOfParcelable)
    {
      Parcel localParcel = Parcel.obtain();
      localParcel.writeArray(paramArrayOfParcelable);
      localParcel.setDataPosition(0);
      paramArrayOfParcelable = localParcel.marshall();
      localParcel.recycle();
      return paramArrayOfParcelable;
    }

    public static Parcel unmarshall(byte[] paramArrayOfByte)
    {
      Parcel localParcel = Parcel.obtain();
      localParcel.unmarshall(paramArrayOfByte, 0, paramArrayOfByte.length);
      localParcel.setDataPosition(0);
      return localParcel;
    }

    public static <T> T unmarshall(byte[] paramArrayOfByte, Parcelable.Creator<T> paramCreator)
    {
      paramArrayOfByte = unmarshall(paramArrayOfByte);
      paramCreator = paramCreator.createFromParcel(paramArrayOfByte);
      paramArrayOfByte.recycle();
      return paramCreator;
    }

    public static <T> List<T> unmarshallArray(byte[] paramArrayOfByte, Parcelable.Creator<T> paramCreator)
    {
      ArrayList localArrayList = new ArrayList();
      paramArrayOfByte = unmarshall(paramArrayOfByte);
      paramArrayOfByte.readList(localArrayList, paramCreator.getClass().getClassLoader());
      paramArrayOfByte.recycle();
      return localArrayList;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.FileUtils
 * JD-Core Version:    0.6.0
 */
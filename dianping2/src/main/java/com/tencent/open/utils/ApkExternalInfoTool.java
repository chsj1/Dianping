package com.tencent.open.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ProtocolException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Properties;
import java.util.zip.ZipException;

public final class ApkExternalInfoTool
{
  public static final String CHANNELID = "channelNo";
  private static final ZipLong a = new ZipLong(101010256L);
  private static final ZipShort b = new ZipShort(38651);

  private static byte[] a(RandomAccessFile paramRandomAccessFile)
    throws IOException
  {
    int j = 1;
    long l = paramRandomAccessFile.length() - 22L;
    paramRandomAccessFile.seek(l);
    byte[] arrayOfByte = a.getBytes();
    int i = paramRandomAccessFile.read();
    if (i != -1)
      if ((i != arrayOfByte[0]) || (paramRandomAccessFile.read() != arrayOfByte[1]) || (paramRandomAccessFile.read() != arrayOfByte[2]) || (paramRandomAccessFile.read() != arrayOfByte[3]));
    for (i = j; ; i = 0)
    {
      if (i == 0)
      {
        throw new ZipException("archive is not a ZIP archive");
        l -= 1L;
        paramRandomAccessFile.seek(l);
        i = paramRandomAccessFile.read();
        break;
      }
      paramRandomAccessFile.seek(16L + l + 4L);
      arrayOfByte = new byte[2];
      paramRandomAccessFile.readFully(arrayOfByte);
      i = new ZipShort(arrayOfByte).getValue();
      if (i == 0)
        return null;
      arrayOfByte = new byte[i];
      paramRandomAccessFile.read(arrayOfByte);
      return arrayOfByte;
    }
  }

  // ERROR //
  public static String read(File paramFile, String paramString)
    throws IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: new 46	java/io/RandomAccessFile
    //   5: dup
    //   6: aload_0
    //   7: ldc 90
    //   9: invokespecial 93	java/io/RandomAccessFile:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   12: astore_2
    //   13: aload_2
    //   14: invokestatic 95	com/tencent/open/utils/ApkExternalInfoTool:a	(Ljava/io/RandomAccessFile;)[B
    //   17: astore_0
    //   18: aload_0
    //   19: ifnonnull +17 -> 36
    //   22: aload_3
    //   23: astore_0
    //   24: aload_2
    //   25: ifnull +9 -> 34
    //   28: aload_2
    //   29: invokevirtual 98	java/io/RandomAccessFile:close	()V
    //   32: aload_3
    //   33: astore_0
    //   34: aload_0
    //   35: areturn
    //   36: new 8	com/tencent/open/utils/ApkExternalInfoTool$ApkExternalInfo
    //   39: dup
    //   40: aconst_null
    //   41: invokespecial 101	com/tencent/open/utils/ApkExternalInfoTool$ApkExternalInfo:<init>	(Lcom/tencent/open/utils/ApkExternalInfoTool$1;)V
    //   44: astore_3
    //   45: aload_3
    //   46: aload_0
    //   47: invokevirtual 103	com/tencent/open/utils/ApkExternalInfoTool$ApkExternalInfo:a	([B)V
    //   50: aload_3
    //   51: getfield 106	com/tencent/open/utils/ApkExternalInfoTool$ApkExternalInfo:a	Ljava/util/Properties;
    //   54: aload_1
    //   55: invokevirtual 112	java/util/Properties:getProperty	(Ljava/lang/String;)Ljava/lang/String;
    //   58: astore_1
    //   59: aload_1
    //   60: astore_0
    //   61: aload_2
    //   62: ifnull -28 -> 34
    //   65: aload_2
    //   66: invokevirtual 98	java/io/RandomAccessFile:close	()V
    //   69: aload_1
    //   70: areturn
    //   71: astore_0
    //   72: aconst_null
    //   73: astore_1
    //   74: aload_1
    //   75: ifnull +7 -> 82
    //   78: aload_1
    //   79: invokevirtual 98	java/io/RandomAccessFile:close	()V
    //   82: aload_0
    //   83: athrow
    //   84: astore_0
    //   85: aload_2
    //   86: astore_1
    //   87: goto -13 -> 74
    //
    // Exception table:
    //   from	to	target	type
    //   2	13	71	finally
    //   13	18	84	finally
    //   36	59	84	finally
  }

  public static String readChannelId(File paramFile)
    throws IOException
  {
    return read(paramFile, "channelNo");
  }

  private static class ApkExternalInfo
  {
    Properties a = new Properties();
    byte[] b;

    void a(byte[] paramArrayOfByte)
      throws IOException
    {
      if (paramArrayOfByte == null);
      ByteBuffer localByteBuffer;
      int i;
      do
      {
        int j;
        do
        {
          do
          {
            return;
            localByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
            i = ApkExternalInfoTool.a().getBytes().length;
            arrayOfByte = new byte[i];
            localByteBuffer.get(arrayOfByte);
            if (ApkExternalInfoTool.a().equals(new ZipShort(arrayOfByte)))
              continue;
            throw new ProtocolException("unknow protocl [" + Arrays.toString(paramArrayOfByte) + "]");
          }
          while (paramArrayOfByte.length - i <= 2);
          arrayOfByte = new byte[2];
          localByteBuffer.get(arrayOfByte);
          j = new ZipShort(arrayOfByte).getValue();
        }
        while (paramArrayOfByte.length - i - 2 < j);
        byte[] arrayOfByte = new byte[j];
        localByteBuffer.get(arrayOfByte);
        this.a.load(new ByteArrayInputStream(arrayOfByte));
        i = paramArrayOfByte.length - i - j - 2;
      }
      while (i <= 0);
      this.b = new byte[i];
      localByteBuffer.get(this.b);
    }

    public String toString()
    {
      return "ApkExternalInfo [p=" + this.a + ", otherData=" + Arrays.toString(this.b) + "]";
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.open.utils.ApkExternalInfoTool
 * JD-Core Version:    0.6.0
 */
package com.dianping.util.encrypt;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64
{
  public static final int DECODE = 0;
  public static final int DONT_GUNZIP = 4;
  public static final int DO_BREAK_LINES = 8;
  public static final int ENCODE = 1;
  private static final byte EQUALS_SIGN = 61;
  private static final byte EQUALS_SIGN_ENC = -1;
  public static final int GZIP = 2;
  private static final int MAX_LINE_LENGTH = 76;
  private static final byte NEW_LINE = 10;
  public static final int NO_OPTIONS = 0;
  public static final int ORDERED = 32;
  private static final String PREFERRED_ENCODING = "US-ASCII";
  public static final int URL_SAFE = 16;
  private static final byte WHITE_SPACE_ENC = -5;
  private static final byte[] _ORDERED_ALPHABET;
  private static final byte[] _ORDERED_DECODABET;
  private static final byte[] _STANDARD_ALPHABET = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
  private static final byte[] _STANDARD_DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };
  private static final byte[] _URL_SAFE_ALPHABET = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
  private static final byte[] _URL_SAFE_DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };

  static
  {
    _ORDERED_ALPHABET = new byte[] { 45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 };
    _ORDERED_DECODABET = new byte[] { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };
  }

  public static byte[] decode(String paramString)
    throws IOException
  {
    return decode(paramString, 0);
  }

  // ERROR //
  public static byte[] decode(String paramString, int paramInt)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull +13 -> 14
    //   4: new 185	java/lang/NullPointerException
    //   7: dup
    //   8: ldc 187
    //   10: invokespecial 190	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   13: athrow
    //   14: aload_0
    //   15: ldc 34
    //   17: invokevirtual 195	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   20: astore_2
    //   21: aload_2
    //   22: astore_0
    //   23: aload_0
    //   24: iconst_0
    //   25: aload_0
    //   26: arraylength
    //   27: iload_1
    //   28: invokestatic 198	com/dianping/util/encrypt/Base64:decode	([BIII)[B
    //   31: astore 11
    //   33: iload_1
    //   34: iconst_4
    //   35: iand
    //   36: ifeq +171 -> 207
    //   39: iconst_1
    //   40: istore_1
    //   41: aload 11
    //   43: ifnull +152 -> 195
    //   46: aload 11
    //   48: arraylength
    //   49: iconst_4
    //   50: if_icmplt +145 -> 195
    //   53: iload_1
    //   54: ifne +141 -> 195
    //   57: ldc 199
    //   59: aload 11
    //   61: iconst_0
    //   62: baload
    //   63: sipush 255
    //   66: iand
    //   67: aload 11
    //   69: iconst_1
    //   70: baload
    //   71: bipush 8
    //   73: ishl
    //   74: ldc 200
    //   76: iand
    //   77: ior
    //   78: if_icmpne +117 -> 195
    //   81: aconst_null
    //   82: astore 4
    //   84: aconst_null
    //   85: astore 9
    //   87: aconst_null
    //   88: astore_3
    //   89: aconst_null
    //   90: astore 6
    //   92: aconst_null
    //   93: astore 8
    //   95: aconst_null
    //   96: astore 7
    //   98: aconst_null
    //   99: astore 5
    //   101: aconst_null
    //   102: astore 10
    //   104: sipush 2048
    //   107: newarray byte
    //   109: astore 12
    //   111: new 202	java/io/ByteArrayOutputStream
    //   114: dup
    //   115: invokespecial 203	java/io/ByteArrayOutputStream:<init>	()V
    //   118: astore_0
    //   119: new 205	java/io/ByteArrayInputStream
    //   122: dup
    //   123: aload 11
    //   125: invokespecial 208	java/io/ByteArrayInputStream:<init>	([B)V
    //   128: astore_2
    //   129: new 210	java/util/zip/GZIPInputStream
    //   132: dup
    //   133: aload_2
    //   134: invokespecial 213	java/util/zip/GZIPInputStream:<init>	(Ljava/io/InputStream;)V
    //   137: astore_3
    //   138: aload_3
    //   139: aload 12
    //   141: invokevirtual 217	java/util/zip/GZIPInputStream:read	([B)I
    //   144: istore_1
    //   145: iload_1
    //   146: iflt +66 -> 212
    //   149: aload_0
    //   150: aload 12
    //   152: iconst_0
    //   153: iload_1
    //   154: invokevirtual 221	java/io/ByteArrayOutputStream:write	([BII)V
    //   157: goto -19 -> 138
    //   160: astore 4
    //   162: aload_3
    //   163: astore 7
    //   165: aload 4
    //   167: astore_3
    //   168: aload_2
    //   169: astore 4
    //   171: aload_0
    //   172: astore 5
    //   174: aload 7
    //   176: astore 6
    //   178: aload_3
    //   179: invokevirtual 224	java/io/IOException:printStackTrace	()V
    //   182: aload_0
    //   183: invokevirtual 227	java/io/ByteArrayOutputStream:close	()V
    //   186: aload 7
    //   188: invokevirtual 228	java/util/zip/GZIPInputStream:close	()V
    //   191: aload_2
    //   192: invokevirtual 229	java/io/ByteArrayInputStream:close	()V
    //   195: aload 11
    //   197: areturn
    //   198: astore_2
    //   199: aload_0
    //   200: invokevirtual 232	java/lang/String:getBytes	()[B
    //   203: astore_0
    //   204: goto -181 -> 23
    //   207: iconst_0
    //   208: istore_1
    //   209: goto -168 -> 41
    //   212: aload_0
    //   213: invokevirtual 235	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   216: astore 4
    //   218: aload_0
    //   219: invokevirtual 227	java/io/ByteArrayOutputStream:close	()V
    //   222: aload_3
    //   223: invokevirtual 228	java/util/zip/GZIPInputStream:close	()V
    //   226: aload_2
    //   227: invokevirtual 229	java/io/ByteArrayInputStream:close	()V
    //   230: aload 4
    //   232: areturn
    //   233: astore_0
    //   234: aload 4
    //   236: areturn
    //   237: astore_3
    //   238: aload 5
    //   240: astore_2
    //   241: aload 4
    //   243: astore_0
    //   244: aload_2
    //   245: invokevirtual 227	java/io/ByteArrayOutputStream:close	()V
    //   248: aload 6
    //   250: invokevirtual 228	java/util/zip/GZIPInputStream:close	()V
    //   253: aload_0
    //   254: invokevirtual 229	java/io/ByteArrayInputStream:close	()V
    //   257: aload_3
    //   258: athrow
    //   259: astore_0
    //   260: goto -38 -> 222
    //   263: astore_0
    //   264: goto -38 -> 226
    //   267: astore_0
    //   268: goto -82 -> 186
    //   271: astore_0
    //   272: goto -81 -> 191
    //   275: astore_0
    //   276: aload 11
    //   278: areturn
    //   279: astore_2
    //   280: goto -32 -> 248
    //   283: astore_2
    //   284: goto -31 -> 253
    //   287: astore_0
    //   288: goto -31 -> 257
    //   291: astore_3
    //   292: aload_0
    //   293: astore_2
    //   294: aload 9
    //   296: astore_0
    //   297: aload 8
    //   299: astore 6
    //   301: goto -57 -> 244
    //   304: astore 4
    //   306: aload_0
    //   307: astore_3
    //   308: aload_2
    //   309: astore_0
    //   310: aload_3
    //   311: astore_2
    //   312: aload 8
    //   314: astore 6
    //   316: aload 4
    //   318: astore_3
    //   319: goto -75 -> 244
    //   322: astore 5
    //   324: aload_0
    //   325: astore 4
    //   327: aload_2
    //   328: astore_0
    //   329: aload 4
    //   331: astore_2
    //   332: aload_3
    //   333: astore 6
    //   335: aload 5
    //   337: astore_3
    //   338: goto -94 -> 244
    //   341: astore 4
    //   343: aload_3
    //   344: astore_2
    //   345: aload 10
    //   347: astore_0
    //   348: aload 4
    //   350: astore_3
    //   351: goto -183 -> 168
    //   354: astore 4
    //   356: aload_3
    //   357: astore_2
    //   358: aload 4
    //   360: astore_3
    //   361: goto -193 -> 168
    //   364: astore_3
    //   365: goto -197 -> 168
    //
    // Exception table:
    //   from	to	target	type
    //   138	145	160	java/io/IOException
    //   149	157	160	java/io/IOException
    //   212	218	160	java/io/IOException
    //   14	21	198	java/io/UnsupportedEncodingException
    //   226	230	233	java/lang/Exception
    //   111	119	237	finally
    //   178	182	237	finally
    //   218	222	259	java/lang/Exception
    //   222	226	263	java/lang/Exception
    //   182	186	267	java/lang/Exception
    //   186	191	271	java/lang/Exception
    //   191	195	275	java/lang/Exception
    //   244	248	279	java/lang/Exception
    //   248	253	283	java/lang/Exception
    //   253	257	287	java/lang/Exception
    //   119	129	291	finally
    //   129	138	304	finally
    //   138	145	322	finally
    //   149	157	322	finally
    //   212	218	322	finally
    //   111	119	341	java/io/IOException
    //   119	129	354	java/io/IOException
    //   129	138	364	java/io/IOException
  }

  public static byte[] decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
    throws IOException
  {
    if (paramArrayOfByte == null)
      throw new NullPointerException("Cannot decode null source array.");
    if ((paramInt1 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length))
      throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", new Object[] { Integer.valueOf(paramArrayOfByte.length), Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
    if (paramInt2 == 0)
      return new byte[0];
    if (paramInt2 < 4)
      throw new IllegalArgumentException("Base64-encoded string must have at least four characters, but length specified was " + paramInt2);
    byte[] arrayOfByte2 = getDecodabet(paramInt3);
    byte[] arrayOfByte1 = new byte[paramInt2 * 3 / 4];
    int j = 0;
    byte[] arrayOfByte3 = new byte[4];
    int k = paramInt1;
    int i = 0;
    int m;
    if (k < paramInt1 + paramInt2)
    {
      m = arrayOfByte2[(paramArrayOfByte[k] & 0xFF)];
      if (m >= -5)
      {
        if (m < -1)
          break label287;
        int n = i + 1;
        arrayOfByte3[i] = paramArrayOfByte[k];
        i = n;
        m = j;
        if (n <= 3)
          break label291;
        j += decode4to3(arrayOfByte3, 0, arrayOfByte1, j, paramInt3);
        i = 0;
        m = j;
        if (paramArrayOfByte[k] != 61)
          break label291;
      }
    }
    while (true)
    {
      paramArrayOfByte = new byte[j];
      System.arraycopy(arrayOfByte1, 0, paramArrayOfByte, 0, j);
      return paramArrayOfByte;
      throw new IOException(String.format("Bad Base64 input character decimal %d in array position %d", new Object[] { Integer.valueOf(paramArrayOfByte[k] & 0xFF), Integer.valueOf(k) }));
      label287: m = j;
      label291: k += 1;
      j = m;
      break;
    }
  }

  static int decode4to3(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
  {
    if (paramArrayOfByte1 == null)
      throw new NullPointerException("Source array was null.");
    if (paramArrayOfByte2 == null)
      throw new NullPointerException("Destination array was null.");
    if ((paramInt1 < 0) || (paramInt1 + 3 >= paramArrayOfByte1.length))
      throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", new Object[] { Integer.valueOf(paramArrayOfByte1.length), Integer.valueOf(paramInt1) }));
    if ((paramInt2 < 0) || (paramInt2 + 2 >= paramArrayOfByte2.length))
      throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", new Object[] { Integer.valueOf(paramArrayOfByte2.length), Integer.valueOf(paramInt2) }));
    byte[] arrayOfByte = getDecodabet(paramInt3);
    if (paramArrayOfByte1[(paramInt1 + 2)] == 61)
    {
      paramArrayOfByte2[paramInt2] = (byte)(((arrayOfByte[paramArrayOfByte1[paramInt1]] & 0xFF) << 18 | (arrayOfByte[paramArrayOfByte1[(paramInt1 + 1)]] & 0xFF) << 12) >>> 16);
      return 1;
    }
    if (paramArrayOfByte1[(paramInt1 + 3)] == 61)
    {
      paramInt1 = (arrayOfByte[paramArrayOfByte1[paramInt1]] & 0xFF) << 18 | (arrayOfByte[paramArrayOfByte1[(paramInt1 + 1)]] & 0xFF) << 12 | (arrayOfByte[paramArrayOfByte1[(paramInt1 + 2)]] & 0xFF) << 6;
      paramArrayOfByte2[paramInt2] = (byte)(paramInt1 >>> 16);
      paramArrayOfByte2[(paramInt2 + 1)] = (byte)(paramInt1 >>> 8);
      return 2;
    }
    paramInt1 = (arrayOfByte[paramArrayOfByte1[paramInt1]] & 0xFF) << 18 | (arrayOfByte[paramArrayOfByte1[(paramInt1 + 1)]] & 0xFF) << 12 | (arrayOfByte[paramArrayOfByte1[(paramInt1 + 2)]] & 0xFF) << 6 | arrayOfByte[paramArrayOfByte1[(paramInt1 + 3)]] & 0xFF;
    paramArrayOfByte2[paramInt2] = (byte)(paramInt1 >> 16);
    paramArrayOfByte2[(paramInt2 + 1)] = (byte)(paramInt1 >> 8);
    paramArrayOfByte2[(paramInt2 + 2)] = (byte)paramInt1;
    return 3;
  }

  private static byte[] encode3to4(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4)
  {
    int j = 0;
    byte[] arrayOfByte = getAlphabet(paramInt4);
    if (paramInt2 > 0)
    {
      paramInt4 = paramArrayOfByte1[paramInt1] << 24 >>> 8;
      label25: if (paramInt2 <= 1)
        break label104;
    }
    label104: for (int i = paramArrayOfByte1[(paramInt1 + 1)] << 24 >>> 16; ; i = 0)
    {
      if (paramInt2 > 2)
        j = paramArrayOfByte1[(paramInt1 + 2)] << 24 >>> 24;
      paramInt1 = i | paramInt4 | j;
      switch (paramInt2)
      {
      default:
        return paramArrayOfByte2;
        paramInt4 = 0;
        break label25;
      case 3:
      case 2:
      case 1:
      }
    }
    paramArrayOfByte2[paramInt3] = arrayOfByte[(paramInt1 >>> 18)];
    paramArrayOfByte2[(paramInt3 + 1)] = arrayOfByte[(paramInt1 >>> 12 & 0x3F)];
    paramArrayOfByte2[(paramInt3 + 2)] = arrayOfByte[(paramInt1 >>> 6 & 0x3F)];
    paramArrayOfByte2[(paramInt3 + 3)] = arrayOfByte[(paramInt1 & 0x3F)];
    return paramArrayOfByte2;
    paramArrayOfByte2[paramInt3] = arrayOfByte[(paramInt1 >>> 18)];
    paramArrayOfByte2[(paramInt3 + 1)] = arrayOfByte[(paramInt1 >>> 12 & 0x3F)];
    paramArrayOfByte2[(paramInt3 + 2)] = arrayOfByte[(paramInt1 >>> 6 & 0x3F)];
    paramArrayOfByte2[(paramInt3 + 3)] = 61;
    return paramArrayOfByte2;
    paramArrayOfByte2[paramInt3] = arrayOfByte[(paramInt1 >>> 18)];
    paramArrayOfByte2[(paramInt3 + 1)] = arrayOfByte[(paramInt1 >>> 12 & 0x3F)];
    paramArrayOfByte2[(paramInt3 + 2)] = 61;
    paramArrayOfByte2[(paramInt3 + 3)] = 61;
    return paramArrayOfByte2;
  }

  static byte[] encode3to4(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
  {
    encode3to4(paramArrayOfByte2, 0, paramInt1, paramArrayOfByte1, 0, paramInt2);
    return paramArrayOfByte1;
  }

  // ERROR //
  public static byte[] encodeBytesToBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull +14 -> 15
    //   4: new 185	java/lang/NullPointerException
    //   7: dup
    //   8: ldc_w 304
    //   11: invokespecial 190	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   14: athrow
    //   15: iload_1
    //   16: ifge +31 -> 47
    //   19: new 239	java/lang/IllegalArgumentException
    //   22: dup
    //   23: new 254	java/lang/StringBuilder
    //   26: dup
    //   27: invokespecial 255	java/lang/StringBuilder:<init>	()V
    //   30: ldc_w 306
    //   33: invokevirtual 261	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   36: iload_1
    //   37: invokevirtual 264	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   40: invokevirtual 268	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   43: invokespecial 252	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   46: athrow
    //   47: iload_2
    //   48: ifge +31 -> 79
    //   51: new 239	java/lang/IllegalArgumentException
    //   54: dup
    //   55: new 254	java/lang/StringBuilder
    //   58: dup
    //   59: invokespecial 255	java/lang/StringBuilder:<init>	()V
    //   62: ldc_w 308
    //   65: invokevirtual 261	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: iload_2
    //   69: invokevirtual 264	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   72: invokevirtual 268	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   75: invokespecial 252	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   78: athrow
    //   79: iload_1
    //   80: iload_2
    //   81: iadd
    //   82: aload_0
    //   83: arraylength
    //   84: if_icmple +43 -> 127
    //   87: new 239	java/lang/IllegalArgumentException
    //   90: dup
    //   91: ldc_w 310
    //   94: iconst_3
    //   95: anewarray 4	java/lang/Object
    //   98: dup
    //   99: iconst_0
    //   100: iload_1
    //   101: invokestatic 247	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   104: aastore
    //   105: dup
    //   106: iconst_1
    //   107: iload_2
    //   108: invokestatic 247	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   111: aastore
    //   112: dup
    //   113: iconst_2
    //   114: aload_0
    //   115: arraylength
    //   116: invokestatic 247	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   119: aastore
    //   120: invokestatic 251	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   123: invokespecial 252	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   126: athrow
    //   127: iload_3
    //   128: iconst_2
    //   129: iand
    //   130: ifeq +140 -> 270
    //   133: aconst_null
    //   134: astore 5
    //   136: aconst_null
    //   137: astore 12
    //   139: aconst_null
    //   140: astore 7
    //   142: aconst_null
    //   143: astore 9
    //   145: aconst_null
    //   146: astore 8
    //   148: aconst_null
    //   149: astore 6
    //   151: aconst_null
    //   152: astore 11
    //   154: aconst_null
    //   155: astore 10
    //   157: new 202	java/io/ByteArrayOutputStream
    //   160: dup
    //   161: invokespecial 203	java/io/ByteArrayOutputStream:<init>	()V
    //   164: astore 4
    //   166: new 6	com/dianping/util/encrypt/Base64$OutputStream
    //   169: dup
    //   170: aload 4
    //   172: iload_3
    //   173: iconst_1
    //   174: ior
    //   175: invokespecial 313	com/dianping/util/encrypt/Base64$OutputStream:<init>	(Ljava/io/OutputStream;I)V
    //   178: astore 5
    //   180: new 315	java/util/zip/GZIPOutputStream
    //   183: dup
    //   184: aload 5
    //   186: invokespecial 318	java/util/zip/GZIPOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   189: astore 6
    //   191: aload 6
    //   193: aload_0
    //   194: iload_1
    //   195: iload_2
    //   196: invokevirtual 319	java/util/zip/GZIPOutputStream:write	([BII)V
    //   199: aload 6
    //   201: invokevirtual 320	java/util/zip/GZIPOutputStream:close	()V
    //   204: aload 6
    //   206: invokevirtual 320	java/util/zip/GZIPOutputStream:close	()V
    //   209: aload 5
    //   211: invokevirtual 321	com/dianping/util/encrypt/Base64$OutputStream:close	()V
    //   214: aload 4
    //   216: invokevirtual 227	java/io/ByteArrayOutputStream:close	()V
    //   219: aload 4
    //   221: invokevirtual 235	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   224: areturn
    //   225: astore 4
    //   227: aload 8
    //   229: astore 7
    //   231: aload 12
    //   233: astore_0
    //   234: aload 10
    //   236: astore 5
    //   238: aload 5
    //   240: astore 6
    //   242: aload_0
    //   243: astore 5
    //   245: aload 4
    //   247: athrow
    //   248: astore_0
    //   249: aload 6
    //   251: astore 4
    //   253: aload 7
    //   255: invokevirtual 320	java/util/zip/GZIPOutputStream:close	()V
    //   258: aload 4
    //   260: invokevirtual 321	com/dianping/util/encrypt/Base64$OutputStream:close	()V
    //   263: aload 5
    //   265: invokevirtual 227	java/io/ByteArrayOutputStream:close	()V
    //   268: aload_0
    //   269: athrow
    //   270: iload_3
    //   271: bipush 8
    //   273: iand
    //   274: ifeq +153 -> 427
    //   277: iconst_1
    //   278: istore 15
    //   280: iload_2
    //   281: iconst_3
    //   282: idiv
    //   283: istore 14
    //   285: iload_2
    //   286: iconst_3
    //   287: irem
    //   288: ifle +145 -> 433
    //   291: iconst_4
    //   292: istore 13
    //   294: iload 14
    //   296: iconst_4
    //   297: imul
    //   298: iload 13
    //   300: iadd
    //   301: istore 14
    //   303: iload 14
    //   305: istore 13
    //   307: iload 15
    //   309: ifeq +13 -> 322
    //   312: iload 14
    //   314: iload 14
    //   316: bipush 76
    //   318: idiv
    //   319: iadd
    //   320: istore 13
    //   322: iload 13
    //   324: newarray byte
    //   326: astore 4
    //   328: iconst_0
    //   329: istore 16
    //   331: iconst_0
    //   332: istore 13
    //   334: iconst_0
    //   335: istore 14
    //   337: iload 16
    //   339: iload_2
    //   340: iconst_2
    //   341: isub
    //   342: if_icmpge +97 -> 439
    //   345: aload_0
    //   346: iload 16
    //   348: iload_1
    //   349: iadd
    //   350: iconst_3
    //   351: aload 4
    //   353: iload 13
    //   355: iload_3
    //   356: invokestatic 301	com/dianping/util/encrypt/Base64:encode3to4	([BII[BII)[B
    //   359: pop
    //   360: iload 14
    //   362: iconst_4
    //   363: iadd
    //   364: istore 18
    //   366: iload 13
    //   368: istore 17
    //   370: iload 18
    //   372: istore 14
    //   374: iload 15
    //   376: ifeq +36 -> 412
    //   379: iload 13
    //   381: istore 17
    //   383: iload 18
    //   385: istore 14
    //   387: iload 18
    //   389: bipush 76
    //   391: if_icmplt +21 -> 412
    //   394: aload 4
    //   396: iload 13
    //   398: iconst_4
    //   399: iadd
    //   400: bipush 10
    //   402: bastore
    //   403: iload 13
    //   405: iconst_1
    //   406: iadd
    //   407: istore 17
    //   409: iconst_0
    //   410: istore 14
    //   412: iload 16
    //   414: iconst_3
    //   415: iadd
    //   416: istore 16
    //   418: iload 17
    //   420: iconst_4
    //   421: iadd
    //   422: istore 13
    //   424: goto -87 -> 337
    //   427: iconst_0
    //   428: istore 15
    //   430: goto -150 -> 280
    //   433: iconst_0
    //   434: istore 13
    //   436: goto -142 -> 294
    //   439: iload 13
    //   441: istore 14
    //   443: iload 16
    //   445: iload_2
    //   446: if_icmpge +27 -> 473
    //   449: aload_0
    //   450: iload 16
    //   452: iload_1
    //   453: iadd
    //   454: iload_2
    //   455: iload 16
    //   457: isub
    //   458: aload 4
    //   460: iload 13
    //   462: iload_3
    //   463: invokestatic 301	com/dianping/util/encrypt/Base64:encode3to4	([BII[BII)[B
    //   466: pop
    //   467: iload 13
    //   469: iconst_4
    //   470: iadd
    //   471: istore 14
    //   473: iload 14
    //   475: aload 4
    //   477: arraylength
    //   478: iconst_1
    //   479: isub
    //   480: if_icmpgt +20 -> 500
    //   483: iload 14
    //   485: newarray byte
    //   487: astore_0
    //   488: aload 4
    //   490: iconst_0
    //   491: aload_0
    //   492: iconst_0
    //   493: iload 14
    //   495: invokestatic 282	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   498: aload_0
    //   499: areturn
    //   500: aload 4
    //   502: areturn
    //   503: astore_0
    //   504: goto -295 -> 209
    //   507: astore_0
    //   508: goto -294 -> 214
    //   511: astore_0
    //   512: goto -293 -> 219
    //   515: astore 6
    //   517: goto -259 -> 258
    //   520: astore 4
    //   522: goto -259 -> 263
    //   525: astore 4
    //   527: goto -259 -> 268
    //   530: astore_0
    //   531: aload 4
    //   533: astore 5
    //   535: aload 11
    //   537: astore 4
    //   539: aload 9
    //   541: astore 7
    //   543: goto -290 -> 253
    //   546: astore_0
    //   547: aload 4
    //   549: astore 6
    //   551: aload 5
    //   553: astore 4
    //   555: aload 6
    //   557: astore 5
    //   559: aload 9
    //   561: astore 7
    //   563: goto -310 -> 253
    //   566: astore_0
    //   567: aload 4
    //   569: astore 7
    //   571: aload 5
    //   573: astore 4
    //   575: aload 7
    //   577: astore 5
    //   579: aload 6
    //   581: astore 7
    //   583: goto -330 -> 253
    //   586: astore 5
    //   588: aload 4
    //   590: astore_0
    //   591: aload 5
    //   593: astore 4
    //   595: aload 10
    //   597: astore 5
    //   599: aload 8
    //   601: astore 7
    //   603: goto -365 -> 238
    //   606: astore 6
    //   608: aload 4
    //   610: astore_0
    //   611: aload 6
    //   613: astore 4
    //   615: aload 8
    //   617: astore 7
    //   619: goto -381 -> 238
    //   622: astore 7
    //   624: aload 4
    //   626: astore_0
    //   627: aload 7
    //   629: astore 4
    //   631: aload 6
    //   633: astore 7
    //   635: goto -397 -> 238
    //
    // Exception table:
    //   from	to	target	type
    //   157	166	225	java/io/IOException
    //   157	166	248	finally
    //   245	248	248	finally
    //   204	209	503	java/lang/Exception
    //   209	214	507	java/lang/Exception
    //   214	219	511	java/lang/Exception
    //   253	258	515	java/lang/Exception
    //   258	263	520	java/lang/Exception
    //   263	268	525	java/lang/Exception
    //   166	180	530	finally
    //   180	191	546	finally
    //   191	204	566	finally
    //   166	180	586	java/io/IOException
    //   180	191	606	java/io/IOException
    //   191	204	622	java/io/IOException
  }

  private static final byte[] getAlphabet(int paramInt)
  {
    if ((paramInt & 0x10) == 16)
      return _URL_SAFE_ALPHABET;
    if ((paramInt & 0x20) == 32)
      return _ORDERED_ALPHABET;
    return _STANDARD_ALPHABET;
  }

  static final byte[] getDecodabet(int paramInt)
  {
    if ((paramInt & 0x10) == 16)
      return _URL_SAFE_DECODABET;
    if ((paramInt & 0x20) == 32)
      return _ORDERED_DECODABET;
    return _STANDARD_DECODABET;
  }

  public static class OutputStream extends FilterOutputStream
  {
    private byte[] b4;
    private boolean breakLines;
    private byte[] buffer;
    private int bufferLength;
    private byte[] decodabet;
    private boolean encode;
    private int lineLength;
    private int options;
    private int position;
    private boolean suspendEncoding;

    public OutputStream(OutputStream paramOutputStream)
    {
      this(paramOutputStream, 1);
    }

    public OutputStream(OutputStream paramOutputStream, int paramInt)
    {
      super();
      boolean bool1;
      if ((paramInt & 0x8) != 0)
      {
        bool1 = true;
        this.breakLines = bool1;
        if ((paramInt & 0x1) == 0)
          break label106;
        bool1 = bool2;
        label34: this.encode = bool1;
        if (!this.encode)
          break label112;
      }
      label106: label112: for (int i = 3; ; i = 4)
      {
        this.bufferLength = i;
        this.buffer = new byte[this.bufferLength];
        this.position = 0;
        this.lineLength = 0;
        this.suspendEncoding = false;
        this.b4 = new byte[4];
        this.options = paramInt;
        this.decodabet = Base64.getDecodabet(paramInt);
        return;
        bool1 = false;
        break;
        bool1 = false;
        break label34;
      }
    }

    public void close()
      throws IOException
    {
      flushBase64();
      super.close();
      this.buffer = null;
      this.out = null;
    }

    public void flushBase64()
      throws IOException
    {
      if (this.position > 0)
      {
        if (this.encode)
        {
          this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position, this.options));
          this.position = 0;
        }
      }
      else
        return;
      throw new IOException("Base64 input not properly padded.");
    }

    public void write(int paramInt)
      throws IOException
    {
      if (this.suspendEncoding)
        this.out.write(paramInt);
      do
        while (true)
        {
          return;
          if (this.encode)
          {
            arrayOfByte = this.buffer;
            i = this.position;
            this.position = (i + 1);
            arrayOfByte[i] = (byte)paramInt;
            if (this.position < this.bufferLength)
              continue;
            this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
            this.lineLength += 4;
            if ((this.breakLines) && (this.lineLength >= 76))
            {
              this.out.write(10);
              this.lineLength = 0;
            }
            this.position = 0;
            return;
          }
          if (this.decodabet[(paramInt & 0x7F)] <= -5)
            break;
          byte[] arrayOfByte = this.buffer;
          int i = this.position;
          this.position = (i + 1);
          arrayOfByte[i] = (byte)paramInt;
          if (this.position < this.bufferLength)
            continue;
          paramInt = Base64.decode4to3(this.buffer, 0, this.b4, 0, this.options);
          this.out.write(this.b4, 0, paramInt);
          this.position = 0;
          return;
        }
      while (this.decodabet[(paramInt & 0x7F)] == -5);
      throw new IOException("Invalid character in Base64 data.");
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (this.suspendEncoding)
        this.out.write(paramArrayOfByte, paramInt1, paramInt2);
      while (true)
      {
        return;
        int i = 0;
        while (i < paramInt2)
        {
          write(paramArrayOfByte[(paramInt1 + i)]);
          i += 1;
        }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.encrypt.Base64
 * JD-Core Version:    0.6.0
 */
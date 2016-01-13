package com.google.zxing.client.android;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import java.io.ByteArrayOutputStream;
import java.util.Map;

final class DecodeHandler extends Handler
{
  private static final String TAG = DecodeHandler.class.getSimpleName();
  private final CaptureActivity activity;
  private final MultiFormatReader multiFormatReader = new MultiFormatReader();
  private boolean running = true;

  DecodeHandler(CaptureActivity paramCaptureActivity, Map<DecodeHintType, Object> paramMap)
  {
    this.multiFormatReader.setHints(paramMap);
    this.activity = paramCaptureActivity;
  }

  private static void bundleThumbnail(PlanarYUVLuminanceSource paramPlanarYUVLuminanceSource, Bundle paramBundle)
  {
    Object localObject = paramPlanarYUVLuminanceSource.renderThumbnail();
    int i = paramPlanarYUVLuminanceSource.getThumbnailWidth();
    localObject = Bitmap.createBitmap(localObject, 0, i, i, paramPlanarYUVLuminanceSource.getThumbnailHeight(), Bitmap.Config.ARGB_8888);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ((Bitmap)localObject).compress(Bitmap.CompressFormat.JPEG, 50, localByteArrayOutputStream);
    paramBundle.putByteArray("barcode_bitmap", localByteArrayOutputStream.toByteArray());
    paramBundle.putFloat("barcode_scaled_factor", i / paramPlanarYUVLuminanceSource.getWidth());
  }

  // ERROR //
  private void decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: invokestatic 113	java/lang/System:currentTimeMillis	()J
    //   3: lstore 8
    //   5: aconst_null
    //   6: astore 4
    //   8: aload_1
    //   9: arraylength
    //   10: newarray byte
    //   12: astore 5
    //   14: iconst_0
    //   15: istore 6
    //   17: iload 6
    //   19: iload_3
    //   20: if_icmpge +53 -> 73
    //   23: iconst_0
    //   24: istore 7
    //   26: iload 7
    //   28: iload_2
    //   29: if_icmpge +35 -> 64
    //   32: aload 5
    //   34: iload 7
    //   36: iload_3
    //   37: imul
    //   38: iload_3
    //   39: iadd
    //   40: iload 6
    //   42: isub
    //   43: iconst_1
    //   44: isub
    //   45: aload_1
    //   46: iload 6
    //   48: iload_2
    //   49: imul
    //   50: iload 7
    //   52: iadd
    //   53: baload
    //   54: bastore
    //   55: iload 7
    //   57: iconst_1
    //   58: iadd
    //   59: istore 7
    //   61: goto -35 -> 26
    //   64: iload 6
    //   66: iconst_1
    //   67: iadd
    //   68: istore 6
    //   70: goto -53 -> 17
    //   73: aload_0
    //   74: getfield 40	com/google/zxing/client/android/DecodeHandler:activity	Lcom/google/zxing/client/android/CaptureActivity;
    //   77: invokevirtual 119	com/google/zxing/client/android/CaptureActivity:getCameraManager	()Lcom/google/zxing/client/android/camera/CameraManager;
    //   80: aload 5
    //   82: iload_3
    //   83: iload_2
    //   84: invokevirtual 125	com/google/zxing/client/android/camera/CameraManager:buildLuminanceSource	([BII)Lcom/google/zxing/PlanarYUVLuminanceSource;
    //   87: astore 5
    //   89: aload 4
    //   91: astore_1
    //   92: aload 5
    //   94: ifnull +36 -> 130
    //   97: new 127	com/google/zxing/BinaryBitmap
    //   100: dup
    //   101: new 129	com/google/zxing/common/HybridBinarizer
    //   104: dup
    //   105: aload 5
    //   107: invokespecial 132	com/google/zxing/common/HybridBinarizer:<init>	(Lcom/google/zxing/LuminanceSource;)V
    //   110: invokespecial 135	com/google/zxing/BinaryBitmap:<init>	(Lcom/google/zxing/Binarizer;)V
    //   113: astore_1
    //   114: aload_0
    //   115: getfield 34	com/google/zxing/client/android/DecodeHandler:multiFormatReader	Lcom/google/zxing/MultiFormatReader;
    //   118: aload_1
    //   119: invokevirtual 139	com/google/zxing/MultiFormatReader:decodeWithState	(Lcom/google/zxing/BinaryBitmap;)Lcom/google/zxing/Result;
    //   122: astore_1
    //   123: aload_0
    //   124: getfield 34	com/google/zxing/client/android/DecodeHandler:multiFormatReader	Lcom/google/zxing/MultiFormatReader;
    //   127: invokevirtual 142	com/google/zxing/MultiFormatReader:reset	()V
    //   130: aload_0
    //   131: getfield 40	com/google/zxing/client/android/DecodeHandler:activity	Lcom/google/zxing/client/android/CaptureActivity;
    //   134: invokevirtual 146	com/google/zxing/client/android/CaptureActivity:getHandler	()Landroid/os/Handler;
    //   137: astore 4
    //   139: aload_1
    //   140: ifnull +109 -> 249
    //   143: invokestatic 113	java/lang/System:currentTimeMillis	()J
    //   146: lstore 10
    //   148: getstatic 22	com/google/zxing/client/android/DecodeHandler:TAG	Ljava/lang/String;
    //   151: new 148	java/lang/StringBuilder
    //   154: dup
    //   155: invokespecial 149	java/lang/StringBuilder:<init>	()V
    //   158: ldc 151
    //   160: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   163: lload 10
    //   165: lload 8
    //   167: lsub
    //   168: invokevirtual 158	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   171: ldc 160
    //   173: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   179: invokestatic 169	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   182: pop
    //   183: aload 4
    //   185: ifnull +39 -> 224
    //   188: aload 4
    //   190: getstatic 175	com/google/zxing/client/android/R$id:decode_succeeded	I
    //   193: aload_1
    //   194: invokestatic 181	android/os/Message:obtain	(Landroid/os/Handler;ILjava/lang/Object;)Landroid/os/Message;
    //   197: astore_1
    //   198: new 90	android/os/Bundle
    //   201: dup
    //   202: invokespecial 182	android/os/Bundle:<init>	()V
    //   205: astore 4
    //   207: aload 5
    //   209: aload 4
    //   211: invokestatic 184	com/google/zxing/client/android/DecodeHandler:bundleThumbnail	(Lcom/google/zxing/PlanarYUVLuminanceSource;Landroid/os/Bundle;)V
    //   214: aload_1
    //   215: aload 4
    //   217: invokevirtual 188	android/os/Message:setData	(Landroid/os/Bundle;)V
    //   220: aload_1
    //   221: invokevirtual 191	android/os/Message:sendToTarget	()V
    //   224: return
    //   225: astore_1
    //   226: aload_0
    //   227: getfield 34	com/google/zxing/client/android/DecodeHandler:multiFormatReader	Lcom/google/zxing/MultiFormatReader;
    //   230: invokevirtual 142	com/google/zxing/MultiFormatReader:reset	()V
    //   233: aload 4
    //   235: astore_1
    //   236: goto -106 -> 130
    //   239: astore_1
    //   240: aload_0
    //   241: getfield 34	com/google/zxing/client/android/DecodeHandler:multiFormatReader	Lcom/google/zxing/MultiFormatReader;
    //   244: invokevirtual 142	com/google/zxing/MultiFormatReader:reset	()V
    //   247: aload_1
    //   248: athrow
    //   249: aload 4
    //   251: ifnull -27 -> 224
    //   254: aload 4
    //   256: getstatic 194	com/google/zxing/client/android/R$id:decode_failed	I
    //   259: invokestatic 197	android/os/Message:obtain	(Landroid/os/Handler;I)Landroid/os/Message;
    //   262: invokevirtual 191	android/os/Message:sendToTarget	()V
    //   265: return
    //
    // Exception table:
    //   from	to	target	type
    //   114	123	225	com/google/zxing/ReaderException
    //   114	123	239	finally
  }

  public void handleMessage(Message paramMessage)
  {
    if (!this.running);
    do
    {
      return;
      if (paramMessage.what != R.id.decode)
        continue;
      decode((byte[])(byte[])paramMessage.obj, paramMessage.arg1, paramMessage.arg2);
      return;
    }
    while (paramMessage.what != R.id.quit);
    this.running = false;
    Looper.myLooper().quit();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.DecodeHandler
 * JD-Core Version:    0.6.0
 */
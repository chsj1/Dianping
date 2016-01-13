package com.tencent.mm.sdk.modelmsg;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import com.tencent.mm.sdk.b.b;
import java.io.ByteArrayOutputStream;

public final class WXMediaMessage
{
  public static final String ACTION_WXAPPMESSAGE = "com.tencent.mm.sdk.openapi.Intent.ACTION_WXAPPMESSAGE";
  private static final int DESCRIPTION_LENGTH_LIMIT = 1024;
  private static final int MEDIA_TAG_NAME_LENGTH_LIMIT = 64;
  private static final int MESSAGE_ACTION_LENGTH_LIMIT = 2048;
  private static final int MESSAGE_EXT_LENGTH_LIMIT = 2048;
  private static final String TAG = "MicroMsg.SDK.WXMediaMessage";
  public static final int THUMB_LENGTH_LIMIT = 32768;
  private static final int TITLE_LENGTH_LIMIT = 512;
  public String description;
  public IMediaObject mediaObject;
  public String mediaTagName;
  public String messageAction;
  public String messageExt;
  public int sdkVer;
  public byte[] thumbData;
  public String title;

  public WXMediaMessage()
  {
    this(null);
  }

  public WXMediaMessage(IMediaObject paramIMediaObject)
  {
    this.mediaObject = paramIMediaObject;
  }

  final boolean checkArgs()
  {
    if ((getType() == 8) && ((this.thumbData == null) || (this.thumbData.length == 0)))
    {
      b.a("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, thumbData should not be null when send emoji");
      return false;
    }
    if ((this.thumbData != null) && (this.thumbData.length > 32768))
    {
      b.a("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, thumbData is invalid");
      return false;
    }
    if ((this.title != null) && (this.title.length() > 512))
    {
      b.a("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, title is invalid");
      return false;
    }
    if ((this.description != null) && (this.description.length() > 1024))
    {
      b.a("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, description is invalid");
      return false;
    }
    if (this.mediaObject == null)
    {
      b.a("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, mediaObject is null");
      return false;
    }
    if ((this.mediaTagName != null) && (this.mediaTagName.length() > 64))
    {
      b.a("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, mediaTagName is too long");
      return false;
    }
    if ((this.messageAction != null) && (this.messageAction.length() > 2048))
    {
      b.a("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, messageAction is too long");
      return false;
    }
    if ((this.messageExt != null) && (this.messageExt.length() > 2048))
    {
      b.a("MicroMsg.SDK.WXMediaMessage", "checkArgs fail, messageExt is too long");
      return false;
    }
    return this.mediaObject.checkArgs();
  }

  public final int getType()
  {
    if (this.mediaObject == null)
      return 0;
    return this.mediaObject.type();
  }

  public final void setThumbImage(Bitmap paramBitmap)
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      paramBitmap.compress(Bitmap.CompressFormat.JPEG, 85, localByteArrayOutputStream);
      this.thumbData = localByteArrayOutputStream.toByteArray();
      localByteArrayOutputStream.close();
      return;
    }
    catch (Exception paramBitmap)
    {
      paramBitmap.printStackTrace();
      b.a("MicroMsg.SDK.WXMediaMessage", "put thumb failed");
    }
  }

  public static abstract interface IMediaObject
  {
    public static final int TYPE_APPDATA = 7;
    public static final int TYPE_CARD_SHARE = 16;
    public static final int TYPE_DEVICE_ACCESS = 12;
    public static final int TYPE_EMOJI = 8;
    public static final int TYPE_EMOTICON_GIFT = 11;
    public static final int TYPE_EMOTICON_SHARED = 15;
    public static final int TYPE_FILE = 6;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_LOCATION_SHARE = 17;
    public static final int TYPE_MALL_PRODUCT = 13;
    public static final int TYPE_MUSIC = 3;
    public static final int TYPE_OLD_TV = 14;
    public static final int TYPE_PRODUCT = 10;
    public static final int TYPE_RECORD = 19;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_TV = 20;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_URL = 5;
    public static final int TYPE_VIDEO = 4;

    public abstract boolean checkArgs();

    public abstract void serialize(Bundle paramBundle);

    public abstract int type();

    public abstract void unserialize(Bundle paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.mm.sdk.modelmsg.WXMediaMessage
 * JD-Core Version:    0.6.0
 */
package com.dianping.base.share.action.base;

import android.content.Context;
import android.graphics.Bitmap;
import com.dianping.archive.DPObject;
import com.dianping.base.share.model.IShareHandler;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.util.ImageUtils;
import com.dianping.util.BitmapUtils;

public abstract class BaseShare
  implements IShare
{
  public static final String TAG = "Share";

  public static Bitmap getThumbnail(Context paramContext, String paramString)
  {
    return BitmapUtils.cropCenterSquareBitmap(ImageUtils.getThumbnail(paramContext, paramString), 120, 120);
  }

  public boolean doShare(Context paramContext, ShareHolder paramShareHolder)
  {
    ShareHolder localShareHolder = paramShareHolder;
    if (paramShareHolder == null)
    {
      localShareHolder = paramShareHolder;
      if (ShareUtil.getShareHandler() != null)
      {
        localShareHolder = ShareUtil.getShareHandler().getShareHolder(this);
        ShareUtil.setShareHandler(null);
      }
    }
    return share(paramContext, localShareHolder);
  }

  protected String getDefaultLogoUrl()
  {
    return "http://m.api.dianping.com/sc/api_res/pic/logo.png";
  }

  public abstract String getElementId();

  public abstract int getIconResId();

  public abstract String getLabel();

  public boolean shareApp(Context paramContext, ShareHolder paramShareHolder)
  {
    return share(paramContext, paramShareHolder);
  }

  public boolean shareDeal(Context paramContext, DPObject paramDPObject)
  {
    return false;
  }

  public boolean shareHotelProd(Context paramContext, DPObject paramDPObject)
  {
    return false;
  }

  public boolean shareLuckyMoney(Context paramContext, DPObject paramDPObject)
  {
    return false;
  }

  public boolean sharePay(Context paramContext, ShareHolder paramShareHolder)
  {
    return share(paramContext, paramShareHolder);
  }

  public boolean shareShop(Context paramContext, DPObject paramDPObject)
  {
    return false;
  }

  public boolean shareWeb(Context paramContext, ShareHolder paramShareHolder)
  {
    return share(paramContext, paramShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.action.base.BaseShare
 * JD-Core Version:    0.6.0
 */
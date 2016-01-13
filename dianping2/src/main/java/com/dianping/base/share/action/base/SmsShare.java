package com.dianping.base.share.action.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.share.util.ShopUtil;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;

public class SmsShare extends BaseShare
{
  public String getElementId()
  {
    return "ShareTypeSMS";
  }

  public int getIconResId()
  {
    return R.drawable.share_to_icon_sms;
  }

  public String getLabel()
  {
    return "短信";
  }

  public boolean share(Context paramContext, ShareHolder paramShareHolder)
  {
    Intent localIntent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:"));
    if (TextUtils.isEmpty(paramShareHolder.content))
      localIntent.putExtra("sms_body", paramShareHolder.desc + " " + paramShareHolder.webUrl);
    try
    {
      while (true)
      {
        paramContext.startActivity(localIntent);
        return true;
        localIntent.putExtra("sms_body", paramShareHolder.content + " " + paramShareHolder.webUrl);
      }
    }
    catch (Exception paramShareHolder)
    {
      Log.e("Share", paramShareHolder.toString());
      ShareUtil.showToast(paramContext, "您尚未安装短信客户端");
    }
    return false;
  }

  public boolean shareShop(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(ShopUtil.getShopName(paramDPObject));
    localStringBuilder.append("\n");
    localStringBuilder.append(ShopUtil.getShopStar(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopPrice(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopCategory(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopRegion(paramDPObject));
    localStringBuilder.append(ShopUtil.getShopAddress(paramDPObject));
    localShareHolder.desc = localStringBuilder.toString();
    localShareHolder.webUrl = ("\n更多商户信息点评请查看:" + ShopUtil.getShopUrl(paramDPObject));
    return share(paramContext, localShareHolder);
  }

  public boolean shareWeb(Context paramContext, ShareHolder paramShareHolder)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.desc = ("【" + paramShareHolder.title + "】" + paramShareHolder.desc);
    localShareHolder.webUrl = paramShareHolder.webUrl;
    localShareHolder.content = paramShareHolder.content;
    return share(paramContext, localShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.action.base.SmsShare
 * JD-Core Version:    0.6.0
 */
package com.dianping.base.share.action.base;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShopUtil;
import com.dianping.base.thirdparty.sinaapi.SinaHelper;
import com.dianping.v1.R.drawable;

public class WeiboShare extends CopyShare
{
  public static final String LABEL = "新浪微博";
  public static final int WEIBOSHARE_REQ = 100;

  public String getElementId()
  {
    return "ShareTypeWeibo";
  }

  public int getIconResId()
  {
    return R.drawable.share_to_icon_weibo;
  }

  public String getLabel()
  {
    return "新浪微博";
  }

  public boolean share(Context paramContext, ShareHolder paramShareHolder)
  {
    Activity localActivity;
    String str2;
    if (SinaHelper.isWeiboAppInstalled(paramContext, true))
    {
      localActivity = (Activity)paramContext;
      str2 = paramShareHolder.title;
      if (!TextUtils.isEmpty(paramShareHolder.content))
        break label58;
    }
    label58: for (String str1 = paramShareHolder.desc; ; str1 = paramShareHolder.content)
    {
      SinaHelper.share(localActivity, str2, str1, getThumbnail(paramContext, paramShareHolder.imageUrl), paramShareHolder.webUrl);
      return false;
    }
  }

  public boolean shareShop(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("在@大众点评客户端 上发现这个店很不错哦!");
    localStringBuilder.append(ShopUtil.getShopName(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopStar(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopPrice(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopRegion(paramDPObject));
    localStringBuilder.append(" ");
    localStringBuilder.append(ShopUtil.getShopCategory(paramDPObject));
    localStringBuilder.append(",");
    if (!TextUtils.isEmpty(ShopUtil.getShopShareContent(paramDPObject)))
    {
      localStringBuilder.append(ShopUtil.getShopShareContent(paramDPObject));
      localStringBuilder.append(",");
    }
    localStringBuilder.append(ShopUtil.getShopAddress(paramDPObject));
    localShareHolder.desc = localStringBuilder.toString();
    localShareHolder.imageUrl = ShopUtil.getShopImageUrl(paramDPObject);
    localShareHolder.webUrl = ShopUtil.getShopUrl(paramDPObject);
    return share(paramContext, localShareHolder);
  }

  public boolean shareWeb(Context paramContext, ShareHolder paramShareHolder)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.desc = ("【" + paramShareHolder.title + "】" + paramShareHolder.desc);
    localShareHolder.imageUrl = paramShareHolder.imageUrl;
    localShareHolder.webUrl = paramShareHolder.webUrl;
    localShareHolder.content = paramShareHolder.content;
    localShareHolder.weiboContent = paramShareHolder.weiboContent;
    return share(paramContext, localShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.action.base.WeiboShare
 * JD-Core Version:    0.6.0
 */
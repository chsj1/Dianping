package com.dianping.base.share.action.base;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.share.util.ShopUtil;
import com.dianping.v1.R.drawable;

public class CopyShare extends BaseShare
{
  public String getElementId()
  {
    return "ShareTypeCopy";
  }

  public int getIconResId()
  {
    return R.drawable.share_to_icon_copy;
  }

  public String getLabel()
  {
    return "复制";
  }

  public boolean share(Context paramContext, ShareHolder paramShareHolder)
  {
    if (TextUtils.isEmpty(paramShareHolder.content));
    for (paramShareHolder = paramShareHolder.desc + " " + paramShareHolder.webUrl; ; paramShareHolder = paramShareHolder.content + " " + paramShareHolder.webUrl)
    {
      ((ClipboardManager)paramContext.getSystemService("clipboard")).setText(paramShareHolder);
      ShareUtil.showToast(paramContext, "已复制到剪贴板");
      return true;
    }
  }

  public boolean shareShop(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    StringBuilder localStringBuilder = new StringBuilder();
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
    return share(paramContext, localShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.action.base.CopyShare
 * JD-Core Version:    0.6.0
 */
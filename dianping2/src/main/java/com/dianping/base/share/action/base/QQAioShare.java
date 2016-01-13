package com.dianping.base.share.action.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.QQAio;
import com.tencent.share.ShareHelper;

public class QQAioShare extends QQShare
{
  public boolean share(Context paramContext, ShareHolder paramShareHolder)
  {
    Intent localIntent = QQAio.getInstance().getQQIntent();
    ShareHelper localShareHelper = ShareHelper.getInstance();
    localShareHelper.init(localIntent);
    localShareHelper.shareToQQ(paramContext, 200002L, paramShareHolder.webUrl, paramShareHolder.title, paramShareHolder.desc, paramShareHolder.imageUrl);
    localShareHelper.release();
    paramShareHolder = new Intent();
    paramShareHolder.putExtra("shareResult", "success");
    paramShareHolder.putExtra("shareChannel", getLabel());
    ((Activity)paramContext).setResult(-1, paramShareHolder);
    ((Activity)paramContext).finish();
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.action.base.QQAioShare
 * JD-Core Version:    0.6.0
 */
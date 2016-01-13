package com.dianping.base.share.action.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.tencent.open.utils.TemporaryStorage;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import java.util.ArrayList;

public class QzoneShare extends QQShare
{
  public static final String LABEL = "QQ空间";

  public static boolean shareToQzone(Activity paramActivity, Bundle paramBundle)
  {
    return shareToQzone(paramActivity, paramBundle, new IUiListener(paramActivity)
    {
      public void onCancel()
      {
        ShareUtil.showToast(this.val$activity, "取消分享");
        Intent localIntent = new Intent();
        localIntent.putExtra("shareResult", "cancel");
        localIntent.putExtra("shareChannel", "QQ空间");
        this.val$activity.setResult(-1, localIntent);
        this.val$activity.finish();
      }

      public void onComplete(Object paramObject)
      {
        ShareUtil.showToast(this.val$activity, "分享成功");
        paramObject = new Intent();
        paramObject.putExtra("shareResult", "success");
        paramObject.putExtra("shareChannel", "QQ空间");
        this.val$activity.setResult(-1, paramObject);
        this.val$activity.finish();
      }

      public void onError(UiError paramUiError)
      {
        ShareUtil.showToast(this.val$activity, "分享失败");
        paramUiError = new Intent();
        paramUiError.putExtra("shareResult", "fail");
        paramUiError.putExtra("shareChannel", "QQ空间");
        this.val$activity.setResult(-1, paramUiError);
        this.val$activity.finish();
      }
    });
  }

  public static boolean shareToQzone(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    try
    {
      TemporaryStorage.remove("shareToQzone");
      Tencent.createInstance("200002", paramActivity).shareToQzone(paramActivity, paramBundle, paramIUiListener);
      return true;
    }
    catch (Exception paramBundle)
    {
      Log.e("Share", paramBundle.toString());
      ShareUtil.showToast(paramActivity, "分享失败");
    }
    return false;
  }

  protected boolean doShare(Activity paramActivity, ShareHolder paramShareHolder)
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("req_type", 1);
    localBundle.putString("title", paramShareHolder.title);
    localBundle.putString("summary", paramShareHolder.desc);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(paramShareHolder.imageUrl);
    localBundle.putStringArrayList("imageUrl", localArrayList);
    localBundle.putString("targetUrl", paramShareHolder.webUrl);
    return shareToQzone(paramActivity, localBundle);
  }

  public String getElementId()
  {
    return "ShareTypeQZone";
  }

  public int getIconResId()
  {
    return R.drawable.share_to_icon_qzone;
  }

  public String getLabel()
  {
    return "QQ空间";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.action.base.QzoneShare
 * JD-Core Version:    0.6.0
 */
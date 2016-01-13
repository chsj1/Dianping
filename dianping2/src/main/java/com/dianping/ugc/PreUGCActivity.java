package com.dianping.ugc;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Bundle;
import com.dianping.accountservice.AccountService;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.ugc.draft.UGCDraftManager;
import com.dianping.ugc.model.UGCContentItem;
import com.dianping.util.Log;
import com.dianping.v1.R.array;
import com.dianping.v1.R.string;
import java.util.ArrayList;

public class PreUGCActivity extends NovaActivity
{
  private static final String TAG = "PreUGCActivity";
  private static final String TUAN_ADD_REVIEW = "tuanaddreview";
  private static final String UGC_CHECKIN = "addcheckin";
  private static final String UGC_REVIEW = "addreview";
  private static final String UGC_UPLOADCOMMUNITYPHOTO = "addcommunityphoto";
  private static final String UGC_UPLOADSHOPPHOTO = "addshopphoto";

  private void gotoAddCommunityPhoto()
  {
    int i = getIntParam("topicid", 0);
    int j = getIntParam("shopid", 0);
    int k = getIntParam("maxnum");
    String str = getStringParam("interesttag");
    Uri.Builder localBuilder1 = Uri.parse("dianping://photoselect").buildUpon();
    Uri.Builder localBuilder2 = Uri.parse("dianping://photoedit").buildUpon();
    Uri.Builder localBuilder3 = Uri.parse("dianping://ugcaddcommunityphoto").buildUpon();
    localBuilder3.appendQueryParameter("topicId", String.valueOf(i));
    localBuilder3.appendQueryParameter("shopId", String.valueOf(j));
    localBuilder2.appendQueryParameter("next", Uri.encode(localBuilder3.build().toString()));
    localBuilder2.appendQueryParameter("enablePOI", String.valueOf(true));
    localBuilder2.appendQueryParameter("enableTag", String.valueOf(true));
    localBuilder2.appendQueryParameter("enableDecal", String.valueOf(true));
    if (str != null)
      localBuilder2.appendQueryParameter("interesttag", str);
    localBuilder1.appendQueryParameter("maxNum", String.valueOf(k));
    localBuilder1.appendQueryParameter("next", Uri.encode(localBuilder2.build().toString()));
    startActivity(new Intent("android.intent.action.VIEW", localBuilder1.build()));
  }

  private void gotoAddShopPhoto(UGCContentItem paramUGCContentItem)
  {
    if (paramUGCContentItem != null)
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://ugcaddshopphoto"));
      ((Intent)localObject).putExtra("draft", paramUGCContentItem);
      startActivity((Intent)localObject);
      return;
    }
    int i = getIntParam("shopid", 0);
    if (i == 0)
    {
      Log.w("PreUGCActivity", "no shop id");
      finish();
      return;
    }
    paramUGCContentItem = getStringParam("shopname");
    Object localObject = getStringParam("category");
    int j = getIntParam("maxnum");
    Uri.Builder localBuilder1 = Uri.parse("dianping://photoselect").buildUpon();
    Uri.Builder localBuilder2 = Uri.parse("dianping://ugcaddshopphoto").buildUpon();
    localBuilder2.appendQueryParameter("shopid", String.valueOf(i));
    localBuilder2.appendQueryParameter("shopname", paramUGCContentItem);
    localBuilder2.appendQueryParameter("category", (String)localObject);
    localBuilder1.appendQueryParameter("maxNum", String.valueOf(j));
    localBuilder1.appendQueryParameter("next", Uri.encode(localBuilder2.build().toString()));
    startActivity(new Intent("android.intent.action.VIEW", localBuilder1.build()));
  }

  private void gotoReview(UGCContentItem paramUGCContentItem)
  {
    Object localObject = Uri.parse("dianping://ugcaddreview").buildUpon();
    int j = getIntParam("reviewid", 0);
    int k = getIntParam("shopid", 0);
    if (getIntParam("orderid", 0) == 0);
    for (int i = getIntParam("rateid", 0); ; i = getIntParam("orderid", 0))
    {
      String str1 = getStringParam("shopName");
      boolean bool = getBooleanParam("fromRecommend");
      String str2 = getStringParam("referToken");
      ((Uri.Builder)localObject).appendQueryParameter("reviewid", String.valueOf(j));
      ((Uri.Builder)localObject).appendQueryParameter("shopid", String.valueOf(k));
      ((Uri.Builder)localObject).appendQueryParameter("orderid", String.valueOf(i));
      if (str1 != null)
        ((Uri.Builder)localObject).appendQueryParameter("shopName", str1);
      ((Uri.Builder)localObject).appendQueryParameter("fromRecommend", String.valueOf(bool));
      if (str2 != null)
        ((Uri.Builder)localObject).appendQueryParameter("referToken", str2);
      localObject = new Intent("android.intent.action.VIEW", ((Uri.Builder)localObject).build());
      if (paramUGCContentItem != null)
        ((Intent)localObject).putExtra("draft", paramUGCContentItem);
      if (getIntent().getExtras() != null)
        ((Intent)localObject).putExtras(getIntent().getExtras());
      startActivity((Intent)localObject);
      return;
    }
  }

  private void processBusiness()
  {
    Object localObject = getIntent().getData().getHost();
    int i;
    if ("addcheckin".equals(localObject))
    {
      i = getIntParam("shopId", 0);
      if (i == 0)
      {
        Log.w("PreUGCActivity", "no shop id");
        finish();
      }
    }
    label208: label214: 
    do
    {
      return;
      localObject = getStringParam("shopName");
      int j = getIntParam("requestCode");
      boolean bool = getBooleanParam("isFromCheckinTopic");
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://ugcaddcheckin"));
      localIntent.putExtra("shopId", i);
      localIntent.putExtra("shopName", (String)localObject);
      localIntent.putExtra("requestCode", j);
      localIntent.putExtra("isFromCheckinTopic", bool);
      startActivity(localIntent);
      return;
      if (("addreview".equals(localObject)) || ("tuanaddreview".equals(localObject)))
      {
        j = getIntParam("shopid", 0);
        int k = getIntParam("reviewid", 0);
        if (getIntParam("orderid", 0) == 0)
        {
          i = getIntParam("rateid", 0);
          if (j != 0)
            break label208;
        }
        while (true)
        {
          if (i != 0)
            break label214;
          Log.w("PreUGCActivity", "no shop or order id");
          finish();
          return;
          i = getIntParam("orderid", 0);
          break;
          i = j;
        }
        if (k != 0);
        for (bool = false; bool; bool = getBooleanParam("checkdraft", false))
        {
          localObject = UGCDraftManager.getInstance().getDraftsById("review", String.valueOf(i), false);
          if ((localObject == null) || (((ArrayList)localObject).size() == 0))
            break;
          showLoadDraftDialog("addreview", (ArrayList)localObject);
          return;
        }
        gotoReview(null);
        return;
      }
      if (!"addshopphoto".equals(localObject))
        continue;
      i = getIntParam("shopid", 0);
      if (i == 0)
      {
        Log.w("PreUGCActivity", "no shop id");
        finish();
        return;
      }
      if (getBooleanParam("checkdraft", false))
      {
        localObject = UGCDraftManager.getInstance().getDraftsById("uploadphoto", String.valueOf(i), false);
        if ((localObject != null) && (((ArrayList)localObject).size() != 0))
        {
          showLoadDraftDialog("addshopphoto", (ArrayList)localObject);
          return;
        }
      }
      gotoAddShopPhoto(null);
      return;
    }
    while (!"addcommunityphoto".equals(localObject));
    gotoAddCommunityPhoto();
  }

  private void showLoadDraftDialog(String paramString, ArrayList<UGCContentItem> paramArrayList)
  {
    AlertDialog.Builder localBuilder;
    String[] arrayOfString;
    if (Build.VERSION.SDK_INT <= 10)
    {
      localBuilder = new AlertDialog.Builder(this);
      localBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          PreUGCActivity.this.finish();
        }
      });
      localBuilder.setTitle(R.string.ugc_dialog_hint);
      arrayOfString = new String[0];
      if (!"addshopphoto".equals(paramString))
        break label132;
      arrayOfString = getResources().getStringArray(R.array.photo_new_or_load);
    }
    while (true)
    {
      localBuilder.setItems(arrayOfString, new DialogInterface.OnClickListener(paramString, paramArrayList)
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          if (paramInt == 0)
            if ("addshopphoto".equals(this.val$type))
              PreUGCActivity.this.gotoAddShopPhoto((UGCContentItem)this.val$drafts.get(0));
          do
          {
            do
            {
              do
                return;
              while (!"addreview".equals(this.val$type));
              PreUGCActivity.this.gotoReview((UGCContentItem)this.val$drafts.get(0));
              return;
            }
            while (paramInt != 1);
            if (!"addshopphoto".equals(this.val$type))
              continue;
            PreUGCActivity.this.gotoAddShopPhoto(null);
            return;
          }
          while (!"addreview".equals(this.val$type));
          PreUGCActivity.this.gotoReview(null);
        }
      });
      paramString = localBuilder.create();
      paramString.setCancelable(false);
      paramString.setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramDialogInterface)
        {
          PreUGCActivity.this.finish();
        }
      });
      paramString.show();
      return;
      localBuilder = new AlertDialog.Builder(this, 3);
      break;
      label132: if (!"addreview".equals(paramString))
        continue;
      arrayOfString = getResources().getStringArray(R.array.review_new_or_load);
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  protected boolean needTitleBarShadow()
  {
    return false;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (accountService().token() != null)
      processBusiness();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      processBusiness();
      return paramBoolean;
    }
    finish();
    return paramBoolean;
  }

  public void startActivity(Intent paramIntent)
  {
    super.startActivity(paramIntent);
    finish();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.PreUGCActivity
 * JD-Core Version:    0.6.0
 */
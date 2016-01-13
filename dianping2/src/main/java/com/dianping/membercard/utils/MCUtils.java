package com.dianping.membercard.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.thirdparty.wxapi.WXHelper;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.model.Location;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MCUtils
{
  public static int BRAND_CARD_TYPE;
  public static int FEED_PRODUCT_TYPE;
  public static int GENERAL_CARD_LEVEL = 1;
  public static int GENERAL_CARD_TYPE;
  public static int MALL_CARD_TYPE;
  public static int MIX_CARD_LEVEL;
  public static int VIP_CARD_LEVEL = 2;

  static
  {
    MIX_CARD_LEVEL = 3;
    FEED_PRODUCT_TYPE = 9547;
    GENERAL_CARD_TYPE = 1;
    MALL_CARD_TYPE = 2;
    BRAND_CARD_TYPE = 3;
  }

  public static String ToDBC(String paramString)
  {
    paramString = paramString.toCharArray();
    int i = 0;
    if (i < paramString.length)
    {
      if (paramString[i] == '　')
        paramString[i] = 32;
      while (true)
      {
        i += 1;
        break;
        if ((paramString[i] <= 65280) || (paramString[i] >= 65375))
          continue;
        paramString[i] = (char)(paramString[i] - 65248);
      }
    }
    return new String(paramString);
  }

  public static String filterTextAfter(char paramChar, String paramString)
  {
    String str = paramString;
    if (paramString != null)
    {
      str = paramString;
      if (paramString.indexOf(paramChar) >= 0)
        str = paramString.substring(0, paramString.indexOf(paramChar));
    }
    return str;
  }

  public static String filterTextLine(String paramString)
  {
    Object localObject = paramString;
    if (paramString != null)
    {
      localObject = paramString;
      if (paramString.indexOf('|') >= 0)
      {
        localObject = new StringBuffer();
        int i = 0;
        while (i < paramString.length())
        {
          if (paramString.charAt(i) != '|')
            ((StringBuffer)localObject).append(paramString.charAt(i));
          i += 1;
        }
        localObject = ((StringBuffer)localObject).toString();
      }
    }
    return (String)localObject;
  }

  public static void gotoMembercardFeedBack(Context paramContext, DPObject paramDPObject)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://feedback?flag=5&shopid=" + paramDPObject.getInt("ShopID") + "&membercardid=" + String.valueOf(paramDPObject.getInt("MemberCardID"))));
    StringBuilder localStringBuilder = new StringBuilder().append(paramDPObject.getString("Title"));
    if (TextUtils.isEmpty(paramDPObject.getString("SubTitle")));
    for (paramDPObject = ""; ; paramDPObject = "(" + paramDPObject.getString("SubTitle") + ")")
    {
      localIntent.putExtra("name", paramDPObject);
      localIntent.addFlags(268435456);
      paramContext.startActivity(localIntent);
      return;
    }
  }

  public static Bundle handleIntent(Activity paramActivity)
  {
    Bundle localBundle = new Bundle();
    if (paramActivity == null);
    while (true)
    {
      return localBundle;
      Object localObject = paramActivity.getIntent().getExtras();
      if (localObject != null)
        localBundle.putAll((Bundle)localObject);
      paramActivity = paramActivity.getIntent().getData();
      if (paramActivity == null)
        continue;
      try
      {
        localObject = paramActivity.getEncodedQuery();
        if (TextUtils.isEmpty((CharSequence)localObject))
          continue;
        i = ((String)localObject).indexOf("url=");
        j = ((String)localObject).indexOf("?");
        if ((i == 0) && (j > i))
        {
          localBundle.putString("url", URLDecoder.decode(((String)localObject).substring(4), "utf-8"));
          return localBundle;
        }
      }
      catch (Exception paramActivity)
      {
        Log.e(MCUtils.class.getSimpleName(), paramActivity.toString());
        return localBundle;
      }
      paramActivity = (Activity)localObject;
      if (i > 0)
      {
        paramActivity = (Activity)localObject;
        if (j > i)
        {
          localBundle.putString("url", URLDecoder.decode(((String)localObject).substring(i + 4), "utf-8"));
          paramActivity = ((String)localObject).substring(0, i);
        }
      }
      paramActivity = paramActivity.split("&");
      int j = paramActivity.length;
      int i = 0;
      while (i < j)
      {
        localObject = paramActivity[i].split("=");
        if (localObject.length > 1)
          localBundle.putString(localObject[0], localObject[1]);
        i += 1;
      }
    }
  }

  public static boolean isCardPaused(DPObject paramDPObject)
  {
    int k;
    if (paramDPObject == null)
      k = 1;
    int m;
    do
    {
      do
      {
        return k;
        paramDPObject = paramDPObject.getArray("ProductList");
        m = 0;
        k = m;
      }
      while (paramDPObject == null);
      k = m;
    }
    while (paramDPObject.length < 1);
    int j = paramDPObject.length;
    int i = 0;
    while (true)
    {
      k = m;
      if (i >= j)
        break;
      if (paramDPObject[i].getInt("ProductType") == 5)
        return true;
      i += 1;
    }
  }

  public static boolean isMemberCardRelativeBroadcast(Intent paramIntent)
  {
    paramIntent = paramIntent.getAction();
    return (paramIntent.equals("com.dianping.action.JOIN_MEMBER_CARD")) || (paramIntent.equals("com.dianping.action.QUIT_MEMBER_CARD")) || (paramIntent.equals("com.dianping.action.UPDATE_LIST_DATA")) || (paramIntent.equals("Card:JoinSuccess"));
  }

  public static void membercardShare2ThirdParty(Context paramContext, DPObject paramDPObject, int paramInt)
  {
    Object localObject2 = paramDPObject.getString("Title");
    Object localObject1 = paramDPObject.getString("SubTitle");
    localObject2 = new StringBuilder().append((String)localObject2);
    if (TextUtils.isEmpty((CharSequence)localObject1))
      localObject1 = "";
    while (true)
    {
      String str = (String)localObject1;
      localObject2 = paramDPObject.getString("WeiboContent");
      localObject1 = localObject2;
      if (TextUtils.isEmpty((CharSequence)localObject2))
        localObject1 = "亲们，" + str + "会员卡真心不错，现在加入即享" + paramDPObject.getString("MemberCardDesc") + ",打开大众点评客户端［查找］－［扫一扫］，就能免费申请，一起洋气起来~";
      try
      {
        localObject2 = URLEncoder.encode((String)localObject1, "utf-8");
        localObject1 = localObject2;
        localObject2 = new Intent("android.intent.action.VIEW");
        ((Intent)localObject2).setData(Uri.parse("dianping://thirdshare?feed=" + paramInt + "&id=" + paramDPObject.getInt("ShopID") + "&membercardid=" + String.valueOf(paramDPObject.getInt("MemberCardID")) + "&type=100&content=" + (String)localObject1));
        ((Intent)localObject2).addFlags(268435456);
        paramContext.startActivity((Intent)localObject2);
        return;
        localObject1 = "(" + (String)localObject1 + ")";
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        while (true)
          localUnsupportedEncodingException.printStackTrace();
      }
    }
  }

  public static boolean membercardShare2WeiXin(Context paramContext, boolean paramBoolean, String paramString1, Location paramLocation, int paramInt, String paramString2, DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return false;
    String str3 = paramDPObject.getString("Title");
    String str2 = paramDPObject.getString("WeixinContent");
    String str1 = str2;
    if (TextUtils.isEmpty(str2))
      str1 = paramDPObject.getString("SubTitle") + "会员卡，现在加入即享" + paramDPObject.getString("MemberCardDesc");
    Drawable localDrawable = paramContext.getResources().getDrawable(R.drawable.card_share_weixin);
    int i = paramDPObject.getInt("MemberCardID");
    paramDPObject = NovaConfigUtils.processParam(paramContext, paramString1);
    int j = paramDPObject.indexOf('?');
    paramString1 = paramDPObject;
    String str4;
    if (j > 0)
    {
      str4 = paramDPObject.substring(0, j + 1);
      str2 = paramDPObject.substring(j + 1);
      paramDPObject = str2;
      if (str2.contains("token=*"))
      {
        paramDPObject = new StringBuilder().append("token=");
        paramString1 = paramString2;
        if (paramString2 == null)
          paramString1 = "";
        paramDPObject = str2.replace("token=*", paramString1);
      }
      paramString1 = paramDPObject;
      if (paramDPObject.contains("latitude=*"))
      {
        if (paramLocation != null)
          break label375;
        paramString1 = paramDPObject.replace("latitude=*", "latitude=");
      }
      paramString2 = paramString1;
      if (paramString1.contains("longitude=*"))
        if (paramLocation != null)
          break label416;
    }
    label416: for (paramString2 = paramString1.replace("longitude=*", "longitude="); ; paramString2 = paramString1.replace("longitude=*", "longitude=" + Location.FMT.format(paramLocation.longitude())))
    {
      paramString1 = paramString2;
      if (paramString2.contains("cityid=*"))
        paramString1 = paramString2.replace("cityid=*", "cityid=" + paramInt);
      paramString1 = str4 + paramString1;
      paramString1 = paramString1 + "&membercardid=" + String.valueOf(i);
      return WXHelper.shareWithFriend(paramContext, str3, str1, ((BitmapDrawable)localDrawable).getBitmap(), paramString1, paramBoolean);
      label375: paramString1 = paramDPObject.replace("latitude=*", "latitude=" + Location.FMT.format(paramLocation.latitude()));
      break;
    }
  }

  public static void sendJoinScoreCardSuccessBroadcast(Activity paramActivity, String paramString)
  {
    Intent localIntent = new Intent("com.dianping.action.JOIN_MEMBER_CARD");
    Bundle localBundle = new Bundle();
    localBundle.putString("membercardid", paramString);
    localIntent.putExtras(localBundle);
    paramActivity.sendBroadcast(localIntent);
  }

  public static void sendUpdateMemberCardListBroadcast(Activity paramActivity, String paramString)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("membercardid", paramString);
    paramString = new Intent("com.dianping.action.UPDATE_LIST_DATA");
    paramString.putExtras(localBundle);
    paramActivity.sendBroadcast(paramString);
  }

  private static void showListDialog(Context paramContext, ArrayList<String> paramArrayList, String paramString, DialogInterface.OnClickListener paramOnClickListener)
  {
    paramArrayList = new ArrayAdapter(paramContext, 17367043, paramArrayList);
    paramContext = new AlertDialog.Builder(paramContext);
    paramContext.setAdapter(paramArrayList, paramOnClickListener);
    paramContext = paramContext.create();
    if (paramString != null)
      paramContext.setTitle(paramString);
    paramContext.show();
    paramContext.setCanceledOnTouchOutside(true);
  }

  public static void showMembercardMoreFeature(Context paramContext, DPObject paramDPObject, DialogInterface.OnClickListener paramOnClickListener1, DialogInterface.OnClickListener paramOnClickListener2, DialogInterface.OnClickListener paramOnClickListener3)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("分享");
    localArrayList.add("投诉");
    localArrayList.add("删除");
    localArrayList.add("取消");
    showListDialog(paramContext, localArrayList, "更多", new DialogInterface.OnClickListener(paramDPObject, paramContext, paramOnClickListener1, paramOnClickListener2, paramOnClickListener3)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        Object localObject = this.val$cardObject.getString("Title");
        paramDialogInterface = this.val$cardObject.getString("SubTitle");
        localObject = new StringBuilder().append((String)localObject);
        if (TextUtils.isEmpty(paramDialogInterface));
        for (paramDialogInterface = ""; ; paramDialogInterface = "(" + paramDialogInterface + ")")
        {
          paramDialogInterface = paramDialogInterface;
          switch (paramInt)
          {
          default:
            return;
          case 0:
          case 1:
          case 2:
          }
        }
        localObject = new ArrayList();
        ((ArrayList)localObject).add("发给微信好友");
        ((ArrayList)localObject).add("分享到社交网站");
        ((ArrayList)localObject).add("取消");
        MCUtils.access$000(this.val$context, (ArrayList)localObject, "分享\"" + paramDialogInterface + "\"到", this.val$shareListener);
        return;
        localObject = new ArrayList();
        ((ArrayList)localObject).add("商家不让用");
        ((ArrayList)localObject).add("优惠与描述不符");
        ((ArrayList)localObject).add("其他原因");
        ((ArrayList)localObject).add("取消");
        MCUtils.access$000(this.val$context, (ArrayList)localObject, "投诉\"" + paramDialogInterface + "\"的理由", this.val$commentListener);
        return;
        new AlertDialog.Builder(this.val$context).setTitle("删除\"" + paramDialogInterface + "\"?").setMessage("您将无法享受该商户的会员特权哦！").setPositiveButton("删除", this.val$deleteListener).setNegativeButton("取消", null).show();
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.MCUtils
 * JD-Core Version:    0.6.0
 */
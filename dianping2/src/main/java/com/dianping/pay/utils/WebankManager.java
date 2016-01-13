package com.dianping.pay.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import com.dianping.archive.DPObject;
import com.dianping.util.encrypt.Md5;
import java.security.PublicKey;

public class WebankManager
{
  public static final int CREDIT_CARD = 2;
  public static final int DEPOSIT_CARD = 1;
  public static final int PAY_CODE_BIND_CARD = 8000;
  public static final int PAY_CODE_SET_PWD = 8030;
  public static final int PAY_CODE_VERIFY_PWD = 8010;
  public static final int PAY_CODE_VERIFY_PWD_MSG = 8020;
  private static final String RSA_PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSL+5qyAfU6s4nm0pc36nLfV2tkE0tdMhCdcNzc66RoG1uf4pACfVFUUB+Gnv7fP5O9UmNUdayfv4JrB8m3Nlz8Libr5938h/dSRdAyUawEEVRZVRaHN3wV0G8iBTZz0J2XCkdzNVTmNd1wTHiD0R20KVsIBEOkkzkaFwkYtXCyQIDAQAB";
  public static final boolean isEncrypt = true;
  private static WebankManager payManager = null;
  private DPObject bankElement;

  public static String encryptByRsa(String paramString)
  {
    if (paramString == null)
      return null;
    Object localObject = null;
    try
    {
      PublicKey localPublicKey = RSAUtil.loadPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSL+5qyAfU6s4nm0pc36nLfV2tkE0tdMhCdcNzc66RoG1uf4pACfVFUUB+Gnv7fP5O9UmNUdayfv4JrB8m3Nlz8Libr5938h/dSRdAyUawEEVRZVRaHN3wV0G8iBTZz0J2XCkdzNVTmNd1wTHiD0R20KVsIBEOkkzkaFwkYtXCyQIDAQAB");
      localObject = localPublicKey;
      label16: return new String(Base64.encode(RSAUtil.encryptData(paramString.getBytes(), localObject), 0));
    }
    catch (Exception localException)
    {
      break label16;
    }
  }

  public static String encryptSetPwd(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString2 == null))
      return null;
    paramString2 = Md5.md5(paramString2);
    String str = Md5.md5(paramString1 + paramString2);
    paramString2 = null;
    try
    {
      PublicKey localPublicKey = RSAUtil.loadPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSL+5qyAfU6s4nm0pc36nLfV2tkE0tdMhCdcNzc66RoG1uf4pACfVFUUB+Gnv7fP5O9UmNUdayfv4JrB8m3Nlz8Libr5938h/dSRdAyUawEEVRZVRaHN3wV0G8iBTZz0J2XCkdzNVTmNd1wTHiD0R20KVsIBEOkkzkaFwkYtXCyQIDAQAB");
      paramString2 = localPublicKey;
      label47: return new String(Base64.encode(RSAUtil.encryptData((paramString1 + "," + str).getBytes(), paramString2), 0));
    }
    catch (Exception localException)
    {
      break label47;
    }
  }

  public static String encryptVerifyPwd(String paramString1, String paramString2, String paramString3)
  {
    if ((paramString1 == null) || (paramString2 == null) || (paramString3 == null))
      return null;
    paramString2 = Md5.md5(paramString2);
    String str = Md5.md5(paramString1 + paramString2);
    paramString3 = Md5.md5(str + paramString3).substring(0, 16);
    try
    {
      paramString1 = AESUtil.encrytBySpecificKey(paramString1 + "," + paramString2, paramString3);
      return paramString1;
    }
    catch (Exception paramString1)
    {
    }
    return null;
  }

  public static void goWebankEntry(Activity paramActivity, boolean paramBoolean, int paramInt, String paramString1, String paramString2)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paywebankentry"));
    localIntent.putExtra("resultcode", paramInt);
    localIntent.putExtra("success", paramBoolean);
    localIntent.putExtra("resultmsg", paramString1);
    localIntent.putExtra("resulttitle", paramString2);
    paramActivity.startActivity(localIntent);
    paramActivity.finish();
  }

  public static WebankManager instance()
  {
    if (payManager == null)
      payManager = new WebankManager();
    return payManager;
  }

  public static void showAlertDialog(Context paramContext, String paramString1, String paramString2)
  {
    showAlertDialog(paramContext, paramString1, paramString2, null);
  }

  public static void showAlertDialog(Context paramContext, String paramString1, String paramString2, DialogInterface.OnClickListener paramOnClickListener)
  {
    paramContext = new AlertDialog.Builder(paramContext, 3);
    paramContext.setTitle(paramString1).setMessage(paramString2).setNegativeButton("我知道了", paramOnClickListener);
    paramContext.create().show();
  }

  public void setBindBankElement(DPObject paramDPObject)
  {
    this.bankElement = paramDPObject;
  }

  public void startWebankPay(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    int i = 0;
    if (!TextUtils.isEmpty(paramString1))
      i = Integer.parseInt(paramString1);
    if (8000 == i)
    {
      paramString1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paywebankbind"));
      if (TextUtils.isEmpty(paramString3))
      {
        paramString1.putExtra("source", 1);
        paramString1.putExtra("paysessionid", paramString2);
        paramString1.putExtra("bankelement", this.bankElement);
        paramContext.startActivity(paramString1);
      }
    }
    do
    {
      return;
      paramString1.putExtra("source", 2);
      paramString1.putExtra("realname", paramString3);
      break;
      if (8010 == i)
      {
        paramString1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paywebankverifypasswordpop"));
        paramString1.putExtra("source", 1);
        paramString1.putExtra("paysessionid", paramString2);
        paramContext.startActivity(paramString1);
        return;
      }
      if (8020 != i)
        continue;
      paramString1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paywebankverifypasswordpop"));
      paramString1.putExtra("source", 2);
      paramString1.putExtra("paysessionid", paramString2);
      paramString1.putExtra("mobileno", paramString4);
      paramContext.startActivity(paramString1);
      return;
    }
    while (8030 != i);
    paramString1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paywebankresetverify"));
    paramString1.putExtra("paysessionid", paramString2);
    paramString1.putExtra("bankelement", this.bankElement);
    paramString1.putExtra("source", 1);
    paramContext.startActivity(paramString1);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.utils.WebankManager
 * JD-Core Version:    0.6.0
 */
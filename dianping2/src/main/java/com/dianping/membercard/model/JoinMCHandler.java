package com.dianping.membercard.model;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.membercard.ChainCardListActivity;
import com.dianping.membercard.MemberInfoActivity;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class JoinMCHandler
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private int mCardLevel;
  private NovaActivity mContext;
  private MApiRequest mJoinRequest;
  private OnJoinCardRequestHandlerListener mListener;
  private String mMemberCardID;
  private int mNeedCard;
  private int mProductID;
  private int mSource;
  private int opType;

  public JoinMCHandler(Context paramContext)
  {
    this.mContext = ((NovaActivity)paramContext);
  }

  private void JoinMCTask(ArrayList<String> paramArrayList)
  {
    this.mJoinRequest = BasicMApiRequest.mapiPost("http://mc.api.dianping.com/joinmc.v2.mc", (String[])paramArrayList.toArray(new String[0]));
    this.mContext.mapiService().exec(this.mJoinRequest, this);
  }

  public void addCard(String paramString1, int paramInt1, int paramInt2, String paramString2)
  {
    this.mMemberCardID = paramString1;
    this.mSource = paramInt1;
    this.mCardLevel = paramInt2;
    switch (paramInt2)
    {
    default:
      gotoMembersOnly(paramString1, paramInt1, paramInt2);
      return;
    case 1:
      gotoMembersOnly(paramString1, paramInt1, paramInt2);
      return;
    case 2:
    }
    gotoMembersOnly(paramString1, paramInt1, paramInt2);
  }

  public void addCardQuick(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    this.mMemberCardID = paramString;
    this.mSource = paramInt1;
    this.mProductID = paramInt2;
    this.mCardLevel = paramInt3;
    switch (paramInt3)
    {
    default:
      joinTask(paramString, paramInt1);
    case 1:
    case 2:
    }
    do
    {
      do
      {
        return;
        joinTask(paramString, paramInt1);
      }
      while (!(this.mContext instanceof ChainCardListActivity));
      this.mContext.statisticsEvent("chaincard5", "chaincard5_join_submit", "普通", 0);
      return;
      gotoCreateOrder(paramString, paramInt1, paramInt2);
    }
    while (!(this.mContext instanceof ChainCardListActivity));
    this.mContext.statisticsEvent("chaincard5", "chaincard5_join_submit", "高级", 0);
  }

  public void gotoCreateOrder(String paramString, int paramInt1, int paramInt2)
  {
    if (this.mContext.getAccount() == null)
    {
      this.mMemberCardID = paramString;
      this.mSource = paramInt1;
      this.mProductID = paramInt2;
      this.mContext.gotoLogin();
      this.opType = 1;
      return;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse("dianping://cardcreateorder?cardid=" + paramString + "&source=" + paramInt1 + "&productid=" + paramInt2));
    this.mContext.startActivityForResult(localIntent, 10);
  }

  public void gotoMemberInfo()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://memberinfo?membercardid=" + this.mMemberCardID + "&from=0" + "&source=" + this.mSource));
    localIntent.putExtra("source", this.mSource);
    this.mContext.startActivityForResult(localIntent, 10);
  }

  public void gotoMembersOnly(String paramString, int paramInt1, int paramInt2)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse("dianping://membersonly?membercardid=" + paramString + "&source=" + paramInt1 + "&cardlevel=" + paramInt2));
    this.mContext.startActivityForResult(localIntent, 10);
  }

  public void joinTask(String paramString, int paramInt)
  {
    joinTask(paramString, paramInt, 0);
  }

  public void joinTask(String paramString, int paramInt1, int paramInt2)
  {
    this.mNeedCard = paramInt2;
    this.mContext.showProgressDialog("正在提交请求，请稍候...");
    this.mMemberCardID = paramString;
    this.mSource = paramInt1;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cardids");
    localArrayList.add(paramString);
    if (this.mContext.getAccount() != null)
    {
      localArrayList.add("token");
      localArrayList.add(this.mContext.accountService().token());
    }
    localArrayList.add("uuid");
    localArrayList.add(Environment.uuid());
    paramString = this.mContext.location();
    if (paramString != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      localArrayList.add("lat");
      localArrayList.add(localDecimalFormat.format(paramString.latitude()));
      localArrayList.add("lng");
      localArrayList.add(localDecimalFormat.format(paramString.longitude()));
    }
    localArrayList.add("needcard");
    localArrayList.add(String.valueOf(paramInt2));
    localArrayList.add("source");
    localArrayList.add(String.valueOf(paramInt1));
    if (this.mContext != null)
    {
      paramString = this.mContext.getResources().getDisplayMetrics();
      localArrayList.add("pixel");
      localArrayList.add(String.valueOf(paramString.widthPixels));
    }
    JoinMCTask(localArrayList);
  }

  public void joinTask(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4)
  {
    this.mContext.showProgressDialog("正在提交请求，请稍候...");
    this.mMemberCardID = paramString1;
    this.mSource = paramInt;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cardids");
    localArrayList.add(paramString1);
    if (this.mContext.getAccount() != null)
    {
      localArrayList.add("token");
      localArrayList.add(this.mContext.accountService().token());
    }
    localArrayList.add("uuid");
    localArrayList.add(Environment.uuid());
    localArrayList.add("username");
    localArrayList.add(paramString2);
    localArrayList.add("gender");
    localArrayList.add(paramString3);
    localArrayList.add("birthday");
    localArrayList.add(paramString4);
    paramString1 = this.mContext.location();
    if (paramString1 != null)
    {
      paramString2 = Location.FMT;
      localArrayList.add("lat");
      localArrayList.add(paramString2.format(paramString1.latitude()));
      localArrayList.add("lng");
      localArrayList.add(paramString2.format(paramString1.longitude()));
    }
    localArrayList.add("source");
    localArrayList.add(String.valueOf(paramInt));
    JoinMCTask(localArrayList);
  }

  public void onLoginAddCard()
  {
    switch (this.opType)
    {
    default:
      return;
    case 0:
      joinTask(this.mMemberCardID, this.mSource, this.mNeedCard);
      return;
    case 1:
    }
    gotoCreateOrder(this.mMemberCardID, this.mSource, this.mProductID);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mJoinRequest)
    {
      this.mContext.dismissDialog();
      Toast.makeText(this.mContext, paramMApiResponse.message().toString(), 0).show();
      this.mJoinRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mContext.dismissDialog();
    int i;
    if (paramMApiRequest == this.mJoinRequest)
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        paramMApiResponse = paramMApiRequest.getObject("SimpleMsg");
        i = paramMApiResponse.getInt("Flag");
        if ((i != 200) && (i != 201))
          break label206;
        Toast.makeText(this.mContext, paramMApiResponse.getString("Content"), 0).show();
        paramMApiRequest = paramMApiRequest.getObject("Card");
        paramMApiResponse = new Intent("com.dianping.action.JOIN_MEMBER_CARD");
        Bundle localBundle = new Bundle();
        localBundle.putString("membercardid", this.mMemberCardID);
        paramMApiResponse.putExtras(localBundle);
        this.mContext.sendBroadcast(paramMApiResponse);
        if (this.mNeedCard != 0)
          break label174;
        if (this.mListener != null)
          this.mListener.onJoinCardFinish(paramMApiRequest);
      }
    while (true)
    {
      this.mJoinRequest = null;
      return;
      label174: if ((paramMApiRequest == null) || (this.mNeedCard != 1) || (this.mListener == null))
        continue;
      this.mListener.onJoinCardFinish(paramMApiRequest);
      continue;
      label206: if (i == 202)
      {
        if ((this.mContext instanceof MemberInfoActivity))
        {
          Toast.makeText(this.mContext, paramMApiResponse.getString("Content"), 0).show();
          continue;
        }
        gotoMemberInfo();
        continue;
      }
      if (i == 203)
      {
        if ((this.mContext instanceof MemberInfoActivity))
        {
          Toast.makeText(this.mContext, paramMApiResponse.getString("Content"), 0).show();
          continue;
        }
        new AlertDialog.Builder(this.mContext).setMessage(paramMApiResponse.getString("Content")).setPositiveButton("登录", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            if (JoinMCHandler.this.mContext.getAccount() == null)
            {
              if (JoinMCHandler.this.mSource != 12)
                break label62;
              JoinMCHandler.this.mContext.statisticsEvent("cardinfo5", "cardinfo5_login_shopinfo", "", 0);
            }
            while (true)
            {
              JoinMCHandler.this.mContext.gotoLogin();
              JoinMCHandler.access$202(JoinMCHandler.this, 0);
              return;
              label62: if (JoinMCHandler.this.mSource == 14)
              {
                JoinMCHandler.this.mContext.statisticsEvent("cardinfo5", "cardinfo5_login_availablecard", "", 0);
                continue;
              }
              if ((JoinMCHandler.this.mSource < 30) || (JoinMCHandler.this.mSource > 39))
                continue;
              JoinMCHandler.this.mContext.statisticsEvent("cardinfo5", "cardinfo5_login_landingpage", "", 0);
            }
          }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            if (JoinMCHandler.this.mContext.getAccount() == null)
            {
              if (JoinMCHandler.this.mSource != 12)
                break label43;
              JoinMCHandler.this.mContext.statisticsEvent("cardinfo5", "cardinfo5_cancel_shopinfo", "", 0);
            }
            label43: 
            do
            {
              return;
              if (JoinMCHandler.this.mSource != 14)
                continue;
              JoinMCHandler.this.mContext.statisticsEvent("cardinfo5", "cardinfo5_cancel_availablecard", "", 0);
              return;
            }
            while ((JoinMCHandler.this.mSource < 30) || (JoinMCHandler.this.mSource > 39));
            JoinMCHandler.this.mContext.statisticsEvent("cardinfo5", "cardinfo5_cancel_landingpage", "", 0);
          }
        }).show();
        continue;
      }
      Toast.makeText(this.mContext, paramMApiResponse.getString("Content"), 0).show();
    }
  }

  public void removeListener()
  {
    this.mListener = null;
  }

  public void setOnJoinCardRequestHandlerListener(OnJoinCardRequestHandlerListener paramOnJoinCardRequestHandlerListener)
  {
    this.mListener = paramOnJoinCardRequestHandlerListener;
  }

  public static abstract interface OnJoinCardRequestHandlerListener
  {
    public abstract void onJoinCardFinish(DPObject paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.model.JoinMCHandler
 * JD-Core Version:    0.6.0
 */
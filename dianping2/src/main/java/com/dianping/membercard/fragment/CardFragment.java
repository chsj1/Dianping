package com.dianping.membercard.fragment;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.DPFragment;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.id;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CardFragment extends DPFragment
  implements View.OnClickListener, LoginResultListener
{
  public static int FROM_MEMBERCARDINFOACTIVITY = 0;
  public static int FROM_MYCARDFRAGMENT = 0;
  private static final String MC_MEMBERCARDINFOACTIVITY_CHECK_LOGIN = "com.dianping.action.MEBMERCARDINFOACTIVITY_CHECK_LOGIN";
  private static final String MC_MYCARDFRAGMENT_CHECK_LOGIN = "com.dianping.action.MYCARDFRAGMENT_CHECK_LOGIN";
  protected DPObject cardObject;
  protected String cardPointURL;
  private String cardProductURL;
  protected int cardlevel = 1;
  private boolean isCardScore = false;
  private boolean isCardScoreBinded = false;
  protected boolean needLogin = false;
  private String scoreUrl = "";
  protected int source = 0;

  public void closeQRView()
  {
  }

  public void gotoBranchCardList()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse("dianping://branchcardlist"));
    localIntent.putExtra("cardObject", this.cardObject);
    startActivityForResult(localIntent, 20);
    statisticsEvent("mycard5", "mycard5_chain_other", null, 0);
  }

  public void gotoMembersOnly(String paramString, int paramInt1, int paramInt2)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse("dianping://membersonly?membercardid=" + paramString + "&source=" + paramInt1 + "&cardlevel=" + paramInt2));
    startActivityForResult(localIntent, 10);
  }

  public void gotoNativeProductDetail(DPObject paramDPObject1, DPObject paramDPObject2)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://productdetail"));
    localIntent.putExtra("card", paramDPObject1);
    localIntent.putExtra("product", paramDPObject2);
    startActivity(localIntent);
  }

  public void gotoPointDetail(String paramString)
  {
    try
    {
      paramString = URLEncoder.encode(paramString, "utf-8");
      startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString)), 10);
      statisticsEvent("mycard5", "mycard5_detail_points", null, 0);
      return;
    }
    catch (UnsupportedEncodingException paramString)
    {
      paramString.printStackTrace();
    }
  }

  public void gotoProductDetail(int paramInt)
  {
    String str = this.cardProductURL + paramInt;
    try
    {
      str = URLEncoder.encode(str, "utf-8");
      startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + str)), 10);
      return;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
    }
  }

  public void gotoProductDetail(String paramString)
  {
    if (paramString == null)
      return;
    try
    {
      paramString = URLEncoder.encode(paramString, "utf-8");
      startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString + "&from=3" + "&title=" + this.cardObject.getString("tilte"))), 10);
      return;
    }
    catch (UnsupportedEncodingException paramString)
    {
      paramString.printStackTrace();
    }
  }

  protected void gotoQrCodeActivity()
  {
    if (this.isCardScore);
    for (String str = "1"; ; str = "0")
    {
      startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://mymcqrcode?membercardid=" + this.cardObject.getInt("MemberCardID") + "&iscardscore=" + str)), 30);
      return;
    }
  }

  public void gotoScoreDetail(String paramString, int paramInt)
  {
    String str = paramString;
    if (paramString.contains("membercardid="))
    {
      int i = this.cardObject.getInt("MemberCardID");
      str = paramString.replace("membercardid=", "membercardid=" + String.valueOf(i));
    }
    this.scoreUrl = str;
    if (paramInt == FROM_MEMBERCARDINFOACTIVITY)
    {
      paramString = new Intent("com.dianping.action.MEBMERCARDINFOACTIVITY_CHECK_LOGIN");
      getActivity().sendBroadcast(paramString);
    }
    do
      return;
    while (paramInt != FROM_MYCARDFRAGMENT);
    paramString = new Intent("com.dianping.action.MYCARDFRAGMENT_CHECK_LOGIN");
    getActivity().sendBroadcast(paramString);
  }

  public void gotoScoreUrl()
  {
    startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + this.scoreUrl)), 10);
  }

  public void gotoShopInfo()
  {
    int i = this.cardObject.getInt("ShopID");
    this.cardObject.getInt("CardType");
    Object localObject = this.cardObject.getString("Title");
    if (i == 0)
      return;
    if (this.source == 12);
    while (true)
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + i));
      ((Intent)localObject).putExtra("fromMC", 1);
      startActivity((Intent)localObject);
      return;
      if (this.source == 14)
      {
        statisticsEvent("cardinfo5", "cardinfo5_shopinfo_availablecard", i + "|" + (String)localObject, 0);
        continue;
      }
      if ((this.source < 30) || (this.source > 39))
        continue;
      statisticsEvent("cardinfo5", "cardinfo5_shopinfo_landingpage", i + "|" + (String)localObject, 0);
    }
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    this.cardObject = ((DPObject)getArguments().getParcelable("card"));
    this.source = getArguments().getInt("source");
    this.cardlevel = getArguments().getInt("cardlevel");
    this.cardProductURL = (this.cardObject.getString("CardProductURL") + "&productid=");
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.container)
    {
      statisticsEvent("mycard5", "mycard5_detail_cover", null, 0);
      if ((this.isCardScore) && (!this.isCardScoreBinded))
        new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("请先提交手机号，获取积分优惠码").setPositiveButton("确认", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            CardFragment.this.statisticsEvent("mycard5", "mycard5_detail_cover_alert", null, 0);
          }
        }).show();
    }
    else
    {
      return;
    }
    paramView = accountService();
    if (paramView.profile() == null)
    {
      paramView.login(this);
      this.needLogin = true;
      return;
    }
    gotoQrCodeActivity();
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    if (this.needLogin)
    {
      this.needLogin = false;
      gotoQrCodeActivity();
    }
  }

  public void refresh(DPObject paramDPObject)
  {
  }

  public void refreshByCardId(int paramInt)
  {
  }

  public void refreshUI()
  {
  }

  public void setCardScore(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.isCardScore = paramBoolean1;
    this.isCardScoreBinded = paramBoolean2;
  }

  public static abstract interface refreshMemberCardHandler
  {
    public abstract void refreshMemberCard(DPObject paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.CardFragment
 * JD-Core Version:    0.6.0
 */
package com.dianping.membercard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.membercard.fragment.NoAddedThirdPartyCardFragment;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class NoAddedThirdPartyCardActivity extends NovaActivity
{
  private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (TextUtils.isEmpty(NoAddedThirdPartyCardActivity.this.mMemberCardID));
      for (paramContext = "0"; ; paramContext = NoAddedThirdPartyCardActivity.this.mMemberCardID)
      {
        MCUtils.sendUpdateMemberCardListBroadcast(NoAddedThirdPartyCardActivity.this, paramContext);
        NoAddedThirdPartyCardActivity.this.finish();
        return;
      }
    }
  };
  private String mMemberCardID;
  private NoAddedThirdPartyCardFragment noAddedThirdPartyCardFragment;

  private boolean isIntentDataValid()
  {
    return (getIntent().getData() != null) && (!TextUtils.isEmpty(getStringParam("membercardid")));
  }

  public String getPageName()
  {
    return "wecardadd";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 1);
  }

  public void onCreate(Bundle paramBundle)
  {
    int j = 0;
    super.onCreate(paramBundle);
    super.setTitle("成为会员");
    if (!isIntentDataValid())
      throw new IllegalArgumentException("intent data is invalid");
    super.setContentView(R.layout.mc_no_added_third_party_activity);
    int i = getIntParam("source");
    paramBundle = new Bundle();
    paramBundle.putString("membercardid", getStringParam("membercardid"));
    paramBundle.putString("shopid", getStringParam("shopid"));
    if (i >= 0)
    {
      if ((TextUtils.isEmpty(getStringParam("source"))) || (!TextUtils.isDigitsOnly(getStringParam("source"))))
        break label253;
      i = Integer.valueOf(getStringParam("source")).intValue();
      label117: paramBundle.putInt("source", i);
      this.noAddedThirdPartyCardFragment = new NoAddedThirdPartyCardFragment();
      this.noAddedThirdPartyCardFragment.setArguments(paramBundle);
      getSupportFragmentManager().beginTransaction().add(R.id.card_fragment_layout, this.noAddedThirdPartyCardFragment).commit();
      paramBundle = this.gaExtra;
      if (!TextUtils.isEmpty(getStringParam("shopid")))
        break label272;
      i = 0;
      label183: paramBundle.shop_id = Integer.valueOf(i);
      paramBundle = this.gaExtra;
      if (!TextUtils.isEmpty(getStringParam("membercardid")))
        break label288;
    }
    label272: label288: for (i = j; ; i = Integer.valueOf(getStringParam("membercardid")).intValue())
    {
      paramBundle.member_card_id = Integer.valueOf(i);
      this.mMemberCardID = getStringParam("membercardid");
      registerReceiver(this.mBroadcastReceiver, new IntentFilter("Card:JoinSuccess"));
      return;
      i = 12;
      break;
      label253: if (getIntParam("source") == 0)
        break label117;
      i = getIntParam("source");
      break label117;
      i = Integer.valueOf(getStringParam("shopid")).intValue();
      break label183;
    }
  }

  protected void onDestroy()
  {
    unregisterReceiver(this.mBroadcastReceiver);
    super.onDestroy();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.NoAddedThirdPartyCardActivity
 * JD-Core Version:    0.6.0
 */
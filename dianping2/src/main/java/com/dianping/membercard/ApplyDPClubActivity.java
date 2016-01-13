package com.dianping.membercard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.base.web.ui.NovaZeusActivity;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.widget.view.GAUserInfo;

public class ApplyDPClubActivity extends NovaZeusActivity
{
  final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      MCUtils.sendJoinScoreCardSuccessBroadcast(ApplyDPClubActivity.this, ApplyDPClubActivity.this.mMemberCardId);
      MCUtils.sendUpdateMemberCardListBroadcast(ApplyDPClubActivity.this, ApplyDPClubActivity.this.mMemberCardId);
      ApplyDPClubActivity.this.finish();
    }
  };
  private String mMemberCardId;

  private void addGa()
  {
    int j = 0;
    String str = getStringParam("membercardid");
    Object localObject = getStringParam("shopid");
    GAUserInfo localGAUserInfo = this.gaExtra;
    if (TextUtils.isEmpty((CharSequence)localObject))
    {
      i = 0;
      localGAUserInfo.shop_id = Integer.valueOf(i);
      localObject = this.gaExtra;
      if (!TextUtils.isEmpty(str))
        break label84;
    }
    label84: for (int i = j; ; i = Integer.valueOf(str).intValue())
    {
      ((GAUserInfo)localObject).member_card_id = Integer.valueOf(i);
      this.mMemberCardId = str;
      return;
      i = Integer.valueOf((String)localObject).intValue();
      break;
    }
  }

  public String getPageName()
  {
    return "applydpclub";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    addGa();
    registerReceiver(this.mBroadcastReceiver, new IntentFilter("Card:JoinSuccess"));
  }

  public void onDestroy()
  {
    unregisterReceiver(this.mBroadcastReceiver);
    super.onDestroy();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.ApplyDPClubActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class MemberCardAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_MEMBER_CARD = "0500Cash.70MemberCard";
  DPObject mcStatus;
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      MemberCardAgent.this.sendRequest();
    }
  };
  MApiRequest request;
  private NovaLinearLayout shopinfoCellContainer;

  public MemberCardAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void createCellView()
  {
    super.removeAllCells();
    this.shopinfoCellContainer = ((NovaLinearLayout)this.res.inflate(getContext(), R.layout.card_shopinfo_entry_cell, getParentView(), false));
    this.shopinfoCellContainer.setGAString("card", getGAExtra());
  }

  private boolean isThirdPartyCard()
  {
    return this.mcStatus.getInt("ThirdPartyType") >= 1;
  }

  private void openThirdPartyCardView()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(this.mcStatus.getString("NavigateUrl")));
    getFragment().startActivity(localIntent);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    int k = 0;
    super.onAgentChanged(paramBundle);
    paramBundle = getShop();
    if ((paramBundle == null) || ((paramBundle.getInt("ShopMemberCardID") <= 0) && ((paramBundle.getArray("StoreCardGroupList") == null) || (paramBundle.getArray("StoreCardGroupList").length == 0))))
    {
      removeAllCells();
      return;
    }
    if (this.shopinfoCellContainer == null)
      createCellView();
    int j = 1;
    if (this.mcStatus == null)
      sendRequest();
    paramBundle = "card";
    TextView localTextView3 = (TextView)this.shopinfoCellContainer.findViewById(16908308);
    TextView localTextView1 = (TextView)this.shopinfoCellContainer.findViewById(R.id.text3);
    TextView localTextView2 = (TextView)this.shopinfoCellContainer.findViewById(R.id.text4);
    localTextView3.setText("免费申请");
    String str2;
    int i;
    label169: String str3;
    if (this.mcStatus != null)
    {
      str2 = this.mcStatus.getString("MemberCardDesc");
      if ((!this.mcStatus.getBoolean("IsMCShop")) || (TextUtils.isEmpty(str2)))
        break label337;
      i = 1;
      Log.v("shop", "updateMCStatus mcStatus.IsMCUser: " + this.mcStatus.getBoolean("IsMCUser"));
      str3 = this.mcStatus.getString("TabMsg");
      if (!TextUtils.isEmpty(str3))
        break label343;
      j = 8;
      label224: localTextView1.setVisibility(j);
      j = k;
      if (TextUtils.isEmpty(str3))
        j = 8;
      localTextView2.setVisibility(j);
      if (!this.mcStatus.getBoolean("IsMCUser"))
        break label349;
      if ((str2 != null) && (str2.length() > 0))
        localTextView3.setText(str2);
      paramBundle = "cardinfo";
      localTextView2.setText(str3);
      localTextView1.setVisibility(8);
      j = i;
    }
    while (true)
    {
      removeAllCells();
      if (j == 0)
        break;
      this.shopinfoCellContainer.setGAString(paramBundle);
      addCell("0500Cash.70MemberCard", this.shopinfoCellContainer, paramBundle, 257);
      return;
      label337: i = 0;
      break label169;
      label343: j = 0;
      break label224;
      label349: String str1 = "clubinfo";
      localTextView1.setText(str3);
      localTextView2.setVisibility(8);
      j = i;
      paramBundle = str1;
      if (str2 == null)
        continue;
      j = i;
      paramBundle = str1;
      if (str2.length() <= 0)
        continue;
      localTextView3.setText(str2 + "，免费申请");
      j = i;
      paramBundle = str1;
    }
  }

  public void onCellClick(String paramString, View paramView)
  {
    super.onCellClick(paramString, paramView);
    paramString = getShop();
    if (paramString == null);
    int i;
    do
    {
      return;
      if (isWeddingShopType())
      {
        paramView = new ArrayList();
        paramView.add(new BasicNameValuePair("shopid", shopId() + ""));
        statisticsEvent("shopinfoq", "shopinfoq_card", "", 0, paramView);
      }
      boolean bool;
      if (this.mcStatus == null)
        bool = false;
      while (true)
      {
        i = paramString.getInt("ShopMemberCardID");
        if (!bool)
          break;
        statisticsEvent("shopinfo5", "shopinfo5_mycard", shopId() + "|" + paramString.getString("Name"), 0);
        if (isThirdPartyCard())
        {
          openThirdPartyCardView();
          return;
          bool = this.mcStatus.getBoolean("IsMCUser");
          continue;
        }
        paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://membercardinfo?membercardid=" + i + "&shopid=" + shopId()));
        getFragment().startActivity(paramString);
        return;
      }
      statisticsEvent("shopinfo5", "shopinfo5_applycard", shopId() + "|" + paramString.getString("Name"), 0);
    }
    while (this.mcStatus == null);
    if (isThirdPartyCard())
    {
      openThirdPartyCardView();
      return;
    }
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://membersonly?membercardid=" + i + "&source=12" + "&shopid=" + shopId()));
    getFragment().startActivity(paramString);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
      this.mcStatus = ((DPObject)paramBundle.getParcelable("mcStatus"));
    paramBundle = new IntentFilter("com.dianping.action.JOIN_MEMBER_CARD");
    getContext().registerReceiver(this.receiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.QUIT_MEMBER_CARD");
    getContext().registerReceiver(this.receiver, paramBundle);
    paramBundle = new IntentFilter("Card:CardChanged");
    getContext().registerReceiver(this.receiver, paramBundle);
  }

  public void onDestroy()
  {
    if (this.request != null)
    {
      getFragment().mapiService().abort(this.request, this, true);
      this.request = null;
    }
    getContext().unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.mcStatus = ((DPObject)paramMApiResponse.result());
    dispatchAgentChanged(false);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("mcStatus", this.mcStatus);
    return localBundle;
  }

  public void sendRequest()
  {
    String str2 = "http://mc.api.dianping.com/getmcstatus.v2.mc?shopid=" + shopId();
    AccountService localAccountService = getFragment().accountService();
    String str1 = str2;
    if (localAccountService.token() != null)
      str1 = str2 + "&token=" + localAccountService.token();
    this.request = BasicMApiRequest.mapiGet(str1 + "&uuid=" + Environment.uuid(), CacheType.DISABLED);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (MemberCardAgent.this.request != null)
          MemberCardAgent.this.getFragment().mapiService().exec(MemberCardAgent.this.request, MemberCardAgent.this);
      }
    }
    , 100L);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.MemberCardAgent
 * JD-Core Version:    0.6.0
 */
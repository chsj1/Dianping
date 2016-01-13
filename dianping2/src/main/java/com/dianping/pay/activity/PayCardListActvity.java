package com.dianping.pay.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.utils.ListViewDresser;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.pay.view.FloatBottomMenu;
import com.dianping.pay.view.FloatBottomMenu.MenuItem;
import com.dianping.pay.view.FloatBottomMenu.OnMenuClickListener;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import java.util.ArrayList;
import java.util.List;

public class PayCardListActvity extends NovaActivity
  implements FloatBottomMenu.OnMenuClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int CODE_HAS_PASSWD = 22000;
  private static final int CODE_NO_PASSWD = 22001;
  private static final int CODE_SESSION_FAILED = 22002;
  private static final int CODE_SYSTEM_EXCEPTION = 22003;
  private static final int MENU_FIND_PWD = 1;
  private static final int MENU_MODIFY_PWD = 0;
  private static final String PWD_MANAGE_BUTTON = "pay_pwd_management";
  private Adapter mAdapter;
  private MApiRequest mBanksRequest;
  private View mBtnOpenMenu;
  private MApiRequest mCheckRequest;
  private boolean mFindModeOn = false;
  private ListView mListView;
  private List<FloatBottomMenu.MenuItem> mMenuItems = new ArrayList();
  private FloatBottomMenu mMenuPanel;
  private TextView mTvHint;
  private String state;

  private DPObject changeBankElementType(DPObject paramDPObject)
  {
    return new DPObject().edit().putString("PaymentID", paramDPObject.getString("ID")).putInt("CardType", paramDPObject.getInt("CardType")).putString("IconUrl", paramDPObject.getString("IconUrl")).putString("BankName", paramDPObject.getString("BankName")).putString("Tip", paramDPObject.getString("EventTitle")).putString("DiscountAmount", String.valueOf(paramDPObject.getDouble("EventDiscountAmount"))).putString("EventUrl", paramDPObject.getString("EventUrl")).putString("TailNumber", paramDPObject.getString("TailNumber")).generate();
  }

  private void createContent()
  {
    setContentView(R.layout.pay_activity_card_list);
    getTitleBar().addRightViewItem("支付密码管理", "pay_pwd_management", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PayCardListActvity.this.showFloatMenu();
      }
    });
    this.mBtnOpenMenu = getTitleBar().findRightViewItemByTag("pay_pwd_management");
    this.mBtnOpenMenu.setVisibility(8);
    this.mMenuPanel = FloatBottomMenu.create(this).setOnMenuClickListener(this);
    this.mTvHint = ((TextView)findViewById(R.id.tv_select_hint));
    this.mListView = ((ListView)findViewById(R.id.listview_card));
    this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        if (PayCardListActvity.RequestStatus.SUCCEED != paramView.getTag());
        do
        {
          return;
          paramAdapterView = (DPObject)paramAdapterView.getItemAtPosition(paramInt);
        }
        while (paramAdapterView == null);
        if (PayCardListActvity.this.mFindModeOn)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paywebankresetverify"));
          paramView.putExtra("source", 2);
          paramView.putExtra("bankelement", PayCardListActvity.this.changeBankElementType(paramAdapterView));
          PayCardListActvity.this.startActivity(paramView);
          PayCardListActvity.this.finish();
          return;
        }
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paycarddetail"));
        paramView.putExtra("bankelement", paramAdapterView);
        PayCardListActvity.this.startActivity(paramView);
      }
    });
    this.mAdapter = new Adapter(null);
    this.mListView.setAdapter(this.mAdapter);
    requestBankList();
  }

  private void findPwdMode(boolean paramBoolean)
  {
    int j = 8;
    Object localObject = this.mTvHint;
    int i;
    if (paramBoolean)
    {
      i = 0;
      ((TextView)localObject).setVisibility(i);
      localObject = this.mBtnOpenMenu;
      if (!paramBoolean)
        break label61;
      i = j;
      label32: ((View)localObject).setVisibility(i);
      if (!paramBoolean)
        break label66;
    }
    label61: label66: for (localObject = "找回支付密码"; ; localObject = "我的银行卡")
    {
      setTitle((CharSequence)localObject);
      this.mFindModeOn = paramBoolean;
      return;
      i = 8;
      break;
      i = 0;
      break label32;
    }
  }

  private void handleRequestResult(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse, boolean paramBoolean)
  {
    if (paramMApiRequest == this.mBanksRequest)
      if ((paramBoolean) && (DPObjectUtils.isDPObjectof(paramMApiResponse.result(), "UserBanklistResult")))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        paramBoolean = paramMApiRequest.getBoolean("HasPass");
        paramMApiRequest = paramMApiRequest.getArray("QuickPayBankElement");
        if ((paramMApiRequest == null) || (paramMApiRequest.length == 0))
        {
          this.mBtnOpenMenu.setVisibility(8);
          this.mAdapter.setCards(paramMApiRequest);
          label78: this.mBanksRequest = null;
          if ("forgot_pwd".equals(this.state))
            findPwdMode(true);
        }
      }
    do
    {
      return;
      this.mBtnOpenMenu.setVisibility(0);
      this.mMenuItems.clear();
      if (paramBoolean)
        this.mMenuItems.add(new FloatBottomMenu.MenuItem(0, "修改支付密码"));
      this.mMenuItems.add(new FloatBottomMenu.MenuItem(1, "找回支付密码"));
      break;
      this.mAdapter.setRequestStatus(RequestStatus.ERROR);
      break label78;
    }
    while (paramMApiRequest != this.mCheckRequest);
    dismissDialog();
    if (paramBoolean)
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      switch (paramMApiRequest.getInt("ResultCode"))
      {
      default:
      case 22000:
      case 22001:
      case 22002:
      case 22003:
      }
    }
    while (true)
    {
      this.mCheckRequest = null;
      return;
      startActivity("dianping://paycheckpwd?action=modifypwd&paysessionid=" + paramMApiRequest.getString("PaySessionID"));
      continue;
      showAlertDialog(paramMApiRequest.getString("ResultTitle"), paramMApiRequest.getString("ResultMsg"));
      continue;
      showToast("网络问题，请稍后重试");
    }
  }

  private void showFloatMenu()
  {
    this.mMenuPanel.setMenuItems(this.mMenuItems);
    this.mMenuPanel.show();
  }

  public void checkQuickPayInfo()
  {
    if (this.mCheckRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://api.p.dianping.com/");
    localUrlBuilder.appendPath("quickpay/ishavefullquickpayinfo.pay");
    localUrlBuilder.addParam("token", accountService().token());
    this.mCheckRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.mCheckRequest, this);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.state = getStringParam("state");
    if (isLogined())
      createContent();
    if ("forgot_pwd".equals(this.state))
      findPwdMode(true);
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if ((paramBoolean) && (isNeedLogin()))
      createContent();
    return super.onLogin(paramBoolean);
  }

  public void onMenuClick(int paramInt, View paramView)
  {
    switch (paramInt)
    {
    default:
      return;
    case 0:
      showProgressDialog("请求数据中...");
      checkQuickPayInfo();
      return;
    case 1:
    }
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://paycardlist?state=forgot_pwd")));
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    handleRequestResult(paramMApiRequest, paramMApiResponse, false);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    handleRequestResult(paramMApiRequest, paramMApiResponse, true);
  }

  public void requestBankList()
  {
    if (this.mBanksRequest != null)
      return;
    this.mAdapter.setRequestStatus(RequestStatus.LOADING);
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://api.p.dianping.com/");
    localUrlBuilder.appendPath("quickpay/getuserbanklist.pay");
    localUrlBuilder.addParam("token", accountService().token());
    this.mBanksRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.mBanksRequest, this);
  }

  private class Adapter extends BasicAdapter
  {
    private DPObject[] mBanks;
    private PayCardListActvity.RequestStatus mStatus = PayCardListActvity.RequestStatus.ERROR;

    private Adapter()
    {
    }

    private void bindView(View paramView, DPObject paramDPObject)
    {
      String str = paramDPObject.getString("IconUrl");
      NetworkThumbView localNetworkThumbView = (NetworkThumbView)paramView.findViewById(R.id.icon_bank);
      if (TextUtils.isEmpty(str))
        localNetworkThumbView.setVisibility(8);
      while (true)
      {
        ListViewDresser.setText((TextView)paramView.findViewById(R.id.tv_title), paramDPObject, "BankName");
        int i = paramDPObject.getInt("CardType");
        paramDPObject = paramDPObject.getString("TailNumber");
        paramView = (TextView)paramView.findViewById(R.id.tv_info);
        if (!TextUtils.isEmpty(paramDPObject));
        switch (i)
        {
        default:
          return;
          localNetworkThumbView.setImage(str);
        case 1:
        case 2:
        }
      }
      paramView.setText("储蓄卡(" + paramDPObject + ")");
      return;
      paramView.setText("信用卡(" + paramDPObject + ")");
    }

    public int getCount()
    {
      if ((this.mBanks == null) || (this.mBanks.length == 0))
        return 1;
      return this.mBanks.length;
    }

    public Object getItem(int paramInt)
    {
      if ((this.mBanks != null) && (this.mBanks.length > paramInt))
        return this.mBanks[paramInt];
      return null;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      switch (PayCardListActvity.3.$SwitchMap$com$dianping$pay$activity$PayCardListActvity$RequestStatus[this.mStatus.ordinal()])
      {
      default:
      case 1:
      case 2:
        do
        {
          do
            return paramView;
          while ((paramView != null) && (LOADING == paramView.getTag()));
          paramView = PayCardListActvity.this.getLayoutInflater().inflate(R.layout.loading_item, paramViewGroup, false);
          paramView.setBackgroundResource(17170443);
          paramView.setTag(LOADING);
          return paramView;
        }
        while ((paramView != null) && (ERROR == paramView.getTag()));
        paramView = (LoadingErrorView)PayCardListActvity.this.getLayoutInflater().inflate(R.layout.error_item, paramViewGroup, false);
        paramView.setTag(ERROR);
        paramView.setBackgroundResource(17170443);
        ((TextView)paramView.findViewById(16908308)).setText("网络连接失败，点击重新加载");
        paramView.setCallBack(new PayCardListActvity.Adapter.1(this));
        return paramView;
      case 3:
      }
      DPObject localDPObject = (DPObject)getItem(paramInt);
      if (localDPObject == null)
      {
        paramView = PayCardListActvity.this.getLayoutInflater().inflate(R.layout.pay_no_card, paramViewGroup, false);
        paramView.setMinimumHeight(PayCardListActvity.this.mListView.getHeight());
        paramView.setClickable(false);
        return paramView;
      }
      View localView;
      if (paramView != null)
      {
        localView = paramView;
        if (PayCardListActvity.RequestStatus.SUCCEED == paramView.getTag());
      }
      else
      {
        localView = PayCardListActvity.this.getLayoutInflater().inflate(R.layout.pay_bank_card_item, paramViewGroup, false);
        localView.setTag(PayCardListActvity.RequestStatus.SUCCEED);
      }
      bindView(localView, localDPObject);
      return localView;
    }

    public void setCards(DPObject[] paramArrayOfDPObject)
    {
      this.mBanks = paramArrayOfDPObject;
      setRequestStatus(PayCardListActvity.RequestStatus.SUCCEED);
    }

    public void setRequestStatus(PayCardListActvity.RequestStatus paramRequestStatus)
    {
      this.mStatus = paramRequestStatus;
      notifyDataSetChanged();
    }
  }

  protected static enum RequestStatus
  {
    static
    {
      ERROR = new RequestStatus("ERROR", 1);
      SUCCEED = new RequestStatus("SUCCEED", 2);
      $VALUES = new RequestStatus[] { LOADING, ERROR, SUCCEED };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.activity.PayCardListActvity
 * JD-Core Version:    0.6.0
 */
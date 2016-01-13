package com.dianping.pay.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.utils.ListViewDresser;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.pay.view.FloatBottomMenu;
import com.dianping.pay.view.FloatBottomMenu.MenuItem;
import com.dianping.pay.view.FloatBottomMenu.OnMenuClickListener;
import com.dianping.util.DeviceUtils;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class PayCardDetailActivity extends NovaActivity
  implements RequestHandler
{
  private static final String TAG_UNBIND_CARD = "unbind_bank_card";
  private DPObject mBankData;
  private NetworkThumbView mBankIcon;
  private FloatBottomMenu mBottomMenu;
  private TextView mTvBankDesc;
  private TextView mTvBankName;
  private TextView mTvDayLimit;
  private TextView mTvOneDealLimit;
  private MApiRequest mUnbindRequest;

  private void setupViews()
  {
    if (this.mBankData == null)
      return;
    Object localObject = this.mBankData.getString("IconUrl");
    if (!TextUtils.isEmpty((CharSequence)localObject))
      this.mBankIcon.setImage((String)localObject);
    ListViewDresser.setText(this.mTvBankName, this.mBankData, "BankName");
    int i = this.mBankData.getInt("CardType");
    localObject = this.mBankData.getString("TailNumber");
    if (!TextUtils.isEmpty((CharSequence)localObject))
      switch (i)
      {
      default:
      case 1:
      case 2:
      }
    while (true)
    {
      localObject = this.mBankData.getObject("PayQuota");
      if (localObject == null)
        break;
      if (!TextUtils.isEmpty(((DPObject)localObject).getString("DayQuota")))
        this.mTvDayLimit.setText(((DPObject)localObject).getString("DayQuota") + "元");
      if (TextUtils.isEmpty(((DPObject)localObject).getString("OneDealQuota")))
        break;
      this.mTvOneDealLimit.setText(((DPObject)localObject).getString("OneDealQuota") + "元");
      return;
      this.mTvBankDesc.setText("储蓄卡(" + (String)localObject + ")");
      continue;
      this.mTvBankDesc.setText("信用卡(" + (String)localObject + ")");
    }
  }

  private void showBottomMenu()
  {
    if (this.mBottomMenu == null)
    {
      FloatBottomMenu.MenuItem localMenuItem = new FloatBottomMenu.MenuItem(0, "解除绑定");
      this.mBottomMenu = FloatBottomMenu.create(this);
      this.mBottomMenu.addMenu(localMenuItem);
      this.mBottomMenu.setOnMenuClickListener(new FloatBottomMenu.OnMenuClickListener()
      {
        public void onMenuClick(int paramInt, View paramView)
        {
          switch (paramInt)
          {
          default:
            return;
          case 0:
          }
          PayCardDetailActivity.this.unbindCard();
        }
      });
    }
    this.mBottomMenu.show();
  }

  private void unbindCard()
  {
    if (this.mUnbindRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://api.p.dianping.com/quickpay/unbindquickpaybankcard.pay");
    localUrlBuilder.addParam("cx", DeviceUtils.cxInfo("payorder"));
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("quickpaycontractid", this.mBankData.getString("ID"));
    localUrlBuilder.addParam("cityid", Integer.valueOf(city().id()));
    this.mUnbindRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.mUnbindRequest, this);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("银行卡详情");
    setContentView(R.layout.pay_activity_bank_card_detail);
    this.mBankData = ((DPObject)getIntent().getParcelableExtra("bankelement"));
    if (this.mBankData == null)
      return;
    this.mBankIcon = ((NetworkThumbView)findViewById(R.id.bank_icon));
    this.mTvBankName = ((TextView)findViewById(R.id.tv_bank_name));
    this.mTvBankDesc = ((TextView)findViewById(R.id.tv_bank_desc));
    this.mTvOneDealLimit = ((TextView)findViewById(R.id.tv_once_limit));
    this.mTvDayLimit = ((TextView)findViewById(R.id.tv_day_limit));
    getTitleBar().addRightViewItem("unbind_bank_card", R.drawable.detail_topbar_icon_more, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PayCardDetailActivity.this.showBottomMenu();
      }
    });
    setupViews();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    dismissDialog();
    if (paramRequest == this.mUnbindRequest)
      this.mUnbindRequest = null;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    dismissDialog();
    paramResponse = (DPObject)paramResponse.result();
    int i = paramResponse.getInt("ResultCode");
    String str1 = paramResponse.getString("ResultTitle");
    String str2 = paramResponse.getString("ResultMsg");
    if (paramRequest == this.mUnbindRequest)
      this.mUnbindRequest = null;
    switch (i)
    {
    default:
      showAlertDialog(str1, str2);
      return;
    case 12000:
      showToast(str2);
      paramRequest = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paycardlist"));
      paramRequest.addFlags(67108864);
      startActivity(paramRequest);
      return;
    case 12001:
      paramRequest = paramResponse.getString("PaySessionID");
      startActivity("dianping://paycheckpwd?action=unbind&cardid=" + this.mBankData.getString("ID") + "&paysessionid=" + paramRequest);
      return;
    case 12002:
    }
    showToast(str2);
    paramRequest = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paycardlist"));
    paramRequest.addFlags(67108864);
    startActivity(paramRequest);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.activity.PayCardDetailActivity
 * JD-Core Version:    0.6.0
 */
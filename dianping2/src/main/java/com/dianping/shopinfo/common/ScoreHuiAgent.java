package com.dianping.shopinfo.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.AddViewContainer;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaButton;

public class ScoreHuiAgent extends ShopCellAgent
  implements RequestHandler, View.OnClickListener
{
  private static final String CELL_SCORE_HUI = "0200Basic.06score";
  private boolean isPayEnable = false;
  private boolean isSendRequest = false;
  private DPObject payDetail;
  private MApiRequest payRequest;
  private View scoreHuiLayout;

  public ScoreHuiAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendPayRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://hui.api.dianping.com/getunicashierentry.hui").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("promostring", getShopExtraParam());
    localBuilder.appendQueryParameter("cityid", Integer.toString(cityId()));
    this.payRequest = mapiGet(this, localBuilder.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.payRequest, this);
  }

  private void setHuiLay()
  {
    SharedPreferences localSharedPreferences = getContext().getSharedPreferences("shopinfo_head", 0);
    if (!localSharedPreferences.getBoolean("isHuiLayerShow", false))
    {
      localSharedPreferences.edit().putBoolean("isHuiLayerShow", true).commit();
      setHuiLayVisibility(true);
    }
  }

  private void setSuspendView()
  {
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_top_pay_layout, null);
    localView.setOnClickListener(this);
    ((TextView)localView.findViewById(R.id.discount_text)).setText(TextUtils.jsonParseText(this.payDetail.getString("RichPromosDesc")));
    if (!TextUtils.isEmpty(this.payDetail.getString("UniCashierBtnTag")))
    {
      ((TextView)localView.findViewById(R.id.tag)).setText(this.payDetail.getString("UniCashierBtnTag"));
      localView.findViewById(R.id.tag).setVisibility(0);
    }
    while (true)
    {
      NovaButton localNovaButton = (NovaButton)localView.findViewById(R.id.pay_button);
      localNovaButton.setText(this.payDetail.getString("UniCashierBtn"));
      localNovaButton.setGAString("pay_ai");
      localNovaButton.setOnClickListener(this);
      setTopView(localView, this);
      return;
      localView.findViewById(R.id.tag).setVisibility(8);
    }
  }

  public View getView()
  {
    return this.scoreHuiLayout;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    paramBundle = getShop();
    if (paramBundle == null)
      return;
    if (((paramBundle.getBoolean("HasMOPay")) || (paramBundle.getBoolean("HasPay"))) && (!this.isSendRequest))
    {
      this.isSendRequest = true;
      sendPayRequest();
    }
    this.scoreHuiLayout = LayoutInflater.from(getContext()).inflate(R.layout.shop_score_hui_layout, null, false);
    ((TextView)this.scoreHuiLayout.findViewById(R.id.shop_score)).setText(paramBundle.getString("ScoreText"));
    setFacilityIcon((AddViewContainer)this.scoreHuiLayout.findViewById(R.id.shop_facility_lay));
    if (this.isPayEnable)
    {
      this.scoreHuiLayout.findViewById(R.id.pay_button).setVisibility(0);
      ((NovaButton)this.scoreHuiLayout.findViewById(R.id.pay_button)).setText(this.payDetail.getString("UniCashierBtn"));
      ((NovaButton)this.scoreHuiLayout.findViewById(R.id.pay_button)).setGAString("pay_ai");
      this.scoreHuiLayout.findViewById(R.id.pay_button).setOnClickListener(this);
      if (TextUtils.isEmpty(this.payDetail.getString("UniCashierBtnTag")))
        break label285;
      ((TextView)this.scoreHuiLayout.findViewById(R.id.tag)).setText(this.payDetail.getString("UniCashierBtnTag"));
      this.scoreHuiLayout.findViewById(R.id.tag).setVisibility(0);
    }
    while (true)
    {
      setSuspendView();
      setHuiLay();
      if ((TextUtils.isEmpty(paramBundle.getString("ScoreText"))) && (!this.isPayEnable))
        break;
      addCell("0200Basic.06score", this.scoreHuiLayout);
      return;
      label285: this.scoreHuiLayout.findViewById(R.id.tag).setVisibility(8);
    }
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.pay_button);
    try
    {
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.payDetail.getString("UniCashierUrl"))));
      return;
    }
    catch (java.lang.Exception paramView)
    {
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.isPayEnable)
      removeTopView();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.payRequest)
      this.payRequest = null;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.payRequest)
    {
      this.payRequest = null;
      this.payDetail = ((DPObject)paramResponse.result());
      if (this.payDetail != null)
      {
        this.isPayEnable = this.payDetail.getBoolean("HasCashierEntry");
        if (this.isPayEnable)
          dispatchAgentChanged(false);
      }
    }
  }

  protected void setFacilityIcon(AddViewContainer paramAddViewContainer)
  {
    if ((paramAddViewContainer == null) || (getShop().getObject("ClientShopStyle") == null));
    DPObject[] arrayOfDPObject;
    do
    {
      return;
      arrayOfDPObject = getShop().getArray("InfraList");
    }
    while ((arrayOfDPObject == null) || (arrayOfDPObject.length <= 0));
    paramAddViewContainer.setVisibility(0);
    paramAddViewContainer.removeAllViews();
    paramAddViewContainer.setAvailableWidth(ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 137.0F) - ViewUtils.getViewWidth(this.scoreHuiLayout.findViewById(R.id.shop_score)));
    int j = 0;
    int k = arrayOfDPObject.length;
    int i = 0;
    label93: DPObject localDPObject;
    NetworkImageView localNetworkImageView;
    LinearLayout.LayoutParams localLayoutParams;
    if (i < k)
    {
      localDPObject = arrayOfDPObject[i];
      localNetworkImageView = (NetworkImageView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_infra_item, null, false);
      localLayoutParams = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 15.0F), ViewUtils.dip2px(getContext(), 15.0F));
      if (j != 0)
        break label206;
      j += 1;
    }
    while (true)
    {
      localLayoutParams.gravity = 17;
      localNetworkImageView.setLayoutParams(localLayoutParams);
      localNetworkImageView.setImage(localDPObject.getString("Icon"));
      paramAddViewContainer.addView(localNetworkImageView);
      i += 1;
      break label93;
      break;
      label206: localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 4.0F);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.ScoreHuiAgent
 * JD-Core Version:    0.6.0
 */
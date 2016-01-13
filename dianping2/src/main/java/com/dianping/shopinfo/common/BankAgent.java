package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;

public class BankAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final String API = "http://m.api.dianping.com/midas_bank/bankpromotion.bin";
  private static final String CELL_BANK = "0500Cash.70Bank";
  private static final String IS_REQUEST_FINISHED = "isRequestFinished";
  private static final String KEY_PROMOTION = "promotion_list";
  private MApiRequest mBankReq;
  private DPObject mResult;
  private boolean mbReqFinished;

  public BankAgent(Object paramObject)
  {
    super(paramObject);
  }

  private CommonCell createBankCell()
  {
    CommonCell localCommonCell = (CommonCell)this.res.inflate(getContext(), R.layout.bank_cell, getParentView(), false);
    localCommonCell.setLeftIcon(R.drawable.detail_bank);
    return localCommonCell;
  }

  private void reqPromotion()
  {
    if (getFragment() == null)
      return;
    if (this.mBankReq != null)
      getFragment().mapiService().abort(this.mBankReq, this, true);
    int i = shopId();
    this.mBankReq = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/midas_bank/bankpromotion.bin").buildUpon().appendQueryParameter("shopid", String.valueOf(i)).build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mBankReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if ((getShop() != null) && (getShopStatus() == 0) && (getShop().getBoolean("HasBankCard")) && (!this.mbReqFinished))
      reqPromotion();
    do
      return;
    while ((this.mResult == null) || (TextUtils.isEmpty(this.mResult.getString("Title"))));
    paramBundle = new LinearLayout(getContext());
    Object localObject = new LinearLayout.LayoutParams(-1, -2);
    paramBundle.setOrientation(1);
    paramBundle.setLayoutParams((ViewGroup.LayoutParams)localObject);
    localObject = createBankCell();
    ((CommonCell)localObject).setGAString("Bank", "", 0);
    ((CommonCell)localObject).setTitle(this.mResult.getString("Title"));
    ((CommonCell)localObject).setSubTitle(this.mResult.getString("SubTitle"));
    ((CommonCell)localObject).setOnClickListener(this);
    paramBundle.addView((View)localObject);
    addCell("0500Cash.70Bank", paramBundle, "Bank", 0);
  }

  public void onClick(View paramView)
  {
    if (this.mResult == null);
    for (paramView = ""; TextUtils.isEmpty(paramView); paramView = this.mResult.getString("Url"))
      return;
    getFragment().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView)));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.mResult = ((DPObject)paramBundle.getParcelable("promotion_list"));
      this.mbReqFinished = paramBundle.getBoolean("isRequestFinished");
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mBankReq = null;
    this.mbReqFinished = true;
    dispatchAgentChanged(false);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mBankReq = null;
    if (paramMApiResponse == null)
      return;
    this.mResult = ((DPObject)paramMApiResponse.result());
    this.mbReqFinished = true;
    dispatchAgentChanged(false);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("promotion_list", this.mResult);
    localBundle.putBoolean("isRequestFinished", this.mbReqFinished);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.BankAgent
 * JD-Core Version:    0.6.0
 */
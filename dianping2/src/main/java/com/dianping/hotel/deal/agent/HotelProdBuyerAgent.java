package com.dianping.hotel.deal.agent;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.tuan.agent.TuanGroupCellAgent;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.BuyDealView.OnBuyClickListener;
import com.dianping.hotel.deal.fragment.HotelProdInfoAgentFragment;
import com.dianping.hotel.deal.widget.HotelProdBuyView;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.id;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HotelProdBuyerAgent extends TuanGroupCellAgent
  implements BuyDealView.OnBuyClickListener
{
  protected static final int BUY_BUTTON_CLICKED = R.id.buy;
  private DPObject dpHotelProdBase;
  protected HotelProdBuyView mBuyItemView;
  protected HotelProdBuyView mBuyItemViewTop;
  private HotelProdInfoAgentFragment mProdFragment;

  public HotelProdBuyerAgent(Object paramObject)
  {
    super(paramObject);
    this.mProdFragment = ((HotelProdInfoAgentFragment)paramObject);
  }

  private void gotoPurchaseByUrl(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    if (paramString.startsWith("dianping"))
    {
      getFragment().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramString)));
      return;
    }
    try
    {
      String str = URLEncoder.encode(paramString, "utf-8");
      paramString = str;
      label47: getFragment().startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString)));
      return;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      break label47;
    }
  }

  private void gotoPurchaseTuanDeal()
  {
    Object localObject1 = this.dpHotelProdBase.getObject("Deal");
    if ((localObject1 == null) || (DPObjectUtils.isArrayEmpty(((DPObject)localObject1).getArray("DealSelectList"))));
    Object localObject2;
    do
    {
      return;
      localObject2 = ((DPObject)localObject1).getArray("DealSelectList");
    }
    while (localObject2.length != 1);
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
    localIntent.putExtra("deal", (Parcelable)localObject1);
    localIntent.putExtra("dealSelect", localObject2[0]);
    localObject1 = getSharedObject("bookingDefaultInfo");
    if ((localObject1 != null) && ((localObject1 instanceof DPObject)))
    {
      localObject2 = new Bundle();
      ((Bundle)localObject2).putParcelable("bookingDefaultInfo", (DPObject)localObject1);
      localIntent.putExtra("extradata", (Bundle)localObject2);
    }
    getFragment().startActivity(localIntent);
  }

  private void gotoPurchaseZeus()
  {
    Object localObject = this.dpHotelProdBase.getArray("HotelPackageList");
    if (DPObjectUtils.isArrayEmpty(localObject));
    while (true)
    {
      return;
      if (localObject.length != 1)
        break;
      localObject = localObject[0];
      if (localObject == null)
        continue;
      gotoPurchaseByUrl(((DPObject)localObject).getString("RedirectUrl"));
      return;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hotelproductselector?id=" + this.mProdFragment.productId));
    localIntent.putExtra("hotelProdPackages", localObject);
    getFragment().startActivity(localIntent);
  }

  protected boolean checkIsLocked()
  {
    if (accountService().token() == null);
    do
      return false;
    while (!getAccount().grouponIsLocked());
    new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("您的账户存在异常已被锁定，请联系客服为您解除锁定。").setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if ((HotelProdBuyerAgent.this.getContext() instanceof Activity))
          ((Activity)HotelProdBuyerAgent.this.getContext()).finish();
      }
    }).setCancelable(false).show();
    return true;
  }

  public View getView()
  {
    if (this.mBuyItemView == null)
      setupView();
    return this.mBuyItemView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null);
    do
    {
      return;
      if (paramBundle == null)
        continue;
      this.dpHotelProdBase = ((DPObject)paramBundle.getParcelable("hotelprodbase"));
    }
    while (this.dpHotelProdBase == null);
    if (this.mBuyItemView == null)
      setupView();
    updateView();
  }

  public void onClick(View paramView)
  {
    if ((handleAction(BUY_BUTTON_CLICKED)) || (this.dpHotelProdBase == null) || (this.dpHotelProdBase.getObject("BuyConfig") == null));
    do
      return;
    while (checkIsLocked());
    if (this.mProdFragment.bizType == 3)
    {
      gotoPurchaseZeus();
      return;
    }
    if (this.mProdFragment.bizType == 1)
    {
      gotoPurchaseTuanDeal();
      return;
    }
    gotoPurchaseByUrl(this.dpHotelProdBase.getObject("BuyConfig").getString("BuyUrl"));
  }

  protected void setupView()
  {
    this.mBuyItemView = new HotelProdBuyView(getContext());
    this.mBuyItemViewTop = new HotelProdBuyView(getContext());
    this.mBuyItemView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    this.mBuyItemViewTop.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    this.mBuyItemView.setShowTags(false);
    this.mBuyItemViewTop.setShowTags(false);
    this.mBuyItemView.setDataModel(this.dpHotelProdBase);
    this.mBuyItemViewTop.setDataModel(this.dpHotelProdBase);
    this.mBuyItemView.setOnBuyClickListener(this);
    this.mBuyItemViewTop.setOnBuyClickListener(this);
  }

  protected void updateView()
  {
    removeAllCells();
    this.mBuyItemView.setDataModel(this.dpHotelProdBase);
    this.mBuyItemViewTop.setDataModel(this.dpHotelProdBase);
    addCell("", this.mBuyItemView);
    if ((this.fragment instanceof AgentFragment.CellStable))
    {
      ((AgentFragment.CellStable)this.fragment).setBottomCell(null, this);
      ((AgentFragment.CellStable)this.fragment).setTopCell(this.mBuyItemViewTop, this);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdBuyerAgent
 * JD-Core Version:    0.6.0
 */
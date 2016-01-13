package com.dianping.shopinfo.base;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.layout;

public class BaseFacilityAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final String API = "http://m.api.dianping.com/mshop/getshopsupportservices.bin";
  private static final String CELL_PET_SERVICE = "0300Service.10BaseServices";
  private static final String IS_REQUEST_FINISHED = "isRequestFinished";
  private static final String KEY_RESULT = "result_list";
  private MApiRequest mReq;
  private DPObject mResult;
  private boolean mbReqFinished;

  public BaseFacilityAgent(Object paramObject)
  {
    super(paramObject);
  }

  private ShopinfoCommonCell createCell(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return null;
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    if (isPetType())
      localShopinfoCommonCell.setGAString("pet_service", getGAExtra());
    while (true)
    {
      localShopinfoCommonCell.hideArrow();
      localObject1 = paramDPObject.getStringArray("SupportServices");
      localObject2 = new StringBuilder();
      int j = localObject1.length;
      int i = 0;
      while (i < j)
      {
        String str = localObject1[i];
        ((StringBuilder)localObject2).append(str + "  ");
        i += 1;
      }
      if (!isSportClubType())
        continue;
      localShopinfoCommonCell.setGAString("fitness_sportfacilities", getGAExtra());
    }
    Object localObject1 = ((StringBuilder)localObject2).toString().trim();
    Object localObject2 = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_dish_textview, null, false);
    ((TextView)localObject2).setLineSpacing(ViewUtils.dip2px(getContext(), 7.4F), 1.0F);
    ((TextView)localObject2).setText((CharSequence)localObject1);
    localShopinfoCommonCell.addContent((View)localObject2, false, null);
    localShopinfoCommonCell.setTitle(paramDPObject.getString("Title"), null);
    return (ShopinfoCommonCell)(ShopinfoCommonCell)localShopinfoCommonCell;
  }

  private void sendRequest()
  {
    if (getFragment() == null)
      return;
    if (this.mReq != null)
      getFragment().mapiService().abort(this.mReq, this, true);
    int i = shopId();
    this.mReq = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/mshop/getshopsupportservices.bin").buildUpon().appendQueryParameter("shopid", String.valueOf(i)).build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
      return;
    while ((this.mResult == null) || (this.mResult.getStringArray("SupportServices") == null) || (this.mResult.getStringArray("SupportServices").length <= 0));
    addCell("0300Service.10BaseServices", createCell(this.mResult), 0);
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
      this.mResult = ((DPObject)paramBundle.getParcelable("result_list"));
      this.mbReqFinished = paramBundle.getBoolean("isRequestFinished");
    }
    if (!this.mbReqFinished)
    {
      sendRequest();
      this.mbReqFinished = true;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mReq = null;
    dispatchAgentChanged(false);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mReq = null;
    if (paramMApiResponse == null)
      return;
    this.mResult = ((DPObject)paramMApiResponse.result());
    dispatchAgentChanged(false);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("result_list", this.mResult);
    localBundle.putBoolean("isRequestFinished", this.mbReqFinished);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.base.BaseFacilityAgent
 * JD-Core Version:    0.6.0
 */
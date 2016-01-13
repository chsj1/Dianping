package com.dianping.shopinfo.mall;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.mall.view.EventSlidesView;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.Arrays;

public class MallHotPromoAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_MALLHOTPROMO = "0600promo.01content";
  private EventSlidesView eventSlidesView;
  DPObject mHotPromoList;
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (MallHotPromoAgent.this.mHotPromoList != null)
      {
        paramView = MallHotPromoAgent.this.mHotPromoList.getString("Url");
        if ((paramView != null) && (!paramView.equals("")))
        {
          Intent localIntent = new Intent("android.intent.action.VIEW");
          localIntent.setData(Uri.parse(paramView));
          MallHotPromoAgent.this.startActivity(localIntent);
        }
      }
    }
  };
  MApiRequest request;

  public MallHotPromoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createContentCell(DPObject[] paramArrayOfDPObject)
  {
    this.eventSlidesView = new EventSlidesView(getContext());
    this.eventSlidesView.setEventInfoList(paramArrayOfDPObject);
    return this.eventSlidesView;
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/gethotpromolist.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopId", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    if (isClothesType())
      localBuilder.appendQueryParameter("type", "1");
    this.request = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getShop() == null)
      removeAllCells();
    do
    {
      do
      {
        return;
        if ((this.mHotPromoList != null) || (this.request != null))
          continue;
        sendRequest();
      }
      while (this.mHotPromoList == null);
      localObject = this.mHotPromoList.getArray("HotPromos");
    }
    while ((localObject == null) || (localObject.length < 1));
    paramBundle = (Bundle)localObject;
    if (localObject.length > 3)
      paramBundle = (DPObject[])Arrays.copyOfRange(localObject, 0, 3);
    removeAllCells();
    Object localObject = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    ((ShopinfoCommonCell)localObject).addContent(createContentCell(paramBundle), false);
    ((ShopinfoCommonCell)localObject).setTitle(this.mHotPromoList.getString("Title") + "(" + this.mHotPromoList.getInt("Count") + ")", this.mListener);
    ((ShopinfoCommonCell)localObject).titleLay.setGAString("mallevent_more", getGAExtra());
    addCell("0600promo.01content", (View)localObject, "mallevent", 0);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.mHotPromoList = ((DPObject)paramMApiResponse.result());
    if (this.mHotPromoList == null)
      return;
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.mall.MallHotPromoAgent
 * JD-Core Version:    0.6.0
 */
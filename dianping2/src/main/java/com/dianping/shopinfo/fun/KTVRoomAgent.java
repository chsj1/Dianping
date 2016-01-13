package com.dianping.shopinfo.fun;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;

public class KTVRoomAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_KTVRoom = "0460KTV.0200KTVRoom";
  private DPObject ktvRoomData = null;
  private MApiRequest mKTVRoomDataRequest;
  protected CommonCell roomCell;

  public KTVRoomAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (getShop() == null);
    boolean bool;
    do
    {
      do
        return;
      while (this.ktvRoomData == null);
      bool = this.ktvRoomData.getBoolean("Showable");
      paramBundle = this.ktvRoomData.getString("WidgetName");
    }
    while ((!bool) || (TextUtils.isEmpty(paramBundle)) || (this.roomCell != null));
    this.roomCell = createCommonCell();
    this.roomCell.setLeftIcon(R.drawable.ktv_icon_roominfo);
    this.roomCell.setGAString("KTVRoominfo", getGAExtra());
    this.roomCell.setTitle(paramBundle);
    paramBundle = new LinearLayout.LayoutParams(-1, -2);
    paramBundle.topMargin = ViewUtils.dip2px(getContext(), 4.3F);
    this.roomCell.getTitleView().setLayoutParams(paramBundle);
    this.roomCell.setTitleLineSpacing(6.4F);
    this.roomCell.setTitleMaxLines(1);
    paramBundle = this.ktvRoomData.getString("SubTitle");
    if (!TextUtils.isEmpty(paramBundle))
      this.roomCell.setRightText(paramBundle);
    addCell("0460KTV.0200KTVRoom", this.roomCell, 257);
  }

  public void onCellClick(String paramString, View paramView)
  {
    super.onCellClick(paramString, paramView);
    try
    {
      paramString = new Intent("android.intent.action.VIEW", Uri.parse(this.ktvRoomData.getString("URL")));
      getFragment().startActivity(paramString);
      return;
    }
    catch (Exception paramString)
    {
      paramString.printStackTrace();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mKTVRoomDataRequest != null)
    {
      mapiService().abort(this.mKTVRoomDataRequest, this, true);
      this.mKTVRoomDataRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mKTVRoomDataRequest)
      this.mKTVRoomDataRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mKTVRoomDataRequest)
    {
      this.mKTVRoomDataRequest = null;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
        this.ktvRoomData = paramMApiRequest;
      dispatchAgentChanged(false);
    }
  }

  public void sendRequest()
  {
    if (this.mKTVRoomDataRequest != null)
      getFragment().mapiService().abort(this.mKTVRoomDataRequest, this, true);
    this.mKTVRoomDataRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/fun/getktvroominfo.fn").buildUpon().appendQueryParameter("shopid", String.valueOf(shopId())).toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mKTVRoomDataRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.fun.KTVRoomAgent
 * JD-Core Version:    0.6.0
 */
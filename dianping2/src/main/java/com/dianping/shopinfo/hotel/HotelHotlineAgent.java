package com.dianping.shopinfo.hotel;

import android.os.Bundle;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import org.json.JSONObject;

public class HotelHotlineAgent extends ShopCellAgent
{
  private static final String CELL_HOTLINE = "0200Basic.25Hotline";

  public HotelHotlineAgent(Object paramObject)
  {
    super(paramObject);
  }

  public CommonCell createCommonCell()
  {
    return (CommonCell)MyResources.getResource(CellAgent.class).inflate(getContext(), R.layout.hotel_mini_common_cell, getParentView(), false);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    paramBundle = getShop();
    if ((paramBundle == null) || (TextUtils.isEmpty(paramBundle.getString("HotelJson"))));
    while (true)
    {
      return;
      try
      {
        paramBundle = new JSONObject(paramBundle.getString("HotelJson"));
        if (paramBundle == null)
          continue;
        String str = paramBundle.optString("reservHotline");
        if (TextUtils.isEmpty(str))
          continue;
        Object localObject = paramBundle.optString("reservHotlineExtraText", "订房热线");
        paramBundle = (Bundle)localObject;
        if (TextUtils.isEmpty((CharSequence)localObject))
          paramBundle = "订房热线";
        localObject = createCommonCell();
        if (localObject == null)
          continue;
        ((CommonCell)localObject).setGAString("hotel_booking_hotline");
        ((CommonCell)localObject).setLeftIcon(R.drawable.detail_icon_phone);
        ((CommonCell)localObject).setTitle(str);
        ((CommonCell)localObject).setRightText(paramBundle);
        addCell("0200Basic.25Hotline", (View)localObject, 257);
        return;
      }
      catch (org.json.JSONException paramBundle)
      {
      }
    }
  }

  public void onCellClick(String paramString, View paramView)
  {
    super.onCellClick(paramString, paramView);
    if ("0200Basic.25Hotline".equals(paramString))
      TelephoneUtils.dial(getContext(), getShop(), ((CommonCell)paramView).getTitle().toString());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelHotlineAgent
 * JD-Core Version:    0.6.0
 */
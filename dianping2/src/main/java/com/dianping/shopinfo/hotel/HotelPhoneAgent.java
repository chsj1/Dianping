package com.dianping.shopinfo.hotel;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.common.PhoneAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HotelPhoneAgent extends PhoneAgent
{
  private boolean loaded;

  public HotelPhoneAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    paramBundle = getShop();
    if (paramBundle == null)
      removeAllCells();
    do
    {
      do
        return;
      while (this.loaded);
      if (!getShop().getBoolean("HotelBooking"))
        continue;
      removeAllCells();
      this.loaded = true;
      return;
    }
    while ((getShop().getBoolean("HotelBooking")) || (!isHotelType()));
    paramBundle = paramBundle.getStringArray("PhoneNos");
    StringBuffer localStringBuffer = new StringBuffer();
    if ((paramBundle != null) && (paramBundle.length > 0))
    {
      int i = 0;
      int j = paramBundle.length;
      while (i < j)
      {
        localStringBuffer.append(paramBundle[i]);
        if (i != paramBundle.length - 1)
          localStringBuffer.append("，");
        i += 1;
      }
      if (this.cell == null)
      {
        this.cell = this.res.inflate(getContext(), R.layout.shopinfo_item_hotel_disable_booking, getParentView(), false);
        NovaTextView localNovaTextView = (NovaTextView)this.cell.findViewById(R.id.tv_dail_hotel);
        localNovaTextView.setText(localStringBuffer.toString());
        localNovaTextView.setGAString("tel", getGAExtra());
        localNovaTextView.setOnClickListener(new View.OnClickListener(paramBundle)
        {
          public void onClick(View paramView)
          {
            if (this.val$phoneNos != null)
            {
              if (this.val$phoneNos.length != 1)
                break label135;
              TelephoneUtils.dial(HotelPhoneAgent.this.getContext(), HotelPhoneAgent.this.getShop(), this.val$phoneNos[0]);
              HotelPhoneAgent.this.statisticsEvent("shopinfo5", "shopinfo5_hoteltel", "", HotelPhoneAgent.this.shopId());
              if (HotelPhoneAgent.this.isWeddingType())
              {
                paramView = new ArrayList();
                paramView.add(new BasicNameValuePair("shopid", HotelPhoneAgent.this.shopId() + ""));
                HotelPhoneAgent.this.statisticsEvent("shopinfow", "shopinfow_tel", "", 0, paramView);
              }
            }
            return;
            label135: paramView = new String[this.val$phoneNos.length];
            int i = 0;
            while (i < this.val$phoneNos.length)
            {
              paramView[i] = this.val$phoneNos[i];
              i += 1;
            }
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(HotelPhoneAgent.this.getContext());
            localBuilder.setTitle("联系商户").setItems(paramView, new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface paramDialogInterface, int paramInt)
              {
                TelephoneUtils.dial(HotelPhoneAgent.this.getContext(), HotelPhoneAgent.this.getShop(), HotelPhoneAgent.1.this.val$phoneNos[paramInt]);
                if (HotelPhoneAgent.this.isWeddingType())
                {
                  paramDialogInterface = new ArrayList();
                  paramDialogInterface.add(new BasicNameValuePair("shopid", HotelPhoneAgent.this.shopId() + ""));
                  HotelPhoneAgent.this.statisticsEvent("shopinfow", "shopinfow_tel", "", 0, paramDialogInterface);
                }
                if (HotelPhoneAgent.this.isHomeDesignShopType())
                {
                  paramDialogInterface = new ArrayList();
                  paramDialogInterface.add(new BasicNameValuePair("shopid", HotelPhoneAgent.this.shopId() + ""));
                  HotelPhoneAgent.this.statisticsEvent("shopinfoh", "shopinfoh_tel", "", 1, paramDialogInterface);
                }
                if (HotelPhoneAgent.this.isHomeMarketShopType())
                {
                  paramDialogInterface = new ArrayList();
                  paramDialogInterface.add(new BasicNameValuePair("shopid", HotelPhoneAgent.this.shopId() + ""));
                  HotelPhoneAgent.this.statisticsEvent("shopinfoh", "shopinfoh_tel", "", 2, paramDialogInterface);
                }
                HotelPhoneAgent.this.statisticsEvent("shopinfo5", "shopinfo5_hoteltel", "", HotelPhoneAgent.this.shopId());
              }
            });
            localBuilder.create().show();
          }
        });
        addCell("", this.cell, 257);
      }
    }
    this.loaded = true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelPhoneAgent
 * JD-Core Version:    0.6.0
 */
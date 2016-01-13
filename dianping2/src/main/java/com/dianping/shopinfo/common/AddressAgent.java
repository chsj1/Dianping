package com.dianping.shopinfo.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.content.CityUtils;
import com.dianping.loader.MyResources;
import com.dianping.map.utils.MapUtils;
import com.dianping.model.City;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class AddressAgent extends ShopCellAgent
{
  protected static final String CELL_ADDRESS = "0200Basic.10Address";
  protected static final String CELL_ADDRESS_WEDDING_FEIHEZUO = "0250Basic.10Address";
  protected static final String CELL_ADDRESS_WEDDING_HEZUO = "0200Basic.40Address";
  protected boolean actionMap;
  protected CommonCell addressCell;

  public AddressAgent(Object paramObject)
  {
    super(paramObject);
  }

  public CommonCell createCommonCell()
  {
    return (CommonCell)MyResources.getResource(CellAgent.class).inflate(getContext(), R.layout.shop_address_cell, getParentView(), false);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    DPObject localDPObject = getShop();
    if (localDPObject == null)
      removeAllCells();
    label295: label311: label325: 
    while (true)
    {
      return;
      if (this.addressCell == null)
      {
        this.addressCell = createCommonCell();
        this.addressCell.setGAString("address", getGAExtra());
      }
      this.addressCell.setLeftIcon(R.drawable.detail_icon_locate);
      StringBuffer localStringBuffer = new StringBuffer();
      if (localDPObject.getInt("CityID") != getFragment().cityId())
      {
        paramBundle = CityUtils.getCityById(localDPObject.getInt("CityID"));
        if (paramBundle != null)
          localStringBuffer.append("(").append(paramBundle.name()).append(")");
      }
      if (TextUtils.isEmpty(localDPObject.getString("Address")))
      {
        paramBundle = "";
        localStringBuffer.append(paramBundle);
        if (!TextUtils.isEmpty(localDPObject.getString("CrossRoad")))
          localStringBuffer.append("(").append(localDPObject.getString("CrossRoad")).append(")");
        this.addressCell.setTitle(localStringBuffer.toString());
        paramBundle = localDPObject.getString("NearbyTransport");
        if (!TextUtils.isEmpty(paramBundle))
          this.addressCell.setSubTitle(paramBundle);
        this.addressCell.setTitleMaxLines(3);
        if (!TextUtils.isEmpty(localDPObject.getString("Address")))
        {
          if (!isWeddingType())
            break label311;
          paramBundle = (DPObject)getSharedObject("WeddingHotelExtra");
          if (paramBundle == null)
            continue;
          if (!paramBundle.getBoolean("Commision"))
            break label295;
          addCell("0200Basic.40Address", this.addressCell, 257);
        }
      }
      while (true)
      {
        if (!this.actionMap)
          break label325;
        this.actionMap = false;
        MapUtils.launchMap(getContext(), localDPObject);
        return;
        paramBundle = localDPObject.getString("Address");
        break;
        addCell("0250Basic.10Address", this.addressCell, 257);
        continue;
        addCell("0200Basic.10Address", this.addressCell, 257);
      }
    }
  }

  public void onCellClick(String paramString, View paramView)
  {
    super.onCellClick(paramString, paramView);
    if (("0200Basic.10Address".equals(paramString)) || ("0200Basic.40Address".equals(paramString)) || ("0250Basic.10Address".equals(paramString)))
    {
      MapUtils.gotoNavi(getContext(), getShop());
      paramString = new ArrayList();
      paramString.add(new BasicNameValuePair("shopid", shopId() + ""));
      if (!isTravelType())
        break label240;
      statisticsEvent("shopinfo5", "shopinfo5_address2", "", 0, paramString);
    }
    while (true)
    {
      if (isWeddingType())
      {
        paramString = new ArrayList();
        paramString.add(new BasicNameValuePair("shopid", shopId() + ""));
        statisticsEvent("shopinfow", "shopinfow_address", "", 0, paramString);
      }
      if (isWeddingShopType())
      {
        paramString = new ArrayList();
        paramString.add(new BasicNameValuePair("shopid", shopId() + ""));
        statisticsEvent("shopinfoq", "shopinfoq_address", "", 0, paramString);
      }
      return;
      label240: statisticsEvent("shopinfo5", "shopinfo5_address", "", 0, paramString);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null)
    {
      this.actionMap = "map".equalsIgnoreCase(getFragment().getStringParam("action"));
      return;
    }
    this.actionMap = paramBundle.getBoolean("actionMap");
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putBoolean("actionMap", this.actionMap);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.AddressAgent
 * JD-Core Version:    0.6.0
 */
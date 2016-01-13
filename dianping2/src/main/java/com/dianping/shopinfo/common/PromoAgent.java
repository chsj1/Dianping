package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class PromoAgent extends ShopCellAgent
{
  private static final String CELL_PROMO_ = "0500Cash.50Promo.";
  private static final String CELL_PROMO_1 = "0500Cash.50Promo.1";
  private static final String CELL_PROMO_Z = "0500Cash.50Promo.Z";
  CommonCell cell;
  boolean isExpand;

  public PromoAgent(Object paramObject)
  {
    super(paramObject);
  }

  public CommonCell createCommonCell()
  {
    CommonCell localCommonCell = super.createCommonCell();
    localCommonCell.setMinimumHeight((int)getResources().getDimension(R.dimen.shopinfo_common_cell_height));
    localCommonCell.setGAString("coupon", getGAExtra());
    return localCommonCell;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    Object localObject = getShop();
    if (localObject == null);
    while (true)
    {
      return;
      paramBundle = ((DPObject)localObject).getArray("Promos");
      if ((paramBundle == null) || (paramBundle.length <= 1))
        break;
      localObject = createCommonCell();
      ((CommonCell)localObject).setLeftIcon(R.drawable.detail_couponicon);
      ((CommonCell)localObject).setTitle(paramBundle[0].getString("PromoTitle"));
      ((CommonCell)localObject).setTag(paramBundle[0]);
      addCell("0500Cash.50Promo.1", (View)localObject, 257);
      if (this.isExpand)
      {
        int i = 1;
        while (i < paramBundle.length)
        {
          localObject = createCommonCell();
          ((CommonCell)localObject).setLeftIcon(R.drawable.detail_couponicon);
          ((CommonCell)localObject).setTitle(paramBundle[i].getString("PromoTitle"));
          ((CommonCell)localObject).setTag(paramBundle[i]);
          addCell("0500Cash.50Promo." + (i + 1), (View)localObject, 257);
          i += 1;
        }
        paramBundle = LayoutInflater.from(getContext()).inflate(R.layout.collapse, getParentView(), false);
        paramBundle.setTag("COLLAPSE");
        addCell("0500Cash.50Promo.Z", paramBundle, 1);
        return;
      }
      if (paramBundle.length == 2)
      {
        localObject = createCommonCell();
        ((CommonCell)localObject).setLeftIcon(R.drawable.detail_couponicon);
        ((CommonCell)localObject).setTitle(paramBundle[1].getString("PromoTitle"));
        ((CommonCell)localObject).setTag(paramBundle[1]);
        addCell("0500Cash.50Promo.2", (View)localObject, 257);
        return;
      }
      if ((paramBundle.length <= 2) || (this.isExpand))
        continue;
      localObject = LayoutInflater.from(getContext()).inflate(R.layout.expand, getParentView(), false);
      ((TextView)((View)localObject).findViewById(16908308)).setText("全部" + paramBundle.length + "条优惠");
      ((View)localObject).setTag("EXPAND");
      addCell("0500Cash.50Promo.Z", (View)localObject, 1);
      return;
    }
    paramBundle = ((DPObject)localObject).getObject("Promo");
    if (paramBundle == null)
    {
      removeAllCells();
      return;
    }
    if (this.cell == null)
      this.cell = createCommonCell();
    this.cell.setLeftIcon(R.drawable.detail_couponicon);
    this.cell.setTitle(paramBundle.getString("PromoTitle"));
    this.cell.setTag(paramBundle);
    addCell("0500Cash.50Promo.1", this.cell, 257);
  }

  public void onCellClick(String paramString, View paramView)
  {
    super.onCellClick(paramString, paramView);
    paramString = getShop();
    if (paramString == null);
    do
    {
      return;
      if ((paramView.getTag() != "EXPAND") && (paramView.getTag() != "COLLAPSE"))
        continue;
      if (!this.isExpand);
      for (boolean bool = true; ; bool = false)
      {
        this.isExpand = bool;
        dispatchAgentChanged(false);
        return;
      }
    }
    while (!(paramView.getTag() instanceof DPObject));
    paramView = (DPObject)paramView.getTag();
    int i;
    if (paramView.getInt("Flag") == 8)
    {
      i = paramView.getInt("ID");
      getFragment().startActivity("dianping://brandpromolist?id=1");
    }
    while (true)
    {
      paramString = new ArrayList();
      paramString.add(new BasicNameValuePair("shopid", shopId() + ""));
      statisticsEvent("shopinfo5", "shopinfo5_coupon", i + "", 0, paramString);
      if (isWeddingType())
      {
        paramString = new ArrayList();
        paramString.add(new BasicNameValuePair("shopid", shopId() + ""));
        statisticsEvent("shopinfow", "shopinfow_coupon", "", 0, paramString);
      }
      if (!isWeddingShopType())
        break;
      paramString = new ArrayList();
      paramString.add(new BasicNameValuePair("shopid", shopId() + ""));
      statisticsEvent("shopinfoq", "shopinfoq_coupon", "", 0, paramString);
      return;
      StringBuffer localStringBuffer = new StringBuffer("dianping://newpromoinfo?type=1&shopid=");
      localStringBuffer.append(shopId());
      i = paramView.getInt("ID");
      localStringBuffer.append("&promoid=").append(i);
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(localStringBuffer.toString()));
      paramView.putParcelableArrayListExtra("promos", new ArrayList(Arrays.asList(paramString.getArray("Promos"))));
      getFragment().startActivity(paramView);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null);
    for (boolean bool = false; ; bool = paramBundle.getBoolean("isExpand"))
    {
      this.isExpand = bool;
      return;
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putBoolean("isExpand", this.isExpand);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.PromoAgent
 * JD-Core Version:    0.6.0
 */
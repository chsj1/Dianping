package com.dianping.shopinfo.wed.home.market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.wed.home.market.widget.HomeMarketHeaderView;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HomeMarketTopAgent extends ShopCellAgent
{
  protected static final String CELL_TOP = "0200Basic.05Info";
  protected HomeMarketHeaderView topView;

  public HomeMarketTopAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    paramBundle = getShop();
    if (paramBundle == null);
    do
      return;
    while (getFragment() == null);
    this.topView = ((HomeMarketHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_home_market_panel, getParentView(), false));
    if (getSharedObject(HomeMarketShopBriefAgent.getHomeMarketBrief()) != null)
      this.topView.setHomeMarketBrief((DPObject)getSharedObject(HomeMarketShopBriefAgent.getHomeMarketBrief()));
    this.topView.setShop(paramBundle, 0);
    addCell("0200Basic.05Info", this.topView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (isHomeDesignShopType())
    {
      paramBundle = new ArrayList();
      paramBundle.add(new BasicNameValuePair("shopid", shopId() + ""));
      statisticsEvent("shopinfoh", "shopinfoh_nopaid", "1", 0, paramBundle);
    }
    while (true)
    {
      new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          UploadPhotoUtil.uploadShopPhoto(HomeMarketTopAgent.this.getContext(), HomeMarketTopAgent.this.getShop());
        }
      };
      return;
      if (!isHomeMarketShopType())
        continue;
      paramBundle = new ArrayList();
      paramBundle.add(new BasicNameValuePair("shopid", shopId() + ""));
      statisticsEvent("shopinfoh", "shopinfoh_nopaid", "2", 0, paramBundle);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.home.market.HomeMarketTopAgent
 * JD-Core Version:    0.6.0
 */
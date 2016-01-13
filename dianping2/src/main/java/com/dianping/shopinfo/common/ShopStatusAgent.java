package com.dianping.shopinfo.common;

import android.os.Bundle;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class ShopStatusAgent extends ShopCellAgent
{
  private static final String CELL_SHOP_STATUS = "0200Basic.00ShopStatus";

  public ShopStatusAgent(Object paramObject)
  {
    super(paramObject);
  }

  private NovaRelativeLayout createShopStatusView(String paramString)
  {
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.shopinfo_shopstatus, getParentView(), false);
    TextView localTextView = (TextView)localNovaRelativeLayout.findViewById(R.id.text_close);
    if ((localTextView != null) && (!TextUtils.isEmpty(paramString)))
    {
      localTextView.setText(paramString);
      return localNovaRelativeLayout;
    }
    return null;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    paramBundle = getShop();
    if ((paramBundle == null) || (getShopStatus() != 0));
    do
    {
      return;
      paramBundle = createShopStatusView(paramBundle.getString("StatusText"));
    }
    while (paramBundle == null);
    addCell("0200Basic.00ShopStatus", paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.ShopStatusAgent
 * JD-Core Version:    0.6.0
 */
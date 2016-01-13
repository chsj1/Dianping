package com.dianping.search.shoplist.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import com.dianping.base.shoplist.activity.AbstractTabListActivity;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.widget.NovaFragment;
import com.dianping.model.City;
import com.dianping.search.deallist.fragment.TuanDealListTabAgentFragment;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;

public class TabListActivity extends AbstractTabListActivity
{
  public NovaFragment[] getFragments()
    throws ClassNotFoundException, IllegalAccessException, InstantiationException
  {
    if (city().isTuan())
      return new NovaFragment[] { new ShopListAgentFragment(), new TuanDealListTabAgentFragment() };
    return new NovaFragment[] { new ShopListAgentFragment() };
  }

  public String[] getTabTitles()
  {
    if (city().isTuan())
      return new String[] { "商户", "闪惠团购" };
    return new String[] { "全部商户" };
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().setBackgroundDrawableResource(17170445);
  }

  public void onResume()
  {
    if (((getCurrentFragment() instanceof ShopListAgentFragment)) && (((ShopListAgentFragment)getCurrentFragment()).getDataSource() != null) && ((((ShopListAgentFragment)getCurrentFragment()).getDataSource().nextStartIndex() != 0) || (((ShopListAgentFragment)getCurrentFragment()).getDataSource().isEnd())))
      ((ShopListAgentFragment)getCurrentFragment()).shopListSendNewPV();
    super.onResume();
  }

  protected void onStart()
  {
    super.onStart();
    Uri localUri = getIntent().getData();
    if ((TextUtils.isEmpty(getKeyword())) || (!"shoplist".equals(localUri.getHost())) || (localUri.getQueryParameter("tab") == null));
    do
    {
      return;
      if (!localUri.getQueryParameter("tab").equals("1"))
        continue;
      showTabTitle(false);
      return;
    }
    while (!localUri.getQueryParameter("tab").equals("0"));
    showTabTitle(false);
  }

  protected void parseUrl()
  {
    super.parseUrl();
    Uri localUri = getIntent().getData();
    if ("tgshoplist".equals(localUri.getQueryParameter("target")))
      this.shareBundle.putInt("tabIndex", 1);
    if ("tgshoplist".equals(localUri.getHost()))
      this.shareBundle.putInt("tabIndex", 1);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.activity.TabListActivity
 * JD-Core Version:    0.6.0
 */
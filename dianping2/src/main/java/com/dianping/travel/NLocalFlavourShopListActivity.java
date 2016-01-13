package com.dianping.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.activity.NShopListActivity;
import com.dianping.base.shoplist.data.DefaultSearchShopListDataSource;
import com.dianping.base.widget.ThreeFilterFragment;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.model.Location;
import com.dianping.travel.data.FlavourSearchShopListDataSource;
import com.dianping.travel.fragment.FlavourDishShopFilterFragment;
import com.dianping.travel.fragment.FlavourStreetShopFilterFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NLocalFlavourShopListActivity extends NShopListActivity
{
  private String mQuery;
  private TextView mTips;

  protected DefaultSearchShopListDataSource createDataSource()
  {
    return new FlavourSearchShopListDataSource(this);
  }

  protected MApiRequest createRequest(int paramInt)
  {
    Object localObject2 = null;
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
    Object localObject1 = getIntent().getData();
    if (localObject1 != null)
      localStringBuilder.append(((Uri)localObject1).getHost() + ".bin?");
    if (this.mQuery != null)
      localStringBuilder.append(this.mQuery);
    localStringBuilder.append("&cityid=").append(cityId());
    localObject1 = location();
    if (localObject1 != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      localStringBuilder.append("&lat=").append(localDecimalFormat.format(((Location)localObject1).latitude()));
      localStringBuilder.append("&lng=").append(localDecimalFormat.format(((Location)localObject1).longitude()));
    }
    int i;
    label183: int j;
    if (this.shopListDataSource.curCategory() == null)
    {
      i = 0;
      if (i > 0)
        localStringBuilder.append("&categoryid=").append(i);
      if (this.shopListDataSource.curRegion() != null)
        break label311;
      i = 0;
      if (this.shopListDataSource.curRegion() != null)
        break label328;
      j = 0;
      label196: if (j >= 0)
        break label345;
      localStringBuilder.append("&range=").append(i);
      label214: if (this.shopListDataSource.curFilterId() != null)
        break label366;
      localObject1 = null;
      label226: if (localObject1 != null)
        localStringBuilder.append("&filterid=").append((String)localObject1);
      if (this.shopListDataSource.curRange() != null)
        break label382;
    }
    label311: label328: label345: label366: label382: for (localObject1 = localObject2; ; localObject1 = this.shopListDataSource.curRange().getString("ID"))
    {
      if (localObject1 != null)
        localStringBuilder.append("&range=").append((String)localObject1);
      localStringBuilder.append("&start=").append(paramInt);
      return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
      i = this.shopListDataSource.curCategory().getInt("ID");
      break;
      i = this.shopListDataSource.curRegion().getInt("ID");
      break label183;
      j = this.shopListDataSource.curRegion().getInt("ParentID");
      break label196;
      if (i <= 0)
        break label214;
      localStringBuilder.append("&regionid=").append(i);
      break label214;
      localObject1 = this.shopListDataSource.curFilterId().getString("ID");
      break label226;
    }
  }

  public void generateFilterFragment()
  {
    Uri localUri = getIntent().getData();
    if (localUri != null)
    {
      if (!"searchflavourshop".equals(localUri.getHost()))
        break label111;
      if (!Boolean.valueOf(localUri.getQueryParameter("istagshop")).booleanValue())
        break label88;
      this.filterFragment = new ThreeFilterFragment();
      this.shopListDataSource.setGATag("travel_taglist");
    }
    while (true)
    {
      if (this.filterFragment != null)
        getSupportFragmentManager().beginTransaction().add(R.id.content, this.filterFragment).commit();
      return;
      label88: this.filterFragment = new FlavourDishShopFilterFragment();
      this.shopListDataSource.setGATag("travel_dishlist");
      continue;
      label111: this.filterFragment = new FlavourStreetShopFilterFragment();
      this.shopListDataSource.setGATag("travel_streetlist");
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getData();
    int j;
    int i;
    if (paramBundle != null)
    {
      this.mQuery = paramBundle.getQuery();
      if (this.mQuery != null)
      {
        paramBundle = this.mQuery.split("&");
        j = paramBundle.length;
        i = 0;
      }
    }
    while (true)
    {
      if (i < j)
      {
        CharSequence localCharSequence = paramBundle[i];
        if (localCharSequence.contains("category"))
          this.mQuery = this.mQuery.replace(localCharSequence, "");
      }
      else
      {
        this.shopListDataSource.setEmptyMsg("没有找到任何商户\n\n请输入正确的商地址或分类");
        return;
      }
      i += 1;
    }
  }

  public void onDataChanged(int paramInt)
  {
    super.onDataChanged(paramInt);
    if (!TextUtils.isEmpty(((FlavourSearchShopListDataSource)this.shopListDataSource).tips()))
    {
      this.mTips.setText(((FlavourSearchShopListDataSource)this.shopListDataSource).tips());
      this.mTips.setVisibility(0);
    }
  }

  protected void setupView()
  {
    super.setContentView(R.layout.flavour_shop_list);
    this.mTips = ((TextView)findViewById(R.id.tips));
  }

  protected void updateTitle(boolean paramBoolean)
  {
    super.setTitle(getIntent().getData().getQueryParameter("name"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.NLocalFlavourShopListActivity
 * JD-Core Version:    0.6.0
 */
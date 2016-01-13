package com.dianping.hotel.deal.activity;

import android.content.Intent;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.hotel.deal.fragment.HotelProdPackageSelectFragment;
import java.util.ArrayList;

public class HotelProdPackageSelectActivity extends NovaActivity
{
  private HotelProdPackageSelectFragment hotelProdPackageSelectFragment;

  protected boolean needTitleBarShadow()
  {
    return false;
  }

  public void onBackPressed()
  {
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getParcelableArrayExtra("hotelProdPackages");
    ArrayList localArrayList = new ArrayList();
    if (paramBundle != null)
    {
      int i = 0;
      while (i < paramBundle.length)
      {
        if ((paramBundle[i] instanceof DPObject))
          localArrayList.add((DPObject)paramBundle[i]);
        i += 1;
      }
    }
    if (localArrayList.isEmpty())
    {
      finish();
      return;
    }
    this.hotelProdPackageSelectFragment = HotelProdPackageSelectFragment.newInstance(this, localArrayList);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.activity.HotelProdPackageSelectActivity
 * JD-Core Version:    0.6.0
 */
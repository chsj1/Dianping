package com.dianping.ugc.photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.ScreenSlidePagerAdapter;
import java.util.ArrayList;

public class ShowPhotoAndMoreActivity extends ShowPhotoActivity
{
  public static final String TAG = "ShowPhotoAndMoreActivity";
  private String catetoryTag;
  private boolean isFirstTimeEntry = true;
  public int totalPicCount = 0;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null)
    {
      this.totalPicCount = getIntent().getIntExtra("totalPicCount", 0);
      this.catetoryTag = getIntent().getStringExtra("catetoryTag");
      return;
    }
    this.totalPicCount = paramBundle.getInt("totalPicCount", 0);
    this.catetoryTag = paramBundle.getString("catetoryTag");
  }

  public void onPageSelected(int paramInt)
  {
    super.onPageSelected(paramInt);
    int i = currentPageIndex();
    if ((this.isFirstTimeEntry) && (i == paramInt))
      this.isFirstTimeEntry = false;
    do
    {
      return;
      if (this.isFirstTimeEntry)
        this.isFirstTimeEntry = false;
      paramInt = ((DPObject)this.shopPhotos.get(currentPage())).getInt("ShopID");
      if (!"hotel".equals(this.catetoryTag))
        continue;
      statisticsEvent("hotelphoto5", "hotelphoto5_slide", String.valueOf(paramInt), 0);
      return;
    }
    while (!"beauty".equals(this.catetoryTag));
    statisticsEvent("beautyphoto5", "beautyphoto5_slide", String.valueOf(paramInt), 0);
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.totalPicCount = paramBundle.getInt("totalPicCount");
    this.catetoryTag = paramBundle.getString("catetoryTag");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("totalPicCount", this.totalPicCount);
    paramBundle.putString("catetoryTag", this.catetoryTag);
  }

  protected PagerAdapter pagerAdapter()
  {
    return new ScreenSlidePagerAdapter(getSupportFragmentManager(), this.currentPage, this.pageList, this.currentBitmap, ShowPhotoAndMoreFragment.class, "showPhotoAndMore");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.ShowPhotoAndMoreActivity
 * JD-Core Version:    0.6.0
 */
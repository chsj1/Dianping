package com.dianping.base.basic;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.dianping.archive.DPObject;
import java.util.ArrayList;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
{
  private Class clazz;
  private Bitmap currentBitmap;
  private int currentPage;
  private ArrayList<DPObject> pageList;
  private String tag;

  public ScreenSlidePagerAdapter(FragmentManager paramFragmentManager)
  {
    super(paramFragmentManager);
  }

  public ScreenSlidePagerAdapter(FragmentManager paramFragmentManager, int paramInt, ArrayList<DPObject> paramArrayList, Bitmap paramBitmap, Class paramClass, String paramString)
  {
    super(paramFragmentManager);
    this.currentPage = paramInt;
    this.pageList = paramArrayList;
    this.currentBitmap = paramBitmap;
    this.tag = paramString;
    this.clazz = paramClass;
  }

  public int getCount()
  {
    return this.pageList.size();
  }

  public Fragment getItem(int paramInt)
  {
    if ((paramInt == this.currentPage) && ((this.tag.equals("beauty")) || (this.tag.equals("hotel")) || ("showPhotoAndMore".equals(this.tag))))
      return ScreenSlidePageFragmentFactory.createScreenSlidePageFragment((DPObject)this.pageList.get(paramInt), paramInt, this.currentBitmap, this.clazz, this.tag);
    if (paramInt == this.currentPage)
      return ScreenSlidePageFragmentFactory.createScreenSlidePageFragment((DPObject)this.pageList.get(paramInt), this.currentBitmap, this.clazz, this.tag);
    if ((this.tag.equals("beauty")) || (this.tag.equals("hotel")) || ("showPhotoAndMore".equals(this.tag)))
      return ScreenSlidePageFragmentFactory.createScreenSlidePageFragment((DPObject)this.pageList.get(paramInt), paramInt, null, this.clazz, this.tag);
    return ScreenSlidePageFragmentFactory.createScreenSlidePageFragment((DPObject)this.pageList.get(paramInt), null, this.clazz, this.tag);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.ScreenSlidePagerAdapter
 * JD-Core Version:    0.6.0
 */
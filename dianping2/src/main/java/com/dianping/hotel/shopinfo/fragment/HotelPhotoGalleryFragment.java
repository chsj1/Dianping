package com.dianping.hotel.shopinfo.fragment;

import android.content.Context;
import com.dianping.base.ugc.photo.ShopPhotoGalleryFragment;
import com.dianping.base.ugc.photo.ShopPhotoGalleryFragment.PhotoAdapter;
import com.dianping.v1.R.layout;

public class HotelPhotoGalleryFragment extends ShopPhotoGalleryFragment
{
  protected ShopPhotoGalleryFragment.PhotoAdapter getPhotoAdapter()
  {
    return new HotelPhotoAdapter(getActivity());
  }

  public class HotelPhotoAdapter extends ShopPhotoGalleryFragment.PhotoAdapter
  {
    public HotelPhotoAdapter(Context arg2)
    {
      super(localContext);
    }

    protected int getShopPhotoLayout()
    {
      return R.layout.hotel_photo;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shopinfo.fragment.HotelPhotoGalleryFragment
 * JD-Core Version:    0.6.0
 */
package com.dianping.ugc.photo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.base.widget.ShopPhotoItem;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class ShowPhotoAndMoreFragment extends ShowPhotoFragment
{
  private View.OnClickListener allPhotoEntranceListener(ShowPhotoAndMoreActivity paramShowPhotoAndMoreActivity)
  {
    return new ShowPhotoAndMoreFragment.2(this, paramShowPhotoAndMoreActivity);
  }

  protected ShopPhotoItem inflaterShopPhotoItem(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    return (ShopPhotoItem)paramLayoutInflater.inflate(R.layout.common_show_shopandmore_item, paramViewGroup, false);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    paramViewGroup = (ShowPhotoAndMoreActivity)getActivity();
    paramLayoutInflater.findViewById(R.id.common_photo_page_back).setOnClickListener(new ShowPhotoAndMoreFragment.1(this));
    int i = getArguments().getInt("position");
    if (paramViewGroup.totalPicCount > 0)
      ((TextView)paramLayoutInflater.findViewById(R.id.common_photo_page_count)).setText(String.format("%s/%s", new Object[] { Integer.valueOf(i + 1), Integer.valueOf(paramViewGroup.totalPicCount) }));
    if (i == paramViewGroup.shopPhotos.size() - 1)
    {
      paramBundle = paramLayoutInflater.findViewById(R.id.common_last_photo_entrance);
      paramBundle.setVisibility(0);
      paramBundle.setOnClickListener(allPhotoEntranceListener(paramViewGroup));
      ((TextView)paramBundle.findViewById(R.id.common_last_photo_entrance_pic_count)).setText(String.format("查看全部%s张", new Object[] { Integer.valueOf(paramViewGroup.totalPicCount) }));
    }
    paramLayoutInflater.findViewById(R.id.common_all_photo_btn).setOnClickListener(allPhotoEntranceListener(paramViewGroup));
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.ShowPhotoAndMoreFragment
 * JD-Core Version:    0.6.0
 */
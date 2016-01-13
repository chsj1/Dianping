package com.dianping.ugc.photo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.ScreenSlidePageFragment;
import com.dianping.base.widget.loading.LoadingLayout;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ShowLargePhotoFragement extends ScreenSlidePageFragment
{
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.large_photo_item, paramViewGroup, false);
    paramViewGroup = (LoadingLayout)paramLayoutInflater.findViewById(R.id.loadinglayout);
    paramViewGroup.creatLoadingLayout(this.isBackground, true, true);
    paramViewGroup.setImageUrl(this.pageObj.getString("Url"));
    paramLayoutInflater.findViewById(R.id.download_button).setOnClickListener(new ShowLargePhotoFragement.1(this, paramLayoutInflater));
    if (this.isBackground)
      paramViewGroup.setLoadingBackgruond(this.bitmap);
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.ShowLargePhotoFragement
 * JD-Core Version:    0.6.0
 */
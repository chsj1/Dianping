package com.dianping.shopinfo.market.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.ScreenSlidePageFragment;
import com.dianping.base.util.ImageUtils;
import com.dianping.base.widget.loading.LoadingLayout;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;

public class MarketPosterSlidePageFragment extends ScreenSlidePageFragment
{
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.shopinfo_market_poster_photo_item, paramViewGroup, false);
    paramViewGroup = (LoadingLayout)paramLayoutInflater.findViewById(R.id.loadinglayout);
    paramViewGroup.creatLoadingLayout(this.isBackground, true, true);
    paramViewGroup.setImageUrl(this.pageObj.getString("Url"));
    paramBundle = (TextView)paramLayoutInflater.findViewById(R.id.time);
    if ((paramBundle != null) && (!TextUtils.isEmpty(this.pageObj.getString("OnlineTime"))))
    {
      paramBundle.setText(this.pageObj.getString("OnlineTime"));
      paramBundle.setVisibility(0);
    }
    paramLayoutInflater.findViewById(R.id.download_button).setOnClickListener(new View.OnClickListener(paramLayoutInflater)
    {
      public void onClick(View paramView)
      {
        ImageUtils.savePhotoToAlbum((NetworkImageView)(NetworkImageView)this.val$view.findViewById(R.id.photo), this.val$view.getContext());
      }
    });
    if (this.isBackground)
      paramViewGroup.setLoadingBackgruond(this.bitmap);
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.market.activity.MarketPosterSlidePageFragment
 * JD-Core Version:    0.6.0
 */
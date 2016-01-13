package com.dianping.main.find.pictureplaza;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.ScreenSlidePageFragment;
import com.dianping.base.util.ImageUtils;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;

public class PlazaSlidePageFragment extends ScreenSlidePageFragment
{
  private boolean isTagShowing = true;
  PoiLargeImageView poiLargeImageView;
  private NovaTextView tagSwitchButton;

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.plaza_photo_item, paramViewGroup, false);
    paramLayoutInflater.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (PlazaSlidePageFragment.this.getActivity() != null)
          PlazaSlidePageFragment.this.getActivity().finish();
      }
    });
    this.poiLargeImageView = ((PoiLargeImageView)paramLayoutInflater.findViewById(R.id.poi_large_image));
    if (this.pageObj != null)
      this.poiLargeImageView.setPoiLargeImageSource(this.pageObj.getObject("source"), 0, this.pageObj.getInt("feedid") + "", this.pageObj.getBoolean("istopicpage"), this.pageObj.getInt("topicid"));
    this.poiLargeImageView.setPoiImageListener(new PoiLargeImageView.PoiImageListener()
    {
      public void onLargeImageClick(int paramInt, Drawable paramDrawable)
      {
        if (PlazaSlidePageFragment.this.getActivity() != null)
          PlazaSlidePageFragment.this.getActivity().finish();
      }

      public void onPoiClick(int paramInt)
      {
        if ((PlazaSlidePageFragment.this.pageObj != null) && (PlazaSlidePageFragment.this.pageObj.getObject("source") != null))
        {
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + PlazaSlidePageFragment.this.pageObj.getObject("source").getInt("ShopId")));
          PlazaSlidePageFragment.this.startActivity(localIntent);
        }
      }
    });
    this.tagSwitchButton = ((NovaTextView)paramLayoutInflater.findViewById(R.id.show_tag));
    this.tagSwitchButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (PlazaSlidePageFragment.this.isTagShowing)
        {
          PlazaSlidePageFragment.access$302(PlazaSlidePageFragment.this, false);
          PlazaSlidePageFragment.this.tagSwitchButton.setText("显示标签");
          PlazaSlidePageFragment.this.poiLargeImageView.hideAllDotMarkView();
          return;
        }
        PlazaSlidePageFragment.access$302(PlazaSlidePageFragment.this, true);
        PlazaSlidePageFragment.this.tagSwitchButton.setText("隐藏标签");
        PlazaSlidePageFragment.this.poiLargeImageView.showAllDotMarkView();
      }
    });
    paramLayoutInflater.findViewById(R.id.download_pic).setOnClickListener(new View.OnClickListener(paramLayoutInflater)
    {
      public void onClick(View paramView)
      {
        ImageUtils.savePhotoToAlbum((DPNetworkImageView)(DPNetworkImageView)this.val$view.findViewById(16908294), this.val$view.getContext());
      }
    });
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaSlidePageFragment
 * JD-Core Version:    0.6.0
 */
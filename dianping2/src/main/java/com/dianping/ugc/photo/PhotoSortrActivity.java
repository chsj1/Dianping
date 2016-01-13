package com.dianping.ugc.photo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.ZoomNetworkImageView;
import com.dianping.widget.ZoomNetworkImageView.OnLoadImageListener;

public class PhotoSortrActivity extends NovaActivity
{
  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Object localObject = getIntent();
    if (((Intent)localObject).getData() != null)
      paramBundle = ((Intent)localObject).getData().getQueryParameter("url");
    for (localObject = ((Intent)localObject).getData().getQueryParameter("name"); paramBundle == null; localObject = ((Intent)localObject).getStringExtra("name"))
    {
      finish();
      return;
      paramBundle = ((Intent)localObject).getStringExtra("url");
    }
    super.setTitle((CharSequence)localObject);
    super.setContentView(R.layout.photo_sortr);
    localObject = (ZoomNetworkImageView)findViewById(R.id.photo);
    ((ZoomNetworkImageView)localObject).placeholderEmpty = R.drawable.placeholder_empty;
    ((ZoomNetworkImageView)localObject).placeholderEmpty = R.drawable.placeholder_empty;
    ((ZoomNetworkImageView)localObject).placeholderLoading = R.drawable.placeholder_loading;
    ((ZoomNetworkImageView)localObject).setImage(paramBundle);
    ((ZoomNetworkImageView)localObject).setImageZoomable(true);
    ((ZoomNetworkImageView)localObject).setOnLoadImageListener(new ZoomNetworkImageView.OnLoadImageListener((ZoomNetworkImageView)localObject)
    {
      public void onLoadFailed()
      {
        PhotoSortrActivity.this.getTitleBar().removeRightViewItem("save");
      }

      public void onLoadSuccess()
      {
        PhotoSortrActivity.this.getTitleBar().addRightViewItem("save", R.drawable.ic_btn_download_photo, new PhotoSortrActivity.1.1(this));
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.PhotoSortrActivity
 * JD-Core Version:    0.6.0
 */
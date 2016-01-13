package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.dianping.v1.R.layout;

public class ImageCachedUserProfileItem extends UserProfileItem
{
  public ImageCachedUserProfileItem(Context paramContext)
  {
    super(paramContext, null);
  }

  public ImageCachedUserProfileItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected View setupView(Context paramContext)
  {
    return LayoutInflater.from(paramContext).inflate(R.layout.image_cached_userprofile_item, this, true);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ImageCachedUserProfileItem
 * JD-Core Version:    0.6.0
 */
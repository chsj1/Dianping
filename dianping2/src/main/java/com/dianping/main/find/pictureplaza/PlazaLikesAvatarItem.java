package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.dianping.base.widget.CircleImageView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaFrameLayout;

public class PlazaLikesAvatarItem extends NovaFrameLayout
{
  View mCover;
  CircleImageView mUserAvatarImage;

  public PlazaLikesAvatarItem(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaLikesAvatarItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mUserAvatarImage = ((CircleImageView)findViewById(R.id.user_avatar_image));
    this.mCover = findViewById(R.id.cover);
  }

  public void setAvatar(String paramString)
  {
    this.mUserAvatarImage.setImage(paramString);
  }

  public void setCoverVisibility(int paramInt)
  {
    this.mCover.setVisibility(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaLikesAvatarItem
 * JD-Core Version:    0.6.0
 */
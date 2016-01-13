package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.dianping.base.widget.CircleImageView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class PlazaStarUserAvatarItem extends NovaLinearLayout
{
  View mCover;
  CircleImageView mUserAvatarImage;
  TextView mUserNameText;

  public PlazaStarUserAvatarItem(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaStarUserAvatarItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mUserAvatarImage = ((CircleImageView)findViewById(R.id.user_avatar_image));
    this.mUserNameText = ((TextView)findViewById(R.id.user_nick_name));
    this.mCover = findViewById(R.id.cover);
  }

  public void setAvatar(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      this.mUserAvatarImage.setVisibility(4);
      return;
    }
    this.mUserAvatarImage.setVisibility(0);
    this.mUserAvatarImage.setImage(paramString);
  }

  public void setCoverVisibility(int paramInt)
  {
    this.mCover.setVisibility(paramInt);
  }

  public void setNickName(String paramString)
  {
    this.mUserNameText.setVisibility(0);
    this.mUserNameText.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaStarUserAvatarItem
 * JD-Core Version:    0.6.0
 */
package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CircleImageView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaRelativeLayout;

public class PlazaStarUserItem extends NovaRelativeLayout
{
  TextView mItemContentText;
  int[] mRankImageResources = { R.drawable.plaza_star_user_top1, R.drawable.plaza_star_user_top2, R.drawable.plaza_star_user_top3 };
  CircleImageView mUserAvatarImage;
  TextView mUserNameText;
  ImageView mUserRankImage;
  TextView mUserRankText;
  LinearLayout mUserTagLayout;

  public PlazaStarUserItem(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaStarUserItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mUserRankImage = ((ImageView)findViewById(R.id.user_rank_image));
    this.mUserRankText = ((TextView)findViewById(R.id.user_rank_text));
    this.mUserAvatarImage = ((CircleImageView)findViewById(R.id.user_avatar_image));
    this.mUserNameText = ((TextView)findViewById(R.id.user_name));
    this.mUserTagLayout = ((LinearLayout)findViewById(R.id.user_tag_layout));
    this.mItemContentText = ((TextView)findViewById(R.id.item_content));
  }

  public void setStarUserData(DPObject paramDPObject, int paramInt)
  {
    if (paramInt < 3)
    {
      this.mUserRankImage.setVisibility(0);
      this.mUserRankImage.setImageResource(this.mRankImageResources[paramInt]);
      this.mUserRankText.setVisibility(4);
      paramInt = paramDPObject.getInt("FeedCount");
      if (paramInt < 0)
        break label189;
      this.mItemContentText.setText("发布了" + paramInt + "条内容");
    }
    while (true)
    {
      paramDPObject = paramDPObject.getObject("User");
      if (paramDPObject != null)
      {
        String str = paramDPObject.getString("Avatar");
        this.mUserAvatarImage.setImage(str);
        str = paramDPObject.getString("Nick");
        this.mUserNameText.setText(str);
        paramInt = paramDPObject.getInt("UserID");
        if (paramInt != 0)
          setOnClickListener(new View.OnClickListener(paramInt)
          {
            public void onClick(View paramView)
            {
              paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user").buildUpon().appendQueryParameter("userid", String.valueOf(this.val$userId)).build());
              PlazaStarUserItem.this.getContext().startActivity(paramView);
            }
          });
      }
      return;
      this.mUserRankImage.setVisibility(4);
      this.mUserRankText.setVisibility(0);
      this.mUserRankText.setText(paramInt + 1 + "");
      break;
      label189: this.mItemContentText.setText("");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaStarUserItem
 * JD-Core Version:    0.6.0
 */
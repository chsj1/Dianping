package com.dianping.main.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CircleImageView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaRelativeLayout;

public class FriendInfoItem extends NovaRelativeLayout
{
  private NovaButton followButton;
  private View followedButton;
  private OnFollowListener onFollowListener;
  private CircleImageView userIconView;
  private TextView userInfoView;
  private TextView userNameView;

  public FriendInfoItem(Context paramContext)
  {
    super(paramContext);
  }

  public FriendInfoItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.userIconView = ((CircleImageView)findViewById(R.id.icon));
    this.userNameView = ((TextView)findViewById(R.id.user_name));
    this.userInfoView = ((TextView)findViewById(R.id.user_info));
    this.followButton = ((NovaButton)findViewById(R.id.do_follow));
    this.followedButton = findViewById(R.id.followed);
  }

  public void setFollowedStatus()
  {
    this.followButton.setVisibility(8);
    this.followedButton.setVisibility(0);
  }

  public void setOnFollowListener(OnFollowListener paramOnFollowListener)
  {
    this.onFollowListener = paramOnFollowListener;
  }

  public void setUser(DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    if (paramDPObject != null)
    {
      this.userIconView.setImage(paramDPObject.getString("UserFace"));
      this.userNameView.setText(paramDPObject.getString("NickName"));
      this.userInfoView.setText(paramDPObject.getInt("FeedCount") + "条点评 " + paramDPObject.getInt("FanCount") + "位粉丝");
      GAUserInfo localGAUserInfo = new GAUserInfo();
      localGAUserInfo.biz_id = String.valueOf(paramDPObject.getInt("UserId"));
      this.followButton.gaUserInfo = localGAUserInfo;
      if ((getContext() instanceof DPActivity))
        ((DPActivity)getContext()).addGAView(this.followButton, paramInt2);
      if (paramDPObject.getInt("UserId") != paramInt1)
        break label174;
      this.followedButton.setVisibility(8);
      this.followButton.setVisibility(8);
    }
    while (true)
    {
      setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user"));
          paramView.putExtra("userId", this.val$user.getInt("UserId"));
          FriendInfoItem.this.getContext().startActivity(paramView);
        }
      });
      return;
      label174: if (paramDPObject.getBoolean("IsFollow"))
      {
        this.followedButton.setVisibility(0);
        this.followButton.setVisibility(8);
        continue;
      }
      this.followedButton.setVisibility(8);
      this.followButton.setVisibility(0);
      this.followButton.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          if (FriendInfoItem.this.onFollowListener != null)
            FriendInfoItem.this.onFollowListener.onFollowClickListener(FriendInfoItem.this, this.val$user.getInt("UserId"));
        }
      });
    }
  }

  public static abstract interface OnFollowListener
  {
    public abstract void onFollowClickListener(FriendInfoItem paramFriendInfoItem, int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.FriendInfoItem
 * JD-Core Version:    0.6.0
 */
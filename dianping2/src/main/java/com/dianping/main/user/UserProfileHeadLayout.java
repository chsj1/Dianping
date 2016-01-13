package com.dianping.main.user;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CircleImageView;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaTextView;

public class UserProfileHeadLayout extends LinearLayout
  implements View.OnClickListener, DialogInterface.OnDismissListener
{
  private boolean mFlagFollowed;
  private boolean mIsMyFan;
  private CircleImageView mIvAvatar;
  private NetworkImageView mIvLevel;
  private ImageView mIvMore;
  private int mMyUserId;
  private NovaTextView mTVEditProfile;
  private NovaTextView mTvFan;
  private NovaTextView mTvFollow;
  private NovaTextView mTvHoney;
  private NovaTextView mTvSendMsg;
  private TextView mTvUserName;
  private DPObject mUser;
  private OnFollowListener onFollowListener;
  private OnFollowedListener onFollowedListener;
  private onMessageListener onMessageListener;

  public UserProfileHeadLayout(Context paramContext)
  {
    super(paramContext);
  }

  public UserProfileHeadLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.do_follow_top)
      if (this.mFlagFollowed)
        if (this.onFollowedListener != null)
          this.onFollowedListener.onFollowedClickListener();
    do
      while (true)
      {
        do
          return;
        while (this.onFollowListener == null);
        this.onFollowListener.onFollowClickListener();
        return;
        if ((i == R.id.edit) || (i == R.id.edit_photo))
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://editprofile"));
          getContext().startActivity(paramView);
          return;
        }
        if (i != R.id.msg)
          break;
        if (this.onMessageListener == null)
          continue;
        this.onMessageListener.onMessageClickListener();
        return;
      }
    while (i != R.id.iv_more);
    paramView = new ActionMoreDialog(getContext(), this.mUser);
    paramView.setOnDismissListener(this);
    paramView.show();
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    setUser(this.mUser, this.mMyUserId);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mIvAvatar = ((CircleImageView)findViewById(R.id.edit_photo));
    this.mTvUserName = ((TextView)findViewById(R.id.tv_name));
    this.mTvSendMsg = ((NovaTextView)findViewById(R.id.msg));
    this.mTvSendMsg.setOnClickListener(this);
    this.mTvFollow = ((NovaTextView)findViewById(R.id.do_follow_top));
    this.mTvFollow.setOnClickListener(this);
    this.mTvSendMsg.setOnClickListener(this);
    this.mTvFan = ((NovaTextView)findViewById(R.id.follower_profile));
    this.mTvHoney = ((NovaTextView)findViewById(R.id.following_profile));
    this.mTVEditProfile = ((NovaTextView)findViewById(R.id.edit));
    this.mTVEditProfile.setOnClickListener(this);
    this.mIvLevel = ((NetworkImageView)findViewById(R.id.iv_level));
    this.mTvFan.setOnClickListener(this);
    this.mTvHoney.setOnClickListener(this);
    this.mIvMore = ((ImageView)findViewById(R.id.iv_more));
    this.mIvMore.setOnClickListener(this);
  }

  public void setFansCount(int paramInt)
  {
    this.mTvFan.setText("粉丝 " + paramInt);
  }

  public void setFollowCount(int paramInt)
  {
    this.mTvHoney.setText("关注 " + paramInt);
  }

  public void setFollowStatus()
  {
    this.mFlagFollowed = false;
    this.mTvFollow.setText("加关注");
  }

  public void setFollowedStatus()
  {
    this.mFlagFollowed = true;
    if (!this.mIsMyFan)
    {
      this.mTvFollow.setText("已关注");
      return;
    }
    this.mTvFollow.setText("互相关注");
  }

  public void setOnFollowListener(OnFollowListener paramOnFollowListener)
  {
    this.onFollowListener = paramOnFollowListener;
  }

  public void setOnFollowedListener(OnFollowedListener paramOnFollowedListener)
  {
    this.onFollowedListener = paramOnFollowedListener;
  }

  public void setOnMessageListener(onMessageListener paramonMessageListener)
  {
    this.onMessageListener = paramonMessageListener;
  }

  public void setUser(DPObject paramDPObject, int paramInt)
  {
    if (paramDPObject != null)
    {
      this.mUser = paramDPObject;
      this.mMyUserId = paramInt;
      this.mIvAvatar.setImage(paramDPObject.getString("Avatar"));
      this.mTvUserName.setText(paramDPObject.getString("Nick"));
      GAUserInfo localGAUserInfo = new GAUserInfo();
      localGAUserInfo.biz_id = String.valueOf(paramDPObject.getInt("UserID"));
      this.mTvFollow.setGAString("do_follow_top", localGAUserInfo);
      this.mTvSendMsg.gaUserInfo = localGAUserInfo;
      this.mIvLevel.setImage(paramDPObject.getString("UserLevel"));
      if (paramDPObject.getInt("UserID") != paramInt)
        break label257;
      this.mTvFollow.setVisibility(8);
      this.mTvSendMsg.setVisibility(8);
      this.mIvAvatar.setOnClickListener(this);
      if ((getContext() instanceof DPActivity))
      {
        ((DPActivity)getContext()).addGAView(this.mIvAvatar, -1);
        ((DPActivity)getContext()).addGAView(this.mTVEditProfile, -1);
        ((DPActivity)getContext()).addGAView(this.mTvFan, -1);
        ((DPActivity)getContext()).addGAView(this.mTvHoney, -1);
      }
    }
    label257: label307: label468: 
    while (true)
    {
      this.mTvFan.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$user.getString("FansUrl");
          if (!TextUtils.isEmpty(paramView))
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
          try
          {
            UserProfileHeadLayout.this.getContext().startActivity(paramView);
            return;
          }
          catch (Exception paramView)
          {
            paramView.printStackTrace();
          }
        }
      });
      this.mTvHoney.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$user.getString("FollowsUrl");
          if (!TextUtils.isEmpty(paramView))
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
          try
          {
            UserProfileHeadLayout.this.getContext().startActivity(paramView);
            return;
          }
          catch (Exception paramView)
          {
            paramView.printStackTrace();
          }
        }
      });
      setFansCount(paramDPObject.getInt("FansCount"));
      setFollowCount(paramDPObject.getInt("FollowsCount"));
      return;
      this.mIsMyFan = paramDPObject.getBoolean("IsFans");
      this.mTvFollow.setVisibility(0);
      this.mTvSendMsg.setVisibility(0);
      this.mTVEditProfile.setVisibility(8);
      if (paramDPObject.getBoolean("IsFollow"))
      {
        setFollowedStatus();
        if (!paramDPObject.getBoolean("IsBeBlacked"))
          break label397;
        this.mTvFollow.setVisibility(8);
        this.mTvSendMsg.setVisibility(8);
        this.mTvHoney.setVisibility(8);
        this.mTvFan.setVisibility(8);
        this.mTvFollow.setVisibility(8);
      }
      while (true)
      {
        if (!(getContext() instanceof DPActivity))
          break label468;
        ((DPActivity)getContext()).addGAView(this.mIvAvatar, -1);
        break;
        setFollowStatus();
        break label307;
        if (!(getContext() instanceof DPActivity))
          continue;
        ((DPActivity)getContext()).addGAView(this.mTvFan, -1);
        ((DPActivity)getContext()).addGAView(this.mTvHoney, -1);
        ((DPActivity)getContext()).addGAView(this.mTvFollow, -1);
        ((DPActivity)getContext()).addGAView(this.mTvSendMsg, -1);
      }
    }
  }

  public static abstract interface OnFollowListener
  {
    public abstract void onFollowClickListener();
  }

  public static abstract interface OnFollowedListener
  {
    public abstract void onFollowedClickListener();
  }

  public static abstract interface onMessageListener
  {
    public abstract void onMessageClickListener();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.UserProfileHeadLayout
 * JD-Core Version:    0.6.0
 */
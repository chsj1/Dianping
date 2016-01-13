package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.id;

public class MyDPFriendItem extends LinearLayout
{
  Button button;
  TextView city;
  int position;
  TextView text1;
  public View text2;
  NetworkThumbView thumb;
  UserProfile user;
  UserPowers userPower;

  public MyDPFriendItem(Context paramContext)
  {
    super(paramContext);
  }

  public MyDPFriendItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.thumb = ((NetworkThumbView)findViewById(16908294));
    this.text1 = ((TextView)findViewById(16908308));
    this.text2 = findViewById(16908309);
    this.city = ((TextView)findViewById(R.id.city));
    this.userPower = ((UserPowers)findViewById(R.id.user_power));
    this.button = ((Button)findViewById(R.id.btn));
  }

  public int position()
  {
    return this.position;
  }

  public void resetStatus()
  {
    this.button.setVisibility(0);
    this.text2.setVisibility(8);
  }

  public void setButtonClickListener(View.OnClickListener paramOnClickListener)
  {
    this.button.setOnClickListener(new View.OnClickListener(paramOnClickListener)
    {
      public void onClick(View paramView)
      {
        if (this.val$listener != null)
          this.val$listener.onClick(MyDPFriendItem.this);
      }
    });
  }

  public void setButtonOnClickListener(View.OnClickListener paramOnClickListener)
  {
    this.button.setOnClickListener(new View.OnClickListener(paramOnClickListener)
    {
      public void onClick(View paramView)
      {
        if (this.val$listener != null)
          this.val$listener.onClick(MyDPFriendItem.this);
      }
    });
  }

  public void setButtonText(String paramString)
  {
    this.button.setText(paramString);
  }

  public void setButtonVisibility(int paramInt)
  {
    this.button.setVisibility(paramInt);
  }

  public void setCity(String paramString)
  {
    if (this.city != null)
      this.city.setText(paramString);
  }

  public void setMyHoney(UserProfile paramUserProfile)
  {
    Object localObject2 = null;
    this.user = paramUserProfile;
    if (paramUserProfile == null)
    {
      this.thumb.setImage(null);
      this.text1.setText(null);
      this.city.setText(null);
      this.userPower.setPower(0);
      return;
    }
    NetworkThumbView localNetworkThumbView = this.thumb;
    Object localObject1 = localObject2;
    if (paramUserProfile.avatar() != null)
      if (paramUserProfile.avatar().length() != 0)
        break label100;
    label100: for (localObject1 = localObject2; ; localObject1 = paramUserProfile.avatar())
    {
      localNetworkThumbView.setImage((String)localObject1);
      this.text1.setText(paramUserProfile.nickName());
      this.userPower.setPower(paramUserProfile.userPower());
      return;
    }
  }

  public void setMyHoney(UserProfile paramUserProfile, int paramInt, Integer paramInteger)
  {
    setMyHoney(paramUserProfile);
    this.position = paramInt;
    if (paramUserProfile != null)
      setStatus(paramInteger);
  }

  public void setSnsHoney(UserProfile paramUserProfile, int paramInt)
  {
    Object localObject2 = null;
    this.user = paramUserProfile;
    this.position = paramInt;
    if (paramUserProfile == null)
    {
      this.thumb.setImage(null);
      this.text1.setText(null);
      this.city.setText(null);
      this.userPower.setPower(0);
      return;
    }
    NetworkThumbView localNetworkThumbView = this.thumb;
    Object localObject1 = localObject2;
    if (paramUserProfile.avatar() != null)
      if (paramUserProfile.avatar().length() != 0)
        break label137;
    label137: for (localObject1 = localObject2; ; localObject1 = paramUserProfile.avatar())
    {
      localNetworkThumbView.setImage((String)localObject1);
      this.text1.setText(paramUserProfile.nickName());
      this.userPower.setPower(paramUserProfile.userPower());
      this.city.setText("微博昵称：" + paramUserProfile.snsNickName());
      return;
    }
  }

  public void setStatus(Integer paramInteger)
  {
    int j = 8;
    Integer localInteger = paramInteger;
    if (paramInteger == null)
      localInteger = Integer.valueOf(0);
    paramInteger = this.button;
    if (localInteger.intValue() == 0)
    {
      i = 0;
      paramInteger.setVisibility(i);
      paramInteger = this.text2;
      if (localInteger.intValue() != 0)
        break label61;
    }
    label61: for (int i = j; ; i = 0)
    {
      paramInteger.setVisibility(i);
      return;
      i = 8;
      break;
    }
  }

  public UserProfile user()
  {
    return this.user;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.MyDPFriendItem
 * JD-Core Version:    0.6.0
 */
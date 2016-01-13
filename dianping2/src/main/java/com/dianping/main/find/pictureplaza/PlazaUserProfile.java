package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.AutoTruncateLinearLayout;
import com.dianping.base.widget.CircleImageView;
import com.dianping.base.widget.ShopPower;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaFrameLayout;
import com.dianping.widget.view.NovaImageView;

public class PlazaUserProfile extends NovaFrameLayout
{
  private AutoTruncateLinearLayout containerView;
  private View dividerLine;
  private CircleImageView headImageview;
  private ShopPower judgePowerView;
  private NetworkImageView levelImage;
  private TextView nameTextView;
  private TextView timeTextView;
  private NovaImageView topImageView;
  private int userId;

  public PlazaUserProfile(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaUserProfile(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private NetworkImageView createExpertView(String paramString)
  {
    NetworkImageView localNetworkImageView = new NetworkImageView(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 54.0F), ViewUtils.dip2px(getContext(), 18.0F));
    localLayoutParams.setMargins(0, 0, ViewUtils.dip2px(getContext(), 5.0F), 0);
    localNetworkImageView.setLayoutParams(localLayoutParams);
    localNetworkImageView.setImage(paramString);
    localNetworkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    return localNetworkImageView;
  }

  public void addExpertContent(String[] paramArrayOfString)
  {
    int i;
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      if (this.containerView.getChildCount() == paramArrayOfString.length)
      {
        i = 0;
        while (i < this.containerView.getChildCount())
        {
          View localView = this.containerView.getChildAt(i);
          if ((localView instanceof NetworkImageView))
            ((NetworkImageView)localView).setImage(paramArrayOfString[i]);
          i += 1;
        }
      }
      this.containerView.removeAllViews();
      i = 0;
    }
    while (i < paramArrayOfString.length)
    {
      if (!TextUtils.isEmpty(paramArrayOfString[i]))
        this.containerView.addView(createExpertView(paramArrayOfString[i]));
      i += 1;
      continue;
      this.containerView.removeAllViews();
    }
  }

  public int getUserId()
  {
    return this.userId;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.nameTextView = ((TextView)findViewById(16908308));
    this.headImageview = ((CircleImageView)findViewById(R.id.plaza_user_pic));
    this.timeTextView = ((TextView)findViewById(16908309));
    this.containerView = ((AutoTruncateLinearLayout)findViewById(R.id.expert_container));
    this.dividerLine = findViewById(16908320);
    this.topImageView = ((NovaImageView)findViewById(R.id.top_image));
    this.levelImage = ((NetworkImageView)findViewById(R.id.level_image));
    this.judgePowerView = ((ShopPower)findViewById(R.id.judge_power));
  }

  public void setHeader(String paramString)
  {
    if (this.headImageview != null)
    {
      this.headImageview.setImage(paramString);
      return;
    }
    this.headImageview.setImageResource(R.drawable.profile_default);
  }

  public void setNick(String paramString)
  {
    if (this.nameTextView != null)
    {
      this.nameTextView.setText(paramString);
      return;
    }
    this.nameTextView.setText("");
  }

  public void setPlazaUserInfo(DPObject paramDPObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString)
  {
    setPlazaUserInfo(paramDPObject, paramString, paramInt, paramBoolean1, false, paramBoolean2, paramBoolean3, true);
  }

  public void setPlazaUserInfo(DPObject paramDPObject, String paramString, int paramInt, boolean paramBoolean)
  {
    setPlazaUserInfo(paramDPObject, paramString, paramInt, paramBoolean, false, false, false, false);
  }

  public void setPlazaUserInfo(DPObject paramDPObject, String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
  {
    if (paramDPObject == null)
      return;
    setNick(paramDPObject.getString("Nick"));
    setHeader(paramDPObject.getString("Avatar"));
    setPublicTime(paramString);
    if (!paramBoolean5)
    {
      paramString = paramDPObject.getString("UserLevel");
      if (!TextUtils.isEmpty(paramString))
      {
        this.levelImage.setVisibility(0);
        this.levelImage.setImage(paramString);
        addExpertContent(paramDPObject.getStringArray("UserTags"));
      }
    }
    while (true)
    {
      setUserId(paramDPObject.getInt("UserID"));
      setTopSignImage(paramBoolean2, paramBoolean3, paramBoolean4);
      if (!paramBoolean1)
        break label141;
      this.judgePowerView.setVisibility(0);
      this.judgePowerView.setPower(paramInt);
      return;
      this.levelImage.setVisibility(8);
      break;
      this.levelImage.setVisibility(8);
    }
    label141: this.judgePowerView.setVisibility(8);
  }

  public void setPlazaUserInfo(DPObject paramDPObject, String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    setPlazaUserInfo(paramDPObject, paramString, 0, false, paramBoolean1, paramBoolean2, false, false);
  }

  public void setPlazaUserWithGrayLine(DPObject paramDPObject, String paramString, boolean paramBoolean)
  {
    setPlazaUserInfo(paramDPObject, paramString, false, false);
    if (paramBoolean)
    {
      this.dividerLine.setVisibility(0);
      return;
    }
    this.dividerLine.setVisibility(8);
  }

  public void setPublicTime(String paramString)
  {
    if (this.timeTextView != null)
    {
      this.timeTextView.setText(paramString);
      return;
    }
    this.timeTextView.setText("");
  }

  public void setTopSignImage(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    this.topImageView.setBackgroundResource(0);
    if (paramBoolean3)
    {
      this.topImageView.setVisibility(0);
      this.topImageView.setBackgroundResource(R.drawable.icon_goodreview);
      return;
    }
    if (paramBoolean1)
    {
      this.topImageView.setVisibility(0);
      this.topImageView.setBackgroundResource(R.drawable.icon_top);
      return;
    }
    if (paramBoolean2)
    {
      this.topImageView.setVisibility(0);
      this.topImageView.setBackgroundResource(R.drawable.icon_essence);
      return;
    }
    this.topImageView.setVisibility(8);
  }

  public void setUserId(int paramInt)
  {
    this.userId = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaUserProfile
 * JD-Core Version:    0.6.0
 */
package com.dianping.main.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;

public class UserProLayout extends LinearLayout
{
  private LinearLayout userProContainer;
  private DPObject[] userPros;

  public UserProLayout(Context paramContext)
  {
    super(paramContext);
  }

  public UserProLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.userProContainer = ((LinearLayout)findViewById(R.id.user_pro_container));
  }

  public void setUserPro(DPObject[] paramArrayOfDPObject, boolean paramBoolean)
  {
    if (paramArrayOfDPObject == this.userPros);
    do
    {
      return;
      this.userPros = paramArrayOfDPObject;
    }
    while ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length <= 0));
    this.userProContainer.removeAllViews();
    int i = 0;
    label33: NovaLinearLayout localNovaLinearLayout;
    if (i < paramArrayOfDPObject.length)
    {
      localObject = paramArrayOfDPObject[i];
      localNovaLinearLayout = (NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.user_pro_tag, this.userProContainer, false);
      ((NetworkImageView)localNovaLinearLayout.findViewById(R.id.user_pro_icon)).setImage(((DPObject)localObject).getString("Pic"));
      ((TextView)localNovaLinearLayout.findViewById(R.id.user_pro_title)).setText(((DPObject)localObject).getString("Title"));
      localNovaLinearLayout.setOnClickListener(new View.OnClickListener((DPObject)localObject)
      {
        public void onClick(View paramView)
        {
          if (!TextUtils.isEmpty(this.val$userPro.getString("Url")))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$userPro.getString("Url")));
            UserProLayout.this.getContext().startActivity(paramView);
          }
        }
      });
      if (!paramBoolean)
        break label177;
    }
    label177: for (Object localObject = "badge_mine"; ; localObject = "badge_other")
    {
      localNovaLinearLayout.setGAString((String)localObject);
      if ((getContext() instanceof DPActivity))
        ((DPActivity)getContext()).addGAView(localNovaLinearLayout, i);
      this.userProContainer.addView(localNovaLinearLayout);
      i += 1;
      break label33;
      break;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.UserProLayout
 * JD-Core Version:    0.6.0
 */
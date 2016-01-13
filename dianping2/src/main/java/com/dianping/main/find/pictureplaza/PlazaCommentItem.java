package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.AutoTruncateLinearLayout;
import com.dianping.base.widget.CircleImageView;
import com.dianping.base.widget.RichTextView;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;

public class PlazaCommentItem extends NovaRelativeLayout
{
  RichTextView mItemContentText;
  TextView mTimeText;
  CircleImageView mUserAvatarImage;
  NovaTextView mUserNameText;
  AutoTruncateLinearLayout mUserTagLayout;

  public PlazaCommentItem(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaCommentItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mUserAvatarImage = ((CircleImageView)findViewById(R.id.user_avatar_image));
    this.mUserNameText = ((NovaTextView)findViewById(R.id.user_name));
    this.mUserTagLayout = ((AutoTruncateLinearLayout)findViewById(R.id.user_tag_layout));
    this.mTimeText = ((TextView)findViewById(R.id.time_text));
    this.mItemContentText = ((RichTextView)findViewById(R.id.item_content));
  }

  public void setCommentData(DPObject paramDPObject)
  {
    String str = paramDPObject.getString("Content");
    this.mItemContentText.setRichText(str);
    str = paramDPObject.getString("Time");
    this.mTimeText.setText(str);
    paramDPObject = paramDPObject.getObject("User");
    if (paramDPObject != null)
      setUserData(paramDPObject);
  }

  public void setUserData(DPObject paramDPObject)
  {
    Object localObject1 = paramDPObject.getString("Avatar");
    this.mUserAvatarImage.setImage((String)localObject1);
    localObject1 = paramDPObject.getStringArray("UserTags");
    Object localObject2;
    if ((localObject1 != null) && (localObject1.length > 0))
    {
      if (this.mUserTagLayout.getChildCount() == localObject1.length)
      {
        i = 0;
        while (i < this.mUserTagLayout.getChildCount())
        {
          localObject2 = this.mUserTagLayout.getChildAt(i);
          if ((localObject2 instanceof NetworkImageView))
            ((NetworkImageView)localObject2).setImage(localObject1[i]);
          i += 1;
        }
      }
      this.mUserTagLayout.removeAllViews();
      i = 0;
    }
    while (i < localObject1.length)
    {
      if (!TextUtils.isEmpty(localObject1[i]))
      {
        localObject2 = new NetworkImageView(getContext());
        ((NetworkImageView)localObject2).setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.dip2px(getContext(), 54.0F), ViewUtils.dip2px(getContext(), 18.0F)));
        ((NetworkImageView)localObject2).setPadding(0, 0, ViewUtils.dip2px(getContext(), 5.0F), 0);
        ((NetworkImageView)localObject2).setImage(localObject1[i]);
        ((NetworkImageView)localObject2).setScaleType(ImageView.ScaleType.FIT_XY);
        this.mUserTagLayout.addView((View)localObject2);
      }
      i += 1;
      continue;
      this.mUserTagLayout.removeAllViews();
    }
    localObject1 = paramDPObject.getString("Nick");
    this.mUserNameText.setText((CharSequence)localObject1);
    int i = paramDPObject.getInt("UserID");
    if (i != 0)
    {
      paramDPObject = new View.OnClickListener(i)
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user").buildUpon().appendQueryParameter("userid", String.valueOf(this.val$userId)).build());
          PlazaCommentItem.this.getContext().startActivity(paramView);
        }
      };
      this.mUserAvatarImage.setOnClickListener(paramDPObject);
      this.mUserAvatarImage.setGAString("comments_profile");
      this.mUserNameText.setOnClickListener(paramDPObject);
      this.mUserNameText.setGAString("comments_profile");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaCommentItem
 * JD-Core Version:    0.6.0
 */
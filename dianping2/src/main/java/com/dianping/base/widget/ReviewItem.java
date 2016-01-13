package com.dianping.base.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;

public class ReviewItem extends NovaLinearLayout
{
  private Context mContext;
  private String[] mImagesArray;
  private LinearLayout mLayPhtotosContainer;
  private String[] mThumbnailsArray;
  private DPObject review;
  TextView user;

  public ReviewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }

  private void addPhotoView()
  {
    String[] arrayOfString = this.mThumbnailsArray;
    if (arrayOfString == null);
    do
      return;
    while (arrayOfString.length == 0);
    int j = ViewUtils.getScreenWidthPixels(getContext());
    if (j <= 480);
    for (int i = 3; ; i = 4)
    {
      j = (j - ViewUtils.dip2px(getContext(), 80.0F) - ViewUtils.dip2px(getContext(), 35.0F)) / i;
      int k = Math.min(i, arrayOfString.length);
      this.mLayPhtotosContainer.removeAllViews();
      i = 0;
      while (i < k)
      {
        NetworkImageView localNetworkImageView = (NetworkImageView)LayoutInflater.from(getContext()).inflate(R.layout.review_item_photo, this.mLayPhtotosContainer, false);
        localNetworkImageView.getLayoutParams().width = j;
        localNetworkImageView.getLayoutParams().height = j;
        localNetworkImageView.setImage(arrayOfString[i]);
        localNetworkImageView.setTag(Integer.valueOf(i));
        localNetworkImageView.setClickable(true);
        if (this.review.getBoolean("isFriend"))
          localNetworkImageView.setGAString("viewfriends_photos");
        this.mLayPhtotosContainer.addView(localNetworkImageView);
        localNetworkImageView.setOnClickListener(new View.OnClickListener(i)
        {
          public void onClick(View paramView)
          {
            paramView = new ArrayList();
            if (ReviewItem.this.mImagesArray == null);
            Object localObject;
            do
            {
              return;
              localObject = ReviewItem.this.mImagesArray;
              int j = localObject.length;
              int i = 0;
              while (i < j)
              {
                String str = localObject[i];
                paramView.add(new DPObject().edit().putString("Url", str).generate());
                i += 1;
              }
              localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showcheckinphoto"));
              ((Intent)localObject).putExtra("position", this.val$index);
              ((Intent)localObject).putParcelableArrayListExtra("pageList", paramView);
            }
            while (!(ReviewItem.this.mContext instanceof Activity));
            ((Activity)ReviewItem.this.getContext()).startActivity((Intent)localObject);
          }
        });
        i += 1;
      }
      break;
    }
  }

  private void setUserProfile()
  {
    AddViewContainer localAddViewContainer = (AddViewContainer)findViewById(R.id.user_icon_container);
    Object localObject4 = (NetworkImageView)findViewById(R.id.review_honor);
    Object localObject3 = (NetworkImageView)findViewById(R.id.shopinfo_user_level);
    Object localObject1 = this.review.getObject("User");
    Object localObject2 = this.review.getString("ReviewHonour");
    if ((localObject4 != null) && (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject2)))
    {
      ((NetworkImageView)localObject4).setVisibility(0);
      ((NetworkImageView)localObject4).setImage((String)localObject2);
    }
    if (localAddViewContainer == null);
    do
    {
      do
      {
        do
          return;
        while (localObject1 == null);
        localObject4 = ((DPObject)localObject1).getObject("UserGrade");
        if ((localObject4 != null) && (localObject3 != null))
        {
          localObject4 = ((DPObject)localObject4).getString("Image");
          if (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject4))
          {
            ((NetworkImageView)localObject3).setVisibility(0);
            ((NetworkImageView)localObject3).setImage((String)localObject4);
          }
        }
        localObject3 = ((DPObject)localObject1).getStringArray("UserTags");
        if ((localObject3 != null) && (localObject3.length > 0))
        {
          if (com.dianping.util.TextUtils.isEmpty((CharSequence)localObject2));
          for (int i = ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 15.0F) * 2 - ViewUtils.dip2px(getContext(), 65.0F) - ViewUtils.getViewWidth(this.user); ; i = ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 15.0F) * 2 - ViewUtils.dip2px(getContext(), 65.0F) - ViewUtils.getViewWidth(this.user) - ViewUtils.dip2px(getContext(), 45.0F))
          {
            localAddViewContainer.setAvailableWidth(i);
            localObject1 = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 50.0F), ViewUtils.dip2px(getContext(), 18.0F));
            ((LinearLayout.LayoutParams)localObject1).leftMargin = ViewUtils.dip2px(getContext(), 5.0F);
            int j = localObject3.length;
            i = 0;
            while (i < j)
            {
              localObject2 = localObject3[i];
              localObject4 = new NetworkImageView(getContext());
              ((NetworkImageView)localObject4).setLayoutParams((ViewGroup.LayoutParams)localObject1);
              ((NetworkImageView)localObject4).setImage((String)localObject2);
              ((NetworkImageView)localObject4).setScaleType(ImageView.ScaleType.FIT_XY);
              localAddViewContainer.addView((View)localObject4);
              i += 1;
            }
          }
          localAddViewContainer.setVisibility(0);
          return;
        }
        localObject1 = ((DPObject)localObject1).getObject("UserLevel");
      }
      while (localObject1 == null);
      localObject1 = ((DPObject)localObject1).getString("Pic");
    }
    while (com.dianping.util.TextUtils.isEmpty((CharSequence)localObject1));
    localObject2 = new NetworkImageView(getContext());
    localObject3 = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 20.0F), ViewUtils.dip2px(getContext(), 12.0F));
    ((LinearLayout.LayoutParams)localObject3).leftMargin = ViewUtils.dip2px(getContext(), 5.0F);
    ((NetworkImageView)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject3);
    ((NetworkImageView)localObject2).setImage((String)localObject1);
    ((NetworkImageView)localObject2).setScaleType(ImageView.ScaleType.FIT_XY);
    localAddViewContainer.addView((View)localObject2);
    localAddViewContainer.setVisibility(0);
  }

  public void setReview(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.review = paramDPObject;
    Object localObject = paramDPObject.getObject("User");
    if (localObject != null)
    {
      ((CircleImageView)findViewById(R.id.user_icon)).setImage(((DPObject)localObject).getString("Avatar"));
      this.user = ((TextView)findViewById(R.id.user));
      this.user.setText(((DPObject)localObject).getString("NickName"));
    }
    if (paramDPObject.getInt("SourceType") == 1)
    {
      localObject = paramDPObject.getString("SourceName");
      if (!android.text.TextUtils.isEmpty((CharSequence)localObject))
      {
        TextView localTextView = (TextView)findViewById(R.id.review_source);
        if (localTextView != null)
          localTextView.setText((CharSequence)localObject);
      }
    }
    localObject = (ShopPower)findViewById(R.id.shop_power);
    if (paramDPObject.getInt("Star") > 0)
    {
      ((ShopPower)localObject).setVisibility(0);
      ((ShopPower)localObject).setPower(paramDPObject.getInt("Star"));
      localObject = (TextView)findViewById(R.id.price);
      if (android.text.TextUtils.isEmpty(paramDPObject.getString("PriceText")))
        break label223;
      ((TextView)localObject).setText(paramDPObject.getString("PriceText"));
    }
    while (true)
    {
      ((TextView)findViewById(R.id.review)).setText(paramDPObject.getString("ReviewBody"));
      setUserPhotos();
      setUserProfile();
      return;
      ((ShopPower)localObject).setVisibility(8);
      break;
      label223: if (paramDPObject.getInt("AvgPrice") > 0)
      {
        ((TextView)localObject).setText("￥" + String.valueOf(paramDPObject.getInt("AvgPrice")) + "/人");
        continue;
      }
      ((TextView)localObject).setText("");
    }
  }

  public void setReviewCount(int paramInt)
  {
    ((TextView)findViewById(16908308)).setText("点评（共" + paramInt + "条）：");
  }

  public void setReviewCountVisible(int paramInt)
  {
    ((TextView)findViewById(16908308)).setVisibility(paramInt);
  }

  public void setUserPhotos()
  {
    this.mLayPhtotosContainer = ((LinearLayout)findViewById(R.id.review_photos_container_lay));
    if (this.mLayPhtotosContainer == null)
      return;
    this.mThumbnailsArray = this.review.getStringArray("Thumbnails");
    this.mImagesArray = this.review.getStringArray("Images");
    if ((this.mThumbnailsArray == null) || (this.mImagesArray == null))
    {
      this.mLayPhtotosContainer.setVisibility(8);
      return;
    }
    if (this.mThumbnailsArray.length > 0)
    {
      this.mLayPhtotosContainer.setVisibility(0);
      addPhotoView();
      return;
    }
    this.mLayPhtotosContainer.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ReviewItem
 * JD-Core Version:    0.6.0
 */
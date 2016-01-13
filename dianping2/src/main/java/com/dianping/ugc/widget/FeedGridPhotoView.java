package com.dianping.ugc.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class FeedGridPhotoView extends LinearLayout
  implements View.OnClickListener
{
  private static final int DEFAULT_COLUMN_COUNT = 3;
  private static final int DEFAULT_MAX_PHOTO_COUNT = 9;
  private static final int DEFAULT_WIDTH = ViewUtils.getScreenWidthPixels(DPApplication.instance()) - ViewUtils.dip2px(DPApplication.instance(), 75.0F);
  private static final int PHOTO_PADDING = ViewUtils.dip2px(DPApplication.instance(), 6.0F);
  private int mColumnCount = 3;
  private int mMaxPhotoCount = 9;
  private FeedGridPhotoView.OnPhotoClickListener mOnPhotoClickListener;
  private Style mStyle = Style.NORMAL;
  private int mTotalWidth = DEFAULT_WIDTH;
  private ArrayList<DPObject> mUrls = new ArrayList();

  public FeedGridPhotoView(Context paramContext)
  {
    super(paramContext);
  }

  public FeedGridPhotoView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    Object localObject = paramView.getTag();
    int i;
    if (((localObject instanceof Integer)) && ((paramView instanceof DPNetworkImageView)))
    {
      i = ((Integer)localObject).intValue();
      localObject = ((BitmapDrawable)((DPNetworkImageView)paramView).getDrawable()).getBitmap();
      if (this.mOnPhotoClickListener != null)
        this.mOnPhotoClickListener.onPhotoClick(i, (Bitmap)localObject);
    }
    else
    {
      return;
    }
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showlargephoto"));
    paramView.putParcelableArrayListExtra("pageList", this.mUrls);
    paramView.putExtra("position", i);
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      ((Bitmap)localObject).compress(Bitmap.CompressFormat.JPEG, 90, localByteArrayOutputStream);
      paramView.putExtra("currentbitmap", localByteArrayOutputStream.toByteArray());
      localByteArrayOutputStream.close();
      getContext().startActivity(paramView);
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public void setMaxPhotoCount(int paramInt)
  {
    this.mMaxPhotoCount = paramInt;
  }

  public void setOnPhotoClickListener(FeedGridPhotoView.OnPhotoClickListener paramOnPhotoClickListener)
  {
    this.mOnPhotoClickListener = paramOnPhotoClickListener;
  }

  public void setPhotos(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    removeAllViews();
    if ((paramArrayOfString1 == null) || (paramArrayOfString2 == null) || (paramArrayOfString1.length != paramArrayOfString2.length))
      setVisibility(8);
    int n;
    int i;
    int m;
    int j;
    label307: 
    do
    {
      return;
      n = paramArrayOfString1.length;
      if (n == 0)
      {
        setVisibility(8);
        return;
      }
      setVisibility(0);
      k = n;
      this.mUrls.clear();
      i = 0;
      while (i < k)
      {
        this.mUrls.add(new DPObject().edit().putString("Url", paramArrayOfString2[i]).generate());
        i += 1;
      }
      m = (int)(this.mTotalWidth * 0.278D);
      if (this.mStyle == Style.SQUARED)
        if (k == 4)
        {
          this.mColumnCount = 2;
          i = k;
          j = m;
          if (k == 1)
          {
            j = m * 2 + PHOTO_PADDING;
            i = k;
          }
        }
      while (true)
      {
        if (i != 1)
          break label307;
        paramArrayOfString2 = new DPNetworkImageView(getContext());
        paramArrayOfString2.placeholderLoading = R.drawable.placeholder_loading;
        paramArrayOfString2.placeholderEmpty = R.drawable.placeholder_empty;
        paramArrayOfString2.placeholderError = R.drawable.placeholder_error;
        paramArrayOfString2.setImage(paramArrayOfString1[0]);
        paramArrayOfString2.setTag(Integer.valueOf(0));
        paramArrayOfString2.setOnClickListener(this);
        paramArrayOfString2.setGAString("photo");
        paramArrayOfString2.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        paramArrayOfString2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        paramArrayOfString2.setImageSize(j, j);
        addView(paramArrayOfString2);
        return;
        this.mColumnCount = 3;
        break;
        this.mColumnCount = 3;
        i = Math.min(this.mMaxPhotoCount, k);
        j = m;
      }
    }
    while (i <= 1);
    int i1 = ((i - 1) / this.mColumnCount + 1) * this.mColumnCount;
    int i2 = this.mColumnCount;
    paramArrayOfString2 = null;
    int k = 0;
    label342: Object localObject1;
    LinearLayout.LayoutParams localLayoutParams;
    if (k < i)
    {
      if (k % this.mColumnCount == 0)
      {
        paramArrayOfString2 = new LinearLayout(getContext());
        paramArrayOfString2.setOrientation(0);
        addView(paramArrayOfString2, new LinearLayout.LayoutParams(-2, -2));
      }
      localObject1 = new DPNetworkImageView(getContext());
      ((DPNetworkImageView)localObject1).setGAString("photo");
      ((DPNetworkImageView)localObject1).placeholderLoading = R.drawable.placeholder_loading;
      ((DPNetworkImageView)localObject1).placeholderEmpty = R.drawable.placeholder_empty;
      ((DPNetworkImageView)localObject1).placeholderError = R.drawable.placeholder_error;
      ((DPNetworkImageView)localObject1).setImage(paramArrayOfString1[k]);
      ((DPNetworkImageView)localObject1).setTag(Integer.valueOf(k));
      ((DPNetworkImageView)localObject1).setOnClickListener(this);
      localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
      int i3 = PHOTO_PADDING;
      if ((i1 - i2 > k) || (i1 - 1 < k))
        break label775;
      m = 0;
      label502: localLayoutParams.setMargins(0, 0, i3, m);
      if ((k != i - 1) || (i >= n) || (this.mStyle != Style.NORMAL))
        break label783;
      FrameLayout localFrameLayout = new FrameLayout(getContext());
      Object localObject2 = new FrameLayout.LayoutParams(-2, -2);
      ((DPNetworkImageView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      ((DPNetworkImageView)localObject1).setScaleType(ImageView.ScaleType.CENTER_CROP);
      ((DPNetworkImageView)localObject1).setImageSize(j, j);
      localFrameLayout.addView((View)localObject1, (ViewGroup.LayoutParams)localObject2);
      localObject1 = new FrameLayout.LayoutParams(-2, -2);
      ((FrameLayout.LayoutParams)localObject1).gravity = 85;
      ((FrameLayout.LayoutParams)localObject1).bottomMargin = ViewUtils.dip2px(getContext(), 3.0F);
      ((FrameLayout.LayoutParams)localObject1).rightMargin = ViewUtils.dip2px(getContext(), 3.0F);
      localObject2 = new TextView(getContext());
      ((TextView)localObject2).setText(String.valueOf(n));
      ((TextView)localObject2).setBackgroundResource(R.drawable.ugc_review_bg_more_photos);
      ((TextView)localObject2).setTextSize(0, getContext().getResources().getDimensionPixelSize(R.dimen.text_size_11));
      ((TextView)localObject2).setTextColor(getContext().getResources().getColor(R.color.light_gray));
      ((TextView)localObject2).setPadding(ViewUtils.dip2px(getContext(), 10.0F), 0, ViewUtils.dip2px(getContext(), 10.0F), 0);
      localFrameLayout.addView((View)localObject2, (ViewGroup.LayoutParams)localObject1);
      localLayoutParams.gravity = 48;
      paramArrayOfString2.addView(localFrameLayout, localLayoutParams);
    }
    while (true)
    {
      k += 1;
      break label342;
      break;
      label775: m = PHOTO_PADDING;
      break label502;
      label783: ((DPNetworkImageView)localObject1).setLayoutParams(localLayoutParams);
      ((DPNetworkImageView)localObject1).setScaleType(ImageView.ScaleType.CENTER_CROP);
      ((DPNetworkImageView)localObject1).setImageSize(j, j);
      paramArrayOfString2.addView((View)localObject1, localLayoutParams);
    }
  }

  public void setStyle(Style paramStyle)
  {
    this.mStyle = paramStyle;
  }

  public void setWidth(int paramInt)
  {
    this.mTotalWidth = paramInt;
  }

  public static enum Style
  {
    static
    {
      $VALUES = new Style[] { NORMAL, SQUARED };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.widget.FeedGridPhotoView
 * JD-Core Version:    0.6.0
 */
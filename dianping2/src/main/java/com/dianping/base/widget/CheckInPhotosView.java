package com.dianping.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.styleable;
import com.dianping.widget.CheckinListItemListener;
import com.dianping.widget.NetworkImageView;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CheckInPhotosView extends LinearLayout
  implements View.OnClickListener
{
  Context context;
  LinearLayout curLine = null;
  ArrayList<NetworkImageView> imageViewArrayList = new ArrayList();
  boolean isSingleBigMode;
  ArrayList<LinearLayout> linearLayoutArrayList = new ArrayList();
  CheckinListItemListener photoListener;
  NetworkImageView singleImage;
  String[] urlBig;
  String[] urlSmall;

  public CheckInPhotosView(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
    setOrientation(1);
  }

  public CheckInPhotosView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
    setOrientation(1);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CheckInPhotosView);
    try
    {
      this.isSingleBigMode = paramContext.getBoolean(R.styleable.CheckInPhotosView_isSingleBigMode, false);
      return;
    }
    catch (java.lang.Exception paramAttributeSet)
    {
      Log.e("CheckinPhotosView init error when get attrs!");
      return;
    }
    finally
    {
      paramContext.recycle();
    }
    throw paramAttributeSet;
  }

  public CheckInPhotosView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet);
  }

  private void addLinearLayout()
  {
    LinearLayout localLinearLayout = new LinearLayout(this.context);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
    localLinearLayout.setOrientation(0);
    if (this.curLine != null)
      addSpace();
    this.curLine = localLinearLayout;
    addView(this.curLine, localLayoutParams);
    this.linearLayoutArrayList.add(localLinearLayout);
  }

  private void addSpace()
  {
    int i = 0;
    while (i < 3 - this.curLine.getChildCount())
    {
      LinearLayout localLinearLayout = new LinearLayout(this.context);
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2, 1.0F);
      this.curLine.addView(localLinearLayout, localLayoutParams);
      i += 1;
    }
  }

  public void onClick(View paramView)
  {
    int i = ((Integer)paramView.getTag()).intValue();
    if ((this.photoListener != null) && (this.urlSmall != null) && (this.urlBig != null) && (this.urlSmall.length == this.urlBig.length))
      if ((this.urlSmall.length != 1) || (!this.isSingleBigMode))
        break label117;
    label117: for (paramView = ((BitmapDrawable)this.singleImage.getDrawable()).getBitmap(); ; paramView = ((BitmapDrawable)((NetworkImageView)this.imageViewArrayList.get(i)).getDrawable()).getBitmap())
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      paramView.compress(Bitmap.CompressFormat.JPEG, 90, localByteArrayOutputStream);
      this.photoListener.onTouchPhoto(this.urlBig, this.urlSmall, i, localByteArrayOutputStream.toByteArray());
      return;
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int j = View.MeasureSpec.getSize(paramInt1);
    int i = ViewUtils.dip2px(this.context, 3.0F);
    j = j / 3 - i;
    Iterator localIterator = this.imageViewArrayList.iterator();
    while (localIterator.hasNext())
    {
      NetworkImageView localNetworkImageView = (NetworkImageView)localIterator.next();
      localNetworkImageView.getLayoutParams().width = j;
      localNetworkImageView.getLayoutParams().height = j;
    }
    super.onMeasure(paramInt1, paramInt2);
    if (this.imageViewArrayList.size() > 0)
    {
      paramInt1 = 1;
      while (paramInt1 < this.linearLayoutArrayList.size())
      {
        ((LinearLayout.LayoutParams)((LinearLayout)this.linearLayoutArrayList.get(paramInt1)).getLayoutParams()).setMargins(0, i, 0, 0);
        paramInt1 += 1;
      }
    }
  }

  public void setImageUrls(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    if ((paramArrayOfString1 == null) || (paramArrayOfString2 == null));
    do
    {
      do
      {
        return;
        if (paramArrayOfString1.length == paramArrayOfString2.length)
          continue;
        Log.e("setImageUrls(String[] urlSmall, String[] urlBig) the two array's length must equal");
        return;
      }
      while ((Arrays.equals(paramArrayOfString1, this.urlSmall)) && (Arrays.equals(paramArrayOfString2, this.urlBig)));
      this.urlSmall = paramArrayOfString1;
      this.urlBig = paramArrayOfString2;
      removeAllViews();
      this.imageViewArrayList.clear();
      this.linearLayoutArrayList.clear();
      int i = 0;
      while (i < paramArrayOfString1.length)
      {
        Object localObject1;
        if ((paramArrayOfString1.length == 1) && (this.isSingleBigMode))
        {
          this.singleImage = new NetworkImageView(this.context);
          localObject1 = new LinearLayout.LayoutParams(-2, -2);
          this.singleImage.setMinimumHeight(ViewUtils.dip2px(this.context, 100.0F));
          this.singleImage.setMinimumWidth(ViewUtils.dip2px(this.context, 100.0F));
          this.singleImage.placeholderLoading = R.drawable.placeholder_loading;
          this.singleImage.placeholderEmpty = R.drawable.placeholder_empty;
          this.singleImage.placeholderError = R.drawable.placeholder_error;
          this.singleImage.setFocusable(false);
          this.singleImage.setTag(Integer.valueOf(0));
          this.singleImage.setImage(paramArrayOfString2[0]);
          this.singleImage.setOnClickListener(this);
          addView(this.singleImage, (ViewGroup.LayoutParams)localObject1);
          i += 1;
          continue;
        }
        if (paramArrayOfString1.length == 4)
          if ((i == 0) || (i == 2))
            addLinearLayout();
        while (true)
        {
          localObject1 = new LinearLayout(this.context);
          Object localObject2 = new LinearLayout.LayoutParams(-1, -2, 1.0F);
          ((LinearLayout)localObject1).setGravity(17);
          this.curLine.addView((View)localObject1, (ViewGroup.LayoutParams)localObject2);
          localObject2 = new NetworkImageView(this.context);
          LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, 0);
          ((NetworkImageView)localObject2).placeholderLoading = R.drawable.placeholder_loading;
          ((NetworkImageView)localObject2).placeholderEmpty = R.drawable.placeholder_empty;
          ((NetworkImageView)localObject2).placeholderError = R.drawable.placeholder_error;
          ((NetworkImageView)localObject2).setFocusable(false);
          ((NetworkImageView)localObject2).setImage(paramArrayOfString1[i]);
          ((NetworkImageView)localObject2).setTag(Integer.valueOf(i));
          ((NetworkImageView)localObject2).setScaleType(ImageView.ScaleType.CENTER_CROP);
          ((NetworkImageView)localObject2).setOnClickListener(this);
          ((LinearLayout)localObject1).addView((View)localObject2, localLayoutParams);
          this.imageViewArrayList.add(localObject2);
          break;
          if (i % 3 != 0)
            continue;
          addLinearLayout();
        }
      }
    }
    while (this.curLine == null);
    addSpace();
  }

  public void setIsSingleBigMode(boolean paramBoolean)
  {
    this.isSingleBigMode = paramBoolean;
  }

  public void setPhotoTouchListener(CheckinListItemListener paramCheckinListItemListener)
  {
    this.photoListener = paramCheckinListItemListener;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CheckInPhotosView
 * JD-Core Version:    0.6.0
 */
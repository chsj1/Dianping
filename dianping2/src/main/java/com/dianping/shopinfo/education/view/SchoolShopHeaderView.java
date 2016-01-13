package com.dianping.shopinfo.education.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetImageHandler;
import com.dianping.widget.NetworkImageView;

public class SchoolShopHeaderView extends ShopInfoHeaderView
{
  private DPObject headerInfo;
  private TextView imgCount;

  public SchoolShopHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SchoolShopHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void setSchoolTags()
  {
    if ((this.headerInfo == null) || (this.headerInfo.getStringArray("SchoolTagList") == null) || (this.headerInfo.getStringArray("SchoolTagList").length <= 0))
      return;
    LinearLayout localLinearLayout = (LinearLayout)findViewById(R.id.schooltags_lay);
    localLinearLayout.removeAllViews();
    String[] arrayOfString = this.headerInfo.getStringArray("SchoolTagList");
    int j = arrayOfString.length;
    int i = 0;
    while (i < j)
    {
      Object localObject = arrayOfString[i];
      TextView localTextView = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.school_feature_tag, null, false);
      localTextView.setText((CharSequence)localObject);
      localObject = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject).rightMargin = ViewUtils.dip2px(getContext(), 5.0F);
      localTextView.setLayoutParams((ViewGroup.LayoutParams)localObject);
      localLinearLayout.addView(localTextView);
      i += 1;
    }
    localLinearLayout.setVisibility(0);
  }

  protected void setBaseInfo(DPObject paramDPObject)
  {
    this.name.setText(getFullName(paramDPObject));
    setIconImage(paramDPObject);
    setSchoolTags();
    setPicCountStr(paramDPObject);
  }

  public void setHeaderInfo(DPObject paramDPObject)
  {
    this.headerInfo = paramDPObject;
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if (this.headerInfo == null)
      return;
    paramDPObject = (ImageView)findViewById(R.id.shop_panel_cover_image);
    MyResources localMyResources = MyResources.getResource(ShopInfoHeaderView.class);
    String str = this.headerInfo.getString("DefaultImg");
    if (TextUtils.isEmpty(str))
    {
      this.icon.setLocalBitmap(BitmapFactory.decodeResource(localMyResources.getResources(), R.drawable.header_default));
      this.icon.setImageHandler(new NetImageHandler(paramDPObject)
      {
        public void onFinish()
        {
          SchoolShopHeaderView.this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
          this.val$coverImage.setVisibility(0);
        }
      });
      return;
    }
    this.icon.setImage(str);
    this.icon.setImageHandler(new NetImageHandler(paramDPObject)
    {
      public void onFinish()
      {
        SchoolShopHeaderView.this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.val$coverImage.setVisibility(0);
      }
    });
  }

  public void setPicCountStr(DPObject paramDPObject)
  {
    this.imgCount = ((TextView)findViewById(R.id.imgCount));
    if (this.imgCount != null)
    {
      if (paramDPObject.getInt("PicCount") >= 0)
      {
        this.imgCount.setVisibility(0);
        this.imgCount.setText(paramDPObject.getInt("PicCount") + "张");
      }
    }
    else
      return;
    this.imgCount.setVisibility(8);
  }

  protected void setScoreInfo(DPObject paramDPObject)
  {
  }

  protected void setScoreSourceInfo(DPObject paramDPObject)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.view.SchoolShopHeaderView
 * JD-Core Version:    0.6.0
 */
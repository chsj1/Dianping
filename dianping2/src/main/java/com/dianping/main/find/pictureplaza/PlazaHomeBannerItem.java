package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaFrameLayout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlazaHomeBannerItem extends NovaFrameLayout
{
  private NetworkImageView bannerImage;
  private TextView bannerSubTitle;
  private TextView bannerTitle;

  public PlazaHomeBannerItem(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaHomeBannerItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean isNumeric(String paramString)
  {
    return Pattern.compile("[0-9]*").matcher(paramString).matches();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.bannerImage = ((NetworkImageView)findViewById(R.id.plaza_banner_image));
    this.bannerTitle = ((TextView)findViewById(R.id.plaza_banner_title));
    this.bannerSubTitle = ((TextView)findViewById(R.id.plaza_banner_subtitle));
  }

  public void setBanner(DPObject paramDPObject)
  {
    setGAString("banner");
    String str1 = paramDPObject.getString("Url");
    if (!TextUtils.isEmpty(str1))
    {
      str2 = str1.substring(str1.indexOf("=") + 1, str1.length());
      if ((!TextUtils.isEmpty(str2)) && (isNumeric(str2)))
        this.gaUserInfo.biz_id = str2;
    }
    this.bannerImage.setImage(paramDPObject.getString("BgPicUrl"));
    String str2 = paramDPObject.getString("Title");
    if (!TextUtils.isEmpty(str2))
    {
      this.bannerTitle.setText(str2);
      paramDPObject = paramDPObject.getString("SubTitle");
      if (TextUtils.isEmpty(paramDPObject))
        break label143;
      this.bannerSubTitle.setText(paramDPObject);
    }
    while (true)
    {
      setOnClickListener(new View.OnClickListener(str1)
      {
        public void onClick(View paramView)
        {
          if (!TextUtils.isEmpty(this.val$url))
            PlazaHomeBannerItem.this.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.val$url)));
        }
      });
      return;
      this.bannerTitle.setVisibility(8);
      break;
      label143: this.bannerSubTitle.setVisibility(8);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaHomeBannerItem
 * JD-Core Version:    0.6.0
 */
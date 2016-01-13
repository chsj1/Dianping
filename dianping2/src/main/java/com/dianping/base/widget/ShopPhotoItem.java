package com.dianping.base.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.util.ImageUtils;
import com.dianping.util.DateUtil;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import java.util.Date;

public class ShopPhotoItem extends LinearLayout
  implements View.OnClickListener
{
  private String ga;
  private DPObject photo;
  private TextView photoTitle;
  private TextView price;
  private DPObject shop;
  private NetworkImageView thumb;
  private TextView time;
  private TextView user;

  public ShopPhotoItem(Context paramContext)
  {
    super(paramContext);
  }

  public ShopPhotoItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public NetworkImageView getPhotoView()
  {
    return this.thumb;
  }

  public void onClick(View paramView)
  {
    if (paramView == this.user)
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user?userid=" + this.photo.getObject("User").getInt("UserID")));
      paramView.putExtra("user", this.photo.getObject("User"));
      getContext().startActivity(paramView);
      if (!TextUtils.isEmpty(this.ga))
        DPApplication.instance().statisticsEvent("shopinfo5", this.ga, "", 0);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.thumb = ((NetworkImageView)findViewById(R.id.photo));
    this.photoTitle = ((TextView)findViewById(R.id.title));
    this.user = ((TextView)findViewById(R.id.user));
    this.time = ((TextView)findViewById(R.id.time));
    this.price = ((TextView)findViewById(R.id.price));
  }

  public void savePhotoToAlbum()
  {
    ImageUtils.savePhotoToAlbum(this.thumb, this.thumb.getContext());
  }

  public void setPhoto(DPObject paramDPObject, String paramString)
  {
    if (paramDPObject == null)
      return;
    this.photo = paramDPObject;
    this.ga = paramString;
    this.thumb = ((NetworkImageView)findViewById(R.id.photo));
    if (this.thumb != null)
      this.thumb.setImage(paramDPObject.getString("Url"));
    TextView localTextView;
    if (this.user != null)
    {
      localTextView = this.user;
      if (paramDPObject.getObject("User") != null)
        break label206;
    }
    label206: for (paramString = ""; ; paramString = paramDPObject.getObject("User").getString("NickName"))
    {
      localTextView.setText(paramString);
      this.user.setOnClickListener(this);
      if (this.photoTitle != null)
        this.photoTitle.setText(paramDPObject.getString("Name"));
      if ((this.time != null) && (paramDPObject.getTime("Time") > 0L))
        this.time.setText("上传于" + DateUtil.format2t(new Date(paramDPObject.getTime("Time"))));
      if (this.price == null)
        break;
      if (TextUtils.isEmpty(paramDPObject.getString("PriceText")))
        break label221;
      this.price.setVisibility(0);
      this.price.setText(paramDPObject.getString("PriceText"));
      return;
    }
    label221: if (paramDPObject.getInt("Price") > 0)
    {
      this.price.setVisibility(0);
      this.price.setText("￥" + paramDPObject.getInt("Price"));
      return;
    }
    this.price.setVisibility(4);
  }

  public void setShop(DPObject paramDPObject, String paramString)
  {
    if (paramDPObject == null);
    do
    {
      return;
      this.ga = paramString;
      this.shop = paramDPObject;
    }
    while (this.user == null);
    paramString = this.user;
    if (this.photo.getObject("User") == null);
    for (paramDPObject = ""; ; paramDPObject = this.photo.getObject("User").getString("NickName"))
    {
      paramString.setText(paramDPObject);
      this.user.setOnClickListener(this);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ShopPhotoItem
 * JD-Core Version:    0.6.0
 */
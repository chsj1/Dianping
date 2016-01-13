package com.dianping.shopinfo.beauty.hair.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.HorizontalImageGallery;
import com.dianping.base.widget.HorizontalImageGallery.OnGalleryImageClickListener;
import com.dianping.base.widget.ShopPower;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class BeautyHairNielShopInfoHeaderView extends ShopInfoHeaderView
{
  private DPObject beautyShop;
  private TextView desc;
  private RelativeLayout emptyGallery;
  private HorizontalImageGallery imageGallery;
  private ArrayList<DPObject> picList = new ArrayList();
  private TextView price;
  private DPObject shop;
  private int totalPicCount = 0;

  public BeautyHairNielShopInfoHeaderView(Context paramContext)
  {
    super(paramContext, null);
  }

  public BeautyHairNielShopInfoHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void buildGallery()
  {
    int j = 0;
    this.imageGallery.removeAllImages();
    ArrayList localArrayList = new ArrayList();
    if (this.totalPicCount > this.picList.size());
    for (boolean bool = true; ; bool = false)
    {
      i = 0;
      while (i < this.picList.size())
      {
        localArrayList.add(((DPObject)this.picList.get(i)).getString("ThumblUrl"));
        i += 1;
      }
    }
    this.imageGallery.setElementName("beauty_multiphoto_detail");
    this.imageGallery.addImages((String[])localArrayList.toArray(new String[0]), bool);
    this.imageGallery.setOnGalleryImageClickListener(new HorizontalImageGallery.OnGalleryImageClickListener()
    {
      public void onGalleryImageClick(int paramInt1, int paramInt2, Drawable paramDrawable)
      {
        if (BeautyHairNielShopInfoHeaderView.this.shop == null)
          return;
        if ((paramInt1 == paramInt2 - 1) && (BeautyHairNielShopInfoHeaderView.this.totalPicCount > 7))
        {
          paramInt1 = BeautyHairNielShopInfoHeaderView.this.shop.getInt("ID");
          paramDrawable = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
          paramDrawable.putExtra("objShop", BeautyHairNielShopInfoHeaderView.this.shop);
          paramDrawable.putExtra("shopId", paramInt1);
          paramDrawable.putExtra("enableUpload", false);
          BeautyHairNielShopInfoHeaderView.this.getContext().startActivity(paramDrawable);
          return;
        }
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphotoandmore"));
        localIntent.putExtra("position", paramInt1);
        localIntent.putExtra("totalPicCount", BeautyHairNielShopInfoHeaderView.this.totalPicCount);
        localIntent.putExtra("categoryTag", "beauty");
        Object localObject = new ArrayList();
        ((ArrayList)localObject).add(BeautyHairNielShopInfoHeaderView.this.shop);
        localIntent.putParcelableArrayListExtra("arrShopObjs", (ArrayList)localObject);
        localIntent.putParcelableArrayListExtra("pageList", BeautyHairNielShopInfoHeaderView.this.picList);
        if (paramDrawable != null)
        {
          localObject = new ByteArrayOutputStream();
          ((BitmapDrawable)paramDrawable).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject);
          localIntent.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject).toByteArray());
        }
        BeautyHairNielShopInfoHeaderView.this.getContext().startActivity(localIntent);
      }
    });
    Object localObject = this.imageGallery;
    if (localArrayList.size() > 0)
    {
      i = 0;
      ((HorizontalImageGallery)localObject).setVisibility(i);
      localObject = this.emptyGallery;
      if (localArrayList.size() != 0)
        break label172;
    }
    label172: for (int i = j; ; i = 8)
    {
      ((RelativeLayout)localObject).setVisibility(i);
      return;
      i = 8;
      break;
    }
  }

  private void buildPicList()
  {
    try
    {
      if (this.picList == null)
        return;
      this.picList = ((ArrayList)this.picList.subList(0, 7));
      return;
    }
    catch (Exception localException)
    {
      Log.e(localException.toString());
    }
  }

  private void setShopDesc()
  {
    if (this.shop != null)
    {
      this.desc.setText(this.shop.getString("ScoreText"));
      this.desc.setVisibility(0);
    }
  }

  private void setShopName()
  {
    this.name.setText(DPObjectUtils.getShopFullName(this.shop));
  }

  private void setShopPower()
  {
    this.power.setPower(this.shop.getInt("ShopPower"));
    this.power.setVisibility(0);
  }

  public void initialPics()
  {
    DPObject[] arrayOfDPObject = this.beautyShop.getArray("Photos");
    if (arrayOfDPObject == null);
    for (this.picList = new ArrayList(); !this.picList.isEmpty(); this.picList = new ArrayList(Arrays.asList(arrayOfDPObject)))
    {
      buildPicList();
      buildGallery();
      return;
    }
    this.imageGallery.setVisibility(8);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.name = ((TextView)findViewById(R.id.title_shop_name));
    this.power = ((ShopPower)findViewById(R.id.shop_power));
    this.price = ((TextView)findViewById(R.id.beauty_price));
    this.desc = ((TextView)findViewById(R.id.beauty_desc));
    this.imageGallery = ((HorizontalImageGallery)findViewById(R.id.beauty_image_gallery));
    this.emptyGallery = ((RelativeLayout)findViewById(R.id.beauty_image_gallery_empty));
  }

  public void setBeautyShop(DPObject paramDPObject1, DPObject paramDPObject2)
  {
    if (paramDPObject1 != null)
    {
      this.shop = paramDPObject1;
      setShopName();
      setShopPower();
      setShopDesc();
      this.totalPicCount = paramDPObject1.getInt("PicCount");
      if (paramDPObject2 != null)
        break label70;
      this.price.setText(paramDPObject1.getString("PriceText"));
    }
    while (true)
    {
      this.price.setVisibility(0);
      if (paramDPObject2 != null)
      {
        this.beautyShop = paramDPObject2;
        initialPics();
      }
      return;
      label70: if (TextUtils.isEmpty(paramDPObject2.getString("DisplayPrice")))
        continue;
      this.price.setText(paramDPObject2.getString("DisplayPrice"));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.beauty.hair.widget.BeautyHairNielShopInfoHeaderView
 * JD-Core Version:    0.6.0
 */
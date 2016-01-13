package com.dianping.ugc.photo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.view.GAHelper;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class AlbumDetailShopActivity extends AlbumDetailBaseActivity
{
  String albumName;
  String cateName;
  private boolean enableUpload;
  private String fullShopName;
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (("com.dianping.action.UPLOAD_PHOTO".equals(paramIntent.getAction())) || ("com.dianping.action.UPDATE_PHOTO".equals(paramIntent.getAction())))
      {
        AlbumDetailShopActivity.this.mapiCacheService().remove(AlbumDetailShopActivity.this.photoTask(AlbumDetailShopActivity.this.shopId, 0));
        if (AlbumDetailShopActivity.this.adapter != null)
          AlbumDetailShopActivity.this.adapter.reset();
      }
    }
  };

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 6);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.rightTitleButton)
    {
      GAHelper.instance().contextStatisticsEvent(this, "toupload", null, "tap");
      UploadPhotoUtil.uploadShopPhoto(this, this.dpObjShop);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.cateName = paramBundle.getString("cateName");
      this.albumName = paramBundle.getString("albumName");
      this.enableUpload = paramBundle.getBoolean("enableUpload");
    }
    while (true)
    {
      paramBundle = new IntentFilter("com.dianping.action.UPLOAD_PHOTO");
      registerReceiver(this.receiver, paramBundle);
      paramBundle = new IntentFilter("com.dianping.action.UPDATE_PHOTO");
      registerReceiver(this.receiver, paramBundle);
      if ((this.shopId != null) && (this.albumName != null) && (this.cateName != null))
        break;
      finish();
      return;
      paramBundle = getIntent();
      this.dpObjShop = ((DPObject)paramBundle.getParcelableExtra("objShop"));
      this.cateName = paramBundle.getStringExtra("cateName");
      if (!TextUtils.isEmpty(this.cateName))
        this.cateName = this.cateName.trim();
      this.albumName = paramBundle.getStringExtra("albumName");
      if (!TextUtils.isEmpty(this.albumName))
        this.albumName = this.albumName.trim();
      this.shopId = paramBundle.getStringExtra("shopId");
      this.enableUpload = paramBundle.getBooleanExtra("enableUpload", true);
      this.fullShopName = DPObjectUtils.getShopFullName(this.dpObjShop);
      setSubtitle(this.fullShopName);
    }
    if ((!TextUtils.isEmpty(this.cateName)) && (this.cateName.equals("菜")) && (this.albumName.equals("更多")))
      this.isMore = true;
    super.setTitle(this.albumName);
    if (!this.enableUpload)
      ViewUtils.hideView(this.rightTitleButton, true);
    if (this.isMore);
    for (int i = 2; ; i = 3)
    {
      this.gridView.setNumColumns(i);
      return;
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    unregisterReceiver(this.receiver);
  }

  public void onFullScreenPicClick(AdapterView<?> paramAdapterView, View paramView, int paramInt)
  {
    if (paramView.getTag() == AlbumDetailBaseActivity.ImageAdapter.LOADING);
    do
    {
      return;
      paramAdapterView = (DPNetworkImageView)paramView.findViewById(R.id.fullscreen_offical_photo);
    }
    while (paramAdapterView == null);
    Object localObject = new ArrayList();
    ((ArrayList)localObject).addAll(this.mFullScreenPhotos);
    ((ArrayList)localObject).addAll(this.adapter.getDataList());
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphoto"));
    paramView.putExtra("pageList", (Serializable)localObject);
    paramView.putExtra("position", paramInt);
    if (this.dpObjShop != null)
    {
      localObject = new ArrayList();
      ((ArrayList)localObject).add(this.dpObjShop);
      paramView.putExtra("arrShopObjs", (Serializable)localObject);
    }
    paramView.putExtra("name", this.fullShopName);
    if ((BitmapDrawable)paramAdapterView.getDrawable() != null)
    {
      localObject = new ByteArrayOutputStream();
      ((BitmapDrawable)paramAdapterView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject);
      paramView.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject).toByteArray());
    }
    startActivity(paramView);
    statisticsEvent("shopinfo5", "shopinfo5_photo_item", this.shopId + "", 0);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramView.getTag() == AlbumDetailBaseActivity.ImageAdapter.LOADING);
    do
    {
      return;
      paramAdapterView = (NetworkThumbView)paramView.findViewById(R.id.img_shop_photo);
    }
    while (paramAdapterView == null);
    Object localObject = new ArrayList();
    ((ArrayList)localObject).addAll(this.mFullScreenPhotos);
    ((ArrayList)localObject).addAll(this.adapter.getDataList());
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphoto"));
    paramView.putExtra("pageList", (Serializable)localObject);
    paramView.putExtra("position", this.mFullScreenPhotos.size() + paramInt);
    localObject = new ArrayList();
    ((ArrayList)localObject).add(this.dpObjShop);
    paramView.putExtra("arrShopObjs", (Serializable)localObject);
    paramView.putExtra("name", this.fullShopName);
    if ((BitmapDrawable)paramAdapterView.getDrawable() != null)
    {
      localObject = new ByteArrayOutputStream();
      ((BitmapDrawable)paramAdapterView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject);
      paramView.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject).toByteArray());
    }
    startActivity(paramView);
    statisticsEvent("shopinfo5", "shopinfo5_photo_item", this.shopId + "", 0);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("cateName", this.cateName);
    paramBundle.putString("albumName", this.albumName);
    paramBundle.putBoolean("enableUpload", this.enableUpload);
  }

  protected MApiRequest photoTask(String paramString, int paramInt)
  {
    if (paramInt != 0)
      statisticsEvent("shopinfo5", "shopinfo5_photo_dropdown", "", 0);
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/getshopalbumdetail.bin?");
    localStringBuffer.append("shopid=");
    localStringBuffer.append(paramString + "");
    localStringBuffer.append("&photocategoryname=");
    localStringBuffer.append(this.cateName);
    localStringBuffer.append("&albumname=");
    localStringBuffer.append(this.albumName);
    localStringBuffer.append("&start=");
    localStringBuffer.append(paramInt + "");
    localStringBuffer.append("&screenwidth=");
    localStringBuffer.append(ViewUtils.getScreenWidthPixels(this) + "");
    localStringBuffer.append("&screenheight=");
    localStringBuffer.append(ViewUtils.getScreenHeightPixels(this) + "");
    return BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.AlbumDetailShopActivity
 * JD-Core Version:    0.6.0
 */
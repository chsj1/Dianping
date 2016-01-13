package com.dianping.base.ugc.photo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.FragmentTabsPagerActivity;
import com.dianping.base.basic.TabPagerFragment;
import com.dianping.base.ugc.widget.PhotoTabsAdapter;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ShopPhotoGalleryActivity extends FragmentTabsPagerActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>, ShopPhotoGalleryFragment.PhotoGalleryShop
{
  ImageButton btnBack;
  CustomImageButton btnUploadPhoto;
  String defaultTabName;
  private DPObject dpObjShop;
  private boolean enableUpload;
  ProgressDialog loadingDialog;
  private int shopId;
  MApiRequest shopRequest;

  private int defaultTabIndex(DPObject[] paramArrayOfDPObject)
  {
    int m = 0;
    int k = 0;
    int j;
    if ((this.defaultTabName != null) && (this.defaultTabName.length() > 0))
    {
      i = 0;
      while (true)
      {
        j = k;
        if (i < paramArrayOfDPObject.length)
        {
          DPObject localDPObject = paramArrayOfDPObject[i];
          if (this.defaultTabName.equals(localDPObject.getString("Name")))
            j = i;
        }
        else
        {
          return j;
        }
        i += 1;
      }
    }
    int i = 0;
    while (true)
    {
      j = m;
      if (i < paramArrayOfDPObject.length)
      {
        if (paramArrayOfDPObject[i].getInt("Count") > 0)
          j = i;
      }
      else
        return j;
      i += 1;
    }
  }

  protected Class getPhotoGalleryFragment()
  {
    return ShopPhotoGalleryFragment.class;
  }

  public DPObject getShop()
  {
    return this.dpObjShop;
  }

  public int getShopId()
  {
    return this.shopId;
  }

  protected PhotoTabsAdapter getmTabsAdapter()
  {
    return new PhotoTabsAdapter(this.tabPagerFragment, this.tabPagerFragment.tabHost(), this.tabPagerFragment.viewPager());
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 6);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.btnUploadPhoto)
      if (this.dpObjShop != null)
        UploadPhotoUtil.uploadShopPhoto(this, this.dpObjShop);
    do
      return;
    while (paramView != this.btnBack);
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.tabPagerFragment.tabHost().findViewById(16908307).setBackgroundDrawable(null);
    if (paramBundle != null)
    {
      this.dpObjShop = ((DPObject)paramBundle.getParcelable("dpObjShop"));
      this.shopId = paramBundle.getInt("shopId");
      this.defaultTabName = paramBundle.getString("defaultTabName");
      this.enableUpload = paramBundle.getBoolean("enableUpload");
    }
    while (this.shopId <= 0)
    {
      finish();
      return;
      paramBundle = getIntent();
      Uri localUri = paramBundle.getData();
      this.defaultTabName = localUri.getQueryParameter("tabname");
      this.dpObjShop = ((DPObject)paramBundle.getParcelableExtra("objShop"));
      this.enableUpload = paramBundle.getBooleanExtra("enableUpload", true);
      if (this.dpObjShop != null)
        this.shopId = this.dpObjShop.getInt("ID");
      if (this.shopId <= 0)
        this.shopId = paramBundle.getIntExtra("shopId", 0);
      if (this.shopId > 0)
        continue;
      paramBundle = localUri.getQueryParameter("id");
      try
      {
        this.shopId = Integer.parseInt(paramBundle);
      }
      catch (NumberFormatException paramBundle)
      {
        paramBundle.printStackTrace();
      }
    }
    if ((this.dpObjShop == null) || (this.dpObjShop.getArray("ShopPhotoCategory") == null))
    {
      sendShopRequest(this.shopId);
      this.loadingDialog = new ProgressDialog(this);
      this.loadingDialog.setMessage("正在加载...");
      this.loadingDialog.show();
      return;
    }
    setupView();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopRequest)
    {
      if (this.loadingDialog != null)
        this.loadingDialog.dismiss();
      Toast.makeText(this, "暂时无法获取商户图片数据", 1).show();
      finish();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopRequest);
    try
    {
      this.dpObjShop = ((DPObject)paramMApiResponse.result());
      if (this.dpObjShop == null)
      {
        Toast.makeText(this, "暂时无法获取商户图片数据", 1).show();
        finish();
        return;
      }
    }
    catch (Exception paramMApiRequest)
    {
      while (true)
        paramMApiRequest.printStackTrace();
      if (this.loadingDialog != null)
        this.loadingDialog.dismiss();
      setupView();
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("dpObjShop", this.dpObjShop);
    paramBundle.putInt("shopId", this.shopId);
    paramBundle.putString("defaultTabName", this.defaultTabName);
    paramBundle.putBoolean("enableUpload", this.enableUpload);
  }

  public void sendShopRequest(int paramInt)
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/shop.bin?");
    localStringBuffer.append("shopid=").append(paramInt);
    this.shopRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
    localMApiService.exec(this.shopRequest, this);
  }

  protected void setOnContentView()
  {
    this.btnBack = ((ImageButton)findViewById(R.id.left_title_button));
    this.btnBack.setVisibility(0);
    this.btnBack.setOnClickListener(this);
    TextView localTextView = (TextView)findViewById(16908310);
    localTextView.setVisibility(0);
    localTextView.setText("相册");
    this.btnUploadPhoto = ((CustomImageButton)findViewById(R.id.right_title_button));
    this.btnUploadPhoto.setImageResource(R.drawable.navibar_icon_addpic);
    this.btnUploadPhoto.setVisibility(0);
    this.btnUploadPhoto.setOnClickListener(this);
    this.btnUploadPhoto.setGAString("toupload", this.btnUploadPhoto.getGAUserInfo());
    findViewById(R.id.title_button).setVisibility(8);
    super.setContentView(R.layout.tab_pager_fragment);
  }

  public void setupView()
  {
    if (!this.enableUpload)
      ViewUtils.hideView(this.btnUploadPhoto, true);
    this.tabPagerFragment.setmTabsAdapter(getmTabsAdapter());
    if (this.dpObjShop == null)
      Log.e("the shop info is null ! ");
    while (true)
    {
      return;
      Object localObject = null;
      try
      {
        DPObject[] arrayOfDPObject = this.dpObjShop.getArray("ShopPhotoCategory");
        localObject = arrayOfDPObject;
        label54: if ((localObject == null) || (localObject.length <= 0))
          continue;
        int i = 0;
        while (i < localObject.length)
        {
          arrayOfDPObject = localObject[i];
          Bundle localBundle = new Bundle();
          localBundle.putInt("shopId", this.shopId);
          localBundle.putBoolean("enableUpload", this.enableUpload);
          localBundle.putString("cateName", arrayOfDPObject.getString("Name"));
          localBundle.putInt("type", arrayOfDPObject.getInt("Type"));
          localBundle.putInt("filter", 0);
          localBundle.putInt("shopType", this.dpObjShop.getInt("ShopType"));
          addTab(arrayOfDPObject.getString("Name"), R.layout.shop_photo_tab_indicator, getPhotoGalleryFragment(), localBundle);
          i += 1;
        }
        if (localObject.length > 0)
          this.tabPagerFragment.tabHost().setCurrentTab(defaultTabIndex(localObject));
        if (localObject.length != 1)
          continue;
        findViewById(16908307).setVisibility(8);
        return;
      }
      catch (Exception localException)
      {
        break label54;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.ugc.photo.ShopPhotoGalleryActivity
 * JD-Core Version:    0.6.0
 */
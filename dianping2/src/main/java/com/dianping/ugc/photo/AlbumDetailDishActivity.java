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
import android.view.View;
import android.widget.AdapterView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.util.LoginUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class AlbumDetailDishActivity extends AlbumDetailBaseActivity
{
  private static final String cateName = "菜";
  private final int RECOMMEND_DISH_REQ_CODE = 21;
  private boolean bUpImg;
  private int dishId;
  private String dishName;
  private String fullShopName;
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (("com.dianping.action.UPLOAD_PHOTO".equals(paramIntent.getAction())) || ("com.dianping.action.UPDATE_PHOTO".equals(paramIntent.getAction())))
      {
        AlbumDetailDishActivity.this.mapiCacheService().remove(AlbumDetailDishActivity.this.photoTask(AlbumDetailDishActivity.this.shopId, 0));
        if (AlbumDetailDishActivity.this.adapter != null)
          AlbumDetailDishActivity.this.adapter.reset();
      }
    }
  };

  private void gotoChooseDish()
  {
    statisticsEvent("shopinfo5", "shopinfo5_dish_album_review", "", 0);
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://choosedish"));
    localIntent.putExtra("shopId", String.valueOf(this.dpObjShop.getInt("ID")));
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(this.dishName);
    localIntent.putStringArrayListExtra("dishes", localArrayList);
    startActivityForResult(localIntent, 21);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    Object localObject;
    if ((paramInt1 == 21) && (paramInt2 == -1))
    {
      localObject = this.dpObjShop.getString("Name");
      str = this.dpObjShop.getString("BranchName");
      localObject = new StringBuilder().append((String)localObject);
      if ((str != null) && (str.length() != 0))
        break label149;
    }
    label149: for (String str = ""; ; str = "(" + str + ")")
    {
      str = str;
      localObject = new Bundle();
      ((Bundle)localObject).putParcelable("shop", this.dpObjShop);
      ((Bundle)localObject).putBoolean("fromRecommend", true);
      ((Bundle)localObject).putString("source", "shopinfo");
      ((Bundle)localObject).putStringArrayList("dishes", paramIntent.getStringArrayListExtra("dishes"));
      AddReviewUtil.addReview(this, Integer.parseInt(this.shopId), str, (Bundle)localObject);
      return;
    }
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.btn_upload);
    for (this.bUpImg = true; accountService().token() == null; this.bUpImg = false)
    {
      LoginUtils.setLoginGASource(this, "rec_add");
      gotoLogin();
      return;
    }
    if (this.bUpImg)
    {
      UploadPhotoUtil.uploadShopPhoto(this, this.dpObjShop);
      return;
    }
    gotoChooseDish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.dishId = paramBundle.getInt("dishId");
      this.dishName = paramBundle.getString("dishName");
    }
    while (true)
    {
      paramBundle = new IntentFilter("com.dianping.action.UPLOAD_PHOTO");
      registerReceiver(this.receiver, paramBundle);
      paramBundle = new IntentFilter("com.dianping.action.UPDATE_PHOTO");
      registerReceiver(this.receiver, paramBundle);
      super.setTitle(this.dishName);
      super.getTitleBar().addRightViewItem("我来推荐", null, this);
      do
      {
        return;
        paramBundle = getIntent();
        this.dpObjShop = ((DPObject)paramBundle.getParcelableExtra("objShop"));
      }
      while (this.dpObjShop == null);
      this.dishId = paramBundle.getIntExtra("dishId", 0);
      this.dishName = paramBundle.getStringExtra("dishName");
      this.shopId = String.valueOf(this.dpObjShop.getInt("ID"));
      this.fullShopName = DPObjectUtils.getShopFullName(this.dpObjShop);
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    unregisterReceiver(this.receiver);
  }

  public void onFullScreenPicClick(AdapterView<?> paramAdapterView, View paramView, int paramInt)
  {
    paramAdapterView = (DPNetworkImageView)paramView.findViewById(R.id.fullscreen_offical_photo);
    if (paramAdapterView == null)
      return;
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphoto?ga=shopinfo5_dish_user"));
    Object localObject = new ArrayList();
    ((ArrayList)localObject).add(this.dpObjShop);
    ArrayList localArrayList = new ArrayList();
    localArrayList.addAll(this.mFullScreenPhotos);
    localArrayList.addAll(this.adapter.getDataList());
    paramView.putParcelableArrayListExtra("arrShopObjs", (ArrayList)localObject);
    paramView.putParcelableArrayListExtra("pageList", localArrayList);
    paramView.putExtra("position", paramInt);
    paramView.putExtra("isRecommend", true);
    paramView.putExtra("name", this.fullShopName);
    if ((BitmapDrawable)paramAdapterView.getDrawable() != null)
    {
      localObject = new ByteArrayOutputStream();
      ((BitmapDrawable)paramAdapterView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject);
      paramView.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject).toByteArray());
    }
    startActivity(paramView);
    statisticsEvent("shopinfo5", "shopinfo5_dish_item", "", 0);
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
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphoto?ga=shopinfo5_dish_user"));
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(this.dpObjShop);
    paramView.putParcelableArrayListExtra("arrShopObjs", localArrayList);
    paramView.putParcelableArrayListExtra("pageList", (ArrayList)localObject);
    paramView.putExtra("position", this.mFullScreenPhotos.size() + paramInt);
    paramView.putExtra("isRecommend", true);
    paramView.putExtra("name", this.fullShopName);
    if ((BitmapDrawable)paramAdapterView.getDrawable() != null)
    {
      localObject = new ByteArrayOutputStream();
      ((BitmapDrawable)paramAdapterView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject);
      paramView.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject).toByteArray());
    }
    startActivity(paramView);
    statisticsEvent("shopinfo5", "shopinfo5_dish_item", "", 0);
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    if (this.bUpImg)
    {
      UploadPhotoUtil.uploadShopPhoto(this, this.dpObjShop);
      return;
    }
    gotoChooseDish();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("dishId", this.dishId);
    paramBundle.putString("dishName", this.dishName);
  }

  protected MApiRequest photoTask(String paramString, int paramInt)
  {
    if (paramInt != 0)
      statisticsEvent("shopinfo5", "shopinfo5_dish_dropdown", "", 0);
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/getshopalbumdetail.bin?");
    localStringBuffer.append("shopid=");
    localStringBuffer.append(paramString + "");
    localStringBuffer.append("&photocategoryname=");
    localStringBuffer.append("菜");
    localStringBuffer.append("&albumname=");
    localStringBuffer.append(this.dishName);
    localStringBuffer.append("&start=");
    localStringBuffer.append(paramInt + "");
    localStringBuffer.append("&screenwidth=");
    localStringBuffer.append(ViewUtils.getScreenWidthPixels(this) + "");
    localStringBuffer.append("&screenheight=");
    localStringBuffer.append(ViewUtils.getScreenHeightPixels(this) + "");
    return BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
  }

  public void setContentView(int paramInt)
  {
    super.setContentView(R.layout.album_detail_dish_layout);
  }

  protected void setEmptyMsg(String paramString)
  {
  }

  protected void setupEmptyView()
  {
    View localView = findViewById(R.id.gallery_empty);
    this.gridView.setEmptyView(localView);
    localView.findViewById(R.id.btn_upload).setOnClickListener(this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.AlbumDetailDishActivity
 * JD-Core Version:    0.6.0
 */
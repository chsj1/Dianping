package com.dianping.ugc.review;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.MyAdapter;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.model.UserProfile;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ReviewPictureGridListActiviy extends NovaActivity
  implements AdapterView.OnItemClickListener
{
  ImagesAdapter adapter;
  GridView gridGallery;
  int mImageHeight;
  int mImageWidth;
  int mShopID;
  String mShopName;
  int mUserID;
  private ArrayList<DPObject> photos = new ArrayList();
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.UPLOAD_PHOTO".equals(paramIntent.getAction()))
        ReviewPictureGridListActiviy.this.adapter.reset();
    }
  };
  private ArrayList<DPObject> shops = new ArrayList();

  private void init()
  {
    this.gridGallery = ((GridView)findViewById(R.id.gridGallery));
    this.gridGallery.setFastScrollEnabled(true);
    this.adapter = new ImagesAdapter(this);
    this.gridGallery.setOnItemClickListener(this);
    this.gridGallery.setAdapter(this.adapter);
    this.gridGallery.setEmptyView(getEmptyView());
  }

  protected View getEmptyView()
  {
    View localView = findViewById(R.id.empty_item);
    if ((getAccount() == null) || (getAccount().id() != this.mUserID));
    for (String str = "TA还没传过照片，真可惜……"; ; str = "哎呀，还没上传过商户照片？在商户页点击“上传照片”按钮试试吧！")
    {
      ((TextView)localView.findViewById(16908308)).setText(str);
      return localView;
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 6);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.gallery_review_pictures);
    this.mShopName = getIntent().getStringExtra("shopName");
    this.mShopID = getIntent().getIntExtra("shopId", 0);
    this.mUserID = getIntent().getIntExtra("userId", 0);
    if ((this.mShopID == 0) || (this.mUserID == 0))
    {
      Toast.makeText(this, "参数错误，请稍后再试", 0).show();
      finish();
    }
    super.setSubtitle(this.mShopName);
    this.mImageWidth = (ViewUtils.getScreenWidthPixels(this) * 45 / 100);
    this.mImageHeight = (this.mImageWidth * 300 / 280);
    init();
    paramBundle = new IntentFilter("com.dianping.action.UPLOAD_PHOTO");
    registerReceiver(this.receiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.UPDATE_PHOTO");
    registerReceiver(this.receiver, paramBundle);
  }

  protected void onDestroy()
  {
    this.adapter.setDone();
    unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if ((paramAdapterView.getItemAtPosition(paramInt) instanceof DPObject))
    {
      paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphoto"));
      paramAdapterView.putExtra("pageList", this.photos);
      paramAdapterView.putExtra("position", paramInt);
      paramAdapterView.putExtra("arrShopObjs", this.shops);
      paramAdapterView.putExtra("isUserPhotoMode", true);
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      ((BitmapDrawable)((DPNetworkImageView)paramView.findViewById(R.id.img_shop_photo)).getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, localByteArrayOutputStream);
      paramAdapterView.putExtra("currentbitmap", localByteArrayOutputStream.toByteArray());
      startActivity(paramAdapterView);
    }
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    this.mShopName = paramBundle.getString("shopName");
    this.mShopID = paramBundle.getInt("shopId");
    this.mUserID = paramBundle.getInt("userId");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("shopName", this.mShopName);
    paramBundle.putInt("shopId", this.mShopID);
    paramBundle.putInt("userId", this.mUserID);
  }

  class ImagesAdapter extends MyAdapter
  {
    public ImagesAdapter(Context arg2)
    {
      super();
    }

    public int getCount()
    {
      int j = 0;
      int i = 0;
      if (this.pending)
        i = this.list.size() + 1;
      do
      {
        return i;
        if (this.request != null)
          return this.list.size() + 1;
        if (this.error != null)
          return this.list.size() + 1;
      }
      while ((this.isEnd) && (this.list.size() == 0));
      int k = this.list.size();
      if (this.isEnd);
      for (i = j; ; i = 1)
        return i + k;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.list.size())
        return this.list.get(paramInt);
      if (isPending())
        return "PENDING";
      if (this.error != null)
        return "ERROR";
      if (this.request != null)
        return "LOADING";
      return "LOADING";
    }

    public MApiRequest getRequest(int paramInt)
    {
      StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/userphoto.bin?");
      localStringBuffer.append("shopid=").append(ReviewPictureGridListActiviy.this.mShopID);
      localStringBuffer.append("&userid=").append(ReviewPictureGridListActiviy.this.mUserID);
      localStringBuffer.append("&start=").append(paramInt);
      localStringBuffer.append("&ismaped=false");
      StringBuilder localStringBuilder = new StringBuilder();
      if (ReviewPictureGridListActiviy.this.getAccount() == null);
      for (String str = ""; ; str = "&token=" + ReviewPictureGridListActiviy.this.accountService().token())
      {
        localStringBuffer.append(str + "&" + Environment.screenInfo());
        return BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
      }
    }

    protected View getView(DPObject paramDPObject, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramView = ReviewPictureGridListActiviy.this.getLayoutInflater().inflate(R.layout.item_of_review_photo, paramViewGroup, false);
        paramView.setLayoutParams(new AbsListView.LayoutParams(ReviewPictureGridListActiviy.this.mImageWidth, ReviewPictureGridListActiviy.this.mImageHeight));
      }
      while (true)
      {
        ((DPNetworkImageView)paramView.findViewById(R.id.img_shop_photo)).setImage(paramDPObject.getString("Url"));
        return paramView;
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        if (paramMApiRequest.getArray("List") != null)
          ReviewPictureGridListActiviy.this.photos.addAll(Arrays.asList(paramMApiRequest.getArray("List")));
        if ((paramMApiRequest.getArray("List") != null) && (paramMApiRequest.getArray("RelativeShop") != null))
          ReviewPictureGridListActiviy.this.shops.addAll(Arrays.asList(paramMApiRequest.getArray("RelativeShop")));
      }
    }

    public void reset()
    {
      super.reset();
      ReviewPictureGridListActiviy.this.photos.clear();
      ReviewPictureGridListActiviy.this.shops.clear();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.ReviewPictureGridListActiviy
 * JD-Core Version:    0.6.0
 */
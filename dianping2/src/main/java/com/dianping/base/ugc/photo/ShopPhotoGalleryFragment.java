package com.dianping.base.ugc.photo;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.NovaFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersListAdapterWrapper;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ShopPhotoGalleryFragment extends NovaFragment
  implements AdapterView.OnItemClickListener
{
  public static final String ACTION_SHOP_PHOTO_UPDATE = "com.dianping.action.UPLOAD_PHOTO";
  public static int PHOTO_TYPE_ALBUM = 0;
  public static int PHOTO_TYPE_DETAIL = 1;
  private static String screenInfo;
  private ShopPhotoGalleryFragment.AlbumAdapter albumAdapter;
  private int albumFrameHeight;
  private int albumFrameWidth;
  private String cateName;
  private TextView emptyTV;
  private boolean enableUpload;
  View fragmentView = null;
  protected StickyGridHeadersGridView gridView;
  private DPObject mFullScreenOffical;
  private StickyGridHeadersListAdapterWrapper mWrapper;
  private ShopPhotoGalleryFragment.PhotoAdapter photoAdapter;
  private int photoType;
  BroadcastReceiver receiver = new ShopPhotoGalleryFragment.1(this);
  private int screenHeight;
  private int screenWidth;
  private int shopId;
  private int shopType;

  private void setupEmptyView()
  {
    this.emptyTV = ((TextView)this.fragmentView.findViewById(R.id.gallery_empty));
    Object localObject = getResources().getDrawable(R.drawable.empty_page_nothing);
    ((Drawable)localObject).setBounds(0, 0, ((Drawable)localObject).getIntrinsicWidth(), ((Drawable)localObject).getIntrinsicHeight());
    this.emptyTV.setCompoundDrawablePadding(8);
    this.emptyTV.setCompoundDrawables((Drawable)localObject, null, null, null);
    this.emptyTV.setMovementMethod(LinkMovementMethod.getInstance());
    this.emptyTV.setText("暂时没有照片\n");
    if (this.enableUpload)
    {
      localObject = new SpannableString("点击右上角可以上传");
      ((SpannableString)localObject).setSpan(new ForegroundColorSpan(R.color.light_gray), 0, ((SpannableString)localObject).length(), 18);
      ((SpannableString)localObject).setSpan(new RelativeSizeSpan(0.8F), 0, ((SpannableString)localObject).length(), 18);
      this.emptyTV.append((CharSequence)localObject);
    }
    this.gridView.setEmptyView(this.emptyTV);
  }

  protected ShopPhotoGalleryFragment.PhotoAdapter getPhotoAdapter()
  {
    return new ShopPhotoGalleryFragment.PhotoAdapter(this, getActivity());
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    setupEmptyView();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (TextUtils.isEmpty(screenInfo))
    {
      DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
      screenInfo = "screenwidth=" + localDisplayMetrics.widthPixels + "&screenheight=" + localDisplayMetrics.heightPixels + "&screendensity=" + localDisplayMetrics.density;
    }
    if (paramBundle != null)
    {
      this.shopId = paramBundle.getInt("shopId");
      this.photoType = paramBundle.getInt("photoType");
      this.cateName = paramBundle.getString("cateName");
      this.shopType = paramBundle.getInt("shopType");
    }
    for (this.enableUpload = paramBundle.getBoolean("enableUpload"); ; this.enableUpload = paramBundle.getBoolean("enableUpload"))
    {
      paramBundle = new IntentFilter("com.dianping.action.UPLOAD_PHOTO");
      registerReceiver(this.receiver, paramBundle);
      paramBundle = new IntentFilter("com.dianping.action.UPDATE_PHOTO");
      registerReceiver(this.receiver, paramBundle);
      this.screenWidth = ViewUtils.getScreenWidthPixels(getActivity());
      this.screenHeight = ViewUtils.getScreenHeightPixels(getActivity());
      this.albumFrameWidth = (this.screenWidth * 45 / 100);
      this.albumFrameHeight = (this.albumFrameWidth * 300 / 280);
      return;
      paramBundle = getArguments();
      this.shopId = paramBundle.getInt("shopId");
      this.photoType = paramBundle.getInt("type");
      this.cateName = paramBundle.getString("cateName");
      this.shopType = paramBundle.getInt("shopType");
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.fragmentView = paramLayoutInflater.inflate(R.layout.shop_photo_gallery, paramViewGroup, false);
    this.gridView = ((StickyGridHeadersGridView)this.fragmentView.findViewById(R.id.gallery_gridview));
    this.gridView.setAreHeadersSticky(false);
    if (this.photoType == PHOTO_TYPE_ALBUM)
    {
      this.albumAdapter = new ShopPhotoGalleryFragment.AlbumAdapter(this, getActivity());
      this.gridView.setAdapter(this.albumAdapter);
    }
    while (true)
    {
      this.gridView.setOnItemClickListener(this);
      return this.fragmentView;
      this.photoAdapter = getPhotoAdapter();
      this.mWrapper = new ShopPhotoGalleryFragment.2(this, this.photoAdapter);
      this.gridView.setAdapter(this.mWrapper);
    }
  }

  public void onDestroy()
  {
    unregisterReceiver(this.receiver);
    if (this.photoAdapter != null)
      this.photoAdapter.cancelLoad();
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    Object localObject1;
    if (this.photoType == PHOTO_TYPE_ALBUM)
      if (this.albumAdapter.getDataList().size() > paramInt)
      {
        paramAdapterView = ((DPObject)this.albumAdapter.getDataList().get(paramInt)).getString("Name");
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://albumdetail"));
        paramView.putExtra("shopId", this.shopId + "");
        paramView.putExtra("cateName", this.cateName);
        paramView.putExtra("albumName", paramAdapterView);
        paramView.putExtra("enableUpload", this.enableUpload);
        localObject1 = getActivity();
        if ((localObject1 instanceof PhotoGalleryShop))
          paramView.putExtra("objShop", ((PhotoGalleryShop)localObject1).getShop());
        startActivity(paramView);
        statisticsEvent("shopinfo5", "shopinfo5_photo_album", this.shopId + "|" + paramAdapterView, 0);
      }
    Object localObject2;
    do
    {
      do
        return;
      while (this.photoAdapter.getDataList().size() <= paramInt);
      if (paramInt == 0)
      {
        paramAdapterView = ((DPObject)this.photoAdapter.getDataList().get(paramInt)).getString("OfficialName");
        if (!TextUtils.isEmpty(paramAdapterView))
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://albumdetail"));
          paramView.putExtra("shopId", this.shopId + "");
          paramView.putExtra("cateName", this.cateName);
          paramView.putExtra("albumName", paramAdapterView);
          paramView.putExtra("enableUpload", this.enableUpload);
          paramAdapterView = getActivity();
          if ((paramAdapterView instanceof PhotoGalleryShop))
            paramView.putExtra("objShop", ((PhotoGalleryShop)paramAdapterView).getShop());
          startActivity(paramView);
          return;
        }
      }
      localObject1 = new ArrayList();
      localObject2 = getActivity();
      paramAdapterView = null;
      if ((localObject2 instanceof PhotoGalleryShop))
      {
        paramAdapterView = (PhotoGalleryShop)localObject2;
        ((ArrayList)localObject1).add(paramAdapterView.getShop());
        paramAdapterView = DPObjectUtils.getShopFullName(paramAdapterView.getShop());
      }
      localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphoto"));
      ((Intent)localObject2).putExtra("pageList", this.photoAdapter.getDataList());
      ((Intent)localObject2).putExtra("position", paramInt);
      ((Intent)localObject2).putExtra("arrShopObjs", (Serializable)localObject1);
      ((Intent)localObject2).putExtra("name", paramAdapterView);
      paramAdapterView = (NetworkImageView)paramView.findViewById(R.id.img_shop_photo);
    }
    while (paramAdapterView == null);
    if ((BitmapDrawable)paramAdapterView.getDrawable() != null)
    {
      paramView = new ByteArrayOutputStream();
      ((BitmapDrawable)paramAdapterView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, paramView);
      ((Intent)localObject2).putExtra("currentbitmap", paramView.toByteArray());
    }
    startActivity((Intent)localObject2);
    statisticsEvent("shopinfo5", "shopinfo5_photo_item", this.shopId + "", 0);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("shopId", this.shopId);
    paramBundle.putInt("photoType", this.photoType);
    paramBundle.putString("cateName", this.cateName);
    paramBundle.putInt("shopType", this.shopType);
    paramBundle.putBoolean("enableUpload", this.enableUpload);
  }

  public static abstract interface PhotoGalleryShop
  {
    public abstract DPObject getShop();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.ugc.photo.ShopPhotoGalleryFragment
 * JD-Core Version:    0.6.0
 */
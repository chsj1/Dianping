package com.dianping.wed.widget;

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
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.weddingfeast.activity.WeddingShopPhotoGalleryActivity;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class WeddingShopPhotoUserGalleryFragment extends NovaFragment
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  public static final String ACTION_SHOP_PHOTO_UPDATE = "com.dianping.action.UPLOAD_PHOTO";
  private static String screenInfo;
  private int albumFrameHeight;
  private int albumFrameWidth;
  private String emptyMsg;
  private TextView emptyTV;
  private String errorMsg;
  View fragmentView = null;
  protected GridView gridView;
  private boolean isEnd = false;
  private boolean isTaskRunning = false;
  private int nextStartIndex = 0;
  private WeddingShopPhotoUserGalleryFragment.PhotoAdapter photoAdapter;
  private ArrayList<DPObject> photoList;
  private MApiRequest photoRequest;
  BroadcastReceiver receiver = new WeddingShopPhotoUserGalleryFragment.1(this);
  private int screenHeight;
  private int screenWidth;
  private int shopId;
  private int tagid;
  private String title;
  private int type;

  private void photoTask(int paramInt1, int paramInt2)
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/getshopalbumdetail.bin?");
    localStringBuffer.append("shopid=");
    localStringBuffer.append(paramInt2 + "");
    localStringBuffer.append("&photocategoryname=");
    localStringBuffer.append(this.title);
    localStringBuffer.append("&start=");
    localStringBuffer.append(this.nextStartIndex + "");
    localStringBuffer.append("&screenwidth=");
    localStringBuffer.append(this.screenWidth + "");
    localStringBuffer.append("&screenheight=");
    localStringBuffer.append(this.screenHeight + "");
    this.photoRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
    localMApiService.exec(this.photoRequest, this);
  }

  private void setEmptyMsg(String paramString, boolean paramBoolean)
  {
    if (paramString == null)
      return;
    if ((paramBoolean) && (!TextUtils.isEmpty(paramString)))
      this.emptyMsg = paramString;
    while (true)
    {
      this.emptyTV.setText(Html.fromHtml(this.emptyMsg));
      return;
      if (!TextUtils.isEmpty(this.emptyMsg))
        continue;
      this.emptyMsg = paramString;
    }
  }

  private void setupEmptyView()
  {
    this.emptyTV = ((TextView)this.fragmentView.findViewById(R.id.gallery_empty));
    Drawable localDrawable = getResources().getDrawable(R.drawable.empty_page_nothing);
    localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
    this.emptyTV.setCompoundDrawablePadding(8);
    this.emptyTV.setCompoundDrawables(localDrawable, null, null, null);
    this.emptyTV.setMovementMethod(LinkMovementMethod.getInstance());
    this.gridView.setEmptyView(this.emptyTV);
  }

  protected WeddingShopPhotoUserGalleryFragment.PhotoAdapter getPhotoAdapter()
  {
    return new WeddingShopPhotoUserGalleryFragment.PhotoAdapter(this);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    setupEmptyView();
    setEmptyMsg("还没小伙伴上传过图片，我先来上传吧！", false);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (TextUtils.isEmpty(screenInfo))
    {
      paramBundle = getResources().getDisplayMetrics();
      screenInfo = "screenwidth=" + paramBundle.widthPixels + "&screenheight=" + paramBundle.heightPixels + "&screendensity=" + paramBundle.density;
    }
    paramBundle = new IntentFilter("com.dianping.action.UPLOAD_PHOTO");
    registerReceiver(this.receiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.UPDATE_PHOTO");
    registerReceiver(this.receiver, paramBundle);
    this.screenWidth = ViewUtils.getScreenWidthPixels(getActivity());
    this.screenHeight = ViewUtils.getScreenHeightPixels(getActivity());
    this.albumFrameWidth = (this.screenWidth * 45 / 100);
    this.albumFrameHeight = (this.albumFrameWidth * 300 / 280);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.fragmentView = paramLayoutInflater.inflate(R.layout.shop_photo_gallery, paramViewGroup, false);
    this.gridView = ((GridView)this.fragmentView.findViewById(R.id.gallery_gridview));
    if (this.photoList == null)
      this.photoList = new ArrayList();
    this.photoAdapter = getPhotoAdapter();
    this.gridView.setAdapter(this.photoAdapter);
    this.gridView.setOnItemClickListener(this);
    return this.fragmentView;
  }

  public void onDestroy()
  {
    unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onDetach()
  {
    super.onDetach();
    if (this.photoRequest != null)
      mapiService().abort(this.photoRequest, null, true);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if ((this.photoList != null) && (this.photoList.size() > paramInt))
    {
      GAHelper.instance().contextStatisticsEvent(getActivity(), "item", ((DPActivity)getActivity()).getCloneUserInfo(), "tap");
      ArrayList localArrayList = new ArrayList();
      Object localObject = getActivity();
      paramAdapterView = null;
      if ((localObject instanceof WeddingShopPhotoGalleryActivity))
      {
        paramAdapterView = (WeddingShopPhotoGalleryActivity)localObject;
        localArrayList.add(paramAdapterView.dpObjShop());
        paramAdapterView = DPObjectUtils.getShopFullName(paramAdapterView.dpObjShop());
      }
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphoto"));
      ((Intent)localObject).putExtra("pageList", this.photoList);
      ((Intent)localObject).putExtra("position", paramInt);
      ((Intent)localObject).putExtra("arrShopObjs", localArrayList);
      ((Intent)localObject).putExtra("name", paramAdapterView);
      paramAdapterView = (NetworkImageView)paramView.findViewById(R.id.img_shop_photo);
      if ((BitmapDrawable)paramAdapterView.getDrawable() != null)
      {
        paramView = new ByteArrayOutputStream();
        ((BitmapDrawable)paramAdapterView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, paramView);
        ((Intent)localObject).putExtra("currentbitmap", paramView.toByteArray());
      }
      startActivity((Intent)localObject);
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (getActivity() == null)
      return;
    this.isTaskRunning = false;
    this.photoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.photoRequest)
    {
      this.isTaskRunning = false;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      this.nextStartIndex = paramMApiRequest.getInt(22275);
      this.isEnd = paramMApiRequest.getBoolean(3851);
      paramMApiRequest = paramMApiRequest.getArray("List");
      int i = 0;
      while (i < paramMApiRequest.length)
      {
        this.photoAdapter.addPhoto(paramMApiRequest[i]);
        i += 1;
      }
      this.photoAdapter.refresh();
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("shopId", this.shopId);
    paramBundle.putInt("Type", this.type);
    paramBundle.putString("Title", this.title);
    paramBundle.putInt("TagID", this.tagid);
  }

  public void setArgument(Bundle paramBundle)
  {
    if (paramBundle == null)
      return;
    this.shopId = paramBundle.getInt("shopId");
    this.type = paramBundle.getInt("Type");
    this.tagid = paramBundle.getInt("TagID");
    this.title = paramBundle.getString("cateName");
    this.photoAdapter.refresh();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.widget.WeddingShopPhotoUserGalleryFragment
 * JD-Core Version:    0.6.0
 */
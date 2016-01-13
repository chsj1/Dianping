package com.dianping.shopinfo.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.speed.SpeedMonitorHelper;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.base.widget.HorizontalImageGallery.OnGalleryImageClickListener;
import com.dianping.shopinfo.activity.ShopInfoActivity;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.BigHeaderView;
import com.dianping.shopinfo.widget.DefaultShopInfoHeaderView;
import com.dianping.shopinfo.widget.MultiFixedHeaderView;
import com.dianping.shopinfo.widget.MultiHeaderView;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HeadAgent extends ShopCellAgent
{
  private static final String BIGPIC = "bigpic";
  protected static final String CELL_TOP = "0200Basic.05Info";
  private static final String MULTIPIC = "multipic";
  private static final String MULTIPIC_FIXED = "multifixed";
  private static final String SMALLPIC = "smallpic";
  protected final View.OnClickListener iconClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = HeadAgent.this.getShop();
      if (paramView == null);
      while (true)
      {
        return;
        if ((!paramView.contains("PicCount")) || (paramView.getInt("PicCount") != 0) || (!TextUtils.isEmpty(paramView.getString("DefaultPic"))))
          break;
        if (!HeadAgent.this.allowUploadEntrance())
          continue;
        UploadPhotoUtil.uploadShopPhoto(HeadAgent.this.getContext(), HeadAgent.this.getShop());
        return;
      }
      Object localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
      ((Intent)localObject).putExtra("objShop", paramView);
      ((Intent)localObject).putExtra("enableUpload", HeadAgent.this.allowUploadEntrance());
      HeadAgent.this.getFragment().startActivity((Intent)localObject);
      localObject = new ArrayList();
      ((List)localObject).add(new BasicNameValuePair("shopid", String.valueOf(paramView.getInt("ID"))));
      if ("smallpic".equals(HeadAgent.this.getPicType()))
        GAHelper.instance().contextStatisticsEvent(HeadAgent.this.getContext(), "view_smallphoto", null, 0, "tap");
      while (true)
      {
        HeadAgent.this.statisticsEvent("shopinfo5", "shopinfo5_viewphoto", "", 0, (List)localObject);
        return;
        if (!"bigpic".equals(HeadAgent.this.getPicType()))
          continue;
        GAHelper.instance().contextStatisticsEvent(HeadAgent.this.getContext(), "view_bigphoto", null, 0, "tap");
      }
    }
  };
  protected final View.OnClickListener mEmptyViewClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (HeadAgent.this.allowUploadEntrance())
      {
        GAHelper.instance().contextStatisticsEvent(HeadAgent.this.getContext(), "toupload", null, "tap");
        UploadPhotoUtil.uploadShopPhoto(HeadAgent.this.getContext(), HeadAgent.this.getShop());
      }
    }
  };
  private final HorizontalImageGallery.OnGalleryImageClickListener mOnGalleryImageClickListener = new HorizontalImageGallery.OnGalleryImageClickListener()
  {
    public void onGalleryImageClick(int paramInt1, int paramInt2, Drawable paramDrawable)
    {
      Object localObject = HeadAgent.this.getShop();
      if (localObject == null)
        return;
      if (paramInt1 == paramInt2 - 1)
      {
        paramInt2 = ((DPObject)localObject).getInt("ID");
        paramDrawable = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
        paramDrawable.putExtra("shopId", paramInt2);
        paramDrawable.putExtra("objShop", (Parcelable)localObject);
        paramDrawable.putExtra("enableUpload", HeadAgent.this.allowUploadEntrance());
        HeadAgent.this.startActivity(paramDrawable);
        GAHelper.instance().contextStatisticsEvent(HeadAgent.this.getContext(), "view_multiphoto", "全部图片", paramInt1, "tap");
        return;
      }
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphotoandmore"));
      localIntent.putExtra("position", paramInt1);
      localIntent.putExtra("totalPicCount", HeadAgent.this.totalCount);
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(localObject);
      localIntent.putParcelableArrayListExtra("arrShopObjs", localArrayList);
      localIntent.putParcelableArrayListExtra("pageList", HeadAgent.this.pageList);
      if (paramDrawable != null)
      {
        localObject = new ByteArrayOutputStream();
        ((BitmapDrawable)paramDrawable).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject);
        localIntent.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject).toByteArray());
      }
      HeadAgent.this.getContext().startActivity(localIntent);
      GAHelper.instance().contextStatisticsEvent(HeadAgent.this.getContext(), "view_multiphoto", null, paramInt1, "tap");
    }
  };
  private ArrayList<DPObject> pageList;
  String shopType;
  protected DefaultShopInfoHeaderView topView;
  private int totalCount;

  public HeadAgent(Object paramObject)
  {
    super(paramObject);
  }

  private boolean allowUploadEntrance()
  {
    DPObject localDPObject = getShop();
    Object localObject;
    if (localDPObject != null)
    {
      localObject = localDPObject.getObject("ClientShopStyle");
      if (localObject != null)
        localObject = ((DPObject)localObject).getString("ShopView");
    }
    return (localDPObject.getInt("Status") != 1) && (localDPObject.getInt("Status") != 4) && (!"gov_agency".equals(localObject));
  }

  private void initHeaderView()
  {
    this.shopType = getPicType();
    if ("multipic".equals(this.shopType))
      setMultiPicInfo();
    if (this.topView == null)
    {
      if ("multipic".equals(this.shopType))
        this.topView = ((MultiHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_multi_header_layout, getParentView(), false));
    }
    else
      return;
    if ("bigpic".equals(this.shopType))
    {
      this.topView = ((BigHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_big_header_layout, getParentView(), false));
      return;
    }
    if ("multifixed".equals(this.shopType))
    {
      this.topView = ((MultiFixedHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_multi_fixed_header_layout, getParentView(), false));
      return;
    }
    this.topView = ((DefaultShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_common_header_layout, getParentView(), false));
  }

  private void resetHeaderView()
  {
    if (this.topView != null)
    {
      if (this.shopType != null)
        break label32;
      if (!"smallpic".equals(getPicType()))
        this.topView = null;
    }
    label32: 
    do
      return;
    while (this.shopType.equals(getPicType()));
    this.topView = null;
  }

  private void setMultiPicInfo()
  {
    DPObject[] arrayOfDPObject = getShop().getArray("AdvancedPics");
    if (arrayOfDPObject == null);
    while (true)
    {
      return;
      this.totalCount = getShop().getInt("PicCount");
      int j = arrayOfDPObject.length;
      this.pageList = new ArrayList(j);
      int i = 0;
      while (i < j)
      {
        DPObject localDPObject = new DPObject().edit().putInt("ShopID", shopId()).putString("Url", arrayOfDPObject[i].getString("Url")).putTime("Time", arrayOfDPObject[i].getLong("UploadTime")).putString("Name", arrayOfDPObject[i].getString("Name")).generate();
        this.pageList.add(localDPObject);
        i += 1;
      }
    }
  }

  public View getView()
  {
    return this.topView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    paramBundle = getShop();
    removeAllCells();
    if (paramBundle == null);
    do
    {
      do
        return;
      while (getFragment() == null);
      resetHeaderView();
      initHeaderView();
      this.topView.setShop(paramBundle, getShopStatus());
      this.topView.setIconClickListener(this.iconClickListener);
      if (("multipic".equals(this.shopType)) && ((this.topView instanceof MultiHeaderView)))
      {
        ((MultiHeaderView)this.topView).setOnGalleryImageClickListener(this.mOnGalleryImageClickListener);
        ((MultiHeaderView)this.topView).setOnEmptyClickedListener(this.mEmptyViewClickListener);
      }
      addCell("0200Basic.05Info", this.topView);
    }
    while (!(getContext() instanceof ShopInfoActivity));
    ((ShopInfoActivity)getContext()).getSpeedMonitorHelper().setResponseTime(1001, System.currentTimeMillis());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.HeadAgent
 * JD-Core Version:    0.6.0
 */
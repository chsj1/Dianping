package com.dianping.base.widget;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.DPActivity;
import com.dianping.app.DPFragment;
import com.dianping.archive.DPObject;
import com.dianping.model.ShopImageData;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class MultiPhotoGridView extends ExpandableHeightGridView
  implements AdapterView.OnItemClickListener
{
  private static final int BACKGROUND_CARAMER = R.drawable.background_caramer;
  private static final int BACKGROUND_PHOTO = R.drawable.background_add_photo;
  private static final int ITEM_PHOTO = R.layout.upload_photo_item;
  private static final int ITEM_REVIEW_PHOTO = R.layout.upload_review_photo_item;
  public static final int UPLOAD_PHOTO_EDIT = 10086;
  public int MAX_PHOTO_COUNT;
  ArrayList<ShopImageData> arrayPhotos;
  String cateName;
  DPFragment fragment;
  String gaCategory = "addphoto5";
  int itemId = ITEM_PHOTO;
  PhotoGridAdapter photoAdapter;
  int photoHeight;
  String photoName;
  int photoWidth;
  int resId = BACKGROUND_CARAMER;
  DPObject shop;

  public MultiPhotoGridView(Context paramContext)
  {
    super(paramContext);
    initAdapter(paramContext);
  }

  public MultiPhotoGridView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initAdapter(paramContext);
  }

  public MultiPhotoGridView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initAdapter(paramContext);
  }

  public void initAdapter(Context paramContext)
  {
    this.photoAdapter = new PhotoGridAdapter(paramContext);
    setAdapter(this.photoAdapter);
    setOnItemClickListener(this);
  }

  public void notifyDataSetChanged()
  {
    this.photoAdapter.notifyDataSetChanged();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (!Environment.getExternalStorageState().equals("mounted"))
    {
      Toast.makeText(getContext(), "您手机里没有内存卡，无法上传照片", 1).show();
      return;
    }
    if (paramInt == this.arrayPhotos.size())
    {
      ((DPActivity)getContext()).statisticsEvent(this.gaCategory, this.gaCategory + "_photo", "", 0);
      if ((this.MAX_PHOTO_COUNT == -1) || (paramInt < this.MAX_PHOTO_COUNT))
      {
        paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://uploadphotogalleryalbumpicture"));
        paramAdapterView.putExtra("MaxNum", this.MAX_PHOTO_COUNT);
        paramAdapterView.putExtra("SelectNum", this.arrayPhotos.size());
        paramAdapterView.putExtra("objShop", this.shop);
        if (!TextUtils.isEmpty(this.cateName))
          paramAdapterView.putExtra("category", this.cateName);
        if (!TextUtils.isEmpty(this.photoName))
          paramAdapterView.putExtra("photoName", this.photoName);
        if (this.fragment != null)
        {
          this.fragment.startActivityForResult(paramAdapterView, 3021);
          return;
        }
        ((DPActivity)getContext()).startActivityForResult(paramAdapterView, 3021);
        return;
      }
      new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("最多同时支持上传" + this.MAX_PHOTO_COUNT + "张图片").setNegativeButton("确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      }).show();
      return;
    }
    ((DPActivity)getContext()).statisticsEvent(this.gaCategory, this.gaCategory + "_edit", "", 0);
    paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://photouploadedit"));
    paramAdapterView.putExtra("objShop", this.shop);
    paramAdapterView.putExtra("position", paramInt);
    paramAdapterView.putExtra("pageList", this.arrayPhotos);
    if (getContext().toString().contains("AddReviewActivity"))
      paramAdapterView.putExtra("isRotate", false);
    if (this.fragment != null)
    {
      this.fragment.startActivityForResult(paramAdapterView, 10086);
      return;
    }
    ((DPActivity)getContext()).startActivityForResult(paramAdapterView, 10086);
  }

  public void setFragment(DPFragment paramDPFragment)
  {
    this.fragment = paramDPFragment;
  }

  public void setGaCategory(String paramString)
  {
    this.gaCategory = paramString;
  }

  public void setPhotoCateName(String paramString1, String paramString2)
  {
    this.cateName = paramString1;
    this.photoName = paramString2;
  }

  public void setPhotoInfo(ArrayList<ShopImageData> paramArrayList, DPObject paramDPObject, int paramInt1, int paramInt2, int paramInt3)
  {
    this.arrayPhotos = paramArrayList;
    this.shop = paramDPObject;
    this.MAX_PHOTO_COUNT = paramInt1;
    this.photoWidth = paramInt2;
    this.photoHeight = paramInt3;
  }

  public void setReviewStyle(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      i = BACKGROUND_PHOTO;
      this.resId = i;
      if (!paramBoolean)
        break label34;
    }
    label34: for (int i = ITEM_REVIEW_PHOTO; ; i = ITEM_PHOTO)
    {
      this.itemId = i;
      return;
      i = BACKGROUND_CARAMER;
      break;
    }
  }

  class PhotoGridAdapter extends BaseAdapter
  {
    Context mContext;

    public PhotoGridAdapter(Context arg2)
    {
      Object localObject;
      this.mContext = localObject;
    }

    public int getCount()
    {
      if (MultiPhotoGridView.this.arrayPhotos == null)
        return 0;
      if (MultiPhotoGridView.this.arrayPhotos.size() >= 9)
        return MultiPhotoGridView.this.arrayPhotos.size();
      return MultiPhotoGridView.this.arrayPhotos.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramInt == MultiPhotoGridView.this.arrayPhotos.size())
      {
        paramView = LayoutInflater.from(this.mContext).inflate(R.layout.upload_photo_item_add, paramViewGroup, false);
        paramView.getLayoutParams().width = MultiPhotoGridView.this.photoWidth;
        paramView.getLayoutParams().height = MultiPhotoGridView.this.photoHeight;
        paramView.setBackgroundResource(MultiPhotoGridView.this.resId);
        return paramView;
      }
      if ((paramView != null) && (paramView.getId() == MultiPhotoGridView.this.itemId));
      while (true)
      {
        paramViewGroup = (NetworkThumbView)paramView.findViewById(R.id.image);
        paramViewGroup.placeholderLoading = R.drawable.placeholder_loading;
        paramViewGroup.placeholderError = R.drawable.placeholder_error;
        paramViewGroup.placeholderEmpty = R.drawable.placeholder_empty;
        if (((ShopImageData)MultiPhotoGridView.this.arrayPhotos.get(paramInt)).oriPath != null)
          paramViewGroup.setImage(((ShopImageData)MultiPhotoGridView.this.arrayPhotos.get(paramInt)).oriPath, ((ShopImageData)MultiPhotoGridView.this.arrayPhotos.get(paramInt)).direction);
        if ((MultiPhotoGridView.this.itemId == MultiPhotoGridView.ITEM_PHOTO) && (((ShopImageData)MultiPhotoGridView.this.arrayPhotos.get(paramInt)).cateName != null))
          ((TextView)paramView.findViewById(R.id.image_desc)).setText(((ShopImageData)MultiPhotoGridView.this.arrayPhotos.get(paramInt)).cateName);
        paramViewGroup = (FrameLayout)paramView.findViewById(R.id.frame);
        paramViewGroup.getLayoutParams().width = MultiPhotoGridView.this.photoWidth;
        paramViewGroup.getLayoutParams().height = MultiPhotoGridView.this.photoHeight;
        return paramView;
        paramView = LayoutInflater.from(this.mContext).inflate(MultiPhotoGridView.this.itemId, paramViewGroup, false);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.MultiPhotoGridView
 * JD-Core Version:    0.6.0
 */
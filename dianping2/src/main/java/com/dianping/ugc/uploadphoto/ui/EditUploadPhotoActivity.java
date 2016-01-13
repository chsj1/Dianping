package com.dianping.ugc.uploadphoto.ui;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.widget.HorizontalImageGallery;
import com.dianping.base.widget.HorizontalImageGallery.OnGalleryImageClickListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.base.widget.phototag.PictureDecalView.DecalItem;
import com.dianping.base.widget.phototag.PictureTagLayout;
import com.dianping.base.widget.phototag.PictureTagView.Direction;
import com.dianping.base.widget.phototag.PictureTagView.TagItem;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.ugc.model.UGCDecalItem;
import com.dianping.ugc.model.UploadPhotoData;
import com.dianping.ugc.model.UploadPhotoDecalData;
import com.dianping.ugc.model.UploadPhotoTagData;
import com.dianping.ugc.uploadphoto.utils.DecalManager;
import com.dianping.ugc.uploadphoto.utils.DecalManager.OnDownloadFinishListener;
import com.dianping.ugc.widget.DecalView;
import com.dianping.ugc.widget.DecalView.State;
import com.dianping.ugc.widget.NotifySizeChangedFrameLayout;
import com.dianping.ugc.widget.NotifySizeChangedFrameLayout.OnSizeChangedListener;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaButton;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.ArrayList<Lcom.dianping.ugc.model.UploadPhotoDecalData;>;
import java.util.ArrayList<Lcom.dianping.ugc.model.UploadPhotoTagData;>;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class EditUploadPhotoActivity extends NovaActivity
  implements DecalManager.OnDownloadFinishListener
{
  private static int HorizontalImageGalleryHeight = 0;
  private static final int INVALID_TAG_ID = -1;
  private static final int INVALID_TAG_POSITON = -1;
  private static final int MAX_DECAL = 10;
  private static final int MAX_INTEREST_TAG = 10;
  private static final int MAX_POI_TAG = 1;
  private static final int REQUEST_CODE_ADD_CATEGORY = 4;
  private static final int REQUEST_CODE_ADD_TAG = 2;
  private static final int REQUEST_CODE_AUTO_ADD_TAG = 3;
  private static final int REQUEST_CODE_EDIT_TAG = 1;
  private static final int REQUEST_CODE_UPDATE_DECALS = 5;
  private static final String TAG = "EditUploadPhotoActivity";
  private boolean enableCategory;
  private boolean enableDecal;
  private boolean enablePOI;
  private boolean enableTag;
  private HashMap<String, ArrayList<UGCDecalItem>> mDecalInfos = new HashMap();
  private MApiRequest mDecalRequest;
  private HashMap<String, DecalView> mDecalThumbViews = new HashMap();
  private LinearLayout mDecalsLayout;
  private String mDefaultTag;
  private EditPhotoViewerAdapter mEditPhotoViewerAdapter;
  private int mEditingTagId = -1;
  private int mEditingTagPositionX = -1;
  private int mEditingTagPositionY = -1;
  private View mHintLayout;
  private HorizontalImageGallery mHorizontalImageGallery;
  private String mNextScheme;
  private int mPhotoIndex = 0;
  private int mPhotoSummaryBottomMargin;
  private ArrayList<UploadPhotoData> mPhotos = new ArrayList();
  private String mShopCategories;
  private ViewPager mViewPhoto;
  private int orderId;
  private int shopId;

  private void addAutoTag()
  {
    int i = 0;
    int j = 0;
    Object localObject = (UploadPhotoData)this.mPhotos.get(this.mPhotoIndex);
    Iterator localIterator = ((UploadPhotoData)localObject).tags.iterator();
    while (localIterator.hasNext())
    {
      UploadPhotoTagData localUploadPhotoTagData = (UploadPhotoTagData)localIterator.next();
      if (localUploadPhotoTagData.tagType == 2)
      {
        i += 1;
        continue;
      }
      if (localUploadPhotoTagData.tagType != 1)
        continue;
      j += 1;
    }
    if ((j == 10) && (i == 1) && (((UploadPhotoData)localObject).poiShopId != null) && (((UploadPhotoData)localObject).poiShopName != null))
    {
      localObject = Toast.makeText(this, R.string.ugc_toast_max_tag, 0);
      ((Toast)localObject).setGravity(17, 0, 0);
      ((Toast)localObject).show();
      return;
    }
    localObject = Uri.parse("dianping://edituploadphotocategory").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("enableCategory", String.valueOf(this.enableCategory));
    ((Uri.Builder)localObject).appendQueryParameter("enablePOI", String.valueOf(this.enablePOI));
    ((Uri.Builder)localObject).appendQueryParameter("enableTag", String.valueOf(this.enableTag));
    localObject = new Intent("android.intent.action.VIEW", ((Uri.Builder)localObject).build());
    ((Intent)localObject).putExtra("shopId", this.shopId);
    ((Intent)localObject).putExtra("orderid", this.orderId);
    ((Intent)localObject).putExtra("index", this.mPhotoIndex);
    ((Intent)localObject).putExtra("tagId", -1);
    ((Intent)localObject).putExtra("photoData", (Parcelable)this.mPhotos.get(this.mPhotoIndex));
    startActivityForResult((Intent)localObject, 3);
  }

  private void addCategory()
  {
    Log.d("EditUploadPhotoActivity", "addCategory");
    Object localObject = Uri.parse("dianping://edituploadphotocategory").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("enableCategory", String.valueOf(this.enableCategory));
    ((Uri.Builder)localObject).appendQueryParameter("enablePOI", String.valueOf(this.enablePOI));
    ((Uri.Builder)localObject).appendQueryParameter("enableTag", String.valueOf(this.enableTag));
    localObject = new Intent("android.intent.action.VIEW", ((Uri.Builder)localObject).build());
    ((Intent)localObject).putExtra("shopId", this.shopId);
    ((Intent)localObject).putExtra("orderid", this.orderId);
    ((Intent)localObject).putExtra("category", this.mShopCategories);
    ((Intent)localObject).putExtra("index", this.mPhotoIndex);
    ((Intent)localObject).putExtra("photoData", (Parcelable)this.mPhotos.get(this.mPhotoIndex));
    startActivityForResult((Intent)localObject, 4);
  }

  private void addGuideView(ViewGroup paramViewGroup, FrameLayout.LayoutParams paramLayoutParams)
  {
    LinearLayout localLinearLayout = new LinearLayout(this);
    localLinearLayout.setOrientation(1);
    localLinearLayout.setGravity(17);
    localLinearLayout.setOnClickListener(new View.OnClickListener(paramViewGroup, localLinearLayout)
    {
      public void onClick(View paramView)
      {
        this.val$root.removeView(this.val$handTipLayout);
      }
    });
    FrameLayout localFrameLayout = new FrameLayout(this);
    Object localObject1 = new View(this);
    Object localObject2 = new FrameLayout.LayoutParams(ViewUtils.dip2px(this, 40.0F), ViewUtils.dip2px(this, 40.0F));
    ((FrameLayout.LayoutParams)localObject2).gravity = 17;
    Object localObject3 = new ShapeDrawable(new OvalShape());
    ((ShapeDrawable)localObject3).getPaint().setStyle(Paint.Style.STROKE);
    ((ShapeDrawable)localObject3).getPaint().setColor(-1);
    ((ShapeDrawable)localObject3).getPaint().setStrokeWidth(0.0F);
    ((View)localObject1).setBackgroundDrawable((Drawable)localObject3);
    localObject3 = new AnimationSet(true);
    Object localObject4 = new ScaleAnimation(1.0F, 1.5F, 1.0F, 1.5F, 1, 0.5F, 1, 0.5F);
    ((ScaleAnimation)localObject4).setDuration(1500L);
    ((ScaleAnimation)localObject4).setRepeatCount(-1);
    ((ScaleAnimation)localObject4).setRepeatMode(1);
    ((AnimationSet)localObject3).addAnimation((Animation)localObject4);
    localObject4 = new AlphaAnimation(1.0F, 0.0F);
    ((AlphaAnimation)localObject4).setDuration(1500L);
    ((AlphaAnimation)localObject4).setRepeatCount(-1);
    ((AlphaAnimation)localObject4).setRepeatMode(1);
    ((AnimationSet)localObject3).addAnimation((Animation)localObject4);
    ((View)localObject1).startAnimation((Animation)localObject3);
    localObject3 = new ImageView(this);
    localObject4 = new FrameLayout.LayoutParams(-2, -2);
    ((FrameLayout.LayoutParams)localObject4).gravity = 17;
    ((ImageView)localObject3).setImageDrawable(getResources().getDrawable(R.drawable.ugc_uploadphoto_guide));
    localFrameLayout.addView((View)localObject1, (ViewGroup.LayoutParams)localObject2);
    localFrameLayout.addView((View)localObject3, (ViewGroup.LayoutParams)localObject4);
    localObject1 = new TextView(this);
    localObject2 = new LinearLayout.LayoutParams(-2, -2);
    ((LinearLayout.LayoutParams)localObject2).gravity = 17;
    ((TextView)localObject1).setGravity(17);
    ((LinearLayout.LayoutParams)localObject2).topMargin = ViewUtils.dip2px(this, 10.0F);
    ((TextView)localObject1).setText("点击图片，添加标签");
    ((TextView)localObject1).setTextColor(getResources().getColor(R.color.white));
    localLinearLayout.addView(localFrameLayout);
    localLinearLayout.addView((View)localObject1, (ViewGroup.LayoutParams)localObject2);
    paramViewGroup.addView(localLinearLayout, paramLayoutParams);
  }

  private void addTag(int paramInt1, int paramInt2, int paramInt3)
  {
    Log.d("EditUploadPhotoActivity", "addTag x=" + paramInt1 + " y=" + paramInt2 + " tagId=" + paramInt3);
    int i = 0;
    int j = 0;
    Object localObject = (UploadPhotoData)this.mPhotos.get(this.mPhotoIndex);
    Iterator localIterator = ((UploadPhotoData)localObject).tags.iterator();
    while (localIterator.hasNext())
    {
      UploadPhotoTagData localUploadPhotoTagData = (UploadPhotoTagData)localIterator.next();
      if (localUploadPhotoTagData.tagType == 2)
      {
        i += 1;
        continue;
      }
      if (localUploadPhotoTagData.tagType != 1)
        continue;
      j += 1;
    }
    if ((j == 10) && (i == 1) && (((UploadPhotoData)localObject).poiShopId != null) && (((UploadPhotoData)localObject).poiShopName != null))
    {
      localObject = Toast.makeText(this, R.string.ugc_toast_max_tag, 0);
      ((Toast)localObject).setGravity(17, 0, 0);
      ((Toast)localObject).show();
      return;
    }
    this.mEditingTagId = paramInt3;
    this.mEditingTagPositionX = paramInt1;
    this.mEditingTagPositionY = paramInt2;
    ((UploadPhotoData)this.mPhotos.get(this.mPhotoIndex)).tags.add(new UploadPhotoTagData(paramInt3, -1.0D, -1.0D, true));
    localObject = Uri.parse("dianping://edituploadphotocategory").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("enableCategory", String.valueOf(this.enableCategory));
    ((Uri.Builder)localObject).appendQueryParameter("enablePOI", String.valueOf(this.enablePOI));
    ((Uri.Builder)localObject).appendQueryParameter("enableTag", String.valueOf(this.enableTag));
    localObject = new Intent("android.intent.action.VIEW", ((Uri.Builder)localObject).build());
    ((Intent)localObject).putExtra("shopId", this.shopId);
    ((Intent)localObject).putExtra("orderid", this.orderId);
    ((Intent)localObject).putExtra("category", this.mShopCategories);
    ((Intent)localObject).putExtra("index", this.mPhotoIndex);
    ((Intent)localObject).putExtra("photoData", (Parcelable)this.mPhotos.get(this.mPhotoIndex));
    ((Intent)localObject).putExtra("tagId", paramInt3);
    startActivityForResult((Intent)localObject, 2);
  }

  private void clearThumbDecals()
  {
    Log.d("EditUploadPhotoActivity", "clearThumbDecals");
    this.mDecalsLayout.removeAllViews();
    this.mDecalThumbViews.clear();
  }

  private void editTag(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    Log.d("EditUploadPhotoActivity", "editTag tagId=" + paramInt3);
    this.mEditingTagId = paramInt3;
    this.mEditingTagPositionX = paramInt1;
    this.mEditingTagPositionY = paramInt2;
    Object localObject = ((UploadPhotoData)this.mPhotos.get(this.mPhotoIndex)).tags.iterator();
    while (((Iterator)localObject).hasNext())
    {
      UploadPhotoTagData localUploadPhotoTagData = (UploadPhotoTagData)((Iterator)localObject).next();
      if (localUploadPhotoTagData.tagId != paramInt3)
        continue;
      localUploadPhotoTagData.isRight = paramBoolean;
    }
    localObject = Uri.parse("dianping://edituploadphotocategory").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("enableCategory", String.valueOf(this.enableCategory));
    ((Uri.Builder)localObject).appendQueryParameter("enablePOI", String.valueOf(this.enablePOI));
    ((Uri.Builder)localObject).appendQueryParameter("enableTag", String.valueOf(this.enableTag));
    localObject = new Intent("android.intent.action.VIEW", ((Uri.Builder)localObject).build());
    ((Intent)localObject).putExtra("shopId", this.shopId);
    ((Intent)localObject).putExtra("orderid", this.orderId);
    ((Intent)localObject).putExtra("category", this.mShopCategories);
    ((Intent)localObject).putExtra("index", this.mPhotoIndex);
    ((Intent)localObject).putExtra("photoData", (Parcelable)this.mPhotos.get(this.mPhotoIndex));
    ((Intent)localObject).putExtra("tagId", paramInt3);
    startActivityForResult((Intent)localObject, 1);
  }

  private void exit(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1)
    {
      localObject = new AlertDialog.Builder(this);
      ((AlertDialog.Builder)localObject).setTitle(R.string.ugc_dialog_hint);
      ((AlertDialog.Builder)localObject).setMessage(R.string.ugc_dialog_exit_editphoto);
      ((AlertDialog.Builder)localObject).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          EditUploadPhotoActivity.this.exit(false, true);
        }
      });
      ((AlertDialog.Builder)localObject).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          GAHelper.instance().contextStatisticsEvent(EditUploadPhotoActivity.this, "cancelEdit", null, 2147483647, "tap");
        }
      });
      ((AlertDialog.Builder)localObject).show();
      return;
    }
    Object localObject = new Intent();
    ((Intent)localObject).putParcelableArrayListExtra("photos", this.mPhotos);
    if (!paramBoolean2)
      setResult(-1, (Intent)localObject);
    finish();
  }

  private void initViews()
  {
    Log.d("EditUploadPhotoActivity", "initViews");
    setContentView(R.layout.ugc_photo_editupload_layout);
    Object localObject1 = (NotifySizeChangedFrameLayout)findViewById(R.id.photo_editupload_layout);
    ((NotifySizeChangedFrameLayout)localObject1).setOnSizeChangedListener(new NotifySizeChangedFrameLayout.OnSizeChangedListener((NotifySizeChangedFrameLayout)localObject1)
    {
      public void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
      {
        if ((paramInt3 != 0) && (paramInt4 != 0))
          this.val$content.setOnSizeChangedListener(null);
        Log.d("EditUploadPhotoActivity", "onSizeChanged content height=" + paramInt1 + " width=" + paramInt2 + " oldwidth=" + paramInt3 + " oldheight=" + paramInt4);
        paramInt1 = this.val$content.getHeight();
        paramInt2 = this.val$content.getWidth();
        EditUploadPhotoActivity.access$902(EditUploadPhotoActivity.this, paramInt1 - EditUploadPhotoActivity.HorizontalImageGalleryHeight - paramInt2);
        if (EditUploadPhotoActivity.this.mPhotoSummaryBottomMargin < ViewUtils.dip2px(EditUploadPhotoActivity.this, 87.0F))
          EditUploadPhotoActivity.access$902(EditUploadPhotoActivity.this, ViewUtils.dip2px(EditUploadPhotoActivity.this, 87.0F));
        Log.d("EditUploadPhotoActivity", "onSizeChanged content height=" + paramInt1 + " width=" + paramInt2 + " tagHintHeight=" + EditUploadPhotoActivity.this.mPhotoSummaryBottomMargin);
        EditUploadPhotoActivity.this.mHintLayout.getLayoutParams().height = EditUploadPhotoActivity.this.mPhotoSummaryBottomMargin;
        EditUploadPhotoActivity.access$1402(EditUploadPhotoActivity.this, new EditUploadPhotoActivity.EditPhotoViewerAdapter(EditUploadPhotoActivity.this, EditUploadPhotoActivity.this));
        EditUploadPhotoActivity.this.mViewPhoto.setAdapter(EditUploadPhotoActivity.this.mEditPhotoViewerAdapter);
        EditUploadPhotoActivity.this.mViewPhoto.setCurrentItem(EditUploadPhotoActivity.this.mPhotoIndex);
        if (!DPActivity.preferences().getBoolean("ugc_uploadphoto_guide", false))
        {
          FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(paramInt2, paramInt2);
          localLayoutParams.topMargin = EditUploadPhotoActivity.HorizontalImageGalleryHeight;
          localLayoutParams.gravity = 48;
          EditUploadPhotoActivity.this.addGuideView(this.val$content, localLayoutParams);
          DPActivity.preferences().edit().putBoolean("ugc_uploadphoto_guide", true).commit();
        }
      }
    });
    this.mHorizontalImageGallery = ((HorizontalImageGallery)findViewById(R.id.photo_editupload_gallery));
    int j = this.mPhotos.size();
    localObject1 = new String[j];
    int i = 0;
    while (i < j)
    {
      localObject1[i] = ((UploadPhotoData)this.mPhotos.get(i)).photoPath;
      i += 1;
    }
    this.mHorizontalImageGallery.addImages(localObject1, false);
    this.mHorizontalImageGallery.setSelectedImage(this.mPhotoIndex);
    this.mViewPhoto = ((ViewPager)findViewById(R.id.photo_editupload_preview));
    this.mViewPhoto.setOffscreenPageLimit(9);
    this.mViewPhoto.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        return true;
      }
    });
    this.mViewPhoto.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
    {
      public void onPageScrollStateChanged(int paramInt)
      {
      }

      public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
      {
      }

      public void onPageSelected(int paramInt)
      {
        EditUploadPhotoActivity.access$1502(EditUploadPhotoActivity.this, paramInt);
        EditUploadPhotoActivity.this.mHorizontalImageGallery.setSelectedImage(EditUploadPhotoActivity.this.mPhotoIndex);
      }
    });
    this.mHorizontalImageGallery.setOnGalleryImageClickListener(new HorizontalImageGallery.OnGalleryImageClickListener()
    {
      public void onGalleryImageClick(int paramInt1, int paramInt2, Drawable paramDrawable)
      {
        EditUploadPhotoActivity.this.mViewPhoto.setCurrentItem(paramInt1);
      }
    });
    this.mHintLayout = findViewById(R.id.photo_editupload_hint_layout);
    localObject1 = findViewById(R.id.photo_editupload_category_layout);
    Object localObject2 = findViewById(R.id.photo_editupload_tag_layout);
    this.mDecalsLayout = ((LinearLayout)findViewById(R.id.photo_editupload_decals_layout));
    if (this.enableCategory)
    {
      ((View)localObject1).setVisibility(0);
      ((View)localObject2).setVisibility(8);
    }
    while (true)
    {
      findViewById(R.id.photo_editupload_decal).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (EditUploadPhotoActivity.this.mEditPhotoViewerAdapter.getDecalsCountByPosition(EditUploadPhotoActivity.this.mPhotoIndex) >= 10)
          {
            Toast.makeText(EditUploadPhotoActivity.this, R.string.ugc_toast_max_decal, 0).show();
            return;
          }
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://addphotodecal"));
          EditUploadPhotoActivity.this.startActivityForResult(paramView, 5);
        }
      });
      findViewById(R.id.photo_editupload_tag).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          EditUploadPhotoActivity.this.addAutoTag();
        }
      });
      localObject1 = (FrameLayout)LayoutInflater.from(this).inflate(R.layout.ugc_photo_next, null, false);
      localObject2 = (NovaButton)((FrameLayout)localObject1).findViewById(R.id.photo_next);
      ((NovaButton)localObject2).setGAString("next");
      ((NovaButton)localObject2).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          EditUploadPhotoActivity.this.next();
        }
      });
      getTitleBar().addRightViewItem((View)localObject1, "next", null);
      localObject1 = new TextView(this);
      ((TextView)localObject1).setText(R.string.cancel);
      ((TextView)localObject1).setGravity(17);
      ((TextView)localObject1).setPadding((int)getResources().getDimension(R.dimen.title_bar_button_margin), 0, 0, 0);
      ((TextView)localObject1).setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
      ((TextView)localObject1).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_15));
      ((TextView)localObject1).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          EditUploadPhotoActivity.this.onBackPressed();
        }
      });
      getTitleBar().setCustomLeftView((View)localObject1);
      setTitle(R.string.ugc_edit_uploadphoto);
      return;
      if ((!this.enableTag) && (!this.enablePOI) && (!this.enableDecal))
        continue;
      ((View)localObject1).setVisibility(8);
      ((View)localObject2).setVisibility(0);
    }
  }

  private void insertDecal(PictureDecalView.DecalItem paramDecalItem)
  {
    insertDecal(paramDecalItem, -1);
  }

  private void insertDecal(PictureDecalView.DecalItem paramDecalItem, int paramInt)
  {
    int i = paramInt;
    int j = i;
    if (i < 0)
      j = this.mPhotoIndex;
    if (this.mEditPhotoViewerAdapter.getDecalsCountByPosition(j) >= 10)
    {
      Toast.makeText(this, R.string.ugc_toast_max_decal, 0).show();
      return;
    }
    this.mEditPhotoViewerAdapter.addDecal(j, paramDecalItem);
    DecalManager.getInstance().getDecal(paramDecalItem.groupId, paramDecalItem.name, paramDecalItem.url, paramInt, true);
  }

  private void insertDecal(String paramString1, String paramString2, Bitmap paramBitmap, String paramString3)
  {
    if (this.mEditPhotoViewerAdapter.getDecalsCountByPosition(this.mPhotoIndex) >= 10)
    {
      Toast.makeText(this, R.string.ugc_toast_max_decal, 0).show();
      return;
    }
    this.mEditPhotoViewerAdapter.addDecal(this.mPhotoIndex, paramString1, paramString2, paramBitmap, paramString3);
    DecalManager.getInstance().getDecal(paramString1, paramString2, paramString3);
  }

  private void next()
  {
    Object localObject = this.mEditPhotoViewerAdapter.getPictureTagLayouts();
    if (localObject != null)
    {
      int k = 0;
      int j = 0;
      i = 0;
      while (i < localObject.length)
      {
        if (localObject[i].hasDecalLoading())
          k = 1;
        if (localObject[i].hasDecalRetry())
          j = 1;
        i += 1;
      }
      if (k != 0)
      {
        Toast.makeText(this, "贴纸加载中，请稍候", 1).show();
        return;
      }
      if (j != 0)
      {
        Toast.makeText(this, "贴纸加载失败，请检查网络情况后重试", 1).show();
        return;
      }
    }
    int i = 0;
    while (i < this.mPhotos.size())
    {
      ((UploadPhotoData)this.mPhotos.get(i)).tags.clear();
      ((UploadPhotoData)this.mPhotos.get(i)).tags.addAll(this.mEditPhotoViewerAdapter.getTagsByPosition(i));
      ((UploadPhotoData)this.mPhotos.get(i)).decals.clear();
      ((UploadPhotoData)this.mPhotos.get(i)).decals.addAll(this.mEditPhotoViewerAdapter.getDecalsByPosition(i));
      i += 1;
    }
    if (this.mNextScheme != null);
    try
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse(this.mNextScheme));
      ((Intent)localObject).putParcelableArrayListExtra("photos", this.mPhotos);
      startActivity((Intent)localObject);
      localObject = new Intent();
      ((Intent)localObject).putParcelableArrayListExtra("photos", this.mPhotos);
      setResult(-1, (Intent)localObject);
      finish();
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  private void processParams()
  {
    Object localObject = getIntent().getParcelableArrayListExtra("photos");
    if (localObject != null)
      this.mPhotos = ((ArrayList)localObject);
    while (true)
    {
      this.shopId = getIntParam("shopId");
      this.orderId = getIntParam("orderid");
      this.enablePOI = getBooleanParam("enablePOI");
      this.enableTag = getBooleanParam("enableTag");
      this.enableCategory = getBooleanParam("enableCategory");
      this.enableDecal = getBooleanParam("enableDecal");
      this.mDefaultTag = getStringParam("interesttag");
      this.mShopCategories = getStringParam("category");
      boolean bool;
      if (!TextUtils.isEmpty(this.mShopCategories))
      {
        bool = true;
        label121: this.enableCategory = bool;
        this.mPhotoIndex = getIntParam("currentIndex", 0);
        localObject = getStringParam("next");
        if (localObject == null)
          break label235;
      }
      try
      {
        label235: for (localObject = URLDecoder.decode((String)localObject, "utf-8"); ; localObject = null)
        {
          this.mNextScheme = ((String)localObject);
          return;
          localObject = getIntent().getStringArrayListExtra("selectedPhotos");
          if (localObject == null)
            break;
          localObject = ((ArrayList)localObject).iterator();
          while (((Iterator)localObject).hasNext())
          {
            String str = (String)((Iterator)localObject).next();
            UploadPhotoData localUploadPhotoData = new UploadPhotoData();
            localUploadPhotoData.photoPath = str;
            this.mPhotos.add(localUploadPhotoData);
          }
          bool = false;
          break label121;
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }

  private void removeTag(int paramInt)
  {
    Log.d("EditUploadPhotoActivity", "removeTag tagId=" + paramInt);
    ArrayList localArrayList = ((UploadPhotoData)this.mPhotos.get(this.mPhotoIndex)).tags;
    int i = 0;
    Iterator localIterator = localArrayList.iterator();
    while (true)
    {
      if (localIterator.hasNext())
      {
        if (((UploadPhotoTagData)localIterator.next()).tagId == paramInt)
          localArrayList.remove(i);
      }
      else
        return;
      i += 1;
    }
  }

  private void requestDecals(String paramString)
  {
    Log.d("EditUploadPhotoActivity", "requestDecals from groupId=" + paramString);
    Object localObject = (ArrayList)this.mDecalInfos.get(paramString);
    if (localObject != null)
    {
      Log.d("EditUploadPhotoActivity", "requestDecals from cache");
      showThumbDecals((ArrayList)localObject);
      return;
    }
    Log.d("EditUploadPhotoActivity", "requestDecals from web");
    localObject = Uri.parse("http://m.api.dianping.com/plaza/getdecalgroup.bin").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("groupid", paramString);
    this.mDecalRequest = BasicMApiRequest.mapiGet(((Uri.Builder)localObject).toString(), CacheType.NORMAL);
    mapiService().exec(this.mDecalRequest, new RequestHandler(paramString)
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (paramMApiRequest == EditUploadPhotoActivity.this.mDecalRequest)
          EditUploadPhotoActivity.access$2302(EditUploadPhotoActivity.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (paramMApiRequest == EditUploadPhotoActivity.this.mDecalRequest)
        {
          EditUploadPhotoActivity.access$2302(EditUploadPhotoActivity.this, null);
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest != null)
          {
            paramMApiRequest.getString("GroupName");
            paramMApiRequest = paramMApiRequest.getArray("DecalList");
            if (paramMApiRequest != null);
            for (int i = paramMApiRequest.length; ; i = 0)
            {
              paramMApiResponse = new ArrayList();
              EditUploadPhotoActivity.this.mDecalInfos.put(this.val$groupId, paramMApiResponse);
              int j = 0;
              while (j < i)
              {
                paramMApiResponse.add(new UGCDecalItem(String.valueOf(paramMApiRequest[j].getInt("GroupId")), paramMApiRequest[j].getString("Name"), paramMApiRequest[j].getString("SmallUrl"), paramMApiRequest[j].getString("BigUrl")));
                j += 1;
              }
            }
            EditUploadPhotoActivity.this.showThumbDecals(paramMApiResponse);
          }
        }
      }
    });
  }

  private void showRetry(String paramString)
  {
    this.mEditPhotoViewerAdapter.showRetry(this.mPhotoIndex, paramString);
  }

  private void showThumbDecals(ArrayList<UGCDecalItem> paramArrayList)
  {
    clearThumbDecals();
    if (paramArrayList != null);
    for (int i = paramArrayList.size(); ; i = 0)
    {
      int k = ViewUtils.dip2px(this, 5.0F);
      int j = 0;
      while (j < i)
      {
        UGCDecalItem localUGCDecalItem = (UGCDecalItem)paramArrayList.get(j);
        Log.d("EditUploadPhotoActivity", "show a thumb decal " + localUGCDecalItem);
        DecalView localDecalView = new DecalView(this);
        localDecalView.setSize((this.mPhotoSummaryBottomMargin - k * 2) / 2, (this.mPhotoSummaryBottomMargin - k * 2) / 2);
        localDecalView.setState(DecalView.State.IDLE);
        localDecalView.setDecalName(localUGCDecalItem.name);
        localDecalView.setCategory(localUGCDecalItem.category);
        localDecalView.setBackgroundColor(getResources().getColor(R.color.photo_decal_gray_bg));
        localDecalView.setOnClickListener(new View.OnClickListener(localUGCDecalItem, localDecalView)
        {
          public void onClick(View paramView)
          {
            EditUploadPhotoActivity.this.insertDecal(this.val$item.id, this.val$item.name + UUID.randomUUID().toString(), this.val$decalView.getDecal(), this.val$item.fullUrl);
          }
        });
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
        localLayoutParams.rightMargin = k;
        localLayoutParams.gravity = 16;
        this.mDecalsLayout.addView(localDecalView, localLayoutParams);
        this.mDecalThumbViews.put(localUGCDecalItem.name, localDecalView);
        DecalManager.getInstance().getDecal(localUGCDecalItem.id, localUGCDecalItem.name, localUGCDecalItem.thumbUrl, -1, false);
        j += 1;
      }
    }
  }

  private void updateDecal(String paramString, Bitmap paramBitmap)
  {
  }

  private void updateDecal(String paramString, Bitmap paramBitmap, int paramInt)
  {
    int i = paramInt;
    if (paramInt < 0)
      i = this.mPhotoIndex;
    this.mEditPhotoViewerAdapter.updateDecal(i, paramString, paramBitmap);
  }

  public String getPageName()
  {
    return "editPic";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    int i;
    Object localObject1;
    if ((paramInt1 == 1) || (paramInt1 == 2) || (paramInt1 == 4))
      if (paramInt2 == -1)
      {
        i = paramIntent.getIntExtra("index", -1);
        paramIntent = (UploadPhotoData)paramIntent.getParcelableExtra("photoData");
        if ((i != -1) && (paramIntent != null))
        {
          this.mPhotos.set(i, paramIntent);
          if (paramInt1 == 4)
            this.mEditPhotoViewerAdapter.updateCategory(i);
          if (((paramInt1 == 2) || (paramInt1 == 1)) && (this.mEditingTagId != -1))
          {
            paramIntent = paramIntent.tags.iterator();
            while (paramIntent.hasNext())
            {
              localObject1 = (UploadPhotoTagData)paramIntent.next();
              if (((UploadPhotoTagData)localObject1).tagId != this.mEditingTagId)
                continue;
              localObject2 = new PictureTagView.TagItem(((UploadPhotoTagData)localObject1).tagId);
              ((PictureTagView.TagItem)localObject2).x = this.mEditingTagPositionX;
              ((PictureTagView.TagItem)localObject2).y = this.mEditingTagPositionY;
              if (((UploadPhotoTagData)localObject1).tagType != 1)
                break label250;
              paramInt2 = 1;
              ((PictureTagView.TagItem)localObject2).type = paramInt2;
              if (!((UploadPhotoTagData)localObject1).isRight)
                break label269;
              paramIntent = PictureTagView.Direction.Right;
              label202: ((PictureTagView.TagItem)localObject2).direction = paramIntent;
              ((PictureTagView.TagItem)localObject2).tagContent = ((UploadPhotoTagData)localObject1).content;
              if (paramInt1 != 2)
                break label276;
              this.mEditPhotoViewerAdapter.insertTag(i, (PictureTagView.TagItem)localObject2);
            }
            label234: this.mEditingTagId = -1;
            this.mEditingTagPositionX = -1;
            this.mEditingTagPositionY = -1;
          }
        }
      }
    label250: 
    do
    {
      do
      {
        do
        {
          do
          {
            return;
            if (((UploadPhotoTagData)localObject1).tagType == 2)
            {
              paramInt2 = 2;
              break;
            }
            paramInt2 = 0;
            break;
            paramIntent = PictureTagView.Direction.Left;
            break label202;
            this.mEditPhotoViewerAdapter.updateTag(i, (PictureTagView.TagItem)localObject2);
            break label234;
          }
          while ((paramInt1 != 2) || (this.mEditingTagId == -1));
          removeTag(this.mEditingTagId);
          this.mEditingTagId = -1;
          this.mEditingTagPositionX = -1;
          this.mEditingTagPositionY = -1;
          return;
          if (paramInt1 != 5)
            break label489;
        }
        while (paramInt2 != -1);
        paramInt1 = paramIntent.getIntExtra("id", -1);
        localObject1 = paramIntent.getStringExtra("name");
        localObject2 = paramIntent.getStringExtra("url");
        String str = String.valueOf(paramIntent.getIntExtra("groupId", -1));
        paramIntent = (Bitmap)paramIntent.getParcelableExtra("smallbitmap");
        Log.d("EditUploadPhotoActivity", "get a decal from addphotodecal activity id=" + String.valueOf(paramInt1) + " name=" + (String)localObject1 + " url=" + (String)localObject2 + " groupid=" + str);
        requestDecals(str);
        insertDecal(str, (String)localObject1 + UUID.randomUUID().toString(), paramIntent, (String)localObject2);
        return;
      }
      while ((paramInt1 != 3) || (paramInt2 != -1));
      paramInt2 = paramIntent.getIntExtra("index", -1);
      localObject1 = (UploadPhotoTagData)paramIntent.getParcelableExtra("tagData");
      paramIntent = (UploadPhotoData)paramIntent.getParcelableExtra("photoData");
    }
    while ((paramInt2 == -1) || (localObject1 == null));
    label269: label276: label489: Object localObject2 = new PictureTagView.TagItem();
    ((PictureTagView.TagItem)localObject2).x = -1;
    ((PictureTagView.TagItem)localObject2).y = -1;
    if (((UploadPhotoTagData)localObject1).tagType == 1);
    for (paramInt1 = 1; ; paramInt1 = 2)
    {
      ((PictureTagView.TagItem)localObject2).type = paramInt1;
      ((PictureTagView.TagItem)localObject2).direction = PictureTagView.Direction.Right;
      ((PictureTagView.TagItem)localObject2).tagContent = ((UploadPhotoTagData)localObject1).content;
      ((UploadPhotoTagData)localObject1).tagId = ((PictureTagView.TagItem)localObject2).ID;
      paramIntent.tags.add(localObject1);
      this.mPhotos.set(paramInt2, paramIntent);
      this.mEditPhotoViewerAdapter.insertTag(paramInt2, (PictureTagView.TagItem)localObject2);
      return;
    }
  }

  public void onBackPressed()
  {
    exit(true, false);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    processParams();
    initViews();
    if (this.enableDecal)
      DecalManager.getInstance().addOnDownloadFinishListener(this);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.enableDecal)
      DecalManager.getInstance().removeOnDownloadFinishListener(this);
  }

  public void onDownloadFinish(String paramString1, String paramString2, Bitmap paramBitmap, int paramInt, boolean paramBoolean)
  {
    Log.d("EditUploadPhotoActivity", "onDownloadFinish get a decal id=" + paramString1 + " name=" + paramString2 + " decal=" + paramBitmap);
    paramString1 = (DecalView)this.mDecalThumbViews.get(paramString2);
    if (paramString1 != null)
      paramString1.setDecal(paramBitmap);
    if (paramBoolean)
    {
      if (paramBitmap == null)
        updateDecal(paramString2, null, paramInt);
    }
    else
      return;
    updateDecal(paramString2, paramBitmap, paramInt);
  }

  private class EditPhotoViewerAdapter extends PagerAdapter
  {
    private int containHeight;
    private int containWidth;
    private final TextView[] mCategoryLayouts;
    private final PictureTagLayout[] mPictureTagLayouts;
    private final ImageView[] mPictureView;
    private int photoWidth;

    public EditPhotoViewerAdapter(Context arg2)
    {
      Context localContext;
      this.photoWidth = ViewUtils.getScreenWidthPixels(localContext);
      this.containHeight = this.photoWidth;
      this.containWidth = this.photoWidth;
      this.mPictureTagLayouts = new PictureTagLayout[getCount()];
      this.mPictureView = new ImageView[getCount()];
      this.mCategoryLayouts = new TextView[getCount()];
    }

    public void addDecal(int paramInt, PictureDecalView.DecalItem paramDecalItem)
    {
      this.mPictureTagLayouts[paramInt].addDecalItem(paramDecalItem, null);
    }

    public void addDecal(int paramInt, String paramString1, String paramString2, Bitmap paramBitmap, String paramString3)
    {
      this.mPictureTagLayouts[paramInt].addDecalItem(paramString1, paramString2, paramString3, paramBitmap);
    }

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      paramObject = (View)paramObject;
      paramObject.setTag(null);
      paramViewGroup.removeView(paramObject);
    }

    public int getCount()
    {
      return EditUploadPhotoActivity.this.mPhotos.size();
    }

    public ArrayList<UploadPhotoDecalData> getDecalsByPosition(int paramInt)
    {
      Object localObject = this.mPictureTagLayouts[paramInt];
      ImageView localImageView = this.mPictureView[paramInt];
      ArrayList localArrayList = new ArrayList();
      if ((localObject != null) && (localImageView != null))
      {
        int[] arrayOfInt1 = new int[2];
        int[] arrayOfInt2 = new int[2];
        ((PictureTagLayout)localObject).getLocationOnScreen(arrayOfInt1);
        localImageView.getLocationOnScreen(arrayOfInt2);
        localObject = ((PictureTagLayout)localObject).getAllDecals().iterator();
        while (((Iterator)localObject).hasNext())
        {
          PictureDecalView.DecalItem localDecalItem = (PictureDecalView.DecalItem)((Iterator)localObject).next();
          Log.d("EditUploadPhotoActivity", " get a decal id=" + localDecalItem.groupId + " name=" + localDecalItem.name + " " + localDecalItem.width + " * " + localDecalItem.height + " w*h");
          UploadPhotoDecalData localUploadPhotoDecalData = new UploadPhotoDecalData();
          localUploadPhotoDecalData.decalId = localDecalItem.groupId;
          localUploadPhotoDecalData.decalName = localDecalItem.name;
          localUploadPhotoDecalData.positionX = ((arrayOfInt1[0] - arrayOfInt2[0] + localDecalItem.centerX) * 10000 / localImageView.getWidth());
          localUploadPhotoDecalData.positionY = ((arrayOfInt1[1] - arrayOfInt2[1] + localDecalItem.centerY) * 10000 / localImageView.getHeight());
          localUploadPhotoDecalData.width = (localDecalItem.width * 10000 / localImageView.getWidth());
          localUploadPhotoDecalData.height = (localDecalItem.height * 10000 / localImageView.getHeight());
          localUploadPhotoDecalData.angle = (int)localDecalItem.rotateAngle;
          localUploadPhotoDecalData.url = localDecalItem.url;
          Log.d("EditUploadPhotoActivity", "generate a decal data" + localUploadPhotoDecalData);
          localArrayList.add(localUploadPhotoDecalData);
        }
      }
      return (ArrayList<UploadPhotoDecalData>)localArrayList;
    }

    public int getDecalsCountByPosition(int paramInt)
    {
      return this.mPictureTagLayouts[paramInt].getAllDecals().size();
    }

    public int getItemPosition(Object paramObject)
    {
      paramObject = (View)paramObject;
      Log.d("EditUploadPhotoActivity", "getItemPosition tag=" + paramObject.getTag());
      paramObject = paramObject.getTag();
      if (!(paramObject instanceof Integer))
        return -2;
      return ((Integer)paramObject).intValue();
    }

    public PictureTagLayout[] getPictureTagLayouts()
    {
      return this.mPictureTagLayouts;
    }

    public ArrayList<UploadPhotoTagData> getTagsByPosition(int paramInt)
    {
      Object localObject = this.mPictureTagLayouts[paramInt];
      ImageView localImageView = this.mPictureView[paramInt];
      ArrayList localArrayList = new ArrayList();
      if (localObject != null)
      {
        int[] arrayOfInt1 = new int[2];
        int[] arrayOfInt2 = new int[2];
        ((PictureTagLayout)localObject).getLocationOnScreen(arrayOfInt1);
        localImageView.getLocationOnScreen(arrayOfInt2);
        localObject = ((PictureTagLayout)localObject).getAllTags().iterator();
        if (((Iterator)localObject).hasNext())
        {
          PictureTagView.TagItem localTagItem = (PictureTagView.TagItem)((Iterator)localObject).next();
          UploadPhotoTagData localUploadPhotoTagData = new UploadPhotoTagData(localTagItem.ID, localTagItem.x, localTagItem.y);
          localUploadPhotoTagData.content = localTagItem.tagContent;
          localUploadPhotoTagData.xPosition = (1.0D * (arrayOfInt1[0] - arrayOfInt2[0] + localTagItem.x) / localImageView.getWidth());
          localUploadPhotoTagData.yPosition = (1.0D * (arrayOfInt1[1] - arrayOfInt2[1] + localTagItem.y) / localImageView.getHeight());
          int i;
          if (localTagItem.type == 1)
          {
            i = 1;
            label191: localUploadPhotoTagData.tagType = i;
            if (localTagItem.direction != PictureTagView.Direction.Right)
              break label317;
          }
          label317: for (boolean bool = true; ; bool = false)
          {
            localUploadPhotoTagData.isRight = bool;
            localArrayList.add(localUploadPhotoTagData);
            Log.d("EditUploadPhotoActivity", "getTagsByPosition position=" + paramInt + " position x=" + localUploadPhotoTagData.xPosition + " position y=" + localUploadPhotoTagData.yPosition + " content=" + localUploadPhotoTagData.content);
            break;
            if (localTagItem.type == 2)
            {
              i = 2;
              break label191;
            }
            i = 0;
            break label191;
          }
        }
      }
      return (ArrayList<UploadPhotoTagData>)localArrayList;
    }

    public void insertTag(int paramInt, PictureTagView.TagItem paramTagItem)
    {
      Log.d("EditUploadPhotoActivity", "insertTag add a new tag id=" + paramTagItem.ID + " x positon=" + paramTagItem.x + " y position=" + paramTagItem.y + " content=" + paramTagItem.tagContent);
      this.mPictureTagLayouts[paramInt].addTagItem(paramTagItem);
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      Log.d("EditUploadPhotoActivity", "instantiateItem position=" + paramInt);
      View localView1 = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.ugc_photo_editupload_item, paramViewGroup, false);
      PictureTagLayout localPictureTagLayout = (PictureTagLayout)localView1.findViewById(R.id.photo_editupload_tag);
      localPictureTagLayout.setTagAddListener(new EditUploadPhotoActivity.EditPhotoViewerAdapter.1(this));
      localPictureTagLayout.setTagClickListener(new EditUploadPhotoActivity.EditPhotoViewerAdapter.2(this));
      localPictureTagLayout.setTagLongClickListener(new EditUploadPhotoActivity.EditPhotoViewerAdapter.3(this, localPictureTagLayout, paramInt));
      localPictureTagLayout.setOnFocusedChangedDecalListener(new EditUploadPhotoActivity.EditPhotoViewerAdapter.4(this, localPictureTagLayout));
      localPictureTagLayout.setRemoveDecalListener(new EditUploadPhotoActivity.EditPhotoViewerAdapter.5(this));
      localPictureTagLayout.setOnRetryDecalListener(new EditUploadPhotoActivity.EditPhotoViewerAdapter.6(this));
      this.mPictureTagLayouts[paramInt] = localPictureTagLayout;
      DPNetworkImageView localDPNetworkImageView = (DPNetworkImageView)localView1.findViewById(R.id.photo_editupload_preview);
      this.mPictureView[paramInt] = localDPNetworkImageView;
      View localView2 = localView1.findViewById(R.id.photo_editupload_delete);
      Object localObject = (TextView)localView1.findViewById(R.id.photo_editupload_category);
      ((FrameLayout.LayoutParams)((TextView)localObject).getLayoutParams()).bottomMargin = EditUploadPhotoActivity.this.mPhotoSummaryBottomMargin;
      this.mCategoryLayouts[paramInt] = localObject;
      localObject = (UploadPhotoData)EditUploadPhotoActivity.this.mPhotos.get(paramInt);
      if (((UploadPhotoData)localObject).containerWidth == 0)
        ((UploadPhotoData)localObject).containerWidth = this.photoWidth;
      if (((UploadPhotoData)localObject).containerHeight == 0)
        ((UploadPhotoData)localObject).containerHeight = this.photoWidth;
      String str = ((UploadPhotoData)localObject).photoPath;
      localDPNetworkImageView.setImageSize(this.photoWidth, 0);
      localDPNetworkImageView.setImage(str);
      localDPNetworkImageView.setLoadChangeListener(new EditUploadPhotoActivity.EditPhotoViewerAdapter.7(this, paramInt, localDPNetworkImageView, localPictureTagLayout, (UploadPhotoData)localObject));
      localView2.setOnClickListener(new EditUploadPhotoActivity.EditPhotoViewerAdapter.8(this, paramInt));
      updateCategory(paramInt);
      paramViewGroup.addView(localView1);
      localView1.setTag(Integer.valueOf(paramInt));
      return localView1;
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }

    public void showRetry(int paramInt, String paramString)
    {
      this.mPictureTagLayouts[paramInt].setDecalRetry(paramString);
    }

    public void updateCategory(int paramInt)
    {
      UploadPhotoData localUploadPhotoData = (UploadPhotoData)EditUploadPhotoActivity.this.mPhotos.get(paramInt);
      TextView localTextView = this.mCategoryLayouts[paramInt];
      if (EditUploadPhotoActivity.this.enableCategory)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(localUploadPhotoData.categoryName))
        {
          localStringBuilder.append(EditUploadPhotoActivity.this.getString(R.string.ugc_photo_edit_categoryname)).append(localUploadPhotoData.categoryName);
          if (!"菜".equals(localUploadPhotoData.categoryName))
            break label196;
          localStringBuilder.append(" ").append(" ").append(" ").append(EditUploadPhotoActivity.this.getString(R.string.ugc_photo_edit_dishname)).append(localUploadPhotoData.title);
        }
        while (true)
        {
          if (!TextUtils.isEmpty(localUploadPhotoData.price))
            localStringBuilder.append(" ").append(" ").append(" ").append(EditUploadPhotoActivity.this.getString(R.string.ugc_photo_edit_averageprice)).append(localUploadPhotoData.price);
          if (localStringBuilder.length() > 0)
          {
            localTextView.setText(localStringBuilder.toString());
            localTextView.setVisibility(0);
          }
          return;
          label196: localStringBuilder.append(" ").append(" ").append(" ").append(EditUploadPhotoActivity.this.getString(R.string.ugc_photo_edit_photoname)).append(localUploadPhotoData.title);
        }
      }
      localTextView.setVisibility(8);
    }

    public void updateDecal(int paramInt, String paramString, Bitmap paramBitmap)
    {
      this.mPictureTagLayouts[paramInt].updateDecalItem(paramString, paramBitmap);
    }

    public void updateTag(int paramInt, PictureTagView.TagItem paramTagItem)
    {
      this.mPictureTagLayouts[paramInt].updateTagItem(paramTagItem);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.uploadphoto.ui.EditUploadPhotoActivity
 * JD-Core Version:    0.6.0
 */
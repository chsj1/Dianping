package com.dianping.ugc.uploadphoto.shopphoto.ui;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.sync.SnsView;
import com.dianping.base.ugc.photo.SelectPhotoUtil;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.ugc.draft.UGCDraftManager;
import com.dianping.ugc.model.UGCUploadPhotoItem;
import com.dianping.ugc.model.UploadPhotoData;
import com.dianping.ugc.uploadphoto.shopphoto.AddShopPhotoService;
import com.dianping.ugc.widget.GridPhotoFragmentView;
import com.dianping.ugc.widget.GridPhotoFragmentView.OnAddListener;
import com.dianping.ugc.widget.GridPhotoFragmentView.OnSelectListener;
import com.dianping.v1.R.array;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaButton;
import java.util.ArrayList;
import java.util.Iterator;

public final class AddShopPhotoActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final int MAX_UPLOAD_PHOTO = 9;
  private static final int REQUEST_CODE_EDIT_PHOTO = 1;
  private String mCategories;
  private GridPhotoFragmentView mGridPhotoFragmentView;
  private UGCUploadPhotoItem mItem = new UGCUploadPhotoItem();
  private MApiRequest mShopRequest;
  private SnsView mSnsView;
  private NovaButton mUploadView;

  private void initViews()
  {
    setContentView(R.layout.ugc_photo_upload_layout);
    Object localObject = LayoutInflater.from(this).inflate(R.layout.ugc_photo_next, null, false);
    this.mUploadView = ((NovaButton)((View)localObject).findViewById(R.id.photo_next));
    this.mUploadView.setGAString("upload");
    this.mUploadView.setText(R.string.ugc_upload);
    this.mUploadView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        AddShopPhotoActivity.this.mItem.feed = AddShopPhotoActivity.this.mSnsView.getFeed();
        GAHelper.instance().contextStatisticsEvent(AddShopPhotoActivity.this, "share", null, AddShopPhotoActivity.this.mItem.feed, "tap");
        AddShopPhotoService.getInstance().uploadShopPhoto(AddShopPhotoActivity.this.mItem);
        AddShopPhotoActivity.this.finish();
      }
    });
    updateTitleView();
    getTitleBar().addRightViewItem((View)localObject, "upload", null);
    localObject = new TextView(this);
    ((TextView)localObject).setText(R.string.cancel);
    ((TextView)localObject).setGravity(17);
    ((TextView)localObject).setPadding((int)getResources().getDimension(R.dimen.title_bar_button_margin), 0, 0, 0);
    ((TextView)localObject).setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
    ((TextView)localObject).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_15));
    ((TextView)localObject).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        AddShopPhotoActivity.this.onBackPressed();
      }
    });
    getTitleBar().setCustomLeftView((View)localObject);
    this.mSnsView = ((SnsView)findViewById(R.id.photo_upload_sns));
    this.mSnsView.setText(getString(R.string.sync));
    this.mSnsView.bindActivity(this);
    this.mSnsView.unCheckedSns();
    this.mGridPhotoFragmentView = ((GridPhotoFragmentView)findViewById(R.id.photo_upload_browser));
    this.mGridPhotoFragmentView.init();
    this.mGridPhotoFragmentView.setMaxSelectedCount(9);
    this.mGridPhotoFragmentView.setShowDefaultSummary(true);
    this.mGridPhotoFragmentView.setOnAddListener(new GridPhotoFragmentView.OnAddListener()
    {
      public void onAdd()
      {
        SelectPhotoUtil.selectPhoto(AddShopPhotoActivity.this, 9 - AddShopPhotoActivity.this.mGridPhotoFragmentView.getCurrentCount());
        GAHelper.instance().contextStatisticsEvent(AddShopPhotoActivity.this, "add", null, 2147483647, "tap");
      }
    });
    this.mGridPhotoFragmentView.setOnSelectListener(new GridPhotoFragmentView.OnSelectListener()
    {
      public void onSelect(int paramInt, ArrayList<UploadPhotoData> paramArrayList)
      {
        Object localObject = Uri.parse("dianping://photoedit").buildUpon();
        ((Uri.Builder)localObject).appendQueryParameter("enableCategory", "true");
        ((Uri.Builder)localObject).appendQueryParameter("shopId", String.valueOf(AddShopPhotoActivity.this.mItem.shopId));
        ((Uri.Builder)localObject).appendQueryParameter("category", AddShopPhotoActivity.this.mCategories);
        localObject = new Intent("android.intent.action.VIEW", ((Uri.Builder)localObject).build());
        ((Intent)localObject).putParcelableArrayListExtra("photos", paramArrayList);
        ((Intent)localObject).putExtra("currentIndex", paramInt);
        AddShopPhotoActivity.this.startActivityForResult((Intent)localObject, 1);
        GAHelper.instance().contextStatisticsEvent(AddShopPhotoActivity.this, "edit", null, paramInt, "tap");
      }
    });
    this.mGridPhotoFragmentView.setPhotos(this.mItem.mPhotos);
  }

  private void processParams(Bundle paramBundle)
  {
    if (paramBundle == null)
    {
      paramBundle = getIntent().getParcelableExtra("draft");
      if ((paramBundle instanceof UGCUploadPhotoItem))
        this.mItem = ((UGCUploadPhotoItem)paramBundle);
      int i;
      do
      {
        return;
        paramBundle = getIntent().getStringArrayListExtra("selectedPhotos");
        if ((paramBundle != null) && (paramBundle.size() > 0))
        {
          paramBundle = paramBundle.iterator();
          while (paramBundle.hasNext())
          {
            String str = (String)paramBundle.next();
            UploadPhotoData localUploadPhotoData = new UploadPhotoData();
            localUploadPhotoData.photoPath = str;
            this.mItem.addUploadPhoto(localUploadPhotoData);
          }
        }
        this.mCategories = getStringParam("category");
        i = getIntParam("shopid", 0);
        this.mItem.shopId = String.valueOf(i);
        this.mItem.shopName = getStringParam("shopname");
        this.mItem.draftId = this.mItem.shopId;
      }
      while (i != 0);
      finish();
      return;
    }
    this.mItem = ((UGCUploadPhotoItem)paramBundle.getParcelable("item"));
  }

  private void requestShop(String paramString)
  {
    this.mShopRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/photo/getphotoshopinfo.bin?shopid=" + paramString, CacheType.NORMAL);
    showProgressDialog(getString(R.string.ugc_toast_fetch_category));
    mapiService().exec(this.mShopRequest, this);
  }

  private void showSaveDraftDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.ugc_dialog_hint);
    localBuilder.setItems(R.array.save_draft_items, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if (paramInt == 0)
        {
          UGCDraftManager.getInstance().removeDraft(AddShopPhotoActivity.this.mItem, true);
          AddShopPhotoActivity.this.finish();
        }
        do
          return;
        while (paramInt != 1);
        UGCDraftManager.getInstance().addDraft(AddShopPhotoActivity.this.mItem);
        AddShopPhotoActivity.this.finish();
        GAHelper.instance().contextStatisticsEvent(AddShopPhotoActivity.this, "save", null, 2147483647, "tap");
      }
    });
    localBuilder.setNegativeButton(getString(R.string.cancel), null);
    localBuilder.show();
  }

  private void updateTitleView()
  {
    NovaButton localNovaButton = this.mUploadView;
    if (this.mItem.getUploadPhotoCount() > 0);
    for (boolean bool = true; ; bool = false)
    {
      localNovaButton.setEnabled(bool);
      return;
    }
  }

  public String getPageName()
  {
    return "confirmPic";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 1000)
      if (paramInt2 == -1)
      {
        paramIntent = paramIntent.getStringArrayListExtra("selectedPhotos");
        if ((paramIntent != null) && (paramIntent.size() > 0))
        {
          paramIntent = paramIntent.iterator();
          while (paramIntent.hasNext())
          {
            String str = (String)paramIntent.next();
            UploadPhotoData localUploadPhotoData = new UploadPhotoData();
            localUploadPhotoData.photoPath = str;
            this.mItem.addUploadPhoto(localUploadPhotoData);
          }
          this.mGridPhotoFragmentView.setPhotos(this.mItem.mPhotos);
          updateTitleView();
        }
      }
    while (true)
    {
      return;
      if (paramInt1 != 1)
        break;
      if (paramInt2 != -1)
        continue;
      paramIntent = paramIntent.getParcelableArrayListExtra("photos");
      if (paramIntent == null)
        continue;
      this.mItem.setUploadPhotos(paramIntent);
      this.mGridPhotoFragmentView.setPhotos(this.mItem.mPhotos);
      updateTitleView();
      return;
    }
    this.mSnsView.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onBackPressed()
  {
    showSaveDraftDialog();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    processParams(paramBundle);
    if (accountService().token() != null)
      initViews();
    if (this.mCategories == null)
      requestShop(this.mItem.shopId);
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      initViews();
      return paramBoolean;
    }
    finish();
    return paramBoolean;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopRequest)
    {
      this.mShopRequest = null;
      dismissDialog();
      showToast(getString(R.string.ugc_toast_fetch_category_fail));
      finish();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopRequest)
    {
      this.mShopRequest = null;
      dismissDialog();
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      this.mItem.shopName = paramMApiRequest.getString("ShopName");
      paramMApiRequest = paramMApiRequest.getStringArray("ShopPhotoCategory");
      paramMApiResponse = new StringBuilder();
      if (paramMApiRequest != null)
      {
        int i = 0;
        while (i < paramMApiRequest.length)
        {
          paramMApiResponse.append(paramMApiRequest[i]).append(",");
          i += 1;
        }
        if (paramMApiResponse.length() > 0)
          paramMApiResponse.deleteCharAt(paramMApiResponse.length() - 1);
        this.mCategories = paramMApiResponse.toString();
      }
    }
    else
    {
      return;
    }
    showToast(getString(R.string.ugc_toast_fetch_category_fail));
    finish();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("item", this.mItem);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.uploadphoto.shopphoto.ui.AddShopPhotoActivity
 * JD-Core Version:    0.6.0
 */
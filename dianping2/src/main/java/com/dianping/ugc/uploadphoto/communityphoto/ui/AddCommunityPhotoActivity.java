package com.dianping.ugc.uploadphoto.communityphoto.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.sync.SnsView;
import com.dianping.base.ugc.photo.SelectPhotoUtil;
import com.dianping.base.widget.TitleBar;
import com.dianping.ugc.model.UGCUploadCommunityPhotoItem;
import com.dianping.ugc.model.UploadPhotoData;
import com.dianping.ugc.uploadphoto.communityphoto.AddCommunityPhotoService;
import com.dianping.ugc.widget.GridPhotoFragmentView;
import com.dianping.ugc.widget.GridPhotoFragmentView.OnAddListener;
import com.dianping.ugc.widget.GridPhotoFragmentView.OnPhotoCountChangedListener;
import com.dianping.ugc.widget.GridPhotoFragmentView.OnSelectListener;
import com.dianping.util.KeyboardUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.Iterator;

public class AddCommunityPhotoActivity extends NovaActivity
{
  private static final int MAX_NUM_TEXT = 500;
  private static final int MAX_UPLOAD_PHOTO = 9;
  private static final int NUM_TEXT_TIP = 450;
  private static final int REQUEST_CODE_EDIT_PHOTO = 1;
  private GridPhotoFragmentView mGridPhotoFragmentView;
  private UGCUploadCommunityPhotoItem mItem = new UGCUploadCommunityPhotoItem();
  private TextView mNumTipTextView;
  private SnsView mSnsView;
  private EditText mTextView;
  private Button mUploadView;

  private void initViews()
  {
    setContentView(R.layout.ugc_communityphoto_upload_layout);
    Object localObject = LayoutInflater.from(this).inflate(R.layout.ugc_photo_next, null, false);
    this.mUploadView = ((Button)((View)localObject).findViewById(R.id.photo_next));
    this.mUploadView.setText(R.string.ugc_publish);
    this.mUploadView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        KeyboardUtils.hideKeyboard(AddCommunityPhotoActivity.this.mTextView);
        AddCommunityPhotoActivity.this.mItem.text = AddCommunityPhotoActivity.this.mTextView.getText().toString();
        AddCommunityPhotoActivity.this.mItem.feed = AddCommunityPhotoActivity.this.mSnsView.getFeed();
        GAHelper.instance().contextStatisticsEvent(AddCommunityPhotoActivity.this, "share", null, AddCommunityPhotoActivity.this.mItem.feed, "tap");
        AddCommunityPhotoService.getInstance().uploadCommunityPhoto(AddCommunityPhotoActivity.this.mItem);
        AddCommunityPhotoActivity.this.finish();
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
        KeyboardUtils.hideKeyboard(AddCommunityPhotoActivity.this.mTextView);
        AddCommunityPhotoActivity.this.onBackPressed();
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
    this.mGridPhotoFragmentView.setShowDefaultSummary(false);
    this.mGridPhotoFragmentView.setOnAddListener(new GridPhotoFragmentView.OnAddListener()
    {
      public void onAdd()
      {
        SelectPhotoUtil.selectPhoto(AddCommunityPhotoActivity.this, 9 - AddCommunityPhotoActivity.this.mGridPhotoFragmentView.getCurrentCount());
      }
    });
    this.mGridPhotoFragmentView.setOnSelectListener(new GridPhotoFragmentView.OnSelectListener()
    {
      public void onSelect(int paramInt, ArrayList<UploadPhotoData> paramArrayList)
      {
        Object localObject = Uri.parse("dianping://photoedit").buildUpon();
        ((Uri.Builder)localObject).appendQueryParameter("enablePOI", "true");
        ((Uri.Builder)localObject).appendQueryParameter("enableTag", "true");
        ((Uri.Builder)localObject).appendQueryParameter("enableDecal", "true");
        localObject = new Intent("android.intent.action.VIEW", ((Uri.Builder)localObject).build());
        ((Intent)localObject).putParcelableArrayListExtra("photos", paramArrayList);
        ((Intent)localObject).putExtra("currentIndex", paramInt);
        AddCommunityPhotoActivity.this.startActivityForResult((Intent)localObject, 1);
      }
    });
    this.mGridPhotoFragmentView.setOnPhotoCountChangedListener(new GridPhotoFragmentView.OnPhotoCountChangedListener()
    {
      public void onPhotoCountChanged(int paramInt)
      {
        Button localButton = AddCommunityPhotoActivity.this.mUploadView;
        if (paramInt > 0);
        for (boolean bool = true; ; bool = false)
        {
          localButton.setEnabled(bool);
          return;
        }
      }
    });
    this.mGridPhotoFragmentView.setPhotos(this.mItem.mPhotos);
    this.mTextView = ((EditText)findViewById(R.id.photo_upload_text));
    this.mNumTipTextView = ((TextView)findViewById(R.id.photo_upload_text_num_tip));
    this.mTextView.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
        int i = paramEditable.length();
        if (i > 450)
        {
          paramEditable = AddCommunityPhotoActivity.this.getString(R.string.ugc_communityphoto_upload_num_tip).replace("%s", String.valueOf(500 - i));
          AddCommunityPhotoActivity.this.mNumTipTextView.setText(paramEditable);
          if (AddCommunityPhotoActivity.this.mNumTipTextView.getVisibility() == 4)
            AddCommunityPhotoActivity.this.mNumTipTextView.setVisibility(0);
        }
        do
          return;
        while (AddCommunityPhotoActivity.this.mNumTipTextView.getVisibility() != 0);
        AddCommunityPhotoActivity.this.mNumTipTextView.setVisibility(4);
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    });
  }

  private void processParams(Bundle paramBundle)
  {
    if (paramBundle == null)
    {
      paramBundle = getIntent().getParcelableExtra("draft");
      if ((paramBundle instanceof UGCUploadCommunityPhotoItem))
      {
        this.mItem = ((UGCUploadCommunityPhotoItem)paramBundle);
        return;
      }
      paramBundle = getIntent().getParcelableArrayListExtra("photos");
      if (paramBundle != null)
        this.mItem.setUploadPhotos(paramBundle);
      this.mItem.shopId = String.valueOf(getIntParam("shopId"));
      this.mItem.topicId = String.valueOf(getIntParam("topicId"));
      return;
    }
    this.mItem = ((UGCUploadCommunityPhotoItem)paramBundle.getParcelable("item"));
  }

  private void updateTitleView()
  {
    Button localButton = this.mUploadView;
    if (this.mItem.getUploadPhotoCount() > 0);
    for (boolean bool = true; ; bool = false)
    {
      localButton.setEnabled(bool);
      return;
    }
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

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    processParams(paramBundle);
    if (accountService().token() != null)
      initViews();
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

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("item", this.mItem);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.uploadphoto.communityphoto.ui.AddCommunityPhotoActivity
 * JD-Core Version:    0.6.0
 */
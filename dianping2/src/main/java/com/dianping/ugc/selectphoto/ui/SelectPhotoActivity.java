package com.dianping.ugc.selectphoto.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.util.Log;
import com.dianping.util.PermissionCheckHelper;
import com.dianping.util.PermissionCheckHelper.PermissionCallbackListener;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.v1.R.style;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaButton;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class SelectPhotoActivity extends NovaActivity
  implements PermissionCheckHelper.PermissionCallbackListener
{
  private static final String CATEGORY_ALL = "all";
  private static final Object FilterClosed = new Object();
  private static final Object FilterOpen = new Object();
  private static final int MSG_FETCH_PHOTO_FINISH = 1;
  public static final String TAG = "SelectPhotoActivity";
  private TextView mBackView;
  private final LinkedHashMap<String, ArrayList<String>> mCategories = new LinkedHashMap();
  private PopupWindow mCategoryFilter;
  private final ArrayList<ImageCategorySummary> mCategorySummary = new ArrayList();
  private TextView mCountView;
  private int mCurrentPhotoIndex;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 1)
      {
        SelectPhotoActivity.this.setPhotoCategory("all");
        SelectPhotoActivity.this.dismissDialog();
      }
    }
  };
  private String mNextScheme;
  private NovaButton mNextView;
  private ArrayList<String> mPhotos = null;
  private HashMap<String, Integer> mPhotosID = new HashMap();
  private PreviewPhotoFragment mPreviewPhotoFragment;
  private SelectPhotoFragment mSelectPhotoFragment;
  private final ArrayList<String> mSelectedPhotos = new ArrayList();
  private TextView mTitleView;
  private int maxSelectCount;

  private void initViews(Bundle paramBundle)
  {
    Object localObject = new FrameLayout(this);
    ((FrameLayout)localObject).setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    ((FrameLayout)localObject).setId(16908300);
    super.setContentView((View)localObject);
    if (paramBundle == null)
    {
      this.mSelectPhotoFragment = new SelectPhotoFragment();
      paramBundle = getSupportFragmentManager().beginTransaction();
      localObject = this.mSelectPhotoFragment;
      SelectPhotoFragment localSelectPhotoFragment = this.mSelectPhotoFragment;
      paramBundle.add(16908300, (Fragment)localObject, "SelectPhotoFragment");
      paramBundle.commit();
    }
    while (true)
    {
      paramBundle = (FrameLayout)LayoutInflater.from(this).inflate(R.layout.ugc_photo_next, null, false);
      this.mNextView = ((NovaButton)paramBundle.findViewById(R.id.photo_next));
      this.mNextView.setGAString("next");
      this.mNextView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectPhotoActivity.this.submit();
        }
      });
      this.mCountView = ((TextView)paramBundle.findViewById(R.id.photo_count));
      getTitleBar().addRightViewItem(paramBundle, "next", null);
      this.mBackView = new TextView(this);
      this.mBackView.setText(R.string.cancel);
      this.mBackView.setGravity(17);
      this.mBackView.setPadding((int)getResources().getDimension(R.dimen.title_bar_button_margin), 0, 0, 0);
      this.mBackView.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
      this.mBackView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_15));
      this.mBackView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectPhotoActivity.this.onBackPressed();
        }
      });
      getTitleBar().setCustomLeftView(this.mBackView);
      this.mTitleView = new TextView(this);
      localObject = new FrameLayout.LayoutParams(-2, -2);
      ((FrameLayout.LayoutParams)localObject).gravity = 17;
      this.mTitleView.setLayoutParams((ViewGroup.LayoutParams)localObject);
      this.mTitleView.setSingleLine(true);
      this.mTitleView.setTextAppearance(this, R.style.TitleBarTitleView);
      this.mTitleView.setGravity(17);
      this.mTitleView.setOnClickListener(new View.OnClickListener(paramBundle)
      {
        public void onClick(View paramView)
        {
          if (SelectPhotoActivity.this.mTitleView.getTag() == SelectPhotoActivity.FilterOpen)
          {
            SelectPhotoActivity.this.setTitle(false, null);
            if ((SelectPhotoActivity.this.mCategoryFilter != null) && (SelectPhotoActivity.this.mCategoryFilter.isShowing()))
            {
              SelectPhotoActivity.this.mCategoryFilter.dismiss();
              SelectPhotoActivity.access$802(SelectPhotoActivity.this, null);
            }
            return;
          }
          SelectPhotoActivity.this.setTitle(true, null);
          paramView = new ListView(SelectPhotoActivity.this);
          paramView.setBackgroundColor(SelectPhotoActivity.this.getResources().getColor(R.color.white));
          paramView.setCacheColorHint(SelectPhotoActivity.this.getResources().getColor(R.color.transparent));
          paramView.setSelector(new ColorDrawable(SelectPhotoActivity.this.getResources().getColor(R.color.transparent)));
          paramView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
          SelectPhotoActivity.4.1 local1 = new SelectPhotoActivity.4.1(this);
          paramView.setAdapter(local1);
          paramView.setOnItemClickListener(new SelectPhotoActivity.4.2(this, local1));
          SelectPhotoActivity.access$802(SelectPhotoActivity.this, new PopupWindow(paramView, -1, -1));
          paramView = new ColorDrawable(-1342177280);
          SelectPhotoActivity.this.mCategoryFilter.setBackgroundDrawable(paramView);
          SelectPhotoActivity.this.mCategoryFilter.setFocusable(true);
          SelectPhotoActivity.this.mCategoryFilter.setTouchable(true);
          SelectPhotoActivity.this.mCategoryFilter.setOnDismissListener(new SelectPhotoActivity.4.3(this));
          SelectPhotoActivity.this.mCategoryFilter.showAsDropDown(this.val$nextLayout);
        }
      });
      setTitle(false, getString(R.string.ugc_title_album_all));
      getTitleBar().setCustomContentView(this.mTitleView);
      return;
      paramBundle = getSupportFragmentManager();
      this.mSelectPhotoFragment = ((SelectPhotoFragment)paramBundle.findFragmentByTag("SelectPhotoFragment"));
      this.mPreviewPhotoFragment = ((PreviewPhotoFragment)paramBundle.findFragmentByTag("PreviewPhotoFragment"));
    }
  }

  private void requirePermission()
  {
    String str = getString(R.string.rationale_external_storage);
    PermissionCheckHelper.instance().requestPermissions(this, 0, new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, new String[] { str }, this);
  }

  private void setPhotoCategory(String paramString)
  {
    ArrayList localArrayList = (ArrayList)this.mCategories.get(paramString);
    if (localArrayList != null);
    while (true)
    {
      this.mPhotos = localArrayList;
      this.mSelectPhotoFragment.refreshPhotos(this.mPhotos, this.mPhotosID);
      GAHelper.instance().contextStatisticsEvent(this, "album", paramString, 2147483647, "tap");
      return;
      localArrayList = new ArrayList();
    }
  }

  private void updateSelectedPhotosUI()
  {
    int i = this.mSelectedPhotos.size();
    if (i == 0)
    {
      this.mCountView.setVisibility(8);
      this.mNextView.setEnabled(false);
    }
    while (true)
    {
      this.mSelectPhotoFragment.refreshViews();
      return;
      this.mCountView.setVisibility(0);
      this.mCountView.setText(String.valueOf(i));
      this.mNextView.setEnabled(true);
    }
  }

  public int getCurrentPhotoIndex()
  {
    return this.mCurrentPhotoIndex;
  }

  public String getPageName()
  {
    return "choosePic";
  }

  public ArrayList<String> getPhotos()
  {
    if (this.mPhotos == null)
      return new ArrayList();
    return this.mPhotos;
  }

  public ArrayList<String> getSelectedPhotos()
  {
    return this.mSelectedPhotos;
  }

  public boolean hasSelectedPhotos()
  {
    return getSelectedPhotos().size() > 0;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public boolean isPhotoSelected(String paramString)
  {
    return this.mSelectedPhotos.contains(paramString);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.maxSelectCount = getIntParam("maxNum", 9);
    String str2 = getStringParam("next");
    String str1 = str2;
    if (str2 != null);
    try
    {
      str1 = URLDecoder.decode(str2, "utf-8");
      this.mNextScheme = str1;
      initViews(paramBundle);
      updateSelectedPhotosUI();
      showSelectView();
      this.mSelectPhotoFragment.setMaxSelectCount(this.maxSelectCount);
      requirePermission();
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public void onPermissionCheckCallback(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    if ((paramInt == 0) && (paramArrayOfInt[0] == 0))
    {
      showProgressDialog(getString(R.string.ugc_toast_searching));
      new Thread(new FetchPhotos(null)).start();
      return;
    }
    Toast.makeText(this, getString(R.string.ugc_permission_alert_external_storage), 1).show();
  }

  public void setBackText(int paramInt)
  {
    this.mBackView.setText(paramInt);
  }

  public void setCurrentPhotoIndex(int paramInt)
  {
    this.mCurrentPhotoIndex = paramInt;
  }

  public boolean setPhotoSelected(String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (this.mSelectedPhotos.size() >= this.maxSelectCount)
      {
        Toast.makeText(this, getResources().getString(R.string.ugc_toast_photo_meetmax).replace("%s", String.valueOf(this.maxSelectCount)), 0).show();
        return false;
      }
      if (!this.mSelectedPhotos.contains(paramString))
        this.mSelectedPhotos.add(paramString);
    }
    while (true)
    {
      updateSelectedPhotosUI();
      return true;
      this.mSelectedPhotos.remove(paramString);
    }
  }

  public void setSelectedPhotos(ArrayList<String> paramArrayList)
  {
    this.mSelectedPhotos.clear();
    this.mSelectedPhotos.addAll(paramArrayList);
    updateSelectedPhotosUI();
  }

  public void setTitle(boolean paramBoolean, String paramString)
  {
    Drawable localDrawable;
    if (paramBoolean)
    {
      this.mTitleView.setTag(FilterOpen);
      localDrawable = getResources().getDrawable(R.drawable.navibar_arrow_up);
      localDrawable.setBounds(0, 0, localDrawable.getMinimumWidth(), localDrawable.getMinimumHeight());
      this.mTitleView.setCompoundDrawables(null, null, localDrawable, null);
    }
    while (true)
    {
      if (paramString != null)
        this.mTitleView.setText(paramString);
      return;
      localDrawable = getResources().getDrawable(R.drawable.navibar_arrow_down);
      localDrawable.setBounds(0, 0, localDrawable.getMinimumWidth(), localDrawable.getMinimumHeight());
      this.mTitleView.setCompoundDrawables(null, null, localDrawable, null);
      this.mTitleView.setTag(FilterClosed);
    }
  }

  public void showPreviewView(boolean paramBoolean)
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.hide(this.mSelectPhotoFragment);
    PreviewPhotoFragment localPreviewPhotoFragment = new PreviewPhotoFragment();
    if (paramBoolean);
    for (PreviewPhotoFragment.PreviewMode localPreviewMode = PreviewPhotoFragment.PreviewMode.FROM_PHOTO; ; localPreviewMode = PreviewPhotoFragment.PreviewMode.FROM_PREVIEW)
    {
      localPreviewPhotoFragment.setPreviewMode(localPreviewMode);
      if (!paramBoolean)
        localPreviewPhotoFragment.setPreviewPhotos(this.mSelectedPhotos);
      localFragmentTransaction.add(16908300, localPreviewPhotoFragment, "PreviewPhotoFragment");
      localFragmentTransaction.addToBackStack("PreviewPhotoFragment");
      localFragmentTransaction.commit();
      return;
    }
  }

  public void showSelectView()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    if (this.mPreviewPhotoFragment != null)
      localFragmentTransaction.remove(this.mPreviewPhotoFragment);
    localFragmentTransaction.show(this.mSelectPhotoFragment);
    localFragmentTransaction.commit();
  }

  public void submit()
  {
    Intent localIntent1 = new Intent();
    localIntent1.putStringArrayListExtra("selectedPhotos", this.mSelectedPhotos);
    if (this.mNextScheme != null);
    try
    {
      Intent localIntent2 = new Intent("android.intent.action.VIEW", Uri.parse(this.mNextScheme));
      localIntent2.putExtras(localIntent1);
      startActivity(localIntent2);
      setResult(-1, localIntent1);
      finish();
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public String updateTitle(String paramString)
  {
    String str = this.mTitleView.getText().toString();
    this.mTitleView.setCompoundDrawables(null, null, null, null);
    this.mTitleView.setText(paramString);
    return str;
  }

  private class FetchPhotos
    implements Runnable
  {
    private FetchPhotos()
    {
    }

    public void run()
    {
      Log.d("SelectPhotoActivity", "fetchPhotos start");
      ArrayList localArrayList2 = new ArrayList();
      Object localObject1 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
      Cursor localCursor = SelectPhotoActivity.this.getContentResolver().query((Uri)localObject1, new String[] { "_data", "_id" }, "mime_type=? or mime_type=?", new String[] { "image/jpeg", "image/png" }, "date_modified desc");
      Object localObject2;
      ArrayList localArrayList1;
      if ((localCursor != null) && (localCursor.moveToFirst()))
      {
        do
        {
          String str = localCursor.getString(localCursor.getColumnIndex("_data"));
          int i = localCursor.getInt(localCursor.getColumnIndex("_id"));
          if (str == null)
            continue;
          localArrayList2.add(str);
          SelectPhotoActivity.this.mPhotosID.put(str, Integer.valueOf(i));
          localObject1 = new File(str);
          if (((File)localObject1).getParentFile() == null)
            continue;
          localObject2 = ((File)localObject1).getParentFile().getName();
          if (localObject2 == null)
            continue;
          i = ((String)localObject2).lastIndexOf(File.separatorChar);
          localObject1 = localObject2;
          if (i != -1)
            localObject1 = ((String)localObject2).substring(i + 1);
          localArrayList1 = (ArrayList)SelectPhotoActivity.this.mCategories.get(localObject1);
          localObject2 = localArrayList1;
          if (localArrayList1 == null)
          {
            localObject2 = new ArrayList();
            SelectPhotoActivity.this.mCategories.put(localObject1, localObject2);
          }
          ((ArrayList)localObject2).add(str);
        }
        while (localCursor.moveToNext());
        localCursor.close();
      }
      localObject1 = SelectPhotoActivity.this.mCategories.keySet().iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (String)((Iterator)localObject1).next();
        localArrayList1 = (ArrayList)SelectPhotoActivity.this.mCategories.get(localObject2);
        SelectPhotoActivity.this.mCategorySummary.add(new SelectPhotoActivity.ImageCategorySummary((String)localArrayList1.get(0), (String)localObject2, localArrayList1.size()));
      }
      SelectPhotoActivity.this.mCategories.put("all", localArrayList2);
      Log.d("SelectPhotoActivity", "fetchPhotos finish");
      SelectPhotoActivity.this.mHandler.sendEmptyMessage(1);
    }
  }

  private static class ImageCategorySummary
  {
    public final String categoryName;
    public final int count;
    public final String pic;

    public ImageCategorySummary(String paramString1, String paramString2, int paramInt)
    {
      this.pic = paramString1;
      this.categoryName = paramString2;
      this.count = paramInt;
    }

    public String toString()
    {
      return "categoryName=" + this.categoryName + " count=" + this.count;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.selectphoto.ui.SelectPhotoActivity
 * JD-Core Version:    0.6.0
 */
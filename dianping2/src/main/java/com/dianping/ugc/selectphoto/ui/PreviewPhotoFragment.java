package com.dianping.ugc.selectphoto.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import com.dianping.base.widget.NovaFragment;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;

public class PreviewPhotoFragment extends NovaFragment
{
  public static final String TAG = "PreviewPhotoFragment";
  private int mLastPosition = 0;
  private ViewPager mPhotoViewer;
  private PreviewPhotoFragment.PhotoViewerAdapter mPhotoViewerAdapter;
  private PreviewMode mPreviewMode;
  private ArrayList<String> mPreviewPhotos = new ArrayList();
  private String oldTitle;
  private SelectPhotoActivity root;

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.root = ((SelectPhotoActivity)getActivity());
    this.root.setBackText(R.string.back);
    this.mPhotoViewerAdapter = new PreviewPhotoFragment.PhotoViewerAdapter(this, this.root);
    this.mPhotoViewer.setAdapter(this.mPhotoViewerAdapter);
    StringBuilder localStringBuilder;
    if (this.mPreviewMode == PreviewMode.FROM_PHOTO)
    {
      i = this.root.getCurrentPhotoIndex();
      this.mLastPosition = i;
      this.mPhotoViewer.setCurrentItem(i);
      paramBundle = this.root;
      localStringBuilder = new StringBuilder().append(i + 1).append("/");
      if (this.mPreviewMode != PreviewMode.FROM_PHOTO)
        break label174;
    }
    label174: for (int i = this.root.getPhotos().size(); ; i = this.root.getSelectedPhotos().size())
    {
      this.oldTitle = paramBundle.updateTitle(i);
      this.mPhotoViewer.setOnPageChangeListener(new PreviewPhotoFragment.1(this));
      GAHelper.instance().setGAPageName("previewPic");
      return;
      i = 0;
      break;
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mPhotoViewer = new ViewPager(paramLayoutInflater.getContext());
    this.mPhotoViewer.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
    this.mPhotoViewer.setOffscreenPageLimit(2);
    return this.mPhotoViewer;
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.root.setTitle(false, this.oldTitle);
    this.root.setBackText(R.string.cancel);
    GAHelper.instance().setGAPageName(this.root.getPageName());
  }

  public void setPreviewMode(PreviewMode paramPreviewMode)
  {
    this.mPreviewMode = paramPreviewMode;
  }

  public void setPreviewPhotos(ArrayList<String> paramArrayList)
  {
    this.mPreviewPhotos.clear();
    this.mPreviewPhotos.addAll(paramArrayList);
  }

  public static enum PreviewMode
  {
    static
    {
      $VALUES = new PreviewMode[] { FROM_PHOTO, FROM_PREVIEW };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.selectphoto.ui.PreviewPhotoFragment
 * JD-Core Version:    0.6.0
 */
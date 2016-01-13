package com.dianping.ugc.selectphoto.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.base.widget.NovaFragment;
import com.dianping.ugc.widget.GridPhotoView;
import com.dianping.util.PermissionCheckHelper.PermissionCallbackListener;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectPhotoFragment extends NovaFragment
{
  public static final int REQUEST_CODE_CAMERA = 101;
  public static final String TAG = "SelectPhotoFragment";
  private String mCameraPhotoPath = null;
  private int mMaxSelectCount = 9;
  private GridPhotoView mPhotoView;
  private View mPreviewButton;
  PermissionCheckHelper.PermissionCallbackListener permissionCallbackListener = new SelectPhotoFragment.1(this);
  private SelectPhotoActivity root;

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.root = ((SelectPhotoActivity)getActivity());
    this.mPhotoView.setMaxSelectedCount(this.mMaxSelectCount);
    this.mPhotoView.setPhotos(this.root.getPhotos());
    this.mPhotoView.setCameraClickListener(new SelectPhotoFragment.2(this));
    this.mPhotoView.setPreviewListener(new SelectPhotoFragment.3(this));
    this.mPhotoView.setSelectChangedListener(new SelectPhotoFragment.4(this));
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((101 == paramInt1) && (paramInt2 == -1))
    {
      this.root.setPhotoSelected(this.mCameraPhotoPath, true);
      this.root.submit();
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.ugc_photo_select_layout, paramViewGroup, false);
    this.mPhotoView = ((GridPhotoView)paramLayoutInflater.findViewById(R.id.photo_browser));
    this.mPreviewButton = paramLayoutInflater.findViewById(R.id.preview);
    this.mPreviewButton.setOnClickListener(new SelectPhotoFragment.5(this));
    return paramLayoutInflater;
  }

  public void onHiddenChanged(boolean paramBoolean)
  {
    if (!paramBoolean)
      this.mPhotoView.setSelectedPhotos(this.root.getSelectedPhotos());
  }

  public void refreshPhotos(ArrayList<String> paramArrayList, HashMap<String, Integer> paramHashMap)
  {
    if (this.mPhotoView != null)
      this.mPhotoView.refresh(paramArrayList, paramHashMap);
  }

  public void refreshViews()
  {
    if (this.mPreviewButton != null)
      this.mPreviewButton.setEnabled(this.root.hasSelectedPhotos());
  }

  public void setMaxSelectCount(int paramInt)
  {
    this.mMaxSelectCount = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.selectphoto.ui.SelectPhotoFragment
 * JD-Core Version:    0.6.0
 */
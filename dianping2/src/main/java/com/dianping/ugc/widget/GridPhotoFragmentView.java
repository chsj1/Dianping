package com.dianping.ugc.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.ugc.model.UploadPhotoData;
import java.util.ArrayList;

public class GridPhotoFragmentView extends MeasuredGridView
{
  private int mColumnCount = 3;
  GridPhotoFragmentView.GridPhotoFragmentViewAdapter mGridPhotoFragmentViewAdapter;
  int mMaxSelectedCount = 9;
  OnAddListener mOnAddListener;
  private OnPhotoCountChangedListener mOnPhotoCountChangedListener;
  OnSelectListener mOnSelectListener;
  final ArrayList<UploadPhotoData> mPhotos = new ArrayList();
  boolean mShowDefaultSummary = true;

  public GridPhotoFragmentView(Context paramContext)
  {
    super(paramContext);
  }

  public GridPhotoFragmentView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void addPhoto(UploadPhotoData paramUploadPhotoData)
  {
    this.mPhotos.add(paramUploadPhotoData);
    this.mGridPhotoFragmentViewAdapter.notifyDataSetChanged();
    if (this.mOnPhotoCountChangedListener != null)
      this.mOnPhotoCountChangedListener.onPhotoCountChanged(this.mPhotos.size());
  }

  public void addPhotos(ArrayList<UploadPhotoData> paramArrayList)
  {
    this.mPhotos.addAll(paramArrayList);
    this.mGridPhotoFragmentViewAdapter.notifyDataSetChanged();
    if (this.mOnPhotoCountChangedListener != null)
      this.mOnPhotoCountChangedListener.onPhotoCountChanged(this.mPhotos.size());
  }

  public int getCurrentCount()
  {
    return this.mPhotos.size();
  }

  public int getItemWidth()
  {
    return this.mGridPhotoFragmentViewAdapter.getItemWidth();
  }

  public ArrayList<UploadPhotoData> getPhotos()
  {
    return this.mPhotos;
  }

  public void init()
  {
    this.mGridPhotoFragmentViewAdapter = new GridPhotoFragmentView.GridPhotoFragmentViewAdapter(this, getContext());
    setAdapter(this.mGridPhotoFragmentViewAdapter);
    setOnItemClickListener(new GridPhotoFragmentView.1(this));
  }

  public void removeByIndex(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < this.mPhotos.size()))
    {
      this.mPhotos.remove(paramInt);
      this.mGridPhotoFragmentViewAdapter.notifyDataSetChanged();
      if (this.mOnPhotoCountChangedListener != null)
        this.mOnPhotoCountChangedListener.onPhotoCountChanged(this.mPhotos.size());
    }
  }

  public void setColumnCount(int paramInt)
  {
    this.mColumnCount = paramInt;
  }

  public void setMaxSelectedCount(int paramInt)
  {
    this.mMaxSelectedCount = paramInt;
  }

  public void setOnAddListener(OnAddListener paramOnAddListener)
  {
    this.mOnAddListener = paramOnAddListener;
  }

  public void setOnPhotoCountChangedListener(OnPhotoCountChangedListener paramOnPhotoCountChangedListener)
  {
    this.mOnPhotoCountChangedListener = paramOnPhotoCountChangedListener;
  }

  public void setOnSelectListener(OnSelectListener paramOnSelectListener)
  {
    this.mOnSelectListener = paramOnSelectListener;
  }

  public void setPhotos(ArrayList<UploadPhotoData> paramArrayList)
  {
    this.mPhotos.clear();
    this.mPhotos.addAll(paramArrayList);
    this.mGridPhotoFragmentViewAdapter.notifyDataSetChanged();
    if (this.mOnPhotoCountChangedListener != null)
      this.mOnPhotoCountChangedListener.onPhotoCountChanged(this.mPhotos.size());
  }

  public void setShowDefaultSummary(boolean paramBoolean)
  {
    this.mShowDefaultSummary = paramBoolean;
  }

  public static abstract interface OnAddListener
  {
    public abstract void onAdd();
  }

  public static abstract interface OnPhotoCountChangedListener
  {
    public abstract void onPhotoCountChanged(int paramInt);
  }

  public static abstract interface OnSelectListener
  {
    public abstract void onSelect(int paramInt, ArrayList<UploadPhotoData> paramArrayList);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.widget.GridPhotoFragmentView
 * JD-Core Version:    0.6.0
 */
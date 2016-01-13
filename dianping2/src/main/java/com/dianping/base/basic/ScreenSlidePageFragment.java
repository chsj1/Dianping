package com.dianping.base.basic;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.app.DPFragment;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.loading.LoadingLayout;

public class ScreenSlidePageFragment extends DPFragment
{
  public static final String ARG_BITMAP = "bitmap";
  public static final String ARG_CURRENT = "current";
  public static final String ARG_PAGE = "page";
  protected Bitmap bitmap;
  protected String categoryTag;
  protected boolean isBackground = false;
  protected DPObject pageObj;
  protected int position;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.pageObj = ((DPObject)getArguments().getParcelable("page"));
    this.isBackground = getArguments().getBoolean("current");
    this.position = getArguments().getInt("position");
    this.bitmap = ((Bitmap)getArguments().getParcelable("bitmap"));
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = new LoadingLayout(paramViewGroup.getContext());
    paramLayoutInflater.creatLoadingLayout(this.isBackground, true, true);
    paramLayoutInflater.setImageUrl(this.pageObj.getString("Url"));
    if (this.isBackground)
      paramLayoutInflater.setLoadingBackgruond(this.bitmap);
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.ScreenSlidePageFragment
 * JD-Core Version:    0.6.0
 */
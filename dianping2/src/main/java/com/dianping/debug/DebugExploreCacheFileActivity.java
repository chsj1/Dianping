package com.dianping.debug;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.dianping.app.DPActivity;
import com.dianping.base.widget.TitleBar;
import java.io.File;

public class DebugExploreCacheFileActivity extends DPActivity
  implements DebugExploreCacheFileFragment.Callbacks, FragmentManager.OnBackStackChangedListener, View.OnClickListener
{
  public static final String PATH = "path";
  public String BASE_PATH = "/data/data/com.dianping.v1/";
  private String mCurrentTotalSize;
  private FragmentManager mFragmentMgr;
  private String mRootPath;
  private TitleBar mTitleBar;
  public String mTotalSize;

  private void addFragment()
  {
    DebugExploreCacheFileFragment localDebugExploreCacheFileFragment = DebugExploreCacheFileFragment.newInstance(this.mRootPath);
    this.mFragmentMgr.beginTransaction().add(16908290, localDebugExploreCacheFileFragment).commit();
  }

  private String getSubPath(String paramString)
  {
    return paramString.substring(this.BASE_PATH.length());
  }

  private void replaceFragment(File paramFile)
  {
    this.mRootPath = paramFile.getAbsolutePath();
    paramFile = DebugExploreCacheFileFragment.newInstance(this.mRootPath);
    this.mFragmentMgr.beginTransaction().replace(16908290, paramFile).setTransition(4097).addToBackStack(this.mRootPath).commit();
  }

  public void onBackStackChanged()
  {
    int i = this.mFragmentMgr.getBackStackEntryCount();
    if (i > 0)
    {
      this.mRootPath = this.mFragmentMgr.getBackStackEntryAt(i - 1).getName();
      this.mTitleBar.setTitle("Path:" + getSubPath(this.mRootPath));
      this.mTitleBar.setSubTitle("Size:" + this.mCurrentTotalSize);
      return;
    }
    this.mTitleBar.setTitle("Path:" + this.BASE_PATH);
    this.mTitleBar.setSubTitle("Size:" + this.mTotalSize);
  }

  public void onClick(View paramView)
  {
    onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mFragmentMgr = getSupportFragmentManager();
    this.mFragmentMgr.addOnBackStackChangedListener(this);
    if (paramBundle == null)
    {
      this.mRootPath = this.BASE_PATH;
      addFragment();
    }
    while (true)
    {
      if (getParent() == null)
        this.mTitleBar = TitleBar.build(this, 100);
      return;
      this.mRootPath = paramBundle.getString("path");
    }
  }

  public void onFileSelect(File paramFile, long paramLong)
  {
    if (paramFile != null)
    {
      if (paramFile.isDirectory())
      {
        this.mCurrentTotalSize = DebugExploreCacheFileAdapter.formatSizeData(paramLong);
        replaceFragment(paramFile);
      }
    }
    else
      return;
    Toast.makeText(this, "you click file ", 0).show();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("path", this.mRootPath);
  }

  protected void onStart()
  {
    super.onStart();
    if (this.mTitleBar != null)
    {
      this.mTitleBar.setTitle("Path:" + this.BASE_PATH);
      this.mTitleBar.setSubTitle("Size:" + this.mTotalSize);
      this.mTitleBar.setLeftView(this);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugExploreCacheFileActivity
 * JD-Core Version:    0.6.0
 */
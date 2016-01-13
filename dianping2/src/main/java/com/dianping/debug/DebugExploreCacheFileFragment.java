package com.dianping.debug;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import java.io.File;
import java.io.FileFilter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ArrayList<Lcom.dianping.debug.DebugExploreCacheFileFragment.FileItem;>;
import java.util.Calendar;
import java.util.Collections;

@TargetApi(11)
public class DebugExploreCacheFileFragment extends ListFragment
{
  public static FileFilter sDirFilter;
  public static FileFilter sFileFilter = new DebugExploreCacheFileFragment.2();
  DebugExploreCacheFileAdapter mAdapter;
  private Callbacks mListener;
  private String mPath;

  static
  {
    sDirFilter = new DebugExploreCacheFileFragment.3();
  }

  private ArrayList<DebugExploreCacheFileFragment.FileItem> getData(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    paramString = new File(paramString);
    DebugExploreCacheFileActivity localDebugExploreCacheFileActivity = (DebugExploreCacheFileActivity)getActivity();
    Object localObject = paramString.listFiles(sDirFilter);
    int j;
    int i;
    if (localObject != null)
    {
      long l = 0L;
      j = localObject.length;
      i = 0;
      while (i < j)
      {
        File localFile = localObject[i];
        DebugExploreCacheFileFragment.FileItem localFileItem = new DebugExploreCacheFileFragment.FileItem(this);
        localFileItem.file = localFile;
        localFileItem.fileName = localFile.getName();
        localFileItem.lastModifyTime = getLastModifyDate(localFile);
        localFileItem.subItemNum = localFile.listFiles().length;
        localFileItem.size = getFolderSize(localFile);
        l += localFileItem.size;
        localArrayList.add(localFileItem);
        i += 1;
      }
      localDebugExploreCacheFileActivity.mTotalSize = DebugExploreCacheFileAdapter.formatSizeData(l);
    }
    paramString = paramString.listFiles(sFileFilter);
    if (paramString != null)
    {
      j = paramString.length;
      i = 0;
      while (i < j)
      {
        localDebugExploreCacheFileActivity = paramString[i];
        localObject = new DebugExploreCacheFileFragment.FileItem(this);
        ((DebugExploreCacheFileFragment.FileItem)localObject).file = localDebugExploreCacheFileActivity;
        ((DebugExploreCacheFileFragment.FileItem)localObject).fileName = localDebugExploreCacheFileActivity.getName();
        ((DebugExploreCacheFileFragment.FileItem)localObject).lastModifyTime = getLastModifyDate(localDebugExploreCacheFileActivity);
        ((DebugExploreCacheFileFragment.FileItem)localObject).subItemNum = 0;
        ((DebugExploreCacheFileFragment.FileItem)localObject).size = localDebugExploreCacheFileActivity.length();
        localArrayList.add(localObject);
        i += 1;
      }
    }
    if (localArrayList != null)
      sorDataOrder(localArrayList);
    return (ArrayList<DebugExploreCacheFileFragment.FileItem>)localArrayList;
  }

  public static long getFolderSize(File paramFile)
  {
    long l = 0L;
    paramFile = paramFile.listFiles();
    int i = 0;
    if (i < paramFile.length)
    {
      if (paramFile[i].isDirectory())
        l += getFolderSize(paramFile[i]);
      while (true)
      {
        i += 1;
        break;
        l += paramFile[i].length();
      }
    }
    return l;
  }

  private String getLastModifyDate(File paramFile)
  {
    long l = paramFile.lastModified();
    paramFile = Calendar.getInstance();
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    paramFile.setTimeInMillis(l);
    return localSimpleDateFormat.format(paramFile.getTime());
  }

  public static DebugExploreCacheFileFragment newInstance(String paramString)
  {
    DebugExploreCacheFileFragment localDebugExploreCacheFileFragment = new DebugExploreCacheFileFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("path", paramString);
    localDebugExploreCacheFileFragment.setArguments(localBundle);
    return localDebugExploreCacheFileFragment;
  }

  private void sorDataOrder(ArrayList<DebugExploreCacheFileFragment.FileItem> paramArrayList)
  {
    Collections.sort(paramArrayList, new DebugExploreCacheFileFragment.4(this));
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    getListView().setOnItemLongClickListener(new DebugExploreCacheFileFragment.1(this));
    setListShown(false);
    setEmptyText("empty");
    this.mPath = getArguments().getString("path");
    if (this.mPath != null)
      this.mAdapter = new DebugExploreCacheFileAdapter((DebugExploreCacheFileActivity)getActivity(), getData(this.mPath));
  }

  public void onAttach(Context paramContext)
  {
    super.onAttach(paramContext);
    try
    {
      this.mListener = ((Callbacks)paramContext);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
    }
    throw new ClassCastException(paramContext.toString() + " must implement FileListFragment.Callbacks");
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    super.onListItemClick(paramListView, paramView, paramInt, paramLong);
    paramListView = (DebugExploreCacheFileAdapter)paramListView.getAdapter();
    if (paramListView != null)
    {
      paramListView = (DebugExploreCacheFileFragment.FileItem)paramListView.getItem(paramInt);
      this.mListener.onFileSelect(paramListView.file, paramListView.size);
    }
  }

  public void onStart()
  {
    super.onStart();
    if (this.mAdapter != null)
      setListAdapter(this.mAdapter);
    setListShown(true);
  }

  public static abstract interface Callbacks
  {
    public abstract void onFileSelect(File paramFile, long paramLong);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugExploreCacheFileFragment
 * JD-Core Version:    0.6.0
 */
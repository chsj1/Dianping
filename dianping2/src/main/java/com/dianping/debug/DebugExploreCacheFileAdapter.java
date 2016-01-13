package com.dianping.debug;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DebugExploreCacheFileAdapter extends BaseAdapter
{
  DebugExploreCacheFileActivity mCtx;
  ArrayList<DebugExploreCacheFileFragment.FileItem> mData = new ArrayList();

  public DebugExploreCacheFileAdapter(DebugExploreCacheFileActivity paramDebugExploreCacheFileActivity, ArrayList<DebugExploreCacheFileFragment.FileItem> paramArrayList)
  {
    this.mCtx = paramDebugExploreCacheFileActivity;
    this.mData = paramArrayList;
  }

  static String formatSizeData(long paramLong)
  {
    if (paramLong <= 0L)
      return "0";
    int i = (int)(Math.log10(paramLong) / Math.log10(1024.0D));
    return new DecimalFormat("#,##0.##").format(paramLong / Math.pow(1024.0D, i)) + " " + new String[] { "B", "kB", "MB", "GB", "TB" }[i];
  }

  public int getCount()
  {
    return this.mData.size();
  }

  public Object getItem(int paramInt)
  {
    return this.mData.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject;
    ImageView localImageView;
    if (paramView == null)
    {
      localObject = new DebugExploreCacheFileAdapter.ViewHolder(this, null);
      paramView = this.mCtx.getLayoutInflater().inflate(R.layout.debug_explore_cache_item, paramViewGroup, false);
      ((DebugExploreCacheFileAdapter.ViewHolder)localObject).iconView = ((ImageView)paramView.findViewById(R.id.iv_icon));
      ((DebugExploreCacheFileAdapter.ViewHolder)localObject).fileName = ((TextView)paramView.findViewById(R.id.tv_filename));
      ((DebugExploreCacheFileAdapter.ViewHolder)localObject).modifyTime = ((TextView)paramView.findViewById(R.id.tv_modifytime));
      ((DebugExploreCacheFileAdapter.ViewHolder)localObject).subFileNum = ((TextView)paramView.findViewById(R.id.tv_subfilenum));
      ((DebugExploreCacheFileAdapter.ViewHolder)localObject).size = ((TextView)paramView.findViewById(R.id.tv_size));
      paramView.setTag(localObject);
      paramViewGroup = (ViewGroup)localObject;
      localObject = (DebugExploreCacheFileFragment.FileItem)this.mData.get(paramInt);
      localImageView = paramViewGroup.iconView;
      if (!((DebugExploreCacheFileFragment.FileItem)localObject).file.isDirectory())
        break label222;
    }
    label222: for (paramInt = R.drawable.ic_provider; ; paramInt = R.drawable.ic_file)
    {
      localImageView.setImageResource(paramInt);
      paramViewGroup.fileName.setText(((DebugExploreCacheFileFragment.FileItem)localObject).fileName);
      paramViewGroup.modifyTime.setText(((DebugExploreCacheFileFragment.FileItem)localObject).lastModifyTime);
      paramViewGroup.subFileNum.setText(String.valueOf(((DebugExploreCacheFileFragment.FileItem)localObject).subItemNum));
      paramViewGroup.size.setText(formatSizeData(((DebugExploreCacheFileFragment.FileItem)localObject).size));
      return paramView;
      paramViewGroup = (DebugExploreCacheFileAdapter.ViewHolder)paramView.getTag();
      break;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugExploreCacheFileAdapter
 * JD-Core Version:    0.6.0
 */
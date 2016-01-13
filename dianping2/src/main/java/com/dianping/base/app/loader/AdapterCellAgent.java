package com.dianping.base.app.loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;

public class AdapterCellAgent extends CellAgent
{
  public AdapterCellAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void addCell(String paramString, ListAdapter paramListAdapter)
  {
    if ((this.fragment instanceof AdapterAgentFragment))
      ((AdapterAgentFragment)this.fragment).addCell(this, paramString, paramListAdapter);
  }

  protected LinearLayout createDividerBlock()
  {
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    localLinearLayout.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(getContext(), 20.0F)));
    localLinearLayout.setBackgroundResource(R.drawable.home_cell_bottom);
    return localLinearLayout;
  }

  public abstract class BasicCellAgentAdapter extends BasicAdapter
  {
    public BasicCellAgentAdapter()
    {
    }

    protected View getFailedView(String paramString, LoadingErrorView.LoadRetry paramLoadRetry, ViewGroup paramViewGroup, View paramView, int paramInt)
    {
      if (paramView == null)
        paramView = null;
      View localView;
      while (true)
      {
        localView = paramView;
        if (paramView == null)
        {
          localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.error_item, paramViewGroup, false);
          localView.setBackgroundResource(R.color.white);
          if (paramInt > 0)
            localView.setMinimumHeight(paramInt);
          localView.setTag(ERROR);
        }
        ((TextView)localView.findViewById(16908308)).setText(paramString);
        if ((localView instanceof LoadingErrorView))
          break;
        return null;
        if (paramView.getTag() == ERROR)
          continue;
        paramView = null;
      }
      ((LoadingErrorView)localView).setCallBack(paramLoadRetry);
      return localView;
    }

    protected View getLoadingView(ViewGroup paramViewGroup, View paramView, int paramInt)
    {
      View localView = null;
      if (paramView == null);
      while (true)
      {
        paramView = localView;
        if (localView == null)
        {
          paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.loading_item, paramViewGroup, false);
          paramView.setBackgroundResource(R.color.white);
          if (paramInt > 0)
            paramView.setMinimumHeight(paramInt);
          paramView.setTag(LOADING);
        }
        return paramView;
        if (paramView.getTag() != LOADING)
          continue;
        localView = paramView;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.AdapterCellAgent
 * JD-Core Version:    0.6.0
 */
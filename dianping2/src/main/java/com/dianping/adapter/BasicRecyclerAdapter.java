package com.dianping.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;

public abstract class BasicRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
  public static final Object EMPTY;
  public static final Object ERROR;
  public static final Object FOOT;
  public static final Object HEAD;
  public static final Object LOADING = new Object();

  static
  {
    ERROR = new Object();
    HEAD = new Object();
    FOOT = new Object();
    EMPTY = new Object();
  }

  protected View getFailedView(ViewGroup paramViewGroup)
  {
    paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.error_item, paramViewGroup, false);
    paramViewGroup.setBackgroundColor(-1);
    paramViewGroup.setTag(ERROR);
    return paramViewGroup;
  }

  protected View getLoadingView(ViewGroup paramViewGroup)
  {
    paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.loading_item, paramViewGroup, false);
    paramViewGroup.setTag(LOADING);
    paramViewGroup.setBackgroundColor(-1);
    return paramViewGroup;
  }

  public class BasicHolder extends RecyclerView.ViewHolder
  {
    public View view;

    public BasicHolder(View arg2)
    {
      super();
      this.view = localView;
      init(localView);
    }

    public void init(View paramView)
    {
    }
  }

  public class FailHolder extends BasicRecyclerAdapter.BasicHolder
  {
    public LoadingErrorView errorView;
    public TextView text;

    public FailHolder(View arg2)
    {
      super(localView);
      this.errorView = ((LoadingErrorView)localView);
      this.text = ((TextView)localView.findViewById(16908308));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.adapter.BasicRecyclerAdapter
 * JD-Core Version:    0.6.0
 */
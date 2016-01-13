package com.dianping.tuan.fragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dianping.base.tuan.fragment.BaseTuanFragment;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

public class BaseTuanPtrListFragment extends BaseTuanFragment
  implements AdapterView.OnItemClickListener
{
  private TextView emptyTextView;
  protected FrameLayout emptyView;
  protected PullToRefreshListView listView;
  protected TextView notificationView;

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    paramLayoutInflater = onSetContentView(paramLayoutInflater, paramViewGroup);
    this.listView = ((PullToRefreshListView)paramLayoutInflater.findViewById(R.id.list));
    if (this.listView == null)
      this.listView = ((PullToRefreshListView)paramLayoutInflater.findViewById(16908298));
    this.listView.setFastScrollEnabled(true);
    this.listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
    this.listView.setDivider(getResources().getDrawable(R.drawable.gray_horizontal_line));
    this.listView.setOnItemClickListener(this);
    this.listView.setOnRefreshListener(new BaseTuanPtrListFragment.1(this));
    this.notificationView = ((TextView)paramLayoutInflater.findViewById(R.id.notification_header));
    this.emptyView = ((FrameLayout)paramLayoutInflater.findViewById(R.id.empty));
    this.emptyTextView = ((TextView)paramLayoutInflater.findViewById(R.id.empty_textview));
    return paramLayoutInflater;
  }

  public void onDestroyView()
  {
    this.emptyView = null;
    this.emptyTextView = null;
    this.listView = null;
    super.onDestroyView();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
  }

  protected void onPullToRefresh()
  {
  }

  protected View onSetContentView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    return paramLayoutInflater.inflate(R.layout.tuan_ptr_list_frame, paramViewGroup, false);
  }

  protected void setEmpty(String paramString)
  {
    if (this.emptyView != null)
      this.listView.setEmptyView(this.emptyView);
    if (this.emptyTextView != null)
      this.emptyTextView.setText(paramString);
  }

  protected void setEmptyView(View paramView)
  {
    if (this.emptyView != null)
      this.listView.setEmptyView(this.emptyView);
    if (paramView != null)
    {
      this.emptyView.removeAllViews();
      this.emptyView.addView(paramView);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.BaseTuanPtrListFragment
 * JD-Core Version:    0.6.0
 */
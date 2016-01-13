package com.dianping.tuan.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;

public class BaseTuanPtrListActivity extends BaseTuanActivity
  implements AdapterView.OnItemClickListener
{
  private TextView emptyTextView;
  private View emptyView;
  protected PullToRefreshListView listView;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    onSetContentView();
    this.listView = ((PullToRefreshListView)findViewById(R.id.list));
    if (this.listView == null)
      this.listView = ((PullToRefreshListView)findViewById(16908298));
    this.emptyView = findViewById(R.id.empty);
    this.emptyTextView = ((TextView)findViewById(R.id.empty_textview));
    this.listView.setSelector(R.drawable.list_item);
    this.listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
    this.listView.setDivider(getResources().getDrawable(R.drawable.list_divider_right_inset));
    this.listView.setFastScrollEnabled(true);
    this.listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
      {
        BaseTuanPtrListActivity.this.onPullToRefresh();
      }
    });
    this.listView.setOnItemClickListener(this);
  }

  public void onDestroy()
  {
    this.emptyView = null;
    this.emptyTextView = null;
    this.listView = null;
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
  }

  protected void onPullToRefresh()
  {
  }

  protected void onSetContentView()
  {
    setContentView(R.layout.tuan_ptr_list_frame);
  }

  protected void setEmpty(String paramString)
  {
    if (this.emptyView != null)
      this.listView.setEmptyView(this.emptyView);
    if (this.emptyTextView != null)
      this.emptyTextView.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.BaseTuanPtrListActivity
 * JD-Core Version:    0.6.0
 */
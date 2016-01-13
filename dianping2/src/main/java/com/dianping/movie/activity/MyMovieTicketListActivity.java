package com.dianping.movie.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.movie.fragment.MovieTicketListFragment;
import com.dianping.movie.fragment.MovieTicketListFragment.OnTitleChangeListener;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class MyMovieTicketListActivity extends NovaListActivity
  implements View.OnClickListener, MovieTicketListFragment.OnTitleChangeListener
{
  private MovieTicketListFragment currentFragment;
  private FragmentManager fragmentManager;
  private boolean isEdit;
  private TitleBar mTitleBar;
  private MovieTicketListFragment movieTicketClosedFragment;
  private MovieTicketListFragment movieTicketOnGoingFragment;
  private Button movieTicketStatusClosed;
  private Button movieTicketStatusToBeUsed;
  private int ticketFilter;

  private void changToMovieTicketClosed()
  {
    FragmentTransaction localFragmentTransaction = this.fragmentManager.beginTransaction();
    if (this.movieTicketClosedFragment == null)
    {
      this.movieTicketClosedFragment = new MovieTicketListFragment();
      Bundle localBundle = new Bundle();
      localBundle.putInt("filter", 2);
      this.movieTicketClosedFragment.setArguments(localBundle);
      localFragmentTransaction.add(R.id.layer_content, this.movieTicketClosedFragment);
    }
    while (true)
    {
      if (this.movieTicketOnGoingFragment != null)
        localFragmentTransaction.hide(this.movieTicketOnGoingFragment);
      localFragmentTransaction.commit();
      this.currentFragment = this.movieTicketClosedFragment;
      this.ticketFilter = 2;
      this.movieTicketStatusClosed.setSelected(true);
      this.movieTicketStatusToBeUsed.setSelected(false);
      return;
      localFragmentTransaction.show(this.movieTicketClosedFragment);
    }
  }

  private void changToMovieTicketOnGoing()
  {
    FragmentTransaction localFragmentTransaction = this.fragmentManager.beginTransaction();
    if (this.movieTicketOnGoingFragment == null)
    {
      this.movieTicketOnGoingFragment = new MovieTicketListFragment();
      Bundle localBundle = new Bundle();
      localBundle.putInt("filter", 1);
      this.movieTicketOnGoingFragment.setArguments(localBundle);
      localFragmentTransaction.add(R.id.layer_content, this.movieTicketOnGoingFragment);
    }
    while (true)
    {
      if (this.movieTicketClosedFragment != null)
        localFragmentTransaction.hide(this.movieTicketClosedFragment);
      localFragmentTransaction.commit();
      this.currentFragment = this.movieTicketOnGoingFragment;
      this.ticketFilter = 1;
      this.movieTicketStatusToBeUsed.setSelected(true);
      this.movieTicketStatusClosed.setSelected(false);
      return;
      localFragmentTransaction.show(this.movieTicketOnGoingFragment);
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.movieticket_status_tobeused)
    {
      statisticsEvent("movie5", "movie5_ticket_tobeused", "", 0);
      if (this.ticketFilter != 1);
    }
    do
    {
      do
      {
        return;
        changToMovieTicketOnGoing();
        return;
      }
      while (i != R.id.movieticket_status_closed);
      statisticsEvent("movie5", "movie5_ticket_closed", "", 0);
    }
    while (this.ticketFilter == 2);
    changToMovieTicketClosed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040011");
    setContentView(R.layout.my_movie_ticket_list_activity);
    this.movieTicketStatusToBeUsed = ((Button)findViewById(R.id.movieticket_status_tobeused));
    this.movieTicketStatusClosed = ((Button)findViewById(R.id.movieticket_status_closed));
    this.movieTicketStatusToBeUsed.setOnClickListener(this);
    this.movieTicketStatusClosed.setOnClickListener(this);
    this.movieTicketStatusToBeUsed.setSelected(true);
    this.movieTicketStatusClosed.setSelected(false);
    this.fragmentManager = getSupportFragmentManager();
    this.mTitleBar = getTitleBar();
    changToMovieTicketOnGoing();
  }

  protected void onDestroy()
  {
    super.onDestroy();
  }

  public void onTitleChanged(int paramInt, boolean paramBoolean)
  {
    this.mTitleBar.removeAllRightViewItem();
    this.isEdit = paramBoolean;
    this.movieTicketStatusToBeUsed.setEnabled(true);
    this.movieTicketStatusClosed.setEnabled(true);
    if (paramInt <= 0)
    {
      setTitle("我的电影票");
      return;
    }
    if (this.isEdit)
    {
      this.mTitleBar.addRightViewItem("取消", "cancel", new View.OnClickListener(paramInt)
      {
        public void onClick(View paramView)
        {
          MyMovieTicketListActivity.access$002(MyMovieTicketListActivity.this, false);
          if (MyMovieTicketListActivity.this.currentFragment != null)
            MyMovieTicketListActivity.this.currentFragment.setIsEdit(MyMovieTicketListActivity.this.isEdit);
          MyMovieTicketListActivity.this.onTitleChanged(this.val$recordCount, MyMovieTicketListActivity.this.isEdit);
        }
      });
      this.movieTicketStatusToBeUsed.setEnabled(false);
      this.movieTicketStatusClosed.setEnabled(false);
      setTitle("删除订单");
      return;
    }
    this.mTitleBar.addRightViewItem("remove", R.drawable.history_remove, new View.OnClickListener(paramInt)
    {
      public void onClick(View paramView)
      {
        MyMovieTicketListActivity.access$002(MyMovieTicketListActivity.this, true);
        if (MyMovieTicketListActivity.this.currentFragment != null)
          MyMovieTicketListActivity.this.currentFragment.setIsEdit(MyMovieTicketListActivity.this.isEdit);
        MyMovieTicketListActivity.this.onTitleChanged(this.val$recordCount, MyMovieTicketListActivity.this.isEdit);
      }
    });
    setTitle("我的电影票");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MyMovieTicketListActivity
 * JD-Core Version:    0.6.0
 */
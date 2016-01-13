package com.dianping.base.widget;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class NovaListFragment extends NovaFragment
{
  ListAdapter mAdapter;
  CharSequence mEmptyText;
  View mEmptyView;
  private final Handler mHandler = new Handler();
  ListView mList;
  View mListContainer;
  boolean mListShown;
  private final AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      NovaListFragment.this.onListItemClick((ListView)paramAdapterView, paramView, paramInt, paramLong);
    }
  };
  private final AdapterView.OnItemLongClickListener mOnLongClickListener = new AdapterView.OnItemLongClickListener()
  {
    public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      return NovaListFragment.this.onListItemLongClick((ListView)paramAdapterView, paramView, paramInt, paramLong);
    }
  };
  View mProgressContainer;
  private final Runnable mRequestFocus = new Runnable()
  {
    public void run()
    {
      NovaListFragment.this.mList.focusableViewAvailable(NovaListFragment.this.mList);
    }
  };
  TextView mStandardEmptyView;

  private void ensureList()
  {
    if (this.mList != null)
      return;
    Object localObject = getView();
    if (localObject == null)
      throw new IllegalStateException("Content view not yet created");
    if ((localObject instanceof ListView))
    {
      this.mList = ((ListView)localObject);
      this.mListShown = true;
      this.mList.setOnItemClickListener(this.mOnClickListener);
      this.mList.setOnItemLongClickListener(this.mOnLongClickListener);
      if (this.mAdapter == null)
        break label270;
      localObject = this.mAdapter;
      this.mAdapter = null;
      setListAdapter((ListAdapter)localObject);
    }
    while (true)
    {
      this.mHandler.post(this.mRequestFocus);
      return;
      this.mStandardEmptyView = ((TextView)((View)localObject).findViewById(16908292));
      if (this.mStandardEmptyView == null)
        this.mEmptyView = ((View)localObject).findViewById(16908292);
      while (true)
      {
        this.mProgressContainer = ((View)localObject).findViewById(R.id.progressContainer);
        this.mListContainer = ((View)localObject).findViewById(R.id.listContainer);
        localObject = ((View)localObject).findViewById(16908298);
        if ((localObject instanceof ListView))
          break;
        throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
        this.mStandardEmptyView.setVisibility(8);
      }
      this.mList = ((ListView)localObject);
      if (this.mList == null)
        throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
      if (this.mEmptyView != null)
      {
        this.mList.setEmptyView(this.mEmptyView);
        break;
      }
      if (this.mEmptyText == null)
        break;
      this.mStandardEmptyView.setText(this.mEmptyText);
      this.mList.setEmptyView(this.mStandardEmptyView);
      break;
      label270: if (this.mProgressContainer == null)
        continue;
      setListShown(false, false);
    }
  }

  private void setListShown(boolean paramBoolean1, boolean paramBoolean2)
  {
    ensureList();
    if (this.mProgressContainer == null)
      throw new IllegalStateException("Can't be used with a custom content view");
    if (this.mListShown == paramBoolean1)
      return;
    this.mListShown = paramBoolean1;
    if (paramBoolean1)
    {
      if (paramBoolean2)
      {
        this.mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432577));
        this.mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432576));
      }
      while (true)
      {
        this.mProgressContainer.setVisibility(8);
        this.mListContainer.setVisibility(0);
        return;
        this.mProgressContainer.clearAnimation();
        this.mListContainer.clearAnimation();
      }
    }
    if (paramBoolean2)
    {
      this.mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432576));
      this.mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432577));
    }
    while (true)
    {
      this.mProgressContainer.setVisibility(0);
      this.mListContainer.setVisibility(8);
      return;
      this.mProgressContainer.clearAnimation();
      this.mListContainer.clearAnimation();
    }
  }

  public ListAdapter getListAdapter()
  {
    return this.mAdapter;
  }

  public ListView getListView()
  {
    ensureList();
    return this.mList;
  }

  public long getSelectedItemId()
  {
    ensureList();
    return this.mList.getSelectedItemId();
  }

  public int getSelectedItemPosition()
  {
    ensureList();
    return this.mList.getSelectedItemPosition();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(R.layout.list_content, paramViewGroup, false);
  }

  public void onDestroyView()
  {
    this.mHandler.removeCallbacks(this.mRequestFocus);
    this.mList = null;
    this.mListShown = false;
    this.mListContainer = null;
    this.mProgressContainer = null;
    this.mEmptyView = null;
    this.mStandardEmptyView = null;
    super.onDestroyView();
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
  }

  public boolean onListItemLongClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    return false;
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ensureList();
  }

  public void setEmptyText(CharSequence paramCharSequence)
  {
    ensureList();
    if (this.mStandardEmptyView == null)
      throw new IllegalStateException("Can't be used with a custom content view");
    this.mStandardEmptyView.setText(paramCharSequence);
    if (this.mEmptyText == null)
      this.mList.setEmptyView(this.mStandardEmptyView);
    this.mEmptyText = paramCharSequence;
  }

  public void setListAdapter(ListAdapter paramListAdapter)
  {
    boolean bool = false;
    if (this.mAdapter != null);
    for (int i = 1; ; i = 0)
    {
      this.mAdapter = paramListAdapter;
      if (this.mList != null)
      {
        this.mList.setAdapter(paramListAdapter);
        if ((!this.mListShown) && (i == 0))
        {
          if (getView().getWindowToken() != null)
            bool = true;
          setListShown(true, bool);
        }
      }
      return;
    }
  }

  public void setListShown(boolean paramBoolean)
  {
    setListShown(paramBoolean, true);
  }

  public void setListShownNoAnimation(boolean paramBoolean)
  {
    setListShown(paramBoolean, false);
  }

  public void setSelection(int paramInt)
  {
    ensureList();
    this.mList.setSelection(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.NovaListFragment
 * JD-Core Version:    0.6.0
 */
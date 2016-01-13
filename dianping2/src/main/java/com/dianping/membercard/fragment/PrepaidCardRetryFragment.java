package com.dianping.membercard.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.membercard.view.PrepaidCardItem;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;

public class PrepaidCardRetryFragment extends CardFragment
{
  private Adapter adapter;
  private PrepaidCardItem cardInfoView;
  private View containerView;
  private LoadingErrorView.LoadRetry retryListener;
  private ListView statusView;

  public void dimissFailedView()
  {
    this.statusView.setVisibility(4);
  }

  public void inflateMemberCard(DPObject paramDPObject)
  {
    this.cardObject = paramDPObject;
    this.cardInfoView.setData(paramDPObject);
    this.cardInfoView.setOnClickListener(this);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.containerView = paramLayoutInflater.inflate(R.layout.prepaid_card_retry_layout, paramViewGroup, false);
    this.cardInfoView = ((PrepaidCardItem)this.containerView.findViewById(R.id.container));
    this.statusView = ((ListView)this.containerView.findViewById(R.id.statusList));
    this.adapter = new Adapter();
    this.statusView.setAdapter(this.adapter);
    inflateMemberCard(this.cardObject);
    return this.containerView;
  }

  public void showFailedView()
  {
    this.statusView.setVisibility(0);
  }

  public void showFailedView(String paramString, LoadingErrorView.LoadRetry paramLoadRetry)
  {
    this.retryListener = paramLoadRetry;
    this.adapter.setErrorMsg(paramString);
  }

  public void showLoadingView()
  {
    this.adapter.reset();
  }

  class Adapter extends BasicAdapter
  {
    String errorMsg;

    public Adapter()
    {
    }

    public int getCount()
    {
      return 1;
    }

    public Object getItem(int paramInt)
    {
      if (this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (getItem(paramInt) == LOADING)
        return getLoadingView(paramViewGroup, paramView);
      return getFailedView(this.errorMsg, PrepaidCardRetryFragment.this.retryListener, paramViewGroup, paramView);
    }

    public void refresh()
    {
      notifyDataSetChanged();
    }

    public void reset()
    {
      this.errorMsg = null;
      notifyDataSetChanged();
    }

    public void setErrorMsg(String paramString)
    {
      this.errorMsg = paramString;
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.PrepaidCardRetryFragment
 * JD-Core Version:    0.6.0
 */
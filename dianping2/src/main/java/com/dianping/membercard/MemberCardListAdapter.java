package com.dianping.membercard;

import android.app.Activity;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.membercard.view.MemberCardListItem;
import com.dianping.membercard.view.ThirdPartyCardListItem;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;
import java.util.List;

public class MemberCardListAdapter extends BasicAdapter
{
  private Activity activity;
  private List<DPObject> mCardList = new ArrayList();
  protected String mEmptyMsg;
  protected String mErrorMsg;
  private boolean mIsEnd;
  private int mNextStartIndex;
  private int mRecordCount;
  private float scale;

  public MemberCardListAdapter(Activity paramActivity)
  {
    this.activity = paramActivity;
    this.scale = new DisplayMetrics().density;
  }

  public int getCount()
  {
    if (this.mIsEnd)
    {
      if (this.mCardList.size() == 0)
        return 1;
      return this.mCardList.size();
    }
    return this.mCardList.size() + 1;
  }

  public List<DPObject> getData()
  {
    return this.mCardList;
  }

  protected View getEmptyMsgView(String paramString1, String paramString2, ViewGroup paramViewGroup, View paramView)
  {
    View localView = null;
    if (paramView == null);
    while (true)
    {
      paramView = localView;
      if (localView == null)
      {
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.card_list_empty_item, paramViewGroup, false);
        paramView.setTag(ERROR);
      }
      paramViewGroup = (TextView)paramView.findViewById(16908308);
      if (!TextUtils.isEmpty(paramString1))
        break;
      if (!TextUtils.isEmpty(paramString2))
        paramViewGroup.setText(Html.fromHtml(paramString2));
      return paramView;
      if (paramView.getTag() != EMPTY)
        continue;
      localView = paramView;
    }
    paramViewGroup.setText(Html.fromHtml(paramString1));
    return paramView;
  }

  protected View getFailedView(String paramString, LoadingErrorView.LoadRetry paramLoadRetry, ViewGroup paramViewGroup, View paramView)
  {
    View localView = null;
    if (paramView == null);
    while (true)
    {
      paramView = localView;
      if (localView == null)
      {
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.card_list_failed_item, paramViewGroup, false);
        paramView.setTag(ERROR);
      }
      ((TextView)paramView.findViewById(16908308)).setText(paramString);
      paramString = (LoadingErrorView)paramView.findViewById(R.id.error);
      paramString.setType(2);
      paramString.setCallBack(paramLoadRetry);
      return paramView;
      if (paramView.getTag() != ERROR)
        continue;
      localView = paramView;
    }
  }

  public Object getItem(int paramInt)
  {
    if (paramInt < this.mCardList.size())
      return this.mCardList.get(paramInt);
    if ((this.mCardList.size() == 0) && (this.mIsEnd))
      return EMPTY;
    if (this.mErrorMsg == null)
      return LOADING;
    return ERROR;
  }

  public long getItemId(int paramInt)
  {
    Object localObject = getItem(paramInt);
    if ((localObject instanceof DPObject))
      return ((DPObject)localObject).getInt("MemberCardID");
    if (localObject == LOADING)
      return -paramInt;
    return -2147483648L;
  }

  public int getItemViewType(int paramInt)
  {
    if ((getItem(paramInt) instanceof DPObject))
    {
      if (paramInt == 0)
        return 0;
      if (paramInt == this.mCardList.size() - 1)
        return 1;
      return 2;
    }
    return 3;
  }

  protected View getLoadingView(ViewGroup paramViewGroup, View paramView)
  {
    View localView = null;
    if (paramView == null);
    while (true)
    {
      paramView = localView;
      if (localView == null)
      {
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.card_list_loading_item, paramViewGroup, false);
        paramView.setTag(LOADING);
      }
      return paramView;
      if (paramView.getTag() != LOADING)
        continue;
      localView = paramView;
    }
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    LayoutInflater localLayoutInflater = LayoutInflater.from(paramViewGroup.getContext());
    if ((localObject instanceof DPObject))
    {
      if (MemberCard.isThirdPartyCard((DPObject)localObject))
      {
        localView = paramView;
        if (!(paramView instanceof ThirdPartyCardListItem))
          localView = localLayoutInflater.inflate(R.layout.mc_third_party_card_list_item, paramViewGroup, false);
        ((ThirdPartyCardListItem)localView).setData((DPObject)localObject);
        return localView;
      }
      View localView = paramView;
      if (!(paramView instanceof MemberCardListItem))
        localView = localLayoutInflater.inflate(R.layout.card_list_item, paramViewGroup, false);
      ((MemberCardListItem)localView).setData((DPObject)localObject);
      return localView;
    }
    if (localObject == LOADING)
    {
      loadNewPage();
      return (MemberCardListItem)getLoadingView(paramViewGroup, paramView);
    }
    if (localObject == EMPTY)
      return (FrameLayout)getEmptyMsgView(this.mEmptyMsg, "", paramViewGroup, paramView);
    return (MemberCardListItem)getFailedView(this.mErrorMsg, new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        MemberCardListAdapter.this.loadNewPage();
      }
    }
    , paramViewGroup, paramView);
  }

  public int getViewTypeCount()
  {
    return 4;
  }

  public int getmNextStartIndex()
  {
    return this.mNextStartIndex;
  }

  public boolean ismIsEnd()
  {
    return this.mIsEnd;
  }

  protected boolean loadNewPage()
  {
    return false;
  }

  protected void reset()
  {
    this.mCardList.clear();
    this.mIsEnd = false;
    this.mErrorMsg = null;
    this.mEmptyMsg = null;
    this.mNextStartIndex = 0;
    this.mRecordCount = 0;
  }

  public void setData(List<DPObject> paramList)
  {
    this.mCardList = paramList;
  }

  public void setEmptyMsg(String paramString)
  {
    this.mEmptyMsg = paramString;
  }

  public void setErrorMsg(String paramString)
  {
    this.mErrorMsg = paramString;
  }

  public void setmEmptyMsg(String paramString)
  {
    this.mEmptyMsg = paramString;
  }

  public void setmErrorMsg(String paramString)
  {
    this.mErrorMsg = paramString;
  }

  public void setmIsEnd(boolean paramBoolean)
  {
    this.mIsEnd = paramBoolean;
  }

  public void setmNextStartIndex(int paramInt)
  {
    this.mNextStartIndex = paramInt;
  }

  public void setmRecordCount(int paramInt)
  {
    this.mRecordCount = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.MemberCardListAdapter
 * JD-Core Version:    0.6.0
 */
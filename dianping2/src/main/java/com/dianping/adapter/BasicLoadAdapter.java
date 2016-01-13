package com.dianping.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.log.NovaLog;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicLoadAdapter extends BasicAdapter
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected ArrayList<DPObject> mData = new ArrayList();
  protected String mEmptyMsg;
  protected String mErrorMsg;
  protected boolean mIsEnd;
  protected boolean mIsPullToRefresh = false;
  protected int mNextStartIndex;
  protected String mQueryId;
  protected int mRecordCount;
  protected MApiRequest mReq;
  protected MApiService mapiService;

  public BasicLoadAdapter(Context paramContext)
  {
    if ((paramContext instanceof DPActivity));
    for (paramContext = ((DPActivity)paramContext).mapiService(); ; paramContext = DPApplication.instance().mapiService())
    {
      this.mapiService = paramContext;
      return;
    }
  }

  public void appendData(DPObject paramDPObject)
  {
    if (this.mIsPullToRefresh)
    {
      this.mIsPullToRefresh = false;
      this.mData.clear();
    }
    this.mEmptyMsg = paramDPObject.getString("EmptyMsg");
    this.mIsEnd = paramDPObject.getBoolean("IsEnd");
    this.mNextStartIndex = paramDPObject.getInt("NextStartIndex");
    this.mRecordCount = paramDPObject.getInt("RecordCount");
    this.mQueryId = paramDPObject.getString("QueryID");
    paramDPObject = paramDPObject.getArray("List");
    if (paramDPObject != null)
    {
      appendDataToList(paramDPObject);
      if ((this.mData.size() == 0) && (this.mEmptyMsg == null))
        this.mEmptyMsg = "数据为空";
      if (paramDPObject.length != 0);
    }
    for (this.mIsEnd = true; ; this.mIsEnd = true)
    {
      notifyDataSetChanged();
      return;
    }
  }

  public void appendData(List<DPObject> paramList)
  {
    if (paramList != null)
      this.mData.addAll(paramList);
    notifyDataSetChanged();
  }

  public void appendData(DPObject[] paramArrayOfDPObject)
  {
    appendDataToList(paramArrayOfDPObject);
    this.mIsEnd = true;
    notifyDataSetChanged();
  }

  protected void appendDataToList(DPObject[] paramArrayOfDPObject)
  {
    if (paramArrayOfDPObject != null)
    {
      int i = 0;
      while (i < paramArrayOfDPObject.length)
      {
        this.mData.add(paramArrayOfDPObject[i]);
        i += 1;
      }
    }
  }

  public boolean areAllItemsEnabled()
  {
    return false;
  }

  public void cancelLoad()
  {
    if (this.mReq != null)
    {
      this.mapiService.abort(this.mReq, this, true);
      this.mReq = null;
    }
  }

  public abstract MApiRequest createRequest(int paramInt);

  protected String emptyMessage()
  {
    return this.mEmptyMsg;
  }

  public int getCount()
  {
    if (this.mIsEnd)
    {
      if (this.mData.size() == 0)
        return 1;
      return this.mData.size();
    }
    return this.mData.size() + 1;
  }

  public ArrayList<DPObject> getDataList()
  {
    return this.mData;
  }

  public Object getItem(int paramInt)
  {
    if (paramInt < this.mData.size())
      return this.mData.get(paramInt);
    if ((this.mEmptyMsg != null) || ((this.mIsEnd) && (this.mData.size() == 0)))
      return EMPTY;
    if (this.mErrorMsg == null)
      return LOADING;
    return ERROR;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public int getItemViewType(int paramInt)
  {
    Object localObject = getItem(paramInt);
    if (localObject == ERROR)
      return 0;
    if (localObject == LOADING)
      return 1;
    if (localObject == EMPTY)
      return 2;
    return 3;
  }

  public int getNextStartIndex()
  {
    return this.mNextStartIndex;
  }

  public int getRecordCount()
  {
    return this.mRecordCount;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    if ((localObject instanceof DPObject))
      return itemViewWithData((DPObject)localObject, paramInt, paramView, paramViewGroup);
    if (localObject == LOADING)
    {
      if (this.mErrorMsg == null)
        loadNewPage();
      return getLoadingView(paramViewGroup, paramView);
    }
    if (localObject == EMPTY)
      return getEmptyView(emptyMessage(), "暂时没有你要找的哦，看看别的吧", paramViewGroup, paramView);
    return getFailedView(this.mErrorMsg, new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        BasicLoadAdapter.this.loadNewPage();
      }
    }
    , paramViewGroup, paramView);
  }

  public int getViewTypeCount()
  {
    return 4;
  }

  public boolean isEnabled(int paramInt)
  {
    return getItem(paramInt) instanceof DPObject;
  }

  public boolean isEnd()
  {
    return this.mIsEnd;
  }

  protected abstract View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup);

  protected boolean loadNewPage()
  {
    if (this.mIsEnd);
    do
      return false;
    while (this.mReq != null);
    this.mErrorMsg = null;
    this.mReq = createRequest(this.mNextStartIndex);
    if (this.mReq != null)
      this.mapiService.exec(this.mReq, this);
    notifyDataSetChanged();
    return true;
  }

  protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.message() == null) || (paramMApiResponse.message().content() == null));
    for (String str = "请求失败，请稍后再试"; ; str = paramMApiResponse.message().content())
    {
      setErrorMsg(str);
      this.mReq = null;
      onRequestComplete(false, paramMApiRequest, paramMApiResponse);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject))
      appendData((DPObject)paramMApiResponse.result());
    while (true)
    {
      this.mReq = null;
      onRequestComplete(true, paramMApiRequest, paramMApiResponse);
      return;
      if (!(paramMApiResponse.result() instanceof DPObject[]))
        break;
      appendData((DPObject[])(DPObject[])paramMApiResponse.result());
    }
    if (paramMApiResponse.message() == null);
    for (String str = "请求失败，请稍后再试"; ; str = paramMApiResponse.message().content())
    {
      setErrorMsg(str);
      break;
    }
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    ArrayList localArrayList = paramBundle.getParcelableArrayList("BaseDPAdapter_dplist");
    if (localArrayList == null)
    {
      NovaLog.e("BasicLoadAdapter", "onRestoreInstanceState(Bundle savedInstanceState):mdata == null");
      return;
    }
    this.mData = localArrayList;
    this.mEmptyMsg = paramBundle.getString("BaseDPAdapter_emptymsg");
    this.mNextStartIndex = paramBundle.getInt("BaseDPAdapter_nextstartindex");
    this.mRecordCount = paramBundle.getInt("BaseDPAdapter_recordcount");
    this.mQueryId = paramBundle.getString("BaseDPAdapter_queryid");
    this.mErrorMsg = paramBundle.getString("BaseDPAdapter_errormsg");
    this.mIsEnd = paramBundle.getBoolean("BaseDPAdapter_isend");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("BaseDPAdapter_emptymsg", this.mEmptyMsg);
    paramBundle.putInt("BaseDPAdapter_nextstartindex", this.mNextStartIndex);
    paramBundle.putInt("BaseDPAdapter_recordcount", this.mRecordCount);
    paramBundle.putString("BaseDPAdapter_queryid", this.mQueryId);
    paramBundle.putString("BaseDPAdapter_errormsg", this.mErrorMsg);
    paramBundle.putBoolean("BaseDPAdapter_isend", this.mIsEnd);
    paramBundle.putParcelableArrayList("BaseDPAdapter_dplist", this.mData);
  }

  public void pullToReset(boolean paramBoolean)
  {
    this.mIsPullToRefresh = paramBoolean;
    cancelLoad();
    this.mNextStartIndex = 0;
    this.mIsEnd = false;
    this.mRecordCount = 0;
    this.mQueryId = null;
    this.mErrorMsg = null;
    this.mEmptyMsg = null;
    loadNewPage();
  }

  public void remove(Object paramObject)
  {
    if (this.mData.remove(paramObject))
      notifyDataSetChanged();
  }

  public void reset()
  {
    cancelLoad();
    this.mData.clear();
    this.mNextStartIndex = 0;
    this.mIsEnd = false;
    this.mRecordCount = 0;
    this.mQueryId = null;
    this.mIsPullToRefresh = false;
    this.mErrorMsg = null;
    this.mEmptyMsg = null;
    notifyDataSetChanged();
  }

  public void setData(ArrayList<DPObject> paramArrayList)
  {
    if (paramArrayList == null)
    {
      NovaLog.e("BasicLoadAdapter", "setData(ArrayList<DPObject> data):data == null");
      return;
    }
    this.mData = paramArrayList;
    notifyDataSetChanged();
  }

  public void setData(DPObject[] paramArrayOfDPObject)
  {
    this.mData.clear();
    int j = paramArrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      this.mData.add(localDPObject);
      i += 1;
    }
    notifyDataSetChanged();
  }

  public void setEmptyMsg(String paramString)
  {
    this.mEmptyMsg = paramString;
    notifyDataSetChanged();
  }

  public void setErrorMsg(String paramString)
  {
    this.mErrorMsg = paramString;
    notifyDataSetChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.adapter.BasicLoadAdapter
 * JD-Core Version:    0.6.0
 */
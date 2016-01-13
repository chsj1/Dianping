package com.dianping.base.app.loader;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.util.Log;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class MyAdapter extends BaseAdapter
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected Context context;
  protected Object error;
  private final LoadingErrorView.LoadRetry errorRetry = new LoadingErrorView.LoadRetry()
  {
    public void loadRetry(View paramView)
    {
      MyAdapter.this.retry();
    }
  };
  protected boolean isEnd;
  protected ArrayList<DPObject> list = new ArrayList();
  private int nextStartIndex;
  protected boolean pending;
  private int recordCount;
  protected MApiRequest request;
  private MyResources res;

  public MyAdapter(Context paramContext)
  {
    this.context = paramContext;
    this.res = MyResources.getResource(getClass());
  }

  private MApiService mapiService()
  {
    if ((this.context instanceof DPActivity))
      return ((DPActivity)this.context).mapiService();
    return DPApplication.instance().mapiService();
  }

  public void addItem(int paramInt, DPObject paramDPObject)
  {
    this.list.add(paramInt, paramDPObject);
    notifyDataSetChanged();
  }

  public boolean areAllItemsEnabled()
  {
    return false;
  }

  public int getCount()
  {
    int j = 1;
    int i = 1;
    if (this.pending)
      i = this.list.size() + 1;
    do
    {
      return i;
      if (this.request != null)
        return this.list.size() + 1;
      if (this.error != null)
        return this.list.size() + 1;
    }
    while ((this.isEnd) && (this.list.size() == 0));
    int k = this.list.size();
    i = j;
    if (this.isEnd)
      i = 0;
    return i + k;
  }

  protected View getEmptyView(ViewGroup paramViewGroup)
  {
    return this.res.inflate(this.context, R.layout.empty_item, paramViewGroup, false);
  }

  public Object getError()
  {
    return this.error;
  }

  protected View getErrorView(ViewGroup paramViewGroup)
  {
    paramViewGroup = this.res.inflate(this.context, R.layout.error_item, paramViewGroup, false);
    if ((paramViewGroup instanceof LoadingErrorView))
      ((LoadingErrorView)paramViewGroup).setCallBack(this.errorRetry);
    return paramViewGroup;
  }

  public Object getItem(int paramInt)
  {
    if (paramInt < this.list.size())
      return this.list.get(paramInt);
    if (isPending())
      return "PENDING";
    if (this.error != null)
      return "ERROR";
    if (this.request != null)
      return "LOADING";
    if ((this.isEnd) && (this.list.size() == 0))
      return "EMPTY";
    return "LOADING";
  }

  public long getItemId(int paramInt)
  {
    Object localObject = getItem(paramInt);
    if ((localObject instanceof DPObject))
      return ((DPObject)localObject).getInt("ID");
    return localObject.hashCode();
  }

  public int getItemViewType(int paramInt)
  {
    if ((getItem(paramInt) instanceof DPObject))
      return 0;
    return 1;
  }

  public ArrayList<DPObject> getList()
  {
    return this.list;
  }

  protected View getLoadingView(ViewGroup paramViewGroup)
  {
    return this.res.inflate(this.context, R.layout.loading_item, paramViewGroup, false);
  }

  protected View getPendingView(ViewGroup paramViewGroup)
  {
    return getLoadingView(paramViewGroup);
  }

  public int getRecordCount()
  {
    return this.recordCount;
  }

  public MApiRequest getRequest()
  {
    return this.request;
  }

  public abstract MApiRequest getRequest(int paramInt);

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    if ((localObject instanceof DPObject))
      return getView((DPObject)localObject, paramView, paramViewGroup);
    if (localObject == "LOADING")
    {
      if (this.request == null)
        loadNextPage();
      return getLoadingView(paramViewGroup);
    }
    if (localObject == "PENDING")
      return getPendingView(paramViewGroup);
    if (localObject == "ERROR")
      return getErrorView(paramViewGroup);
    return getEmptyView(paramViewGroup);
  }

  protected abstract View getView(DPObject paramDPObject, View paramView, ViewGroup paramViewGroup);

  public int getViewTypeCount()
  {
    return 2;
  }

  public boolean hasStableIds()
  {
    return true;
  }

  public boolean isEnabled(int paramInt)
  {
    Object localObject = getItem(paramInt);
    if (localObject == "LOADING");
    do
      return false;
    while ((localObject == "PENDING") || (localObject == "EMPTY"));
    return true;
  }

  public boolean isEnd()
  {
    return this.isEnd;
  }

  public boolean isPending()
  {
    return this.pending;
  }

  public void loadNextPage()
  {
    if (this.request != null)
      return;
    if (this.nextStartIndex > 0);
    for (int i = this.nextStartIndex; ; i = this.list.size())
    {
      this.request = getRequest(i);
      if (this.request != null)
        mapiService().exec(this.request, this);
      if (this.request != null)
        break;
      new Handler().post(new Runnable()
      {
        public void run()
        {
          MyAdapter.this.pending = true;
          MyAdapter.this.notifyDataSetChanged();
        }
      });
      return;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.error = paramMApiResponse.error();
    notifyDataSetChanged();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      paramMApiResponse = paramMApiRequest.getArray("List");
      if (paramMApiResponse == null)
      {
        Log.e("adapter", "ResultList.List is not found in response");
        this.isEnd = true;
      }
    }
    while (true)
    {
      this.error = null;
      this.request = null;
      notifyDataSetChanged();
      return;
      this.list.addAll(Arrays.asList(paramMApiResponse));
      int i = paramMApiRequest.getInt("RecordCount");
      if (i > 0)
        this.recordCount = i;
      if ((paramMApiResponse.length == 0) || (paramMApiRequest.getBoolean("IsEnd")));
      for (boolean bool = true; ; bool = false)
      {
        this.isEnd = bool;
        this.nextStartIndex = paramMApiRequest.getInt("NextStartIndex");
        break;
      }
      if (!(paramMApiResponse.result() instanceof DPObject[]))
        continue;
      paramMApiRequest = (DPObject[])(DPObject[])paramMApiResponse.result();
      this.list.clear();
      this.list.addAll(Arrays.asList(paramMApiRequest));
      this.recordCount = paramMApiRequest.length;
      this.isEnd = true;
    }
  }

  public DPObject removeItem(int paramInt)
  {
    if ((paramInt > -1) && (paramInt < this.list.size()))
    {
      DPObject localDPObject = (DPObject)this.list.remove(paramInt);
      notifyDataSetChanged();
      return localDPObject;
    }
    return null;
  }

  public boolean removeItem(DPObject paramDPObject)
  {
    return removeItem(this.list.indexOf(paramDPObject)) != null;
  }

  public boolean replaceItem(int paramInt, DPObject paramDPObject)
  {
    if ((paramInt > -1) && (paramInt < this.list.size()))
    {
      this.list.remove(paramInt);
      this.list.add(paramInt, paramDPObject);
      notifyDataSetChanged();
      return true;
    }
    return false;
  }

  public void reset()
  {
    if (this.request != null)
      mapiService().abort(this.request, this, true);
    this.request = null;
    this.pending = false;
    this.error = null;
    this.list.clear();
    this.recordCount = 0;
    this.nextStartIndex = 0;
    this.isEnd = false;
    notifyDataSetChanged();
  }

  public void retry()
  {
    if (this.error != null)
    {
      this.error = null;
      loadNextPage();
      notifyDataSetChanged();
    }
  }

  public void setDone()
  {
    if (this.request != null)
      mapiService().abort(this.request, this, true);
    this.request = null;
    this.pending = false;
    this.error = null;
    this.isEnd = true;
    notifyDataSetChanged();
  }

  public void setError(Object paramObject)
  {
    this.pending = false;
    this.error = paramObject;
    notifyDataSetChanged();
  }

  public void setList(ArrayList<DPObject> paramArrayList)
  {
    this.list = paramArrayList;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.MyAdapter
 * JD-Core Version:    0.6.0
 */
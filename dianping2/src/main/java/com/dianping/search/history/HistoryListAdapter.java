package com.dianping.search.history;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.adapter.MulDeleListAdapter;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.NovaTabFragmentActivity;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.base.util.HistoryHelper;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HistoryListAdapter extends MulDeleListAdapter
{
  private static final boolean DEBUG = false;
  static final DecimalFormat FMT = new DecimalFormat("#.00000");
  private static final String TAG = "HistoryListAdapter";
  final NovaActivity context;
  private String errorMsg;
  private boolean isEnd;
  MApiService mapiService;
  private int nextStartIndex;
  private double offsetLatitude;
  private double offsetLongitude;
  private int recordCount = -1;
  NetworkTask task;

  public HistoryListAdapter(NovaActivity paramNovaActivity, double paramDouble1, double paramDouble2)
  {
    this.context = paramNovaActivity;
    this.mapiService = paramNovaActivity.mapiService();
    this.offsetLatitude = paramDouble1;
    this.offsetLongitude = paramDouble2;
  }

  public void appendShops(DPObject paramDPObject)
  {
    if (paramDPObject.getInt("StartIndex") == this.nextStartIndex)
    {
      this.dataList.addAll(Arrays.asList(paramDPObject.getArray("List")));
      this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
      this.isEnd = paramDPObject.getBoolean("IsEnd");
      this.recordCount = paramDPObject.getInt("RecordCount");
      notifyDataSetChanged();
    }
  }

  public boolean areAllItemsEnabled()
  {
    return true;
  }

  public int getCount()
  {
    if (this.isEnd)
      return this.dataList.size();
    return this.dataList.size() + 1;
  }

  public Object getItem(int paramInt)
  {
    if (paramInt < this.dataList.size())
      return this.dataList.get(paramInt);
    if (this.errorMsg == null)
      return LOADING;
    return ERROR;
  }

  public long getItemId(int paramInt)
  {
    Object localObject = getItem(paramInt);
    if (((localObject instanceof DPObject)) && (((DPObject)localObject).isClass("Shop")))
      return ((DPObject)localObject).getInt("ID");
    if (localObject == LOADING)
      return -paramInt;
    return -2147483648L;
  }

  public int getItemViewType(int paramInt)
  {
    Object localObject = getItem(paramInt);
    if ((localObject instanceof DPObject))
      return 0;
    if (localObject == LOADING)
      return 1;
    return 2;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    if ((localObject instanceof DPObject))
    {
      DPObject localDPObject = ((DPObject)localObject).edit().putString("ScoreText", "").putInt("ViewType", 0).generate();
      if ((paramView instanceof ShopListItem));
      for (paramView = (ShopListItem)paramView; ; paramView = null)
      {
        localObject = paramView;
        if (paramView == null)
          localObject = (ShopListItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shop_item, paramViewGroup, false);
        ((ShopListItem)localObject).setShop(localDPObject, -1, this.offsetLatitude, this.offsetLongitude, NovaTabFragmentActivity.isShowShopImg);
        paramInt = getChecked(paramInt);
        ((ShopListItem)localObject).setEditable(this.isEdit);
        ((ShopListItem)localObject).setChecked(paramInt);
        return localObject;
      }
    }
    if (localObject == LOADING)
    {
      if (this.errorMsg == null)
        loadNewPage();
      return getLoadingView(paramViewGroup, paramView);
    }
    return (View)getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        HistoryListAdapter.this.loadNewPage();
      }
    }
    , paramViewGroup, paramView);
  }

  public int getViewTypeCount()
  {
    return 3;
  }

  public boolean hasStableIds()
  {
    return true;
  }

  public boolean loadNewPage()
  {
    if (this.isEnd);
    do
      return false;
    while (this.task != null);
    this.errorMsg = null;
    this.task = new NetworkTask(null);
    this.task.execute(new Integer[] { Integer.valueOf(this.nextStartIndex) });
    notifyDataSetChanged();
    return true;
  }

  public void onFinish()
  {
    if (this.task != null)
      this.task.cancel(true);
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    this.dataList = paramBundle.getParcelableArrayList("shops");
    this.nextStartIndex = paramBundle.getInt("nextStartIndex");
    this.isEnd = paramBundle.getBoolean("isEnd");
    this.recordCount = paramBundle.getInt("recordCount");
    this.errorMsg = paramBundle.getString("error");
    notifyDataSetChanged();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelableArrayList("shops", this.dataList);
    paramBundle.putInt("nextStartIndex", this.nextStartIndex);
    paramBundle.putBoolean("isEnd", this.isEnd);
    paramBundle.putInt("recordCount", this.recordCount);
    paramBundle.putString("error", this.errorMsg);
  }

  public void reset()
  {
    onFinish();
    this.dataList.clear();
    this.nextStartIndex = 0;
    this.isEnd = false;
    this.recordCount = -1;
    this.errorMsg = null;
    notifyDataSetChanged();
  }

  public void setError(String paramString)
  {
    this.errorMsg = paramString;
    notifyDataSetChanged();
  }

  private class NetworkTask extends AsyncTask<Integer, Void, DPObject>
  {
    private MApiResponse response;

    private NetworkTask()
    {
    }

    public DPObject doInBackground(Integer[] arg1)
    {
      HashMap localHashMap = new HashMap(25);
      Object localObject2 = new ArrayList(25);
      int k = ???[0].intValue();
      Object localObject3;
      int i;
      boolean bool;
      DPObject[] arrayOfDPObject1;
      int j;
      label72: int m;
      Object localObject4;
      synchronized (HistoryHelper.getInstance())
      {
        localObject3 = ???.getIds();
        i = localObject3.length;
        if (i >= k + 25)
          break label571;
        bool = true;
        arrayOfDPObject1 = new DPObject[i - k];
        j = k;
        if (j < i)
        {
          m = localObject3[j].intValue();
          localObject4 = ???.getShopById(m);
          if (localObject4 != null)
            break label584;
          ((ArrayList)localObject2).add(Integer.valueOf(m));
          localHashMap.put(Integer.valueOf(m), Integer.valueOf(j - k));
        }
      }
      monitorexit;
      if (((ArrayList)localObject2).size() > 0)
      {
        try
        {
          localObject3 = new StringBuilder("http://m.api.dianping.com/shoplist.bin?");
          localObject4 = new StringBuilder();
          i = 0;
          while (i < ((ArrayList)localObject2).size())
          {
            if (i > 0)
              ((StringBuilder)localObject4).append(',');
            ((StringBuilder)localObject4).append(((ArrayList)localObject2).get(i));
            i += 1;
          }
          try
          {
            localObject2 = (Location)HistoryListAdapter.this.context.locationService().location().decodeToObject(Location.DECODER);
            if ((((Location)localObject2).latitude() != 0.0D) && (((Location)localObject2).longitude() != 0.0D))
            {
              ((StringBuilder)localObject3).append("&lat=").append(HistoryListAdapter.FMT.format(((Location)localObject2).latitude()));
              ((StringBuilder)localObject3).append("&lng=").append(HistoryListAdapter.FMT.format(((Location)localObject2).longitude()));
            }
            localObject2 = BasicMApiRequest.mapiPost(((StringBuilder)localObject3).toString(), new String[] { "ids", ((StringBuilder)localObject4).toString() });
            this.response = ((MApiResponse)HistoryListAdapter.this.mapiService.execSync((Request)localObject2));
            if (!(this.response.result() instanceof DPObject))
              break label496;
            localObject2 = (DPObject)this.response.result();
            if (localObject2 == null)
              break label596;
            if (((DPObject)localObject2).getArray("List") != null);
          }
          catch (NullPointerException localNullPointerException)
          {
            while (true)
              localNullPointerException.printStackTrace();
          }
        }
        catch (Exception )
        {
          ???.printStackTrace();
          return null;
        }
        DPObject[] arrayOfDPObject2 = localNullPointerException.getArray("List");
        j = arrayOfDPObject2.length;
        i = 0;
        while (i < j)
        {
          localObject3 = arrayOfDPObject2[i];
          m = ((DPObject)localObject3).getInt("ID");
          arrayOfDPObject1[((Integer)localObject1.get(Integer.valueOf(m))).intValue()] = localObject3;
          ???.addShop(m, (DPObject)localObject3);
          i += 1;
          continue;
          label496: return null;
        }
      }
      return new DPObject("ResultList").edit().putInt("RecordCount", -1).putInt("StartIndex", k).putInt("NextStartIndex", arrayOfDPObject1.length + k).putBoolean("IsEnd", bool).putArray("List", arrayOfDPObject1).generate();
      while (true)
      {
        j += 1;
        break label72;
        label571: i = k + 25;
        bool = false;
        break;
        label584: arrayOfDPObject1[(j - k)] = localObject4;
      }
      label596: return (DPObject)(DPObject)(DPObject)null;
    }

    public void onCancelled()
    {
      HistoryListAdapter.this.task = null;
    }

    public void onPostExecute(DPObject paramDPObject)
    {
      if (paramDPObject == null)
      {
        HistoryListAdapter localHistoryListAdapter = HistoryListAdapter.this;
        if (this.response == null)
        {
          paramDPObject = "error";
          localHistoryListAdapter.setError(paramDPObject);
        }
      }
      while (true)
      {
        HistoryListAdapter.this.task = null;
        return;
        paramDPObject = this.response.message().toString();
        break;
        HistoryListAdapter.this.appendShops(paramDPObject);
        paramDPObject = paramDPObject.getArray("List");
        HistoryListAdapter.this.appendCheckList(paramDPObject.length);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.history.HistoryListAdapter
 * JD-Core Version:    0.6.0
 */
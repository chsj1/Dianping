package com.dianping.main.user;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;

public abstract class UserListAdapter extends BasicAdapter
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public ArrayList<Integer> confirms = new ArrayList();
  String errorMsg;
  boolean isEnd;
  MApiService mapiService;
  int nextStartIndex;
  int recordCount;
  MApiRequest req;
  public ArrayList<UserProfile> users = new ArrayList();

  public UserListAdapter(DPActivity paramDPActivity)
  {
    this.mapiService = paramDPActivity.mapiService();
  }

  public void appendUsers(DPObject paramDPObject)
  {
    if (paramDPObject.getInt("StartIndex") == this.nextStartIndex)
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
      int j = arrayOfDPObject.length;
      int i = 0;
      Object localObject1;
      while (true)
        if (i < j)
        {
          Object localObject2 = arrayOfDPObject[i];
          localObject1 = null;
          try
          {
            localObject2 = (UserProfile)((DPObject)localObject2).decodeToObject(UserProfile.DECODER);
            localObject1 = localObject2;
            if (localObject1 != null)
              this.users.add(localObject1);
            i += 1;
          }
          catch (ArchiveException localArchiveException)
          {
            while (true)
              localArchiveException.printStackTrace();
          }
        }
      j = paramDPObject.getInt("StartIndex");
      if (j < this.users.size())
      {
        localObject1 = (UserProfile)this.users.get(j);
        ArrayList localArrayList = this.confirms;
        if (((UserProfile)localObject1).isHoney())
          i = 2;
        while (true)
        {
          localArrayList.add(j, Integer.valueOf(i));
          j += 1;
          break;
          if (((UserProfile)localObject1).isInvited())
          {
            i = 1;
            continue;
          }
          i = 0;
        }
      }
      this.isEnd = paramDPObject.getBoolean("IsEnd");
      this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
      this.recordCount = paramDPObject.getInt("RecordCount");
    }
    notifyDataSetChanged();
  }

  public boolean areAllItemsEnabled()
  {
    return true;
  }

  public abstract MApiRequest createRequest(int paramInt);

  public int getCount()
  {
    if (this.isEnd)
      return this.users.size();
    return this.users.size() + 1;
  }

  public Object getItem(int paramInt)
  {
    if (paramInt < this.users.size())
      return this.users.get(paramInt);
    if (this.errorMsg == null)
      return LOADING;
    return ERROR;
  }

  public long getItemId(int paramInt)
  {
    Object localObject = getItem(paramInt);
    if (paramInt < this.users.size())
      return ((UserProfile)localObject).id();
    if (localObject == LOADING)
      return -paramInt;
    return -2147483648L;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    if ((localObject instanceof UserProfile))
      throw new RuntimeException("to override");
    if (localObject == LOADING)
    {
      if (this.errorMsg == null)
        loadNewPage();
      return getLoadingView(paramViewGroup, paramView);
    }
    return getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        UserListAdapter.this.loadNewPage();
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
    while (this.req != null);
    this.errorMsg = null;
    this.req = createRequest(this.nextStartIndex);
    this.mapiService.exec(this.req, this);
    notifyDataSetChanged();
    return true;
  }

  public void onFinish()
  {
    if (this.req != null)
    {
      this.mapiService.abort(this.req, this, true);
      this.req = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    setError(paramMApiResponse.message().content());
    this.req = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.req)
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
        appendUsers(paramMApiRequest);
      this.req = null;
    }
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    this.users = paramBundle.getParcelableArrayList("users");
    this.nextStartIndex = paramBundle.getInt("nextStartIndex");
    this.isEnd = paramBundle.getBoolean("isEnd");
    this.recordCount = paramBundle.getInt("recordCount");
    this.errorMsg = paramBundle.getString("error");
    this.confirms = paramBundle.getIntegerArrayList("confirms");
    notifyDataSetChanged();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelableArrayList("users", this.users);
    paramBundle.putInt("nextStartIndex", this.nextStartIndex);
    paramBundle.putBoolean("isEnd", this.isEnd);
    paramBundle.putInt("recordCount", this.recordCount);
    paramBundle.putString("error", this.errorMsg);
    paramBundle.putIntegerArrayList("confirms", this.confirms);
  }

  public int recordCount()
  {
    return this.recordCount;
  }

  public void reset()
  {
    onFinish();
    this.users = new ArrayList();
    this.nextStartIndex = 0;
    this.isEnd = false;
    this.recordCount = 0;
    this.errorMsg = null;
    notifyDataSetChanged();
  }

  public void setError(String paramString)
  {
    this.errorMsg = paramString;
    notifyDataSetChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.UserListAdapter
 * JD-Core Version:    0.6.0
 */
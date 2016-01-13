package com.dianping.base.app.loader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.base.widget.NovaFragment;
import com.dianping.base.widget.TableView;
import com.dianping.configservice.ConfigService;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.UserProfile;
import com.dianping.util.DPUrl;
import com.dianping.util.Log;
import com.dianping.v1.R;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.view.GAUserInfo;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CellAgent
  implements NovaFragment.onProgressDialogCancelListener
{
  protected AgentFragment fragment;
  public String hostName = "";
  public String index = "";
  private Map<MApiRequest, RequestHandler<MApiRequest, MApiResponse>> mapiRequestMap = new HashMap();
  public MyResources res;

  public CellAgent(Object paramObject)
  {
    if (!(paramObject instanceof AgentFragment))
      throw new RuntimeException();
    this.fragment = ((AgentFragment)paramObject);
    this.res = MyResources.getResource(getClass());
  }

  private MApiRequest findRequest(Set<MApiRequest> paramSet, MApiRequest paramMApiRequest)
  {
    paramMApiRequest = paramMApiRequest.url();
    if (paramMApiRequest.lastIndexOf("?") < 0);
    while (paramMApiRequest.length() == 0)
    {
      return null;
      paramMApiRequest = paramMApiRequest.substring(0, paramMApiRequest.lastIndexOf("?"));
    }
    paramSet = paramSet.iterator();
    while (paramSet.hasNext())
    {
      MApiRequest localMApiRequest = (MApiRequest)paramSet.next();
      if (localMApiRequest.url().startsWith(paramMApiRequest))
        return localMApiRequest;
    }
    return null;
  }

  public AccountService accountService()
  {
    return this.fragment.accountService();
  }

  public void addCell(String paramString, View paramView)
  {
    this.fragment.addCell(this, paramString, paramView);
  }

  public void addDividerCell(String paramString)
  {
    addDividerCell(paramString, R.drawable.home_cell_bottom);
  }

  public void addDividerCell(String paramString, int paramInt)
  {
    View localView = new View(getContext());
    if (paramInt > 0)
      localView.setBackgroundDrawable(this.res.getDrawable(paramInt));
    localView.setLayoutParams(new ViewGroup.LayoutParams(-1, (int)TypedValue.applyDimension(1, 10.0F, getFragment().getResources().getDisplayMetrics())));
    addCell(paramString, localView);
  }

  public void addDividerLine(String paramString)
  {
    addDividerLine(paramString, R.drawable.gray_horizontal_line);
  }

  public void addDividerLine(String paramString, int paramInt)
  {
    View localView = new View(getContext());
    if (paramInt > 0)
      localView.setBackgroundDrawable(this.res.getDrawable(paramInt));
    localView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    addCell(paramString, localView);
  }

  public void addEmptyCell(String paramString)
  {
    addDividerCell(paramString, 0);
  }

  public int cityId()
  {
    return this.fragment.cityId();
  }

  public ConfigService configService()
  {
    return this.fragment.configService();
  }

  protected TableView createCellContainer()
  {
    return (TableView)MyResources.getResource(CellAgent.class).inflate(getContext(), R.layout.agent_cell_parent, getParentView(), false);
  }

  public View createErrorCell(LoadingErrorView.LoadRetry paramLoadRetry)
  {
    View localView = this.res.inflate(getContext(), R.layout.error_item, getParentView(), false);
    if ((localView instanceof LoadingErrorView))
      ((LoadingErrorView)localView).setCallBack(paramLoadRetry);
    return localView;
  }

  public View createLoadingCell()
  {
    return this.res.inflate(getContext(), R.layout.loading_item, getParentView(), false);
  }

  public void dismissDialog()
  {
    this.fragment.dismissDialog();
  }

  public void dispatchAgentChanged(String paramString, Bundle paramBundle)
  {
    if (paramBundle != null)
      paramBundle.putString("_host", this.fragment.findHostForCell(this));
    this.fragment.dispatchAgentChanged(paramString, paramBundle);
  }

  public void dispatchAgentChanged(boolean paramBoolean)
  {
    AgentFragment localAgentFragment = this.fragment;
    CellAgent localCellAgent = this;
    if (paramBoolean)
      localCellAgent = null;
    localAgentFragment.dispatchCellChanged(localCellAgent);
  }

  public void dispatchMessage(AgentMessage paramAgentMessage)
  {
    paramAgentMessage.host = this;
    this.fragment.dispatchMessage(paramAgentMessage);
  }

  public UserProfile getAccount()
  {
    return this.fragment.getAccount();
  }

  public City getCity()
  {
    return this.fragment.city();
  }

  public Context getContext()
  {
    return this.fragment.getActivity();
  }

  public AgentFragment getFragment()
  {
    return this.fragment;
  }

  public GAUserInfo getGAExtra()
  {
    if (getContext() != null)
      return ((DPActivity)getContext()).getCloneUserInfo();
    return null;
  }

  public ViewGroup getParentView()
  {
    return (ViewGroup)this.fragment.contentView();
  }

  public MyResources getResources()
  {
    return this.res;
  }

  public Object getSharedObject(String paramString)
  {
    return this.fragment.sharedObject(paramString);
  }

  public View getView()
  {
    return null;
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
  }

  public boolean isLogined()
  {
    if (getAccount() == null);
    do
      return false;
    while (TextUtils.isEmpty(accountService().token()));
    return true;
  }

  public Location location()
  {
    return this.fragment.location();
  }

  public MApiRequest mapiGet(RequestHandler<MApiRequest, MApiResponse> paramRequestHandler, String paramString, CacheType paramCacheType)
  {
    paramString = BasicMApiRequest.mapiGet(paramString, paramCacheType);
    paramCacheType = findRequest(this.mapiRequestMap.keySet(), paramString);
    if (paramCacheType != null)
    {
      mapiService().abort(paramCacheType, (RequestHandler)this.mapiRequestMap.get(paramCacheType), true);
      this.mapiRequestMap.remove(paramCacheType);
      Log.i(getClass().getSimpleName(), "abort an existed request with the same url: " + paramString.url());
    }
    this.mapiRequestMap.put(paramString, paramRequestHandler);
    return paramString;
  }

  public MApiRequest mapiPost(RequestHandler<MApiRequest, MApiResponse> paramRequestHandler, String paramString, String[] paramArrayOfString)
  {
    paramString = BasicMApiRequest.mapiPost(paramString, paramArrayOfString);
    paramArrayOfString = findRequest(this.mapiRequestMap.keySet(), paramString);
    if (paramArrayOfString != null)
    {
      mapiService().abort(paramArrayOfString, (RequestHandler)this.mapiRequestMap.get(paramArrayOfString), true);
      this.mapiRequestMap.remove(paramArrayOfString);
      Log.i(getClass().getSimpleName(), "abort an existed request with the same url: " + paramString.url());
    }
    this.mapiRequestMap.put(paramString, paramRequestHandler);
    return paramString;
  }

  public MApiService mapiService()
  {
    return this.fragment.mapiService();
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
  }

  public void onAgentChanged(Bundle paramBundle)
  {
  }

  public void onCreate(Bundle paramBundle)
  {
  }

  public void onDestroy()
  {
    Iterator localIterator = this.mapiRequestMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      MApiRequest localMApiRequest = (MApiRequest)localIterator.next();
      mapiService().abort(localMApiRequest, (RequestHandler)this.mapiRequestMap.get(localMApiRequest), true);
      Log.i(getClass().getSimpleName(), "abort a request from the map with url: " + localMApiRequest.url());
    }
  }

  public void onPause()
  {
  }

  public void onProgressDialogCancel()
  {
  }

  public void onResume()
  {
  }

  public void onStop()
  {
  }

  public void removeAllCells()
  {
    this.fragment.removeAllCells(this);
  }

  public void removeCell(String paramString)
  {
    this.fragment.removeCell(this, paramString);
  }

  public Bundle saveInstanceState()
  {
    return new Bundle();
  }

  public void setSharedObject(String paramString, Object paramObject)
  {
    this.fragment.setSharedObject(paramString, paramObject);
  }

  public void showProgressDialog(String paramString)
  {
    this.fragment.showProgressDialog(paramString, this);
  }

  public void showSimpleAlertDialog(String paramString1, String paramString2, String paramString3, DialogInterface.OnClickListener paramOnClickListener1, String paramString4, DialogInterface.OnClickListener paramOnClickListener2)
  {
    this.fragment.showSimpleAlertDialog(paramString1, paramString2, paramString3, paramOnClickListener1, paramString4, paramOnClickListener2);
  }

  public void showToast(String paramString)
  {
    this.fragment.showToast(paramString);
  }

  public void startActivity(Intent paramIntent)
  {
    getFragment().startActivity(paramIntent);
  }

  public void startActivity(DPUrl paramDPUrl)
  {
    getFragment().startActivity(paramDPUrl);
  }

  public void startActivity(String paramString)
  {
    getFragment().startActivity(paramString);
  }

  public void startActivityForResult(Intent paramIntent, int paramInt)
  {
    getFragment().startActivityForResult(paramIntent, paramInt);
  }

  public void startActivityForResult(DPUrl paramDPUrl, int paramInt)
  {
    getFragment().startActivityForResult(paramDPUrl.getIntent(), paramInt);
  }

  public void startActivityForResult(String paramString, int paramInt)
  {
    getFragment().startActivityForResult(paramString, paramInt);
  }

  public void statisticsEvent(String paramString1, String paramString2, String paramString3, int paramInt)
  {
    statisticsEvent(paramString1, paramString2, paramString3, paramInt, null);
  }

  public void statisticsEvent(String paramString1, String paramString2, String paramString3, int paramInt, List<NameValuePair> paramList)
  {
    this.fragment.statisticsEvent(paramString1, paramString2, paramString3, paramInt, paramList);
  }

  public String token()
  {
    return accountService().token();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.CellAgent
 * JD-Core Version:    0.6.0
 */
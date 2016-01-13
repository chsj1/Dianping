package com.dianping.base.app.loader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.NovaFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.loader.MyClassLoader;
import com.dianping.loader.MyResources.ResourceOverrideable;
import com.dianping.loader.RepositoryManager;
import com.dianping.loader.SiteManager;
import com.dianping.loader.model.FileSpec;
import com.dianping.loader.model.FragmentSpec;
import com.dianping.loader.model.SiteSpec;
import com.dianping.util.Log;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Map<Ljava.lang.String;Lcom.dianping.base.app.loader.AgentInfo;>;
import java.util.Set;

public abstract class AgentFragment extends NovaFragment
{
  static final String TAG;
  protected static final Comparator<Cell> cellComparator;
  public static final Handler handler = new Handler(Looper.getMainLooper());
  private ViewGroup agentContainerView;
  protected final ArrayList<String> agentList = new ArrayList();
  protected final HashMap<String, CellAgent> agents = new HashMap();
  protected final HashMap<String, Cell> cells = new HashMap();
  private View contentView;
  private String host;
  private Handler messageHandler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramMessage)
    {
      paramMessage = (AgentMessage)paramMessage.obj;
      CellAgent localCellAgent = paramMessage.target;
      if (AgentFragment.this.getActivity() == null);
      do
      {
        return;
        Iterator localIterator = AgentFragment.this.agentList.iterator();
        while (localIterator.hasNext())
        {
          Object localObject = (String)localIterator.next();
          localObject = (CellAgent)AgentFragment.this.agents.get(localObject);
          if (((localCellAgent != null) && (localCellAgent != localObject)) || (paramMessage.host == localObject) || (localObject == null))
            continue;
          ((CellAgent)localObject).handleMessage(paramMessage);
        }
      }
      while (!paramMessage.callback);
      paramMessage.dispatched = true;
      paramMessage.host.handleMessage(paramMessage);
    }
  };
  final HashMap<FragmentSpec, FileSpec> missingAgents = new HashMap();
  private final Runnable notifyCellChanged = new Runnable()
  {
    public void run()
    {
      AgentFragment.handler.removeCallbacks(this);
      AgentFragment.this.updateAgentContainer();
    }
  };
  public boolean pluginHasLoaded;
  final Runnable repoListener = new Runnable()
  {
    public void run()
    {
      if (AgentFragment.this.missingAgents.size() == 0);
      do
      {
        do
          return;
        while (AgentFragment.this.site == null);
        Object localObject1 = new HashMap();
        Object localObject2 = new HashSet();
        Object localObject3 = AgentFragment.this.missingAgents.entrySet().iterator();
        while (true)
          if (((Iterator)localObject3).hasNext())
          {
            Object localObject4 = (Map.Entry)((Iterator)localObject3).next();
            FileSpec localFileSpec = (FileSpec)((Map.Entry)localObject4).getValue();
            if (((HashSet)localObject2).contains(localFileSpec))
              continue;
            MyClassLoader localMyClassLoader = MyClassLoader.getClassLoader(AgentFragment.this.site, localFileSpec);
            if (localMyClassLoader == null)
            {
              ((HashSet)localObject2).add(localFileSpec);
              continue;
            }
            localObject4 = (FragmentSpec)((Map.Entry)localObject4).getKey();
            try
            {
              ((HashMap)localObject1).put(localObject4, (CellAgent)localMyClassLoader.loadClass(((FragmentSpec)localObject4).name()).getConstructor(new Class[] { Object.class }).newInstance(new Object[] { AgentFragment.this }));
              Log.i(AgentFragment.TAG, "loaded missing activity " + ((FragmentSpec)localObject4).host() + " (" + ((FragmentSpec)localObject4).name() + ")");
            }
            catch (Exception localException)
            {
              while (true)
                Log.e(AgentFragment.TAG, "fail to create instance of " + ((FragmentSpec)localObject4).name(), localException);
            }
          }
        if (((HashMap)localObject1).size() <= 0)
          continue;
        localObject1 = ((HashMap)localObject1).entrySet().iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject3 = (Map.Entry)((Iterator)localObject1).next();
          localObject2 = (FragmentSpec)((Map.Entry)localObject3).getKey();
          AgentFragment.this.missingAgents.remove(localObject2);
          if (AgentFragment.this.agents.containsKey(((FragmentSpec)localObject2).host()))
          {
            CellAgent localCellAgent = (CellAgent)AgentFragment.this.agents.remove(((FragmentSpec)localObject2).host());
            AgentFragment.this.agentList.remove(((FragmentSpec)localObject2).host());
            AgentFragment.this.removeAllCells(localCellAgent);
            if (AgentFragment.this.isResumed())
              localCellAgent.onPause();
            localCellAgent.onDestroy();
          }
          localObject3 = (CellAgent)((Map.Entry)localObject3).getValue();
          ((CellAgent)localObject3).onCreate(null);
          if (AgentFragment.this.isResumed())
            ((CellAgent)localObject3).onResume();
          AgentFragment.this.agentList.add(((FragmentSpec)localObject2).host());
          AgentFragment.this.agents.put(((FragmentSpec)localObject2).host(), localObject3);
          ((CellAgent)localObject3).hostName = ((FragmentSpec)localObject2).host();
        }
        AgentFragment.this.dispatchCellChanged(null);
      }
      while (AgentFragment.this.missingAgents.size() <= 0);
      AgentFragment.handler.postDelayed(this, 500L);
    }
  };
  private HashMap<String, Object> sharedObject = new HashMap();
  SiteSpec site;

  static
  {
    cellComparator = new Comparator()
    {
      public int compare(Cell paramCell1, Cell paramCell2)
      {
        if (paramCell1.owner.index.equals(paramCell2.owner.index))
          return paramCell1.name.compareTo(paramCell2.name);
        return paramCell1.owner.index.compareTo(paramCell2.owner.index);
      }
    };
    TAG = AgentFragment.class.getSimpleName();
  }

  private void destroyAgents()
  {
    Iterator localIterator = this.agentList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      ((CellAgent)this.agents.get(str)).onDestroy();
    }
  }

  private Map<String, AgentInfo> getDefaultAgentList()
  {
    Object localObject1 = generaterDefaultConfigAgentList();
    if (localObject1 == null)
      throw new RuntimeException("generaterDefaultConfigAgentList return null");
    Iterator localIterator = ((ArrayList)localObject1).iterator();
    while (true)
    {
      if (localIterator.hasNext())
      {
        Object localObject3 = (AgentListConfig)localIterator.next();
        try
        {
          if (!((AgentListConfig)localObject3).shouldShow())
            continue;
          Object localObject2 = ((AgentListConfig)localObject3).getAgentInfoList();
          localObject1 = localObject2;
          if (localObject2 != null)
            break;
          localObject2 = new LinkedHashMap();
          localObject3 = ((AgentListConfig)localObject3).getAgentList().entrySet().iterator();
          while (true)
          {
            localObject1 = localObject2;
            if (!((Iterator)localObject3).hasNext())
              break;
            localObject1 = (Map.Entry)((Iterator)localObject3).next();
            ((Map)localObject2).put(((Map.Entry)localObject1).getKey(), new AgentInfo((Class)((Map.Entry)localObject1).getValue(), ""));
          }
        }
        catch (Exception localException)
        {
          if (Environment.isDebug())
            throw new RuntimeException("there has a exception " + localException);
          localException.printStackTrace();
        }
        continue;
      }
      throw new RuntimeException("getDefaultAgentList() agentListConfig no one should be shown?");
    }
    return (Map<String, AgentInfo>)(Map<String, AgentInfo>)(Map<String, AgentInfo>)localException;
  }

  private void pauseAgents()
  {
    Iterator localIterator = this.agentList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      ((CellAgent)this.agents.get(str)).onPause();
    }
  }

  private void resumeAgents()
  {
    Iterator localIterator = this.agentList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      ((CellAgent)this.agents.get(str)).onResume();
    }
  }

  private void setupAgents()
  {
    this.agentList.clear();
    this.agents.clear();
    RepositoryManager localRepositoryManager = DPApplication.instance().repositoryManager();
    if (this.site == null)
      this.site = ((SiteSpec)getActivity().getIntent().getParcelableExtra("_site"));
    if (this.site == null)
      this.site = DPApplication.instance().siteManager().site();
    if (this.site == null);
    HashMap localHashMap;
    Object localObject1;
    Object localObject2;
    for (FragmentSpec[] arrayOfFragmentSpec = new FragmentSpec[0]; ; arrayOfFragmentSpec = this.site.fragments(Environment.versionCode()))
    {
      localHashMap = new HashMap();
      if (this.site == null)
        break;
      localObject1 = this.site.files();
      j = localObject1.length;
      i = 0;
      while (i < j)
      {
        localObject2 = localObject1[i];
        if (localObject2 != null)
          localHashMap.put(((FileSpec)localObject2).id(), localObject2);
        i += 1;
      }
    }
    int j = arrayOfFragmentSpec.length;
    int i = 0;
    if (i < j)
    {
      FragmentSpec localFragmentSpec = arrayOfFragmentSpec[i];
      if ((localFragmentSpec == null) || (com.dianping.util.TextUtils.isEmpty(localFragmentSpec.host())));
      label590: 
      while (true)
      {
        i += 1;
        break;
        if (!localFragmentSpec.host().startsWith(this.host))
          continue;
        localObject1 = null;
        if (localFragmentSpec.code() != null)
        {
          localObject2 = (FileSpec)localHashMap.get(localFragmentSpec.code());
          localObject1 = localObject2;
          if (localObject2 == null)
          {
            Log.e(TAG, "fail to find file " + localFragmentSpec.code());
            continue;
          }
        }
        if (localObject1 == null)
          localObject2 = getActivity().getClassLoader();
        while (true)
        {
          while (true)
          {
            if (localObject2 == null)
              break label590;
            try
            {
              localObject1 = (CellAgent)((ClassLoader)localObject2).loadClass(localFragmentSpec.name()).getConstructor(new Class[] { Object.class }).newInstance(new Object[] { this });
              this.agentList.add(localFragmentSpec.host());
              this.agents.put(localFragmentSpec.host(), localObject1);
              ((CellAgent)localObject1).hostName = localFragmentSpec.host();
              this.pluginHasLoaded = true;
            }
            catch (Exception localException)
            {
              Log.e(TAG, "fail to create instance of " + localFragmentSpec.name(), localException);
              this.pluginHasLoaded = false;
            }
          }
          break;
          MyClassLoader localMyClassLoader = MyClassLoader.getClassLoader(this.site, localException);
          localObject2 = localMyClassLoader;
          if (localMyClassLoader != null)
            continue;
          localObject2 = new ArrayList();
          if (!RepositoryManager.appendDepsList(localHashMap, (List)localObject2, localException.id()))
          {
            Log.e(TAG, "dependency fail in file " + localException.id());
            break;
          }
          Iterator localIterator = ((ArrayList)localObject2).iterator();
          while (localIterator.hasNext())
          {
            if (MyClassLoader.getClassLoader(this.site, (FileSpec)localIterator.next()) == null)
              continue;
            localIterator.remove();
          }
          localRepositoryManager.require((FileSpec[])((ArrayList)localObject2).toArray(new FileSpec[0]));
          this.missingAgents.put(localFragmentSpec, localException);
          handler.removeCallbacks(this.repoListener);
          handler.post(this.repoListener);
          localObject2 = localMyClassLoader;
        }
      }
    }
    setDefaultAgent();
  }

  private void stopAgents()
  {
    Iterator localIterator = this.agentList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      ((CellAgent)this.agents.get(str)).onStop();
    }
  }

  public void addCell(CellAgent paramCellAgent, String paramString, View paramView)
  {
    String str = getCellName(paramCellAgent, paramString);
    Cell localCell = new Cell();
    localCell.owner = paramCellAgent;
    localCell.name = paramString;
    localCell.view = paramView;
    this.cells.put(str, localCell);
    notifyCellChanged();
  }

  protected void addCellToContainerView(String paramString, Cell paramCell)
  {
    this.agentContainerView.addView(paramCell.view);
  }

  public ViewGroup agentContainerView()
  {
    return this.agentContainerView;
  }

  public View contentView()
  {
    return this.contentView;
  }

  public void dispatchAgentChanged(String paramString, Bundle paramBundle)
  {
    CellAgent localCellAgent = (CellAgent)this.agents.get(paramString);
    if ((!android.text.TextUtils.isEmpty(paramString)) && (localCellAgent == null))
      return;
    dispatchCellChanged(localCellAgent, paramBundle);
  }

  public void dispatchCellChanged(CellAgent paramCellAgent)
  {
    dispatchCellChanged(paramCellAgent, null);
  }

  public void dispatchCellChanged(CellAgent paramCellAgent, Bundle paramBundle)
  {
    label7: Iterator localIterator;
    if (getActivity() == null)
      return;
    else
      localIterator = this.agentList.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        break label7;
      Object localObject = (String)localIterator.next();
      localObject = (CellAgent)this.agents.get(localObject);
      if (((paramCellAgent != null) && (paramCellAgent != localObject)) || (localObject == null))
        break;
      ((NovaActivity)getActivity()).setClassLoader(localObject.getClass().getClassLoader());
      try
      {
        ((CellAgent)localObject).onAgentChanged(paramBundle);
        ((NovaActivity)getActivity()).setClassLoader(null);
      }
      finally
      {
        ((NovaActivity)getActivity()).setClassLoader(null);
      }
    }
  }

  public void dispatchMessage(AgentMessage paramAgentMessage)
  {
    Message localMessage = new Message();
    localMessage.obj = paramAgentMessage;
    this.messageHandler.sendMessage(localMessage);
  }

  public CellAgent findAgent(String paramString)
  {
    return (CellAgent)this.agents.get(paramString);
  }

  public String findHostForCell(CellAgent paramCellAgent)
  {
    Iterator localIterator = this.agents.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (paramCellAgent == localEntry.getValue())
        return (String)localEntry.getKey();
    }
    return null;
  }

  protected abstract ArrayList<AgentListConfig> generaterDefaultConfigAgentList();

  protected String getCellName(CellAgent paramCellAgent, String paramString)
  {
    if (android.text.TextUtils.isEmpty(paramCellAgent.index))
      return paramString;
    return paramCellAgent.index + paramString;
  }

  public TitleBar getTitleBar()
  {
    return ((NovaActivity)getActivity()).getTitleBar();
  }

  public boolean hasCell(CellAgent paramCellAgent, String paramString)
  {
    paramString = (Cell)this.cells.get(getCellName(paramCellAgent, paramString));
    if (paramString == null);
    do
      return false;
    while ((paramCellAgent != null) && (paramString.owner != paramCellAgent));
    return true;
  }

  protected void notifyCellChanged()
  {
    handler.removeCallbacks(this.notifyCellChanged);
    handler.post(this.notifyCellChanged);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    setupAgents();
    Iterator localIterator = this.agentList.iterator();
    if (localIterator.hasNext())
    {
      Object localObject = (String)localIterator.next();
      CellAgent localCellAgent = (CellAgent)this.agents.get(localObject);
      if (paramBundle == null);
      for (localObject = null; ; localObject = paramBundle.getBundle("agent/" + (String)localObject))
      {
        localCellAgent.onCreate((Bundle)localObject);
        break;
      }
    }
    dispatchCellChanged(null);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    Iterator localIterator = this.agentList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      ((CellAgent)this.agents.get(str)).onActivityResult(paramInt1, paramInt2, paramIntent);
    }
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    if (!(paramActivity instanceof MyResources.ResourceOverrideable))
      throw new RuntimeException("activity must implements MyResources.ResourceOverrideable");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
      this.host = paramBundle.getString("host");
    if (android.text.TextUtils.isEmpty(this.host))
    {
      paramBundle = getActivity().getIntent().getData();
      if (paramBundle == null)
      {
        getActivity().finish();
        throw new RuntimeException("only support starting activity from url schema");
      }
      this.host = paramBundle.getHost();
      if (android.text.TextUtils.isEmpty(this.host))
      {
        getActivity().finish();
        throw new RuntimeException("host can't be null");
      }
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    destroyAgents();
    handler.removeCallbacks(this.repoListener);
  }

  public void onLogin(boolean paramBoolean)
  {
  }

  public void onPause()
  {
    super.onPause();
    pauseAgents();
  }

  public void onResume()
  {
    super.onResume();
    resumeAgents();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("host", this.host);
    Iterator localIterator = this.agentList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Bundle localBundle = ((CellAgent)this.agents.get(str)).saveInstanceState();
      if (localBundle == null)
        continue;
      paramBundle.putBundle("agent/" + str, localBundle);
    }
  }

  public void onStop()
  {
    super.onStop();
    stopAgents();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.contentView = paramView;
    if (this.agentContainerView == null)
    {
      if (!(paramView instanceof ViewGroup))
        throw new RuntimeException("agentContainerView must be ViewGroup");
      this.agentContainerView = ((ViewGroup)paramView);
    }
  }

  public void removeAllCells(CellAgent paramCellAgent)
  {
    Iterator localIterator = this.cells.entrySet().iterator();
    while (localIterator.hasNext())
    {
      if (((Cell)((Map.Entry)localIterator.next()).getValue()).owner != paramCellAgent)
        continue;
      localIterator.remove();
    }
    notifyCellChanged();
  }

  public void removeCell(CellAgent paramCellAgent, String paramString)
  {
    if (hasCell(paramCellAgent, paramString))
    {
      this.cells.remove(getCellName(paramCellAgent, paramString));
      notifyCellChanged();
    }
  }

  protected void resetAgentContainerView()
  {
    this.agentContainerView.removeAllViews();
  }

  public void resetAgents(Bundle paramBundle)
  {
    ArrayList localArrayList = (ArrayList)this.agentList.clone();
    HashMap localHashMap1 = (HashMap)this.agents.clone();
    HashMap localHashMap2 = (HashMap)this.cells.clone();
    this.cells.clear();
    setupAgents();
    Iterator localIterator1 = this.agentList.iterator();
    Object localObject;
    while (localIterator1.hasNext())
    {
      localObject = (String)localIterator1.next();
      if (!localArrayList.contains(localObject))
      {
        localCellAgent = (CellAgent)this.agents.get(localObject);
        if (paramBundle == null);
        for (localObject = null; ; localObject = paramBundle.getBundle("agent/" + (String)localObject))
        {
          localCellAgent.onCreate((Bundle)localObject);
          localCellAgent.onResume();
          break;
        }
      }
      localArrayList.remove(localObject);
      CellAgent localCellAgent = (CellAgent)localHashMap1.get(localObject);
      localCellAgent.index = ((CellAgent)this.agents.get(localObject)).index;
      Iterator localIterator2 = localHashMap2.entrySet().iterator();
      while (localIterator2.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator2.next();
        if (((Cell)localEntry.getValue()).owner != localCellAgent)
          continue;
        this.cells.put(getCellName(localCellAgent, ((Cell)localEntry.getValue()).name), updateCell((Cell)localEntry.getValue(), localCellAgent, ((Cell)localEntry.getValue()).name));
      }
      this.agents.put(localObject, localCellAgent);
      localCellAgent.hostName = ((String)localObject);
    }
    paramBundle = localArrayList.iterator();
    while (paramBundle.hasNext())
    {
      localObject = (CellAgent)localHashMap1.get((String)paramBundle.next());
      if (isResumed())
        ((CellAgent)localObject).onPause();
      ((CellAgent)localObject).onStop();
      ((CellAgent)localObject).onDestroy();
    }
    localArrayList.clear();
    localHashMap1.clear();
    localHashMap2.clear();
    notifyCellChanged();
    dispatchCellChanged(null);
  }

  public void setAgentContainerView(ViewGroup paramViewGroup)
  {
    this.agentContainerView = paramViewGroup;
  }

  protected void setDefaultAgent()
  {
    Object localObject = getDefaultAgentList();
    if (localObject == null)
      throw new RuntimeException("generaterDefaultAgent() can not return null");
    try
    {
      localObject = ((Map)localObject).entrySet().iterator();
      while (((Iterator)localObject).hasNext())
      {
        Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
        if (this.agents.containsKey(localEntry.getKey()))
          continue;
        this.agentList.add(localEntry.getKey());
        CellAgent localCellAgent = (CellAgent)((AgentInfo)localEntry.getValue()).agentClass.getConstructor(new Class[] { Object.class }).newInstance(new Object[] { this });
        localCellAgent.index = ((AgentInfo)localEntry.getValue()).index;
        this.agents.put(localEntry.getKey(), localCellAgent);
        localCellAgent.hostName = ((String)localEntry.getKey());
      }
    }
    catch (Exception localException)
    {
      Log.e(TAG, localException.toString());
    }
  }

  public void setHost(String paramString)
  {
    this.host = paramString;
  }

  public void setSharedObject(String paramString, Object paramObject)
  {
    this.sharedObject.put(paramString, paramObject);
  }

  public Object sharedObject(String paramString)
  {
    return this.sharedObject.get(paramString);
  }

  protected void updateAgentContainer()
  {
    Object localObject = new ArrayList(this.cells.values());
    Collections.sort((List)localObject, cellComparator);
    resetAgentContainerView();
    localObject = ((ArrayList)localObject).iterator();
    while (true)
    {
      Cell localCell;
      String str;
      if (((Iterator)localObject).hasNext())
      {
        localCell = (Cell)((Iterator)localObject).next();
        str = findHostForCell(localCell.owner);
        if (!android.text.TextUtils.isEmpty(str));
      }
      else
      {
        return;
      }
      addCellToContainerView(str, localCell);
    }
  }

  protected Cell updateCell(Cell paramCell, CellAgent paramCellAgent, String paramString)
  {
    return paramCell;
  }

  public static abstract interface CellStable
  {
    public abstract void setBottomCell(View paramView, CellAgent paramCellAgent);

    public abstract void setTopCell(View paramView, CellAgent paramCellAgent);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.AgentFragment
 * JD-Core Version:    0.6.0
 */
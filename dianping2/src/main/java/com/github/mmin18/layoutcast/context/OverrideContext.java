package com.github.mmin18.layoutcast.context;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

public class OverrideContext extends ContextWrapper
{
  public static final int ACTIVITY_CREATED = 1;
  public static final int ACTIVITY_NONE = 0;
  public static final int ACTIVITY_RESUMED = 3;
  public static final int ACTIVITY_STARTED = 2;
  private static final int STATE_REQUIRE_RECREATE = 5;
  private static final WeakHashMap<Activity, Integer> activities = new WeakHashMap();
  private static final Application.ActivityLifecycleCallbacks lifecycleCallback = new Application.ActivityLifecycleCallbacks()
  {
    public void onActivityCreated(Activity paramActivity, Bundle paramBundle)
    {
      OverrideContext.activities.put(paramActivity, Integer.valueOf(1));
    }

    public void onActivityDestroyed(Activity paramActivity)
    {
      OverrideContext.activities.remove(paramActivity);
    }

    public void onActivityPaused(Activity paramActivity)
    {
      OverrideContext.activities.put(paramActivity, Integer.valueOf(2));
    }

    public void onActivityResumed(Activity paramActivity)
    {
      OverrideContext.activities.put(paramActivity, Integer.valueOf(3));
      OverrideContext.access$100(paramActivity);
    }

    public void onActivitySaveInstanceState(Activity paramActivity, Bundle paramBundle)
    {
    }

    public void onActivityStarted(Activity paramActivity)
    {
      OverrideContext.activities.put(paramActivity, Integer.valueOf(2));
    }

    public void onActivityStopped(Activity paramActivity)
    {
      OverrideContext.activities.put(paramActivity, Integer.valueOf(1));
    }
  };
  private static Resources overrideResources;
  private final Context base;
  private Resources resources;
  private int state;
  private Resources.Theme theme;

  protected OverrideContext(Context paramContext, Resources paramResources)
  {
    super(paramContext);
    this.base = paramContext;
    this.resources = paramResources;
  }

  private static void checkActivityState(Activity paramActivity)
  {
    if (((paramActivity.getBaseContext() instanceof OverrideContext)) && (((OverrideContext)paramActivity.getBaseContext()).state == 5))
      paramActivity.recreate();
  }

  public static int getActivityState(Activity paramActivity)
  {
    paramActivity = (Integer)activities.get(paramActivity);
    if (paramActivity == null)
      return 0;
    return paramActivity.intValue();
  }

  public static Activity[] getAllActivities()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = activities.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Activity localActivity = (Activity)localEntry.getKey();
      if ((localActivity == null) || (((Integer)localEntry.getValue()).intValue() <= 0))
        continue;
      localArrayList.add(localActivity);
    }
    return (Activity[])localArrayList.toArray(new Activity[localArrayList.size()]);
  }

  public static int getApplicationState()
  {
    int j = 0;
    int i = 0;
    Iterator localIterator = activities.entrySet().iterator();
    while (localIterator.hasNext())
    {
      int m = ((Integer)((Map.Entry)localIterator.next()).getValue()).intValue();
      int k = j;
      if (m >= 1)
        k = j + 1;
      j = k;
      if (m < 3)
        continue;
      i += 1;
      j = k;
    }
    if (i > 0)
      return 2;
    if (j > 0)
      return 1;
    return 0;
  }

  public static Activity getTopActivity()
  {
    Object localObject = null;
    Iterator localIterator = activities.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Activity localActivity = (Activity)localEntry.getKey();
      if ((localActivity == null) || (((Integer)localEntry.getValue()).intValue() != 3))
        continue;
      localObject = localActivity;
    }
    return localObject;
  }

  public static void initApplication(Application paramApplication)
  {
    paramApplication.registerActivityLifecycleCallbacks(lifecycleCallback);
  }

  public static OverrideContext override(ContextWrapper paramContextWrapper, Resources paramResources)
    throws Exception
  {
    Object localObject = paramContextWrapper.getBaseContext();
    if ((localObject instanceof OverrideContext))
    {
      localObject = (OverrideContext)localObject;
      ((OverrideContext)localObject).setResources(paramResources);
      paramResources = (Resources)localObject;
    }
    while (true)
    {
      if (Build.VERSION.SDK_INT > 17)
      {
        localObject = ContextThemeWrapper.class.getDeclaredField("mResources");
        ((Field)localObject).setAccessible(true);
        ((Field)localObject).set(paramContextWrapper, null);
      }
      localObject = ContextThemeWrapper.class.getDeclaredField("mTheme");
      ((Field)localObject).setAccessible(true);
      ((Field)localObject).set(paramContextWrapper, null);
      return paramResources;
      paramResources = new OverrideContext((Context)localObject, paramResources);
      localObject = ContextWrapper.class.getDeclaredField("mBase");
      ((Field)localObject).setAccessible(true);
      ((Field)localObject).set(paramContextWrapper, paramResources);
    }
  }

  public static OverrideContext overrideDefault(ContextWrapper paramContextWrapper)
    throws Exception
  {
    return override(paramContextWrapper, overrideResources);
  }

  public static void setGlobalResources(Resources paramResources)
    throws Exception
  {
    overrideResources = paramResources;
    Object localObject = null;
    Activity[] arrayOfActivity = getAllActivities();
    int j = arrayOfActivity.length;
    int i = 0;
    while (true)
      if (i < j)
      {
        Activity localActivity = arrayOfActivity[i];
        try
        {
          override(localActivity, paramResources);
          label35: i += 1;
        }
        catch (Exception localException)
        {
          break label35;
        }
      }
    if (localException != null)
      throw localException;
    paramResources = getTopActivity();
    if (paramResources != null)
      paramResources.runOnUiThread(new Runnable(paramResources)
      {
        public void run()
        {
          OverrideContext.access$100(this.val$a);
        }
      });
  }

  public AssetManager getAssets()
  {
    if (this.resources == null)
      return this.base.getAssets();
    return this.resources.getAssets();
  }

  public Resources getResources()
  {
    if (this.resources == null)
      return this.base.getResources();
    return this.resources;
  }

  public Resources.Theme getTheme()
  {
    if (this.resources == null)
      return this.base.getTheme();
    if (this.theme == null)
    {
      this.theme = this.resources.newTheme();
      this.theme.setTo(this.base.getTheme());
    }
    return this.theme;
  }

  protected void setResources(Resources paramResources)
  {
    if (this.resources != paramResources)
    {
      this.resources = paramResources;
      this.theme = null;
      this.state = 5;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.github.mmin18.layoutcast.context.OverrideContext
 * JD-Core Version:    0.6.0
 */
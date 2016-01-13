package com.dianping.base.app.loader;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.loader.MyResources;
import com.dianping.loader.MyResources.ResourceOverrideable;

public abstract class AgentActivity extends NovaActivity
  implements MyResources.ResourceOverrideable
{
  private AssetManager assetManager;
  protected AgentFragment mFragment;
  private MyResources myResources;
  private Resources resources;
  private Resources.Theme theme;

  protected abstract AgentFragment getAgentFragment();

  public AssetManager getAssets()
  {
    if (this.assetManager == null)
      return super.getAssets();
    return this.assetManager;
  }

  public MyResources getOverrideResources()
  {
    return this.myResources;
  }

  public Resources getResources()
  {
    if (this.resources == null)
      return super.getResources();
    return this.resources;
  }

  public Resources.Theme getTheme()
  {
    if (this.theme == null)
      return super.getTheme();
    return this.theme;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void initViewAgentView(Bundle paramBundle)
  {
    paramBundle = getSupportFragmentManager();
    this.mFragment = ((AgentFragment)paramBundle.findFragmentByTag("agentfragment"));
    if (this.mFragment == null)
      this.mFragment = getAgentFragment();
    FrameLayout localFrameLayout = new FrameLayout(this);
    localFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    localFrameLayout.setId(16908300);
    super.setContentView(localFrameLayout);
    paramBundle = paramBundle.beginTransaction();
    paramBundle.replace(16908300, this.mFragment, "agentfragment");
    paramBundle.commit();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initViewAgentView(paramBundle);
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if ((this.mFragment != null) && (this.mFragment.isAdded()) && (!this.mFragment.isRemoving()))
      this.mFragment.onLogin(paramBoolean);
    return super.onLogin(paramBoolean);
  }

  public void setOverrideResources(MyResources paramMyResources)
  {
    if (paramMyResources == null)
    {
      this.myResources = null;
      this.resources = null;
      this.assetManager = null;
      this.theme = null;
      return;
    }
    this.myResources = paramMyResources;
    this.resources = paramMyResources.getResources();
    this.assetManager = paramMyResources.getAssets();
    paramMyResources = paramMyResources.getResources().newTheme();
    paramMyResources.setTo(getTheme());
    this.theme = paramMyResources;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.AgentActivity
 * JD-Core Version:    0.6.0
 */
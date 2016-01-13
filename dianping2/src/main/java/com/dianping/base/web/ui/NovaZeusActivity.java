package com.dianping.base.web.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import com.dianping.accountservice.AccountService;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.locationservice.LocationService;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.zeus.ui.ZeusFragment;
import com.dianping.zeus.widget.BaseTitleBar;
import java.net.URLDecoder;

public class NovaZeusActivity extends NovaActivity
{
  public static final String TAG = NovaZeusActivity.class.getSimpleName();
  public boolean mIsUtmGAUploaded = false;
  public ZeusFragment mZeusFragment;

  public void finish()
  {
    super.finish();
    try
    {
      InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
      if (localInputMethodManager != null)
        localInputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  protected Class<? extends NovaZeusFragment> getEfteFragmentClass()
  {
    return NovaZeusFragment.class;
  }

  public String getPageName()
  {
    if (this.mZeusFragment != null)
      return ((NovaZeusFragment)this.mZeusFragment).getPageName();
    return "web";
  }

  protected Bundle handleIntent()
  {
    Bundle localBundle = new Bundle();
    Object localObject1 = getIntent().getExtras();
    if (localObject1 != null)
      localBundle.putAll((Bundle)localObject1);
    localObject1 = getIntent().getData();
    if (localObject1 != null)
      try
      {
        Object localObject2 = ((Uri)localObject1).getEncodedQuery();
        if (!TextUtils.isEmpty((CharSequence)localObject2))
        {
          int i = ((String)localObject2).indexOf("url=");
          int j = ((String)localObject2).indexOf("?");
          if ((i == 0) && (j > i))
          {
            localBundle.putString("url", URLDecoder.decode(((String)localObject2).substring(4)));
            return localBundle;
          }
          localObject1 = localObject2;
          if (i > 0)
          {
            localObject1 = localObject2;
            if (j > i)
            {
              localBundle.putString("url", URLDecoder.decode(((String)localObject2).substring(i + 4)));
              localObject1 = ((String)localObject2).substring(0, i);
            }
          }
          localObject1 = ((String)localObject1).split("&");
          j = localObject1.length;
          i = 0;
          while (i < j)
          {
            localObject2 = localObject1[i].split("=");
            if (localObject2.length > 1)
              localBundle.putString(localObject2[0], localObject2[1]);
            i += 1;
          }
        }
      }
      catch (Exception localException)
      {
        Log.e(TAG, localException.toString());
      }
    return (Bundle)(Bundle)localBundle;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  protected boolean isNeedCity()
  {
    return !"false".equals(getStringParam("isneedcity"));
  }

  protected boolean needTitleBarShadow()
  {
    return !this.mZeusFragment.mIsNoTitleBar;
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    super.onAccountChanged(paramAccountService);
    if (paramAccountService.token() != null)
      ((NovaZeusFragment)this.mZeusFragment).onLogin(true);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    this.mZeusFragment.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onBackPressed()
  {
    this.mZeusFragment.getTitleBarHost().performLLClick();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.activity_nova_efte_web);
    this.mZeusFragment = ((ZeusFragment)getSupportFragmentManager().findFragmentByTag("nova_web_fragment"));
    if (this.mZeusFragment == null)
    {
      this.mZeusFragment = ((ZeusFragment)Fragment.instantiate(this, getEfteFragmentClass().getName(), handleIntent()));
      paramBundle = getSupportFragmentManager().beginTransaction();
      paramBundle.replace(R.id.root_view, this.mZeusFragment, "nova_web_fragment");
      paramBundle.commit();
    }
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    super.onLocationChanged(paramLocationService);
    ((NovaZeusFragment)this.mZeusFragment).onLocationChanged(paramLocationService);
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    ((NovaZeusFragment)this.mZeusFragment).onLogin(paramBoolean);
    return true;
  }

  public void onLoginCancel()
  {
    ((NovaZeusFragment)this.mZeusFragment).onLoginCancel();
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    if (this.mIsUtmGAUploaded)
      this.mZeusFragment.ga();
    this.mIsUtmGAUploaded = true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.web.ui.NovaZeusActivity
 * JD-Core Version:    0.6.0
 */
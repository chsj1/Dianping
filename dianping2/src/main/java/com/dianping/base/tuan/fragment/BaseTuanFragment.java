package com.dianping.base.tuan.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.NovaFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.id;

public class BaseTuanFragment extends NovaFragment
  implements AccountListener, LoginResultListener, RequestHandler<MApiRequest, MApiResponse>
{
  private View fragmentTitleBarView;
  private View leftTitleButton;
  private TextView titleTextView;
  private MApiRequest userProfileRequest;
  private MApiRequest userRequest;

  public TitleBar getActivityTitleBar()
  {
    if ((getActivity() != null) && (!getActivity().isFinishing()))
      return ((NovaActivity)getActivity()).getTitleBar();
    return null;
  }

  public final View getFragmentTitleBar()
  {
    return this.fragmentTitleBarView;
  }

  public void hideFragmentTitleBar()
  {
    if (this.fragmentTitleBarView != null)
      this.fragmentTitleBarView.setVisibility(8);
  }

  public void hideLeftBackButton()
  {
    if (this.leftTitleButton != null)
      this.leftTitleButton.setVisibility(8);
  }

  public final void invalidateTitleBar()
  {
    TitleBar localTitleBar = getActivityTitleBar();
    if (localTitleBar != null)
      onCreateTitleBar(localTitleBar);
  }

  public boolean isLogined()
  {
    if (getAccount() == null);
    do
      return false;
    while (TextUtils.isEmpty(accountService().token()));
    return true;
  }

  @Deprecated
  public void onAccountChanged(AccountService paramAccountService)
  {
    onAccountSwitched(getAccount());
  }

  public void onAccountInfoChanged(UserProfile paramUserProfile)
  {
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    initLeftTitleButton();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    accountService().addListener(this);
  }

  public void onCreateTitleBar(TitleBar paramTitleBar)
  {
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    return onSetContentView(paramLayoutInflater, paramViewGroup);
  }

  public void onDestroy()
  {
    accountService().removeListener(this);
    if ((this instanceof CityConfig.SwitchListener))
      cityConfig().removeListener((CityConfig.SwitchListener)this);
    super.onDestroy();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    return false;
  }

  public void onLoginCancel()
  {
  }

  @Deprecated
  public void onLoginCancel(AccountService paramAccountService)
  {
    onLoginCancel();
  }

  @Deprecated
  public void onLoginSuccess(AccountService paramAccountService)
  {
    onLogin(true);
  }

  @Deprecated
  public void onProfileChanged(AccountService paramAccountService)
  {
    onAccountInfoChanged(getAccount());
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.userRequest)
      this.userRequest = null;
    try
    {
      onUpdateAccount();
      do
        return;
      while (paramMApiRequest != this.userProfileRequest);
      this.userProfileRequest = null;
      try
      {
        onUpdateTuanProfile(null);
        return;
      }
      catch (java.lang.Exception paramMApiRequest)
      {
        return;
      }
    }
    catch (java.lang.Exception paramMApiRequest)
    {
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.userRequest)
      this.userRequest = null;
    try
    {
      accountService().update((DPObject)paramMApiResponse.result());
      onUpdateAccount();
      do
        return;
      while (paramMApiRequest != this.userProfileRequest);
      this.userProfileRequest = null;
      try
      {
        onUpdateTuanProfile((DPObject)paramMApiResponse.result());
        return;
      }
      catch (java.lang.Exception paramMApiRequest)
      {
        return;
      }
    }
    catch (java.lang.Exception paramMApiRequest)
    {
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("key_for_mark_state", "key_for_mark_state");
  }

  protected View onSetContentView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    return paramLayoutInflater.inflate(17367043, paramViewGroup, false);
  }

  protected void onUpdateAccount()
  {
  }

  protected void onUpdateTuanProfile(DPObject paramDPObject)
  {
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.fragmentTitleBarView = paramView.findViewById(R.id.title_bar);
    if (this.fragmentTitleBarView != null)
    {
      this.titleTextView = ((TextView)this.fragmentTitleBarView.findViewById(16908310));
      if (this.titleTextView == null)
        this.titleTextView = ((TextView)this.fragmentTitleBarView.findViewById(R.id.title));
      this.leftTitleButton = this.fragmentTitleBarView.findViewById(R.id.left_title_button);
    }
  }

  public void setTitle(String paramString)
  {
    if (this.titleTextView != null)
      this.titleTextView.setText(paramString);
    TitleBar localTitleBar = getActivityTitleBar();
    if (localTitleBar != null)
      localTitleBar.setTitle(paramString);
  }

  public void showFragmentTitleBar()
  {
    if (this.fragmentTitleBarView != null)
      this.fragmentTitleBarView.setVisibility(0);
  }

  public void showLeftBackButton()
  {
    if (this.leftTitleButton != null)
      this.leftTitleButton.setVisibility(0);
  }

  protected void updateAccount()
  {
    if (!isLogined());
    do
      return;
    while (this.userRequest != null);
    this.userRequest = mapiGet(this, "http://m.api.dianping.com/user.bin?&userid=0&refresh=true&token=" + accountService().token(), CacheType.DISABLED);
    mapiService().exec(this.userRequest, this);
  }

  protected void updateTuanProfile()
  {
    if (!isLogined())
    {
      onUpdateTuanProfile(null);
      return;
    }
    if (this.userProfileRequest != null)
      this.userProfileRequest = null;
    this.userProfileRequest = mapiGet(this, "http://app.t.dianping.com/tuanprofilegn.bin?token=" + accountService().token(), CacheType.CRITICAL);
    mapiService().exec(this.userProfileRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.fragment.BaseTuanFragment
 * JD-Core Version:    0.6.0
 */
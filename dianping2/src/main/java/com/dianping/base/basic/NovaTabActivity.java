package com.dianping.base.basic;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.CityConfig;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.UserProfile;
import com.dianping.util.Log;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.lang.reflect.Method;

public class NovaTabActivity extends TabActivity
{
  private static final String TAG = NovaActivity.class.getSimpleName();
  public static final int TITLE_TYPE_ARROW = 3;
  public static final int TITLE_TYPE_DOUBLE_TEXT_BUTTON = 9;
  public static final int TITLE_TYPE_NONE = 2;
  public static final int TITLE_TYPE_WIDE = 1;
  protected ImageButton leftTitleButton;
  private LocationService locationService;
  private SharedPreferences prefs;
  protected ImageButton rightTitleButton;
  protected TextView subtitleText;
  protected TextView titleText;

  private View findTitleRoot()
  {
    View localView1 = null;
    View localView2 = findViewById(R.id.title_bar);
    if (localView2 != null)
      localView1 = (View)localView2.getParent();
    do
    {
      return localView1;
      localView2 = findViewById(16908310);
    }
    while (localView2 == null);
    return (View)localView2.getParent();
  }

  public AccountService accountService()
  {
    return (AccountService)DPApplication.instance().getService("account");
  }

  public City city()
  {
    return DPApplication.instance().cityConfig().currentCity();
  }

  public CityConfig cityConfig()
  {
    return DPApplication.instance().cityConfig();
  }

  public int cityId()
  {
    return city().id();
  }

  protected int customTitleType()
  {
    return 1;
  }

  public void finish()
  {
    super.finish();
    if (getIntent().hasExtra("start_from_main"));
    try
    {
      Activity.class.getDeclaredMethod("overridePendingTransition", new Class[] { Integer.TYPE, Integer.TYPE }).invoke(this, new Object[] { Integer.valueOf(R.anim.back_enter), Integer.valueOf(R.anim.back_exit) });
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public UserProfile getAccount()
  {
    Object localObject = accountService().profile();
    if (localObject != null)
      try
      {
        localObject = (UserProfile)((DPObject)localObject).edit().putString("Token", accountService().token()).generate().decodeToObject(UserProfile.DECODER);
        return localObject;
      }
      catch (ArchiveException localArchiveException)
      {
        Log.w(localArchiveException.getLocalizedMessage());
      }
    return (UserProfile)null;
  }

  public Object getService(String paramString)
  {
    return DPApplication.instance().getService(paramString);
  }

  protected void initCustomTitle()
  {
    if (getParent() == null)
    {
      if (customTitleType() != 1)
        break label105;
      requestWindowFeature(7);
      super.setContentView(R.layout.custom_tab_activity);
      getWindow().setFeatureInt(7, R.layout.wide_title_bar);
      this.leftTitleButton = ((ImageButton)findViewById(R.id.left_title_button));
      this.titleText = ((TextView)findViewById(16908310));
      this.subtitleText = ((TextView)findViewById(R.id.subtitle));
      this.rightTitleButton = ((ImageButton)findViewById(R.id.right_title_button));
      setTitle(getTitle());
    }
    label105: 
    do
    {
      return;
      if (customTitleType() == 2)
      {
        requestWindowFeature(1);
        return;
      }
      if (customTitleType() != 3)
        continue;
      requestWindowFeature(7);
      super.setContentView(R.layout.custom_tab_activity);
      this.leftTitleButton = ((ImageButton)findViewById(R.id.left_title_button));
      this.titleText = ((TextView)findViewById(16908310));
      this.subtitleText = ((TextView)findViewById(R.id.subtitle));
      this.rightTitleButton = ((ImageButton)findViewById(R.id.right_title_button));
      setTitle(getTitle());
      return;
    }
    while (customTitleType() != 9);
    requestWindowFeature(7);
    super.setContentView(R.layout.custom_tab_activity);
    super.getWindow().setFeatureInt(7, R.layout.double_text_title_bar);
    this.leftTitleButton = ((ImageButton)findViewById(R.id.left_title_button));
    this.titleText = ((TextView)findViewById(16908310));
    this.subtitleText = ((TextView)findViewById(R.id.subtitle));
    setTitle(getTitle());
  }

  public boolean isLocalPromoCity()
  {
    return city().isLocalPromo();
  }

  public boolean isLogined()
  {
    if (getAccount() == null);
    do
      return false;
    while (TextUtils.isEmpty(accountService().token()));
    return true;
  }

  public boolean isPromoCity()
  {
    return city().isPromo();
  }

  public LocationService locationService()
  {
    if (this.locationService == null)
      this.locationService = ((LocationService)getService("location"));
    return this.locationService;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    preferences();
    initCustomTitle();
    if (this.leftTitleButton != null)
      this.leftTitleButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          NovaTabActivity.this.finish();
        }
      });
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    try
    {
      super.onRestoreInstanceState(paramBundle);
      return;
    }
    catch (Exception paramBundle)
    {
    }
  }

  public SharedPreferences preferences()
  {
    if (this.prefs == null)
      this.prefs = DPActivity.preferences(this);
    return this.prefs;
  }

  public void setRightTitleButton(int paramInt, View.OnClickListener paramOnClickListener)
  {
    if (this.rightTitleButton == null)
      return;
    if (paramInt == -1)
    {
      this.rightTitleButton.setVisibility(8);
      return;
    }
    this.rightTitleButton.setVisibility(0);
    this.rightTitleButton.setImageResource(paramInt);
    this.rightTitleButton.setOnClickListener(paramOnClickListener);
  }

  public void setSubtitle(CharSequence paramCharSequence)
  {
    if (this.subtitleText != null)
      this.subtitleText.setText(paramCharSequence);
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    super.setTitle(paramCharSequence);
    if (this.titleText != null)
      this.titleText.setText(paramCharSequence);
  }

  public void setTitleVisibility(int paramInt)
  {
    View localView = findTitleRoot();
    if (localView != null)
    {
      localView.setVisibility(paramInt);
      return;
    }
    Log.e(TAG, "findTitleRoot null");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.NovaTabActivity
 * JD-Core Version:    0.6.0
 */
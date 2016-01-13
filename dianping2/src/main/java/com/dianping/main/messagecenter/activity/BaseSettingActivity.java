package com.dianping.main.messagecenter.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.util.ViewUtils;
import java.util.ArrayList;

public class BaseSettingActivity extends NovaActivity
{
  protected static final String K_SWITCH_ID = "ID";
  protected static final String K_SWITCH_STATUS = "Status";
  protected static final String K_TOKEN = "token";
  protected String mConfig;
  protected View mEmptyView;
  protected View mErrorView;
  protected LinearLayout mLaySetting;
  protected ListView mLvSetting;
  protected MApiRequest mQuerySettingReq;
  protected BasicAdapter mSettingAdapter;
  protected ArrayList<DPObject> mSettingObjList;
  protected String mToken;
  protected MApiRequest mUpdateSettingReq;

  private void initTitleBar()
  {
    super.getTitleBar().setLeftView(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BaseSettingActivity.this.onBackPressed();
      }
    });
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onBackPressed()
  {
    super.onBackPressed();
    if (this.mConfig == null)
    {
      finish();
      return;
    }
    sendUpdateSettingReq();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mToken = accountService().token();
    initTitleBar();
  }

  protected void removeOldEmptyView()
  {
    if ((this.mLaySetting != null) && (this.mEmptyView != null))
    {
      this.mLaySetting.removeView(this.mEmptyView);
      this.mEmptyView = null;
    }
  }

  protected void removeOldErrorView()
  {
    if ((this.mLaySetting != null) && (this.mErrorView != null))
    {
      this.mLaySetting.removeView(this.mErrorView);
      this.mErrorView = null;
    }
  }

  protected void sendQuerySettingReq()
  {
  }

  protected void sendUpdateSettingReq()
  {
  }

  protected void setEmptyView()
  {
    this.mEmptyView = getLoadingView();
    ViewUtils.hideView(this.mEmptyView, true);
    if ((this.mLaySetting != null) && (this.mLvSetting != null))
    {
      this.mLaySetting.addView(this.mEmptyView);
      this.mLvSetting.setEmptyView(this.mEmptyView);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.messagecenter.activity.BaseSettingActivity
 * JD-Core Version:    0.6.0
 */
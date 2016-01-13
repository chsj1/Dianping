package com.dianping.base.share.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.business.IShareEngine;
import com.dianping.base.share.business.ShareEngineFactory;
import com.dianping.base.sso.ILogininListener;
import com.dianping.base.sso.IThirdSSOLogin;
import com.dianping.base.sso.ThirdSSOLoginFactory;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.UserProfile;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import org.json.JSONObject;

public class SnsView extends LinearLayout
  implements ILogininListener, RequestHandler<MApiRequest, MApiResponse>
{
  NovaActivity mActivity;
  private CheckBox mCbQzone;
  private CheckBox mCbSinaWeibo;
  private int mFeed;
  IShareEngine mShareEngine;
  private MApiRequest mSsoBindReq;
  IThirdSSOLogin mSsoLogin;
  private TextView mTvText;

  public SnsView(Context paramContext)
  {
    super(paramContext);
  }

  public SnsView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void initItemStatus(CheckBox paramCheckBox, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1)
    {
      paramCheckBox.setOnCheckedChangeListener(null);
      paramCheckBox.setChecked(paramBoolean2);
      return;
    }
    paramCheckBox.setChecked(false);
    paramCheckBox.setOnCheckedChangeListener(new SyncCheckedChangeListener(null));
  }

  private void initSyncItem(int paramInt1, int paramInt2)
  {
    CheckBox localCheckBox = this.mCbSinaWeibo;
    boolean bool2;
    if ((paramInt1 & 0x1) != 0)
    {
      bool1 = true;
      if ((paramInt2 & 0x1) == 0)
        break label114;
      bool2 = true;
      label23: initItemStatus(localCheckBox, bool1, bool2);
      localCheckBox = this.mCbQzone;
      if ((paramInt1 & 0x20) == 0)
        break label120;
      bool1 = true;
      label47: if ((paramInt2 & 0x20) == 0)
        break label126;
      bool2 = true;
      label57: initItemStatus(localCheckBox, bool1, bool2);
      paramInt1 = ConfigHelper.disableFeed;
      if ((paramInt1 & 0x1) != 0)
        break label132;
      bool1 = true;
      label79: ViewUtils.showView(this.mCbSinaWeibo, bool1);
      if ((paramInt1 & 0x20) != 0)
        break label138;
    }
    label132: label138: for (boolean bool1 = true; ; bool1 = false)
    {
      ViewUtils.showView(this.mCbQzone, bool1);
      return;
      bool1 = false;
      break;
      label114: bool2 = false;
      break label23;
      label120: bool1 = false;
      break label47;
      label126: bool2 = false;
      break label57;
      bool1 = false;
      break label79;
    }
  }

  private void sendSsoBindReq(int paramInt, Object paramObject)
  {
    if (this.mSsoBindReq != null)
    {
      this.mActivity.mapiService().abort(this.mSsoBindReq, this, true);
      this.mSsoBindReq = null;
    }
    this.mFeed = paramInt;
    Object localObject = null;
    switch (paramInt)
    {
    default:
      paramObject = localObject;
    case 1:
    case 32:
    }
    while (true)
    {
      this.mSsoBindReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/ssobind.bin", new String[] { "token", this.mActivity.accountService().token(), "ssoresponse", paramObject, "platform", this.mShareEngine.getPlatform() });
      this.mActivity.mapiService().exec(this.mSsoBindReq, this);
      return;
      paramObject = ((Bundle)paramObject).toString();
      continue;
      paramObject = ((JSONObject)paramObject).toString();
    }
  }

  public void bindActivity(NovaActivity paramNovaActivity)
  {
    this.mActivity = paramNovaActivity;
    if ((this.mActivity != null) && (this.mActivity.getAccount() != null))
      initSyncItem(DPActivity.preferences(), this.mActivity.getAccount().feedFlag());
  }

  public int getFeed()
  {
    int j = 0;
    if (this.mCbSinaWeibo.isChecked());
    for (int i = 1; ; i = 0)
    {
      if (this.mCbQzone.isChecked())
        j = 32;
      return i + j;
    }
  }

  public String getSnsString()
  {
    String str1 = "";
    if (this.mCbSinaWeibo.isChecked())
      str1 = "" + "新浪微博";
    String str2 = str1;
    if (this.mCbQzone.isChecked())
    {
      str2 = str1;
      if (str1.length() > 0)
        str2 = str1 + "|";
      str2 = str2 + "QQ空间";
    }
    return str2;
  }

  public void initSyncItem(SharedPreferences paramSharedPreferences, int paramInt)
  {
    initSyncItem(paramInt, paramSharedPreferences.getInt("syncMask", paramInt));
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (this.mSsoLogin != null)
      this.mSsoLogin.callback(paramInt1, paramInt2, paramIntent);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTvText = ((TextView)findViewById(R.id.tv_text));
    this.mCbSinaWeibo = ((CheckBox)findViewById(R.id.cb_weibo));
    this.mCbQzone = ((CheckBox)findViewById(R.id.cb_qzone));
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mSsoBindReq)
    {
      this.mActivity.showToast("绑定失败,请重试");
      this.mSsoBindReq = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    int i;
    if (paramMApiRequest == this.mSsoBindReq)
    {
      i = DPActivity.preferences().getInt("syncMask", 0);
      switch (this.mFeed)
      {
      default:
      case 1:
      case 32:
      }
    }
    while (true)
    {
      this.mSsoBindReq = null;
      return;
      this.mCbSinaWeibo.setOnCheckedChangeListener(null);
      this.mCbSinaWeibo.setChecked(true);
      SnsHelper.updateFeedFlag(DPActivity.preferences(), this.mActivity.getAccount().feedFlag() ^ 0x1);
      SnsHelper.updateSyncMask(DPActivity.preferences(), i ^ 0x1);
      continue;
      this.mCbQzone.setOnCheckedChangeListener(null);
      this.mCbQzone.setChecked(true);
      SnsHelper.updateFeedFlag(DPActivity.preferences(), this.mActivity.getAccount().feedFlag() ^ 0x20);
      SnsHelper.updateSyncMask(DPActivity.preferences(), i ^ 0x20);
    }
  }

  public void onSSOLoginCancel(int paramInt)
  {
  }

  public void onSSOLoginFailed(int paramInt)
  {
    this.mActivity.showToast("登录失败,请重试");
  }

  public void onSSOLoginSucceed(int paramInt, Object paramObject)
  {
    sendSsoBindReq(paramInt, paramObject);
  }

  public void setText(String paramString)
  {
    this.mTvText.setText(paramString);
  }

  public void unCheckedSns()
  {
    this.mCbSinaWeibo.setChecked(false);
    this.mCbQzone.setChecked(false);
  }

  private class SyncCheckedChangeListener
    implements CompoundButton.OnCheckedChangeListener
  {
    private SyncCheckedChangeListener()
    {
    }

    public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
    {
      int j;
      int i;
      if ((paramBoolean) && (SnsView.this.mActivity != null))
      {
        j = paramCompoundButton.getId();
        i = 0;
        if (j != R.id.cb_weibo)
          break label110;
        i = 1;
      }
      while (true)
      {
        SnsView.this.mShareEngine = ShareEngineFactory.createShareEngine(i);
        String str = SnsView.this.mShareEngine.getSsoLoginUrl();
        SnsView.this.mSsoLogin = ThirdSSOLoginFactory.ssoLogin(str);
        if (SnsView.this.mSsoLogin != null)
          SnsView.this.mSsoLogin.ssoLogin(str, SnsView.this.mActivity, SnsView.this);
        paramCompoundButton.setChecked(false);
        return;
        label110: if (j != R.id.cb_qzone)
          continue;
        i = 32;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.sync.SnsView
 * JD-Core Version:    0.6.0
 */
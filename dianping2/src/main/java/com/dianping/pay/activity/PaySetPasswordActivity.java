package com.dianping.pay.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.pay.entity.PaySetPwdDataSource;
import com.dianping.pay.entity.PaySetPwdDataSource.SetPasswordListener;
import com.dianping.pay.utils.WebankManager;
import com.dianping.pay.view.GridPasswordView;
import com.dianping.pay.view.GridPasswordView.OnPasswordChangedListener;
import com.dianping.pay.view.NumberKeyboardView;
import com.dianping.pay.view.NumberKeyboardView.OnNumKeyboardListener;
import com.dianping.pay.view.WebankBindStepView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class PaySetPasswordActivity extends NovaActivity
  implements PaySetPwdDataSource.SetPasswordListener
{
  protected PaySetPwdDataSource dataSource;
  protected GridPasswordView gridPassword;
  protected WebankBindStepView layerBindStep;
  protected NumberKeyboardView numberKeyboard;
  protected TextView textTitle;

  private void backOperation()
  {
    if (this.dataSource.password == null)
    {
      if ((4 == this.dataSource.source) || (3 == this.dataSource.source))
      {
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paycardlist"));
        localIntent.addFlags(67108864);
        startActivity(localIntent);
        finish();
        return;
      }
      WebankManager.goWebankEntry(this, false, 0, null, null);
      return;
    }
    resetPassword();
  }

  private void initView()
  {
    super.setContentView(R.layout.pay_webank_set_password_activity);
    super.getTitleBar().setLeftView(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PaySetPasswordActivity.this.backOperation();
      }
    });
    this.layerBindStep = ((WebankBindStepView)findViewById(R.id.bind_step_layer));
    this.layerBindStep.setStep(2);
    this.textTitle = ((TextView)findViewById(R.id.set_pwd_title));
    this.gridPassword = ((GridPasswordView)findViewById(R.id.grid_password));
    this.numberKeyboard = ((NumberKeyboardView)findViewById(R.id.num_keyboard));
    this.numberKeyboard.setKeyboardInputListener(new NumberKeyboardView.OnNumKeyboardListener()
    {
      public void onKeyChanged(int paramInt)
      {
        PaySetPasswordActivity.this.gridPassword.keyChanged(paramInt);
      }
    });
    this.gridPassword.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener()
    {
      public void onChanged(String paramString)
      {
      }

      public void onMaxLength(String paramString)
      {
        if (PaySetPasswordActivity.this.dataSource.password == null)
        {
          PaySetPasswordActivity.this.dataSource.password = paramString;
          PaySetPasswordActivity.this.gridPassword.clearPassword();
          PaySetPasswordActivity.this.updateView();
          Toast.makeText(PaySetPasswordActivity.this, "请再次输入密码", 0).show();
          return;
        }
        if (paramString.equals(PaySetPasswordActivity.this.dataSource.password))
        {
          PaySetPasswordActivity.this.dataSource.reqSetPwd();
          return;
        }
        PaySetPasswordActivity.this.dataSource.password = null;
        PaySetPasswordActivity.this.gridPassword.clearPassword();
        PaySetPasswordActivity.this.updateView();
        Toast.makeText(PaySetPasswordActivity.this, "两次输入的密码不同，请重新输入", 1).show();
      }
    });
    updateView();
  }

  private void resetPassword()
  {
    this.dataSource.password = null;
    this.gridPassword.clearPassword();
    updateView();
  }

  private void updateView()
  {
    if (this.dataSource.password == null)
      if ((4 == this.dataSource.source) || (3 == this.dataSource.source))
      {
        this.textTitle.setText("设置新密码");
        if (1 != this.dataSource.source)
          break label136;
        this.layerBindStep.setVisibility(0);
      }
    while (true)
    {
      if (3 != this.dataSource.source)
        break label148;
      setTitle("修改支付密码");
      return;
      this.textTitle.setText("请设置6位支付密码");
      break;
      if ((4 == this.dataSource.source) || (3 == this.dataSource.source))
      {
        this.textTitle.setText("请再次输入以确认");
        break;
      }
      this.textTitle.setText("请再次输入完成支付");
      break;
      label136: this.layerBindStep.setVisibility(8);
    }
    label148: if (4 == this.dataSource.source)
    {
      setTitle("找回支付密码");
      return;
    }
    setTitle("银行卡支付");
  }

  public void onBackPressed()
  {
    backOperation();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.dataSource = new PaySetPwdDataSource(this);
    this.dataSource.setPwdListener = this;
    this.dataSource.source = getIntParam("source");
    this.dataSource.paySessionId = getStringParam("paysessionid");
    initView();
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.dataSource.releaseRequests();
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.dataSource.restoreData(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.dataSource.saveData(paramBundle);
  }

  public void onSetPwdFail()
  {
    resetPassword();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.activity.PaySetPasswordActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.pay.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import com.dianping.app.DPActivity;
import com.dianping.pay.entity.PayVerifyPwdDataSource;
import com.dianping.pay.entity.PayVerifyPwdDataSource.VerifyPasswordListener;
import com.dianping.pay.view.GridPasswordView;
import com.dianping.pay.view.GridPasswordView.OnPasswordChangedListener;
import com.dianping.pay.view.NumberKeyboardView;
import com.dianping.pay.view.NumberKeyboardView.OnNumKeyboardListener;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaImageView;
import com.dianping.widget.view.NovaTextView;

public class PayVerifyPasswordPopActivity extends DPActivity
  implements PayVerifyPwdDataSource.VerifyPasswordListener
{
  private NovaImageView closeBtn;
  protected PayVerifyPwdDataSource dataSource;
  private GridPasswordView gridPassword;
  private NumberKeyboardView numberKeyboard;
  private NovaTextView textForgetPwd;
  private View verifyPwdPopBg;
  private LinearLayout viewContent;

  private void initView()
  {
    this.textForgetPwd = ((NovaTextView)findViewById(R.id.forget_pwd));
    this.textForgetPwd.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paycardlist?state=forgot_pwd"));
        PayVerifyPasswordPopActivity.this.startActivity(paramView);
        PayVerifyPasswordPopActivity.this.finish();
      }
    });
    this.viewContent = ((LinearLayout)findViewById(R.id.content));
    this.closeBtn = ((NovaImageView)findViewById(R.id.close));
    this.closeBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PayVerifyPasswordPopActivity.this.finish();
        PayVerifyPasswordPopActivity.this.overridePendingTransition(0, 0);
      }
    });
    this.verifyPwdPopBg = findViewById(R.id.verify_pwd_pop_bg);
    this.verifyPwdPopBg.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PayVerifyPasswordPopActivity.this.finish();
        PayVerifyPasswordPopActivity.this.overridePendingTransition(0, 0);
      }
    });
    this.gridPassword = ((GridPasswordView)findViewById(R.id.grid_password));
    this.numberKeyboard = ((NumberKeyboardView)findViewById(R.id.num_keyboard));
    this.numberKeyboard.setKeyboardInputListener(new NumberKeyboardView.OnNumKeyboardListener()
    {
      public void onKeyChanged(int paramInt)
      {
        PayVerifyPasswordPopActivity.this.gridPassword.keyChanged(paramInt);
      }
    });
    this.gridPassword.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener()
    {
      public void onChanged(String paramString)
      {
      }

      public void onMaxLength(String paramString)
      {
        PayVerifyPasswordPopActivity.this.dataSource.password = paramString;
        PayVerifyPasswordPopActivity.this.dataSource.reqVerifyPwd();
      }
    });
    Animation localAnimation = AnimationUtils.loadAnimation(this, R.anim.popup_show);
    this.verifyPwdPopBg.startAnimation(localAnimation);
    this.viewContent.postDelayed(new Runnable()
    {
      public void run()
      {
        Animation localAnimation = AnimationUtils.loadAnimation(PayVerifyPasswordPopActivity.this, R.anim.popup_up_in);
        PayVerifyPasswordPopActivity.this.verifyPwdPopBg.startAnimation(localAnimation);
      }
    }
    , 100L);
  }

  public void onClearPwd()
  {
    this.dataSource.password = null;
    this.gridPassword.clearPassword();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.pay_webank_verify_pwd_pop_activity);
    this.dataSource = new PayVerifyPwdDataSource(this);
    this.dataSource.verifyPwdListener = this;
    this.dataSource.source = getIntParam("source");
    this.dataSource.paySessionId = getStringParam("paysessionid");
    this.dataSource.mobileNo = getStringParam("mobileno");
    initView();
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.dataSource.releaseRequests();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.activity.PayVerifyPasswordPopActivity
 * JD-Core Version:    0.6.0
 */
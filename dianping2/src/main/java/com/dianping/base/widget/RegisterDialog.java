package com.dianping.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;

public class RegisterDialog extends Dialog
{
  private Button mBtnPositive;
  private TextView mCancelTextView;
  private TextView mLoginTextView;

  public RegisterDialog(Context paramContext)
  {
    super(paramContext, R.style.dialog_fullscreen);
  }

  protected RegisterDialog(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.register_suggest_dialog);
    this.mBtnPositive = ((Button)findViewById(R.id.btn_positive));
    this.mCancelTextView = ((TextView)findViewById(R.id.tv_negative));
    this.mLoginTextView = ((TextView)findViewById(R.id.tv_login));
    this.mCancelTextView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        RegisterDialog.this.cancel();
      }
    });
  }

  public RegisterDialog setLogin(DialogInterface.OnClickListener paramOnClickListener)
  {
    if (this.mLoginTextView != null)
      this.mLoginTextView.setOnClickListener(new View.OnClickListener(paramOnClickListener)
      {
        public void onClick(View paramView)
        {
          if (this.val$listener != null)
            this.val$listener.onClick(RegisterDialog.this, 0);
          RegisterDialog.this.dismiss();
        }
      });
    return this;
  }

  public RegisterDialog setPositiveButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
  {
    if (this.mBtnPositive != null)
    {
      this.mBtnPositive.setText(paramCharSequence);
      this.mBtnPositive.setOnClickListener(new View.OnClickListener(paramOnClickListener)
      {
        public void onClick(View paramView)
        {
          if (this.val$listener != null)
            this.val$listener.onClick(RegisterDialog.this, 0);
          RegisterDialog.this.dismiss();
        }
      });
    }
    return this;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.RegisterDialog
 * JD-Core Version:    0.6.0
 */
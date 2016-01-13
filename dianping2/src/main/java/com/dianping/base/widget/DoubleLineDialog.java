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

public class DoubleLineDialog extends Dialog
{
  private Button btnNegative;
  private Button btnPositive;
  private TextView content;
  private TextView tips;

  public DoubleLineDialog(Context paramContext)
  {
    super(paramContext, R.style.dialog_fullscreen);
  }

  protected DoubleLineDialog(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.double_line_dialog);
    this.btnPositive = ((Button)findViewById(R.id.btn_positive));
    this.btnNegative = ((Button)findViewById(R.id.btn_negative));
    this.tips = ((TextView)findViewById(R.id.text_tips));
    this.content = ((TextView)findViewById(R.id.text_content));
    findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        DoubleLineDialog.this.cancel();
      }
    });
  }

  public DoubleLineDialog setContent(CharSequence paramCharSequence)
  {
    if (this.content != null)
      this.content.setText(paramCharSequence);
    return this;
  }

  public DoubleLineDialog setNegativeButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
  {
    if (this.btnNegative != null)
    {
      this.btnNegative.setText(paramCharSequence);
      this.btnNegative.setOnClickListener(new View.OnClickListener(paramOnClickListener)
      {
        public void onClick(View paramView)
        {
          if (this.val$listener != null)
            this.val$listener.onClick(DoubleLineDialog.this, 0);
          DoubleLineDialog.this.dismiss();
        }
      });
    }
    return this;
  }

  public DoubleLineDialog setPositiveButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
  {
    if (this.btnPositive != null)
    {
      this.btnPositive.setText(paramCharSequence);
      this.btnPositive.setOnClickListener(new View.OnClickListener(paramOnClickListener)
      {
        public void onClick(View paramView)
        {
          if (this.val$listener != null)
            this.val$listener.onClick(DoubleLineDialog.this, 0);
          DoubleLineDialog.this.dismiss();
        }
      });
    }
    return this;
  }

  public DoubleLineDialog setTips(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
  {
    if (this.tips != null)
    {
      this.tips.setText(paramCharSequence);
      this.tips.setOnClickListener(new View.OnClickListener(paramOnClickListener)
      {
        public void onClick(View paramView)
        {
          if (this.val$listener != null)
            this.val$listener.onClick(DoubleLineDialog.this, 0);
          DoubleLineDialog.this.dismiss();
        }
      });
    }
    return this;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.DoubleLineDialog
 * JD-Core Version:    0.6.0
 */
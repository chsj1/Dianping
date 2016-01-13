package com.dianping.base.share.model;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class PayVerifyDialog extends Dialog
{
  private View cancel;
  private EditText codeInput;
  private TextView contentView;
  private TextView errorView;
  private Button getCode;
  private View ok;

  public PayVerifyDialog(Context paramContext)
  {
    super(paramContext);
    paramContext = View.inflate(paramContext, R.layout.pay_vertify_dialog, null);
    this.contentView = ((TextView)paramContext.findViewById(R.id.content));
    this.codeInput = ((EditText)paramContext.findViewById(R.id.code_input));
    this.getCode = ((Button)paramContext.findViewById(R.id.get_code));
    this.errorView = ((TextView)paramContext.findViewById(R.id.error));
    this.cancel = paramContext.findViewById(R.id.cancel);
    this.ok = paramContext.findViewById(R.id.ok);
    super.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    super.setCancelable(false);
    super.setCanceledOnTouchOutside(false);
    super.setContentView(paramContext);
  }

  public CharSequence getCode()
  {
    return this.codeInput.getText();
  }

  public EditText getCodeInputText()
  {
    return this.codeInput;
  }

  public PayVerifyDialog setCancelButton(View.OnClickListener paramOnClickListener)
  {
    this.cancel.setOnClickListener(paramOnClickListener);
    return this;
  }

  public PayVerifyDialog setContent(String paramString)
  {
    this.contentView.setText(paramString);
    return this;
  }

  public PayVerifyDialog setError(String paramString)
  {
    this.errorView.setText(paramString);
    return this;
  }

  public PayVerifyDialog setGetCodeButton(View.OnClickListener paramOnClickListener)
  {
    this.getCode.setOnClickListener(paramOnClickListener);
    return this;
  }

  public PayVerifyDialog setGetCodeButtonEnable(boolean paramBoolean)
  {
    this.getCode.setEnabled(paramBoolean);
    return this;
  }

  public PayVerifyDialog setGetCodeButtonText(String paramString)
  {
    this.getCode.setText(paramString);
    return this;
  }

  public PayVerifyDialog setOkButton(View.OnClickListener paramOnClickListener)
  {
    this.ok.setOnClickListener(paramOnClickListener);
    return this;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.model.PayVerifyDialog
 * JD-Core Version:    0.6.0
 */
package com.dianping.hui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class HuiAlertDialog extends AlertDialog
  implements View.OnClickListener
{
  private Button cancelButton;
  OnDialogOperationListener listener;
  private String message;
  private TextView messageView;
  private Button payButton;
  private String richMessage;
  private String title;
  private TextView titleView;

  public HuiAlertDialog(Context paramContext)
  {
    super(paramContext);
  }

  public HuiAlertDialog(Context paramContext, String paramString1, String paramString2)
  {
    this(paramContext, paramString1, paramString2, null);
  }

  public HuiAlertDialog(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    super(paramContext);
    this.title = paramString1;
    this.message = paramString2;
    this.richMessage = paramString3;
  }

  public OnDialogOperationListener getListener()
  {
    return this.listener;
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.cancelButton)
    {
      if (this.listener != null)
        this.listener.cancel();
      cancel();
    }
    do
      return;
    while (i != R.id.payButton);
    if (this.listener != null)
      this.listener.confirm();
    cancel();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.hui_cashier_dialog);
    super.getWindow().setGravity(17);
    setupViews();
  }

  public void setListener(OnDialogOperationListener paramOnDialogOperationListener)
  {
    this.listener = paramOnDialogOperationListener;
  }

  public void setupViews()
  {
    this.cancelButton = ((Button)findViewById(R.id.cancelButton));
    this.cancelButton.setOnClickListener(this);
    this.payButton = ((Button)findViewById(R.id.payButton));
    this.payButton.setOnClickListener(this);
    this.titleView = ((TextView)findViewById(R.id.dialogTitle));
    this.messageView = ((TextView)findViewById(R.id.dialogMessage));
    if (!android.text.TextUtils.isEmpty(this.title))
      this.titleView.setText(this.title);
    if (!android.text.TextUtils.isEmpty(this.richMessage))
      this.messageView.setText(com.dianping.util.TextUtils.jsonParseText(this.richMessage));
    do
      return;
    while (android.text.TextUtils.isEmpty(this.message));
    this.messageView.setText(this.message);
  }

  public static abstract interface OnDialogOperationListener
  {
    public abstract void cancel();

    public abstract void confirm();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.widget.HuiAlertDialog
 * JD-Core Version:    0.6.0
 */
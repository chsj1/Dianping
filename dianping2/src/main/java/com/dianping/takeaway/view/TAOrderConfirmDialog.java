package com.dianping.takeaway.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;

public class TAOrderConfirmDialog extends Dialog
  implements View.OnClickListener
{
  private Button cancelButton;
  private View contentLayout;
  private int interval;
  OnDialogOperationListener listener;
  private int maxValue;
  private int minValue;
  private Button okButton;
  private View outsideLayout;
  private TATimePickerView picker;
  private TextView tipsView;

  private TAOrderConfirmDialog(Context paramContext)
  {
    super(paramContext, R.style.FullScreenDialog);
  }

  public TAOrderConfirmDialog(Context paramContext, int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramContext);
    this.minValue = paramInt1;
    this.maxValue = paramInt2;
    this.interval = paramInt3;
  }

  private void setValueRangeAndInterval(int paramInt1, int paramInt2, int paramInt3)
  {
    this.picker.setInterval(paramInt3);
    this.picker.setNumRange(paramInt1, paramInt2);
    this.picker.setValue(paramInt2);
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
    while (i != R.id.okButton);
    if (this.listener != null)
      this.listener.confirm(this.picker.getValue());
    cancel();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.takeaway_dialog_confirm_order);
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
    this.okButton = ((Button)findViewById(R.id.okButton));
    this.tipsView = ((TextView)findViewById(R.id.tipsText));
    this.okButton.setOnClickListener(this);
    this.picker = ((TATimePickerView)findViewById(R.id.timePicker));
    setValueRangeAndInterval(this.minValue, this.maxValue, this.interval);
    this.tipsView.setText("您在" + this.maxValue + "分钟前下的单");
    this.outsideLayout = findViewById(R.id.outsideLayout);
    this.outsideLayout.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getAction() == 1)
          TAOrderConfirmDialog.this.cancel();
        return true;
      }
    });
    this.contentLayout = findViewById(R.id.contentLayout);
    this.contentLayout.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        return true;
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.view.TAOrderConfirmDialog
 * JD-Core Version:    0.6.0
 */
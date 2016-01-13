package com.dianping.selectdish.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;

public class SelectDishAlertDialog extends Dialog
  implements View.OnClickListener
{
  private String content;
  private TextView contentView;
  private OnDialogClickListener dialogClickListener = null;
  private Button leftBtn;
  private String leftBtnMsg;
  private Button rightBtn;
  private String rightBtnMsg;
  private String title;
  private TextView titleView;

  public SelectDishAlertDialog(Context paramContext)
  {
    super(paramContext, R.style.FullScreenDialog);
  }

  public SelectDishAlertDialog(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    super(paramContext, R.style.FullScreenDialog);
    this.title = paramString1;
    this.content = paramString2;
    this.leftBtnMsg = paramString3;
    this.rightBtnMsg = paramString4;
  }

  public void onClick(View paramView)
  {
    if (this.dialogClickListener == null);
    int i;
    do
    {
      return;
      i = paramView.getId();
      if (i != R.id.left_btn)
        continue;
      this.dialogClickListener.onCancelClick(this);
      return;
    }
    while (i != R.id.right_btn);
    this.dialogClickListener.onConfirmClick(this);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.selectdish_alert_dialog);
    super.getWindow().setGravity(17);
    setupViews();
  }

  public void setDialogClickListener(OnDialogClickListener paramOnDialogClickListener)
  {
    this.dialogClickListener = paramOnDialogClickListener;
  }

  public void setupViews()
  {
    this.leftBtn = ((Button)findViewById(R.id.left_btn));
    this.leftBtn.setOnClickListener(this);
    this.rightBtn = ((Button)findViewById(R.id.right_btn));
    this.rightBtn.setOnClickListener(this);
    this.titleView = ((TextView)findViewById(R.id.dlg_title));
    this.contentView = ((TextView)findViewById(R.id.dlg_content));
    if (!TextUtils.isEmpty(this.title))
    {
      this.titleView.setText(this.title);
      this.titleView.setVisibility(0);
      if (TextUtils.isEmpty(this.content))
        break label185;
      this.contentView.setText(this.content);
      this.contentView.setVisibility(0);
    }
    while (true)
    {
      if (!TextUtils.isEmpty(this.leftBtnMsg))
        this.leftBtn.setText(this.leftBtnMsg);
      if (!TextUtils.isEmpty(this.rightBtnMsg))
        this.rightBtn.setText(this.rightBtnMsg);
      return;
      this.titleView.setVisibility(8);
      break;
      label185: this.contentView.setVisibility(8);
    }
  }

  public static abstract interface OnDialogClickListener
  {
    public abstract void onCancelClick(Dialog paramDialog);

    public abstract void onConfirmClick(Dialog paramDialog);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.view.SelectDishAlertDialog
 * JD-Core Version:    0.6.0
 */
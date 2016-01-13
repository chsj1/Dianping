package com.dianping.takeaway.view;

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

public class TAAlertDialog extends Dialog
  implements View.OnClickListener
{
  private String content;
  private TextView contentView;
  private Button leftBtn;
  private String leftBtnMsg;
  OnDialogOperationListener listener;
  private Button rightBtn;
  private String rightBtnMsg;
  private String title;
  private TextView titleView;

  public TAAlertDialog(Context paramContext)
  {
    super(paramContext, R.style.FullScreenDialog);
  }

  public TAAlertDialog(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    super(paramContext, R.style.FullScreenDialog);
    this.title = paramString1;
    this.content = paramString2;
    this.leftBtnMsg = paramString3;
    this.rightBtnMsg = paramString4;
  }

  public OnDialogOperationListener getListener()
  {
    return this.listener;
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.left_btn)
    {
      if (this.listener != null)
        this.listener.cancel();
      cancel();
    }
    do
      return;
    while (i != R.id.right_btn);
    if (this.listener != null)
      this.listener.confirm(0);
    cancel();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.takeaway_address_delete_dialog);
    super.getWindow().setGravity(17);
    setupViews();
  }

  public void setListener(OnDialogOperationListener paramOnDialogOperationListener)
  {
    this.listener = paramOnDialogOperationListener;
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
      this.titleView.setText(this.title);
    if (!TextUtils.isEmpty(this.content))
      this.contentView.setText(this.content);
    if (!TextUtils.isEmpty(this.leftBtnMsg))
      this.leftBtn.setText(this.leftBtnMsg);
    if (!TextUtils.isEmpty(this.rightBtnMsg))
      this.rightBtn.setText(this.rightBtnMsg);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.view.TAAlertDialog
 * JD-Core Version:    0.6.0
 */
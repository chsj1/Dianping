package com.dianping.base.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;

public class BeautifulProgressDialog extends AlertDialog
  implements View.OnClickListener
{
  private ImageView mBtnCancel;
  private View mContent;
  private View mDivider;
  private TextView mMessageText;
  private CharSequence mMesssage;

  public BeautifulProgressDialog(Context paramContext)
  {
    super(paramContext, R.style.dialog_fullscreen);
  }

  public BeautifulProgressDialog(Context paramContext, int paramInt)
  {
    super(paramContext, R.style.dialog_fullscreen);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.btn_cancel)
      cancel();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setupView();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
      return super.onKeyDown(paramInt, paramKeyEvent);
    return false;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!ViewUtils.isPointInsideView(paramMotionEvent.getX(), paramMotionEvent.getY(), this.mContent))
      return true;
    return super.onTouchEvent(paramMotionEvent);
  }

  public void setCancelable(boolean paramBoolean)
  {
    super.setCancelable(paramBoolean);
    if (paramBoolean)
    {
      this.mDivider.setVisibility(0);
      this.mBtnCancel.setVisibility(0);
      return;
    }
    this.mDivider.setVisibility(8);
    this.mBtnCancel.setVisibility(8);
  }

  public void setMessage(CharSequence paramCharSequence)
  {
    super.setMessage(paramCharSequence);
    this.mMesssage = paramCharSequence;
  }

  protected void setupView()
  {
    super.setContentView(R.layout.beautiful_progress_dialog);
    this.mDivider = findViewById(R.id.divider);
    this.mContent = findViewById(R.id.content);
    this.mMessageText = ((TextView)findViewById(R.id.message));
    this.mMessageText.setText(this.mMesssage);
    this.mBtnCancel = ((ImageView)findViewById(R.id.btn_cancel));
    this.mBtnCancel.setOnClickListener(this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.BeautifulProgressDialog
 * JD-Core Version:    0.6.0
 */
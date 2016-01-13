package com.dianping.ugc.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.base.ugc.widget.SoftKeyboardStateHelper;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.regex.Pattern;

public class CommentInputView extends NovaRelativeLayout
  implements View.OnClickListener, GestureDetector.OnGestureListener, View.OnTouchListener
{
  public static final int COMMENTLAYOUTHEIGTHDP = 48;
  private static final int MAX_NUM_ADD_COMMENT = 600;
  private static final String TAG = "CommentInputView";
  private boolean enableRemoveIsSelf;
  private EditText mCommentEditText;
  private OnCommentSendListener mCommentSendListener;
  private GestureDetector mGestureDetector;
  private OnViewRemovedListener mOnViewRemovedListener;

  public CommentInputView(Context paramContext)
  {
    super(paramContext);
    initViews();
  }

  public CommentInputView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initViews();
  }

  private void hideKeyboard()
  {
    if (this.mCommentEditText != null)
      KeyboardUtils.hideKeyboard(this.mCommentEditText);
  }

  private void initViews()
  {
    setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    localLinearLayout.setOrientation(0);
    localLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_feed_comment_input));
    localLinearLayout.setPadding(0, ViewUtils.dip2px(getContext(), 9.0F), 0, ViewUtils.dip2px(getContext(), 8.0F));
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 48.0F));
    localLayoutParams.addRule(12);
    this.mCommentEditText = new EditText(getContext());
    this.mCommentEditText.setOnClickListener(new CommentInputView.1(this));
    this.mCommentEditText.setBackgroundDrawable(getResources().getDrawable(R.drawable.ugc_feed_comment_input_bg));
    Object localObject = Pattern.compile("[\\u4e00-\\u9fa5]");
    this.mCommentEditText.setFilters(new InputFilter[] { new CommentInputView.2(this, (Pattern)localObject) });
    this.mCommentEditText.setPadding(ViewUtils.dip2px(getContext(), 10.0F), 0, 0, 1);
    this.mCommentEditText.setLineSpacing(4.0F, 0.9F);
    localObject = new LinearLayout.LayoutParams(0, -1);
    ((LinearLayout.LayoutParams)localObject).weight = 1.0F;
    ((LinearLayout.LayoutParams)localObject).leftMargin = ViewUtils.dip2px(getContext(), 10.0F);
    localLinearLayout.addView(this.mCommentEditText, (ViewGroup.LayoutParams)localObject);
    localObject = new TextView(getContext());
    LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-2, -1);
    localLayoutParams1.gravity = 16;
    localLayoutParams1.leftMargin = ViewUtils.dip2px(getContext(), 20.0F);
    localLayoutParams1.rightMargin = ViewUtils.dip2px(getContext(), 20.0F);
    ((TextView)localObject).setGravity(17);
    ((TextView)localObject).setText("发送");
    ((TextView)localObject).setTextColor(getResources().getColor(R.color.light_gray));
    ((TextView)localObject).setTextSize(0, getResources().getDimension(R.dimen.text_size_15));
    ((TextView)localObject).setOnClickListener(new CommentInputView.3(this));
    localLinearLayout.addView((View)localObject, localLayoutParams1);
    this.mCommentEditText.addTextChangedListener(new CommentInputView.4(this, (TextView)localObject));
    addView(localLinearLayout, localLayoutParams);
    setOnClickListener(null);
    setClickable(false);
    this.mGestureDetector = new GestureDetector(this);
  }

  private void removeSelfView()
  {
    hideKeyboard();
    if (getParent() != null)
    {
      ((ViewGroup)getParent()).removeView(this);
      if (this.mOnViewRemovedListener != null)
        this.mOnViewRemovedListener.OnViewRemoved();
    }
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if ((this.mCommentEditText != null) && (this.enableRemoveIsSelf))
    {
      KeyboardUtils.popupKeyboard(this.mCommentEditText);
      setOnClickListener(this);
      setClickable(true);
    }
    if (getParent() != null)
      new SoftKeyboardStateHelper((ViewGroup)getParent()).addSoftKeyboardStateListener(new CommentInputView.5(this));
  }

  public void onClick(View paramView)
  {
    if (this.enableRemoveIsSelf)
    {
      removeSelfView();
      return;
    }
    hideKeyboard();
  }

  public boolean onDown(MotionEvent paramMotionEvent)
  {
    return false;
  }

  public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    if ((Math.abs(paramMotionEvent1.getX() - paramMotionEvent2.getX()) > 0.0F) || (Math.abs(paramMotionEvent1.getY() - paramMotionEvent2.getY()) > 0.0F))
    {
      if (!this.enableRemoveIsSelf)
        break label47;
      removeSelfView();
    }
    while (true)
    {
      return false;
      label47: hideKeyboard();
    }
  }

  public void onLongPress(MotionEvent paramMotionEvent)
  {
  }

  public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    return false;
  }

  public void onShowPress(MotionEvent paramMotionEvent)
  {
  }

  public boolean onSingleTapUp(MotionEvent paramMotionEvent)
  {
    return false;
  }

  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    return this.mGestureDetector.onTouchEvent(paramMotionEvent);
  }

  public void sendCommentSuccess()
  {
    if ((this.mCommentSendListener != null) && (this.mCommentEditText != null) && (!TextUtils.isEmpty(this.mCommentEditText.getText().toString().trim())))
    {
      this.mCommentSendListener.onCommentSend(this.mCommentEditText.getText().toString());
      if (!this.enableRemoveIsSelf)
        break label72;
      removeSelfView();
    }
    while (true)
    {
      this.mCommentEditText.setText(null);
      return;
      label72: hideKeyboard();
    }
  }

  public void setCommentInputHint(String paramString)
  {
    if (this.mCommentEditText != null)
      this.mCommentEditText.setHint(paramString);
  }

  public void setEnableRemoveIsSelf(boolean paramBoolean)
  {
    this.enableRemoveIsSelf = paramBoolean;
    if (!paramBoolean)
    {
      setOnClickListener(null);
      setClickable(false);
    }
  }

  public void setOnCommentInputListener(OnCommentSendListener paramOnCommentSendListener)
  {
    this.mCommentSendListener = paramOnCommentSendListener;
  }

  public void setOnViewRemovedListener(OnViewRemovedListener paramOnViewRemovedListener)
  {
    this.mOnViewRemovedListener = paramOnViewRemovedListener;
  }

  public void setRequestFocus()
  {
    if (this.mCommentEditText != null)
    {
      this.mCommentEditText.setFocusable(true);
      this.mCommentEditText.setFocusableInTouchMode(true);
      this.mCommentEditText.requestFocus();
      KeyboardUtils.popupKeyboard(this.mCommentEditText);
    }
  }

  public static abstract interface OnCommentSendListener
  {
    public abstract void onCommentSend(String paramString);
  }

  public static abstract interface OnViewRemovedListener
  {
    public abstract void OnViewRemoved();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.widget.CommentInputView
 * JD-Core Version:    0.6.0
 */
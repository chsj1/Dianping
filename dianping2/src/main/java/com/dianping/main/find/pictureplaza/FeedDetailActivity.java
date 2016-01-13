package com.dianping.main.find.pictureplaza;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.TitleBar;

public class FeedDetailActivity extends AgentActivity
{
  private FeedCommentListAgent mCommentAgent;
  private FeedDetailFragment mFragment;

  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 0)
    {
      View localView = getCurrentFocus();
      if (this.mCommentAgent != null)
        this.mCommentAgent.handleRootViewTouchEvent(localView, paramMotionEvent);
    }
    return super.dispatchTouchEvent(paramMotionEvent);
  }

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new FeedDetailFragment();
    return this.mFragment;
  }

  public String getPageName()
  {
    if (getIntParam("isfromnotificationcenter", 0) == 1)
      return "moments_feed";
    return "moments_feed_comments";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onBackPressed()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
    if (localInputMethodManager != null)
      localInputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    super.onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getTitleBar().setTitle("");
  }

  public void setCommentAgent(FeedCommentListAgent paramFeedCommentListAgent)
  {
    this.mCommentAgent = paramFeedCommentListAgent;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.FeedDetailActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.main.guide.guidance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.widget.Flipper;
import com.dianping.base.widget.FlipperAdapter;
import com.dianping.main.guide.MainActivity;
import com.dianping.util.Log;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class GuidanceActivity extends DPActivity
{
  protected boolean isMainLaunched;
  int[] mBackgrounds = GuidanceDrawable.backgroundImages;
  protected GuidanceFlipper mFlipper;
  int[] mForgrounds = GuidanceDrawable.foregroundImages;
  protected ImageAdapter mImageAdapter;
  protected int mViewCount;

  public String getPageName()
  {
    return "guidance";
  }

  protected void gotoMainActivity()
  {
    this.isMainLaunched = true;
    int i = Environment.versionCode();
    Object localObject = preferences().edit();
    ((SharedPreferences.Editor)localObject).putInt("guidanceShowVersion", i);
    ((SharedPreferences.Editor)localObject).commit();
    localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://home"));
    try
    {
      startActivity((Intent)localObject);
      finish();
      return;
    }
    catch (Exception localException)
    {
      while (true)
      {
        startActivity(new Intent(this, MainActivity.class));
        finish();
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    DPApplication.instance().getSharedPreferences("cx", 0).edit().putLong("firstLaunchTime", System.currentTimeMillis()).commit();
    this.mFlipper = new GuidanceFlipper(this);
    this.mFlipper.setBackgroundResource(R.drawable.guidance_background);
    setupView();
    this.mFlipper.enableNavigationDotView(this.mViewCount);
    if (this.mImageAdapter != null)
      this.mFlipper.setAdapter(this.mImageAdapter);
    super.setContentView(this.mFlipper);
    this.mFlipper.setCurrentItem(Integer.valueOf(0));
    this.mFlipper.update();
    this.isMainLaunched = false;
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    return true;
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      gotoMainActivity();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    return true;
  }

  public void setupView()
  {
    this.mViewCount = this.mBackgrounds.length;
    this.mImageAdapter = new ImageAdapter(this);
  }

  class GuidanceFlipper extends Flipper<Integer>
  {
    public GuidanceFlipper(Context arg2)
    {
      super();
    }

    protected void onScrollX(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat)
    {
      if ((paramFloat < 0.0F) && (this.previousView == null) && (paramMotionEvent2.getX() - paramMotionEvent1.getX() > flipDistance() + paramFloat));
      while (true)
      {
        return;
        if ((paramFloat <= 0.0F) || (this.nextView != null) || (paramMotionEvent2.getX() - paramMotionEvent1.getX() >= flipDistance() + paramFloat))
          break;
        if (GuidanceActivity.this.isMainLaunched)
          continue;
        GuidanceActivity.this.gotoMainActivity();
        GuidanceActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        return;
      }
      super.onScrollX(paramMotionEvent1, paramMotionEvent2, paramFloat);
    }
  }

  class ImageAdapter
    implements FlipperAdapter<Integer>
  {
    public ImageAdapter(Context arg2)
    {
    }

    public Integer getNextItem(Integer paramInteger)
    {
      if (paramInteger.intValue() == GuidanceActivity.this.mViewCount)
      {
        GuidanceActivity.this.startActivity("dianping://home");
        GuidanceActivity.this.finish();
      }
      if (paramInteger.intValue() + 1 < GuidanceActivity.this.mViewCount)
        return Integer.valueOf(paramInteger.intValue() + 1);
      return null;
    }

    public Integer getPreviousItem(Integer paramInteger)
    {
      if (paramInteger.intValue() > 0)
        return Integer.valueOf(paramInteger.intValue() - 1);
      return null;
    }

    public View getView(Integer paramInteger, View paramView)
    {
      if ((paramInteger == null) || (paramInteger.intValue() < 0))
        return null;
      if (paramView == null)
        paramView = GuidanceActivity.this.getLayoutInflater().inflate(R.layout.guidance, null);
      try
      {
        while (true)
        {
          ((ImageView)paramView.findViewById(R.id.iv_bg)).setImageResource(GuidanceActivity.this.mBackgrounds[paramInteger.intValue()]);
          ((ImageView)paramView.findViewById(R.id.iv_fg)).setImageResource(GuidanceActivity.this.mForgrounds[paramInteger.intValue()]);
          ImageView localImageView = (ImageView)paramView.findViewById(R.id.iv_skip);
          localImageView.setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              GuidanceActivity.this.gotoMainActivity();
            }
          });
          localButton = (Button)paramView.findViewById(R.id.guidance_skip);
          localButton.setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              GuidanceActivity.this.gotoMainActivity();
            }
          });
          if (paramInteger.intValue() == GuidanceActivity.this.mBackgrounds.length - 1)
            break;
          localImageView.setVisibility(8);
          localButton.setVisibility(0);
          return paramView;
        }
      }
      catch (Throwable localThrowable)
      {
        Button localButton;
        while (true)
          Log.e(localThrowable.toString());
        localThrowable.setVisibility(0);
        localButton.setVisibility(8);
      }
      return paramView;
    }

    public void onMoved(Integer paramInteger1, Integer paramInteger2)
    {
    }

    public void onMoving(Integer paramInteger1, Integer paramInteger2)
    {
    }

    public void onTap(Integer paramInteger)
    {
      if (paramInteger.intValue() < GuidanceActivity.this.mViewCount - 1)
      {
        GuidanceActivity.this.mFlipper.moveToNext(true);
        return;
      }
      GuidanceActivity.this.gotoMainActivity();
    }

    public void recycleView(View paramView)
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.guide.guidance.GuidanceActivity
 * JD-Core Version:    0.6.0
 */
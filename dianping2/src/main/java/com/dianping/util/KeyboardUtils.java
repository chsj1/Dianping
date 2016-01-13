package com.dianping.util;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import java.lang.reflect.Method;

public class KeyboardUtils
{
  public static boolean beforeCupcake()
  {
    return ("2".equals(Build.VERSION.SDK)) || ("1".equals(Build.VERSION.SDK));
  }

  public static SoftKeyboardController getSoftKeyboardController(View paramView)
  {
    return new SoftKeyboardHandler(paramView);
  }

  public static void hideKeyboard(View paramView)
  {
    ((InputMethodManager)paramView.getContext().getSystemService("input_method")).hideSoftInputFromWindow(paramView.getWindowToken(), 2);
  }

  public static void popupKeyboard(View paramView)
  {
    new Handler(paramView)
    {
      public void handleMessage(Message paramMessage)
      {
        switch (paramMessage.what)
        {
        default:
        case 1:
        }
        while (true)
        {
          super.handleMessage(paramMessage);
          return;
          this.val$view.requestFocus();
          ((InputMethodManager)this.val$view.getContext().getSystemService("input_method")).toggleSoftInput(0, 2);
        }
      }
    }
    .sendEmptyMessageDelayed(1, 300L);
  }

  public static abstract interface SoftKeyboardController
  {
    public abstract void hide();

    public abstract void show();
  }

  private static class SoftKeyboardHandler extends Handler
    implements KeyboardUtils.SoftKeyboardController
  {
    final boolean beforeCupcake = KeyboardUtils.beforeCupcake();
    int count = 0;
    final View view;

    public SoftKeyboardHandler(View paramView)
    {
      this.view = paramView;
    }

    public void handleMessage(Message paramMessage)
    {
      if (this.beforeCupcake);
      while (true)
      {
        return;
        switch (paramMessage.what)
        {
        default:
          return;
        case 1:
          this.count = 0;
          removeMessages(2);
          removeMessages(4);
          sendEmptyMessageDelayed(2, 200L);
          return;
        case 2:
          if (this.view.isShown());
          try
          {
            paramMessage = this.view.getContext().getSystemService("input_method");
            paramMessage.getClass().getDeclaredMethod("showSoftInput", new Class[] { View.class, Integer.TYPE }).invoke(paramMessage, new Object[] { this.view, Integer.valueOf(1) });
            int i = this.count;
            this.count = (i + 1);
            if (i > 10)
              continue;
            boolean bool = ((Boolean)paramMessage.getClass().getDeclaredMethod("isActive", new Class[0]).invoke(paramMessage, new Object[0])).booleanValue();
            if (bool)
              continue;
            sendEmptyMessageDelayed(2, 100L);
            return;
          }
          catch (Exception paramMessage)
          {
            paramMessage.printStackTrace();
            return;
          }
        case 3:
        case 4:
        }
      }
      this.count = 0;
      removeMessages(2);
      removeMessages(4);
      sendEmptyMessageDelayed(4, 200L);
      return;
      try
      {
        paramMessage = this.view.getContext().getSystemService("input_method");
        paramMessage.getClass().getDeclaredMethod("hideSoftInputFromWindow", new Class[] { IBinder.class, Integer.TYPE }).invoke(paramMessage, new Object[] { this.view.getWindowToken(), Integer.valueOf(2) });
        return;
      }
      catch (Exception paramMessage)
      {
        paramMessage.printStackTrace();
      }
    }

    public void hide()
    {
      sendEmptyMessage(3);
    }

    public void show()
    {
      sendEmptyMessage(1);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.KeyboardUtils
 * JD-Core Version:    0.6.0
 */
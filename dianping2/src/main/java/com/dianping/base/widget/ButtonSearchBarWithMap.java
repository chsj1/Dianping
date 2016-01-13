package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ButtonSearchBarWithMap extends LinearLayout
{
  private ButtonSearchBar btnSearchBar;
  private ImageView gotoMap;
  GotoMapListener mGotoMapListener;

  public ButtonSearchBarWithMap(Context paramContext)
  {
    this(paramContext, null);
  }

  public ButtonSearchBarWithMap(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LayoutInflater.from(paramContext).inflate(R.layout.button_search_bar_with_map, this, true);
    setBackgroundColor(-1);
    setGravity(16);
    setOrientation(0);
    this.btnSearchBar = ((ButtonSearchBar)findViewById(R.id.button_search_bar));
    this.gotoMap = ((ImageView)findViewById(R.id.goto_map));
    this.gotoMap.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (ButtonSearchBarWithMap.this.mGotoMapListener != null)
          ButtonSearchBarWithMap.this.mGotoMapListener.onGotoMap(paramView);
      }
    });
  }

  public void setButtonSearchBarListener(ButtonSearchBar.ButtonSearchBarListener paramButtonSearchBarListener)
  {
    this.btnSearchBar.setButtonSearchBarListener(paramButtonSearchBarListener);
  }

  public void setGotoMapListener(GotoMapListener paramGotoMapListener)
  {
    this.mGotoMapListener = paramGotoMapListener;
  }

  public void setHint(int paramInt)
  {
    if (this.btnSearchBar != null)
      this.btnSearchBar.setHint(paramInt);
  }

  public void setHint(String paramString)
  {
    if (this.btnSearchBar != null)
      this.btnSearchBar.setHint(paramString);
  }

  public void setKeyword(String paramString)
  {
    if (this.btnSearchBar != null)
      this.btnSearchBar.setKeyword(paramString);
  }

  public static abstract interface GotoMapListener
  {
    public abstract void onGotoMap(View paramView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ButtonSearchBarWithMap
 * JD-Core Version:    0.6.0
 */
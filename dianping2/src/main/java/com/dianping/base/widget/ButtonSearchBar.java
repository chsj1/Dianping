package com.dianping.base.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.NovaLinearLayout;

public class ButtonSearchBar extends NovaLinearLayout
  implements View.OnClickListener
{
  private static final String TAG = ButtonSearchBar.class.getSimpleName();
  private TextView btnSearch;
  private ImageView iconSearch;
  private ButtonSearchBarListener mListener;

  public ButtonSearchBar(Context paramContext)
  {
    super(paramContext);
  }

  public ButtonSearchBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LayoutInflater.from(paramContext).inflate(R.layout.button_search_bar_inner, this, true);
    setId(R.id.button_search_bar);
    int j = paramAttributeSet.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", -1);
    int i = j;
    if (j == -1)
      i = R.drawable.search_bar_button_background;
    setBackgroundResource(i);
    setGravity(19);
    setOrientation(0);
    this.btnSearch = ((TextView)findViewById(R.id.start_search));
    this.iconSearch = ((ImageView)findViewById(R.id.search_icon));
    setOnClickListener(this);
  }

  public static Intent getResultIntent(Bundle paramBundle, String paramString1, String paramString2)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://searchshoplist"));
    if (paramString1 != null)
    {
      localIntent.putExtra("query", paramString1);
      localIntent.putExtra("keyword", paramString1);
      localIntent.putExtra("suggest_text_1", paramString1);
    }
    if (paramString2 != null)
      localIntent.putExtra("suggest_text_2", paramString2);
    if (paramBundle != null)
      localIntent.putExtra("app_data", paramBundle);
    return localIntent;
  }

  public ImageView getSearchIconView()
  {
    return this.iconSearch;
  }

  public TextView getSearchTextView()
  {
    return this.btnSearch;
  }

  public void onClick(View paramView)
  {
    if (this.mListener != null)
      post(new Runnable()
      {
        public void run()
        {
          ButtonSearchBar.this.mListener.onSearchRequested();
        }
      });
  }

  public void setButtonSearchBarListener(ButtonSearchBarListener paramButtonSearchBarListener)
  {
    this.mListener = paramButtonSearchBarListener;
  }

  public void setHint(int paramInt)
  {
    if (this.btnSearch == null)
      return;
    if (paramInt > 0)
      try
      {
        this.btnSearch.setHint(paramInt);
        return;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        return;
      }
    this.btnSearch.setHint(R.string.search_hint);
  }

  public void setHint(String paramString)
  {
    if (this.btnSearch != null)
      this.btnSearch.setHint(paramString);
  }

  public void setKeyword(String paramString)
  {
    if (this.btnSearch != null)
      this.btnSearch.setText(paramString);
  }

  public static abstract interface ButtonSearchBarListener
  {
    public abstract void onSearchRequested();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ButtonSearchBar
 * JD-Core Version:    0.6.0
 */
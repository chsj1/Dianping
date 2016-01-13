package com.dianping.movie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.base.web.ui.NovaZeusActivity;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.movie.fragment.SelectSeatWebFragment;

public class WebMovieSeatActivity extends NovaZeusActivity
{
  public static final int REQUEST_CODE_SUBMIT_ORDER = 100;
  private String title = "";

  protected Class<? extends NovaZeusFragment> getEfteFragmentClass()
  {
    return SelectSeatWebFragment.class;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.title = getIntent().getStringExtra("title");
    if (TextUtils.isEmpty(this.title))
      this.title = "正在载入";
    setTitle(this.title);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.WebMovieSeatActivity
 * JD-Core Version:    0.6.0
 */
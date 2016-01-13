package com.dianping.movie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.base.app.NovaActivity;
import com.dianping.util.Log;
import java.net.URISyntaxException;

public class MovieBaseActivity extends NovaActivity
{
  public String from;

  public void finish()
  {
    String str = getIntent().getStringExtra("next_redirect_");
    if (!TextUtils.isEmpty(str));
    try
    {
      startActivity(Intent.parseUri(str, 1));
      super.finish();
      return;
    }
    catch (URISyntaxException localURISyntaxException)
    {
      while (true)
        Log.e("Activity finish ", localURISyntaxException.getLocalizedMessage());
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.from = getStringParam("from");
    if (this.from == null)
      this.from = "";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieBaseActivity
 * JD-Core Version:    0.6.0
 */
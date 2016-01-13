package com.dianping.map.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.EditSearchBar;
import com.dianping.base.widget.EditSearchBar.OnKeywordChangeListner;
import com.dianping.base.widget.TitleBar;
import com.dianping.map.fragment.GooglePlacesSearchFragment;
import com.dianping.map.fragment.GooglePlacesSearchFragment.OnResultItemClickListner;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class GooglePlacesSearchActivity extends NovaActivity
  implements GooglePlacesSearchFragment.OnResultItemClickListner, EditSearchBar.OnKeywordChangeListner
{
  private static final String LOG_TAG = GooglePlacesSearchActivity.class.getSimpleName();
  private boolean doSearch;
  private GooglePlacesSearchFragment mGooglePlacesSearchFragment;

  protected TitleBar initCustomTitle()
  {
    if (this.doSearch)
      return super.initCustomTitle();
    return TitleBar.build(this, 4);
  }

  public void onCreate(Bundle paramBundle)
  {
    Uri localUri = getIntent().getData();
    this.doSearch = false;
    if (localUri != null)
    {
      if (localUri.getQueryParameter("keyword") != null)
        this.doSearch = true;
      super.onCreate(paramBundle);
      super.setContentView(R.layout.google_places_search);
      this.mGooglePlacesSearchFragment = ((GooglePlacesSearchFragment)getSupportFragmentManager().findFragmentById(R.id.google_places_search_list_fragment));
      this.mGooglePlacesSearchFragment.setOnResultItemClickListner(this);
      this.mGooglePlacesSearchFragment.setGaAction(localUri.getQueryParameter("gaaction"));
      if (this.doSearch)
      {
        paramBundle = (TextView)findViewById(16908310);
        if (paramBundle != null)
          paramBundle.setText(localUri.getQueryParameter("keyword"));
        return;
      }
    }
    else
    {
      Log.e(LOG_TAG, "can't start this activity because of the uri param is null");
      finish();
      return;
    }
    paramBundle = (EditSearchBar)findViewById(R.id.search_bar);
    ((EditText)paramBundle.findViewById(R.id.search_edit)).setImeOptions(6);
    paramBundle.setOnKeywordChangeListner(this);
  }

  public void onKeywordChanged(String paramString)
  {
    this.mGooglePlacesSearchFragment.onKeywordChanged(paramString);
  }

  public void onResultItemClick(Intent paramIntent)
  {
    setResult(-1, paramIntent);
    finish();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.activity.GooglePlacesSearchActivity
 * JD-Core Version:    0.6.0
 */
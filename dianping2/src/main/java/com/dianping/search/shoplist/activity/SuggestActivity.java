package com.dianping.search.shoplist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.AbstractSearchFragment;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.search.shoplist.fragment.GetAroundSuggestFragment;

public class SuggestActivity extends NovaActivity
  implements AbstractSearchFragment.OnSearchFragmentListener
{
  AbstractSearchFragment suggestFragment;

  public String getPageName()
  {
    return "suggest";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    hideTitleBar();
    this.suggestFragment = GetAroundSuggestFragment.newInstance(this);
    this.suggestFragment.setOnSearchFragmentListener(this);
  }

  public void onSearchFragmentDetach()
  {
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    Object localObject = paramDPObject.getString("Url");
    if (!TextUtils.isEmpty((CharSequence)localObject))
      startActivity((String)localObject);
    while (true)
    {
      finish();
      return;
      localObject = new Intent("android.intent.action.VIEW", this.suggestFragment.buildUri(paramDPObject));
      paramDPObject = paramDPObject.getString("Value");
      if (!TextUtils.isEmpty(paramDPObject))
        ((Intent)localObject).putExtra("value", paramDPObject);
      startActivity((Intent)localObject);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.activity.SuggestActivity
 * JD-Core Version:    0.6.0
 */
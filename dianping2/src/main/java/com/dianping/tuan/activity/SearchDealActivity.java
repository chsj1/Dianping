package com.dianping.tuan.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.tuan.fragment.TuanSearchFragment;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class SearchDealActivity extends NovaActivity
  implements AbstractSearchFragment.OnSearchFragmentListener
{
  DPObject categoryForPopularSuggest;
  String channel;
  String fromwhere;
  String key;
  DPObject regionForPopularSuggest;
  String screening;
  DPObject sortForPopularSuggest;

  protected ArrayList<NameValuePair> getStatisticExtra()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("from", this.fromwhere));
    return localArrayList;
  }

  protected void handleIntent()
  {
    Intent localIntent = getIntent();
    if (localIntent == null);
    Bundle localBundle;
    do
    {
      return;
      localBundle = localIntent.getExtras();
    }
    while (localBundle == null);
    this.categoryForPopularSuggest = ((DPObject)localBundle.getParcelable("categoryforpopularsuggest"));
    this.regionForPopularSuggest = ((DPObject)localBundle.getParcelable("regionforpopularsuggest"));
    this.sortForPopularSuggest = ((DPObject)localBundle.getParcelable("sortforpopularsuggest"));
    this.screening = localBundle.getString("screeningforpopularsuggest");
    this.fromwhere = localBundle.getString("fromwhere");
    this.channel = localBundle.getString("channel");
    this.key = localIntent.getData().getQueryParameter("keyword");
  }

  public void onBackPressed()
  {
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    handleIntent();
    paramBundle = TuanSearchFragment.createAndAdd(this, this.categoryForPopularSuggest, this.channel, false);
    paramBundle.setKeyword(this.key);
    paramBundle.setOnSearchFragmentListener(this);
    setTitleVisibility(8);
  }

  public void onSearchFragmentDetach()
  {
  }

  public void startSearch(DPObject paramDPObject)
  {
    finish();
    Object localObject = paramDPObject.getString("Url");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      startActivity((String)localObject);
      return;
    }
    localObject = Uri.parse("dianping://deallist").buildUpon();
    String str = paramDPObject.getString("Keyword");
    if (!TextUtils.isEmpty(str))
      ((Uri.Builder)localObject).appendQueryParameter("keyword", str);
    localObject = new Intent("android.intent.action.VIEW", Uri.parse(((Uri.Builder)localObject).toString()));
    paramDPObject = paramDPObject.getString("Value");
    if (!TextUtils.isEmpty(paramDPObject))
      ((Intent)localObject).putExtra("value", paramDPObject);
    ((Intent)localObject).putExtra("fromwhere", this.fromwhere);
    ((Intent)localObject).putExtra("categoryforpopularsuggest", this.categoryForPopularSuggest);
    ((Intent)localObject).putExtra("regionforpopularsuggest", this.regionForPopularSuggest);
    ((Intent)localObject).putExtra("sortforpopularsuggest", this.sortForPopularSuggest);
    ((Intent)localObject).putExtra("screeningforpopularsuggest", this.screening);
    startActivity((Intent)localObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.SearchDealActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.membercard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.base.widget.ButtonSearchBar.ButtonSearchBarListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.membercard.fragment.AvailableCardListFragment;
import com.dianping.membercard.fragment.AvailableCardSearchFragment;
import com.dianping.membercard.utils.MCIntentUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;

public class AvailableCardListActivity extends NovaActivity
  implements AbstractSearchFragment.OnSearchFragmentListener
{
  private AvailableCardListFragment availableCardListFragment = null;
  private AvailableCardSearchFragment mAvailableCardSearchFragment;
  private ButtonSearchBar mButtonSearcBar;
  private String mKeyword;
  private int openSearch = 0;

  private void gotoSearchIfNeed()
  {
    if (this.openSearch > 0)
    {
      gotoSearch();
      this.openSearch = 0;
    }
  }

  private void setupTitleBarLayout()
  {
    if (this.mKeyword == null)
      getTitleBar().addRightViewItem(AddedThirdPartyCardActivity.class.getSimpleName(), R.drawable.navibar_icon_search, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          AvailableCardListActivity.access$002(AvailableCardListActivity.this, AvailableCardSearchFragment.newInstance(AvailableCardListActivity.this));
          AvailableCardListActivity.this.mAvailableCardSearchFragment.setOnSearchFragmentListener(AvailableCardListActivity.this);
          AvailableCardListActivity.this.setTitleVisibility(8);
        }
      });
    do
      return;
    while (this.mButtonSearcBar != null);
    this.mButtonSearcBar = ((ButtonSearchBar)findViewById(R.id.button_search_bar));
    this.mButtonSearcBar.setKeyword(this.mKeyword);
    this.mButtonSearcBar.setButtonSearchBarListener(new ButtonSearchBar.ButtonSearchBarListener()
    {
      public void onSearchRequested()
      {
        AvailableCardListActivity.access$002(AvailableCardListActivity.this, AvailableCardSearchFragment.newInstance(AvailableCardListActivity.this));
        AvailableCardListActivity.this.mAvailableCardSearchFragment.setOnSearchFragmentListener(AvailableCardListActivity.this);
        AvailableCardListActivity.this.mAvailableCardSearchFragment.setKeyword(AvailableCardListActivity.this.mKeyword);
        AvailableCardListActivity.this.setTitleVisibility(8);
      }
    });
  }

  public String getPageName()
  {
    return "availablecardlist";
  }

  public void gotoSearch()
  {
    this.mAvailableCardSearchFragment = AvailableCardSearchFragment.newInstance(this);
    this.mAvailableCardSearchFragment.setOnSearchFragmentListener(this);
    statisticsEvent("availablecard5", "availablecard5_keyword", null, 0);
  }

  protected TitleBar initCustomTitle()
  {
    if (this.mKeyword != null)
      return TitleBar.build(this, 5);
    return TitleBar.build(this, 100);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (this.availableCardListFragment != null)
      this.availableCardListFragment.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    this.mKeyword = getStringParam("keyword");
    super.onCreate(paramBundle);
    paramBundle = new FrameLayout(this);
    paramBundle.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    paramBundle.setId(16908300);
    paramBundle.setBackgroundResource(R.drawable.main_background);
    super.setContentView(paramBundle);
    if (!TextUtils.isEmpty(getStringParam("showsearch")))
      this.openSearch = Integer.parseInt(getStringParam("showsearch"));
    this.availableCardListFragment = new AvailableCardListFragment();
    this.availableCardListFragment.setArguments(MCIntentUtils.getBundle(getIntent()));
    getSupportFragmentManager().beginTransaction().add(16908300, this.availableCardListFragment).commit();
    setupTitleBarLayout();
  }

  public void onSearchFragmentDetach()
  {
    setTitleVisibility(0);
  }

  public void onStart()
  {
    super.onStart();
    gotoSearchIfNeed();
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    while (true)
    {
      return;
      paramDPObject = paramDPObject.getString("Keyword");
      if (this.mKeyword == null)
        break;
      if (paramDPObject.equals(this.mKeyword))
        continue;
      this.mKeyword = paramDPObject;
      this.availableCardListFragment.setKeyword(this.mKeyword);
      this.mButtonSearcBar.setKeyword(this.mKeyword);
      this.availableCardListFragment.reset();
      return;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse("dianping://availablecardlist?keyword=" + paramDPObject));
    localIntent.putExtra("keyword", paramDPObject);
    localIntent.putExtra("source", 14);
    startActivity(localIntent);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.AvailableCardListActivity
 * JD-Core Version:    0.6.0
 */
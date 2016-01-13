package com.dianping.main.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.dianping.app.CityConfig;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.basic.MainSearchFragment;
import com.dianping.base.util.RedAlertManager;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.base.widget.ButtonSearchBar.ButtonSearchBarListener;
import com.dianping.base.widget.MeasuredTextView;
import com.dianping.loader.MyResources;
import com.dianping.main.guide.MainActivity;
import com.dianping.main.guide.SkinManager;
import com.dianping.model.City;
import com.dianping.util.SearchUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.NovaImageButton;
import com.dianping.widget.view.NovaLinearLayout;

public class HomeTitleBarAgent extends HomeAgent
  implements AbstractSearchFragment.OnSearchFragmentListener
{
  static final String ACTION_RED_ALERTS = "com.dianping.action.RedAlerts";
  private static final String SPKEY_HAS_SHOWN_MAGIC_PLUS = "hasShownPopupWindow";
  private static final int TITLE_CITY_MAX_LENGTH = 4;
  public static int adapterTypeCount = 0;
  private View homeTitleBar;
  private TextView leftTitleBtn;
  private MeasuredTextView mMagicPlusDotTextView;
  private NovaImageButton mPopupImageButton;
  private ImageView popUpArrow;
  private HomePopUpMenu popUpMenu;
  BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ((HomeTitleBarAgent.this.getContext() == null) || (HomeTitleBarAgent.this.popUpMenu == null))
        return;
      if ("com.dianping.action.RedAlerts".equals(paramIntent.getAction()))
        HomeTitleBarAgent.this.popUpMenu.updateToReview(true);
      HomeTitleBarAgent.this.updatePlusSignRedAlert();
    }
  };
  private TextView rightTitleBtn;
  ButtonSearchBar searchBar;
  MainSearchFragment searchFragment;
  private SharedPreferences sharedPreferences;

  public HomeTitleBarAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initView()
  {
    String str2 = getCity().name();
    String str1 = str2;
    if (str2 != null)
    {
      str1 = str2;
      if (str2.length() > 4)
        str1 = str2.substring(0, 3) + "...";
    }
    setTitleBtn(this.leftTitleBtn, str1);
    setTitleBtn(this.rightTitleBtn, str1);
  }

  private void setTitleBtn(TextView paramTextView, String paramString)
  {
    if ((paramTextView != null) && (paramString != null))
    {
      if (paramString.length() < 4)
        break label38;
      paramTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_15));
    }
    while (true)
    {
      paramTextView.setText(paramString);
      return;
      label38: paramTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_16));
    }
  }

  private void updatePlusSignRedAlert()
  {
    if (RedAlertManager.getInstance().checkRedAlertByTag("me.toreview") != null)
    {
      this.mMagicPlusDotTextView.setText("");
      this.mMagicPlusDotTextView.setBackgroundResource(R.drawable.home_navibar_tips_reddot);
      this.mMagicPlusDotTextView.setWidth(ViewUtils.dip2px(getContext(), 12.0F));
      this.mMagicPlusDotTextView.setHeight(ViewUtils.dip2px(getContext(), 12.0F));
      this.mMagicPlusDotTextView.setVisibility(0);
      return;
    }
    this.mMagicPlusDotTextView.setVisibility(8);
  }

  public View createTitleBar()
  {
    this.homeTitleBar = this.res.inflate(getContext(), R.layout.main_home_title_bar, getParentView(), false);
    View localView = this.homeTitleBar.findViewById(R.id.btn_back_wx);
    localView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ((NovaActivity)HomeTitleBarAgent.this.getContext()).finish();
      }
    });
    this.leftTitleBtn = ((TextView)this.homeTitleBar.findViewById(R.id.city));
    this.rightTitleBtn = ((TextView)this.homeTitleBar.findViewById(R.id.right_title_button));
    this.rightTitleBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://switchcity"));
        if (HomeTitleBarAgent.this.getCity().isForeign());
        for (int i = 1; ; i = 0)
        {
          paramView.putExtra("area", i);
          HomeTitleBarAgent.this.startActivity(paramView);
          return;
        }
      }
    });
    this.leftTitleBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://switchcity"));
        if (HomeTitleBarAgent.this.getCity().isForeign());
        for (int i = 1; ; i = 0)
        {
          paramView.putExtra("area", i);
          HomeTitleBarAgent.this.startActivity(paramView);
          return;
        }
      }
    });
    if (((NovaApplication)getContext().getApplicationContext()).getStartType() == 1)
    {
      this.leftTitleBtn.setVisibility(8);
      localView.setVisibility(0);
      this.rightTitleBtn.setVisibility(0);
    }
    while (true)
    {
      this.mPopupImageButton = ((NovaImageButton)this.homeTitleBar.findViewById(R.id.notify));
      this.mPopupImageButton.setGAString("more");
      this.mPopupImageButton.gaUserInfo.index = Integer.valueOf(0);
      this.mPopupImageButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HomeTitleBarAgent.this.showPopUpArrow(true);
          HomeTitleBarAgent.this.popUpMenu.show(paramView);
          ((NovaLinearLayout)HomeTitleBarAgent.this.popUpMenu.getContentView()).setGAString("magicplus");
          ((DPActivity)HomeTitleBarAgent.this.getContext()).addGAView(HomeTitleBarAgent.this.popUpMenu.getContentView(), 0);
        }
      });
      SearchUtils.getSearchableInfo(getContext(), ((NovaActivity)getContext()).getComponentName());
      this.searchBar = ((ButtonSearchBar)this.homeTitleBar.findViewById(R.id.button_search_bar));
      this.searchBar.setGAString("homesearch");
      this.searchBar.setHint(R.string.default_search_hint);
      this.searchBar.setBackgroundResource(R.drawable.home_topbar_search);
      this.searchBar.getSearchTextView().setHintTextColor(getResources().getColor("my_home_light_gray"));
      this.searchBar.getSearchIconView().setImageResource(R.drawable.ic_home_search);
      this.searchBar.setButtonSearchBarListener(new ButtonSearchBar.ButtonSearchBarListener()
      {
        public void onSearchRequested()
        {
          HomeTitleBarAgent.this.searchFragment = MainSearchFragment.newInstance((NovaActivity)HomeTitleBarAgent.this.getContext());
          try
          {
            ((MainActivity)HomeTitleBarAgent.this.getContext()).registerSearchFragment();
            label33: HomeTitleBarAgent.this.searchFragment.setOnSearchFragmentListener(HomeTitleBarAgent.this);
            HomeTitleBarAgent.this.statisticsEvent("index5", "index5_keyword_click", "", 0);
            return;
          }
          catch (Exception localException)
          {
            break label33;
          }
        }
      });
      this.mMagicPlusDotTextView = ((MeasuredTextView)this.homeTitleBar.findViewById(R.id.findmain_mail_text));
      updatePlusSignRedAlert();
      this.popUpArrow = ((ImageView)this.homeTitleBar.findViewById(R.id.tips_arrow));
      this.popUpMenu = new HomePopUpMenu(getContext(), -2, -2);
      this.popUpMenu.setOnDismissListener(new PopupWindow.OnDismissListener()
      {
        public void onDismiss()
        {
          HomeTitleBarAgent.this.showPopUpArrow(false);
        }
      });
      initView();
      SkinManager.setTitleBarSkin(getContext(), this.homeTitleBar);
      return this.homeTitleBar;
      this.leftTitleBtn.setVisibility(0);
      localView.setVisibility(8);
      this.rightTitleBtn.setVisibility(8);
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt2 == -1) && (paramInt1 == 64256) && (paramIntent.getStringExtra("keyword").equals("清空搜索记录")))
      new SearchRecentSuggestions(getContext(), "com.dianping.app.DianpingSuggestionProvider", 3).clearHistory();
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    super.onCitySwitched(paramCity1, paramCity2);
    initView();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    DPApplication.instance().cityConfig().addListener(this);
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.action.RedAlerts");
    getContext().registerReceiver(this.receiver, paramBundle);
    if (this.sharedPreferences == null)
      this.sharedPreferences = getFragment().preferences(getContext());
    addCell("05TitleBar", createTitleBar());
  }

  public void onDestroy()
  {
    DPApplication.instance().cityConfig().removeListener(this);
    getContext().unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onSearchFragmentDetach()
  {
    try
    {
      ((MainActivity)getContext()).unregisterSearchFragment();
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public void showPopUpArrow(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.popUpArrow.setVisibility(0);
      return;
    }
    this.popUpArrow.setVisibility(8);
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    String str = paramDPObject.getString(getResources().getString(R.string.search_keyword_ga_suffix));
    int i = paramDPObject.getInt(getResources().getString(R.string.search_keyword_ga_position));
    Object localObject = "index5_keyword";
    if (!TextUtils.isEmpty(str))
      localObject = "index5_keyword" + str;
    statisticsEvent("index5", (String)localObject, paramDPObject.getString("Keyword"), i);
    localObject = paramDPObject.getString("Url");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)localObject)));
      return;
    }
    localObject = new Bundle();
    ((Bundle)localObject).putString("source", "com.dianping.action.FIND");
    localObject = ButtonSearchBar.getResultIntent((Bundle)localObject, paramDPObject.getString("Keyword"), String.valueOf(paramDPObject.getInt("Count")));
    paramDPObject = paramDPObject.getString("Value");
    if (!TextUtils.isEmpty(paramDPObject))
      ((Intent)localObject).putExtra("value", paramDPObject);
    startActivity((Intent)localObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeTitleBarAgent
 * JD-Core Version:    0.6.0
 */
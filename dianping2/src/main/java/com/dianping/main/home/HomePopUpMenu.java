package com.dianping.main.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.base.util.RedAlertManager;
import com.dianping.base.widget.MeasuredTextView;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;

public class HomePopUpMenu extends PopupWindow
  implements View.OnClickListener, LoginResultListener
{
  private static final String TAG = HomePopUpMenu.class.getSimpleName();
  private static final String TITLE_ADDSHOP = "添加商户";
  private static final int TITLE_ADDSHOP_INDEX = 1;
  private static final String TITLE_PAY = "付款码";
  private static final String TITLE_SCAN = "扫一扫";
  private static final String TITLE_TOREVIEW = "写点评";
  private static final int TITLE_TOREVIEW_INDEX = 0;
  private static View mLoginBackView;
  private Context mContext;
  private HomePopUpMenuAdapter mHomePopupAdapter;
  private ListView mListView;
  private ArrayList<HomePopUpMenuItem> mMenuItems = new ArrayList();
  private int mScreenWidth;

  public HomePopUpMenu(Context paramContext, int paramInt1, int paramInt2)
  {
    super(paramContext);
    this.mContext = paramContext;
    setFocusable(true);
    setTouchable(true);
    setOutsideTouchable(true);
    this.mScreenWidth = ViewUtils.getScreenWidthPixels(this.mContext);
    setWidth(paramInt1);
    setHeight(paramInt2);
    setBackgroundDrawable(new BitmapDrawable());
    setContentView(LayoutInflater.from(this.mContext).inflate(R.layout.main_home_popup_menu, null));
    initData();
    initUI();
  }

  private View getLoginBackView()
  {
    return mLoginBackView;
  }

  private void gotoLogin(View paramView)
  {
    try
    {
      ((DPActivity)this.mContext).accountService().login(this);
      setLoginBackView(paramView);
      return;
    }
    catch (Exception paramView)
    {
      Log.v(TAG, "gotoLogin exception " + paramView.getMessage());
    }
  }

  private void initData()
  {
    this.mMenuItems.add(new HomePopUpMenuItem("写点评", R.drawable.home_navibar_tips_icon_comment, "dianping://recommenddealreview", "comment"));
    this.mMenuItems.add(new HomePopUpMenuItem("添加商户", R.drawable.home_navibar_tips_icon_store, "dianping://web?url=http://m.dianping.com/poi/app/shop/addShop?newtoken=", "shop"));
    this.mMenuItems.add(new HomePopUpMenuItem("扫一扫", R.drawable.home_navibar_tips_icon_scan, "dianping://barcodescan", "code"));
    this.mMenuItems.add(new HomePopUpMenuItem("付款码", R.drawable.home_add_icon_pay, "dianping://usercode", "paycode"));
    updateToReview(false);
  }

  private void initUI()
  {
    this.mListView = ((ListView)getContentView().findViewById(R.id.menu_list));
    this.mHomePopupAdapter = new HomePopUpMenuAdapter();
    this.mListView.setAdapter(this.mHomePopupAdapter);
  }

  private boolean isLogin()
  {
    try
    {
      String str = ((DPActivity)this.mContext).accountService().token();
      return str != null;
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  private boolean needGotoLogin(View paramView)
  {
    if (isLogin())
      return false;
    gotoLogin(paramView);
    return true;
  }

  private void setLoginBackView(View paramView)
  {
    mLoginBackView = paramView;
  }

  public void onClick(View paramView)
  {
    dismiss();
    int i;
    String str;
    if ((paramView.getTag() instanceof Integer))
    {
      i = ((Integer)paramView.getTag()).intValue();
      str = ((HomePopUpMenuItem)this.mMenuItems.get(i)).url;
      if (!TextUtils.isEmpty(str))
        break label48;
    }
    while (true)
    {
      return;
      label48: if (i == 0)
      {
        if (needGotoLogin(paramView))
          continue;
        RedAlertManager.getInstance().updateRedAlert("me.toreview");
        this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        this.mContext.sendBroadcast(new Intent("com.dianping.action.RedAlerts"));
        return;
      }
      if (1 != i)
        break;
      if (needGotoLogin(paramView))
        continue;
      this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
      return;
    }
    this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
    setLoginBackView(null);
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    if (getLoginBackView() != null)
      onClick(getLoginBackView());
    setLoginBackView(null);
  }

  public void show(View paramView)
  {
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    showAtLocation(paramView, 0, this.mScreenWidth - getWidth(), arrayOfInt[1] + paramView.getHeight());
  }

  public void updateToReview(boolean paramBoolean)
  {
    if ((this.mMenuItems == null) || (this.mMenuItems.size() == 0));
    while (true)
    {
      return;
      if (RedAlertManager.getInstance().checkRedAlertByTag("me.toreview") == null)
        ((HomePopUpMenuItem)this.mMenuItems.get(0)).setRedAlertCount(-1);
      while (paramBoolean)
      {
        this.mHomePopupAdapter.notifyDataSetChanged();
        return;
        ((HomePopUpMenuItem)this.mMenuItems.get(0)).setRedAlertCount(0);
      }
    }
  }

  class HomePopUpMenuAdapter extends BasicAdapter
  {
    HomePopUpMenuAdapter()
    {
    }

    public int getCount()
    {
      return HomePopUpMenu.this.mMenuItems.size();
    }

    public HomePopUpMenu.HomePopUpMenuItem getItem(int paramInt)
    {
      return (HomePopUpMenu.HomePopUpMenuItem)HomePopUpMenu.this.mMenuItems.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = (NovaRelativeLayout)LayoutInflater.from(HomePopUpMenu.this.mContext).inflate(R.layout.main_home_popup_menu_item, paramViewGroup, false);
      if ((paramInt == 0) || (paramInt == getCount() - 1))
      {
        paramView = (AbsListView.LayoutParams)paramViewGroup.getLayoutParams();
        paramView.width = -1;
        paramView.height = ViewUtils.dip2px(HomePopUpMenu.this.mContext, 50.0F);
      }
      paramView = (TextView)paramViewGroup.findViewById(R.id.title);
      Object localObject = (ImageView)paramViewGroup.findViewById(R.id.icon);
      HomePopUpMenu.HomePopUpMenuItem localHomePopUpMenuItem = getItem(paramInt);
      paramView.setText(localHomePopUpMenuItem.title);
      ((ImageView)localObject).setImageResource(localHomePopUpMenuItem.icon);
      localObject = (MeasuredTextView)paramViewGroup.findViewById(R.id.redalert1);
      MeasuredTextView localMeasuredTextView = (MeasuredTextView)paramViewGroup.findViewById(R.id.redalert2);
      if (localHomePopUpMenuItem.isShowRedAlert)
        if (localHomePopUpMenuItem.redAlert > 0)
          if (localHomePopUpMenuItem.redAlert > 99)
          {
            paramView = "99";
            localMeasuredTextView.setText(paramView);
            localMeasuredTextView.setBackgroundResource(0);
            if (localHomePopUpMenuItem.redAlert >= 10)
              break label371;
            localMeasuredTextView.setFlag(true);
            localMeasuredTextView.setBackgroundResource(R.drawable.home_navibar_tips_red_b);
            paramView = new RelativeLayout.LayoutParams(-2, -2);
            paramView.setMargins(ViewUtils.dip2px(HomePopUpMenu.this.mContext, 27.0F), ViewUtils.dip2px(HomePopUpMenu.this.mContext, 6.0F), 0, 0);
            localMeasuredTextView.setPadding(0, 0, 0, ViewUtils.dip2px(HomePopUpMenu.this.mContext, 1.0F));
            localMeasuredTextView.setLayoutParams(paramView);
            label270: ((MeasuredTextView)localObject).setVisibility(8);
            localMeasuredTextView.setVisibility(0);
            label283: if (paramInt != getCount() - 1)
              break label461;
            paramViewGroup.findViewById(R.id.divider).setVisibility(8);
          }
      while (true)
      {
        paramViewGroup.setTag(Integer.valueOf(paramInt));
        paramViewGroup.setOnClickListener(HomePopUpMenu.this);
        paramViewGroup.setGAString(((HomePopUpMenu.HomePopUpMenuItem)HomePopUpMenu.this.mMenuItems.get(paramInt)).gaString);
        return paramViewGroup;
        paramView = localHomePopUpMenuItem.redAlert + "";
        break;
        label371: int i = ViewUtils.dip2px(HomePopUpMenu.this.mContext, 4.0F);
        localMeasuredTextView.setPadding(i, ViewUtils.dip2px(HomePopUpMenu.this.mContext, 2.0F), i, ViewUtils.dip2px(HomePopUpMenu.this.mContext, 3.0F));
        localMeasuredTextView.setBackgroundResource(R.drawable.home_navibar_tips_reddigit);
        break label270;
        ((MeasuredTextView)localObject).setVisibility(0);
        localMeasuredTextView.setVisibility(8);
        break label283;
        ((MeasuredTextView)localObject).setVisibility(8);
        localMeasuredTextView.setVisibility(8);
        break label283;
        label461: paramViewGroup.findViewById(R.id.divider).setVisibility(0);
      }
    }
  }

  class HomePopUpMenuItem
  {
    public String gaString;
    public int icon;
    public boolean isShowRedAlert = false;
    public int redAlert = 0;
    public String title;
    public String url;

    public HomePopUpMenuItem(String paramInt, int paramString1, String paramString2, String arg5)
    {
      this.title = paramInt;
      this.icon = paramString1;
      this.url = paramString2;
      Object localObject;
      this.gaString = localObject;
      this.isShowRedAlert = false;
      this.redAlert = -1;
    }

    public HomePopUpMenuItem(String paramInt1, int paramString1, String paramString2, String paramInt2, int arg6)
    {
      this.title = paramInt1;
      this.icon = paramString1;
      this.url = paramString2;
      this.gaString = paramInt2;
      int i;
      if (i >= 0)
        bool = true;
      this.isShowRedAlert = bool;
      this.redAlert = i;
    }

    public void setRedAlertCount(int paramInt)
    {
      if (paramInt >= 0);
      for (boolean bool = true; ; bool = false)
      {
        this.isShowRedAlert = bool;
        this.redAlert = paramInt;
        return;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomePopUpMenu
 * JD-Core Version:    0.6.0
 */
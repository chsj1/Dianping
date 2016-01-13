package com.dianping.selectdish.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaListView;

public class SelectDishPagePortalPopupWindow extends PopupWindow
  implements LoginResultListener
{
  private static final int DISH_MENU_ENTRANCE_TYPE_TOGETHER_MENU = 0;
  private static final String TAG = SelectDishPagePortalPopupWindow.class.getSimpleName();
  private View arrowUpView;
  private int arrowUpViewHeight;
  private int arrowUpViewWidth;
  private final Context context;
  private SelectDishPagePortalListViewAdapter entranceListViewAdapter;
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private DPObject itemClickBeforeLogin = null;
  private int mScreenWidth = 0;
  private final DPObject[] menuEntranceList;

  public SelectDishPagePortalPopupWindow(Context paramContext, DPObject[] paramArrayOfDPObject)
  {
    super(paramContext);
    this.context = paramContext;
    this.menuEntranceList = paramArrayOfDPObject;
    setFocusable(true);
    setTouchable(true);
    setOutsideTouchable(true);
    setWidth(-2);
    setHeight(-2);
    setBackgroundDrawable(new BitmapDrawable(null, (Bitmap)null));
    setContentView(LayoutInflater.from(paramContext).inflate(R.layout.selectdish_page_portal_popup_window, null));
    initDatas();
    initViews();
  }

  private void clickEntrance(DPObject paramDPObject)
  {
    String str = paramDPObject.getString("Schema");
    if (TextUtils.isEmpty(str))
      return;
    int i = paramDPObject.getInt("Type");
    this.gaUserInfo.title = String.valueOf(i);
    GAHelper.instance().contextStatisticsEvent(this.context, "menuorder_list", this.gaUserInfo, "tap");
    if (i != 0)
    {
      this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
      return;
    }
    if (isLogin())
    {
      this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
      return;
    }
    this.itemClickBeforeLogin = paramDPObject;
    gotoLogin();
  }

  private void gotoLogin()
  {
    try
    {
      ((DPActivity)this.context).accountService().login(this);
      return;
    }
    catch (Exception localException)
    {
      Log.v(TAG, "gotoLogin exception " + localException.getMessage());
    }
  }

  private void initDatas()
  {
    this.mScreenWidth = ViewUtils.getScreenWidthPixels(this.context);
    this.entranceListViewAdapter = new SelectDishPagePortalListViewAdapter(null);
  }

  private void initViews()
  {
    Object localObject = getContentView();
    this.arrowUpView = ((View)localObject).findViewById(R.id.sd_tips_up_arrow);
    this.arrowUpView.measure(0, 0);
    this.arrowUpViewWidth = this.arrowUpView.getMeasuredWidth();
    this.arrowUpViewHeight = this.arrowUpView.getMeasuredHeight();
    localObject = (NovaListView)((View)localObject).findViewById(R.id.sd_more_portal_listview);
    ((NovaListView)localObject).setAdapter(this.entranceListViewAdapter);
    ((NovaListView)localObject).setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        SelectDishPagePortalPopupWindow.this.clickEntrance(SelectDishPagePortalPopupWindow.this.entranceListViewAdapter.getItem(paramInt));
        SelectDishPagePortalPopupWindow.this.dismiss();
      }
    });
  }

  private boolean isLogin()
  {
    try
    {
      String str = ((DPActivity)this.context).accountService().token();
      return str != null;
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    if (this.itemClickBeforeLogin != null)
    {
      clickEntrance(this.itemClickBeforeLogin);
      this.itemClickBeforeLogin = null;
    }
  }

  public void showBelowView(View paramView1, View paramView2)
  {
    int[] arrayOfInt = new int[2];
    paramView1.getLocationOnScreen(arrayOfInt);
    int i = arrayOfInt[1];
    paramView2.getLocationOnScreen(arrayOfInt);
    int j = arrayOfInt[0];
    showAtLocation(paramView1, 0, this.mScreenWidth - getWidth(), paramView1.getHeight() + i - this.arrowUpViewHeight);
    paramView1 = (FrameLayout.LayoutParams)this.arrowUpView.getLayoutParams();
    paramView1.rightMargin = (this.mScreenWidth - j - paramView2.getWidth() / 2 - this.arrowUpViewWidth / 2);
    this.arrowUpView.setLayoutParams(paramView1);
  }

  private class SelectDishPagePortalListViewAdapter extends BasicAdapter
  {
    private SelectDishPagePortalListViewAdapter()
    {
    }

    public int getCount()
    {
      if (SelectDishPagePortalPopupWindow.this.menuEntranceList != null)
        return SelectDishPagePortalPopupWindow.this.menuEntranceList.length;
      return 0;
    }

    public DPObject getItem(int paramInt)
    {
      return SelectDishPagePortalPopupWindow.this.menuEntranceList[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      int i = 0;
      DPObject localDPObject = getItem(paramInt);
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = LayoutInflater.from(SelectDishPagePortalPopupWindow.this.context).inflate(R.layout.selectdish_entrance_list_item_view, null, false);
      paramView = (ImageView)paramViewGroup.findViewById(R.id.sd_icon_imageview);
      TextView localTextView = (TextView)paramViewGroup.findViewById(R.id.sd_title_textview);
      View localView = paramViewGroup.findViewById(R.id.sd_divider_view);
      switch (localDPObject.getInt("Type"))
      {
      default:
      case 0:
      case 1:
      case 2:
      }
      while (true)
      {
        localTextView.setText(localDPObject.getString("Title"));
        if (paramInt == getCount() - 1)
          i = 4;
        localView.setVisibility(i);
        return paramViewGroup;
        paramView.setImageDrawable(SelectDishPagePortalPopupWindow.this.context.getResources().getDrawable(R.drawable.selectdish_popwindow_icon_together));
        continue;
        paramView.setImageDrawable(SelectDishPagePortalPopupWindow.this.context.getResources().getDrawable(R.drawable.selectdish_popwindow_icon_helpyou));
        continue;
        paramView.setImageDrawable(SelectDishPagePortalPopupWindow.this.context.getResources().getDrawable(R.drawable.selectdish_popwindow_icon_feedback));
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.view.SelectDishPagePortalPopupWindow
 * JD-Core Version:    0.6.0
 */
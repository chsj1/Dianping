package com.dianping.hui.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class HuiPayResultPopUpMenu extends PopupWindow
  implements View.OnClickListener
{
  private static final int POP_ITEM_INDEX_COMMON_QUESTION = 0;
  private static final int POP_ITEM_INDEX_CONTACT_BUSINESS = 1;
  private static final String TITLE_COMMON_QUE = "常见问题";
  private static final String TITLE_CONTACT_BUSINESS = "联系商户";
  private static final String TITLE_CONTACT_CUSTOME_SERVICE = "联系客服";
  private Context mContext;
  private HuiPayResultPopUpMenu.MenuAdapter mHomePopupAdapter;
  private ListView mListView;
  private ArrayList<HuiPayResultPopUpMenu.HomePopUpMenuItem> mMenuItems = new ArrayList();
  private int mScreenWidth;
  PopItemClickListener popItemClickListener;

  public HuiPayResultPopUpMenu(Context paramContext)
  {
    this(paramContext, -2, -2);
  }

  public HuiPayResultPopUpMenu(Context paramContext, int paramInt1, int paramInt2)
  {
    super(paramContext);
    this.mContext = paramContext;
    this.popItemClickListener = ((PopItemClickListener)paramContext);
    setFocusable(true);
    setTouchable(true);
    setOutsideTouchable(true);
    this.mScreenWidth = ViewUtils.getScreenWidthPixels(this.mContext);
    setWidth(paramInt1);
    setHeight(paramInt2);
    setBackgroundDrawable(new BitmapDrawable());
    setContentView(LayoutInflater.from(this.mContext).inflate(R.layout.hui_pay_result_pop_menu, null));
    initData();
    initUI();
  }

  private void initData()
  {
    this.mMenuItems.add(new HuiPayResultPopUpMenu.HomePopUpMenuItem(this, "常见问题"));
    this.mMenuItems.add(new HuiPayResultPopUpMenu.HomePopUpMenuItem(this, "联系商户"));
    this.mMenuItems.add(new HuiPayResultPopUpMenu.HomePopUpMenuItem(this, "联系客服"));
  }

  private void initUI()
  {
    this.mListView = ((ListView)getContentView().findViewById(R.id.menu_list));
    this.mHomePopupAdapter = new HuiPayResultPopUpMenu.MenuAdapter(this);
    this.mListView.setAdapter(this.mHomePopupAdapter);
  }

  public void onClick(View paramView)
  {
    dismiss();
    int i;
    if ((paramView.getTag() instanceof Integer))
    {
      i = ((Integer)paramView.getTag()).intValue();
      if (i == 0)
        this.popItemClickListener.commonQuestion();
    }
    else
    {
      return;
    }
    if (1 == i)
    {
      this.popItemClickListener.contactBusiness();
      return;
    }
    this.popItemClickListener.contactCustomerService();
  }

  public void show(View paramView)
  {
    paramView.getLocationOnScreen(new int[2]);
    showAsDropDown(paramView, this.mScreenWidth - getWidth(), 2);
  }

  public static abstract interface PopItemClickListener
  {
    public abstract void commonQuestion();

    public abstract void contactBusiness();

    public abstract void contactCustomerService();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.view.HuiPayResultPopUpMenu
 * JD-Core Version:    0.6.0
 */
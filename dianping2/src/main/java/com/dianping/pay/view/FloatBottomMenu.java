package com.dianping.pay.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import java.util.List;

public class FloatBottomMenu
{
  private FloatBottomMenu.MenuAdapter mAdapter;
  private Context mCtx;
  private Dialog mDialog;
  private OnMenuClickListener mListener;

  private FloatBottomMenu(Context paramContext)
  {
    this.mCtx = paramContext;
    init();
  }

  public static FloatBottomMenu create(Context paramContext)
  {
    return new FloatBottomMenu(paramContext);
  }

  private void init()
  {
    Object localObject2 = new FrameLayout.LayoutParams(-1, -1);
    ((FrameLayout.LayoutParams)localObject2).width = ViewUtils.getScreenWidthPixels(this.mCtx);
    this.mDialog = new Dialog(this.mCtx, R.style.FullScreenDialog);
    Object localObject1 = View.inflate(this.mCtx, R.layout.pay_float_bottom_menu, null);
    this.mDialog.setContentView((View)localObject1, (ViewGroup.LayoutParams)localObject2);
    localObject2 = new FloatBottomMenu.1(this);
    ((View)localObject1).setOnClickListener((View.OnClickListener)localObject2);
    ((View)localObject1).findViewById(R.id.btn_cancel).setOnClickListener((View.OnClickListener)localObject2);
    localObject1 = (ListView)((View)localObject1).findViewById(R.id.pay_menu_list);
    this.mAdapter = new FloatBottomMenu.MenuAdapter(this);
    ((ListView)localObject1).setOnItemClickListener(new FloatBottomMenu.2(this));
    ((ListView)localObject1).setAdapter(this.mAdapter);
  }

  public void addMenu(MenuItem paramMenuItem)
  {
    this.mAdapter.addMenu(paramMenuItem);
  }

  public boolean removeMenu(int paramInt)
  {
    return this.mAdapter.removeMenu(paramInt);
  }

  public FloatBottomMenu setMenuItems(List<MenuItem> paramList)
  {
    this.mAdapter.setMenuItems(paramList);
    return this;
  }

  public FloatBottomMenu setOnMenuClickListener(OnMenuClickListener paramOnMenuClickListener)
  {
    this.mListener = paramOnMenuClickListener;
    return this;
  }

  public void show()
  {
    this.mDialog.show();
  }

  public static class MenuItem
  {
    private int id;
    private int layoutResource;
    private String title;

    public MenuItem(int paramInt1, int paramInt2)
    {
      this.id = paramInt1;
      this.layoutResource = paramInt2;
    }

    public MenuItem(int paramInt, String paramString)
    {
      this.id = paramInt;
      this.title = paramString;
    }
  }

  public static abstract interface OnMenuClickListener
  {
    public abstract void onMenuClick(int paramInt, View paramView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.view.FloatBottomMenu
 * JD-Core Version:    0.6.0
 */
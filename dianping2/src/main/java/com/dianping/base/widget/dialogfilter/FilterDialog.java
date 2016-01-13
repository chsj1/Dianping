package com.dianping.base.widget.dialogfilter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class FilterDialog extends Dialog
{
  private Activity activity;
  private View.OnClickListener close = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      FilterDialog.this.cancel();
    }
  };
  private View contentView;
  protected OnFilterListener listener;
  private Object tag;

  public FilterDialog(Activity paramActivity)
  {
    super(paramActivity, 16973840);
    this.activity = paramActivity;
    this.contentView = getLayoutInflater().inflate(R.layout.filter_dialog, null, false);
    this.contentView.findViewById(R.id.filter_top).setOnClickListener(this.close);
    this.contentView.findViewById(R.id.filter_bottom).setOnClickListener(this.close);
    super.setContentView(this.contentView);
  }

  public Activity getActivity()
  {
    return this.activity;
  }

  public ViewGroup getFilterViewParent()
  {
    return (ViewGroup)this.contentView.findViewById(R.id.filter_content);
  }

  public Object getTag()
  {
    return this.tag;
  }

  public void setFilterView(View paramView)
  {
    ViewGroup localViewGroup = (ViewGroup)this.contentView.findViewById(R.id.filter_content);
    localViewGroup.removeAllViews();
    ViewGroup.LayoutParams localLayoutParams = localViewGroup.getLayoutParams();
    localLayoutParams.height = paramView.getLayoutParams().height;
    localViewGroup.setLayoutParams(localLayoutParams);
    localViewGroup.addView(paramView);
  }

  public void setOnFilterListener(OnFilterListener paramOnFilterListener)
  {
    this.listener = paramOnFilterListener;
  }

  public void setTag(Object paramObject)
  {
    this.tag = paramObject;
  }

  public void show(View paramView)
  {
    Object localObject = new Rect();
    this.activity.getWindow().getDecorView().getWindowVisibleDisplayFrame((Rect)localObject);
    int i = ((Rect)localObject).top;
    localObject = new int[2];
    paramView.getLocationOnScreen(localObject);
    int j = localObject[0];
    int k = paramView.getWidth() / 2;
    int m = localObject[1];
    int n = paramView.getHeight();
    paramView = this.contentView.findViewById(R.id.filter_trig);
    localObject = (RelativeLayout.LayoutParams)paramView.getLayoutParams();
    ((RelativeLayout.LayoutParams)localObject).leftMargin = (j + k - ((ImageView)paramView).getDrawable().getIntrinsicWidth() / 2);
    paramView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    paramView = this.contentView.findViewById(R.id.filter_top);
    localObject = (RelativeLayout.LayoutParams)paramView.getLayoutParams();
    ((RelativeLayout.LayoutParams)localObject).height = (m - i + n);
    paramView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    show();
  }

  public static abstract interface OnFilterListener
  {
    public abstract void onFilter(FilterDialog paramFilterDialog, Object paramObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.dialogfilter.FilterDialog
 * JD-Core Version:    0.6.0
 */
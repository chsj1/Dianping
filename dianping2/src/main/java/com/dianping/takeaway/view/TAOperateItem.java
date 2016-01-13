package com.dianping.takeaway.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TAOperateItem extends RelativeLayout
{
  public TextView contentText;
  public TextView expandText;
  public ImageView expandView;
  public ImageView iconView;
  private int padding;

  public TAOperateItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  private void initView(Context paramContext)
  {
    LayoutInflater.from(paramContext).inflate(R.layout.takeaway_operation_item, this);
    this.padding = ViewUtils.dip2px(paramContext, 15.0F);
    setPadding(this.padding, 0, this.padding, 0);
    this.iconView = ((ImageView)findViewById(R.id.iconView));
    this.contentText = ((TextView)findViewById(R.id.contentText));
    this.expandView = ((ImageView)findViewById(R.id.expandView));
    this.expandText = ((TextView)findViewById(R.id.expandText));
  }

  public void setBorder(BORDER paramBORDER)
  {
    switch (1.$SwitchMap$com$dianping$takeaway$view$TAOperateItem$BORDER[paramBORDER.ordinal()])
    {
    default:
    case 1:
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      setPadding(this.padding, 0, this.padding, 0);
      return;
      setBackgroundResource(R.drawable.takeaway_bg_operate_item_line_top);
      continue;
      setBackgroundResource(R.drawable.takeaway_bg_operate_item_line_bottom);
      continue;
      setBackgroundResource(R.drawable.takeaway_bg_operate_item_line_both);
      continue;
      setBackgroundResource(R.drawable.takeaway_bg_operate_item_line_none);
    }
  }

  public static enum BORDER
  {
    static
    {
      BOTTOM = new BORDER("BOTTOM", 1);
      BOTH = new BORDER("BOTH", 2);
      NONE = new BORDER("NONE", 3);
      $VALUES = new BORDER[] { TOP, BOTTOM, BOTH, NONE };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.view.TAOperateItem
 * JD-Core Version:    0.6.0
 */
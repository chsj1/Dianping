package com.dianping.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class TechnicianItemView extends NovaLinearLayout
{
  public NetworkThumbView iconView;
  private TextView nameView;
  private TextView starView;
  private ImageView statusView;
  private TextView titleView;

  public TechnicianItemView(Context paramContext)
  {
    super(paramContext);
  }

  public TechnicianItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public static TechnicianItemView createView(Context paramContext, ViewGroup paramViewGroup)
  {
    return (TechnicianItemView)LayoutInflater.from(paramContext).inflate(R.layout.shopinfo_technician_item, paramViewGroup, false);
  }

  public void init(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2, int paramInt3)
  {
    if (!TextUtils.isEmpty(paramString1))
      this.iconView.setImage(paramString1);
    if (!TextUtils.isEmpty(paramString2))
      this.nameView.setText(paramString2);
    if (paramInt1 > -1)
      this.starView.setText(paramInt1 + "");
    if (!TextUtils.isEmpty(paramString3))
      this.titleView.setText(paramString3);
    if (paramInt2 == 1)
    {
      this.statusView.setImageResource(R.drawable.technician_status_icon);
      this.statusView.setVisibility(0);
    }
    setGAString("beautyprofessional", paramString2, paramInt3);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.iconView = ((NetworkThumbView)findViewById(R.id.technician_icon));
    this.nameView = ((TextView)findViewById(R.id.technician_name));
    this.starView = ((TextView)findViewById(R.id.technician_star));
    this.titleView = ((TextView)findViewById(R.id.technician_title));
    this.statusView = ((ImageView)findViewById(R.id.technician_status));
    int i = ViewUtils.getScreenWidthPixels(getContext()) * 21 / 100;
    this.iconView.getLayoutParams().width = i;
    this.iconView.getLayoutParams().height = i;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.TechnicianItemView
 * JD-Core Version:    0.6.0
 */
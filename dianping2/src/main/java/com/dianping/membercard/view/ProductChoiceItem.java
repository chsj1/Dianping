package com.dianping.membercard.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class ProductChoiceItem extends NovaLinearLayout
{
  private ImageView arrowView;
  private int color = R.color.black;
  private TextView descView;
  private int from = 0;
  private Spanned fromHtml;
  private Button joinBtn;
  private RelativeLayout panel;
  private String prefix_format;
  private TextView titleView;

  public ProductChoiceItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public ProductChoiceItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void fillData(DPObject paramDPObject)
  {
    this.prefix_format = ("<b><font color=\"" + getResources().getColor(this.color) + "\">%s</font></b>%s");
    int i = paramDPObject.getInt("ProductLevel");
    String str2 = paramDPObject.getString("ProductName");
    String str1;
    if (!TextUtils.isEmpty(paramDPObject.getString("ProductRemark")))
    {
      paramDPObject = paramDPObject.getString("ProductRemark");
      if (i != 2)
        break label173;
      str1 = "高级会员:&#160;&#160;";
      this.joinBtn.setText("成为高级会员");
    }
    while (true)
    {
      if (!TextUtils.isEmpty(str2))
      {
        this.fromHtml = Html.fromHtml(String.format(this.prefix_format, new Object[] { str1, MCUtils.ToDBC(str2.replace("|", "")) }));
        this.titleView.setText(this.fromHtml);
      }
      if (!TextUtils.isEmpty(paramDPObject))
        this.descView.setText(MCUtils.ToDBC(paramDPObject));
      return;
      paramDPObject = paramDPObject.getString("ProductDesc");
      break;
      label173: str1 = "普通会员:&#160;&#160;";
      this.joinBtn.setText("成为普通会员");
    }
  }

  public void fillData(DPObject paramDPObject, int paramInt)
  {
    this.from = paramInt;
    this.color = R.color.deal_dark_black;
    fillData(paramDPObject);
    this.joinBtn.setText("立即升级");
  }

  public ImageView getArrowView()
  {
    return this.arrowView;
  }

  public TextView getDescView()
  {
    return this.descView;
  }

  public Button getJoinBtn()
  {
    return this.joinBtn;
  }

  public RelativeLayout getPanel()
  {
    return this.panel;
  }

  public TextView getTitleView()
  {
    return this.titleView;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.panel = ((RelativeLayout)findViewById(R.id.product_choice_panel));
    this.titleView = ((TextView)findViewById(R.id.product_choice_title));
    this.arrowView = ((ImageView)findViewById(R.id.product_choice_arrow));
    this.descView = ((TextView)findViewById(R.id.product_choice_desc));
    this.joinBtn = ((Button)findViewById(R.id.product_choice_join_btn));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.ProductChoiceItem
 * JD-Core Version:    0.6.0
 */
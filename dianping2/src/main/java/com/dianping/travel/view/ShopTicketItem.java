package com.dianping.travel.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;

public class ShopTicketItem extends LinearLayout
{
  private TextView address;
  private TextView addressTitle;
  private ImageView arrow;
  private Button book;
  private boolean expandAble;
  private LinearLayout extra;
  private boolean isExpand;
  private TextView name;
  private TextView pMode;
  private TextView pModeTitle;
  private TextView policy;
  private TextView policyTitle;
  private TextView price;
  private TextView tcPrice;

  public ShopTicketItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public ShopTicketItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void creatTextItem(String paramString1, String paramString2)
  {
    if (!TextUtils.isEmpty(paramString1))
    {
      TextView localTextView = new TextView(getContext());
      localTextView.setText(paramString1);
      localTextView.setTextColor(-6710887);
      localTextView.setTextSize(14.0F);
      paramString1 = new LinearLayout.LayoutParams(-2, -2);
      paramString1.setMargins(0, 0, 0, 0);
      localTextView.setLayoutParams(paramString1);
      this.extra.addView(localTextView);
      this.expandAble = true;
    }
    if (!TextUtils.isEmpty(paramString2))
    {
      paramString1 = new TextView(getContext());
      paramString1.setText(paramString2);
      paramString1.setTextColor(-13487566);
      paramString1.setTextSize(14.0F);
      paramString2 = new LinearLayout.LayoutParams(-2, -2);
      paramString2.setMargins(0, 0, 0, 20);
      paramString1.setLayoutParams(paramString2);
      this.extra.addView(paramString1);
      this.expandAble = true;
    }
  }

  public void expand()
  {
    this.extra.setVisibility(0);
    this.arrow.setImageResource(R.drawable.ic_arrow_up_black);
    this.isExpand = true;
  }

  public boolean expandAble()
  {
    return this.expandAble;
  }

  public boolean isExpand()
  {
    return this.isExpand;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.name = ((TextView)findViewById(R.id.name));
    this.price = ((TextView)findViewById(R.id.price));
    this.tcPrice = ((TextView)findViewById(R.id.tcprice));
    this.book = ((Button)findViewById(R.id.bookBtn));
    this.arrow = ((ImageView)findViewById(R.id.arrow));
    this.extra = ((LinearLayout)findViewById(R.id.extra));
    this.policyTitle = ((TextView)findViewById(R.id.policyTitle));
    this.policy = ((TextView)findViewById(R.id.policy));
    this.addressTitle = ((TextView)findViewById(R.id.addressTitle));
    this.address = ((TextView)findViewById(R.id.address));
    this.pModeTitle = ((TextView)findViewById(R.id.pModeTitle));
    this.pMode = ((TextView)findViewById(R.id.pMode));
  }

  public void setData(DPObject paramDPObject)
  {
    DPObject[] arrayOfDPObject = paramDPObject.getArray("Description");
    this.name.setText(paramDPObject.getString("Name"));
    this.price.setText("票面价￥" + paramDPObject.getInt("Price"));
    this.tcPrice.setText("￥" + paramDPObject.getInt("TCPrice"));
    int i = 0;
    while (i < arrayOfDPObject.length)
    {
      paramDPObject = arrayOfDPObject[i];
      creatTextItem(paramDPObject.getString("ID"), paramDPObject.getString("Name"));
      i += 1;
    }
    if (!this.expandAble)
      this.arrow.setVisibility(8);
  }

  public void setOnBookBtnClickedListener(View.OnClickListener paramOnClickListener)
  {
    this.book.setOnClickListener(paramOnClickListener);
  }

  public void shrink()
  {
    this.extra.setVisibility(8);
    this.arrow.setImageResource(R.drawable.ic_arrow_down_black);
    this.isExpand = false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.view.ShopTicketItem
 * JD-Core Version:    0.6.0
 */
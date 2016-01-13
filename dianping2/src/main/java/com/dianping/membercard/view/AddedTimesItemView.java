package com.dianping.membercard.view;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.Locale;

public class AddedTimesItemView extends LinearLayout
{
  private static final String POSITIVE_LEFT_TIMES_FORMAT = "还可消费<span><monospace> <big><big><big><font color=\"#FF8000\">%1$d</font></big></big></big> </monospace></span>份";
  private static final String ZERO_LEFT_TIMES_FORMAT = "还可消费<font color=\"#878787\">0</font>份";
  private TextView mLeftTimes;
  private TextView mProductDesc;
  private TextView mProductName;

  public AddedTimesItemView(Context paramContext)
  {
    super(paramContext);
    setupView(paramContext);
  }

  public AddedTimesItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setupView(paramContext);
  }

  private void setupView(Context paramContext)
  {
    LayoutInflater.from(getContext()).inflate(R.layout.card_purchased_times_list_item, this, true);
    this.mProductName = ((TextView)findViewById(R.id.product_name));
    this.mLeftTimes = ((TextView)findViewById(R.id.left_times));
    this.mProductDesc = ((TextView)findViewById(R.id.product_desc));
  }

  public AddedTimesItemView loadData(DPObject paramDPObject)
  {
    if ((paramDPObject == null) || (!MemberCard.isDPObjectTimesCard(paramDPObject)))
      return this;
    String str2 = paramDPObject.getString("ProductName");
    String str1 = str2;
    if (str2.contains("|"))
      str1 = str2.substring(0, str2.indexOf("|"));
    setProductName(str1);
    setLeftTimes(Double.valueOf(paramDPObject.getDouble("ProductValue")).intValue());
    setProductDesc(paramDPObject.getString("Tips"));
    return this;
  }

  public void setLeftTimes(int paramInt)
  {
    if (paramInt > 0)
    {
      localSpanned = Html.fromHtml(String.format(Locale.getDefault(), "还可消费<span><monospace> <big><big><big><font color=\"#FF8000\">%1$d</font></big></big></big> </monospace></span>份", new Object[] { Integer.valueOf(paramInt) }));
      this.mLeftTimes.setText(localSpanned);
    }
    do
      return;
    while (paramInt != 0);
    Spanned localSpanned = Html.fromHtml(String.format("还可消费<font color=\"#878787\">0</font>份", new Object[] { Integer.valueOf(paramInt) }));
    this.mLeftTimes.setText(localSpanned);
  }

  public void setProductDesc(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
      this.mProductDesc.setText(paramString);
  }

  public void setProductName(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
      this.mProductName.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.AddedTimesItemView
 * JD-Core Version:    0.6.0
 */
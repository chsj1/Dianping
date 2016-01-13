package com.dianping.hui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.hui.util.HuiUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HuiCashierBookingTicketsActivity extends NovaActivity
{
  private static final int RULE_HEIGHT = 40;
  private static final int TICKET_DIVIDER_HEIGHT = 10;
  private static final int TICKET_ITEM_HEIGHT = 100;
  String discountItemId;
  private String selectedTicketId;
  private List<DPObject> ticketOptions = new ArrayList();
  int type;
  private BookingTicketAdapter unusableListAdapter;
  private ListView unusableListView;
  private List<DPObject> unusableOptions = new ArrayList();
  private LinearLayout unusableTicketLayout;
  private BookingTicketAdapter usableListAdapter;
  private ListView usableListView;
  private List<DPObject> usableOptions = new ArrayList();
  private LinearLayout usableTicketLayout;

  private int calcListViewHeight(List<DPObject> paramList)
  {
    if (paramList.isEmpty())
      return 0;
    int j = paramList.size();
    int i = 0;
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      if (TextUtils.isEmpty(((DPObject)paramList.next()).getString("Rule")))
        continue;
      i += 1;
    }
    return ViewUtils.dip2px(this, j * 100 + (j - 1) * 10 + i * 40);
  }

  private void initView()
  {
    int j = 8;
    this.usableTicketLayout = ((LinearLayout)findViewById(R.id.usable_ticket_layout));
    Object localObject = this.usableTicketLayout;
    if (this.usableOptions.size() == 0)
    {
      i = 8;
      ((LinearLayout)localObject).setVisibility(i);
      this.usableListView = ((ListView)findViewById(R.id.usable_ticket_list));
      localObject = new LinearLayout.LayoutParams(-1, calcListViewHeight(this.usableOptions));
      this.usableListView.setLayoutParams((ViewGroup.LayoutParams)localObject);
      this.usableListAdapter = new BookingTicketAdapter(this, this.usableOptions, true, this.selectedTicketId);
      this.usableListView.setAdapter(this.usableListAdapter);
      this.unusableTicketLayout = ((LinearLayout)findViewById(R.id.unusable_ticket_layout));
      localObject = this.unusableTicketLayout;
      if (this.unusableOptions.size() != 0)
        break label256;
    }
    label256: for (int i = j; ; i = 0)
    {
      ((LinearLayout)localObject).setVisibility(i);
      this.unusableListView = ((ListView)findViewById(R.id.unusable_ticket_list));
      localObject = new LinearLayout.LayoutParams(-1, calcListViewHeight(this.unusableOptions));
      this.unusableListView.setLayoutParams((ViewGroup.LayoutParams)localObject);
      this.unusableListAdapter = new BookingTicketAdapter(this, this.unusableOptions, false, this.selectedTicketId);
      this.unusableListView.setAdapter(this.unusableListAdapter);
      localObject = (LinearLayout)findViewById(R.id.all_tickets_layout);
      ((LinearLayout)localObject).setFocusable(true);
      ((LinearLayout)localObject).setFocusableInTouchMode(true);
      ((LinearLayout)localObject).requestFocus();
      return;
      i = 0;
      break;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.discountItemId = getStringParam("discountitemid");
    this.selectedTicketId = getStringParam("selectedid");
    this.type = getIntParam("type", 20);
    this.ticketOptions = getIntent().getParcelableArrayListExtra("options");
    int i = 0;
    if (i < this.ticketOptions.size())
    {
      paramBundle = (DPObject)this.ticketOptions.get(i);
      if (((DPObject)this.ticketOptions.get(i)).getBoolean("CanUse"))
        this.usableOptions.add(paramBundle);
      while (true)
      {
        i += 1;
        break;
        this.unusableOptions.add(paramBundle);
      }
    }
    super.setContentView(R.layout.booking_pay_ticket_layout);
    super.getWindow().setBackgroundDrawable(null);
    super.setTitle("尊享券");
    initView();
  }

  class BookingTicketAdapter extends BaseAdapter
  {
    private Context context;
    private List<DPObject> dataList;
    private boolean isUsableList;
    String selectedId;

    public BookingTicketAdapter(List<DPObject> paramBoolean, boolean paramString, String arg4)
    {
      this.context = paramBoolean;
      this.dataList = paramString;
      boolean bool;
      this.isUsableList = bool;
      Object localObject;
      this.selectedId = localObject;
    }

    public int getCount()
    {
      return this.dataList.size();
    }

    public Object getItem(int paramInt)
    {
      return this.dataList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = (DPObject)this.dataList.get(paramInt);
      TextView localTextView1;
      TextView localTextView2;
      TextView localTextView3;
      TextView localTextView4;
      TextView localTextView5;
      ImageView localImageView;
      TextView localTextView6;
      RadioButton localRadioButton;
      if (paramView == null)
      {
        paramView = LayoutInflater.from(this.context).inflate(R.layout.booking_pay_ticket_item, null);
        localTextView1 = (TextView)paramView.findViewById(R.id.icon);
        localTextView2 = (TextView)paramView.findViewById(R.id.amount);
        localTextView3 = (TextView)paramView.findViewById(R.id.content);
        localTextView4 = (TextView)paramView.findViewById(R.id.subtitle);
        localTextView5 = (TextView)paramView.findViewById(R.id.rule);
        localImageView = (ImageView)paramView.findViewById(R.id.rule_divider);
        localTextView6 = (TextView)paramView.findViewById(R.id.expiry_date);
        localRadioButton = (RadioButton)paramView.findViewById(R.id.select_btn);
        Object localObject = paramView.findViewById(R.id.divider);
        if (paramInt != this.dataList.size() - 1)
          break label403;
        paramInt = 8;
        label156: ((View)localObject).setVisibility(paramInt);
        localTextView2.setText(paramViewGroup.getString("Deduction"));
        localTextView3.setText(paramViewGroup.getString("Content"));
        localObject = paramViewGroup.getString("Remark");
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          localTextView6.setText((CharSequence)localObject);
          localTextView6.setVisibility(0);
        }
        localObject = paramViewGroup.getString("Subtitle");
        if (TextUtils.isEmpty((CharSequence)localObject))
          break label408;
        localTextView4.setText((CharSequence)localObject);
        localTextView4.setVisibility(0);
        label242: localObject = paramViewGroup.getString("Rule");
        if (TextUtils.isEmpty((CharSequence)localObject))
          break label418;
        localTextView5.setText(HuiUtils.addSpaceToCnPunc((String)localObject));
        localTextView5.setVisibility(0);
        localImageView.setVisibility(0);
      }
      while (true)
      {
        if (!this.isUsableList)
          break label435;
        localTextView1.setTextColor(HuiCashierBookingTicketsActivity.this.getResources().getColor(R.color.light_red));
        localTextView2.setTextColor(HuiCashierBookingTicketsActivity.this.getResources().getColor(R.color.light_red));
        localTextView3.setTextColor(HuiCashierBookingTicketsActivity.this.getResources().getColor(R.color.deep_gray));
        localTextView6.setTextColor(HuiCashierBookingTicketsActivity.this.getResources().getColor(R.color.light_gray));
        localRadioButton.setChecked(paramViewGroup.getString("Id").equals(this.selectedId));
        localRadioButton.setVisibility(0);
        paramView.setOnClickListener(new HuiCashierBookingTicketsActivity.BookingTicketAdapter.1(this, localRadioButton, paramViewGroup));
        return paramView;
        break;
        label403: paramInt = 0;
        break label156;
        label408: localTextView4.setVisibility(8);
        break label242;
        label418: localTextView5.setVisibility(8);
        localImageView.setVisibility(8);
      }
      label435: localTextView1.setTextColor(HuiCashierBookingTicketsActivity.this.getResources().getColor(R.color.yy_translucent_light_red));
      localTextView2.setTextColor(HuiCashierBookingTicketsActivity.this.getResources().getColor(R.color.yy_translucent_light_red));
      localTextView3.setTextColor(HuiCashierBookingTicketsActivity.this.getResources().getColor(R.color.yy_translucent_deep_gray));
      localTextView4.setTextColor(HuiCashierBookingTicketsActivity.this.getResources().getColor(R.color.yy_translucent_deep_gray));
      localTextView5.setTextColor(HuiCashierBookingTicketsActivity.this.getResources().getColor(R.color.yy_translucent_deep_gray));
      localTextView6.setTextColor(HuiCashierBookingTicketsActivity.this.getResources().getColor(R.color.yy_translucent_light_gray));
      localRadioButton.setVisibility(4);
      return (View)paramView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiCashierBookingTicketsActivity
 * JD-Core Version:    0.6.0
 */
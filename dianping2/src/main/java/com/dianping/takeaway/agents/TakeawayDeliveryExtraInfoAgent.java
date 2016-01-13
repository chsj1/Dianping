package com.dianping.takeaway.agents;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.loader.MyResources;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource.LoadCause;
import com.dianping.takeaway.fragment.TakeawayDeliveryDetailFragment;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;

public class TakeawayDeliveryExtraInfoAgent extends CellAgent
{
  private TextView companyType;
  private ImageView companyTypeCheckbox;
  private View companyTypeView;
  protected TakeawayDeliveryDataSource dataSource = this.fragment.getDataSource();
  protected TakeawayDeliveryDetailFragment fragment = (TakeawayDeliveryDetailFragment)getFragment();
  private View invoiceLayout;
  private EditText invoiceTitleContentView;
  private View invoiceTitleLayout;
  private View invoiceTypeLayout;
  private CompoundButton needInvoiceSwitch;
  private TextView needInvoiceTipView;
  private TextView personalType;
  private ImageView personalTypeCheckbox;
  private View personalTypeView;
  private EditText remarkView;
  private View view;

  public TakeawayDeliveryExtraInfoAgent(Object paramObject)
  {
    super(paramObject);
    setupView();
  }

  private void setDefaultInvoiceView()
  {
    this.fragment.forceHideKeyboard();
    this.personalTypeCheckbox.setSelected(true);
    this.personalType.setTextColor(getResources().getColor(R.color.light_red));
    this.companyTypeCheckbox.setSelected(false);
    this.companyType.setTextColor(getResources().getColor(R.color.light_gray));
    this.invoiceTitleContentView.setText("");
    this.invoiceTitleLayout.setVisibility(8);
  }

  @TargetApi(16)
  private void setupView()
  {
    this.view = LayoutInflater.from(getContext()).inflate(R.layout.takeaway_delivery_extra_info_agent_layout, null);
    this.invoiceLayout = this.view.findViewById(R.id.invoice_layout);
    this.needInvoiceTipView = ((TextView)this.view.findViewById(R.id.need_invoice_tip));
    this.needInvoiceSwitch = ((CompoundButton)this.view.findViewById(R.id.need_invoice_switch));
    this.invoiceTypeLayout = this.view.findViewById(R.id.invoice_type_layout);
    this.personalTypeView = this.view.findViewById(R.id.personal_type_layout);
    this.personalTypeCheckbox = ((ImageView)this.view.findViewById(R.id.personal_checkbox));
    this.personalType = ((TextView)this.view.findViewById(R.id.personal_type));
    this.companyTypeView = this.view.findViewById(R.id.company_type_layout);
    this.companyTypeCheckbox = ((ImageView)this.view.findViewById(R.id.company_checkbox));
    this.companyType = ((TextView)this.view.findViewById(R.id.company_type));
    this.invoiceTitleLayout = this.view.findViewById(R.id.invoice_title_layout);
    this.invoiceTitleContentView = ((EditText)this.view.findViewById(R.id.invoice_title_content));
    if ((Build.VERSION.SDK_INT >= 16) && (((Switch)this.needInvoiceSwitch).getTrackDrawable() != null))
      ((Switch)this.needInvoiceSwitch).setSwitchMinWidth(((Switch)this.needInvoiceSwitch).getTrackDrawable().getIntrinsicWidth());
    this.needInvoiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
      {
        TakeawayDeliveryExtraInfoAgent.this.setDefaultInvoiceView();
        paramCompoundButton = TakeawayDeliveryExtraInfoAgent.this.dataSource.getGAUserInfo();
        GAHelper.instance().contextStatisticsEvent(TakeawayDeliveryExtraInfoAgent.this.fragment.getActivity(), "receipt", paramCompoundButton, "tap");
        if (paramBoolean)
        {
          TakeawayDeliveryExtraInfoAgent.this.dataSource.selectedInvoiceType = 1;
          TakeawayDeliveryExtraInfoAgent.this.invoiceTypeLayout.setVisibility(0);
          return;
        }
        TakeawayDeliveryExtraInfoAgent.this.dataSource.selectedInvoiceType = 0;
        TakeawayDeliveryExtraInfoAgent.this.invoiceTypeLayout.setVisibility(8);
      }
    });
    this.needInvoiceSwitch.setChecked(false);
    this.personalTypeView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayDeliveryExtraInfoAgent.this.dataSource.selectedInvoiceType = 1;
        TakeawayDeliveryExtraInfoAgent.this.setDefaultInvoiceView();
      }
    });
    this.companyTypeView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayDeliveryExtraInfoAgent.this.dataSource.selectedInvoiceType = 2;
        TakeawayDeliveryExtraInfoAgent.this.personalTypeCheckbox.setSelected(false);
        TakeawayDeliveryExtraInfoAgent.this.personalType.setTextColor(TakeawayDeliveryExtraInfoAgent.this.getResources().getColor(R.color.light_gray));
        TakeawayDeliveryExtraInfoAgent.this.companyTypeCheckbox.setSelected(true);
        TakeawayDeliveryExtraInfoAgent.this.companyType.setTextColor(TakeawayDeliveryExtraInfoAgent.this.getResources().getColor(R.color.light_red));
        TakeawayDeliveryExtraInfoAgent.this.invoiceTitleContentView.requestFocus();
        ((InputMethodManager)TakeawayDeliveryExtraInfoAgent.this.getContext().getSystemService("input_method")).toggleSoftInput(0, 2);
        TakeawayDeliveryExtraInfoAgent.this.invoiceTitleLayout.setVisibility(0);
      }
    });
    this.invoiceTitleContentView.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
        TakeawayDeliveryExtraInfoAgent.this.dataSource.inputInvoiceHeader = paramEditable.toString();
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    });
    this.remarkView = ((EditText)this.view.findViewById(R.id.remark_view));
    this.remarkView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        TakeawayDeliveryExtraInfoAgent.this.fragment.setRemarkViewTouchEvent();
        return false;
      }
    });
    this.remarkView.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
        TakeawayDeliveryExtraInfoAgent.this.dataSource.remark = paramEditable.toString();
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    });
  }

  private void updateView()
  {
    if (this.dataSource.isInvoiceSupported)
    {
      if (TextUtils.isEmpty(this.dataSource.invoiceMinFee))
      {
        this.needInvoiceTipView.setVisibility(8);
        this.needInvoiceSwitch.setVisibility(0);
        GAUserInfo localGAUserInfo = this.dataSource.getGAUserInfo();
        GAHelper.instance().contextStatisticsEvent(this.fragment.getActivity(), "receipt", localGAUserInfo, "view");
      }
      while (true)
      {
        this.invoiceLayout.setVisibility(0);
        return;
        this.needInvoiceTipView.setText(this.dataSource.invoiceMinFee);
        this.needInvoiceTipView.setVisibility(0);
        this.needInvoiceSwitch.setVisibility(8);
      }
    }
    this.invoiceLayout.setVisibility(8);
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ((this.dataSource.loadCause == TakeawayDeliveryDataSource.LoadCause.FIR_LOAD) && (paramAgentMessage != null) && ("DELIVERY_LOAD_ORDER_SUCCESS".equals(paramAgentMessage.what)))
      updateView();
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    addCell("6000extrainfo", this.view);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.agents.TakeawayDeliveryExtraInfoAgent
 * JD-Core Version:    0.6.0
 */
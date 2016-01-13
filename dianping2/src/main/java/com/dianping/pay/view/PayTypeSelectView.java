package com.dianping.pay.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;

public class PayTypeSelectView extends LinearLayout
{
  static final String PAY_METHOD_URL = "http://api.p.dianping.com/querynopwdquickpaymethod.pay";
  static final String SWITCH_METHOD_URL = "http://api.p.dianping.com/switchnopwddefaultpaymethod.pay";
  private NovaButton btnWxOpen;
  private NetworkImageView imagePayIcon;
  private boolean isOpenWxWebview = false;
  private boolean isShowWxQuickPay;
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new PayTypeSelectView.1(this);
  private LinearLayout payMethodLayer;
  private ArrayList<DPObject> payMethodList;
  private MApiRequest payMethodReq;
  private DPObject selectPayMethod;
  private MApiRequest switchMethodReq;
  private NovaTextView textModify;
  private TextView textPayInfo;
  private TextView textPayType;
  private String wxBindUrl;
  private RelativeLayout wxOpenLayer;

  public PayTypeSelectView(Context paramContext)
  {
    this(paramContext, null);
  }

  public PayTypeSelectView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    inflate(paramContext, R.layout.pay_type_select_view, this);
    this.payMethodList = new ArrayList();
    initView();
    updateView();
  }

  private void initView()
  {
    this.payMethodLayer = ((LinearLayout)findViewById(R.id.pay_method_layer));
    this.imagePayIcon = ((NetworkImageView)findViewById(R.id.pay_icon));
    this.textPayType = ((TextView)findViewById(R.id.pay_type));
    this.textModify = ((NovaTextView)findViewById(R.id.modify));
    this.textPayInfo = ((TextView)findViewById(R.id.pay_info));
    this.wxOpenLayer = ((RelativeLayout)findViewById(R.id.wx_open_layer));
    this.btnWxOpen = ((NovaButton)findViewById(R.id.wx_open));
    this.textModify.setOnClickListener(new PayTypeSelectView.2(this));
    this.btnWxOpen.setOnClickListener(new PayTypeSelectView.3(this));
  }

  private void updatePayMethodView(DPObject paramDPObject)
  {
    if (TextUtils.isEmpty(paramDPObject.getString("IconUrl")))
      this.imagePayIcon.setVisibility(8);
    while (true)
    {
      Object localObject2 = "";
      if (!TextUtils.isEmpty(paramDPObject.getString("PayMethodName")))
        localObject2 = "" + paramDPObject.getString("PayMethodName");
      Object localObject1 = localObject2;
      if (!TextUtils.isEmpty(paramDPObject.getString("PayMethodInfo")))
        localObject1 = (String)localObject2 + paramDPObject.getString("PayMethodInfo");
      localObject2 = localObject1;
      if (!TextUtils.isEmpty((CharSequence)localObject1))
        localObject2 = (String)localObject1 + "付款";
      this.textPayType.setText((CharSequence)localObject2);
      if (!TextUtils.isEmpty(paramDPObject.getString("PaymethodDescription")))
        break;
      this.textPayInfo.setVisibility(8);
      return;
      this.imagePayIcon.setImage(paramDPObject.getString("IconUrl"));
      this.imagePayIcon.setVisibility(0);
    }
    this.textPayInfo.setText(paramDPObject.getString("PaymethodDescription"));
    this.textPayInfo.setVisibility(0);
  }

  private void updateView()
  {
    if (this.payMethodList.size() == 0)
      if (this.isShowWxQuickPay)
      {
        this.payMethodLayer.setVisibility(8);
        this.wxOpenLayer.setVisibility(0);
      }
    do
    {
      return;
      this.payMethodLayer.setVisibility(8);
      this.wxOpenLayer.setVisibility(8);
      return;
      if (this.payMethodList.size() != 1)
        continue;
      this.payMethodLayer.setVisibility(0);
      this.wxOpenLayer.setVisibility(8);
      this.textModify.setVisibility(8);
      updatePayMethodView((DPObject)this.payMethodList.get(0));
      return;
    }
    while (this.payMethodList.size() <= 1);
    this.payMethodLayer.setVisibility(0);
    this.wxOpenLayer.setVisibility(8);
    this.textModify.setVisibility(0);
    updatePayMethodView((DPObject)this.payMethodList.get(0));
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    reqPayMethodList();
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.payMethodReq != null)
    {
      ((DPActivity)getContext()).mapiService().abort(this.payMethodReq, this.mapiHandler, true);
      this.payMethodReq = null;
    }
  }

  public void onRefresh()
  {
    reqPayMethodList();
  }

  public void onResume()
  {
    if (this.isOpenWxWebview)
    {
      reqPayMethodList();
      this.isOpenWxWebview = false;
    }
  }

  protected void reqPayMethodList()
  {
    if (this.payMethodReq != null)
      ((DPActivity)getContext()).mapiService().abort(this.payMethodReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(((DPActivity)getContext()).accountService().token());
    this.payMethodReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/querynopwdquickpaymethod.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    ((DPActivity)getContext()).mapiService().exec(this.payMethodReq, this.mapiHandler);
  }

  protected void reqSwitchPayMethod()
  {
    if (this.selectPayMethod.getString("ContractId").equals(((DPObject)this.payMethodList.get(0)).getString("ContractId")))
      return;
    if (this.switchMethodReq != null)
      ((DPActivity)getContext()).mapiService().abort(this.switchMethodReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(((DPActivity)getContext()).accountService().token());
    localArrayList.add("contractid");
    localArrayList.add(this.selectPayMethod.getString("ContractId"));
    this.switchMethodReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/switchnopwddefaultpaymethod.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    ((DPActivity)getContext()).mapiService().exec(this.switchMethodReq, this.mapiHandler);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.view.PayTypeSelectView
 * JD-Core Version:    0.6.0
 */
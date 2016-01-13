package com.dianping.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.SimpleMsg;
import com.dianping.tuan.widget.PickUserDiscountItem;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;

public class PickUserDiscountActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  private static final int DISCOUNTTOOL_ID_CASHIER = 3;
  private static final int DISCOUNTTOOL_ID_DISOUNT = 2;
  private static final int DISCOUNTTOOL_ID_GIFTCARD = 4;
  private static final int DISCOUNTTOOL_ID_REDUCTION = 6;
  private static final int DISCOUNTTOOL_ID_SCORE = 5;
  private static final String HINT_TEXT_1 = "以下优惠任选其一，支付完成后优惠不可退";
  private static final String HINT_TEXT_2 = "请输入优惠码，支付完成后优惠不可退";
  private static final String HINT_TEXT_3_PREFIX = "以下优惠任选其一，不可与";
  private static final String HINT_TEXT_3_SUFFIX = "同享，支付完成后优惠不可退";
  private static final String HINT_TEXT_4_PREFIX = "请输入优惠码，不可与";
  private static final String HINT_TEXT_4_SUFFIX = "同享，支付完成后优惠不可退";
  private static final String HINT_TEXT_5 = "使用现金券，支付完成后优惠不可退";
  private static final String HINT_TEXT_6_PREFIX = "使用现金券，不可与";
  private static final String HINT_TEXT_6_SUFFIX = "同享，支付完成后优惠不可退";
  private static final String RMB = "¥";
  protected ArrayList<DPObject> abstractDiscountToolList;
  protected double amount;
  protected Button btnCheckCode;
  protected boolean canUseCashier = false;
  protected boolean canUseDiscount = false;
  protected LinearLayout codeInput;
  protected int count;
  protected String currentDiscountCode;
  protected DiscountAdapter discountAdapter;
  DPObject dpHongbaoDiscountTool = null;
  DPObject dpReductionDiscountTool = null;
  protected MApiRequest getUserCouponListRequest;
  protected LinearLayout layerCashier;
  protected LinearLayout layerCodeSwitcher;
  protected LinearLayout layerDiscount;
  protected LinearLayout layerDiscountCode;
  protected LinearLayout layerUseCashier;
  protected ListView lvDisount;
  protected Context mContext;
  protected double priceForHongbao;
  protected double priceIfUseCashier;
  protected int productCode;
  protected int productId;
  protected EditText promoEditText;
  protected RadioButton rbUseCashier;
  protected RadioButton rbUseDiscountCode;
  protected String reductionDesc = "";
  protected DPObject selectedDiscount;
  protected int selectedReductionId;
  protected TextView tvExclusiveRuleTip;
  protected boolean useDiscountCodeSelected = false;
  protected boolean useHongbao;
  protected View vHeader;
  protected View vSepDiscountCode;
  protected MApiRequest verifyUserCouponCodeRequest;

  private void handleRules()
  {
    if (this.abstractDiscountToolList == null)
      return;
    int i = 0;
    Object localObject;
    if (i < this.abstractDiscountToolList.size())
    {
      localObject = (DPObject)this.abstractDiscountToolList.get(i);
      int j = ((DPObject)localObject).getInt("ID");
      if (j == 6)
        this.dpReductionDiscountTool = ((DPObject)localObject);
      while (true)
      {
        i += 1;
        break;
        if (j == 3)
        {
          if (!((DPObject)localObject).getBoolean("CanUse"))
            continue;
          this.dpHongbaoDiscountTool = ((DPObject)localObject);
          this.canUseCashier = true;
          continue;
        }
        if ((j != 2) || (!((DPObject)localObject).getBoolean("CanUse")))
          continue;
        this.canUseDiscount = true;
      }
    }
    if ((this.selectedReductionId > 0) && (this.dpReductionDiscountTool != null))
    {
      localObject = this.dpReductionDiscountTool.getArray("DiscountEvents");
      if (DPObjectUtils.isArrayEmpty(localObject))
        i = 0;
    }
    while (true)
    {
      if (i < localObject.length)
      {
        if (localObject[i].getInt("ID") == this.selectedReductionId)
          this.reductionDesc = localObject[i].getString("Desc");
      }
      else
      {
        if (!this.canUseDiscount)
          break;
        requestGetUserCouponList();
        return;
      }
      i += 1;
    }
    if ((!this.canUseCashier) && (!this.canUseDiscount))
      this.useDiscountCodeSelected = true;
    updateView();
  }

  private void onCouponSelected(DPObject paramDPObject)
  {
    Intent localIntent = new Intent();
    if (paramDPObject != null)
    {
      localIntent.putExtra("usediscount", true);
      localIntent.putExtra("selecteddiscount", paramDPObject);
    }
    setResult(-1, localIntent);
    finish();
  }

  private void requestVerifyUserCouponCode(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      Toast.makeText(this, "请输入优惠代码", 0).show();
    do
      return;
    while (this.verifyUserCouponCodeRequest != null);
    this.currentDiscountCode = paramString;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://api.p.dianping.com/");
    localUrlBuilder.appendPath("verifyUserCouponCode.pay");
    localUrlBuilder.addParam("productid", Integer.valueOf(this.productId));
    localUrlBuilder.addParam("productcode", Integer.valueOf(this.productCode));
    localUrlBuilder.addParam("count", Integer.valueOf(this.count));
    localUrlBuilder.addParam("amount", Double.valueOf(this.amount));
    localUrlBuilder.addParam("cityid", Integer.valueOf(city().id()));
    localUrlBuilder.addParam("couponcode", paramString);
    localUrlBuilder.addParam("token", accountService().token());
    this.verifyUserCouponCodeRequest = new BasicMApiRequest(localUrlBuilder.buildUrl(), "GET", null, CacheType.DISABLED, false, null);
    mapiService().exec(this.verifyUserCouponCodeRequest, this);
    showProgressDialog("正在验证优惠代码...");
  }

  private void setupView()
  {
    setContentView(R.layout.pick_user_discount_layout);
    this.tvExclusiveRuleTip = ((TextView)findViewById(R.id.tv_exclusive_rule_tip));
    this.tvExclusiveRuleTip.setVisibility(8);
    this.lvDisount = ((ListView)findViewById(R.id.lv_discount));
    this.lvDisount.setOnItemClickListener(this);
    this.vHeader = LayoutInflater.from(this.mContext).inflate(R.layout.pick_user_discount_header, this.lvDisount, false);
    this.layerCashier = ((LinearLayout)this.vHeader.findViewById(R.id.layer_cashier));
    this.layerCashier.setVisibility(8);
    this.layerUseCashier = ((LinearLayout)this.vHeader.findViewById(R.id.layer_use_cashier));
    this.layerUseCashier.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = PickUserDiscountActivity.this;
        if (!PickUserDiscountActivity.this.useHongbao);
        for (boolean bool = true; ; bool = false)
        {
          paramView.useHongbao = bool;
          if (PickUserDiscountActivity.this.useHongbao)
          {
            PickUserDiscountActivity.this.useDiscountCodeSelected = false;
            PickUserDiscountActivity.this.selectedDiscount = null;
          }
          PickUserDiscountActivity.this.updateView();
          paramView = new Intent();
          paramView.putExtra("usehongbao", PickUserDiscountActivity.this.useHongbao);
          PickUserDiscountActivity.this.setResult(-1, paramView);
          PickUserDiscountActivity.this.finish();
          return;
        }
      }
    });
    this.rbUseCashier = ((RadioButton)this.vHeader.findViewById(R.id.rb_use_cashier));
    this.layerDiscountCode = ((LinearLayout)this.vHeader.findViewById(R.id.layer_discountcode));
    this.layerDiscountCode.setVisibility(8);
    this.vSepDiscountCode = this.vHeader.findViewById(R.id.sep_discountcode);
    this.layerCodeSwitcher = ((LinearLayout)this.vHeader.findViewById(R.id.code_switcher));
    this.layerCodeSwitcher.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = PickUserDiscountActivity.this;
        if (!PickUserDiscountActivity.this.useDiscountCodeSelected);
        for (boolean bool = true; ; bool = false)
        {
          paramView.useDiscountCodeSelected = bool;
          if (PickUserDiscountActivity.this.useDiscountCodeSelected)
          {
            PickUserDiscountActivity.this.useHongbao = false;
            PickUserDiscountActivity.this.selectedDiscount = null;
          }
          PickUserDiscountActivity.this.updateView();
          if ((!PickUserDiscountActivity.this.useDiscountCodeSelected) && (PickUserDiscountActivity.this.selectedDiscount != null) && (!TextUtils.isEmpty(PickUserDiscountActivity.this.selectedDiscount.getString("DiscountCode"))))
          {
            PickUserDiscountActivity.this.setResult(-1, new Intent());
            PickUserDiscountActivity.this.finish();
          }
          return;
        }
      }
    });
    this.codeInput = ((LinearLayout)this.vHeader.findViewById(R.id.code_input));
    this.codeInput.setVisibility(8);
    this.promoEditText = ((EditText)this.vHeader.findViewById(R.id.promo_code));
    this.promoEditText.setOnKeyListener(new View.OnKeyListener()
    {
      public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
      {
        if (paramInt == 66)
        {
          PickUserDiscountActivity.this.requestVerifyUserCouponCode(PickUserDiscountActivity.this.promoEditText.getText().toString());
          return true;
        }
        return false;
      }
    });
    this.btnCheckCode = ((Button)this.vHeader.findViewById(R.id.check_code_btn));
    this.btnCheckCode.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        InputMethodManager localInputMethodManager = (InputMethodManager)PickUserDiscountActivity.this.getSystemService("input_method");
        if (localInputMethodManager.isActive())
          localInputMethodManager.hideSoftInputFromWindow(paramView.getWindowToken(), 0);
        PickUserDiscountActivity.this.requestVerifyUserCouponCode(PickUserDiscountActivity.this.promoEditText.getText().toString());
      }
    });
    this.rbUseDiscountCode = ((RadioButton)this.vHeader.findViewById(R.id.check));
    this.layerDiscount = ((LinearLayout)this.vHeader.findViewById(R.id.layer_discount));
    this.layerDiscount.setVisibility(8);
    this.lvDisount.addHeaderView(this.vHeader);
    this.discountAdapter = new DiscountAdapter();
    this.lvDisount.setAdapter(this.discountAdapter);
  }

  private void updateView()
  {
    double d;
    label94: String str2;
    label180: label195: String str1;
    if (this.canUseCashier)
    {
      this.layerCashier.setVisibility(0);
      d = this.dpHongbaoDiscountTool.getDouble("Balance");
      if (d < this.priceForHongbao)
      {
        this.priceIfUseCashier = d;
        if (!this.useHongbao)
          break label287;
        this.rbUseCashier.setChecked(true);
        this.rbUseCashier.setText("-¥ " + PriceFormatUtils.formatPrice(this.priceIfUseCashier) + " ");
        if (!this.canUseDiscount)
          break label375;
        this.layerDiscountCode.setVisibility(0);
        if (!this.useDiscountCodeSelected)
          break label346;
        this.vSepDiscountCode.setVisibility(0);
        this.codeInput.setVisibility(0);
        this.rbUseDiscountCode.setChecked(true);
        if ((this.selectedDiscount != null) && (!TextUtils.isEmpty(this.selectedDiscount.getString("DiscountCode"))))
          this.promoEditText.setText(this.selectedDiscount.getString("DiscountCode"));
        if (!this.canUseDiscount)
          break label387;
        this.layerDiscount.setVisibility(0);
        if (this.discountAdapter.getDiscountCount() == 0)
          this.layerDiscount.setVisibility(8);
        this.discountAdapter.notifyDataSetChanged();
        str2 = "";
        if ((this.canUseCashier) || (this.canUseDiscount))
          break label430;
        if (!TextUtils.isEmpty(this.reductionDesc))
          break label399;
        str1 = "请输入优惠码，支付完成后优惠不可退";
      }
    }
    while (true)
    {
      if (TextUtils.isEmpty(str1))
        break label676;
      this.tvExclusiveRuleTip.setText(str1);
      this.tvExclusiveRuleTip.setVisibility(0);
      return;
      this.priceIfUseCashier = this.priceForHongbao;
      break;
      label287: this.rbUseCashier.setChecked(false);
      this.rbUseCashier.setText("¥ " + PriceFormatUtils.formatPrice(d) + " ");
      break label94;
      this.layerCashier.setVisibility(8);
      break label94;
      label346: this.vSepDiscountCode.setVisibility(8);
      this.codeInput.setVisibility(8);
      this.rbUseDiscountCode.setChecked(false);
      break label180;
      label375: this.layerDiscountCode.setVisibility(8);
      break label180;
      label387: this.layerDiscount.setVisibility(8);
      break label195;
      label399: str1 = "请输入优惠码，不可与" + this.reductionDesc + "同享，支付完成后优惠不可退";
      continue;
      label430: if ((!this.canUseCashier) && (this.canUseDiscount))
      {
        if (this.discountAdapter.getDiscountCount() == 0)
        {
          if (TextUtils.isEmpty(this.reductionDesc))
          {
            str1 = "请输入优惠码，支付完成后优惠不可退";
            continue;
          }
          str1 = "请输入优惠码，不可与" + this.reductionDesc + "同享，支付完成后优惠不可退";
          continue;
        }
        if (TextUtils.isEmpty(this.reductionDesc))
        {
          str1 = "以下优惠任选其一，支付完成后优惠不可退";
          continue;
        }
        str1 = "以下优惠任选其一，不可与" + this.reductionDesc + "同享，支付完成后优惠不可退";
        continue;
      }
      if ((this.canUseCashier) && (!this.canUseDiscount))
      {
        if (TextUtils.isEmpty(this.reductionDesc))
        {
          str1 = "使用现金券，支付完成后优惠不可退";
          continue;
        }
        str1 = "使用现金券，不可与" + this.reductionDesc + "同享，支付完成后优惠不可退";
        continue;
      }
      str1 = str2;
      if (!this.canUseCashier)
        continue;
      str1 = str2;
      if (!this.canUseDiscount)
        continue;
      if (TextUtils.isEmpty(this.reductionDesc))
      {
        str1 = "以下优惠任选其一，支付完成后优惠不可退";
        continue;
      }
      str1 = "以下优惠任选其一，不可与" + this.reductionDesc + "同享，支付完成后优惠不可退";
    }
    label676: this.tvExclusiveRuleTip.setVisibility(8);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mContext = this;
    paramBundle = getIntent();
    this.count = paramBundle.getIntExtra("count", 0);
    this.productId = paramBundle.getIntExtra("productid", 0);
    this.productCode = paramBundle.getIntExtra("productcode", 1);
    this.amount = paramBundle.getDoubleExtra("amount", 0.0D);
    this.priceForHongbao = paramBundle.getDoubleExtra("priceforhongbao", 0.0D);
    this.useHongbao = paramBundle.getBooleanExtra("usehongbao", false);
    this.selectedDiscount = ((DPObject)paramBundle.getParcelableExtra("selecteddiscount"));
    this.selectedReductionId = paramBundle.getIntExtra("selectedreductionid", 0);
    this.abstractDiscountToolList = paramBundle.getParcelableArrayListExtra("abstractdiscounttool");
    if (this.useHongbao)
      this.selectedDiscount = null;
    if ((this.selectedDiscount != null) && (!TextUtils.isEmpty(this.selectedDiscount.getString("DiscountCode"))))
      this.useDiscountCodeSelected = true;
    if (!isLogined())
    {
      finish();
      return;
    }
    setupView();
    handleRules();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.getUserCouponListRequest != null)
    {
      mapiService().abort(this.getUserCouponListRequest, this, true);
      this.getUserCouponListRequest = null;
    }
    if (this.verifyUserCouponCodeRequest != null)
    {
      mapiService().abort(this.verifyUserCouponCodeRequest, this, true);
      this.verifyUserCouponCodeRequest = null;
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if (DPObjectUtils.isDPObjectof(paramAdapterView, "Discount"))
    {
      paramAdapterView = (DPObject)paramAdapterView;
      this.useHongbao = false;
      this.useDiscountCodeSelected = false;
      updateView();
      if (this.selectedDiscount == null)
        break label72;
      if (paramAdapterView.getInt("ID") == this.selectedDiscount.getInt("ID"))
        onCouponSelected(null);
    }
    else
    {
      return;
    }
    onCouponSelected(paramAdapterView);
    return;
    label72: onCouponSelected(paramAdapterView);
  }

  public void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
    if (this.verifyUserCouponCodeRequest != null)
    {
      mapiService().abort(this.verifyUserCouponCodeRequest, this, true);
      this.verifyUserCouponCodeRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.getUserCouponListRequest)
    {
      this.getUserCouponListRequest = null;
      this.discountAdapter.setErrorMsg(paramMApiResponse);
    }
    do
      return;
    while (paramMApiRequest != this.verifyUserCouponCodeRequest);
    dismissDialog();
    this.verifyUserCouponCodeRequest = null;
    Toast.makeText(this.mContext, paramMApiResponse.content(), 0).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest == this.getUserCouponListRequest)
    {
      this.getUserCouponListRequest = null;
      this.discountAdapter.appendList(paramMApiResponse);
    }
    do
    {
      do
        return;
      while (paramMApiRequest != this.verifyUserCouponCodeRequest);
      dismissDialog();
      this.verifyUserCouponCodeRequest = null;
    }
    while (!DPObjectUtils.isDPObjectof(paramMApiResponse, "Discount"));
    onCouponSelected(paramMApiResponse.edit().putString("DiscountCode", this.currentDiscountCode).generate());
  }

  public void requestGetUserCouponList()
  {
    if (this.getUserCouponListRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://api.p.dianping.com/");
    localUrlBuilder.appendPath("getusercouponlist.pay");
    localUrlBuilder.addParam("cityid", Integer.valueOf(city().id()));
    localUrlBuilder.addParam("count", Integer.valueOf(this.count));
    localUrlBuilder.addParam("productid", Integer.valueOf(this.productId));
    localUrlBuilder.addParam("productcode", Integer.valueOf(this.productCode));
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("start", Integer.valueOf(this.discountAdapter.getNextStartIndex()));
    localUrlBuilder.addParam("limit", Integer.valueOf(25));
    localUrlBuilder.addParam("amount", Double.valueOf(this.amount));
    this.getUserCouponListRequest = new BasicMApiRequest(localUrlBuilder.buildUrl(), "GET", null, CacheType.DISABLED, false, null);
    mapiService().exec(this.getUserCouponListRequest, this);
  }

  class DiscountAdapter extends BasicAdapter
  {
    private ArrayList<DPObject> dpDisountList = new ArrayList();
    private String errorMsg = null;
    private boolean isEnd = false;
    private int nextStartIndex = 0;

    public DiscountAdapter()
    {
    }

    public void appendList(DPObject paramDPObject)
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
      if (arrayOfDPObject != null)
      {
        if (this.nextStartIndex == 0)
          this.dpDisountList.clear();
        this.dpDisountList.addAll(Arrays.asList(arrayOfDPObject));
        this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
        this.isEnd = paramDPObject.getBoolean("IsEnd");
        if ((!this.isEnd) && (arrayOfDPObject.length == 0))
          this.isEnd = true;
      }
      if ((!PickUserDiscountActivity.this.canUseCashier) && (this.dpDisountList.size() == 0))
        PickUserDiscountActivity.this.useDiscountCodeSelected = true;
      PickUserDiscountActivity.this.updateView();
    }

    public int getCount()
    {
      if (PickUserDiscountActivity.this.canUseDiscount)
      {
        if (this.isEnd)
          return this.dpDisountList.size();
        return this.dpDisountList.size() + 1;
      }
      return 0;
    }

    public int getDiscountCount()
    {
      return this.dpDisountList.size();
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.dpDisountList.size())
        return this.dpDisountList.get(paramInt);
      if (this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public int getNextStartIndex()
    {
      return this.nextStartIndex;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(localObject, "Discount"))
      {
        DPObject localDPObject = (DPObject)localObject;
        if ((paramView instanceof PickUserDiscountItem));
        for (paramView = (PickUserDiscountItem)paramView; ; paramView = null)
        {
          localObject = paramView;
          if (paramView == null)
            localObject = (PickUserDiscountItem)LayoutInflater.from(PickUserDiscountActivity.this.mContext).inflate(R.layout.pickuserdiscount_list_item, paramViewGroup, false);
          ((PickUserDiscountItem)localObject).setDiscount(localDPObject, PickUserDiscountActivity.this.selectedDiscount);
          return localObject;
        }
      }
      if (localObject == LOADING)
      {
        if ((this.errorMsg == null) && (!this.isEnd))
          PickUserDiscountActivity.this.requestGetUserCouponList();
        return getLoadingView(paramViewGroup, paramView);
      }
      return (View)getFailedView(this.errorMsg, new PickUserDiscountActivity.DiscountAdapter.1(this), paramViewGroup, paramView);
    }

    public void setErrorMsg(SimpleMsg paramSimpleMsg)
    {
      if (paramSimpleMsg == null);
      for (paramSimpleMsg = "请求失败，请稍后再试"; ; paramSimpleMsg = paramSimpleMsg.content())
      {
        this.errorMsg = paramSimpleMsg;
        PickUserDiscountActivity.this.updateView();
        return;
      }
    }
  }

  public static final class ViewHolder
  {
    public TextView extraView;
    public TextView priceView;
    public RadioButton radioButton;
    public TextView titleView;
    public TextView validView;

    public void reset()
    {
      this.priceView.setVisibility(0);
      this.titleView.setVisibility(0);
      this.validView.setVisibility(0);
      this.extraView.setText("");
      this.priceView.setText("");
      this.titleView.setText("");
      this.validView.setText("");
      this.radioButton.setVisibility(0);
      this.radioButton.setChecked(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.PickUserDiscountActivity
 * JD-Core Version:    0.6.0
 */
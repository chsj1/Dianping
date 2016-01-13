package com.dianping.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.tuan.widget.PromoDeskCouponItem;
import com.dianping.util.DeviceUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PayDiscountSelectActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected Button btnCheckCode;
  protected Button btnConfirm;
  protected boolean canUseCouponPromo = false;
  protected boolean canUseHongBaoPromo = false;
  protected boolean canUseShopCouponPromo = false;
  protected int cityId;
  protected LinearLayout codeInput;
  protected String currentCode;
  protected DPObject dpCouponPromoTool;
  protected DPObject dpDiscountPromoTool;
  protected DPObject dpHongBaoPromoTool;
  protected DPObject dpPromoDesk;
  protected ArrayList<DPObject> dpPromoDeskShopCouponList = new ArrayList();
  protected ArrayList<DPObject> dpPromoDeskShopCouponUnusableList = new ArrayList();
  protected ArrayList<DPObject> dpPromoProductList = new ArrayList();
  protected DPObject dpShopCouponPromoTool;
  protected MApiRequest getPromoDeskCouponRequest;
  protected ImageView ivCodeCheck;
  protected ImageView ivUseHongBaoPromo;
  protected LinearLayout layerCodeSwitcher;
  protected LinearLayout layerCouponPromo;
  protected LinearLayout layerHongBaoPromo;
  protected LinearLayout layerPromoDeskCode;
  protected LinearLayout layerShopCoupon;
  protected LinearLayout layerUseHongBaoPromo;
  protected ListView lvPromoDeskCoupon;
  protected Context mContext;
  protected double maxPriceIfUseHongbao;
  protected String mobileNo;
  protected double priceIfUseCashier;
  protected PromoDeskCouponAdapter promoDeskCouponAdapter;
  protected EditText promoEditText;
  protected String reductionDesc = "";
  protected boolean restrictCouponPromo;
  protected boolean restrictHongbaoPromo;
  protected boolean restrictShopCouponPromo;
  protected RMBLabelItem rmbCheckCodeRMBLabel;
  protected RMBLabelItem rmbHongbaoRMBLabel;
  protected DPObject selectedCouponPromo;
  protected DPObject selectedDiscountPromoEvent;
  protected DPObject selectedDiscountPromoGroup;
  protected DPObject selectedShopCouponPromo;
  protected int shopId;
  protected String token;
  protected double totalNeedPay;
  protected TextView tvCodeCheckHint;
  protected TextView tvExclusiveRuleTip;
  protected TextView tvHongBaoPromoHint;
  protected boolean useCodeSelected = false;
  protected boolean useHongbaoPromo;
  protected View vCouponSeperater;
  protected View vHeader;
  protected View vSepPromoDeskCode;
  protected MApiRequest verifyPromoDeskCodeRequest;

  private void confirmPromo()
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("usehongbaopromo", this.useHongbaoPromo);
    localIntent.putExtra("selectedcouponpromo", this.selectedCouponPromo);
    localIntent.putExtra("selectedshopcouponpromo", this.selectedShopCouponPromo);
    setResult(-1, localIntent);
    finish();
  }

  private void handleRules()
  {
    int[] arrayOfInt;
    int i;
    if (this.totalNeedPay > 0.0D)
    {
      if ((this.dpHongBaoPromoTool != null) && (!this.restrictHongbaoPromo) && (this.dpHongBaoPromoTool.getBoolean("CanUse")) && (productCodeCanUse(this.dpHongBaoPromoTool.getIntArray("ProductCodes"))))
        this.canUseHongBaoPromo = true;
      if ((this.dpCouponPromoTool != null) && (!this.restrictCouponPromo) && (this.dpCouponPromoTool.getBoolean("CanUse")))
        this.canUseCouponPromo = true;
      if ((this.dpShopCouponPromoTool != null) && (!this.restrictShopCouponPromo) && (this.dpShopCouponPromoTool.getBoolean("CanUse")))
        this.canUseShopCouponPromo = true;
      if ((this.selectedDiscountPromoGroup != null) && (this.selectedDiscountPromoEvent != null))
      {
        arrayOfInt = this.selectedDiscountPromoGroup.getIntArray("MutexTools");
        if (arrayOfInt != null)
          i = 0;
      }
    }
    while (true)
    {
      if (i < arrayOfInt.length)
      {
        if ((arrayOfInt[i] == 3) || (arrayOfInt[i] == 2))
          this.reductionDesc = this.selectedDiscountPromoEvent.getString("Title");
      }
      else
      {
        if ((!this.canUseHongBaoPromo) && (!this.canUseCouponPromo))
          this.useCodeSelected = true;
        if ((!this.canUseCouponPromo) && (!this.canUseShopCouponPromo))
          break;
        requestGetPromoDeskCoupon();
        return;
      }
      i += 1;
    }
    updateView();
  }

  private boolean productCodeCanUse(int[] paramArrayOfInt)
  {
    int i3 = 0;
    int i2 = 0;
    int i1 = i3;
    int i;
    if (paramArrayOfInt != null)
    {
      i1 = i3;
      if (paramArrayOfInt.length > 0)
      {
        i1 = i3;
        if (this.dpPromoProductList != null)
          i = 0;
      }
    }
    while (true)
    {
      i1 = i2;
      int m;
      int n;
      int j;
      if (i < paramArrayOfInt.length)
      {
        m = 0;
        n = paramArrayOfInt[i];
        j = 0;
      }
      while (true)
      {
        i1 = i2;
        int k = m;
        if (j < this.dpPromoProductList.size())
        {
          DPObject localDPObject = (DPObject)this.dpPromoProductList.get(j);
          if ((localDPObject.getBoolean("Selected")) && (localDPObject.getInt("ProductCode") == n))
          {
            i1 = 1;
            k = 1;
          }
        }
        else
        {
          if (k == 0)
            break;
          return i1;
        }
        j += 1;
      }
      i += 1;
      i2 = i1;
    }
  }

  private void requestVerifyPromoDeskCode(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      Toast.makeText(this.mContext, "请输入优惠码", 0).show();
    do
      return;
    while ((this.verifyPromoDeskCodeRequest != null) || (this.dpPromoProductList == null) || (this.dpPromoProductList.size() == 0));
    JSONArray localJSONArray = new JSONArray();
    int i = 0;
    while (i < this.dpPromoProductList.size())
    {
      localObject = new JSONObject();
      DPObject localDPObject = (DPObject)this.dpPromoProductList.get(i);
      try
      {
        ((JSONObject)localObject).put("productId", localDPObject.getInt("ProductId"));
        ((JSONObject)localObject).put("productCode", localDPObject.getInt("ProductCode"));
        ((JSONObject)localObject).put("price", localDPObject.getDouble("Price"));
        ((JSONObject)localObject).put("quantity", localDPObject.getInt("Quantity"));
        ((JSONObject)localObject).put("selected", localDPObject.getBoolean("Selected"));
        ((JSONObject)localObject).put("noDiscountAmount", localDPObject.getDouble("NoDiscountAmount"));
        localJSONArray.put(localObject);
        i += 1;
      }
      catch (JSONException paramString)
      {
        paramString.printStackTrace();
        return;
      }
    }
    this.currentCode = paramString;
    Object localObject = UrlBuilder.createBuilder("http://api.p.dianping.com/");
    ((UrlBuilder)localObject).appendPath("verifypromodeskcode.pay");
    ((UrlBuilder)localObject).addParam("cityid", Integer.valueOf(this.cityId));
    if (this.shopId > 0)
      ((UrlBuilder)localObject).addParam("shopid", Integer.valueOf(this.shopId));
    ((UrlBuilder)localObject).addParam("code", paramString);
    ((UrlBuilder)localObject).addParam("token", this.token);
    if (!TextUtils.isEmpty(this.mobileNo))
      ((UrlBuilder)localObject).addParam("mobileNo", this.mobileNo);
    ((UrlBuilder)localObject).addParam("promoProductList", localJSONArray.toString());
    ((UrlBuilder)localObject).addParam("cx", DeviceUtils.cxInfo("couponcode"));
    this.verifyPromoDeskCodeRequest = new BasicMApiRequest(((UrlBuilder)localObject).buildUrl(), "GET", null, CacheType.DISABLED, false, null);
    mapiService().exec(this.verifyPromoDeskCodeRequest, this);
    showProgressDialog("正在验证优惠码...");
  }

  private void setupView()
  {
    setContentView(R.layout.pay_discount_select_layout);
    this.tvExclusiveRuleTip = ((TextView)findViewById(R.id.tv_exclusive_rule_tip));
    this.tvExclusiveRuleTip.setVisibility(8);
    this.btnConfirm = ((Button)findViewById(R.id.btn_confirm));
    this.btnConfirm.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PayDiscountSelectActivity.this.confirmPromo();
      }
    });
    this.btnConfirm.setVisibility(8);
    this.lvPromoDeskCoupon = ((ListView)findViewById(R.id.lv_promodeskcoupon));
    this.vHeader = LayoutInflater.from(this.mContext).inflate(R.layout.pay_discount_select_header, this.lvPromoDeskCoupon, false);
    this.layerHongBaoPromo = ((LinearLayout)this.vHeader.findViewById(R.id.layer_hongbaopromo));
    this.layerHongBaoPromo.setVisibility(8);
    this.layerUseHongBaoPromo = ((LinearLayout)this.vHeader.findViewById(R.id.layer_use_hongbaopromo));
    this.layerUseHongBaoPromo.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = PayDiscountSelectActivity.this;
        if (!PayDiscountSelectActivity.this.useHongbaoPromo);
        for (boolean bool = true; ; bool = false)
        {
          paramView.useHongbaoPromo = bool;
          if (PayDiscountSelectActivity.this.useHongbaoPromo)
          {
            PayDiscountSelectActivity.this.useCodeSelected = false;
            PayDiscountSelectActivity.this.selectedCouponPromo = null;
            PayDiscountSelectActivity.this.selectedShopCouponPromo = null;
          }
          PayDiscountSelectActivity.this.updateView();
          PayDiscountSelectActivity.this.confirmPromo();
          return;
        }
      }
    });
    this.tvHongBaoPromoHint = ((TextView)this.vHeader.findViewById(R.id.tv_hongbaopromo_hint));
    this.ivUseHongBaoPromo = ((ImageView)this.vHeader.findViewById(R.id.iv_use_hongbaopromo));
    this.rmbHongbaoRMBLabel = ((RMBLabelItem)this.vHeader.findViewById(R.id.rmb_hongbaopromo));
    this.layerPromoDeskCode = ((LinearLayout)this.vHeader.findViewById(R.id.layer_promodeskcode));
    this.layerPromoDeskCode.setVisibility(8);
    this.vSepPromoDeskCode = this.vHeader.findViewById(R.id.sep_promodeskcode);
    this.layerCodeSwitcher = ((LinearLayout)this.vHeader.findViewById(R.id.code_switcher));
    this.layerCodeSwitcher.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = PayDiscountSelectActivity.this;
        boolean bool;
        if (!PayDiscountSelectActivity.this.useCodeSelected)
        {
          bool = true;
          paramView.useCodeSelected = bool;
          if (!PayDiscountSelectActivity.this.useCodeSelected)
            break label69;
          PayDiscountSelectActivity.this.useHongbaoPromo = false;
          PayDiscountSelectActivity.this.selectedCouponPromo = null;
          PayDiscountSelectActivity.this.selectedShopCouponPromo = null;
        }
        while (true)
        {
          PayDiscountSelectActivity.this.updateView();
          return;
          bool = false;
          break;
          label69: PayDiscountSelectActivity.this.selectedCouponPromo = null;
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
          PayDiscountSelectActivity.this.requestVerifyPromoDeskCode(PayDiscountSelectActivity.this.promoEditText.getText().toString());
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
        InputMethodManager localInputMethodManager = (InputMethodManager)PayDiscountSelectActivity.this.getSystemService("input_method");
        if (localInputMethodManager.isActive())
          localInputMethodManager.hideSoftInputFromWindow(paramView.getWindowToken(), 0);
        PayDiscountSelectActivity.this.requestVerifyPromoDeskCode(PayDiscountSelectActivity.this.promoEditText.getText().toString());
      }
    });
    this.tvCodeCheckHint = ((TextView)this.vHeader.findViewById(R.id.tv_codecheck_hint));
    this.ivCodeCheck = ((ImageView)this.vHeader.findViewById(R.id.iv_codecheck));
    this.rmbCheckCodeRMBLabel = ((RMBLabelItem)this.vHeader.findViewById(R.id.rmb_codecheck));
    this.layerCouponPromo = ((LinearLayout)this.vHeader.findViewById(R.id.layer_couponpromo));
    this.layerCouponPromo.setVisibility(8);
    this.layerShopCoupon = ((LinearLayout)this.vHeader.findViewById(R.id.layer_shop_coupon));
    this.vCouponSeperater = this.vHeader.findViewById(R.id.coupon_seperater);
    this.vCouponSeperater.setVisibility(8);
    this.lvPromoDeskCoupon.addHeaderView(this.vHeader);
    this.promoDeskCouponAdapter = new PromoDeskCouponAdapter();
    this.lvPromoDeskCoupon.setAdapter(this.promoDeskCouponAdapter);
  }

  private void showShopCouponList()
  {
    Object localObject;
    if (getShopCouponCount() > 0)
    {
      int i = 0;
      if (i < this.dpPromoDeskShopCouponList.size())
      {
        localObject = (DPObject)this.dpPromoDeskShopCouponList.get(i);
        PromoDeskCouponItem localPromoDeskCouponItem = (PromoDeskCouponItem)LayoutInflater.from(this.mContext).inflate(R.layout.promodeskcoupon_list_item, this.layerShopCoupon, false);
        if (localPromoDeskCouponItem.setPromoDeskCoupon((DPObject)localObject, this.selectedShopCouponPromo, this.dpPromoProductList))
        {
          localPromoDeskCouponItem.setEnabled(true);
          localPromoDeskCouponItem.setClickable(true);
        }
        while (true)
        {
          localPromoDeskCouponItem.setOnClickListener(new View.OnClickListener((DPObject)localObject)
          {
            public void onClick(View paramView)
            {
              PayDiscountSelectActivity.this.useHongbaoPromo = false;
              PayDiscountSelectActivity.this.useCodeSelected = false;
              if (PayDiscountSelectActivity.this.selectedShopCouponPromo != null)
                if (this.val$dpPromoDeskShopCoupon.getInt("ID") == PayDiscountSelectActivity.this.selectedShopCouponPromo.getInt("ID"))
                {
                  PayDiscountSelectActivity.this.selectedShopCouponPromo = null;
                  PayDiscountSelectActivity.this.updateView();
                  if ((!PayDiscountSelectActivity.this.hasShopCouponAndCoupon()) || (PayDiscountSelectActivity.this.selectedCouponPromo != null) || (PayDiscountSelectActivity.this.selectedShopCouponPromo != null))
                    break label131;
                  PayDiscountSelectActivity.this.confirmPromo();
                }
              label131: 
              do
              {
                return;
                PayDiscountSelectActivity.this.selectedShopCouponPromo = this.val$dpPromoDeskShopCoupon;
                break;
                PayDiscountSelectActivity.this.selectedShopCouponPromo = this.val$dpPromoDeskShopCoupon;
                break;
              }
              while (PayDiscountSelectActivity.this.hasShopCouponAndCoupon());
              PayDiscountSelectActivity.this.confirmPromo();
            }
          });
          this.layerShopCoupon.addView(localPromoDeskCouponItem);
          i += 1;
          break;
          localPromoDeskCouponItem.setEnabled(false);
          localPromoDeskCouponItem.setClickable(false);
        }
      }
    }
    if (hasUnusableShopCoupons())
    {
      localObject = (NovaRelativeLayout)LayoutInflater.from(this.mContext).inflate(R.layout.more_unusable_coupons_item, this.layerShopCoupon, false);
      ((TextView)((NovaRelativeLayout)localObject).findViewById(R.id.title)).setText("您还有其他商家抵用券，但该订单不可用");
      ((NovaRelativeLayout)localObject).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://payunusablecoupon"));
          Bundle localBundle = new Bundle();
          localBundle.putParcelableArrayList("unusableShopCouponList", PayDiscountSelectActivity.this.dpPromoDeskShopCouponUnusableList);
          paramView.putExtras(localBundle);
          PayDiscountSelectActivity.this.startActivity(paramView);
        }
      });
      this.layerShopCoupon.addView((View)localObject);
    }
  }

  private void updateView()
  {
    if (this.dpHongBaoPromoTool == null)
    {
      this.layerHongBaoPromo.setVisibility(8);
      if (this.dpCouponPromoTool != null)
        break label555;
      this.layerPromoDeskCode.setVisibility(8);
    }
    while (true)
    {
      this.layerShopCoupon.removeAllViews();
      label62: String str;
      label104: double d1;
      if ((this.dpCouponPromoTool == null) && (this.dpShopCouponPromoTool == null))
      {
        this.layerCouponPromo.setVisibility(8);
        str = "";
        if (this.totalNeedPay > 0.0D)
        {
          if ((this.canUseHongBaoPromo) || (this.promoDeskCouponAdapter.getCouponCount() != 0) || (getShopCouponCount() != 0))
            break label1130;
          str = "请输入优惠码，支付完成后优惠不可退";
        }
        if (TextUtils.isEmpty(str))
          break label1189;
        this.tvExclusiveRuleTip.setText(str);
        this.tvExclusiveRuleTip.setVisibility(0);
        label129: if ((!hasShopCouponAndCoupon()) || ((this.selectedCouponPromo == null) && (this.selectedShopCouponPromo == null)))
          break label1201;
        this.btnConfirm.setVisibility(0);
        return;
        if (this.restrictHongbaoPromo)
        {
          this.layerHongBaoPromo.setVisibility(0);
          this.layerUseHongBaoPromo.setClickable(false);
          this.rmbHongbaoRMBLabel.setRMBLabelValue(1.7976931348623157E+308D);
          this.tvHongBaoPromoHint.setText("不可用");
          this.tvHongBaoPromoHint.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
          this.tvHongBaoPromoHint.setTextColor(getResources().getColor(R.color.text_color_gray));
          this.ivUseHongBaoPromo.setImageDrawable(getResources().getDrawable(R.drawable.rad_disable));
          break;
        }
        if (this.totalNeedPay > 0.0D)
        {
          if (this.canUseHongBaoPromo)
          {
            this.layerHongBaoPromo.setVisibility(0);
            this.layerUseHongBaoPromo.setClickable(true);
            d1 = this.dpHongBaoPromoTool.getDouble("Balance");
            if (d1 < this.maxPriceIfUseHongbao);
            for (this.priceIfUseCashier = d1; ; this.priceIfUseCashier = this.maxPriceIfUseHongbao)
            {
              this.tvHongBaoPromoHint.setText("");
              if (!this.useHongbaoPromo)
                break label377;
              this.rmbHongbaoRMBLabel.setRMBLabelValue(-Math.abs(this.priceIfUseCashier));
              this.ivUseHongBaoPromo.setImageDrawable(getResources().getDrawable(R.drawable.rad_pressed));
              break;
            }
            label377: this.rmbHongbaoRMBLabel.setRMBLabelValue(Math.abs(d1));
            this.ivUseHongBaoPromo.setImageDrawable(getResources().getDrawable(R.drawable.rad_normal));
            break;
          }
          this.layerHongBaoPromo.setVisibility(0);
          this.layerUseHongBaoPromo.setClickable(false);
          this.rmbHongbaoRMBLabel.setRMBLabelValue(1.7976931348623157E+308D);
          this.tvHongBaoPromoHint.setText("不可同享");
          this.tvHongBaoPromoHint.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
          this.tvHongBaoPromoHint.setTextColor(getResources().getColor(R.color.text_color_gray));
          this.ivUseHongBaoPromo.setImageDrawable(getResources().getDrawable(R.drawable.rad_disable));
          break;
        }
        this.layerHongBaoPromo.setVisibility(0);
        this.layerUseHongBaoPromo.setClickable(false);
        this.rmbHongbaoRMBLabel.setRMBLabelValue(1.7976931348623157E+308D);
        this.tvHongBaoPromoHint.setText("");
        this.ivUseHongBaoPromo.setImageDrawable(getResources().getDrawable(R.drawable.rad_disable));
        break;
        label555: this.layerPromoDeskCode.setVisibility(0);
        if (this.restrictCouponPromo)
        {
          this.vSepPromoDeskCode.setVisibility(8);
          this.codeInput.setVisibility(8);
          this.layerCodeSwitcher.setClickable(false);
          this.tvCodeCheckHint.setText("不可用");
          this.tvCodeCheckHint.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
          this.tvCodeCheckHint.setTextColor(getResources().getColor(R.color.text_color_gray));
          this.ivCodeCheck.setImageDrawable(getResources().getDrawable(R.drawable.rad_disable));
          continue;
        }
        this.tvCodeCheckHint.setText("");
        this.rmbCheckCodeRMBLabel.setRMBLabelValue(1.7976931348623157E+308D);
        if (this.totalNeedPay > 0.0D)
          if (this.useCodeSelected)
          {
            this.vSepPromoDeskCode.setVisibility(0);
            this.codeInput.setVisibility(0);
            this.layerCodeSwitcher.setClickable(true);
            this.ivCodeCheck.setImageDrawable(getResources().getDrawable(R.drawable.rad_pressed));
            if ((this.selectedCouponPromo != null) && (!TextUtils.isEmpty(this.selectedCouponPromo.getString("CouponCode"))))
            {
              this.promoEditText.setText(this.selectedCouponPromo.getString("CouponCode"));
              d1 = 0.0D;
            }
          }
      }
      try
      {
        double d2 = Double.valueOf(this.selectedCouponPromo.getString("Price")).doubleValue();
        d1 = d2;
        label799: this.rmbCheckCodeRMBLabel.setRMBLabelValue(-Math.abs(d1));
        continue;
        this.promoEditText.setText("");
        continue;
        this.layerCodeSwitcher.setClickable(true);
        this.vSepPromoDeskCode.setVisibility(8);
        this.codeInput.setVisibility(8);
        this.ivCodeCheck.setImageDrawable(getResources().getDrawable(R.drawable.rad_normal));
        continue;
        this.layerCodeSwitcher.setClickable(false);
        this.vSepPromoDeskCode.setVisibility(8);
        this.codeInput.setVisibility(8);
        this.ivCodeCheck.setImageDrawable(getResources().getDrawable(R.drawable.rad_disable));
        continue;
        if ((this.restrictCouponPromo) && (this.restrictShopCouponPromo))
        {
          this.layerCouponPromo.setVisibility(8);
          break label62;
        }
        if (this.totalNeedPay > 0.0D)
        {
          if ((!this.canUseCouponPromo) && (!this.canUseShopCouponPromo))
          {
            this.layerCouponPromo.setVisibility(8);
            break label62;
          }
          int i;
          if ((getShopCouponCount() > 0) || (hasUnusableShopCoupons()))
          {
            i = 1;
            label996: if ((this.promoDeskCouponAdapter.getCouponCount() <= 0) && (!this.promoDeskCouponAdapter.hasUnusableCoupons()))
              break label1055;
          }
          label1055: for (int j = 1; ; j = 0)
          {
            this.layerCouponPromo.setVisibility(0);
            if ((i != 0) || (j != 0))
              break label1061;
            this.layerCouponPromo.setVisibility(8);
            break;
            i = 0;
            break label996;
          }
          label1061: if (i != 0)
          {
            showShopCouponList();
            this.layerShopCoupon.setVisibility(0);
          }
          while (true)
          {
            this.promoDeskCouponAdapter.notifyDataSetChanged();
            if ((i == 0) || (j == 0))
              break;
            this.vCouponSeperater.setVisibility(0);
            break;
            this.layerShopCoupon.setVisibility(8);
          }
        }
        this.layerCouponPromo.setVisibility(8);
        break label62;
        label1130: if ((this.canUseShopCouponPromo) && (!TextUtils.isEmpty(this.reductionDesc)))
        {
          str = "除商户抵用券外，其他优惠不与" + this.reductionDesc + "同享，支付完成后优惠不可退";
          break label104;
        }
        str = "抵用券/优惠码/现金券任选一种，支付完成后优惠不可退";
        break label104;
        label1189: this.tvExclusiveRuleTip.setVisibility(8);
        break label129;
        label1201: this.btnConfirm.setVisibility(8);
        return;
      }
      catch (Exception localException)
      {
        break label799;
      }
    }
  }

  public void appendPromoDeskCouponList(DPObject paramDPObject)
  {
    appendShopCouponList(paramDPObject);
    this.promoDeskCouponAdapter.appendCouponList(paramDPObject);
  }

  public void appendShopCouponList(DPObject paramDPObject)
  {
    DPObject[] arrayOfDPObject1 = paramDPObject.getArray("ShopCouponList");
    DPObject[] arrayOfDPObject2 = paramDPObject.getArray("UnavailableShopCouponList");
    int i = paramDPObject.getInt("NextStartIndex");
    if (arrayOfDPObject1 != null)
    {
      if (i == 0)
        this.dpPromoDeskShopCouponList.clear();
      this.dpPromoDeskShopCouponList.addAll(Arrays.asList(arrayOfDPObject1));
    }
    if ((arrayOfDPObject2 != null) && (arrayOfDPObject2.length != 0) && (this.dpPromoDeskShopCouponUnusableList.size() == 0))
      this.dpPromoDeskShopCouponUnusableList.addAll(Arrays.asList(arrayOfDPObject2));
  }

  public int getShopCouponCount()
  {
    if (this.dpPromoDeskShopCouponList == null)
      return 0;
    return this.dpPromoDeskShopCouponList.size();
  }

  public boolean hasShopCouponAndCoupon()
  {
    return (getShopCouponCount() > 0) && (this.promoDeskCouponAdapter.getCouponCount() > 0);
  }

  public boolean hasUnusableShopCoupons()
  {
    if (this.dpPromoDeskShopCouponUnusableList == null);
    do
      return false;
    while (this.dpPromoDeskShopCouponUnusableList.size() <= 0);
    return true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mContext = this;
    paramBundle = getIntent();
    this.cityId = paramBundle.getIntExtra("cityid", 1);
    this.shopId = paramBundle.getIntExtra("shopid", 0);
    this.token = paramBundle.getStringExtra("token");
    this.mobileNo = paramBundle.getStringExtra("mobileno");
    this.dpPromoProductList = paramBundle.getParcelableArrayListExtra("promoproductlist");
    this.selectedDiscountPromoGroup = ((DPObject)paramBundle.getParcelableExtra("selecteddiscountpromogroup"));
    this.selectedDiscountPromoEvent = ((DPObject)paramBundle.getParcelableExtra("selecteddiscountpromoevent"));
    this.useHongbaoPromo = paramBundle.getBooleanExtra("usehongbaopromo", false);
    this.maxPriceIfUseHongbao = paramBundle.getDoubleExtra("maxpriceifusehongbao", 0.0D);
    this.selectedCouponPromo = ((DPObject)paramBundle.getParcelableExtra("selectedcouponpromo"));
    this.selectedShopCouponPromo = ((DPObject)paramBundle.getParcelableExtra("selectedshopcouponpromo"));
    this.dpPromoDesk = ((DPObject)paramBundle.getParcelableExtra("promodesk"));
    if (this.dpPromoDesk != null)
    {
      this.dpDiscountPromoTool = this.dpPromoDesk.getObject("Discount");
      this.dpHongBaoPromoTool = this.dpPromoDesk.getObject("HongBao");
      this.dpCouponPromoTool = this.dpPromoDesk.getObject("Coupon");
      this.dpShopCouponPromoTool = this.dpPromoDesk.getObject("ShopCoupon");
    }
    this.restrictHongbaoPromo = paramBundle.getBooleanExtra("restricthongbaopromo", false);
    this.restrictCouponPromo = paramBundle.getBooleanExtra("restrictcouponpromo", false);
    this.restrictShopCouponPromo = paramBundle.getBooleanExtra("restrictshopcouponpromo", false);
    this.totalNeedPay = paramBundle.getDoubleExtra("totalneedpay", 0.0D);
    if (TextUtils.isEmpty(this.token))
    {
      if (isLogined())
        this.token = accountService().token();
    }
    else
    {
      if (this.useHongbaoPromo)
      {
        this.selectedCouponPromo = null;
        this.selectedShopCouponPromo = null;
      }
      if ((this.selectedCouponPromo != null) && (!TextUtils.isEmpty(this.selectedCouponPromo.getString("CouponCode"))))
        this.useCodeSelected = true;
      setupView();
      handleRules();
      return;
    }
    finish();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.getPromoDeskCouponRequest != null)
    {
      mapiService().abort(this.getPromoDeskCouponRequest, this, true);
      this.getPromoDeskCouponRequest = null;
    }
    if (this.verifyPromoDeskCodeRequest != null)
    {
      mapiService().abort(this.verifyPromoDeskCodeRequest, this, true);
      this.verifyPromoDeskCodeRequest = null;
    }
  }

  public boolean onGoBack()
  {
    confirmPromo();
    return super.onGoBack();
  }

  public void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
    if (this.getPromoDeskCouponRequest != null)
    {
      mapiService().abort(this.getPromoDeskCouponRequest, this, true);
      this.getPromoDeskCouponRequest = null;
    }
    if (this.verifyPromoDeskCodeRequest != null)
    {
      mapiService().abort(this.verifyPromoDeskCodeRequest, this, true);
      this.verifyPromoDeskCodeRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.getPromoDeskCouponRequest)
    {
      dismissDialog();
      this.getPromoDeskCouponRequest = null;
      this.promoDeskCouponAdapter.setErrorMsg(paramMApiResponse);
    }
    do
      return;
    while (paramMApiRequest != this.verifyPromoDeskCodeRequest);
    dismissDialog();
    this.verifyPromoDeskCodeRequest = null;
    Toast.makeText(this.mContext, paramMApiResponse.content(), 0).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.getPromoDeskCouponRequest)
    {
      dismissDialog();
      this.getPromoDeskCouponRequest = null;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "PromoDeskCouponList"))
      {
        appendPromoDeskCouponList((DPObject)paramMApiResponse);
        updateView();
      }
    }
    do
    {
      do
        return;
      while (paramMApiRequest != this.verifyPromoDeskCodeRequest);
      dismissDialog();
      this.verifyPromoDeskCodeRequest = null;
    }
    while (!DPObjectUtils.isDPObjectof(paramMApiResponse, "PromoDeskCoupon"));
    this.selectedCouponPromo = ((DPObject)paramMApiResponse).edit().putString("CouponCode", this.currentCode).generate();
    confirmPromo();
  }

  public void requestGetPromoDeskCoupon()
  {
    if (this.getPromoDeskCouponRequest != null);
    do
      return;
    while ((this.dpPromoProductList == null) || (this.dpPromoProductList.size() == 0));
    JSONArray localJSONArray = new JSONArray();
    int i = 0;
    while (i < this.dpPromoProductList.size())
    {
      localObject = new JSONObject();
      DPObject localDPObject = (DPObject)this.dpPromoProductList.get(i);
      try
      {
        ((JSONObject)localObject).put("productId", localDPObject.getInt("ProductId"));
        ((JSONObject)localObject).put("productCode", localDPObject.getInt("ProductCode"));
        ((JSONObject)localObject).put("price", localDPObject.getDouble("Price"));
        ((JSONObject)localObject).put("quantity", localDPObject.getInt("Quantity"));
        ((JSONObject)localObject).put("selected", localDPObject.getBoolean("Selected"));
        ((JSONObject)localObject).put("noDiscountAmount", localDPObject.getDouble("NoDiscountAmount"));
        localJSONArray.put(localObject);
        i += 1;
      }
      catch (JSONException localJSONException)
      {
        localJSONException.printStackTrace();
        return;
      }
    }
    Object localObject = UrlBuilder.createBuilder("http://api.p.dianping.com/");
    ((UrlBuilder)localObject).appendPath("promo/getpromodeskcoupon.pay");
    ((UrlBuilder)localObject).addParam("cityId", Integer.valueOf(this.cityId));
    if (this.shopId > 0)
      ((UrlBuilder)localObject).addParam("shopId", Integer.valueOf(this.shopId));
    ((UrlBuilder)localObject).addParam("token", this.token);
    ((UrlBuilder)localObject).addParam("start", Integer.valueOf(this.promoDeskCouponAdapter.getNextStartIndex()));
    ((UrlBuilder)localObject).addParam("limit", Integer.valueOf(25));
    ((UrlBuilder)localObject).addParam("promoProductList", localJSONException.toString());
    this.getPromoDeskCouponRequest = new BasicMApiRequest(((UrlBuilder)localObject).buildUrl(), "GET", null, CacheType.DISABLED, false, null);
    mapiService().exec(this.getPromoDeskCouponRequest, this);
    showProgressDialog("正在获取抵用券...");
  }

  class PromoDeskCouponAdapter extends BasicAdapter
  {
    public final Object UNUSABLECOUPON = new Object();
    private ArrayList<DPObject> dpPromoDeskCouponList = new ArrayList();
    private ArrayList<DPObject> dpPromoDeskCouponUnusableList = new ArrayList();
    private String errorMsg = null;
    private boolean isEnd = true;
    private int nextStartIndex = 0;

    public PromoDeskCouponAdapter()
    {
    }

    private boolean hasUnusableCoupons()
    {
      if (this.dpPromoDeskCouponUnusableList == null);
      do
        return false;
      while (this.dpPromoDeskCouponUnusableList.size() <= 0);
      return true;
    }

    public void appendCouponList(DPObject paramDPObject)
    {
      DPObject[] arrayOfDPObject1 = paramDPObject.getArray("List");
      DPObject[] arrayOfDPObject2 = paramDPObject.getArray("UnavailableList");
      this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
      this.isEnd = paramDPObject.getBoolean("IsEnd");
      if (arrayOfDPObject1 != null)
      {
        if (this.nextStartIndex == 0)
          this.dpPromoDeskCouponList.clear();
        this.dpPromoDeskCouponList.addAll(Arrays.asList(arrayOfDPObject1));
      }
      if ((arrayOfDPObject2 != null) && (arrayOfDPObject2.length != 0) && (this.dpPromoDeskCouponUnusableList.size() == 0))
        this.dpPromoDeskCouponUnusableList.addAll(Arrays.asList(arrayOfDPObject2));
      if ((!PayDiscountSelectActivity.this.canUseHongBaoPromo) && (this.dpPromoDeskCouponList.size() == 0))
        PayDiscountSelectActivity.this.useCodeSelected = true;
    }

    public int getCount()
    {
      if (PayDiscountSelectActivity.this.canUseCouponPromo)
      {
        if ((!this.isEnd) || ((this.isEnd) && (hasUnusableCoupons())))
          return this.dpPromoDeskCouponList.size() + 1;
        return this.dpPromoDeskCouponList.size();
      }
      return 0;
    }

    public int getCouponCount()
    {
      if (this.dpPromoDeskCouponList == null)
        return 0;
      return this.dpPromoDeskCouponList.size();
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.dpPromoDeskCouponList.size())
        return this.dpPromoDeskCouponList.get(paramInt);
      if ((this.isEnd) && (hasUnusableCoupons()))
        return this.UNUSABLECOUPON;
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
      if (DPObjectUtils.isDPObjectof(localObject, "PromoDeskCoupon"))
      {
        DPObject localDPObject = (DPObject)localObject;
        if ((paramView instanceof PromoDeskCouponItem))
        {
          paramView = (PromoDeskCouponItem)paramView;
          localObject = paramView;
          if (paramView == null)
            localObject = (PromoDeskCouponItem)LayoutInflater.from(PayDiscountSelectActivity.this.mContext).inflate(R.layout.promodeskcoupon_list_item, paramViewGroup, false);
          if (!((PromoDeskCouponItem)localObject).setPromoDeskCoupon(localDPObject, PayDiscountSelectActivity.this.selectedCouponPromo, PayDiscountSelectActivity.this.dpPromoProductList))
            break label125;
          ((PromoDeskCouponItem)localObject).setEnabled(true);
          ((PromoDeskCouponItem)localObject).setClickable(true);
        }
        while (true)
        {
          ((PromoDeskCouponItem)localObject).setOnClickListener(new PayDiscountSelectActivity.PromoDeskCouponAdapter.1(this, localDPObject));
          return localObject;
          paramView = null;
          break;
          label125: ((PromoDeskCouponItem)localObject).setEnabled(false);
          ((PromoDeskCouponItem)localObject).setClickable(false);
        }
      }
      if (localObject == LOADING)
      {
        if ((this.errorMsg == null) && (!this.isEnd))
          PayDiscountSelectActivity.this.requestGetPromoDeskCoupon();
        return getLoadingView(paramViewGroup, paramView);
      }
      if (localObject == this.UNUSABLECOUPON)
      {
        paramView = (NovaRelativeLayout)LayoutInflater.from(PayDiscountSelectActivity.this.mContext).inflate(R.layout.more_unusable_coupons_item, paramViewGroup, false);
        ((TextView)paramView.findViewById(R.id.title)).setText("您还有其他点评抵用券，但该订单不可用");
        paramView.setOnClickListener(new PayDiscountSelectActivity.PromoDeskCouponAdapter.2(this));
        return paramView;
      }
      return (View)getFailedView(this.errorMsg, new PayDiscountSelectActivity.PromoDeskCouponAdapter.3(this), paramViewGroup, paramView);
    }

    public boolean isEnabled(int paramInt)
    {
      int k = 0;
      Object localObject = getItem(paramInt);
      int j = k;
      if (DPObjectUtils.isDPObjectof(localObject, "PromoDeskCoupon"))
      {
        localObject = (DPObject)localObject;
        double d3 = ((DPObject)localObject).getDouble("OrderPriceLimit");
        int i = ((DPObject)localObject).getInt("ProductCode");
        boolean bool = ((DPObject)localObject).getBoolean("CanUse");
        double d1 = 0.0D;
        paramInt = 0;
        while (paramInt < PayDiscountSelectActivity.this.dpPromoProductList.size())
        {
          localObject = (DPObject)PayDiscountSelectActivity.this.dpPromoProductList.get(paramInt);
          double d2 = d1;
          if (((DPObject)localObject).getBoolean("Selected"))
          {
            d2 = d1;
            if (((DPObject)localObject).getInt("ProductCode") == i)
              d2 = d1 + ((DPObject)localObject).getInt("Quantity") * ((DPObject)localObject).getDouble("Price");
          }
          paramInt += 1;
          d1 = d2;
        }
        j = k;
        if (bool)
        {
          j = k;
          if (d1 >= Double.valueOf(d3).doubleValue())
            j = 1;
        }
      }
      return j;
    }

    public void setErrorMsg(SimpleMsg paramSimpleMsg)
    {
      if (paramSimpleMsg == null);
      for (paramSimpleMsg = "请求失败，请稍后再试"; ; paramSimpleMsg = paramSimpleMsg.content())
      {
        this.errorMsg = paramSimpleMsg;
        PayDiscountSelectActivity.this.updateView();
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
 * Qualified Name:     com.dianping.tuan.activity.PayDiscountSelectActivity
 * JD-Core Version:    0.6.0
 */
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
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PickDiscountActivity extends BaseTuanPtrListActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String TAG = PickDiscountActivity.class.getSimpleName();
  protected final DateFormat VALID_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
  protected Adapter adapter;
  protected RadioButton codeCheckBtn;
  protected View codeInput;
  protected String discountCode;
  protected String discountId;
  protected DPObject dpDiscount;
  protected DPObject dpProduct;
  protected String orderId;
  protected String productType;
  protected EditText promoEditText;
  protected MApiRequest verifyCouponCodeRequest;

  private void initView()
  {
    View localView = getLayoutInflater().inflate(R.layout.discount_fragment_list_v2_header, this.listView, false);
    localView.findViewById(R.id.check_code_btn).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PickDiscountActivity.this.verifyCouponCode(PickDiscountActivity.this.promoEditText.getText().toString());
      }
    });
    this.codeInput = localView.findViewById(R.id.code_input);
    this.promoEditText = ((EditText)localView.findViewById(R.id.promo_code));
    this.promoEditText.setOnKeyListener(new View.OnKeyListener()
    {
      public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
      {
        if (paramInt == 66)
        {
          PickDiscountActivity.this.verifyCouponCode(PickDiscountActivity.this.promoEditText.getText().toString());
          return true;
        }
        return false;
      }
    });
    localView.findViewById(R.id.code_switcher).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = PickDiscountActivity.this.codeCheckBtn;
        if (!PickDiscountActivity.this.codeCheckBtn.isChecked());
        for (boolean bool = true; ; bool = false)
        {
          paramView.setChecked(bool);
          return;
        }
      }
    });
    this.listView.setHeaderDividersEnabled(false);
    this.listView.addHeaderView(localView);
    this.listView.setBackgroundResource(R.drawable.main_background);
    this.listView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.adapter = new Adapter(this);
    this.listView.setAdapter(this.adapter);
    this.codeCheckBtn = ((RadioButton)localView.findViewById(R.id.check));
    this.codeCheckBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
      {
        paramCompoundButton = PickDiscountActivity.this.codeInput;
        if (paramBoolean);
        for (int i = 0; ; i = 8)
        {
          paramCompoundButton.setVisibility(i);
          PickDiscountActivity.this.uncheckDiscount();
          return;
        }
      }
    });
    if ((this.dpDiscount == null) || (TextUtils.isEmpty(this.dpDiscount.getString("DiscountCode"))))
    {
      this.codeCheckBtn.setChecked(false);
      return;
    }
    this.codeCheckBtn.setChecked(true);
    this.promoEditText.setText(this.dpDiscount.getString("DiscountCode"));
  }

  private void uncheckDiscount()
  {
    this.discountId = "null";
    onDiscountSelected(null);
    this.adapter.notifyDataSetChanged();
  }

  private void verifyCouponCode(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      Toast.makeText(this, "请输入优惠代码", 0).show();
      return;
    }
    if (this.verifyCouponCodeRequest != null)
    {
      Log.i(TAG, "already requesting");
      return;
    }
    this.discountCode = paramString;
    this.verifyCouponCodeRequest = BasicMApiRequest.mapiPost("http://api.p.dianping.com/verifyDiscountCode.pay", new String[] { "producttype", this.productType, "token", accountService().token(), "code", paramString, "orderid", this.orderId });
    mapiService().exec(this.verifyCouponCodeRequest, this);
    showProgressDialog("正在验证优惠代码...");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.orderId = getIntent().getStringExtra("orderid");
    this.dpProduct = ((DPObject)getIntent().getParcelableExtra("product"));
    this.dpDiscount = ((DPObject)getIntent().getParcelableExtra("discount"));
    if ((this.orderId == null) || (this.dpProduct == null))
    {
      finish();
      return;
    }
    this.productType = (this.dpProduct.getInt("ProductType") + "");
    if (this.dpDiscount != null)
      this.discountId = (this.dpDiscount.getInt("ID") + "");
    initView();
  }

  public void onDestroy()
  {
    this.adapter.cancelLoad();
    if (this.verifyCouponCodeRequest != null)
    {
      mapiService().abort(this.verifyCouponCodeRequest, this, true);
      this.verifyCouponCodeRequest = null;
    }
    super.onDestroy();
  }

  protected void onDiscountSelected(DPObject paramDPObject)
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("discount", paramDPObject);
    setResult(-1, localIntent);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if ((paramAdapterView instanceof DPObject))
    {
      paramView = String.valueOf(((DPObject)paramAdapterView).getInt("ID"));
      if (paramView.equalsIgnoreCase(this.discountId))
        uncheckDiscount();
    }
    else
    {
      return;
    }
    this.discountId = paramView;
    onDiscountSelected((DPObject)paramAdapterView);
    finish();
  }

  public void onPause()
  {
    super.onPause();
    KeyboardUtils.hideKeyboard(this.listView);
  }

  public void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
    if (this.verifyCouponCodeRequest != null)
      this.verifyCouponCodeRequest = null;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    paramMApiResponse = paramMApiResponse.message();
    paramMApiResponse = paramMApiResponse.title() + ":" + paramMApiResponse.content();
    if (this.verifyCouponCodeRequest == paramMApiRequest)
    {
      this.verifyCouponCodeRequest = null;
      Toast.makeText(this, paramMApiResponse, 0).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      if (this.verifyCouponCodeRequest == paramMApiRequest)
      {
        this.verifyCouponCodeRequest = null;
        if (paramMApiResponse != null)
        {
          onDiscountSelected(paramMApiResponse.edit().putString("DiscountCode", this.discountCode).generate());
          finish();
        }
      }
    }
  }

  class Adapter extends BasicLoadAdapter
  {
    public Adapter(Context arg2)
    {
      super();
    }

    public MApiRequest createRequest(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://api.p.dianping.com/");
      localStringBuilder.append("discountlist.pay");
      localStringBuilder.append("?orderid=").append(PickDiscountActivity.this.orderId);
      localStringBuilder.append("&producttype=").append(PickDiscountActivity.this.productType);
      localStringBuilder.append("&token=").append(PickDiscountActivity.this.accountService().token());
      localStringBuilder.append("&start=").append(paramInt);
      return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    }

    protected String emptyMessage()
    {
      return "没有满足使用条件的抵用券";
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = null;
      Date localDate = null;
      if (PickDiscountActivity.this.isDPObjectof(paramDPObject, "Discount"))
      {
        localObject = localDate;
        if (paramView != null)
        {
          localObject = localDate;
          if (getItemViewType(paramInt) == 0)
            localObject = paramView;
        }
        if (localObject != null)
          break label324;
        paramView = new PickDiscountActivity.ViewHolder();
        localObject = LayoutInflater.from(PickDiscountActivity.this).inflate(R.layout.discount_list_item_1, paramViewGroup, false);
        paramView.priceView = ((TextView)((View)localObject).findViewById(R.id.price));
        paramView.titleView = ((TextView)((View)localObject).findViewById(16908310));
        paramView.validView = ((TextView)((View)localObject).findViewById(R.id.valid_time));
        paramView.extraView = ((TextView)((View)localObject).findViewById(R.id.extra));
        paramView.radioButton = ((RadioButton)((View)localObject).findViewById(R.id.check));
        ((View)localObject).setTag(paramView);
      }
      while (true)
      {
        paramView.reset();
        paramViewGroup = new Date(paramDPObject.getTime("Date"));
        localDate = new Date(paramDPObject.getTime("BeginDate"));
        paramView.priceView.setText("￥" + PriceFormatUtils.formatPrice(paramDPObject.getString("Price")));
        paramView.titleView.setText(paramDPObject.getString("Title"));
        paramView.validView.setText(PickDiscountActivity.this.VALID_DATE.format(localDate) + " 至 " + PickDiscountActivity.this.VALID_DATE.format(paramViewGroup) + "有效");
        if (!String.valueOf(paramDPObject.getInt("ID")).equalsIgnoreCase(PickDiscountActivity.this.discountId))
          break;
        paramView.radioButton.setChecked(true);
        return localObject;
        label324: paramView = (PickDiscountActivity.ViewHolder)((View)localObject).getTag();
      }
      paramView.radioButton.setChecked(false);
      return (View)localObject;
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
 * Qualified Name:     com.dianping.tuan.activity.PickDiscountActivity
 * JD-Core Version:    0.6.0
 */
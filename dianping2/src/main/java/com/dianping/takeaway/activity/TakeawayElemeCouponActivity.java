package com.dianping.takeaway.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.base.widget.TableView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.takeaway.util.TakeawayPreferencesManager;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TakeawayElemeCouponActivity extends NovaActivity
{
  private static final int VIEW_TYPE_COUNT = 2;
  private static final int VIEW_UNUSABLE = 1;
  private static final int VIEW_USABLE = 0;
  private final int RESULT_CODE_DELIVERYDETAIL_ELEME_COUPON_COMMON = 81;
  private final int RESULT_CODE_DELIVERYDETAIL_ELEME_COUPON_PHONE_CHANGE = 82;
  private int availableCouponAmount;
  private DPObject[] availableCouponList;
  private boolean canUseThirdPartyCode;
  private String cartId;
  private RelativeLayout changeMobileLayout;
  private CheckBox checkedCheckBox;
  private RMBLabelItem checkedPrice;
  private EditText codeEditText;
  private CheckBox codeUseCheckBox;
  private RelativeLayout codeUseCheckBoxLayout;
  private View codeUseDivider;
  private RelativeLayout codeUseLayout;
  private View couponDivider;
  private DPObject couponList;
  private TableView couponListView;
  private TextView couponTitle;
  private String elemePhone;
  private String elemeToken;
  private Context mContext;
  private TextView mobileTitle;
  private TextView notUseCodeTextView;
  private String promoCode;
  private double rmbValue;
  private String thirdpartycouponstr;
  private int thirdpartycoupontype;
  private int unavailableCouponAmount;
  private DPObject[] unavailableCouponList;
  private Button usePromoCodeButton;
  private MApiRequest verifyElemeCodeRequest;

  private void exit()
  {
    Intent localIntent = new Intent();
    if ((!TextUtils.isEmpty(this.thirdpartycouponstr)) && (this.thirdpartycoupontype == 1) && (!this.codeUseCheckBox.isChecked()))
    {
      localIntent.putExtra("thirdpartycouponstr", "");
      localIntent.putExtra("thirdpartycoupontype", 0);
    }
    setResult(81, localIntent);
    finish();
  }

  private void setupMobileLayout()
  {
    this.changeMobileLayout = ((RelativeLayout)super.findViewById(R.id.change_mobile_layout));
    this.mobileTitle = ((TextView)super.findViewById(R.id.mobile_title));
    this.mobileTitle.setText("手机号" + this.elemePhone.substring(0, 3) + "****" + this.elemePhone.substring(7) + "的优惠券");
    this.changeMobileLayout.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayElemeCouponActivity.this.setResult(82);
        TakeawayElemeCouponActivity.this.finish();
      }
    });
  }

  private void setupViews()
  {
    this.notUseCodeTextView = ((TextView)super.findViewById(R.id.code_cannot_use));
    this.codeUseCheckBox = ((CheckBox)super.findViewById(R.id.codeuse_checkbox));
    this.codeUseCheckBoxLayout = ((RelativeLayout)super.findViewById(R.id.codeuse_checkbox_layout));
    this.codeUseLayout = ((RelativeLayout)super.findViewById(R.id.codeuse_layout));
    this.codeUseDivider = super.findViewById(R.id.codeuse_divider);
    this.usePromoCodeButton = ((Button)super.findViewById(R.id.use_promo_code));
    this.codeEditText = ((EditText)super.findViewById(R.id.code_edittext));
    this.couponTitle = ((TextView)super.findViewById(R.id.coupon_title));
    this.couponDivider = super.findViewById(R.id.coupon_divider);
    this.couponListView = ((TableView)super.findViewById(R.id.coupon_listview));
    this.codeUseCheckBox.setClickable(false);
    this.codeUseCheckBox.setFocusable(false);
    this.codeUseCheckBox.setFocusableInTouchMode(false);
    if (this.availableCouponAmount + this.unavailableCouponAmount <= 0)
    {
      this.couponTitle.setVisibility(8);
      this.couponListView.setVisibility(8);
      this.couponDivider.setVisibility(8);
    }
    if (this.canUseThirdPartyCode)
    {
      this.notUseCodeTextView.setVisibility(8);
      this.codeUseCheckBox.setVisibility(0);
      this.codeUseCheckBoxLayout.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (TakeawayElemeCouponActivity.this.codeUseCheckBox.isChecked())
          {
            TakeawayElemeCouponActivity.this.codeUseCheckBox.setChecked(false);
            TakeawayElemeCouponActivity.this.codeUseLayout.setVisibility(8);
            TakeawayElemeCouponActivity.this.codeUseDivider.setVisibility(8);
          }
          do
          {
            return;
            TakeawayElemeCouponActivity.this.codeUseCheckBox.setChecked(true);
            TakeawayElemeCouponActivity.this.codeUseLayout.setVisibility(0);
            TakeawayElemeCouponActivity.this.codeUseDivider.setVisibility(0);
          }
          while (TakeawayElemeCouponActivity.this.checkedCheckBox == null);
          TakeawayElemeCouponActivity.this.checkedCheckBox.setChecked(false);
          TakeawayElemeCouponActivity.this.checkedPrice.setRMBLabelStyle6(false, TakeawayElemeCouponActivity.this.getResources().getColor(R.color.deep_gray));
          TakeawayElemeCouponActivity.this.checkedPrice.setRMBLabelValue(-TakeawayElemeCouponActivity.this.rmbValue);
        }
      });
      if ((this.thirdpartycoupontype == 1) && (!TextUtils.isEmpty(this.thirdpartycouponstr)))
      {
        this.codeUseCheckBox.setChecked(true);
        this.codeUseLayout.setVisibility(0);
        this.codeUseDivider.setVisibility(0);
        this.codeEditText.setText(this.thirdpartycouponstr);
      }
      this.usePromoCodeButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TakeawayElemeCouponActivity.access$602(TakeawayElemeCouponActivity.this, TakeawayElemeCouponActivity.this.codeEditText.getText().toString());
          if (TextUtils.isEmpty(TakeawayElemeCouponActivity.this.promoCode))
          {
            TakeawayElemeCouponActivity.this.showToast("优惠码不能为空");
            return;
          }
          TakeawayElemeCouponActivity.this.verifyElemeCode();
        }
      });
      return;
    }
    this.notUseCodeTextView.setVisibility(0);
    this.codeUseCheckBoxLayout.setClickable(false);
    this.codeUseLayout.setVisibility(8);
    this.codeUseCheckBox.setVisibility(8);
  }

  private void verifyElemeCode()
  {
    if (this.verifyElemeCodeRequest != null)
      mapiService().abort(this.verifyElemeCodeRequest, null, true);
    Uri.Builder localBuilder = Uri.parse("http://waimai.api.dianping.com/verifyelemecode.ta").buildUpon();
    localBuilder.appendQueryParameter("phone", this.elemePhone);
    localBuilder.appendQueryParameter("token", this.elemeToken);
    localBuilder.appendQueryParameter("code", this.promoCode);
    localBuilder.appendQueryParameter("cartid", this.cartId);
    this.verifyElemeCodeRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.verifyElemeCodeRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayElemeCouponActivity.access$902(TakeawayElemeCouponActivity.this, null);
        String str = "验证失败，请重试";
        paramMApiRequest = str;
        if (paramMApiResponse != null)
        {
          paramMApiRequest = str;
          if (paramMApiResponse.message() != null)
          {
            paramMApiRequest = str;
            if (!TextUtils.isEmpty(paramMApiResponse.message().content()))
              paramMApiRequest = paramMApiResponse.message().content();
          }
        }
        TakeawayElemeCouponActivity.this.showToast(paramMApiRequest);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayElemeCouponActivity.access$902(TakeawayElemeCouponActivity.this, null);
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        if (paramMApiRequest.getInt("Code") == 1)
        {
          paramMApiRequest = new Intent();
          paramMApiRequest.putExtra("thirdpartycouponstr", TakeawayElemeCouponActivity.this.promoCode);
          paramMApiRequest.putExtra("thirdpartycoupontype", 1);
          TakeawayElemeCouponActivity.this.setResult(81, paramMApiRequest);
          TakeawayElemeCouponActivity.this.finish();
          return;
        }
        TakeawayElemeCouponActivity.this.showToast(paramMApiRequest.getString("Content"));
        TakeawayElemeCouponActivity.this.codeEditText.setText("");
      }
    });
  }

  public void onBackPressed()
  {
    exit();
    super.onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.takeaway_eleme_coupon_layout);
    super.getWindow().setBackgroundDrawable(null);
    this.mContext = this;
    if (paramBundle == null)
    {
      this.cartId = super.getStringParam("cartid");
      this.canUseThirdPartyCode = super.getBooleanParam("canusethirdpartycode");
      this.couponList = super.getObjectParam("elemecouponlist");
      this.thirdpartycouponstr = super.getStringParam("thirdpartycouponstr");
    }
    for (this.thirdpartycoupontype = super.getIntParam("thirdpartycoupontype"); ; this.thirdpartycoupontype = paramBundle.getInt("thirdpartycoupontype"))
    {
      paramBundle = TakeawayPreferencesManager.getTakeawayDeliveryPreferences(this.mContext);
      this.elemePhone = paramBundle.getString("eleme_phone", null);
      this.elemeToken = paramBundle.getString("eleme_token", null);
      setupMobileLayout();
      if (this.couponList != null)
      {
        this.availableCouponList = this.couponList.getArray("List");
        this.unavailableCouponList = this.couponList.getArray("UnavailableList");
        if (this.availableCouponList != null)
          this.availableCouponAmount = this.availableCouponList.length;
        if (this.unavailableCouponList != null)
          this.unavailableCouponAmount = this.unavailableCouponList.length;
      }
      setupViews();
      this.couponListView.setAdapter(new ElemeCouponAdapter());
      return;
      this.cartId = paramBundle.getString("cartid");
      this.canUseThirdPartyCode = paramBundle.getBoolean("canusethirdpartycode");
      this.couponList = ((DPObject)paramBundle.getParcelable("taelemecouponlist"));
      this.thirdpartycouponstr = paramBundle.getString("thirdpartycouponstr");
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.verifyElemeCodeRequest != null)
    {
      mapiService().abort(this.verifyElemeCodeRequest, null, true);
      this.verifyElemeCodeRequest = null;
    }
  }

  protected void onLeftTitleButtonClicked()
  {
    exit();
    super.onLeftTitleButtonClicked();
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.cartId = paramBundle.getString("cartid");
    this.canUseThirdPartyCode = paramBundle.getBoolean("canusethirdpartycode");
    this.couponList = ((DPObject)paramBundle.getParcelable("taelemecouponlist"));
    this.thirdpartycouponstr = paramBundle.getString("thirdpartycouponstr");
    this.thirdpartycoupontype = paramBundle.getInt("thirdpartycoupontype");
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("cartid", this.cartId);
    paramBundle.putBoolean("canusethirdpartycode", this.canUseThirdPartyCode);
    paramBundle.putParcelable("taelemecouponlist", this.couponList);
    paramBundle.putString("thirdpartycouponstr", this.thirdpartycouponstr);
    paramBundle.putInt("thirdpartycoupontype", this.thirdpartycoupontype);
  }

  class ElemeCouponAdapter extends BaseAdapter
  {
    ElemeCouponAdapter()
    {
    }

    public int getCount()
    {
      return TakeawayElemeCouponActivity.this.availableCouponAmount + TakeawayElemeCouponActivity.this.unavailableCouponAmount;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < TakeawayElemeCouponActivity.this.availableCouponAmount)
        return TakeawayElemeCouponActivity.this.availableCouponList[paramInt];
      return TakeawayElemeCouponActivity.this.unavailableCouponList[(paramInt - TakeawayElemeCouponActivity.this.availableCouponAmount)];
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public int getItemViewType(int paramInt)
    {
      if (paramInt < TakeawayElemeCouponActivity.this.availableCouponAmount)
        return 0;
      return 1;
    }

    public View getUnUsableView(DPObject paramDPObject, View paramView, ViewGroup paramViewGroup)
    {
      View localView = paramView;
      if (paramView == null)
        localView = LayoutInflater.from(TakeawayElemeCouponActivity.this.mContext).inflate(R.layout.takeaway_eleme_unusable_coupon_item, paramViewGroup, false);
      paramView = (TextView)localView.findViewById(R.id.price_textview);
      paramViewGroup = (TextView)localView.findViewById(R.id.date_textview);
      paramView.setText(paramDPObject.getString("Title"));
      paramViewGroup.setText(paramDPObject.getString("ExpireDate"));
      return localView;
    }

    public View getUsableView(DPObject paramDPObject, View paramView, ViewGroup paramViewGroup)
    {
      View localView = paramView;
      if (paramView == null)
        localView = TakeawayElemeCouponActivity.this.getLayoutInflater().inflate(R.layout.takeaway_eleme_usable_coupon_item, paramViewGroup, false);
      paramView = (RMBLabelItem)localView.findViewById(R.id.price_textview);
      paramViewGroup = (TextView)localView.findViewById(R.id.date_textview);
      CheckBox localCheckBox = (CheckBox)localView.findViewById(R.id.couponuse_checkbox);
      RMBLabelItem localRMBLabelItem = (RMBLabelItem)localView.findViewById(R.id.price);
      paramView.setRMBLabelStyle6(false, TakeawayElemeCouponActivity.this.getResources().getColor(R.color.deep_gray));
      paramView.setRMBLabelValue(Double.parseDouble(paramDPObject.getString("Title")));
      paramViewGroup.setText(paramDPObject.getString("ExpireDate"));
      localRMBLabelItem.setRMBLabelStyle6(false, TakeawayElemeCouponActivity.this.getResources().getColor(R.color.deep_gray));
      localRMBLabelItem.setRMBLabelValue(Double.parseDouble(paramDPObject.getString("Price")));
      if ((TakeawayElemeCouponActivity.this.thirdpartycoupontype == 2) && (TakeawayElemeCouponActivity.this.thirdpartycouponstr != null) && (TakeawayElemeCouponActivity.this.thirdpartycouponstr.equals(paramDPObject.getString("PromoCipher"))))
      {
        localCheckBox.setChecked(true);
        TakeawayElemeCouponActivity.access$502(TakeawayElemeCouponActivity.this, -Double.parseDouble(paramDPObject.getString("Price")));
        localRMBLabelItem.setRMBLabelValue(TakeawayElemeCouponActivity.this.rmbValue);
        TakeawayElemeCouponActivity.access$402(TakeawayElemeCouponActivity.this, localRMBLabelItem);
        TakeawayElemeCouponActivity.access$302(TakeawayElemeCouponActivity.this, localCheckBox);
      }
      localView.setOnClickListener(new View.OnClickListener(localCheckBox, paramDPObject)
      {
        public void onClick(View paramView)
        {
          TakeawayElemeCouponActivity.this.codeUseCheckBox.setChecked(false);
          paramView = new Intent();
          if (this.val$couponUseCheckBox.isChecked())
          {
            this.val$couponUseCheckBox.setChecked(false);
            paramView.putExtra("thirdpartycouponstr", "");
            paramView.putExtra("thirdpartycoupontype", 0);
          }
          while (true)
          {
            TakeawayElemeCouponActivity.this.setResult(81, paramView);
            TakeawayElemeCouponActivity.this.finish();
            return;
            this.val$couponUseCheckBox.setChecked(true);
            paramView.putExtra("thirdpartycouponstr", this.val$item.getString("PromoCipher"));
            paramView.putExtra("thirdpartycoupontype", 2);
          }
        }
      });
      localCheckBox.setClickable(false);
      localCheckBox.setFocusable(false);
      localCheckBox.setFocusableInTouchMode(false);
      return localView;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = (DPObject)getItem(paramInt);
      if (getItemViewType(paramInt) == 0)
        return getUsableView(localDPObject, paramView, paramViewGroup);
      return getUnUsableView(localDPObject, paramView, paramViewGroup);
    }

    public int getViewTypeCount()
    {
      return 2;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayElemeCouponActivity
 * JD-Core Version:    0.6.0
 */
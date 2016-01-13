package com.dianping.wed.weddingfeast.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.DeviceUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.util.WedBookingUtil;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WeddingFeastBookingActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static int WEDDING_REQ_STATUS_DONE;
  private static int WEDDING_REQ_STATUS_ERROR;
  private static int WEDDING_REQ_STATUS_INIT = 0;
  private static int WEDDING_REQ_STATUS_LOADING = 1;
  LinearLayout bg_saw;
  DPObject[] bookingPromoList;
  boolean isDefaultFocus = true;
  boolean isExpanded = true;
  boolean isSubmitSuccess = false;
  private MApiRequest mGetBookingPromoReq;
  private MApiRequest mGetHistoryReq;
  boolean mIsRepeatBook = false;
  private MApiRequest mPostWeddingBookReq;
  private int mWeddingReqStatus = WEDDING_REQ_STATUS_INIT;
  String phoneNum;
  EditText phoneNumEdit;
  LinearLayout prefer_promo;
  LinearLayout rebate_promo;
  String shopId;
  String shopName;
  NovaButton submitBtn;
  ArrayList<DPObject> weddingPromoList = new ArrayList();

  static
  {
    WEDDING_REQ_STATUS_DONE = 2;
    WEDDING_REQ_STATUS_ERROR = 3;
  }

  private void sendHistoryRequest()
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/wedding/weddinghotelbookinghistory.bin?");
    localStringBuffer.append("dpid=").append(DeviceUtils.dpid());
    localStringBuffer.append("&userid=").append(getUserId());
    localStringBuffer.append("&type=1");
    this.mGetHistoryReq = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    localMApiService.exec(this.mGetHistoryReq, this);
  }

  private void setSubmitButtonState()
  {
    if ((TextUtils.isEmpty(this.phoneNumEdit.getText().toString())) || (this.phoneNumEdit.getText().toString().length() < 11) || (!this.phoneNumEdit.getText().toString().startsWith("1")))
    {
      this.submitBtn.setClickable(false);
      this.submitBtn.setEnabled(false);
      return;
    }
    this.submitBtn.setClickable(true);
    this.submitBtn.setEnabled(true);
  }

  private void setupView()
  {
    super.setContentView(R.layout.wedding_feast_booking);
    this.prefer_promo = ((LinearLayout)findViewById(R.id.prefer_promo));
    this.rebate_promo = ((LinearLayout)findViewById(R.id.rebate_promo));
    this.bg_saw = ((LinearLayout)findViewById(R.id.bg_saw));
    int j = getResources().getDisplayMetrics().widthPixels;
    int i = 0;
    while (i < j / 30 + 1)
    {
      ImageView localImageView = new ImageView(this);
      localImageView.setLayoutParams(new LinearLayout.LayoutParams(30, 16));
      localImageView.setImageResource(R.drawable.bg_sawtooth);
      this.bg_saw.addView(localImageView);
      i += 1;
    }
    this.phoneNumEdit = ((EditText)findViewById(R.id.phoneNum));
    this.phoneNumEdit.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramView, boolean paramBoolean)
      {
        if (paramBoolean)
          GAHelper.instance().contextStatisticsEvent(WeddingFeastBookingActivity.this.getApplicationContext(), "mobile", WeddingFeastBookingActivity.this.gaExtra, "tap");
      }
    });
    this.phoneNumEdit.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
        WeddingFeastBookingActivity.this.setSubmitButtonState();
      }
    });
    this.submitBtn = ((NovaButton)findViewById(R.id.find_conditions_go_btn));
    this.submitBtn.setGAString("submit");
    this.submitBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        WeddingFeastBookingActivity.this.checkAndSubmitBookingInfo();
      }
    });
    setSubmitButtonState();
  }

  void checkAndSubmitBookingInfo()
  {
    Object localObject = new ForegroundColorSpan(-16777216);
    this.phoneNum = this.phoneNumEdit.getText().toString();
    if (this.phoneNum.length() < 11)
    {
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder("手机号码必须为11位");
      localSpannableStringBuilder.setSpan(localObject, 0, "手机号码必须为11位".length(), 0);
      this.phoneNumEdit.setError(localSpannableStringBuilder);
      return;
    }
    if (this.mPostWeddingBookReq == null)
    {
      localObject = new HashMap();
      ((Map)localObject).put("bookingUserMobile", this.phoneNumEdit.getText().toString());
      ((Map)localObject).put("bookingType", "5");
      ((Map)localObject).put("shopID", this.shopId);
      ((Map)localObject).put("shopName", this.shopName);
      ((Map)localObject).put("type", "1");
      this.mPostWeddingBookReq = BasicMApiRequest.mapiPost(WedBookingUtil.getBookingUrl("http://m.api.dianping.com/wedding/weddinghotelbooking.bin", (Map)localObject), new String[0]);
    }
    mapiService().exec(this.mPostWeddingBookReq, this);
    showProgressDialog("正在提交");
  }

  public void getBookingPromo()
  {
    if (this.mGetBookingPromoReq == null)
    {
      StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/wedding/getbookingpromo.bin");
      localStringBuilder.append("?shopID=").append(this.shopId).append("&shopName=");
      this.mGetBookingPromoReq = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    }
    mapiService().exec(this.mGetBookingPromoReq, this);
    this.mWeddingReqStatus = WEDDING_REQ_STATUS_LOADING;
    updateBookingPromoView();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Uri localUri = getIntent().getData();
    if (localUri != null)
    {
      this.shopId = localUri.getQueryParameter("shopid");
      this.shopName = localUri.getQueryParameter("shopname");
    }
    if (paramBundle != null)
    {
      this.shopId = paramBundle.getString("shopid");
      this.shopName = paramBundle.getString("shopname");
    }
    setupView();
    getBookingPromo();
    sendHistoryRequest();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.mPostWeddingBookReq != null)
    {
      mapiService().abort(this.mPostWeddingBookReq, this, true);
      this.mPostWeddingBookReq = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mPostWeddingBookReq)
    {
      this.mPostWeddingBookReq = null;
      dismissDialog();
      if ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (!TextUtils.isEmpty(paramMApiResponse.message().toString())))
      {
        this.mIsRepeatBook = true;
        paramMApiRequest = Toast.makeText(this, paramMApiResponse.message().toString(), 1);
        paramMApiRequest.setGravity(17, 0, 0);
        paramMApiRequest.show();
      }
    }
    do
    {
      return;
      paramMApiRequest = Toast.makeText(this, "网络不给力啊，请稍后再试试", 1);
      paramMApiRequest.setGravity(17, 0, 0);
      paramMApiRequest.show();
      return;
    }
    while ((paramMApiRequest != this.mGetBookingPromoReq) || (!(paramMApiResponse.result() instanceof DPObject[])));
    this.mWeddingReqStatus = WEDDING_REQ_STATUS_ERROR;
    updateBookingPromoView();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mPostWeddingBookReq)
    {
      this.isSubmitSuccess = true;
      if (this.gaExtra.shop_id == null)
      {
        if ((this.shopId == null) || ("null".equals(this.shopId)) || ("".equals(this.shopId)))
          break label235;
        if (TextUtils.isDigitsOnly(this.shopId))
          this.gaExtra.shop_id = Integer.valueOf(this.shopId);
      }
      else
      {
        GAHelper.instance().contextStatisticsEvent(getApplicationContext(), "submit_success", this.gaExtra, "tap");
        dismissDialog();
        paramMApiRequest = new StringBuffer("http://m.dianping.com/wed/mobile/hunyan/bookrouter_new?");
        paramMApiRequest.append("shopId=").append(this.shopId).append("&userPhone=").append(this.phoneNumEdit.getText().toString()).append("&act=YY");
        paramMApiRequest = WedBookingUtil.getBookingUrl(paramMApiRequest.toString(), null);
      }
    }
    label235: 
    do
    {
      do
      {
        do
        {
          try
          {
            paramMApiRequest = URLEncoder.encode(paramMApiRequest, "UTF-8");
            paramMApiResponse = new Intent("android.intent.action.VIEW");
            paramMApiResponse.setData(Uri.parse("dianping://weddinghotelweb?url=" + paramMApiRequest));
            startActivity(paramMApiResponse);
            finish();
            return;
            this.gaExtra.shop_id = Integer.valueOf(0);
            break;
            this.gaExtra.shop_id = Integer.valueOf(0);
          }
          catch (java.io.UnsupportedEncodingException paramMApiRequest)
          {
            return;
          }
          if (paramMApiRequest != this.mGetBookingPromoReq)
            break label299;
        }
        while (!(paramMApiResponse.result() instanceof DPObject[]));
        this.bookingPromoList = ((DPObject[])(DPObject[])paramMApiResponse.result());
        this.mWeddingReqStatus = WEDDING_REQ_STATUS_DONE;
        updateBookingPromoView();
        return;
      }
      while (paramMApiRequest != this.mGetHistoryReq);
      this.mGetHistoryReq = null;
      paramMApiRequest = ((DPObject)paramMApiResponse.result()).getString("BookingUserMobile");
    }
    while ((paramMApiRequest == null) || (paramMApiRequest.trim().length() <= 0));
    label299: this.phoneNumEdit.setText(paramMApiRequest);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("shopid", this.shopId);
    paramBundle.putString("shopname", this.shopName);
    super.onSaveInstanceState(paramBundle);
  }

  void updateBookingPromoView()
  {
    if ((this.prefer_promo == null) || (this.rebate_promo == null));
    do
    {
      return;
      this.prefer_promo.removeAllViews();
      this.rebate_promo.removeAllViews();
      if (this.mWeddingReqStatus == WEDDING_REQ_STATUS_LOADING)
      {
        localView1 = LayoutInflater.from(this).inflate(R.layout.loading_item, this.prefer_promo, false);
        this.prefer_promo.addView(localView1);
        localView1 = LayoutInflater.from(this).inflate(R.layout.loading_item, this.rebate_promo, false);
        this.rebate_promo.addView(localView1);
        return;
      }
      if (this.mWeddingReqStatus != WEDDING_REQ_STATUS_ERROR)
        continue;
      localView1 = LayoutInflater.from(this).inflate(R.layout.error_item, this.prefer_promo, false);
      localView2 = LayoutInflater.from(this).inflate(R.layout.error_item, this.rebate_promo, false);
      if ((localView1 instanceof LoadingErrorView))
        ((LoadingErrorView)localView1).setCallBack(new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            WeddingFeastBookingActivity.this.getBookingPromo();
          }
        });
      this.prefer_promo.addView(localView1);
      this.rebate_promo.addView(localView2);
      return;
    }
    while (this.mWeddingReqStatus != WEDDING_REQ_STATUS_DONE);
    Object localObject = new LinearLayout.LayoutParams(-1, -1);
    View localView1 = LayoutInflater.from(this).inflate(R.layout.wedding_feast_perfer_promo, null, false);
    localView1.setLayoutParams((ViewGroup.LayoutParams)localObject);
    View localView2 = LayoutInflater.from(this).inflate(R.layout.wedding_feast_rebate_promo, null, false);
    localView2.setLayoutParams((ViewGroup.LayoutParams)localObject);
    int i = 0;
    label228: String str;
    if (i < this.bookingPromoList.length)
    {
      localObject = this.bookingPromoList[i];
      str = ((DPObject)localObject).getString("ID");
      if (!"优惠".equals(str))
        break label303;
      ((TextView)localView1.findViewById(R.id.content_prefer)).setText(((DPObject)localObject).getString("Name"));
      this.prefer_promo.addView(localView1);
    }
    while (true)
    {
      i += 1;
      break label228;
      break;
      label303: if (!"返利".equals(str))
        continue;
      ((TextView)localView2.findViewById(R.id.content_rebate)).setText(((DPObject)localObject).getString("Name"));
      this.rebate_promo.addView(localView2);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.weddingfeast.activity.WeddingFeastBookingActivity
 * JD-Core Version:    0.6.0
 */
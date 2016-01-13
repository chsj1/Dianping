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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.util.WedBookingUtil;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.message.BasicNameValuePair;

public class WeddingFeastPromoActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private ArrayList<String> expandViewText = new ArrayList();
  private ArrayList<LinearLayout> extraPromoDetailViews = new ArrayList();
  private ArrayList<Boolean> isExpanded = new ArrayList();
  private MApiRequest mGetHistoryReq;
  private MApiRequest mGetPromoReq;
  private MApiRequest mPostPromoReq;
  private String phoneNum;
  private EditText phoneNumEdit;
  private DPObject[] promoList;
  private String shopId;
  private String shopName;
  private NovaButton submitBtn;
  private LinearLayout weddinghotel_promo;

  private void sendGetHistoryReq()
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/wedding/weddinghotelbookinghistory.bin?");
    localStringBuffer.append("dpid=").append(DeviceUtils.dpid());
    localStringBuffer.append("&userid=").append(getUserId());
    localStringBuffer.append("&type=1");
    this.mGetHistoryReq = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    localMApiService.exec(this.mGetHistoryReq, this);
  }

  private void sendGetPromoReq()
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/wedding/weddinghotelpromo.bin?");
    localStringBuffer.append("shopid=").append(this.shopId);
    this.mGetPromoReq = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    localMApiService.exec(this.mGetPromoReq, this);
  }

  private void setExpandState(View paramView1, View paramView2, boolean paramBoolean, String paramString)
  {
    if ((paramView1 == null) || (paramView2 == null))
      return;
    if (!paramBoolean)
    {
      ((TextView)paramView1.findViewById(16908308)).setText("收起");
      ((ImageView)paramView1.findViewById(R.id.arrow)).setImageResource(R.drawable.arrow_up_tuan);
      paramView2.setVisibility(0);
      return;
    }
    ((TextView)paramView1.findViewById(16908308)).setText(paramString);
    ((ImageView)paramView1.findViewById(R.id.arrow)).setImageResource(R.drawable.arrow_down_tuan);
    paramView1.findViewById(16908308).setVisibility(0);
    paramView2.setVisibility(8);
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
    super.setContentView(R.layout.wedding_feast_promo);
    this.weddinghotel_promo = ((LinearLayout)findViewById(R.id.weddinghotel_promo));
    this.phoneNumEdit = ((EditText)findViewById(R.id.phoneNum));
    this.phoneNumEdit.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramView, boolean paramBoolean)
      {
        if (paramBoolean)
          if (WeddingFeastPromoActivity.this.gaExtra.shop_id == null)
          {
            if ((WeddingFeastPromoActivity.this.shopId == null) || ("null".equals(WeddingFeastPromoActivity.this.shopId)) || ("".equals(WeddingFeastPromoActivity.this.shopId)))
              break label132;
            if (!TextUtils.isDigitsOnly(WeddingFeastPromoActivity.this.shopId))
              break label115;
            WeddingFeastPromoActivity.this.gaExtra.shop_id = Integer.valueOf(WeddingFeastPromoActivity.this.shopId);
          }
        while (true)
        {
          GAHelper.instance().contextStatisticsEvent(WeddingFeastPromoActivity.this.getApplicationContext(), "mobile", WeddingFeastPromoActivity.this.gaExtra, "tap");
          return;
          label115: WeddingFeastPromoActivity.this.gaExtra.shop_id = Integer.valueOf(0);
          continue;
          label132: WeddingFeastPromoActivity.this.gaExtra.shop_id = Integer.valueOf(0);
        }
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
        WeddingFeastPromoActivity.this.setSubmitButtonState();
      }
    });
    this.submitBtn = ((NovaButton)findViewById(R.id.find_conditions_go_btn));
    this.submitBtn.setGAString("submit");
    this.submitBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        WeddingFeastPromoActivity.this.checkAndSubmitBookingInfo();
      }
    });
    setSubmitButtonState();
    if ((this.promoList != null) && (this.promoList.length > 0))
    {
      int i = 0;
      while (i < this.promoList.length)
      {
        Object localObject1 = this.promoList[i];
        Object localObject3 = ((DPObject)localObject1).getString("Title");
        Object localObject4 = ((DPObject)localObject1).getString("Desc");
        localObject1 = ((DPObject)localObject1).getStringArray("Detail");
        Object localObject2 = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.wedding_feast_promo_item_layout, null, false);
        ((TextView)((LinearLayout)localObject2).findViewById(R.id.weddinghotel_promo_title)).setText((CharSequence)localObject3);
        ((TextView)((LinearLayout)localObject2).findViewById(R.id.weddinghotel_promo_desc)).setText((CharSequence)localObject4);
        localObject3 = (LinearLayout)((LinearLayout)localObject2).findViewById(R.id.promo_detail);
        localObject4 = (LinearLayout)((LinearLayout)localObject2).findViewById(R.id.extra_promo_detail);
        ((LinearLayout)localObject4).setVisibility(8);
        this.extraPromoDetailViews.add(localObject4);
        this.isExpanded.add(Boolean.valueOf(false));
        ((LinearLayout)localObject4).removeAllViews();
        ((LinearLayout)localObject3).removeAllViews();
        if ((localObject1 != null) && (localObject1.length > 0))
        {
          int j = 0;
          if (j < localObject1.length)
          {
            TextView localTextView = new TextView(this);
            localTextView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            localTextView.setPadding(0, ViewUtils.dip2px(this, 10.0F), 0, ViewUtils.dip2px(this, 10.0F));
            localTextView.setText(localObject1[j]);
            localTextView.setTextSize(2, 17.0F);
            localTextView.setTextColor(getResources().getColor(R.color.light_gray));
            ImageView localImageView = new ImageView(this);
            localImageView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            localImageView.setImageResource(R.drawable.mc_dotted_line);
            if ((j > 1) && (this.promoList.length > 1))
            {
              ((LinearLayout)localObject4).addView(localTextView);
              ((LinearLayout)localObject4).addView(localImageView);
            }
            while (true)
            {
              j += 1;
              break;
              ((LinearLayout)localObject3).addView(localTextView);
              if ((localObject1.length <= 2) && (j == localObject1.length - 1))
                continue;
              ((LinearLayout)localObject3).addView(localImageView);
            }
          }
        }
        this.weddinghotel_promo.addView((View)localObject2);
        if (this.promoList.length > 1)
        {
          if ((localObject1 != null) && (localObject1.length > 2))
          {
            localObject2 = LayoutInflater.from(this).inflate(R.layout.expand_wedding_feast, null, false);
            if (localObject2 != null)
            {
              localObject3 = (TextView)((View)localObject2).findViewById(16908308);
              localObject1 = ((String)this.expandViewText.get(i)).replace("0", localObject1.length - 2 + "");
              ((TextView)localObject3).setText((CharSequence)localObject1);
              ((TextView)localObject3).setPadding(0, ViewUtils.dip2px(this, 10.0F), 0, ViewUtils.dip2px(this, 10.0F));
              ((View)localObject2).findViewById(R.id.line).setVisibility(8);
              ((View)localObject2).setClickable(true);
              ((View)localObject2).setOnClickListener(new MyOnClickListener(i, (String)localObject1));
              this.weddinghotel_promo.addView((View)localObject2);
            }
          }
          localObject1 = new View(this);
          ((View)localObject1).setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(this, 25.0F)));
          ((View)localObject1).setBackgroundColor(getResources().getColor(R.color.common_bk_color));
          this.weddinghotel_promo.addView((View)localObject1);
        }
        i += 1;
      }
    }
  }

  void checkAndSubmitBookingInfo()
  {
    ForegroundColorSpan localForegroundColorSpan = new ForegroundColorSpan(-16777216);
    this.phoneNum = this.phoneNumEdit.getText().toString();
    if (this.phoneNum.length() < 11)
    {
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder("手机号码必须为11位");
      localSpannableStringBuilder.setSpan(localForegroundColorSpan, 0, "手机号码必须为11位".length(), 0);
      this.phoneNumEdit.setError(localSpannableStringBuilder);
      return;
    }
    submitWeddingBookingInfo();
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
    if ((this.shopId == null) && (paramBundle != null))
    {
      this.shopId = paramBundle.getString("shopid");
      this.shopName = paramBundle.getString("shopname");
    }
    this.expandViewText.add("更多0个优惠");
    this.expandViewText.add("更多0个礼品");
    this.expandViewText.add("更多0个优惠");
    sendGetPromoReq();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.mPostPromoReq != null)
    {
      mapiService().abort(this.mPostPromoReq, this, true);
      this.mPostPromoReq = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mPostPromoReq)
    {
      dismissDialog();
      if ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (!TextUtils.isEmpty(paramMApiResponse.message().toString())))
      {
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
    while ((paramMApiRequest != this.mGetPromoReq) || (!(paramMApiResponse.result() instanceof DPObject[])));
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mPostPromoReq)
      if (this.gaExtra.shop_id == null)
      {
        if ((this.shopId == null) || ("null".equals(this.shopId)) || ("".equals(this.shopId)))
          break label230;
        if (TextUtils.isDigitsOnly(this.shopId))
          this.gaExtra.shop_id = Integer.valueOf(this.shopId);
      }
      else
      {
        GAHelper.instance().contextStatisticsEvent(getApplicationContext(), "wedbooking_submit_success", this.gaExtra, "tap");
        dismissDialog();
        paramMApiRequest = new StringBuffer("http://m.dianping.com/wed/mobile/hunyan/bookrouter_new?");
        paramMApiRequest.append("shopId=").append(this.shopId).append("&userPhone=").append(this.phoneNumEdit.getText().toString()).append("&act=YH");
        paramMApiRequest = WedBookingUtil.getBookingUrl(paramMApiRequest.toString(), null);
      }
    label230: 
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
          if (paramMApiRequest != this.mGetPromoReq)
            break label291;
        }
        while (!(paramMApiResponse.result() instanceof DPObject[]));
        this.promoList = ((DPObject[])(DPObject[])paramMApiResponse.result());
        setupView();
        sendGetHistoryReq();
        return;
      }
      while (paramMApiRequest != this.mGetHistoryReq);
      this.mGetHistoryReq = null;
      paramMApiRequest = ((DPObject)paramMApiResponse.result()).getString("BookingUserMobile");
    }
    while ((paramMApiRequest == null) || (paramMApiRequest.trim().length() <= 0));
    label291: this.phoneNumEdit.setText(paramMApiRequest);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("shopid", this.shopId);
    paramBundle.putString("shopname", this.shopName);
    super.onSaveInstanceState(paramBundle);
  }

  public void submitWeddingBookingInfo()
  {
    if (this.mPostPromoReq == null)
    {
      HashMap localHashMap = new HashMap();
      localHashMap.put("bookingUserMobile", this.phoneNumEdit.getText().toString());
      localHashMap.put("bookingType", "10");
      localHashMap.put("shopID", this.shopId);
      localHashMap.put("shopName", this.shopName);
      localHashMap.put("type", "2");
      this.mPostPromoReq = BasicMApiRequest.mapiPost(WedBookingUtil.getBookingUrl("http://m.api.dianping.com/wedding/weddinghotelbooking.bin", localHashMap), new String[0]);
    }
    mapiService().exec(this.mPostPromoReq, this);
    showProgressDialog("正在提交");
  }

  class MyOnClickListener
    implements View.OnClickListener
  {
    int i;
    String text;

    public MyOnClickListener(int paramString, String arg3)
    {
      this.i = paramString;
      Object localObject;
      this.text = localObject;
    }

    public void onClick(View paramView)
    {
      boolean bool = false;
      WeddingFeastPromoActivity.this.setExpandState(paramView, (View)WeddingFeastPromoActivity.this.extraPromoDetailViews.get(this.i), ((Boolean)WeddingFeastPromoActivity.this.isExpanded.get(this.i)).booleanValue(), this.text);
      paramView = new ArrayList();
      paramView.add(new BasicNameValuePair("shopid", WeddingFeastPromoActivity.this.shopId + ""));
      if (!((Boolean)WeddingFeastPromoActivity.this.isExpanded.get(this.i)).booleanValue())
        WeddingFeastPromoActivity.this.statisticsEvent("shopinfow", "shopinfow_multicoupon_more", this.i + 1 + "", 0, paramView);
      while (true)
      {
        paramView = WeddingFeastPromoActivity.this.isExpanded;
        int j = this.i;
        if (!((Boolean)WeddingFeastPromoActivity.this.isExpanded.get(this.i)).booleanValue())
          bool = true;
        paramView.set(j, Boolean.valueOf(bool));
        return;
        WeddingFeastPromoActivity.this.statisticsEvent("shopinfow", "shopinfow_multicoupon_less", this.i + 1 + "", 0, paramView);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.weddingfeast.activity.WeddingFeastPromoActivity
 * JD-Core Version:    0.6.0
 */
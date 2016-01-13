package com.dianping.wed.weddingfeast.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.DeviceUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaTextView;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class WeddingFeastFuzzyRecommendActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private float FONTSCALE;
  private View anim_lay;
  private int[] categoryIdArray;
  private int categoryIds = 0;
  private String[] categorys;
  private String cityId;
  private MApiRequest mGetFuzzyRecommendInfoReq;
  private MApiRequest mGetHistoryReq;
  private int orderCount;
  private String phoneNum;
  private EditText phoneNumEdit;
  private int priceType = 2;
  private String[] prices;
  private String promoInfo;
  private DPObject[] regionList;
  private int regionType = 0;
  private MeasuredGridView regionView;
  private String[] regions;
  private ArrayList<String> regionsNames = new ArrayList();
  private ArrayList<String> selectedRegions = new ArrayList();
  private String shopId;
  private String source = "0";
  private NovaButton submitBtn;
  private int tableType = 0;
  private String[] tables;

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

  private void sendInfoRequest()
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer1 = new StringBuffer("http://m.api.dianping.com/wedding/weddinghotelfuzzyrecommend.bin?");
    StringBuffer localStringBuffer2 = localStringBuffer1.append("cityid=");
    if (this.cityId == null);
    for (Object localObject = Integer.valueOf(cityId()); ; localObject = this.cityId)
    {
      localStringBuffer2.append(localObject);
      this.mGetFuzzyRecommendInfoReq = BasicMApiRequest.mapiGet(localStringBuffer1.toString(), CacheType.DISABLED);
      localMApiService.exec(this.mGetFuzzyRecommendInfoReq, this);
      return;
    }
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
    super.setContentView(R.layout.wedding_feast_fuzzy_recommend);
    this.phoneNumEdit = ((EditText)findViewById(R.id.phoneNum));
    this.phoneNumEdit.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramView, boolean paramBoolean)
      {
        if (paramBoolean)
          if (WeddingFeastFuzzyRecommendActivity.this.gaExtra.shop_id == null)
          {
            if ((WeddingFeastFuzzyRecommendActivity.this.shopId == null) || ("null".equals(WeddingFeastFuzzyRecommendActivity.this.shopId)) || ("".equals(WeddingFeastFuzzyRecommendActivity.this.shopId)))
              break label132;
            if (!TextUtils.isDigitsOnly(WeddingFeastFuzzyRecommendActivity.this.shopId))
              break label115;
            WeddingFeastFuzzyRecommendActivity.this.gaExtra.shop_id = Integer.valueOf(WeddingFeastFuzzyRecommendActivity.this.shopId);
          }
        while (true)
        {
          GAHelper.instance().contextStatisticsEvent(WeddingFeastFuzzyRecommendActivity.this.getApplicationContext(), "mobile", WeddingFeastFuzzyRecommendActivity.this.gaExtra, "tap");
          return;
          label115: WeddingFeastFuzzyRecommendActivity.this.gaExtra.shop_id = Integer.valueOf(0);
          continue;
          label132: WeddingFeastFuzzyRecommendActivity.this.gaExtra.shop_id = Integer.valueOf(0);
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
        WeddingFeastFuzzyRecommendActivity.this.setSubmitButtonState();
      }
    });
    this.submitBtn = ((NovaButton)findViewById(R.id.find_conditions_go_btn));
    this.submitBtn.setGAString("submit");
    this.submitBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        WeddingFeastFuzzyRecommendActivity.this.checkAndSubmitBookingInfo();
      }
    });
    setSubmitButtonState();
    TextView localTextView = (TextView)findViewById(R.id.orderCountView);
    this.regionView = ((MeasuredGridView)findViewById(R.id.regionView));
    MeasuredGridView localMeasuredGridView1 = (MeasuredGridView)findViewById(R.id.tableView);
    MeasuredGridView localMeasuredGridView2 = (MeasuredGridView)findViewById(R.id.priceView);
    MeasuredGridView localMeasuredGridView3 = (MeasuredGridView)findViewById(R.id.categoryView);
    localTextView.setText(this.orderCount + "");
    this.regionView.setAdapter(new GridViewAdapter(this, this.regions, "region"));
    localMeasuredGridView1.setAdapter(new GridViewAdapter(this, this.tables, "table"));
    localMeasuredGridView2.setAdapter(new GridViewAdapter(this, this.prices, "price"));
    localMeasuredGridView3.setAdapter(new GridViewAdapter(this, this.categorys, "category"));
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

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    this.anim_lay = findViewById(R.id.animation_lay);
    Animation localAnimation = AnimationUtils.loadAnimation(this, R.anim.wedding_fuzzyrecommend_resume);
    this.anim_lay.startAnimation(localAnimation);
    if (paramIntent != null)
      this.selectedRegions = paramIntent.getStringArrayListExtra("selectedRegions");
    if (this.selectedRegions.size() <= 0)
    {
      paramIntent = (TextView)this.regionView.getChildAt(0);
      paramIntent.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_line_round_corner_bk));
      paramIntent.setTextColor(getResources().getColor(R.color.light_red));
      paramIntent = (TextView)this.regionView.getChildAt(this.regionView.getCount() - 1);
      paramIntent.setBackgroundDrawable(getResources().getDrawable(R.drawable.black_line_round_corner_bk));
      paramIntent.setTextColor(getResources().getColor(R.color.deep_gray));
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Uri localUri = getIntent().getData();
    if (localUri != null)
    {
      this.shopId = localUri.getQueryParameter("shopid");
      this.cityId = localUri.getQueryParameter("cityid");
    }
    if ((this.shopId == null) && (paramBundle != null))
    {
      this.shopId = paramBundle.getString("shopid");
      this.cityId = paramBundle.getString("cityid");
    }
    this.source = getStringParam("source");
    sendInfoRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mGetHistoryReq)
    {
      this.mGetHistoryReq = null;
      paramMApiRequest = ((DPObject)paramMApiResponse.result()).getString("BookingUserMobile");
      if ((paramMApiRequest != null) && (paramMApiRequest.trim().length() > 0))
        this.phoneNumEdit.setText(paramMApiRequest);
    }
    do
      return;
    while (paramMApiRequest != this.mGetFuzzyRecommendInfoReq);
    if (this.gaExtra.shop_id == null)
    {
      if ((this.shopId == null) || ("null".equals(this.shopId)) || ("".equals(this.shopId)))
        break label302;
      if (!TextUtils.isDigitsOnly(this.shopId))
        break label288;
      this.gaExtra.shop_id = Integer.valueOf(this.shopId);
    }
    while (true)
    {
      GAHelper.instance().contextStatisticsEvent(getApplicationContext(), "submit_success", this.gaExtra, "tap");
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      this.regions = paramMApiRequest.getStringArray("Regions");
      this.tables = paramMApiRequest.getStringArray("Tables");
      this.prices = paramMApiRequest.getStringArray("Prices");
      this.orderCount = paramMApiRequest.getInt("OrderCount");
      this.promoInfo = paramMApiRequest.getString("PromoInfo");
      this.regionList = paramMApiRequest.getArray("RegionList");
      this.categorys = paramMApiRequest.getStringArray("Categorys");
      this.categoryIdArray = paramMApiRequest.getIntArray("CategoryIds");
      paramMApiRequest = this.regionList;
      int j = paramMApiRequest.length;
      int i = 0;
      while (i < j)
      {
        paramMApiResponse = paramMApiRequest[i];
        this.regionsNames.add(paramMApiResponse.getString("Name"));
        i += 1;
      }
      label288: this.gaExtra.shop_id = Integer.valueOf(0);
      continue;
      label302: this.gaExtra.shop_id = Integer.valueOf(0);
    }
    setupView();
    sendHistoryRequest();
  }

  public void submitWeddingBookingInfo()
  {
    StringBuffer localStringBuffer = new StringBuffer("http://m.dianping.com/wed/mobile/hunyan/fuzzy/mobileWedHotelFuzzyRecommendResult/");
    localStringBuffer.append("shopId=").append(this.shopId);
    localStringBuffer.append("&phoneNo=").append(this.phoneNum);
    localStringBuffer.append("&dpId=").append(DeviceUtils.dpid());
    localStringBuffer.append("&cityId=").append(cityId());
    localStringBuffer.append("&tableType=").append(this.tableType);
    localStringBuffer.append("&priceType=").append(this.priceType);
    localStringBuffer.append("&regionType=").append(this.regionType);
    localStringBuffer.append("&categoryIds=").append(this.categoryIds);
    localStringBuffer.append("&source=").append(this.source);
    Object localObject2 = "";
    Object localObject1;
    if ((this.selectedRegions != null) && (this.selectedRegions.size() > 0))
    {
      Iterator localIterator = this.selectedRegions.iterator();
      if (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        DPObject[] arrayOfDPObject = this.regionList;
        int j = arrayOfDPObject.length;
        int i = 0;
        for (localObject1 = localObject2; ; localObject1 = localObject2)
        {
          localObject2 = localObject1;
          if (i >= j)
            break;
          DPObject localDPObject = arrayOfDPObject[i];
          localObject2 = localObject1;
          if (str.equals(localDPObject.getString("Name")))
            localObject2 = (String)localObject1 + localDPObject.getInt("ID") + ",";
          i += 1;
        }
      }
      localObject1 = ((String)localObject2).substring(0, ((String)localObject2).length() - 1);
    }
    while (true)
    {
      localStringBuffer.append("&regionIds=").append((String)localObject1);
      localStringBuffer.append("?dpshare=0");
      try
      {
        localObject1 = URLEncoder.encode(localStringBuffer.toString(), "UTF-8");
        localObject2 = new Intent("android.intent.action.VIEW");
        ((Intent)localObject2).setData(Uri.parse("dianping://weddinghotelweb?url=" + (String)localObject1));
        startActivity((Intent)localObject2);
        finish();
        return;
        localObject1 = "0";
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
      }
    }
  }

  class GridViewAdapter extends BaseAdapter
  {
    Context context;
    String[] list;
    String name;

    GridViewAdapter(Context paramArrayOfString, String[] paramString, String arg4)
    {
      this.context = paramArrayOfString;
      this.list = paramString;
      Object localObject;
      this.name = localObject;
    }

    public int getCount()
    {
      return this.list.length;
    }

    public Object getItem(int paramInt)
    {
      return Integer.valueOf(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = new NovaTextView(this.context);
      paramView.setGravity(17);
      paramView.setPadding(0, ViewUtils.dip2px(this.context, 7.0F), 0, ViewUtils.dip2px(this.context, 7.0F));
      paramView.setText(this.list[paramInt]);
      paramView.setTextSize(2, 15.0F);
      if ((paramInt == 0) && (!"price".equals(this.name)))
      {
        paramView.setBackgroundDrawable(WeddingFeastFuzzyRecommendActivity.this.getResources().getDrawable(R.drawable.red_line_round_corner_bk));
        paramView.setTextColor(WeddingFeastFuzzyRecommendActivity.this.getResources().getColor(R.color.light_red));
        if (!"region".equals(this.name))
          break label333;
        paramInt = 0;
        label123: if (paramInt < WeddingFeastFuzzyRecommendActivity.this.regions.length)
        {
          if (!WeddingFeastFuzzyRecommendActivity.this.regions[paramInt].equals(paramView.getText()))
            break label326;
          paramViewGroup = new GAUserInfo();
          if (WeddingFeastFuzzyRecommendActivity.this.shopId != null)
            paramViewGroup.shop_id = Integer.valueOf(WeddingFeastFuzzyRecommendActivity.this.shopId);
          paramViewGroup.index = Integer.valueOf(paramInt + 1);
          paramView.setGAString("region_main", paramViewGroup);
        }
      }
      label539: 
      while (true)
      {
        if ("指定区域".equals(paramView.getText()))
          paramView.setGAString("region_district");
        paramView.setOnClickListener(new WeddingFeastFuzzyRecommendActivity.GridViewAdapter.1(this));
        return paramView;
        if ((paramInt == 2) && ("price".equals(this.name)))
        {
          paramView.setBackgroundDrawable(WeddingFeastFuzzyRecommendActivity.this.getResources().getDrawable(R.drawable.red_line_round_corner_bk));
          paramView.setTextColor(WeddingFeastFuzzyRecommendActivity.this.getResources().getColor(R.color.light_red));
          break;
        }
        paramView.setBackgroundDrawable(WeddingFeastFuzzyRecommendActivity.this.getResources().getDrawable(R.drawable.black_line_round_corner_bk));
        paramView.setTextColor(WeddingFeastFuzzyRecommendActivity.this.getResources().getColor(R.color.text_gray));
        break;
        label326: paramInt += 1;
        break label123;
        label333: if ("table".equals(this.name))
        {
          paramInt = 0;
          while (true)
          {
            if (paramInt >= WeddingFeastFuzzyRecommendActivity.this.tables.length)
              break label435;
            if (WeddingFeastFuzzyRecommendActivity.this.tables[paramInt].equals(paramView.getText()))
            {
              paramViewGroup = new GAUserInfo();
              if (WeddingFeastFuzzyRecommendActivity.this.shopId != null)
                paramViewGroup.shop_id = Integer.valueOf(WeddingFeastFuzzyRecommendActivity.this.shopId);
              paramViewGroup.index = Integer.valueOf(paramInt + 1);
              paramView.setGAString("table", paramViewGroup);
              break;
            }
            paramInt += 1;
          }
          continue;
        }
        label435: if (!"price".equals(this.name))
          continue;
        paramInt = 0;
        while (true)
        {
          if (paramInt >= WeddingFeastFuzzyRecommendActivity.this.prices.length)
            break label539;
          if (WeddingFeastFuzzyRecommendActivity.this.prices[paramInt].equals(paramView.getText()))
          {
            paramViewGroup = new GAUserInfo();
            if (WeddingFeastFuzzyRecommendActivity.this.shopId != null)
              paramViewGroup.shop_id = Integer.valueOf(WeddingFeastFuzzyRecommendActivity.this.shopId);
            paramViewGroup.index = Integer.valueOf(paramInt + 1);
            paramView.setGAString("price", paramViewGroup);
            break;
          }
          paramInt += 1;
        }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.weddingfeast.activity.WeddingFeastFuzzyRecommendActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.wed.weddingfeast.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.DateUtil;
import com.dianping.util.DeviceUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.util.WedBookingUtil;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaTextView;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WeddingFeastBookingDateActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private int[] MONTH_ID_ARRAY = { R.id.month1, R.id.month2, R.id.month3, R.id.month4, R.id.month5, R.id.month6, R.id.month7, R.id.month8, R.id.month9, R.id.month10, R.id.month11, R.id.month12 };
  private String[] MONTH_STR_ARRAY = { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" };
  private int currentFocusIndex;
  int currentMonth;
  int currentYear;
  ImageView expandArrow;
  RadioButton genBtn;
  int hallID;
  String hallName;
  boolean isDefaultFocus = true;
  boolean isExpanded = true;
  boolean isSubmitSuccess = false;
  RadioButton ladyBtn;
  private MApiRequest mGetHistoryReq;
  boolean mIsRepeatBook = false;
  private MApiRequest mPostSubmitReq;
  private boolean[][] monthChecks = (boolean[][])Array.newInstance(Boolean.TYPE, new int[] { 3, 12 });
  View[] monthView = new View[12];
  EditText nameEdit;
  private String phoneNo;
  EditText phoneNumEdit;
  LinearLayout promoLay;
  int shopID;
  String shopName;
  NovaButton submitBtn;
  ArrayList<DPObject> weddingPromoList = new ArrayList();
  NovaTextView[] yearView = new NovaTextView[3];
  int[] years = new int[3];

  private void changeFocusYear(int paramInt)
  {
    if (paramInt == 0)
    {
      this.yearView[0].setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_round_corner_bk));
      this.yearView[1].setBackgroundColor(getResources().getColor(R.color.white));
      this.yearView[2].setBackgroundColor(getResources().getColor(R.color.white));
      this.currentFocusIndex = 0;
      setMonthView();
      paramInt = 0;
      while (paramInt < this.monthView.length)
      {
        clickMonthView(this.monthView[paramInt], paramInt, false);
        paramInt += 1;
      }
    }
    if (paramInt == 1)
    {
      this.yearView[1].setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_round_corner_bk));
      this.yearView[0].setBackgroundColor(getResources().getColor(R.color.white));
      this.yearView[2].setBackgroundColor(getResources().getColor(R.color.white));
      this.currentFocusIndex = 1;
      setMonthView();
      paramInt = 0;
      while (paramInt < this.monthView.length)
      {
        clickMonthView(this.monthView[paramInt], paramInt, false);
        paramInt += 1;
      }
    }
    if (paramInt == 2)
    {
      this.yearView[2].setBackgroundResource(R.drawable.gray_round_corner_bk);
      this.yearView[1].setBackgroundResource(R.color.white);
      this.yearView[0].setBackgroundResource(R.color.white);
      this.currentFocusIndex = 2;
      setMonthView();
      paramInt = 0;
      while (paramInt < this.monthView.length)
      {
        clickMonthView(this.monthView[paramInt], paramInt, false);
        paramInt += 1;
      }
    }
  }

  private boolean checkMonthChecks()
  {
    if (!hasCheckItem())
    {
      showToast("请选择月份!");
      return false;
    }
    return true;
  }

  private boolean checkPhoneNo()
  {
    if ((this.phoneNumEdit.getText() == null) || (TextUtils.isEmpty(this.phoneNumEdit.getText().toString())))
    {
      this.phoneNumEdit.requestFocus();
      showToast("请填写手机号码!");
      return false;
    }
    if ((this.phoneNumEdit.getText() != null) && (this.phoneNumEdit.getText().toString().trim().length() < 11))
    {
      this.phoneNumEdit.requestFocus();
      showToast("请填写正确的手机号码!");
      return false;
    }
    return true;
  }

  private void clickMonthView(View paramView, int paramInt, boolean paramBoolean)
  {
    if (this.monthChecks[this.currentFocusIndex][paramInt] != 0)
    {
      paramView.findViewById(R.id.check).setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_check_on_weddingfeast));
      ((TextView)paramView.findViewById(R.id.month)).setTextColor(getResources().getColor(R.color.orange_red));
    }
    while (true)
    {
      setSubmitButtonState();
      if (paramBoolean)
      {
        if (this.gaExtra.shop_id == null)
          this.gaExtra.shop_id = Integer.valueOf(this.shopID);
        GAHelper.instance().contextStatisticsEvent(getApplicationContext(), "month", this.gaExtra, "tap");
      }
      return;
      paramView.findViewById(R.id.check).setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_check_off_weddingfeast));
      ((TextView)paramView.findViewById(R.id.month)).setTextColor(getResources().getColor(R.color.black));
    }
  }

  private void decodeBookingHistory(String paramString)
  {
    if (paramString == null);
    do
    {
      return;
      paramString = paramString.split(",");
    }
    while ((paramString == null) || (paramString.length == 0));
    int i = 0;
    while (true)
    {
      int j;
      if (i < paramString.length)
      {
        if (paramString[i] != null)
        {
          String[] arrayOfString = paramString[i].split("-");
          if ((arrayOfString != null) && (arrayOfString.length == 2))
            try
            {
              j = Integer.parseInt(arrayOfString[0]);
              if ((j - this.currentYear < 0) || (j - this.currentYear >= 3))
                break label214;
              int k = Integer.parseInt(arrayOfString[1]);
              if ((j < this.currentYear) || ((j == this.currentYear) && (k < this.currentMonth)) || (k < 1) || (k > 12))
                break label214;
              this.monthChecks[(j - this.currentYear)][(k - 1)] = 1;
            }
            catch (Exception localException)
            {
              localException.printStackTrace();
            }
        }
      }
      else
      {
        i = 2;
        label167: if (i >= 0)
          j = 0;
        while (true)
        {
          if (j < 12)
          {
            if (this.monthChecks[i][j] != 0)
              changeFocusYear(i);
          }
          else
          {
            i -= 1;
            break label167;
            break;
          }
          j += 1;
        }
      }
      label214: i += 1;
    }
  }

  private String handlebookingInfo()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    while (i < 3)
    {
      int j = 0;
      if (j < 12)
      {
        String str;
        if (this.monthChecks[i][j] != 0)
        {
          str = "" + (this.currentYear + i) + "-" + (j + 1);
          if (localStringBuilder.length() != 0)
            break label98;
          localStringBuilder.append(str);
        }
        while (true)
        {
          j += 1;
          break;
          label98: localStringBuilder.append("," + str);
        }
      }
      i += 1;
    }
    return localStringBuilder.toString();
  }

  private boolean hasCheckItem()
  {
    int k = 0;
    int i = 0;
    if ((i >= 3) || (k != 0))
      return k;
    int j = 0;
    while (true)
    {
      int m = k;
      if (j < 12)
      {
        if (this.monthChecks[i][j] != 0)
          m = 1;
      }
      else
      {
        i += 1;
        k = m;
        break;
      }
      j += 1;
    }
  }

  private void sendHistoryRequest()
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/wedding/weddinghotelbookinghistory.bin?");
    localStringBuffer.append("dpid=").append(DeviceUtils.dpid());
    localStringBuffer.append("&userid=").append(getUserId());
    localStringBuffer.append("&type=0");
    this.mGetHistoryReq = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    localMApiService.exec(this.mGetHistoryReq, this);
  }

  private void sendSubmitRequest()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("bookingDates", handlebookingInfo());
    localHashMap.put("bookingUserMobile", this.phoneNumEdit.getText().toString());
    localHashMap.put("bookingType", "5");
    localHashMap.put("hallID", this.hallID + "");
    localHashMap.put("userID", getUserId() + "");
    localHashMap.put("shopID", this.shopID + "");
    localHashMap.put("shopName", this.shopName);
    localHashMap.put("hallName", this.hallName);
    this.mPostSubmitReq = BasicMApiRequest.mapiPost(WedBookingUtil.getBookingUrl("http://m.api.dianping.com/wedding/weddinghotelbooking.bin", localHashMap), new String[0]);
    mapiService().exec(this.mPostSubmitReq, this);
  }

  private void setMonthView()
  {
    int i;
    if (this.currentFocusIndex == 0)
    {
      i = 0;
      if (i < 12)
      {
        if (i < this.currentMonth - 1)
        {
          this.monthView[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_round_corner_bk));
          this.monthView[i].findViewById(R.id.check).setVisibility(8);
          this.monthView[i].setClickable(false);
          ((TextView)this.monthView[i].findViewById(R.id.month)).setTextColor(getResources().getColor(R.color.black));
        }
        while (true)
        {
          i += 1;
          break;
          this.monthView[i].setBackgroundColor(getResources().getColor(R.color.white));
          this.monthView[i].findViewById(R.id.check).setVisibility(0);
          this.monthView[i].setClickable(true);
          ((TextView)this.monthView[i].findViewById(R.id.month)).setTextColor(getResources().getColor(R.color.black));
        }
      }
    }
    else
    {
      i = 0;
      while (i < 12)
      {
        this.monthView[i].setBackgroundColor(getResources().getColor(R.color.white));
        this.monthView[i].findViewById(R.id.check).setVisibility(0);
        this.monthView[i].setClickable(true);
        ((TextView)this.monthView[i].findViewById(R.id.month)).setTextColor(getResources().getColor(R.color.black));
        i += 1;
      }
    }
  }

  private void setSubmitButtonState()
  {
    if ((TextUtils.isEmpty(this.phoneNumEdit.getText().toString())) || (this.phoneNumEdit.getText().toString().length() < 11) || (!hasCheckItem()) || (!this.phoneNumEdit.getText().toString().startsWith("1")))
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
    super.setContentView(R.layout.wedding_feast_booking_date);
    this.phoneNumEdit = ((EditText)findViewById(R.id.phoneNum));
    this.submitBtn = ((NovaButton)findViewById(R.id.submit_btn));
    this.yearView[0] = ((NovaTextView)findViewById(R.id.year1));
    this.yearView[1] = ((NovaTextView)findViewById(R.id.year2));
    this.yearView[2] = ((NovaTextView)findViewById(R.id.year3));
    this.yearView[0].setGAString("year");
    this.yearView[1].setGAString("year");
    this.yearView[2].setGAString("year");
    this.yearView[0].setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_round_corner_bk));
    this.yearView[0].setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        WeddingFeastBookingDateActivity.this.changeFocusYear(0);
      }
    });
    this.yearView[1].setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        WeddingFeastBookingDateActivity.this.changeFocusYear(1);
      }
    });
    this.yearView[2].setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        WeddingFeastBookingDateActivity.this.changeFocusYear(2);
      }
    });
    int i = 0;
    while (i < this.MONTH_ID_ARRAY.length)
    {
      this.monthView[i] = findViewById(this.MONTH_ID_ARRAY[i]);
      ((TextView)this.monthView[i].findViewById(R.id.month)).setText(this.MONTH_STR_ARRAY[i]);
      this.monthView[i].setOnClickListener(this);
      i += 1;
    }
    this.submitBtn.setGAString("submit");
    this.submitBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if ((WeddingFeastBookingDateActivity.this.checkPhoneNo()) && (WeddingFeastBookingDateActivity.this.checkMonthChecks()))
        {
          WeddingFeastBookingDateActivity.this.sendSubmitRequest();
          WeddingFeastBookingDateActivity.this.showProgressDialog("请稍等..");
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
        WeddingFeastBookingDateActivity.this.setSubmitButtonState();
      }
    });
    this.phoneNumEdit.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (WeddingFeastBookingDateActivity.this.gaExtra.shop_id == null)
          WeddingFeastBookingDateActivity.this.gaExtra.shop_id = Integer.valueOf(WeddingFeastBookingDateActivity.this.shopID);
        GAHelper.instance().contextStatisticsEvent(WeddingFeastBookingDateActivity.this.getApplicationContext(), "mobile", WeddingFeastBookingDateActivity.this.gaExtra, "tap");
      }
    });
  }

  public void onClick(View paramView)
  {
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    int i = 0;
    if (paramView.getId() == R.id.month1)
    {
      paramView = this.monthChecks[this.currentFocusIndex];
      if (this.monthChecks[this.currentFocusIndex][0] == 0)
      {
        i = 1;
        paramView[0] = i;
        clickMonthView(this.monthView[0], 0, true);
      }
    }
    do
    {
      return;
      i = 0;
      break;
      if (paramView.getId() == R.id.month2)
      {
        paramView = this.monthChecks[this.currentFocusIndex];
        if (this.monthChecks[this.currentFocusIndex][1] == 0)
          i = 1;
        paramView[1] = i;
        clickMonthView(this.monthView[1], 1, true);
        return;
      }
      if (paramView.getId() == R.id.month3)
      {
        paramView = this.monthChecks[this.currentFocusIndex];
        i = j;
        if (this.monthChecks[this.currentFocusIndex][2] == 0)
          i = 1;
        paramView[2] = i;
        clickMonthView(this.monthView[2], 2, true);
        return;
      }
      if (paramView.getId() == R.id.month4)
      {
        paramView = this.monthChecks[this.currentFocusIndex];
        i = k;
        if (this.monthChecks[this.currentFocusIndex][3] == 0)
          i = 1;
        paramView[3] = i;
        clickMonthView(this.monthView[3], 3, true);
        return;
      }
      if (paramView.getId() == R.id.month5)
      {
        paramView = this.monthChecks[this.currentFocusIndex];
        i = m;
        if (this.monthChecks[this.currentFocusIndex][4] == 0)
          i = 1;
        paramView[4] = i;
        clickMonthView(this.monthView[4], 4, true);
        return;
      }
      if (paramView.getId() == R.id.month6)
      {
        paramView = this.monthChecks[this.currentFocusIndex];
        i = n;
        if (this.monthChecks[this.currentFocusIndex][5] == 0)
          i = 1;
        paramView[5] = i;
        clickMonthView(this.monthView[5], 5, true);
        return;
      }
      if (paramView.getId() == R.id.month7)
      {
        paramView = this.monthChecks[this.currentFocusIndex];
        i = i1;
        if (this.monthChecks[this.currentFocusIndex][6] == 0)
          i = 1;
        paramView[6] = i;
        clickMonthView(this.monthView[6], 6, true);
        return;
      }
      if (paramView.getId() == R.id.month8)
      {
        paramView = this.monthChecks[this.currentFocusIndex];
        i = i2;
        if (this.monthChecks[this.currentFocusIndex][7] == 0)
          i = 1;
        paramView[7] = i;
        clickMonthView(this.monthView[7], 7, true);
        return;
      }
      if (paramView.getId() == R.id.month9)
      {
        paramView = this.monthChecks[this.currentFocusIndex];
        i = i3;
        if (this.monthChecks[this.currentFocusIndex][8] == 0)
          i = 1;
        paramView[8] = i;
        clickMonthView(this.monthView[8], 8, true);
        return;
      }
      if (paramView.getId() == R.id.month10)
      {
        paramView = this.monthChecks[this.currentFocusIndex];
        i = i4;
        if (this.monthChecks[this.currentFocusIndex][9] == 0)
          i = 1;
        paramView[9] = i;
        clickMonthView(this.monthView[9], 9, true);
        return;
      }
      if (paramView.getId() != R.id.month11)
        continue;
      paramView = this.monthChecks[this.currentFocusIndex];
      i = i5;
      if (this.monthChecks[this.currentFocusIndex][10] == 0)
        i = 1;
      paramView[10] = i;
      clickMonthView(this.monthView[10], 10, true);
      return;
    }
    while (paramView.getId() != R.id.month12);
    paramView = this.monthChecks[this.currentFocusIndex];
    i = i6;
    if (this.monthChecks[this.currentFocusIndex][11] == 0)
      i = 1;
    paramView[11] = i;
    clickMonthView(this.monthView[11], 11, true);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setupView();
    Object localObject = getIntent();
    paramBundle = (DPObject)((Intent)localObject).getParcelableExtra("objShop");
    localObject = (DPObject)((Intent)localObject).getParcelableExtra("hallItem");
    if (paramBundle != null)
    {
      this.shopID = paramBundle.getInt("ID");
      this.shopName = paramBundle.getString("Name");
    }
    if (localObject != null)
    {
      this.hallID = ((DPObject)localObject).getInt("ID");
      this.hallName = ((DPObject)localObject).getString("Name");
    }
    paramBundle = new Date(DateUtil.currentTimeMillis());
    this.currentYear = (paramBundle.getYear() + 1900);
    this.currentMonth = (paramBundle.getMonth() + 1);
    this.yearView[0].setText(this.currentYear + "年");
    this.yearView[1].setText(this.currentYear + 1 + "年");
    this.yearView[2].setText(this.currentYear + 2 + "年");
    this.currentFocusIndex = 0;
    setMonthView();
    changeFocusYear(0);
    sendHistoryRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mPostSubmitReq)
    {
      if ((paramMApiResponse == null) || (paramMApiResponse.message() == null))
        break label39;
      showToast(paramMApiResponse.message().content());
    }
    while (true)
    {
      dismissDialog();
      return;
      label39: showToast("预约失败");
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mGetHistoryReq)
    {
      this.mGetHistoryReq = null;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      paramMApiResponse = paramMApiRequest.getString("BookingDates");
      this.phoneNo = paramMApiRequest.getString("BookingUserMobile");
      decodeBookingHistory(paramMApiResponse);
      if ((this.phoneNo != null) && (this.phoneNo.trim().length() > 0))
        this.phoneNumEdit.setText(this.phoneNo);
    }
    do
      return;
    while (paramMApiRequest != this.mPostSubmitReq);
    paramMApiRequest = paramMApiResponse.result();
    if ((paramMApiRequest instanceof DPObject))
    {
      if (this.gaExtra.shop_id == null)
        this.gaExtra.shop_id = Integer.valueOf(this.shopID);
      GAHelper.instance().contextStatisticsEvent(getApplicationContext(), "submit_success", this.gaExtra, "tap");
      paramMApiRequest = (DPObject)paramMApiRequest;
      dismissDialog();
      paramMApiRequest = new StringBuffer("http://m.dianping.com/wed/mobile/hunyan/bookrouter_new?");
      paramMApiRequest.append("shopId=").append(this.shopID).append("&userPhone=").append(this.phoneNumEdit.getText().toString()).append("&act=DQ");
      paramMApiRequest = WedBookingUtil.getBookingUrl(paramMApiRequest.toString(), null);
    }
    try
    {
      paramMApiRequest = URLEncoder.encode(paramMApiRequest, "UTF-8");
      paramMApiResponse = new Intent("android.intent.action.VIEW");
      paramMApiResponse.setData(Uri.parse("dianping://weddinghotelweb?url=" + paramMApiRequest));
      startActivity(paramMApiResponse);
      finish();
      dismissDialog();
      return;
    }
    catch (java.io.UnsupportedEncodingException paramMApiRequest)
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.weddingfeast.activity.WeddingFeastBookingDateActivity
 * JD-Core Version:    0.6.0
 */
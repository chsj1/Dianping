package com.dianping.booking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class BookingRulesExplanationActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private TextView bindTip;
  String bindTipContent;
  private DPObject bookingRules;
  private ImageView[] creditLevelImage = new ImageView[5];
  String errorMsg;
  IntentFilter filter = null;
  int lastCreditLevel;
  private TextView latestBindPhoneNo;
  private ImageView levelImage1;
  private ImageView levelImage2;
  private ImageView levelImage3;
  private ImageView levelImage4;
  private ImageView levelImage5;
  private TextView levelNum;
  private TextView levelTip;
  private ImageView levelTipArrow;
  String levelTipContent;
  private LinearLayout levelTipLayout;
  private MApiRequest mRequest;
  private RelativeLayout rebindPhoneLayout;
  BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.booking.BOOKING_BIND_PHONE"))
      {
        paramIntent = BookingRulesExplanationActivity.this;
        if (BookingRulesExplanationActivity.this.getAccount() != null)
          break label40;
      }
      label40: for (paramContext = ""; ; paramContext = BookingRulesExplanationActivity.this.getAccount().token())
      {
        paramIntent.getBookingRecordTask(paramContext, Environment.uuid(), 0);
        return;
      }
    }
  };
  String tel;

  private void dismissLevelTip()
  {
    this.levelTip.setVisibility(8);
    this.levelTipArrow.setImageResource(R.drawable.yy_level_arrow_down);
  }

  private void getBookingRecordTask(String paramString1, String paramString2, int paramInt)
  {
    if (this.mRequest != null)
      return;
    if (!TextUtils.isEmpty(paramString1));
    for (paramString1 = "http://rs.api.dianping.com/getbookinghistory.yy?" + "token=" + paramString1 + "&clientUUID=" + paramString2 + "&startIndex=" + paramInt; ; paramString1 = "http://rs.api.dianping.com/getbookinghistory.yy?" + "clientUUID=" + paramString2 + "&startIndex=" + paramInt)
    {
      this.mRequest = BasicMApiRequest.mapiGet(paramString1, CacheType.CRITICAL);
      mapiService().exec(this.mRequest, this);
      return;
    }
  }

  private void initCreditLevelImage(int paramInt)
  {
    if (paramInt == 0)
      initCreditLevelImage(paramInt, R.drawable.gray_star, R.drawable.gray_star, R.drawable.gray_star);
    while (true)
    {
      return;
      if ((paramInt >= 1) && (paramInt <= 10))
      {
        initCreditLevelImage(paramInt, R.drawable.gray_star, R.drawable.golden_star_half, R.drawable.golden_star);
        return;
      }
      if ((paramInt >= 11) && (paramInt <= 20))
      {
        initCreditLevelImage(paramInt - 10, R.drawable.gray_diamond, R.drawable.blue_diamond_half, R.drawable.blue_diamond);
        return;
      }
      if ((paramInt >= 21) && (paramInt <= 30))
      {
        initCreditLevelImage(paramInt - 20, R.drawable.gray_crown, R.drawable.blue_crown_half, R.drawable.blue_crown);
        return;
      }
      if ((paramInt >= 31) && (paramInt <= 40))
      {
        initCreditLevelImage(paramInt - 30, R.drawable.gray_crown, R.drawable.golden_crown_half, R.drawable.golden_crown);
        return;
      }
      if (paramInt <= 40)
        continue;
      paramInt = 0;
      while (paramInt < this.creditLevelImage.length)
      {
        this.creditLevelImage[paramInt].setImageResource(R.drawable.golden_crown);
        paramInt += 1;
      }
    }
  }

  private void initCreditLevelImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int j = paramInt1 / 2;
    int i = 0;
    while (i < j)
    {
      this.creditLevelImage[i].setImageResource(paramInt4);
      i += 1;
    }
    if (paramInt1 % 2 != 0)
    {
      this.creditLevelImage[j].setImageResource(paramInt3);
      paramInt1 = j + 1;
      while (paramInt1 < this.creditLevelImage.length)
      {
        this.creditLevelImage[paramInt1].setImageResource(paramInt2);
        paramInt1 += 1;
      }
    }
    paramInt1 = j;
    while (paramInt1 < this.creditLevelImage.length)
    {
      this.creditLevelImage[paramInt1].setImageResource(paramInt2);
      paramInt1 += 1;
    }
  }

  private void refreshBookingTipsView(DPObject paramDPObject)
  {
    this.lastCreditLevel = paramDPObject.getObject("CreditInfo").getInt("LastCreditLevel");
    this.tel = paramDPObject.getString("Tel");
    this.levelNum.setText("LV" + this.lastCreditLevel);
    initCreditLevelImage(this.lastCreditLevel);
    this.latestBindPhoneNo.setText(this.tel);
  }

  private void setupViews()
  {
    this.levelNum = ((TextView)findViewById(R.id.level_num));
    this.levelNum.setText("LV" + this.lastCreditLevel);
    this.levelImage1 = ((ImageView)findViewById(R.id.level_image_1));
    this.levelImage2 = ((ImageView)findViewById(R.id.level_image_2));
    this.levelImage3 = ((ImageView)findViewById(R.id.level_image_3));
    this.levelImage4 = ((ImageView)findViewById(R.id.level_image_4));
    this.levelImage5 = ((ImageView)findViewById(R.id.level_image_5));
    this.creditLevelImage[0] = this.levelImage1;
    this.creditLevelImage[1] = this.levelImage2;
    this.creditLevelImage[2] = this.levelImage3;
    this.creditLevelImage[3] = this.levelImage4;
    this.creditLevelImage[4] = this.levelImage5;
    initCreditLevelImage(this.lastCreditLevel);
    this.levelTipLayout = ((LinearLayout)findViewById(R.id.level_tip_layout));
    this.levelTipLayout.setOnClickListener(this);
    this.levelTip = ((TextView)findViewById(R.id.level_tip));
    this.levelTip.setText(this.levelTipContent);
    this.levelTipArrow = ((ImageView)findViewById(R.id.arrow));
    this.rebindPhoneLayout = ((RelativeLayout)findViewById(R.id.rebind_phone_layout));
    this.rebindPhoneLayout.setOnClickListener(this);
    this.latestBindPhoneNo = ((TextView)findViewById(R.id.latest_bind_phoneno));
    this.latestBindPhoneNo.setText(this.tel);
    this.bindTip = ((TextView)findViewById(R.id.bind_tip));
    this.bindTip.setText(this.bindTipContent);
  }

  private void showLevelTip()
  {
    this.levelTip.setVisibility(0);
    this.levelTipArrow.setImageResource(R.drawable.yy_level_arrow_up);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.level_tip_layout)
      if (ViewUtils.isShow(this.levelTip))
        dismissLevelTip();
    do
    {
      return;
      showLevelTip();
      statisticsEvent("booking6", "booking6_phonebind_creditdetail", "", 0);
      return;
      if (i != R.id.rebind_phone_layout)
        continue;
      statisticsEvent("booking6", "booking6_phonebind_modifyphone", "", 0);
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://bookingbindphone"));
      paramView.putExtra("phone", "");
      paramView.putExtra("editable", true);
      paramView.putExtra("validation", 1);
      startActivity(paramView);
      return;
    }
    while (i != R.id.left_title_button);
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.booking_rules_explanation);
    super.getWindow().setBackgroundDrawable(null);
    this.filter = new IntentFilter("com.dianping.booking.BOOKING_BIND_PHONE");
    registerReceiver(this.receiver, this.filter);
    this.lastCreditLevel = getIntent().getIntExtra("lastCreditLevel", 0);
    this.tel = getIntent().getStringExtra("tel");
    this.levelTipContent = getIntent().getStringExtra("levelTip");
    this.bindTipContent = getIntent().getStringExtra("bindTip");
    setupViews();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.receiver != null)
      unregisterReceiver(this.receiver);
    if (this.mRequest != null)
    {
      mapiService().abort(this.mRequest, this, true);
      this.mRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRequest)
    {
      this.errorMsg = paramMApiResponse.message().toString();
      this.mRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRequest)
    {
      if (!(paramMApiResponse.result() instanceof DPObject))
        break label47;
      this.bookingRules = ((DPObject)paramMApiResponse.result());
      refreshBookingTipsView(this.bookingRules);
    }
    while (true)
    {
      this.mRequest = null;
      return;
      label47: this.errorMsg = paramMApiResponse.message().toString();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingRulesExplanationActivity
 * JD-Core Version:    0.6.0
 */
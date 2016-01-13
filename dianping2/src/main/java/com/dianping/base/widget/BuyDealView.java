package com.dianping.base.widget;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.DPApplication;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.configservice.ConfigService;
import com.dianping.model.UserProfile;
import com.dianping.util.Log;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaLinearLayout;

public class BuyDealView extends NovaLinearLayout
  implements LoginResultListener
{
  public static final String RMB = "¥";
  protected AccountService accountService;
  protected NovaButton buy;
  protected OnBuyClickListener buyClickListener;
  protected ConfigService configService;
  protected DPObject dpDeal;
  protected LinearLayout eventsView;
  protected AutoHideTextView originalPrice;
  protected TextView price;
  protected boolean showTags = false;

  public BuyDealView(Context paramContext)
  {
    this(paramContext, null);
  }

  public BuyDealView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    inflate(paramContext, R.layout.deal_buy_item, this);
    initView();
  }

  public AccountService accountService()
  {
    if (this.accountService == null)
      this.accountService = ((AccountService)getService("account"));
    return this.accountService;
  }

  public void buy()
  {
    if (this.dpDeal == null);
    do
    {
      return;
      if ((isLogined()) || (this.dpDeal.getInt("DealType") != 2))
        continue;
      accountService().login(this);
      return;
    }
    while (checkIsLocked());
    if ((this.dpDeal.getArray("DealSelectList") != null) && (this.dpDeal.getArray("DealSelectList").length > 1))
    {
      localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://dealselector"));
      localIntent.putExtra("dpDeal", this.dpDeal);
      getContext().startActivity(localIntent);
      return;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
    localIntent.putExtra("deal", this.dpDeal);
    getContext().startActivity(localIntent);
  }

  protected boolean checkIsLocked()
  {
    if (accountService().token() == null);
    do
      return false;
    while (!getAccount().grouponIsLocked());
    new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("您的账户存在异常已被锁定，请联系客服为您解除锁定。").setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if ((BuyDealView.this.getContext() instanceof Activity))
          ((Activity)BuyDealView.this.getContext()).finish();
      }
    }).setCancelable(false).show();
    return true;
  }

  public ConfigService configService()
  {
    if (this.configService == null)
      this.configService = ((ConfigService)getService("config"));
    return this.configService;
  }

  public UserProfile getAccount()
  {
    Object localObject = DPApplication.instance().accountService().profile();
    if (localObject != null)
      try
      {
        localObject = (UserProfile)((DPObject)localObject).edit().putString("Token", accountService().token()).generate().decodeToObject(UserProfile.DECODER);
        return localObject;
      }
      catch (ArchiveException localArchiveException)
      {
        Log.w(localArchiveException.getLocalizedMessage());
      }
    return (UserProfile)null;
  }

  public Object getService(String paramString)
  {
    return DPApplication.instance().getService(paramString);
  }

  protected void initView()
  {
    this.price = ((TextView)findViewById(R.id.price));
    this.originalPrice = ((AutoHideTextView)findViewById(R.id.original_price));
    this.buy = ((NovaButton)findViewById(R.id.buy));
    this.eventsView = ((LinearLayout)findViewById(R.id.tags));
    this.buy.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        int i = 1;
        if (BuyDealView.this.buyClickListener != null)
        {
          BuyDealView.this.buyClickListener.onClick(paramView);
          return;
        }
        BuyDealView.this.buy();
        label90: String str;
        if (BuyDealView.this.dpDeal.getInt("DealType") == 3)
        {
          DPApplication.instance().statisticsEvent("tuan5", "tuan5_detail_lotterybuy", "" + BuyDealView.this.dpDeal.getInt("ID"), 0);
          if (BuyDealView.this.dpDeal.getInt("dealchannel") != 1)
            break label213;
          boolean bool = BuyDealView.this.dpDeal.getBoolean("IsHotelBookable");
          paramView = DPApplication.instance();
          str = BuyDealView.this.dpDeal.getInt("ID") + "";
          if (!bool)
            break label215;
        }
        while (true)
        {
          paramView.statisticsEvent("tuan5", "hotel_tuan5_detail_buy", str, i);
          return;
          DPApplication.instance().statisticsEvent("tuan5", "tuan5_detail_buy", "" + BuyDealView.this.dpDeal.getInt("ID"), 0);
          break label90;
          label213: break;
          label215: i = 0;
        }
      }
    });
    this.originalPrice.setOnVisibilityChangedListener(new AutoHideTextView.OnVisibilityChangedListener()
    {
      public void onVisibilityChanged(int paramInt)
      {
        if (paramInt == 8)
        {
          BuyDealView.this.removeAllViews();
          View.inflate(BuyDealView.this.getContext(), R.layout.deal_buy_item2, BuyDealView.this);
          BuyDealView.this.initView();
          BuyDealView.this.updateView();
          BuyDealView.this.post(new Runnable()
          {
            public void run()
            {
              BuyDealView.this.requestLayout();
            }
          });
        }
      }
    });
    this.buy.setGAString("buy");
    ((NovaActivity)getContext()).addGAView(this.buy, -1, "tuandeal", "tuandeal".equals(((NovaActivity)getContext()).getPageName()));
  }

  public boolean isLogined()
  {
    if (getAccount() == null);
    do
      return false;
    while (TextUtils.isEmpty(accountService().token()));
    return true;
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if (this.price.getPaint().measureText(this.price.getText().toString()) > this.price.getMeasuredWidth())
      this.price.setTextSize(2, 25.0F);
  }

  public void setDeal(DPObject paramDPObject)
  {
    this.dpDeal = paramDPObject;
    this.buy.gaUserInfo.dealgroup_id = Integer.valueOf(this.dpDeal.getInt("ID"));
    updateView();
  }

  public void setOnBuyClickListener(OnBuyClickListener paramOnBuyClickListener)
  {
    this.buyClickListener = paramOnBuyClickListener;
  }

  public void setShowTags(boolean paramBoolean)
  {
    this.showTags = paramBoolean;
    updateView();
  }

  public void updateBuyButton()
  {
  }

  protected void updateView()
  {
    if ((this.dpDeal == null) || (this.price == null))
      return;
    Object localObject1 = new SpannableString("¥" + PriceFormatUtils.formatPrice(this.dpDeal.getDouble("OriginalPrice")));
    ((SpannableString)localObject1).setSpan(new StrikethroughSpan(), 0, ((SpannableString)localObject1).length(), 33);
    this.originalPrice.setText((CharSequence)localObject1);
    localObject1 = this.dpDeal.getObject("DetailConfig");
    if (localObject1 != null)
    {
      localObject1 = ((DPObject)localObject1).getObject("DealDetailBuySubConfig");
      Object localObject2;
      if (localObject1 != null)
      {
        localObject2 = new SpannableString(PriceFormatUtils.formatPrice(this.dpDeal.getDouble("Price")));
        if (((DPObject)localObject1).getBoolean("PriceStrikeThrough"))
          ((SpannableString)localObject2).setSpan(new StrikethroughSpan(), 0, ((SpannableString)localObject2).length(), 33);
        this.price.setText((CharSequence)localObject2);
        if (TextUtils.isEmpty(((DPObject)localObject1).getString("ButtonText")))
          break label425;
        this.buy.setText(((DPObject)localObject1).getString("ButtonText"));
        label191: if (!((DPObject)localObject1).getBoolean("ButtonEnable"))
          break label438;
        this.buy.setEnabled(true);
      }
      while (true)
      {
        this.eventsView.removeAllViews();
        this.eventsView.setOnClickListener(null);
        localObject1 = this.dpDeal.getArray("EventList");
        if ((!this.showTags) || (DPObjectUtils.isArrayEmpty(localObject1)))
          break;
        localObject2 = new LinearLayout.LayoutParams(-2, -2);
        ((LinearLayout.LayoutParams)localObject2).setMargins(5, 0, 0, 0);
        i = 0;
        while (i < localObject1.length)
        {
          if (!TextUtils.isEmpty(localObject1[i].getString("ShortTitle")))
          {
            ColorBorderTextView localColorBorderTextView = new ColorBorderTextView(getContext());
            localColorBorderTextView.setTextColor(localObject1[i].getString("Color"));
            localColorBorderTextView.setBorderColor("#C8" + localObject1[i].getString("Color").substring(1));
            localColorBorderTextView.setText(localObject1[i].getString("ShortTitle"));
            localColorBorderTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
            localColorBorderTextView.setSingleLine();
            localColorBorderTextView.setEllipsize(TextUtils.TruncateAt.END);
            localColorBorderTextView.setPadding(1, 0, 1, 0);
            this.eventsView.addView(localColorBorderTextView, (ViewGroup.LayoutParams)localObject2);
          }
          i += 1;
        }
        label425: this.buy.setText("立即抢购");
        break label191;
        label438: this.buy.setEnabled(false);
      }
    }
    this.price.setText(PriceFormatUtils.formatPrice(this.dpDeal.getDouble("Price")));
    int i = this.dpDeal.getInt("Status");
    if (this.dpDeal.getInt("DealType") == 3)
      this.buy.setText("免费抽奖");
    while (true)
    {
      if ((i & 0x10) != 0)
      {
        this.buy.setText("即将开始");
        this.buy.setEnabled(false);
      }
      if ((i & 0x2) != 0)
      {
        this.buy.setText("卖光了");
        this.buy.setEnabled(false);
      }
      if ((i & 0x4) != 0)
      {
        this.buy.setText("已结束");
        this.buy.setEnabled(false);
      }
      if ((getAccount() == null) || ((i & 0x8) == 0))
        break;
      this.buy.setEnabled(false);
      break;
      this.buy.setText("立即抢购");
    }
    this.eventsView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (BuyDealView.this.dpDeal != null)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://dealtaginfo"));
          paramView.putExtra("deal", BuyDealView.this.dpDeal);
          BuyDealView.this.getContext().startActivity(paramView);
          DPApplication.instance().statisticsEvent("tuan5", "tuan5_detail_event", "eventid", BuyDealView.this.dpDeal.getInt("EventTags"));
          if (BuyDealView.this.dpDeal.getInt("dealchannel") == 1)
            DPApplication.instance().statisticsEvent("tuan5", "hotel_tuan5_detail_event", "dealgrpid", BuyDealView.this.dpDeal.getInt("ID"));
        }
      }
    });
    this.eventsView.setVisibility(0);
  }

  public static abstract interface OnBuyClickListener
  {
    public abstract void onClick(View paramView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.BuyDealView
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.common;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HuiPayAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String HIDE_PROMO = "收起";
  protected static final String MORE_PROMO = "更多特惠";
  private static final int PROMO_STATUS_ENABLE = 10;
  private static final int PROMO_STATUS_QIANGED = 60;
  private static final int PROMO_STATUS_UNABLE = 40;
  private static final int PROMO_STATUS_USABLE_NEXT_TIME = 80;
  protected String COMMON_CELL_INDEX = "0475HuiPay.10Hui";
  protected String activityDes;
  int businessType;
  boolean enabledMOPay;
  LinearLayout expandLayout;
  protected NovaRelativeLayout expandView;
  protected View.OnClickListener gotoPayListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(new BasicNameValuePair("shopId", Integer.toString(HuiPayAgent.this.shopId())));
      if (!HuiPayAgent.this.payEnabled)
      {
        paramView = Toast.makeText(HuiPayAgent.this.getContext(), "以下优惠尚未开始", 1);
        paramView.setGravity(17, 0, 0);
        paramView.show();
        return;
      }
      if (paramView.getId() == R.id.button)
      {
        HuiPayAgent.this.popup.dismiss();
        HuiPayAgent.this.statisticsEvent("hui7", "hui7_coupon_pay", "3", HuiPayAgent.this.businessType, localArrayList);
      }
      while (true)
      {
        HuiPayAgent.this.gotoPay();
        return;
        HuiPayAgent.this.statisticsEvent("hui7", "hui7_coupon_pay", "1", HuiPayAgent.this.businessType, localArrayList);
      }
    }
  };
  boolean isExpand;
  LinearLayout linearLayout;
  protected DPObject mopayPromos;
  boolean payEnabled = false;
  private MApiRequest payPromoReq;
  Dialog popup;
  private int shopId;
  protected DPObject[] shopPromos;
  protected View.OnClickListener showDialogListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      HuiPayAgent.this.statisticsEvent("hui6", "hui6_coupon_click", "", HuiPayAgent.this.shopId());
      HuiPayAgent.this.popup = HuiPayAgent.this.buildDialog((HuiPayAgent.DialogInfo)paramView.getTag());
      HuiPayAgent.this.popup.findViewById(R.id.button).setTag(paramView);
      HuiPayAgent.this.popup.show();
    }
  };

  public HuiPayAgent(Object paramObject)
  {
    super(paramObject);
  }

  private String addSpaceToCnPunc(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return paramString;
    paramString = paramString.toCharArray();
    StringBuffer localStringBuffer = new StringBuffer();
    int j = paramString.length;
    int i = 0;
    while (i < j)
    {
      char c = paramString[i];
      localStringBuffer.append(c);
      if (isCnPunc(c))
        localStringBuffer.append(' ');
      i += 1;
    }
    return localStringBuffer.toString();
  }

  private void displayDialogContent(LinearLayout paramLinearLayout, DPObject[] paramArrayOfDPObject)
  {
    paramLinearLayout.findViewById(R.id.content_using_time).setVisibility(8);
    paramLinearLayout.findViewById(R.id.content_rules).setVisibility(8);
    paramLinearLayout.findViewById(R.id.content_valid_time).setVisibility(8);
    paramLinearLayout.findViewById(R.id.content_valid_forthline).setVisibility(8);
    paramLinearLayout.findViewById(R.id.content_valid_fifthline).setVisibility(8);
    int j = paramArrayOfDPObject.length;
    int i = 0;
    if (i < j)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      int k = localDPObject.getInt("Type");
      String[] arrayOfString = localDPObject.getStringArray("RuleDescs");
      switch (k)
      {
      default:
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      }
      while (true)
      {
        i += 1;
        break;
        ((TextView)paramLinearLayout.findViewById(R.id.content_using_time_header)).setText(localDPObject.getString("Title"));
        ((TextView)paramLinearLayout.findViewById(R.id.using_time)).setText(addSpaceToCnPunc(arrayOfString[0]));
        paramLinearLayout.findViewById(R.id.content_using_time).setVisibility(0);
        continue;
        ((TextView)paramLinearLayout.findViewById(R.id.content_rules_header)).setText(localDPObject.getString("Title"));
        displayDialogContentRules((LinearLayout)paramLinearLayout.findViewById(R.id.rules), arrayOfString);
        paramLinearLayout.findViewById(R.id.content_rules).setVisibility(0);
        continue;
        ((TextView)paramLinearLayout.findViewById(R.id.content_valid_time_header)).setText(localDPObject.getString("Title"));
        ((TextView)paramLinearLayout.findViewById(R.id.valid_time)).setText(addSpaceToCnPunc(arrayOfString[0]));
        paramLinearLayout.findViewById(R.id.content_valid_time).setVisibility(0);
        continue;
        ((TextView)paramLinearLayout.findViewById(R.id.content_forthline_header)).setText(localDPObject.getString("Title"));
        ((TextView)paramLinearLayout.findViewById(R.id.forthline_detail)).setText(addSpaceToCnPunc(arrayOfString[0]));
        paramLinearLayout.findViewById(R.id.content_valid_forthline).setVisibility(0);
        continue;
        ((TextView)paramLinearLayout.findViewById(R.id.content_fifthline_header)).setText(localDPObject.getString("Title"));
        ((TextView)paramLinearLayout.findViewById(R.id.fifthline_detail)).setText(addSpaceToCnPunc(arrayOfString[0]));
        paramLinearLayout.findViewById(R.id.content_valid_fifthline).setVisibility(0);
      }
    }
  }

  private void displayDialogContentRules(LinearLayout paramLinearLayout, String[] paramArrayOfString)
  {
    paramLinearLayout.removeAllViews();
    int j = paramArrayOfString.length;
    int i = 0;
    while (i < j)
    {
      String str = paramArrayOfString[i];
      RelativeLayout localRelativeLayout = (RelativeLayout)this.res.inflate(getContext(), R.layout.shop_huihui_discount_popup_content_rule, getParentView(), false);
      ((TextView)localRelativeLayout.findViewById(R.id.detail)).setText(addSpaceToCnPunc(str));
      paramLinearLayout.addView(localRelativeLayout);
      i += 1;
    }
  }

  private boolean isCnPunc(char paramChar)
  {
    char[] arrayOfChar = new char[8];
    char[] tmp6_5 = arrayOfChar;
    tmp6_5[0] = -244;
    char[] tmp11_6 = tmp6_5;
    tmp11_6[1] = 12290;
    char[] tmp16_11 = tmp11_6;
    tmp16_11[2] = -230;
    char[] tmp21_16 = tmp16_11;
    tmp21_16[3] = -229;
    char[] tmp26_21 = tmp21_16;
    tmp26_21[4] = -230;
    char[] tmp31_26 = tmp26_21;
    tmp31_26[5] = 12289;
    char[] tmp36_31 = tmp31_26;
    tmp36_31[6] = -248;
    char[] tmp42_36 = tmp36_31;
    tmp42_36[7] = -247;
    tmp42_36;
    int j = arrayOfChar.length;
    int i = 0;
    while (i < j)
    {
      if (arrayOfChar[i] == paramChar)
        return true;
      i += 1;
    }
    return false;
  }

  Dialog buildDialog(DialogInfo paramDialogInfo)
  {
    if (this.popup == null)
    {
      this.popup = new Dialog(getContext());
      this.popup.requestWindowFeature(1);
      this.popup.setContentView(R.layout.shop_huihui_discount_popup);
      this.popup.getWindow().setBackgroundDrawableResource(R.color.transparent);
      this.popup.getWindow().setLayout(-1, -2);
      this.popup.getWindow().setGravity(1);
      this.popup.setCanceledOnTouchOutside(true);
    }
    ((TextView)this.popup.findViewById(R.id.title)).setText(paramDialogInfo.title);
    Object localObject = (LinearLayout)this.popup.findViewById(R.id.content);
    ImageView localImageView;
    if ((paramDialogInfo.content != null) && (paramDialogInfo.content.length != 0))
    {
      displayDialogContent((LinearLayout)localObject, paramDialogInfo.content);
      ((LinearLayout)localObject).setVisibility(0);
      localObject = (Button)this.popup.findViewById(R.id.button);
      ((Button)localObject).setText(paramDialogInfo.buttonTxt);
      ((Button)localObject).setTag(Integer.valueOf(paramDialogInfo.promoId));
      localImageView = (ImageView)this.popup.findViewById(R.id.icon_stamp);
      switch (paramDialogInfo.status)
      {
      default:
      case 10:
      case 80:
      case 40:
      case 60:
      }
    }
    while (true)
    {
      return this.popup;
      ((LinearLayout)localObject).setVisibility(8);
      break;
      ((Button)localObject).setEnabled(true);
      localImageView.setVisibility(0);
      ((Button)localObject).setOnClickListener(this.gotoPayListener);
      continue;
      ((Button)localObject).setEnabled(false);
      localImageView.setVisibility(8);
    }
  }

  protected View createPromoCell(DPObject paramDPObject)
  {
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.pay_promo_shopinfo, getParentView(), false);
    localNovaRelativeLayout.setGAString("pay", getGAExtra());
    TextView localTextView1 = (TextView)localNovaRelativeLayout.findViewById(R.id.promo_title);
    String str1 = paramDPObject.getString("Desc");
    TextView localTextView2 = (TextView)localNovaRelativeLayout.findViewById(R.id.promo_desc);
    TextView localTextView3 = (TextView)localNovaRelativeLayout.findViewById(R.id.promo_inventory);
    TextView localTextView4 = (TextView)localNovaRelativeLayout.findViewById(R.id.promo_tips);
    TextView localTextView5 = (TextView)localNovaRelativeLayout.findViewById(R.id.activity_desc);
    String str2 = paramDPObject.getString("GrabbedDesc");
    String str3 = paramDPObject.getString("Tips");
    ViewUtils.setVisibilityAndContent(localTextView3, str2);
    ViewUtils.setVisibilityAndContent(localTextView2, str1);
    ViewUtils.setVisibilityAndContent(localTextView5, this.activityDes);
    if (TextUtils.isEmpty(this.activityDes))
      ViewUtils.setVisibilityAndContent(localTextView4, str3);
    while (true)
    {
      setItemView(localNovaRelativeLayout, paramDPObject);
      localTextView1.setText(paramDPObject.getString("Title"));
      return localNovaRelativeLayout;
      localTextView4.setVisibility(8);
    }
  }

  void gotoPay()
  {
    Object localObject1;
    if ((getFragment() != null) && (this.mopayPromos != null))
    {
      localObject1 = this.mopayPromos.getString("UniCashierUrl");
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        localObject1 = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject1));
        getFragment().startActivity((Intent)localObject1);
      }
    }
    else
    {
      return;
    }
    Object localObject2 = getShop();
    if (localObject2 != null)
    {
      localObject1 = ((DPObject)localObject2).getString("BranchName");
      localObject2 = new StringBuilder().append(((DPObject)localObject2).getString("Name"));
      if (TextUtils.isEmpty((CharSequence)localObject1));
      for (localObject1 = ""; ; localObject1 = "(" + (String)localObject1 + ")")
      {
        localObject1 = (String)localObject1;
        localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://huiunifiedcashier"));
        ((Intent)localObject2).putExtra("shopid", this.shopId);
        ((Intent)localObject2).putExtra("shopname", (String)localObject1);
        ((Intent)localObject2).putExtra("source", 11);
        getFragment().startActivity((Intent)localObject2);
        return;
      }
    }
    Log.e("huihui", "gotoPay fail");
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null)
      return;
    label90: 
    do
    {
      while (true)
        if (getShopStatus() == 0)
        {
          if (!this.enabledMOPay)
            break;
          this.enabledMOPay = getShop().getBoolean("HasMOPay");
        }
        else
        {
          if (!this.enabledMOPay)
            continue;
          if (this.mopayPromos != null)
            break label90;
          showDefaultPay();
          return;
        }
      this.enabledMOPay = getShop().getBoolean("HasMOPay");
      if (!this.enabledMOPay)
        break;
      reqPayPromo();
      return;
    }
    while (getShopStatus() != 0);
    this.linearLayout = new LinearLayout(getContext());
    this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.linearLayout.setOrientation(1);
    this.expandLayout = new LinearLayout(getContext());
    this.expandLayout.setOrientation(1);
    paramBundle = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.phone_pay_cell, getParentView(), false);
    paramBundle.setGAString("pay", getGAExtra());
    Button localButton = (Button)paramBundle.findViewById(R.id.buy);
    localButton.setEnabled(this.payEnabled);
    Object localObject = this.mopayPromos.getString("TicketText");
    ViewUtils.setVisibilityAndContent((TextView)paramBundle.findViewById(R.id.text), (String)localObject);
    localObject = (TextView)paramBundle.findViewById(R.id.tip);
    localButton.setOnClickListener(this.gotoPayListener);
    paramBundle.setOnClickListener(this.gotoPayListener);
    ((TextView)localObject).setText(this.mopayPromos.getString("Title"));
    this.linearLayout.addView(paramBundle);
    if ((this.shopPromos != null) && (this.shopPromos.length > 0))
    {
      if (this.shopPromos.length != 1)
        break label367;
      this.linearLayout.addView(createPromoCell(this.shopPromos[0]));
    }
    while (true)
    {
      addCell(this.COMMON_CELL_INDEX, this.linearLayout, 0);
      if (!this.isExpand)
        break;
      this.linearLayout.postDelayed(new Runnable()
      {
        public void run()
        {
          HuiPayAgent.this.scrollToCenter();
        }
      }
      , 100L);
      return;
      label367: int i = 0;
      if (i < this.shopPromos.length)
      {
        int j = this.shopPromos[i].getInt("IsShow");
        if (this.shopPromos[i].getInt("Status") != 50)
        {
          if (j != 0)
            break label447;
          paramBundle = createPromoCell(this.shopPromos[i]);
          this.expandLayout.addView(paramBundle);
        }
        while (true)
        {
          i += 1;
          break;
          label447: if (j != 1)
            continue;
          this.linearLayout.addView(createPromoCell(this.shopPromos[i]));
        }
      }
      if (this.isExpand)
        this.expandLayout.setVisibility(0);
      while (true)
      {
        this.linearLayout.addView(this.expandLayout);
        this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.promo_expand, getParentView(), false));
        ((TextView)this.expandView.findViewById(R.id.expand_text)).setText("更多特惠");
        this.expandView.setClickable(true);
        setExpandState();
        this.expandView.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = HuiPayAgent.this;
            if (!HuiPayAgent.this.isExpand);
            for (boolean bool = true; ; bool = false)
            {
              paramView.isExpand = bool;
              HuiPayAgent.this.setExpandState();
              if (!HuiPayAgent.this.isExpand)
                break;
              HuiPayAgent.this.expandLayout.setVisibility(0);
              HuiPayAgent.this.statisticsEvent("pay5", "shopinfo5_pay_more", "展开", 0);
              return;
            }
            HuiPayAgent.this.expandLayout.setVisibility(8);
            HuiPayAgent.this.statisticsEvent("pay5", "shopinfo5_pay_more", "收起", 0);
          }
        });
        this.linearLayout.addView(this.expandView);
        if (this.expandLayout.getChildCount() == 0)
          break label614;
        this.expandView.setVisibility(0);
        break;
        this.expandLayout.setVisibility(8);
      }
      label614: this.expandView.setVisibility(8);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getFragment() == null);
    do
    {
      return;
      this.shopId = shopId();
      this.enabledMOPay = ConfigHelper.enableMOPay;
      if ((this.enabledMOPay) && (getShop() != null))
        this.enabledMOPay = getShop().getBoolean("HasMOPay");
      if (paramBundle == null)
        continue;
      paramBundle = paramBundle.getParcelableArray("shopPromos");
      if ((paramBundle == null) || (paramBundle.length <= 0))
        continue;
      this.shopPromos = new DPObject[paramBundle.length];
      int i = 0;
      while (i < paramBundle.length)
      {
        this.shopPromos[i] = ((DPObject)paramBundle[i]);
        i += 1;
      }
    }
    while (!this.enabledMOPay);
    reqPayPromo();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.payPromoReq != null)
    {
      getFragment().mapiService().abort(this.payPromoReq, this, true);
      this.payPromoReq = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.payPromoReq)
      this.payPromoReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.payPromoReq) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      this.mopayPromos = ((DPObject)paramMApiResponse.result());
      this.shopPromos = this.mopayPromos.getArray("Offers");
      if (this.mopayPromos.getInt("ShowStatus") != 10)
        break label108;
    }
    label108: for (boolean bool = true; ; bool = false)
    {
      this.payEnabled = bool;
      this.businessType = this.mopayPromos.getInt("BusinessType");
      this.activityDes = this.mopayPromos.getString("ActivityDes");
      dispatchAgentChanged(false);
      this.payPromoReq = null;
      return;
    }
  }

  protected void reqPayPromo()
  {
    if (getFragment() == null)
      return;
    if (this.payPromoReq != null)
      getFragment().mapiService().abort(this.payPromoReq, this, true);
    String str = null;
    if (getFragment().accountService() != null)
      str = getFragment().accountService().token();
    this.payPromoReq = BasicMApiRequest.mapiGet(Uri.parse("http://hui.api.dianping.com/getmopaypromos.hui").buildUpon().appendQueryParameter("shopid", String.valueOf(this.shopId)).appendQueryParameter("token", str).appendQueryParameter("clientuuid", Environment.uuid()).appendQueryParameter("promostring", getShopExtraParam()).appendQueryParameter("cityid", Integer.toString(cityId())).build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.payPromoReq, this);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelableArray("shopPromos", this.shopPromos);
    return localBundle;
  }

  protected void scrollToCenter()
  {
    ScrollView localScrollView = getFragment().getScrollView();
    localScrollView.setSmoothScrollingEnabled(true);
    localScrollView.requestChildFocus(this.linearLayout, this.linearLayout);
  }

  void setExpandState()
  {
    if (this.expandView == null)
      return;
    if (this.isExpand)
    {
      ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_up);
      ((TextView)this.expandView.findViewById(R.id.expand_text)).setText("收起");
      scrollToCenter();
      return;
    }
    ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_down);
    ((TextView)this.expandView.findViewById(R.id.expand_text)).setText("更多特惠");
  }

  protected void setItemView(View paramView, DPObject paramDPObject)
  {
    int j = paramDPObject.getInt("ID");
    int k = paramDPObject.getInt("Status");
    int i;
    Object localObject;
    String str;
    if (paramDPObject.contains("ShowPop"))
    {
      i = paramDPObject.getInt("ShowPop");
      ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.status_text), paramDPObject.getString("StatusDesc"));
      if (paramView.getTag() != null)
        break label166;
      localObject = paramDPObject.getObject("RuleDo");
      if (localObject != null)
        break label142;
      str = "";
      label82: if (localObject != null)
        break label153;
      localObject = null;
      label90: paramView.setTag(new DialogInfo(str, localObject, paramDPObject.getString("OperateTip"), j, k));
    }
    while (true)
    {
      if (i != 1)
        break label198;
      paramView.setOnClickListener(this.showDialogListener);
      paramView.setClickable(true);
      return;
      i = 1;
      break;
      label142: str = ((DPObject)localObject).getString("Title");
      break label82;
      label153: localObject = ((DPObject)localObject).getArray("RuleDetailDos");
      break label90;
      label166: ((DialogInfo)paramView.getTag()).buttonTxt = paramDPObject.getString("OperateTip");
      ((DialogInfo)paramView.getTag()).status = k;
    }
    label198: paramView.setClickable(false);
  }

  protected void showDefaultPay()
  {
    removeAllCells();
    View localView = this.res.inflate(getContext(), R.layout.phone_pay_cell, getParentView(), false);
    TextView localTextView = (TextView)localView.findViewById(R.id.tip);
    Button localButton = (Button)localView.findViewById(R.id.buy);
    localTextView.setText("优惠即将开始");
    localButton.setOnClickListener(this.gotoPayListener);
    localView.setOnClickListener(this.gotoPayListener);
    addCell(this.COMMON_CELL_INDEX, this.linearLayout, 0);
  }

  class DialogInfo
  {
    String buttonTxt;
    DPObject[] content;
    int promoId;
    int status;
    String title;

    public DialogInfo(String paramArrayOfDPObject, DPObject[] paramString1, String paramInt1, int paramInt2, int arg6)
    {
      this.title = paramArrayOfDPObject;
      this.content = paramString1;
      this.buttonTxt = paramInt1;
      this.promoId = paramInt2;
      int i;
      this.status = i;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.HuiPayAgent
 * JD-Core Version:    0.6.0
 */
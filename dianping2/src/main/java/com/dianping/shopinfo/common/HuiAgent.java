package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.RichTextView;
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
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;

public class HuiAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String HIDE_PROMO = "收起";
  protected static final String MORE_PROMO = "更多优惠";
  protected String COMMON_CELL_INDEX = "0475HuiPay.10Hui";
  protected DPObject MOPayPromosInfoDo;
  boolean enabledMOPay;
  LinearLayout expandLayout;
  protected NovaRelativeLayout expandView;
  boolean isExpand;
  LinearLayout linearLayout;
  boolean payEnabled = false;
  private MApiRequest payPromoReq;
  private int shopId;

  public HuiAgent(Object paramObject)
  {
    super(paramObject);
  }

  private ViewGroup createHuiHeader()
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shopinfo_hui_header, getParentView(), false);
    TextView localTextView1 = (TextView)localNovaLinearLayout.findViewById(R.id.title);
    TextView localTextView2 = (TextView)localNovaLinearLayout.findViewById(R.id.promo1);
    TextView localTextView3 = (TextView)localNovaLinearLayout.findViewById(R.id.promo2);
    TextView localTextView4 = (TextView)localNovaLinearLayout.findViewById(R.id.hui_salecount);
    String str1 = this.MOPayPromosInfoDo.getString("Title");
    String str2 = this.MOPayPromosInfoDo.getString("UniCashierUrl");
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "";
    arrayOfString[1] = "";
    Object localObject = this.MOPayPromosInfoDo.getArray("Tags");
    if ((localObject != null) && (localObject.length > 0))
    {
      int i = 0;
      while ((i < localObject.length) && (i < 2))
      {
        arrayOfString[i] = localObject[i].getString("Name");
        i += 1;
      }
    }
    localObject = this.MOPayPromosInfoDo.getString("OrderNumDesc");
    if (!TextUtils.isEmpty(str1))
      localTextView1.setText(str1);
    if (!TextUtils.isEmpty(arrayOfString[0]))
    {
      localTextView2.setVisibility(0);
      localTextView2.setText(arrayOfString[0]);
      if (TextUtils.isEmpty(arrayOfString[1]))
        break label290;
      localNovaLinearLayout.findViewById(R.id.promo2_content).setVisibility(0);
      localTextView3.setText(arrayOfString[1]);
    }
    while (true)
    {
      if (!TextUtils.isEmpty((CharSequence)localObject))
        localTextView4.setText((CharSequence)localObject);
      localNovaLinearLayout.setOnClickListener(new View.OnClickListener(str2)
      {
        public void onClick(View paramView)
        {
          try
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$UniCashierUrl));
            HuiAgent.this.getFragment().startActivity(paramView);
            return;
          }
          catch (java.lang.Exception paramView)
          {
          }
        }
      });
      return localNovaLinearLayout;
      localTextView2.setVisibility(8);
      break;
      label290: localNovaLinearLayout.findViewById(R.id.promo2_content).setVisibility(8);
    }
  }

  protected ViewGroup createPromoCell(DPObject paramDPObject)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.shopinfo_hui_item, getParentView(), false);
    localNovaLinearLayout.setGAString("hui_ai", getGAExtra());
    TextView localTextView1 = (TextView)localNovaLinearLayout.findViewById(R.id.hui_item_title);
    TextView localTextView2 = (TextView)localNovaLinearLayout.findViewById(R.id.hui_item_time);
    TextView localTextView3 = (TextView)localNovaLinearLayout.findViewById(R.id.hui_item_status);
    RichTextView localRichTextView = (RichTextView)localNovaLinearLayout.findViewById(R.id.price);
    String str1 = paramDPObject.getString("Title");
    String str2 = paramDPObject.getString("PromoDesc");
    String str3 = paramDPObject.getString("StatusDesc");
    String str4 = paramDPObject.getString("RichDesc");
    int i;
    if (paramDPObject.getInt("Status") == 0)
    {
      i = -6710887;
      paramDPObject = this.MOPayPromosInfoDo.getString("UniCashierUrl");
      localTextView1.setTextColor(i);
      localTextView1.setText(str1);
      if (TextUtils.isEmpty(str2))
        break label233;
      localTextView2.setVisibility(0);
      localTextView2.setText(str2);
      label169: if (TextUtils.isEmpty(str3))
        break label243;
      localTextView3.setVisibility(0);
      localTextView3.setText(str3);
      label190: if (TextUtils.isEmpty(str4))
        break label253;
      localRichTextView.setVisibility(0);
      localRichTextView.setRichText(str4);
    }
    while (true)
    {
      localNovaLinearLayout.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          try
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$UniCashierUrl));
            HuiAgent.this.getFragment().startActivity(paramView);
            return;
          }
          catch (java.lang.Exception paramView)
          {
          }
        }
      });
      return localNovaLinearLayout;
      i = -16777216;
      break;
      label233: localTextView2.setVisibility(8);
      break label169;
      label243: localTextView3.setVisibility(8);
      break label190;
      label253: localRichTextView.setVisibility(8);
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null)
      return;
    ViewGroup localViewGroup;
    int i;
    while (true)
      if (getShopStatus() == 0)
      {
        if (this.enabledMOPay)
          this.enabledMOPay = getShop().getBoolean("HasMOPay");
      }
      else
      {
        if ((!this.enabledMOPay) || (this.MOPayPromosInfoDo == null))
          continue;
        this.linearLayout = new LinearLayout(getContext());
        this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        this.linearLayout.setOrientation(1);
        this.expandLayout = new LinearLayout(getContext());
        this.expandLayout.setOrientation(1);
        localViewGroup = createHuiHeader();
        this.linearLayout.addView(localViewGroup);
        paramBundle = this.MOPayPromosInfoDo.getArray("Promos");
        if ((paramBundle == null) || (paramBundle.length <= 0))
          break label502;
        localViewGroup.findViewById(R.id.line).setVisibility(0);
        i = 0;
        int j = paramBundle.length;
        label171: if (i >= j)
          break label258;
        localViewGroup = paramBundle[i];
        if (localViewGroup == null)
          break;
        if (!localViewGroup.getBoolean("IsShow"))
          break label241;
        localViewGroup = createPromoCell(localViewGroup);
        this.linearLayout.addView(localViewGroup);
      }
    while (true)
    {
      i += 1;
      break label171;
      this.enabledMOPay = getShop().getBoolean("HasMOPay");
      if (!this.enabledMOPay)
        break;
      reqHuiPromo();
      return;
      label241: localViewGroup = createPromoCell(localViewGroup);
      this.expandLayout.addView(localViewGroup);
    }
    label258: if (this.isExpand)
    {
      this.expandLayout.setVisibility(0);
      label273: this.linearLayout.addView(this.expandLayout);
      if (paramBundle.length > 1)
      {
        this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.promo_expand, getParentView(), false));
        paramBundle = new RelativeLayout.LayoutParams(-1, 1);
        paramBundle.leftMargin = ViewUtils.dip2px(getContext(), 50.0F);
        this.expandView.findViewById(R.id.line).setLayoutParams(paramBundle);
        ((TextView)this.expandView.findViewById(R.id.expand_text)).setText("更多优惠");
        this.expandView.setGAString("hui_ai_expand");
        this.expandView.setClickable(true);
        setExpandState();
        this.expandView.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = HuiAgent.this;
            if (!HuiAgent.this.isExpand);
            for (boolean bool = true; ; bool = false)
            {
              paramView.isExpand = bool;
              HuiAgent.this.setExpandState();
              if (!HuiAgent.this.isExpand)
                break;
              HuiAgent.this.expandView.setGAString("hui_ai_contract");
              HuiAgent.this.expandLayout.setVisibility(0);
              return;
            }
            HuiAgent.this.expandView.setGAString("hui_ai_expand");
            HuiAgent.this.expandLayout.setVisibility(8);
          }
        });
        this.linearLayout.addView(this.expandView);
        if (this.expandLayout.getChildCount() == 0)
          break label490;
        this.expandView.setVisibility(0);
      }
    }
    while (true)
    {
      addCell(this.COMMON_CELL_INDEX, this.linearLayout, 64);
      if (!this.isExpand)
        break;
      this.linearLayout.postDelayed(new Runnable()
      {
        public void run()
        {
          HuiAgent.this.scrollToCenter();
        }
      }
      , 100L);
      return;
      this.expandLayout.setVisibility(8);
      break label273;
      label490: this.expandView.setVisibility(8);
      continue;
      label502: localViewGroup.findViewById(R.id.line).setVisibility(4);
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
      this.MOPayPromosInfoDo = ((DPObject)paramBundle.getParcelable("MOPayPromosInfoDo"));
    }
    while (!this.enabledMOPay);
    reqHuiPromo();
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
      this.MOPayPromosInfoDo = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
      this.payPromoReq = null;
    }
  }

  protected void reqHuiPromo()
  {
    if (getFragment() == null)
      return;
    if (this.payPromoReq != null)
      getFragment().mapiService().abort(this.payPromoReq, this, true);
    this.payPromoReq = BasicMApiRequest.mapiGet(Uri.parse("http://hui.api.dianping.com/getmopaypromosinfo.hui").buildUpon().appendQueryParameter("shopId", String.valueOf(this.shopId)).appendQueryParameter("promostring", getShopExtraParam()).appendQueryParameter("clientuuid", Environment.uuid()).appendQueryParameter("cityid", Integer.toString(cityId())).build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.payPromoReq, this);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("MOPayPromosInfoDo", this.MOPayPromosInfoDo);
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
    ((TextView)this.expandView.findViewById(R.id.expand_text)).setText("更多优惠");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.HuiAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.base.tuan.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.BuyDealView;
import com.dianping.base.widget.BuyDealView.OnBuyClickListener;
import com.dianping.base.widget.CssStyleManager;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class DealDetailMoreActivity extends BaseTuanActivity
  implements AccountListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int EXTRA_TYPE_MOREDETAIL = 1000;
  private final DecimalFormat PRICE_DF = new DecimalFormat("#.###");
  private Button btnBuy;
  private BuyDealView buyDealView;
  private WebView contentWebView;
  protected MApiRequest dealInfoRequest;
  protected DPObject dpobjDeal;
  private ArrayList<DPObject> dpobjPairList = new ArrayList();

  private boolean checkIsLocked()
  {
    if (accountService().token() == null);
    do
      return false;
    while (!getAccount().grouponIsLocked());
    new AlertDialog.Builder(this).setTitle("提示").setMessage("您的账户存在异常已被锁定，请联系客服为您解除锁定。").setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        DealDetailMoreActivity.this.finish();
      }
    }).setCancelable(false).show();
    return true;
  }

  public void buy()
  {
    if (this.dpobjDeal == null);
    do
    {
      return;
      if ((ConfigHelper.dynamicLogin) && (this.dpobjDeal.getInt("DealType") != 2));
      for (int i = 1; (!isLogined()) && (i == 0); i = 0)
      {
        gotoLogin();
        return;
      }
    }
    while (checkIsLocked());
    if ((this.dpobjDeal.getArray("DealSelectList") != null) && (this.dpobjDeal.getArray("DealSelectList").length > 1))
    {
      localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://dealselector"));
      localIntent.putExtra("dpDeal", this.dpobjDeal);
      startActivity(localIntent);
      return;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
    localIntent.putExtra("deal", this.dpobjDeal);
    startActivity(localIntent);
  }

  protected void loadDealInfo()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://app.t.dianping.com/");
    localStringBuilder.append("dealgn.bin");
    localStringBuilder.append("?cityid=").append(city().id());
    localStringBuilder.append("&id=").append(this.dpobjDeal.getInt("ID"));
    Object localObject = accountService().token();
    if (!TextUtils.isEmpty((CharSequence)localObject))
      localStringBuilder.append("&token=").append((String)localObject);
    localObject = location();
    if (localObject != null)
    {
      localStringBuilder.append("&lat=").append(((Location)localObject).latitude());
      localStringBuilder.append("&lng=").append(((Location)localObject).longitude());
    }
    this.dealInfoRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.dealInfoRequest, this);
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    loadDealInfo();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null);
    for (this.dpobjDeal = ((DPObject)paramBundle.getParcelable("mDeal")); this.dpobjDeal == null; this.dpobjDeal = ((DPObject)getIntent().getParcelableExtra("mDeal")))
    {
      finish();
      return;
    }
    Object localObject = this.dpobjDeal.getArray("StructedDetails");
    if (localObject != null)
    {
      paramBundle = (Bundle)localObject;
      if (localObject.length != 0);
    }
    else
    {
      paramBundle = this.dpobjDeal.getArray("DetailInfo");
    }
    if ((paramBundle == null) || (paramBundle.length == 0))
    {
      finish();
      return;
    }
    this.dpobjPairList.addAll(Arrays.asList(paramBundle));
    setContentView(R.layout.deal_detail_more_layout);
    this.buyDealView = ((BuyDealView)findViewById(R.id.deal_buy_item));
    this.buyDealView.setOnBuyClickListener(new BuyDealView.OnBuyClickListener()
    {
      public void onClick(View paramView)
      {
        DealDetailMoreActivity.this.buy();
        DealDetailMoreActivity.this.statisticsEvent("tuan5", "tuan5_detail_morebuy", DealDetailMoreActivity.this.dpobjDeal.getInt("ID") + "", 0);
      }
    });
    this.contentWebView = ((WebView)findViewById(R.id.content_webview));
    this.contentWebView.setPadding(10, 0, 10, 0);
    paramBundle = "";
    localObject = this.dpobjPairList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      DPObject localDPObject = (DPObject)((Iterator)localObject).next();
      if (localDPObject.getInt("Type") != 1000)
        continue;
      paramBundle = paramBundle + localDPObject.getString("Name");
    }
    localObject = this.contentWebView.getSettings();
    ((WebSettings)localObject).setBuiltInZoomControls(false);
    ((WebSettings)localObject).setSaveFormData(false);
    ((WebSettings)localObject).setSavePassword(false);
    ((WebSettings)localObject).setJavaScriptEnabled(true);
    ((WebSettings)localObject).setSupportZoom(false);
    ((WebSettings)localObject).setUseWideViewPort(false);
    ((WebSettings)localObject).setDomStorageEnabled(true);
    ((WebSettings)localObject).setJavaScriptCanOpenWindowsAutomatically(true);
    this.contentWebView.setScrollBarStyle(0);
    this.contentWebView.setWebViewClient(new WebViewClient()
    {
      public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
      {
        if (paramString.startsWith("dianping://showcheckinphoto"))
          paramWebView = "";
        try
        {
          paramString = URLDecoder.decode(paramString.replace("dianping://showcheckinphoto?img=", ""), "UTF-8");
          paramWebView = paramString;
          if ((paramWebView != null) && (!"".equals(paramWebView)))
          {
            paramString = new ArrayList();
            paramString.add(new DPObject().edit().putString("Url", paramWebView).generate());
            paramWebView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showcheckinphoto"));
            paramWebView.putExtra("position", 0);
            paramWebView.putParcelableArrayListExtra("pageList", paramString);
            DealDetailMoreActivity.this.startActivity(paramWebView);
          }
          return true;
        }
        catch (UnsupportedEncodingException paramString)
        {
          while (true)
            paramString.printStackTrace();
        }
      }
    });
    this.contentWebView.loadDataWithBaseURL(CssStyleManager.instance(this).getCssFilePath(), CssStyleManager.instance(this).makeHtml(paramBundle.trim(), true), "text/html", "UTF-8", null);
    if (this.dpobjDeal.getBoolean("IsGoodShop"))
    {
      paramBundle = (RelativeLayout.LayoutParams)this.buyDealView.getLayoutParams();
      paramBundle.addRule(12, -1);
      this.buyDealView.setLayoutParams(paramBundle);
      paramBundle = (RelativeLayout.LayoutParams)this.contentWebView.getLayoutParams();
      paramBundle.addRule(2, this.buyDealView.getId());
      this.contentWebView.setLayoutParams(paramBundle);
    }
    while (true)
    {
      updateBuyItemView();
      return;
      paramBundle = (RelativeLayout.LayoutParams)this.buyDealView.getLayoutParams();
      paramBundle.addRule(10, -1);
      this.buyDealView.setLayoutParams(paramBundle);
      paramBundle = (RelativeLayout.LayoutParams)this.contentWebView.getLayoutParams();
      paramBundle.addRule(3, this.buyDealView.getId());
      this.contentWebView.setLayoutParams(paramBundle);
    }
  }

  protected void onDestroy()
  {
    if (this.dealInfoRequest != null)
    {
      mapiService().abort(this.dealInfoRequest, this, true);
      this.dealInfoRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.dealInfoRequest)
      this.dealInfoRequest = null;
    paramMApiRequest = paramMApiResponse.message();
    if (paramMApiRequest != null)
      Toast.makeText(this, paramMApiRequest.content(), 1).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.dealInfoRequest);
    try
    {
      this.dpobjDeal = ((DPObject)paramMApiResponse.result());
      updateBuyItemView();
      this.dealInfoRequest = null;
      return;
    }
    catch (Exception paramMApiRequest)
    {
      while (true)
        paramMApiRequest.printStackTrace();
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("mDeal", this.dpobjDeal);
  }

  public void updateBuyItemView()
  {
    if ((this.dpobjDeal == null) || (this.dpobjDeal.getInt("ID") == 0))
      return;
    this.buyDealView.setDeal(this.dpobjDeal);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.activity.DealDetailMoreActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.wed.baby.activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.FragmentTabActivity.TabManager;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.base.widget.NovaFragmentTabActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.loader.MyResources.ResourceOverrideable;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.baby.fragment.WeddingProductPicDetailFragment;
import com.dianping.wed.baby.fragment.WeddingProductRecommandFragment;
import com.dianping.wed.baby.fragment.WeddingProductTextDetailFragment;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaTextView;

public class WeddingProductDetailsActivity extends NovaFragmentTabActivity
  implements MyResources.ResourceOverrideable, View.OnClickListener, TabHost.OnTabChangeListener, RequestHandler<MApiRequest, MApiResponse>
{
  private AssetManager assetManager;
  private String bookingbtntext;
  NovaButton btnBooking;
  DPObject chatObject;
  MApiRequest chatRequest;
  private DPObject dpProduct;
  private DPObject dpShop;
  private int index = 0;
  int mCurTab = 0;
  private MyResources myResources;
  private String productCategoryID;
  private int productId;
  private Resources resources;
  private int shopId;
  private String shopName;
  private Resources.Theme theme;

  public void finish()
  {
    super.finish();
    overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
  }

  public AssetManager getAssets()
  {
    if (this.assetManager == null)
      return super.getAssets();
    return this.assetManager;
  }

  public MyResources getOverrideResources()
  {
    return this.myResources;
  }

  public Resources getResources()
  {
    if (this.resources == null)
      return super.getResources();
    return this.resources;
  }

  public Resources.Theme getTheme()
  {
    if (this.theme == null)
      return super.getTheme();
    return this.theme;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  void initFragments()
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("pictype", 0);
    localBundle.putInt("productId", this.productId);
    if (this.dpProduct != null)
      localBundle.putParcelable("product", this.dpProduct);
    TextView localTextView = new TextView(this);
    localTextView.setGravity(17);
    localTextView.setText("图文详情");
    localTextView.setTextColor(getResources().getColorStateList(R.color.text_color_ligth_orange_to_white));
    localTextView.setTextSize(2, 16.0F);
    localTextView.setBackgroundResource(R.drawable.wed_selector_tab_view);
    this.mTabManager.addTab(this.mTabHost.newTabSpec("picdetail").setIndicator(localTextView), WeddingProductPicDetailFragment.class, localBundle);
    localTextView = new TextView(this);
    localTextView.setGravity(17);
    localTextView.setText("套餐详情");
    localTextView.setTextColor(getResources().getColorStateList(R.color.text_color_ligth_orange_to_white));
    localTextView.setTextSize(2, 16.0F);
    localTextView.setBackgroundResource(R.drawable.wed_selector_tab_view);
    this.mTabManager.addTab(this.mTabHost.newTabSpec("textdetail").setIndicator(localTextView), WeddingProductTextDetailFragment.class, localBundle);
    if (this.shopId > 0)
    {
      localBundle.putInt("shopid", this.shopId);
      localTextView = new TextView(this);
      localTextView.setGravity(17);
      localTextView.setText("商户推荐");
      localTextView.setTextColor(getResources().getColorStateList(R.color.text_color_ligth_orange_to_white));
      localTextView.setTextSize(2, 16.0F);
      localTextView.setBackgroundResource(R.drawable.wed_selector_tab_view);
      this.mTabManager.addTab(this.mTabHost.newTabSpec("recommanddetail").setIndicator(localTextView), WeddingProductRecommandFragment.class, localBundle);
    }
    if (this.index == 1)
    {
      this.mTabHost.setCurrentTabByTag("textdetail");
      return;
    }
    if ((this.index == 2) && (this.shopId > 0))
    {
      this.mTabHost.setCurrentTabByTag("recommanddetail");
      return;
    }
    this.mTabHost.setCurrentTabByTag("picdetail");
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.button_wedding_booking)
    {
      paramView = Uri.parse("dianping://weddingbabybooking").buildUpon();
      paramView.appendQueryParameter("shopid", this.shopId + "").appendQueryParameter("productid", this.productId + "").appendQueryParameter("bookingBtnText", this.bookingbtntext);
      if (!TextUtils.isEmpty(this.shopName))
        paramView.appendQueryParameter("shopname", this.shopName);
      if (!TextUtils.isEmpty(getStringParam("productCategoryID")))
        paramView.appendQueryParameter("productcategoryid", getStringParam("productCategoryID"));
      startActivity(new Intent("android.intent.action.VIEW", paramView.build()));
    }
    label650: 
    do
      while (true)
      {
        return;
        if (paramView.getId() == R.id.textview_wedding_booking)
        {
          if (this.dpShop == null)
            continue;
          paramView = this.dpShop.getStringArray("PhoneNos");
          if ((paramView == null) || (paramView.length <= 0))
            continue;
          if (paramView.length == 1)
          {
            TelephoneUtils.dial(this, this.dpShop, paramView[0]);
            return;
          }
          new AlertDialog.Builder(this).setTitle("联系商户").setAdapter(new ArrayAdapter(this, R.layout.simple_list_item_1, 16908308, paramView, paramView)
          {
            public String getItem(int paramInt)
            {
              return "拨打电话：" + this.val$phoneNos[paramInt];
            }
          }
          , new DialogInterface.OnClickListener(paramView)
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              TelephoneUtils.dial(WeddingProductDetailsActivity.this, WeddingProductDetailsActivity.this.dpShop, this.val$phoneNos[paramInt]);
            }
          }).show();
          return;
        }
        if (paramView.getId() == R.id.back_view)
        {
          onBackPressed();
          return;
        }
        if (paramView.getId() != R.id.share_view)
          break;
        if ((this.shopId <= 0) || (this.productId <= 0))
          continue;
        ShareHolder localShareHolder = new ShareHolder();
        if (this.dpProduct == null)
        {
          paramView = "这个东西很不错哦！";
          localShareHolder.title = paramView;
          if (this.dpProduct != null)
            break label650;
        }
        for (paramView = ""; ; paramView = this.dpProduct.getString("DefaultPic"))
        {
          localShareHolder.imageUrl = paramView;
          localShareHolder.webUrl = ("http://m.dianping.com/wed/mobile/shop/" + this.shopId + "/product/" + this.productId);
          paramView = new StringBuilder("在大众点评上发现这个东西很不错哦！");
          if (this.dpProduct != null)
          {
            paramView.append(this.dpProduct.getString("Name") + "，");
            if (this.dpProduct.getInt("ShowPriceType") == 1)
              paramView.append("￥" + this.dpProduct.getInt("Price") + "，");
          }
          if (this.dpShop != null)
          {
            paramView.append(this.dpShop.getString("Name") + "，");
            paramView.append(this.dpShop.getString("Address") + "。");
          }
          localShareHolder.desc = paramView.toString();
          ShareUtil.gotoShareTo(this, ShareType.WEB, localShareHolder, "", "", 0);
          paramView = getCloneUserInfo();
          paramView.shop_id = Integer.valueOf(this.shopId);
          paramView.biz_id = (this.productId + "");
          GAHelper.instance().contextStatisticsEvent(this, "share", paramView, "tap");
          return;
          paramView = this.dpProduct.getString("Name");
          break;
        }
      }
    while ((paramView.getId() != R.id.textview_wedding_chat) || (this.chatObject == null));
    paramView = this.chatObject.getString("RedirectLink");
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramView)));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getIntent().getData() != null)
    {
      this.shopName = getStringParam("shopName");
      this.productCategoryID = getStringParam("productCategoryID");
      this.productId = getIntParam("productid");
      this.shopId = getIntParam("shopid");
      this.dpShop = getObjectParam("shop");
      this.dpProduct = getObjectParam("product");
      this.index = getIntParam("index");
    }
    while (true)
    {
      hideTitleBar();
      initFragments();
      this.btnBooking = ((NovaButton)findViewById(R.id.button_wedding_booking));
      if (!TextUtils.isEmpty(getStringParam("bookingbtntext")))
      {
        this.bookingbtntext = getStringParam("bookingbtntext");
        this.btnBooking.setText(this.bookingbtntext);
      }
      this.btnBooking.setOnClickListener(this);
      sendChatRequest();
      paramBundle = getCloneUserInfo();
      paramBundle.shop_id = Integer.valueOf(this.shopId);
      paramBundle.biz_id = (this.productId + "");
      this.btnBooking.setGAString("actionbar_wedbooking", paramBundle);
      NovaTextView localNovaTextView = (NovaTextView)findViewById(R.id.textview_wedding_booking);
      localNovaTextView.setOnClickListener(this);
      localNovaTextView.setGAString("actionbar_tel", paramBundle);
      findViewById(R.id.back_view).setOnClickListener(this);
      findViewById(R.id.share_view).setOnClickListener(this);
      return;
      if ((paramBundle == null) || (!paramBundle.containsKey("shopid")))
        continue;
      this.shopName = paramBundle.getString("shopName");
      this.productCategoryID = paramBundle.getString("productCategoryID");
      this.productId = paramBundle.getInt("productid");
      this.shopId = paramBundle.getInt("shopid");
      this.dpShop = ((DPObject)paramBundle.getParcelable("shop"));
      this.dpProduct = ((DPObject)paramBundle.getParcelable("product"));
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.chatRequest)
      this.chatRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.chatRequest)
    {
      this.chatObject = ((DPObject)paramMApiResponse.result());
      if (this.chatObject != null)
      {
        paramMApiRequest = (NovaTextView)findViewById(R.id.textview_wedding_chat);
        paramMApiRequest.setOnClickListener(this);
        paramMApiRequest.setGAString("actionbar_im");
        if (this.chatObject.getInt("Visible") != 0)
          break label114;
        ((LinearLayout.LayoutParams)this.btnBooking.getLayoutParams()).leftMargin = ViewUtils.dip2px(this, 10.0F);
        paramMApiRequest.setVisibility(8);
      }
    }
    while (this.chatObject.getInt("MessageCount") > 0)
    {
      paramMApiRequest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.wed_icon_chat_red, 0, 0);
      return;
      label114: paramMApiRequest.setVisibility(0);
      ((LinearLayout.LayoutParams)this.btnBooking.getLayoutParams()).leftMargin = 0;
      GAHelper.instance().contextStatisticsEvent(this, "actionbar_im", this.gaExtra, "view");
    }
    paramMApiRequest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.wed_icon_chat, 0, 0);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("shopName", this.shopName);
    paramBundle.putString("productCategoryID", this.productCategoryID);
    paramBundle.putInt("productid", this.productId);
    paramBundle.putInt("shopid", this.shopId);
    paramBundle.putParcelable("shop", this.dpShop);
    paramBundle.putParcelable("product", this.dpProduct);
  }

  public void onTabChanged(String paramString)
  {
    GAUserInfo localGAUserInfo = getCloneUserInfo();
    localGAUserInfo.biz_id = (this.productId + "");
    localGAUserInfo.shop_id = Integer.valueOf(this.shopId);
    if (this.dpShop != null)
      localGAUserInfo.category_id = Integer.valueOf(this.dpShop.getInt("CategoryID"));
    if ("picdetail".equals(paramString))
      GAHelper.instance().contextStatisticsEvent(this, "graphic_detail", localGAUserInfo, "tap");
    do
    {
      return;
      if (!"textdetail".equals(paramString))
        continue;
      GAHelper.instance().contextStatisticsEvent(this, "packages_detail", localGAUserInfo, "tap");
      return;
    }
    while (!"recommanddetail".equals(paramString));
    GAHelper.instance().contextStatisticsEvent(this, "shoprecommend", localGAUserInfo, "tap");
  }

  void sendChatRequest()
  {
    if (this.chatRequest != null);
    do
      return;
    while (this.shopId <= 0);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/realcomentrance.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", this.shopId + "");
    localBuilder.appendQueryParameter("productid", this.productId + "");
    if (isLogined())
      localBuilder.appendQueryParameter("token", accountService().token());
    this.chatRequest = mapiGet(this, localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.chatRequest, this);
  }

  protected void setOnContentView()
  {
    super.setContentView(R.layout.tab_pager_fragment_wedding_details);
  }

  public void setOverrideResources(MyResources paramMyResources)
  {
    if (paramMyResources == null)
    {
      this.myResources = null;
      this.resources = null;
      this.assetManager = null;
      this.theme = null;
      return;
    }
    this.myResources = paramMyResources;
    this.resources = paramMyResources.getResources();
    this.assetManager = paramMyResources.getAssets();
    paramMyResources = paramMyResources.getResources().newTheme();
    paramMyResources.setTo(getTheme());
    this.theme = paramMyResources;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.WeddingProductDetailsActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.hotel.shoplist.agent;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.shoplist.agent.ShopListContentAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.shoplist.util.ShopListUtils;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;

public class HotelShopListContentAgent extends ShopListContentAgent
{
  private static final String AD_SHOP_LOC_KEY = "__HotelShopListShopLocKey";
  private static final String SHOP_TAG_KEY = "__HotelShopListShopType";
  private static final int TYPE_AD = 2;
  private static final int TYPE_HOTEL = 0;
  private static final int TYPE_SEP = 1;
  private final int FIND = 1;
  private final int GUIDE = 2;
  private FrameLayout compass;
  private ImageView eye;
  private NovaLinearLayout find;
  private boolean findAnimationLoaded = false;
  private FrameLayout frameLayout;
  private boolean guideAnimationLoaded = false;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      super.handleMessage(paramMessage);
      switch (paramMessage.what)
      {
      default:
        return;
      case 0:
        paramMessage = new AnimationDrawable();
        paramMessage.addFrame(HotelShopListContentAgent.this.getResources().getDrawable(R.drawable.hotel_shoplist_eye3), 50);
        paramMessage.addFrame(HotelShopListContentAgent.this.getResources().getDrawable(R.drawable.hotel_shoplist_eye2), 50);
        paramMessage.addFrame(HotelShopListContentAgent.this.getResources().getDrawable(R.drawable.hotel_shoplist_eye1), 50);
        paramMessage.setOneShot(true);
        HotelShopListContentAgent.this.eye.setBackgroundDrawable(paramMessage);
        paramMessage.start();
        sendEmptyMessageDelayed(1, 5000L);
        return;
      case 1:
        paramMessage = new AnimationDrawable();
        paramMessage.addFrame(HotelShopListContentAgent.this.getResources().getDrawable(R.drawable.hotel_shoplist_eye1), 50);
        paramMessage.addFrame(HotelShopListContentAgent.this.getResources().getDrawable(R.drawable.hotel_shoplist_eye2), 50);
        paramMessage.addFrame(HotelShopListContentAgent.this.getResources().getDrawable(R.drawable.hotel_shoplist_eye3), 50);
        paramMessage.addFrame(HotelShopListContentAgent.this.getResources().getDrawable(R.drawable.hotel_shoplist_eye2), 50);
        paramMessage.addFrame(HotelShopListContentAgent.this.getResources().getDrawable(R.drawable.hotel_shoplist_eye1), 3000);
        paramMessage.setOneShot(false);
        HotelShopListContentAgent.this.eye.setBackgroundDrawable(paramMessage);
        paramMessage.start();
        return;
      case 2:
        paramMessage = new TranslateAnimation(0.0F, ViewUtils.dip2px(HotelShopListContentAgent.this.getContext(), 50.0F) * -1, 0.0F, 0.0F);
        paramMessage.setDuration(500L);
        paramMessage.setAnimationListener(new Animation.AnimationListener()
        {
          public void onAnimationEnd(Animation paramAnimation)
          {
            paramAnimation = (FrameLayout.LayoutParams)HotelShopListContentAgent.this.find.getLayoutParams();
            paramAnimation.rightMargin = (ViewUtils.dip2px(HotelShopListContentAgent.this.getContext(), 25.0F) * -1);
            HotelShopListContentAgent.this.find.setLayoutParams(paramAnimation);
            HotelShopListContentAgent.this.find.clearAnimation();
            if (HotelShopListContentAgent.this.type == 1)
              if (!HotelShopListContentAgent.this.findAnimationLoaded)
              {
                HotelShopListContentAgent.access$302(HotelShopListContentAgent.this, true);
                HotelShopListContentAgent.1.this.sendEmptyMessage(0);
              }
            do
              return;
            while ((HotelShopListContentAgent.this.type != 2) || (HotelShopListContentAgent.this.guideAnimationLoaded));
            HotelShopListContentAgent.access$402(HotelShopListContentAgent.this, true);
            HotelShopListContentAgent.this.handler.sendEmptyMessage(3);
          }

          public void onAnimationRepeat(Animation paramAnimation)
          {
          }

          public void onAnimationStart(Animation paramAnimation)
          {
          }
        });
        HotelShopListContentAgent.this.find.setAnimation(paramMessage);
        return;
      case 3:
      }
      paramMessage = (ImageView)HotelShopListContentAgent.this.compass.findViewById(R.id.indicator);
      RotateAnimation localRotateAnimation1 = new RotateAnimation(45.0F, 65.0F, 1, 0.5F, 1, 0.5F);
      localRotateAnimation1.setDuration(600L);
      localRotateAnimation1.setFillAfter(true);
      RotateAnimation localRotateAnimation2 = new RotateAnimation(65.0F, 35.0F, 1, 0.5F, 1, 0.5F);
      localRotateAnimation2.setDuration(900L);
      localRotateAnimation2.setFillAfter(true);
      RotateAnimation localRotateAnimation3 = new RotateAnimation(35.0F, 45.0F, 1, 0.5F, 1, 0.5F);
      localRotateAnimation3.setDuration(300L);
      localRotateAnimation3.setFillAfter(true);
      RotateAnimation localRotateAnimation4 = new RotateAnimation(45.0F, 45.0F, 1, 0.5F, 1, 0.5F);
      localRotateAnimation4.setDuration(3000L);
      localRotateAnimation4.setFillAfter(true);
      localRotateAnimation1.setAnimationListener(new Animation.AnimationListener(paramMessage, localRotateAnimation2)
      {
        public void onAnimationEnd(Animation paramAnimation)
        {
          this.val$indicator.clearAnimation();
          this.val$indicator.startAnimation(this.val$rotateAnimation2);
        }

        public void onAnimationRepeat(Animation paramAnimation)
        {
        }

        public void onAnimationStart(Animation paramAnimation)
        {
        }
      });
      localRotateAnimation2.setAnimationListener(new Animation.AnimationListener(paramMessage, localRotateAnimation3)
      {
        public void onAnimationEnd(Animation paramAnimation)
        {
          this.val$indicator.clearAnimation();
          this.val$indicator.startAnimation(this.val$rotateAnimation3);
        }

        public void onAnimationRepeat(Animation paramAnimation)
        {
        }

        public void onAnimationStart(Animation paramAnimation)
        {
        }
      });
      localRotateAnimation3.setAnimationListener(new Animation.AnimationListener(paramMessage, localRotateAnimation4)
      {
        public void onAnimationEnd(Animation paramAnimation)
        {
          this.val$indicator.clearAnimation();
          this.val$indicator.startAnimation(this.val$rotateAnimation4);
        }

        public void onAnimationRepeat(Animation paramAnimation)
        {
        }

        public void onAnimationStart(Animation paramAnimation)
        {
        }
      });
      localRotateAnimation4.setAnimationListener(new Animation.AnimationListener(paramMessage, localRotateAnimation1)
      {
        public void onAnimationEnd(Animation paramAnimation)
        {
          this.val$indicator.clearAnimation();
          this.val$indicator.startAnimation(this.val$rotateAnimation1);
        }

        public void onAnimationRepeat(Animation paramAnimation)
        {
        }

        public void onAnimationStart(Animation paramAnimation)
        {
        }
      });
      paramMessage.startAnimation(localRotateAnimation1);
    }
  };
  private int type;

  public HotelShopListContentAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected void addCell()
  {
    if (Build.VERSION.SDK_INT > 10)
    {
      ((ViewGroup)this.frameLayout.findViewById(R.id.content)).addView(this.shopListView);
      this.frameLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1.0F));
      addCell("050ShopList", this.frameLayout);
      return;
    }
    ((ViewGroup)this.frameLayout.findViewById(R.id.content)).addView(this.shopListView);
    this.frameLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1.0F));
    addCell("050ShopList", this.frameLayout);
  }

  public PullToRefreshListView getListView()
  {
    return this.shopListView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    Object localObject1;
    if ((getDataSource() != null) && (getDataSource().hotelTabIndex == 2))
    {
      localObject1 = getDataSource().selectedListUrl;
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        this.frameLayout.findViewById(R.id.web).setVisibility(0);
        paramBundle = (WebView)this.frameLayout.findViewById(R.id.web);
        paramBundle.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        paramBundle.setScrollBarStyle(0);
        paramBundle.getSettings().setJavaScriptEnabled(true);
        paramBundle.getSettings().setUseWideViewPort(true);
        paramBundle.getSettings().setLoadWithOverviewMode(true);
        paramBundle.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        paramBundle.loadUrl((String)localObject1);
        paramBundle.setWebViewClient(new WebViewClient()
        {
          public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
          {
            return super.shouldOverrideUrlLoading(paramWebView, paramString);
          }
        });
        return;
      }
      getDataSource().hotelTabIndex = 1;
      this.frameLayout.findViewById(R.id.web).setVisibility(4);
      Object localObject2 = getDataSource().moreHotelsEntrance;
      localObject1 = (TextView)this.frameLayout.findViewById(R.id.name);
      if ((localObject2 == null) || (getDataSource().hotelTabIndex != 0))
        break label546;
      GAUserInfo localGAUserInfo = new GAUserInfo();
      localGAUserInfo.query_id = getDataSource().queryId();
      this.find.setVisibility(0);
      this.find.setGAString("hotellist_discovery", localGAUserInfo);
      this.find.setOnClickListener(new View.OnClickListener((DPObject)localObject2)
      {
        public void onClick(View paramView)
        {
          Object localObject = this.val$finalMoreHotelsEntrance.getString("ID");
          if (!TextUtils.isEmpty((CharSequence)localObject))
          {
            if (!((String)localObject).startsWith("http"))
              break label75;
            paramView = Uri.parse("dianping://hotelbookingweb");
            localObject = Uri.parse((String)localObject);
            paramView = new Intent("android.intent.action.VIEW", paramView.buildUpon().appendQueryParameter("url", ((Uri)localObject).toString()).build());
            HotelShopListContentAgent.this.getFragment().startActivity(paramView);
          }
          label75: 
          do
            return;
          while (!((String)localObject).startsWith("dianping"));
          paramView = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
          HotelShopListContentAgent.this.getFragment().startActivity(paramView);
        }
      });
      this.type = ((DPObject)localObject2).getInt("Type");
      localObject2 = (FrameLayout.LayoutParams)this.find.getLayoutParams();
      ((FrameLayout.LayoutParams)localObject2).rightMargin = (ViewUtils.dip2px(getContext(), 75.0F) * -1);
      this.find.setLayoutParams((ViewGroup.LayoutParams)localObject2);
      switch (this.type)
      {
      default:
        label320: this.handler.sendEmptyMessageDelayed(2, 50L);
        GAHelper.instance().contextStatisticsEvent(getContext(), "hotellist_discovery_view", localGAUserInfo, "view");
      case 1:
      case 2:
      }
    }
    while (true)
    {
      super.onAgentChanged(paramBundle);
      paramBundle = new GAUserInfo();
      paramBundle.query_id = getDataSource().queryId();
      paramBundle.title = ("searchAroundCities=" + getDataSource().searchAroundCities);
      GAHelper.instance().contextStatisticsEvent(getActivity(), "hotellist_shop", paramBundle, "view");
      return;
      int i;
      if (getDataSource() != null)
      {
        i = 1;
        label431: if (getDataSource().hotelTabIndex != 0)
          break label486;
      }
      label486: for (int j = 1; ((i & j) != 0) || (getDataSource().hotelTabIndex == 1); j = 0)
      {
        this.frameLayout.findViewById(R.id.web).setVisibility(4);
        break;
        i = 0;
        break label431;
      }
      this.eye.setVisibility(0);
      this.compass.setVisibility(8);
      ((TextView)localObject1).setText("发现");
      break label320;
      this.eye.setVisibility(8);
      this.compass.setVisibility(0);
      ((TextView)localObject1).setText("攻略");
      break label320;
      label546: this.find.setVisibility(8);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    this.frameLayout = ((FrameLayout)inflater().inflate(R.layout.hotel_shoplist_content, (ViewGroup)getFragment().contentView(), false));
    this.find = ((NovaLinearLayout)this.frameLayout.findViewById(R.id.find));
    this.eye = ((ImageView)this.frameLayout.findViewById(R.id.eye));
    this.compass = ((FrameLayout)this.frameLayout.findViewById(R.id.compass));
    super.onCreate(paramBundle);
  }

  public void resetListView()
  {
    if ((Build.VERSION.SDK_INT > 10) && (this.shopListView != null))
      return;
    inflateViews();
    initShopList();
    addCell();
  }

  protected void sendAdClientGA(int paramInt)
  {
    if ((this.shopListView == null) || (getDataSource() == null) || (this.shopListAdapter == null));
    while (true)
    {
      return;
      int j = this.shopListView.getFirstVisiblePosition();
      int k = this.shopListView.getLastVisiblePosition();
      if (paramInt == 1);
      try
      {
        if (getDataSource().startIndex() == 0)
          getDataSource().mGaRecordTables.clear();
        int i = getDataSource().startIndex();
        while (true)
        {
          Object localObject1;
          if (i < getDataSource().shops().size())
          {
            localObject1 = (DPObject)getDataSource().shops().get(i);
            if (localObject1 == null)
              break label422;
          }
          while (paramInt <= k)
          {
            Object localObject2 = this.shopListView.getItemAtPosition(paramInt);
            localObject1 = localObject2;
            if ((localObject2 instanceof ShopDataModel))
              localObject1 = ((ShopDataModel)localObject2).shopObj;
            if ((localObject1 instanceof DPObject))
            {
              localObject1 = (DPObject)localObject1;
              if ((((DPObject)localObject1).getBoolean("IsAdShop")) && (!getDataSource().mGaRecordTables.get(((DPObject)localObject1).getInt("ID"))))
                if (((DPObject)localObject1).getInt("__HotelShopListShopType") == 2)
                {
                  AdClientUtils.sendAdGa((DPObject)localObject1, "3", String.valueOf(((DPObject)localObject1).getInt("__HotelShopListShopLocKey")));
                  ShopListUtils.logAdGa((DPObject)localObject1, "3", String.valueOf(((DPObject)localObject1).getInt("__HotelShopListShopLocKey")));
                  getDataSource().mGaRecordTables.put(((DPObject)localObject1).getInt("ID"), true);
                  break label433;
                  if (!((DPObject)localObject1).getBoolean("IsAdShop"))
                    break label440;
                  if (((DPObject)localObject1).getInt("__HotelShopListShopType") == 2)
                  {
                    AdClientUtils.sendAdGa((DPObject)localObject1, "1", String.valueOf(((DPObject)localObject1).getInt("__HotelShopListShopLocKey")));
                    ShopListUtils.logAdGa((DPObject)localObject1, "1", String.valueOf(((DPObject)localObject1).getInt("__HotelShopListShopLocKey")));
                    break label440;
                  }
                  if (((DPObject)localObject1).getInt("__HotelShopListShopType") != 0)
                    break label440;
                  AdClientUtils.sendAdGa((DPObject)localObject1, "1", String.valueOf(((DPObject)localObject1).getInt("ListPosition") + 1));
                  ShopListUtils.logAdGa((DPObject)localObject1, "1", String.valueOf(((DPObject)localObject1).getInt("ListPosition") + 1));
                  break label440;
                }
                else if (((DPObject)localObject1).getInt("__HotelShopListShopType") == 0)
                {
                  AdClientUtils.sendAdGa((DPObject)localObject1, "3", String.valueOf(((DPObject)localObject1).getInt("ListPosition") + 1));
                  ShopListUtils.logAdGa((DPObject)localObject1, "3", String.valueOf(((DPObject)localObject1).getInt("ListPosition") + 1));
                  getDataSource().mGaRecordTables.put(((DPObject)localObject1).getInt("ID"), true);
                  break label433;
                  label422: if (paramInt != 2)
                    break;
                  paramInt = j;
                  continue;
                }
            }
            label433: paramInt += 1;
          }
          label440: i += 1;
        }
      }
      catch (Exception localException)
      {
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.agent.HotelShopListContentAgent
 * JD-Core Version:    0.6.0
 */
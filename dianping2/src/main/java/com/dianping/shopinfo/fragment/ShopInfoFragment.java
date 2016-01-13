package com.dianping.shopinfo.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.app.DPActivity;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.util.URLTemplate;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.moduleconfig.AgentHelper;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.common.DefaultShopinfoAgentListConfig;
import com.dianping.shopinfo.common.FavoriteAgent;
import com.dianping.shopinfo.common.HistoryAgent;
import com.dianping.shopinfo.common.MoreAgent;
import com.dianping.shopinfo.common.ShareAgent;
import com.dianping.shopinfo.community.CommunityShopinfoAgentListConfig;
import com.dianping.shopinfo.district.config.BusinessDistrictAgentListConfig;
import com.dianping.shopinfo.fun.config.FunShopInfoAgentListConfig;
import com.dianping.shopinfo.massage.config.MassageShopInfoAgentListConfig;
import com.dianping.shopinfo.pet.PetShopinfoAgentListConfig;
import com.dianping.shopinfo.utils.AgentMap;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.MyScrollView;
import com.dianping.widget.MyScrollView.OnScrollListener;
import com.dianping.widget.view.NovaImageView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map<Ljava.lang.String;Lcom.dianping.base.app.loader.AgentInfo;>;
import org.json.JSONObject;

public class ShopInfoFragment extends GroupAgentFragment
  implements FullRequestHandle<MApiRequest, MApiResponse>
{
  private static final String MESSAGE_SHOP_READY = "com.dianping.v1.shopready";
  private static final String SHOP_HOST = "shopinfo/";
  private static final String SHOP_VIEW_PREFIX = "shop_";
  String action;
  public ViewGroup contentView;
  TextView cooperationInfo;
  public String exSearchResultShopView;
  ViewGroup mEmptyContainer;
  public View mFragmentView;
  View mLoadingView;
  ViewGroup mShopContainer;
  private ScrollView shoinfoScrollView;
  public DPObject shop;
  public String shopExtraParam;
  public int shopId;
  public MApiRequest shopRequest;
  public boolean shopRetrieved;
  private ViewGroup titleBar;
  public ViewGroup toolbarView;
  ShopCellAgent topAgent;
  private int topAgentMargin = 0;
  ViewGroup topContainer;

  private String formatNumber(int paramInt)
  {
    return String.format("%05d", new Object[] { Integer.valueOf(paramInt) });
  }

  private Map<String, AgentInfo> getShopinfoAgentList()
  {
    Object localObject1 = "common_default";
    if (this.shop != null)
    {
      Object localObject2 = this.shop.getObject("ClientShopStyle");
      if (localObject2 != null)
      {
        localObject2 = ((DPObject)localObject2).getString("ShopView");
        if (!android.text.TextUtils.isEmpty((CharSequence)localObject2))
          localObject1 = localObject2;
        Log.d("ShopConfig", "get shopView = " + (String)localObject1);
        localObject2 = null;
        List localList = AgentHelper.getInstance().getAgentList(getActivity(), "shop_" + (String)localObject1);
        localObject1 = localObject2;
        if (localList == null)
          break label412;
        localObject1 = localObject2;
        if (localList.size() <= 0)
          break label412;
        localObject2 = new HashMap();
        int j = 0;
        int k = 0;
        int i;
        if (localList != null)
          i = localList.size();
        while (true)
        {
          localObject1 = localObject2;
          if (k >= i)
            break;
          localObject1 = (ArrayList)localList.get(k);
          int i1 = 0;
          int n = 0;
          int m;
          label176: String str;
          if (localObject1 != null)
          {
            m = ((ArrayList)localObject1).size();
            if (n >= m)
              break label394;
            str = (String)((ArrayList)localObject1).get(n);
            if ((android.text.TextUtils.isEmpty(str)) || (AgentMap.getAgentClass(str) == null))
              break label366;
            Log.i("ShopConfig", "find class = " + AgentMap.getAgentClass(str) + ", it's index = " + formatNumber(j) + "." + formatNumber(i1));
            ((Map)localObject2).put("shopinfo/" + str, new AgentInfo(AgentMap.getAgentClass(str), formatNumber(j) + "." + formatNumber(i1)));
            i1 += 10;
          }
          while (true)
          {
            n += 1;
            break label176;
            i = 0;
            break;
            m = 0;
            break label176;
            label366: Log.i("ShopConfig", "couldn't find class = " + str);
          }
          label394: j += 10;
          k += 1;
        }
      }
    }
    localObject1 = null;
    label412: return (Map<String, AgentInfo>)(Map<String, AgentInfo>)localObject1;
  }

  private void gotoAction(String paramString)
    throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    if ("addreview".equals(paramString))
    {
      paramString = (CellAgent)this.agents.get("shopinfo/common_review");
      paramString.getClass().getDeclaredMethod("addReview", new Class[0]).invoke(paramString, new Object[0]);
    }
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    if ((this.shop == null) || (this.shop.getObject("ClientShopStyle") == null) || (android.text.TextUtils.isEmpty(this.shop.getObject("ClientShopStyle").getString("ShopView"))))
      return null;
    ArrayList localArrayList = new ArrayList();
    Map localMap = getShopinfoAgentList();
    if (localMap != null)
    {
      localArrayList.add(new AgentListConfig(localMap)
      {
        public Map<String, AgentInfo> getAgentInfoList()
        {
          this.val$agentInfoHashMap.put("shopinfo/default_share", new AgentInfo(ShareAgent.class, ""));
          this.val$agentInfoHashMap.put("shopinfo/default_history", new AgentInfo(HistoryAgent.class, ""));
          this.val$agentInfoHashMap.put("shopinfo/default_report", new AgentInfo(MoreAgent.class, ""));
          this.val$agentInfoHashMap.put("shopinfo/default_favorite", new AgentInfo(FavoriteAgent.class, ""));
          return this.val$agentInfoHashMap;
        }

        public Map<String, Class<? extends CellAgent>> getAgentList()
        {
          return null;
        }

        public boolean shouldShow()
        {
          return true;
        }
      });
      return localArrayList;
    }
    localArrayList.add(new FunShopInfoAgentListConfig(this.shop));
    localArrayList.add(new MassageShopInfoAgentListConfig(this.shop));
    localArrayList.add(new BusinessDistrictAgentListConfig(this.shop));
    localArrayList.add(new CommunityShopinfoAgentListConfig(this.shop));
    localArrayList.add(new PetShopinfoAgentListConfig(this.shop));
    localArrayList.add(new DefaultShopinfoAgentListConfig());
    return localArrayList;
  }

  protected View getLoadingView()
  {
    return getActivity().getLayoutInflater().inflate(R.layout.loading_item_fullscreen, null);
  }

  public ScrollView getScrollView()
  {
    return this.shoinfoScrollView;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    if (!this.shopRetrieved)
      sendShopRequest();
    super.onActivityCreated(paramBundle);
    ((DPActivity)getActivity()).gaExtra.shop_id = Integer.valueOf(this.shopId);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.shopId = getIntParam("id");
    this.shopExtraParam = getStringParam("shopextraparam");
    this.exSearchResultShopView = getStringParam("shopview");
    this.action = getStringParam("action");
    if (this.shopId <= 0)
      this.shopId = getIntParam("shopid");
    String str = getStringParam("_fb_");
    if (!com.dianping.util.TextUtils.isEmpty(str))
    {
      HashMap localHashMap = new HashMap();
      localHashMap.put("act", String.valueOf(4));
      AdClientUtils.report(str, localHashMap);
    }
    if (paramBundle == null)
    {
      this.shop = getObjectParam("shop");
      this.shopRetrieved = false;
    }
    while (true)
    {
      if (this.shop != null)
        paramBundle = this.shop.getString("ShopStyle");
      try
      {
        paramBundle = new JSONObject(paramBundle);
        paramBundle = new DPObject().edit().putString("ShopView", paramBundle.getString("shopView")).putString("PicMode", paramBundle.getString("picMode")).generate();
        this.shop = this.shop.edit().putObject("ClientShopStyle", paramBundle).generate();
        return;
        this.shop = ((DPObject)paramBundle.getParcelable("shop"));
        this.shopRetrieved = paramBundle.getBoolean("shopRetrieved");
      }
      catch (Exception paramBundle)
      {
      }
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mFragmentView = paramLayoutInflater.inflate(R.layout.shop_info, null, false);
    this.contentView = ((ViewGroup)this.mFragmentView.findViewById(16908290));
    this.cooperationInfo = ((TextView)this.mFragmentView.findViewById(R.id.cooperation));
    this.mEmptyContainer = ((ViewGroup)this.mFragmentView.findViewById(R.id.empty));
    this.mShopContainer = ((ViewGroup)this.mFragmentView.findViewById(R.id.content_shop));
    this.shoinfoScrollView = ((MyScrollView)this.mFragmentView.findViewById(R.id.shopinfo_scrollview));
    this.toolbarView = ((ViewGroup)this.mFragmentView.findViewById(R.id.tabs));
    this.titleBar = ((ViewGroup)this.mFragmentView.findViewById(R.id.titlebar));
    this.titleBar.findViewById(R.id.left_view).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ShopInfoFragment.this.getActivity().finish();
      }
    });
    paramLayoutInflater = (FrameLayout)this.mFragmentView.findViewById(R.id.root);
    ((MyScrollView)this.shoinfoScrollView).setOnScrollListener(new MyScrollView.OnScrollListener()
    {
      public void onScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
      {
        Object localObject;
        if ((ShopInfoFragment.this.topAgent != null) && (ShopInfoFragment.this.topAgent.getView() != null))
        {
          localObject = ShopInfoFragment.this.topAgent.getView();
          if (localObject != null)
            break label41;
        }
        label41: 
        do
        {
          return;
          localObject = ((View)localObject).getParent();
        }
        while (localObject == null);
        if (((ViewGroup)localObject).getTop() + ShopInfoFragment.this.topAgentMargin <= paramInt2)
        {
          ShopInfoFragment.this.topContainer.setVisibility(0);
          return;
        }
        ShopInfoFragment.this.topContainer.setVisibility(8);
      }
    });
    this.topContainer = new FrameLayout(getActivity());
    paramViewGroup = new FrameLayout.LayoutParams(-1, -2, 48);
    paramViewGroup.topMargin = ViewUtils.dip2px(getActivity(), 50.0F);
    this.topContainer.setLayoutParams(paramViewGroup);
    this.topContainer.setVisibility(4);
    paramLayoutInflater.addView(this.topContainer);
    setAgentContainerView(this.contentView);
    return this.mFragmentView;
  }

  public void onDestroy()
  {
    if (this.shopRequest != null)
    {
      mapiService().abort(this.shopRequest, this, true);
      this.shopRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopRequest)
    {
      showContent();
      this.shopRetrieved = false;
      this.shopRequest = null;
      resetAgents(null);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopRequest)
    {
      showContent();
      this.shop = ((DPObject)paramMApiResponse.result());
      this.shopRetrieved = true;
      this.shopRequest = null;
      if (this.shop != null)
        paramMApiRequest = this.shop.getString("ShopStyle");
    }
    try
    {
      paramMApiRequest = new JSONObject(paramMApiRequest);
      paramMApiRequest = new DPObject().edit().putString("ShopView", paramMApiRequest.getString("shopView")).putString("PicMode", paramMApiRequest.getString("picMode")).generate();
      this.shop = this.shop.edit().putObject("ClientShopStyle", paramMApiRequest).generate();
      paramMApiRequest = new Intent("com.dianping.v1.shopready");
      paramMApiRequest.putExtra("shop", this.shop);
      LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(paramMApiRequest);
      resetAgents(null);
      paramMApiRequest = this.shop.getString("CooperationInfo");
      if (!android.text.TextUtils.isEmpty(paramMApiRequest))
      {
        this.cooperationInfo.setVisibility(0);
        this.cooperationInfo.setText(paramMApiRequest);
        if (android.text.TextUtils.isEmpty(this.action));
      }
    }
    catch (Exception paramMApiRequest)
    {
      try
      {
        while (true)
        {
          gotoAction(this.action);
          return;
          paramMApiRequest = paramMApiRequest;
          paramMApiRequest.printStackTrace();
        }
        this.cooperationInfo.setVisibility(8);
      }
      catch (Exception paramMApiRequest)
      {
      }
    }
  }

  public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
  {
  }

  public void onRequestStart(MApiRequest paramMApiRequest)
  {
    if ((paramMApiRequest == this.shopRequest) && ((this.shop == null) || (this.shop.getObject("ClientShopStyle") == null) || (android.text.TextUtils.isEmpty(this.shop.getObject("ClientShopStyle").getString("ShopView")))))
      showLoading("");
  }

  public void removeTopView()
  {
    this.topContainer.removeAllViews();
    this.topContainer.setVisibility(8);
  }

  void sendShopRequest()
  {
    StringBuilder localStringBuilder = new StringBuilder(new URLTemplate("http://m.api.dianping.com/shop.bin?shopid=<id|shopid:int>&promoid=[promoid:int]&extra=[extra]").generate((DPActivity)getActivity(), true));
    Object localObject = locationService().location();
    if (localObject != null)
    {
      localStringBuilder.append("&lat=").append(Location.FMT.format(((DPObject)localObject).getDouble("Lat")));
      localStringBuilder.append("&lng=").append(Location.FMT.format(((DPObject)localObject).getDouble("Lng")));
    }
    localObject = Environment.uuid();
    if (localObject != null)
      localStringBuilder.append("&clientuuid=").append((String)localObject);
    this.shopRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.shopRequest, this);
  }

  public void setHuiLayerVisibility(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      FrameLayout localFrameLayout = (FrameLayout)LayoutInflater.from(getActivity()).inflate(R.layout.shopinfo_hui_layer, null, false);
      ((FrameLayout)this.mFragmentView.findViewById(R.id.root)).addView(localFrameLayout);
      Button localButton = (Button)localFrameLayout.findViewById(R.id.kown_button);
      localFrameLayout.setClickable(true);
      localButton.setOnClickListener(new View.OnClickListener(localFrameLayout)
      {
        public void onClick(View paramView)
        {
          ((FrameLayout)ShopInfoFragment.this.mFragmentView.findViewById(R.id.root)).removeView(this.val$mHuiLayer);
        }
      });
    }
  }

  public void setKtvLayerVisibility(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      FrameLayout localFrameLayout = (FrameLayout)LayoutInflater.from(getActivity()).inflate(R.layout.shopinfo_ktv_layer, null, false);
      ((FrameLayout)this.mFragmentView.findViewById(R.id.root)).addView(localFrameLayout);
      ImageView localImageView = (ImageView)localFrameLayout.findViewById(R.id.know_ktv);
      localFrameLayout.setClickable(true);
      localImageView.setOnClickListener(new View.OnClickListener(localFrameLayout)
      {
        public void onClick(View paramView)
        {
          ((FrameLayout)ShopInfoFragment.this.mFragmentView.findViewById(R.id.root)).removeView(this.val$mKtvLayer);
        }
      });
    }
  }

  public View setTitleRightButton(String paramString, int paramInt, View.OnClickListener paramOnClickListener)
  {
    if (paramInt == R.drawable.ic_action_share)
    {
      paramInt = R.id.share;
      this.mFragmentView.findViewById(paramInt).setVisibility(0);
    }
    while (true)
    {
      this.mFragmentView.findViewById(paramInt).setOnClickListener(paramOnClickListener);
      this.mFragmentView.findViewById(paramInt).setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
        {
          if (paramMotionEvent.getAction() == 0)
            ((NovaImageView)paramView).setAlpha(123);
          if ((paramMotionEvent.getAction() == 1) || (paramMotionEvent.getAction() == 3))
            ((NovaImageView)paramView).setAlpha(255);
          return false;
        }
      });
      return this.mFragmentView.findViewById(paramInt);
      if (paramInt == R.drawable.detail_topbar_icon_more)
      {
        paramInt = R.id.more;
        this.mFragmentView.findViewById(R.id.more).setVisibility(0);
        continue;
      }
      if ((paramInt != R.drawable.ic_action_favorite_off_normal) && (paramInt != R.drawable.ic_action_favorite_on_normal))
        break;
      int i = R.id.favorite;
      this.mFragmentView.findViewById(R.id.favorite).setVisibility(0);
      ((NovaImageView)this.mFragmentView.findViewById(R.id.favorite)).setImageResource(paramInt);
      paramInt = i;
    }
    return null;
  }

  public void setTopAgentMarginTop(int paramInt)
  {
    this.topAgentMargin = paramInt;
  }

  public void setTopView(View paramView, ShopCellAgent paramShopCellAgent)
  {
    this.topContainer.removeAllViews();
    this.topContainer.addView(paramView);
    this.topContainer.setVisibility(8);
    this.topContainer.setBackgroundColor(getResources().getColor(R.color.white));
    this.topAgent = paramShopCellAgent;
    paramShopCellAgent.getView();
  }

  protected void showContent()
  {
    this.mEmptyContainer.setVisibility(8);
    this.mShopContainer.setVisibility(0);
  }

  protected void showLoading(String paramString)
  {
    this.mShopContainer.setVisibility(8);
    if (this.mLoadingView == null)
      this.mLoadingView = getLoadingView();
    if (!android.text.TextUtils.isEmpty(paramString))
    {
      TextView localTextView = (TextView)this.mLoadingView.findViewById(16908308);
      if (localTextView != null)
        localTextView.setText(paramString);
    }
    if (this.mEmptyContainer.getChildAt(0) != this.mLoadingView)
    {
      this.mEmptyContainer.removeAllViews();
      this.mEmptyContainer.addView(this.mLoadingView);
    }
    this.mEmptyContainer.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.fragment.ShopInfoFragment
 * JD-Core Version:    0.6.0
 */
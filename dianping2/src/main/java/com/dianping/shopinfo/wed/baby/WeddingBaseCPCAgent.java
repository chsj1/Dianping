package com.dianping.shopinfo.wed.baby;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.app.CityConfig;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.base.widget.ShopPower;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.UserProfile;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.HashMap;
import java.util.Map;

public class WeddingBaseCPCAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String API_URL = "http://m.api.dianping.com/midas/cpmAppAds.bin";
  static final int GA_ACT_CLICK = 2;
  static final int GA_ACT_LOAD = 1;
  static final int GA_ACT_SHOW = 3;
  private static final int SLOT_ID = 11004;
  static final String TAG = WeddingCoCPCAgent.class.getSimpleName();
  protected int cooperateType = 1;
  private DPObject error;
  MeasuredGridView gridView;
  private ListAdapter listViewAdapter;
  private MApiRequest request;
  private boolean requestSent = false;
  private int screenHeight;
  private int shopId;
  protected DPObject[] shopList;
  private int shopType;

  public WeddingBaseCPCAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void recordCPMGA(int paramInt1, int paramInt2, int paramInt3, String paramString, int paramInt4)
  {
    Object localObject2 = DeviceUtils.mac();
    Object localObject1 = localObject2;
    if (TextUtils.isEmpty((CharSequence)localObject2))
      localObject1 = Environment.imei();
    try
    {
      localObject2 = new HashMap();
      ((HashMap)localObject2).put("ad", String.valueOf(paramInt1));
      ((HashMap)localObject2).put("act", String.valueOf(paramInt2));
      ((HashMap)localObject2).put("slot", String.valueOf(11004));
      ((HashMap)localObject2).put("user_agent", Environment.mapiUserAgent());
      ((HashMap)localObject2).put("guid_str", localObject1);
      ((HashMap)localObject2).put("client_version", Environment.versionName());
      ((HashMap)localObject2).put("shop_id", String.valueOf(this.shopId));
      ((HashMap)localObject2).put("page_city_id", String.valueOf(getCity().id()));
      ((HashMap)localObject2).put("device_id", localObject1);
      ((HashMap)localObject2).put("host", "m.api.dianping.com");
      ((HashMap)localObject2).put("dpid", DeviceUtils.dpid());
      ((HashMap)localObject2).put("adshop_id", String.valueOf(paramInt3));
      ((HashMap)localObject2).put("adidx", String.valueOf(paramInt4));
      if (getShop() != null)
      {
        ((HashMap)localObject2).put("channel_id", String.valueOf(getShop().getInt("ShopType")));
        ((HashMap)localObject2).put("category_id", String.valueOf(getShop().getInt("CategoryID")));
      }
      if (getAccount() != null)
        ((HashMap)localObject2).put("user_id", String.valueOf(getAccount().id()));
      if (location() != null)
      {
        ((HashMap)localObject2).put("lat", String.valueOf(location().latitude()));
        ((HashMap)localObject2).put("lng", String.valueOf(location().longitude()));
      }
      if (!TextUtils.isEmpty(DeviceUtils.token()))
        ((HashMap)localObject2).put("token", DeviceUtils.token());
      ((HashMap)localObject2).put("dpreqid", GAHelper.requestId);
      AdClientUtils.report(paramString, (Map)localObject2);
      return;
    }
    catch (java.lang.Exception paramString)
    {
      Log.e(TAG, "Exception when CPM GA", paramString);
    }
  }

  private void recordCPMGAByList(DPObject[] paramArrayOfDPObject)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0));
    while (true)
    {
      return;
      int i = 0;
      while (i < paramArrayOfDPObject.length)
      {
        DPObject localDPObject = paramArrayOfDPObject[i];
        if (localDPObject != null)
          recordCPMGA(localDPObject.getInt("LaunchID"), 1, localDPObject.getInt("ShopID"), localDPObject.getString("Feedback"), i + 1);
        i += 1;
      }
    }
  }

  protected View createSuggestionView()
  {
    View localView = this.res.inflate(getContext(), R.layout.shop_wedding_suggestion, getParentView(), false);
    if (this.cooperateType == 2);
    for (this.shopList = getSubArray(this.shopList, 4); ; this.shopList = getSubArray(this.shopList, 8))
    {
      if (this.listViewAdapter == null)
        this.listViewAdapter = new ListAdapter();
      this.gridView = ((MeasuredGridView)localView.findViewById(R.id.suggest_shop_grid));
      this.gridView.setAdapter(this.listViewAdapter);
      getFragment().getScrollView().setOnTouchListener(new View.OnTouchListener()
      {
        int[] arr = new int[this.maxCount];
        int maxCount = 20;
        int shownRegion = WeddingBaseCPCAgent.this.screenHeight - 80;

        public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
        {
          int i = WeddingBaseCPCAgent.this.gridView.getCount();
          if ((i == 0) || (i > this.maxCount));
          while (true)
          {
            return false;
            i = 0;
            while (i < WeddingBaseCPCAgent.this.gridView.getCount())
            {
              paramView = new int[2];
              if (WeddingBaseCPCAgent.this.gridView.getChildAt(i) != null)
              {
                WeddingBaseCPCAgent.this.gridView.getChildAt(i).getLocationOnScreen(paramView);
                if ((paramView[1] > 0) && (paramView[1] < this.shownRegion) && (this.arr[i] == 0) && (WeddingBaseCPCAgent.this.shopList != null) && (WeddingBaseCPCAgent.this.shopList.length >= i) && (WeddingBaseCPCAgent.this.shopList[i] != null))
                {
                  this.arr[i] = 1;
                  paramView = WeddingBaseCPCAgent.this.shopList[i];
                  int j = paramView.getInt("LaunchID");
                  int k = paramView.getInt("ShopID");
                  paramView = paramView.getString("Feedback");
                  WeddingBaseCPCAgent.this.recordCPMGA(j, 3, k, paramView, i + 1);
                }
              }
              i += 1;
            }
          }
        }
      });
      return localView;
    }
  }

  protected DPObject[] getSubArray(DPObject[] paramArrayOfDPObject, int paramInt)
  {
    Object localObject;
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length <= paramInt))
    {
      localObject = paramArrayOfDPObject;
      return localObject;
    }
    DPObject[] arrayOfDPObject = new DPObject[paramInt];
    int i = 0;
    while (true)
    {
      localObject = arrayOfDPObject;
      if (i >= paramInt)
        break;
      arrayOfDPObject[i] = paramArrayOfDPObject[i];
      i += 1;
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((paramBundle != null) && (paramBundle.containsKey("shop")))
      this.cooperateType = ((DPObject)paramBundle.getParcelable("shop")).getInt("CooperateType");
    if ((this.shopList != null) || (this.error != null));
    do
    {
      do
      {
        return;
        paramBundle = getShop();
      }
      while (paramBundle == null);
      this.shopId = paramBundle.getInt("ID");
    }
    while ((this.shopId <= 0) || (this.requestSent));
    this.requestSent = true;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/midas/cpmAppAds.bin").buildUpon();
    localBuilder.appendQueryParameter("cityId", String.valueOf(DPApplication.instance().cityConfig().currentCity().id())).appendQueryParameter("viewShopId", String.valueOf(this.shopId)).appendQueryParameter("shopType", String.valueOf(this.shopType)).appendQueryParameter("slotId", String.valueOf(11004)).appendQueryParameter("dpId", String.valueOf(DeviceUtils.dpid())).appendQueryParameter("categoryId", String.valueOf(paramBundle.getInt("CategoryID"))).appendQueryParameter("categoryName", getShop().getString("CategoryName"));
    if (!TextUtils.isEmpty(DeviceUtils.token()))
      localBuilder.appendQueryParameter("token", DeviceUtils.token());
    if (location() != null)
    {
      double d1 = location().latitude();
      double d2 = location().longitude();
      if ((d1 > 0.0D) && (d2 > 0.0D))
        localBuilder.appendQueryParameter("latitude", String.valueOf(d1)).appendQueryParameter("longitude", String.valueOf(d2));
    }
    this.request = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getShop();
    if (paramBundle == null)
    {
      Log.e(TAG, "Null shop data. Can not update shop info.");
      return;
    }
    this.shopId = paramBundle.getInt("ID");
    if (this.shopId <= 0)
    {
      Log.e(TAG, "Invalid shop id. Can not update shop info.");
      return;
    }
    this.shopType = paramBundle.getInt("ShopType");
    this.screenHeight = ViewUtils.getScreenHeightPixels(getContext());
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((this.request != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.request, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request != paramMApiRequest)
      return;
    this.request = null;
    if ((paramMApiResponse.error() instanceof DPObject));
    for (this.error = ((DPObject)paramMApiResponse.error()); ; this.error = new DPObject())
    {
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request != paramMApiRequest);
    do
    {
      return;
      this.request = null;
      this.error = null;
      this.shopList = ((DPObject)paramMApiResponse.result()).getArray("CpmAdList");
    }
    while ((this.shopList == null) || (this.shopList.length == 0));
    dispatchAgentChanged("shopinfo/wed_cpc", null);
    dispatchAgentChanged("shopinfo/wed_unco_cpc", null);
    recordCPMGAByList(this.shopList);
  }

  public class ListAdapter extends BasicAdapter
  {
    public ListAdapter()
    {
    }

    private void setMarkText(DPObject paramDPObject, NovaLinearLayout paramNovaLinearLayout)
    {
      paramNovaLinearLayout = (TextView)paramNovaLinearLayout.findViewById(R.id.mark_text);
      if (!TextUtils.isEmpty(paramDPObject.getString("Mark")))
      {
        paramNovaLinearLayout.setVisibility(0);
        paramNovaLinearLayout.setText(paramDPObject.getString("Mark"));
      }
    }

    private void setShopItemView(DPObject paramDPObject, NovaLinearLayout paramNovaLinearLayout, int paramInt)
    {
      Object localObject1 = (NetworkImageView)paramNovaLinearLayout.findViewById(R.id.shop_image);
      ShopPower localShopPower = (ShopPower)paramNovaLinearLayout.findViewById(R.id.shop_power);
      TextView localTextView1 = (TextView)paramNovaLinearLayout.findViewById(R.id.desc);
      TextView localTextView2 = (TextView)paramNovaLinearLayout.findViewById(R.id.shop_name);
      TextView localTextView3 = (TextView)paramNovaLinearLayout.findViewById(R.id.region);
      TextView localTextView4 = (TextView)paramNovaLinearLayout.findViewById(R.id.distance);
      ((NetworkImageView)localObject1).setImage(paramDPObject.getString("ImgUrl"));
      int i = (int)((WeddingBaseCPCAgent.this.getContext().getResources().getDisplayMetrics().widthPixels - 40) / 2 * 0.75D);
      localObject1 = (FrameLayout)paramNovaLinearLayout.findViewById(R.id.shop_frame);
      Object localObject2 = ((FrameLayout)localObject1).getLayoutParams();
      ((ViewGroup.LayoutParams)localObject2).height = i;
      ((FrameLayout)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      setMarkText(paramDPObject, paramNovaLinearLayout);
      localObject1 = paramDPObject.getString("BranchName");
      localObject2 = new StringBuilder().append(paramDPObject.getString("ShopName"));
      if ((localObject1 == null) || (((String)localObject1).length() == 0))
      {
        localObject1 = "";
        localTextView2.setText((String)localObject1);
        if (!TextUtils.isEmpty(paramDPObject.getString("RegionName")))
          break label384;
        localTextView3.setVisibility(8);
        label228: if (!TextUtils.isEmpty(paramDPObject.getString("SecondCategory")))
          break label398;
        localTextView1.setVisibility(8);
      }
      while (true)
      {
        localTextView4.setText(paramDPObject.getString("DistanceStr"));
        localShopPower.setPower(paramDPObject.getInt("Star"));
        i = paramDPObject.getInt("LaunchID");
        int j = paramDPObject.getInt("ShopID");
        paramDPObject = paramDPObject.getString("Feedback");
        localObject1 = WeddingBaseCPCAgent.this.getGAExtra();
        ((GAUserInfo)localObject1).index = Integer.valueOf(paramInt + 1);
        ((GAUserInfo)localObject1).shop_id = Integer.valueOf(WeddingBaseCPCAgent.this.shopId());
        paramNovaLinearLayout.setGAString("cpc", (GAUserInfo)localObject1);
        paramNovaLinearLayout.setOnClickListener(new View.OnClickListener(j, i, paramDPObject, paramInt)
        {
          public void onClick(View paramView)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + this.val$shopAdId));
            WeddingBaseCPCAgent.this.getFragment().startActivity(paramView);
            WeddingBaseCPCAgent.this.recordCPMGA(this.val$adId, 2, this.val$shopAdId, this.val$feedback, this.val$position + 1);
          }
        });
        return;
        localObject1 = "(" + (String)localObject1 + ")";
        break;
        label384: localTextView3.setText(paramDPObject.getString("RegionName"));
        break label228;
        label398: localTextView1.setText(paramDPObject.getString("SecondCategory"));
      }
    }

    public int getCount()
    {
      if (WeddingBaseCPCAgent.this.shopList == null)
        return 0;
      return WeddingBaseCPCAgent.this.shopList.length;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < WeddingBaseCPCAgent.this.shopList.length)
        return WeddingBaseCPCAgent.this.shopList[paramInt];
      return null;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        localObject = (DPObject)localObject;
        if (!(paramView instanceof ShopListItem));
        for (paramView = (NovaLinearLayout)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shop_wedding_suggestion_item, paramViewGroup, false); ; paramView = (NovaLinearLayout)paramView)
        {
          setShopItemView((DPObject)localObject, paramView, paramInt);
          return paramView;
        }
      }
      if (localObject == ERROR)
        Log.e(WeddingBaseCPCAgent.TAG, "ERROR IN getView");
      while (true)
      {
        return null;
        if (localObject != LOADING)
          continue;
        Log.e(WeddingBaseCPCAgent.TAG, "LOADING in getView");
        getLoadingView(paramViewGroup, paramView);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingBaseCPCAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.car;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
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
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.List;

public class ServiceListAgent extends ShopCellAgent
{
  private static final String API_URL = "http://m.api.dianping.com/car/service/fetch/infolist.car";
  private static final String CELL_CAR_SERVICE = "0600Service.10ServiceList";
  private static final String RMB = "￥";
  private static final String TAG = ServiceListAgent.class.getSimpleName();
  protected int categoryId;
  protected ViewGroup itemListContainerView;
  protected MApiRequest mRequest;
  private RequestHandler<MApiRequest, MApiResponse> mRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      ServiceListAgent.this.mRequest = null;
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (ServiceListAgent.this.mRequest != paramMApiRequest);
      do
      {
        return;
        ServiceListAgent.this.serviceObject = ((DPObject)paramMApiResponse.result());
      }
      while ((ServiceListAgent.this.serviceObject == null) || (ServiceListAgent.this.serviceObject.getArray("List") == null));
      ServiceListAgent.this.serviceDataList = ServiceListAgent.this.convertResult(ServiceListAgent.this.serviceObject.getArray("List"));
      ServiceListAgent.this.dispatchAgentChanged(false);
    }
  };
  private View.OnClickListener onExpandClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      ServiceListAgent.this.expand(paramView);
    }
  };
  private View.OnClickListener onServiceItemClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      Object localObject = paramView.getTag();
      if (localObject == null);
      do
        return;
      while (!(localObject instanceof ServiceListAgent.ServiceInfoData));
      paramView = (ServiceListAgent.ServiceInfoData)paramView.getTag();
      ServiceListAgent.this.goPay(paramView);
      ServiceListAgent.this.statisticsManually("shopinfo_car_service_item", "tap", "" + paramView.serviceType);
    }
  };
  protected List<ServiceGroupData> serviceDataList;
  protected DPObject serviceObject;
  protected View serviceView;
  protected int shopId;

  public ServiceListAgent(Object paramObject)
  {
    super(paramObject);
  }

  private List<ServiceGroupData> convertResult(DPObject[] paramArrayOfDPObject)
  {
    ArrayList localArrayList = new ArrayList();
    int j = paramArrayOfDPObject.length;
    int i = 0;
    if (i < j)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      if (localDPObject.getArray("List") == null);
      while (true)
      {
        i += 1;
        break;
        List localList = convertServiceInfoData(localDPObject.getArray("List"));
        ServiceGroupData localServiceGroupData = new ServiceGroupData();
        localServiceGroupData.displayLimit = localDPObject.getInt("DisplayLimit");
        localServiceGroupData.list = localList;
        localServiceGroupData.title = localDPObject.getString("Title");
        localServiceGroupData.promoType = localDPObject.getInt("PromoType");
        localArrayList.add(localServiceGroupData);
      }
    }
    return localArrayList;
  }

  private List<ServiceInfoData> convertServiceInfoData(DPObject[] paramArrayOfDPObject)
  {
    ArrayList localArrayList = new ArrayList();
    int j = paramArrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      ServiceInfoData localServiceInfoData = new ServiceInfoData();
      localServiceInfoData.action = localDPObject.getString("Action");
      localServiceInfoData.name = localDPObject.getString("Name");
      localServiceInfoData.price = localDPObject.getString("Price");
      localServiceInfoData.priceDesc = localDPObject.getString("PriceDesc");
      localServiceInfoData.promos = localDPObject.getStringArray("Promos");
      localServiceInfoData.serviceType = localDPObject.getInt("ServiceType");
      localArrayList.add(localServiceInfoData);
      i += 1;
    }
    return localArrayList;
  }

  private View createItemView(ServiceInfoData paramServiceInfoData, int paramInt1, int paramInt2)
  {
    View localView = MyResources.getResource(ServiceListAgent.class).inflate(getContext(), R.layout.car_service_item, getParentView(), false);
    localView.setTag(paramServiceInfoData);
    localView.setOnClickListener(this.onServiceItemClickListener);
    Object localObject = (ImageView)localView.findViewById(R.id.car_service_type_icon);
    if (paramInt2 == 1)
      ((ImageView)localObject).setImageResource(R.drawable.detail_booking_icon);
    if (paramInt1 == 0)
    {
      localView.findViewById(R.id.car_service_item_top_line).setVisibility(8);
      ((ImageView)localObject).setVisibility(0);
    }
    if ((paramServiceInfoData.promos != null) && (paramServiceInfoData.promos.length > 0) && (!TextUtils.isEmpty(paramServiceInfoData.promos[0])))
    {
      localObject = (TextView)localView.findViewById(R.id.car_service_item_promo);
      ((TextView)localObject).setText(paramServiceInfoData.promos[0]);
      ((TextView)localObject).setVisibility(0);
    }
    ((TextView)localView.findViewById(R.id.car_service_item_title)).setText(paramServiceInfoData.name);
    renderPrice((TextView)localView.findViewById(R.id.car_service_item_price), paramServiceInfoData);
    return (View)localView;
  }

  private View createServiceGroupView(ServiceGroupData paramServiceGroupData)
  {
    View localView1 = MyResources.getResource(ServiceListAgent.class).inflate(getContext(), R.layout.shop_car_service_group_cell, getParentView(), false);
    ViewGroup localViewGroup = (ViewGroup)localView1.findViewById(R.id.car_service_item_container);
    Object localObject = (TextView)localView1.findViewById(R.id.car_service_group_title);
    View localView2 = localView1.findViewById(R.id.car_service_more_container);
    View localView3 = localView1.findViewById(R.id.car_service_less_container);
    ((TextView)localObject).setText(paramServiceGroupData.title);
    localView3.setVisibility(8);
    localView2.setVisibility(8);
    int i = 0;
    int k = paramServiceGroupData.list.size();
    if (i < k)
    {
      localObject = createItemView((ServiceInfoData)paramServiceGroupData.list.get(i), i, paramServiceGroupData.promoType);
      localViewGroup.addView((View)localObject);
      if (i + 1 > paramServiceGroupData.displayLimit)
        if (!paramServiceGroupData.hide)
          break label176;
      label176: for (int j = 8; ; j = 0)
      {
        ((View)localObject).setVisibility(j);
        i += 1;
        break;
      }
    }
    if (paramServiceGroupData.list.size() <= paramServiceGroupData.displayLimit)
      return localView1;
    setExpandView(paramServiceGroupData, localView2, localView3);
    return (View)localView1;
  }

  private View createServiceView()
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shop_car_service_cell, getParentView(), false);
    localNovaLinearLayout.gaUserInfo.shop_id = Integer.valueOf(this.shopId);
    localNovaLinearLayout.gaUserInfo.category_id = Integer.valueOf(this.categoryId);
    localNovaLinearLayout.setGAString("shopinfo_car_service_list");
    ((DPActivity)getFragment().getActivity()).addGAView(localNovaLinearLayout, -1);
    this.itemListContainerView = ((ViewGroup)localNovaLinearLayout.findViewById(R.id.car_service_group_container));
    int i = 0;
    int j = this.serviceDataList.size();
    while (i < j)
    {
      View localView = createServiceGroupView((ServiceGroupData)this.serviceDataList.get(i));
      this.itemListContainerView.addView(localView);
      if (i == j - 1)
        localView.findViewById(R.id.car_service_group_divider).setVisibility(8);
      i += 1;
    }
    return localNovaLinearLayout;
  }

  private void expand(View paramView)
  {
    if ((paramView == null) || (paramView.getTag() == null));
    do
      return;
    while (!(paramView.getTag() instanceof ServiceGroupData));
    paramView = (ServiceGroupData)paramView.getTag();
    if (!paramView.hide);
    for (boolean bool = true; ; bool = false)
    {
      paramView.hide = bool;
      dispatchAgentChanged(false);
      return;
    }
  }

  private void extractShopInfo()
  {
    DPObject localDPObject = getShop();
    if (localDPObject == null)
    {
      Log.e(TAG, "Null shop data. Can not update shop info.");
      return;
    }
    this.shopId = localDPObject.getInt("ID");
    this.categoryId = localDPObject.getInt("CategoryID");
  }

  private String extractToken()
  {
    String str2 = accountService().token();
    String str1 = str2;
    if (TextUtils.isEmpty(str2))
      str1 = "";
    return str1;
  }

  private boolean paramIsValid()
  {
    return this.shopId > 0;
  }

  private void renderPrice(TextView paramTextView, ServiceInfoData paramServiceInfoData)
  {
    SpannableString localSpannableString = new SpannableString("￥" + paramServiceInfoData.price + " " + paramServiceInfoData.priceDesc);
    localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
    localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_list)), 1, localSpannableString.length() - paramServiceInfoData.priceDesc.length(), 33);
    localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), localSpannableString.length() - paramServiceInfoData.priceDesc.length(), localSpannableString.length(), 33);
    localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, localSpannableString.length() - paramServiceInfoData.priceDesc.length(), 33);
    localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_gray)), localSpannableString.length() - paramServiceInfoData.priceDesc.length(), localSpannableString.length(), 33);
    paramTextView.setText(localSpannableString);
  }

  private void sendRequest(String paramString)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/car/service/fetch/infolist.car").buildUpon();
    localBuilder.appendQueryParameter("shopid", this.shopId + "");
    localBuilder.appendQueryParameter("token", paramString);
    this.mRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this.mRequestHandler);
  }

  private void setExpandView(ServiceGroupData paramServiceGroupData, View paramView1, View paramView2)
  {
    int j = 8;
    if (paramServiceGroupData.hide)
    {
      i = 0;
      paramView1.setVisibility(i);
      ((TextView)paramView1.findViewById(R.id.car_service_more_text)).setText("查看更多" + (paramServiceGroupData.list.size() - paramServiceGroupData.displayLimit) + "个服务");
      if (!paramServiceGroupData.hide)
        break label123;
    }
    label123: for (int i = j; ; i = 0)
    {
      paramView2.setVisibility(i);
      paramView2.setTag(paramServiceGroupData);
      paramView1.setTag(paramServiceGroupData);
      paramView2.setOnClickListener(this.onExpandClickListener);
      paramView1.setOnClickListener(this.onExpandClickListener);
      return;
      i = 8;
      break;
    }
  }

  protected void goPay(ServiceInfoData paramServiceInfoData)
  {
    if (TextUtils.isEmpty(paramServiceInfoData.action))
      return;
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramServiceInfoData.action)));
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if ((this.serviceObject == null) || (this.serviceDataList == null) || (this.serviceDataList.size() == 0))
      return;
    super.onAgentChanged(paramBundle);
    this.serviceView = createServiceView();
    addCell("0600Service.10ServiceList", this.serviceView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    extractShopInfo();
    if (!paramIsValid())
      return;
    sendRequest(extractToken());
  }

  protected void statisticsManually(String paramString1, String paramString2, String paramString3)
  {
    GAUserInfo localGAUserInfo = new GAUserInfo();
    localGAUserInfo.shop_id = Integer.valueOf(this.shopId);
    localGAUserInfo.category_id = Integer.valueOf(this.categoryId);
    localGAUserInfo.title = paramString3;
    GAHelper.instance().contextStatisticsEvent(getContext(), paramString1, localGAUserInfo, paramString2);
  }

  class ServiceGroupData
  {
    int displayLimit;
    boolean hide = true;
    List<ServiceListAgent.ServiceInfoData> list;
    int promoType;
    String title;

    ServiceGroupData()
    {
    }
  }

  class ServiceInfoData
  {
    String action;
    String name;
    String price;
    String priceDesc;
    String[] promos;
    int serviceType;

    ServiceInfoData()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.car.ServiceListAgent
 * JD-Core Version:    0.6.0
 */
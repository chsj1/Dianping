package com.dianping.shopinfo.car;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MeasuredListView;
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
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParkNearbyAgent extends ShopCellAgent
{
  private static final String API_URL = "http://mapi.dianping.com/car/shop/findnearbypark.car?";
  private static final String CELL_CAR_PARK_Nearby = "7050ParkNearby.carPark";
  private static final String TAG = ParkNearbyAgent.class.getSimpleName();
  protected DPObject mParkingInfo;
  protected View mParkingView;
  protected MApiRequest mRequest;
  private View.OnClickListener onItemClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = paramView.getTag();
      if (paramView == null);
      do
      {
        do
          return;
        while (!(paramView instanceof ParkNearbyAgent.ParkNearbyItemHolder));
        paramView = (ParkNearbyAgent.ParkNearbyItemHolder)paramView;
      }
      while (paramView.shopId <= 0);
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramView.shopId));
      localIntent.putExtra("shopId", paramView.shopId);
      ParkNearbyAgent.this.startActivity(localIntent);
    }
  };

  public ParkNearbyAgent(Object paramObject)
  {
    super(paramObject);
  }

  private ParkNearbyHolder createParkNearbyHolder()
  {
    ParkNearbyHolder localParkNearbyHolder = new ParkNearbyHolder();
    localParkNearbyHolder.parkListUrl = this.mParkingInfo.getString("ParkListUrl");
    DPObject[] arrayOfDPObject = this.mParkingInfo.getArray("List");
    if (arrayOfDPObject != null)
    {
      int j = arrayOfDPObject.length;
      int i = 0;
      if (i < j)
      {
        DPObject localDPObject = arrayOfDPObject[i];
        if (localDPObject == null);
        while (true)
        {
          i += 1;
          break;
          localParkNearbyHolder.shopList.add(localDPObject);
        }
      }
    }
    return localParkNearbyHolder;
  }

  private View createParkNearbyView(ParkNearbyHolder paramParkNearbyHolder)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shop_car_parknearby_cell, getParentView(), false);
    Object localObject = (NovaRelativeLayout)localNovaLinearLayout.findViewById(R.id.car_parknearby_title);
    if (!TextUtils.isEmpty(paramParkNearbyHolder.parkListUrl))
    {
      ((NovaRelativeLayout)localObject).setTag(paramParkNearbyHolder.parkListUrl);
      ((NovaRelativeLayout)localObject).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = paramView.getTag();
          if (paramView == null);
          do
          {
            do
              return;
            while (!(paramView instanceof String));
            paramView = paramView.toString();
          }
          while (TextUtils.isEmpty(paramView));
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
          ParkNearbyAgent.this.startActivity(paramView);
        }
      });
      ((NovaRelativeLayout)localObject).gaUserInfo.shop_id = Integer.valueOf(shopId());
      ((NovaRelativeLayout)localObject).setGAString("shopinfo_car_park_nearby_list");
      ((DPActivity)getFragment().getActivity()).addGAView((View)localObject, -1);
    }
    while (true)
    {
      localNovaLinearLayout.gaUserInfo.shop_id = Integer.valueOf(shopId());
      localNovaLinearLayout.setGAString("shopinfo_car_park_nearby");
      ((DPActivity)getFragment().getActivity()).addGAView(localNovaLinearLayout, -1);
      localObject = (MeasuredListView)localNovaLinearLayout.findViewById(R.id.car_parknearby_lists);
      if (paramParkNearbyHolder.shopList.size() <= 0)
        break;
      ((MeasuredListView)localObject).setAdapter(new ParkNearbyAdapter(paramParkNearbyHolder.shopList));
      return localNovaLinearLayout;
      ((NovaRelativeLayout)localObject).findViewById(R.id.car_parknearby_title_arrow).setVisibility(8);
    }
    ((MeasuredListView)localObject).setVisibility(8);
    localNovaLinearLayout.findViewById(R.id.car_parknearby_line).setVisibility(8);
    return (View)localNovaLinearLayout;
  }

  private boolean paramIsValid()
  {
    if (getShop() == null)
      Log.e(TAG, "Null shop data. Can not update shop info.");
    do
      return false;
    while (shopId() <= 0);
    return true;
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/car/shop/findnearbypark.car?").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.mRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        ParkNearbyAgent.this.mRequest = null;
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (ParkNearbyAgent.this.mRequest != paramMApiRequest);
        do
        {
          return;
          ParkNearbyAgent.this.mParkingInfo = ((DPObject)paramMApiResponse.result());
        }
        while (ParkNearbyAgent.this.mParkingInfo == null);
        ParkNearbyAgent.this.dispatchAgentChanged(false);
      }
    });
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (this.mParkingView != null);
    do
    {
      do
        return;
      while (this.mParkingInfo == null);
      super.onAgentChanged(paramBundle);
      paramBundle = createParkNearbyHolder();
    }
    while (paramBundle == null);
    this.mParkingView = createParkNearbyView(paramBundle);
    addCell("7050ParkNearby.carPark", this.mParkingView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!paramIsValid())
      return;
    sendRequest();
  }

  class ParkNearbyAdapter extends BasicAdapter
  {
    private List<ParkNearbyAgent.ParkNearbyItemHolder> list = new ArrayList();

    public ParkNearbyAdapter()
    {
      DPObject localDPObject;
      if (localDPObject == null);
      while (true)
      {
        return;
        this$1 = localDPObject.iterator();
        while (ParkNearbyAgent.this.hasNext())
        {
          localDPObject = (DPObject)ParkNearbyAgent.this.next();
          if ((localDPObject.getInt("ShopId") == 0) || (TextUtils.isEmpty(localDPObject.getString("Name"))) || (TextUtils.isEmpty(localDPObject.getString("Address"))))
            continue;
          this.list.add(convertToParkNearbyItemHolder(localDPObject));
        }
      }
    }

    protected ParkNearbyAgent.ParkNearbyItemHolder convertToParkNearbyItemHolder(DPObject paramDPObject)
    {
      ParkNearbyAgent.ParkNearbyItemHolder localParkNearbyItemHolder = new ParkNearbyAgent.ParkNearbyItemHolder(ParkNearbyAgent.this);
      localParkNearbyItemHolder.shopId = paramDPObject.getInt("ShopId");
      localParkNearbyItemHolder.distanceDesc = paramDPObject.getString("DistanceDesc");
      localParkNearbyItemHolder.leftPark = paramDPObject.getInt("LeftPark");
      localParkNearbyItemHolder.totalPark = paramDPObject.getInt("TotalPark");
      localParkNearbyItemHolder.address = paramDPObject.getString("Address");
      localParkNearbyItemHolder.name = paramDPObject.getString("Name");
      localParkNearbyItemHolder.picUrl = paramDPObject.getString("PicUrl");
      return localParkNearbyItemHolder;
    }

    public int getCount()
    {
      return this.list.size();
    }

    public Object getItem(int paramInt)
    {
      if (this.list.size() <= paramInt)
        return null;
      return this.list.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      ParkNearbyAgent.ParkNearbyItemHolder localParkNearbyItemHolder = (ParkNearbyAgent.ParkNearbyItemHolder)this.list.get(paramInt);
      Object localObject = paramView;
      if (paramView == null)
        localObject = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shop_car_parknearby_item, null, false);
      paramView = (NovaLinearLayout)localObject;
      if (!TextUtils.isEmpty(localParkNearbyItemHolder.picUrl))
        ((NetworkImageView)paramView.findViewById(R.id.parknearby_photo)).setImage(localParkNearbyItemHolder.picUrl);
      ((TextView)paramView.findViewById(R.id.parknearby_name)).setText(localParkNearbyItemHolder.name);
      int i;
      int j;
      if (localParkNearbyItemHolder.leftPark >= 0)
      {
        i = 1;
        if (localParkNearbyItemHolder.totalPark <= 0)
          break label435;
        j = 1;
        label114: paramViewGroup = (TextView)paramView.findViewById(R.id.parknearby_park);
        if ((i == 0) || (j == 0))
          break label441;
        localObject = localParkNearbyItemHolder.leftPark + "";
        SpannableString localSpannableString = new SpannableString(String.format("空余车位 %s%s", new Object[] { (String)localObject + "/", localParkNearbyItemHolder.totalPark + "" }));
        localSpannableString.setSpan(new ForegroundColorSpan(ParkNearbyAgent.this.getResources().getColor(R.color.light_red)), 5, ((String)localObject).length() + 5, 33);
        localSpannableString.setSpan(new AbsoluteSizeSpan(ParkNearbyAgent.this.res.getDimensionPixelSize(R.dimen.text_size_14)), 5, ((String)localObject).length() + 5, 33);
        paramViewGroup.setText(localSpannableString);
        paramViewGroup.setVisibility(0);
      }
      while (true)
      {
        ((TextView)paramView.findViewById(R.id.parknearby_address)).setText(localParkNearbyItemHolder.address);
        if (!TextUtils.isEmpty(localParkNearbyItemHolder.distanceDesc))
          ((TextView)paramView.findViewById(R.id.parknearby_distance)).setText(localParkNearbyItemHolder.distanceDesc);
        paramView.setTag(localParkNearbyItemHolder);
        paramView.gaUserInfo.shop_id = Integer.valueOf(ParkNearbyAgent.this.shopId());
        paramView.gaUserInfo.index = Integer.valueOf(paramInt + 1);
        paramView.setGAString("shopinfo_car_park_nearby_item");
        paramView.setOnClickListener(ParkNearbyAgent.this.onItemClickListener);
        ((DPActivity)ParkNearbyAgent.this.getFragment().getActivity()).addGAView(paramView, paramInt);
        return paramView;
        i = 0;
        break;
        label435: j = 0;
        break label114;
        label441: paramViewGroup.setText("");
        paramViewGroup.setVisibility(8);
      }
    }
  }

  class ParkNearbyHolder
  {
    protected String parkListUrl;
    protected List<DPObject> shopList = new ArrayList();

    ParkNearbyHolder()
    {
    }
  }

  class ParkNearbyItemHolder
  {
    protected String address;
    protected String distanceDesc;
    protected int leftPark;
    protected String name;
    protected String picUrl;
    protected int shopId;
    protected int totalPark;

    ParkNearbyItemHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.car.ParkNearbyAgent
 * JD-Core Version:    0.6.0
 */
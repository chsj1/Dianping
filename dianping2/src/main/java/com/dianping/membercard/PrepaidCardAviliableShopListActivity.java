package com.dianping.membercard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;

public class PrepaidCardAviliableShopListActivity extends NovaListActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  MApiRequest nearestShopRequest;
  int prepaidCardID;
  protected ShopListAdapter shopListAdapter = new ShopListAdapter();

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.shopListAdapter.setShouldShowImage(NovaConfigUtils.isShowImageInMobileNetwork());
    paramBundle = getIntent().getData().getQueryParameter("prepaidcardid");
    if (!TextUtils.isEmpty(paramBundle))
      try
      {
        this.prepaidCardID = Integer.parseInt(paramBundle);
        paramBundle = (DPObject)getIntent().getParcelableExtra("shopList");
        this.listView.setAdapter(this.shopListAdapter);
        if (paramBundle != null)
          this.shopListAdapter.appendShop(paramBundle);
        this.listView.setOnItemClickListener(this);
        return;
      }
      catch (java.lang.Exception paramBundle)
      {
        Toast.makeText(this, "参数错误，请稍后再试。", 0).show();
        finish();
        return;
      }
    Toast.makeText(this, "参数错误，请稍后再试。", 0).show();
    finish();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if ((paramAdapterView instanceof DPObject))
    {
      paramAdapterView = (DPObject)paramAdapterView;
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramAdapterView.getInt("ID")));
      paramView.putExtra("shopId", paramAdapterView.getInt("ID"));
      paramView.putExtra("shop", paramAdapterView);
      startActivity(paramView);
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiRequest = "服务器错误，请稍后再试";
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiResponse != null)
      paramMApiRequest = paramMApiResponse.toString();
    this.shopListAdapter.setError(paramMApiRequest);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject))
      this.shopListAdapter.appendShop((DPObject)paramMApiResponse.result());
  }

  public void reload(int paramInt)
  {
    updateNearestShop(paramInt);
  }

  public void updateNearestShop(int paramInt)
  {
    Object localObject2 = "http://app.t.dianping.com/prepaidcardshoplistgn.bin?prepaidcardid=" + this.prepaidCardID + "&start=" + paramInt + "&cityid=" + city().id();
    Object localObject3 = accountService();
    Object localObject1 = localObject2;
    if (((AccountService)localObject3).token() != null)
      localObject1 = (String)localObject2 + "&token=" + ((AccountService)localObject3).token();
    localObject3 = locationService().location();
    localObject2 = localObject1;
    if (localObject3 != null)
    {
      double d1 = ((DPObject)localObject3).getDouble("Lat");
      double d2 = ((DPObject)localObject3).getDouble("Lng");
      localObject2 = (String)localObject1 + "&lat=" + d1 + "&lng=" + d2;
    }
    this.nearestShopRequest = BasicMApiRequest.mapiGet((String)localObject2, CacheType.DISABLED);
    mapiService().exec(this.nearestShopRequest, this);
  }

  public class ShopListAdapter extends BasicAdapter
  {
    protected String errorMsg;
    protected boolean isEnd;
    private int nextStartIndex;
    protected double offsetLatitude;
    protected double offsetLongitude;
    protected ArrayList<DPObject> shops = new ArrayList();
    protected boolean shouldShowImage;
    protected boolean showDistance;

    public ShopListAdapter()
    {
    }

    public void appendShop(DPObject paramDPObject)
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
      int j = arrayOfDPObject.length;
      int i = 0;
      while (i < j)
      {
        DPObject localDPObject = arrayOfDPObject[i];
        this.shops.add(localDPObject);
        i += 1;
      }
      this.isEnd = paramDPObject.getBoolean("IsEnd");
      this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
      notifyDataSetChanged();
    }

    public int getCount()
    {
      if ((!this.isEnd) || (!TextUtils.isEmpty(this.errorMsg)))
        return this.shops.size() + 1;
      return this.shops.size();
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.shops.size())
        return this.shops.get(paramInt);
      if ((!this.isEnd) && (TextUtils.isEmpty(this.errorMsg)))
        return LOADING;
      if (!TextUtils.isEmpty(this.errorMsg))
        return ERROR;
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        DPObject localDPObject = (DPObject)localObject;
        if ((paramView instanceof ShopListItem));
        for (paramView = (ShopListItem)paramView; ; paramView = null)
        {
          localObject = paramView;
          if (paramView == null)
            localObject = (ShopListItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shop_item, paramViewGroup, false);
          if (localDPObject != null)
            ((ShopListItem)localObject).setShop(localDPObject, -1, this.offsetLatitude, this.offsetLongitude, this.shouldShowImage);
          if (!this.showDistance)
            ((ShopListItem)localObject).showDistanceText(false);
          return localObject;
        }
      }
      if (localObject == LOADING)
      {
        PrepaidCardAviliableShopListActivity.this.reload(this.nextStartIndex);
        return getLoadingView(paramViewGroup, paramView);
      }
      return (View)getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          PrepaidCardAviliableShopListActivity.ShopListAdapter.this.isEnd = false;
          PrepaidCardAviliableShopListActivity.ShopListAdapter.this.errorMsg = null;
          PrepaidCardAviliableShopListActivity.this.reload(PrepaidCardAviliableShopListActivity.ShopListAdapter.this.nextStartIndex);
          PrepaidCardAviliableShopListActivity.ShopListAdapter.this.notifyDataSetChanged();
        }
      }
      , paramViewGroup, paramView);
    }

    public void setError(String paramString)
    {
      this.errorMsg = paramString;
      notifyDataSetChanged();
    }

    public void setShouldShowImage(boolean paramBoolean)
    {
      this.shouldShowImage = paramBoolean;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.PrepaidCardAviliableShopListActivity
 * JD-Core Version:    0.6.0
 */
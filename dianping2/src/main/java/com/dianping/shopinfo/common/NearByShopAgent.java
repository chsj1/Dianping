package com.dianping.shopinfo.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.ShopInfoGridView;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class NearByShopAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_NEAR_ITEM = "8500ShopInfo.20nearbyshop";
  private static final String URL = "http://m.api.dianping.com/mshop/nearby.bin";
  View.OnClickListener mClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = NearByShopAgent.this.getShop();
      if (paramView == null)
        return;
      Object localObject = new ArrayList();
      ((List)localObject).add(new BasicNameValuePair("shopid", NearByShopAgent.this.shopId() + ""));
      NearByShopAgent.this.statisticsEvent("shopinfo5", "shopinfo5_nearby", "全部", 0, (List)localObject);
      if (NearByShopAgent.this.isWeddingShopType())
      {
        localObject = new ArrayList();
        ((List)localObject).add(new BasicNameValuePair("shopid", NearByShopAgent.this.shopId() + ""));
        NearByShopAgent.this.statisticsEvent("shopinfoq", "shopinfoq_recommendshop", "", 0, (List)localObject);
      }
      localObject = new StringBuilder("dianping://nearbyshoplist");
      ((StringBuilder)localObject).append("?shopid=").append(NearByShopAgent.this.shopId());
      ((StringBuilder)localObject).append("&shopname=").append(paramView.getString("Name"));
      ((StringBuilder)localObject).append("&title=").append(paramView.getString("Name") + "附近");
      ((StringBuilder)localObject).append("&cityid=").append(paramView.getInt("CityID"));
      ((StringBuilder)localObject).append("&shoplatitude=").append(paramView.getDouble("Latitude"));
      ((StringBuilder)localObject).append("&shoplongitude=").append(paramView.getDouble("Longitude"));
      ((StringBuilder)localObject).append("&categoryid=").append(0);
      ((StringBuilder)localObject).append("&category=").append("全部");
      NearByShopAgent.this.getFragment().startActivity(((StringBuilder)localObject).toString());
    }
  };
  private ArrayList<DPObject> mNearByShopList = new ArrayList();
  private MApiRequest mReq;
  private boolean mbReqFinished;

  public NearByShopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createNearByInfoView()
  {
    Object localObject = getShop();
    if ((localObject != null) && (((DPObject)localObject).getDouble("Latitude") != 0.0D) && (((DPObject)localObject).getDouble("Longitude") != 0.0D))
    {
      localObject = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
      if ((this.mNearByShopList != null) && (this.mNearByShopList.size() > 0))
      {
        View localView = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_grid_view, null);
        ShopInfoGridView localShopInfoGridView = (ShopInfoGridView)localView.findViewById(R.id.id_gridview);
        localShopInfoGridView.setNumColumns(3);
        localShopInfoGridView.setAdapter(new GridAdapter(this.mNearByShopList));
        localShopInfoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
          public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
          {
            paramAdapterView = ((DPObject)NearByShopAgent.this.mNearByShopList.get(paramInt)).getString("Url");
            if (!TextUtils.isEmpty(paramAdapterView))
            {
              paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse(paramAdapterView));
              NearByShopAgent.this.getContext().startActivity(paramAdapterView);
            }
          }
        });
        ((ShopinfoCommonCell)localObject).addContent(localView, false);
      }
      ((ShopinfoCommonCell)localObject).setTitle("在这家店附近找", this.mClickListener, "nearby_search", getGAExtra());
      return localObject;
    }
    return (View)null;
  }

  private void sendRequest()
  {
    if (getFragment() == null)
      return;
    if (this.mReq != null)
      getFragment().mapiService().abort(this.mReq, this, true);
    int i = shopId();
    this.mReq = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/mshop/nearby.bin").buildUpon().appendQueryParameter("shopid", String.valueOf(i)).build().toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.mReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if ((isWeddingType()) || (isHotelType()) || (getShop() == null));
    do
    {
      return;
      paramBundle = createNearByInfoView();
    }
    while (paramBundle == null);
    addCell("8500ShopInfo.20nearbyshop", paramBundle, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!this.mbReqFinished)
    {
      sendRequest();
      this.mbReqFinished = true;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mReq = null;
    dispatchAgentChanged(false);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mReq = null;
    if ((paramMApiResponse == null) || (paramMApiResponse.result() == null))
      return;
    paramMApiRequest = ((DPObject)paramMApiResponse.result()).getArray("ShopNearbys");
    if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
    {
      this.mNearByShopList.clear();
      this.mNearByShopList.addAll(Arrays.asList(paramMApiRequest));
    }
    dispatchAgentChanged(false);
  }

  private class GridAdapter extends BaseAdapter
  {
    private ArrayList<DPObject> mDataList;

    public GridAdapter()
    {
      Object localObject;
      this.mDataList = localObject;
    }

    public int getCount()
    {
      return this.mDataList.size();
    }

    public Object getItem(int paramInt)
    {
      return this.mDataList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = (DPObject)this.mDataList.get(paramInt);
      if (paramView == null)
      {
        paramView = LayoutInflater.from(NearByShopAgent.this.getContext()).inflate(R.layout.shopinfo_nearby_label_item, null);
        paramViewGroup = new NearByShopAgent.ViewHolder(NearByShopAgent.this);
        paramViewGroup.mIcon = ((NetworkImageView)paramView.findViewById(R.id.icon));
        paramViewGroup.mTitle = ((TextView)paramView.findViewById(R.id.text));
        paramView.setTag(paramViewGroup);
      }
      while (true)
      {
        paramViewGroup.mIcon.setImage(localDPObject.getString("Icon"));
        paramViewGroup.mTitle.setText(TextUtils.jsonParseText(localDPObject.getString("Name")));
        return paramView;
        paramViewGroup = (NearByShopAgent.ViewHolder)paramView.getTag();
      }
    }
  }

  class ViewHolder
  {
    public NetworkImageView mIcon;
    public TextView mTitle;

    ViewHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.NearByShopAgent
 * JD-Core Version:    0.6.0
 */
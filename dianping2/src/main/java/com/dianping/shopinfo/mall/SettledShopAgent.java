package com.dianping.shopinfo.mall;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.adapter.CustomGridViewAdapter;
import com.dianping.base.widget.CustomGridView;
import com.dianping.base.widget.CustomGridView.OnItemClickListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.Arrays;

public class SettledShopAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, CustomGridView.OnItemClickListener
{
  private static final String CELL_SETTLEDSHOP = "0300Basic.15content";
  private SettledShopGridAdapter adapter;
  DPObject mSettledShopList;
  MApiRequest request;

  public SettledShopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createContentCell(DPObject[] paramArrayOfDPObject)
  {
    DPObject[] arrayOfDPObject;
    if ((paramArrayOfDPObject.length >= 3) && (paramArrayOfDPObject.length < 6))
      arrayOfDPObject = (DPObject[])Arrays.copyOf(paramArrayOfDPObject, 3);
    while (true)
    {
      paramArrayOfDPObject = this.res.inflate(getContext(), R.layout.shopinfo_mall_settledshop, getParentView(), false);
      if (this.adapter == null)
        this.adapter = new SettledShopGridAdapter();
      CustomGridView localCustomGridView = (CustomGridView)paramArrayOfDPObject.findViewById(R.id.gallery_gridview);
      localCustomGridView.setVerticalDivider(this.res.getDrawable(R.drawable.tuan_home_divider_vertical));
      localCustomGridView.setHorizontalDivider(this.res.getDrawable(R.drawable.tuan_home_divider));
      localCustomGridView.setEndHorizontalDivider(null);
      localCustomGridView.setAdapter(this.adapter);
      localCustomGridView.setOnItemClickListener(this);
      this.adapter.setData(arrayOfDPObject);
      this.adapter.notifyDataSetChanged();
      return paramArrayOfDPObject;
      arrayOfDPObject = paramArrayOfDPObject;
      if (paramArrayOfDPObject.length < 6)
        continue;
      arrayOfDPObject = (DPObject[])Arrays.copyOf(paramArrayOfDPObject, 6);
    }
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/getsettledshoplist.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopId", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.request = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((this.mSettledShopList == null) && (this.request == null))
      sendRequest();
    if (this.mSettledShopList != null)
    {
      paramBundle = this.mSettledShopList.getArray("Shops");
      if ((paramBundle != null) && (paramBundle.length >= 1))
        break label51;
    }
    label51: 
    do
    {
      return;
      removeAllCells();
    }
    while (paramBundle.length < 3);
    addCell("0300Basic.15content", createContentCell(paramBundle), "inmall", 0);
  }

  public void onItemClick(CustomGridView paramCustomGridView, View paramView, int paramInt, long paramLong)
  {
    if (this.mSettledShopList != null)
    {
      paramCustomGridView = this.mSettledShopList.getArray("Shops");
      if ((paramCustomGridView != null) && (paramInt <= paramCustomGridView.length - 1) && (paramInt >= 0))
        break label34;
    }
    label34: 
    do
    {
      do
      {
        return;
        paramCustomGridView = paramCustomGridView[paramInt];
      }
      while (paramCustomGridView == null);
      paramCustomGridView = paramCustomGridView.getString("ShopUrl");
    }
    while ((paramCustomGridView == null) || (paramCustomGridView.equals("")));
    paramView = new Intent("android.intent.action.VIEW");
    paramView.setData(Uri.parse(paramCustomGridView));
    startActivity(paramView);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.mSettledShopList = ((DPObject)paramMApiResponse.result());
    if (this.mSettledShopList == null)
      return;
    dispatchAgentChanged(false);
  }

  class SettledShopGridAdapter extends CustomGridViewAdapter
  {
    private DPObject[] settledShopList;

    SettledShopGridAdapter()
    {
    }

    public int getColumnCount()
    {
      return 3;
    }

    public int getCount()
    {
      if (this.settledShopList == null)
        return 0;
      return this.settledShopList.length;
    }

    public Object getItem(int paramInt)
    {
      if ((this.settledShopList == null) || (this.settledShopList.length <= paramInt))
        return null;
      return this.settledShopList[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getItemView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = (DPObject)getItem(paramInt);
      View localView = paramView;
      if (paramView == null)
        localView = ((LayoutInflater)(LayoutInflater)SettledShopAgent.this.getContext().getSystemService("layout_inflater")).inflate(R.layout.shopinfo_mall_settledshop_item, paramViewGroup, false);
      if (localDPObject == null)
        return null;
      paramView = localDPObject.getString("ShopPic");
      ((NetworkImageView)localView.findViewById(R.id.item_photo)).setImage(paramView);
      paramView = (TextView)localView.findViewById(R.id.item_text);
      if (paramView != null)
        paramView.setText(localDPObject.getString("ShopName"));
      paramView = (TextView)localView.findViewById(R.id.item_tag);
      if (paramView != null)
      {
        paramViewGroup = localDPObject.getString("ShopTag");
        if ((paramViewGroup != null) && (paramViewGroup != ""))
        {
          paramView.setText(paramViewGroup);
          paramView.setVisibility(0);
        }
      }
      ((NovaRelativeLayout)localView).setGAString("inmall_single", localDPObject.getString("ShopName"), paramInt);
      GAHelper.instance().statisticsEvent(localView, "view");
      return localView;
    }

    public void setData(DPObject[] paramArrayOfDPObject)
    {
      this.settledShopList = paramArrayOfDPObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.mall.SettledShopAgent
 * JD-Core Version:    0.6.0
 */
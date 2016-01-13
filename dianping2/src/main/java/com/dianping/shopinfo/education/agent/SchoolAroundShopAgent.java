package com.dianping.shopinfo.education.agent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
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
import com.dianping.widget.view.NovaRelativeLayout;

public class SchoolAroundShopAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, CustomGridView.OnItemClickListener
{
  private static final String CELL_AROUNDSHOP = "0300Around.01";
  private DPObject aroundShops;
  MApiRequest request;

  public SchoolAroundShopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createContentCell(DPObject[] paramArrayOfDPObject)
  {
    View localView = this.res.inflate(getContext(), R.layout.shopinfo_edu_aroundshop, getParentView(), false);
    Object localObject1 = (NovaRelativeLayout)localView.findViewById(R.id.edu_aroundshop_detail);
    ((NovaRelativeLayout)localObject1).setGAString("edu_schoolnearby_more", getGAExtra());
    if ((this.aroundShops.getString("DetailLink") != null) && (!this.aroundShops.getString("DetailLink").equals("")))
      ((NovaRelativeLayout)localObject1).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW");
          paramView.setData(Uri.parse(SchoolAroundShopAgent.this.aroundShops.getString("DetailLink")));
          SchoolAroundShopAgent.this.getFragment().startActivity(paramView);
        }
      });
    localObject1 = (TextView)localView.findViewById(R.id.edu_aroundshop_title);
    Object localObject2 = this.aroundShops.getString("Title");
    if ((localObject2 != null) && (!((String)localObject2).equals("")))
      ((TextView)localObject1).setText((CharSequence)localObject2);
    localObject1 = new AroundShopGridAdapter();
    localObject2 = (CustomGridView)localView.findViewById(R.id.gallery_gridview);
    ((CustomGridView)localObject2).setVerticalDivider(this.res.getDrawable(R.drawable.tuan_home_divider_vertical));
    ((CustomGridView)localObject2).setHorizontalDivider(this.res.getDrawable(R.drawable.tuan_home_divider));
    ((CustomGridView)localObject2).setEndHorizontalDivider(null);
    ((CustomGridView)localObject2).setAdapter((Adapter)localObject1);
    ((CustomGridView)localObject2).setOnItemClickListener(this);
    ((AroundShopGridAdapter)localObject1).setData(paramArrayOfDPObject);
    ((AroundShopGridAdapter)localObject1).notifyDataSetChanged();
    return (View)(View)localView;
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/edu/aroundshopinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopId", shopId() + "");
    this.request = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((this.aroundShops == null) && (this.request == null))
      sendRequest();
    if (this.aroundShops != null)
    {
      paramBundle = this.aroundShops.getArray("CategoryShopInfoList");
      if ((paramBundle != null) && (paramBundle.length >= 1))
        break label51;
    }
    label51: 
    do
    {
      return;
      removeAllCells();
    }
    while (paramBundle.length < 4);
    addCell("0300Around.01", createContentCell(paramBundle));
  }

  public void onItemClick(CustomGridView paramCustomGridView, View paramView, int paramInt, long paramLong)
  {
    if (this.aroundShops != null)
    {
      paramCustomGridView = this.aroundShops.getArray("CategoryShopInfoList");
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
      paramCustomGridView = paramCustomGridView.getString("ShopListUrl");
    }
    while ((paramCustomGridView == null) || (paramCustomGridView.equals("")));
    paramView = new Intent("android.intent.action.VIEW");
    paramView.setData(Uri.parse(paramCustomGridView));
    getFragment().startActivity(paramView);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.aroundShops = ((DPObject)paramMApiResponse.result());
    if (this.aroundShops == null)
      return;
    dispatchAgentChanged(false);
  }

  class AroundShopGridAdapter extends CustomGridViewAdapter
  {
    private DPObject[] aroundShopList;

    AroundShopGridAdapter()
    {
    }

    public int getColumnCount()
    {
      return 3;
    }

    public int getCount()
    {
      if (this.aroundShopList == null)
        return 0;
      return this.aroundShopList.length;
    }

    public Object getItem(int paramInt)
    {
      if ((this.aroundShopList == null) || (this.aroundShopList.length <= paramInt))
        return null;
      return this.aroundShopList[paramInt];
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
        localView = ((LayoutInflater)(LayoutInflater)SchoolAroundShopAgent.this.getContext().getSystemService("layout_inflater")).inflate(R.layout.shopinfo_edu_aroundshop_item, paramViewGroup, false);
      if (localDPObject == null)
        paramView = null;
      do
      {
        return paramView;
        ((NovaRelativeLayout)localView).setGAString("edu_schoolnearby", localDPObject.getString("CategoryName"), paramInt);
        paramView = localDPObject.getString("Icon");
        ((NetworkImageView)localView.findViewById(R.id.item_photo)).setImage(paramView);
        paramView = (TextView)localView.findViewById(R.id.item_text);
        if (paramView != null)
          paramView.setText(localDPObject.getString("CategoryName"));
        paramViewGroup = (TextView)localView.findViewById(R.id.tuancount);
        paramInt = localDPObject.getInt("TuanCount");
        paramView = localView;
      }
      while (paramInt <= 0);
      paramViewGroup.setVisibility(0);
      paramViewGroup.setText(paramInt + "个团购");
      return localView;
    }

    public void setData(DPObject[] paramArrayOfDPObject)
    {
      this.aroundShopList = paramArrayOfDPObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.SchoolAroundShopAgent
 * JD-Core Version:    0.6.0
 */
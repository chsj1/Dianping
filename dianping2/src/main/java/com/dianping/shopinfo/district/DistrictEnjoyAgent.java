package com.dianping.shopinfo.district;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.base.widget.TableView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DistrictEnjoyAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, TabHost.TabContentFactory, TabHost.OnTabChangeListener
{
  private static final String CELL_ENJOY = "0500District.01shop";
  private NovaTextView footerText;
  private int indexTab = 0;
  private double latitude = 1.0D;
  private double longitude = 1.0D;
  private ViewGroup mContentView;
  private EnjoyAdapter mEnjoyAdapter = null;
  private DPObject[] mEnjoyList = null;
  private MApiRequest mEnjoyReq = null;
  private Map<String, Integer> mIndexHash = new HashMap();
  private TabHost mTabHost;
  private TableView mTableView;
  private Map<Integer, String> mTagHash = new HashMap();
  private View mView;

  public DistrictEnjoyAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initEnjoyTabView()
  {
    this.mContentView = ((ViewGroup)this.res.inflate(getContext(), R.layout.shopinfo_district_enjoy_table, getParentView(), false));
    this.mTableView = ((TableView)this.mContentView.findViewById(R.id.district_enjoy_table));
    if (this.mEnjoyList != null)
    {
      this.mEnjoyAdapter = new EnjoyAdapter(this.mEnjoyList[0].getArray("List"));
      this.mTableView.setAdapter(this.mEnjoyAdapter);
    }
    this.mView = this.res.inflate(getContext(), R.layout.shopinfo_district_enjoy_layout, getParentView(), false);
    this.mTabHost = ((TabHost)this.mView.findViewById(16908306));
    this.mTabHost.setFocusable(false);
    this.mTabHost.setup();
    this.footerText = ((NovaTextView)this.mView.findViewById(R.id.shoplist_more));
    this.footerText.gaUserInfo.index = Integer.valueOf(this.indexTab);
    Object localObject = this.mEnjoyList[0].getString("NaviUrl");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      this.footerText.setTag(localObject);
      this.footerText.setText(this.mEnjoyList[0].getString("NaviTitle"));
      this.footerText.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          DistrictEnjoyAgent.this.startActivity((String)DistrictEnjoyAgent.this.footerText.getTag());
        }
      });
    }
    while (true)
    {
      localObject = this.mTagHash.values().iterator();
      while (((Iterator)localObject).hasNext())
      {
        String str = (String)((Iterator)localObject).next();
        TextView localTextView = (TextView)this.res.inflate(getContext(), R.layout.tuan_tab_indicator, null, false);
        localTextView.setText(str);
        this.mTabHost.addTab(this.mTabHost.newTabSpec(str).setIndicator(localTextView).setContent(this));
      }
      this.footerText.setVisibility(8);
    }
    this.mTabHost.setCurrentTabByTag((String)this.mTagHash.get(Integer.valueOf(0)));
    this.mTabHost.setOnTabChangedListener(this);
    ((NovaActivity)getContext()).addGAView(this.mView.findViewById(R.id.shoplist), -1);
    ((NovaActivity)getContext()).addGAView(this.footerText, this.indexTab);
    addCell("0500District.01shop", this.mView);
  }

  private void requestEnjoyInfo()
  {
    if (this.mEnjoyReq != null)
      getFragment().mapiService().abort(this.mEnjoyReq, this, true);
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/multishoplist.bin");
    localStringBuffer.append("?shopid=").append(getShop().getInt("ID")).append("&cityid=").append(cityId());
    DPObject localDPObject = getFragment().locationService().location();
    if (localDPObject != null)
    {
      this.latitude = localDPObject.getDouble("Lat");
      this.longitude = localDPObject.getDouble("Lng");
      localStringBuffer.append("&lat=").append(Location.FMT.format(this.latitude)).append("&lng=").append(Location.FMT.format(this.longitude)).append("&locatecityid=").append(localDPObject.getObject("City").getInt("ID"));
    }
    this.mEnjoyReq = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.mEnjoyReq, this);
  }

  public View createTabContent(String paramString)
  {
    return this.mContentView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((this.mEnjoyList == null) || (this.mEnjoyList.length == 0))
    {
      removeAllCells();
      return;
    }
    initEnjoyTabView();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if ((getFragment() == null) || (getShop() == null))
      return;
    requestEnjoyInfo();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mEnjoyReq != null)
    {
      getFragment().mapiService().abort(this.mEnjoyReq, this, true);
      this.mEnjoyReq = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mEnjoyReq == paramMApiRequest)
    {
      this.mEnjoyReq = null;
      dispatchAgentChanged(false);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != null) && (this.mEnjoyReq == paramMApiRequest))
    {
      this.mEnjoyReq = null;
      if ((paramMApiResponse.result() != null) && ((paramMApiResponse.result() instanceof DPObject[])))
      {
        this.mEnjoyList = ((DPObject[])(DPObject[])paramMApiResponse.result());
        int i = 0;
        while (i < this.mEnjoyList.length)
        {
          this.mTagHash.put(Integer.valueOf(i), this.mEnjoyList[i].getString("TabTitle"));
          this.mIndexHash.put(this.mEnjoyList[i].getString("TabTitle"), Integer.valueOf(i));
          i += 1;
        }
        dispatchAgentChanged(false);
      }
    }
  }

  public void onTabChanged(String paramString)
  {
    this.mTabHost.setCurrentTabByTag(paramString);
    if (this.mIndexHash == null)
      return;
    this.indexTab = ((Integer)this.mIndexHash.get(paramString)).intValue();
    this.mEnjoyAdapter.setDataList(this.mEnjoyList[this.indexTab].getArray("List"));
    this.mEnjoyAdapter.notifyDataSetChanged();
    paramString = this.mEnjoyList[this.indexTab].getString("NaviUrl");
    if (!TextUtils.isEmpty(paramString))
    {
      this.footerText.gaUserInfo.index = Integer.valueOf(this.indexTab);
      this.footerText.setTag(paramString);
      this.footerText.setText(this.mEnjoyList[this.indexTab].getString("NaviTitle"));
      this.footerText.setVisibility(0);
      ((NovaActivity)getContext()).addGAView(this.footerText, this.indexTab);
      return;
    }
    this.footerText.setVisibility(8);
  }

  class EnjoyAdapter extends BasicAdapter
  {
    DPObject[] dataList;

    public EnjoyAdapter(DPObject[] arg2)
    {
      Object localObject;
      this.dataList = localObject;
    }

    public int getCount()
    {
      return this.dataList.length;
    }

    public Object getItem(int paramInt)
    {
      return this.dataList[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null)
      {
        paramViewGroup = DistrictEnjoyAgent.this.res.inflate(DistrictEnjoyAgent.this.getContext(), R.layout.shop_item, DistrictEnjoyAgent.this.getParentView(), false);
        paramViewGroup.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
      }
      paramView = (ShopListItem)paramViewGroup;
      paramViewGroup = this.dataList[paramInt];
      if ((DistrictEnjoyAgent.this.isCommunityType()) && (TextUtils.isEmpty(paramViewGroup.getString("DistanceText"))))
        paramView.setShop(paramViewGroup, -1, 0.0D, 0.0D, true);
      while (true)
      {
        paramView.gaUserInfo.shop_id = Integer.valueOf(paramViewGroup.getInt("ID"));
        paramView.gaUserInfo.index = Integer.valueOf(DistrictEnjoyAgent.this.indexTab);
        paramView.setOnClickListener(new View.OnClickListener(paramViewGroup)
        {
          public void onClick(View paramView)
          {
            DistrictEnjoyAgent.this.startActivity("dianping://shopinfo?shopid=" + this.val$shop.getInt("ID"));
          }
        });
        return paramView;
        paramView.setShop(paramViewGroup, -1, DistrictEnjoyAgent.this.latitude, DistrictEnjoyAgent.this.longitude, true);
      }
    }

    public void setDataList(DPObject[] paramArrayOfDPObject)
    {
      this.dataList = paramArrayOfDPObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.district.DistrictEnjoyAgent
 * JD-Core Version:    0.6.0
 */
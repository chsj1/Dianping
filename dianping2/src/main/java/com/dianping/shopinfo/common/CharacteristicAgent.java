package com.dianping.shopinfo.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.MeasuredListView;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.utils.FacilitiesGridAdapter;
import com.dianping.shopinfo.widget.ShopInfoGridView;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class CharacteristicAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String CELL_SPECIAL_ITEM = "7000ShopInfo.";
  private static int NUM_COLUMN = 0;
  public static final String SHOP_EXTRA_INFO = "shopExtraInfo";
  private final View.OnClickListener contentListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      CharacteristicAgent.this.gotoFeatureDetail();
    }
  };
  protected ArrayList<DPObject> mCharacteristicList = new ArrayList();
  private MApiRequest mShopExtraRequest;
  private MApiRequest mShopFeatureRequest;
  protected DPObject shopExtra = new DPObject();

  public CharacteristicAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createCharacteristicAgent()
  {
    int i = 1;
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    Object localObject1 = (LinearLayout)this.res.inflate(getContext(), R.layout.shopinfo_feature_default_layout, getParentView(), false);
    Object localObject2 = (ShopInfoGridView)((LinearLayout)localObject1).findViewById(R.id.id_gridview);
    ArrayList localArrayList;
    if ((this.mCharacteristicList != null) && (this.mCharacteristicList.size() > 0))
    {
      localArrayList = new ArrayList();
      i = 0;
      while ((i < this.mCharacteristicList.size()) && (i < NUM_COLUMN * 2))
      {
        localArrayList.add(this.mCharacteristicList.get(i));
        i += 1;
      }
      ((ShopInfoGridView)localObject2).setVisibility(0);
      ((LinearLayout)localObject1).findViewById(R.id.middle_divder_line).setVisibility(0);
      ((ShopInfoGridView)localObject2).setIsDisplayDivider(false);
      ((ShopInfoGridView)localObject2).setNumColumns(NUM_COLUMN);
      ((ShopInfoGridView)localObject2).setVerticalSpacing(20);
      ((ShopInfoGridView)localObject2).setAdapter(new FacilitiesGridAdapter(getContext(), localArrayList));
      ((ShopInfoGridView)localObject2).setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          CharacteristicAgent.this.gotoFeatureDetail();
        }
      });
      i = 0;
    }
    while (true)
    {
      localObject2 = (MeasuredListView)((LinearLayout)localObject1).findViewById(R.id.id_list);
      if (getShop().getObject("ShopExtraInfo") == null)
        break label583;
      this.shopExtra = this.shopExtra.edit().putObject("ShopExtraInfo", getShop().getObject("ShopExtraInfo")).generate();
      Object localObject3 = getShop().getObject("ShopExtraInfo").getArray("ShopExtraInfos");
      localArrayList = new ArrayList();
      if (localObject3 != null)
      {
        int j = 0;
        while (true)
          if (j < localObject3.length)
          {
            HashMap localHashMap = new HashMap();
            localHashMap.put("Name", localObject3[j].getString("Name"));
            localHashMap.put("Value", localObject3[j].getString("Value"));
            localArrayList.add(localHashMap);
            j += 1;
            continue;
            ((ShopInfoGridView)localObject2).setVisibility(8);
            ((LinearLayout)localObject1).findViewById(R.id.middle_divder_line).setVisibility(8);
            break;
          }
        if ((localArrayList == null) || (localArrayList.size() <= 0))
          break;
        ((MeasuredListView)localObject2).setVisibility(0);
        localObject3 = getContext();
        i = R.layout.shopinfo_feature_item;
        j = R.id.title;
        int k = R.id.sub_title;
        ((MeasuredListView)localObject2).setAdapter(new SimpleAdapter((Context)localObject3, localArrayList, i, new String[] { "Name", "Value" }, new int[] { j, k }));
        ((MeasuredListView)localObject2).setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
          public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
          {
            CharacteristicAgent.this.gotoFeatureDetail();
          }
        });
        i = 0;
        if (i == 0)
          ((NovaRelativeLayout)localShopinfoCommonCell.addContent((View)localObject1, false, null)).setGAString("info", getGAExtra());
        if (this.shopExtra != null)
          break label592;
        localObject1 = null;
        label515: if ((i == 0) || ((localObject1 != null) && (localObject1.length >= 1) && (this.shopExtra.getBoolean("CertificationsShow"))))
          break label606;
      }
    }
    label583: label592: label606: for (i = 1; ; i = 0)
    {
      if (i == 0)
        break label612;
      localShopinfoCommonCell.hideTitle();
      localObject1 = localShopinfoCommonCell;
      if (i != 0)
        localObject1 = null;
      return localObject1;
      ((MeasuredListView)localObject2).setVisibility(8);
      break;
      ((MeasuredListView)localObject2).setVisibility(8);
      break;
      ((MeasuredListView)localObject2).setVisibility(8);
      break;
      localObject1 = this.shopExtra.getArray("Certifications");
      break label515;
    }
    label612: if (getShop().getObject("ShopExtraInfo") == null)
      localObject1 = "商户信息";
    while (true)
    {
      localShopinfoCommonCell.setTitle((String)localObject1, this.contentListener);
      localShopinfoCommonCell.titleLay.setGAString("info", getGAExtra());
      break;
      if (TextUtils.isEmpty(getShop().getObject("ShopExtraInfo").getString("ShopExtraInfosTitle")))
      {
        localObject1 = "商户信息";
        continue;
      }
      localObject1 = getShop().getObject("ShopExtraInfo").getString("ShopExtraInfosTitle");
    }
  }

  private void gotoFeatureDetail()
  {
    Object localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopdetails"));
    ((Intent)localObject).putExtra("shop", getShop());
    ((Intent)localObject).putExtra("shopextra", this.shopExtra);
    ((Intent)localObject).putParcelableArrayListExtra("featurelist", this.mCharacteristicList);
    getFragment().startActivity((Intent)localObject);
    localObject = new ArrayList();
    ((List)localObject).add(new BasicNameValuePair("shopid", shopId() + ""));
    statisticsEvent("shopinfo5", "shopinfo5_info", "", 0, (List)localObject);
    if (isWeddingShopType())
    {
      localObject = new ArrayList();
      ((List)localObject).add(new BasicNameValuePair("shopid", shopId() + ""));
      statisticsEvent("shopinfoq", "shopinfoq_shopprofile", "", 0, (List)localObject);
    }
  }

  private void reqShopExtra()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/mshop/shopextra.bin?shopid=" + shopId()).buildUpon();
    if (location() != null)
      localBuilder.appendQueryParameter("lng", String.valueOf(location().longitude())).appendQueryParameter("lat", String.valueOf(location().latitude()));
    this.mShopExtraRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.mShopExtraRequest, this);
  }

  private void sendRequest()
  {
    this.mShopFeatureRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/shopfeaturetags.bin?shopid=" + shopId(), CacheType.DISABLED);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (CharacteristicAgent.this.mShopFeatureRequest != null)
          CharacteristicAgent.this.getFragment().mapiService().exec(CharacteristicAgent.this.mShopFeatureRequest, CharacteristicAgent.this);
      }
    }
    , 100L);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      do
        return;
      while (getShopStatus() != 0);
      paramBundle = createCharacteristicAgent();
    }
    while (paramBundle == null);
    addCell("7000ShopInfo.", paramBundle, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
    reqShopExtra();
  }

  public void onDestroy()
  {
    if (this.mShopFeatureRequest != null)
    {
      getFragment().mapiService().abort(this.mShopFeatureRequest, this, true);
      this.mShopFeatureRequest = null;
    }
    if (this.mShopExtraRequest != null)
    {
      getFragment().mapiService().abort(this.mShopExtraRequest, this, true);
      this.mShopExtraRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopFeatureRequest)
      this.mShopFeatureRequest = null;
    if (paramMApiRequest == this.mShopExtraRequest)
      this.mShopExtraRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mShopFeatureRequest == paramMApiRequest)
    {
      this.mShopFeatureRequest = null;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject[])))
      {
        this.mCharacteristicList.clear();
        this.mCharacteristicList.addAll(Arrays.asList((DPObject[])(DPObject[])paramMApiResponse.result()));
        dispatchAgentChanged(false);
      }
    }
    label64: 
    do
    {
      do
      {
        do
        {
          break label64;
          do
            return;
          while (this.mShopExtraRequest != paramMApiRequest);
          this.mShopExtraRequest = null;
        }
        while ((paramMApiResponse == null) || (!(paramMApiResponse.result() instanceof DPObject)));
        this.shopExtra = ((DPObject)paramMApiResponse.result());
        setSharedObject("shopExtraInfo", this.shopExtra);
        paramMApiRequest = new Bundle();
        paramMApiRequest.putParcelable("shopExtraInfo", this.shopExtra);
        dispatchAgentChanged(false);
      }
      while (isMallType());
      dispatchAgentChanged("shopinfo/common_mallinfo", paramMApiRequest);
    }
    while (isEducationType());
    dispatchAgentChanged("shopinfo/common_brandstory", paramMApiRequest);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.CharacteristicAgent
 * JD-Core Version:    0.6.0
 */
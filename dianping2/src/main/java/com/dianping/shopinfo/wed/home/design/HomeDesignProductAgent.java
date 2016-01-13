package com.dianping.shopinfo.wed.home.design;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MeasuredGridView;
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
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.Arrays;
import java.util.List;

public class HomeDesignProductAgent extends ShopCellAgent
{
  private static final String API_URL = "http://m.api.dianping.com/wedding/productlist.bin?";
  private static final String CELL_PRODUCT = "0800HomeDesign.01Product";
  private static final int REQUEST_LIMIT = 4;
  private static final String REQUEST_PAGE_NAME = "shop";
  private static final int REQUEST_START_INDEX = 0;
  private static final String TAG = HomeDesignProductAgent.class.getSimpleName();
  private int albumFrameHeight;
  private int albumFrameWidth;
  String categoryDesc;
  private int coverStyleType;
  private MeasuredGridView gridView;
  private PhotoAdapter photoAdapter;
  List<DPObject> photoList;
  private DPObject productInfo;
  MApiRequest request;
  private RequestHandler<MApiRequest, MApiResponse> requestHandler;
  private int screenWidth;
  int shopId;
  int type;
  private int verticalAlbumFrameHeight;
  private int verticalAlbumFrameWidth;

  public HomeDesignProductAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (!isHomeDesignShopType());
    do
    {
      do
        return;
      while (this.productInfo == null);
      localObject = this.productInfo.getArray("List");
    }
    while (localObject.length <= 0);
    this.type = this.productInfo.getInt("Type");
    this.categoryDesc = this.productInfo.getString("CategoryDesc");
    this.coverStyleType = this.productInfo.getInt("CoverStyleType");
    paramBundle = (Bundle)localObject;
    if (localObject.length > 2)
    {
      paramBundle = new DPObject[2];
      i = 0;
      while (i < 2)
      {
        paramBundle[i] = localObject[i];
        i += 1;
      }
    }
    removeAllCells();
    Object localObject = this.res.inflate(getContext(), R.layout.shop_wedding_allproduct, getParentView(), false);
    if (TextUtils.isEmpty(this.categoryDesc))
      this.categoryDesc = "案例";
    ((TextView)((View)localObject).findViewById(R.id.product_window_title)).setText("商户" + this.categoryDesc);
    int i = this.productInfo.getInt("RecordCount");
    ((TextView)((View)localObject).findViewById(R.id.product_window_bottom_text)).setText("查看全部" + i + "个" + this.categoryDesc);
    this.screenWidth = ViewUtils.getScreenWidthPixels(getContext());
    this.albumFrameWidth = (this.screenWidth * 43 / 100);
    this.albumFrameHeight = (this.albumFrameWidth * 210 / 280);
    this.verticalAlbumFrameWidth = this.albumFrameWidth;
    this.verticalAlbumFrameHeight = (this.verticalAlbumFrameWidth * 374 / 280);
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)((View)localObject).findViewById(R.id.product_window_bottom);
    localNovaRelativeLayout.setGAString("shopinfoq_producttab");
    localNovaRelativeLayout.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = HomeDesignProductAgent.this.getGAExtra();
        paramView.shop_id = Integer.valueOf(HomeDesignProductAgent.this.shopId());
        GAHelper.instance().contextStatisticsEvent(HomeDesignProductAgent.this.getContext(), "packageinfo_more", paramView, "tap");
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://homedesignproductlist").buildUpon().appendQueryParameter("id", String.valueOf(HomeDesignProductAgent.this.shopId)).build().toString()));
        paramView.putExtra("shop", HomeDesignProductAgent.this.getShop());
        paramView.putExtra("isEmptySource", false);
        paramView.putExtra("categoryDesc", HomeDesignProductAgent.this.categoryDesc);
        HomeDesignProductAgent.this.getFragment().startActivity(paramView);
      }
    });
    if (this.type != 0)
    {
      if (this.photoAdapter == null)
        this.photoAdapter = new PhotoAdapter();
      this.photoList = Arrays.asList(paramBundle);
      this.gridView = ((MeasuredGridView)((View)localObject).findViewById(R.id.gallery_gridview));
      this.gridView.setAdapter(this.photoAdapter);
      this.photoAdapter.notifyDataSetChanged();
    }
    addCell("0800HomeDesign.01Product", (View)localObject, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!isHomeDesignShopType())
      return;
    paramBundle = getShop();
    if (paramBundle == null)
    {
      Log.e(TAG, "Null shop data. Can not update shop products.");
      return;
    }
    this.shopId = paramBundle.getInt("ID");
    if (this.shopId <= 0)
    {
      Log.e(TAG, "Invalid shop id. Can not update shop products.");
      return;
    }
    paramBundle = new StringBuffer("http://m.api.dianping.com/wedding/productlist.bin?");
    paramBundle.append("shopid=").append(this.shopId).append("&start=").append(0).append("&limit=").append(4).append("&pageName=").append("shop");
    this.request = BasicMApiRequest.mapiGet(paramBundle.toString(), CacheType.DISABLED);
    this.requestHandler = new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        HomeDesignProductAgent.this.request = null;
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (HomeDesignProductAgent.this.request == paramMApiRequest)
        {
          HomeDesignProductAgent.this.request = null;
          HomeDesignProductAgent.access$002(HomeDesignProductAgent.this, (DPObject)paramMApiResponse.result());
          if (HomeDesignProductAgent.this.productInfo != null);
        }
        else
        {
          return;
        }
        HomeDesignProductAgent.this.dispatchAgentChanged(false);
      }
    };
    getFragment().mapiService().exec(this.request, this.requestHandler);
  }

  public void onDestroy()
  {
    if ((this.request != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.request, this.requestHandler, true);
    super.onDestroy();
  }

  public class PhotoAdapter extends BasicAdapter
  {
    public PhotoAdapter()
    {
    }

    private void setOnClickListenerByType(DPObject paramDPObject, View paramView, int paramInt)
    {
      paramView.setOnClickListener(new View.OnClickListener(paramInt, paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = HomeDesignProductAgent.this.getGAExtra();
          paramView.shop_id = Integer.valueOf(HomeDesignProductAgent.this.shopId());
          paramView.index = Integer.valueOf(this.val$position + 1);
          GAHelper.instance().contextStatisticsEvent(HomeDesignProductAgent.this.getContext(), "packageinfo_detail", paramView, "tap");
          int i = this.val$source.getInt("ID");
          paramView = Uri.parse("dianping://homeproductdetail").buildUpon().appendQueryParameter("productid", String.valueOf(i)).appendQueryParameter("cityid", HomeDesignProductAgent.this.cityId() + "").build().toString();
          HomeDesignProductAgent.this.startActivity(paramView);
        }
      });
    }

    private void setViewDetailsByType(DPObject paramDPObject, View paramView)
    {
      Object localObject1 = paramDPObject.getString("DefaultPic");
      Object localObject2 = (NetworkImageView)paramView.findViewById(R.id.img_shop_photo);
      if (HomeDesignProductAgent.this.coverStyleType == 2)
        ((NetworkImageView)localObject2).getLayoutParams().width = HomeDesignProductAgent.this.verticalAlbumFrameWidth;
      for (((NetworkImageView)localObject2).getLayoutParams().height = HomeDesignProductAgent.this.verticalAlbumFrameHeight; ; ((NetworkImageView)localObject2).getLayoutParams().height = HomeDesignProductAgent.this.albumFrameHeight)
      {
        ((NetworkImageView)localObject2).setImage((String)localObject1);
        localObject1 = (TextView)paramView.findViewById(R.id.lay_img_desc_title);
        localObject2 = (TextView)paramView.findViewById(R.id.lay_img_desc_area);
        paramView = (TextView)paramView.findViewById(R.id.lay_img_desc_style);
        String str = paramDPObject.getString("Name");
        int i = paramDPObject.getInt("Area");
        paramDPObject = paramDPObject.getString("Style");
        ((TextView)localObject1).setText(str);
        ((TextView)localObject2).setText(i + "平米");
        paramView.setText(paramDPObject);
        return;
        ((NetworkImageView)localObject2).getLayoutParams().width = HomeDesignProductAgent.this.albumFrameWidth;
      }
    }

    public int getCount()
    {
      if (HomeDesignProductAgent.this.photoList == null)
        return 0;
      return HomeDesignProductAgent.this.photoList.size();
    }

    public Object getItem(int paramInt)
    {
      if ((HomeDesignProductAgent.this.photoList == null) || (HomeDesignProductAgent.this.photoList.size() <= paramInt))
        return null;
      return HomeDesignProductAgent.this.photoList.get(paramInt);
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
        if ((paramView == null) || (paramView.getId() != R.id.item_of_photo_album))
          paramView = HomeDesignProductAgent.this.res.inflate(HomeDesignProductAgent.this.getContext(), R.layout.home_design_product_item, paramViewGroup, false);
        while (true)
        {
          ((NovaLinearLayout)paramView).setGAString("shopinfoq_product");
          setViewDetailsByType((DPObject)localObject, paramView);
          setOnClickListenerByType((DPObject)localObject, paramView, paramInt);
          return paramView;
        }
      }
      if (localObject == ERROR)
        Log.e(HomeDesignProductAgent.TAG, "ERROR IN getView");
      while (true)
      {
        return null;
        if (localObject != LOADING)
          continue;
        Log.e(HomeDesignProductAgent.TAG, "LOADING in getView");
        getLoadingView(paramViewGroup, paramView);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.home.design.HomeDesignProductAgent
 * JD-Core Version:    0.6.0
 */
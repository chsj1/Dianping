package com.dianping.shopinfo.wed.baby;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.base.widget.wed.GridProductAdapter;
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
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;

public class WeddingProductTrvInfoAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String API_URL = "http://m.api.dianping.com/wedding/productlist.bin?";
  private static final String CELL_PRODUCT = "0400Prod.55Product";
  public static final int PRODUCT_PHOTO = 1;
  private static final int REQUEST_AVAILABLE = 1;
  private static final int REQUEST_LIMIT = 4;
  private static final String REQUEST_PAGE_NAME = "productlist";
  private static final int REQUEST_PENDING = 2;
  private static final int REQUEST_START_INDEX = 0;
  static final String TAG = WeddingProductTrvInfoAgent.class.getSimpleName();
  String categoryDesc;
  int coverStyleType;
  private DPObject error;
  private MeasuredGridView gridView;
  boolean isEmptySource = false;
  int layoutModule = -1;
  private GridProductAdapter photoAdapter;
  private DPObject productInfo;
  MApiRequest request;
  private int requestStatus;
  private int shopId;
  int type;

  public WeddingProductTrvInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void fillFourShot(DPObject[] paramArrayOfDPObject, View paramView, int paramInt)
  {
    if (this.type == 0)
      return;
    if (this.photoAdapter == null)
      this.photoAdapter = new GridProductAdapter(getContext(), paramArrayOfDPObject, this.coverStyleType);
    this.gridView = ((MeasuredGridView)paramView.findViewById(R.id.gallery_gridview));
    this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(paramArrayOfDPObject)
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        int i = this.val$source[paramInt].getInt("ID");
        paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://babyproductdetail").buildUpon().appendQueryParameter("productid", String.valueOf(i)).appendQueryParameter("shopid", String.valueOf(WeddingProductTrvInfoAgent.this.shopId())).build().toString()));
        paramAdapterView.putExtra("shop", WeddingProductTrvInfoAgent.this.getShop());
        WeddingProductTrvInfoAgent.this.startActivity(paramAdapterView);
        paramAdapterView = WeddingProductTrvInfoAgent.this.getGAExtra();
        paramAdapterView.index = Integer.valueOf(paramInt);
        GAHelper.instance().contextStatisticsEvent(WeddingProductTrvInfoAgent.this.getContext(), "travel_product_detail", paramAdapterView, "tap");
      }
    });
    this.gridView.setAdapter(this.photoAdapter);
    this.photoAdapter.notifyDataSetChanged();
  }

  private DPObject[] getSubArray(DPObject[] paramArrayOfDPObject, int paramInt)
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

  private void initViewCell(View paramView, int paramInt)
  {
    ((TextView)paramView.findViewById(R.id.product_window_title)).setText("旅游婚纱" + this.categoryDesc);
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)paramView.findViewById(R.id.product_window_bottom);
    localNovaRelativeLayout.setGAString("travel_product_more");
    paramInt = this.productInfo.getInt("RecordCount");
    TextView localTextView = (TextView)paramView.findViewById(R.id.product_window_bottom_text);
    paramView = (ImageView)paramView.findViewById(R.id.product_window_arrow_right);
    if (paramInt == 2)
    {
      localTextView.setVisibility(8);
      paramView.setVisibility(8);
      return;
    }
    localTextView.setText("查看全部" + paramInt + "个产品");
    localNovaRelativeLayout.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://babyproductlist").buildUpon().appendQueryParameter("id", String.valueOf(WeddingProductTrvInfoAgent.this.shopId)).appendQueryParameter("productCategoryId", "1632").build().toString()));
        paramView.putExtra("isEmptySource", WeddingProductTrvInfoAgent.this.isEmptySource);
        paramView.putExtra("shop", WeddingProductTrvInfoAgent.this.getShop());
        paramView.putExtra("categoryDesc", WeddingProductTrvInfoAgent.this.categoryDesc);
        WeddingProductTrvInfoAgent.this.getFragment().startActivity(paramView);
      }
    });
  }

  private void sendRequest()
  {
    if ((this.requestStatus == 2) || (this.shopId <= 0))
      return;
    this.requestStatus = 2;
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/wedding/productlist.bin?");
    localStringBuffer.append("shopid=").append(this.shopId + "").append("&start=").append(0).append("&limit=").append(4).append("&pageName=").append("productlist").append("&productcategoryid=").append(1632);
    this.request = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (!isWeddingShopType());
    while (true)
    {
      return;
      if (this.productInfo != null)
        break;
      if (this.error != null)
        continue;
      paramBundle = getShop();
      if (paramBundle == null)
        continue;
      this.shopId = paramBundle.getInt("ID");
      if (this.shopId <= 0)
        continue;
      sendRequest();
      return;
    }
    this.type = this.productInfo.getInt("Type");
    this.categoryDesc = "产品";
    this.layoutModule = this.type;
    Object localObject = this.productInfo.getArray("List");
    this.coverStyleType = this.productInfo.getInt("CoverStyleType");
    if ((localObject == null) || (localObject.length < 2))
    {
      removeAllCells();
      return;
    }
    if (localObject.length >= 4)
      paramBundle = getSubArray(localObject, 4);
    while (true)
    {
      removeAllCells();
      localObject = this.res.inflate(getContext(), R.layout.shop_wedding_allproduct, getParentView(), false);
      initViewCell((View)localObject, this.layoutModule);
      fillFourShot(paramBundle, (View)localObject, this.layoutModule);
      addCell("0400Prod.55Product", (View)localObject, 0);
      return;
      if (localObject.length != 3)
      {
        paramBundle = (Bundle)localObject;
        if (localObject.length != 2)
          continue;
      }
      paramBundle = getSubArray(localObject, 2);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!isWeddingShopType())
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
    sendRequest();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((this.request != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.request, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    if ((paramMApiResponse.error() instanceof DPObject));
    for (this.error = ((DPObject)paramMApiResponse.error()); ; this.error = new DPObject())
    {
      this.requestStatus = 1;
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.productInfo = ((DPObject)paramMApiResponse.result());
    this.requestStatus = 1;
    if (this.productInfo == null)
      return;
    this.error = null;
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingProductTrvInfoAgent
 * JD-Core Version:    0.6.0
 */
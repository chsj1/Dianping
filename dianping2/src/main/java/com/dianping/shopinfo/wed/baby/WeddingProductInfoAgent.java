package com.dianping.shopinfo.wed.baby;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class WeddingProductInfoAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final String API_URL = "http://m.api.dianping.com/wedding/productrecommend.bin?";
  private static final String CELL_PRODUCT = "0300Prod.55Product";
  public static final int PRODUCT_PHOTO = 1;
  private static final int REQUEST_AVAILABLE = 1;
  private static final int REQUEST_LIMIT = 4;
  private static final String REQUEST_PAGE_NAME = "shop";
  private static final int REQUEST_PENDING = 2;
  private static final int REQUEST_START_INDEX = 0;
  static final String TAG = WeddingProductInfoAgent.class.getSimpleName();
  String categoryDesc;
  int coverStyleType;
  private DPObject error;
  private MeasuredGridView gridView;
  private boolean isCrawlProduct = false;
  boolean isEmptySource = false;
  int layoutModule = -1;
  private GridProductAdapter photoAdapter;
  private DPObject productInfo;
  MApiRequest request;
  private int requestStatus;
  private int shopId;
  int type;

  public WeddingProductInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void fillFourShot(DPObject[] paramArrayOfDPObject, View paramView, int paramInt)
  {
    if ((this.type == 0) && (!this.isCrawlProduct))
      return;
    if (this.photoAdapter == null)
      this.photoAdapter = new GridProductAdapter(getContext(), paramArrayOfDPObject, this.coverStyleType);
    this.gridView = ((MeasuredGridView)paramView.findViewById(R.id.gallery_gridview));
    this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(paramArrayOfDPObject)
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        int i = this.val$source[paramInt].getInt("ID");
        paramView = new ArrayList();
        paramView.add(new BasicNameValuePair("shopid", WeddingProductInfoAgent.this.shopId() + ""));
        if (WeddingProductInfoAgent.this.isCrawlProduct)
        {
          paramAdapterView = Uri.parse("dianping://babycrawlproductdetail").buildUpon().appendQueryParameter("productid", String.valueOf(i)).appendQueryParameter("shopid", String.valueOf(WeddingProductInfoAgent.this.shopId())).build().toString();
          WeddingProductInfoAgent.this.statisticsEvent("shopinfoq", "shopinfoq_noproduct", String.valueOf(paramInt), 0, paramView);
        }
        while (true)
        {
          paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse(paramAdapterView));
          paramAdapterView.putExtra("shop", WeddingProductInfoAgent.this.getShop());
          WeddingProductInfoAgent.this.startActivity(paramAdapterView);
          return;
          paramAdapterView = Uri.parse("dianping://babyproductdetail").buildUpon().appendQueryParameter("productid", String.valueOf(i)).appendQueryParameter("shopid", String.valueOf(WeddingProductInfoAgent.this.shopId())).build().toString();
          paramView = WeddingProductInfoAgent.this.getGAExtra();
          paramView.index = Integer.valueOf(paramInt);
          GAHelper.instance().contextStatisticsEvent(WeddingProductInfoAgent.this.getContext(), "product_detail", paramView, "tap");
        }
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
    if (TextUtils.isEmpty(this.categoryDesc))
      this.categoryDesc = "产品";
    Object localObject = (TextView)paramView.findViewById(R.id.product_window_title);
    if (this.isCrawlProduct)
      ((TextView)localObject).setText("产品展示");
    TextView localTextView;
    while (true)
    {
      localObject = (NovaRelativeLayout)paramView.findViewById(R.id.product_window_bottom);
      ((NovaRelativeLayout)localObject).setGAString("product_more");
      paramInt = this.productInfo.getInt("RecordCount");
      localTextView = (TextView)paramView.findViewById(R.id.product_window_bottom_text);
      if (!this.isCrawlProduct)
        break;
      localTextView.setVisibility(8);
      paramView.findViewById(R.id.product_window_arrow_right).setVisibility(8);
      return;
      ((TextView)localObject).setText("本店" + this.categoryDesc);
    }
    if (paramInt <= 4)
    {
      ((NovaRelativeLayout)localObject).setVisibility(8);
      return;
    }
    localTextView.setText("查看全部" + paramInt + "个" + this.categoryDesc);
    ((NovaRelativeLayout)localObject).setOnClickListener(this);
    ((NovaRelativeLayout)localObject).setTag("ALLPRODUCT");
  }

  private void sendRequest()
  {
    if (this.requestStatus == 2)
      return;
    this.requestStatus = 2;
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/wedding/productrecommend.bin?");
    localStringBuffer.append("shopid=").append(this.shopId).append("&start=").append(0).append("&limit=").append(4).append("&pageName=").append("shop");
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
    this.categoryDesc = this.productInfo.getString("CategoryDesc");
    this.layoutModule = this.type;
    Object localObject = this.productInfo.getArray("List");
    this.coverStyleType = this.productInfo.getInt("CoverStyleType");
    if ((isSpecBabyType()) && (localObject != null) && (localObject.length > 0))
    {
      localObject = this.res.inflate(getContext(), R.layout.shop_wedding_cell, getParentView(), false);
      TextView localTextView = (TextView)((View)localObject).findViewById(R.id.cell_text);
      StringBuilder localStringBuilder = new StringBuilder().append("查看本店").append(this.productInfo.getInt("RecordCount")).append("个");
      if (TextUtils.isEmpty(this.categoryDesc));
      for (paramBundle = "产品"; ; paramBundle = this.categoryDesc)
      {
        localTextView.setText(paramBundle);
        ((View)localObject).setTag("ALLPRODUCT");
        ((View)localObject).setOnClickListener(this);
        addCell("0300Prod.55Product", (View)localObject, 0);
        return;
      }
    }
    if (localObject != null)
    {
      paramBundle = (Bundle)localObject;
      if (localObject.length != 0);
    }
    else
    {
      paramBundle = this.productInfo.getArray("CrawlProductList");
      if ((paramBundle == null) || (paramBundle.length == 0))
      {
        removeAllCells();
        return;
      }
      this.isCrawlProduct = true;
    }
    localObject = paramBundle;
    if (paramBundle.length > 4)
      localObject = getSubArray(paramBundle, 4);
    removeAllCells();
    paramBundle = this.res.inflate(getContext(), R.layout.shop_wedding_allproduct, getParentView(), false);
    initViewCell(paramBundle, this.layoutModule);
    fillFourShot(localObject, paramBundle, this.layoutModule);
    addCell("0300Prod.55Product", paramBundle, 0);
  }

  public void onClick(View paramView)
  {
    if (paramView.getTag().equals("ALLPRODUCT"))
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://babyproductlist").buildUpon().appendQueryParameter("id", String.valueOf(this.shopId)).build().toString()));
      paramView.putExtra("isEmptySource", this.isEmptySource);
      paramView.putExtra("shop", getShop());
      paramView.putExtra("categoryDesc", this.categoryDesc);
      getFragment().startActivity(paramView);
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
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingProductInfoAgent
 * JD-Core Version:    0.6.0
 */
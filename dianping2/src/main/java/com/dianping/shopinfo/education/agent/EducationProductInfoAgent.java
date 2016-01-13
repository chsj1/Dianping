package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.dianping.shopinfo.education.widget.EducationGridProductAdapter;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class EducationProductInfoAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final String API_URL = "http://mapi.dianping.com/edu/eduproductrecommend.bin?";
  private static final String CELL_PRODUCT = "0300Basic.01product";
  public static final int PRODUCT_PHOTO = 1;
  private static final int REQUEST_AVAILABLE = 1;
  private static final int REQUEST_LIMIT = 4;
  private static final String REQUEST_PAGE_NAME = "shop";
  private static final int REQUEST_PENDING = 2;
  private static final int REQUEST_START_INDEX = 0;
  static final String TAG = EducationProductInfoAgent.class.getSimpleName();
  String categoryDesc;
  int coverStyleType;
  private DPObject error;
  private MeasuredGridView gridView;
  boolean isEmptySource = false;
  int layoutModule = -1;
  private EducationGridProductAdapter photoAdapter;
  private DPObject productInfo;
  MApiRequest request;
  private int sRequestStatus;
  private int shopId;
  int type;

  public EducationProductInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void fillFourShot(DPObject[] paramArrayOfDPObject, View paramView, int paramInt)
  {
    if (this.type == 0)
      return;
    if (this.photoAdapter == null)
      this.photoAdapter = new EducationGridProductAdapter(getContext(), paramArrayOfDPObject, this.coverStyleType);
    this.gridView = ((MeasuredGridView)paramView.findViewById(R.id.gallery_gridview));
    this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(paramArrayOfDPObject)
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        paramAdapterView = this.val$source[paramInt].getString("ProductDetailPageUrl");
        if (!TextUtils.isEmpty(paramAdapterView))
        {
          paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse(paramAdapterView));
          paramAdapterView.putExtra("shop", EducationProductInfoAgent.this.getShop());
          EducationProductInfoAgent.this.getFragment().startActivity(paramAdapterView);
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
    ((TextView)paramView.findViewById(R.id.product_window_title)).setText("本店" + this.categoryDesc);
    RelativeLayout localRelativeLayout = (RelativeLayout)paramView.findViewById(R.id.product_window_bottom);
    paramInt = this.productInfo.getInt("RecordCount");
    TextView localTextView = (TextView)paramView.findViewById(R.id.product_window_bottom_text);
    paramView = (ImageView)paramView.findViewById(R.id.product_window_arrow_right);
    if (paramInt <= 4)
    {
      localTextView.setVisibility(4);
      paramView.setVisibility(4);
      return;
    }
    localTextView.setText("查看全部" + paramInt + "个" + this.categoryDesc);
    localRelativeLayout.setOnClickListener(this);
    ((NovaRelativeLayout)localRelativeLayout).setGAString("edu_product_more", getGAExtra());
    localRelativeLayout.setTag("ALLPRODUCT");
  }

  private void sendRequest()
  {
    if (this.sRequestStatus == 2)
      return;
    this.sRequestStatus = 2;
    StringBuffer localStringBuffer = new StringBuffer("http://mapi.dianping.com/edu/eduproductrecommend.bin?");
    localStringBuffer.append("shopid=").append(this.shopId).append("&start=").append(0).append("&limit=").append(4).append("&pageName=").append("shop");
    this.request = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.productInfo == null)
    {
      if (this.error != null);
      do
      {
        do
        {
          return;
          paramBundle = getShop();
        }
        while (paramBundle == null);
        this.shopId = paramBundle.getInt("ID");
      }
      while (this.shopId <= 0);
      sendRequest();
      return;
    }
    this.type = this.productInfo.getInt("Type");
    this.categoryDesc = this.productInfo.getString("CategoryDesc");
    this.layoutModule = this.type;
    Object localObject = this.productInfo.getArray("List");
    this.coverStyleType = this.productInfo.getInt("CoverStyleType");
    if ((localObject == null) || (localObject.length == 0))
    {
      removeAllCells();
      return;
    }
    paramBundle = (Bundle)localObject;
    if (localObject.length > 4)
      paramBundle = getSubArray(localObject, 4);
    removeAllCells();
    localObject = this.res.inflate(getContext(), R.layout.shop_education_allproduct, getParentView(), false);
    initViewCell((View)localObject, this.layoutModule);
    fillFourShot(paramBundle, (View)localObject, this.layoutModule);
    addCell("0300Basic.01product", (View)localObject, 0);
  }

  public void onClick(View paramView)
  {
    if ((!paramView.getTag().equals("ALLPRODUCT")) || (this.productInfo == null))
      return;
    paramView = new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse(this.productInfo.getString("ProductListPageUrl")).buildUpon().appendQueryParameter("id", String.valueOf(this.shopId)).build().toString()));
    paramView.putExtra("isEmptySource", this.isEmptySource);
    paramView.putExtra("shop", getShop());
    paramView.putExtra("categoryDesc", this.categoryDesc);
    getFragment().startActivity(paramView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!isEducationType())
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
      this.sRequestStatus = 1;
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.productInfo = ((DPObject)paramMApiResponse.result());
    this.sRequestStatus = 1;
    if (this.productInfo == null)
      return;
    this.error = null;
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.EducationProductInfoAgent
 * JD-Core Version:    0.6.0
 */
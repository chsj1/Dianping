package com.dianping.shopinfo.verticalchannel.book;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ColorBorderTextView;
import com.dianping.base.widget.ExpandableHeightGridView;
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
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.List;

public class ProductAgent extends ShopCellAgent
{
  private static final String API_URL = "http://m.api.dianping.com/vc/book/productlist.vcb?";
  private static final String CELL_VERTICAL_CHANNEL_PRODUCT = "0700Product.10ProductList";
  private static final String RMB = "￥";
  private static final String TAG = ProductAgent.class.getSimpleName();
  protected int albumFrameHeight;
  protected int categoryId;
  protected DPObject mProductInfo;
  private View mProductListView;
  protected MApiRequest mRequest;
  private RequestHandler<MApiRequest, MApiResponse> mRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      ProductAgent.this.mRequest = null;
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (ProductAgent.this.mRequest != paramMApiRequest);
      do
      {
        return;
        ProductAgent.this.mProductInfo = ((DPObject)paramMApiResponse.result());
      }
      while (ProductAgent.this.mProductInfo == null);
      ProductAgent.this.dispatchAgentChanged(false);
    }
  };
  private ProductAdapter productAdapter;
  protected View.OnClickListener productDetailListerner = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = paramView.findViewById(R.id.img_product_photo);
      if (paramView == null);
      do
      {
        return;
        paramView = paramView.getTag().toString();
      }
      while (TextUtils.isEmpty(paramView));
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
      ProductAgent.this.startActivity(paramView);
    }
  };
  protected List<DPObject> productList = new ArrayList();
  protected int shopId;
  private View.OnClickListener viewAllListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = paramView.getTag().toString();
      if (TextUtils.isEmpty(paramView))
        return;
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
      ProductAgent.this.startActivity(paramView);
    }
  };

  public ProductAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void calImageHeight()
  {
    this.albumFrameHeight = ((ViewUtils.getScreenWidthPixels(getContext()) - 15 - 12 - 10) / 2 * 210 / 280);
  }

  private List<DPObject> convertProductList()
  {
    DPObject[] arrayOfDPObject = this.mProductInfo.getArray("List");
    if ((arrayOfDPObject == null) || (arrayOfDPObject.length == 0))
      localObject = null;
    ArrayList localArrayList;
    int j;
    int i;
    do
    {
      return localObject;
      localArrayList = new ArrayList();
      j = arrayOfDPObject.length;
      i = 0;
      localObject = localArrayList;
    }
    while (i >= j);
    Object localObject = arrayOfDPObject[i];
    if (localObject == null);
    while (true)
    {
      i += 1;
      break;
      localArrayList.add(localObject);
    }
  }

  private ProductTitleHolder createProductTitleHolder()
  {
    if (this.mProductInfo == null);
    String str1;
    String str2;
    int i;
    do
    {
      do
      {
        return null;
        str1 = this.mProductInfo.getString("Title");
        str2 = this.mProductInfo.getString("Action");
      }
      while ((TextUtils.isEmpty(str1)) || (TextUtils.isEmpty(str2)));
      i = this.mProductInfo.getInt("RecordCount");
    }
    while (i == 0);
    return new ProductTitleHolder(str1, str2, i);
  }

  private void extractShopInfo()
  {
    DPObject localDPObject = getShop();
    if (localDPObject == null)
    {
      Log.e(TAG, "Null shop data. Can not update shop info.");
      return;
    }
    this.shopId = localDPObject.getInt("ID");
    this.categoryId = localDPObject.getInt("CategoryID");
  }

  private void renderProductTitleGroup(ProductTitleHolder paramProductTitleHolder)
  {
    ((TextView)this.mProductListView.findViewById(R.id.title_content_desc)).setText(paramProductTitleHolder.title);
    ((TextView)this.mProductListView.findViewById(R.id.title_content_num)).setText("(" + paramProductTitleHolder.totalSize + ")");
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.mProductListView.findViewById(R.id.product_group_title);
    localNovaRelativeLayout.gaUserInfo.shop_id = Integer.valueOf(this.shopId);
    localNovaRelativeLayout.gaUserInfo.category_id = Integer.valueOf(this.categoryId);
    localNovaRelativeLayout.setGAString("showinfo5_newproduct_more");
    localNovaRelativeLayout.setTag(paramProductTitleHolder.action);
    localNovaRelativeLayout.setOnClickListener(this.viewAllListener);
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/vc/book/productlist.vcb?").buildUpon();
    localBuilder.appendQueryParameter("shopid", this.shopId + "");
    this.mRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this.mRequestHandler);
  }

  public View getView()
  {
    return this.mProductListView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (this.mProductListView != null);
    do
    {
      do
      {
        return;
        paramBundle = createProductTitleHolder();
      }
      while (paramBundle == null);
      this.productList = convertProductList();
    }
    while ((this.productList == null) || (this.productList.size() == 0));
    this.mProductListView = this.res.inflate(getContext(), R.layout.shop_vertical_channel_product_cell, getParentView(), false);
    renderProductTitleGroup(paramBundle);
    this.productAdapter = new ProductAdapter();
    paramBundle = (ExpandableHeightGridView)this.mProductListView.findViewById(R.id.gridview_product_details);
    paramBundle.setExpanded(true);
    paramBundle.setAdapter(this.productAdapter);
    addCell("0700Product.10ProductList", this.mProductListView);
    paramBundle = (NovaLinearLayout)this.mProductListView;
    paramBundle.gaUserInfo.shop_id = Integer.valueOf(this.shopId);
    paramBundle.gaUserInfo.category_id = Integer.valueOf(this.categoryId);
    paramBundle.setGAString("shopinfo5_newproduct");
    ((DPActivity)getFragment().getActivity()).addGAView(paramBundle, -1);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    extractShopInfo();
    if (this.shopId <= 0)
    {
      Log.e(TAG, "Invalid shop id. Can not update shop info.");
      return;
    }
    calImageHeight();
    sendRequest();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((this.mRequest != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.mRequest, this.mRequestHandler, true);
  }

  class ProductAdapter extends BasicAdapter
  {
    ProductAdapter()
    {
    }

    private ColorBorderTextView buildProductPromoView(String paramString)
    {
      ColorBorderTextView localColorBorderTextView = new ColorBorderTextView(ProductAgent.this.getContext());
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
      localLayoutParams.setMargins(5, 0, 0, 0);
      localColorBorderTextView.setLayoutParams(localLayoutParams);
      localColorBorderTextView.setBackgroundResource(R.drawable.background_round_textview_lightred);
      localColorBorderTextView.setEllipsize(TextUtils.TruncateAt.END);
      localColorBorderTextView.setGravity(16);
      localColorBorderTextView.setSingleLine(true);
      localColorBorderTextView.setTextColor(ProductAgent.this.getContext().getResources().getColor(R.color.light_red));
      localColorBorderTextView.setTextSize(2, 11.0F);
      localColorBorderTextView.setText(paramString);
      return localColorBorderTextView;
    }

    private boolean renderGridItem(int paramInt, NetworkImageView paramNetworkImageView, ProductAgent.ProductViewHolder paramProductViewHolder)
    {
      DPObject localDPObject = (DPObject)getItem(paramInt);
      String str1 = localDPObject.getString("ImgUrl");
      String str2 = localDPObject.getString("Action");
      String str3 = localDPObject.getString("Title");
      String str4 = localDPObject.getString("Price");
      if ((TextUtils.isEmpty(str1)) || (TextUtils.isEmpty(str2)) || (TextUtils.isEmpty(str3)) || (TextUtils.isEmpty(str4)))
        return false;
      paramNetworkImageView.getLayoutParams().height = ProductAgent.this.albumFrameHeight;
      paramNetworkImageView.setImage(str1);
      paramNetworkImageView.setTag(str2);
      paramProductViewHolder.productTitleView.setText(str3);
      renderPrice(paramProductViewHolder.productPriceView, str4);
      paramNetworkImageView = localDPObject.getString("OriginPrice");
      renderOriginPrice(paramProductViewHolder.productOriginPriceView, paramNetworkImageView);
      paramNetworkImageView = localDPObject.getStringArray("PromoList");
      if (paramNetworkImageView != null)
      {
        int i = paramNetworkImageView.length;
        paramInt = 0;
        while (paramInt < i)
        {
          localDPObject = paramNetworkImageView[paramInt];
          paramProductViewHolder.productPromosLayout.addView(buildProductPromoView(localDPObject));
          paramInt += 1;
        }
      }
      return true;
    }

    private void renderOriginPrice(TextView paramTextView, String paramString)
    {
      if (TextUtils.isEmpty(paramString))
        return;
      paramString = new SpannableString("￥" + paramString);
      paramString.setSpan(new StrikethroughSpan(), 1, paramString.length(), 33);
      paramString.setSpan(new AbsoluteSizeSpan(ProductAgent.this.res.getDimensionPixelSize(R.dimen.text_size_hint)), 0, paramString.length(), 33);
      paramString.setSpan(new ForegroundColorSpan(ProductAgent.this.getResources().getColor(R.color.light_gray)), 0, paramString.length(), 33);
      paramTextView.setText(paramString);
    }

    private void renderPrice(TextView paramTextView, String paramString)
    {
      paramString = new SpannableString("￥" + paramString);
      paramString.setSpan(new AbsoluteSizeSpan(16, true), 0, 1, 33);
      paramTextView.setText(paramString);
    }

    public int getCount()
    {
      if (ProductAgent.this.productList != null)
        return ProductAgent.this.productList.size();
      return 0;
    }

    public Object getItem(int paramInt)
    {
      if ((ProductAgent.this.productList == null) || (ProductAgent.this.productList.size() <= paramInt))
        return null;
      return ProductAgent.this.productList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      NetworkImageView localNetworkImageView;
      if (paramView == null)
      {
        ProductAgent.ProductViewHolder localProductViewHolder = new ProductAgent.ProductViewHolder(ProductAgent.this);
        paramView = ((LayoutInflater)(LayoutInflater)ProductAgent.this.getContext().getSystemService("layout_inflater")).inflate(R.layout.vertical_channel_product_item, paramViewGroup, false);
        paramViewGroup = (NovaRelativeLayout)paramView;
        paramViewGroup.gaUserInfo.shop_id = Integer.valueOf(ProductAgent.this.shopId);
        paramViewGroup.gaUserInfo.category_id = Integer.valueOf(ProductAgent.this.categoryId);
        paramViewGroup.setGAString("showinfo5_newproduct_detail");
        localNetworkImageView = (NetworkImageView)paramView.findViewById(R.id.img_product_photo);
        localProductViewHolder.productPhotoView = localNetworkImageView;
        localProductViewHolder.productTitleView = ((TextView)paramView.findViewById(R.id.text_product_title));
        localProductViewHolder.productPriceView = ((TextView)paramView.findViewById(R.id.text_product_price));
        localProductViewHolder.productPromosLayout = ((LinearLayout)paramView.findViewById(R.id.container_product_promos));
        localProductViewHolder.productOriginPriceView = ((TextView)paramView.findViewById(R.id.text_product_origin_price));
        paramView.setTag(localProductViewHolder);
        paramViewGroup = localProductViewHolder;
      }
      while (!renderGridItem(paramInt, localNetworkImageView, paramViewGroup))
      {
        return null;
        paramViewGroup = (ProductAgent.ProductViewHolder)paramView.getTag();
        localNetworkImageView = paramViewGroup.productPhotoView;
        paramViewGroup.productPromosLayout.removeAllViews();
      }
      paramView.setOnClickListener(ProductAgent.this.productDetailListerner);
      return paramView;
    }
  }

  class ProductTitleHolder
  {
    private String action;
    private String title;
    private int totalSize;

    public ProductTitleHolder(String paramString1, String paramInt, int arg4)
    {
      this.title = paramString1;
      this.action = paramInt;
      int i;
      this.totalSize = i;
    }
  }

  class ProductViewHolder
  {
    protected TextView productOriginPriceView;
    protected NetworkImageView productPhotoView;
    protected TextView productPriceView;
    protected LinearLayout productPromosLayout;
    protected TextView productTitleView;

    ProductViewHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.verticalchannel.book.ProductAgent
 * JD-Core Version:    0.6.0
 */
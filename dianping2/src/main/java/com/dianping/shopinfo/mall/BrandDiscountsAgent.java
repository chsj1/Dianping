package com.dianping.shopinfo.mall;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.adapter.CustomGridViewAdapter;
import com.dianping.base.widget.CustomGridView;
import com.dianping.base.widget.CustomGridView.OnItemClickListener;
import com.dianping.base.widget.NetworkThumbView;
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
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaFrameLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BrandDiscountsAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, CustomGridView.OnItemClickListener
{
  private static final String CELL_BRANDDISCOUNTS = "0620Basic.50BrandDiscounts";
  private static final Integer[] COLORS = { Integer.valueOf(-10301511), Integer.valueOf(-33672), Integer.valueOf(-1079669), Integer.valueOf(-14472), Integer.valueOf(-83617), Integer.valueOf(-8007522) };
  ArrayList<DPObject> brands;
  DPObject error;
  DPObject preferentialShopList;
  MApiRequest request;

  public BrandDiscountsAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createBrandDiscountsView()
  {
    Object localObject;
    if (this.preferentialShopList == null)
      localObject = null;
    ShopinfoCommonCell localShopinfoCommonCell;
    int i;
    do
    {
      return localObject;
      localObject = this.preferentialShopList.getArray("Shops");
      if ((localObject == null) || (localObject.length < 3))
        return null;
      localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
      localObject = this.preferentialShopList.getString("Title");
      i = this.preferentialShopList.getInt("Count");
      View localView = this.res.inflate(getContext(), R.layout.brand_discount_container, getParentView(), false);
      CustomGridView localCustomGridView = (CustomGridView)localView.findViewById(R.id.brand_grid);
      localCustomGridView.setVerticalDivider(this.res.getDrawable(R.drawable.tuan_home_divider_vertical));
      localCustomGridView.setHorizontalDivider(this.res.getDrawable(R.drawable.tuan_home_divider));
      localCustomGridView.setOnItemClickListener(this);
      localCustomGridView.setEndHorizontalDivider(null);
      localCustomGridView.setAdapter(new BrandGridAdapter());
      localShopinfoCommonCell.addContent(localView, false, null);
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        localShopinfoCommonCell.setTitle((String)localObject, new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = BrandDiscountsAgent.this.preferentialShopList.getString("Url");
            if (!TextUtils.isEmpty(paramView))
            {
              paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
              BrandDiscountsAgent.this.getFragment().startActivity(paramView);
            }
          }
        });
        localShopinfoCommonCell.titleLay.setGAString("inmall_sales_more", getGAExtra());
      }
      localObject = localShopinfoCommonCell;
    }
    while (i <= 0);
    localShopinfoCommonCell.setSubTitle("(" + i + ")");
    return (View)localShopinfoCommonCell;
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/getshoppingpromoshoplist.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", "" + shopId());
    localBuilder.appendQueryParameter("cityid", "" + cityId());
    this.request = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
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
      paramBundle = createBrandDiscountsView();
    }
    while (paramBundle == null);
    addCell("0620Basic.50BrandDiscounts", paramBundle, "inmall_sales", 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.preferentialShopList = ((DPObject)paramBundle.getParcelable("preferentialShopList"));
      this.error = ((DPObject)paramBundle.getParcelable("error"));
    }
    if ((this.preferentialShopList == null) && (this.error == null))
      sendRequest();
  }

  public void onDestroy()
  {
    if (this.request != null)
    {
      getFragment().mapiService().abort(this.request, this, true);
      this.request = null;
    }
    super.onDestroy();
  }

  public void onItemClick(CustomGridView paramCustomGridView, View paramView, int paramInt, long paramLong)
  {
    paramCustomGridView = (DPObject)this.brands.get(paramInt);
    if (paramCustomGridView == null);
    do
    {
      return;
      paramCustomGridView = paramCustomGridView.getString("ShopUrl");
    }
    while (TextUtils.isEmpty(paramCustomGridView));
    paramCustomGridView = new Intent("android.intent.action.VIEW", Uri.parse(paramCustomGridView));
    getFragment().startActivity(paramCustomGridView);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    if ((paramMApiResponse.error() instanceof DPObject));
    for (this.error = ((DPObject)paramMApiResponse.error()); ; this.error = new DPObject())
    {
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.preferentialShopList = ((DPObject)paramMApiResponse.result());
    if (this.preferentialShopList.getArray("Shops") != null)
      this.brands = new ArrayList(Arrays.asList(this.preferentialShopList.getArray("Shops")));
    this.error = null;
    dispatchAgentChanged(false);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("preferentialShopList", this.preferentialShopList);
    localBundle.putParcelable("error", this.error);
    return localBundle;
  }

  class BrandGridAdapter extends CustomGridViewAdapter
  {
    private ArrayList<Integer> colors = new ArrayList();
    private Random random = new Random();

    public BrandGridAdapter()
    {
      this.colors.addAll(Arrays.asList(BrandDiscountsAgent.COLORS));
    }

    public int getColumnCount()
    {
      return 3;
    }

    public int getCount()
    {
      int i = 3;
      if ((BrandDiscountsAgent.this.brands == null) || (BrandDiscountsAgent.this.brands.size() < 3))
        i = 0;
      do
        return i;
      while (BrandDiscountsAgent.this.brands.size() < 6);
      return 6;
    }

    public Object getItem(int paramInt)
    {
      return BrandDiscountsAgent.this.brands.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getItemView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = (DPObject)getItem(paramInt);
      NovaFrameLayout localNovaFrameLayout = (NovaFrameLayout)BrandDiscountsAgent.this.res.inflate(BrandDiscountsAgent.this.getContext(), R.layout.brand_discount_item, paramViewGroup, false);
      localNovaFrameLayout.setGAString("inmall_sales_single", "", paramInt);
      ((TextView)localNovaFrameLayout.findViewById(R.id.title)).setText(paramView.getString("ShopName"));
      paramViewGroup = (TextView)localNovaFrameLayout.findViewById(R.id.subtitle);
      Object localObject1 = (TextView)localNovaFrameLayout.findViewById(R.id.promo_tips);
      Object localObject2 = paramView.getString("Discount");
      String str = paramView.getString("DiscountForDeal");
      if ((str != null) && (str.trim().length() > 0))
      {
        paramViewGroup.setVisibility(8);
        ((TextView)localObject1).setText(str);
      }
      while (true)
      {
        localObject1 = (NetworkThumbView)localNovaFrameLayout.findViewById(R.id.brand_pic);
        localObject2 = (TextView)localNovaFrameLayout.findViewById(R.id.brand_type);
        if (!TextUtils.isEmpty(paramView.getString("ShopPic")))
          break;
        paramViewGroup = paramView.getString("CategoryName");
        if (!TextUtils.isEmpty(paramViewGroup))
        {
          paramView = paramViewGroup;
          if (paramViewGroup.length() > 5)
            paramView = paramViewGroup.substring(0, 5);
          ((TextView)localObject2).setText(paramView);
          paramView = new GradientDrawable();
          if (this.colors.size() > 0)
          {
            paramInt = this.random.nextInt(this.colors.size());
            paramView.setColor(((Integer)this.colors.get(paramInt)).intValue());
            this.colors.remove(paramInt);
          }
          paramView.setCornerRadius(3.0F);
          ((TextView)localObject2).setBackgroundDrawable(paramView);
          ((NetworkThumbView)localObject1).setVisibility(8);
          ((TextView)localObject2).setVisibility(0);
        }
        return localNovaFrameLayout;
        if ((localObject2 != null) && (((String)localObject2).trim().length() > 0))
        {
          ((TextView)localObject1).setVisibility(8);
          paramViewGroup.setText((CharSequence)localObject2);
          continue;
        }
        paramViewGroup.setVisibility(8);
        ((TextView)localObject1).setVisibility(8);
      }
      ((NetworkThumbView)localObject1).setImage(paramView.getString("ShopPic"));
      ((NetworkThumbView)localObject1).setVisibility(0);
      ((TextView)localObject2).setVisibility(8);
      return (View)(View)localNovaFrameLayout;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.mall.BrandDiscountsAgent
 * JD-Core Version:    0.6.0
 */
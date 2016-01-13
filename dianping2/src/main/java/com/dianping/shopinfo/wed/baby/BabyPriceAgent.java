package com.dianping.shopinfo.wed.baby;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.dianping.shopinfo.wed.baby.widget.BabyPriceAdapter;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class BabyPriceAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private final String CELL_PRICE = "2900BabyPrice.";
  private final String REQUEST_URL = "http://m.api.dianping.com/wedding/productpricelist.bin?";
  private BabyPriceAdapter babyPriceAdapter;
  private RelativeLayout layoutMore;
  private MeasuredGridView measuredGridView;
  private DPObject priceObj;
  private MApiRequest priceRequest;
  private TextView textView;

  public BabyPriceAgent(Object paramObject)
  {
    super(paramObject);
    sendHttpRequest();
  }

  private void sendHttpRequest()
  {
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/wedding/productpricelist.bin?");
    localStringBuilder.append("shopid=").append(shopId());
    this.priceRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.priceRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.priceObj != null)
    {
      if ((this.priceObj.getStringArray("Headers") == null) && (this.priceObj.getStringArray("Headers").length == 0))
        removeAllCells();
    }
    else
      return;
    Object localObject1 = new ArrayList();
    paramBundle = this.priceObj.getStringArray("Headers");
    int i = 0;
    while (i < paramBundle.length)
    {
      ((ArrayList)localObject1).add(paramBundle[i]);
      i += 1;
    }
    DPObject[] arrayOfDPObject = this.priceObj.getArray("ProductPrices");
    if ((arrayOfDPObject == null) || (arrayOfDPObject.length == 0))
    {
      removeAllCells();
      return;
    }
    i = 0;
    while ((arrayOfDPObject != null) && (i < arrayOfDPObject.length))
    {
      localObject2 = arrayOfDPObject[i].getStringArray("Values");
      int k = localObject2.length;
      int j = 0;
      while (j < k)
      {
        ((ArrayList)localObject1).add(localObject2[j]);
        j += 1;
      }
      i += 1;
    }
    localObject1 = (String[])((ArrayList)localObject1).toArray(new String[((ArrayList)localObject1).size()]);
    Object localObject2 = this.res.inflate(getContext(), R.layout.baby_price_agent, getParentView(), false);
    this.measuredGridView = ((MeasuredGridView)((View)localObject2).findViewById(R.id.gridview_pricelist));
    this.measuredGridView.setNumColumns(paramBundle.length);
    this.textView = ((TextView)((View)localObject2).findViewById(R.id.textview_pricelist_more));
    this.layoutMore = ((RelativeLayout)((View)localObject2).findViewById(R.id.relativelayout_babyprice_more));
    this.babyPriceAdapter = new BabyPriceAdapter(getContext(), localObject1, paramBundle.length);
    if (arrayOfDPObject.length <= 3)
    {
      this.babyPriceAdapter.setExpand(true);
      this.layoutMore.setVisibility(8);
    }
    while (true)
    {
      this.measuredGridView.setAdapter(this.babyPriceAdapter);
      addCell("2900BabyPrice.", (View)localObject2);
      return;
      this.babyPriceAdapter.setExpand(false);
      this.layoutMore.setVisibility(0);
      this.layoutMore.setOnClickListener(this);
    }
  }

  public void onClick(View paramView)
  {
    paramView = this.babyPriceAdapter;
    if (!this.babyPriceAdapter.isExpand());
    for (boolean bool = true; ; bool = false)
    {
      paramView.setExpand(bool);
      this.babyPriceAdapter.notifyDataSetChanged();
      if (!this.babyPriceAdapter.isExpand())
        break;
      this.textView.setText("收起");
      this.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_shop, 0);
      return;
    }
    this.textView.setText("展开更多报价");
    this.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_shop, 0);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.priceRequest != null)
      mapiService().abort(this.priceRequest, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.priceRequest)
      this.priceRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.priceRequest)
    {
      this.priceRequest = null;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        this.priceObj = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.BabyPriceAgent
 * JD-Core Version:    0.6.0
 */
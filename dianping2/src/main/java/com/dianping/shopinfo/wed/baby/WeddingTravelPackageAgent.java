package com.dianping.shopinfo.wed.baby;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaRelativeLayout;

public class WeddingTravelPackageAgent extends ShopCellAgent
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  final String CELL_ID = "0292Wedding.27Travel";
  MApiRequest mApiRequest;
  DPObject[] objects;
  DPObject packageObject;
  int picHeight;
  int picWidth;
  int productCategoryId;

  public WeddingTravelPackageAgent(Object paramObject)
  {
    super(paramObject);
    sendRequest();
  }

  void initViews()
  {
    this.objects = this.packageObject.getArray("List");
    if ((this.objects == null) || (this.objects.length == 0))
    {
      removeAllCells();
      return;
    }
    this.picHeight = this.packageObject.getInt("PicHeight");
    this.picWidth = this.packageObject.getInt("PicWidth");
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.wed_selection_agent, getParentView(), false);
    LinearLayout localLinearLayout1 = (LinearLayout)localView.findViewById(R.id.content);
    Object localObject1 = (NovaRelativeLayout)localView.findViewById(R.id.product_window_bottom);
    ((NovaRelativeLayout)localObject1).setClickable(true);
    ((NovaRelativeLayout)localObject1).setOnClickListener(this);
    ((NovaRelativeLayout)localObject1).setGAString("trippackageinfo_more");
    this.productCategoryId = this.packageObject.getInt("ProductCategoryId");
    localObject1 = (TextView)localView.findViewById(R.id.product_window_bottom_text);
    Object localObject2 = this.packageObject.getString("CategoryDesc");
    if (!TextUtils.isEmpty((CharSequence)localObject2))
      ((TextView)localObject1).setText((CharSequence)localObject2);
    localObject1 = (TextView)localView.findViewById(R.id.product_window_title);
    localObject2 = this.packageObject.getString("Title");
    int i;
    label204: NovaRelativeLayout localNovaRelativeLayout;
    Object localObject5;
    Object localObject3;
    Object localObject4;
    TextView localTextView1;
    int j;
    if (!TextUtils.isEmpty((CharSequence)localObject2))
    {
      ((TextView)localObject1).setText((CharSequence)localObject2);
      i = 0;
      if (i >= this.objects.length)
        break label988;
      localObject1 = this.objects[i];
      localNovaRelativeLayout = (NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.wed_selection_item, getParentView(), false);
      if (i == 0)
        localNovaRelativeLayout.findViewById(R.id.view_wed_selection_divider).setVisibility(4);
      localObject5 = (NetworkImageView)localNovaRelativeLayout.findViewById(R.id.imageView_selection_item);
      TextView localTextView3 = (TextView)localNovaRelativeLayout.findViewById(R.id.wed_selection_name);
      localObject2 = (TextView)localNovaRelativeLayout.findViewById(R.id.textview_wed_selection_tag);
      localObject3 = (TextView)localNovaRelativeLayout.findViewById(R.id.textview_wed_selection_originprice);
      localObject4 = (TextView)localNovaRelativeLayout.findViewById(R.id.textview_wed_selection_originprice_symbol);
      TextView localTextView2 = (TextView)localNovaRelativeLayout.findViewById(R.id.textview_wed_selection_price);
      localTextView1 = (TextView)localNovaRelativeLayout.findViewById(R.id.wed_selection_properties);
      LinearLayout localLinearLayout2 = (LinearLayout)localNovaRelativeLayout.findViewById(R.id.wed_selection_content);
      if ((this.picHeight == 0) && (this.picWidth == 0))
      {
        this.picHeight = ((DPObject)localObject1).getInt("PicHeight");
        this.picWidth = ((DPObject)localObject1).getInt("PicWidth");
      }
      if ((this.picWidth > 0) && (this.picHeight > 0))
      {
        j = ViewUtils.dip2px(getContext(), 90.0F);
        float f = this.picHeight * 1.0F / this.picWidth;
        int k = (int)(j * f);
        ((NetworkImageView)localObject5).getLayoutParams().height = k;
        ((NetworkImageView)localObject5).getLayoutParams().width = j;
        if (k > j)
          localLinearLayout2.setPadding(0, ViewUtils.dip2px(getContext(), 10.0F), 0, ViewUtils.dip2px(getContext(), 10.0F));
      }
      ((NetworkImageView)localObject5).setImage(((DPObject)localObject1).getString("DefaultPic"));
      localTextView3.setText(((DPObject)localObject1).getString("Name"));
      localObject5 = ((DPObject)localObject1).getString("SpecialTag");
      if (!TextUtils.isEmpty((CharSequence)localObject5))
        break label804;
      ((TextView)localObject2).setVisibility(8);
      label542: j = ((DPObject)localObject1).getInt("OriginPrice");
      ((TextView)localObject4).setVisibility(8);
      if (j <= 0)
        break label818;
      ((TextView)localObject3).setVisibility(0);
      ((TextView)localObject3).getPaint().setFlags(16);
      ((TextView)localObject3).setText(j + "");
      ((TextView)localObject4).setVisibility(0);
      label609: j = ((DPObject)localObject1).getInt("Price");
      localTextView2.setText("￥" + j);
      localObject3 = ((DPObject)localObject1).getArray("Properties");
      if ((localObject3 != null) && (localObject3.length != 0))
        break label828;
      localTextView1.setVisibility(8);
    }
    while (true)
    {
      localObject1 = new GAUserInfo();
      ((GAUserInfo)localObject1).shop_id = Integer.valueOf(shopId());
      ((GAUserInfo)localObject1).index = Integer.valueOf(i + 1);
      j = this.objects[i].getInt("ID");
      ((GAUserInfo)localObject1).biz_id = (j + "");
      localNovaRelativeLayout.setGAString("trippackageinfo_detail", (GAUserInfo)localObject1);
      localNovaRelativeLayout.setClickable(true);
      localNovaRelativeLayout.setOnClickListener(this);
      localNovaRelativeLayout.setTag(Integer.valueOf(i));
      localLinearLayout1.addView(localNovaRelativeLayout, new LinearLayout.LayoutParams(-1, -2));
      i += 1;
      break label204;
      ((TextView)localObject1).setText("精选套餐");
      break;
      label804: ((TextView)localObject2).setVisibility(0);
      ((TextView)localObject2).setText((CharSequence)localObject5);
      break label542;
      label818: ((TextView)localObject3).setVisibility(8);
      break label609;
      label828: localTextView1.setVisibility(0);
      localTextView1.setText("");
      localObject1 = "";
      j = 0;
      while (j < localObject3.length)
      {
        localObject2 = localObject3[j];
        localObject4 = ((DPObject)localObject2).getString("ID");
        localObject1 = (String)localObject1 + (String)localObject4;
        localObject2 = ((DPObject)localObject2).getString("Name");
        localObject1 = (String)localObject1 + ":";
        localObject2 = (String)localObject1 + (String)localObject2;
        localObject1 = localObject2;
        if (j != localObject3.length - 1)
          localObject1 = (String)localObject2 + "  |  ";
        j += 1;
      }
      localTextView1.setText((CharSequence)localObject1);
    }
    label988: addCell("0292Wedding.27Travel", localView);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.packageObject == null)
    {
      removeAllCells();
      return;
    }
    initViews();
  }

  public void onClick(View paramView)
  {
    if (R.id.product_window_bottom == paramView.getId())
    {
      paramView = Uri.parse("dianping://weddingpackagelist").buildUpon();
      paramView.appendQueryParameter("productcategoryid", this.productCategoryId + "");
      paramView.appendQueryParameter("shopid", shopId() + "");
      paramView = new Intent("android.intent.action.VIEW", paramView.build());
      paramView.putExtra("shop", getShop());
      startActivity(paramView);
    }
    do
      return;
    while ((paramView.getTag() == null) || (!(paramView.getTag() instanceof Integer)));
    int i = ((Integer)paramView.getTag()).intValue();
    i = this.objects[i].getInt("ID");
    paramView = new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://babyproductdetail").buildUpon().appendQueryParameter("productid", String.valueOf(i)).appendQueryParameter("shopid", String.valueOf(shopId())).build().toString()));
    paramView.putExtra("shop", getShop());
    startActivity(paramView);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mApiRequest)
      this.mApiRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mApiRequest)
    {
      this.mApiRequest = null;
      this.packageObject = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
    }
  }

  void sendRequest()
  {
    if (this.mApiRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/shoppackagelist.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("start", "0");
    localBuilder.appendQueryParameter("productcategoryid", "1632");
    localBuilder.appendQueryParameter("pageName", "shopinfo");
    this.mApiRequest = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.mApiRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingTravelPackageAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.wed.baby;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;

public class WeddingCaseAgent extends ShopCellAgent
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  final String CELL_ID = "0290Wedding.27Case";
  DPObject caseObject;
  MApiRequest caseRequest;
  DPObject[] cases;
  int picHeight;
  int picWidth;
  int productCategoryId;
  final int screenWidth = ViewUtils.getScreenWidthPixels(getContext());

  public WeddingCaseAgent(Object paramObject)
  {
    super(paramObject);
    sendCaseRequest();
  }

  private void initViews()
  {
    this.cases = this.caseObject.getArray("List");
    if ((this.cases == null) || (this.cases.length < 3))
    {
      removeAllCells();
      return;
    }
    this.productCategoryId = this.caseObject.getInt("ProductCategoryId");
    this.picHeight = this.caseObject.getInt("PicHeight");
    this.picWidth = this.caseObject.getInt("PicWidth");
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.wed_case_agent, getParentView(), false);
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)localView.findViewById(R.id.content);
    Object localObject1 = (NovaRelativeLayout)localView.findViewById(R.id.relativelayout_wed_case_title);
    ((NovaRelativeLayout)localObject1).setClickable(true);
    ((NovaRelativeLayout)localObject1).setOnClickListener(this);
    ((NovaRelativeLayout)localObject1).setGAString("caseinfo_more");
    ((TextView)localView.findViewById(R.id.product_window_title)).setText(this.caseObject.getString("Title"));
    ((TextView)localView.findViewById(R.id.product_window_bottom_text)).setText(this.caseObject.getString("Desc"));
    int i = 0;
    while (i < this.cases.length)
    {
      Object localObject2 = this.cases[i];
      localObject1 = (NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.wed_case_item, getParentView(), false);
      ((TextView)((NovaLinearLayout)localObject1).findViewById(R.id.wed_case_desc)).setText(((DPObject)localObject2).getString("Title"));
      Object localObject3 = (NetworkImageView)((NovaLinearLayout)localObject1).findViewById(R.id.wed_case_item_image);
      String str = ((DPObject)localObject2).getString("DefaultPic");
      ((NetworkImageView)localObject3).setIsCorner(true);
      ((NetworkImageView)localObject3).setImage(str);
      if ((this.picHeight == 0) && (this.picWidth == 0))
      {
        this.picHeight = ((DPObject)localObject2).getInt("PicHeight");
        this.picWidth = ((DPObject)localObject2).getInt("PicWidth");
      }
      if ((this.picWidth > 0) && (this.picHeight > 0))
      {
        int j = (this.screenWidth - ViewUtils.dip2px(getContext(), 50.0F)) / 3;
        float f = this.picHeight * 1.0F / this.picWidth;
        int k = (int)(j * f);
        ((NetworkImageView)localObject3).getLayoutParams().height = k;
        ((NetworkImageView)localObject3).getLayoutParams().width = j;
      }
      localObject3 = (ImageView)((NovaLinearLayout)localObject1).findViewById(R.id.wed_case_tag);
      if (((DPObject)localObject2).getInt("Special") == 0)
        ((ImageView)localObject3).setVisibility(8);
      localObject2 = new LinearLayout.LayoutParams(0, -2);
      ((LinearLayout.LayoutParams)localObject2).weight = 1.0F;
      if (i > 0)
        ((LinearLayout.LayoutParams)localObject2).leftMargin = ViewUtils.dip2px(getContext(), 10.0F);
      ((NovaLinearLayout)localObject1).setClickable(true);
      ((NovaLinearLayout)localObject1).setGAString("caseinfo_detail");
      ((NovaLinearLayout)localObject1).setOnClickListener(this);
      ((NovaLinearLayout)localObject1).setTag(Integer.valueOf(i));
      localNovaLinearLayout.addView((View)localObject1, (ViewGroup.LayoutParams)localObject2);
      i += 1;
    }
    addCell("0290Wedding.27Case", localView);
  }

  private void sendCaseRequest()
  {
    if (this.caseRequest != null);
    do
      return;
    while (shopId() <= 0);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/shopcaserecommend.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.caseRequest = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.caseRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.caseObject == null)
    {
      removeAllCells();
      return;
    }
    removeAllCells();
    initViews();
  }

  public void onClick(View paramView)
  {
    if (R.id.relativelayout_wed_case_title == paramView.getId())
    {
      paramView = Uri.parse("dianping://weddingcaselist").buildUpon();
      paramView.appendQueryParameter("shopid", shopId() + "");
      paramView.appendQueryParameter("productcategoryid", this.productCategoryId + "");
      paramView = new Intent("android.intent.action.VIEW", paramView.build());
      paramView.putExtra("shop", getShop());
      startActivity(paramView);
    }
    do
      return;
    while (!(paramView instanceof NovaLinearLayout));
    int i = ((Integer)paramView.getTag()).intValue();
    paramView = Uri.parse("dianping://weddingcasedetail").buildUpon();
    paramView.appendQueryParameter("shopid", shopId() + "");
    paramView.appendQueryParameter("caseid", this.cases[i].getInt("CaseId") + "");
    paramView = new Intent("android.intent.action.VIEW", paramView.build());
    paramView.putExtra("shop", getShop());
    startActivity(paramView);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.caseRequest)
      this.caseRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.caseRequest)
    {
      this.caseRequest = null;
      this.caseObject = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingCaseAgent
 * JD-Core Version:    0.6.0
 */
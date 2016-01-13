package com.dianping.shopinfo.oversea;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.City;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.v1.R.layout;

public class ImpressionAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_IMPRESSION = "0500Impression.";
  private static final String URL = "http://m.api.dianping.com/shoptip.overseas";
  private DPObject mImperssionObj;
  private MApiRequest mImpressionReq;
  View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (ImpressionAgent.this.mImperssionObj == null)
        return;
      paramView = ImpressionAgent.this.mImperssionObj.getString("Url");
      if (!TextUtils.isEmpty(paramView))
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
        ImpressionAgent.this.startActivity(paramView);
      }
      ImpressionAgent.this.statisticsEvent("overseas", "oversea_shop_impression_click", ImpressionAgent.this.mImperssionObj.getString("Title"), 0);
    }
  };

  public ImpressionAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createImpressionCell()
  {
    String str1 = this.mImperssionObj.getString("Content");
    String str2 = this.mImperssionObj.getString("Title");
    String str3 = this.mImperssionObj.getString("Url");
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    TextView localTextView;
    if (!TextUtils.isEmpty(str1))
    {
      localTextView = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_impression_textview, null, false);
      localTextView.setText(str1);
      if (TextUtils.isEmpty(str3))
        break label134;
      localShopinfoCommonCell.addContent(localTextView, false, this.mListener);
    }
    while (true)
    {
      if (!TextUtils.isEmpty(str2))
      {
        if (TextUtils.isEmpty(str3))
          break;
        localShopinfoCommonCell.setTitle(str2, this.mListener);
      }
      return localShopinfoCommonCell;
      label134: localShopinfoCommonCell.addContent(localTextView, false, null);
    }
    localShopinfoCommonCell.setTitle(str2, null);
    localShopinfoCommonCell.hideArrow();
    return localShopinfoCommonCell;
  }

  private void sendImpressionReq()
  {
    if (getFragment() == null);
    do
      return;
    while (this.mImpressionReq != null);
    this.mImpressionReq = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/shoptip.overseas").buildUpon().appendQueryParameter("shopid", String.valueOf(shopId())).build().toString(), CacheType.DISABLED);
    mapiService().exec(this.mImpressionReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.mImperssionObj == null);
    do
      return;
    while ((TextUtils.isEmpty(this.mImperssionObj.getString("Title"))) && (TextUtils.isEmpty(this.mImperssionObj.getString("Content"))));
    removeAllCells();
    addCell("0500Impression.", createImpressionCell(), 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    if ((getCity() != null) && (getCity().isForeignCity()))
      sendImpressionReq();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mImpressionReq != null)
    {
      mapiService().abort(this.mImpressionReq, this, true);
      this.mImpressionReq = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mImpressionReq)
    {
      this.mImperssionObj = null;
      this.mImpressionReq = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mImpressionReq)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mImperssionObj = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
      this.mImpressionReq = null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.oversea.ImpressionAgent
 * JD-Core Version:    0.6.0
 */
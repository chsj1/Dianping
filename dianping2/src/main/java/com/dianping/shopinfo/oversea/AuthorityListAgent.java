package com.dianping.shopinfo.oversea;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
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
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.layout;

public class AuthorityListAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_AUTHORITY_LIST = "0300AuthorityList.";
  private static final String URL = "http://m.api.dianping.com/mshop/overseashopextra.bin";
  private View.OnClickListener clickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (paramView == null);
      do
      {
        do
        {
          return;
          paramView = (DPObject)paramView.getTag();
        }
        while (paramView == null);
        paramView = paramView.getString("Scheme");
      }
      while (TextUtils.isEmpty(paramView));
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
      AuthorityListAgent.this.getFragment().startActivity(paramView);
    }
  };
  private DPObject mAuthorityListObject;
  private MApiRequest mImpressionReq;

  public AuthorityListAgent(Object paramObject)
  {
    super(paramObject);
  }

  private ViewGroup createAuthorityListCell()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    Object localObject1 = this.mAuthorityListObject.getObject("Awards");
    localShopinfoCommonCell.setTitle(((DPObject)localObject1).getString("Title"));
    localShopinfoCommonCell.hideArrow();
    localObject1 = ((DPObject)localObject1).getArray("Items");
    int i = 0;
    int j = localObject1.length;
    while (i < j)
    {
      Object localObject2 = localObject1[i];
      CommonCell localCommonCell = (CommonCell)this.res.inflate(getContext(), R.layout.shopinfo_cell, getParentView(), false);
      localCommonCell.setTitle(localObject2.getString("Title"));
      localCommonCell.setSubTitle(localObject2.getString("SubTitle"));
      localCommonCell.setLeftIconUrl(localObject2.getString("Icon"));
      localCommonCell.setArrowIconVisibility(TextUtils.isEmpty(localObject2.getString("Scheme")));
      localCommonCell.setTag(localObject2);
      localCommonCell.setGAString("rewardtag", localObject2.getString("Title"), i);
      localCommonCell.setOnClickListener(this.clickListener);
      localShopinfoCommonCell.addContent(localCommonCell, false);
      i += 1;
    }
    return (ViewGroup)localShopinfoCommonCell;
  }

  private void sendAuthorityListReq()
  {
    if ((getFragment() == null) || (this.mImpressionReq != null))
      return;
    this.mImpressionReq = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/mshop/overseashopextra.bin").buildUpon().appendQueryParameter("shopid", String.valueOf(shopId())).build().toString(), CacheType.NORMAL);
    mapiService().exec(this.mImpressionReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (this.mAuthorityListObject != null)
    {
      paramBundle = this.mAuthorityListObject.getObject("Awards");
      if ((paramBundle != null) && (paramBundle.getArray("Items") != null) && (paramBundle.getArray("Items").length > 0))
        addCell("0300AuthorityList.", createAuthorityListCell());
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendAuthorityListReq();
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
    if (paramMApiRequest == this.mImpressionReq);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mImpressionReq)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mAuthorityListObject = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
        paramMApiRequest = new Bundle();
        paramMApiRequest.putParcelable("RelevantListObject", this.mAuthorityListObject);
        dispatchAgentChanged("shopinfo/oversea_guidetips", paramMApiRequest);
      }
      this.mImpressionReq = null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.oversea.AuthorityListAgent
 * JD-Core Version:    0.6.0
 */
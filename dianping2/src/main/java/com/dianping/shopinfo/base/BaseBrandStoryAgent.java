package com.dianping.shopinfo.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.utils.SharedDataInferface;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;

public abstract class BaseBrandStoryAgent extends ShopCellAgent
  implements SharedDataInferface, RequestHandler
{
  private static final String CELL_BRAND_STROY = "8600BrandStory.";
  public static final String SHOP_EXTRA_INFO = "shopExtraInfo";
  protected MApiRequest brandStoryRequest;
  private boolean hasShow;
  protected DPObject storyData;

  public BaseBrandStoryAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createBrandStoryCell(DPObject paramDPObject)
  {
    if (TextUtils.isEmpty(paramDPObject.getString("Desc")))
      return null;
    1 local1 = new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        try
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$data.getString("Url")));
          BaseBrandStoryAgent.this.getFragment().startActivity(paramView);
          return;
        }
        catch (Exception paramView)
        {
          paramView.printStackTrace();
        }
      }
    };
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_brandstory_content, null, false);
    if (isForeignType())
    {
      localNovaRelativeLayout.setGAString("introduction", getGAExtra());
      localShopinfoCommonCell.titleLay.setGAString("introduction", getGAExtra());
    }
    while (true)
    {
      TextView localTextView = (TextView)localNovaRelativeLayout.findViewById(R.id.textview);
      localTextView.setLineSpacing(ViewUtils.dip2px(getContext(), 7.4F), 1.0F);
      localTextView.setText(paramDPObject.getString("Desc"));
      localNovaRelativeLayout.setOnClickListener(local1);
      localShopinfoCommonCell.setTitle(paramDPObject.getString("Title"), local1);
      localShopinfoCommonCell.addContent(localNovaRelativeLayout, false);
      localShopinfoCommonCell.setOnClickListener(local1);
      return localShopinfoCommonCell;
      localNovaRelativeLayout.setGAString("brand", getGAExtra());
      localShopinfoCommonCell.titleLay.setGAString("brand", getGAExtra());
    }
  }

  public final void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.storyData == null)
      this.storyData = getData(paramBundle);
    if (this.storyData == null);
    do
    {
      do
      {
        return;
        removeAllCells();
        paramBundle = createBrandStoryCell(this.storyData);
      }
      while (paramBundle == null);
      addCell("8600BrandStory.", paramBundle);
    }
    while (this.hasShow);
    if (isForeignType())
      GAHelper.instance().contextStatisticsEvent(getContext(), "introduction", getGAExtra(), "view");
    while (true)
    {
      this.hasShow = true;
      return;
      GAHelper.instance().contextStatisticsEvent(getContext(), "brand", getGAExtra(), "view");
    }
  }

  public final void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (this.brandStoryRequest == paramRequest)
      this.brandStoryRequest = null;
  }

  public final void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (this.brandStoryRequest == paramRequest)
    {
      this.brandStoryRequest = null;
      if (((paramResponse.result() instanceof DPObject)) && (paramResponse.result() != null))
      {
        this.storyData = ((DPObject)paramResponse.result());
        dispatchAgentChanged(false);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.base.BaseBrandStoryAgent
 * JD-Core Version:    0.6.0
 */
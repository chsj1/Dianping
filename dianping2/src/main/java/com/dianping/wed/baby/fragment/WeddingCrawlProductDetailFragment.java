package com.dianping.wed.baby.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import java.util.ArrayList;

public class WeddingCrawlProductDetailFragment extends WeddingProductBaseFragment
{
  private NetworkImageView imageView;

  protected void dispatchProductChanged()
  {
    super.dispatchProductChanged();
    if (this.dpProduct != null)
    {
      String str = this.dpProduct.getString("DefaultPic");
      if (this.dpProduct.getInt("CoverStyleType") == 2)
      {
        RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)this.imageView.getLayoutParams();
        if (localLayoutParams != null)
          localLayoutParams.height = -2;
      }
      this.imageView.setImage(str);
    }
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new WeddingCrawlProductDetailFragment.1(this));
    return localArrayList;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.wedding_baby_crawl_product_detail, paramViewGroup, false);
    this.pullToRefreshScrollView = ((PullToRefreshScrollView)paramLayoutInflater.findViewById(R.id.scroll));
    this.pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.imageView = ((NetworkImageView)paramLayoutInflater.findViewById(R.id.imageview_crawl_top));
    setAgentContainerView((ViewGroup)paramLayoutInflater.findViewById(R.id.content));
    return paramLayoutInflater;
  }

  protected void sendproductRequest()
  {
    if (this.productRequest != null)
      return;
    this.productRequest = BasicMApiRequest.mapiGet(new StringBuilder().append("http://m.api.dianping.com/wedding/crawlproductdetail.bin?productid=").append(this.productid).toString(), CacheType.DISABLED);
    mapiService().exec(this.productRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingCrawlProductDetailFragment
 * JD-Core Version:    0.6.0
 */
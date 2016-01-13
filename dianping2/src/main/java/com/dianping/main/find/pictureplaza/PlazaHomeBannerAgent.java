package com.dianping.main.find.pictureplaza;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.layout;

public class PlazaHomeBannerAgent extends PlazaAdapterCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_PLAZA_BANNER = "0100plaza.01banner";
  private BannerAdapter bannerAdapter;
  private MApiRequest bannerRequest;
  private DPObject mBObject;

  public PlazaHomeBannerAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void requestBanner()
  {
    if (this.bannerRequest != null)
      getFragment().mapiService().abort(this.bannerRequest, this, true);
    this.bannerRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/plaza/getplazabanner.bin").buildUpon().build().toString(), CacheType.DISABLED);
    mapiService().exec(this.bannerRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((this.mBObject != null) && (!TextUtils.isEmpty(this.mBObject.getString("Title"))))
    {
      this.bannerAdapter = new BannerAdapter(this.mBObject, null);
      addCell("0100plaza.01banner", this.bannerAdapter);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestBanner();
  }

  protected void onRefresh()
  {
    super.onRefresh();
    this.bannerAdapter = null;
    requestBanner();
    onRefreshStart();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.bannerRequest == paramMApiRequest)
    {
      this.bannerRequest = null;
      this.mBObject = null;
      dispatchAgentChanged(false);
      onRefreshComplete();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != null) && (this.bannerRequest == paramMApiRequest))
    {
      this.bannerRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mBObject = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
      onRefreshComplete();
    }
  }

  private class BannerAdapter extends BasicAdapter
  {
    private DPObject bannerObj;

    private BannerAdapter(DPObject arg2)
    {
      Object localObject;
      this.bannerObj = localObject;
    }

    public int getCount()
    {
      if (this.bannerObj != null)
        return 1;
      return 0;
    }

    public Object getItem(int paramInt)
    {
      return this.bannerObj;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      return 4;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (this.bannerObj == null)
        return paramView;
      if ((paramView instanceof PlazaHomeBannerItem));
      for (paramView = (PlazaHomeBannerItem)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
          localObject = (PlazaHomeBannerItem)PlazaHomeBannerAgent.this.res.inflate(PlazaHomeBannerAgent.this.getContext(), R.layout.plaza_home_banner_layout, paramViewGroup, false);
        ((PlazaHomeBannerItem)localObject).setBanner(this.bannerObj);
        return localObject;
      }
    }

    public int getViewTypeCount()
    {
      return 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaHomeBannerAgent
 * JD-Core Version:    0.6.0
 */
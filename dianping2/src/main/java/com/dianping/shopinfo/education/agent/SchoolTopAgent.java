package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.common.TopAgent;
import com.dianping.shopinfo.education.view.SchoolShopHeaderView;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.layout;

public class SchoolTopAgent extends TopAgent
  implements RequestHandler
{
  protected static final String CELL_TOP = "0200Basic.01top";
  public static final String SCHOOL_SHOP_INFO_KEY = "SchoolShopInfo";
  protected final View.OnClickListener iconClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      boolean bool = true;
      paramView = SchoolTopAgent.this.getShop();
      if (paramView == null);
      do
        return;
      while ((!paramView.contains("PicCount")) || (paramView.getInt("PicCount") <= 0));
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
      localIntent.putExtra("objShop", paramView);
      if ((paramView.getInt("Status") == 1) || (paramView.getInt("Status") == 4))
        bool = false;
      localIntent.putExtra("enableUpload", bool);
      SchoolTopAgent.this.getFragment().startActivity(localIntent);
    }
  };
  private DPObject schoolExtendInfo;
  private MApiRequest schoolExtendInfoReq;
  protected ShopInfoHeaderView topView;

  public SchoolTopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/edu/schoolextendedinfo.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.schoolExtendInfoReq = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.schoolExtendInfoReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    paramBundle = getShop();
    removeAllCells();
    if (paramBundle == null);
    do
      return;
    while (getFragment() == null);
    if ((this.topView != null) && (!this.topView.getClass().equals(SchoolShopHeaderView.class)))
      this.topView = null;
    if (this.topView == null)
      this.topView = ((SchoolShopHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_school_head, getParentView(), false));
    if (this.schoolExtendInfo != null)
    {
      ((SchoolShopHeaderView)this.topView).setHeaderInfo(this.schoolExtendInfo);
      setSharedObject("SchoolShopInfo", this.schoolExtendInfo);
      dispatchAgentChanged("shopinfo/school_toolbar", null);
    }
    if (getShopStatus() == 0)
      this.topView.setShop(paramBundle, 0);
    while (true)
    {
      this.topView.setIconClickListen(this.iconClickListener);
      addCell("0200Basic.01top", this.topView, 0);
      return;
      this.topView.setShop(paramBundle);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (this.schoolExtendInfoReq == paramRequest)
      this.schoolExtendInfoReq = null;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.schoolExtendInfoReq)
    {
      this.schoolExtendInfoReq = null;
      this.schoolExtendInfo = ((DPObject)paramResponse.result());
      if (this.schoolExtendInfo != null)
        dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.SchoolTopAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.hospital.agent;

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
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.common.TopAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.hospital.view.HospitalShopHeaderView;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class HospitalTopAgent extends TopAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String CELL_TOP = "0200Basic.05Info";
  private DPObject headerInfo;
  protected final View.OnClickListener iconClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      boolean bool = true;
      paramView = HospitalTopAgent.this.getShop();
      if (paramView == null)
        return;
      if ((paramView.contains("PicCount")) && (paramView.getInt("PicCount") == 0) && (TextUtils.isEmpty(paramView.getString("DefaultPic"))))
      {
        UploadPhotoUtil.uploadShopPhoto(HospitalTopAgent.this.getContext(), HospitalTopAgent.this.getShop());
        return;
      }
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
      localIntent.putExtra("objShop", paramView);
      if ((paramView.getInt("Status") == 1) || (paramView.getInt("Status") == 4))
        bool = false;
      localIntent.putExtra("enableUpload", bool);
      HospitalTopAgent.this.getFragment().startActivity(localIntent);
    }
  };
  private MApiRequest infoRequest;
  DPObject shop;
  protected ShopInfoHeaderView topView;

  public HospitalTopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void handleInfo()
  {
    if (this.headerInfo == null);
    do
    {
      return;
      if (!TextUtils.isEmpty(this.headerInfo.getString("Rank")))
      {
        localTextView = (TextView)this.topView.findViewById(R.id.rankInfo);
        localTextView.setText(this.headerInfo.getString("Rank"));
        localTextView.setVisibility(0);
      }
      if (TextUtils.isEmpty(this.headerInfo.getString("HealthInsurance")))
        continue;
      localTextView = (TextView)this.topView.findViewById(R.id.insuranceInfo);
      localTextView.setText(this.headerInfo.getString("HealthInsurance"));
      localTextView.setVisibility(0);
    }
    while (this.shop.getInt("PicCount") == 0);
    TextView localTextView = (TextView)this.topView.findViewById(R.id.imgCount);
    localTextView.setVisibility(0);
    localTextView.setText(this.shop.getInt("PicCount") + "张");
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/medicine/gethospitalrankinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.infoRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.infoRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    this.shop = getShop();
    removeAllCells();
    if (this.shop == null);
    do
      return;
    while (getFragment() == null);
    if ((this.topView != null) && (!this.topView.getClass().equals(HospitalShopHeaderView.class)))
      this.topView = null;
    if (this.topView == null)
      this.topView = ((HospitalShopHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_hospital_head, getParentView(), false));
    if ((this.headerInfo != null) && (this.headerInfo.getBoolean("IsShow")))
    {
      ((HospitalShopHeaderView)this.topView).setHeaderInfo(this.headerInfo);
      handleInfo();
    }
    if (getShopStatus() == 0)
      this.topView.setShop(this.shop, 0);
    while (true)
    {
      this.topView.setOnClickListener(this.iconClickListener);
      addCell("0200Basic.05Info", this.topView, 0);
      return;
      this.topView.setShop(this.shop);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.infoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.infoRequest = null;
    if ((paramMApiResponse == null) || (paramMApiResponse.result() == null));
    do
    {
      return;
      this.headerInfo = ((DPObject)paramMApiResponse.result());
    }
    while ((this.headerInfo == null) || (!this.headerInfo.getBoolean("IsShow")));
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hospital.agent.HospitalTopAgent
 * JD-Core Version:    0.6.0
 */
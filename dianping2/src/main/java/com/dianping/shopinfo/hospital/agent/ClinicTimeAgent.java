package com.dianping.shopinfo.hospital.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;

public class ClinicTimeAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_CLINICTIME = "0200Basic.40clinictime";
  CommonCell clinicTimeCell;
  DPObject clinicTimeInfo;
  MApiRequest infoRequest;

  public ClinicTimeAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendInfoRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/medicine/getclinictimeinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.infoRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.infoRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getShop() == null)
      removeAllCells();
    do
    {
      return;
      if (this.clinicTimeInfo != null)
        continue;
      sendInfoRequest();
      return;
    }
    while (!this.clinicTimeInfo.getBoolean("IsShow"));
    if (this.clinicTimeCell == null)
    {
      this.clinicTimeCell = createCommonCell();
      GAHelper.instance().contextStatisticsEvent(getContext(), "opentime", getGAExtra(), "view");
    }
    this.clinicTimeCell.setLeftIcon(R.drawable.detail_icon_time);
    this.clinicTimeCell.setTitle(this.clinicTimeInfo.getString("Time"));
    this.clinicTimeCell.getTitleView().setGravity(16);
    paramBundle = new LinearLayout.LayoutParams(-1, -2);
    paramBundle.topMargin = ViewUtils.dip2px(getContext(), 4.3F);
    this.clinicTimeCell.getTitleView().setLayoutParams(paramBundle);
    ((ViewGroup.MarginLayoutParams)((NetworkImageView)this.clinicTimeCell.findViewById(16908295)).getLayoutParams()).setMargins(0, 0, ViewUtils.dip2px(getContext(), 12.0F), 0);
    this.clinicTimeCell.setTitleLineSpacing(6.4F);
    this.clinicTimeCell.setTitleMaxLines(1);
    this.clinicTimeCell.setGAString("opentime", getGAExtra());
    addCell("0200Basic.40clinictime", this.clinicTimeCell, 257);
  }

  public void onCellClick(String paramString, View paramView)
  {
    if (this.clinicTimeInfo == null)
      return;
    paramString = new DPObject().edit().putString("Name", "门诊时间").putString("Value", this.clinicTimeInfo.getString("Time")).generate();
    paramString = new DPObject().edit().putArray("ShopExtraInfos", new DPObject[] { paramString }).putString("ShopExtraInfosTitle", "门诊时间").generate();
    paramString = new DPObject().edit().putObject("ShopExtraInfo", paramString).generate();
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopdetails"));
    paramView.putExtra("shop", getShop());
    paramView.putExtra("shopid", shopId());
    paramView.putExtra("shopextra", paramString);
    getFragment().startActivity(paramView);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.infoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.infoRequest = null;
    this.clinicTimeInfo = ((DPObject)paramMApiResponse.result());
    if (this.clinicTimeInfo == null);
    do
      return;
    while (!this.clinicTimeInfo.getBoolean("IsShow"));
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hospital.agent.ClinicTimeAgent
 * JD-Core Version:    0.6.0
 */
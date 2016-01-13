package com.dianping.shopinfo.hospital.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.widget.NetworkImageView;

public class HospitalOrderAgent extends ShopCellAgent
  implements RequestHandler
{
  private static final String CELL_HOSPITAL_ORDER = "0305hospitalorder.";
  private DPObject hospitalOrder;
  private MApiRequest hospitalOrderReq;

  public HospitalOrderAgent(Object paramObject)
  {
    super(paramObject);
  }

  private boolean isShow()
  {
    return (this.hospitalOrder != null) && (this.hospitalOrder.getBoolean("IsShow")) && (!android.text.TextUtils.isEmpty(this.hospitalOrder.getString("Title"))) && (!android.text.TextUtils.isEmpty(this.hospitalOrder.getString("JumpUrl"))) && (!android.text.TextUtils.isEmpty(this.hospitalOrder.getString("ToplistInfo")));
  }

  private void sendRequset()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/medicine/gethospitaltoplistinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.hospitalOrderReq = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.hospitalOrderReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (!isShow())
      return;
    paramBundle = createCommonCell();
    paramBundle.setTitle(this.hospitalOrder.getString("Title"));
    paramBundle.getRightText().setVisibility(0);
    paramBundle.getRightText().setText(com.dianping.util.TextUtils.jsonParseText(this.hospitalOrder.getString("ToplistInfo")));
    paramBundle.setLeftIcon(R.drawable.school_order);
    ViewGroup.LayoutParams localLayoutParams = ((NetworkImageView)paramBundle.findViewById(16908295)).getLayoutParams();
    localLayoutParams.height = ViewUtils.dip2px(getContext(), 15.0F);
    localLayoutParams.width = ViewUtils.dip2px(getContext(), 15.0F);
    ((ViewGroup.MarginLayoutParams)localLayoutParams).setMargins(0, 0, ViewUtils.dip2px(getContext(), 19.0F), 0);
    paramBundle.setTitleMaxLines(1);
    paramBundle.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        try
        {
          if (!android.text.TextUtils.isEmpty(HospitalOrderAgent.this.hospitalOrder.getString("JumpUrl")))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(HospitalOrderAgent.this.hospitalOrder.getString("JumpUrl")));
            HospitalOrderAgent.this.getFragment().startActivity(paramView);
          }
          return;
        }
        catch (java.lang.Exception paramView)
        {
        }
      }
    });
    addCell("0305hospitalorder.", paramBundle, 256);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequset();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (this.hospitalOrderReq == paramRequest)
      this.hospitalOrderReq = null;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.hospitalOrderReq)
    {
      this.hospitalOrderReq = null;
      this.hospitalOrder = ((DPObject)paramResponse.result());
      if (this.hospitalOrder != null)
        dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hospital.agent.HospitalOrderAgent
 * JD-Core Version:    0.6.0
 */
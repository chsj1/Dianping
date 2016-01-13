package com.dianping.membercard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaImageView;

public class MCHuiCashierRightView extends FrameLayout
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private NovaImageView checkBox;
  private boolean isChecked = false;
  private MApiRequest mcRightRequest;
  private double memoryAmount = -1.0D;
  private double reachAmount = -1.0D;
  private TextView subTitleText;
  private TextView titleText;

  public MCHuiCashierRightView(Context paramContext)
  {
    super(paramContext);
    initView(paramContext);
  }

  public MCHuiCashierRightView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  private MApiService getMapiService()
  {
    return (MApiService)(MApiService)DPApplication.instance().getService("mapi");
  }

  private void initView(Context paramContext)
  {
    LayoutInflater.from(paramContext).inflate(R.layout.view_mchui_cachierright, this, true);
    this.titleText = ((TextView)findViewById(R.id.textview_title));
    this.subTitleText = ((TextView)findViewById(R.id.textview_desc));
    this.checkBox = ((NovaImageView)findViewById(R.id.checkbox_membercard));
    setVisibility(8);
  }

  private void refreshCheckBoxView()
  {
    if (this.isChecked)
    {
      this.checkBox.setImageResource(R.drawable.mc_cbx_checked_disable);
      return;
    }
    this.checkBox.setImageResource(R.drawable.cbx_disable);
  }

  public void destroyView()
  {
    if (this.mcRightRequest != null)
      getMapiService().abort(this.mcRightRequest, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mcRightRequest)
      this.mcRightRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mcRightRequest)
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest.isClass("PayShowClubGiftDo"))
        break label28;
    }
    label28: String str;
    do
    {
      do
        return;
      while (!paramMApiRequest.getBoolean("ShowStatus"));
      paramMApiResponse = paramMApiRequest.getString("Title");
      str = paramMApiRequest.getString("SubTitle");
    }
    while (TextUtils.isEmpty(paramMApiResponse));
    this.titleText.setText(TextUtils.jsonParseText(paramMApiResponse));
    if (TextUtils.isEmpty(str))
    {
      this.subTitleText.setVisibility(8);
      paramMApiRequest = paramMApiRequest.getString("ReachAmount");
    }
    while (true)
    {
      try
      {
        this.reachAmount = Double.parseDouble(paramMApiRequest);
        if (this.memoryAmount >= this.reachAmount)
        {
          bool = true;
          this.isChecked = bool;
          refreshCheckBoxView();
          setVisibility(0);
          return;
          this.subTitleText.setText(TextUtils.jsonParseText(str));
        }
      }
      catch (Exception paramMApiRequest)
      {
        Log.e(paramMApiRequest.getMessage());
        return;
      }
      boolean bool = false;
    }
  }

  public void refreshCashierRightView(int paramInt)
  {
    if (this.mcRightRequest != null)
    {
      getMapiService().abort(this.mcRightRequest, this, true);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder("http://mc.api.dianping.com/getpayshowclubgift.mc?");
    localStringBuilder.append("shopid=").append(paramInt);
    this.mcRightRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    getMapiService().exec(this.mcRightRequest, this);
  }

  public void setNewAmount(double paramDouble)
  {
    this.memoryAmount = paramDouble;
    if (this.reachAmount < 0.0D)
      return;
    if (paramDouble >= this.reachAmount);
    for (boolean bool = true; ; bool = false)
    {
      this.isChecked = bool;
      refreshCheckBoxView();
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.MCHuiCashierRightView
 * JD-Core Version:    0.6.0
 */
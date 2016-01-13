package com.dianping.membercard.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
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
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaFrameLayout;

public class MCHuiInfoDislayView extends NovaFrameLayout
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private LayoutInflater inflater;
  private LinearLayout infoContainer;
  private MApiRequest mcInfoRequest;

  public MCHuiInfoDislayView(Context paramContext)
  {
    super(paramContext);
    initView(paramContext);
  }

  public MCHuiInfoDislayView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  private void fillInfos(String[] paramArrayOfString)
  {
    int j = 1;
    int k = paramArrayOfString.length;
    int i = 0;
    while (i < k)
    {
      String str = paramArrayOfString[i];
      TextView localTextView = (TextView)this.inflater.inflate(R.layout.textview_info_item, this.infoContainer, false);
      localTextView.setText(TextUtils.jsonParseText(str));
      if (j == 0)
        localTextView.setPadding(0, ViewUtils.dip2px(getContext(), 4.0F), 0, 0);
      this.infoContainer.addView(localTextView);
      j = 0;
      i += 1;
    }
  }

  private MApiService getMapiService()
  {
    return (MApiService)(MApiService)DPApplication.instance().getService("mapi");
  }

  private void initView(Context paramContext)
  {
    setGAString("card_payresult");
    this.inflater = LayoutInflater.from(paramContext);
    this.inflater.inflate(R.layout.view_mchui_infodisplay, this, true);
    this.infoContainer = ((LinearLayout)findViewById(R.id.layout_infocontainer));
    setVisibility(8);
  }

  public void destroyView()
  {
    if (this.mcInfoRequest != null)
      getMapiService().abort(this.mcInfoRequest, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mcInfoRequest)
      this.mcInfoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mcInfoRequest)
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest.isClass("PaySuccessClubDisplayResult"))
        break label28;
    }
    label28: 
    do
    {
      do
        return;
      while (!paramMApiRequest.getBoolean("ShowStatus"));
      paramMApiResponse = paramMApiRequest.getStringArray("Titles");
    }
    while ((paramMApiResponse == null) || (paramMApiResponse.length == 0));
    fillInfos(paramMApiResponse);
    paramMApiRequest = paramMApiRequest.getString("ActionUrl");
    if (!TextUtils.isEmpty(paramMApiRequest))
      setOnClickListener(new View.OnClickListener(paramMApiRequest)
      {
        public void onClick(View paramView)
        {
          try
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$actionUrl));
            MCHuiInfoDislayView.this.getContext().startActivity(paramView);
            GAHelper.instance().statisticsEvent(MCHuiInfoDislayView.this, "tap");
            return;
          }
          catch (Exception paramView)
          {
            Log.e(paramView.getMessage());
          }
        }
      });
    setVisibility(0);
    GAHelper.instance().statisticsEvent(this, "view");
  }

  public void refreshInfoDisplayView(int paramInt, String paramString)
  {
    if (this.mcInfoRequest != null)
    {
      getMapiService().abort(this.mcInfoRequest, this, true);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder("http://mc.api.dianping.com/getpaysuccessclubdisplay.mc?");
    localStringBuilder.append("shopid=").append(paramInt).append("&orderid=").append(paramString);
    this.mcInfoRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    getMapiService().exec(this.mcInfoRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.MCHuiInfoDislayView
 * JD-Core Version:    0.6.0
 */
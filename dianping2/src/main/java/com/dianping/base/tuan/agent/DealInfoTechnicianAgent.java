package com.dianping.base.tuan.agent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.TechnicianItemView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.NovaImageView;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DealInfoTechnicianAgent extends TuanGroupCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final int NUM = 4;
  protected DealInfoCommonCell commCell;
  protected View contentView;
  protected DPObject dpBestShop;
  protected DPObject dpDeal;
  protected DPObject dpTechniciansResult = null;
  protected MApiRequest getTechniciansResultReq = null;
  protected LinearLayout layerProfile;
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = DealInfoTechnicianAgent.this.dpTechniciansResult.getString("ListUrl");
      if (!TextUtils.isEmpty(paramView))
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + URLEncoder.encode(paramView)));
        DealInfoTechnicianAgent.this.startActivity(paramView);
      }
    }
  };

  public DealInfoTechnicianAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void requestGetTechniciansResult()
  {
    if (this.getTechniciansResultReq != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://mapi.dianping.com/");
    localUrlBuilder.appendPath("technician/gettechnicians.bin");
    localUrlBuilder.addParam("shopid", Integer.valueOf(this.dpBestShop.getInt("ID")));
    this.getTechniciansResultReq = new BasicMApiRequest(localUrlBuilder.buildUrl(), "GET", null, CacheType.DISABLED, false, null);
    mapiService().exec(this.getTechniciansResultReq, this);
  }

  private void setupView()
  {
    this.contentView = this.res.inflate(getContext(), R.layout.tuan_deal_info_technician, getParentView(), false);
    this.commCell = new DealInfoCommonCell(getContext());
    this.commCell.addContent(this.contentView, false);
    this.layerProfile = ((LinearLayout)this.contentView.findViewById(R.id.dealinfo_technician_profile));
  }

  private void updateView()
  {
    removeAllCells();
    if (this.dpTechniciansResult == null);
    Object localObject1;
    do
    {
      return;
      localObject1 = this.dpTechniciansResult.getArray("Technicians");
    }
    while (DPObjectUtils.isArrayEmpty(localObject1));
    String str1 = this.dpTechniciansResult.getString("Title");
    int m = localObject1.length;
    int j = 0;
    int i = 0;
    String str3;
    int k;
    if (j < m)
    {
      str3 = localObject1[j];
      k = i + 1;
      if (i < 4);
    }
    while (true)
    {
      localObject1 = this.dpTechniciansResult.getString("AddUrl");
      Object localObject2;
      Object localObject3;
      if ((k < 4) && (!TextUtils.isEmpty((CharSequence)localObject1)))
      {
        localObject2 = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_technician_add_item, this.layerProfile, false);
        localObject3 = (NovaImageView)((View)localObject2).findViewById(R.id.technician_add_button);
        ((NovaImageView)localObject3).setGAString("technician_add");
        ((TextView)((View)localObject2).findViewById(R.id.technician_add_tip)).setText(getResources().getString(R.string.shopinfo_technician_add) + str1);
        i = ViewUtils.getScreenWidthPixels(getContext()) * 21 / 100;
        ((NovaImageView)localObject3).getLayoutParams().width = i;
        ((NovaImageView)localObject3).getLayoutParams().height = i;
        ((NovaImageView)localObject3).setOnClickListener(new View.OnClickListener((String)localObject1)
        {
          public void onClick(View paramView)
          {
            paramView = this.val$addUrl;
            try
            {
              String str = URLEncoder.encode(paramView, "utf-8");
              paramView = str;
              label14: paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramView));
              DealInfoTechnicianAgent.this.getFragment().startActivity(paramView);
              return;
            }
            catch (UnsupportedEncodingException localUnsupportedEncodingException)
            {
              break label14;
            }
          }
        });
        this.layerProfile.addView((View)localObject2);
      }
      this.layerProfile.setVisibility(0);
      if (str1 != null)
        this.commCell.setTitle(str1 + "(" + this.dpTechniciansResult.getInt("Count") + ")", this.mListener);
      if ((this.fragment instanceof GroupAgentFragment))
      {
        addCell("032Technician.01Technician0", this.commCell);
        return;
        localObject2 = str3.getString("Name");
        localObject3 = str3.getString("PhotoUrl");
        i = str3.getInt("Star");
        String str2 = str3.getString("Title");
        int n = str3.getInt("Certified");
        str3 = str3.getString("DetailPageUrl");
        TechnicianItemView localTechnicianItemView = TechnicianItemView.createView(getContext(), this.layerProfile);
        localTechnicianItemView.init((String)localObject3, (String)localObject2, i, str2, n, k);
        localTechnicianItemView.iconView.setTag(Integer.valueOf(k));
        localTechnicianItemView.iconView.setOnClickListener(new View.OnClickListener(str3)
        {
          public void onClick(View paramView)
          {
            if (TextUtils.isEmpty(this.val$detailUrl))
              return;
            paramView = this.val$detailUrl;
            try
            {
              String str = URLEncoder.encode(this.val$detailUrl, "utf-8");
              paramView = str;
              label28: paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramView));
              DealInfoTechnicianAgent.this.startActivity(paramView);
              return;
            }
            catch (UnsupportedEncodingException localUnsupportedEncodingException)
            {
              break label28;
            }
          }
        });
        this.layerProfile.addView(localTechnicianItemView);
        j += 1;
        i = k;
        break;
      }
      addCell("032Technician.01Technician0", this.commCell);
      addEmptyCell("032Technician.01Technician1");
      return;
      k = i;
    }
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ((paramAgentMessage != null) && ("dealInfoShop".equals(paramAgentMessage.what)) && (paramAgentMessage.body != null) && (paramAgentMessage.body.getParcelable("shop") != null))
    {
      this.dpBestShop = ((DPObject)paramAgentMessage.body.getParcelable("shop"));
      requestGetTechniciansResult();
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      paramBundle = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal != paramBundle)
        this.dpDeal = paramBundle;
    }
    if (getContext() == null);
    do
      return;
    while ((this.dpDeal == null) || (this.contentView != null));
    setupView();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getTechniciansResultReq)
      this.getTechniciansResultReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.getTechniciansResultReq)
    {
      this.getTechniciansResultReq = null;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "TechniciansResult"))
      {
        this.dpTechniciansResult = ((DPObject)paramMApiResponse);
        updateView();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoTechnicianAgent
 * JD-Core Version:    0.6.0
 */
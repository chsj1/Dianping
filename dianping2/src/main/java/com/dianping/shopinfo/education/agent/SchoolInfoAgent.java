package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;

public class SchoolInfoAgent extends ShopCellAgent
  implements RequestHandler
{
  private static final String CELL_SCHOOL_ORDER = "0500school.01info";
  private DPObject schoolRelatedInfo;
  private MApiRequest schoolRelatedReq;

  public SchoolInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createContentCell(DPObject paramDPObject)
  {
    Object localObject1 = paramDPObject.getString("Title");
    Object localObject2 = paramDPObject.getString("DetailLink");
    paramDPObject = paramDPObject.getString("Introduction");
    View localView = this.res.inflate(getContext(), R.layout.shopinfo_edu_shopinfo, getParentView(), false);
    Object localObject3 = (NovaRelativeLayout)localView.findViewById(R.id.edu_shopintro_detail);
    ((NovaRelativeLayout)localObject3).setGAString("edu_schoolbrief", getGAExtra());
    ((NovaRelativeLayout)localObject3).setOnClickListener(new View.OnClickListener((String)localObject2)
    {
      public void onClick(View paramView)
      {
        if (!TextUtils.isEmpty(this.val$detailLink))
        {
          paramView = new Intent("android.intent.action.VIEW");
          paramView.setData(Uri.parse(this.val$detailLink));
          SchoolInfoAgent.this.getFragment().startActivity(paramView);
        }
      }
    });
    localObject3 = (NovaLinearLayout)localView.findViewById(R.id.edu_shopintro_text_lay);
    ((NovaLinearLayout)localObject3).setGAString("edu_schoolbrief", getGAExtra());
    ((NovaLinearLayout)localObject3).setOnClickListener(new View.OnClickListener((String)localObject2)
    {
      public void onClick(View paramView)
      {
        if (!TextUtils.isEmpty(this.val$detailLink))
        {
          paramView = new Intent("android.intent.action.VIEW");
          paramView.setData(Uri.parse(this.val$detailLink));
          SchoolInfoAgent.this.getFragment().startActivity(paramView);
        }
      }
    });
    localObject2 = (TextView)localView.findViewById(R.id.edu_shopintro_title);
    if ((!TextUtils.isEmpty((CharSequence)localObject1)) && (localObject2 != null))
      ((TextView)localObject2).setText((CharSequence)localObject1);
    localObject1 = (TextView)localView.findViewById(R.id.edu_shopintro_text);
    if ((!TextUtils.isEmpty(paramDPObject)) && (localObject1 != null))
      ((TextView)localObject1).setText(paramDPObject);
    return (View)(View)(View)localView;
  }

  private void sendRequset()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/edu/schoolrelationinfo.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.schoolRelatedReq = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.schoolRelatedReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if ((this.schoolRelatedInfo == null) || (this.schoolRelatedInfo.getArray("SchoolSimpleInfoList") == null));
    do
    {
      do
      {
        return;
        paramBundle = this.schoolRelatedInfo.getArray("SchoolSimpleInfoList");
      }
      while (paramBundle.length < 1);
      paramBundle = paramBundle[0];
    }
    while ((paramBundle == null) || (TextUtils.isEmpty(paramBundle.getString("Title"))) || (TextUtils.isEmpty(paramBundle.getString("Introduction"))));
    addCell("0500school.01info", createContentCell(paramBundle));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequset();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (this.schoolRelatedReq == paramRequest)
      this.schoolRelatedReq = null;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.schoolRelatedReq)
    {
      this.schoolRelatedReq = null;
      this.schoolRelatedInfo = ((DPObject)paramResponse.result());
      if (this.schoolRelatedInfo != null)
      {
        setSharedObject("eduRelatedInfo", this.schoolRelatedInfo);
        dispatchAgentChanged(false);
        dispatchAgentChanged("shopinfo/school_teacher", null);
        dispatchAgentChanged("shopinfo/school_admission", null);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.SchoolInfoAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.base.widget.TechnicianItemView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.utils.SharedDataInferface;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.NovaImageView;
import com.dianping.widget.view.NovaRelativeLayout;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class BaseTechnicianAgent extends ShopCellAgent
  implements SharedDataInferface, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int NUM = 4;
  protected static final String SHOP_EXTRA_INFO = "shopExtraInfo";
  protected String CELL_TECHNICIAN = "0500Technician.panel";
  private final int STATUS_CLOSED = 1;
  private View.OnClickListener listClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if ((BaseTechnicianAgent.this.getShop() == null) || (BaseTechnicianAgent.this.techniciansInfo == null))
        return;
      String str = BaseTechnicianAgent.this.techniciansInfo.getString("ListUrl");
      paramView = str;
      if (str != null);
      try
      {
        paramView = URLEncoder.encode(str, "utf-8");
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramView));
        BaseTechnicianAgent.this.getFragment().startActivity(paramView);
        return;
      }
      catch (UnsupportedEncodingException paramView)
      {
        while (true)
          paramView = str;
      }
    }
  };
  protected MApiRequest request;
  protected DPObject techniciansInfo;

  public BaseTechnicianAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createTechniciansView()
  {
    Object localObject4 = null;
    View localView = null;
    Object localObject5 = "";
    int j = 0;
    int m;
    Object localObject6;
    Object localObject2;
    int i;
    Object localObject1;
    Object localObject3;
    String str;
    int k;
    if ((this.techniciansInfo != null) && (this.techniciansInfo.getInt("Count") > 0))
    {
      m = this.techniciansInfo.getInt("Count");
      localObject4 = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
      ((NovaRelativeLayout)((ShopinfoCommonCell)localObject4).findViewById(R.id.title_layout)).setGAString("technician_more");
      localObject5 = this.techniciansInfo.getString("Title");
      localObject6 = this.techniciansInfo.getArray("Technicians");
      localObject2 = localObject4;
      i = m;
      localObject1 = localObject5;
      localObject3 = localView;
      if (localObject6 != null)
      {
        localObject2 = localObject4;
        i = m;
        localObject1 = localObject5;
        localObject3 = localView;
        if (localObject6.length > 0)
        {
          localView = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_technician_view, null, false);
          int n = localObject6.length;
          j = 0;
          i = 0;
          if (j >= n)
            break label684;
          str = localObject6[j];
          k = i + 1;
          if (i < 4)
            break label410;
        }
      }
    }
    while (true)
    {
      localObject6 = this.techniciansInfo.getString("AddUrl");
      localObject2 = localObject4;
      i = m;
      localObject1 = localObject5;
      localObject3 = localView;
      if (k < 4)
      {
        localObject2 = localObject4;
        i = m;
        localObject1 = localObject5;
        localObject3 = localView;
        if (!TextUtils.isEmpty((CharSequence)localObject6))
        {
          localObject1 = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_technician_add_item, (ViewGroup)localView, false);
          localObject2 = (NovaImageView)((View)localObject1).findViewById(R.id.technician_add_button);
          ((NovaImageView)localObject2).setGAString("technician_add");
          ((TextView)((View)localObject1).findViewById(R.id.technician_add_tip)).setText(getResources().getString(R.string.shopinfo_technician_add) + (String)localObject5);
          i = ViewUtils.getScreenWidthPixels(getContext()) * 21 / 100;
          ((NovaImageView)localObject2).getLayoutParams().width = i;
          ((NovaImageView)localObject2).getLayoutParams().height = i;
          ((NovaImageView)localObject2).setOnClickListener(new View.OnClickListener((String)localObject6)
          {
            public void onClick(View paramView)
            {
              paramView = this.val$addUrl;
              try
              {
                String str = URLEncoder.encode(paramView, "utf-8");
                paramView = str;
                label14: paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramView));
                BaseTechnicianAgent.this.getFragment().startActivity(paramView);
                return;
              }
              catch (UnsupportedEncodingException localUnsupportedEncodingException)
              {
                break label14;
              }
            }
          });
          ((LinearLayout)localView).addView((View)localObject1);
          localObject3 = localView;
          localObject1 = localObject5;
          i = m;
          localObject2 = localObject4;
        }
      }
      while (true)
      {
        localObject4 = this.listClickListener;
        if ((localObject2 != null) && (localObject3 != null))
          break label624;
        return null;
        label410: localObject1 = str.getString("Name");
        localObject2 = str.getString("PhotoUrl");
        i = str.getInt("Star");
        localObject3 = str.getString("Title");
        int i1 = str.getInt("Certified");
        str = str.getString("DetailPageUrl");
        TechnicianItemView localTechnicianItemView = TechnicianItemView.createView(getContext(), (ViewGroup)localView);
        localTechnicianItemView.iconView.setGAString("technician_detail");
        localTechnicianItemView.init((String)localObject2, (String)localObject1, i, (String)localObject3, i1, k);
        localTechnicianItemView.iconView.setOnClickListener(new View.OnClickListener(str)
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
              BaseTechnicianAgent.this.getFragment().startActivity(paramView);
              return;
            }
            catch (UnsupportedEncodingException localUnsupportedEncodingException)
            {
              break label28;
            }
          }
        });
        ((LinearLayout)localView).addView(localTechnicianItemView);
        j += 1;
        i = k;
        break;
        localObject2 = localObject4;
        i = j;
        localObject1 = localObject5;
        localObject3 = localView;
        if (this.techniciansInfo == null)
          continue;
        localObject2 = localObject4;
        i = j;
        localObject1 = localObject5;
        localObject3 = localView;
        if (this.techniciansInfo.getInt("Count") != 0)
          continue;
        localObject1 = new Bundle();
        ((Bundle)localObject1).putBoolean("ShowTechEmptyModule", true);
        dispatchAgentChanged("shopinfo/common_emptytech", (Bundle)localObject1);
        localObject2 = localObject4;
        i = j;
        localObject1 = localObject5;
        localObject3 = localView;
      }
      label624: ((ShopinfoCommonCell)localObject2).addContent((View)localObject3, false, null);
      localObject5 = new StringBuilder();
      localObject3 = localObject1;
      if (localObject1 == null)
        localObject3 = "";
      ((ShopinfoCommonCell)localObject2).setTitle((String)localObject3 + "(" + i + ")", (View.OnClickListener)localObject4);
      return localObject2;
      label684: k = i;
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.techniciansInfo == null)
      this.techniciansInfo = getData(paramBundle);
    if (this.techniciansInfo == null);
    do
    {
      do
      {
        return;
        removeAllCells();
        paramBundle = getShop();
      }
      while ((paramBundle == null) || (paramBundle.getInt("Status") == 1));
      paramBundle = createTechniciansView();
    }
    while (paramBundle == null);
    addCell(this.CELL_TECHNICIAN, paramBundle, 0);
  }

  public void onDestroy()
  {
    if (this.request != null)
    {
      getFragment().mapiService().abort(this.request, this, true);
      this.request = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request == paramMApiRequest)
    {
      this.request = null;
      dispatchAgentChanged(false);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request == paramMApiRequest)
    {
      this.techniciansInfo = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.base.BaseTechnicianAgent
 * JD-Core Version:    0.6.0
 */
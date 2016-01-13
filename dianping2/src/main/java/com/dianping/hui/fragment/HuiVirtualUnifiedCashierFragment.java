package com.dianping.hui.fragment;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;

public class HuiVirtualUnifiedCashierFragment extends AgentFragment
{
  private static final String VIRTUAL_CASHIER_REQ = "http://hui.api.dianping.com/loadvirtualunifiedcashier.bin?";
  private LinearLayout containerView;
  private String couponDescTitle;
  private String couponDescUrl;
  private DPObject[] couponProducts;
  private View huiCashierErrorLayout;
  private View huiCashierLoadedLayout;
  private View huiCashierLoadingLayout;
  private RequestHandler<MApiRequest, MApiResponse> requestHandler = new HuiVirtualUnifiedCashierFragment.1(this);
  private int scanType;
  private int shopId;
  private String shopName;
  private MApiRequest virtualUnifiedCashierReq;

  private void addRightTitleLink(String paramString1, String paramString2)
  {
    ((TextView)super.getTitleBar().findViewById(R.id.title_bar_title)).setMaxWidth(ViewUtils.dip2px(getActivity(), 200.0F));
    NovaTextView localNovaTextView = (NovaTextView)LayoutInflater.from(getActivity()).inflate(R.layout.title_bar_text, null, false);
    localNovaTextView.setPadding(0, 0, 0, 0);
    localNovaTextView.setText(paramString1);
    super.getTitleBar().addRightViewItem(localNovaTextView, "huirules", new HuiVirtualUnifiedCashierFragment.4(this, paramString2));
  }

  private void loadCashier()
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("scanType", this.scanType);
    localBundle.putParcelableArray("couponProducts", this.couponProducts);
    dispatchAgentChanged(null, localBundle);
    manageLayouts(false, false, true);
    if (!TextUtils.isEmpty(this.couponDescTitle));
    try
    {
      addRightTitleLink(this.couponDescTitle, this.couponDescUrl);
      return;
    }
    catch (Exception localException)
    {
      Log.e("Hui", "add right title link fail", localException);
    }
  }

  private void manageLayouts(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    int j = 0;
    View localView = this.huiCashierLoadingLayout;
    if (paramBoolean1)
    {
      i = 0;
      localView.setVisibility(i);
      localView = this.huiCashierErrorLayout;
      if (!paramBoolean2)
        break label72;
      i = 0;
      label36: localView.setVisibility(i);
      localView = this.huiCashierLoadedLayout;
      if (!paramBoolean3)
        break label79;
    }
    label72: label79: for (int i = j; ; i = 8)
    {
      localView.setVisibility(i);
      return;
      i = 8;
      break;
      i = 8;
      break label36;
    }
  }

  private void reqVirtualUnifiedCashierData()
  {
    if (this.virtualUnifiedCashierReq != null)
      mapiService().abort(this.virtualUnifiedCashierReq, this.requestHandler, true);
    Uri.Builder localBuilder = Uri.parse("http://hui.api.dianping.com/loadvirtualunifiedcashier.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", Integer.toString(this.shopId));
    localBuilder.appendQueryParameter("cityid", Integer.toString(cityId()));
    this.virtualUnifiedCashierReq = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.virtualUnifiedCashierReq, this.requestHandler);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new HuiVirtualUnifiedCashierFragment.3(this));
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    super.getActivity().setTitle(this.shopName);
    manageLayouts(true, false, false);
    reqVirtualUnifiedCashierData();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.getActivity().getWindow().setBackgroundDrawable(null);
    setHost("hui_virtual_cashier");
    this.shopId = getIntParam("shopid");
    this.shopName = getStringParam("shopname");
    this.scanType = getIntParam("scantype");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = (ViewGroup)paramLayoutInflater.inflate(R.layout.hui_unifiedcashier_fragment, paramViewGroup, false);
    this.containerView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.hui_cashier_container_layout));
    this.huiCashierLoadingLayout = paramLayoutInflater.findViewById(R.id.hui_cashier_loading_layout);
    this.huiCashierLoadedLayout = paramLayoutInflater.findViewById(R.id.hui_cashier_loaded_layout);
    this.huiCashierErrorLayout = paramLayoutInflater.findViewById(R.id.error_layout);
    this.huiCashierErrorLayout.setOnClickListener(new HuiVirtualUnifiedCashierFragment.2(this));
    setAgentContainerView(this.containerView);
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.fragment.HuiVirtualUnifiedCashierFragment
 * JD-Core Version:    0.6.0
 */
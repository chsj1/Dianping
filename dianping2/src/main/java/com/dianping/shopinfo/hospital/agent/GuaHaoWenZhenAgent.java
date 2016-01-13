package com.dianping.shopinfo.hospital.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaRelativeLayout;

public class GuaHaoWenZhenAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_GUAHAOWENZHEN = "0301guahaowenzhen.";
  private DPObject guahaoInfo;
  private MApiRequest guahaoInfoRequest;
  private View.OnClickListener guahaoListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (GuaHaoWenZhenAgent.this.guahaoInfo != null)
      {
        if (TextUtils.isEmpty(GuaHaoWenZhenAgent.this.getToken()))
          GuaHaoWenZhenAgent.this.accountService().login(new LoginResultListener()
          {
            public void onLoginCancel(AccountService paramAccountService)
            {
            }

            public void onLoginSuccess(AccountService paramAccountService)
            {
              GuaHaoWenZhenAgent.this.sendUrlRequest();
            }
          });
      }
      else
        return;
      GuaHaoWenZhenAgent.this.sendUrlRequest();
    }
  };
  private DPObject urlInfo;
  private MApiRequest urlRequest;
  private DPObject wenzhenInfo;
  private MApiRequest wenzhenInfoRequest;
  private View.OnClickListener wenzhenListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if ((GuaHaoWenZhenAgent.this.wenzhenInfo != null) && (!TextUtils.isEmpty(GuaHaoWenZhenAgent.this.wenzhenInfo.getString("JumpUrl"))))
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(GuaHaoWenZhenAgent.this.wenzhenInfo.getString("JumpUrl")));
        GuaHaoWenZhenAgent.this.getFragment().startActivity(paramView);
      }
    }
  };

  public GuaHaoWenZhenAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createCommonCell(String paramString1, String paramString2, View.OnClickListener paramOnClickListener, String paramString3, boolean paramBoolean)
  {
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.shopinfo_hospital_common_item, getParentView(), false);
    localNovaRelativeLayout.setGAString(paramString3, getGAExtra());
    paramString3 = (LinearLayout)localNovaRelativeLayout.findViewById(R.id.common_item_line);
    NetworkImageView localNetworkImageView = (NetworkImageView)localNovaRelativeLayout.findViewById(R.id.common_item_icon);
    TextView localTextView = (TextView)localNovaRelativeLayout.findViewById(R.id.common_item_title);
    ImageView localImageView = (ImageView)localNovaRelativeLayout.findViewById(R.id.common_item_indicator);
    localNetworkImageView.setImage(paramString1);
    localTextView.setText(paramString2);
    if (paramBoolean)
    {
      localImageView.setVisibility(0);
      paramString1 = localNetworkImageView.getLayoutParams();
      paramString1.height = ViewUtils.dip2px(getContext(), 15.0F);
      paramString1.width = ViewUtils.dip2px(getContext(), 15.0F);
      ((ViewGroup.MarginLayoutParams)paramString1).setMargins(0, 0, ViewUtils.dip2px(getContext(), 19.0F), 0);
      paramString1 = (RelativeLayout.LayoutParams)paramString3.getLayoutParams();
      paramString1.setMargins(ViewUtils.dip2px(getContext(), 15.0F), 0, 0, 0);
      paramString1.addRule(14, 0);
    }
    localNovaRelativeLayout.setOnClickListener(paramOnClickListener);
    return localNovaRelativeLayout;
  }

  private View createGuaHaoCell()
  {
    if (!isShowGuaHaoInfo())
      return null;
    String str1 = this.guahaoInfo.getString("Icon");
    String str2 = this.guahaoInfo.getString("Title");
    if (!isShowWenZhenInfo());
    for (boolean bool = true; ; bool = false)
      return createCommonCell(str1, str2, this.guahaoListener, "docappt", bool);
  }

  private View createGuaHaoWenZhenCell()
  {
    LinearLayout localLinearLayout = createLinearLayout();
    NovaRelativeLayout localNovaRelativeLayout1 = (NovaRelativeLayout)createGuaHaoCell();
    NovaRelativeLayout localNovaRelativeLayout2 = (NovaRelativeLayout)createWenZhenCell();
    if ((localNovaRelativeLayout1 == null) && (localNovaRelativeLayout2 == null))
      return null;
    if ((localNovaRelativeLayout1 != null) && (localNovaRelativeLayout2 == null))
    {
      localLinearLayout.addView(localNovaRelativeLayout1);
      return localLinearLayout;
    }
    if ((localNovaRelativeLayout1 == null) && (localNovaRelativeLayout2 != null))
    {
      localLinearLayout.addView(localNovaRelativeLayout2);
      return localLinearLayout;
    }
    localLinearLayout.addView(localNovaRelativeLayout1);
    localLinearLayout.addView(localNovaRelativeLayout2);
    return localLinearLayout;
  }

  private LinearLayout createLinearLayout()
  {
    LinearLayout localLinearLayout = new LinearLayout(super.getContext());
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1.0F));
    localLinearLayout.setOrientation(0);
    localLinearLayout.setGravity(17);
    return localLinearLayout;
  }

  private View createWenZhenCell()
  {
    if (!isShowWenZhenInfo())
      return null;
    String str1 = this.wenzhenInfo.getString("IconUrl");
    String str2 = this.wenzhenInfo.getString("Title");
    if (!isShowGuaHaoInfo());
    for (boolean bool = true; ; bool = false)
      return createCommonCell(str1, str2, this.wenzhenListener, "inquiry", bool);
  }

  private String getToken()
  {
    if (accountService() == null)
      return "";
    return accountService().token();
  }

  private boolean isShowGuaHaoInfo()
  {
    if (this.guahaoInfo == null);
    do
      return false;
    while ((!this.guahaoInfo.getBoolean("Show")) || (TextUtils.isEmpty(this.guahaoInfo.getString("Icon"))) || (TextUtils.isEmpty(this.guahaoInfo.getString("Title"))));
    return true;
  }

  private boolean isShowWenZhenInfo()
  {
    if (this.wenzhenInfo == null);
    do
      return false;
    while ((!this.wenzhenInfo.getBoolean("IsCanConsult")) || (TextUtils.isEmpty(this.wenzhenInfo.getString("IconUrl"))) || (TextUtils.isEmpty(this.wenzhenInfo.getString("Title"))) || (TextUtils.isEmpty(this.wenzhenInfo.getString("JumpUrl"))));
    return true;
  }

  private void sendGuaHaoInfoRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/medicine/getregisteringinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.guahaoInfoRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.guahaoInfoRequest, this);
  }

  private void sendUrlRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/medicine/getregisteringurl.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    localBuilder.appendQueryParameter("token", getToken() + "");
    this.urlRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.urlRequest, this);
  }

  private void sendWenZhenInfoRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/medicine/gethospitalconsultinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.wenzhenInfoRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.wenzhenInfoRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      return;
      paramBundle = createGuaHaoWenZhenCell();
    }
    while (paramBundle == null);
    addCell("0301guahaowenzhen.", paramBundle, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if ((this.guahaoInfo == null) && (this.guahaoInfoRequest == null))
      sendGuaHaoInfoRequest();
    if ((this.wenzhenInfo == null) && (this.wenzhenInfoRequest == null))
      sendWenZhenInfoRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.guahaoInfoRequest)
      this.guahaoInfoRequest = null;
    do
      return;
    while (paramMApiRequest != this.wenzhenInfoRequest);
    this.wenzhenInfoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.guahaoInfoRequest)
    {
      this.guahaoInfoRequest = null;
      if ((paramMApiResponse != null) && (paramMApiResponse.result() != null));
    }
    do
    {
      do
      {
        do
          while (true)
          {
            return;
            this.guahaoInfo = ((DPObject)paramMApiResponse.result());
            if (this.guahaoInfo == null)
              continue;
            dispatchAgentChanged(false);
            return;
            if (paramMApiRequest != this.wenzhenInfoRequest)
              break;
            this.wenzhenInfoRequest = null;
            if ((paramMApiResponse == null) || (paramMApiResponse.result() == null))
              continue;
            this.wenzhenInfo = ((DPObject)paramMApiResponse.result());
            if (this.wenzhenInfo == null)
              continue;
            dispatchAgentChanged(false);
            return;
          }
        while (paramMApiRequest != this.urlRequest);
        this.urlRequest = null;
      }
      while ((paramMApiResponse == null) || (paramMApiResponse.result() == null));
      this.urlInfo = ((DPObject)paramMApiResponse.result());
    }
    while ((this.urlInfo == null) || (TextUtils.isEmpty(this.urlInfo.getString("Url"))));
    startActivity(this.urlInfo.getString("Url"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hospital.agent.GuaHaoWenZhenAgent
 * JD-Core Version:    0.6.0
 */
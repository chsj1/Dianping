package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaRelativeLayout;

public class ShoperEntranceAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final String CELL_SHOP_CLAIM_ENTRANCE = "8700ShopClaimEntrance.";
  private static final int MAX_RETRY_COUNT = 3;
  private View contentLayout;
  private boolean isExpand = false;
  private int mRetryCount = 0;
  private DPObject mShopClaimEntranceObject;
  private MApiRequest mShopClaimEntranceRequest;
  private View titleInfoCell;
  private NovaRelativeLayout titleLayout;

  public ShoperEntranceAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequest()
  {
    if (this.mShopClaimEntranceRequest != null);
    do
      return;
    while (!ConfigHelper.iamShoperSwitch);
    this.mShopClaimEntranceRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/mshop/iamshoper.bin").buildUpon().appendQueryParameter("type", Integer.toString(1)).appendQueryParameter("shopid", Integer.toString(shopId())).toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.mShopClaimEntranceRequest, this);
  }

  private void setExpandViewAction()
  {
    if (this.contentLayout == null)
      return;
    this.contentLayout.postDelayed(new Runnable()
    {
      public void run()
      {
        ShoperEntranceAgent.this.setExpandViewState();
      }
    }
    , 100L);
  }

  private void setExpandViewState()
  {
    if (this.titleInfoCell == null)
      return;
    if (this.isExpand)
    {
      ((ImageView)this.titleInfoCell.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_up);
      this.contentLayout.setVisibility(0);
      return;
    }
    ((ImageView)this.titleInfoCell.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_down);
    this.contentLayout.setVisibility(8);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.mShopClaimEntranceObject == null)
    {
      removeAllCells();
      return;
    }
    Object localObject = this.mShopClaimEntranceObject.getString("Title");
    paramBundle = this.mShopClaimEntranceObject.getString("PicUrl");
    if ((TextUtils.isEmpty((CharSequence)localObject)) || (TextUtils.isEmpty(paramBundle)))
    {
      removeAllCells();
      return;
    }
    if (this.titleInfoCell == null)
    {
      this.titleInfoCell = LayoutInflater.from(getContext()).inflate(R.layout.shop_verify_entrance_cell, getParentView(), false);
      this.titleLayout = ((NovaRelativeLayout)this.titleInfoCell.findViewById(16908304));
      this.contentLayout = this.titleInfoCell.findViewById(16908290);
    }
    TextView localTextView = (TextView)this.titleInfoCell.findViewById(R.id.title);
    localTextView.setMaxWidth(ViewUtils.getScreenWidthPixels(getContext()) * 2 / 3);
    localTextView.setEllipsize(TextUtils.TruncateAt.END);
    localTextView.setText((CharSequence)localObject);
    ViewUtils.setVisibilityAndContent((TextView)this.titleInfoCell.findViewById(16908308), this.mShopClaimEntranceObject.getString("Tag"));
    localObject = (NetworkImageView)this.titleInfoCell.findViewById(16908296);
    int i = (int)(ViewUtils.getScreenWidthPixels(getContext()) * 0.859D);
    ((NetworkImageView)localObject).setLayoutParams(new LinearLayout.LayoutParams(i, (int)(i * 0.378D)));
    ((NetworkImageView)localObject).setImage(paramBundle);
    this.titleLayout.setOnClickListener(this);
    this.titleInfoCell.findViewById(R.id.shopowner_detail).setOnClickListener(this);
    this.titleInfoCell.findViewById(R.id.shopowner_download).setOnClickListener(this);
    setExpandViewState();
    addCell("8700ShopClaimEntrance.", this.titleInfoCell, 0);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    boolean bool;
    if (i == 16908304)
      if (!ViewUtils.isShow(this.contentLayout))
      {
        bool = true;
        this.isExpand = bool;
        if (!this.isExpand)
          break label59;
        this.titleLayout.setGAString("shopowner_open", getGAExtra());
        label49: setExpandViewAction();
      }
    label59: label127: 
    do
    {
      do
      {
        do
        {
          do
          {
            return;
            bool = false;
            break;
            this.titleLayout.setGAString("shopowner_close", getGAExtra());
            break label49;
            if (i != R.id.shopowner_detail)
              break label127;
          }
          while (this.mShopClaimEntranceObject == null);
          paramView = this.mShopClaimEntranceObject.getString("Url");
        }
        while (TextUtils.isEmpty(paramView));
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView)));
        return;
      }
      while ((i != R.id.shopowner_download) || (this.mShopClaimEntranceObject == null));
      paramView = this.mShopClaimEntranceObject.getString("Addin");
    }
    while (TextUtils.isEmpty(paramView));
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView)));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.mShopClaimEntranceObject = ((DPObject)paramBundle.getParcelable("ShopClaimEntranceObject"));
      this.isExpand = paramBundle.getBoolean("isExpand");
    }
    if (this.mShopClaimEntranceObject != null)
      dispatchAgentChanged(false);
    sendRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mShopClaimEntranceRequest == paramMApiRequest)
    {
      this.mRetryCount += 1;
      this.mShopClaimEntranceRequest = null;
      if (this.mRetryCount >= 3)
        dispatchAgentChanged(false);
    }
    else
    {
      return;
    }
    sendRequest();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != null) && (this.mShopClaimEntranceRequest == paramMApiRequest))
    {
      this.mShopClaimEntranceRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mShopClaimEntranceObject = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("ShopClaimEntranceObject", this.mShopClaimEntranceObject);
    localBundle.putBoolean("isExpand", this.isExpand);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.ShoperEntranceAgent
 * JD-Core Version:    0.6.0
 */
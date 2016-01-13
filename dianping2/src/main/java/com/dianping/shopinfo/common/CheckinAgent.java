package com.dianping.shopinfo.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CircleImageView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class CheckinAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_CHECKIN = "4000Checkin.";
  private NovaRelativeLayout cell;
  private int count = 0;
  private DPObject[] dpActionList;
  private DPObject[] dpAvatarList;
  private MApiRequest mShopUgcRequest;
  private DPObject shopUgcResult;

  public CheckinAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void creatCell()
  {
    this.cell = ((NovaRelativeLayout)this.res.inflate(getContext(), R.layout.shop_checkin_agent_layout, getParentView(), false));
    this.cell.setGAString("been", getGAExtra());
    ((TextView)this.cell.findViewById(R.id.count)).setText("(" + String.valueOf(this.count) + ")");
    Object localObject = (CircleImageView)this.cell.findViewById(R.id.icon_first);
    ImageView localImageView1 = (ImageView)this.cell.findViewById(R.id.friend_first);
    CircleImageView localCircleImageView1 = (CircleImageView)this.cell.findViewById(R.id.icon_second);
    ImageView localImageView2 = (ImageView)this.cell.findViewById(R.id.friend_second);
    CircleImageView localCircleImageView2 = (CircleImageView)this.cell.findViewById(R.id.icon_third);
    ImageView localImageView3 = (ImageView)this.cell.findViewById(R.id.friend_third);
    CircleImageView[] arrayOfCircleImageView = new CircleImageView[3];
    arrayOfCircleImageView[0] = localObject;
    arrayOfCircleImageView[1] = localCircleImageView1;
    arrayOfCircleImageView[2] = localCircleImageView2;
    if ((this.dpAvatarList != null) && (this.dpAvatarList.length > 0))
    {
      int i = 0;
      while ((i < this.dpAvatarList.length) && (i < 3))
      {
        localObject = this.dpAvatarList[i].getString("Avatar");
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          arrayOfCircleImageView[i].setImage((String)localObject);
          arrayOfCircleImageView[i].setVisibility(0);
          if (this.dpAvatarList[i].getBoolean("IsFollow"))
            new ImageView[] { localImageView1, localImageView2, localImageView3 }[i].setVisibility(0);
        }
        i += 1;
      }
    }
  }

  private void sendUgcRequest()
  {
    this.mShopUgcRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/review/shopugc.bin").buildUpon().appendQueryParameter("shopid", String.valueOf(shopId())).build().toString(), CacheType.DISABLED);
    super.getFragment().mapiService().exec(this.mShopUgcRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.count != 0)
    {
      creatCell();
      addCell("4000Checkin.", this.cell, "been", 1);
    }
  }

  public void onCellClick(String paramString, View paramView)
  {
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://visitedlist"));
    paramString.putExtra("shopid", shopId());
    getContext().startActivity(paramString);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendUgcRequest();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mShopUgcRequest != null)
    {
      getFragment().mapiService().abort(this.mShopUgcRequest, this, true);
      this.mShopUgcRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopUgcRequest)
      this.mShopUgcRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopUgcRequest)
    {
      this.shopUgcResult = ((DPObject)paramMApiResponse.result());
      if (this.shopUgcResult != null)
      {
        this.dpAvatarList = this.shopUgcResult.getArray("UserList");
        this.count = this.shopUgcResult.getInt("Count");
        this.dpActionList = this.shopUgcResult.getArray("ActionList");
      }
      paramMApiRequest = new Bundle();
      paramMApiRequest.putParcelableArray("dpActionList", this.dpActionList);
      dispatchAgentChanged("shopinfo/common_toolbar", paramMApiRequest);
      dispatchAgentChanged("shopinfo/edu_toolbar", paramMApiRequest);
      dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.CheckinAgent
 * JD-Core Version:    0.6.0
 */
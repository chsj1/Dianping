package com.dianping.shopinfo.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.base.widget.ShopPower;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;

public class CommunityHeadAgent extends ShopCellAgent
{
  protected static final String CELL_TOP = "0200Basic.05Info";
  private View communityHeadView;

  public CommunityHeadAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initHeaderView()
  {
    this.communityHeadView = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_community_head_layout, getParentView(), false);
    ((TextView)this.communityHeadView.findViewById(R.id.shop_name)).setText(getShop().getString("Name"));
    ((ShopPower)this.communityHeadView.findViewById(R.id.shop_power)).setPower(getShop().getInt("ShopPower"));
    ((NetworkImageView)this.communityHeadView.findViewById(R.id.shop_icon)).setImage(getShop().getString("DefaultPic"));
    ((TextView)this.communityHeadView.findViewById(R.id.shop_score)).setText(getShop().getString("ScoreText"));
    ((TextView)this.communityHeadView.findViewById(R.id.price_avg)).setText(getShop().getString("PriceText"));
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getShop() == null)
      return;
    initHeaderView();
    addCell("0200Basic.05Info", this.communityHeadView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        UploadPhotoUtil.uploadShopPhoto(CommunityHeadAgent.this.getContext(), CommunityHeadAgent.this.getShop());
      }
    };
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.community.CommunityHeadAgent
 * JD-Core Version:    0.6.0
 */
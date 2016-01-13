package com.dianping.shopinfo.common;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import com.dianping.widget.view.NovaImageView;
import com.dianping.widget.view.NovaTextView;

public class MoreAgent extends ShopCellAgent
{
  private NovaTextView mTvMenu1;
  private NovaTextView mTvMenu2;

  public MoreAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void report()
  {
    if (getShop() == null)
      return;
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web").buildUpon().appendQueryParameter("url", Uri.parse("http://m.dianping.com/poi/app/shop/updateShopType").buildUpon().appendQueryParameter("shopId", String.valueOf(super.shopId())).appendQueryParameter("newtoken", "!").toString()).build());
    super.getFragment().startActivity(localIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new FrameLayout.LayoutParams(-1, -1);
        paramView.width = ViewUtils.getScreenWidthPixels(MoreAgent.this.getContext());
        Dialog localDialog = new Dialog(MoreAgent.this.getContext(), R.style.Transparent);
        View localView = MoreAgent.this.res.inflate(MoreAgent.this.getContext(), R.layout.shopinfo_more_bar, MoreAgent.this.getParentView(), false);
        localDialog.setContentView(localView, paramView);
        localDialog.show();
        localView.findViewById(R.id.lay_toolbar_bg).setOnClickListener(new View.OnClickListener(localDialog)
        {
          public void onClick(View paramView)
          {
            this.val$dialog.dismiss();
          }
        });
        if (localView != null)
        {
          MoreAgent.access$002(MoreAgent.this, (NovaTextView)localView.findViewById(R.id.id_menu1));
          MoreAgent.this.mTvMenu1.setText("报错");
          MoreAgent.this.mTvMenu1.setGAString("report");
          MoreAgent.this.mTvMenu1.setOnClickListener(new View.OnClickListener(localDialog)
          {
            public void onClick(View paramView)
            {
              MoreAgent.this.report();
              this.val$dialog.dismiss();
            }
          });
          MoreAgent.access$202(MoreAgent.this, (NovaTextView)localView.findViewById(R.id.id_menu2));
          MoreAgent.this.mTvMenu2.setText("搜索附近");
          MoreAgent.this.mTvMenu1.setGAString("nearby_search_2");
          MoreAgent.this.mTvMenu2.setOnClickListener(new View.OnClickListener(localDialog)
          {
            public void onClick(View paramView)
            {
              paramView = MoreAgent.this.getShop();
              if (paramView == null)
                return;
              StringBuilder localStringBuilder = new StringBuilder("dianping://nearbyshoplist");
              localStringBuilder.append("?shopid=").append(MoreAgent.this.shopId());
              localStringBuilder.append("&shopname=").append(paramView.getString("Name"));
              localStringBuilder.append("&title=").append(paramView.getString("Name") + "附近");
              localStringBuilder.append("&cityid=").append(paramView.getInt("CityID"));
              localStringBuilder.append("&shoplatitude=").append(paramView.getDouble("Latitude"));
              localStringBuilder.append("&shoplongitude=").append(paramView.getDouble("Longitude"));
              localStringBuilder.append("&categoryid=").append(0);
              localStringBuilder.append("&category=").append("全部");
              MoreAgent.this.getFragment().startActivity(localStringBuilder.toString());
              this.val$dialog.dismiss();
            }
          });
          localView.findViewById(R.id.id_cancel).setOnClickListener(new View.OnClickListener(localDialog)
          {
            public void onClick(View paramView)
            {
              this.val$dialog.dismiss();
            }
          });
        }
      }
    };
    ((NovaImageView)getFragment().setTitleRightButton("6More", R.drawable.detail_topbar_icon_more, paramBundle)).setGAString("shopinfo_more", getGAExtra());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.MoreAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.search.shoplist.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;
import com.dianping.base.shoplist.agent.ShopListHeaderAgent;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import org.json.JSONObject;

public class ShopListRedirectBarAgent extends ShopListHeaderAgent
{
  private View dividerLine;
  private View redirectBar;
  private View redirectHeader;

  public ShopListRedirectBarAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    paramBundle = (NewShopListDataSource)getDataSource();
    if ((paramBundle == null) || (paramBundle.startIndex() != 0) || (!fetchListView()) || (paramBundle.targetType != 3) || (TextUtils.isEmpty(paramBundle.targetInfo)) || (paramBundle.bannerIsShown))
      if ((this.redirectHeader != null) && (this.redirectBar != null) && (this.listView != null) && (!this.listView.isPullToRefreshing()));
    do
    {
      return;
      this.redirectBar.setVisibility(8);
      this.dividerLine.setVisibility(8);
      return;
      try
      {
        paramBundle = new JSONObject(paramBundle.targetInfo);
        if (this.redirectBar == null)
        {
          if (this.redirectHeader == null)
          {
            this.redirectHeader = LayoutInflater.from(getActivity()).inflate(R.layout.shoplist_redirect_bar, getParentView(), false);
            this.redirectHeader.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(getContext(), 0.0F)));
          }
          addListHeader(this.redirectHeader);
          this.redirectBar = this.redirectHeader.findViewById(R.id.redirect_bar);
          this.dividerLine = this.redirectHeader.findViewById(R.id.divider_line);
        }
        NetworkImageView localNetworkImageView = (NetworkImageView)this.redirectBar.findViewById(R.id.redirect_icon);
        TextView localTextView1 = (TextView)this.redirectBar.findViewById(R.id.redirect_title);
        TextView localTextView2 = (TextView)this.redirectBar.findViewById(R.id.redirect_sub_title);
        localNetworkImageView.setImage(paramBundle.optString("naviicon"));
        localTextView1.setText(paramBundle.optString("navititle"));
        localTextView2.setText(paramBundle.optString("navisubtitle"));
        this.redirectBar.setOnClickListener(new View.OnClickListener(paramBundle)
        {
          public void onClick(View paramView)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$jsonObject.optString("urlschema")).buildUpon().build());
            ShopListRedirectBarAgent.this.startActivity(paramView);
          }
        });
        this.redirectBar.setVisibility(0);
        this.dividerLine.setVisibility(0);
        return;
      }
      catch (org.json.JSONException paramBundle)
      {
      }
    }
    while ((this.redirectHeader == null) || (this.redirectBar == null) || (this.listView == null) || (this.listView == null));
    this.redirectBar.setVisibility(8);
    this.dividerLine.setVisibility(8);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((this.redirectHeader != null) && (this.redirectBar != null) && (this.listView != null) && (this.listView != null))
    {
      this.redirectBar.setVisibility(8);
      this.dividerLine.setVisibility(8);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListRedirectBarAgent
 * JD-Core Version:    0.6.0
 */
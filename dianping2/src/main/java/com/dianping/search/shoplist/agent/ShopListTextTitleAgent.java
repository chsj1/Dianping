package com.dianping.search.shoplist.agent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.shoplist.agent.ShopListAgent;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ShopListTextTitleAgent extends ShopListAgent
{
  private static final String CELL_TITLE_BAR = "000TitleBar";
  private View contentView;
  private NewShopListDataSource dataSource;
  private TextView titleView;

  public ShopListTextTitleAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((getDataSource() instanceof NewShopListDataSource))
    {
      this.dataSource = ((NewShopListDataSource)getDataSource());
      if (this.contentView == null)
      {
        this.contentView = inflater().inflate(R.layout.shop_list_title_bar, getParentView(), false);
        this.titleView = ((TextView)this.contentView.findViewById(R.id.title_bar_title));
        if (TextUtils.isEmpty(this.dataSource.title))
          break label136;
        this.titleView.setText(this.dataSource.title);
      }
    }
    while (true)
    {
      this.titleView.setVisibility(0);
      this.contentView.findViewById(R.id.left_view).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          ShopListTextTitleAgent.this.getActivity().onBackPressed();
        }
      });
      addCell("000TitleBar", this.contentView);
      return;
      label136: this.titleView.setText("周边商户");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListTextTitleAgent
 * JD-Core Version:    0.6.0
 */
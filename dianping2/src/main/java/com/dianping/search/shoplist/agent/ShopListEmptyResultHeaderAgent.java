package com.dianping.search.shoplist.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.shoplist.agent.ShopListHeaderAgent;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopListEmptyResultHeaderAgent extends ShopListHeaderAgent
{
  private static final String OTHER_INFO_RESULT_DESC = "specialresdesc";
  private static final String OTHER_INFO_SWITCH_DESC = "specialrestitle";
  private static final String OTHER_INFO_SWITCH_TITLE = "specialresaction";
  private static final String URL_SCHEMA = "urlschema";
  private View emptyResultHeaderView;

  public ShopListEmptyResultHeaderAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    Object localObject = (NewShopListDataSource)getDataSource();
    if ((localObject == null) || (((NewShopListDataSource)localObject).startIndex() != 0));
    do
      while (true)
      {
        return;
        if (((NewShopListDataSource)localObject).targetType == 2)
          break;
        if ((this.emptyResultHeaderView == null) || ((fetchListView()) && (this.listView.isPullToRefreshing())))
          continue;
        this.emptyResultHeaderView.setVisibility(8);
        return;
      }
    while (!fetchListView());
    if (this.emptyResultHeaderView == null)
    {
      paramBundle = LayoutInflater.from(getActivity()).inflate(R.layout.search_shoplist_emptyheader, getParentView(), false);
      paramBundle.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(getContext(), 0.0F)));
      addListHeader(paramBundle);
      this.emptyResultHeaderView = paramBundle.findViewById(R.id.empty_header_layout);
      paramBundle = new ViewHolder();
      paramBundle.title = ((TextView)this.emptyResultHeaderView.findViewById(R.id.title));
      paramBundle.button = ((Button)this.emptyResultHeaderView.findViewById(R.id.button));
      paramBundle.message = ((TextView)this.emptyResultHeaderView.findViewById(R.id.message));
      this.emptyResultHeaderView.setTag(paramBundle);
    }
    while (true)
    {
      try
      {
        localObject = new JSONObject(((NewShopListDataSource)localObject).targetInfo);
        paramBundle.title.setText(((JSONObject)localObject).optString("specialrestitle", ""));
        paramBundle.button.setText(((JSONObject)localObject).optString("specialresaction", ""));
        paramBundle.button.setOnClickListener(new View.OnClickListener((JSONObject)localObject)
        {
          public void onClick(View paramView)
          {
            paramView = this.val$infoObj.optString("urlschema", "");
            if (android.text.TextUtils.isEmpty(paramView))
              return;
            try
            {
              String str = URLDecoder.decode(paramView, "utf-8");
              paramView = str;
              paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView).buildUpon().build());
              ShopListEmptyResultHeaderAgent.this.startActivity(paramView);
              ShopListEmptyResultHeaderAgent.this.getActivity().finish();
              return;
            }
            catch (UnsupportedEncodingException localUnsupportedEncodingException)
            {
              while (true)
                localUnsupportedEncodingException.printStackTrace();
            }
          }
        });
        paramBundle.message.setText(com.dianping.util.TextUtils.highLightShow(getContext(), ((JSONObject)localObject).optString("specialresdesc", ""), R.color.tuan_common_orange));
        this.emptyResultHeaderView.setVisibility(0);
        return;
      }
      catch (JSONException paramBundle)
      {
        paramBundle.printStackTrace();
        this.emptyResultHeaderView.setVisibility(8);
        return;
      }
      paramBundle = (ViewHolder)this.emptyResultHeaderView.getTag();
    }
  }

  class ViewHolder
  {
    Button button;
    TextView message;
    TextView title;

    ViewHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListEmptyResultHeaderAgent
 * JD-Core Version:    0.6.0
 */
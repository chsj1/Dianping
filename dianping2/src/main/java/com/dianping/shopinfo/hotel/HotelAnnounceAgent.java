package com.dianping.shopinfo.hotel;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.common.AnnounceAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import org.json.JSONArray;
import org.json.JSONObject;

public class HotelAnnounceAgent extends AnnounceAgent
{
  public HotelAnnounceAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (paramBundle == null);
    while (true)
    {
      return;
      Object localObject5 = paramBundle.getString("HotelAnnounce");
      if (TextUtils.isEmpty((CharSequence)localObject5))
        continue;
      removeAllCells();
      Object localObject4 = null;
      String str = null;
      Object localObject3 = null;
      Object localObject2 = null;
      int j = 0;
      paramBundle = localObject4;
      Object localObject1 = localObject3;
      try
      {
        localObject5 = new JSONArray((String)localObject5);
        i = j;
        paramBundle = localObject4;
        localObject1 = localObject3;
        if (((JSONArray)localObject5).length() > 0)
        {
          paramBundle = localObject4;
          localObject1 = localObject3;
          localObject5 = ((JSONArray)localObject5).getJSONObject(0);
          paramBundle = localObject4;
          localObject1 = localObject3;
          str = ((JSONObject)localObject5).optString("title");
          paramBundle = str;
          localObject1 = localObject3;
          localObject2 = ((JSONObject)localObject5).optString("content");
          paramBundle = str;
          localObject1 = localObject2;
          i = ((JSONObject)localObject5).optInt("id");
        }
        if (TextUtils.isEmpty(str))
          continue;
        if (this.announceView == null)
          this.announceView = ((LinearLayout)this.res.inflate(getContext(), R.layout.shop_announce, getParentView(), false));
        paramBundle = this.announceView.findViewById(R.id.tv_announce);
        if ((paramBundle instanceof TextView))
          ((TextView)paramBundle).setText(str);
        if (!TextUtils.isEmpty((CharSequence)localObject2))
          this.announceView.setOnClickListener(new View.OnClickListener((String)localObject2, i)
          {
            public void onClick(View paramView)
            {
              HotelAnnounceAgent.this.getFragment().startActivity(this.val$fAnnounceUrl);
              HotelAnnounceAgent.this.statisticsEvent("shopinfo5", "shopinfo5_hotelbanner_click", String.valueOf(this.val$fAnnounceId), 0);
              HotelAnnounceAgent.this.statisticsEvent("shopinfo5", "shopinfo5_ad", ((TextView)paramView.findViewById(R.id.tv_announce)).getText().toString(), 0);
            }
          });
        addCell("0000.10Announce", this.announceView, 1024);
        statisticsEvent("shopinfo5", "shopinfo5_hotelbanner", String.valueOf(i), 0);
        return;
      }
      catch (Exception localBundle)
      {
        while (true)
        {
          int i = j;
          Bundle localBundle = paramBundle;
          localObject2 = localObject1;
        }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelAnnounceAgent
 * JD-Core Version:    0.6.0
 */
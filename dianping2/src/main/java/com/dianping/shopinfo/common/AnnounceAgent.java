package com.dianping.shopinfo.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import org.json.JSONArray;
import org.json.JSONObject;

public class AnnounceAgent extends ShopCellAgent
{
  protected static final String CELL_ANNOUNCE = "0000.10Announce";
  protected LinearLayout announceView;

  public AnnounceAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    DPObject localDPObject = getShop();
    removeAllCells();
    if (localDPObject == null);
    while (true)
    {
      return;
      ImageView localImageView = null;
      String str = null;
      View localView = null;
      Object localObject2 = null;
      int n = 0;
      int j = 0;
      int m = 0;
      int i = n;
      paramBundle = localImageView;
      Object localObject1 = localView;
      try
      {
        Object localObject3 = new JSONArray(localDPObject.getString("Announce"));
        k = m;
        i = n;
        paramBundle = localImageView;
        localObject1 = localView;
        if (((JSONArray)localObject3).length() > 0)
        {
          i = n;
          paramBundle = localImageView;
          localObject1 = localView;
          localObject3 = ((JSONArray)localObject3).getJSONObject(0);
          i = n;
          paramBundle = localImageView;
          localObject1 = localView;
          str = ((JSONObject)localObject3).optString("title");
          i = n;
          paramBundle = str;
          localObject1 = localView;
          localObject2 = ((JSONObject)localObject3).optString("content");
          i = n;
          paramBundle = str;
          localObject1 = localObject2;
          j = ((JSONObject)localObject3).optInt("id");
          i = j;
          paramBundle = str;
          localObject1 = localObject2;
          k = ((JSONObject)localObject3).optInt("type");
        }
        paramBundle = k + "," + j;
        localObject1 = getContext().getSharedPreferences("shopinfo_announce", 0);
        if ((((SharedPreferences)localObject1).getBoolean(paramBundle, false)) || (TextUtils.isEmpty(str)))
          continue;
        if (this.announceView == null)
          this.announceView = ((LinearLayout)this.res.inflate(getContext(), R.layout.shop_announce, getParentView(), false));
        localView = this.announceView.findViewById(R.id.tv_announce);
        localImageView = (ImageView)this.announceView.findViewById(R.id.arrow);
        localImageView.setImageResource(R.drawable.close);
        localImageView.setOnClickListener(new View.OnClickListener((SharedPreferences)localObject1, paramBundle)
        {
          public void onClick(View paramView)
          {
            this.val$preferences.edit().putBoolean(this.val$identifier, true).commit();
            AnnounceAgent.this.announceView.setVisibility(8);
          }
        });
        if ((localView instanceof TextView))
          ((TextView)localView).setText(str);
        if (!TextUtils.isEmpty((CharSequence)localObject2))
          this.announceView.setOnClickListener(new View.OnClickListener((String)localObject2, localDPObject, j)
          {
            public void onClick(View paramView)
            {
              AnnounceAgent.this.getFragment().startActivity(this.val$fAnnounceUrl);
              if (this.val$shop.getInt("CategoryID") == 60)
                AnnounceAgent.this.statisticsEvent("shopinfo5", "shopinfo5_hotelbanner_click", String.valueOf(this.val$fAnnounceId), 0);
              AnnounceAgent.this.statisticsEvent("shopinfo5", "shopinfo5_ad", ((TextView)paramView.findViewById(R.id.tv_announce)).getText().toString(), 0);
            }
          });
        addCell("0000.10Announce", this.announceView, 1024);
        if (localDPObject.getInt("CategoryID") != 60)
          continue;
        statisticsEvent("shopinfo5", "shopinfo5_hotelbanner", String.valueOf(j), 0);
        return;
      }
      catch (Exception localBundle)
      {
        while (true)
        {
          j = i;
          Bundle localBundle = paramBundle;
          int k = m;
          localObject2 = localObject1;
        }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.AnnounceAgent
 * JD-Core Version:    0.6.0
 */
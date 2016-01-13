package com.dianping.shopinfo.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.http.message.BasicNameValuePair;

public class RecommendDishAgent extends ShopCellAgent
{
  private static final String CELL_DISH = "2000Dish.";
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      DPObject localDPObject = RecommendDishAgent.this.getShop();
      if (localDPObject == null)
        return;
      paramView = localDPObject.getString("RecommendDishUrl");
      int i;
      if (TextUtils.isEmpty(paramView))
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://recommendlist"));
        paramView.putExtra("shopId", RecommendDishAgent.this.shopId());
        if ((DPActivity.preferences().getBoolean("isShowListImage", true)) || (NetworkUtils.isWIFIConnection(RecommendDishAgent.this.getContext())))
        {
          i = 1;
          if (i == 0)
            break label186;
          paramView.putExtra("showImage", true);
        }
      }
      while (true)
      {
        paramView.putExtra("objShop", localDPObject);
        RecommendDishAgent.this.getFragment().startActivity(paramView);
        paramView = new ArrayList();
        paramView.add(new BasicNameValuePair("shopid", RecommendDishAgent.this.shopId() + ""));
        RecommendDishAgent.this.statisticsEvent("shopinfo5", "shopinfo5_dish", "", 0, paramView);
        return;
        i = 0;
        break;
        label186: paramView.putExtra("showImage", false);
        continue;
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
      }
    }
  };

  public RecommendDishAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createDishCell(DPObject paramDPObject)
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.titleLay.setGAString("dish", getGAExtra());
    StringBuffer localStringBuffer = new StringBuffer();
    StringTokenizer localStringTokenizer = new StringTokenizer(paramDPObject.getString("DishTags"), "|");
    int i = 1;
    if (localStringTokenizer.hasMoreTokens())
    {
      String str = localStringTokenizer.nextToken();
      int j = str.indexOf(',');
      localObject = str;
      if (j > 0)
        localObject = str.substring(0, j);
      localObject = ((String)localObject).trim();
      if (i != 0)
        i = 0;
      while (true)
      {
        localStringBuffer.append((String)localObject);
        break;
        localStringBuffer.append("  ");
      }
    }
    Object localObject = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_dish_textview, null, false);
    ((TextView)localObject).setLineSpacing(ViewUtils.dip2px(getContext(), 7.4F), 1.0F);
    ((TextView)localObject).setText(localStringBuffer);
    if ((TextUtils.isEmpty(paramDPObject.getString("RecommendDishUrl"))) && (getShop().getInt("ShopType") != 10))
    {
      this.mListener = null;
      localShopinfoCommonCell.findViewById(R.id.indicator).setVisibility(8);
    }
    ((NovaRelativeLayout)localShopinfoCommonCell.addContent((View)localObject, false, this.mListener)).setGAString("dish", getGAExtra());
    localShopinfoCommonCell.setTitle("网友推荐", this.mListener);
    localShopinfoCommonCell.setIcon(R.drawable.detail_icon_good);
    return (View)localShopinfoCommonCell;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (isWeddingType());
    do
    {
      return;
      removeAllCells();
      paramBundle = getShop();
    }
    while ((paramBundle == null) || (paramBundle.getInt("Status") == 1) || (paramBundle.getString("DishTags") == null) || (paramBundle.getString("DishTags").length() <= 0));
    addCell("2000Dish.", createDishCell(paramBundle), 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.RecommendDishAgent
 * JD-Core Version:    0.6.0
 */
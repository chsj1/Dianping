package com.dianping.shopinfo.hotel.senic;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.common.CharacteristicAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Map<Ljava.lang.String;Ljava.lang.String;>;
import java.util.Set;
import org.apache.http.message.BasicNameValuePair;

public class ScenicCharacteristicAgent extends CharacteristicAgent
{
  private final View.OnClickListener contentListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://scenicdetails"));
      paramView.putExtra("shop", ScenicCharacteristicAgent.this.getShop());
      paramView.putExtra("shopid", ScenicCharacteristicAgent.this.shopId());
      paramView.putExtra("shopextra", ScenicCharacteristicAgent.this.shopExtra);
      paramView.putParcelableArrayListExtra("featurelist", ScenicCharacteristicAgent.this.mCharacteristicList);
      ScenicCharacteristicAgent.this.getFragment().startActivity(paramView);
      paramView = new ArrayList();
      paramView.add(new BasicNameValuePair("shopid", ScenicCharacteristicAgent.this.shopId() + ""));
      ScenicCharacteristicAgent.this.statisticsEvent("shopinfo5", "shopinfo5_info", "", 0, paramView);
    }
  };
  private boolean isCharacteristicNull = true;
  private boolean isFeatureNull = true;

  public ScenicCharacteristicAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createCharacteristicAgent()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    Object localObject2 = getShopMainExtraPropMap();
    Object localObject1 = new LinearLayout(getContext());
    ((LinearLayout)localObject1).setOrientation(1);
    ((LinearLayout)localObject1).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    ((LinearLayout)localObject1).setPadding(ViewUtils.dip2px(getContext(), 15.0F), ViewUtils.dip2px(getContext(), 5.7F), ViewUtils.dip2px(getContext(), 15.0F), ViewUtils.dip2px(getContext(), 5.7F));
    int j = 0;
    int i = j;
    if (localObject2 != null)
    {
      i = j;
      if (!((Map)localObject2).isEmpty())
        i = 1;
    }
    if (localObject2 != null)
    {
      localObject2 = ((Map)localObject2).entrySet().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        Map.Entry localEntry = (Map.Entry)((Iterator)localObject2).next();
        LinearLayout localLinearLayout = new LinearLayout(getContext());
        localLinearLayout.setOrientation(0);
        localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        localLinearLayout.setPadding(0, ViewUtils.dip2px(getContext(), 4.3F), 0, ViewUtils.dip2px(getContext(), 4.3F));
        TextView localTextView1 = new TextView(getContext());
        localTextView1.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        localTextView1.setTextSize(0, getContext().getResources().getDimensionPixelSize(R.dimen.text_size_14));
        localTextView1.setGravity(19);
        localTextView1.getPaint().setFakeBoldText(true);
        localTextView1.setText((CharSequence)localEntry.getKey());
        TextView localTextView2 = new TextView(getContext());
        localTextView2.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0F));
        localTextView2.setTextSize(0, getContext().getResources().getDimensionPixelSize(R.dimen.text_size_14));
        localTextView2.setGravity(19);
        localTextView2.setText((CharSequence)localEntry.getValue());
        if (((String)localEntry.getKey()).equals("景点简介: "))
        {
          localTextView2.setSingleLine(false);
          localTextView2.setMaxLines(3);
          localTextView2.setEllipsize(TextUtils.TruncateAt.END);
        }
        localLinearLayout.addView(localTextView1, 0);
        localLinearLayout.addView(localTextView2, 1);
        ((LinearLayout)localObject1).addView(localLinearLayout);
      }
    }
    if ((!this.isCharacteristicNull) || (!this.isFeatureNull))
    {
      if (i != 0)
        ((NovaRelativeLayout)localShopinfoCommonCell.addContent((View)localObject1, false, this.contentListener)).setGAString("info", getGAExtra());
    }
    else
    {
      if ((!this.isCharacteristicNull) || (!this.isFeatureNull))
        break label543;
      localShopinfoCommonCell.hideTitle();
    }
    while (true)
    {
      localObject1 = localShopinfoCommonCell;
      if (this.isCharacteristicNull)
      {
        localObject1 = localShopinfoCommonCell;
        if (this.isFeatureNull)
          localObject1 = null;
      }
      return localObject1;
      localShopinfoCommonCell.findViewById(R.id.middle_divder_line).setVisibility(8);
      localShopinfoCommonCell.findViewById(R.id.content).setVisibility(8);
      break;
      label543: localShopinfoCommonCell.setTitle("实用信息", this.contentListener);
      localShopinfoCommonCell.titleLay.setGAString("info", getGAExtra());
    }
  }

  private Map<String, String> getShopMainExtraPropMap()
  {
    Object localObject1;
    if (this.shopExtra == null)
      localObject1 = new LinkedHashMap(0);
    LinkedHashMap localLinkedHashMap;
    do
    {
      return localObject1;
      localLinkedHashMap = new LinkedHashMap();
      localObject1 = this.shopExtra.getString("Time");
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        this.isFeatureNull = false;
        localLinkedHashMap.put("开放时间: ", localObject1);
      }
      localObject1 = getShop();
      Object localObject2 = ((DPObject)localObject1).getStringArray("PhoneNos");
      StringBuffer localStringBuffer = new StringBuffer();
      if ((localObject2 != null) && (localObject2.length > 0))
      {
        i = 0;
        j = localObject2.length;
        while (i < j)
        {
          localStringBuffer.append(localObject2[i]);
          if (i != localObject2.length - 1)
            localStringBuffer.append("，");
          i += 1;
        }
      }
      if (!TextUtils.isEmpty(localStringBuffer))
      {
        this.isFeatureNull = false;
        localLinkedHashMap.put("商户电话: ", localStringBuffer.toString());
      }
      int j = 0;
      localObject2 = this.shopExtra.getObject("BrandStory");
      int i = j;
      if (localObject2 != null)
      {
        i = j;
        if (!TextUtils.isEmpty(((DPObject)localObject2).getString("Desc")))
        {
          this.isFeatureNull = false;
          localLinkedHashMap.put("景点简介: ", ((DPObject)localObject2).getString("Desc"));
          i = 1;
        }
      }
      if (i == 0)
      {
        localObject2 = ((DPObject)localObject1).getString("StarTips");
        if (!TextUtils.isEmpty((CharSequence)localObject2))
        {
          this.isFeatureNull = false;
          localLinkedHashMap.put("名人出没: ", localObject2);
        }
      }
      if (!TextUtils.isEmpty(((DPObject)localObject1).getString("AltName")))
        this.isFeatureNull = false;
      localObject1 = localLinkedHashMap;
    }
    while (TextUtils.isEmpty(this.shopExtra.getString("Path")));
    this.isFeatureNull = false;
    return (Map<String, String>)(Map<String, String>)localLinkedHashMap;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    removeAllCells();
    if (getShop() == null);
    do
    {
      do
        return;
      while (getShopStatus() != 0);
      paramBundle = createCharacteristicAgent();
    }
    while (paramBundle == null);
    addCell("7000ShopInfo.", paramBundle, 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.senic.ScenicCharacteristicAgent
 * JD-Core Version:    0.6.0
 */
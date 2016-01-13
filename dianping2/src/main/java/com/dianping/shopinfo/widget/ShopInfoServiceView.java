package com.dianping.shopinfo.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;

public class ShopInfoServiceView extends NovaLinearLayout
{
  ShopInfoServiceItemView businessCell;
  protected Context context;
  private ArrayList<ShopServiceItemInfo> shopServiceInfos = new ArrayList();

  public ShopInfoServiceView(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
  }

  public ShopInfoServiceView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
  }

  private void createFourEntry(ArrayList<ShopServiceItemInfo> paramArrayList)
  {
    int i = 0;
    while (i < paramArrayList.size())
    {
      this.businessCell = ((ShopInfoServiceItemView)LayoutInflater.from(this.context).inflate(R.layout.hui_shopinfo_cell_four_bussiness, this, false));
      ShopServiceItemInfo localShopServiceItemInfo = (ShopServiceItemInfo)paramArrayList.get(i);
      this.businessCell.setItemInfo(localShopServiceItemInfo);
      addView(this.businessCell);
      i += 1;
    }
  }

  private void createSingleEntry(ArrayList<ShopServiceItemInfo> paramArrayList)
  {
    this.businessCell = ((ShopInfoServiceItemView)LayoutInflater.from(this.context).inflate(R.layout.hui_shopinfo_cell_one_bussiness, this, false));
    paramArrayList = (ShopServiceItemInfo)paramArrayList.get(0);
    this.businessCell.setItemInfo(paramArrayList);
    addView(this.businessCell);
  }

  private void createThreeEntry(ArrayList<ShopServiceItemInfo> paramArrayList)
  {
    int i = 0;
    while (i < paramArrayList.size())
    {
      this.businessCell = ((ShopInfoServiceItemView)LayoutInflater.from(this.context).inflate(R.layout.hui_shopinfo_cell_three_bussiness, this, false));
      ShopServiceItemInfo localShopServiceItemInfo = (ShopServiceItemInfo)paramArrayList.get(i);
      this.businessCell.setItemInfo(localShopServiceItemInfo);
      addView(this.businessCell);
      i += 1;
    }
  }

  private void createTwoEntry(ArrayList<ShopServiceItemInfo> paramArrayList)
  {
    int i = 0;
    while (i < paramArrayList.size())
    {
      this.businessCell = ((ShopInfoServiceItemView)LayoutInflater.from(this.context).inflate(R.layout.hui_shopinfo_cell_two_bussiness, this, false));
      ShopServiceItemInfo localShopServiceItemInfo = (ShopServiceItemInfo)paramArrayList.get(i);
      if ((!TextUtils.isEmpty(localShopServiceItemInfo.promoInfo)) && (!TextUtils.isEmpty(localShopServiceItemInfo.extraInfo)))
        localShopServiceItemInfo.extraInfo = "";
      this.businessCell.setItemInfo(localShopServiceItemInfo);
      addView(this.businessCell);
      i += 1;
    }
  }

  private void setViewLayoutParams()
  {
    setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    setOrientation(0);
    setGravity(17);
  }

  public void addItem(ShopServiceItemInfo paramShopServiceItemInfo)
  {
    this.shopServiceInfos.add(paramShopServiceItemInfo);
  }

  public void createCellView()
  {
    removeAllViews();
    int j = this.shopServiceInfos.size();
    int i = j;
    if (j > 4)
    {
      i = j - 1;
      while (i >= 4)
      {
        this.shopServiceInfos.remove(i);
        i -= 1;
      }
      i = this.shopServiceInfos.size();
    }
    switch (i)
    {
    default:
      return;
    case 1:
      createSingleEntry(this.shopServiceInfos);
      return;
    case 2:
      createTwoEntry(this.shopServiceInfos);
      return;
    case 3:
      createThreeEntry(this.shopServiceInfos);
      return;
    case 4:
    }
    createFourEntry(this.shopServiceInfos);
  }

  public int getItemCount()
  {
    if (this.shopServiceInfos != null)
      return this.shopServiceInfos.size();
    return 0;
  }

  public void setData(ArrayList<ShopServiceItemInfo> paramArrayList)
  {
    if (paramArrayList != null)
      this.shopServiceInfos = paramArrayList;
    setViewLayoutParams();
    createCellView();
  }

  public static class ShopServiceItemInfo
  {
    public String extraInfo;
    public String gaString;
    public int picResId;
    public String promoInfo;
    public String scheme;
    public String title;
    public int type;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.ShopInfoServiceView
 * JD-Core Version:    0.6.0
 */
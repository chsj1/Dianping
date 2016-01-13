package com.dianping.selectdish.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.TogetherCartManager;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;

public class SelectDishMenuCartView extends RelativeLayout
{
  private View checkCartButton;
  private TextView dishCountTextView;
  private TextView dishTypeInfoTextView;
  private boolean isTogetherMenu;
  private RMBLabelItem totalPriceItem;

  public SelectDishMenuCartView(Context paramContext)
  {
    super(paramContext);
  }

  public SelectDishMenuCartView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private int getTotalDishCount()
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager localTogetherCartManager = TogetherCartManager.getInstance();
      if (localTogetherCartManager.isOwner == 1)
        return localTogetherCartManager.getTotalDishCount();
      return localTogetherCartManager.getTotalDishCount() + localTogetherCartManager.getOtherTotalCount();
    }
    return NewCartManager.getInstance().getTotalDishCount();
  }

  private int getTotalMeatTypeDishCount()
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager localTogetherCartManager = TogetherCartManager.getInstance();
      if (localTogetherCartManager.isOwner == 1)
        return localTogetherCartManager.getGenusTypeDishCount(2);
      return localTogetherCartManager.getGenusTypeDishCount(2) + localTogetherCartManager.getGenusFromOtherDish(2);
    }
    return NewCartManager.getInstance().getTotalMeatTypeDishCount();
  }

  private double getTotalOriginPrice()
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager localTogetherCartManager = TogetherCartManager.getInstance();
      if (localTogetherCartManager.isOwner == 1)
        return localTogetherCartManager.getTotalOriginPrice();
      return localTogetherCartManager.getTotalOriginPrice() + localTogetherCartManager.getOtherTotalOriginPrice();
    }
    return NewCartManager.getInstance().getTotalOriginPrice();
  }

  private int getTotalOtherTypeDishCount()
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager localTogetherCartManager = TogetherCartManager.getInstance();
      if (localTogetherCartManager.isOwner == 1)
        return localTogetherCartManager.getGenusTypeDishCount(-1);
      return localTogetherCartManager.getGenusTypeDishCount(-1) + localTogetherCartManager.getGenusFromOtherDish(-1);
    }
    return NewCartManager.getInstance().getTotalOtherTypeDishCount();
  }

  private double getTotalPrice()
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager localTogetherCartManager = TogetherCartManager.getInstance();
      if (localTogetherCartManager.isOwner == 1)
        return localTogetherCartManager.getTotalPrice();
      return localTogetherCartManager.getTotalPrice() + localTogetherCartManager.getOtherTotalPrice();
    }
    return NewCartManager.getInstance().getTotalPrice();
  }

  private int getTotalSelectFreeDishCount()
  {
    if (this.isTogetherMenu)
      return TogetherCartManager.getInstance().getTotalSelectFreeDishCount();
    return NewCartManager.getInstance().getTotalSelectFreeDishCount();
  }

  private int getTotalVegetableTypeDishCount()
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager localTogetherCartManager = TogetherCartManager.getInstance();
      if (localTogetherCartManager.isOwner == 1)
        return localTogetherCartManager.getGenusTypeDishCount(3);
      return localTogetherCartManager.getGenusTypeDishCount(3) + localTogetherCartManager.getGenusFromOtherDish(3);
    }
    return NewCartManager.getInstance().getTotalVegetableTypeDishCount();
  }

  private boolean hasHistoryFreeDish()
  {
    if (this.isTogetherMenu)
      return TogetherCartManager.getInstance().hasHistoryFreeDish();
    return NewCartManager.getInstance().hasHistoryFreeDish();
  }

  private boolean haveGroupOnInfo()
  {
    if (this.isTogetherMenu);
    do
      return false;
    while (NewCartManager.getInstance().groupOnDealId <= 0);
    return true;
  }

  public View getDishCountView()
  {
    return this.dishCountTextView;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.dishCountTextView = ((TextView)findViewById(R.id.sd_total_count_textview));
    this.dishTypeInfoTextView = ((TextView)findViewById(R.id.sd_dish_type_textview));
    this.totalPriceItem = ((RMBLabelItem)findViewById(R.id.sd_total_price_view));
    this.checkCartButton = findViewById(R.id.sd_check_cart_button);
  }

  public void refresh()
  {
    int i = getTotalDishCount() + getTotalSelectFreeDishCount();
    Object localObject;
    boolean bool;
    label48: int j;
    int k;
    if (i > 99)
    {
      localObject = "99+";
      this.dishCountTextView.setText((CharSequence)localObject);
      if ((i <= 0) && (!hasHistoryFreeDish()) && (!haveGroupOnInfo()))
        break label169;
      bool = true;
      this.checkCartButton.setEnabled(bool);
      i = getTotalVegetableTypeDishCount();
      j = getTotalMeatTypeDishCount();
      k = getTotalOtherTypeDishCount();
      localObject = new StringBuilder();
      if ((i != 0) || (j != 0))
        break label175;
      ((StringBuilder)localObject).append("共").append(k).append("道菜");
    }
    while (true)
    {
      this.dishTypeInfoTextView.setText(((StringBuilder)localObject).toString());
      if (getTotalPrice() < getTotalOriginPrice())
        break label225;
      this.totalPriceItem.setRMBLabelStyle(2, 2, false, getResources().getColor(R.color.light_red));
      this.totalPriceItem.setRMBLabelValue(getTotalPrice());
      return;
      localObject = String.valueOf(i);
      break;
      label169: bool = false;
      break label48;
      label175: if (j > 0)
        ((StringBuilder)localObject).append(j).append("荤");
      if (i > 0)
        ((StringBuilder)localObject).append(i).append("素");
      if (k <= 0)
        continue;
      ((StringBuilder)localObject).append(k).append("其他");
    }
    label225: this.totalPriceItem.setRMBLabelStyle(2, 3, false, getResources().getColor(R.color.light_red));
    this.totalPriceItem.setRMBLabelValue(getTotalPrice(), getTotalOriginPrice());
  }

  public void setCheckCartListener(View.OnClickListener paramOnClickListener)
  {
    this.checkCartButton.setOnClickListener(paramOnClickListener);
  }

  public void setIsTogetherMenu(boolean paramBoolean)
  {
    this.isTogetherMenu = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.view.SelectDishMenuCartView
 * JD-Core Version:    0.6.0
 */
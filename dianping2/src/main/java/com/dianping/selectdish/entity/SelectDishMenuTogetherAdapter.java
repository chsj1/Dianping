package com.dianping.selectdish.entity;

import android.content.Context;
import com.dianping.selectdish.TogetherCartManager;

public class SelectDishMenuTogetherAdapter extends SelectDishMenuAdapter
{
  public SelectDishMenuTogetherAdapter(SelectDishMenuAdapter.SelectDishMenuAdapterListener paramSelectDishMenuAdapterListener, Context paramContext, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramSelectDishMenuAdapterListener, paramContext, paramInt1, paramInt2, paramInt3);
  }

  protected int getDishCountByDishId(int paramInt)
  {
    TogetherCartManager localTogetherCartManager = TogetherCartManager.getInstance();
    if (localTogetherCartManager.isOwner == 1)
      return localTogetherCartManager.getDishCountByDishIdinTotalDish(paramInt);
    return localTogetherCartManager.getDishCountByDishIdinTotalDish(paramInt) + localTogetherCartManager.getDishCountByDishIdinOtherDish(paramInt);
  }

  protected int isDishBought(int paramInt)
  {
    return TogetherCartManager.getInstance().isDishBought(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.entity.SelectDishMenuTogetherAdapter
 * JD-Core Version:    0.6.0
 */
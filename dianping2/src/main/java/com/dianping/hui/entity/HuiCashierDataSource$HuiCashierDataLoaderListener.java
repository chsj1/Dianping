package com.dianping.hui.entity;

import android.content.DialogInterface.OnClickListener;

public abstract interface HuiCashierDataSource$HuiCashierDataLoaderListener
{
  public abstract void createOrder(HuiMapiStatus paramHuiMapiStatus, Object paramObject);

  public abstract void dismissLoadingDialog();

  public abstract void loadCashierStrategy(HuiMapiStatus paramHuiMapiStatus, Object paramObject);

  public abstract void setHuiRules(String paramString1, String paramString2);

  public abstract void setSubmitButtonEnable(boolean paramBoolean);

  public abstract void showMessageDialog(String paramString1, String paramString2, DialogInterface.OnClickListener paramOnClickListener);

  public abstract void showPayDialog(String paramString);

  public abstract void showPayDialog(String paramString1, String paramString2);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.entity.HuiCashierDataSource.HuiCashierDataLoaderListener
 * JD-Core Version:    0.6.0
 */
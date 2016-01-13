package com.dianping.base.shoplist.data;

public abstract interface DataSource
{
  public static final int DATATYPE_ALL_FILTERS = 20;
  public static final int DATATYPE_INVOKEURL = 22;
  public static final int DATATYPE_OFFSET_GPS = 11;
  public static final int DATATYPE_SEARCHTIPS = 21;
  public static final int DATATYPE_SHOPITEM = 12;
  public static final int DATATYPE_SHOPLIST = 10;
  public static final int DATATYPE_STATUS = 1;
  public static final int STATUS_FAILED = 3;
  public static final int STATUS_LOADING = 1;
  public static final int STATUS_NONE = 0;
  public static final int STATUS_SUCCESS = 2;

  public abstract void addDataChangeListener(OnDataChangeListener paramOnDataChangeListener);

  public abstract void reload(boolean paramBoolean);

  public abstract void removeDataChangeListener(OnDataChangeListener paramOnDataChangeListener);

  public abstract void reset(boolean paramBoolean);

  public abstract void setDataLoader(DataLoader paramDataLoader);

  public abstract void setDataSourceStatusChangeListener(OnDataSourceStatusChangeListener paramOnDataSourceStatusChangeListener);

  public abstract int status();

  public static abstract interface DataLoader
  {
    public abstract void loadData(int paramInt, boolean paramBoolean);
  }

  public static abstract interface OnDataChangeListener
  {
    public abstract void onDataChanged(int paramInt);
  }

  public static abstract interface OnDataSourceStatusChangeListener
  {
    public abstract void onDataSourceStatusChange(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.data.DataSource
 * JD-Core Version:    0.6.0
 */
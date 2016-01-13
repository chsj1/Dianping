package com.dianping.base.shoplist.data.model;

import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.data.DataSource;
import java.util.ArrayList;

@Deprecated
public abstract interface ShopListDataSource extends DataSource
{
  public static final int DATATYPE_OFFSET_GPS = 11;
  public static final int DATATYPE_SHOPLIST = 10;

  public abstract ArrayList<DPObject> bannerList();

  public abstract String emptyMsg();

  public abstract String errorMsg();

  public abstract boolean hasSearchDate();

  public abstract boolean isEnd();

  public abstract boolean isRank();

  public abstract View lastExtraView();

  public abstract int nextStartIndex();

  public abstract double offsetLatitude();

  public abstract double offsetLongitude();

  public abstract String queryId();

  public abstract int recordCount();

  public abstract ArrayList<DPObject> shops();

  public abstract boolean showDistance();

  public abstract int startIndex();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.data.model.ShopListDataSource
 * JD-Core Version:    0.6.0
 */
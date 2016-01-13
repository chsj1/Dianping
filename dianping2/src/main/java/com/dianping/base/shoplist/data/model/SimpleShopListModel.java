package com.dianping.base.shoplist.data.model;

import android.view.View;
import com.dianping.archive.DPObject;
import java.util.ArrayList;

public abstract interface SimpleShopListModel
{
  public abstract ArrayList<DPObject> bannerList();

  public abstract int cityId();

  public abstract double customLatitude();

  public abstract double customLongitude();

  public abstract String errorMsg();

  public abstract boolean isEnd();

  public abstract String keyWordInfo();

  public abstract View lastExtraView();

  public abstract double offsetLatitude();

  public abstract double offsetLongitude();

  public abstract String queryId();

  public abstract ArrayList<DPObject> shops();

  public abstract boolean showDistance();

  public abstract int startIndex();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.data.model.SimpleShopListModel
 * JD-Core Version:    0.6.0
 */
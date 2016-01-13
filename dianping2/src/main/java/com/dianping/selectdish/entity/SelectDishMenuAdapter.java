package com.dianping.selectdish.entity;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.view.SelectDishMenuItem;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaFrameLayout;
import java.util.ArrayList;

public class SelectDishMenuAdapter extends BasicLoadAdapter
{
  private final SelectDishMenuAdapterListener adapterLister;
  private int currentCategory = -2147483648;
  private int currentSort = -2147483648;
  private Object currentStyle = this.styleBig;
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private Context mContext;
  private int shopId;
  private final Object styleBig = new Object();
  private final int styleBigCartandRecommendHeight;
  private final int styleBigCartandRecommendWidth;
  private final int styleBigImageHeight;
  private final int styleBigImageWidth;
  private final Object styleSmall = new Object();

  public SelectDishMenuAdapter(SelectDishMenuAdapterListener paramSelectDishMenuAdapterListener, Context paramContext, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramContext);
    this.mContext = paramContext;
    this.shopId = paramInt1;
    this.gaUserInfo.shop_id = Integer.valueOf(paramInt1);
    this.adapterLister = paramSelectDishMenuAdapterListener;
    this.currentSort = paramInt2;
    this.currentCategory = paramInt3;
    this.styleBigImageWidth = ((ViewUtils.getScreenWidthPixels(this.mContext) - ViewUtils.dip2px(this.mContext, 46.0F)) / 2);
    this.styleBigImageHeight = (this.styleBigImageWidth * 75 / 100);
    this.styleBigCartandRecommendWidth = (this.styleBigImageWidth / 2 - ViewUtils.dip2px(this.mContext, 12.0F) - ViewUtils.dip2px(this.mContext, 3.0F));
    this.styleBigCartandRecommendHeight = ViewUtils.dip2px(this.mContext, 25.0F);
  }

  public MApiRequest createRequest(int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/dishmenu.bin").buildUpon();
    localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    if (this.currentSort != -2147483648)
      localBuilder.appendQueryParameter("sort", String.valueOf(this.currentSort));
    if (this.currentCategory != -2147483648)
      localBuilder.appendQueryParameter("category", String.valueOf(this.currentCategory));
    return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
  }

  protected int getDishCountByDishId(int paramInt)
  {
    return NewCartManager.getInstance().getDishCountByDishId(paramInt);
  }

  public boolean isCurrentStyleSmall()
  {
    return this.currentStyle == this.styleSmall;
  }

  protected int isDishBought(int paramInt)
  {
    return NewCartManager.getInstance().isDishBought(paramInt);
  }

  protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if ((paramView == null) || (paramView.getTag() != this.currentStyle))
    {
      paramView = LayoutInflater.from(paramViewGroup.getContext());
      if (this.currentStyle == this.styleBig)
      {
        paramInt = R.layout.selectdish_menuitem;
        paramView = (SelectDishMenuItem)paramView.inflate(paramInt, null, false);
        label50: paramView.setShopId(this.shopId);
        if (this.currentStyle != this.styleBig)
          break label247;
        paramInt = 0;
        label71: paramView.setMode(paramInt);
        paramDPObject = new DishInfo(paramDPObject);
        paramDPObject.hasbought = isDishBought(paramDPObject.dishId);
        paramView.setData(paramDPObject);
        if (this.currentStyle == this.styleBig)
        {
          paramView.setPhotoSize(this.styleBigImageWidth, this.styleBigImageHeight);
          paramView.setCartandRecommendWidth(this.styleBigCartandRecommendWidth, this.styleBigCartandRecommendHeight);
        }
        paramInt = getDishCountByDishId(paramDPObject.dishId);
        paramViewGroup = (NovaFrameLayout)paramView.findViewById(R.id.addcart);
        this.gaUserInfo.title = String.valueOf(paramDPObject.dishId);
        paramViewGroup.setGAString("addcart", this.gaUserInfo);
        if (paramDPObject.soldout)
          break label252;
        paramViewGroup.setOnClickListener(new View.OnClickListener(paramDPObject)
        {
          public void onClick(View paramView)
          {
            if (SelectDishMenuAdapter.this.adapterLister != null)
              SelectDishMenuAdapter.this.adapterLister.startAddDishAnimation(paramView, 600, this.val$dishInfo);
          }
        });
        paramView.setCount(paramInt);
      }
    }
    while (true)
    {
      paramView.setTag(this.currentStyle);
      paramView.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          if (SelectDishMenuAdapter.this.adapterLister != null)
            SelectDishMenuAdapter.this.adapterLister.gotoDishDetail(this.val$dishInfo);
        }
      });
      return paramView;
      paramInt = R.layout.selectdish_menuitem_small;
      break;
      paramView = (SelectDishMenuItem)paramView;
      break label50;
      label247: paramInt = 1;
      break label71;
      label252: paramViewGroup.setOnClickListener(null);
      paramView.setCount(0);
    }
  }

  protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.adapterLister != null)
    {
      paramMApiRequest = paramMApiResponse.result();
      paramMApiResponse = this.adapterLister;
      if (getDataList().size() != 0)
        break label43;
    }
    label43: for (boolean bool = true; ; bool = false)
    {
      paramMApiResponse.dealData(paramBoolean, paramMApiRequest, bool);
      return;
    }
  }

  public void reset()
  {
    super.reset();
  }

  public void setBigStyle()
  {
    if (this.currentStyle != this.styleBig)
    {
      this.currentStyle = this.styleBig;
      notifyDataSetChanged();
    }
  }

  public void setCategory(int paramInt)
  {
    this.currentCategory = paramInt;
  }

  public void setSmallStyle()
  {
    if (this.currentStyle != this.styleSmall)
    {
      this.currentStyle = this.styleSmall;
      notifyDataSetChanged();
    }
  }

  public void setSort(int paramInt)
  {
    this.currentSort = paramInt;
  }

  public static abstract interface SelectDishMenuAdapterListener
  {
    public abstract void dealData(boolean paramBoolean1, Object paramObject, boolean paramBoolean2);

    public abstract void gotoDishDetail(DishInfo paramDishInfo);

    public abstract void startAddDishAnimation(View paramView, int paramInt, DishInfo paramDishInfo);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.entity.SelectDishMenuAdapter
 * JD-Core Version:    0.6.0
 */
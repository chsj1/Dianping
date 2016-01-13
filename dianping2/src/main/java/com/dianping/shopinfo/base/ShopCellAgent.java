package com.dianping.shopinfo.base;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.app.loader.GroupCellAgent;
import com.dianping.base.widget.TicketCell;
import com.dianping.base.widget.ToolbarButton;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.util.HashMap;

public class ShopCellAgent extends GroupCellAgent
{
  public static final int SHOP_STATUS_DONE = 0;
  public static final int SHOP_STATUS_ERROR = -1;
  public static final int SHOP_STATUS_LOADING = 1;

  @Deprecated
  protected HashMap<String, ToolbarButton> mToolBarButtonOrder = new HashMap();
  protected final ShopInfoFragment shopInfoFragment;

  public ShopCellAgent(Object paramObject)
  {
    super(paramObject);
    if (!(this.fragment instanceof ShopInfoFragment))
      throw new RuntimeException();
    this.shopInfoFragment = ((ShopInfoFragment)this.fragment);
  }

  private boolean isBabyShopView(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    do
      return false;
    while ((!"wedding_all".equals(paramString)) && (!"baby_all".equals(paramString)) && (!"renovation_all".equals(paramString)) && (!"beauty_photo".equals(paramString)) && (!"baby_photo".equals(paramString)) && (!"gravida_care".equals(paramString)) && (!"gravida_photo".equals(paramString)) && (!"baby_other".equals(paramString)) && (!"baby_edu".equals(paramString)) && (!"baby_shopping".equals(paramString)) && (!"baby_fun".equals(paramString)));
    return true;
  }

  public void addCell(String paramString1, View paramView, String paramString2, int paramInt)
  {
    super.addCell(paramString1, paramView, paramInt);
    GAHelper.instance().contextStatisticsEvent(getContext(), paramString2, null, "view");
  }

  @Deprecated
  public ToolbarButton addToolbarButton(CharSequence paramCharSequence, Drawable paramDrawable, View.OnClickListener paramOnClickListener, String paramString)
  {
    if (this.mToolBarButtonOrder.containsKey(paramString))
      getToolbarView().removeView((View)this.mToolBarButtonOrder.get(paramString));
    ToolbarButton localToolbarButton = createToolbarItem();
    localToolbarButton.setTitle(paramCharSequence);
    localToolbarButton.setIcon(paramDrawable);
    localToolbarButton.setOnClickListener(paramOnClickListener);
    addToolbarButton(localToolbarButton, paramString);
    this.mToolBarButtonOrder.put(paramString, localToolbarButton);
    return localToolbarButton;
  }

  @Deprecated
  public void addToolbarButton(View paramView, String paramString)
  {
    paramView.setTag(paramString);
    int k = -1;
    int i = 0;
    int m = getToolbarView().getChildCount();
    int j;
    while (true)
    {
      j = k;
      View localView;
      if (paramString != null)
      {
        j = k;
        if (i < m)
        {
          localView = getToolbarView().getChildAt(i);
          if ((localView.getTag() instanceof String))
            break label77;
        }
      }
      for (j = i; ; j = i)
      {
        if (j >= 0)
          break label107;
        getToolbarView().addView(paramView);
        return;
        label77: if (paramString.compareTo((String)localView.getTag()) >= 0)
          break;
      }
      i += 1;
    }
    label107: getToolbarView().addView(paramView, j);
  }

  public CommonCell createCommonCell()
  {
    return (CommonCell)MyResources.getResource(CellAgent.class).inflate(getContext(), R.layout.common_cell, getParentView(), false);
  }

  @Deprecated
  public TicketCell createTicketCell()
  {
    return (TicketCell)MyResources.getResource(CellAgent.class).inflate(getContext(), R.layout.ticket_cell, getParentView(), false);
  }

  @Deprecated
  public ToolbarButton createToolbarItem()
  {
    return (ToolbarButton)MyResources.getResource(CellAgent.class).inflate(getContext(), R.layout.toolbar_button, getToolbarView(), false);
  }

  public String getExSearchResultShopView()
  {
    return this.shopInfoFragment.exSearchResultShopView;
  }

  public ShopInfoFragment getFragment()
  {
    return this.shopInfoFragment;
  }

  public ViewGroup getParentView()
  {
    return this.shopInfoFragment.contentView;
  }

  public String getPicType()
  {
    DPObject localDPObject = getShop();
    Object localObject2 = null;
    Object localObject1 = localObject2;
    if (localDPObject != null)
    {
      localObject1 = localObject2;
      if (localDPObject.getObject("ClientShopStyle") != null)
        localObject1 = localDPObject.getObject("ClientShopStyle").getString("PicMode");
    }
    return (String)localObject1;
  }

  public DPObject getShop()
  {
    return this.shopInfoFragment.shop;
  }

  public String getShopExtraParam()
  {
    return this.shopInfoFragment.shopExtraParam;
  }

  public int getShopStatus()
  {
    if (this.shopInfoFragment.shopRetrieved)
      return 0;
    if (this.shopInfoFragment.shopRequest != null)
      return 1;
    return -1;
  }

  @Deprecated
  public ViewGroup getToolbarView()
  {
    return this.shopInfoFragment.toolbarView;
  }

  public View getView()
  {
    return null;
  }

  public boolean isBanquetType()
  {
    DPObject localDPObject = getShop();
    if ((localDPObject != null) && (localDPObject.getObject("ClientShopStyle") != null))
      return "hall_all".equals(localDPObject.getObject("ClientShopStyle").getString("ShopView"));
    return false;
  }

  public boolean isBeautyHairType()
  {
    DPObject localDPObject = getShop();
    return (localDPObject != null) && (localDPObject.getObject("ClientShopStyle") != null) && ("beauty_hair".equalsIgnoreCase(localDPObject.getObject("ClientShopStyle").getString("ShopView")));
  }

  public boolean isClothesType()
  {
    DPObject localDPObject = getShop();
    return (localDPObject != null) && (localDPObject.getObject("ClientShopStyle") != null) && ("shopping_clothes".equalsIgnoreCase(localDPObject.getObject("ClientShopStyle").getString("ShopView")));
  }

  public boolean isCommonDefalutType()
  {
    DPObject localDPObject = getShop();
    return (localDPObject != null) && (localDPObject.getObject("ClientShopStyle") != null) && ("common_default".equalsIgnoreCase(localDPObject.getObject("ClientShopStyle").getString("ShopView")));
  }

  public boolean isCommunityType()
  {
    DPObject localDPObject = getShop();
    if ((localDPObject == null) || (localDPObject.getObject("ClientShopStyle") == null))
      return false;
    return "community_common".equals(localDPObject.getObject("ClientShopStyle").getString("ShopView"));
  }

  public boolean isEducationType()
  {
    DPObject localDPObject = getShop();
    return (localDPObject != null) && (localDPObject.getObject("ClientShopStyle") != null) && ("education_all".equalsIgnoreCase(localDPObject.getObject("ClientShopStyle").getString("ShopView")));
  }

  public boolean isExSearchResultType()
  {
    String str = getExSearchResultShopView();
    return (!TextUtils.isEmpty(str)) && (("bd_shop".equalsIgnoreCase(str)) || ("community_common".equalsIgnoreCase(str)));
  }

  public boolean isForeignBigType()
  {
    DPObject localDPObject = getShop();
    return (localDPObject != null) && (localDPObject.getObject("ClientShopStyle") != null) && (("oversea_big".equalsIgnoreCase(localDPObject.getObject("ClientShopStyle").getString("ShopView"))) || ("oversea_shopping_mall".equals(localDPObject.getObject("ClientShopStyle").getString("ShopView"))));
  }

  public boolean isForeignSmallType()
  {
    DPObject localDPObject = getShop();
    return (localDPObject != null) && (localDPObject.getObject("ClientShopStyle") != null) && ("oversea_small".equalsIgnoreCase(localDPObject.getObject("ClientShopStyle").getString("ShopView")));
  }

  public boolean isForeignType()
  {
    DPObject localDPObject = getShop();
    return (localDPObject != null) && (localDPObject.getBoolean("IsForeignShop"));
  }

  public boolean isHomeDesignShopType()
  {
    DPObject localDPObject = getShop();
    if ((localDPObject == null) || (localDPObject.getObject("ClientShopStyle") == null))
      return false;
    return "renovation_design".equals(localDPObject.getObject("ClientShopStyle").getString("ShopView"));
  }

  public boolean isHomeMarketShopType()
  {
    DPObject localDPObject = getShop();
    if ((localDPObject == null) || (localDPObject.getObject("ClientShopStyle") == null))
      return false;
    return "renovation_market".equals(localDPObject.getObject("ClientShopStyle").getString("ShopView"));
  }

  public boolean isHospitalType()
  {
    DPObject localDPObject = getShop();
    return (localDPObject != null) && (localDPObject.getObject("ClientShopStyle") != null) && ("hospital_all".equalsIgnoreCase(localDPObject.getObject("ClientShopStyle").getString("ShopView")));
  }

  public boolean isHotelType()
  {
    DPObject localDPObject = getShop();
    if ((localDPObject == null) || (localDPObject.getObject("ClientShopStyle") == null))
      return false;
    return "hotel_common".equals(localDPObject.getObject("ClientShopStyle").getString("ShopView"));
  }

  public boolean isKTVType()
  {
    DPObject localDPObject = getShop();
    if ((localDPObject == null) || (localDPObject.getObject("ClientShopStyle") == null))
      return false;
    return "common_funktv".equals(localDPObject.getObject("ClientShopStyle").getString("ShopView"));
  }

  public boolean isMallType()
  {
    DPObject localDPObject = getShop();
    return (localDPObject != null) && (localDPObject.getObject("ClientShopStyle") != null) && ("shopping_mall".equalsIgnoreCase(localDPObject.getObject("ClientShopStyle").getString("ShopView")));
  }

  public boolean isPetType()
  {
    DPObject localDPObject = getShop();
    if ((localDPObject == null) || (localDPObject.getObject("ClientShopStyle") == null))
      return false;
    return "pet_common".equals(localDPObject.getObject("ClientShopStyle").getString("ShopView"));
  }

  public boolean isSchoolType()
  {
    DPObject localDPObject = getShop();
    return (localDPObject != null) && (localDPObject.getObject("ClientShopStyle") != null) && ("school_all".equalsIgnoreCase(localDPObject.getObject("ClientShopStyle").getString("ShopView")));
  }

  public boolean isShopRetrieved()
  {
    return this.shopInfoFragment.shopRetrieved;
  }

  public boolean isSpecBabyType()
  {
    DPObject localDPObject = getShop();
    if (localDPObject == null);
    int i;
    do
    {
      do
        return false;
      while (!isWeddingShopType());
      i = localDPObject.getInt("CategoryID");
    }
    while ((161 != i) && (27760 != i) && (27767 != i) && (139 != i) && (138 != i) && (i != 27809) && (i != 27810) && (i != 27811) && (i != 27812) && (i != 125));
    return true;
  }

  public boolean isSportClubType()
  {
    DPObject localDPObject = getShop();
    if ((localDPObject == null) || (localDPObject.getObject("ClientShopStyle") == null))
      return false;
    return "sports_club".equals(localDPObject.getObject("ClientShopStyle").getString("ShopView"));
  }

  public boolean isTravelType()
  {
    DPObject localDPObject = getShop();
    return (localDPObject != null) && (localDPObject.getObject("Tourist") != null) && (localDPObject.getObject("Tourist").getBoolean("NewPage"));
  }

  public boolean isWeddingShopType()
  {
    DPObject localDPObject = getShop();
    if ((localDPObject == null) || (localDPObject.getObject("ClientShopStyle") == null));
    do
      return false;
    while (isWeddingType());
    return isBabyShopView(localDPObject.getObject("ClientShopStyle").getString("ShopView"));
  }

  public boolean isWeddingType()
  {
    DPObject localDPObject = getShop();
    if ((localDPObject == null) || (localDPObject.getObject("ClientShopStyle") == null))
      return false;
    return "wedding_hotel".equals(localDPObject.getObject("ClientShopStyle").getString("ShopView"));
  }

  public void removeTopView()
  {
    this.shopInfoFragment.removeTopView();
  }

  public void setHuiLayVisibility(boolean paramBoolean)
  {
    this.shopInfoFragment.setHuiLayerVisibility(paramBoolean);
  }

  public void setKtvLayVisibility(boolean paramBoolean)
  {
    this.shopInfoFragment.setKtvLayerVisibility(paramBoolean);
  }

  public void setShop(DPObject paramDPObject)
  {
    this.shopInfoFragment.shop = paramDPObject;
  }

  public void setTopAgentMarginTop(int paramInt)
  {
    this.shopInfoFragment.setTopAgentMarginTop(paramInt);
  }

  public void setTopView(View paramView, ShopCellAgent paramShopCellAgent)
  {
    this.shopInfoFragment.setTopView(paramView, this);
  }

  public int shopId()
  {
    return this.shopInfoFragment.shopId;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.base.ShopCellAgent
 * JD-Core Version:    0.6.0
 */
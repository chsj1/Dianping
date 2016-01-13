package com.dianping.membercard.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopPower;
import com.dianping.membercard.MembersOnlyActivity;
import com.dianping.membercard.utils.EmptyHeaderHolder;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.membercard.view.AddCardButtonView;
import com.dianping.membercard.view.MemberCardListItem;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.FlipperPager;
import com.dianping.widget.FlipperPager.FlipperPagerAdapter;
import java.util.ArrayList;
import java.util.Iterator;

public class NoAddedMallCardFragment extends CardFragment
  implements AdapterView.OnItemClickListener, View.OnClickListener
{
  private static final int buttonInHeaderViewIndex = 1;
  private AddCardButtonView addCardButtonView;
  private AddCardButtonView addCardButtonView2;
  private MemberCardListItem cardInfoView;
  private View currentSelectedShopTypeView;
  private int currentShopType = -1;
  private ArrayList<DPObject> currentShopTypeFloorIndexList = new ArrayList();
  private ArrayList<DPObject> currentShopTypeProductList = new ArrayList();
  private FloorIndexAdapter floorIndexAdapter;
  private DPObject[] floorIndexListObject;
  private ListView floorIndexListView;
  private Adapter floorInfoAdapter;
  private ListView floorInfoListView;
  private FrameLayout footView;
  private Handler handler = new Handler()
  {
    AlphaAnimation dismissAnimation = new AlphaAnimation(1.0F, 0.0F);
    AlphaAnimation showAnimation = new AlphaAnimation(0.0F, 1.0F);

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        super.handleMessage(paramMessage);
      case 1:
      case 2:
        do
        {
          do
            return;
          while (NoAddedMallCardFragment.this.indexView.getVisibility() != 0);
          NoAddedMallCardFragment.this.indexView.setVisibility(8);
          this.dismissAnimation.setDuration(500L);
          NoAddedMallCardFragment.this.indexView.startAnimation(this.dismissAnimation);
          return;
        }
        while (NoAddedMallCardFragment.this.indexView.getVisibility() == 0);
        this.showAnimation.setDuration(500L);
        NoAddedMallCardFragment.this.indexView.startAnimation(this.showAnimation);
        NoAddedMallCardFragment.this.indexView.setVisibility(0);
        return;
      case 3:
      }
      NoAddedMallCardFragment.this.floorInfoListView.setSelection(paramMessage.arg1);
    }
  };
  private TextView indexView;
  private FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -2, 49);
  private boolean pined = false;
  protected ShopPower power;
  private FlipperPager productListFlipper;
  private DPObject[] productListObject;
  private FrameLayout rootView;
  private View shopInfoView;
  private FrameLayout shopTypeHeaderLayout;
  private DPObject[] shopTypeListObject;
  private TableLayout shopTypeTableLayout;

  private AddCardButtonView createAddCardButtonView()
  {
    AddCardButtonView localAddCardButtonView = new AddCardButtonView(getActivity());
    MemberCard.getGeneralCardAddButtonProduct(this.cardObject, this.cardlevel);
    if (MemberCard.isScoreCard(this.cardObject))
    {
      localAddCardButtonView.setMallScoreProduct();
      return localAddCardButtonView;
    }
    localAddCardButtonView.setMallAndBrandProduct();
    return localAddCardButtonView;
  }

  private void dismissPinedView()
  {
    if ((this.rootView == null) || (this.addCardButtonView == null));
    do
      return;
    while (!this.pined);
    this.pined = false;
    this.addCardButtonView2.setVisibility(4);
  }

  private View newOneEmptyView(int paramInt)
  {
    View localView = new EmptyHeaderHolder(paramInt).inflate(getActivity(), null, this.floorInfoListView);
    localView.setBackgroundResource(R.drawable.main_background);
    return localView;
  }

  private void resetShopTypeLayoutState(ViewGroup paramViewGroup)
  {
    int i = 0;
    if (i < paramViewGroup.getChildCount())
    {
      View localView = paramViewGroup.getChildAt(i);
      if (((ViewGroup)localView.getParent()).getChildAt(((ViewGroup)localView.getParent()).getChildCount() - 1) == localView)
        localView.setBackgroundResource(R.drawable.itemlist_2st_selected);
      while (true)
      {
        ((TextView)localView.findViewById(16908308)).setTextColor(getResources().getColor(R.color.deep_gray));
        ((TextView)localView.findViewById(16908309)).setTextColor(getResources().getColor(R.color.deep_gray));
        i += 1;
        break;
        localView.setBackgroundResource(R.drawable.mc_itemlist_2st_selected_line);
      }
    }
  }

  private void setupProductListHeaderView()
  {
    if ((this.cardObject.getArray("ProductAList") != null) && (this.cardObject.getArray("ProductAList").length > 0))
    {
      this.productListFlipper.setAdapter(new FlipperAdapter(this.cardObject.getArray("ProductAList")));
      if (this.productListObject.length > 1)
      {
        this.productListFlipper.enableNavigationDotMode(true);
        this.productListFlipper.startAutoFlip();
      }
    }
  }

  private void setupScrollAddButtonListener()
  {
    this.floorInfoListView.setOnScrollListener(new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
      {
        if (paramInt1 < 1)
          NoAddedMallCardFragment.this.dismissPinedView();
        do
          return;
        while (paramInt1 != 1);
        NoAddedMallCardFragment.this.showPinedView();
      }

      public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
      {
      }
    });
    FragmentActivity localFragmentActivity = getActivity();
    if ((localFragmentActivity != null) && ((localFragmentActivity instanceof MembersOnlyActivity)))
    {
      this.addCardButtonView.setOnAddButtonClickListener((MembersOnlyActivity)localFragmentActivity);
      this.addCardButtonView2.setOnAddButtonClickListener((MembersOnlyActivity)localFragmentActivity);
    }
  }

  private void setupShopTypeLayout(ViewGroup paramViewGroup)
  {
    TableRow localTableRow = (TableRow)paramViewGroup.findViewById(R.id.shop_type_list);
    Object localObject2 = null;
    paramViewGroup = null;
    Object localObject1 = localObject2;
    if (this.shopTypeListObject != null)
    {
      localObject1 = localObject2;
      if (this.shopTypeListObject.length > 0)
      {
        if (this.shopTypeListObject.length <= 1)
          break label244;
        localObject2 = null;
        int i = 0;
        while (i < this.shopTypeListObject.length)
        {
          DPObject localDPObject = this.shopTypeListObject[i];
          localObject1 = LayoutInflater.from(getActivity()).inflate(R.layout.mc_shop_type_item, localTableRow, false);
          localObject2 = localObject1;
          Object localObject3 = paramViewGroup;
          if (paramViewGroup == null)
            localObject3 = localObject1;
          ((View)localObject1).setOnClickListener(this);
          ((TextView)((View)localObject1).findViewById(16908308)).setText(localDPObject.getString("Name") + "");
          ((TextView)((View)localObject1).findViewById(16908309)).setText(localDPObject.getInt("ShopCount") + "");
          ((View)localObject1).setTag(localDPObject);
          ((View)localObject1).setBackgroundResource(R.drawable.mc_itemlist_2st_selected_line);
          localTableRow.addView((View)localObject1);
          i += 1;
          paramViewGroup = (ViewGroup)localObject3;
        }
        localObject1 = paramViewGroup;
        if (localObject2 != null)
          localObject2.setBackgroundResource(R.drawable.itemlist_2st_selected);
      }
    }
    for (localObject1 = paramViewGroup; ; localObject1 = localObject2)
    {
      if (localObject1 != null)
      {
        ((View)localObject1).performClick();
        this.handler.removeMessages(3);
      }
      return;
      label244: sortProductByShopType(this.shopTypeListObject[0]);
    }
  }

  private void showPinedView()
  {
    if ((this.rootView == null) || (this.addCardButtonView == null));
    do
      return;
    while (this.pined);
    this.pined = true;
    this.addCardButtonView2.setVisibility(0);
  }

  private void sortProductByShopType(DPObject paramDPObject)
  {
    int j = paramDPObject.getInt("ID");
    if (j == this.currentShopType);
    do
    {
      return;
      this.currentShopType = j;
      this.currentShopTypeProductList.clear();
      paramDPObject = this.productListObject;
      int k = paramDPObject.length;
      int i = 0;
      Object localObject;
      while (i < k)
      {
        localObject = paramDPObject[i];
        if (localObject.getInt("ShopType") == j)
          this.currentShopTypeProductList.add(localObject);
        i += 1;
      }
      this.currentShopTypeFloorIndexList.clear();
      if (this.floorIndexListObject != null)
      {
        paramDPObject = this.floorIndexListObject;
        j = paramDPObject.length;
        i = 0;
        while (i < j)
        {
          localObject = paramDPObject[i];
          k = localObject.getInt("ID");
          Iterator localIterator = this.currentShopTypeProductList.iterator();
          while (localIterator.hasNext())
          {
            if (((DPObject)localIterator.next()).getInt("Floor") != k)
              continue;
            this.currentShopTypeFloorIndexList.add(localObject);
          }
          i += 1;
        }
      }
      if (this.floorInfoAdapter == null)
        continue;
      this.floorInfoAdapter.reset();
    }
    while (this.floorIndexAdapter == null);
    this.floorIndexAdapter.reset();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.footView = new FrameLayout(getActivity());
    this.footView.setBackgroundResource(17170445);
    this.floorInfoListView.addFooterView(this.footView, null, false);
    setupProductListHeaderView();
    this.cardInfoView.setData(this.cardObject);
    this.floorInfoListView.addHeaderView(this.cardInfoView, null, false);
    this.addCardButtonView = createAddCardButtonView();
    this.addCardButtonView2 = createAddCardButtonView();
    if (this.addCardButtonView != null)
    {
      this.floorInfoListView.addHeaderView(this.addCardButtonView);
      this.floorInfoListView.addHeaderView(newOneEmptyView(EmptyHeaderHolder.DEFAULT_HEIGHT));
    }
    if (this.addCardButtonView2 != null)
    {
      this.rootView.addView(this.addCardButtonView2);
      this.addCardButtonView2.setVisibility(8);
    }
    if ((this.source != 12) && (this.cardObject.getInt("CardType") != 3))
    {
      this.power.setPower(this.cardObject.getInt("Power"));
      this.floorInfoListView.addHeaderView(this.shopInfoView);
      this.floorInfoListView.addHeaderView(newOneEmptyView(EmptyHeaderHolder.DEFAULT_HEIGHT));
    }
    while (true)
    {
      if ((this.cardObject.getArray("ProductAList") != null) && (this.cardObject.getArray("ProductAList").length > 0))
      {
        this.floorInfoListView.addHeaderView((ViewGroup)this.productListFlipper.getParent(), null, false);
        this.floorInfoListView.addHeaderView(newOneEmptyView(EmptyHeaderHolder.DEFAULT_HEIGHT));
      }
      this.shopTypeListObject = this.cardObject.getArray("ShopTypeList");
      if ((this.shopTypeListObject != null) && (this.shopTypeListObject.length > 1))
        this.floorInfoListView.addHeaderView(this.shopTypeHeaderLayout, null, false);
      if ((this.floorIndexListObject != null) && (this.floorIndexListObject.length > 0))
      {
        this.floorIndexAdapter = new FloorIndexAdapter(null);
        this.floorIndexListView.setAdapter(this.floorIndexAdapter);
      }
      if (this.productListObject != null)
      {
        this.floorInfoAdapter = new Adapter();
        this.floorInfoListView.setAdapter(this.floorInfoAdapter);
      }
      setupShopTypeLayout(this.shopTypeTableLayout);
      if (this.addCardButtonView != null)
        setupScrollAddButtonListener();
      return;
      this.power.setVisibility(4);
    }
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    this.floorIndexListObject = this.cardObject.getArray("FloorList");
    this.productListObject = this.cardObject.getArray("ProductList");
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.card_shop_info)
      gotoShopInfo();
    do
      return;
    while ((paramView.getParent() == null) || (!(paramView.getParent() instanceof TableRow)) || (this.currentSelectedShopTypeView == paramView));
    this.currentSelectedShopTypeView = paramView;
    resetShopTypeLayoutState((ViewGroup)(ViewGroup)paramView.getParent());
    ((TextView)paramView.findViewById(16908308)).setTextColor(getResources().getColor(R.color.orange_red));
    ((TextView)paramView.findViewById(16908309)).setTextColor(getResources().getColor(R.color.orange_red));
    statisticsEvent("mycard5", "mycard5_detail_tag", ((TextView)paramView.findViewById(16908308)).getText() + "|" + this.cardObject.getInt("CardType"), 0);
    if (this.pined)
    {
      Message localMessage = Message.obtain();
      localMessage.what = 3;
      localMessage.arg1 = (this.floorInfoListView.getHeaderViewsCount() - 1);
      this.handler.removeMessages(3);
      this.handler.sendMessageDelayed(localMessage, 50L);
    }
    sortProductByShopType((DPObject)paramView.getTag());
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.rootView = ((FrameLayout)paramLayoutInflater.inflate(R.layout.mc_noadded_mallcard_layout, paramViewGroup, false));
    this.indexView = ((TextView)this.rootView.findViewById(16908310));
    this.floorInfoListView = ((ListView)this.rootView.findViewById(16908298));
    this.floorInfoListView.setHeaderDividersEnabled(false);
    this.floorInfoListView.setSelector(R.drawable.transparent_bg);
    this.floorIndexListView = ((ListView)this.rootView.findViewById(R.id.index_list));
    this.cardInfoView = ((MemberCardListItem)paramLayoutInflater.inflate(R.layout.no_added_card_info_layout, this.floorInfoListView, false));
    this.shopInfoView = paramLayoutInflater.inflate(R.layout.card_shop_info, this.floorInfoListView, false);
    this.shopInfoView.setOnClickListener(this);
    this.power = ((ShopPower)this.shopInfoView.findViewById(R.id.shop_power));
    this.productListFlipper = ((FlipperPager)paramLayoutInflater.inflate(R.layout.mc_product_flipper_layout, this.floorInfoListView, false).findViewById(R.id.product_list_pager));
    this.shopTypeHeaderLayout = ((FrameLayout)paramLayoutInflater.inflate(R.layout.mc_shop_type_layout, this.floorInfoListView, false));
    this.shopTypeTableLayout = ((TableLayout)this.shopTypeHeaderLayout.findViewById(R.id.table_layout));
    this.floorIndexListView.setOnItemClickListener(this);
    this.floorIndexListView.setOnTouchListener(new View.OnTouchListener()
    {
      private static final int marginRight = 20;

      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        int j = NoAddedMallCardFragment.this.floorIndexListView.pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
        int k;
        int m;
        if ((j > -1) && (j < NoAddedMallCardFragment.this.floorIndexListView.getCount()))
        {
          paramView = (DPObject)NoAddedMallCardFragment.this.floorIndexAdapter.getItem(j);
          k = NoAddedMallCardFragment.this.floorInfoAdapter.object2Position(paramView);
          m = NoAddedMallCardFragment.this.floorInfoListView.getHeaderViewsCount();
          if (!NoAddedMallCardFragment.this.pined)
            break label248;
        }
        label248: for (int i = 1; ; i = 0)
        {
          NoAddedMallCardFragment.this.handler.removeMessages(3);
          paramView = Message.obtain();
          paramView.what = 3;
          paramView.arg1 = (m + k - i);
          NoAddedMallCardFragment.this.handler.sendMessageDelayed(paramView, 10L);
          if ((Build.VERSION.SDK_INT >= 11) && (0.0F < paramMotionEvent.getY()) && (paramMotionEvent.getY() < NoAddedMallCardFragment.this.floorIndexListView.getHeight()))
          {
            NoAddedMallCardFragment.this.handler.removeMessages(1);
            NoAddedMallCardFragment.this.handler.sendEmptyMessage(2);
            NoAddedMallCardFragment.this.indexView.setText(((DPObject)NoAddedMallCardFragment.this.floorIndexListView.getItemAtPosition(j)).getString("Name"));
          }
          NoAddedMallCardFragment.this.handler.sendEmptyMessageDelayed(1, 1500L);
          return false;
        }
      }
    });
    return this.rootView;
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramAdapterView == this.floorIndexListView);
    do
    {
      do
        return;
      while (paramAdapterView != this.floorInfoListView);
      paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    }
    while ((!(paramAdapterView instanceof DPObject)) || (!((DPObject)paramAdapterView).isClass("Product")));
    gotoProductDetail(((DPObject)paramAdapterView).getInt("ProductID"));
  }

  private class Adapter extends BasicAdapter
  {
    private float density = NoAddedMallCardFragment.this.getResources().getDisplayMetrics().density;
    private ArrayList<DPObject> list = new ArrayList();

    public Adapter()
    {
      bulidAdapter();
    }

    public void bulidAdapter()
    {
      DPObject localDPObject1;
      if (NoAddedMallCardFragment.this.currentShopTypeFloorIndexList.size() == 0)
      {
        localObject = NoAddedMallCardFragment.this.currentShopTypeProductList.iterator();
        while (((Iterator)localObject).hasNext())
        {
          localDPObject1 = (DPObject)((Iterator)localObject).next();
          this.list.add(localDPObject1);
        }
      }
      Object localObject = NoAddedMallCardFragment.this.currentShopTypeFloorIndexList.iterator();
      while (((Iterator)localObject).hasNext())
      {
        localDPObject1 = (DPObject)((Iterator)localObject).next();
        this.list.add(localDPObject1);
        Iterator localIterator = NoAddedMallCardFragment.this.currentShopTypeProductList.iterator();
        while (localIterator.hasNext())
        {
          DPObject localDPObject2 = (DPObject)localIterator.next();
          if (localDPObject1.getInt("ID") != localDPObject2.getInt("Floor"))
            continue;
          this.list.add(localDPObject2);
        }
      }
      localObject = NoAddedMallCardFragment.this.footView.getLayoutParams();
      if (NoAddedMallCardFragment.this.pined)
        if (localObject != null)
        {
          int j = NoAddedMallCardFragment.this.floorInfoListView.getHeight() - NoAddedMallCardFragment.this.shopTypeHeaderLayout.getHeight() - ViewUtils.dip2px(NoAddedMallCardFragment.this.getActivity(), 25.0F) * NoAddedMallCardFragment.this.currentShopTypeFloorIndexList.size() - ViewUtils.dip2px(NoAddedMallCardFragment.this.getActivity(), 25.0F) * (this.list.size() - NoAddedMallCardFragment.this.currentShopTypeFloorIndexList.size());
          int i = j;
          if (j < 0)
            i = 0;
          ((ViewGroup.LayoutParams)localObject).height = i;
          NoAddedMallCardFragment.this.footView.setLayoutParams((ViewGroup.LayoutParams)localObject);
        }
      do
        return;
      while (localObject == null);
      ((ViewGroup.LayoutParams)localObject).height = 0;
      NoAddedMallCardFragment.this.footView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    }

    protected View createCategoryItem(String paramString)
    {
      View localView = LayoutInflater.from(NoAddedMallCardFragment.this.getActivity()).inflate(R.layout.mc_floor_index_item, NoAddedMallCardFragment.this.floorInfoListView, false);
      ((TextView)localView).setTypeface(null, 1);
      ((TextView)localView.findViewById(16908308)).setText(paramString);
      return localView;
    }

    public int getCount()
    {
      return this.list.size();
    }

    public Object getItem(int paramInt)
    {
      return this.list.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        DPObject localDPObject = (DPObject)localObject;
        if (localDPObject.isClass("Product"))
        {
          if ((paramView instanceof ViewGroup));
          for (paramView = (ViewGroup)paramView; ; paramView = null)
          {
            localObject = paramView;
            if (paramView == null)
              localObject = (ViewGroup)LayoutInflater.from(NoAddedMallCardFragment.this.getActivity()).inflate(R.layout.mc_product_item, paramViewGroup, false);
            ((TextView)((ViewGroup)localObject).findViewById(16908310)).setText(localDPObject.getString("ProductName"));
            ((TextView)((ViewGroup)localObject).findViewById(16908290)).setText(localDPObject.getString("ProductDesc"));
            ((ImageView)((ViewGroup)localObject).findViewById(R.id.arrow_view)).setVisibility(8);
            return localObject;
          }
        }
        if (localDPObject.isClass("Floor"))
          return createCategoryItem(localDPObject.getString("Name"));
        return LayoutInflater.from(NoAddedMallCardFragment.this.getActivity()).inflate(17367043, NoAddedMallCardFragment.this.floorInfoListView, false);
      }
      return (View)new TextView(NoAddedMallCardFragment.this.getActivity());
    }

    public int getViewTypeCount()
    {
      return 2;
    }

    public int object2Position(DPObject paramDPObject)
    {
      if (paramDPObject == null)
        return -1;
      int i = this.list.indexOf(paramDPObject);
      if (i > -1);
      while (true)
      {
        return i;
        i = -1;
      }
    }

    public void reset()
    {
      this.list.clear();
      bulidAdapter();
      notifyDataSetChanged();
    }
  }

  private class FlipperAdapter extends FlipperPager.FlipperPagerAdapter
    implements View.OnClickListener
  {
    DPObject[] productList;

    public FlipperAdapter(DPObject[] arg2)
    {
      Object localObject;
      this.productList = localObject;
    }

    public int getCount()
    {
      return this.productList.length;
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      View localView = LayoutInflater.from(NoAddedMallCardFragment.this.getActivity()).inflate(R.layout.mc_product_item, paramViewGroup, false);
      localView.setOnClickListener(this);
      localView.setTag(this.productList[paramInt]);
      ((TextView)localView.findViewById(16908310)).setText(this.productList[paramInt].getString("ProductName"));
      ((TextView)localView.findViewById(16908290)).setText(this.productList[paramInt].getString("ProductDesc"));
      ((ImageView)localView.findViewById(R.id.arrow_view)).setVisibility(8);
      paramViewGroup.addView(localView, 0);
      return localView;
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }

    public void onClick(View paramView)
    {
      paramView = paramView.getTag();
      if (((paramView instanceof DPObject)) && (((DPObject)paramView).isClass("Product")));
    }
  }

  private class FloorIndexAdapter extends BasicAdapter
  {
    private FloorIndexAdapter()
    {
    }

    public int getCount()
    {
      return NoAddedMallCardFragment.this.currentShopTypeFloorIndexList.size();
    }

    public Object getItem(int paramInt)
    {
      return NoAddedMallCardFragment.this.currentShopTypeFloorIndexList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject2 = getItem(paramInt);
      if ((localObject2 instanceof DPObject))
      {
        if ((paramView instanceof TextView));
        for (paramView = (TextView)paramView; ; paramView = null)
        {
          Object localObject1 = paramView;
          if (paramView == null)
            localObject1 = (TextView)LayoutInflater.from(NoAddedMallCardFragment.this.getActivity()).inflate(R.layout.list_index_item, paramViewGroup, false);
          ((TextView)localObject1).setText(((DPObject)localObject2).getString("Name"));
          return localObject1;
        }
      }
      return (View)new TextView(NoAddedMallCardFragment.this.getActivity());
    }

    public void reset()
    {
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.NoAddedMallCardFragment
 * JD-Core Version:    0.6.0
 */
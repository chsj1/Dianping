package com.dianping.membercard.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.util.ImageUtils;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.membercard.MemberCardInfoActivity;
import com.dianping.membercard.MyCardActivity;
import com.dianping.membercard.utils.EmptyHeaderHolder;
import com.dianping.membercard.view.CardChainShopItem;
import com.dianping.membercard.view.CardScoreItem;
import com.dianping.membercard.view.MemberCardImageView;
import com.dianping.membercard.view.MemberCardItem;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.FlipAnimator;
import com.dianping.widget.FlipperPager;
import com.dianping.widget.FlipperPager.FlipperPagerAdapter;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.google.zxing.WriterException;
import java.util.ArrayList;
import java.util.Iterator;

public class MallCardFragment extends CardFragment
  implements AdapterView.OnItemClickListener, View.OnClickListener, CardDetailRequestTask.CardDetailRequestHandler
{
  private static final int CARD_TYPE_MALL = 2;
  private static final String UPDATE_USER_NAME = "com.dianping.action.UPDATE_USER_INFO";
  private View backView;
  private CardChainShopItem cardChainShopItem;
  private CardDetailRequestTask cardDetailRequestTask = null;
  private FrameLayout cardHeaderView;
  private MemberCardItem cardView;
  private int currentShopType = -1;
  private ArrayList<DPObject> currentShopTypeFloorIndexList = new ArrayList();
  private ArrayList<DPObject> currentShopTypeProductList = new ArrayList();
  private FrameLayout floatShopTypeHeaderLayout;
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
        return;
      case 3:
      }
      MallCardFragment.this.floorInfoListView.setSelection(paramMessage.arg1);
    }
  };
  private int index;
  private FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -2, 49);
  private LinearLayout mCardHeaderLayout;
  private LinearLayout mCardScoreLayout;
  private TextView mCardTips;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.action.UPDATE_USER_INFO"))
      {
        paramContext = paramIntent.getStringExtra("username");
        if ((MallCardFragment.this.cardObject != null) && (MallCardFragment.this.cardView != null))
        {
          MallCardFragment.this.cardObject = MallCardFragment.this.cardObject.edit().putString("UserName", paramContext).generate();
          MallCardFragment.this.cardView.updateUserNameOnly(MallCardFragment.this.cardObject);
        }
      }
    }
  };
  private boolean pined = false;
  private FlipperPager productListFlipper;
  private DPObject[] productListObject;
  PullToRefreshListView pullToRefreshListView;
  private ImageView qrcodeImage;
  Handler refreshHandler;
  private Resources resources;
  private FrameLayout rootView;
  private FrameLayout shopTypeHeaderLayout;
  private DPObject[] shopTypeListObject;
  private TableLayout shopTypeTableLayout;

  private int dip2px(float paramFloat)
  {
    return (int)(paramFloat * getResources().getDisplayMetrics().density + 0.5F);
  }

  private void dismissPinedView()
  {
    if (this.pined)
    {
      this.pined = false;
      this.lp.gravity = 17;
      this.rootView.removeView(this.shopTypeTableLayout);
      this.shopTypeHeaderLayout.addView(this.shopTypeTableLayout, this.lp);
    }
  }

  private void initChainCardsSelectView()
  {
    if (this.cardChainShopItem != null)
      return;
    this.cardChainShopItem = new CardChainShopItem(this.floorInfoListView.getContext());
    this.cardChainShopItem.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MallCardFragment.this.gotoBranchCardList();
      }
    });
  }

  private boolean isChainMallCard(int paramInt, String paramString, DPObject[] paramArrayOfDPObject)
  {
    return (!TextUtils.isEmpty(paramString)) && (paramArrayOfDPObject != null) && (paramArrayOfDPObject.length > 1) && (paramInt == 2);
  }

  private View newOneEmptyView(int paramInt)
  {
    View localView = new EmptyHeaderHolder(paramInt).inflate(getActivity(), null, this.floorInfoListView);
    localView.setBackgroundResource(R.drawable.main_background);
    return localView;
  }

  private void resetShopTypeLayoutState(ViewGroup paramViewGroup, int paramInt)
  {
    int i = 0;
    while (true)
    {
      if (i < paramViewGroup.getChildCount());
      try
      {
        View localView = paramViewGroup.getChildAt(i);
        if (((ViewGroup)localView.getParent()).getChildAt(((ViewGroup)localView.getParent()).getChildCount() - 1) == localView)
          localView.setBackgroundResource(R.drawable.itemlist_2st_selected);
        while (this.resources != null)
          if (paramInt == i)
          {
            ((TextView)localView.findViewById(16908308)).setTextColor(this.resources.getColor(R.color.orange_red));
            ((TextView)localView.findViewById(16908309)).setTextColor(this.resources.getColor(R.color.orange_red));
            break;
            localView.setBackgroundResource(R.drawable.mc_itemlist_2st_selected_line);
            continue;
          }
          else
          {
            ((TextView)localView.findViewById(16908308)).setTextColor(this.resources.getColor(R.color.deep_gray));
            ((TextView)localView.findViewById(16908309)).setTextColor(this.resources.getColor(R.color.deep_gray));
            break;
            return;
          }
        label174: i += 1;
      }
      catch (Exception localException)
      {
        break label174;
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
          break label259;
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
          ((View)localObject1).setTag(2147483647, Integer.valueOf(i));
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
        ((View)localObject1).post(new Runnable((View)localObject1)
        {
          public void run()
          {
            this.val$view.performClick();
            MallCardFragment.this.handler.removeMessages(3);
          }
        });
      return;
      label259: sortProductByShopType(this.shopTypeListObject[0]);
    }
  }

  private void showPinedView()
  {
    if (!this.pined)
    {
      this.pined = true;
      this.lp.gravity = 49;
      this.shopTypeHeaderLayout.removeView(this.shopTypeTableLayout);
      this.rootView.addView(this.shopTypeTableLayout, this.lp);
    }
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

  private void tryAddChainCardsSelectView()
  {
    if (isChainMallCard(this.cardObject.getInt("CardType"), this.cardObject.getString("MemberCardGroupID"), this.cardObject.getArray("SubCardList")))
    {
      initChainCardsSelectView();
      this.cardChainShopItem.setChainShop(this.cardObject);
      this.floorInfoListView.addHeaderView(this.cardChainShopItem, null, false);
      this.floorInfoListView.addHeaderView(newOneEmptyView(EmptyHeaderHolder.DEFAULT_HEIGHT));
    }
  }

  public void closeQRView()
  {
    if ((this.cardObject.getBoolean("IsQRCodeOn")) && (this.cardView.getVisibility() == 4))
    {
      FlipAnimator localFlipAnimator = new FlipAnimator(this.cardView, this.backView, this.cardView.getWidth() / 2, this.cardView.getHeight() / 2);
      localFlipAnimator.reverse();
      this.cardView.clearAnimation();
      this.backView.clearAnimation();
      this.cardHeaderView.startAnimation(localFlipAnimator);
    }
  }

  public TextView createShowCardTips(CharSequence paramCharSequence)
  {
    TextView localTextView = new TextView(getActivity());
    Resources localResources = getResources();
    localTextView.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
    localTextView.setPadding(0, ViewUtils.dip2px(getActivity(), 2.0F), 0, localResources.getDimensionPixelSize(R.dimen.mc_item_split_height));
    localTextView.setBackgroundResource(R.color.transparent);
    localTextView.setGravity(17);
    localTextView.setTextSize(0, localResources.getDimension(R.dimen.text_medium_1));
    localTextView.setTextColor(localResources.getColor(R.color.light_gray));
    localTextView.setText(paramCharSequence);
    localTextView.setEnabled(false);
    localTextView.setClickable(true);
    return localTextView;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.cardDetailRequestTask != null)
      this.cardDetailRequestTask.load(this);
    while (true)
    {
      refresh();
      return;
      this.cardDetailRequestTask = new CardDetailRequestTask(this);
    }
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    this.floorIndexListObject = this.cardObject.getArray("FloorList");
    this.productListObject = this.cardObject.getArray("ProductList");
  }

  public void onCardDetailRequestFailed(MApiResponse paramMApiResponse, int paramInt)
  {
    this.pullToRefreshListView.onRefreshComplete();
  }

  public void onCardDetailRequestFinish(DPObject paramDPObject, CardDetailRequestTask.ResponseDataType paramResponseDataType)
  {
    if (paramResponseDataType == CardDetailRequestTask.ResponseDataType.CURRENT_CARD_INFO)
    {
      this.pullToRefreshListView.onRefreshComplete();
      new Handler().postDelayed(new Runnable(paramDPObject)
      {
        public void run()
        {
          if (MallCardFragment.this.refreshHandler != null)
          {
            Message localMessage = Message.obtain();
            Bundle localBundle = new Bundle();
            localBundle.putParcelable("card", this.val$cardObject);
            localMessage.setData(localBundle);
            MallCardFragment.this.refreshHandler.sendMessage(localMessage);
          }
        }
      }
      , 300L);
      return;
    }
    this.pullToRefreshListView.onRefreshComplete();
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.container)
      super.onClick(paramView);
    do
      return;
    while ((paramView.getParent() == null) || (!(paramView.getParent() instanceof TableRow)));
    this.index = ((Integer)paramView.getTag(2147483647)).intValue();
    resetShopTypeLayoutState((ViewGroup)(ViewGroup)this.shopTypeTableLayout.findViewById(R.id.shop_type_list), this.index);
    resetShopTypeLayoutState((ViewGroup)this.floatShopTypeHeaderLayout.findViewById(R.id.table_layout).findViewById(R.id.shop_type_list), this.index);
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

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.UPDATE_USER_INFO");
    registerReceiver(this.mReceiver, paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.resources = getResources();
    this.rootView = ((FrameLayout)paramLayoutInflater.inflate(R.layout.mc_mallcard_layout, paramViewGroup, false));
    this.pullToRefreshListView = ((PullToRefreshListView)this.rootView.findViewById(R.id.card_list));
    this.pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    this.pullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
      {
        if ((MallCardFragment.this.cardDetailRequestTask != null) && (MallCardFragment.this.getActivity() != null))
        {
          int i = MallCardFragment.this.cardObject.getInt("MemberCardID");
          MallCardFragment.this.cardDetailRequestTask.doRequest(i, -1);
        }
        MallCardFragment.this.statisticsEvent("mycard5", "mycard5_detail_refresh", null, 0);
      }
    });
    this.floorInfoListView = this.pullToRefreshListView;
    this.floorInfoListView.setHeaderDividersEnabled(false);
    this.floorInfoListView.setSelector(R.drawable.list_item);
    this.floorIndexListView = ((ListView)this.rootView.findViewById(R.id.index_list));
    this.cardHeaderView = ((FrameLayout)paramLayoutInflater.inflate(R.layout.membercard_container, this.floorInfoListView, false));
    this.cardHeaderView.setOnClickListener(this);
    this.cardView = ((MemberCardItem)this.cardHeaderView.findViewById(R.id.card_front));
    this.backView = this.cardHeaderView.findViewById(R.id.card_back);
    this.qrcodeImage = ((ImageView)this.backView.findViewById(R.id.card_qrcode));
    this.mCardTips = createShowCardTips("");
    this.mCardHeaderLayout = new LinearLayout(this.floorInfoListView.getContext());
    paramViewGroup = new AbsListView.LayoutParams(-1, -2);
    this.mCardHeaderLayout.setLayoutParams(paramViewGroup);
    this.mCardHeaderLayout.setOrientation(1);
    this.mCardHeaderLayout.setBackgroundResource(R.drawable.main_background);
    this.mCardHeaderLayout.setLayoutParams(paramViewGroup);
    this.mCardHeaderLayout.addView(this.cardHeaderView);
    this.mCardHeaderLayout.addView(this.mCardTips);
    if (this.cardObject.getObject("CardScore") != null)
    {
      paramViewGroup = new CardScoreItem(getActivity());
      paramViewGroup.setCardScore(this.cardObject.getObject("CardScore"), 2);
      boolean bool = paramViewGroup.isBinded();
      paramViewGroup.setOnClickListener(new View.OnClickListener(bool)
      {
        public void onClick(View paramView)
        {
          MallCardFragment localMallCardFragment = MallCardFragment.this;
          if (this.val$isBinded)
          {
            paramView = "1";
            localMallCardFragment.statisticsEvent("mycard5", "mycard5_detail_score", paramView, 0);
            paramView = MallCardFragment.this.cardObject.getObject("CardScore").getString("ScoreUrl");
            if (!(MallCardFragment.this.getActivity() instanceof MyCardActivity))
              break label74;
            MallCardFragment.this.gotoScoreDetail(paramView, CardFragment.FROM_MYCARDFRAGMENT);
          }
          label74: 
          do
          {
            return;
            paramView = "0";
            break;
          }
          while (!(MallCardFragment.this.getActivity() instanceof MemberCardInfoActivity));
          MallCardFragment.this.gotoScoreDetail(paramView, CardFragment.FROM_MEMBERCARDINFOACTIVITY);
        }
      });
      setCardScore(true, bool);
      this.mCardHeaderLayout.addView(paramViewGroup);
      this.mCardHeaderLayout.addView(newOneEmptyView(EmptyHeaderHolder.DEFAULT_HEIGHT));
    }
    this.productListFlipper = ((FlipperPager)paramLayoutInflater.inflate(R.layout.mc_product_flipper_layout, this.floorInfoListView, false).findViewById(R.id.product_list_pager));
    this.shopTypeHeaderLayout = ((FrameLayout)paramLayoutInflater.inflate(R.layout.mc_shop_type_layout, this.floorInfoListView, false));
    this.floatShopTypeHeaderLayout = ((FrameLayout)this.rootView.findViewById(R.id.float_head_shop_type));
    this.shopTypeTableLayout = ((TableLayout)this.shopTypeHeaderLayout.findViewById(R.id.table_layout));
    this.floorIndexListView.setOnItemClickListener(this);
    this.floorInfoListView.setOnItemClickListener(this);
    this.floorInfoListView.setOnScrollListener(new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
      {
        if ((MallCardFragment.this.shopTypeListObject != null) && (MallCardFragment.this.shopTypeListObject.length > 1))
        {
          if (paramInt1 >= MallCardFragment.this.floorInfoListView.getHeaderViewsCount() - 1)
            break label46;
          MallCardFragment.this.dismissPinedView();
        }
        label46: 
        do
          return;
        while (paramInt1 != MallCardFragment.this.floorInfoListView.getHeaderViewsCount() - 1);
        MallCardFragment.this.showPinedView();
      }

      public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
      {
      }
    });
    return this.rootView;
  }

  public void onDestroy()
  {
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    super.onDestroy();
  }

  public void onDetach()
  {
    if (this.cardDetailRequestTask != null)
      this.cardDetailRequestTask.abortRequest();
    super.onDetach();
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
    statisticsEvent("mycard5", "mycard5_detail_shopitem", this.cardObject.getInt("CardType") + "|" + ((DPObject)paramAdapterView).getInt("ProductID"), 0);
  }

  public void refresh()
  {
    this.footView = new FrameLayout(getActivity());
    this.footView.setBackgroundResource(17170445);
    this.floorInfoListView.addFooterView(this.footView, null, false);
    setupProductListHeaderView();
    this.floorInfoListView.setHeaderDividersEnabled(false);
    this.floorInfoListView.addHeaderView(this.mCardHeaderLayout, null, false);
    tryAddChainCardsSelectView();
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
    setupShopTypeLayout((ViewGroup)this.floatShopTypeHeaderLayout.findViewById(R.id.table_layout));
    this.cardView.setData(this.cardObject);
    ((MemberCardImageView)this.backView.findViewById(R.id.card_image)).setImageResource(R.drawable.card_back);
    Object localObject2 = this.cardObject.getString("QRCode");
    int i = ViewUtils.dip2px(getActivity(), 120.0F);
    int j = ViewUtils.dip2px(getActivity(), 120.0F);
    if ((!this.cardObject.getBoolean("IsQRCodeOn")) || (TextUtils.isEmpty((CharSequence)localObject2)))
    {
      this.qrcodeImage.setVisibility(8);
      return;
    }
    Object localObject1 = null;
    try
    {
      localObject2 = ImageUtils.encodeAsBitmap((String)localObject2, i, j);
      localObject1 = localObject2;
      if (localObject1 != null)
        this.qrcodeImage.setImageBitmap(localObject1);
      this.qrcodeImage.setVisibility(0);
      return;
    }
    catch (WriterException localWriterException)
    {
      while (true)
        localWriterException.printStackTrace();
    }
  }

  public void refresh(DPObject paramDPObject)
  {
    this.cardObject = paramDPObject;
    refresh();
  }

  public void refreshByCardId(int paramInt)
  {
    this.cardObject = this.cardObject.edit().putInt("MemberCardID", paramInt).generate();
    refreshUI();
  }

  public void refreshUI()
  {
    this.pullToRefreshListView.setRefreshing();
  }

  public void setRefreshHander(Handler paramHandler)
  {
    this.refreshHandler = paramHandler;
  }

  private class Adapter extends BasicAdapter
  {
    private ArrayList<DPObject> list = new ArrayList();

    public Adapter()
    {
      bulidAdapter();
    }

    public void bulidAdapter()
    {
      int i = 0;
      DPObject localDPObject1;
      if (MallCardFragment.this.currentShopTypeFloorIndexList.size() == 0)
      {
        localObject = MallCardFragment.this.currentShopTypeProductList.iterator();
        while (((Iterator)localObject).hasNext())
        {
          localDPObject1 = (DPObject)((Iterator)localObject).next();
          this.list.add(localDPObject1);
        }
      }
      Object localObject = MallCardFragment.this.currentShopTypeFloorIndexList.iterator();
      while (((Iterator)localObject).hasNext())
      {
        localDPObject1 = (DPObject)((Iterator)localObject).next();
        this.list.add(localDPObject1);
        Iterator localIterator = MallCardFragment.this.currentShopTypeProductList.iterator();
        while (localIterator.hasNext())
        {
          DPObject localDPObject2 = (DPObject)localIterator.next();
          if (localDPObject1.getInt("ID") != localDPObject2.getInt("Floor"))
            continue;
          this.list.add(localDPObject2);
        }
      }
      localObject = MallCardFragment.this.footView.getLayoutParams();
      if (MallCardFragment.this.pined)
        if (localObject != null)
        {
          j = MallCardFragment.this.floorInfoListView.getHeight() - MallCardFragment.this.shopTypeHeaderLayout.getHeight() - MallCardFragment.this.dip2px(25.0F) * MallCardFragment.this.currentShopTypeFloorIndexList.size() - MallCardFragment.this.dip2px(50.0F) * (this.list.size() - MallCardFragment.this.currentShopTypeFloorIndexList.size());
          i = j;
          if (j < 0)
            i = 0;
          ((ViewGroup.LayoutParams)localObject).height = i;
          MallCardFragment.this.footView.setLayoutParams((ViewGroup.LayoutParams)localObject);
        }
      do
        return;
      while (localObject == null);
      int j = MallCardFragment.this.floorInfoListView.getHeight() - MallCardFragment.this.shopTypeHeaderLayout.getHeight() - MallCardFragment.this.dip2px(25.0F) * MallCardFragment.this.currentShopTypeFloorIndexList.size() - MallCardFragment.this.dip2px(50.0F) * (this.list.size() - MallCardFragment.this.currentShopTypeFloorIndexList.size());
      if (j < 0);
      while (true)
      {
        ((ViewGroup.LayoutParams)localObject).height = i;
        MallCardFragment.this.footView.setLayoutParams((ViewGroup.LayoutParams)localObject);
        return;
        i = j;
      }
    }

    protected View createCategoryItem(String paramString)
    {
      View localView = LayoutInflater.from(MallCardFragment.this.getActivity()).inflate(R.layout.mc_floor_index_item, MallCardFragment.this.floorInfoListView, false);
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
              localObject = (ViewGroup)LayoutInflater.from(MallCardFragment.this.getActivity()).inflate(R.layout.mc_product_item, paramViewGroup, false);
            ((TextView)((ViewGroup)localObject).findViewById(16908310)).setText(localDPObject.getString("ProductName"));
            ((TextView)((ViewGroup)localObject).findViewById(16908290)).setText(localDPObject.getString("ProductDesc"));
            ((ImageView)((ViewGroup)localObject).findViewById(R.id.arrow_view)).setVisibility(0);
            return localObject;
          }
        }
        if (localDPObject.isClass("Floor"))
          return createCategoryItem(localDPObject.getString("Name"));
        return LayoutInflater.from(MallCardFragment.this.getActivity()).inflate(17367043, MallCardFragment.this.floorInfoListView, false);
      }
      return (View)new TextView(MallCardFragment.this.getActivity());
    }

    public int getViewTypeCount()
    {
      return 2;
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
      View localView = LayoutInflater.from(MallCardFragment.this.getActivity()).inflate(R.layout.mc_product_item, paramViewGroup, false);
      localView.setOnClickListener(this);
      localView.setTag(this.productList[paramInt]);
      ((TextView)localView.findViewById(16908310)).setText(this.productList[paramInt].getString("ProductName"));
      ((TextView)localView.findViewById(16908290)).setText(this.productList[paramInt].getString("ProductDesc"));
      ((ImageView)localView.findViewById(R.id.arrow_view)).setVisibility(0);
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
      if (((paramView instanceof DPObject)) && (((DPObject)paramView).isClass("Product")))
      {
        MallCardFragment.this.gotoProductDetail(((DPObject)paramView).getInt("ProductID"));
        MallCardFragment.this.statisticsEvent("mycard5", "mycard5_detail_activity", MallCardFragment.this.cardObject.getInt("CardType") + "|" + ((DPObject)paramView).getInt("ProductID"), 0);
      }
    }
  }

  private class FloorIndexAdapter extends BasicAdapter
  {
    private FloorIndexAdapter()
    {
    }

    public int getCount()
    {
      return MallCardFragment.this.currentShopTypeFloorIndexList.size();
    }

    public Object getItem(int paramInt)
    {
      return MallCardFragment.this.currentShopTypeFloorIndexList.get(paramInt);
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
            localObject1 = (TextView)LayoutInflater.from(MallCardFragment.this.getActivity()).inflate(R.layout.list_index_item, paramViewGroup, false);
          ((TextView)localObject1).setText(((DPObject)localObject2).getString("Name"));
          return localObject1;
        }
      }
      return (View)new TextView(MallCardFragment.this.getActivity());
    }

    public void reset()
    {
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.MallCardFragment
 * JD-Core Version:    0.6.0
 */
package com.dianping.membercard.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.adapter.MergeAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.membercard.MemberCardInfoActivity;
import com.dianping.membercard.MyCardActivity;
import com.dianping.membercard.model.JoinMCHandler;
import com.dianping.membercard.model.JoinMCHandler.OnJoinCardRequestHandlerListener;
import com.dianping.membercard.utils.EmptyHeaderHolder;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.membercard.utils.ProductListItemFactory;
import com.dianping.membercard.utils.ProductListItemStyle;
import com.dianping.membercard.utils.ProductListItemType;
import com.dianping.membercard.utils.ProductType;
import com.dianping.membercard.utils.ViewHolder;
import com.dianping.membercard.utils.ViewHolderFactory;
import com.dianping.membercard.view.CardChainShopItem;
import com.dianping.membercard.view.CardScoreItem;
import com.dianping.membercard.view.MemberCardImageView;
import com.dianping.membercard.view.MemberCardItem;
import com.dianping.membercard.view.ProductChoiceItem;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.FlipAnimator;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.util.ArrayList;

public class MemberCardFragment extends CardFragment
  implements AdapterView.OnItemClickListener, JoinMCHandler.OnJoinCardRequestHandlerListener, CardFragment.refreshMemberCardHandler, CardDetailRequestTask.CardDetailRequestHandler
{
  private static int REFRESH_MSG = 0;
  private static final String UPDATE_USER_NAME = "com.dianping.action.UPDATE_USER_INFO";
  private View backView;
  private View cardContainer;
  private CardDetailRequestTask cardDetailRequestTask = null;
  private LinearLayout cardShops;
  private Adapter chainShopAdapter;
  private Adapter feedListAdapter;
  private DPObject[] feeds;
  boolean isGroup;
  private PullToRefreshListView listView;
  private MemberCardItem mCardInfoView;
  private JoinMCHandler mJoinMCHandler;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.action.UPDATE_USER_INFO"))
      {
        paramContext = paramIntent.getStringExtra("username");
        Log.v("UpdateName", "MemberCardFragment:" + paramContext);
        if ((MemberCardFragment.this.cardObject != null) && (MemberCardFragment.this.mCardInfoView != null))
        {
          MemberCardFragment.this.cardObject = MemberCardFragment.this.cardObject.edit().putString("UserName", paramContext).generate();
          MemberCardFragment.this.mCardInfoView.updateUserNameOnly(MemberCardFragment.this.cardObject);
        }
      }
    }
  };
  private MemberCard membercard;
  int membercardid;
  private MergeAdapter mergeAdapter;
  private Adapter pointAdapter;
  private Adapter prepaidCardAdapter;
  private Adapter productListAdapter;
  private DPObject[] products;
  private ImageView qrcodeImage;
  Handler refreshHandler;
  private Adapter scoreAdapter;
  private TextView shopCount;
  private Adapter tipsAdapter;
  String title;
  private int viewType;
  private Adapter vipAdapter;

  private void setGeneralProduct()
  {
    Object localObject = this.cardObject.getString("MemberCardGroupID");
    if (this.cardObject.getObject("CardScore") != null)
    {
      this.scoreAdapter = new Adapter("新积分");
      this.scoreAdapter.setItemList(this.cardObject.getObject("CardScore"));
      this.mergeAdapter.addAdapter(this.scoreAdapter);
      this.mergeAdapter.addAdapter(new EmptyAdapter(null));
    }
    if ((localObject != null) && (!TextUtils.isEmpty((CharSequence)localObject)))
    {
      this.chainShopAdapter = new Adapter("连锁店");
      localObject = new DPObject("ChainShop").edit().putString("SubTitle", this.cardObject.getString("SubTitle")).generate();
      this.chainShopAdapter.setItemList((DPObject)localObject);
      this.mergeAdapter.addAdapter(this.chainShopAdapter);
      this.mergeAdapter.addAdapter(new EmptyAdapter(null));
    }
    this.products = this.membercard.getGeneralCardList();
    if ((this.products != null) && (this.products.length > 0))
    {
      this.productListAdapter = new Adapter("");
      this.productListAdapter.setItemList(this.products);
      this.mergeAdapter.addAdapter(this.productListAdapter);
      this.mergeAdapter.addAdapter(new EmptyAdapter(null));
    }
    if (this.cardObject.getObject("CardPoint") != null)
    {
      this.pointAdapter = new Adapter("会员积分");
      this.pointAdapter.setItemList(this.cardObject.getObject("CardPoint"));
      this.mergeAdapter.addAdapter(this.pointAdapter);
      this.mergeAdapter.addAdapter(new EmptyAdapter(null));
    }
    if ((this.cardObject.getInt("CardLevel") == 2) || (this.cardObject.getInt("CardLevel") == 3))
    {
      this.vipAdapter = new Adapter("");
      this.vipAdapter.setItemList(this.membercard.getVIPCard());
      this.mergeAdapter.addAdapter(this.vipAdapter);
      this.mergeAdapter.addAdapter(new EmptyAdapter(null));
    }
  }

  private void setSavingProduct(DPObject paramDPObject)
  {
    this.vipAdapter = new Adapter("");
    this.vipAdapter.setItemList(paramDPObject);
    this.mergeAdapter.addAdapter(this.vipAdapter);
    this.mergeAdapter.addAdapter(new EmptyAdapter(null));
    this.vipAdapter.type = 8;
  }

  private void setTimesProduct(DPObject paramDPObject)
  {
    this.vipAdapter = new Adapter("");
    this.vipAdapter.setItemList(paramDPObject);
    this.mergeAdapter.addAdapter(this.vipAdapter);
    this.mergeAdapter.addAdapter(new EmptyAdapter(null));
    this.vipAdapter.type = 9;
  }

  public void closeQRView()
  {
    if ((this.cardObject.getBoolean("IsQRCodeOn")) && (this.mCardInfoView.getVisibility() == 4))
    {
      FlipAnimator localFlipAnimator = new FlipAnimator(this.mCardInfoView, this.backView, this.mCardInfoView.getWidth() / 2, this.mCardInfoView.getHeight() / 2);
      if (this.mCardInfoView.getVisibility() == 4)
        localFlipAnimator.reverse();
      this.mCardInfoView.clearAnimation();
      this.backView.clearAnimation();
      this.cardContainer.startAnimation(localFlipAnimator);
    }
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.cardDetailRequestTask != null)
      this.cardDetailRequestTask.load(this);
    while (true)
    {
      this.listView.addHeaderView(this.cardContainer);
      this.listView.addHeaderView(this.cardShops);
      this.mergeAdapter = new MergeAdapter();
      this.listView.setAdapter(this.mergeAdapter);
      refresh();
      return;
      this.cardDetailRequestTask = new CardDetailRequestTask(this);
    }
  }

  public void onCardDetailRequestFailed(MApiResponse paramMApiResponse, int paramInt)
  {
    this.listView.onRefreshComplete();
  }

  public void onCardDetailRequestFinish(DPObject paramDPObject, CardDetailRequestTask.ResponseDataType paramResponseDataType)
  {
    if (paramResponseDataType == CardDetailRequestTask.ResponseDataType.CURRENT_CARD_INFO)
    {
      refreshMemberCard(paramDPObject);
      this.listView.onRefreshComplete();
      return;
    }
    this.listView.onRefreshComplete();
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.container)
      super.onClick(paramView);
    do
    {
      return;
      paramView = paramView.getTag();
    }
    while ((!(paramView instanceof DPObject)) || (!((DPObject)paramView).isClass("Product")));
    gotoProductDetail(((DPObject)paramView).getInt("ProductID"));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mJoinMCHandler = new JoinMCHandler(getActivity());
    this.mJoinMCHandler.setOnJoinCardRequestHandlerListener(this);
    this.refreshHandler = new Handler()
    {
      public void handleMessage(Message paramMessage)
      {
        super.handleMessage(paramMessage);
        if (paramMessage.what == MemberCardFragment.REFRESH_MSG)
        {
          paramMessage = (DPObject)paramMessage.getData().get("card");
          MemberCardFragment.this.refresh(paramMessage);
        }
      }
    };
    paramBundle = new IntentFilter("com.dianping.action.UPDATE_USER_INFO");
    registerReceiver(this.mReceiver, paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.listView = ((PullToRefreshListView)paramLayoutInflater.inflate(R.layout.mc_member_card_layout, paramViewGroup, false));
    this.listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    this.listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
      {
        if ((MemberCardFragment.this.cardDetailRequestTask != null) && (MemberCardFragment.this.getActivity() != null))
        {
          int i = MemberCardFragment.this.cardObject.getInt("MemberCardID");
          MemberCardFragment.this.cardDetailRequestTask.doRequest(i, -1);
        }
        MemberCardFragment.this.statisticsEvent("mycard5", "mycard5_detail_refresh", null, 0);
      }
    });
    this.listView.setOnItemClickListener(this);
    this.cardContainer = paramLayoutInflater.inflate(R.layout.membercard_container, this.listView, false);
    this.cardContainer.setOnClickListener(this);
    this.mCardInfoView = ((MemberCardItem)this.cardContainer.findViewById(R.id.card_front));
    this.backView = this.cardContainer.findViewById(R.id.card_back);
    this.qrcodeImage = ((ImageView)this.backView.findViewById(R.id.card_qrcode));
    this.cardShops = ((LinearLayout)paramLayoutInflater.inflate(R.layout.mc_chain_card_shops, this.listView, false));
    this.shopCount = ((TextView)this.cardShops.findViewById(R.id.shop_count));
    return this.listView;
  }

  public void onDestroy()
  {
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    if (this.mJoinMCHandler != null)
      this.mJoinMCHandler.removeListener();
    if (this.cardDetailRequestTask != null)
      this.cardDetailRequestTask.abortRequest();
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if ((paramAdapterView instanceof DPObject))
    {
      paramView = (DPObject)paramAdapterView;
      if (!((DPObject)paramAdapterView).isClass("Product"))
        break label380;
      if (!MemberCard.isDPObjectSavingCard(paramView))
        break label140;
      if (this.cardObject.getInt("UserCardLevel") != 1)
        break label116;
      gotoMembersOnly(String.valueOf(this.cardObject.getInt("MemberCardID")), this.source, 2);
      statisticsEvent("mycard5", "mycard5_detail_highlevel", this.membercardid + "|" + this.title, 0);
    }
    label116: label380: 
    do
      while (true)
      {
        return;
        gotoProductDetail(paramView.getString("ProductUrl"));
        statisticsEvent("mycard5", "mycard5_detail_payment", null, 0);
        return;
        if (MemberCard.isDPObjectTimesCard(paramView))
        {
          if (this.cardObject.getInt("UserCardLevel") == 1)
          {
            gotoMembersOnly(String.valueOf(this.cardObject.getInt("MemberCardID")), this.source, 2);
            statisticsEvent("mycard5", "mycard5_detail_highlevel", this.membercardid + "|" + this.title, 0);
            return;
          }
          gotoProductDetail(paramView.getString("ProductUrl"));
          statisticsEvent("mycard5", "mycard5_detail_times", null, 0);
          return;
        }
        gotoNativeProductDetail(this.cardObject, paramView);
        statisticsEvent("mycard5", "mycard5_fixed", paramView.getInt("ProductID") + "|" + paramView.getString("ProductName") + "|" + this.cardObject.getInt("MemberCardID") + "|" + this.cardObject.getString("Title"), 0);
        statisticsEvent("mycard5", "mycard5_detail_prodetail", paramView.getInt("ProductType") + "", 0);
        return;
        if (((DPObject)paramAdapterView).isClass("CardPoint"))
        {
          this.cardPointURL = ((DPObject)paramAdapterView).getString("CardPointURL");
          gotoPointDetail(this.cardPointURL);
          return;
        }
        if (((DPObject)paramAdapterView).isClass("Card"))
        {
          gotoBranchCardList();
          return;
        }
        if (!((DPObject)paramAdapterView).isClass("CardScore"))
          break;
        paramView = (DPObject)paramAdapterView;
        paramAdapterView = paramView.getString("Score");
        paramView = paramView.getString("ScoreUrl");
        if (!TextUtils.isEmpty(paramAdapterView))
        {
          paramInt = 1;
          if (paramInt == 0)
            break label519;
        }
        for (paramAdapterView = "1"; ; paramAdapterView = "0")
        {
          statisticsEvent("mycard5", "mycard5_detail_score", paramAdapterView, 0);
          if (!(getActivity() instanceof MyCardActivity))
            break label526;
          gotoScoreDetail(paramView, CardFragment.FROM_MYCARDFRAGMENT);
          return;
          paramInt = 0;
          break;
        }
        if (!(getActivity() instanceof MemberCardInfoActivity))
          continue;
        gotoScoreDetail(paramView, CardFragment.FROM_MEMBERCARDINFOACTIVITY);
        return;
      }
    while (!((DPObject)paramAdapterView).isClass("ChainShop"));
    label140: label526: paramAdapterView = (DPObject)paramAdapterView;
    label519: gotoBranchCardList();
  }

  public void onJoinCardFinish(DPObject paramDPObject)
  {
  }

  public void refresh()
  {
    this.mergeAdapter.clear();
    this.membercard = new MemberCard(this.cardObject);
    this.mCardInfoView.setData(this.cardObject);
    this.mCardInfoView.invalidate();
    ((MemberCardImageView)this.backView.findViewById(R.id.card_image)).setImage(this.cardObject.getString("BgImage"));
    int j = this.cardObject.getInt("UserCardLevel");
    int i = j;
    if (j == 0)
      i = 1;
    if (i == 1)
    {
      this.shopCount.setText("消费前向服务员出示此卡");
      setGeneralProduct();
      this.mCardInfoView.hideCardVipIcon();
    }
    do
      return;
    while (i != 2);
    this.mCardInfoView.showCardVipIcon();
    this.shopCount.setText("结账时向服务员出示此卡");
    Object localObject = this.cardObject.getString("MemberCardGroupID");
    if ((localObject != null) && (!TextUtils.isEmpty((CharSequence)localObject)))
    {
      this.chainShopAdapter = new Adapter("连锁店");
      localObject = new DPObject().edit().putString("SubTitle", this.cardObject.getString("SubTitle")).generate();
      this.chainShopAdapter.setItemList((DPObject)localObject);
      this.mergeAdapter.addAdapter(this.chainShopAdapter);
      this.mergeAdapter.addAdapter(new EmptyAdapter(null));
    }
    localObject = this.membercard.getVIPCard();
    if (MemberCard.isDPObjectSavingCard((DPObject)localObject))
    {
      setSavingProduct((DPObject)localObject);
      this.viewType = 1;
    }
    while (true)
    {
      this.mergeAdapter.notifyDataSetChanged();
      return;
      if (!MemberCard.isDPObjectTimesCard((DPObject)localObject))
        continue;
      setTimesProduct((DPObject)localObject);
      this.viewType = 2;
    }
  }

  public void refresh(DPObject paramDPObject)
  {
    this.cardObject = paramDPObject;
    this.membercardid = this.cardObject.getInt("MemberCardID");
    this.title = this.cardObject.getString("Title");
    refresh();
  }

  public void refreshByCardId(int paramInt)
  {
    this.cardObject = this.cardObject.edit().putInt("MemberCardID", paramInt).generate();
    refreshUI();
  }

  public void refreshMemberCard(DPObject paramDPObject)
  {
    Message localMessage = Message.obtain();
    localMessage.what = REFRESH_MSG;
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("card", paramDPObject);
    localMessage.setData(localBundle);
    this.refreshHandler.sendMessage(localMessage);
  }

  public void refreshUI()
  {
    this.listView.setRefreshing();
  }

  private class Adapter extends BasicAdapter
  {
    private ArrayList<DPObject> itemList = new ArrayList();
    private String sectionName;
    public int type = 0;

    public Adapter(String arg2)
    {
      Object localObject;
      this.sectionName = localObject;
    }

    public boolean areAllItemsEnabled()
    {
      return false;
    }

    public int getCount()
    {
      if (this.itemList.size() > 0)
        return this.itemList.size();
      return 0;
    }

    public Object getItem(int paramInt)
    {
      return this.itemList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = (DPObject)getItem(paramInt);
      if (ProductListItemType.SAVING.equals(this.type))
      {
        paramView = ViewHolderFactory.createSaving(MemberCardFragment.this.getActivity(), localDPObject, ProductListItemStyle.ADDED_SAVE_PRODUCT).getView();
        paramView.setClickable(false);
        paramView.setEnabled(true);
        return paramView;
      }
      if (ProductListItemType.TIMES.equals(this.type))
        return ProductListItemFactory.createAddedTimesItem(MemberCardFragment.this.getActivity(), localDPObject);
      if ((paramView instanceof ViewGroup))
        paramView = (ViewGroup)paramView;
      while (localDPObject.isClass("Product"))
      {
        if (localDPObject.getInt("ProductLevel") == 2)
        {
          paramView = (ProductChoiceItem)(ViewGroup)LayoutInflater.from(MemberCardFragment.this.getActivity()).inflate(R.layout.mc_product_choice_item, paramViewGroup, false);
          paramView.fillData(localDPObject, 1);
          paramView.setEnabled(false);
          paramView.setClickable(true);
          paramView.getPanel().setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              MemberCardFragment.this.gotoMembersOnly(String.valueOf(MemberCardFragment.this.cardObject.getInt("MemberCardID")), MemberCardFragment.this.source, 2);
              MemberCardFragment.this.statisticsEvent("mycard5", "mycard5_detail_highlevel", MemberCardFragment.this.membercardid + "|" + MemberCardFragment.this.title, 0);
            }
          });
          paramInt = localDPObject.getInt("ProductID");
          paramView.getJoinBtn().setOnClickListener(new View.OnClickListener(paramInt)
          {
            public void onClick(View paramView)
            {
              MemberCardFragment.this.mJoinMCHandler.gotoCreateOrder(String.valueOf(MemberCardFragment.this.cardObject.getInt("MemberCardID")), MemberCardFragment.this.source, this.val$productid);
              MemberCardFragment.this.statisticsEvent("mycard5", "mycard5_detail_upgrade", MemberCardFragment.this.membercardid + "|" + MemberCardFragment.this.title, 0);
            }
          });
          paramView.getJoinBtn().setFocusable(false);
          return paramView;
          continue;
        }
        paramView = ProductType.valueOf(localDPObject);
        paramViewGroup = MCUtils.filterTextLine(localDPObject.getString("ProductName"));
        return ProductListItemFactory.createProductItemJumpView(MemberCardFragment.this.getActivity(), paramView, paramViewGroup);
      }
      if (localDPObject.isClass("CardScore"))
      {
        paramView = new CardScoreItem(MemberCardFragment.this.getActivity());
        paramView.setCardScore(localDPObject, 1);
        MemberCardFragment.this.setCardScore(true, paramView.isBinded());
        return paramView;
      }
      if (localDPObject.isClass("ChainShop"))
      {
        paramView = new CardChainShopItem(MemberCardFragment.this.getActivity());
        paramView.setChainShop(localDPObject);
        return paramView;
      }
      return ProductListItemFactory.createProductItemJumpView(MemberCardFragment.this.getActivity(), ProductType.POINT, localDPObject.getString("CardPointDesc"));
    }

    public int getViewTypeCount()
    {
      return 2;
    }

    public void insertItemList(DPObject paramDPObject)
    {
      if (paramDPObject == null)
        return;
      this.itemList.add(0, paramDPObject);
      notifyDataSetChanged();
    }

    public boolean isEnabled(int paramInt)
    {
      return true;
    }

    public void reset()
    {
      this.itemList.clear();
      notifyDataSetChanged();
    }

    public void setItemList(DPObject paramDPObject)
    {
      if (paramDPObject == null)
        return;
      this.itemList.clear();
      this.itemList.add(paramDPObject);
      notifyDataSetChanged();
    }

    public void setItemList(DPObject[] paramArrayOfDPObject)
    {
      if (paramArrayOfDPObject == null)
        return;
      this.itemList.clear();
      int i = 0;
      while (i < paramArrayOfDPObject.length)
      {
        this.itemList.add(paramArrayOfDPObject[i]);
        i += 1;
      }
      notifyDataSetChanged();
    }
  }

  private class EmptyAdapter extends BasicAdapter
  {
    private EmptyAdapter()
    {
    }

    public int getCount()
    {
      return 1;
    }

    public Object getItem(int paramInt)
    {
      return HEAD;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      return new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT).inflate(MemberCardFragment.this.getActivity(), null, MemberCardFragment.this.listView);
    }

    public boolean isEnabled(int paramInt)
    {
      return false;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.MemberCardFragment
 * JD-Core Version:    0.6.0
 */
package com.dianping.membercard.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.dianping.archive.DPObject;
import com.dianping.membercard.MembersOnlyActivity;
import com.dianping.membercard.utils.BaseViewHolder;
import com.dianping.membercard.utils.EmptyHeaderHolder;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.membercard.utils.ProductListItemFactory;
import com.dianping.membercard.utils.ProductListItemStyle;
import com.dianping.membercard.utils.ProductType;
import com.dianping.membercard.utils.ViewHolder;
import com.dianping.membercard.utils.ViewHolderAdapter;
import com.dianping.membercard.utils.ViewHolderFactory;
import com.dianping.membercard.view.AddCardButtonView;
import com.dianping.membercard.view.MemberCardListItem;
import com.dianping.membercard.view.OneLineListItemView;
import com.dianping.membercard.view.OneLineListItemView.ItemStyle;
import com.dianping.membercard.view.ShopPowerItemView;
import com.dianping.membercard.view.TwoLineListItemView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.List;

public class NoAddedGeneralCardFragment extends CardFragment
{
  private AddCardButtonView addCardButtonView;
  private String browserPriceLabel;
  private String browserShopInfoLabel;
  private MemberCardListItem cardInfoView;
  private String generalPrivilegeLabel;
  private boolean isInListView = true;
  private ListView listView;
  private FrameLayout mFrameLayout;
  private MemberCard memberCard;
  private ViewHolder shopViewHolder;
  private String specialTipsLabel;
  private DPObject vipObject;
  private String vipPrivilegeLabel;

  private AddCardButtonView createAddCardButtonView()
  {
    AddCardButtonView localAddCardButtonView = new AddCardButtonView(getActivity());
    GAUserInfo localGAUserInfo = new GAUserInfo();
    localGAUserInfo.member_card_id = Integer.valueOf(this.cardObject.getInt("MemberCardID"));
    localGAUserInfo.shop_id = Integer.valueOf(this.cardObject.getInt("ShopID"));
    localAddCardButtonView.updateGAuserinfo(localGAUserInfo);
    localAddCardButtonView.setProduct(MemberCard.getGeneralCardAddButtonProduct(this.cardObject, this.cardlevel));
    return localAddCardButtonView;
  }

  private ViewHolder createProductHolder(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return null;
    ProductType localProductType = ProductType.valueOf(paramDPObject);
    ProductListItemStyle localProductListItemStyle = null;
    if ((ProductType.DISCOUNT == localProductType) || (ProductType.BADGE == localProductType) || (ProductType.SCORE == localProductType))
      localProductListItemStyle = ProductListItemStyle.TWO_LINE_TEXT_PRODUCT;
    return ViewHolderFactory.createProductViewHolder(getActivity(), paramDPObject, localProductListItemStyle);
  }

  private void doWithGeneralCard()
  {
    ViewHolderAdapter localViewHolderAdapter = new ViewHolderAdapter();
    this.listView.addHeaderView(this.cardInfoView);
    this.addCardButtonView = createAddCardButtonView();
    if (this.addCardButtonView != null)
    {
      this.listView.addHeaderView(this.addCardButtonView);
      setupAddButtonListener();
    }
    localViewHolderAdapter.add(new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT));
    ArrayList localArrayList = new ArrayList();
    Object localObject = this.memberCard.getGeneralCardList();
    if (localObject != null)
    {
      int j = localObject.length;
      i = 0;
      while (i < j)
      {
        ViewHolder localViewHolder = createProductHolder(localObject[i]);
        if (localViewHolder != null)
          localArrayList.add(localViewHolder);
        i += 1;
      }
    }
    localObject = createProductHolder(this.cardObject.getObject("CardPoint"));
    if (localObject != null)
      localArrayList.add(localObject);
    localObject = createProductHolder(this.cardObject.getObject("CardScore"));
    if (localObject != null)
      localArrayList.add(localObject);
    localViewHolderAdapter.add(localArrayList);
    if (localArrayList.size() == 1)
    {
      i = 1;
      if ((i == 0) || (!(((ViewHolder)localArrayList.get(0)).getView() instanceof TwoLineListItemView)))
        break label315;
    }
    label315: for (int i = 1; ; i = 0)
    {
      if (i != 0)
        ((TwoLineListItemView)((ViewHolder)localArrayList.get(0)).getView()).setExpandableState(true);
      localViewHolderAdapter.add(new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT));
      if (!isSourceFromShopDetails())
        localViewHolderAdapter.add(this.shopViewHolder);
      localViewHolderAdapter.add(new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT));
      this.listView.setAdapter(localViewHolderAdapter);
      return;
      i = 0;
      break;
    }
  }

  private void doWithSavingCard()
  {
    ViewHolderAdapter localViewHolderAdapter = new ViewHolderAdapter();
    this.cardInfoView.showCardVipIcon();
    this.listView.addHeaderView(this.cardInfoView);
    this.addCardButtonView = createAddCardButtonView();
    if (this.addCardButtonView != null)
    {
      this.listView.addHeaderView(this.addCardButtonView);
      setupAddButtonListener();
    }
    localViewHolderAdapter.add(new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT));
    Object localObject = createProductHolder(this.vipObject);
    if (localObject != null)
    {
      localViewHolderAdapter.add((ViewHolder)localObject);
      localViewHolderAdapter.add(new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT));
    }
    localViewHolderAdapter.add(new BaseViewHolder(ProductListItemFactory.createTwoTextLineItem(getActivity(), this.specialTipsLabel, this.vipObject.getString("Tips"))));
    localViewHolderAdapter.add(new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT));
    localObject = ProductListItemFactory.createProductOneTextItem(getActivity(), this.browserPriceLabel);
    if (localObject != null)
    {
      ((OneLineListItemView)localObject).configStyles(new OneLineListItemView.ItemStyle[] { OneLineListItemView.ItemStyle.ARROW_JUMPABLE });
      ((OneLineListItemView)localObject).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          NoAddedGeneralCardFragment.this.gotoShopPrice();
        }
      });
      localViewHolderAdapter.add(new BaseViewHolder((View)localObject));
    }
    if (this.shopViewHolder != null)
      localViewHolderAdapter.add(this.shopViewHolder);
    localViewHolderAdapter.add(new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT));
    this.listView.setAdapter(localViewHolderAdapter);
  }

  private void doWithTimesCard()
  {
    ViewHolderAdapter localViewHolderAdapter = new ViewHolderAdapter();
    this.cardInfoView.showCardVipIcon();
    this.listView.addHeaderView(this.cardInfoView);
    this.addCardButtonView = createAddCardButtonView();
    if (this.addCardButtonView != null)
    {
      this.listView.addHeaderView(this.addCardButtonView);
      setupAddButtonListener();
    }
    localViewHolderAdapter.add(new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT));
    ViewHolder localViewHolder = createProductHolder(this.vipObject);
    if (localViewHolder != null)
    {
      localViewHolderAdapter.add(localViewHolder);
      localViewHolderAdapter.add(new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT));
    }
    localViewHolderAdapter.add(new BaseViewHolder(ProductListItemFactory.createTwoTextLineItem(getActivity(), this.specialTipsLabel, this.vipObject.getString("Tips"))));
    localViewHolderAdapter.add(new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT));
    if (this.shopViewHolder != null)
    {
      localViewHolderAdapter.add(this.shopViewHolder);
      localViewHolderAdapter.add(new EmptyHeaderHolder(EmptyHeaderHolder.DEFAULT_HEIGHT));
    }
    this.listView.setAdapter(localViewHolderAdapter);
  }

  private void gotoShopPrice()
  {
    int i = this.cardObject.getInt("ShopID");
    statisticsEvent("cardinfo5", "cardinfo5_menuphoto", i + "|" + this.cardObject.getString("Title") + "|" + this.source, 0);
    if (i > 0)
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto?id=" + i + "&tabname=价目表")));
  }

  private void initViews()
  {
    this.cardInfoView.setData(this.memberCard.getCardObject());
    if (!isSourceFromShopDetails())
    {
      ShopPowerItemView localShopPowerItemView = ProductListItemFactory.createShopItemView(getActivity(), this.browserShopInfoLabel, this.memberCard.getPower());
      localShopPowerItemView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          NoAddedGeneralCardFragment.this.gotoShopInfo();
        }
      });
      this.shopViewHolder = new BaseViewHolder(localShopPowerItemView);
    }
  }

  private boolean isSourceFromShopDetails()
  {
    return false;
  }

  private void setupAddButtonListener()
  {
    this.listView.setOnScrollListener(new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
      {
        if (paramInt1 < NoAddedGeneralCardFragment.this.listView.getHeaderViewsCount() - 1)
          if (!NoAddedGeneralCardFragment.this.isInListView)
          {
            paramAbsListView = NoAddedGeneralCardFragment.this.addCardButtonView.getButtonView();
            NoAddedGeneralCardFragment.this.mFrameLayout.removeView(paramAbsListView);
            NoAddedGeneralCardFragment.this.addCardButtonView.addButtonView();
            NoAddedGeneralCardFragment.access$102(NoAddedGeneralCardFragment.this, true);
          }
        do
          return;
        while ((paramInt1 != NoAddedGeneralCardFragment.this.listView.getHeaderViewsCount() - 1) || (NoAddedGeneralCardFragment.this.isInListView != true));
        paramAbsListView = NoAddedGeneralCardFragment.this.addCardButtonView.removeButtonView();
        NoAddedGeneralCardFragment.this.mFrameLayout.addView(paramAbsListView);
        NoAddedGeneralCardFragment.access$102(NoAddedGeneralCardFragment.this, false);
      }

      public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
      {
      }
    });
    FragmentActivity localFragmentActivity = getActivity();
    if ((localFragmentActivity != null) && ((localFragmentActivity instanceof MembersOnlyActivity)))
      this.addCardButtonView.setOnAddButtonClickListener((MembersOnlyActivity)localFragmentActivity);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.memberCard = new MemberCard(this.cardObject);
    initViews();
    try
    {
      if (this.cardlevel == MCUtils.GENERAL_CARD_LEVEL)
      {
        doWithGeneralCard();
        return;
      }
      this.vipObject = this.memberCard.getVIPCard();
      if ((this.vipObject != null) && (MemberCard.isDPObjectSavingCard(this.vipObject)))
      {
        doWithSavingCard();
        return;
      }
    }
    catch (Exception paramBundle)
    {
      paramBundle.printStackTrace();
      return;
    }
    if ((this.vipObject != null) && (MemberCard.isDPObjectTimesCard(this.vipObject)))
      doWithTimesCard();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.browserShopInfoLabel = getResources().getString(R.string.mc_browser_shop_info);
    this.generalPrivilegeLabel = getResources().getString(R.string.mc_general_privilege);
    this.vipPrivilegeLabel = getResources().getString(R.string.mc_vip_privilege);
    this.browserPriceLabel = getResources().getString(R.string.mc_browser_bill_price);
    this.specialTipsLabel = getResources().getString(R.string.mc_special_tips);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mFrameLayout = ((FrameLayout)paramLayoutInflater.inflate(R.layout.mc_no_added_member_card_layout, paramViewGroup, false));
    this.listView = ((ListView)this.mFrameLayout.findViewById(R.id.list));
    this.cardInfoView = ((MemberCardListItem)paramLayoutInflater.inflate(R.layout.no_added_card_info_layout, this.listView, false));
    return this.mFrameLayout;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.NoAddedGeneralCardFragment
 * JD-Core Version:    0.6.0
 */
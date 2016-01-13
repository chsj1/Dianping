package com.dianping.membercard.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.DPFragment;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.membercard.model.JoinMCHandler2;
import com.dianping.membercard.model.JoinMCHandler2.OnJoinThirdPartyCardHandlerListener;
import com.dianping.membercard.utils.EmptyHeaderHolder;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.membercard.utils.ProductListItemStyle;
import com.dianping.membercard.utils.ViewHolder;
import com.dianping.membercard.utils.ViewHolderAdapter;
import com.dianping.membercard.utils.ViewHolderFactory;
import com.dianping.membercard.view.ThirdPartyCardListItem;
import com.dianping.membercard.view.TwoLineListItemView;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class NoAddedThirdPartyCardFragment extends DPFragment
{
  private final String API_NAME_GET_MC_STATUS = "getmcstatus.v2.mc";
  private MApiRequest cardInfoRequest;
  private ThirdPartyCardListItem cardInfoView;
  private DPObject cardObject;
  private MApiRequest cardStatusRequest;
  private NovaButton joinCardButton;
  private View.OnClickListener joinClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (NoAddedThirdPartyCardFragment.this.source == 12);
      for (paramView = "cardadd_wlife_shop"; ; paramView = "cardadd_wlife_availablecard")
      {
        NoAddedThirdPartyCardFragment.this.statisticsEvent("cardadd", paramView, "", 0);
        NoAddedThirdPartyCardFragment.this.joinCardAction();
        return;
      }
    }
  };
  private JoinMCHandler2 joinMCHandler;
  private ProgressDialog loadingProgressDialog;
  private final LoginResultListener loginResultListener = new LoginResultListener()
  {
    public void onLoginCancel(AccountService paramAccountService)
    {
    }

    public void onLoginSuccess(AccountService paramAccountService)
    {
      NoAddedThirdPartyCardFragment.this.onUserLogin();
    }
  };
  private RequestHandler<MApiRequest, MApiResponse> mApiRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == NoAddedThirdPartyCardFragment.this.cardInfoRequest)
        NoAddedThirdPartyCardFragment.this.setRequestCardInfoFailedMessage(paramMApiResponse.message().content());
      do
        return;
      while (paramMApiRequest != NoAddedThirdPartyCardFragment.this.cardStatusRequest);
      NoAddedThirdPartyCardFragment.this.setRequestCardStatusFailedMessage(paramMApiResponse.message().content());
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == NoAddedThirdPartyCardFragment.this.cardInfoRequest)
        NoAddedThirdPartyCardFragment.this.onCardInfoRequestFinish(paramMApiRequest, paramMApiResponse);
      do
        return;
      while (paramMApiRequest != NoAddedThirdPartyCardFragment.this.cardStatusRequest);
      NoAddedThirdPartyCardFragment.this.onCardStatusRequestFinish(paramMApiRequest, paramMApiResponse);
    }
  };
  private String memberCardId;
  private JoinMCHandler2.OnJoinThirdPartyCardHandlerListener onJoinThirdPartyCardHandlerListener = new JoinMCHandler2.OnJoinThirdPartyCardHandlerListener()
  {
    public void onRequestJoinThirdPartyCardFailed(SimpleMsg paramSimpleMsg)
    {
      NoAddedThirdPartyCardFragment.this.setRequestJoinCardFailedResult(paramSimpleMsg);
    }

    public void onRequestJoinThirdPartyCardSuccess(DPObject paramDPObject)
    {
      NoAddedThirdPartyCardFragment.this.setRequestJoinCardSuccessfully(paramDPObject);
    }
  };
  private ListView otherInfoListView;
  private ViewHolderAdapter productsListViewAdapter;
  private NetworkImageView providerLogoImageView;
  private TextView providerTextView;
  private FrameLayout rootView;
  private String shopid;
  private int source = 12;
  private TextView upgradeTextView;

  private void closeAllExpandProductWithoutTwoLineListItemView(TwoLineListItemView paramTwoLineListItemView)
  {
    int j = this.productsListViewAdapter.getCount();
    int i = 0;
    while (i < j)
    {
      Object localObject = (ViewHolder)this.productsListViewAdapter.getItem(i);
      if ((((ViewHolder)localObject).getView() instanceof TwoLineListItemView))
      {
        localObject = (TwoLineListItemView)((ViewHolder)localObject).getView();
        if (!localObject.equals(paramTwoLineListItemView))
          ((TwoLineListItemView)localObject).setExpandableState(false);
      }
      i += 1;
    }
  }

  private void configHolderViewEvents(ViewHolder paramViewHolder)
  {
    if ((paramViewHolder.getView() instanceof TwoLineListItemView))
    {
      paramViewHolder = (TwoLineListItemView)paramViewHolder.getView();
      paramViewHolder.setOnClickListener(new View.OnClickListener(paramViewHolder)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$view;
          if (!this.val$view.isExpanded());
          for (boolean bool = true; ; bool = false)
          {
            paramView.setExpandableState(bool);
            NoAddedThirdPartyCardFragment.this.closeAllExpandProductWithoutTwoLineListItemView(this.val$view);
            return;
          }
        }
      });
    }
  }

  private ViewHolder createProductHolder(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return null;
    ProductListItemStyle localProductListItemStyle = ProductListItemStyle.WELIFE_PRODUCT;
    return ViewHolderFactory.createProductViewHolderForWeLife(getActivity(), paramDPObject, localProductListItemStyle);
  }

  private void dismissLoadingView()
  {
    if ((this.loadingProgressDialog != null) && (this.loadingProgressDialog.isShowing()))
    {
      this.loadingProgressDialog.dismiss();
      this.loadingProgressDialog = null;
    }
  }

  private void initParams()
  {
    this.source = getArguments().getInt("source");
    this.memberCardId = getArguments().getString("membercardid");
    this.shopid = getArguments().getString("shopid");
    this.joinCardButton.gaUserInfo.member_card_id = Integer.valueOf(this.memberCardId);
    GAUserInfo localGAUserInfo = this.joinCardButton.gaUserInfo;
    if (TextUtils.isEmpty(this.shopid));
    for (int i = 0; ; i = Integer.valueOf(this.shopid).intValue())
    {
      localGAUserInfo.shop_id = Integer.valueOf(i);
      return;
    }
  }

  private void initViews()
  {
    this.otherInfoListView = ((ListView)this.rootView.findViewById(R.id.list));
    this.otherInfoListView.setHeaderDividersEnabled(false);
    this.joinCardButton = ((NovaButton)this.rootView.findViewById(R.id.submit));
    this.joinCardButton.setGAString("wecard_add_button");
    this.joinCardButton.setOnClickListener(this.joinClickListener);
    this.upgradeTextView = ((TextView)this.rootView.findViewById(R.id.upgradetip));
    this.cardInfoView = ((ThirdPartyCardListItem)LayoutInflater.from(getActivity()).inflate(R.layout.no_added_third_party_card_info, this.otherInfoListView, false));
    View localView = LayoutInflater.from(getActivity()).inflate(R.layout.mc_logo_welife_provider, this.otherInfoListView, false);
    this.providerTextView = ((TextView)localView.findViewById(R.id.welife_provider_desc));
    this.providerLogoImageView = ((NetworkImageView)localView.findViewById(R.id.welife_logo_imageview));
    this.cardInfoView.setData(this.cardObject);
    this.otherInfoListView.addHeaderView(this.cardInfoView, null, false);
    this.otherInfoListView.addHeaderView(newOneEmptyView(EmptyHeaderHolder.DEFAULT_HEIGHT));
    this.otherInfoListView.addFooterView(newOneEmptyView(EmptyHeaderHolder.DEFAULT_HEIGHT));
    this.otherInfoListView.addFooterView(localView);
    this.otherInfoListView.addFooterView(newOneEmptyView(EmptyHeaderHolder.DEFAULT_HEIGHT));
    this.productsListViewAdapter = new ViewHolderAdapter();
    this.otherInfoListView.setAdapter(this.productsListViewAdapter);
    this.otherInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
      }
    });
  }

  private boolean isCardInfoData(Object paramObject)
  {
    return (paramObject != null) && ((paramObject instanceof DPObject)) && (((DPObject)paramObject).isClass("Card"));
  }

  private boolean isCardStatusData(Object paramObject)
  {
    return (paramObject != null) && ((paramObject instanceof DPObject)) && (((DPObject)paramObject).isClass("MCStatus"));
  }

  private void joinCardAction()
  {
    if (!((NovaActivity)getActivity()).isLogined())
    {
      accountService().login(this.loginResultListener);
      return;
    }
    showLoadingView("正在提交请求，请稍候..");
    requestJoinCard(this.memberCardId, this.source);
  }

  private View newOneEmptyView(int paramInt)
  {
    View localView = new EmptyHeaderHolder(paramInt).inflate(getActivity(), null, this.otherInfoListView);
    localView.setBackgroundResource(R.drawable.main_background);
    return localView;
  }

  private void onCardInfoRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (!isCardInfoData(paramMApiResponse.result()))
    {
      setRequestCardInfoFailedMessage("错误：数据异常");
      return;
    }
    setRequestCardInfoSuccessfulResult((DPObject)paramMApiResponse.result());
  }

  private void onCardStatusRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (!isCardStatusData(paramMApiResponse.result()))
    {
      setRequestCardStatusFailedMessage("错误：数据异常");
      return;
    }
    setRequestCardStatusSuccessfulResult((DPObject)paramMApiResponse.result());
  }

  private void onUserLogin()
  {
    showLoadingView("正在更新信息，请稍候...");
    requestCardStatus(this.memberCardId);
  }

  private void onViewDisplay()
  {
    if (this.cardObject != null)
    {
      setCardInfo(this.cardObject);
      return;
    }
    showLoadingView("正在发起请求，请稍候...");
    requestCardInfo(this.memberCardId);
  }

  private void openBindThirdPartyCardWebview(String paramString)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("membercardids", this.memberCardId);
    localBundle.putString("addedcardviewtitle", this.cardObject.getString("Title"));
    localBundle.putString("shopid", this.shopid);
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString));
    paramString.putExtras(localBundle);
    getActivity().startActivity(paramString);
  }

  private void openJoinedCardInfoView(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse(paramString));
    localIntent.putExtra("membercardid", this.memberCardId);
    localIntent.putExtra("title", this.cardObject.getString("Title"));
    startActivity(localIntent);
  }

  private void requestCardInfo(String paramString)
  {
    if (this.cardInfoRequest != null)
    {
      mapiService().abort(this.cardInfoRequest, this.mApiRequestHandler, true);
      this.cardInfoRequest = null;
    }
    StringBuilder localStringBuilder = new StringBuilder("http://mc.api.dianping.com/");
    localStringBuilder.append("getcardinfo.v2.mc");
    localStringBuilder.append("?membercardid=");
    localStringBuilder.append(paramString);
    if (accountService().token() != null)
    {
      localStringBuilder.append("&token=");
      localStringBuilder.append(accountService().token());
    }
    paramString = location();
    if (paramString != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      localStringBuilder.append("&lat=").append(localDecimalFormat.format(paramString.latitude()));
      localStringBuilder.append("&lng=").append(localDecimalFormat.format(paramString.longitude()));
    }
    localStringBuilder.append("&uuid=");
    localStringBuilder.append(Environment.uuid());
    paramString = getResources().getDisplayMetrics();
    localStringBuilder.append("&pixel=").append(paramString.widthPixels);
    this.cardInfoRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.cardInfoRequest, this.mApiRequestHandler);
  }

  private void requestCardStatus(String paramString)
  {
    if (this.cardStatusRequest != null)
    {
      mapiService().abort(this.cardStatusRequest, this.mApiRequestHandler, true);
      this.cardStatusRequest = null;
    }
    StringBuilder localStringBuilder = new StringBuilder("http://mc.api.dianping.com/");
    localStringBuilder.append("getmcstatus.v2.mc").append("?membercardid=").append(paramString);
    if ((accountService() != null) && (accountService().token() != null))
      localStringBuilder.append("&token=").append(accountService().token());
    localStringBuilder.append("&uuid=").append(Environment.uuid());
    this.cardStatusRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.cardStatusRequest, this.mApiRequestHandler);
  }

  private void requestJoinCard(String paramString, int paramInt)
  {
    if (this.joinMCHandler == null)
    {
      this.joinMCHandler = new JoinMCHandler2((NovaActivity)getActivity());
      this.joinMCHandler.setJoinThirdPartyCardHandlerListener(this.onJoinThirdPartyCardHandlerListener);
    }
    this.joinMCHandler.joinThirdPartyCards(paramString, paramInt);
  }

  private void setCardInfo(DPObject paramDPObject)
  {
    this.cardInfoView.setData(paramDPObject);
    updateProductListContents(paramDPObject);
    updateUpgradeTips(paramDPObject);
    updateCardProvider(paramDPObject);
    updateJoinCardButton();
  }

  private void setRequestCardInfoFailedMessage(String paramString)
  {
    dismissLoadingView();
    showMessageInToast(paramString);
  }

  private void setRequestCardInfoSuccessfulResult(DPObject paramDPObject)
  {
    dismissLoadingView();
    this.cardObject = paramDPObject;
    setCardInfo(paramDPObject);
  }

  private void setRequestCardStatusFailedMessage(String paramString)
  {
    dismissLoadingView();
    showMessageInToast(paramString);
  }

  private void setRequestCardStatusSuccessfulResult(DPObject paramDPObject)
  {
    dismissLoadingView();
    if (!paramDPObject.getBoolean("IsMCUser"))
    {
      joinCardAction();
      return;
    }
    MCUtils.sendJoinScoreCardSuccessBroadcast(getActivity(), this.memberCardId);
    openJoinedCardInfoView(paramDPObject.getString("NavigateUrl"));
    getActivity().finish();
  }

  private void setRequestJoinCardFailedResult(SimpleMsg paramSimpleMsg)
  {
    dismissLoadingView();
    showMessageInToast(paramSimpleMsg.content());
  }

  private void setRequestJoinCardSuccessfully(DPObject paramDPObject)
  {
    dismissLoadingView();
    int i = paramDPObject.getInt("Code");
    String str = paramDPObject.getString("Msg");
    paramDPObject = paramDPObject.getString("RedirectUrl");
    if (i == 200)
    {
      MCUtils.sendJoinScoreCardSuccessBroadcast(getActivity(), this.memberCardId);
      MCUtils.sendUpdateMemberCardListBroadcast(getActivity(), this.memberCardId);
      openJoinedCardInfoView(paramDPObject);
      getActivity().finish();
      return;
    }
    if (i == 201)
    {
      openBindThirdPartyCardWebview(paramDPObject);
      return;
    }
    showMessageInToast(str);
  }

  private void showLoadingView(String paramString)
  {
    if ((this.loadingProgressDialog != null) && (this.loadingProgressDialog.isShowing()))
    {
      this.loadingProgressDialog.dismiss();
      this.loadingProgressDialog = null;
    }
    ProgressDialog localProgressDialog = new ProgressDialog(getActivity());
    localProgressDialog.setCancelable(false);
    String str = paramString;
    if (TextUtils.isEmpty(paramString))
      str = "载入中...";
    localProgressDialog.setMessage(str);
    this.loadingProgressDialog = localProgressDialog;
    localProgressDialog.show();
  }

  private void showMessageInToast(String paramString)
  {
    Toast.makeText(getActivity(), paramString, 0).show();
  }

  private void stopAllRequest()
  {
    if (this.cardInfoRequest != null)
    {
      mapiService().abort(this.cardInfoRequest, this.mApiRequestHandler, true);
      this.cardInfoRequest = null;
    }
    if (this.cardStatusRequest != null)
    {
      mapiService().abort(this.cardStatusRequest, this.mApiRequestHandler, true);
      this.cardStatusRequest = null;
    }
    if (this.joinMCHandler != null)
    {
      this.joinMCHandler.setJoinThirdPartyCardHandlerListener(null);
      this.joinMCHandler = null;
    }
  }

  private void updateCardProvider(DPObject paramDPObject)
  {
    this.providerTextView.setText(paramDPObject.getString("ProviderDesc"));
    this.providerLogoImageView.setImage(paramDPObject.getString("ProviderLogo"));
  }

  private void updateJoinCardButton()
  {
    int j = 8;
    NovaButton localNovaButton = this.joinCardButton;
    if (this.cardObject == null)
    {
      i = 8;
      localNovaButton.setVisibility(i);
      localNovaButton = this.joinCardButton;
      if (!MCUtils.isCardPaused(this.cardObject))
        break label51;
    }
    label51: for (int i = j; ; i = 0)
    {
      localNovaButton.setVisibility(i);
      return;
      i = 0;
      break;
    }
  }

  private void updateProductListContents(DPObject paramDPObject)
  {
    this.productsListViewAdapter.reset();
    ArrayList localArrayList = new ArrayList();
    paramDPObject = paramDPObject.getArray("ProductList");
    if ((paramDPObject != null) && (paramDPObject.length >= 1))
    {
      int j = paramDPObject.length;
      i = 0;
      while (i < j)
      {
        ViewHolder localViewHolder = createProductHolder(paramDPObject[i]);
        if (localViewHolder != null)
        {
          configHolderViewEvents(localViewHolder);
          localArrayList.add(localViewHolder);
        }
        i += 1;
      }
      if (localArrayList.size() > 0)
        ((ViewHolder)localArrayList.get(localArrayList.size() - 1)).getView().setBackgroundResource(R.drawable.mc_listview_item_background_rectangle_single);
    }
    if (localArrayList.size() == 1)
    {
      i = 1;
      if ((i == 0) || (!(((ViewHolder)localArrayList.get(0)).getView() instanceof TwoLineListItemView)))
        break label204;
    }
    label204: for (int i = 1; ; i = 0)
    {
      if (i != 0)
        ((TwoLineListItemView)((ViewHolder)localArrayList.get(0)).getView()).setExpandableState(true);
      this.productsListViewAdapter.add(localArrayList);
      return;
      i = 0;
      break;
    }
  }

  private void updateUpgradeTips(DPObject paramDPObject)
  {
    String str = paramDPObject.getString("UpgradeTips");
    TextView localTextView = this.upgradeTextView;
    if (TextUtils.isEmpty(str));
    for (int i = 8; ; i = 0)
    {
      localTextView.setVisibility(i);
      if (!TextUtils.isEmpty(str))
      {
        this.upgradeTextView.setText(str);
        this.joinCardButton.setText("立即升级");
      }
      if (MCUtils.isCardPaused(paramDPObject))
        this.upgradeTextView.setVisibility(8);
      return;
    }
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.cardObject == null)
      return;
    setCardInfo(this.cardObject);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.rootView = ((FrameLayout)paramLayoutInflater.inflate(R.layout.mc_noadded_thirdpartycard_layout, paramViewGroup, false));
    initViews();
    initParams();
    return this.rootView;
  }

  public void onDestroyView()
  {
    stopAllRequest();
    super.onDestroyView();
  }

  public void onResume()
  {
    super.onResume();
    onViewDisplay();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.NoAddedThirdPartyCardFragment
 * JD-Core Version:    0.6.0
 */
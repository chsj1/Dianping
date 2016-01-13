package com.dianping.membercard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.membercard.model.JoinMCHandler;
import com.dianping.membercard.model.JoinMCHandler.OnJoinCardRequestHandlerListener;
import com.dianping.membercard.model.JoinMCHandler2;
import com.dianping.membercard.model.JoinMCHandler2.OnJoinThirdPartyCardHandlerListener;
import com.dianping.membercard.model.JoinMScoreCHandler;
import com.dianping.membercard.model.JoinMScoreCHandler.OnJoinScoreCardHandlerListener;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.membercard.view.AvailableCardListItem;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ChainCardListActivity extends NovaListActivity
  implements AdapterView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, JoinMCHandler.OnJoinCardRequestHandlerListener
{
  private static final int SCORE_CARD_BIND_REQUEST_CODE = 10;
  private static String beMemberLabel;
  private static String buyNowLabel;
  private static String freeJoinLabel;
  private final String MESSAGE_DIALOG_ADD_CARD_WAITING = "正在提交请求，请稍候...";
  Adapter adapter;
  String addingWeLifeCardTitle;
  LinearLayout btnBg;
  int cardlevel = 1;
  private MApiRequest chainCardListRequest;
  ArrayList<Integer> checkList = new ArrayList();
  String emptyMsg;
  String errorMsg;
  boolean isEdit;
  boolean isEnd;
  boolean isScoreCardChainList;
  private int joinCardType = 0;
  JoinMCHandler2 joinMCHandler2;
  ArrayList<DPObject> lists = new ArrayList();
  private LoginResultListener loginResultListener = new LoginResultListener()
  {
    public void onLoginCancel(AccountService paramAccountService)
    {
    }

    public void onLoginSuccess(AccountService paramAccountService)
    {
      if (ChainCardListActivity.this.joinCardType == 1)
        ChainCardListActivity.this.addScoreCards(ChainCardListActivity.this.membercardid, ChainCardListActivity.this.source);
      do
        return;
      while (ChainCardListActivity.this.joinCardType != 2);
      ChainCardListActivity.this.addThirdPartyCard(ChainCardListActivity.this.membercardid, ChainCardListActivity.this.source);
    }
  };
  private IntentFilter mFilter;
  JoinMCHandler mJoinMCHandler;
  JoinMScoreCHandler mJoinMScoreCHandler;
  private MyReceiver mReceiver;
  Button mSubmitButton;
  String membercardgroupid;
  String membercardid;
  int nextStartIndex;
  private JoinMScoreCHandler.OnJoinScoreCardHandlerListener onJoinScoreCardHandlerListener = new JoinMScoreCHandler.OnJoinScoreCardHandlerListener()
  {
    public void onJoinScoreCardFail(String paramString)
    {
      ChainCardListActivity.this.dismissDialog();
      Toast.makeText(ChainCardListActivity.this, paramString, 0).show();
    }

    public void onJoinScoreCardFailForNeedMemberInfo(String paramString1, String paramString2)
    {
      ChainCardListActivity.this.dismissDialog();
      ChainCardListActivity.this.openBindScoreCardWebview(paramString2);
    }

    public void onJoinScoreCardSuccess()
    {
      ChainCardListActivity.this.dismissDialog();
      MCUtils.sendJoinScoreCardSuccessBroadcast(ChainCardListActivity.this, ChainCardListActivity.this.membercardid);
      if (ChainCardListActivity.this.isEdit)
      {
        ChainCardListActivity.this.gotoMyCard();
        ChainCardListActivity.this.finish();
      }
    }
  };
  private JoinMCHandler2.OnJoinThirdPartyCardHandlerListener onJoinThirdPartyCardHandlerListener = new JoinMCHandler2.OnJoinThirdPartyCardHandlerListener()
  {
    public void onRequestJoinThirdPartyCardFailed(SimpleMsg paramSimpleMsg)
    {
      ChainCardListActivity.this.dismissDialog();
      Toast.makeText(ChainCardListActivity.this, paramSimpleMsg.content(), 0).show();
    }

    public void onRequestJoinThirdPartyCardSuccess(DPObject paramDPObject)
    {
      ChainCardListActivity.this.dismissDialog();
      int i = paramDPObject.getInt("Code");
      String str = paramDPObject.getString("Msg");
      paramDPObject = paramDPObject.getString("RedirectUrl");
      if (i == 200)
      {
        MCUtils.sendJoinScoreCardSuccessBroadcast(ChainCardListActivity.this, ChainCardListActivity.this.membercardid);
        MCUtils.sendUpdateMemberCardListBroadcast(ChainCardListActivity.this, ChainCardListActivity.this.membercardid);
        if (ChainCardListActivity.this.isEdit)
        {
          ChainCardListActivity.this.openThirdPartyCardInfoView(paramDPObject, ChainCardListActivity.this.addingWeLifeCardTitle);
          ChainCardListActivity.this.finish();
        }
        return;
      }
      if (i == 201)
      {
        ChainCardListActivity.this.openBindThirdPartyCardWebview(paramDPObject);
        return;
      }
      Toast.makeText(ChainCardListActivity.this, str, 0).show();
    }
  };
  int productid;
  int recordCount;
  TextView shopCount;
  TextView shopName;
  int source;

  private void addScoreCards(String paramString, int paramInt)
  {
    if (getAccount() == null)
    {
      this.joinCardType = 1;
      accountService().login(this.loginResultListener);
      return;
    }
    showProgressDialog("正在提交请求，请稍候...");
    this.mJoinMScoreCHandler.joinScoreCards(paramString, paramInt);
  }

  private void addThirdPartyCard(String paramString, int paramInt)
  {
    if (getAccount() == null)
    {
      this.joinCardType = 2;
      accountService().login(this.loginResultListener);
      return;
    }
    showProgressDialog("正在提交请求，请稍候...");
    this.joinMCHandler2.joinThirdPartyCards(paramString, paramInt);
  }

  private void appendCards(DPObject paramDPObject)
  {
    Object localObject = paramDPObject.getArray("List");
    int i = paramDPObject.getInt("StartIndex");
    if ((localObject != null) && (i == this.nextStartIndex))
    {
      if (this.nextStartIndex == 0)
      {
        this.lists.clear();
        this.lists.addAll(Arrays.asList(localObject));
      }
    }
    else if (this.lists.size() > 0)
    {
      localObject = ((DPObject)this.lists.get(0)).getString("Title");
      this.shopName.setText((CharSequence)localObject);
      this.cardlevel = ((DPObject)this.lists.get(0)).getInt("CardLevel");
      this.productid = getProductID((DPObject)this.lists.get(0));
      if (this.cardlevel != MCUtils.GENERAL_CARD_LEVEL)
        break label263;
      this.mSubmitButton.setText(freeJoinLabel);
    }
    while (true)
    {
      this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
      this.isEnd = paramDPObject.getBoolean("IsEnd");
      this.emptyMsg = paramDPObject.getString("EmptyMsg");
      this.recordCount = paramDPObject.getInt("RecordCount");
      paramDPObject = this.lists.iterator();
      do
        if (!paramDPObject.hasNext())
          break;
      while (((DPObject)paramDPObject.next()).getBoolean("Joined"));
      this.titleButton.setVisibility(0);
      this.adapter.buildAdapter();
      this.adapter.notifyDataSetChanged();
      return;
      this.lists.addAll(Arrays.asList(localObject));
      break;
      label263: if (this.cardlevel == MCUtils.VIP_CARD_LEVEL)
      {
        this.mSubmitButton.setText(buyNowLabel);
        continue;
      }
      this.mSubmitButton.setText(beMemberLabel);
    }
    this.isEdit = false;
    this.titleButton.setVisibility(8);
    this.btnBg.setVisibility(8);
    this.adapter.buildAdapter();
    this.adapter.notifyDataSetChanged();
  }

  private void chainCardListTask(String paramString, int paramInt)
  {
    paramString = new StringBuilder("http://mc.api.dianping.com/");
    paramString.append("chaincardlist.mc?");
    paramString.append("uuid=");
    paramString.append(Environment.uuid());
    if (accountService().token() != null)
      paramString.append("&token=").append(accountService().token());
    paramString.append("&cardgroupid=").append(this.membercardgroupid);
    Object localObject = getResources().getDisplayMetrics();
    paramString.append("&pixel=").append(((DisplayMetrics)localObject).widthPixels);
    paramString.append("&startindex=").append(paramInt);
    localObject = location();
    if (localObject != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      paramString.append("&lat=").append(localDecimalFormat.format(((Location)localObject).latitude()));
      paramString.append("&lng=").append(localDecimalFormat.format(((Location)localObject).longitude()));
    }
    paramString.append("&source=").append(this.source);
    this.chainCardListRequest = BasicMApiRequest.mapiGet(paramString.toString(), CacheType.DISABLED);
    mapiService().exec(this.chainCardListRequest, this);
  }

  private void handlerResult(DPObject paramDPObject)
  {
    appendCards(paramDPObject);
  }

  private void openBindScoreCardWebview(String paramString)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("membercardids", this.membercardid);
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString));
    paramString.putExtras(localBundle);
    startActivityForResult(paramString, 10);
  }

  private void openBindThirdPartyCardWebview(String paramString)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("membercardids", String.valueOf(this.membercardid));
    if (this.lists.size() > 0)
      localBundle.putString("shopid", String.valueOf(((DPObject)this.lists.get(0)).getInt("ShopID")));
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString));
    paramString.putExtras(localBundle);
    startActivity(paramString);
  }

  private void openThirdPartyCardInfoView(String paramString1, String paramString2)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse(paramString1));
    localIntent.putExtra("title", paramString2);
    localIntent.putExtra("source", this.source);
    startActivity(localIntent);
  }

  private void setupListView()
  {
    if (this.adapter != null)
    {
      this.adapter.buildAdapter();
      this.adapter.notifyDataSetChanged();
      this.listView.setOnItemClickListener(this);
      updateTitleButton();
      if (this.source != 14)
        break label86;
      this.titleText.setText("更多会员卡");
    }
    label86: 
    do
    {
      return;
      this.adapter = new Adapter();
      this.adapter.buildAdapter();
      this.listView.setAdapter(this.adapter);
      break;
    }
    while ((this.source < 30) || (this.source > 39));
    this.titleText.setText("会员卡推荐");
  }

  private void updateTitleButton()
  {
    if (this.isEdit)
    {
      this.titleButton.setText("取消");
      this.titleButton.setVisibility(0);
      this.mSubmitButton.setVisibility(0);
      this.btnBg.setVisibility(0);
      return;
    }
    this.titleButton.setText("批量添加");
    this.titleButton.setVisibility(0);
    this.mSubmitButton.setVisibility(8);
    this.btnBg.setVisibility(8);
  }

  public void batchJoinTask()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int j = 0;
    int i = 0;
    while (i < this.checkList.size())
    {
      int k = j;
      if (((Integer)this.checkList.get(i)).intValue() > 0)
      {
        k = j + 1;
        localStringBuilder.append(((Integer)this.checkList.get(i)).toString());
        localStringBuilder.append(",");
      }
      i += 1;
      j = k;
    }
    if (j > 0)
    {
      localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
      this.membercardid = localStringBuilder.toString();
      if (MemberCard.isThirdPartyCard((DPObject)this.lists.get(0)))
      {
        this.addingWeLifeCardTitle = ((DPObject)this.lists.get(0)).getString("Title");
        addThirdPartyCard(this.membercardid, this.source);
      }
      while (true)
      {
        statisticsEvent("chaincard5", "chaincard5_join", this.membercardgroupid + "|" + j + "|" + this.source, 0);
        return;
        if (this.isScoreCardChainList)
        {
          addScoreCards(this.membercardid, this.source);
          continue;
        }
        this.mJoinMCHandler.addCardQuick(this.membercardid, this.source, this.productid, this.cardlevel);
      }
    }
    Toast.makeText(this, "请选择您要添加的分店", 0).show();
  }

  public int getProductID(DPObject paramDPObject)
  {
    int j = 0;
    paramDPObject = paramDPObject.getArray("ProductList");
    int i = j;
    if (paramDPObject != null)
    {
      i = j;
      if (paramDPObject.length > 0)
        i = paramDPObject[0].getInt("ProductID");
    }
    return i;
  }

  public void gotoMyCard()
  {
    if (this.membercardid == null)
      return;
    String str = this.membercardid;
    if (this.membercardid.contains(","))
      str = this.membercardid.substring(0, this.membercardid.indexOf(","));
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://membercardinfo?membercardid=" + str)));
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (((paramInt2 == -1) || (paramInt2 == 10) || (paramInt2 == 20)) && (this.isEdit))
    {
      setResult(paramInt2);
      finish();
      gotoMyCard();
    }
  }

  public void onClick(View paramView)
  {
    boolean bool = false;
    if (paramView.getId() == R.id.title_button)
    {
      if (!this.isEdit)
        statisticsEvent("chaincard5", "chaincard5_numadd", this.membercardgroupid + "|" + this.source, 0);
      if (!this.isEdit)
        bool = true;
      this.isEdit = bool;
      updateTitleButton();
      this.adapter.buildAdapter();
      this.adapter.notifyDataSetChanged();
    }
    do
      return;
    while (paramView.getId() != R.id.submit);
    batchJoinTask();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    freeJoinLabel = getResources().getString(R.string.mc_free_join);
    buyNowLabel = getResources().getString(R.string.mc_buy_now);
    beMemberLabel = getResources().getString(R.string.mc_be_member);
    parseIntent();
    this.mReceiver = new MyReceiver(null);
    this.mFilter = new IntentFilter("com.dianping.action.QUIT_MEMBER_CARD");
    registerReceiver(this.mReceiver, this.mFilter);
    paramBundle = new IntentFilter("com.dianping.action.JOIN_MEMBER_CARD");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.UPDATE_LIST_DATA");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("Card:JoinSuccess");
    registerReceiver(this.mReceiver, paramBundle);
    this.mJoinMCHandler = new JoinMCHandler(this);
    this.mJoinMCHandler.setOnJoinCardRequestHandlerListener(this);
    this.mJoinMScoreCHandler = new JoinMScoreCHandler(this);
    this.mJoinMScoreCHandler.setJoinScoreCardHandlerListener(this.onJoinScoreCardHandlerListener);
    this.joinMCHandler2 = new JoinMCHandler2(this);
    this.joinMCHandler2.setJoinThirdPartyCardHandlerListener(this.onJoinThirdPartyCardHandlerListener);
    setupListView();
  }

  protected void onDestroy()
  {
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    if (this.mJoinMCHandler != null)
      this.mJoinMCHandler.removeListener();
    this.mJoinMScoreCHandler.setJoinScoreCardHandlerListener(null);
    this.joinMCHandler2.stopJoinThirdPartyCards();
    this.joinMCHandler2.setJoinThirdPartyCardHandlerListener(null);
    super.onDestroy();
  }

  public void onFinish()
  {
    if (this.chainCardListRequest != null)
    {
      mapiService().abort(this.chainCardListRequest, this, true);
      this.chainCardListRequest = null;
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (!(paramView instanceof AvailableCardListItem));
    do
    {
      return;
      paramAdapterView = (DPObject)this.listView.getItemAtPosition(paramInt);
    }
    while (!(paramAdapterView instanceof DPObject));
    int i = paramAdapterView.getInt("MemberCardID");
    if (this.isEdit)
    {
      ((AvailableCardListItem)paramView).setChecked();
      if (((AvailableCardListItem)paramView).isChecked())
      {
        this.checkList.set(paramInt, Integer.valueOf(i));
        return;
      }
      this.checkList.set(paramInt, Integer.valueOf(0));
      return;
    }
    this.membercardid = String.valueOf(i);
    paramView = paramAdapterView.getString("Title");
    if (paramAdapterView.getBoolean("Joined"))
    {
      if (MemberCard.isThirdPartyCard(paramAdapterView))
        openThirdPartyCardInfoView(paramAdapterView.getString("NavigateUrl"), paramView);
      while (true)
      {
        statisticsEvent("chaincard5", "chaincard5_item_view", i + "|" + paramView + "|" + this.source, 0);
        return;
        gotoMyCard();
      }
    }
    if (MemberCard.isThirdPartyCard(paramAdapterView))
      openThirdPartyCardInfoView(paramAdapterView.getString("NavigateUrl"), paramView);
    while (true)
    {
      statisticsEvent("chaincard5", "chaincard5_item_add", i + "|" + paramView + "|" + this.source, 0);
      return;
      this.mJoinMCHandler.addCard(String.valueOf(this.membercardid), this.source, paramAdapterView.getInt("CardLevel"), paramAdapterView.getString("Title"));
    }
  }

  public void onJoinCardFinish(DPObject paramDPObject)
  {
    paramDPObject = new Intent("com.dianping.action.JOIN_MEMBER_CARD");
    Bundle localBundle = new Bundle();
    localBundle.putString("membercardgroupid", this.membercardgroupid);
    paramDPObject.putExtras(localBundle);
    sendBroadcast(paramDPObject);
    if (this.isEdit)
    {
      gotoMyCard();
      finish();
    }
  }

  public boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mJoinMCHandler.onLoginAddCard();
      return true;
    }
    return false;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.errorMsg = paramMApiResponse.message().toString();
    this.adapter.notifyDataSetChanged();
    this.chainCardListRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.chainCardListRequest)
    {
      if (!(paramMApiResponse.result() instanceof DPObject))
        break label39;
      handlerResult((DPObject)paramMApiResponse.result());
    }
    while (true)
    {
      this.chainCardListRequest = null;
      return;
      label39: this.errorMsg = paramMApiResponse.message().toString();
      this.adapter.notifyDataSetChanged();
    }
  }

  public void parseIntent()
  {
    Object localObject = getIntent().getData();
    this.membercardgroupid = ((Uri)localObject).getQueryParameter("membercardgroupid");
    String str = ((Uri)localObject).getQueryParameter("source");
    try
    {
      this.source = Integer.parseInt(str);
      label35: str = ((Uri)localObject).getQueryParameter("flag");
      if ((str != null) && (str.equals("1")));
      for (this.isEdit = true; ; this.isEdit = false)
      {
        localObject = ((Uri)localObject).getQueryParameter("isscorecard");
        if (!TextUtils.isEmpty((CharSequence)localObject))
          break;
        this.isScoreCardChainList = false;
        return;
      }
      this.isScoreCardChainList = Boolean.valueOf((String)localObject).booleanValue();
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      break label35;
    }
  }

  public void refresh()
  {
    this.adapter.reset();
  }

  public void refreshList(String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      Intent localIntent = new Intent("com.dianping.action.JOIN_MEMBER_CARD");
      Bundle localBundle = new Bundle();
      localBundle.putString("membercardgroupid", this.membercardgroupid);
      localIntent.putExtras(localBundle);
      sendBroadcast(localIntent);
    }
    if (paramString.contains(","))
      return;
    while (true)
    {
      int i;
      try
      {
        int j = Integer.parseInt(paramString);
        i = 0;
        if (i >= this.lists.size())
          break;
        if (((DPObject)this.lists.get(i)).getInt("MemberCardID") == j)
        {
          paramString = ((DPObject)this.lists.get(i)).edit().putBoolean("Joined", paramBoolean).generate();
          this.lists.set(i, paramString);
          return;
        }
      }
      catch (NumberFormatException paramString)
      {
        return;
      }
      i += 1;
    }
  }

  protected void setupView()
  {
    super.setContentView(R.layout.chain_card_list);
    this.btnBg = ((LinearLayout)findViewById(R.id.submit_bg));
    this.mSubmitButton = ((Button)findViewById(R.id.submit));
    this.mSubmitButton.setOnClickListener(this);
    this.titleButton.setOnClickListener(this);
    this.shopName = ((TextView)findViewById(R.id.shop_name));
    this.shopCount = ((TextView)findViewById(R.id.shop_count));
  }

  class Adapter extends BasicAdapter
  {
    private ArrayList<DPObject> itemLists = new ArrayList();

    public Adapter()
    {
    }

    private boolean loadNewPage()
    {
      if (ChainCardListActivity.this.isEnd);
      do
        return false;
      while (ChainCardListActivity.this.chainCardListRequest != null);
      ChainCardListActivity.this.errorMsg = null;
      ChainCardListActivity localChainCardListActivity = ChainCardListActivity.this;
      if (ChainCardListActivity.this.getAccount() == null);
      for (String str = ""; ; str = ChainCardListActivity.this.getAccount().token())
      {
        localChainCardListActivity.chainCardListTask(str, ChainCardListActivity.this.nextStartIndex);
        notifyDataSetChanged();
        return true;
      }
    }

    public void buildAdapter()
    {
      this.itemLists.clear();
      ChainCardListActivity.this.checkList.clear();
      DPObject localDPObject;
      if (ChainCardListActivity.this.isEdit)
      {
        localIterator = ChainCardListActivity.this.lists.iterator();
        while (localIterator.hasNext())
        {
          localDPObject = (DPObject)localIterator.next();
          if (localDPObject.getBoolean("Joined"))
            continue;
          this.itemLists.add(localDPObject);
          ChainCardListActivity.this.checkList.add(Integer.valueOf(0));
        }
        ChainCardListActivity.this.shopCount.setText("(" + this.itemLists.size() + "家未添加分店)");
        return;
      }
      Iterator localIterator = ChainCardListActivity.this.lists.iterator();
      while (localIterator.hasNext())
      {
        localDPObject = (DPObject)localIterator.next();
        this.itemLists.add(localDPObject);
      }
      ChainCardListActivity.this.shopCount.setText("(" + this.itemLists.size() + "家分店)");
    }

    public int getCount()
    {
      if (ChainCardListActivity.this.emptyMsg != null)
        return 1;
      if (ChainCardListActivity.this.isEnd)
        return this.itemLists.size();
      return this.itemLists.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if ((ChainCardListActivity.this.emptyMsg != null) && (paramInt == 0))
        return EMPTY;
      if (paramInt < this.itemLists.size())
        return this.itemLists.get(paramInt);
      if (ChainCardListActivity.this.errorMsg == null)
        return LOADING;
      return ERROR;
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
        paramView = (AvailableCardListItem)ChainCardListActivity.this.getLayoutInflater().inflate(R.layout.available_card_list_item, paramViewGroup, false);
        int i;
        int j;
        String str;
        if (ChainCardListActivity.this.isEdit)
        {
          i = 2;
          paramView.setAvailableCard((DPObject)localObject, i);
          i = ((DPObject)localObject).getInt("MemberCardID");
          j = ((DPObject)localObject).getInt("MemberCardID");
          paramViewGroup = ((DPObject)localObject).getString("Title");
          bool = MemberCard.isThirdPartyCard((DPObject)localObject);
          str = ((DPObject)localObject).getString("NavigateUrl");
          if (((DPObject)localObject).getBoolean("Joined"))
            break label218;
          paramView.status.setOnClickListener(new View.OnClickListener(i, bool, paramViewGroup)
          {
            public void onClick(View paramView)
            {
              ChainCardListActivity.this.membercardid = String.valueOf(this.val$cardid);
              ChainCardListActivity localChainCardListActivity;
              StringBuilder localStringBuilder;
              if (this.val$isThirdPartyCard)
              {
                ChainCardListActivity.this.addingWeLifeCardTitle = this.val$title;
                ChainCardListActivity.this.addThirdPartyCard(ChainCardListActivity.this.membercardid, ChainCardListActivity.this.source);
                localChainCardListActivity = ChainCardListActivity.this;
                localStringBuilder = new StringBuilder().append(this.val$cardid).append("|").append(this.val$title).append("|");
                if ((ChainCardListActivity.this.cardlevel != 1) && (ChainCardListActivity.this.cardlevel != 0))
                  break label258;
              }
              label258: for (paramView = "普通"; ; paramView = "高级")
              {
                localChainCardListActivity.statisticsEvent("chaincard5", "chaincard5_directlyadd", paramView, 0);
                return;
                if (!ChainCardListActivity.this.isScoreCardChainList)
                {
                  ChainCardListActivity.this.mJoinMCHandler.addCardQuick(ChainCardListActivity.this.membercardid, ChainCardListActivity.this.source, ChainCardListActivity.this.productid, ChainCardListActivity.this.cardlevel);
                  break;
                }
                ChainCardListActivity.this.addScoreCards(ChainCardListActivity.this.membercardid, ChainCardListActivity.this.source);
                break;
              }
            }
          });
          label148: if (ChainCardListActivity.this.isEdit)
          {
            paramViewGroup = paramView.checkBox;
            if (((Integer)ChainCardListActivity.this.checkList.get(paramInt)).intValue() <= 0)
              break label243;
          }
        }
        label218: label243: for (boolean bool = true; ; bool = false)
        {
          paramViewGroup.setChecked(bool);
          paramView.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(paramInt, j)
          {
            public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
            {
              if (paramBoolean)
              {
                ChainCardListActivity.this.checkList.set(this.val$pos, Integer.valueOf(this.val$addcardid));
                return;
              }
              ChainCardListActivity.this.checkList.set(this.val$pos, Integer.valueOf(0));
            }
          });
          return paramView;
          i = 1;
          break;
          paramView.status.setOnClickListener(new View.OnClickListener(bool, str, paramViewGroup, i)
          {
            public void onClick(View paramView)
            {
              if (this.val$isThirdPartyCard)
              {
                ChainCardListActivity.this.openThirdPartyCardInfoView(this.val$thirdPartyCardUri, this.val$title);
                return;
              }
              ChainCardListActivity.this.membercardid = String.valueOf(this.val$cardid);
              ChainCardListActivity.this.gotoMyCard();
            }
          });
          break label148;
        }
      }
      if (localObject == LOADING)
      {
        if (ChainCardListActivity.this.errorMsg == null)
          loadNewPage();
        return getLoadingView(paramViewGroup, paramView);
      }
      if (localObject == EMPTY)
        return getEmptyView(ChainCardListActivity.this.emptyMsg, "数据为空", paramViewGroup, paramView);
      return getFailedView(ChainCardListActivity.this.errorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          ChainCardListActivity.Adapter.this.loadNewPage();
        }
      }
      , paramViewGroup, paramView);
    }

    public void reset()
    {
      ChainCardListActivity.this.onFinish();
      ChainCardListActivity.this.lists = new ArrayList();
      ChainCardListActivity.this.nextStartIndex = 0;
      ChainCardListActivity.this.isEnd = false;
      ChainCardListActivity.this.errorMsg = null;
      ChainCardListActivity.this.emptyMsg = null;
      buildAdapter();
      notifyDataSetChanged();
    }
  }

  private class MyReceiver extends BroadcastReceiver
  {
    private MyReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      if (MCUtils.isMemberCardRelativeBroadcast(paramIntent))
      {
        if (!paramContext.equals("com.dianping.action.UPDATE_LIST_DATA"))
          break label29;
        ChainCardListActivity.this.refresh();
      }
      label29: 
      do
      {
        return;
        if (paramContext.equals("Card:JoinSuccess"))
        {
          MCUtils.sendJoinScoreCardSuccessBroadcast(ChainCardListActivity.this, ChainCardListActivity.this.membercardid);
          ChainCardListActivity.this.setResult(-1);
          ChainCardListActivity.this.finish();
          return;
        }
        paramIntent = paramIntent.getStringExtra("membercardid");
      }
      while (paramIntent == null);
      if (paramContext.equals("com.dianping.action.JOIN_MEMBER_CARD"))
        ChainCardListActivity.this.refreshList(paramIntent, true);
      while (ChainCardListActivity.this.adapter != null)
      {
        ChainCardListActivity.this.adapter.buildAdapter();
        ChainCardListActivity.this.adapter.notifyDataSetChanged();
        return;
        if (!paramContext.equals("com.dianping.action.QUIT_MEMBER_CARD"))
          continue;
        ChainCardListActivity.this.refreshList(paramIntent, false);
      }
      ChainCardListActivity.this.adapter = new ChainCardListActivity.Adapter(ChainCardListActivity.this);
      ChainCardListActivity.this.adapter.buildAdapter();
      ChainCardListActivity.this.listView.setAdapter(ChainCardListActivity.this.adapter);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.ChainCardListActivity
 * JD-Core Version:    0.6.0
 */
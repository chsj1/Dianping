package com.dianping.membercard;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.membercard.model.JoinMCHandler;
import com.dianping.membercard.model.JoinMCHandler.OnJoinCardRequestHandlerListener;
import com.dianping.membercard.model.JoinMScoreCHandler;
import com.dianping.membercard.model.JoinMScoreCHandler.OnJoinScoreCardHandlerListener;
import com.dianping.membercard.utils.BasicCardAdapter;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.membercard.view.AvailableCardListItem;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;

public class BranchCardListActivity extends NovaListActivity
  implements AdapterView.OnItemClickListener, JoinMCHandler.OnJoinCardRequestHandlerListener
{
  private static final String MESSAGE_DIALOG_ADD_CARD_WAITING = "正在提交请求，请稍候...";
  private static final int REQUEST_CODE_BIND_SCORE_CARD_10 = 10;
  public static final int RESULT_CODE_ADD_SCORE_CARD_SUCCEED_50 = 50;
  CardAdapter adapter;
  DPObject cardobject;
  JoinMScoreCHandler joinMScoreCHandler;
  ArrayList<DPObject> lists = new ArrayList();
  private IntentFilter mFilter;
  JoinMCHandler mJoinMCHandler;
  private MyReceiver mReceiver;
  String membercardgroupid;
  int membercardid;
  private JoinMScoreCHandler.OnJoinScoreCardHandlerListener onJoinScoreCardHandlerListener = new JoinMScoreCHandler.OnJoinScoreCardHandlerListener()
  {
    public void onJoinScoreCardFail(String paramString)
    {
      BranchCardListActivity.this.dismissDialog();
      Toast.makeText(BranchCardListActivity.this, paramString, 0).show();
    }

    public void onJoinScoreCardFailForNeedMemberInfo(String paramString1, String paramString2)
    {
      BranchCardListActivity.this.dismissDialog();
      BranchCardListActivity.this.openBindScoreCardWebview(paramString2);
    }

    public void onJoinScoreCardSuccess()
    {
      BranchCardListActivity.this.dismissDialog();
      MCUtils.sendJoinScoreCardSuccessBroadcast(BranchCardListActivity.this, String.valueOf(BranchCardListActivity.this.membercardgroupid));
      BranchCardListActivity.this.gotoAddScoreCardSucceedView();
    }
  };
  int source;

  private void addScoreCard()
  {
    if (getAccount() == null)
      throw new RuntimeException("it is not login status");
    showProgressDialog("正在提交请求，请稍候...");
    this.joinMScoreCHandler.joinScoreCards(String.valueOf(this.membercardid), this.source);
  }

  private void gotoAddScoreCardSucceedView()
  {
    Intent localIntent = new Intent();
    Bundle localBundle = new Bundle();
    localBundle.putInt("membercardid", this.membercardid);
    localIntent.putExtras(localBundle);
    setResult(50, localIntent);
    finish();
  }

  private void openBindScoreCardWebview(String paramString)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("membercardids", String.valueOf(this.membercardid));
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString));
    paramString.putExtras(localBundle);
    startActivityForResult(paramString, 10);
  }

  private void setupListView()
  {
    if (this.adapter != null)
      this.adapter.notifyDataSetChanged();
    while (true)
    {
      this.listView.setOnItemClickListener(this);
      return;
      this.adapter = new CardAdapter(this, this.lists, false);
      this.listView.setAdapter(this.adapter);
    }
  }

  public void goBackMyCard(DPObject paramDPObject)
  {
    Object localObject = this.cardobject.getArray("SubCardList");
    int i = 0;
    while (true)
    {
      if (i < localObject.length)
      {
        if (localObject[i].getInt("MemberCardID") == paramDPObject.getInt("MemberCardID"))
          localObject[i] = paramDPObject;
      }
      else
      {
        String str1 = this.cardobject.getString("ShopIDs");
        String str2 = this.cardobject.getString("ProductTypeList");
        this.cardobject = paramDPObject.edit().putArray("SubCardList", localObject).generate();
        this.cardobject = this.cardobject.edit().putString("ShopIDs", str1).generate();
        this.cardobject = this.cardobject.edit().putString("ProductTypeList", str2).generate();
        paramDPObject = new Intent();
        localObject = new Bundle();
        ((Bundle)localObject).putParcelable("cardobject", this.cardobject);
        paramDPObject.putExtras((Bundle)localObject);
        setResult(20, paramDPObject);
        finish();
        return;
      }
      i += 1;
    }
  }

  public void joinTask()
  {
    this.mJoinMCHandler.joinTask(String.valueOf(this.membercardid), this.source, 1);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    parseIntent();
    this.source = 15;
    this.mReceiver = new MyReceiver(null);
    this.mFilter = new IntentFilter("com.dianping.action.QUIT_MEMBER_CARD");
    registerReceiver(this.mReceiver, this.mFilter);
    paramBundle = new IntentFilter("com.dianping.action.JOIN_MEMBER_CARD");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("Card:JoinSuccess");
    registerReceiver(this.mReceiver, paramBundle);
    this.mJoinMCHandler = new JoinMCHandler(this);
    this.mJoinMCHandler.setOnJoinCardRequestHandlerListener(this);
    this.joinMScoreCHandler = new JoinMScoreCHandler(this);
    this.joinMScoreCHandler.setJoinScoreCardHandlerListener(this.onJoinScoreCardHandlerListener);
    setupListView();
  }

  protected void onDestroy()
  {
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    if (this.mJoinMCHandler != null)
      this.mJoinMCHandler.removeListener();
    this.joinMScoreCHandler.setJoinScoreCardHandlerListener(null);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = (DPObject)this.listView.getItemAtPosition(paramInt);
    this.membercardid = paramAdapterView.getInt("MemberCardID");
    paramView = paramAdapterView.getString("MemberCardGroupID");
    paramInt = paramAdapterView.getInt("ShopID");
    String str = paramAdapterView.getString("Title");
    boolean bool = MemberCard.isScoreCard(this.cardobject);
    if (paramAdapterView.getBoolean("Joined"))
    {
      goBackMyCard(paramAdapterView);
      statisticsEvent("mycard5", "mycard5_chain_otheradded", paramView + "|" + paramInt + "|" + str, 0);
      return;
    }
    new AlertDialog.Builder(this).setMessage("您尚未添加此分店，是否添加？").setPositiveButton("添加", new DialogInterface.OnClickListener(bool, paramView, paramInt, str)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if (this.val$isScoreCard)
          BranchCardListActivity.this.addScoreCard();
        while (true)
        {
          BranchCardListActivity.this.statisticsEvent("mycard5", "mycard5_chain_otheraddyes", this.val$memberCardGroupID + "|" + this.val$shopid + "|" + this.val$title, 0);
          return;
          BranchCardListActivity.this.joinTask();
        }
      }
    }).setNegativeButton("取消", null).show();
    statisticsEvent("mycard5", "mycard5_chain_otheradd", paramView + "|" + paramInt + "|" + str, 0);
  }

  public void onJoinCardFinish(DPObject paramDPObject)
  {
    goBackMyCard(paramDPObject);
  }

  public void parseIntent()
  {
    Object localObject2 = getIntent();
    Object localObject1 = ((Intent)localObject2).getData();
    this.cardobject = ((DPObject)((Intent)localObject2).getExtras().getParcelable("cardObject"));
    this.lists.clear();
    if (this.cardobject != null)
    {
      localObject2 = this.cardobject.getArray("SubCardList");
      if (localObject2 != null)
      {
        int i = 0;
        while (i < localObject2.length)
        {
          this.lists.add(localObject2[i]);
          i += 1;
        }
      }
    }
    this.membercardgroupid = ((Uri)localObject1).getQueryParameter("membercardgroupid");
    localObject1 = ((Uri)localObject1).getQueryParameter("source");
    try
    {
      this.source = Integer.parseInt((String)localObject1);
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
  }

  public void refreshList(String paramString, boolean paramBoolean)
  {
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

  class CardAdapter extends BasicCardAdapter<DPObject>
  {
    public CardAdapter(List<DPObject> paramBoolean, boolean arg3)
    {
      super(localList, bool);
    }

    public int getItemResource()
    {
      return R.layout.available_card_list_item;
    }

    public View getItemView(int paramInt, View paramView, BasicCardAdapter<DPObject>.ViewHolder paramBasicCardAdapter)
    {
      paramView = (AvailableCardListItem)paramView;
      paramView.setAvailableCard((DPObject)getItem(paramInt), 3);
      return paramView;
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
        paramIntent = paramIntent.getStringExtra("membercardid");
        if (paramIntent != null);
      }
      else
      {
        return;
      }
      if (paramContext.equals("com.dianping.action.JOIN_MEMBER_CARD"))
        BranchCardListActivity.this.refreshList(paramIntent, true);
      while (true)
      {
        BranchCardListActivity.this.adapter.replaceAll(BranchCardListActivity.this.lists);
        BranchCardListActivity.this.setupListView();
        return;
        if (paramContext.equals("com.dianping.action.QUIT_MEMBER_CARD"))
        {
          BranchCardListActivity.this.refreshList(paramIntent, false);
          continue;
        }
        if (!paramContext.equals("Card:JoinSuccess"))
          continue;
        BranchCardListActivity.this.lists = new ArrayList();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.BranchCardListActivity
 * JD-Core Version:    0.6.0
 */
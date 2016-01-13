package com.dianping.membercard;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyPrepaidCardConsumeListActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
  private static final int REQUEST_CODE_RECHARGE = 0;
  private static final String TIME_FORMAT_STRING = "yyyy年MM月dd日";
  private static final String URL = "http://app.t.dianping.com/myprepaidcardconsumelistgn.bin";
  private static MApiRequest mPrePaidCardHistoryReq;
  String mAccountId;
  View mAttentionLay;
  TextView mAttentionTv;
  TextView mBalanceTv;
  MApiRequest mCancelPrePaidCardGiftSendReq;
  String mCardId;
  View mConsumeContainerLay;
  View mConsumeEmpty;
  ImageView mConsumeExpandArrow;
  View mConsumeHistoryTitle;
  LinearLayout mConsumeListLay;
  View mContentView;
  DPObject mDealObj;
  DPObject mDeletingCardTrade;
  View mErrorView;
  Button mGiftBtn;
  View mGiftContainerLay;
  View mGiftEmpty;
  ImageView mGiftExpandArrow;
  View mGiftHistoryTitle;
  LinearLayout mGiftListLay;
  View mGiftUpLineView;
  DPObject mHistoryObj;
  boolean mIsConsumeListExpand = false;
  boolean mIsGiftListExpand = false;
  boolean mIsRechargeListExpand = false;
  View mLoadingView;
  Button mRechargeBtn;
  View mRechargeContainerLay;
  View mRechargeEmpty;
  ImageView mRechargeExpandArrow;
  View mRechargeHistoryTitle;
  LinearLayout mRechargeListLay;
  String title;

  protected void deleteGiftTrade(DPObject paramDPObject)
  {
    String str1 = accountService().token();
    if (str1 == null);
    String str2;
    do
    {
      return;
      str2 = this.mCardId;
    }
    while (TextUtils.isEmpty(str2));
    if (this.mCancelPrePaidCardGiftSendReq != null)
      mapiService().abort(this.mCancelPrePaidCardGiftSendReq, this, true);
    showProgressDialog("正在取消赠送...");
    this.mCancelPrePaidCardGiftSendReq = BasicMApiRequest.mapiPost("http://app.t.dianping.com/cancelprepaidcardgiftgn.bin", new String[] { "token", str1, "accountid", this.mAccountId, "prepaidcardid", str2, "tradeid", paramDPObject.getString("TradeID") });
    mapiService().exec(this.mCancelPrePaidCardGiftSendReq, this);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 0)
      requestHistory();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getData();
    if (paramBundle != null)
    {
      this.mCardId = paramBundle.getQueryParameter("prepaidcardid");
      this.mAccountId = paramBundle.getQueryParameter("accountid");
      this.title = paramBundle.getQueryParameter("title");
    }
    setupView();
    requestHistory();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (mPrePaidCardHistoryReq != null)
    {
      mapiService().abort(mPrePaidCardHistoryReq, this, true);
      mPrePaidCardHistoryReq = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == mPrePaidCardHistoryReq)
    {
      mPrePaidCardHistoryReq = null;
      this.mLoadingView.setVisibility(8);
      this.mErrorView.setVisibility(0);
      this.mContentView.setVisibility(8);
    }
    do
      return;
    while (paramMApiRequest != this.mCancelPrePaidCardGiftSendReq);
    dismissDialog();
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest != null)
      showAlertDialog(paramMApiRequest.getString("Title"), paramMApiRequest.getString("Content"));
    this.mCancelPrePaidCardGiftSendReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == mPrePaidCardHistoryReq)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mHistoryObj = ((DPObject)paramMApiResponse.result());
        this.mLoadingView.setVisibility(8);
        this.mErrorView.setVisibility(8);
        this.mContentView.setVisibility(0);
        updateView();
      }
      mPrePaidCardHistoryReq = null;
    }
    do
      return;
    while (paramMApiRequest != this.mCancelPrePaidCardGiftSendReq);
    dismissDialog();
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest.getInt("Flag") == 1)
      requestHistory();
    showAlertDialog(paramMApiRequest.getString("Title"), paramMApiRequest.getString("Content"));
    this.mCancelPrePaidCardGiftSendReq = null;
  }

  void requestHistory()
  {
    if (mPrePaidCardHistoryReq != null)
      mapiService().abort(mPrePaidCardHistoryReq, this, true);
    Object localObject = accountService().token();
    if (localObject == null)
      return;
    localObject = Uri.parse("http://app.t.dianping.com/myprepaidcardconsumelistgn.bin").buildUpon().appendQueryParameter("accountid", this.mAccountId).appendQueryParameter("token", (String)localObject);
    if (!TextUtils.isEmpty(this.mCardId))
      ((Uri.Builder)localObject).appendQueryParameter("prepaidcardid", this.mCardId);
    mPrePaidCardHistoryReq = BasicMApiRequest.mapiGet(((Uri.Builder)localObject).build().toString(), CacheType.DISABLED);
    mapiService().exec(mPrePaidCardHistoryReq, this);
    this.mLoadingView.setVisibility(0);
    this.mErrorView.setVisibility(8);
    this.mContentView.setVisibility(8);
  }

  void setupView()
  {
    super.setContentView(R.layout.my_prepaidcard_history_layout);
    this.mContentView = findViewById(R.id.content);
    this.mLoadingView = findViewById(R.id.loading_view);
    this.mErrorView = findViewById(R.id.error_view);
    if ((this.mErrorView instanceof LoadingErrorView))
      ((LoadingErrorView)this.mErrorView).setCallBack(new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          MyPrepaidCardConsumeListActivity.this.requestHistory();
        }
      });
    super.setTitle(this.title);
    this.mBalanceTv = ((TextView)findViewById(R.id.balance));
    this.mRechargeBtn = ((Button)findViewById(R.id.rechargeBtn));
    this.mRechargeBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MyPrepaidCardConsumeListActivity.this.statisticsEvent("usedetail5", "usedetail5_buy ", "", 0);
        if (MyPrepaidCardConsumeListActivity.this.mDealObj != null)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
          paramView.putExtra("deal", MyPrepaidCardConsumeListActivity.this.mDealObj);
          MyPrepaidCardConsumeListActivity.this.startActivityForResult(paramView, 0);
          return;
        }
        String str = MyPrepaidCardConsumeListActivity.this.mHistoryObj.getString("ChargeDisableReason");
        paramView = str;
        if (str == null)
          paramView = "不能充值";
        MyPrepaidCardConsumeListActivity.this.showAlertDialog(null, paramView);
      }
    });
    this.mConsumeHistoryTitle = findViewById(R.id.consume_history_title);
    this.mConsumeContainerLay = findViewById(R.id.consume_container_lay);
    this.mConsumeExpandArrow = ((ImageView)findViewById(R.id.consume_history_arrow));
    this.mConsumeHistoryTitle.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (MyPrepaidCardConsumeListActivity.this.mIsConsumeListExpand)
        {
          MyPrepaidCardConsumeListActivity.this.mIsConsumeListExpand = false;
          MyPrepaidCardConsumeListActivity.this.mConsumeExpandArrow.setImageResource(R.drawable.ic_arrow_down_black);
          MyPrepaidCardConsumeListActivity.this.mConsumeContainerLay.setVisibility(8);
          return;
        }
        MyPrepaidCardConsumeListActivity.this.mIsConsumeListExpand = true;
        MyPrepaidCardConsumeListActivity.this.mConsumeExpandArrow.setImageResource(R.drawable.ic_arrow_up_black);
        MyPrepaidCardConsumeListActivity.this.mConsumeContainerLay.setVisibility(0);
        MyPrepaidCardConsumeListActivity.this.statisticsEvent("usedetail5", "usedetail5_conrecord ", "", 0);
      }
    });
    this.mConsumeEmpty = findViewById(R.id.consume_history_empty);
    this.mConsumeListLay = ((LinearLayout)findViewById(R.id.consume_history_lay));
    this.mRechargeHistoryTitle = findViewById(R.id.recharge_history_title);
    this.mRechargeContainerLay = findViewById(R.id.recharge_container_lay);
    this.mRechargeExpandArrow = ((ImageView)findViewById(R.id.recharge_history_arrow));
    this.mRechargeHistoryTitle.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (MyPrepaidCardConsumeListActivity.this.mIsRechargeListExpand)
        {
          MyPrepaidCardConsumeListActivity.this.mIsRechargeListExpand = false;
          MyPrepaidCardConsumeListActivity.this.mRechargeExpandArrow.setImageResource(R.drawable.ic_arrow_down_black);
          MyPrepaidCardConsumeListActivity.this.mRechargeContainerLay.setVisibility(8);
          return;
        }
        MyPrepaidCardConsumeListActivity.this.mIsRechargeListExpand = true;
        MyPrepaidCardConsumeListActivity.this.mRechargeExpandArrow.setImageResource(R.drawable.ic_arrow_up_black);
        MyPrepaidCardConsumeListActivity.this.mRechargeContainerLay.setVisibility(0);
        MyPrepaidCardConsumeListActivity.this.statisticsEvent("usedetail5", "usedetail5_paidrecord ", "", 0);
      }
    });
    this.mRechargeEmpty = findViewById(R.id.recharge_history_empty);
    this.mRechargeListLay = ((LinearLayout)findViewById(R.id.recharge_history_lay));
    this.mGiftUpLineView = findViewById(R.id.gift_up_line);
    this.mGiftHistoryTitle = findViewById(R.id.gift_history_title);
    this.mGiftContainerLay = findViewById(R.id.gift_container_lay);
    this.mGiftExpandArrow = ((ImageView)findViewById(R.id.gift_history_arrow));
    this.mGiftHistoryTitle.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (MyPrepaidCardConsumeListActivity.this.mIsGiftListExpand)
        {
          MyPrepaidCardConsumeListActivity.this.mIsGiftListExpand = false;
          MyPrepaidCardConsumeListActivity.this.mGiftExpandArrow.setImageResource(R.drawable.ic_arrow_down_black);
          MyPrepaidCardConsumeListActivity.this.mGiftContainerLay.setVisibility(8);
          return;
        }
        MyPrepaidCardConsumeListActivity.this.mIsGiftListExpand = true;
        MyPrepaidCardConsumeListActivity.this.mGiftExpandArrow.setImageResource(R.drawable.ic_arrow_up_black);
        MyPrepaidCardConsumeListActivity.this.mGiftContainerLay.setVisibility(0);
        MyPrepaidCardConsumeListActivity.this.statisticsEvent("usedetail5", "usedetail5_presentrecord ", "", 0);
      }
    });
    this.mGiftEmpty = findViewById(R.id.gift_history_empty);
    this.mGiftListLay = ((LinearLayout)findViewById(R.id.gift_history_lay));
    this.mAttentionLay = findViewById(R.id.attention_lay);
    this.mAttentionTv = ((TextView)findViewById(R.id.attention_content));
  }

  void updateView()
  {
    if (this.mHistoryObj == null)
      return;
    Object localObject3 = this.mHistoryObj.getString("Balance");
    String str = this.mHistoryObj.getString("SpecialDesc");
    this.mDealObj = this.mHistoryObj.getObject("ReleventDeal");
    Object localObject6 = this.mHistoryObj.getObject("ConsumeList");
    Object localObject5 = this.mHistoryObj.getObject("PaidList");
    Object localObject4 = this.mHistoryObj.getObject("GiftList");
    Object localObject2 = null;
    DPObject[] arrayOfDPObject = null;
    Object localObject1 = null;
    if (localObject6 != null)
      localObject2 = ((DPObject)localObject6).getArray("List");
    if (localObject5 != null)
      arrayOfDPObject = ((DPObject)localObject5).getArray("List");
    if (localObject4 != null)
      localObject1 = ((DPObject)localObject4).getArray("List");
    if (!TextUtils.isEmpty((CharSequence)localObject3))
    {
      this.mBalanceTv.setText("￥" + (String)localObject3);
      this.mConsumeListLay.removeAllViews();
      this.mRechargeListLay.removeAllViews();
      this.mGiftListLay.removeAllViews();
      if (localObject2 != null)
        break label290;
      this.mConsumeEmpty.setVisibility(0);
      label199: if (arrayOfDPObject != null)
        break label453;
      this.mRechargeEmpty.setVisibility(0);
      label211: if (localObject1 != null)
        break label612;
      this.mGiftEmpty.setVisibility(8);
      this.mGiftListLay.setVisibility(8);
      this.mGiftHistoryTitle.setVisibility(8);
      this.mGiftUpLineView.setVisibility(8);
    }
    while (true)
    {
      if (TextUtils.isEmpty(str))
        break label922;
      this.mAttentionLay.setVisibility(0);
      this.mAttentionTv.setText(str);
      return;
      this.mBalanceTv.setText("￥0");
      break;
      label290: if (localObject2.length == 0)
      {
        this.mConsumeEmpty.setVisibility(0);
        this.mConsumeListLay.setVisibility(8);
        break label199;
      }
      this.mConsumeEmpty.setVisibility(8);
      this.mConsumeListLay.setVisibility(0);
      int i = 0;
      while (i < localObject2.length)
      {
        localObject3 = localObject2[i];
        if (localObject3 != null)
        {
          localObject4 = LayoutInflater.from(this).inflate(R.layout.prepaid_history_list_item, this.mConsumeListLay, false);
          localObject5 = (TextView)((View)localObject4).findViewById(R.id.date);
          localObject6 = new Date(((DPObject)localObject3).getTime("TradeTime"));
          ((TextView)localObject5).setText(FORMAT.format((Date)localObject6));
          ((TextView)((View)localObject4).findViewById(R.id.cash)).setText(((DPObject)localObject3).getString("Cash"));
          this.mConsumeListLay.addView((View)localObject4);
        }
        i += 1;
      }
      break label199;
      label453: if (arrayOfDPObject.length == 0)
      {
        this.mRechargeEmpty.setVisibility(0);
        this.mRechargeListLay.setVisibility(8);
        break label211;
      }
      this.mRechargeEmpty.setVisibility(8);
      this.mRechargeListLay.setVisibility(0);
      i = 0;
      while (i < arrayOfDPObject.length)
      {
        localObject2 = arrayOfDPObject[i];
        if (localObject2 != null)
        {
          localObject3 = LayoutInflater.from(this).inflate(R.layout.prepaid_history_list_item, this.mRechargeListLay, false);
          localObject4 = (TextView)((View)localObject3).findViewById(R.id.date);
          localObject5 = new Date(((DPObject)localObject2).getTime("TradeTime"));
          ((TextView)localObject4).setText(FORMAT.format((Date)localObject5));
          ((TextView)((View)localObject3).findViewById(R.id.cash)).setText(((DPObject)localObject2).getString("Cash"));
          this.mRechargeListLay.addView((View)localObject3);
        }
        i += 1;
      }
      break label211;
      label612: if (localObject1.length == 0)
      {
        this.mGiftEmpty.setVisibility(8);
        this.mGiftListLay.setVisibility(8);
        this.mGiftHistoryTitle.setVisibility(8);
        this.mGiftUpLineView.setVisibility(8);
        continue;
      }
      this.mGiftEmpty.setVisibility(8);
      this.mGiftListLay.setVisibility(0);
      this.mGiftUpLineView.setVisibility(0);
      this.mGiftHistoryTitle.setVisibility(0);
      i = 0;
      if (i < localObject1.length)
      {
        arrayOfDPObject = localObject1[i];
        int j;
        if (arrayOfDPObject != null)
        {
          localObject2 = LayoutInflater.from(this).inflate(R.layout.prepaid_gift_list_item, this.mGiftListLay, false);
          j = Color.parseColor(arrayOfDPObject.getString("FontColor"));
          localObject3 = (TextView)((View)localObject2).findViewById(R.id.detail);
          ((TextView)localObject3).setText(arrayOfDPObject.getString("TradeDetail"));
          ((TextView)localObject3).setTextColor(j);
          boolean bool = arrayOfDPObject.getBoolean("Cancelable");
          localObject3 = (TextView)((View)localObject2).findViewById(R.id.status);
          localObject4 = (Button)((View)localObject2).findViewById(R.id.cancel);
          if (!Boolean.valueOf(bool).booleanValue())
            break label860;
          ((Button)localObject4).setTag(arrayOfDPObject);
          ((Button)localObject4).setVisibility(0);
          ((Button)localObject4).setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              MyPrepaidCardConsumeListActivity.this.statisticsEvent("usedetail5", "usedetail5_cancelpresent", "", 0);
              paramView = (DPObject)paramView.getTag();
              AlertDialog.Builder localBuilder = new AlertDialog.Builder(MyPrepaidCardConsumeListActivity.this);
              localBuilder.setTitle("取消赠送");
              localBuilder.setMessage("确认取消赠送后，赠送金额将返回原卡。您的好友将不能再领取此赠送。");
              localBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener(paramView)
              {
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                  MyPrepaidCardConsumeListActivity.this.statisticsEvent("usedetail5", "usedetail5_cancelconfirm", "", 0);
                  MyPrepaidCardConsumeListActivity.this.deleteGiftTrade(this.val$prepaidCardTrade);
                }
              });
              localBuilder.setNegativeButton("放弃", new DialogInterface.OnClickListener()
              {
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                  MyPrepaidCardConsumeListActivity.this.statisticsEvent("usedetail5", "usedetail5_cancelabandon", "", 0);
                  paramDialogInterface.dismiss();
                }
              });
              localBuilder.create().show();
            }
          });
          ((TextView)localObject3).setVisibility(8);
        }
        while (true)
        {
          this.mGiftListLay.addView((View)localObject2);
          i += 1;
          break;
          label860: ((TextView)localObject3).setText(arrayOfDPObject.getString("StatusMsg"));
          ((TextView)localObject3).setTextColor(j);
          ((Button)localObject4).setVisibility(8);
          ((TextView)localObject3).setVisibility(0);
        }
      }
      localObject1 = LayoutInflater.from(this).inflate(R.layout.prepaid_gift_tip_item, this.mGiftListLay, false);
      this.mGiftListLay.addView((View)localObject1);
    }
    label922: this.mAttentionLay.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.MyPrepaidCardConsumeListActivity
 * JD-Core Version:    0.6.0
 */
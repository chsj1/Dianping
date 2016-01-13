package com.dianping.tuan.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.widget.SortListView;
import com.dianping.base.widget.SortListView.SortListAdapter;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class BankListActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  protected static final int CREDIT_CARD = 3;
  protected static final int DEPOSIT_CARD = 6;
  protected TextView creditcardActivityIcon;
  protected TextView creditcardText;
  protected TextView depositcardActivityIcon;
  protected TextView depositcardText;
  protected DPObject dpCurrentPaymentTool;
  protected LinearLayout layerBankcards;
  protected RelativeLayout layerCreditcard;
  protected RelativeLayout layerDepositcard;
  protected MApiRequest listRequest;
  protected SortListView.SortListAdapter mAdapter;
  protected SortListView mBankList;
  protected DPObject[] mCardCategory;
  protected int mCardType;
  protected String orderId;
  protected int productCode = 1;
  protected int productType = -1;

  private void refreshList()
  {
    Object localObject2 = this.mCardCategory;
    int j = localObject2.length;
    int i = 0;
    while (true)
    {
      if (i < j)
      {
        Object localObject1 = localObject2[i];
        if (((DPObject)localObject1).getInt("CardType") == this.mCardType)
        {
          DPObject[] arrayOfDPObject = ((DPObject)localObject1).getArray("BankList");
          ArrayList localArrayList2 = new ArrayList();
          ArrayList localArrayList1 = new ArrayList();
          localObject1 = null;
          int k = 0;
          if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
          {
            int m = arrayOfDPObject.length;
            j = 0;
            i = k;
            if (j < m)
            {
              String str = arrayOfDPObject[j].getString("RankLetter");
              if (!str.equals(localObject1))
              {
                if (localObject1 != null)
                {
                  localObject2 = localObject1;
                  if ("*".equals(localObject1))
                    localObject2 = "常用";
                  localArrayList2.add(localObject2);
                  localArrayList1.add(Integer.valueOf(i));
                }
                localObject1 = str;
                i = 1;
              }
              while (true)
              {
                j += 1;
                break;
                i += 1;
              }
            }
            localArrayList2.add(localObject1);
            localArrayList1.add(Integer.valueOf(i));
            localObject1 = new String[localArrayList2.size()];
            localArrayList2.toArray(localObject1);
            localObject2 = new int[localArrayList1.size()];
            i = 0;
            while (i < localArrayList1.size())
            {
              localObject2[i] = ((Integer)localArrayList1.get(i)).intValue();
              i += 1;
            }
            this.mAdapter = new BankListAdapter(arrayOfDPObject, localObject1, localObject2);
            this.mBankList.setAdapter(this.mAdapter);
          }
        }
      }
      else
      {
        return;
      }
      i += 1;
    }
  }

  private void requestBankList()
  {
    if (this.listRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://api.p.dianping.com/");
    localUrlBuilder.appendPath("getproposebankcashier.pay");
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("orderid", this.orderId);
    localUrlBuilder.addParam("producttype", Integer.valueOf(this.productType));
    localUrlBuilder.addParam("productcode", Integer.valueOf(this.productCode));
    localUrlBuilder.addParam("preposebankcashierid", Integer.valueOf(this.dpCurrentPaymentTool.getInt("PreposeBankCashierID")));
    this.listRequest = new BasicMApiRequest(localUrlBuilder.buildUrl(), "GET", null, CacheType.CRITICAL, false, null);
    mapiService().exec(this.listRequest, this);
    findViewById(R.id.loading).setVisibility(0);
  }

  public void finish()
  {
    super.finish();
    overridePendingTransition(R.anim.fade_light_in, R.anim.slide_out_to_bottom);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.bank_list);
    this.orderId = getIntent().getStringExtra("orderid");
    this.productType = getIntent().getIntExtra("producttype", -1);
    this.productCode = getIntent().getIntExtra("productcode", 0);
    this.dpCurrentPaymentTool = ((DPObject)getIntent().getParcelableExtra("paymenttool"));
    if (((TextUtils.isEmpty(this.orderId)) || (this.dpCurrentPaymentTool == null)) && (this.productType <= 0) && (this.productCode <= 0))
    {
      finish();
      return;
    }
    paramBundle = new TextView(this);
    paramBundle.setText("取消");
    paramBundle.setGravity(17);
    paramBundle.setPadding((int)getResources().getDimension(R.dimen.title_bar_button_margin), 0, 0, 0);
    paramBundle.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
    paramBundle.setTextSize(2, 15.0F);
    paramBundle.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BankListActivity.this.finish();
      }
    });
    super.getTitleBar().setCustomLeftView(paramBundle);
    this.mCardType = 3;
    this.mBankList = ((SortListView)findViewById(R.id.sort_bank_list));
    this.mBankList.setOnItemClickListener(this);
    this.mBankList.setVisibility(4);
    this.layerBankcards = ((LinearLayout)findViewById(R.id.layer_bankcards));
    this.layerBankcards.setVisibility(4);
    this.layerCreditcard = ((RelativeLayout)findViewById(R.id.layer_creditcard));
    this.creditcardText = ((TextView)findViewById(R.id.creditcard_text));
    this.creditcardActivityIcon = ((TextView)findViewById(R.id.creditcard_activityicon));
    this.layerDepositcard = ((RelativeLayout)findViewById(R.id.layer_depositcard));
    this.depositcardText = ((TextView)findViewById(R.id.depositcard_text));
    this.depositcardActivityIcon = ((TextView)findViewById(R.id.depositcard_activityicon));
    this.layerCreditcard.setClickable(true);
    this.layerCreditcard.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (BankListActivity.this.mCardType == 3)
          return;
        BankListActivity.this.mCardType = 3;
        BankListActivity.this.layerCreditcard.setSelected(true);
        BankListActivity.this.layerDepositcard.setSelected(false);
        BankListActivity.this.refreshList();
      }
    });
    this.layerDepositcard.setClickable(true);
    this.layerDepositcard.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (BankListActivity.this.mCardType == 6)
          return;
        BankListActivity.this.mCardType = 6;
        BankListActivity.this.layerCreditcard.setSelected(false);
        BankListActivity.this.layerDepositcard.setSelected(true);
        BankListActivity.this.refreshList();
      }
    });
    requestBankList();
  }

  protected void onDestroy()
  {
    if (this.listRequest != null)
    {
      mapiService().abort(this.listRequest, this, true);
      this.listRequest = null;
    }
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = (DPObject)this.mAdapter.getItem(paramInt);
    paramView = new Intent();
    paramView.putExtra("bankelement", paramAdapterView);
    setResult(-1, paramView);
    finish();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.listRequest)
    {
      this.listRequest = null;
      Toast.makeText(this, paramMApiResponse.message().content(), 0).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.listRequest)
    {
      this.listRequest = null;
      dismissDialog();
      this.mBankList.setVisibility(0);
      this.layerBankcards.setVisibility(0);
      if (this.mCardType == 3)
      {
        this.layerCreditcard.setSelected(true);
        this.layerDepositcard.setSelected(false);
      }
      try
      {
        this.mCardCategory = ((DPObject[])(DPObject[])paramMApiResponse.result());
        paramMApiRequest = this.mCardCategory;
        int j = paramMApiRequest.length;
        int i = 0;
        label85: if (i < j)
        {
          paramMApiResponse = paramMApiRequest[i];
          String str = paramMApiResponse.getString("CardTip");
          if (3 == paramMApiResponse.getInt("CardType"))
          {
            this.creditcardText.setText(paramMApiResponse.getString("CardName"));
            if (!TextUtils.isEmpty(str))
            {
              this.creditcardActivityIcon.setText(str);
              this.creditcardActivityIcon.setVisibility(0);
            }
          }
          while (true)
          {
            i += 1;
            break label85;
            this.layerCreditcard.setSelected(false);
            this.layerDepositcard.setSelected(true);
            break;
            if (6 != paramMApiResponse.getInt("CardType"))
              continue;
            this.depositcardText.setText(paramMApiResponse.getString("CardName"));
            if (TextUtils.isEmpty(str))
              continue;
            this.depositcardActivityIcon.setText(str);
            this.depositcardActivityIcon.setVisibility(0);
          }
        }
      }
      catch (Exception paramMApiRequest)
      {
        paramMApiRequest.printStackTrace();
        refreshList();
        findViewById(R.id.loading).setVisibility(8);
      }
    }
  }

  class BankListAdapter extends SortListView.SortListAdapter
  {
    private DPObject[] dpBankElments;

    public BankListAdapter(DPObject[] paramArrayOfString, String[] paramArrayOfInt, int[] arg4)
    {
      super(arrayOfInt);
      this.dpBankElments = paramArrayOfString;
    }

    public int getCount()
    {
      return this.dpBankElments.length;
    }

    public Object getItem(int paramInt)
    {
      return this.dpBankElments[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return this.dpBankElments[paramInt].hashCode();
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      paramView = paramViewGroup;
      if (paramViewGroup == null)
        paramView = View.inflate(BankListActivity.this, R.layout.bank_list_item, null);
      Object localObject1 = (DPObject)getItem(paramInt);
      Object localObject2 = (TextView)paramView.findViewById(R.id.text1);
      paramViewGroup = (TextView)paramView.findViewById(R.id.text2);
      ((TextView)localObject2).setText(((DPObject)localObject1).getString("BankName"));
      localObject2 = (DPNetworkImageView)paramView.findViewById(R.id.icon);
      String str = ((DPObject)localObject1).getString("IconUrl");
      if (TextUtils.isEmpty(str))
      {
        ((DPNetworkImageView)localObject2).setImageBitmap(null);
        ((DPNetworkImageView)localObject2).setVisibility(8);
      }
      while (true)
      {
        localObject1 = ((DPObject)localObject1).getString("Tip");
        if (!TextUtils.isEmpty((CharSequence)localObject1))
          break;
        paramViewGroup.setVisibility(8);
        return paramView;
        ((DPNetworkImageView)localObject2).setImage(str);
        ((DPNetworkImageView)localObject2).setVisibility(0);
      }
      paramViewGroup.setVisibility(0);
      paramViewGroup.setText((CharSequence)localObject1);
      return (View)(View)paramView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.BankListActivity
 * JD-Core Version:    0.6.0
 */
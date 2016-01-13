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
import com.dianping.base.tuan.utils.PayUtils;
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

public class BankElementListActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  protected static final int CARD_RANK_SELECTED_DEFAULT = 1;
  protected static final int CREDIT_CARD = 3;
  protected static final int DEPOSIT_CARD = 6;
  protected static final int REQUEST_METHOD_UNKNOWN = -1;
  protected static final int REQUEST_METHOD_WITH_ORDER = 1;
  protected static final int REQUEST_METHOD_WITH_PRODUCT = 2;
  protected int cityId;
  protected TextView creditcardActivityIcon;
  protected TextView creditcardText;
  protected TextView depositcardActivityIcon;
  protected TextView depositcardText;
  protected DPObject dpCurrentPaymentTool;
  protected MApiRequest getProposeBankListRequest;
  protected MApiRequest getProposeBankListWithMultiOrderRequest;
  protected LinearLayout layerBankcards;
  protected RelativeLayout layerCreditcard;
  protected RelativeLayout layerDepositcard;
  protected SortListView.SortListAdapter mAdapter;
  protected SortListView mBankList;
  protected DPObject[] mCardCategory;
  protected int mCardType;
  protected int mRequestMethod = -1;
  protected int mainProductCode;
  protected String[] orderIdList;
  protected String[] productCodeList;
  protected String[] productIdList;
  protected String token;

  private void refreshBankList()
  {
    Object localObject2;
    int j;
    int i;
    if (this.mCardCategory != null)
    {
      localObject2 = this.mCardCategory;
      j = localObject2.length;
      i = 0;
    }
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
            localArrayList2.add("");
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

  private void requestProposeBankList()
  {
    if (this.mRequestMethod == -1)
      return;
    if (this.mRequestMethod == 1)
      requestProposeBankListWithOrder();
    while (true)
    {
      findViewById(R.id.loading).setVisibility(0);
      return;
      if (this.mRequestMethod != 2)
        continue;
      requestProposeBankListWithProduct();
    }
  }

  private void requestProposeBankListWithOrder()
  {
    if (this.getProposeBankListWithMultiOrderRequest != null);
    UrlBuilder localUrlBuilder;
    String str;
    do
    {
      return;
      localUrlBuilder = UrlBuilder.createBuilder("http://api.p.dianping.com/");
      str = PayUtils.generatePrepayInfos(this.orderIdList, this.productCodeList);
    }
    while ((str == null) || (TextUtils.isEmpty(this.token)));
    localUrlBuilder.appendPath("getproposebanklistwithmultiorder.pay");
    localUrlBuilder.addParam("prepayorders", str);
    localUrlBuilder.addParam("token", this.token);
    localUrlBuilder.addParam("preposebankcashierid", Integer.valueOf(this.dpCurrentPaymentTool.getInt("PreposeBankCashierID")));
    localUrlBuilder.addParam("mainproductcode", Integer.valueOf(this.mainProductCode));
    this.getProposeBankListWithMultiOrderRequest = new BasicMApiRequest(localUrlBuilder.buildUrl(), "GET", null, CacheType.CRITICAL, false, null);
    mapiService().exec(this.getProposeBankListWithMultiOrderRequest, this);
  }

  private void requestProposeBankListWithProduct()
  {
    if (this.getProposeBankListRequest != null);
    UrlBuilder localUrlBuilder;
    String str;
    do
    {
      return;
      localUrlBuilder = UrlBuilder.createBuilder("http://api.p.dianping.com/");
      str = PayUtils.generatePrepayInfos(this.productIdList, this.productCodeList);
    }
    while ((str == null) || (TextUtils.isEmpty(this.token)));
    localUrlBuilder.appendPath("getproposebanklist.pay");
    localUrlBuilder.addParam("prepayinfos", str);
    localUrlBuilder.addParam("token", this.token);
    localUrlBuilder.addParam("preposebankcashierid", Integer.valueOf(this.dpCurrentPaymentTool.getInt("PreposeBankCashierID")));
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    localUrlBuilder.addParam("mainproductcode", Integer.valueOf(this.mainProductCode));
    this.getProposeBankListRequest = new BasicMApiRequest(localUrlBuilder.buildUrl(), "GET", null, CacheType.CRITICAL, false, null);
    mapiService().exec(this.getProposeBankListRequest, this);
  }

  private void setupView()
  {
    setContentView(R.layout.bank_list);
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
        if (BankElementListActivity.this.mCardType == 3)
          return;
        BankElementListActivity.this.mCardType = 3;
        BankElementListActivity.this.layerCreditcard.setSelected(true);
        BankElementListActivity.this.layerDepositcard.setSelected(false);
        BankElementListActivity.this.refreshBankList();
      }
    });
    this.layerDepositcard.setClickable(true);
    this.layerDepositcard.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (BankElementListActivity.this.mCardType == 6)
          return;
        BankElementListActivity.this.mCardType = 6;
        BankElementListActivity.this.layerCreditcard.setSelected(false);
        BankElementListActivity.this.layerDepositcard.setSelected(true);
        BankElementListActivity.this.refreshBankList();
      }
    });
    TextView localTextView = new TextView(this);
    localTextView.setText("取消");
    localTextView.setGravity(17);
    localTextView.setPadding((int)getResources().getDimension(R.dimen.title_bar_button_margin), 0, 0, 0);
    localTextView.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
    localTextView.setTextSize(2, 15.0F);
    localTextView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BankElementListActivity.this.finish();
      }
    });
    super.getTitleBar().setCustomLeftView(localTextView);
  }

  private void updateView(DPObject paramDPObject)
  {
    if (paramDPObject != null)
    {
      this.mCardCategory = paramDPObject.getArray("CardCategory");
      if (this.mCardCategory != null)
      {
        paramDPObject = this.mCardCategory;
        int j = paramDPObject.length;
        int i = 0;
        if (i < j)
        {
          Object localObject = paramDPObject[i];
          String str = localObject.getString("CardTip");
          if (3 == localObject.getInt("CardType"))
          {
            this.creditcardText.setText(localObject.getString("CardName"));
            if (!TextUtils.isEmpty(str))
            {
              this.creditcardActivityIcon.setText(str);
              this.creditcardActivityIcon.setVisibility(0);
            }
            if (localObject.getInt("Rank") == 1)
              this.mCardType = 3;
          }
          while (true)
          {
            i += 1;
            break;
            if (6 != localObject.getInt("CardType"))
              continue;
            this.depositcardText.setText(localObject.getString("CardName"));
            if (!TextUtils.isEmpty(str))
            {
              this.depositcardActivityIcon.setText(str);
              this.depositcardActivityIcon.setVisibility(0);
            }
            if (localObject.getInt("Rank") != 1)
              continue;
            this.mCardType = 6;
          }
        }
      }
    }
    this.mBankList.setVisibility(0);
    this.layerBankcards.setVisibility(0);
    if (this.mCardType == 3)
    {
      this.layerCreditcard.setSelected(true);
      this.layerDepositcard.setSelected(false);
    }
    while (true)
    {
      refreshBankList();
      return;
      this.layerCreditcard.setSelected(false);
      this.layerDepositcard.setSelected(true);
    }
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
    paramBundle = getIntent();
    this.orderIdList = paramBundle.getStringArrayExtra("orderidlist");
    this.productCodeList = paramBundle.getStringArrayExtra("productcodelist");
    this.productIdList = paramBundle.getStringArrayExtra("productidlist");
    if ((this.orderIdList != null) && (this.productCodeList != null) && (this.orderIdList.length == this.productCodeList.length))
      this.mRequestMethod = 1;
    while (true)
    {
      this.cityId = paramBundle.getIntExtra("cityid", 0);
      this.token = paramBundle.getStringExtra("token");
      this.dpCurrentPaymentTool = ((DPObject)paramBundle.getParcelableExtra("paymenttool"));
      this.mainProductCode = paramBundle.getIntExtra("mainproductcode", 0);
      if (TextUtils.isEmpty(this.token))
      {
        if (!isLogined())
          break;
        this.token = accountService().token();
      }
      else
      {
        if ((this.dpCurrentPaymentTool != null) && (this.dpCurrentPaymentTool.getInt("PreposeBankCashierID") > 0))
          break label221;
        finish();
        return;
        if ((this.productCodeList != null) && (this.productIdList != null) && (this.productCodeList.length == this.productIdList.length))
        {
          this.mRequestMethod = 2;
          continue;
        }
        finish();
        return;
      }
    }
    finish();
    return;
    label221: if (this.mainProductCode == 0)
    {
      finish();
      return;
    }
    setupView();
    this.mCardType = 3;
    requestProposeBankList();
  }

  protected void onDestroy()
  {
    if (this.getProposeBankListWithMultiOrderRequest != null)
    {
      mapiService().abort(this.getProposeBankListWithMultiOrderRequest, this, true);
      this.getProposeBankListWithMultiOrderRequest = null;
    }
    if (this.getProposeBankListRequest != null)
    {
      mapiService().abort(this.getProposeBankListRequest, this, true);
      this.getProposeBankListRequest = null;
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
    if (paramMApiRequest == this.getProposeBankListWithMultiOrderRequest)
    {
      this.getProposeBankListWithMultiOrderRequest = null;
      Toast.makeText(this, paramMApiResponse.message().content(), 0).show();
      findViewById(R.id.loading).setVisibility(8);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest == this.getProposeBankListWithMultiOrderRequest)
    {
      this.getProposeBankListWithMultiOrderRequest = null;
      updateView(paramMApiResponse);
    }
    while (true)
    {
      findViewById(R.id.loading).setVisibility(8);
      return;
      if (paramMApiRequest != this.getProposeBankListRequest)
        continue;
      this.getProposeBankListRequest = null;
      updateView(paramMApiResponse);
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
        paramView = View.inflate(BankElementListActivity.this, R.layout.bank_list_item, null);
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
 * Qualified Name:     com.dianping.tuan.activity.BankElementListActivity
 * JD-Core Version:    0.6.0
 */
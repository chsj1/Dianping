package com.dianping.takeaway.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.takeaway.view.OnDialogOperationListener;
import com.dianping.takeaway.view.TAAlertDialog;
import com.dianping.takeaway.view.TAToastView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaImageView;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TakeawayDeliveryAddressListActivity extends NovaActivity
  implements AdapterView.OnItemClickListener
{
  public int MAX_NUM_OF_ADDRESS_SIZE = 2147483647;
  public final int REQUEST_CODE_MODIFY_ADDRESS = 10;
  public final int RESULT_ADDRESS_NOT_CHANGE = 3;
  public NovaActivity activity = this;
  public List<DPObject> addressList;
  public BroadcastReceiver addressListUpdateReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      TakeawayDeliveryAddressListActivity.this.getAddressListTask();
    }
  };
  private ListView addressListView;
  public Context context = this;
  public DPObject deleteAddress = null;
  public MApiRequest deleteAddressRequest;
  public TextView editButton;
  public MApiRequest getAddressListRequest;
  public boolean isEditting = false;
  public TakeawayDeliveryAddressListAdapter listAdapter;
  public TAToastView loadingView;
  public DPObject originAddress;
  public RequestHandler<MApiRequest, MApiResponse> requestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      TakeawayDeliveryAddressListActivity.this.loadingView.hideToast();
      if (paramMApiRequest == TakeawayDeliveryAddressListActivity.this.getAddressListRequest)
        TakeawayDeliveryAddressListActivity.this.getAddressListRequest = null;
      while (true)
      {
        if ((paramMApiResponse != null) && (paramMApiResponse.message() != null))
          TakeawayDeliveryAddressListActivity.this.showShortToast(paramMApiResponse.message().content());
        return;
        if (paramMApiRequest != TakeawayDeliveryAddressListActivity.this.deleteAddressRequest)
          continue;
        TakeawayDeliveryAddressListActivity.this.deleteAddressRequest = null;
        TakeawayDeliveryAddressListActivity.this.deleteAddress = null;
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      TakeawayDeliveryAddressListActivity.this.loadingView.hideToast();
      if (paramMApiRequest == TakeawayDeliveryAddressListActivity.this.getAddressListRequest)
        TakeawayDeliveryAddressListActivity.this.getAddressListRequest = null;
      while (true)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest != null)
          {
            paramMApiResponse = paramMApiRequest.getArray("MyUserAddress");
            TakeawayDeliveryAddressListActivity.this.MAX_NUM_OF_ADDRESS_SIZE = paramMApiRequest.getInt("MaxNum");
            TakeawayDeliveryAddressListActivity.this.addressList.clear();
            TakeawayDeliveryAddressListActivity.this.addressList.addAll(Arrays.asList(paramMApiResponse));
            TakeawayDeliveryAddressListActivity.this.listAdapter.notifyDataSetChanged();
          }
        }
        return;
        if (paramMApiRequest != TakeawayDeliveryAddressListActivity.this.deleteAddressRequest)
          continue;
        if ((TakeawayDeliveryAddressListActivity.this.originAddress != null) && (TakeawayDeliveryAddressListActivity.this.originAddress.getInt("AddressKey") == TakeawayDeliveryAddressListActivity.this.deleteAddress.getInt("AddressKey")))
          TakeawayDeliveryAddressListActivity.this.originAddress = null;
        TakeawayDeliveryAddressListActivity.this.deleteAddressRequest = null;
        TakeawayDeliveryAddressListActivity.this.deleteAddress = null;
        TakeawayDeliveryAddressListActivity.this.showShortToast("删除成功");
      }
    }
  };
  private String shopId;

  private void initView()
  {
    this.editButton = ((TextView)getLayoutInflater().inflate(R.layout.takeaway_titlebar_right_btn, null));
    this.editButton.setText("编辑");
    super.getTitleBar().addRightViewItem(this.editButton, "", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = TakeawayDeliveryAddressListActivity.this;
        boolean bool;
        if (!TakeawayDeliveryAddressListActivity.this.isEditting)
        {
          bool = true;
          paramView.isEditting = bool;
          if (TakeawayDeliveryAddressListActivity.this.isEditting)
            break label60;
          TakeawayDeliveryAddressListActivity.this.editButton.setText("编辑");
        }
        while (true)
        {
          TakeawayDeliveryAddressListActivity.this.listAdapter.notifyDataSetChanged();
          return;
          bool = false;
          break;
          label60: TakeawayDeliveryAddressListActivity.this.editButton.setText("完成");
          TakeawayDeliveryAddressListActivity.this.statisticsEvent("takeaway6", "takeaway6_changeadd_editclk", "", 0);
        }
      }
    });
    super.getTitleBar().setLeftView(R.drawable.ic_back_u, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayDeliveryAddressListActivity.this.addressIsOk();
      }
    });
    this.addressListView = ((ListView)findViewById(R.id.address_list));
    this.addressListView.setOnItemClickListener(this);
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)LayoutInflater.from(this).inflate(R.layout.takeaway_deliveryaddress_listhead, null);
    localNovaLinearLayout.setGAString("newadd");
    localNovaLinearLayout.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayDeliveryAddressListActivity.this.statisticsEvent("takeaway6", "takeaway6_changeadd_newaddclk", "", 0);
        if (TakeawayDeliveryAddressListActivity.this.addressList.size() >= TakeawayDeliveryAddressListActivity.this.MAX_NUM_OF_ADDRESS_SIZE)
        {
          TakeawayDeliveryAddressListActivity.this.showShortToast("最多保存" + TakeawayDeliveryAddressListActivity.this.MAX_NUM_OF_ADDRESS_SIZE + "个常用送餐地址哟~");
          return;
        }
        TakeawayDeliveryAddressListActivity.this.gotoModifyAddress(null);
      }
    });
    this.addressListView.addHeaderView(localNovaLinearLayout);
    this.listAdapter = new TakeawayDeliveryAddressListAdapter();
    this.addressListView.setAdapter(this.listAdapter);
    this.loadingView = ((TAToastView)findViewById(R.id.loading_view));
  }

  public void addressIsOk()
  {
    Object localObject = (DPObject)getIntent().getParcelableExtra("address");
    if (this.originAddress == null)
      setResult(0, null);
    while (true)
    {
      super.finish();
      return;
      if ((localObject != null) && (this.originAddress.getInt("AddressKey") == ((DPObject)localObject).getInt("AddressKey")) && (this.originAddress.getString("Poi").equals(((DPObject)localObject).getString("Poi"))) && (this.originAddress.getString("Address").equals(((DPObject)localObject).getString("Address"))) && (this.originAddress.getString("Phone").equals(((DPObject)localObject).getString("Phone"))))
      {
        localObject = new Intent();
        ((Intent)localObject).putExtra("address", this.originAddress);
        setResult(3, (Intent)localObject);
        continue;
      }
      localObject = new Intent();
      ((Intent)localObject).putExtra("address", this.originAddress);
      setResult(-1, (Intent)localObject);
    }
  }

  public void deleteAddressTask()
  {
    if ((this.deleteAddressRequest != null) || (this.deleteAddress == null))
      return;
    this.loadingView.showToast("正在删除中...", true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("operation");
    localArrayList.add("3");
    localArrayList.add("useraddresskey");
    localArrayList.add(String.valueOf(this.deleteAddress.getInt("AddressKey")));
    localArrayList.add("shopid");
    localArrayList.add(this.shopId);
    Location localLocation = location();
    if (localLocation != null)
    {
      localArrayList.add("locatecityid");
      localArrayList.add(String.valueOf(localLocation.city().id()));
    }
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(super.cityId()));
    this.deleteAddressRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/changeusercontact.ta", (String[])localArrayList.toArray(new String[0]));
    super.mapiService().exec(this.deleteAddressRequest, this.requestHandler);
  }

  public void getAddressListTask()
  {
    if (this.getAddressListRequest != null)
      return;
    this.loadingView.showToast("加载中...", true);
    this.getAddressListRequest = BasicMApiRequest.mapiGet(Uri.parse("http://waimai.api.dianping.com/getaddresslist.ta").buildUpon().appendQueryParameter("shopid", this.shopId).toString(), CacheType.DISABLED);
    super.mapiService().exec(this.getAddressListRequest, this.requestHandler);
  }

  public String getPageName()
  {
    return "takeawayaddresslist";
  }

  public void gotoModifyAddress(DPObject paramDPObject)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayaddressmodify?shopid=" + this.shopId));
    if (paramDPObject != null)
      localIntent.putExtra("address", paramDPObject);
    super.startActivityForResult(localIntent, 10);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 10) && (paramInt2 == -1))
    {
      this.originAddress = ((DPObject)paramIntent.getParcelableExtra("address"));
      addressIsOk();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.takeaway_activity_deliveryaddresslist);
    super.getWindow().setBackgroundDrawable(null);
    Intent localIntent = super.getIntent();
    if (paramBundle == null)
      this.originAddress = ((DPObject)localIntent.getParcelableExtra("address"));
    for (this.shopId = super.getStringParam("shopid"); ; this.shopId = paramBundle.getString("shopid"))
    {
      this.addressList = new ArrayList();
      initView();
      getAddressListTask();
      paramBundle = new IntentFilter();
      paramBundle.addAction("com.dianping.takeaway.UPDATE_ADDRESS_LIST");
      super.registerReceiver(this.addressListUpdateReceiver, paramBundle);
      return;
      this.originAddress = ((DPObject)paramBundle.getParcelable("address"));
    }
  }

  protected void onDestroy()
  {
    super.unregisterReceiver(this.addressListUpdateReceiver);
    if (this.getAddressListRequest != null)
    {
      mapiService().abort(this.getAddressListRequest, null, true);
      this.getAddressListRequest = null;
    }
    if (this.deleteAddressRequest != null)
    {
      mapiService().abort(this.deleteAddressRequest, null, true);
      this.deleteAddressRequest = null;
    }
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = (DPObject)paramAdapterView.getItemAtPosition(paramInt);
    if (paramAdapterView == null)
      return;
    if (paramAdapterView.getBoolean("NeedUpdate"))
    {
      paramView = new GAUserInfo();
      paramView.shop_id = Integer.valueOf(Integer.parseInt(this.shopId));
      GAHelper.instance().contextStatisticsEvent(this.activity, "update", paramView, "tap");
      gotoModifyAddress(paramAdapterView);
      return;
    }
    statisticsEvent("takeaway6", "takeaway6_changeadd_chooseaddclk", "", 0);
    GAHelper.instance().contextStatisticsEvent(this.activity, "choose", null, "tap");
    this.originAddress = paramAdapterView;
    addressIsOk();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      addressIsOk();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("address", this.originAddress);
    paramBundle.putString("shopid", this.shopId);
  }

  public class TakeawayDeliveryAddressListAdapter extends BaseAdapter
  {
    public TakeawayDeliveryAddressListAdapter()
    {
    }

    public int getCount()
    {
      if (TakeawayDeliveryAddressListActivity.this.addressList.isEmpty())
        TakeawayDeliveryAddressListActivity.this.editButton.setVisibility(8);
      while (true)
      {
        return TakeawayDeliveryAddressListActivity.this.addressList.size();
        TakeawayDeliveryAddressListActivity.this.editButton.setVisibility(0);
      }
    }

    public Object getItem(int paramInt)
    {
      return TakeawayDeliveryAddressListActivity.this.addressList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      int j = 8;
      DPObject localDPObject;
      int i;
      if (paramView == null)
      {
        paramView = LayoutInflater.from(TakeawayDeliveryAddressListActivity.this.context).inflate(R.layout.takeaway_deliveryaddress_item, null);
        paramViewGroup = new TakeawayDeliveryAddressListActivity.ViewHolder();
        paramViewGroup.tipView = paramView.findViewById(R.id.update_tip);
        paramViewGroup.addressView = ((TextView)paramView.findViewById(R.id.address));
        paramViewGroup.phoneView = ((TextView)paramView.findViewById(R.id.phone));
        paramViewGroup.deleteBtn = ((NovaImageView)paramView.findViewById(R.id.delete_btn));
        paramViewGroup.editBtn = ((NovaImageView)paramView.findViewById(R.id.edit_btn));
        paramViewGroup.selectBtn = ((ImageView)paramView.findViewById(R.id.select_btn));
        paramViewGroup.deleteBtn.setGAString("delete");
        paramViewGroup.editBtn.setGAString("edit");
        paramViewGroup.dividerView = paramView.findViewById(R.id.divider_view);
        paramView.setTag(paramViewGroup);
        localDPObject = (DPObject)TakeawayDeliveryAddressListActivity.this.addressList.get(paramInt);
        boolean bool = localDPObject.getBoolean("NeedUpdate");
        Object localObject1 = paramViewGroup.tipView;
        if (!bool)
          break label405;
        i = 0;
        label190: ((View)localObject1).setVisibility(i);
        Object localObject2 = localDPObject.getString("Poi");
        String str = localDPObject.getString("Address");
        TextView localTextView = paramViewGroup.addressView;
        StringBuilder localStringBuilder = new StringBuilder();
        localObject1 = localObject2;
        if (localObject2 == null)
          localObject1 = "";
        localObject2 = localStringBuilder.append((String)localObject1);
        localObject1 = str;
        if (str == null)
          localObject1 = "";
        localTextView.setText((String)localObject1);
        paramViewGroup.phoneView.setText(localDPObject.getString("Phone"));
        if (TakeawayDeliveryAddressListActivity.this.isEditting)
          break label424;
        paramViewGroup.deleteBtn.setVisibility(4);
        paramViewGroup.editBtn.setVisibility(4);
        if ((TakeawayDeliveryAddressListActivity.this.originAddress == null) || (TakeawayDeliveryAddressListActivity.this.originAddress.getInt("AddressKey") != localDPObject.getInt("AddressKey")))
          break label412;
        TakeawayDeliveryAddressListActivity.this.originAddress = localDPObject;
        paramViewGroup.selectBtn.setVisibility(0);
        label369: paramViewGroup = paramViewGroup.dividerView;
        if (paramInt != getCount() - 1)
          break label486;
      }
      label405: label412: label424: label486: for (paramInt = j; ; paramInt = 0)
      {
        paramViewGroup.setVisibility(paramInt);
        return paramView;
        paramViewGroup = (TakeawayDeliveryAddressListActivity.ViewHolder)paramView.getTag();
        break;
        i = 8;
        break label190;
        paramViewGroup.selectBtn.setVisibility(8);
        break label369;
        paramViewGroup.deleteBtn.setVisibility(0);
        paramViewGroup.editBtn.setVisibility(0);
        paramViewGroup.selectBtn.setVisibility(8);
        paramViewGroup.deleteBtn.setOnClickListener(new View.OnClickListener(localDPObject)
        {
          public void onClick(View paramView)
          {
            paramView = new TAAlertDialog(TakeawayDeliveryAddressListActivity.this.context);
            paramView.setCanceledOnTouchOutside(true);
            paramView.setListener(new OnDialogOperationListener()
            {
              public void cancel()
              {
              }

              public void confirm(int paramInt)
              {
                TakeawayDeliveryAddressListActivity.this.deleteAddress = TakeawayDeliveryAddressListActivity.TakeawayDeliveryAddressListAdapter.1.this.val$addressObj;
                TakeawayDeliveryAddressListActivity.this.deleteAddressTask();
              }
            });
            paramView.show();
            TakeawayDeliveryAddressListActivity.this.statisticsEvent("takeaway6", "takeaway6_changeadd_dltaddclk", "", 0);
          }
        });
        paramViewGroup.editBtn.setOnClickListener(new View.OnClickListener(localDPObject)
        {
          public void onClick(View paramView)
          {
            TakeawayDeliveryAddressListActivity.this.gotoModifyAddress(this.val$addressObj);
            TakeawayDeliveryAddressListActivity.this.statisticsEvent("takeaway6", "takeaway6_changeadd_editaddclk", "", 0);
          }
        });
        break label369;
      }
    }
  }

  static class ViewHolder
  {
    public TextView addressView;
    public NovaImageView deleteBtn;
    public View dividerView;
    public NovaImageView editBtn;
    public TextView phoneView;
    public ImageView selectBtn;
    public View tipView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayDeliveryAddressListActivity
 * JD-Core Version:    0.6.0
 */
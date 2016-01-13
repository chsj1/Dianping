package com.dianping.booking.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.Cell;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.base.widget.TitleBar;
import com.dianping.booking.BookingInfoActivity;
import com.dianping.booking.adapter.BookingDialogAdapter;
import com.dianping.booking.agent.AbstractBookingInfoAgent;
import com.dianping.booking.agent.BookingBannerAgent;
import com.dianping.booking.agent.BookingContactAgent;
import com.dianping.booking.agent.BookingInfoAgent;
import com.dianping.booking.agent.BookingSubmitAgent;
import com.dianping.booking.util.OrderSource;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.DeviceUtils;
import com.dianping.util.PermissionCheckHelper;
import com.dianping.util.PermissionCheckHelper.PermissionCallbackListener;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BookingInfoFragment extends AgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public static final int CLOSE_REQUEST_CODE = 200;
  private static final int MSG_REQ = 300;
  private static final int NEED_VALIDATION = 1;
  public static final int READ_CONTACTS_REQUEST_CODE = 500;
  public static final int REQUEST_CONTACT_CODE = 300;
  public static final int SUBMIT_REQUEST_CODE = 100;
  EditText bookerName;
  EditText bookerPhone;
  private DPObject bookingConfig;
  private ScrollView bookingInfoSrollView;
  private DPObject bookingRecord;
  private MApiRequest bookingRequest;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
      case 300:
      }
      do
        return;
      while (BookingInfoFragment.this.bookingInfoSrollView == null);
      BookingInfoFragment.this.bookingInfoSrollView.scrollTo(0, 0);
    }
  };
  String name = "";
  String phone = "";
  ArrayList<String> phoneList = new ArrayList();

  private void book(int paramInt)
  {
    Object localObject2 = ((AbstractBookingInfoAgent)findAgent("booking_info/info")).validateInput();
    if ((localObject2 == null) || (((Map)localObject2).isEmpty()));
    do
    {
      return;
      localObject1 = ((AbstractBookingInfoAgent)findAgent("booking_info/contact")).validateInput();
    }
    while ((localObject1 == null) || (((Map)localObject1).isEmpty()));
    showProgressDialog("正在提交订单，请稍候...");
    boolean bool = ((String)((Map)localObject2).get("prepay")).equals("true");
    String str2 = (String)((Map)localObject2).get("prepayamout");
    ((Map)localObject2).remove("prepay");
    ((Map)localObject2).remove("prepayamout");
    if (bool);
    ArrayList localArrayList;
    for (String str1 = "http://rs.api.dianping.com/prepaybook.yy"; ; str1 = "http://rs.api.dianping.com/book.yy")
    {
      localArrayList = new ArrayList();
      localObject2 = ((Map)localObject2).entrySet().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        Map.Entry localEntry = (Map.Entry)((Iterator)localObject2).next();
        localArrayList.add(localEntry.getKey());
        localArrayList.add(localEntry.getValue());
      }
    }
    Object localObject1 = ((Map)localObject1).entrySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      localArrayList.add(((Map.Entry)localObject2).getKey());
      localArrayList.add(((Map.Entry)localObject2).getValue());
    }
    localArrayList.add("shopid");
    localArrayList.add(String.valueOf(getBookingInfoActivity().shopId));
    localArrayList.add("shopname");
    localArrayList.add(getBookingInfoActivity().shopName);
    if (accountService().token() != null)
    {
      localArrayList.add("token");
      localArrayList.add(accountService().token());
    }
    localArrayList.add("deviceid");
    localArrayList.add(Environment.imei());
    localArrayList.add("clientUUID");
    localArrayList.add(Environment.uuid());
    localArrayList.add("forcebook");
    localArrayList.add(String.valueOf(paramInt));
    if (bool)
    {
      localArrayList.add("prepayamout");
      localArrayList.add(str2);
    }
    if (this.bookingRecord != null)
    {
      localArrayList.add("ismodify");
      localArrayList.add("1");
      localArrayList.add("serializedid");
      localArrayList.add(this.bookingRecord.getString("SerialNumber"));
    }
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("booking"));
    this.bookingRequest = BasicMApiRequest.mapiPost(str1, (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.bookingRequest, this);
  }

  private void callMerchant()
  {
    Object localObject2 = new ArrayList();
    Object localObject1 = getBookingInfoActivity().shop;
    if (localObject1 == null)
      return;
    String[] arrayOfString = ((DPObject)localObject1).getStringArray("PhoneNos");
    if (arrayOfString != null)
    {
      int i = 0;
      while (i < arrayOfString.length)
      {
        ((ArrayList)localObject2).add("拨打电话: " + arrayOfString[i]);
        i += 1;
      }
    }
    localObject2 = new BookingDialogAdapter(getActivity(), (ArrayList)localObject2);
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
    localBuilder.setTitle("联系商户").setAdapter((ListAdapter)localObject2, new DialogInterface.OnClickListener(arrayOfString, (DPObject)localObject1)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if (paramInt < this.val$phoneNos.length)
        {
          BookingInfoFragment.this.statisticsEvent("booking5", "booking5_tel_call", this.val$shop.getInt("ID") + "|" + this.val$shop.getString("Name"), 0);
          TelephoneUtils.dial(BookingInfoFragment.this.getActivity(), this.val$shop, this.val$phoneNos[paramInt]);
        }
      }
    });
    localObject1 = localBuilder.create();
    ((AlertDialog)localObject1).show();
    ((AlertDialog)localObject1).setCanceledOnTouchOutside(true);
  }

  private Bundle createBundle(int paramInt)
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("type", paramInt);
    return localBundle;
  }

  private void onBookingConfigChanged(DPObject paramDPObject1, DPObject paramDPObject2)
  {
    if (paramDPObject1 != null)
    {
      Bundle localBundle = new Bundle();
      localBundle.putInt("type", 1);
      localBundle.putParcelable("config", paramDPObject1);
      Intent localIntent = new Intent();
      localIntent.setAction("com.dianping.booking.BOOKING_INFO_CHANGE");
      localIntent.putExtra("bookinginfo", paramDPObject1);
      getActivity().sendBroadcast(localIntent);
      if (paramDPObject2 != null)
        localBundle.putParcelable("record", paramDPObject2);
      dispatchAgentChanged(null, localBundle);
      setupAllView(paramDPObject1);
    }
  }

  private void setupAllView(DPObject paramDPObject)
  {
    DPObject localDPObject = getBookingInfoActivity().shop;
    if ((paramDPObject != null) && ((paramDPObject.getInt("Flag") & 0x1) != 0) && (localDPObject != null))
      super.getTitleBar().addRightViewItem("团购订座", "groupon", new View.OnClickListener(localDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://grouponbooking?shopid=" + BookingInfoFragment.this.getBookingInfoActivity().shopId + "&shopname=" + BookingInfoFragment.this.getBookingInfoActivity().shopName));
          paramView.putExtra("shop", this.val$shop);
          BookingInfoFragment.this.startActivityForResult(paramView, 200);
          BookingInfoFragment.this.statisticsEvent("booking5", "booking5_groupon", "", 0);
        }
      });
    while (true)
    {
      this.mHandler.sendEmptyMessageDelayed(300, 500L);
      return;
      if ((localDPObject == null) || (localDPObject.getObject("Deals") == null) || (localDPObject.getStringArray("PhoneNos") == null) || (localDPObject.getStringArray("PhoneNos").length == 0))
        continue;
      super.getTitleBar().addRightViewItem("团购订座", "groupon", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          BookingInfoFragment.this.callMerchant();
        }
      });
    }
  }

  protected void addCellToContainerView(String paramString, Cell paramCell)
  {
    if (paramString.equalsIgnoreCase("booking_info/submit"))
    {
      ((ViewGroup)agentContainerView().findViewById(R.id.submit_booking_area)).addView(paramCell.view);
      return;
    }
    ((ViewGroup)agentContainerView().findViewById(R.id.agent_container_layout)).addView(paramCell.view);
  }

  public void dispatchCellChanged(CellAgent paramCellAgent, Bundle paramBundle)
  {
    super.dispatchCellChanged(paramCellAgent, paramBundle);
    if ((paramBundle != null) && (paramBundle.getInt("type") == 7))
    {
      if (isModifyRecord())
        new AlertDialog.Builder(getActivity()).setTitle("修改订单").setMessage("修改后，原先的订座将不会保留哦").setPositiveButton("确定修改", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            BookingInfoFragment.this.statisticsEvent("mybooking6", "mybooking6_orderssucceed_modify_submit", "", 0);
            BookingInfoFragment.this.book(0);
          }
        }).setNegativeButton("还是不改了", null).show();
    }
    else
      return;
    book(0);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new AgentListConfig()
    {
      public Map<String, AgentInfo> getAgentInfoList()
      {
        return null;
      }

      public Map<String, Class<? extends CellAgent>> getAgentList()
      {
        HashMap localHashMap = new HashMap();
        localHashMap.put("booking_info/banner", BookingBannerAgent.class);
        localHashMap.put("booking_info/info", BookingInfoAgent.class);
        localHashMap.put("booking_info/contact", BookingContactAgent.class);
        localHashMap.put("booking_info/submit", BookingSubmitAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public BookingInfoActivity getBookingInfoActivity()
  {
    return (BookingInfoActivity)getActivity();
  }

  protected Contact getContact(String paramString)
  {
    String str = null;
    Cursor localCursor2 = null;
    ArrayList localArrayList = new ArrayList();
    Contact localContact = new Contact("", localArrayList);
    Cursor localCursor1 = localCursor2;
    Object localObject = str;
    try
    {
      Uri localUri = ContactsContract.Contacts.CONTENT_URI;
      localCursor1 = localCursor2;
      localObject = str;
      localCursor2 = super.getActivity().getContentResolver().query(localUri, null, "_id=?", new String[] { paramString }, null);
      localCursor1 = localCursor2;
      localObject = localCursor2;
      if (localCursor2.moveToFirst())
      {
        localCursor1 = localCursor2;
        localObject = localCursor2;
        paramString = super.getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id=" + paramString, null, null);
        while (true)
        {
          localCursor1 = localCursor2;
          localObject = localCursor2;
          if (!paramString.moveToNext())
            break;
          localCursor1 = localCursor2;
          localObject = localCursor2;
          str = paramString.getString(paramString.getColumnIndex("data1"));
          localCursor1 = localCursor2;
          localObject = localCursor2;
          localContact.name = paramString.getString(paramString.getColumnIndex("display_name"));
          localCursor1 = localCursor2;
          localObject = localCursor2;
          if (TextUtils.isEmpty(str))
            continue;
          localCursor1 = localCursor2;
          localObject = localCursor2;
          localArrayList.add(str);
        }
      }
    }
    catch (Exception paramString)
    {
      localObject = localCursor1;
      paramString.printStackTrace();
      if (localCursor1 != null)
        localCursor1.close();
      do
      {
        return localContact;
        localCursor1 = localCursor2;
        localObject = localCursor2;
        paramString.close();
      }
      while (localCursor2 == null);
      localCursor2.close();
      return localContact;
    }
    finally
    {
      if (localObject != null)
        ((Cursor)localObject).close();
    }
    throw paramString;
  }

  public boolean isModifyRecord()
  {
    return this.bookingRecord != null;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (isModifyRecord())
    {
      super.getTitleBar().setTitle("修改订单");
      paramBundle = (Button)getActivity().findViewById(R.id.submit_booking);
      if (paramBundle != null)
        paramBundle.setText("确认修改");
    }
    onBookingConfigChanged(this.bookingConfig, this.bookingRecord);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == 100)
      if (paramInt2 == -1)
        book(0);
    do
      while (true)
      {
        return;
        if (paramInt1 != 200)
          break;
        if (paramInt2 != -1)
          continue;
        getActivity().finish();
        return;
      }
    while ((paramInt2 != -1) || (paramInt1 != 300));
    this.bookerName = ((EditText)super.getActivity().findViewById(R.id.booker_name));
    this.bookerPhone = ((EditText)super.getActivity().findViewById(R.id.booker_phone));
    Object localObject = paramIntent.getData().getLastPathSegment();
    if (PermissionCheckHelper.isPermissionGranted(getActivity(), "android.permission.READ_CONTACTS"))
    {
      onContactReturned(getContact((String)localObject));
      return;
    }
    paramIntent = PermissionCheckHelper.instance();
    FragmentActivity localFragmentActivity = getActivity();
    String str = getResources().getString(R.string.booking_rationale_contacts);
    localObject = new PermissionCheckHelper.PermissionCallbackListener((String)localObject)
    {
      public void onPermissionCheckCallback(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt)
      {
        if ((paramInt == 500) && (paramArrayOfInt != null) && (paramArrayOfInt.length > 0))
        {
          if (paramArrayOfInt[0] == 0)
            BookingInfoFragment.this.onContactReturned(BookingInfoFragment.this.getContact(this.val$contactId));
        }
        else
          return;
        BookingInfoFragment.this.showToast("无法访问通讯录");
      }
    };
    paramIntent.requestPermissions(localFragmentActivity, 500, new String[] { "android.permission.READ_CONTACTS" }, new String[] { str }, (PermissionCheckHelper.PermissionCallbackListener)localObject);
  }

  public void onBookingConfigChanged(DPObject paramDPObject)
  {
    onBookingConfigChanged(paramDPObject, this.bookingRecord);
  }

  protected void onContactReturned(Contact paramContact)
  {
    this.name = paramContact.name;
    this.phoneList = paramContact.number;
    if (this.phoneList.size() == 1)
    {
      this.phone = ((String)this.phoneList.get(0));
      setBookerInfo(this.name, this.phone);
      return;
    }
    if (this.phoneList.size() > 1)
    {
      paramContact = this.phoneList.iterator();
      while (paramContact.hasNext())
      {
        localObject = (String)paramContact.next();
        if ((TextUtils.isEmpty((CharSequence)localObject)) || (!((String)localObject).equals(this.name)))
          continue;
        this.name = this.bookerName.getText().toString();
      }
      paramContact = new BookingDialogAdapter(super.getActivity(), this.phoneList);
      Object localObject = new AlertDialog.Builder(super.getActivity());
      ((AlertDialog.Builder)localObject).setTitle("请选择手机号").setAdapter(paramContact, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          BookingInfoFragment.this.phone = ((String)BookingInfoFragment.this.phoneList.get(paramInt));
          BookingInfoFragment.this.setBookerInfo(BookingInfoFragment.this.name, BookingInfoFragment.this.phone);
        }
      });
      paramContact = ((AlertDialog.Builder)localObject).create();
      paramContact.setCanceledOnTouchOutside(true);
      paramContact.show();
      return;
    }
    this.phone = "";
    setBookerInfo(this.name, this.phone);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setHost("booking_info");
    paramBundle = getActivity().getIntent();
    this.bookingConfig = ((DPObject)paramBundle.getExtras().getParcelable("config"));
    this.bookingRecord = ((DPObject)paramBundle.getExtras().getParcelable("bookingRecord"));
    getActivity().getWindow().setSoftInputMode(18);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = (ViewGroup)paramLayoutInflater.inflate(R.layout.online_booking_info_scaffold, paramViewGroup, false);
    setAgentContainerView(paramLayoutInflater);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.bookingRequest != null)
    {
      mapiService().abort(this.bookingRequest, this, true);
      this.bookingRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.bookingRequest)
    {
      dismissDialog();
      if ((paramMApiResponse == null) || (!(paramMApiResponse.error() instanceof SimpleMsg)))
        break label83;
      paramMApiRequest = ((SimpleMsg)paramMApiResponse.error()).content();
      if (!TextUtils.isEmpty(paramMApiRequest))
        new AlertDialog.Builder(getActivity()).setMessage(paramMApiRequest).setPositiveButton("知道了", null).show().setCanceledOnTouchOutside(true);
    }
    while (true)
    {
      this.bookingRequest = null;
      return;
      label83: Toast.makeText(getActivity(), "网络不给力，再试试吧", 0).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.bookingRequest)
    {
      this.bookingRequest = null;
      dismissDialog();
      paramMApiRequest = null;
      if (paramMApiResponse == null)
        paramMApiRequest = "商户预订信息已更新，请重新输入";
      if (paramMApiResponse.statusCode() != 200)
      {
        if (!(paramMApiResponse.result() instanceof DPObject))
          break label98;
        paramMApiRequest = ((DPObject)paramMApiResponse.result()).getString("Content");
      }
      if (paramMApiRequest == null)
        break label105;
      new AlertDialog.Builder(getActivity()).setMessage(paramMApiRequest).setPositiveButton("确定", null).show();
    }
    label98: label105: 
    do
    {
      return;
      paramMApiRequest = "商户预订信息已更新，请重新输入";
      break;
    }
    while (!(paramMApiResponse.result() instanceof DPObject));
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest.getInt("ValidationStatus") == 1)
    {
      dispatchAgentChanged("booking_info/contact", createBundle(6));
      return;
    }
    int i = paramMApiRequest.getInt("IsSuccess");
    statisticsEvent("booking5", "booking5_submit", OrderSource.getEventLabelByFromeType(getBookingInfoActivity().ordersource), 0);
    paramMApiResponse = paramMApiRequest.getString("Message");
    Object localObject;
    switch (i / 10)
    {
    case 5:
    case 6:
    case 7:
    case 8:
    default:
      paramMApiResponse = new Bundle();
      paramMApiResponse.putParcelable("result", paramMApiRequest);
      paramMApiResponse.putInt("shopId", getBookingInfoActivity().shopId);
      localObject = new Intent();
      ((Intent)localObject).putExtra("recordId", paramMApiRequest.getObject("Record").getInt("ID"));
      getActivity().setResult(-1, (Intent)localObject);
      if (paramMApiRequest.getObject("Record").getObject("PrepayInfo") == null)
        break;
      getBookingInfoActivity().gotoBookingPrepay(paramMApiResponse);
    case 2:
    case 4:
    case 3:
    case 9:
    }
    while (isModifyRecord())
    {
      paramMApiResponse = new Intent("com.dianping.booking.BOOKING_DETAIL_REFRESH");
      localObject = new Bundle();
      ((Bundle)localObject).putInt("replaceId", this.bookingRecord.getInt("ID"));
      ((Bundle)localObject).putParcelable("bookingRecord", paramMApiRequest.getObject("Record"));
      paramMApiResponse.putExtras((Bundle)localObject);
      getActivity().sendBroadcast(paramMApiResponse);
      return;
      new AlertDialog.Builder(getActivity()).setMessage(paramMApiResponse).setPositiveButton("确定", null).show();
      return;
      new AlertDialog.Builder(getActivity()).setMessage(paramMApiResponse).setPositiveButton("确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface = new Intent();
          paramDialogInterface.setAction("com.dianping.booking.BOOKING_INFO_CHANGE");
          BookingInfoFragment.this.getActivity().sendBroadcast(paramDialogInterface);
          ((BookingInfoActivity)BookingInfoFragment.this.getActivity()).getBookingConfigTask();
        }
      }).show();
      return;
      new AlertDialog.Builder(getActivity()).setMessage(paramMApiRequest.getString("Message")).setPositiveButton("是", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          BookingInfoFragment.this.book(1);
        }
      }).setNegativeButton("否", null).show();
      return;
      switch (i % 10)
      {
      default:
        return;
      case 0:
        new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage(paramMApiResponse).setPositiveButton("知道了", null).show();
        statisticsEvent("booking5", "booking5_grouponuser", "", 0);
        return;
      case 1:
      case 2:
      case 3:
        new AlertDialog.Builder(getActivity()).setMessage(paramMApiResponse).setPositiveButton("确定", null).show();
        return;
      case 4:
      }
      new AlertDialog.Builder(getActivity()).setMessage(paramMApiResponse).setPositiveButton("确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface = new Intent();
          paramDialogInterface.setAction("com.dianping.booking.BOOKING_INFO_CHANGE");
          BookingInfoFragment.this.getActivity().sendBroadcast(paramDialogInterface);
          ((BookingInfoActivity)BookingInfoFragment.this.getActivity()).getBookingConfigTask();
        }
      }).show();
      return;
      getBookingInfoActivity().gotoBookingResult(paramMApiResponse);
    }
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.bookingInfoSrollView = ((ScrollView)paramView.findViewById(R.id.booking_info));
  }

  protected void resetAgentContainerView()
  {
    ((ViewGroup)agentContainerView().findViewById(R.id.agent_container_layout)).removeAllViews();
    ((ViewGroup)agentContainerView().findViewById(R.id.submit_booking_area)).removeAllViews();
  }

  void setBookerInfo(String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(this.bookerName.getText().toString()))
    {
      if ((!TextUtils.isEmpty(paramString2)) && (paramString2.equals(paramString1)))
      {
        this.bookerPhone.setText(paramString2);
        return;
      }
      this.bookerName.setText(paramString1);
      this.bookerPhone.setText(paramString2);
      return;
    }
    if (TextUtils.isEmpty(this.bookerPhone.getText().toString()))
    {
      this.bookerPhone.setText(paramString2);
      return;
    }
    if ((!TextUtils.isEmpty(paramString2)) && (paramString2.equals(paramString1)))
    {
      this.bookerPhone.setText(paramString2);
      return;
    }
    this.bookerName.setText(paramString1);
    this.bookerPhone.setText(paramString2);
  }

  class Contact
  {
    String name;
    ArrayList<String> number;

    Contact(ArrayList<String> arg2)
    {
      Object localObject1;
      this.name = localObject1;
      Object localObject2;
      this.number = localObject2;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.fragment.BookingInfoFragment
 * JD-Core Version:    0.6.0
 */
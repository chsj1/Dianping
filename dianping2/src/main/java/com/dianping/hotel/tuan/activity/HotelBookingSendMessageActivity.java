package com.dianping.hotel.tuan.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.RightTitleButton;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HotelBookingSendMessageActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private final int REQUEST_CONTACT = 1;
  private final DateFormat VALID_DATE = new SimpleDateFormat("yyyy-MM-dd E");
  private TextView dateView;
  private DPObject dpDeal;
  private DPObject dpReservation;
  boolean enableSend = true;
  private ImageButton imageButton;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 0:
      }
      HotelBookingSendMessageActivity.this.enableSend = true;
      HotelBookingSendMessageActivity.this.mHandler.sendEmptyMessageDelayed(0, 60000L);
    }
  };
  private TextView nameView;
  private int orderId;
  private TextView peopleNumView;
  private EditText phoneView;
  private MApiRequest sendMsgRequest;
  private TextView titleView;

  private String getPhoneContacts(String paramString)
  {
    Cursor localCursor = null;
    Object localObject3 = null;
    String str2 = "";
    Object localObject1 = localObject3;
    String str1 = str2;
    Object localObject2 = localCursor;
    try
    {
      Uri localUri = ContactsContract.Contacts.CONTENT_URI;
      localObject1 = localObject3;
      str1 = str2;
      localObject2 = localCursor;
      localCursor = getContentResolver().query(localUri, null, "_id=?", new String[] { paramString }, null);
      localObject3 = str2;
      localObject1 = localCursor;
      str1 = str2;
      localObject2 = localCursor;
      if (localCursor.moveToFirst())
      {
        localObject3 = str2;
        localObject1 = localCursor;
        str1 = str2;
        localObject2 = localCursor;
        if (Integer.parseInt(localCursor.getString(localCursor.getColumnIndex("has_phone_number"))) > 0)
        {
          localObject1 = localCursor;
          str1 = str2;
          localObject2 = localCursor;
          localObject3 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id=" + paramString, null, null);
          paramString = str2;
          localObject1 = localCursor;
          str1 = str2;
          localObject2 = localCursor;
          if (((Cursor)localObject3).moveToNext())
          {
            localObject1 = localCursor;
            str1 = str2;
            localObject2 = localCursor;
            paramString = ((Cursor)localObject3).getString(((Cursor)localObject3).getColumnIndex("data1"));
          }
          localObject1 = localCursor;
          str1 = paramString;
          localObject2 = localCursor;
          ((Cursor)localObject3).close();
          localObject3 = paramString;
        }
      }
      paramString = (String)localObject3;
      if (localCursor != null)
      {
        localCursor.close();
        paramString = (String)localObject3;
      }
      return paramString;
    }
    catch (Exception paramString)
    {
      localObject2 = localObject1;
      paramString.printStackTrace();
      paramString = str1;
      return str1;
    }
    finally
    {
      if (localObject2 != null)
        ((Cursor)localObject2).close();
    }
    throw paramString;
  }

  private void hideKeyBoard()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
    if (this.phoneView != null)
      localInputMethodManager.hideSoftInputFromWindow(this.phoneView.getWindowToken(), 0);
  }

  private void sendMessage()
  {
    if (this.sendMsgRequest != null)
      return;
    this.sendMsgRequest = BasicMApiRequest.mapiGet("http://app.t.dianping.com/sendreservationsmsgn.bin?token=" + accountService().token() + "&orderid=" + this.orderId + "&phonenumber=" + this.phoneView.getText().toString().trim(), CacheType.DISABLED);
    mapiService().exec(this.sendMsgRequest, this);
    showProgressDialog("请稍候...");
    this.enableSend = false;
  }

  private void setupTitle()
  {
    RightTitleButton localRightTitleButton = new RightTitleButton(this);
    localRightTitleButton.setText("发送");
    getTitleBar().addRightViewItem(localRightTitleButton, "send", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (HotelBookingSendMessageActivity.this.validatePhone(HotelBookingSendMessageActivity.this.phoneView))
        {
          HotelBookingSendMessageActivity.this.hideKeyBoard();
          if (HotelBookingSendMessageActivity.this.enableSend)
            HotelBookingSendMessageActivity.this.sendMessage();
        }
        else
        {
          return;
        }
        Toast.makeText(HotelBookingSendMessageActivity.this, "一分钟内只能发送一次", 0).show();
      }
    });
  }

  private void setupView()
  {
    this.titleView = ((TextView)findViewById(R.id.title));
    this.dateView = ((TextView)findViewById(R.id.day));
    this.peopleNumView = ((TextView)findViewById(R.id.people_num));
    this.nameView = ((TextView)findViewById(R.id.people_name));
    this.phoneView = ((EditText)findViewById(R.id.phone));
    this.titleView.setText(this.dpDeal.getString("ShortTitle"));
    this.dateView.setText(this.VALID_DATE.format(Long.valueOf(this.dpReservation.getTime("Date"))));
    this.peopleNumView.setText("" + this.dpReservation.getInt("TouristNum"));
    this.nameView.setText(this.dpReservation.getString("Name"));
    this.phoneView.setText(this.dpReservation.getString("PhoneNumber"));
    this.imageButton = ((ImageButton)findViewById(R.id.chose_people));
    this.imageButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent();
        paramView.setAction("android.intent.action.PICK");
        paramView.setData(ContactsContract.Contacts.CONTENT_URI);
        HotelBookingSendMessageActivity.this.startActivityForResult(paramView, 1);
      }
    });
  }

  private boolean validatePhone(EditText paramEditText)
  {
    String str2 = paramEditText.getText().toString().trim();
    String str1 = null;
    if (str2.length() < 11)
      str1 = "手机号码必须为11位";
    while (str1 != null)
    {
      paramEditText.setError(Html.fromHtml("<font color=#ff0000>" + str1 + "</font>"));
      return false;
      if (str2.startsWith("1"))
        continue;
      str1 = "请输入正确的手机号";
    }
    return true;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 != 1) || (paramInt2 != -1) || (paramIntent == null))
      return;
    paramIntent = getPhoneContacts(paramIntent.getData().getLastPathSegment());
    this.phoneView.setText(paramIntent);
    this.phoneView.setSelection(this.phoneView.getText().toString().length());
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.hotel_booking_send_message);
    paramBundle = getIntent().getExtras();
    if (paramBundle != null)
      this.dpDeal = ((DPObject)paramBundle.getParcelable("deal"));
    if (this.dpDeal == null)
    {
      finish();
      return;
    }
    this.orderId = getIntParam("orderid");
    this.dpReservation = this.dpDeal.getObject("Reservation");
    setupTitle();
    setupView();
    this.mHandler.sendEmptyMessage(0);
  }

  protected void onLeftTitleButtonClicked()
  {
    hideKeyBoard();
    super.onLeftTitleButtonClicked();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.sendMsgRequest)
    {
      super.showToast(paramMApiResponse.message().content());
      this.sendMsgRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.sendMsgRequest)
    {
      super.showToast("短信发送成功");
      this.sendMsgRequest = null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.tuan.activity.HotelBookingSendMessageActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.SimpleMsg;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.education.view.EduBookingDialog;
import com.dianping.shopinfo.education.view.EduBookingDialog.OnEduBookingDialogClickListener;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class EducationBookingAgent extends ShopCellAgent
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>, EduBookingDialog.OnEduBookingDialogClickListener
{
  public static final String API_ROOT = "http://mapi.dianping.com/";
  private static final String API_URL = "http://mapi.dianping.com/edu/shopextendedinfo.bin?";
  private static final String CELL_EDUCATION_BOOKING = "0200Basic.02booking";
  private static final String COOPERATE_TYPE_KEY = "CooperateType";
  private static final int COOPERATIVE = 1;
  public static final String EDUCATION_SHOP_INFO_KEY = "EducationShopInfo";
  private static final int REQUEST_AVAILABLE = 1;
  private static final int REQUEST_PENDING = 2;
  private static final String TAG = EducationBookingAgent.class.getSimpleName();
  private static final int UNCOOPERATIVE = 2;
  private MApiRequest bookingRequest;
  private View bookingView;
  private String btnText;
  private DPObject error;
  private MApiRequest request;
  private int sRequestStatus;
  int shopId;
  private DPObject shopInfo;

  public EducationBookingAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createBookingView(String paramString1, String paramString2)
  {
    View localView = this.res.inflate(getContext(), R.layout.shop_info_education_booking_panel, getParentView(), false);
    ((NovaRelativeLayout)localView).setGAString("edu_booking", getGAExtra());
    localView.setOnClickListener(this);
    TextView localTextView1 = (TextView)localView.findViewById(R.id.booking_present_title);
    TextView localTextView2 = (TextView)localView.findViewById(R.id.booking_content);
    localTextView1.setText(paramString1);
    localTextView2.setText(paramString2);
    return localView;
  }

  private void sendBookingRequest(String paramString)
  {
    if (this.bookingRequest != null)
      return;
    if (this.bookingRequest == null)
    {
      StringBuffer localStringBuffer = new StringBuffer("http://mapi.dianping.com/edu/submituserbookinginfo.bin?");
      localStringBuffer.append("shopid=").append(this.shopId);
      localStringBuffer.append("&mobile=").append(paramString);
      this.bookingRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
      getFragment().mapiService().exec(this.bookingRequest, this);
    }
    showProgressDialog("正在提交");
  }

  private void sendRequest()
  {
    if (this.sRequestStatus == 2)
      return;
    this.sRequestStatus = 2;
    StringBuffer localStringBuffer = new StringBuffer("http://mapi.dianping.com/edu/shopextendedinfo.bin?");
    localStringBuffer.append("shopid=").append(this.shopId);
    this.request = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public View getView()
  {
    return this.bookingView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.shopInfo == null)
      if (this.error == null);
    do
    {
      do
      {
        do
        {
          return;
          paramBundle = getShop();
        }
        while (paramBundle == null);
        this.shopId = paramBundle.getInt("ID");
      }
      while (this.shopId <= 0);
      sendRequest();
      return;
    }
    while (this.shopInfo.getInt("CooperateType") != 1);
    try
    {
      paramBundle = this.shopInfo.getArray("PromoInfoList");
      if ((paramBundle != null) && (paramBundle.length >= 0))
      {
        String str = paramBundle[0];
        paramBundle = str.getString("Title");
        str = str.getString("Content");
        if ((!TextUtils.isEmpty(paramBundle)) && (!TextUtils.isEmpty(str)))
        {
          this.bookingView = createBookingView(paramBundle, str);
          addCell("0200Basic.02booking", this.bookingView);
        }
      }
      this.btnText = this.shopInfo.getString("BookingBtnText");
      return;
    }
    catch (java.lang.Exception paramBundle)
    {
    }
  }

  public void onClick(View paramView)
  {
    paramView = null;
    if (0 == 0)
    {
      paramView = new EduBookingDialog(getContext());
      paramView.setOnEduBookingDialogClickListener(this);
    }
    if (this.shopInfo != null)
    {
      DPObject[] arrayOfDPObject = this.shopInfo.getArray("PromoInfoList");
      paramView.initDialog(this.btnText, arrayOfDPObject, this.shopInfo.getString("UserMobile"));
      paramView.show();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getShop();
    if (paramBundle == null)
    {
      Log.e(TAG, "Null shop data. Can not update shop info.");
      return;
    }
    this.shopId = paramBundle.getInt("ID");
    if (this.shopId <= 0)
    {
      Log.e(TAG, "Invalid shop id. Can not update shop info.");
      return;
    }
    sendRequest();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((this.request != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.request, this, true);
  }

  public void onEduBookingDialogClick(String paramString)
  {
    sendBookingRequest(paramString);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
    {
      this.request = null;
      if ((paramMApiResponse.error() instanceof DPObject))
      {
        this.error = ((DPObject)paramMApiResponse.error());
        this.sRequestStatus = 1;
        dispatchAgentChanged(false);
      }
    }
    do
    {
      return;
      this.error = new DPObject();
      break;
    }
    while (paramMApiRequest != this.bookingRequest);
    this.bookingRequest = null;
    dismissDialog();
    if ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (!TextUtils.isEmpty(paramMApiResponse.message().toString())))
    {
      paramMApiRequest = Toast.makeText(getContext(), paramMApiResponse.message().toString(), 1);
      paramMApiRequest.setGravity(17, 0, 0);
      paramMApiRequest.show();
      return;
    }
    paramMApiRequest = Toast.makeText(getContext(), "网络不给力啊，请稍后再试试", 1);
    paramMApiRequest.setGravity(17, 0, 0);
    paramMApiRequest.show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request == paramMApiRequest)
    {
      this.request = null;
      this.shopInfo = ((DPObject)paramMApiResponse.result());
      this.sRequestStatus = 1;
      if (this.shopInfo != null);
    }
    do
    {
      do
      {
        return;
        setSharedObject("EducationShopInfo", this.shopInfo);
        this.error = null;
        dispatchAgentChanged(false);
        dispatchAgentChanged("shopinfo/edu_top", null);
        dispatchAgentChanged("shopinfo/edu_toolbar", null);
        return;
      }
      while (paramMApiRequest != this.bookingRequest);
      this.bookingRequest = null;
      paramMApiRequest = new ArrayList();
      paramMApiRequest.add(new BasicNameValuePair("shopid", getShop().getInt("ID") + ""));
      statisticsEvent("shopinfoe", "edu_booking_suc", "", 0, paramMApiRequest);
      dismissDialog();
      paramMApiResponse = (DPObject)paramMApiResponse.result();
    }
    while (paramMApiResponse == null);
    paramMApiRequest = paramMApiResponse.getString("BookingCallBackUrl");
    paramMApiResponse = paramMApiResponse.getString("ErrorMsg");
    if (!TextUtils.isEmpty(paramMApiRequest))
    {
      paramMApiRequest = new Intent("android.intent.action.VIEW", Uri.parse(paramMApiRequest));
      getFragment().startActivity(paramMApiRequest);
      return;
    }
    if (!TextUtils.isEmpty(paramMApiResponse))
    {
      paramMApiRequest = Toast.makeText(getContext(), paramMApiResponse, 1);
      paramMApiRequest.setGravity(17, 0, 0);
      paramMApiRequest.show();
      return;
    }
    paramMApiRequest = Toast.makeText(getContext(), "网络不给力啊，请稍后再试试", 1);
    paramMApiRequest.setGravity(17, 0, 0);
    paramMApiRequest.show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.EducationBookingAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.education.agent;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.base.widget.ToolbarButton;
import com.dianping.base.widget.ToolbarImageButton;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.SimpleMsg;
import com.dianping.shopinfo.base.ShopInfoToolbarAgent;
import com.dianping.shopinfo.education.view.EduBookingDialog;
import com.dianping.shopinfo.education.view.EduBookingDialog.OnEduBookingDialogClickListener;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class EducationToolbarAgent extends ShopInfoToolbarAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private MApiRequest bookingRequest;
  private View.OnClickListener listener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = EducationToolbarAgent.this.getShop();
      if (paramView == null)
        return;
      switch (paramView.getInt("Status"))
      {
      case 2:
      case 3:
      default:
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("shop", paramView);
        AddReviewUtil.addReview(EducationToolbarAgent.this.getContext(), paramView.getInt("ID"), paramView.getString("Name"), localBundle);
        return;
      case 1:
      case 4:
      }
      Toast.makeText(EducationToolbarAgent.this.getContext(), "暂停收录点评", 0).show();
    }
  };
  private DPObject shopInfo;

  public EducationToolbarAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void editEduToolBar()
  {
    getToolbarView().removeAllViews();
    String[] arrayOfString = getShop().getStringArray("PhoneNos");
    if ((arrayOfString != null) && (arrayOfString.length > 0))
      initTelephoneButton(arrayOfString);
    while (true)
    {
      initReviewButton();
      initBookingButton();
      return;
      initPhotoButton();
    }
  }

  private void initBookingButton()
  {
    String str = this.shopInfo.getString("BookingBtnText");
    NovaButton localNovaButton = (NovaButton)LayoutInflater.from(getContext()).inflate(R.layout.edu_toolbar_booking_view, getToolbarView(), false);
    if (!com.dianping.util.TextUtils.isEmpty(str))
      localNovaButton.setText(str);
    localNovaButton.setGAString("edu_booking", getGAExtra());
    localNovaButton.setOnClickListener(this);
    getToolbarView().setBackgroundColor(getResources().getColor(R.color.white));
    addToolbarButton(localNovaButton, "7Booking");
  }

  private void initPhotoButton()
  {
    ToolbarButton localToolbarButton = (ToolbarButton)LayoutInflater.from(getContext()).inflate(R.layout.wed_toolbar_button, getToolbarView(), false);
    ((ToolbarImageButton)localToolbarButton.findViewById(16908294)).setImageResource(R.drawable.detail_footerbar_icon_camera_u);
    ((TextView)localToolbarButton.findViewById(16908308)).setText("传图片");
    localToolbarButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        UploadPhotoUtil.uploadShopPhoto(EducationToolbarAgent.this.getContext(), EducationToolbarAgent.this.getShop());
      }
    });
    localToolbarButton.setGAString("toupload", getGAExtra());
    addToolbarButton(localToolbarButton, "6Photo");
  }

  private void initReviewButton()
  {
    ToolbarButton localToolbarButton = (ToolbarButton)LayoutInflater.from(getContext()).inflate(R.layout.wed_toolbar_button, getToolbarView(), false);
    ((ToolbarImageButton)localToolbarButton.findViewById(16908294)).setImageResource(R.drawable.detail_footerbar_icon_comment_u);
    ((TextView)localToolbarButton.findViewById(16908308)).setText("写点评");
    localToolbarButton.setOnClickListener(this.listener);
    localToolbarButton.setGAString("toreview", getGAExtra());
    addToolbarButton(localToolbarButton, "8Review");
    DPObject localDPObject = getShop();
    if (localDPObject != null);
    switch (localDPObject.getInt("Status"))
    {
    case 2:
    case 3:
    default:
      localToolbarButton.setEnabled(true);
      return;
    case 1:
    case 4:
    }
    localToolbarButton.setEnabled(false);
  }

  private void initTelephoneButton(String[] paramArrayOfString)
  {
    Object localObject = new StringBuffer();
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      int i = 0;
      int j = paramArrayOfString.length;
      while (i < j)
      {
        ((StringBuffer)localObject).append(paramArrayOfString[i]);
        if (i != paramArrayOfString.length - 1)
          ((StringBuffer)localObject).append("，");
        i += 1;
      }
    }
    localObject = (ToolbarButton)LayoutInflater.from(getContext()).inflate(R.layout.wed_toolbar_button, getToolbarView(), false);
    ((ToolbarImageButton)((ToolbarButton)localObject).findViewById(16908294)).setImageResource(R.drawable.wed_shopinfo_tel);
    ((TextView)((ToolbarButton)localObject).findViewById(16908308)).setText("咨询");
    ((ToolbarButton)localObject).setOnClickListener(new View.OnClickListener(paramArrayOfString)
    {
      public void onClick(View paramView)
      {
        if ((this.val$phoneNos != null) && (this.val$phoneNos.length > 0))
        {
          if (this.val$phoneNos.length == 1)
            TelephoneUtils.dial(EducationToolbarAgent.this.getContext(), EducationToolbarAgent.this.getShop(), this.val$phoneNos[0]);
        }
        else
          return;
        paramView = new String[this.val$phoneNos.length];
        int i = 0;
        while (i < this.val$phoneNos.length)
        {
          paramView[i] = ("拨打电话: " + this.val$phoneNos[i]);
          i += 1;
        }
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(EducationToolbarAgent.this.getContext());
        localBuilder.setTitle("联系商户").setItems(paramView, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            TelephoneUtils.dial(EducationToolbarAgent.this.getContext(), EducationToolbarAgent.this.getShop(), EducationToolbarAgent.1.this.val$phoneNos[paramInt]);
          }
        });
        localBuilder.create().show();
      }
    });
    ((ToolbarButton)localObject).setGAString("actionbar_tel", getGAExtra());
    addToolbarButton((View)localObject, "4Telephone");
  }

  private void sendBookingRequest(String paramString)
  {
    if (this.bookingRequest != null)
      return;
    if (this.bookingRequest == null)
    {
      StringBuffer localStringBuffer = new StringBuffer("http://mapi.dianping.com/edu/submituserbookinginfo.bin?");
      localStringBuffer.append("shopid=").append(shopId());
      localStringBuffer.append("&mobile=").append(paramString);
      this.bookingRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
      getFragment().mapiService().exec(this.bookingRequest, this);
    }
    showProgressDialog("正在提交");
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.shopInfo = ((DPObject)getSharedObject("EducationShopInfo"));
    if ((this.shopInfo == null) || (getShop() == null));
    do
    {
      do
        return;
      while (this.shopInfo.getInt("CooperateType") != 1);
      paramBundle = this.shopInfo.getArray("PromoInfoList");
    }
    while ((paramBundle == null) || (paramBundle.length < 0));
    editEduToolBar();
  }

  public void onClick(View paramView)
  {
    paramView = null;
    if (0 == 0)
    {
      paramView = new EduBookingDialog(getContext());
      paramView.setOnEduBookingDialogClickListener(new EduBookingDialog.OnEduBookingDialogClickListener()
      {
        public void onEduBookingDialogClick(String paramString)
        {
          EducationToolbarAgent.this.sendBookingRequest(paramString);
        }
      });
    }
    if (this.shopInfo != null)
    {
      paramView.initDialog(this.shopInfo.getString("BookingBtnText"), this.shopInfo.getArray("PromoInfoList"), this.shopInfo.getString("UserMobile"));
      paramView.show();
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    super.onRequestFailed(paramMApiRequest, paramMApiResponse);
    if (paramMApiRequest == this.bookingRequest)
    {
      this.bookingRequest = null;
      dismissDialog();
      if ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (!android.text.TextUtils.isEmpty(paramMApiResponse.message().toString())))
      {
        paramMApiRequest = Toast.makeText(getContext(), paramMApiResponse.message().toString(), 1);
        paramMApiRequest.setGravity(17, 0, 0);
        paramMApiRequest.show();
      }
    }
    else
    {
      return;
    }
    paramMApiRequest = Toast.makeText(getContext(), "网络不给力啊，请稍后再试试", 1);
    paramMApiRequest.setGravity(17, 0, 0);
    paramMApiRequest.show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    super.onRequestFinish(paramMApiRequest, paramMApiResponse);
    if (paramMApiRequest == this.bookingRequest)
    {
      this.bookingRequest = null;
      paramMApiRequest = new ArrayList();
      paramMApiRequest.add(new BasicNameValuePair("shopid", getShop().getInt("ID") + ""));
      statisticsEvent("shopinfoe", "edu_booking_suc", "", 0, paramMApiRequest);
      dismissDialog();
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      if (paramMApiResponse != null);
    }
    else
    {
      return;
    }
    paramMApiRequest = paramMApiResponse.getString("BookingCallBackUrl");
    paramMApiResponse = paramMApiResponse.getString("ErrorMsg");
    if (!android.text.TextUtils.isEmpty(paramMApiRequest))
    {
      paramMApiRequest = new Intent("android.intent.action.VIEW", Uri.parse(paramMApiRequest));
      getFragment().startActivity(paramMApiRequest);
      return;
    }
    if (!android.text.TextUtils.isEmpty(paramMApiResponse))
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
 * Qualified Name:     com.dianping.shopinfo.education.agent.EducationToolbarAgent
 * JD-Core Version:    0.6.0
 */
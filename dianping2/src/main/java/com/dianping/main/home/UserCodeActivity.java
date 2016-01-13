package com.dianping.main.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.barcode.BarCodeAndQRCodeUtils;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.pay.view.PayTypeSelectView;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class UserCodeActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final int MESSAGE_RELOAD = 0;
  private static final int MESSAGE_RELOAD_INFO = 2;
  private static final int MESSAGE_TRY = 1;
  private static final int REQUEST_CODE_PAYWXNOPWD = 1;
  private static final int TIME_RELOAD = 60000;
  private static final int TIME_RELOAD_INFO = 1000;
  private static final int TIME_TRY = 3000;
  private ImageView mBarCodeView;
  private LinearLayout mCodeLayout;
  private int mCurrentUserCodeIndex;
  private LinearLayout mErrorLayout;
  private MApiRequest mGetCodeRequest;
  private Handler mHandle = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 0)
      {
        UserCodeActivity.this.mHandle.removeMessages(0);
        UserCodeActivity.this.showImage();
        UserCodeActivity.this.mHandle.sendEmptyMessageDelayed(0, 60000L);
      }
      do
      {
        return;
        if (paramMessage.what != 1)
          continue;
        UserCodeActivity.this.sendHasSucceedRequest();
        return;
      }
      while (paramMessage.what != 2);
      UserCodeActivity.this.updateInfoTextView(true, null);
    }
  };
  private MApiRequest mHasSucceedRequest;
  private View mInfoIcon;
  private TextView mInfoView;
  private boolean mIsFirstUpdate = true;
  private ImageView mQrCodeView;
  private LinearLayout mReloadView;
  private String mUserCode;
  private String[] mUserCodes = new String[0];
  private String mUserCodesString;
  private PayTypeSelectView mpayTypeSelectView;
  private AddPopWindow popWindow;

  private void sendGetCodeRequest()
  {
    if (this.mGetCodeRequest != null)
      mapiService().abort(this.mGetCodeRequest, this, true);
    this.mUserCode = null;
    showProgressDialog("二维码请求中，请稍后...");
    this.mGetCodeRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/codepay/getusercode.bin?newtoken=" + accountService().newToken(), CacheType.DISABLED);
    mapiService().exec(this.mGetCodeRequest, this);
  }

  private void sendHasSucceedRequest()
  {
    if (this.mHasSucceedRequest != null)
      mapiService().abort(this.mHasSucceedRequest, this, true);
    this.mHasSucceedRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/codepay/getcodescheme.bin", new String[] { "usercodes", this.mUserCodesString, "newtoken", accountService().newToken(), "currentcode", this.mUserCode });
    mapiService().exec(this.mHasSucceedRequest, this);
  }

  private void showImage()
  {
    stopTryMessage();
    if (this.mUserCodes.length > this.mCurrentUserCodeIndex)
    {
      String[] arrayOfString = this.mUserCodes;
      int i = this.mCurrentUserCodeIndex;
      this.mCurrentUserCodeIndex = (i + 1);
      this.mUserCode = arrayOfString[i];
      this.mBarCodeView.setImageBitmap(BarCodeAndQRCodeUtils.createBarcode(this, this.mUserCode, getResources().getDimensionPixelSize(R.dimen.barcode_width), getResources().getDimensionPixelSize(R.dimen.barcode_height), true));
      this.mQrCodeView.setImageBitmap(BarCodeAndQRCodeUtils.createQRImage(this, this.mUserCode, getResources().getDimensionPixelSize(R.dimen.qrcode_height), getResources().getDimensionPixelSize(R.dimen.qrcode_height)));
      if (!this.mIsFirstUpdate)
      {
        updateInfoTextView(false, "已更新");
        this.mHandle.sendEmptyMessageDelayed(2, 1000L);
      }
      this.mIsFirstUpdate = false;
      startTryMessage(0L);
      return;
    }
    sendGetCodeRequest();
  }

  private void startTryMessage(long paramLong)
  {
    if (this.isResumed)
      this.mHandle.sendEmptyMessageDelayed(1, paramLong);
  }

  private void stopTryMessage()
  {
    if (this.mHasSucceedRequest != null)
    {
      mapiService().abort(this.mHasSucceedRequest, this, true);
      this.mGetCodeRequest = null;
    }
    this.mHandle.removeMessages(1);
  }

  private void updateInfoTextView(boolean paramBoolean, String paramString)
  {
    if (paramBoolean)
    {
      this.mInfoIcon.setVisibility(0);
      this.mInfoView.setTextColor(getResources().getColor(R.color.light_gray));
      this.mInfoView.setText("每分钟自动更新");
      this.mReloadView.setEnabled(true);
      return;
    }
    this.mReloadView.setEnabled(false);
    this.mInfoIcon.setVisibility(8);
    this.mInfoView.setTextColor(getResources().getColor(R.color.deep_gray));
    this.mInfoView.setText(paramString);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (this.mpayTypeSelectView != null));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getWindow().getAttributes();
    paramBundle.screenBrightness = 1.0F;
    getWindow().setAttributes(paramBundle);
    setContentView(R.layout.activity_user_code);
    this.mCodeLayout = ((LinearLayout)findViewById(R.id.code_layout));
    this.mErrorLayout = ((LinearLayout)findViewById(R.id.errorView));
    this.mBarCodeView = ((ImageView)findViewById(R.id.barcode));
    this.mQrCodeView = ((ImageView)findViewById(R.id.qrcode));
    this.mReloadView = ((LinearLayout)findViewById(R.id.reload));
    this.mReloadView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        UserCodeActivity.this.mHandle.sendEmptyMessage(0);
      }
    });
    this.mInfoView = ((TextView)findViewById(R.id.info));
    this.mInfoIcon = findViewById(R.id.info_icon);
    this.popWindow = new AddPopWindow(this);
    updateInfoTextView(true, null);
    setTitle("付款码");
    if (isLogined())
    {
      this.mHandle.sendEmptyMessage(0);
      return;
    }
    paramBundle = new ArrayList();
    paramBundle.add(new BasicNameValuePair("goto", "dianping://usercode"));
    gotoLogin(paramBundle);
    finish();
  }

  protected void onDestroy()
  {
    if (this.mGetCodeRequest != null)
    {
      mapiService().abort(this.mGetCodeRequest, this, true);
      this.mGetCodeRequest = null;
    }
    stopTryMessage();
    this.mHandle.removeMessages(0);
    this.mHandle.removeMessages(2);
    super.onDestroy();
  }

  protected void onPause()
  {
    super.onPause();
    stopTryMessage();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mGetCodeRequest)
    {
      this.mCodeLayout.setVisibility(8);
      this.mErrorLayout.setVisibility(0);
      dismissDialog();
      this.mGetCodeRequest = null;
      updateInfoTextView(true, null);
    }
    do
      return;
    while (paramMApiRequest != this.mHasSucceedRequest);
    paramMApiResponse = paramMApiResponse.message();
    paramMApiRequest = paramMApiResponse;
    if (paramMApiResponse == null)
      paramMApiRequest = new SimpleMsg("", "网络不给力", 0, 0);
    if (paramMApiRequest.statusCode() == 0)
      startTryMessage(3000L);
    while (true)
    {
      this.mHasSucceedRequest = null;
      return;
      if (paramMApiRequest.statusCode() != 1)
        continue;
      this.mHandle.removeMessages(0);
      showSimpleAlertDialog(paramMApiRequest.title(), paramMApiRequest.content(), "重新扫码", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          UserCodeActivity.this.mHandle.sendEmptyMessage(0);
        }
      }
      , "返回", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          UserCodeActivity.this.finish();
        }
      });
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mGetCodeRequest)
    {
      this.mCodeLayout.setVisibility(0);
      this.mErrorLayout.setVisibility(8);
      dismissDialog();
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mUserCodesString = ((DPObject)paramMApiResponse.result()).getString("UserCodes");
        if (!TextUtils.isEmpty(this.mUserCodesString))
        {
          this.mUserCodes = this.mUserCodesString.split(",");
          this.mCurrentUserCodeIndex = 0;
          this.mUserCode = null;
          this.mHandle.sendEmptyMessage(0);
        }
      }
      this.mGetCodeRequest = null;
    }
    do
      return;
    while (paramMApiRequest != this.mHasSucceedRequest);
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      startActivity(((DPObject)paramMApiResponse.result()).getString("Scheme"));
      finish();
    }
    stopTryMessage();
    this.mHasSucceedRequest = null;
  }

  protected void onResume()
  {
    super.onResume();
    if (!TextUtils.isEmpty(this.mUserCode))
      startTryMessage(0L);
    if (this.mpayTypeSelectView != null);
  }

  class AddPopWindow extends PopupWindow
  {
    private View conentView;

    public AddPopWindow(Activity arg2)
    {
      this.conentView = ((LayoutInflater)localTextView.getSystemService("layout_inflater")).inflate(R.layout.pay_code_popview, null);
      setContentView(this.conentView);
      setHeight(-2);
      setWidth(-2);
      setFocusable(true);
      setOutsideTouchable(true);
      update();
      setBackgroundDrawable(new ColorDrawable(0));
      TextView localTextView = (TextView)this.conentView.findViewById(R.id.pay_code_pop_tv);
      this.conentView.setOnClickListener(new View.OnClickListener(UserCodeActivity.this)
      {
        public void onClick(View paramView)
        {
          UserCodeActivity.this.startActivityForResult("dianping://paywxnopwd", 1);
          UserCodeActivity.AddPopWindow.this.dismiss();
        }
      });
    }

    public void showPopupWindow(View paramView)
    {
      if (!isShowing())
      {
        showAsDropDown(paramView, 0, -5);
        return;
      }
      dismiss();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.UserCodeActivity
 * JD-Core Version:    0.6.0
 */
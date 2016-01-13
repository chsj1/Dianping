package com.dianping.queue.activity;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.queue.util.QueueBroadcastUtils;
import com.dianping.util.KeyboardUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class QueueBindPhoneActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String BINDING_HINT = "客官，新号码绑定中...";
  protected static final String BIND_PHONE_FAIL = "出问题了，绑定失败，稍后再试试吧";
  protected static final int COUNT_DOWN_NUM = 60;
  protected static final String GET_VALIDATION_CODE_FAIL = "获取验证码失败，请稍后再试";
  protected static final int MSG_COUNT_DOWN = 100;
  protected static final String TIP_FROM_MY_QUEUE_LIST = "需要修改排号手机号？请验证一下哦~";
  protected static final String validationLabel = "获取验证码";
  protected MApiRequest bindRequest;
  protected final Context context = this;
  protected TextView getValidationCodeButton;
  protected MApiRequest getValidationCodeRequest;
  protected final Handler handler = new CountDownHandler(null);
  protected EditText phoneEditText;
  protected Button submitBindButton;
  protected TextView titleTip;
  protected EditText validationCodeEditText;

  private boolean checkInput()
  {
    ForegroundColorSpan localForegroundColorSpan = new ForegroundColorSpan(-16777216);
    SpannableStringBuilder localSpannableStringBuilder;
    if ((this.phoneEditText.getText() == null) || (TextUtils.isEmpty(this.phoneEditText.getText().toString().trim())))
    {
      this.phoneEditText.requestFocus();
      localSpannableStringBuilder = new SpannableStringBuilder("手机号码不能为空");
      localSpannableStringBuilder.setSpan(localForegroundColorSpan, 0, "手机号码不能为空".length(), 0);
      this.phoneEditText.setError(localSpannableStringBuilder);
      return false;
    }
    if ((this.validationCodeEditText.getText() == null) || (TextUtils.isEmpty(this.validationCodeEditText.getText().toString().trim())))
    {
      this.validationCodeEditText.requestFocus();
      localSpannableStringBuilder = new SpannableStringBuilder("验证码不能为空");
      localSpannableStringBuilder.setSpan(localForegroundColorSpan, 0, "验证码不能为空".length(), 0);
      this.validationCodeEditText.setError(localSpannableStringBuilder);
      return false;
    }
    return true;
  }

  private void getValidationCode()
  {
    if (this.getValidationCodeRequest != null)
      mapiService().abort(this.getValidationCodeRequest, this, true);
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/queue/getphoneverifycode.qu?").buildUpon();
    String str = this.phoneEditText.getEditableText().toString();
    this.getValidationCodeRequest = BasicMApiRequest.mapiPost(localBuilder.toString(), new String[] { "mobile", str });
    mapiService().exec(this.getValidationCodeRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        QueueBindPhoneActivity.this.showAlertDialog(null, "获取验证码失败，请稍后再试");
        QueueBindPhoneActivity.this.handler.removeMessages(100);
        QueueBindPhoneActivity.this.updateGetValidationCodeButton(-1);
        QueueBindPhoneActivity.this.getValidationCodeRequest = null;
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        QueueBindPhoneActivity.this.showShortToast("获取成功，请等待验证码短信");
        QueueBindPhoneActivity.this.getValidationCodeRequest = null;
      }
    });
  }

  private void hideSoftKeyBoard()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
    if (this.phoneEditText != null)
      localInputMethodManager.hideSoftInputFromWindow(this.phoneEditText.getWindowToken(), 0);
    if (this.validationCodeEditText != null)
      localInputMethodManager.hideSoftInputFromInputMethod(this.validationCodeEditText.getWindowToken(), 0);
  }

  private void submitBind()
  {
    if (this.bindRequest != null)
      mapiService().abort(this.bindRequest, this, true);
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/queue/getphoneverifyresult.qu?").buildUpon();
    String str1 = this.phoneEditText.getEditableText().toString();
    String str2 = this.validationCodeEditText.getText().toString();
    this.bindRequest = BasicMApiRequest.mapiPost(localBuilder.toString(), new String[] { "mobile", str1, "code", str2 });
    mapiService().exec(this.bindRequest, this);
  }

  private void updateGetValidationCodeButton(int paramInt)
  {
    if (paramInt <= 0)
    {
      this.getValidationCodeButton.setEnabled(true);
      this.getValidationCodeButton.setText("获取验证码");
      return;
    }
    this.getValidationCodeButton.setEnabled(false);
    this.getValidationCodeButton.setText("获取验证码(" + paramInt + ")");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.queue_bind_phone);
    this.validationCodeEditText = ((EditText)findViewById(R.id.validation_code));
    this.titleTip = ((TextView)findViewById(R.id.title_tip));
    this.titleTip.setText("需要修改排号手机号？请验证一下哦~");
    super.setTitle("修改排号手机");
    this.phoneEditText = ((EditText)findViewById(R.id.queue_phone));
    this.phoneEditText.requestFocus();
    KeyboardUtils.popupKeyboard(this.phoneEditText);
    this.getValidationCodeButton = ((TextView)findViewById(R.id.get_validation_btn));
    this.getValidationCodeButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if ((QueueBindPhoneActivity.this.phoneEditText.getText() == null) || (TextUtils.isEmpty(QueueBindPhoneActivity.this.phoneEditText.getText().toString().trim())))
        {
          paramView = new ForegroundColorSpan(-16777216);
          QueueBindPhoneActivity.this.phoneEditText.requestFocus();
          SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder("手机号码不能为空");
          localSpannableStringBuilder.setSpan(paramView, 0, "手机号码不能为空".length(), 0);
          QueueBindPhoneActivity.this.phoneEditText.setError(localSpannableStringBuilder);
          return;
        }
        QueueBindPhoneActivity.this.getValidationCode();
        QueueBindPhoneActivity.this.handler.removeMessages(100);
        paramView = Message.obtain();
        paramView.what = 100;
        paramView.arg1 = 60;
        QueueBindPhoneActivity.this.handler.sendMessageDelayed(paramView, 0L);
      }
    });
    this.submitBindButton = ((Button)findViewById(R.id.submit_bind_btn));
    this.submitBindButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        QueueBindPhoneActivity.this.hideSoftKeyBoard();
        if (!QueueBindPhoneActivity.this.checkInput())
          return;
        QueueBindPhoneActivity.this.submitBind();
        QueueBindPhoneActivity.this.showProgressDialog("客官，新号码绑定中...");
      }
    });
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.getValidationCodeRequest != null)
    {
      mapiService().abort(this.getValidationCodeRequest, this, true);
      this.getValidationCodeRequest = null;
    }
    if (this.bindRequest != null)
    {
      mapiService().abort(this.bindRequest, this, true);
      this.bindRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.bindRequest)
    {
      showAlertDialog(null, "出问题了，绑定失败，稍后再试试吧");
      this.bindRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.bindRequest)
    {
      paramMApiRequest = new Bundle();
      paramMApiRequest.putString("phone", this.phoneEditText.getEditableText().toString());
      QueueBroadcastUtils.sendQueueBroadcast(this.context, "com.dianping.queue.QUEUE_PHONE_BIND_SUCCESS", paramMApiRequest);
      this.bindRequest = null;
      finish();
    }
  }

  private class CountDownHandler extends Handler
  {
    private CountDownHandler()
    {
    }

    public void handleMessage(Message paramMessage)
    {
      if ((paramMessage.what == 100) && (paramMessage.arg1 >= 0))
      {
        QueueBindPhoneActivity.this.updateGetValidationCodeButton(paramMessage.arg1);
        Message localMessage = Message.obtain();
        localMessage.what = 100;
        paramMessage.arg1 -= 1;
        sendMessageDelayed(localMessage, 1000L);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.queue.activity.QueueBindPhoneActivity
 * JD-Core Version:    0.6.0
 */
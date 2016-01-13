package com.dianping.main.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.Log;
import com.dianping.util.log.FileAppender;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.message.BasicNameValuePair;

public class FeedbackActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, TextWatcher
{
  private static final String TAG = FeedbackActivity.class.getSimpleName();
  private static final Pattern rfc2822 = Pattern.compile("^[a-z0-9A-Z!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9A-Z!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9A-Z](?:[a-z0-9A-Z-]*[a-z0-9A-Z])?\\.)+[a-z0-9A-Z](?:[a-z0-9A-Z-]*[a-z0-9A-Z])?$");
  private MApiService apiService;
  private String callId;
  EditText editContent;
  EditText editEmail;
  private int flag = 0;
  boolean isShopError = false;
  String membercardid;
  private MApiRequest request;
  String shopid;
  private Button submit;

  public void afterTextChanged(Editable paramEditable)
  {
    if (TextUtils.isEmpty(this.editContent.getText().toString().trim()))
    {
      this.submit.setEnabled(false);
      return;
    }
    this.submit.setEnabled(true);
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  boolean isEmailValidate(String paramString)
  {
    Matcher localMatcher = rfc2822.matcher(paramString);
    if (TextUtils.isEmpty(paramString));
    do
      return true;
    while (localMatcher.matches());
    Toast.makeText(this, "请填写正确的邮箱地址", 0).show();
    return false;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.feedback);
    this.callId = UUID.randomUUID().toString();
    paramBundle = (TextView)findViewById(R.id.text1);
    this.editContent = ((EditText)findViewById(R.id.feedback_content).findViewById(R.id.itemInput));
    this.editContent.setSingleLine(false);
    this.editContent.setGravity(48);
    this.editContent.setMinLines(11);
    this.editContent.addTextChangedListener(this);
    this.editEmail = ((EditText)findViewById(R.id.feedback_email).findViewById(R.id.itemInput));
    Object localObject = getIntent().getExtras();
    if ((localObject != null) && (((Bundle)localObject).get("shopId") != null))
    {
      setTitle("其他错误");
      this.isShopError = true;
      paramBundle.setText("感谢您报告商户的错误信息。我们将回复邮件发送至您填入的邮箱中");
      this.editContent.setHint("请输入错误信息或修改意见（字数500以内）");
    }
    if (localObject != null)
      this.flag = ((Bundle)localObject).getInt("flag");
    localObject = getIntent();
    Uri localUri = ((Intent)localObject).getData();
    String str = localUri.getQueryParameter("flag");
    if (!TextUtils.isEmpty(str));
    try
    {
      this.flag = Integer.parseInt(str);
      this.shopid = localUri.getQueryParameter("shopid");
      this.membercardid = localUri.getQueryParameter("membercardid");
      if (this.flag == 5)
      {
        setTitle("其他原因");
        setSubtitle(((Intent)localObject).getStringExtra("name"));
        paramBundle.setText("请详细描述投诉原因，便于我们尽快处理。");
        this.editContent.setHint("请输入投诉理由（字数500以内）");
        super.getTitleBar().setLeftView(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            FeedbackActivity.this.statisticsEvent("mycard5", "mycard5_complaint_cancel", "其他原因", 0);
            FeedbackActivity.this.finish();
          }
        });
      }
      paramBundle = getAccount();
      if ((paramBundle != null) && (paramBundle.email() != null))
        this.editEmail.setText(paramBundle.email());
      if (getIntent().getData() != null)
      {
        paramBundle = getIntent().getData().getQueryParameter("content");
        if (paramBundle != null)
          this.editContent.setText(paramBundle);
        paramBundle = getIntent().getData().getQueryParameter("email");
        if (paramBundle != null)
          this.editEmail.setText(paramBundle);
      }
      this.submit = ((Button)findViewById(R.id.submit));
      this.submit.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = FeedbackActivity.this.editEmail.getText().toString();
          String str = FeedbackActivity.this.editContent.getText().toString();
          if (TextUtils.isEmpty(str.trim()))
            Toast.makeText(FeedbackActivity.this, "请输入反馈内容", 0).show();
          while (true)
          {
            new Thread("Upload_Log_Thread")
            {
              public void run()
              {
                String str = FileAppender.getInstance().getCodeLog();
                FileAppender.getInstance().postLog(str, "http://m.api.dianping.com/applog/appcodelog.api");
              }
            }
            .start();
            do
              return;
            while (!FeedbackActivity.this.isEmailValidate(paramView));
            if (FeedbackActivity.this.isShopError)
            {
              FeedbackActivity.this.submit(paramView, str, FeedbackActivity.this.getIntent().getExtras().get("shopId").toString());
              continue;
            }
            FeedbackActivity.this.submit(paramView, str, null);
          }
        }
      });
      this.submit.setEnabled(false);
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
        finish();
    }
  }

  protected void onPause()
  {
    super.onPause();
    KeyboardUtils.hideKeyboard(this.editContent);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    this.request = null;
    if (paramMApiResponse.message() != null)
    {
      Log.i(TAG, paramMApiResponse.message().toString());
      Toast.makeText(this, paramMApiResponse.message().toString(), 1).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest != null)
      Toast.makeText(this, paramMApiRequest.getString("Content"), 0).show();
    if (this.flag == 3)
      statisticsEvent("mybooking5", "mybooking5_feesback_submit", "", 0);
    while (true)
    {
      finish();
      return;
      paramMApiRequest = new ArrayList();
      paramMApiRequest.add(new BasicNameValuePair("shopid", this.shopid));
      statisticsEvent("addfeedback5", "addfeedback5_extra", "", 0, paramMApiRequest);
    }
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void submit(String paramString1, String paramString2, String paramString3)
  {
    if (this.request != null)
    {
      Log.i(TAG, "already requesting");
      return;
    }
    if (this.apiService == null)
      this.apiService = ((MApiService)getService("mapi"));
    Object localObject = getAccount();
    if (localObject == null);
    for (localObject = null; ; localObject = ((UserProfile)localObject).token())
      try
      {
        if (!this.isShopError)
          break;
        String str = paramString3 + "";
        paramString3 = paramString1;
        if ("".equals(paramString1))
          paramString3 = Environment.deviceId() + "";
        this.request = BasicMApiRequest.mapiPost("http://m.api.dianping.com/addshopfeedback.bin", new String[] { "flag", "4", "shopid", str, "email", paramString3, "token", localObject, "content", paramString2, "callid", this.callId });
        this.apiService.exec(this.request, this);
        showProgressDialog("正在提交...");
        return;
      }
      catch (Exception paramString1)
      {
        paramString1.printStackTrace();
        return;
      }
    paramString3 = new ArrayList();
    if (!TextUtils.isEmpty(paramString2))
    {
      paramString2 = new StringBuilder(paramString2);
      if (!TextUtils.isEmpty((CharSequence)localObject))
        paramString2.append(" Token=").append((String)localObject).append("; ");
      if (!TextUtils.isEmpty(Environment.deviceId()))
        paramString2.append(" Device=").append(Environment.deviceId()).append("; ");
      if (!TextUtils.isEmpty(Environment.sessionId()))
        paramString2.append(" Session=").append(Environment.sessionId()).append("; ");
      paramString2.append(" City=").append(cityId()).append("; ");
      if (this.flag == 5)
      {
        paramString3.add("comment");
        label386: paramString3.add(paramString2.toString());
      }
    }
    else
    {
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        paramString3.add("token");
        paramString3.add(localObject);
      }
      if (!TextUtils.isEmpty(paramString1))
      {
        paramString3.add("email");
        paramString3.add(paramString1);
      }
      if (this.flag != 3)
        break label562;
      paramString1 = "http://rs.api.dianping.com/addfeedback.yy";
    }
    while (true)
    {
      paramString2 = location();
      if (paramString2 != null)
      {
        localObject = Location.FMT;
        paramString3.add("lat");
        paramString3.add(String.valueOf(((NumberFormat)localObject).format(paramString2.latitude())));
        paramString3.add("lng");
        paramString3.add(String.valueOf(((NumberFormat)localObject).format(paramString2.longitude())));
      }
      this.request = BasicMApiRequest.mapiPost(paramString1, (String[])paramString3.toArray(new String[0]));
      this.apiService.exec(this.request, this);
      break;
      paramString3.add("content");
      break label386;
      label562: if (this.flag == 4)
      {
        paramString1 = "http://mc.api.dianping.com/addfeedback.mc";
        continue;
      }
      if (this.flag == 5)
      {
        paramString1 = "http://mc.api.dianping.com/addusercomment.mc";
        paramString3.add("shopid");
        paramString3.add(this.shopid);
        paramString3.add("membercardid");
        paramString3.add(this.membercardid);
        continue;
      }
      if (this.flag == 1)
      {
        paramString1 = "http://app.t.dianping.com/addfeedbackgn.bin";
        paramString3.add("flag");
        paramString3.add(String.valueOf(0));
        continue;
      }
      if (this.flag == 7)
      {
        paramString1 = "http://app.t.dianping.com/addfeedbackgn.bin";
        paramString3.add("type");
        paramString3.add("prepaidcard");
        statisticsEvent("paidcardinfo5", "paidcardinfo5_feedback_submit", "", 0);
        continue;
      }
      if (this.flag == 6)
      {
        paramString1 = "http://m.api.dianping.com/addfeedback.bin";
        paramString3.add("flag");
        paramString3.add(String.valueOf(6));
        continue;
      }
      if (this.flag == 8)
      {
        paramString1 = "http://waimai.api.dianping.com/feedback.ta";
        paramString3.add("device");
        paramString3.add(Environment.deviceId());
        paramString3.add("cityid");
        paramString3.add(String.valueOf(cityId()));
        continue;
      }
      paramString1 = "http://m.api.dianping.com/addfeedback.bin";
      paramString3.add("flag");
      paramString3.add(String.valueOf(0));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.FeedbackActivity
 * JD-Core Version:    0.6.0
 */
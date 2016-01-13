package com.dianping.membercard;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.DPDatePickerDialog;
import com.dianping.base.widget.DPDatePickerDialog.OnDatePickerClickListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.membercard.model.JoinMCHandler;
import com.dianping.membercard.model.JoinMCHandler.OnJoinCardRequestHandlerListener;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import com.dianping.widget.view.NovaButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberInfoActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>, DPDatePickerDialog.OnDatePickerClickListener, JoinMCHandler.OnJoinCardRequestHandlerListener
{
  private static final int GENDER_MAN = 1;
  private static final int GENDER_WOMAN = 0;
  private static final String TITLE = "会员信息";
  private TextView birthdayPicker;
  int from = 0;
  private int gender;
  private DPDatePickerDialog mBirthdayDialog;
  TextView mBirthdayTextView;
  Calendar mCurCalendar;
  MApiRequest mGetUserInfoRequest = null;
  JoinMCHandler mJoinMCHandler;
  private RadioButton mManBtn;
  EditText mNameEditText;
  NovaButton mSubmitButton;
  MApiRequest mUpdateUserInfoRequest = null;
  private RadioButton mWomanBtn;
  private String membercardid;
  private int reload = 1;
  int source;

  private boolean checkEditBox()
  {
    this.mNameEditText.setError(null);
    this.mBirthdayTextView.setError(null);
    ForegroundColorSpan localForegroundColorSpan = new ForegroundColorSpan(-16777216);
    if (TextUtils.isEmpty(this.mNameEditText.getText().toString()))
    {
      this.mNameEditText.requestFocus();
      localObject = new SpannableStringBuilder("姓名不能为空");
      ((SpannableStringBuilder)localObject).setSpan(localForegroundColorSpan, 0, "姓名不能为空".length(), 0);
      this.mNameEditText.setError((CharSequence)localObject);
      return false;
    }
    Object localObject = this.mNameEditText.getText().toString().trim();
    if (!Pattern.compile("^[a-zA-Z一-龥]+$").matcher((CharSequence)localObject).matches())
    {
      this.mNameEditText.requestFocus();
      localObject = new SpannableStringBuilder("姓名只能为英文和汉字");
      ((SpannableStringBuilder)localObject).setSpan(localForegroundColorSpan, 0, "姓名只能为英文和汉字".length(), 0);
      this.mNameEditText.setError((CharSequence)localObject);
      return false;
    }
    if (((String)localObject).length() > 12)
    {
      this.mNameEditText.requestFocus();
      localObject = new SpannableStringBuilder("姓名长度不能大于12个");
      ((SpannableStringBuilder)localObject).setSpan(localForegroundColorSpan, 0, "姓名长度不能大于12个".length(), 0);
      this.mNameEditText.setError((CharSequence)localObject);
      return false;
    }
    if ((TextUtils.isEmpty(this.mBirthdayTextView.getText().toString())) && ("您的生日".equals(this.mBirthdayTextView.getText().toString())))
    {
      this.mBirthdayTextView.requestFocus();
      localObject = new SpannableStringBuilder("生日不能为空");
      ((SpannableStringBuilder)localObject).setSpan(localForegroundColorSpan, 0, "生日不能为空".length(), 0);
      this.mBirthdayTextView.setError((CharSequence)localObject);
      return false;
    }
    return true;
  }

  private void saveUserInfo()
  {
    SharedPreferences localSharedPreferences = getSharedPreferences("mcuserinfo", 0);
    localSharedPreferences.edit().putString("name", this.mNameEditText.getText().toString().trim()).commit();
    localSharedPreferences.edit().putString("birthday", this.mBirthdayTextView.getText().toString().trim()).commit();
    localSharedPreferences.edit().putInt("gender", this.gender).commit();
  }

  private void setupView()
  {
    super.setContentView(R.layout.member_info);
    this.mNameEditText = ((EditText)findViewById(R.id.user_name));
    this.mBirthdayTextView = ((TextView)findViewById(R.id.user_birthday));
    this.mBirthdayTextView.setOnClickListener(this);
    this.mManBtn = ((RadioButton)findViewById(R.id.manBtn));
    this.mManBtn.setOnClickListener(this);
    this.mWomanBtn = ((RadioButton)findViewById(R.id.womanBtn));
    this.mWomanBtn.setOnClickListener(this);
    this.mSubmitButton = ((NovaButton)findViewById(R.id.submit));
    this.mSubmitButton.setGAString("dpcard_memberinfo_submit_button");
    this.mSubmitButton.setOnClickListener(this);
    if (this.from == 1)
      this.mSubmitButton.setText("保存");
  }

  private void showDayDialog(Calendar paramCalendar)
  {
    this.mBirthdayDialog = new DPDatePickerDialog(this, R.style.dialog);
    this.mBirthdayDialog.setYearRange(1930, 2013).setPositiveButton("确定", this).setCurrentCalendar(paramCalendar).show();
  }

  private void updateBirthDay()
  {
    if (this.mCurCalendar != null)
    {
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
      this.mBirthdayTextView.setText(localSimpleDateFormat.format(Long.valueOf(this.mCurCalendar.getTimeInMillis())).toString());
      this.mBirthdayTextView.setTextColor(getResources().getColor(R.color.deep_black));
    }
  }

  private void updateGender()
  {
    if (this.gender == 0)
    {
      this.mWomanBtn.setChecked(true);
      return;
    }
    this.mManBtn.setChecked(true);
  }

  public String getPageName()
  {
    return "memberinfo";
  }

  public void getUserInfoTask()
  {
    showProgressDialog("正在发起请求，请稍候...");
    String str2 = "http://mc.api.dianping.com/getuserinfo.mc?" + "membercardid=" + this.membercardid;
    String str1 = str2;
    if (accountService().token() != null)
      str1 = str2 + "&token=" + accountService().token();
    this.mGetUserInfoRequest = BasicMApiRequest.mapiGet(str1, CacheType.DISABLED);
    mapiService().exec(this.mGetUserInfoRequest, this);
  }

  public void joinTask()
  {
    String str1 = this.mNameEditText.getText().toString().trim();
    String str2 = this.mBirthdayTextView.getText().toString().trim();
    this.mJoinMCHandler.joinTask(this.membercardid, this.source, str1, String.valueOf(this.gender), str2);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.submit)
      if (checkEditBox());
    do
    {
      return;
      if (this.from == 1)
      {
        updateUserInfoTask();
        return;
      }
      if (this.source == 12)
        statisticsEvent("cardinfo5", "cardinfo5_joinsubmit_shopinfo", "", 0);
      while (true)
      {
        joinTask();
        return;
        if (this.source != 14)
          continue;
        statisticsEvent("cardinfo5", "cardinfo5_joinsubmit_availablecard", "", 0);
      }
      if (paramView.getId() == R.id.manBtn)
      {
        this.gender = 1;
        updateGender();
        return;
      }
      if (paramView.getId() != R.id.womanBtn)
        continue;
      this.gender = 0;
      updateGender();
      return;
    }
    while (paramView.getId() != R.id.user_birthday);
    if (this.mCurCalendar == null)
    {
      this.mCurCalendar = Calendar.getInstance();
      this.mCurCalendar.set(1, 1985);
      this.mCurCalendar.set(2, 6);
      this.mCurCalendar.set(5, 15);
    }
    showDayDialog(this.mCurCalendar);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setTitle("会员信息");
    parseIntent();
    setupView();
    if (this.from == 0)
    {
      this.mJoinMCHandler = new JoinMCHandler(this);
      this.mJoinMCHandler.setOnJoinCardRequestHandlerListener(this);
    }
    if (this.reload == 1)
    {
      this.gender = 0;
      updateGender();
      getUserInfoTask();
      return;
    }
    setUserInfo();
  }

  public void onDatePickerClick(Dialog paramDialog)
  {
    if (this.mBirthdayDialog == null)
      return;
    if (this.mBirthdayDialog.isShowing())
      this.mBirthdayDialog.dismiss();
    this.mCurCalendar = this.mBirthdayDialog.getCurrentCalendar();
    this.mBirthdayTextView.setTextColor(getResources().getColor(R.color.deep_black));
    updateBirthDay();
  }

  protected void onDestroy()
  {
    if (this.mJoinMCHandler != null)
      this.mJoinMCHandler.removeListener();
    super.onDestroy();
  }

  public void onJoinCardFinish(DPObject paramDPObject)
  {
    setResult(20);
    finish();
  }

  public boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      joinTask();
      return true;
    }
    return false;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mGetUserInfoRequest)
    {
      dismissDialog();
      Toast.makeText(this, paramMApiResponse.message().toString(), 0).show();
      this.mGetUserInfoRequest = null;
    }
    do
      return;
    while (paramMApiRequest != this.mUpdateUserInfoRequest);
    dismissDialog();
    Toast.makeText(this, paramMApiResponse.message().toString(), 0).show();
    this.mUpdateUserInfoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.mGetUserInfoRequest)
    {
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        update((DPObject)paramMApiResponse.result());
      this.mGetUserInfoRequest = null;
    }
    do
      return;
    while (paramMApiRequest != this.mUpdateUserInfoRequest);
    if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      saveUserInfo();
      paramMApiRequest = new Intent("com.dianping.action.UPDATE_USER_INFO");
      paramMApiResponse = new Bundle();
      String str1 = this.mNameEditText.getText().toString().trim();
      String str2 = this.mBirthdayTextView.getText().toString().trim();
      paramMApiResponse.putString("username", str1);
      paramMApiResponse.putInt("gender", this.gender);
      paramMApiResponse.putString("birthday", str2);
      paramMApiRequest.putExtras(paramMApiResponse);
      sendBroadcast(paramMApiRequest);
      setResult(10, paramMApiRequest);
      finish();
    }
    this.mUpdateUserInfoRequest = null;
  }

  public void parseIntent()
  {
    Object localObject = getIntent().getData();
    this.membercardid = ((Uri)localObject).getQueryParameter("membercardid");
    String str = ((Uri)localObject).getQueryParameter("from");
    try
    {
      this.from = Integer.parseInt(str);
      label35: str = ((Uri)localObject).getQueryParameter("source");
      try
      {
        this.source = Integer.parseInt(str);
        label51: localObject = ((Uri)localObject).getQueryParameter("reload");
        if ((localObject != null) && (((String)localObject).trim().length() > 0));
        try
        {
          this.reload = Integer.parseInt((String)localObject);
          return;
        }
        catch (NumberFormatException localNumberFormatException1)
        {
          return;
        }
      }
      catch (NumberFormatException localNumberFormatException2)
      {
        break label51;
      }
    }
    catch (NumberFormatException localNumberFormatException3)
    {
      break label35;
    }
  }

  public void setUserInfo()
  {
    Object localObject1 = getSharedPreferences("mcuserinfo", 0);
    Object localObject2 = ((SharedPreferences)localObject1).getString("name", null);
    this.mNameEditText.setText((CharSequence)localObject2);
    if (localObject2 != null)
    {
      int i = ((String)localObject2).length();
      if (i > 0)
        this.mNameEditText.setSelection(i);
    }
    this.gender = ((SharedPreferences)localObject1).getInt("gender", 0);
    updateGender();
    localObject1 = ((SharedPreferences)localObject1).getString("birthday", null);
    if ((localObject1 != null) && (((String)localObject1).trim().length() > 0));
    try
    {
      localObject2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
      this.mCurCalendar = Calendar.getInstance();
      this.mCurCalendar.setTimeInMillis(((SimpleDateFormat)localObject2).parse((String)localObject1).getTime());
      updateBirthDay();
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public void update(DPObject paramDPObject)
  {
    String str = paramDPObject.getString("UserName");
    this.mNameEditText.setText(str);
    if (str != null)
    {
      int i = str.length();
      if (i > 0)
        this.mNameEditText.setSelection(i);
    }
    this.gender = paramDPObject.getInt("Gender");
    updateGender();
    long l = paramDPObject.getTime("Birthday");
    if (l > 0L)
    {
      this.mCurCalendar = Calendar.getInstance();
      this.mCurCalendar.setTimeInMillis(l);
    }
    updateBirthDay();
  }

  public void updateUserInfoTask()
  {
    showProgressDialog("正在提交请求，请稍候...");
    ArrayList localArrayList = new ArrayList();
    if (accountService().token() != null)
    {
      localArrayList.add("token");
      localArrayList.add(accountService().token());
    }
    localArrayList.add("username");
    localArrayList.add(this.mNameEditText.getText().toString().trim());
    localArrayList.add("gender");
    localArrayList.add(String.valueOf(this.gender));
    localArrayList.add("birthday");
    localArrayList.add(this.mBirthdayTextView.getText().toString().trim());
    this.mUpdateUserInfoRequest = BasicMApiRequest.mapiPost("http://mc.api.dianping.com/updateuserinfo.mc", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.mUpdateUserInfoRequest, this);
    statisticsEvent("mycard5", "mycard5_profile_submit", "", 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.MemberInfoActivity
 * JD-Core Version:    0.6.0
 */
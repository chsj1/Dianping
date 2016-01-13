package com.dianping.membercard;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.ImageUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.google.zxing.WriterException;
import java.text.SimpleDateFormat;

@SuppressLint({"SimpleDateFormat"})
public class MyQrCodeActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String MEMBERCARD_RIGHT_TIP = "扫描二维码，尊享会员特权";
  private static final String PREPAIDCARD_RIGHT_TIP = "请向服务员出示二维码，进行验证";
  private static final String UPDATE_UI = "com.dianping.action.UPDATE_USER_INFO";
  private String birthday;
  private String birthdayPrefix;
  private TextView birthdayText;
  IntentFilter filter;
  private int gender;
  private boolean isCardScore = false;
  MApiRequest mGetUserInfoRequest = null;
  private boolean mIsPrepaidCard = false;
  BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.action.UPDATE_USER_INFO"))
      {
        MyQrCodeActivity.access$002(MyQrCodeActivity.this, paramIntent.getStringExtra("username"));
        if ((MyQrCodeActivity.this.name != null) && (MyQrCodeActivity.this.name.trim().length() > 0))
        {
          MyQrCodeActivity.access$102(MyQrCodeActivity.this, paramIntent.getStringExtra("birthday"));
          MyQrCodeActivity.access$202(MyQrCodeActivity.this, paramIntent.getIntExtra("gender", 0));
          MyQrCodeActivity.this.updateName();
          MyQrCodeActivity.this.updateBirthday();
          MyQrCodeActivity.this.writeUserInfo();
        }
      }
    }
  };
  private String memberCardID = "";
  private TextView memberNameText;
  private String name;
  private String namePrefix;
  private Bitmap qrbmp;
  private String qrcode;
  private TextView qrcodeHint;
  private ImageView qrcodeImgView;
  private int reload = 1;
  private String tip = "扫描二维码，尊享会员特权";

  public void getUserInfoTask()
  {
    String str1 = "http://mc.api.dianping.com/getuserinfo.mc";
    if (accountService().token() != null)
      str1 = "http://mc.api.dianping.com/getuserinfo.mc" + "?token=" + accountService().token();
    String str2 = str1;
    if (!TextUtils.isEmpty(this.memberCardID))
      str2 = str1 + "&membercardid=" + this.memberCardID;
    this.mGetUserInfoRequest = BasicMApiRequest.mapiGet(str2, CacheType.DISABLED);
    mapiService().exec(this.mGetUserInfoRequest, this);
  }

  public void gotoMemberInfo()
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://memberinfo?from=1&reload=" + this.reload)));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getData();
    if (!TextUtils.isEmpty(paramBundle.getQueryParameter("qrcode")))
    {
      this.qrcode = paramBundle.getQueryParameter("qrcode");
      this.mIsPrepaidCard = true;
      this.tip = "请向服务员出示二维码，进行验证";
    }
    if (paramBundle.getQuery().contains("iscardscore"))
      if (paramBundle.getQueryParameter("iscardscore").equals("1"))
      {
        this.isCardScore = true;
        if (paramBundle.getQuery().contains("membercardid"))
          this.memberCardID = paramBundle.getQueryParameter("membercardid");
      }
    while (true)
    {
      this.namePrefix = getResources().getString(R.string.mc_username_text);
      this.birthdayPrefix = getResources().getString(R.string.mc_birthday_text);
      setupView();
      readUserInfo();
      getUserInfoTask();
      this.filter = new IntentFilter("com.dianping.action.UPDATE_USER_INFO");
      registerReceiver(this.mReceiver, this.filter);
      updateQRCode();
      return;
      this.isCardScore = false;
      continue;
      this.isCardScore = false;
    }
  }

  protected void onDestroy()
  {
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    if (this.mGetUserInfoRequest != null)
    {
      mapiService().abort(this.mGetUserInfoRequest, this, true);
      this.mGetUserInfoRequest = null;
    }
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      setResult(30);
      finish();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.reload = 1;
    if (paramMApiRequest == this.mGetUserInfoRequest)
      this.mGetUserInfoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mGetUserInfoRequest)
    {
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        this.name = paramMApiRequest.getString("UserName");
        this.gender = paramMApiRequest.getInt("Gender");
        this.reload = 0;
        updateName();
        paramMApiResponse = new SimpleDateFormat("yyyy-MM-dd");
        long l = paramMApiRequest.getTime("Birthday");
        if (l > 0L)
          this.birthday = paramMApiResponse.format(Long.valueOf(l)).toString();
        updateBirthday();
        if (!this.mIsPrepaidCard)
        {
          this.qrcode = paramMApiRequest.getString("QRCode");
          if (!TextUtils.isEmpty(this.qrcode))
            break label177;
          if (this.isCardScore)
          {
            paramMApiRequest = BitmapFactory.decodeResource(getResources(), R.drawable.mc_qrcode_network_error);
            this.qrcodeImgView.setImageBitmap(paramMApiRequest);
            this.qrcodeHint.setVisibility(4);
          }
        }
      }
      this.mGetUserInfoRequest = null;
    }
    else
    {
      return;
    }
    label177: if (this.isCardScore)
      updateCardScoreQRCode();
    while (true)
    {
      writeUserInfo();
      break;
      updateQRCode();
    }
  }

  public void readUserInfo()
  {
    if (this.isCardScore)
    {
      localSharedPreferences = getSharedPreferences("mccardscore_" + this.memberCardID, 0);
      this.name = localSharedPreferences.getString("name", "");
      this.gender = localSharedPreferences.getInt("gender", 0);
      updateName();
      this.birthday = localSharedPreferences.getString("birthday", "");
      updateBirthday();
      this.qrcode = localSharedPreferences.getString("qrcode", "");
      updateCardScoreQRCode();
    }
    do
      return;
    while (this.mIsPrepaidCard);
    SharedPreferences localSharedPreferences = getSharedPreferences("mcuserinfo", 0);
    this.name = localSharedPreferences.getString("name", "");
    this.gender = localSharedPreferences.getInt("gender", 0);
    updateName();
    this.birthday = localSharedPreferences.getString("birthday", "");
    updateBirthday();
    this.qrcode = localSharedPreferences.getString("qrcode", "u:0;m:0;no:0");
    updateQRCode();
  }

  public void setupView()
  {
    super.setTitle("我的二维码");
    super.setContentView(R.layout.card_qrcode_layout);
    this.memberNameText = ((TextView)findViewById(R.id.card_qrcode_name));
    this.birthdayText = ((TextView)findViewById(R.id.card_qrcode_birthday));
    this.qrcodeImgView = ((ImageView)findViewById(R.id.card_qrcode_img));
    this.qrcodeHint = ((TextView)findViewById(R.id.card_qrcode_hint));
    this.qrcodeHint.setText(this.tip);
    ((ImageButton)findViewById(R.id.card_qrcode_setting)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MyQrCodeActivity.this.statisticsEvent("myqrcode5", "myqrcode5_profile", null, 0);
        MyQrCodeActivity.this.gotoMemberInfo();
      }
    });
    this.leftTitleButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MyQrCodeActivity.this.setResult(30);
        MyQrCodeActivity.this.finish();
      }
    });
  }

  public void updateBirthday()
  {
    if (TextUtils.isEmpty(this.birthday))
    {
      this.birthdayText.setText(this.birthdayPrefix);
      return;
    }
    this.birthdayText.setText(this.birthdayPrefix + this.birthday);
  }

  public void updateCardScoreQRCode()
  {
    int i = ViewUtils.dip2px(this, 235.0F);
    int j = ViewUtils.dip2px(this, 235.0F);
    try
    {
      if ((this.qrcode != null) && (!"".equals(this.qrcode)))
      {
        this.qrbmp = ImageUtils.encodeAsBitmap(this.qrcode, i, j);
        this.qrcodeImgView.setImageBitmap(this.qrbmp);
        this.qrcodeHint.setVisibility(0);
        return;
      }
      this.qrcodeHint.setVisibility(4);
      return;
    }
    catch (WriterException localWriterException)
    {
      localWriterException.printStackTrace();
    }
  }

  public void updateName()
  {
    if (!TextUtils.isEmpty(this.name))
    {
      TextView localTextView = this.memberNameText;
      StringBuilder localStringBuilder = new StringBuilder().append(this.namePrefix).append(this.name).append(" ");
      if (this.gender == 0);
      for (String str = "女士"; ; str = "先生")
      {
        localTextView.setText(str);
        return;
      }
    }
    this.memberNameText.setText(this.namePrefix);
  }

  public void updateQRCode()
  {
    int i = ViewUtils.dip2px(this, 235.0F);
    int j = ViewUtils.dip2px(this, 235.0F);
    try
    {
      if ((this.qrcode != null) && (!"".equals(this.qrcode)));
      for (this.qrbmp = ImageUtils.encodeAsBitmap(this.qrcode, i, j); ; this.qrbmp = ImageUtils.encodeAsBitmap("u:0;m:0;no:0", i, j))
      {
        this.qrcodeImgView.setImageBitmap(this.qrbmp);
        this.qrcodeHint.setVisibility(0);
        return;
      }
    }
    catch (WriterException localWriterException)
    {
      localWriterException.printStackTrace();
    }
  }

  public void writeUserInfo()
  {
    if (this.isCardScore)
    {
      localSharedPreferences = getSharedPreferences("mccardscore_" + this.memberCardID, 0);
      if (this.name != null)
        localSharedPreferences.edit().putString("name", this.name).commit();
      if (this.birthday != null)
        localSharedPreferences.edit().putString("birthday", this.birthday).commit();
      if (this.qrcode != null)
        localSharedPreferences.edit().putString("qrcode", this.qrcode).commit();
      localSharedPreferences.edit().putInt("gender", this.gender).commit();
    }
    do
      return;
    while (this.mIsPrepaidCard);
    SharedPreferences localSharedPreferences = getSharedPreferences("mcuserinfo", 0);
    if (this.name != null)
      localSharedPreferences.edit().putString("name", this.name).commit();
    if (this.birthday != null)
      localSharedPreferences.edit().putString("birthday", this.birthday).commit();
    if (this.qrcode != null)
      localSharedPreferences.edit().putString("qrcode", this.qrcode).commit();
    localSharedPreferences.edit().putInt("gender", this.gender).commit();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.MyQrCodeActivity
 * JD-Core Version:    0.6.0
 */
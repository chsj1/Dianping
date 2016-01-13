package com.dianping.base.share.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.business.IShareEngine;
import com.dianping.base.share.business.ShareEngineFactory;
import com.dianping.base.sso.ILogininListener;
import com.dianping.base.sso.IThirdSSOLogin;
import com.dianping.base.sso.ThirdSSOLoginFactory;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import java.util.UUID;

public class SnsShareActivity extends NovaActivity
  implements View.OnClickListener, ILogininListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int AT_FRIEND_REQ_CODE = 1000;
  private static final String K_CALL_ID = "callId";
  private static final String K_CONTENT = "content";
  private static final String K_EXTRA = "extra";
  private static final String K_FEED = "feed";
  private static final String K_IMAGE_URL = "imageurl";
  private static final String K_NAME = "Name";
  private static final String K_PLATFORM = "platform";
  private static final String K_SSO_RESPONSE = "ssoresponse";
  private static final String K_THUMB_URL = "thumburl";
  private static final String K_TOKEN = "token";
  private static final String K_TYPE = "type";
  private static final int MAX_WORD_COUNT = 140;
  private static final String TEST_PLATFORM = "sina";
  private boolean mBindStatus = false;
  private Button mBtnShare;
  private String mCallId;
  String mContent;
  EditText mEtWord;
  private String mExtra;
  long mExtraWordCount;
  private int mFeed = 1;
  private String mImageUrl;
  private NetworkImageView mIvThumb;
  private String mPlatform;
  private IShareEngine mShareEngine;
  private MApiRequest mSnsBindStatusReq;
  private MApiRequest mSsoBindReq;
  private IThirdSSOLogin mSsoLogin;
  String mSsoResponse;
  private MApiRequest mThirdShareReq;
  private String mThumbUrl;
  private String mToken;
  private TextView mTvAt;
  private TextView mTvHide;
  private TextView mTvLeftCount;
  private int mType = 5;
  private TextWatcher mWordWatcher;

  static long calculateLength(CharSequence paramCharSequence)
  {
    double d = 0.0D;
    int i = 0;
    if (i < paramCharSequence.length())
    {
      int j = paramCharSequence.charAt(i);
      if ((j > 0) && (j < 127))
        d += 0.5D;
      while (true)
      {
        i += 1;
        break;
        d += 1.0D;
      }
    }
    return Math.round(d);
  }

  private long getInputCount()
  {
    return calculateLength(this.mEtWord.getText().toString());
  }

  private void initView()
  {
    this.mEtWord = ((EditText)findViewById(R.id.et_word));
    this.mIvThumb = ((NetworkImageView)findViewById(R.id.iv_thumb));
    this.mTvAt = ((TextView)findViewById(R.id.tv_at));
    this.mTvLeftCount = ((TextView)findViewById(R.id.tv_left_count));
    this.mBtnShare = ((Button)findViewById(R.id.btn_share));
    this.mTvHide = ((TextView)findViewById(R.id.tv_hide));
    if ((TextUtils.isEmpty(this.mThumbUrl)) && (TextUtils.isEmpty(this.mImageUrl)))
    {
      ViewUtils.hideView(this.mIvThumb, true);
      this.mTvAt.setOnClickListener(this);
      this.mWordWatcher = new WordWatcher(null);
      this.mEtWord.addTextChangedListener(this.mWordWatcher);
      this.mEtWord.setText(this.mContent);
      this.mBtnShare.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (TextUtils.isEmpty(SnsShareActivity.this.mContent))
          {
            SnsShareActivity.this.showToast("分享内容不能为空");
            return;
          }
          SnsShareActivity.this.sendThirdShareReq();
        }
      });
      this.mTvHide.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          KeyboardUtils.hideKeyboard(SnsShareActivity.this.mEtWord);
        }
      });
      return;
    }
    ViewUtils.showView(this.mIvThumb);
    this.mIvThumb.setOnClickListener(this);
    NetworkImageView localNetworkImageView = this.mIvThumb;
    if (TextUtils.isEmpty(this.mThumbUrl));
    for (String str = this.mImageUrl; ; str = this.mThumbUrl)
    {
      localNetworkImageView.setImage(str);
      break;
    }
  }

  private void onSnsBindStatusSuccess(DPObject paramDPObject)
  {
    paramDPObject = paramDPObject.getObject(this.mShareEngine.getSnsStatusName());
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramDPObject != null)
    {
      bool1 = paramDPObject.getBoolean("IsBind");
      bool2 = paramDPObject.getBoolean("IsExpire");
    }
    if ((bool1) && (!bool2))
    {
      this.mBindStatus = true;
      return;
    }
    ssoOAuth();
  }

  private void onSsoBindSuccess(DPObject paramDPObject)
  {
    showToast("绑定成功");
    this.mBindStatus = true;
  }

  private void onThirdShareSuccess(DPObject paramDPObject)
  {
    if (this.mBindStatus)
      showToast("分享成功");
    while (true)
    {
      finish();
      return;
      showToast("分享失败");
    }
  }

  private void sendSnsBindStatusReq()
  {
    if (this.mSnsBindStatusReq != null)
    {
      mapiService().abort(this.mSnsBindStatusReq, this, true);
      this.mSnsBindStatusReq = null;
    }
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/snsbindstatus.bin").buildUpon();
    localBuilder.appendQueryParameter("token", this.mToken);
    this.mSnsBindStatusReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.mSnsBindStatusReq, this);
  }

  private void ssoOAuth()
  {
    String str = this.mShareEngine.getSsoLoginUrl();
    this.mSsoLogin = ThirdSSOLoginFactory.ssoLogin(str);
    this.mSsoLogin.ssoLogin(str, this, this);
  }

  private void startAtFriendActivityForResult()
  {
    Uri.Builder localBuilder = Uri.parse("dianping://atfriend").buildUpon();
    localBuilder.appendQueryParameter("platform", this.mPlatform);
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(localBuilder.build());
    startActivityForResult(localIntent, 1000);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 1000)
      if (paramInt2 == -1)
      {
        paramIntent = paramIntent.getStringExtra("Name");
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("@");
        localStringBuilder.append(paramIntent);
        localStringBuilder.append(" ");
        paramIntent = localStringBuilder.toString();
        this.mEtWord.append(paramIntent);
      }
    do
      return;
    while (this.mSsoLogin == null);
    this.mSsoLogin.callback(paramInt1, paramInt2, paramIntent);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.tv_at)
      startAtFriendActivityForResult();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getAccount() == null)
    {
      gotoLogin();
      finish();
      return;
    }
    this.mToken = accountService().token();
    if (paramBundle == null)
    {
      paramBundle = getIntent().getData();
      this.mContent = paramBundle.getQueryParameter("content");
      if (this.mContent == null)
        this.mContent = "";
      this.mExtra = paramBundle.getQueryParameter("extra");
      if (this.mExtra == null)
        this.mExtra = "";
      this.mImageUrl = paramBundle.getQueryParameter("imageurl");
      if (this.mImageUrl == null)
        this.mImageUrl = "";
      this.mThumbUrl = paramBundle.getQueryParameter("thumburl");
      if (this.mThumbUrl == null)
        this.mThumbUrl = "";
      this.mPlatform = paramBundle.getQueryParameter("platform");
      if (this.mPlatform == null)
        this.mPlatform = "sina";
      this.mCallId = UUID.randomUUID().toString();
    }
    while (true)
    {
      this.mShareEngine = ShareEngineFactory.createShareEngine(this.mFeed);
      this.mPlatform = this.mShareEngine.getPlatform();
      this.mExtraWordCount = calculateLength(this.mExtra);
      super.setContentView(R.layout.activity_sns_share);
      initView();
      sendSnsBindStatusReq();
      return;
      this.mFeed = paramBundle.getInt("feed");
      this.mType = paramBundle.getInt("type");
      this.mContent = paramBundle.getString("content");
      this.mExtra = paramBundle.getString("extra");
      this.mImageUrl = paramBundle.getString("imageurl");
      this.mThumbUrl = paramBundle.getString("thumburl");
      this.mCallId = paramBundle.getString("callId");
      this.mPlatform = paramBundle.getString("platform");
    }
  }

  protected void onLeftTitleButtonClicked()
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("shareResult", "cancel");
    localIntent.putExtra("shareChannel", "新浪微博");
    setResult(-1, localIntent);
    super.onLeftTitleButtonClicked();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
      startActivity(getIntent());
    return super.onLogin(paramBoolean);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message().content();
    if (paramMApiRequest.equals(this.mSnsBindStatusReq))
    {
      this.mSnsBindStatusReq = null;
      showToast(paramMApiResponse);
      finish();
    }
    do
    {
      return;
      if (!paramMApiRequest.equals(this.mSsoBindReq))
        continue;
      this.mSsoBindReq = null;
      showToast(paramMApiResponse);
      finish();
      return;
    }
    while (!paramMApiRequest.equals(this.mThirdShareReq));
    this.mThirdShareReq = null;
    paramMApiRequest = new Intent();
    paramMApiRequest.putExtra("shareResult", "fail");
    paramMApiRequest.putExtra("shareChannel", "新浪微博");
    setResult(-1, paramMApiRequest);
    showToast(paramMApiResponse);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    Object localObject = paramMApiResponse.result();
    paramMApiResponse = null;
    if ((localObject instanceof DPObject))
      paramMApiResponse = (DPObject)localObject;
    if (paramMApiRequest.equals(this.mSnsBindStatusReq))
    {
      this.mSnsBindStatusReq = null;
      onSnsBindStatusSuccess(paramMApiResponse);
    }
    do
    {
      return;
      if (!paramMApiRequest.equals(this.mSsoBindReq))
        continue;
      this.mSsoBindReq = null;
      onSsoBindSuccess(paramMApiResponse);
      return;
    }
    while (!paramMApiRequest.equals(this.mThirdShareReq));
    this.mThirdShareReq = null;
    paramMApiRequest = new Intent();
    paramMApiRequest.putExtra("shareResult", "success");
    paramMApiRequest.putExtra("shareChannel", "新浪微博");
    if (this.mBindStatus)
      setResult(-1, paramMApiRequest);
    while (true)
    {
      onThirdShareSuccess(paramMApiResponse);
      return;
      setResult(64035, paramMApiRequest);
    }
  }

  public void onSSOLoginCancel(int paramInt)
  {
  }

  public void onSSOLoginFailed(int paramInt)
  {
    showToast("sso登录失败");
    Intent localIntent = new Intent();
    localIntent.putExtra("shareResult", "fail");
    localIntent.putExtra("shareChannel", "新浪微博");
    setResult(-1, localIntent);
    finish();
  }

  public void onSSOLoginSucceed(int paramInt, Object paramObject)
  {
    if (paramInt == 1)
    {
      this.mSsoResponse = ((Bundle)paramObject).toString();
      sendSsoBindReq();
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("feed", this.mFeed);
    paramBundle.putInt("type", this.mType);
    paramBundle.putString("content", this.mContent);
    paramBundle.putString("extra", this.mExtra);
    paramBundle.putString("imageurl", this.mImageUrl);
    paramBundle.putString("thumburl", this.mThumbUrl);
    paramBundle.putString("callId", this.mCallId);
    paramBundle.putString("platform", this.mPlatform);
  }

  void sendSsoBindReq()
  {
    if (this.mSsoBindReq != null)
    {
      mapiService().abort(this.mSsoBindReq, this, true);
      this.mSsoBindReq = null;
    }
    this.mSsoBindReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/ssobind.bin", new String[] { "token", this.mToken, "ssoresponse", this.mSsoResponse, "platform", this.mPlatform });
    mapiService().exec(this.mSsoBindReq, this);
  }

  void sendThirdShareReq()
  {
    if (this.mThirdShareReq != null)
    {
      mapiService().abort(this.mThirdShareReq, this, true);
      this.mThirdShareReq = null;
    }
    this.mContent = (this.mContent + " " + this.mExtra);
    this.mThirdShareReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/thirdshare.bin", new String[] { "token", this.mToken, "feed", String.valueOf(this.mFeed), "type", String.valueOf(this.mType), "content", this.mContent, "imageurl", this.mImageUrl, "callId", this.mCallId });
    mapiService().exec(this.mThirdShareReq, this);
  }

  void setLeftCount()
  {
    String str = String.format("您还可以输入%s个字", new Object[] { String.valueOf(140L - getInputCount() - this.mExtraWordCount) });
    this.mTvLeftCount.setText(str);
  }

  private class WordWatcher
    implements TextWatcher
  {
    private int mEditEnd;
    private int mEditStart;

    private WordWatcher()
    {
    }

    public void afterTextChanged(Editable paramEditable)
    {
      this.mEditStart = SnsShareActivity.this.mEtWord.getSelectionStart();
      this.mEditEnd = SnsShareActivity.this.mEtWord.getSelectionEnd();
      SnsShareActivity.this.mEtWord.removeTextChangedListener(this);
      while (true)
      {
        if ((SnsShareActivity.calculateLength(paramEditable.toString()) <= 140L - SnsShareActivity.this.mExtraWordCount) || (this.mEditStart == 0))
        {
          SnsShareActivity.this.mEtWord.setSelection(this.mEditStart);
          SnsShareActivity.this.mEtWord.addTextChangedListener(this);
          SnsShareActivity.this.setLeftCount();
          return;
        }
        paramEditable.delete(this.mEditStart - 1, this.mEditEnd);
        this.mEditStart -= 1;
        this.mEditEnd -= 1;
      }
    }

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
      SnsShareActivity.this.mContent = paramCharSequence.toString();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.activity.SnsShareActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.base.share.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.sync.SnsHelper;
import com.dianping.base.share.sync.SnsView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.DPBasicItem;
import java.util.UUID;

public class ThirdShareActivity extends NovaActivity
  implements AccountListener, RequestHandler<MApiRequest, MApiResponse>
{
  public static final String K_CONTENT = "content";
  public static final String K_EXTRA = "extra";
  public static final String K_FEED = "feed";
  public static final String K_ID = "id";
  public static final String K_MEMBER_CARD_ID = "membercardid";
  public static final String K_TUAN_URL = "tuanUrl";
  public static final String K_TYPE = "type";
  public static final String K_UUID = "callid";
  static final String MC_URL = "http://mc.api.dianping.com/";
  private Button btnSubmit;
  EditText contentEdt;
  private TextView countTv;
  String mCallId;
  String mContent;
  String mExtra;
  private int mFeed;
  String mId;
  String mMemberCardId;
  String mTuanUrl;
  int mType;
  MApiRequest shareCard;
  MApiRequest shareReq;
  SnsView snsView;

  private void setupView()
  {
    super.setTitle("分享");
    super.setContentView(R.layout.thirdshare);
    this.contentEdt = ((EditText)((DPBasicItem)findViewById(R.id.content_edt)).findViewById(R.id.itemInput));
    this.contentEdt.setMinLines(7);
    this.contentEdt.setMaxLines(7);
    this.contentEdt.setSingleLine(false);
    this.contentEdt.setGravity(48);
    this.countTv = ((TextView)findViewById(R.id.count_tv));
    this.btnSubmit = ((Button)findViewById(R.id.submit));
    setCountText(this.mContent);
    this.contentEdt.setText(this.mContent);
    this.contentEdt.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
        ThirdShareActivity.this.setCountText(paramEditable.toString());
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    });
    this.btnSubmit.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (TextUtils.isEmpty(ThirdShareActivity.this.contentEdt.getText()))
          Toast.makeText(ThirdShareActivity.this, "请输入要分享的内容.", 0).show();
        while (true)
        {
          return;
          int i = ThirdShareActivity.this.snsView.getFeed();
          Object localObject1 = ThirdShareActivity.this;
          Object localObject2 = new StringBuilder().append(ThirdShareActivity.this.contentEdt.getText().toString());
          if (TextUtils.isEmpty(ThirdShareActivity.this.mTuanUrl))
            paramView = "";
          while (true)
          {
            ((ThirdShareActivity)localObject1).mContent = paramView;
            if (i <= 0)
              break label608;
            if (ThirdShareActivity.this.mType == 100)
            {
              ThirdShareActivity.this.mContent = ThirdShareActivity.this.contentEdt.getText().toString();
              ThirdShareActivity.this.shareCard = BasicMApiRequest.mapiPost("http://mc.api.dianping.com/sharecard.mc?", new String[] { "token", ThirdShareActivity.this.accountService().token(), "feed", String.valueOf(i), "content", ThirdShareActivity.this.mContent, "type", String.valueOf(ThirdShareActivity.this.mType), "membercardid", ThirdShareActivity.this.mMemberCardId });
              ThirdShareActivity.this.mapiService().exec(ThirdShareActivity.this.shareCard, ThirdShareActivity.this);
              ThirdShareActivity.this.showProgressDialog("正在分享...");
              paramView = ThirdShareActivity.this.snsView.getSnsString();
              if (ThirdShareActivity.this.mType != 0)
                break;
              ThirdShareActivity.this.statisticsEvent("more5", "more5_tellfriend", paramView, 0);
              return;
              paramView = ThirdShareActivity.this.mTuanUrl;
              continue;
            }
            else
            {
              localObject2 = ThirdShareActivity.this;
              String str1 = ThirdShareActivity.this.accountService().token();
              String str2 = ThirdShareActivity.this.mContent;
              String str3 = ThirdShareActivity.this.mCallId;
              int j = ThirdShareActivity.this.mType;
              if (ThirdShareActivity.this.mId == null)
              {
                paramView = "";
                label375: if (ThirdShareActivity.this.mExtra != null)
                  break label521;
              }
              label521: for (localObject1 = ""; ; localObject1 = ThirdShareActivity.this.mExtra)
              {
                ((ThirdShareActivity)localObject2).shareReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/thirdshare.bin?", new String[] { "token", str1, "feed", String.valueOf(i), "content", str2, "callid", str3, "type", String.valueOf(j), "id", paramView, "extra", localObject1 });
                ThirdShareActivity.this.mapiService().exec(ThirdShareActivity.this.shareReq, ThirdShareActivity.this);
                break;
                paramView = ThirdShareActivity.this.mId;
                break label375;
              }
            }
          }
          if (ThirdShareActivity.this.mType == 3)
          {
            ThirdShareActivity.this.statisticsEvent("shopinfo5", "shopinfo5_share", paramView, 0);
            return;
          }
          if (ThirdShareActivity.this.mType == 100)
          {
            ThirdShareActivity.this.statisticsEvent("mycard5", "mycard5_share_snssuccess", paramView, 0);
            return;
          }
          if (ThirdShareActivity.this.mType != 4)
            continue;
          ThirdShareActivity.this.statisticsEvent("tuan5", "tuan5_detail_share", paramView, 0);
          return;
        }
        label608: Toast.makeText(ThirdShareActivity.this, "请选择一种分享方式", 0).show();
      }
    });
    this.snsView = ((SnsView)findViewById(R.id.sync_to_sns));
    this.snsView.setText("分享到");
    this.snsView.bindActivity(this);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    this.snsView.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Uri localUri;
    String str2;
    String str1;
    if (paramBundle == null)
    {
      paramBundle = getIntent();
      localUri = paramBundle.getData();
      str2 = localUri.getQueryParameter("feed");
      str1 = localUri.getQueryParameter("type");
      this.mMemberCardId = localUri.getQueryParameter("membercardid");
    }
    try
    {
      this.mFeed = Integer.parseInt(str2);
      while (true)
      {
        try
        {
          label53: this.mType = Integer.parseInt(str1);
          this.mContent = localUri.getQueryParameter("content");
          this.mId = localUri.getQueryParameter("id");
          this.mExtra = localUri.getQueryParameter("extra");
          this.mTuanUrl = paramBundle.getStringExtra("tuanUrl");
          this.mCallId = UUID.randomUUID().toString();
          setupView();
          if (getAccount() != null)
            continue;
          gotoLogin();
          return;
        }
        catch (Exception localException1)
        {
          this.mType = -1;
          continue;
        }
        this.mFeed = paramBundle.getInt("feed");
        this.mContent = paramBundle.getString("content");
        this.mCallId = paramBundle.getString("callid");
        this.mType = paramBundle.getInt("type");
        this.mId = paramBundle.getString("id");
        this.mExtra = paramBundle.getString("extra");
        this.mTuanUrl = paramBundle.getString("tuanUrl");
        this.mMemberCardId = paramBundle.getString("membercardid");
      }
    }
    catch (Exception localException2)
    {
      break label53;
    }
  }

  protected void onDestroy()
  {
    int i = getAccount().feedFlag();
    i = preferences().getInt("syncMask", i);
    SharedPreferences.Editor localEditor = preferences().edit();
    localEditor.putInt("syncMask", i);
    localEditor.commit();
    super.onDestroy();
  }

  public void onLoginCancel()
  {
    finish();
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (((paramMApiRequest == this.shareReq) || (paramMApiRequest == this.shareCard)) && (paramMApiResponse.message() != null))
      showMessageDialog(paramMApiResponse.message());
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (((paramMApiRequest == this.shareReq) || (paramMApiRequest == this.shareCard)) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      DPObject localDPObject = (DPObject)paramMApiResponse.result();
      paramMApiResponse = localDPObject.getString("Content");
      paramMApiRequest = paramMApiResponse;
      if (paramMApiResponse == null)
        paramMApiRequest = "分享成功";
      dismissDialog();
      Toast.makeText(this, paramMApiRequest, 0).show();
      SnsHelper.updateFeedFlag(preferences(), localDPObject.getInt("Flag"));
      finish();
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("feed", this.mFeed);
    paramBundle.putInt("type", this.mType);
    paramBundle.putString("content", this.mContent);
    paramBundle.putString("callid", this.mCallId);
    paramBundle.putString("id", this.mId);
    paramBundle.putString("extra", this.mExtra);
    paramBundle.putString("tuanUrl", this.mTuanUrl);
  }

  void setCountText(String paramString)
  {
    StringBuffer localStringBuffer1 = new StringBuffer();
    StringBuffer localStringBuffer2 = localStringBuffer1.append("还可输入");
    if (paramString == null);
    for (int i = 0; ; i = paramString.length())
    {
      localStringBuffer2.append(140 - i).append("字");
      this.countTv.setText(localStringBuffer1);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.activity.ThirdShareActivity
 * JD-Core Version:    0.6.0
 */
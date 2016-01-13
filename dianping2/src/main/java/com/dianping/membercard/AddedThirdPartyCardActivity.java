package com.dianping.membercard;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.membercard.model.JoinMCHandler2;
import com.dianping.membercard.model.JoinMCHandler2.OnQuitThirdPartyCardHandlerListener;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class AddedThirdPartyCardActivity extends NovaActivity
{
  private final String API_NAME_ADD_USER_COMMENT = "addusercomment.mc";
  private MApiRequest addUserCommentRequest;
  private MApiRequest cardInfoRequest;
  private DPObject cardObject;
  private JoinMCHandler2 joinMCHandler2;
  private RequestHandler<MApiRequest, MApiResponse> mApiRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == AddedThirdPartyCardActivity.this.cardInfoRequest)
        AddedThirdPartyCardActivity.this.onRequestCardInfoFailed(paramMApiResponse);
      do
        return;
      while (paramMApiRequest != AddedThirdPartyCardActivity.this.addUserCommentRequest);
      AddedThirdPartyCardActivity.this.onRequestAddUserCommentTaskFailed(paramMApiResponse);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == AddedThirdPartyCardActivity.this.cardInfoRequest)
        AddedThirdPartyCardActivity.this.onRequestCardInfoFinish(paramMApiResponse);
      do
        return;
      while (paramMApiRequest != AddedThirdPartyCardActivity.this.addUserCommentRequest);
      AddedThirdPartyCardActivity.this.onRequestAddUserCommentTaskFinish(paramMApiResponse);
    }
  };
  private NovaZeusFragment mWebFragment;
  private String memberCardId;
  private JoinMCHandler2.OnQuitThirdPartyCardHandlerListener onQuitThirdPartyCardHandlerListener = new JoinMCHandler2.OnQuitThirdPartyCardHandlerListener()
  {
    public void onRequestQuitThirdPartyCardResult(boolean paramBoolean, SimpleMsg paramSimpleMsg)
    {
      AddedThirdPartyCardActivity.this.dismissDialog();
      if ((paramBoolean) && ((paramSimpleMsg.flag() == 200) || (paramSimpleMsg.flag() == 201)))
      {
        Toast.makeText(AddedThirdPartyCardActivity.this, paramSimpleMsg.content(), 0).show();
        paramSimpleMsg = new Intent("com.dianping.action.QUIT_MEMBER_CARD");
        paramSimpleMsg.putExtra("membercardid", AddedThirdPartyCardActivity.this.memberCardId);
        AddedThirdPartyCardActivity.this.sendBroadcast(paramSimpleMsg);
        MCUtils.sendUpdateMemberCardListBroadcast(AddedThirdPartyCardActivity.this, AddedThirdPartyCardActivity.this.memberCardId);
        AddedThirdPartyCardActivity.this.finish();
        return;
      }
      AddedThirdPartyCardActivity.this.showMessageDialog(paramSimpleMsg);
    }
  };
  private String shopId;
  private boolean showShareViewAfterGetCardInfo = false;
  private int userCardLevel = 1;
  private String welifeCardTitle;

  private void initEvents()
  {
    getTitleBar().addRightViewItem("tag", R.drawable.title_icon_more, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        AddedThirdPartyCardActivity.this.showMoreFeature();
      }
    });
  }

  private void initParams()
  {
    this.memberCardId = getStringParam("membercardid");
    this.shopId = getStringParam("shopid");
    this.welifeCardTitle = getStringParam("title");
    if (TextUtils.isEmpty(this.welifeCardTitle))
      this.welifeCardTitle = getIntent().getStringExtra("title");
    setTitle(this.welifeCardTitle);
    this.joinMCHandler2 = new JoinMCHandler2(this);
    this.joinMCHandler2.setOnQuitThirdPartyCardHandlerListener(this.onQuitThirdPartyCardHandlerListener);
  }

  private boolean isCardObject(Object paramObject)
  {
    return (paramObject != null) && ((paramObject instanceof DPObject)) && (((DPObject)paramObject).isClass("Card"));
  }

  private boolean isIntentDataValid()
  {
    if (getIntent().getData() == null);
    Uri localUri;
    String str1;
    String str2;
    do
    {
      return false;
      localUri = getIntent().getData();
      str1 = getStringParam("membercardid");
      str2 = getStringParam("shopid");
    }
    while ((TextUtils.isEmpty(localUri.getQuery().substring(localUri.getQuery().indexOf("url=") + "url=".length()))) || (TextUtils.isEmpty(str1)) || (TextUtils.isEmpty(str2)));
    return true;
  }

  private void onRequestAddUserCommentTaskFailed(MApiResponse paramMApiResponse)
  {
    dismissDialog();
    Toast.makeText(this, paramMApiResponse.message().content(), 0).show();
    this.addUserCommentRequest = null;
  }

  private void onRequestAddUserCommentTaskFinish(MApiResponse paramMApiResponse)
  {
    dismissDialog();
    Toast.makeText(this, ((DPObject)paramMApiResponse.result()).getString("Content"), 0).show();
    this.addUserCommentRequest = null;
  }

  private void onRequestCardInfoFailed(MApiResponse paramMApiResponse)
  {
    if (this.showShareViewAfterGetCardInfo)
    {
      this.showShareViewAfterGetCardInfo = false;
      dismissDialog();
      Toast.makeText(this, paramMApiResponse.message().content(), 0).show();
    }
    this.cardObject = null;
    this.cardInfoRequest = null;
  }

  private void onRequestCardInfoFinish(MApiResponse paramMApiResponse)
  {
    if (this.showShareViewAfterGetCardInfo)
      dismissDialog();
    if (isCardObject(paramMApiResponse.result()))
    {
      this.cardObject = ((DPObject)paramMApiResponse.result());
      this.memberCardId = String.valueOf(this.cardObject.getInt("MemberCardID"));
      this.shopId = String.valueOf(this.cardObject.getInt("ShopID"));
      this.welifeCardTitle = this.cardObject.getString("Title");
      setTitle(this.welifeCardTitle);
    }
    while (true)
    {
      this.cardInfoRequest = null;
      if (this.showShareViewAfterGetCardInfo)
      {
        this.showShareViewAfterGetCardInfo = false;
        showMoreFeature();
      }
      return;
      this.cardObject = null;
    }
  }

  private void quitmcTask()
  {
    showProgressDialog("正在提交请求，请稍候...");
    this.joinMCHandler2.quitThirdPartyCard(this.memberCardId);
  }

  private void requestAddUserCommentTask(String paramString)
  {
    showProgressDialog("正在提交请求，请稍候...");
    if (this.addUserCommentRequest != null)
    {
      mapiService().abort(this.addUserCommentRequest, this.mApiRequestHandler, true);
      this.addUserCommentRequest = null;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("membercardid");
    localArrayList.add(this.memberCardId);
    if (accountService().token() != null)
    {
      localArrayList.add("token");
      localArrayList.add(accountService().token());
    }
    localArrayList.add("shopid");
    localArrayList.add(String.valueOf(this.cardObject.getInt("ShopID")));
    localArrayList.add("comment");
    localArrayList.add(paramString);
    this.addUserCommentRequest = BasicMApiRequest.mapiPost("http://mc.api.dianping.com/addusercomment.mc", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.addUserCommentRequest, this.mApiRequestHandler);
  }

  private void requestCardInfo()
  {
    if (this.cardInfoRequest != null)
    {
      mapiService().abort(this.cardInfoRequest, this.mApiRequestHandler, true);
      this.cardInfoRequest = null;
    }
    StringBuilder localStringBuilder = new StringBuilder("http://mc.api.dianping.com/getcardinfo.v2.mc?membercardid=");
    localStringBuilder.append(this.memberCardId);
    if (accountService().token() != null)
    {
      localStringBuilder.append("&token=");
      localStringBuilder.append(accountService().token());
    }
    Object localObject = location();
    if (localObject != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      localStringBuilder.append("&lat=").append(localDecimalFormat.format(((Location)localObject).latitude()));
      localStringBuilder.append("&lng=").append(localDecimalFormat.format(((Location)localObject).longitude()));
    }
    if (this.userCardLevel != 0)
    {
      localStringBuilder.append("&usercardlevel=");
      localStringBuilder.append(this.userCardLevel);
    }
    localStringBuilder.append("&uuid=");
    localStringBuilder.append(Environment.uuid());
    localStringBuilder.append("&from=1");
    localObject = getResources().getDisplayMetrics();
    localStringBuilder.append("&pixel=").append(((DisplayMetrics)localObject).widthPixels);
    this.cardInfoRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.cardInfoRequest, this.mApiRequestHandler);
  }

  private void showConfirmDialog(String paramString)
  {
    new AlertDialog.Builder(this).setTitle("确认投诉").setPositiveButton("确定", new DialogInterface.OnClickListener(paramString)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        AddedThirdPartyCardActivity.this.requestAddUserCommentTask(this.val$s);
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener(paramString)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        AddedThirdPartyCardActivity.this.statisticsEvent("mycard5", "mycard5_complaint_cancel", this.val$s, 0);
      }
    }).show();
  }

  private void showMoreFeature()
  {
    MCUtils.showMembercardMoreFeature(this, this.cardObject, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        switch (paramInt)
        {
        default:
          return;
        case 0:
          paramDialogInterface = AddedThirdPartyCardActivity.this.getAccount();
          AddedThirdPartyCardActivity localAddedThirdPartyCardActivity = AddedThirdPartyCardActivity.this;
          String str = ConfigHelper.mcWeixinUrl;
          Location localLocation = AddedThirdPartyCardActivity.this.location();
          paramInt = AddedThirdPartyCardActivity.this.cityId();
          if (paramDialogInterface == null);
          for (paramDialogInterface = ""; ; paramDialogInterface = paramDialogInterface.token())
          {
            MCUtils.membercardShare2WeiXin(localAddedThirdPartyCardActivity, false, str, localLocation, paramInt, paramDialogInterface, AddedThirdPartyCardActivity.this.cardObject);
            return;
          }
        case 1:
        }
        MCUtils.membercardShare2ThirdParty(AddedThirdPartyCardActivity.this, AddedThirdPartyCardActivity.this.cardObject, AddedThirdPartyCardActivity.this.getAccount().feedFlag());
      }
    }
    , new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        switch (paramInt)
        {
        default:
          return;
        case 0:
          AddedThirdPartyCardActivity.this.showConfirmDialog("商家不让用");
          return;
        case 1:
          AddedThirdPartyCardActivity.this.showConfirmDialog("优惠与描述不符");
          return;
        case 2:
        }
        MCUtils.gotoMembercardFeedBack(AddedThirdPartyCardActivity.this, AddedThirdPartyCardActivity.this.cardObject);
      }
    }
    , new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        AddedThirdPartyCardActivity.this.quitmcTask();
      }
    });
  }

  public String getPageName()
  {
    return "wecarddetail";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!isIntentDataValid())
      throw new IllegalArgumentException("intent data is invalid");
    paramBundle = getIntent();
    Uri localUri = paramBundle.getData().buildUpon().appendQueryParameter("notitlebar", "1").build();
    setIntent(new Intent(paramBundle.getAction(), localUri));
    super.setContentView(R.layout.activity_card_welife_detail);
    this.mWebFragment = ((NovaZeusFragment)getSupportFragmentManager().findFragmentByTag("welife_detail_web_fragment"));
    if (this.mWebFragment == null)
    {
      this.mWebFragment = ((NovaZeusFragment)Fragment.instantiate(this, NovaZeusFragment.class.getName(), MCUtils.handleIntent(this)));
      paramBundle = getSupportFragmentManager().beginTransaction();
      paramBundle.replace(R.id.welife_detail_root_view, this.mWebFragment, "welife_detail_web_fragment");
      paramBundle.commit();
    }
    initParams();
    this.gaExtra.member_card_id = Integer.valueOf(this.memberCardId);
    this.gaExtra.shop_id = Integer.valueOf(this.shopId);
  }

  public void onDestroy()
  {
    if (this.joinMCHandler2 != null)
    {
      this.joinMCHandler2.setOnQuitThirdPartyCardHandlerListener(null);
      this.joinMCHandler2.stopAllRequest();
    }
    if (this.cardInfoRequest != null)
    {
      mapiService().abort(this.cardInfoRequest, this.mApiRequestHandler, true);
      this.cardInfoRequest = null;
    }
    if (this.addUserCommentRequest != null)
    {
      mapiService().abort(this.addUserCommentRequest, this.mApiRequestHandler, true);
      this.addUserCommentRequest = null;
    }
    super.onDestroy();
  }

  protected void onLeftTitleButtonClicked()
  {
    statisticsEvent("mycard", "mycard_wlifecard_view", "收起", 0);
    super.onLeftTitleButtonClicked();
  }

  public void onResume()
  {
    super.onResume();
    initEvents();
    if (this.cardObject == null)
      requestCardInfo();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.AddedThirdPartyCardActivity
 * JD-Core Version:    0.6.0
 */
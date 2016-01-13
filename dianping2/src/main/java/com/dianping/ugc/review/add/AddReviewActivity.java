package com.dianping.ugc.review.add;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.share.sync.SnsView;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.TitleBar;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.model.UserProfile;
import com.dianping.ugc.draft.UGCDraftManager;
import com.dianping.ugc.model.UGCReviewItem;
import com.dianping.ugc.review.ReviewService;
import com.dianping.v1.R.array;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddReviewActivity extends AgentActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String ACTION_ADD_REVIEW = "com.dianping.ugc.addreview";
  public static int SCORE_VISIBLE = 1;
  ArrayList<String> dishes = null;
  public String errorMsg = "";
  Bundle extras;
  boolean fromRecommend;
  boolean hasCommitted = false;
  private boolean loadDraft = false;
  private SnsView mLaySyncShare;
  int minimumReviewWord;
  UGCReviewItem originugcReviewItem;
  private final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      AddReviewActivity.this.dismissDialog();
      paramContext = (DPObject)paramIntent.getParcelableExtra("success");
      if (paramContext != null)
      {
        AddReviewActivity.this.statisticsEvent("addreview5", "addreview5_result", "成功", 0);
        DPApplication.instance().sendBroadcast(new Intent("com.dianping.action.HONEY_CHANGED"));
        paramIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://addreviewsuccess?source=" + AddReviewActivity.this.source));
        paramIntent.putExtra("shopName", "点评成功");
        if (!TextUtils.isEmpty(AddReviewActivity.this.ugcReviewItem.shopId))
          paramIntent.putExtra("shopId", AddReviewActivity.this.ugcReviewItem.shopId);
        if (!TextUtils.isEmpty(AddReviewActivity.this.ugcReviewItem.orderid))
          paramIntent.putExtra("orderId", AddReviewActivity.this.ugcReviewItem.orderid);
        if (paramContext.getObject("ReviewEncourage") != null)
        {
          paramIntent.putExtra("ReviewEncourage", paramContext.getObject("ReviewEncourage"));
          paramIntent.putExtra("reviewId", paramContext.getInt("ReviewId"));
        }
        if (paramContext.getObject("Share") != null)
        {
          paramIntent.putExtra("Title", paramContext.getObject("Share").getString("Title"));
          paramIntent.putExtra("IconUrl", paramContext.getObject("Share").getString("IconUrl"));
          paramIntent.putExtra("Url", paramContext.getObject("Share").getString("Url"));
        }
        paramIntent.putExtra("RecommendType", paramContext.getInt("RecommendType"));
        paramIntent.putExtra("Announce", paramContext.getString("Announce"));
        paramIntent.putExtra("OperationInfo", paramContext.getObject("OperationInfo"));
        AddReviewActivity.this.startActivity(paramIntent);
        AddReviewActivity.this.finish();
        return;
      }
      AddReviewActivity.this.statisticsEvent("addreview5", "addreview5_result", "失败", 0);
      paramContext = paramIntent.getStringExtra("error");
      Toast.makeText(AddReviewActivity.this, paramContext, 0).show();
    }
  };
  int reviewBodyLength;
  DPObject reviewForm;
  private MApiRequest reviewFormRequest;
  DPObject shop;
  String source;
  UGCReviewItem ugcReviewItem;

  private boolean checkCommitStatus()
  {
    int i = 0;
    while (true)
    {
      try
      {
        if (TextUtils.isEmpty(this.ugcReviewItem.star))
          continue;
        i = Integer.valueOf(this.ugcReviewItem.star).intValue();
        if (i > 0)
          continue;
        Toast.makeText(this, "请选择星级", 0).show();
        return false;
        if (!this.ugcReviewItem.hasScore())
          continue;
        Object localObject = this.ugcReviewItem.scores;
        i = 0;
        if (i >= localObject.length)
          continue;
        if (TextUtils.isEmpty(localObject[i]))
        {
          Toast.makeText(this, "请给商户的" + this.ugcReviewItem.scoreNames[i] + "打分", 0).show();
          return false;
          String str = this.ugcReviewItem.comment;
          localObject = str;
          if (TextUtils.isEmpty(str))
            continue;
          localObject = Pattern.compile("\\s*|\t|\r|\n").matcher(str).replaceAll("");
          if ((!TextUtils.isEmpty((CharSequence)localObject)) && (((String)localObject).length() >= this.minimumReviewWord))
            continue;
          Toast.makeText(this, "点评需要满" + this.minimumReviewWord + "字才能提交哦", 0).show();
          localObject = new Bundle();
          ((Bundle)localObject).putBoolean("IsError", true);
          this.mFragment.dispatchAgentChanged("addreview/editview", (Bundle)localObject);
          return false;
          if ((TextUtils.isEmpty(this.ugcReviewItem.comment)) || (this.ugcReviewItem.comment.trim().length() <= 2000))
            continue;
          Toast.makeText(this, "不能超过2000字哦", 0).show();
          return false;
          return true;
        }
      }
      catch (Exception localException)
      {
        return false;
      }
      i += 1;
    }
  }

  private void initViews()
  {
    Object localObject = new TextView(this);
    ((TextView)localObject).setText(R.string.cancel);
    ((TextView)localObject).setGravity(17);
    ((TextView)localObject).setPadding((int)getResources().getDimension(R.dimen.title_bar_button_margin), 0, 0, 0);
    ((TextView)localObject).setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
    ((TextView)localObject).setTextSize(2, 15.0F);
    ((TextView)localObject).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        AddReviewActivity.this.onBackPressed();
      }
    });
    super.getTitleBar().setCustomLeftView((View)localObject);
    localObject = (Button)getLayoutInflater().inflate(R.layout.addreview_button_layout, null);
    ((Button)localObject).setText("发表");
    super.getTitleBar().addRightViewItem((View)localObject, "addreview_commit_referpage", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        AddReviewActivity.this.submit();
      }
    });
  }

  private void loadDishes(DPObject paramDPObject)
  {
    if ((this.fromRecommend) && (this.dishes != null) && (this.dishes.size() > 0))
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
      int i = 0;
      while (i < arrayOfDPObject.length)
      {
        DPObject localDPObject = arrayOfDPObject[i];
        Object localObject2 = localDPObject;
        if (localDPObject.getInt("Type") == 9)
        {
          Object localObject1 = "";
          int j = 0;
          while (j < this.dishes.size())
          {
            localObject2 = (String)localObject1 + (String)this.dishes.get(j);
            localObject1 = localObject2;
            if (j != this.dishes.size() - 1)
              localObject1 = (String)localObject2 + "、";
            j += 1;
          }
          localObject2 = localDPObject;
          if (!TextUtils.isEmpty((CharSequence)localObject1))
            localObject2 = localDPObject.edit().putString("Value", (String)localObject1).generate();
        }
        arrayOfDPObject[i] = localObject2;
        i += 1;
      }
      this.reviewForm = paramDPObject.edit().putArray("List", arrayOfDPObject).generate();
    }
  }

  private void loadReviewForm(String paramString1, String paramString2)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/reviewform.bin").buildUpon();
    String str = paramString1;
    if (paramString1 == null)
      str = "0";
    localBuilder.appendQueryParameter("shopId", str);
    paramString1 = paramString2;
    if (paramString2 == null)
      paramString1 = "0";
    localBuilder.appendQueryParameter("orderid", paramString1);
    localBuilder.appendQueryParameter("token", accountService().token());
    localBuilder.appendQueryParameter("referToken", this.ugcReviewItem.referToken);
    localBuilder.appendQueryParameter("reviewid", String.valueOf(this.ugcReviewItem.reviewId));
    this.reviewFormRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.reviewFormRequest, this);
    showProgressDialog("载入中...");
    if (this.managedDialog != null)
      this.managedDialog.setCanceledOnTouchOutside(false);
  }

  private void processParams(Bundle paramBundle)
  {
    int i;
    if (paramBundle != null)
    {
      this.ugcReviewItem = ((UGCReviewItem)paramBundle.getParcelable("draft"));
      this.originugcReviewItem = ((UGCReviewItem)paramBundle.getParcelable("origindraft"));
      this.loadDraft = paramBundle.getBoolean("fromDraft");
      if (!this.loadDraft)
      {
        this.shop = ((DPObject)getIntent().getParcelableExtra("shop"));
        if (this.shop == null)
          break label365;
        i = this.shop.getInt("ID");
        paramBundle = DPObjectUtils.getShopFullName(this.shop);
        label93: if (i != -1)
          this.ugcReviewItem.shopId = String.valueOf(i);
        if (paramBundle != null)
          this.ugcReviewItem.shopName = paramBundle;
        int j = getIntParam("orderid", -1);
        if (j != -1)
          this.ugcReviewItem.orderid = String.valueOf(j);
        this.ugcReviewItem.reviewId = getIntParam("reviewid", getIntParam("reviewID", 0));
        this.ugcReviewItem.reviewType = getIntParam("reviewType", 0);
        this.ugcReviewItem.referToken = getStringParam("referToken");
        UGCReviewItem localUGCReviewItem = this.ugcReviewItem;
        if (i != 0)
          break label385;
        paramBundle = String.valueOf(j);
        label215: localUGCReviewItem.draftId = paramBundle;
        this.extras = getIntent().getExtras();
      }
      this.fromRecommend = getBooleanParam("fromRecommend", false);
      this.dishes = getIntent().getStringArrayListExtra("dishes");
      this.source = getStringParam("source");
      if (ConfigHelper.minimumReviewWord <= 0)
        break label393;
      i = ConfigHelper.minimumReviewWord;
      label278: this.minimumReviewWord = i;
      if (this.ugcReviewItem.reviewId == 0)
        break label399;
    }
    label385: label393: label399: for (boolean bool = true; ; bool = false)
    {
      this.hasCommitted = bool;
      return;
      this.ugcReviewItem = ((UGCReviewItem)getIntent().getParcelableExtra("draft"));
      if (this.ugcReviewItem == null)
        this.ugcReviewItem = new UGCReviewItem();
      for (this.loadDraft = false; ; this.loadDraft = true)
      {
        this.originugcReviewItem = new UGCReviewItem();
        break;
      }
      label365: i = getIntParam("shopid", -1);
      paramBundle = getStringParam("shopName");
      break label93;
      paramBundle = String.valueOf(i);
      break label215;
      i = 30;
      break label278;
    }
  }

  private void readReviewFormValue(DPObject paramDPObject)
  {
    this.ugcReviewItem.reviewId = paramDPObject.getInt("ReviewId");
    Object localObject;
    int i;
    label88: String str2;
    if (TextUtils.isEmpty(this.ugcReviewItem.shopName))
    {
      this.ugcReviewItem.shopName = paramDPObject.getString("ShopName");
      if (TextUtils.isEmpty(this.ugcReviewItem.shopName))
      {
        localObject = "写点评";
        setTitle((CharSequence)localObject);
      }
    }
    else
    {
      this.ugcReviewItem.tuanTitle = paramDPObject.getString("TuanTitle");
      localObject = paramDPObject.getArray("List");
      i = 0;
      if (i >= localObject.length)
        break label413;
      str2 = localObject[i];
      switch (str2.getInt("Type"))
      {
      case 4:
      case 6:
      case 7:
      default:
      case 1:
      case 2:
      case 3:
      case 5:
      case 8:
      case 9:
      }
    }
    while (true)
    {
      i += 1;
      break label88;
      localObject = this.ugcReviewItem.shopName;
      break;
      String str1 = str2.getString("Value");
      if (TextUtils.isEmpty(str1))
        continue;
      this.ugcReviewItem.averagePrice = str1;
      continue;
      str1 = str2.getString("Value");
      if (TextUtils.isEmpty(str1))
        continue;
      this.ugcReviewItem.comment = str1;
      continue;
      str1 = str2.getString("Value");
      if (TextUtils.isEmpty(str1))
        continue;
      str2 = str2.getString("Param");
      if (str2.equalsIgnoreCase("score1"))
      {
        this.ugcReviewItem.scores[0] = str1;
        continue;
      }
      if (str2.equalsIgnoreCase("score2"))
      {
        this.ugcReviewItem.scores[1] = str1;
        continue;
      }
      if (!str2.equalsIgnoreCase("score3"))
        continue;
      this.ugcReviewItem.scores[2] = str1;
      continue;
      str1 = str2.getString("Value");
      if (TextUtils.isEmpty(str1))
        continue;
      this.ugcReviewItem.star = str1;
      continue;
      str1 = str2.getString("Value");
      if (TextUtils.isEmpty(str1))
        continue;
      this.ugcReviewItem.shoptags = str1;
      continue;
      str1 = str2.getString("Value");
      if (TextUtils.isEmpty(str1))
        continue;
      this.ugcReviewItem.shopdishtags = str1;
    }
    label413: loadDishes(paramDPObject);
  }

  private void showSaveDraftDialog()
  {
    Object localObject = new AlertDialog.Builder(this);
    ((AlertDialog.Builder)localObject).setTitle(R.string.ugc_dialog_hint);
    ((AlertDialog.Builder)localObject).setItems(R.array.select_draft_items, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if (paramInt == 0)
        {
          UGCDraftManager.getInstance().removeDraft(AddReviewActivity.this.ugcReviewItem, true);
          AddReviewActivity.this.finish();
        }
        do
          return;
        while (paramInt != 1);
        UGCDraftManager.getInstance().addDraft(AddReviewActivity.this.ugcReviewItem);
        AddReviewActivity.this.finish();
      }
    });
    ((AlertDialog.Builder)localObject).setNegativeButton(R.string.cancel, null);
    localObject = ((AlertDialog.Builder)localObject).create();
    ((AlertDialog)localObject).setOnDismissListener(new DialogInterface.OnDismissListener()
    {
      public void onDismiss(DialogInterface paramDialogInterface)
      {
        if (AddReviewActivity.this.hasCommitted)
        {
          AddReviewActivity.this.statisticsEvent("editreview5", "editreview5_cancel_cancel", "", 0);
          return;
        }
        AddReviewActivity.this.statisticsEvent("addreview5", "addreview5_cancel_cancel", "", 0);
      }
    });
    ((AlertDialog)localObject).show();
  }

  protected AgentFragment getAgentFragment()
  {
    return new AddReviewConfigurableFragment();
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
    super.onAccountSwitched(paramUserProfile);
    if (paramUserProfile == null)
      finish();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    Fragment localFragment = getSupportFragmentManager().findFragmentById(16908300);
    if (localFragment != null)
      localFragment.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onBackPressed()
  {
    boolean bool = this.ugcReviewItem.equals(this.originugcReviewItem);
    if ((this.loadDraft) || ((this.ugcReviewItem.reviewId == 0) && (!this.loadDraft) && (!bool)))
    {
      showSaveDraftDialog();
      return;
    }
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (accountService().token() == null)
      finish();
    processParams(paramBundle);
    initViews();
    loadReviewForm(this.ugcReviewItem.shopId, this.ugcReviewItem.orderid);
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.ugc.addreview");
    LocalBroadcastManager.getInstance(this).registerReceiver(this.receiver, paramBundle);
    if (this.hasCommitted)
      if (this.source != null)
      {
        if (!"shopinfo".equals(this.source))
          break label115;
        statisticsEvent("shopinfo5", "shopinfo5_toreview_edit", "", 0);
      }
    label115: 
    do
    {
      do
      {
        do
          return;
        while (!"shopreviewlist".equals(this.source));
        statisticsEvent("shopinfo5", "shopinfo5_review_edit", "", 0);
        return;
      }
      while (this.source == null);
      if (!"shopinfo".equals(this.source))
        continue;
      statisticsEvent("shopinfo5", "shopinfo5_toreview", "", 0);
      return;
    }
    while (!"shopreviewlist".equals(this.source));
    statisticsEvent("shopinfo5", "shopinfo5_review_add", "", 0);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.reviewFormRequest != null)
    {
      mapiService().abort(this.reviewFormRequest, null, true);
      this.reviewFormRequest = null;
    }
    LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiver);
  }

  public void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
    if (this.reviewFormRequest != null)
    {
      mapiService().abort(this.reviewFormRequest, null, true);
      this.reviewFormRequest = null;
      finish();
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.reviewFormRequest)
    {
      this.reviewFormRequest = null;
      dismissDialog();
      showMessageDialog(paramMApiResponse.message(), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.cancel();
          AddReviewActivity.this.finish();
        }
      });
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.reviewFormRequest)
    {
      this.reviewFormRequest = null;
      dismissDialog();
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      this.reviewForm = paramMApiResponse;
      if (paramMApiResponse.getInt("reviewId") == 0)
        break label246;
      this.hasCommitted = true;
      if (this.source != null)
      {
        if (!"shopinfo".equals(this.source))
          break label217;
        statisticsEvent("shopinfo5", "shopinfo5_toreview_edit", "", 0);
      }
      readReviewFormValue(paramMApiResponse);
      loadDishes(paramMApiResponse);
      if (TextUtils.isEmpty(this.ugcReviewItem.shopName))
      {
        this.ugcReviewItem.shopName = paramMApiResponse.getString("ShopName");
        if (!TextUtils.isEmpty(this.ugcReviewItem.shopName))
          break label311;
      }
    }
    label311: for (paramMApiRequest = "写点评"; ; paramMApiRequest = this.ugcReviewItem.shopName)
    {
      setTitle(paramMApiRequest);
      ((AgentFragment)getSupportFragmentManager().findFragmentById(16908300)).resetAgents(null);
      paramMApiRequest = paramMApiResponse.getString("BannerInfo");
      if (!TextUtils.isEmpty(paramMApiRequest))
        ((AddReviewConfigurableFragment)this.mFragment).showBannerView(paramMApiRequest);
      paramMApiRequest = paramMApiResponse.getString("TuanTitle");
      this.ugcReviewItem.tuanTitle = paramMApiRequest;
      if (!TextUtils.isEmpty(paramMApiRequest))
        ((AddReviewConfigurableFragment)this.mFragment).showTuanTitleView(paramMApiRequest);
      return;
      label217: if (!"shopreviewlist".equals(this.source))
        break;
      statisticsEvent("shopinfo5", "shopinfo5_review_edit", "", 0);
      break;
      label246: if (this.source == null)
        break;
      if ("shopinfo".equals(this.source))
      {
        statisticsEvent("shopinfo5", "shopinfo5_toreview", "", 0);
        break;
      }
      if (!"shopreviewlist".equals(this.source))
        break;
      statisticsEvent("shopinfo5", "shopinfo5_review_add", "", 0);
      break;
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("draft", this.ugcReviewItem);
    paramBundle.putParcelable("origindraft", this.originugcReviewItem);
    paramBundle.putBoolean("fromDraft", this.loadDraft);
    if ((this.ugcReviewItem != null) && (this.ugcReviewItem.reviewId == 0))
      UGCDraftManager.getInstance().addDraft(this.ugcReviewItem);
  }

  public void submit()
  {
    if (checkCommitStatus())
    {
      if (!this.hasCommitted)
        break label161;
      statisticsEvent("editreview5", "editreview5_submit", "" + this.ugcReviewItem.mPhotos.size(), 0);
    }
    while (true)
    {
      if (this.mLaySyncShare != null)
        statisticsEvent("addreview5", "addreview5_sns", this.mLaySyncShare.getSnsString(), 0);
      Location localLocation = location();
      if (localLocation != null)
      {
        this.ugcReviewItem.lat = Double.parseDouble(Location.FMT.format(localLocation.latitude()));
        this.ugcReviewItem.lng = Double.parseDouble(Location.FMT.format(localLocation.longitude()));
      }
      this.ugcReviewItem.userToken = accountService().token();
      ReviewService.getInstance().review(this.ugcReviewItem);
      showProgressDialog("正在提交，请稍候...");
      return;
      label161: statisticsEvent("addreview5", "addreview5_submit", "" + this.ugcReviewItem.mPhotos.size(), 0);
    }
  }

  public static class AddReviewCellAgent extends CellAgent
  {
    public AddReviewCellAgent(Object paramObject)
    {
      super();
    }

    public AddReviewActivity getActivity()
    {
      return (AddReviewActivity)getContext();
    }

    public Bundle getBundle()
    {
      return ((AddReviewActivity)getContext()).extras;
    }

    public boolean getFromRecommend()
    {
      return ((AddReviewActivity)getContext()).fromRecommend;
    }

    public boolean getHasCommitted()
    {
      return ((AddReviewActivity)getContext()).hasCommitted;
    }

    public int getMinimumReviewWord()
    {
      return ((AddReviewActivity)getContext()).minimumReviewWord;
    }

    public int getReviewBodyLength()
    {
      return ((AddReviewActivity)getContext()).reviewBodyLength;
    }

    public UGCReviewItem getReviewData()
    {
      return ((AddReviewActivity)getContext()).ugcReviewItem;
    }

    public DPObject getReviewForm()
    {
      return ((AddReviewActivity)getContext()).reviewForm;
    }

    public int getReviewType()
    {
      return ((AddReviewActivity)getContext()).ugcReviewItem.reviewType;
    }

    public DPObject getShop()
    {
      return ((AddReviewActivity)getContext()).shop;
    }

    public String getShopName()
    {
      return ((AddReviewActivity)getContext()).ugcReviewItem.shopName;
    }

    public String getSource()
    {
      return ((AddReviewActivity)getContext()).source;
    }

    public boolean isLoadDraft()
    {
      return ((AddReviewActivity)getContext()).loadDraft;
    }

    public String orderId()
    {
      return ((AddReviewActivity)getContext()).ugcReviewItem.orderid;
    }

    public void setErrorMsg(String paramString)
    {
      ((AddReviewActivity)getContext()).errorMsg = paramString;
    }

    public void setLaySyncShare(SnsView paramSnsView)
    {
      AddReviewActivity.access$102((AddReviewActivity)getContext(), paramSnsView);
    }

    public void setMinimumReviewWord(int paramInt)
    {
      ((AddReviewActivity)getContext()).minimumReviewWord = paramInt;
    }

    public void setReviewBodyLength(int paramInt)
    {
      ((AddReviewActivity)getContext()).reviewBodyLength = paramInt;
    }

    public String shopId()
    {
      return ((AddReviewActivity)getContext()).ugcReviewItem.shopId;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.add.AddReviewActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.main.find.pictureplaza;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.DeviceUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class PlazaFeedItem extends NovaLinearLayout
  implements LoginResultListener
{
  protected String actionUrl = null;
  private int curPosition = 0;
  protected String editUrl = null;
  protected String feedCont;
  protected int feedId = 0;
  protected OnFeedItemListener feedItemListener = null;
  protected int feedType = 0;
  private PlazaFeedIntroView introView;
  protected boolean isFromUserCenter = false;
  private boolean isLike = false;
  protected PlazaFeedCommentView likeAndCommentView;
  private int mTopicId = 0;
  private boolean mTopicPage = false;
  protected String mainId = "";
  private PlazaUserProfile pUserProfile;
  private DPObject[] pics;
  protected PoiImageGallery poiImageGallery;
  private PoiLargeImageView.PoiImageListener poiImageListener = new PoiLargeImageView.PoiImageListener()
  {
    public void onLargeImageClick(int paramInt, Drawable paramDrawable)
    {
      if ((paramInt < 0) || (PlazaFeedItem.this.pics == null) || (paramInt > PlazaFeedItem.this.pics.length - 1))
        return;
      ArrayList localArrayList = new ArrayList(PlazaFeedItem.this.pics.length);
      Object localObject2 = PlazaFeedItem.this.pics[paramInt];
      Object localObject1 = new DPObject().edit().putInt("ShopID", ((DPObject)localObject2).getInt("ShopId")).putString("Name", ((DPObject)localObject2).getString("ShopName")).generate();
      int i = 0;
      while (i < PlazaFeedItem.this.pics.length)
      {
        localArrayList.add(new DPObject().edit().putInt("ShopID", ((DPObject)localObject2).getInt("ShopId")).putString("Url", PlazaFeedItem.this.pics[i].getString("PicUrl")).generate());
        i += 1;
      }
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showcheckinphoto"));
      localIntent.putExtra("shopname", ((DPObject)localObject2).getString("ShopName"));
      localObject2 = new ArrayList();
      ((ArrayList)localObject2).add(localObject1);
      localIntent.putParcelableArrayListExtra("arrShopObjs", (ArrayList)localObject2);
      if (paramDrawable != null)
      {
        localObject1 = new ByteArrayOutputStream();
        ((BitmapDrawable)paramDrawable).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject1);
        localIntent.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject1).toByteArray());
      }
      localIntent.putExtra("position", paramInt);
      localIntent.putParcelableArrayListExtra("pageList", localArrayList);
      PlazaFeedItem.this.getContext().startActivity(localIntent);
    }

    public void onPoiClick(int paramInt)
    {
      if ((PlazaFeedItem.this.isFromUserCenter) && (!TextUtils.isEmpty(PlazaFeedItem.this.actionUrl)));
      for (Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(PlazaFeedItem.this.actionUrl)); ; localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + PlazaFeedItem.this.shopId)))
      {
        PlazaFeedItem.this.getContext().startActivity(localIntent);
        return;
      }
    }
  };
  protected int reviewType = 0;
  protected String shareIconUrl = null;
  protected int shopId = 0;
  private DPObject user;
  private int userId = 0;

  public PlazaFeedItem(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaFeedItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private boolean isOwnStatus()
  {
    AccountService localAccountService = ((DPActivity)getContext()).accountService();
    return (localAccountService.token() != null) && (localAccountService.id() == this.userId);
  }

  public void goToDetail(int paramInt1, int paramInt2)
  {
    int i = 0;
    if (this.isLike)
      i = 1;
    int j;
    Uri.Builder localBuilder;
    if (this.isFromUserCenter)
    {
      j = 1;
      localBuilder = Uri.parse("dianping://plazadetail").buildUpon();
      if (!this.isFromUserCenter)
        break label188;
    }
    label188: for (Object localObject = this.mainId; ; localObject = String.valueOf(this.feedId))
    {
      localObject = new Intent("android.intent.action.VIEW", localBuilder.appendQueryParameter("feedid", (String)localObject).appendQueryParameter("needsoftinput", paramInt1 + "").appendQueryParameter("islike", i + "").appendQueryParameter("isfromusercenter", j + "").appendQueryParameter("position", String.valueOf(paramInt2)).appendQueryParameter("feedtype", String.valueOf(this.feedType)).appendQueryParameter("feedauthorid", String.valueOf(this.userId)).build());
      getContext().startActivity((Intent)localObject);
      return;
      j = 0;
      break;
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.pUserProfile = ((PlazaUserProfile)findViewById(R.id.user_profile_layout));
    this.poiImageGallery = ((PoiImageGallery)findViewById(R.id.my_poi_gallery));
    this.introView = ((PlazaFeedIntroView)findViewById(R.id.intro_layout));
    this.likeAndCommentView = ((PlazaFeedCommentView)findViewById(16908318));
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
  }

  protected void rediectToEdit(int paramInt, String paramString)
  {
    try
    {
      getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.editUrl)));
      if (this.feedItemListener != null)
        this.feedItemListener.onItemEditListener(paramInt, paramString);
      return;
    }
    catch (java.lang.NullPointerException paramString)
    {
      Log.d("FeedItem Edit", "editUrl is null !");
      return;
    }
    catch (java.lang.Exception paramString)
    {
    }
  }

  protected void reportUserFeedItem(int paramInt1, int paramInt2)
  {
    Object localObject;
    Uri.Builder localBuilder;
    ArrayList localArrayList;
    if (paramInt2 == 0)
    {
      localObject = DeviceUtils.cxInfo("plaza");
      localBuilder = Uri.parse("http://m.api.dianping.com/review/reportugcfeed.bin").buildUpon();
      localArrayList = new ArrayList();
      localArrayList.add("mainid");
      if (!this.isFromUserCenter)
        break label173;
    }
    label173: for (String str = this.mainId; ; str = String.valueOf(this.feedId))
    {
      localArrayList.add(str);
      localArrayList.add("feedtype");
      localArrayList.add(String.valueOf(paramInt2));
      localArrayList.add("cx");
      localArrayList.add(localObject);
      localArrayList.add("causetype");
      localArrayList.add(String.valueOf(paramInt1));
      localObject = BasicMApiRequest.mapiPost(localBuilder.build().toString(), (String[])localArrayList.toArray(new String[localArrayList.size()]));
      DPApplication.instance().mapiService().exec((Request)localObject, new RequestHandler()
      {
        public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
        }

        public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
          Toast.makeText(DPApplication.instance(), "举报成功，感谢你为点评用户做的贡献！", 0).show();
        }
      });
      return;
      localObject = DeviceUtils.cxInfo("ugc");
      break;
    }
  }

  protected void sendReport()
  {
    if (((DPActivity)getContext()).accountService().token() == null)
    {
      ((DPActivity)getContext()).accountService().login(this);
      return;
    }
    Object localObject = new AlertDialog.Builder(getContext());
    ((AlertDialog.Builder)localObject).setTitle("举报类型");
    7 local7 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        PlazaFeedItem.this.reportUserFeedItem(paramInt + 1, PlazaFeedItem.this.feedType);
        paramDialogInterface.dismiss();
      }
    };
    ((AlertDialog.Builder)localObject).setItems(new String[] { "广告", "反动", "色情", "灌水" }, local7);
    localObject = ((AlertDialog.Builder)localObject).create();
    ((AlertDialog)localObject).setCanceledOnTouchOutside(true);
    ((AlertDialog)localObject).setInverseBackgroundForced(true);
    ((AlertDialog)localObject).show();
    GAHelper.instance().contextStatisticsEvent(getContext(), "do_report", null, "tap");
  }

  public void setFeedItemListener(OnFeedItemListener paramOnFeedItemListener)
  {
    this.feedItemListener = paramOnFeedItemListener;
  }

  public void setFeedUgc(DPObject paramDPObject, int paramInt, HashMap<Integer, Integer> paramHashMap1, HashMap<Integer, Integer> paramHashMap2)
  {
    setFeedUgc(paramDPObject, paramInt, paramHashMap1, paramHashMap2, false, 0);
  }

  public void setFeedUgc(DPObject paramDPObject, int paramInt, HashMap<Integer, Integer> paramHashMap1, HashMap<Integer, Integer> paramHashMap2, boolean paramBoolean)
  {
    this.isFromUserCenter = paramBoolean;
    setFeedUgc(paramDPObject, paramInt, paramHashMap1, paramHashMap2, false, 0);
  }

  public void setFeedUgc(DPObject paramDPObject, int paramInt1, HashMap<Integer, Integer> paramHashMap1, HashMap<Integer, Integer> paramHashMap2, boolean paramBoolean, int paramInt2)
  {
    if ((paramDPObject == null) || (paramHashMap1 == null) || (paramHashMap2 == null))
      return;
    this.feedId = paramDPObject.getInt("FeedId");
    this.mainId = paramDPObject.getString("MainId");
    this.curPosition = paramInt1;
    this.user = paramDPObject.getObject("User");
    if (this.user != null)
      this.userId = this.user.getInt("UserID");
    this.feedType = paramDPObject.getInt("FeedType");
    setGAString("feed");
    Object localObject2 = this.gaUserInfo;
    Object localObject1;
    boolean bool;
    if (this.isFromUserCenter)
    {
      localObject1 = this.mainId;
      ((GAUserInfo)localObject2).biz_id = ((String)localObject1);
      ((NovaActivity)getContext()).addGAView(this, paramInt1);
      if (!this.isFromUserCenter)
        break label521;
      localObject1 = this.pUserProfile;
      localObject2 = this.user;
      int i = paramDPObject.getInt("Star");
      if (this.feedType != 1)
        break label515;
      bool = true;
      label168: ((PlazaUserProfile)localObject1).setPlazaUserInfo((DPObject)localObject2, i, bool, paramDPObject.getBoolean("IsElite"), paramDPObject.getBoolean("IsGoodReview"), paramDPObject.getString("Time"));
      this.actionUrl = paramDPObject.getString("ActionUrl");
      this.editUrl = paramDPObject.getString("EditUrl");
      this.reviewType = paramDPObject.getInt("ReviewType");
      label233: this.pUserProfile.setGAString("profile");
      localObject2 = this.pUserProfile.getGAUserInfo();
      if (!this.isFromUserCenter)
        break label556;
      localObject1 = this.mainId;
      label265: ((GAUserInfo)localObject2).biz_id = ((String)localObject1);
      this.pics = paramDPObject.getArray("PlazaPics");
      if ((this.pics != null) && (this.pics.length != 0))
        break label568;
      this.shareIconUrl = "http://m.api.dianping.com/sc/api_res/pic/logo.png";
      this.poiImageGallery.setVisibility(8);
      if (this.shopId == 0)
      {
        paramHashMap2 = paramDPObject.getObject("Poi");
        if (paramHashMap2 != null)
          this.shopId = paramHashMap2.getInt("ShopId");
      }
      this.feedCont = paramDPObject.getString("Content");
      if (!TextUtils.isEmpty(this.feedCont))
        break label691;
      this.introView.setVisibility(8);
      paramHashMap2 = new StringBuffer();
      if (paramDPObject.getInt("LikeCount") >= 1)
        break label746;
      paramHashMap1 = "";
      label400: localObject1 = paramHashMap2.append(paramHashMap1);
      if (paramDPObject.getInt("CommentCount") >= 1)
        break label776;
      paramHashMap1 = "";
      label422: ((StringBuffer)localObject1).append(paramHashMap1);
      this.isLike = paramDPObject.getBoolean("IsLike");
      paramHashMap1 = this.likeAndCommentView;
      if (!this.isFromUserCenter)
        break label812;
    }
    label515: label521: label812: for (paramDPObject = this.mainId; ; paramDPObject = String.valueOf(this.feedId))
    {
      paramHashMap1.setCommentPraise(paramDPObject, paramHashMap2.toString(), this.isLike);
      this.likeAndCommentView.setFeedCommentListener(new PlazaFeedCommentView.FeedCommentListener(paramInt1)
      {
        public void onCommentClick()
        {
          PlazaFeedItem.this.goToDetail(1, this.val$position);
        }

        public void onCommentPraiseTextClick()
        {
          PlazaFeedItem.this.goToDetail(0, this.val$position);
        }

        public void onMoreClick()
        {
          PlazaFeedItem.this.showMoreDialog(PlazaFeedItem.this.isOwnStatus());
        }

        public void onPraiseClick()
        {
          OnFeedItemListener localOnFeedItemListener;
          int i;
          int j;
          if (PlazaFeedItem.this.feedItemListener != null)
          {
            localOnFeedItemListener = PlazaFeedItem.this.feedItemListener;
            i = PlazaFeedItem.this.userId;
            j = this.val$position;
            if (!PlazaFeedItem.this.isFromUserCenter)
              break label73;
          }
          label73: for (String str = PlazaFeedItem.this.mainId; ; str = String.valueOf(PlazaFeedItem.this.feedId))
          {
            localOnFeedItemListener.onLikeClickListener(i, j, str, PlazaFeedItem.this.isLike, PlazaFeedItem.this.feedType);
            return;
          }
        }
      });
      this.pUserProfile.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (PlazaFeedItem.this.user == null);
          do
            return;
          while (((PlazaFeedItem.this.getContext() instanceof Activity)) && (((Activity)PlazaFeedItem.this.getContext()).getIntent() != null) && (((Activity)PlazaFeedItem.this.getContext()).getIntent().getData() != null) && ("user".equals(((Activity)PlazaFeedItem.this.getContext()).getIntent().getData().getHost())));
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user").buildUpon().appendQueryParameter("userid", PlazaFeedItem.this.userId + "").build());
          PlazaFeedItem.this.getContext().startActivity(paramView);
        }
      });
      return;
      localObject1 = String.valueOf(this.feedId);
      break;
      bool = false;
      break label168;
      this.pUserProfile.setPlazaUserInfo(this.user, paramDPObject.getString("Time"), paramDPObject.getBoolean("IsTop"), paramDPObject.getBoolean("IsElite"));
      break label233;
      label556: localObject1 = String.valueOf(this.feedId);
      break label265;
      label568: this.shareIconUrl = this.pics[0].getString("PicUrl");
      this.poiImageGallery.setVisibility(0);
      this.mTopicPage = paramBoolean;
      this.mTopicId = paramInt2;
      this.shopId = this.pics[0].getInt("ShopId");
      this.poiImageGallery.setPoiImageListener(this.poiImageListener);
      localObject2 = this.poiImageGallery;
      DPObject[] arrayOfDPObject = this.pics;
      if (this.isFromUserCenter);
      for (localObject1 = this.mainId; ; localObject1 = String.valueOf(this.feedId))
      {
        ((PoiImageGallery)localObject2).setImageSource(arrayOfDPObject, paramHashMap2, paramInt1, (String)localObject1, paramBoolean, paramInt2, this.reviewType);
        break;
      }
      label691: this.introView.setVisibility(0);
      localObject1 = this.introView;
      if (this.isFromUserCenter);
      for (paramHashMap2 = this.mainId; ; paramHashMap2 = String.valueOf(this.feedId))
      {
        ((PlazaFeedIntroView)localObject1).setIntroduce(paramHashMap2, paramInt1, this.feedCont, paramHashMap1);
        break;
      }
      label746: paramHashMap1 = paramDPObject.getInt("LikeCount") + "人点赞";
      break label400;
      paramHashMap1 = " " + paramDPObject.getInt("CommentCount") + "人评论";
      break label422;
    }
  }

  protected void shareTo()
  {
    ShareHolder localShareHolder = new ShareHolder();
    Object localObject1;
    label63: label75: Object localObject3;
    StringBuilder localStringBuilder;
    if (this.feedType == 0)
    {
      localShareHolder.title = "我在大众点评 · 图趣上发现了不错的内容，快来看吧！";
      localShareHolder.imageUrl = this.shareIconUrl;
      if (this.feedType != 0)
        break label336;
      localObject2 = new StringBuilder().append("http://m.dianping.com/daren/s/detail?pid=");
      if (!this.isFromUserCenter)
        break label325;
      localObject1 = this.mainId;
      localShareHolder.webUrl = ((String)localObject1);
      localObject3 = new StringBuffer();
      localObject1 = null;
      if (this.user != null)
        localObject1 = this.user.getString("Nick");
      localStringBuilder = new StringBuilder().append("作者：");
      if (((String)localObject1).length() <= 12)
        break label366;
    }
    label325: label336: label366: for (Object localObject2 = ((String)localObject1).substring(0, 12); ; localObject2 = localObject1)
    {
      ((StringBuffer)localObject3).append((String)localObject2).append("\n").append(this.feedCont);
      localShareHolder.desc = ((StringBuffer)localObject3).toString();
      localObject3 = new StringBuilder().append(localShareHolder.title).append(" 作者: ");
      localObject2 = localObject1;
      if (((String)localObject1).length() > 12)
        localObject2 = ((String)localObject1).substring(0, 12);
      localObject2 = (String)localObject2 + ", " + this.feedCont;
      localObject1 = localObject2;
      if (((String)localObject2).length() > 110)
        localObject1 = ((String)localObject2).substring(0, 100) + "...";
      localShareHolder.weiboContent = ((String)localObject1);
      ShareUtil.gotoShareTo(getContext(), ShareType.WEB, localShareHolder, "do_share", "tap");
      GAHelper.instance().contextStatisticsEvent(getContext(), "do_share", null, "tap");
      return;
      localShareHolder.title = "我在大众点评上发现了不错的内容，快来看吧！";
      break;
      localObject1 = String.valueOf(this.feedId);
      break label63;
      localShareHolder.webUrl = ("http://m.dianping.com/review/" + this.mainId);
      break label75;
    }
  }

  protected void showDeleteDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
    localBuilder.setTitle("提示").setMessage("确定删除这条内容么?").setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        OnFeedItemListener localOnFeedItemListener;
        if (PlazaFeedItem.this.feedItemListener != null)
        {
          localOnFeedItemListener = PlazaFeedItem.this.feedItemListener;
          paramInt = PlazaFeedItem.this.curPosition;
          if (!PlazaFeedItem.this.isFromUserCenter)
            break label66;
        }
        label66: for (String str = PlazaFeedItem.this.mainId; ; str = String.valueOf(PlazaFeedItem.this.feedId))
        {
          localOnFeedItemListener.onItemDeleteListener(paramInt, str, PlazaFeedItem.this.feedType);
          paramDialogInterface.dismiss();
          return;
        }
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.dismiss();
      }
    });
    localBuilder.show();
    GAHelper.instance().contextStatisticsEvent(getContext(), "do_del", null, "tap");
  }

  protected void showMoreDialog(boolean paramBoolean)
  {
    Object localObject = new AlertDialog.Builder(getContext());
    4 local4 = new DialogInterface.OnClickListener(paramBoolean)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if (paramInt == 0)
          if (this.val$isOwn)
            if (PlazaFeedItem.this.feedType == 0)
              PlazaFeedItem.this.shareTo();
        while (true)
        {
          paramDialogInterface.dismiss();
          return;
          PlazaFeedItem.this.showDeleteDialog();
          continue;
          if (PlazaFeedItem.this.feedType == 0)
          {
            PlazaFeedItem.this.shareTo();
            continue;
          }
          PlazaFeedItem.this.sendReport();
          continue;
          if (paramInt == 1)
          {
            if (this.val$isOwn)
            {
              if (PlazaFeedItem.this.feedType == 0)
              {
                PlazaFeedItem.this.showDeleteDialog();
                continue;
              }
              if (PlazaFeedItem.this.feedType == 1)
              {
                PlazaFeedItem.this.rediectToEdit(PlazaFeedItem.this.curPosition, PlazaFeedItem.this.mainId);
                continue;
              }
              PlazaFeedItem.this.sendReport();
              continue;
            }
            PlazaFeedItem.this.sendReport();
            continue;
          }
          PlazaFeedItem.this.sendReport();
        }
      }
    };
    if (paramBoolean)
      if (this.feedType == 0)
        ((AlertDialog.Builder)localObject).setItems(new String[] { "分享", "删除", "举报" }, local4);
    while (true)
    {
      localObject = ((AlertDialog.Builder)localObject).create();
      ((AlertDialog)localObject).setCanceledOnTouchOutside(true);
      ((AlertDialog)localObject).setInverseBackgroundForced(true);
      ((AlertDialog)localObject).show();
      return;
      if (this.feedType == 1)
      {
        ((AlertDialog.Builder)localObject).setItems(new String[] { "删除", "编辑", "举报" }, local4);
        continue;
      }
      ((AlertDialog.Builder)localObject).setItems(new String[] { "删除", "举报" }, local4);
      continue;
      if (this.feedType == 0)
      {
        ((AlertDialog.Builder)localObject).setItems(new String[] { "分享", "举报" }, local4);
        continue;
      }
      ((AlertDialog.Builder)localObject).setItems(new String[] { "举报" }, local4);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaFeedItem
 * JD-Core Version:    0.6.0
 */
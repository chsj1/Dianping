package com.dianping.main.find.friendsgowhere;

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
import com.dianping.main.find.pictureplaza.OnFeedItemListener;
import com.dianping.main.find.pictureplaza.PlazaFeedCommentView;
import com.dianping.main.find.pictureplaza.PlazaFeedCommentView.FeedCommentListener;
import com.dianping.main.find.pictureplaza.PlazaFeedIntroView;
import com.dianping.main.find.pictureplaza.PlazaFeedPoiView;
import com.dianping.main.find.pictureplaza.PlazaUserProfile;
import com.dianping.main.find.pictureplaza.PoiImageGallery;
import com.dianping.main.find.pictureplaza.PoiLargeImageView.PoiImageListener;
import com.dianping.util.DeviceUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class FriendsFeedItem extends NovaLinearLayout
  implements LoginResultListener
{
  private String actionUrl = null;
  private int curPosition = 0;
  private PlazaFeedPoiView extraPoiLayout;
  private String feedCont;
  private int feedType;
  private OnFeedItemListener friendsFeedItemListener = null;
  private PlazaFeedIntroView introView;
  protected boolean isLike = false;
  private PlazaFeedCommentView likeAndCommentView;
  private String mainId = "";
  private PlazaUserProfile pUserProfile;
  private PoiImageGallery poiImageGallery;
  private String shareIconUrl = null;
  private int shopId = 0;
  private DPObject user;
  private int userId = 0;

  public FriendsFeedItem(Context paramContext)
  {
    super(paramContext);
  }

  public FriendsFeedItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void reportUserFeedItem(int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0);
    for (Object localObject = DeviceUtils.cxInfo("plaza"); ; localObject = DeviceUtils.cxInfo("ugc"))
    {
      Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/reportugcfeed.bin").buildUpon();
      ArrayList localArrayList = new ArrayList();
      localArrayList.add("mainid");
      localArrayList.add(this.mainId);
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
    }
  }

  private void sendReport()
  {
    if (((DPActivity)getContext()).accountService().token() == null)
    {
      ((DPActivity)getContext()).accountService().login(this);
      return;
    }
    Object localObject = new AlertDialog.Builder(getContext());
    ((AlertDialog.Builder)localObject).setTitle("举报类型");
    8 local8 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        FriendsFeedItem.this.reportUserFeedItem(paramInt + 1, FriendsFeedItem.this.feedType);
        paramDialogInterface.dismiss();
      }
    };
    ((AlertDialog.Builder)localObject).setItems(new String[] { "广告", "反动", "色情", "灌水" }, local8);
    localObject = ((AlertDialog.Builder)localObject).create();
    ((AlertDialog)localObject).setCanceledOnTouchOutside(true);
    ((AlertDialog)localObject).setInverseBackgroundForced(true);
    ((AlertDialog)localObject).show();
    GAHelper.instance().contextStatisticsEvent(getContext(), "do_report", null, "tap");
  }

  private void shareTo()
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.title = "我在大众点评上发现了不错的内容，快来看吧！";
    localShareHolder.imageUrl = this.shareIconUrl;
    localShareHolder.webUrl = ("http://m.dianping.com/daren/s/detail?pid=" + this.mainId);
    Object localObject3 = new StringBuffer();
    Object localObject1 = null;
    if (this.user != null)
      localObject1 = this.user.getString("Nick");
    StringBuilder localStringBuilder = new StringBuilder().append("作者：");
    if (((String)localObject1).length() > 12);
    for (Object localObject2 = ((String)localObject1).substring(0, 12); ; localObject2 = localObject1)
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
    }
  }

  private void showDeleteDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
    localBuilder.setTitle("提示").setMessage("确定删除这条内容么?").setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if (FriendsFeedItem.this.friendsFeedItemListener != null)
          FriendsFeedItem.this.friendsFeedItemListener.onItemDeleteListener(FriendsFeedItem.this.curPosition, FriendsFeedItem.this.mainId, FriendsFeedItem.this.feedType);
        paramDialogInterface.dismiss();
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

  public void goToDetail(int paramInt1, int paramInt2)
  {
    int i = 0;
    if (this.isLike)
      i = 1;
    Uri.Builder localBuilder = Uri.parse("dianping://plazadetail").buildUpon().appendQueryParameter("feedid", this.mainId).appendQueryParameter("needsoftinput", paramInt1 + "").appendQueryParameter("islike", i + "").appendQueryParameter("position", String.valueOf(paramInt2)).appendQueryParameter("feedtype", String.valueOf(this.feedType));
    if (this.user == null);
    for (Object localObject = "0"; ; localObject = String.valueOf(this.userId))
    {
      localObject = new Intent("android.intent.action.VIEW", localBuilder.appendQueryParameter("feedauthorid", (String)localObject).build());
      getContext().startActivity((Intent)localObject);
      return;
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.pUserProfile = ((PlazaUserProfile)findViewById(R.id.user_profile_layout));
    this.poiImageGallery = ((PoiImageGallery)findViewById(R.id.my_poi_gallery));
    this.introView = ((PlazaFeedIntroView)findViewById(R.id.intro_layout));
    this.likeAndCommentView = ((PlazaFeedCommentView)findViewById(16908318));
    this.extraPoiLayout = ((PlazaFeedPoiView)findViewById(R.id.extra_poi_layout));
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
  }

  public void setFriendsFeedItemListener(OnFeedItemListener paramOnFeedItemListener)
  {
    this.friendsFeedItemListener = paramOnFeedItemListener;
  }

  public void setFriendsFeedUgc(DPObject paramDPObject, int paramInt, HashMap<Integer, Integer> paramHashMap1, HashMap<Integer, Integer> paramHashMap2)
  {
    if ((paramDPObject == null) || (paramHashMap1 == null) || (paramHashMap2 == null))
      return;
    this.mainId = paramDPObject.getString("MainId");
    this.user = paramDPObject.getObject("User");
    if (this.user != null)
      this.userId = this.user.getInt("UserID");
    this.curPosition = paramInt;
    this.feedType = paramDPObject.getInt("FeedType");
    this.actionUrl = paramDPObject.getString("ActionUrl");
    setGAString("feed");
    this.gaUserInfo.biz_id = this.mainId;
    ((NovaActivity)getContext()).addGAView(this, paramInt);
    Object localObject = this.pUserProfile;
    DPObject localDPObject = this.user;
    String str = paramDPObject.getString("Time");
    int i = paramDPObject.getInt("Star");
    boolean bool;
    if (this.feedType == 1)
    {
      bool = true;
      ((PlazaUserProfile)localObject).setPlazaUserInfo(localDPObject, str, i, bool);
      this.pUserProfile.setGAString("profile");
      this.pUserProfile.getGAUserInfo().biz_id = this.mainId;
      localObject = paramDPObject.getArray("PlazaPics");
      if ((localObject != null) && (localObject.length > 0))
        this.shopId = localObject[0].getInt("ShopId");
      if ((this.shopId == 0) && (paramDPObject.getObject("Poi") != null))
        this.shopId = paramDPObject.getObject("Poi").getInt("ShopId");
      if ((this.feedType != 3) && (localObject != null) && (localObject.length != 0))
        break label522;
      this.poiImageGallery.setVisibility(8);
      this.shareIconUrl = "http://m.api.dianping.com/sc/api_res/pic/logo.png";
      label295: this.feedCont = paramDPObject.getString("Content");
      if ((this.feedType != 2) && (this.feedType != 3) && (!TextUtils.isEmpty(this.feedCont)))
        break label582;
      this.introView.setVisibility(8);
      label341: paramHashMap1 = paramDPObject.getObject("Poi");
      if (((this.feedType != 1) && (this.feedType != 3)) || (paramHashMap1 == null))
        break label610;
      this.extraPoiLayout.setVisibility(0);
      this.extraPoiLayout.setPoiInfo(paramHashMap1, 0);
      this.extraPoiLayout.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (!TextUtils.isEmpty(FriendsFeedItem.this.actionUrl));
          for (paramView = new Intent("android.intent.action.VIEW", Uri.parse(FriendsFeedItem.this.actionUrl)); ; paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + FriendsFeedItem.this.shopId)))
          {
            FriendsFeedItem.this.getContext().startActivity(paramView);
            return;
          }
        }
      });
      label401: paramHashMap2 = new StringBuffer();
      if (paramDPObject.getInt("LikeCount") >= 1)
        break label622;
      paramHashMap1 = "";
      label424: localObject = paramHashMap2.append(paramHashMap1);
      if (paramDPObject.getInt("CommentCount") >= 1)
        break label652;
    }
    label522: label652: for (paramHashMap1 = ""; ; paramHashMap1 = " " + paramDPObject.getInt("CommentCount") + "人评论")
    {
      ((StringBuffer)localObject).append(paramHashMap1);
      this.isLike = paramDPObject.getBoolean("IsLike");
      this.likeAndCommentView.setCommentPraise(this.mainId, paramHashMap2.toString(), this.isLike);
      this.likeAndCommentView.setFeedCommentListener(new PlazaFeedCommentView.FeedCommentListener(paramInt)
      {
        public void onCommentClick()
        {
          FriendsFeedItem.this.goToDetail(1, this.val$position);
        }

        public void onCommentPraiseTextClick()
        {
          FriendsFeedItem.this.goToDetail(0, this.val$position);
        }

        public void onMoreClick()
        {
          AccountService localAccountService = ((DPActivity)FriendsFeedItem.this.getContext()).accountService();
          FriendsFeedItem localFriendsFeedItem = FriendsFeedItem.this;
          if ((localAccountService.token() != null) && (localAccountService.id() == FriendsFeedItem.this.userId));
          for (boolean bool = true; ; bool = false)
          {
            localFriendsFeedItem.showMoreDialog(bool);
            return;
          }
        }

        public void onPraiseClick()
        {
          if (FriendsFeedItem.this.friendsFeedItemListener != null)
            FriendsFeedItem.this.friendsFeedItemListener.onLikeClickListener(FriendsFeedItem.this.userId, this.val$position, FriendsFeedItem.this.mainId, FriendsFeedItem.this.isLike, FriendsFeedItem.this.feedType);
        }
      });
      this.pUserProfile.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (FriendsFeedItem.this.user == null)
            return;
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user").buildUpon().appendQueryParameter("userid", FriendsFeedItem.this.userId + "").build());
          FriendsFeedItem.this.getContext().startActivity(paramView);
        }
      });
      return;
      bool = false;
      break;
      this.poiImageGallery.setVisibility(0);
      this.poiImageGallery.setPoiImageListener(new PoiLargeImageView.PoiImageListener(localObject)
      {
        public void onLargeImageClick(int paramInt, Drawable paramDrawable)
        {
          if ((paramInt < 0) || (this.val$pics == null) || (paramInt > this.val$pics.length - 1))
            return;
          Object localObject2 = this.val$pics[paramInt];
          Object localObject1 = new DPObject().edit().putInt("ShopID", ((DPObject)localObject2).getInt("ShopId")).putString("Name", ((DPObject)localObject2).getString("ShopName")).generate();
          ArrayList localArrayList = new ArrayList(this.val$pics.length);
          int i = 0;
          while (i < this.val$pics.length)
          {
            localArrayList.add(new DPObject().edit().putInt("ShopID", ((DPObject)localObject2).getInt("ShopId")).putString("Url", this.val$pics[i].getString("PicUrl")).generate());
            i += 1;
          }
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showcheckinphoto"));
          localIntent.putExtra("position", paramInt);
          localIntent.putExtra("shopname", ((DPObject)localObject2).getString("ShopName"));
          localObject2 = new ArrayList();
          ((ArrayList)localObject2).add(localObject1);
          localIntent.putParcelableArrayListExtra("arrShopObjs", (ArrayList)localObject2);
          localIntent.putParcelableArrayListExtra("pageList", localArrayList);
          if (paramDrawable != null)
          {
            localObject1 = new ByteArrayOutputStream();
            ((BitmapDrawable)paramDrawable).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject1);
            localIntent.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject1).toByteArray());
          }
          FriendsFeedItem.this.getContext().startActivity(localIntent);
        }

        public void onPoiClick(int paramInt)
        {
          if (!TextUtils.isEmpty(FriendsFeedItem.this.actionUrl));
          for (Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(FriendsFeedItem.this.actionUrl)); ; localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + FriendsFeedItem.this.shopId)))
          {
            FriendsFeedItem.this.getContext().startActivity(localIntent);
            return;
          }
        }
      });
      this.poiImageGallery.setImageSource(localObject, paramHashMap2, paramInt, this.mainId, false, 0);
      this.shareIconUrl = localObject[0].getString("PicUrl");
      break label295;
      label582: this.introView.setVisibility(0);
      this.introView.setIntroduce(this.mainId, paramInt, this.feedCont, paramHashMap1);
      break label341;
      label610: this.extraPoiLayout.setVisibility(8);
      break label401;
      label622: paramHashMap1 = paramDPObject.getInt("LikeCount") + "人点赞";
      break label424;
    }
  }

  protected void showMoreDialog(boolean paramBoolean)
  {
    Object localObject = new AlertDialog.Builder(getContext());
    5 local5 = new DialogInterface.OnClickListener(paramBoolean)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if (paramInt == 0)
          if (FriendsFeedItem.this.feedType == 0)
            FriendsFeedItem.this.shareTo();
        while (true)
        {
          paramDialogInterface.dismiss();
          return;
          if (this.val$isOwn)
          {
            FriendsFeedItem.this.showDeleteDialog();
            continue;
          }
          FriendsFeedItem.this.sendReport();
          continue;
          if (paramInt == 1)
          {
            if (FriendsFeedItem.this.feedType == 0)
            {
              if (this.val$isOwn)
              {
                FriendsFeedItem.this.showDeleteDialog();
                continue;
              }
              FriendsFeedItem.this.sendReport();
              continue;
            }
            FriendsFeedItem.this.sendReport();
            continue;
          }
          FriendsFeedItem.this.sendReport();
        }
      }
    };
    if (this.feedType == 0)
      if (paramBoolean)
        ((AlertDialog.Builder)localObject).setItems(new String[] { "分享", "删除", "举报" }, local5);
    while (true)
    {
      localObject = ((AlertDialog.Builder)localObject).create();
      ((AlertDialog)localObject).setCanceledOnTouchOutside(true);
      ((AlertDialog)localObject).setInverseBackgroundForced(true);
      ((AlertDialog)localObject).show();
      return;
      ((AlertDialog.Builder)localObject).setItems(new String[] { "分享", "举报" }, local5);
      continue;
      if (paramBoolean)
      {
        ((AlertDialog.Builder)localObject).setItems(new String[] { "删除", "举报" }, local5);
        continue;
      }
      ((AlertDialog.Builder)localObject).setItems(new String[] { "举报" }, local5);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.friendsgowhere.FriendsFeedItem
 * JD-Core Version:    0.6.0
 */
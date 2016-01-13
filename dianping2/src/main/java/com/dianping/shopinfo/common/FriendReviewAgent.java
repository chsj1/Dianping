package com.dianping.shopinfo.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.ugc.feed.model.FeedItem;
import com.dianping.ugc.feed.model.FeedUser;
import com.dianping.ugc.feed.view.FeedItemView;
import com.dianping.ugc.widget.FeedGridPhotoView.Style;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;

public class FriendReviewAgent extends ShopCellAgent
  implements RequestHandler
{
  private static final String ACTION_REFRESH_FEED_COMMENT = "com.dianping.ADDCOMMENT";
  private static final String ACTION_REFRESH_FEED_LIKE = "com.dianping.REFRESHLIKE";
  private static final String BINDPHONE = "renao:bindPhoneSuccess";
  private static final String CELL_FRIEND_REVIEW = "2900friend.here";
  private String TAG = "FriendReviewAgent";
  private DPObject friendReview;
  private DPObject[] mFeedArray;
  final BroadcastReceiver mFeedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (FriendReviewAgent.this.mReviewCell == null);
      Object localObject;
      do
        while (true)
        {
          return;
          paramContext = paramIntent.getStringExtra("feedId");
          localObject = (NovaRelativeLayout)FriendReviewAgent.this.mReviewCell.findViewWithTag(paramContext);
          if (localObject == null)
            continue;
          localObject = (FeedItemView)((NovaRelativeLayout)localObject).findViewById(R.id.review_item);
          if (localObject == null)
            continue;
          if (!"com.dianping.ADDCOMMENT".equalsIgnoreCase(paramIntent.getAction()))
            break;
          if (com.dianping.util.TextUtils.isEmpty(paramContext))
            continue;
          ((FeedItemView)localObject).addComment((FeedUser)paramIntent.getParcelableExtra("toUser"), paramIntent.getStringExtra("content"));
          return;
        }
      while ((!"com.dianping.REFRESHLIKE".equalsIgnoreCase(paramIntent.getAction())) || (com.dianping.util.TextUtils.isEmpty(paramContext)));
      int i = paramIntent.getIntExtra("likeCount", 0);
      ((FeedItemView)localObject).addLike(paramIntent.getBooleanExtra("isLiked", false), i);
    }
  };
  private LocalBroadcastManager mLocalBroadcastManager;
  private View.OnClickListener mOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      FriendReviewAgent.this.clickItem(0);
    }
  };
  private View mReviewCell;
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("renao:bindPhoneSuccess".equals(paramIntent.getAction()))
      {
        FriendReviewAgent.this.removeAllCells();
        FriendReviewAgent.this.setSharedObject("CanBeBind", Boolean.valueOf(false));
        FriendReviewAgent.this.dispatchAgentChanged("shopinfo/common_friendhere", null);
        FriendReviewAgent.this.sendRequest();
      }
    }
  };
  private MApiRequest request;

  public FriendReviewAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void clickItem(int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("dianping://review?id=");
    ((StringBuilder)localObject).append(shopId());
    if (getShop() != null)
    {
      ((StringBuilder)localObject).append("&shopname=").append(getShop().getString("Name"));
      String str = getShop().getString("BranchName");
      if (!android.text.TextUtils.isEmpty(str))
        ((StringBuilder)localObject).append("(").append(str).append(")");
      ((StringBuilder)localObject).append("&shopstatus=").append(getShop().getInt("Status"));
    }
    if (paramInt > 0)
      ((StringBuilder)localObject).append("&selectedreviewid=").append(paramInt);
    ((StringBuilder)localObject).append("&tagtype=900");
    localObject = new Intent("android.intent.action.VIEW", Uri.parse(((StringBuilder)localObject).toString()));
    ((Intent)localObject).putExtra("shop", getShop());
    getFragment().startActivity((Intent)localObject);
  }

  private View createReviewAgent()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.titleLay.setGAString("friendsreviewlist");
    if (this.mFeedArray != null)
    {
      int i = 0;
      Object localObject;
      while (i < this.mFeedArray.length)
      {
        localObject = new FeedItem(this.mFeedArray[i]);
        FeedItemView localFeedItemView = (FeedItemView)LayoutInflater.from(getContext()).inflate(R.layout.ugc_reviewlist_item, getParentView(), false);
        localFeedItemView.setTag(((FeedItem)localObject).feedId);
        localFeedItemView.enableCommentSummary(true);
        localFeedItemView.enableExpandContent(false);
        localFeedItemView.showCommentList(false);
        localFeedItemView.setContentMaxLines(2);
        localFeedItemView.setPhotoStyle(FeedGridPhotoView.Style.NORMAL);
        localFeedItemView.setMaxPhotoCount(3);
        localFeedItemView.setData((FeedItem)localObject);
        localShopinfoCommonCell.addContent(localFeedItemView, false, null);
        i += 1;
      }
      if (this.mFeedArray.length >= 2)
      {
        localObject = this.res.inflate(getContext(), R.layout.shopinfo_common_cell_footer, getParentView(), false);
        ((TextView)((View)localObject).findViewById(R.id.title)).setText("查看全部好友点评");
        ((View)localObject).setOnClickListener(this.mOnClickListener);
        localShopinfoCommonCell.addView((View)localObject);
        ((NovaRelativeLayout)localShopinfoCommonCell.findViewById(R.id.cell_footer)).setGAString("friendsreviewall");
      }
    }
    localShopinfoCommonCell.setTitle("好友点评", this.mOnClickListener);
    localShopinfoCommonCell.setSubTitle("(" + this.friendReview.getInt("RecordCount") + ")");
    return (View)localShopinfoCommonCell;
  }

  private void sendRequest()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://m.api.dianping.com/friendship/friendreviewlist.bin?");
    localStringBuilder.append("shopid=").append(shopId());
    this.request = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (!isLogined());
    do
      return;
    while ((getShop() == null) || (this.friendReview == null) || (this.mFeedArray == null) || (this.mFeedArray.length <= 0));
    this.mReviewCell = createReviewAgent();
    addCell("2900friend.here", this.mReviewCell);
    GAHelper.instance().contextStatisticsEvent(getContext(), "viewfriends", null, "view");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(getContext());
    paramBundle = new IntentFilter("renao:bindPhoneSuccess");
    getContext().registerReceiver(this.receiver, paramBundle);
    if (!isLogined())
      return;
    sendRequest();
  }

  public void onDestroy()
  {
    super.onDestroy();
    getContext().unregisterReceiver(this.receiver);
    if (this.mFeedReceiver != null)
      this.mLocalBroadcastManager.unregisterReceiver(this.mFeedReceiver);
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    Log.d(this.TAG, paramRequest.url());
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.request)
    {
      this.request = null;
      this.friendReview = ((DPObject)paramResponse.result());
      this.mFeedArray = this.friendReview.getArray("List");
      if (this.friendReview != null)
      {
        setSharedObject("CanBeBind", Boolean.valueOf(this.friendReview.getBoolean("CanBeBind")));
        Log.d(this.TAG, this.friendReview.getBoolean("CanBeBind") + "");
        dispatchAgentChanged(false);
        dispatchAgentChanged("shopinfo/common_friendhere", null);
      }
    }
  }

  public void onResume()
  {
    super.onResume();
    if (this.mFeedReceiver != null)
      this.mLocalBroadcastManager.unregisterReceiver(this.mFeedReceiver);
  }

  public void onStop()
  {
    super.onStop();
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.dianping.REFRESHLIKE");
    localIntentFilter.addAction("com.dianping.ADDCOMMENT");
    this.mLocalBroadcastManager.registerReceiver(this.mFeedReceiver, localIntentFilter);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.FriendReviewAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.main.find.friendsgowhere;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.ugc.feed.model.FeedItem;
import com.dianping.ugc.feed.model.FeedUser;
import com.dianping.ugc.feed.view.FeedItemView;
import com.dianping.ugc.feed.view.FeedItemView.OnCommentListener;
import com.dianping.ugc.model.UGCReviewItem;
import com.dianping.ugc.model.UploadPhotoData;
import com.dianping.ugc.widget.CommentInputView;
import com.dianping.ugc.widget.CommentInputView.OnCommentSendListener;
import com.dianping.ugc.widget.CommentInputView.OnViewRemovedListener;
import com.dianping.ugc.widget.FeedGridPhotoView.Style;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class FriendsFeedAgent extends AdapterCellAgent
{
  private static final String CELL_FRIEND_FEED = "030Friends.";
  private FeedListAdapter feedAdapter;
  private int mCommentViewMarginBottom;
  final BroadcastReceiver mFeedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      if ("com.dianping.REVIEWREFRESH".equals(paramContext))
      {
        paramContext = (UGCReviewItem)paramIntent.getParcelableExtra("updatedugcreviewitem");
        FriendsFeedAgent.this.feedAdapter.updateEditedReview(paramContext);
      }
      do
      {
        return;
        if ("com.dianping.REVIEWDELETE".equals(paramContext))
        {
          paramContext = paramIntent.getStringExtra("feedId");
          FriendsFeedAgent.this.feedAdapter.removeReviewById(paramContext);
          return;
        }
        if (!"com.dianping.REFRESHLIKE".equals(paramContext))
          continue;
        int i = paramIntent.getIntExtra("likeCount", 0);
        boolean bool = paramIntent.getBooleanExtra("isLiked", false);
        paramContext = paramIntent.getStringExtra("feedId");
        FriendsFeedAgent.this.feedAdapter.updateLike(paramContext, bool, i);
        return;
      }
      while (!"com.dianping.ADDCOMMENT".equals(paramContext));
      paramContext = paramIntent.getStringExtra("feedId");
      FeedUser localFeedUser = (FeedUser)paramIntent.getParcelableExtra("toUser");
      paramIntent = paramIntent.getStringExtra("content");
      FriendsFeedAgent.this.feedAdapter.appendComment(paramContext, localFeedUser, paramIntent);
    }
  };

  public FriendsFeedAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initAdapter()
  {
    this.feedAdapter = new FeedListAdapter(getContext());
    addCell("030Friends.", this.feedAdapter);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initAdapter();
    getParentView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        Rect localRect = new Rect();
        FriendsFeedAgent.this.getParentView().getWindowVisibleDisplayFrame(localRect);
        if ((FriendsFeedAgent.this.getParentView().getRootView().getHeight() > localRect.bottom) && (FriendsFeedAgent.this.mCommentViewMarginBottom != -1))
        {
          int i = FriendsFeedAgent.this.mCommentViewMarginBottom;
          int j = localRect.bottom;
          int k = ViewUtils.dip2px(FriendsFeedAgent.this.getContext(), 48.0F);
          ((FriendsGoWhereFragment)FriendsFeedAgent.this.getFragment()).getFragmentListView().smoothScrollBy(i - j + k, 200);
          FriendsFeedAgent.access$102(FriendsFeedAgent.this, -1);
        }
      }
    });
  }

  public void onDestroy()
  {
    super.onDestroy();
    try
    {
      LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mFeedReceiver);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public void onPause()
  {
    super.onPause();
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.dianping.REVIEWREFRESH");
    localIntentFilter.addAction("com.dianping.REVIEWDELETE");
    localIntentFilter.addAction("com.dianping.REFRESHLIKE");
    localIntentFilter.addAction("com.dianping.ADDCOMMENT");
    LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mFeedReceiver, localIntentFilter);
  }

  public void onResume()
  {
    super.onResume();
    try
    {
      LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mFeedReceiver);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  class FeedListAdapter extends BasicLoadAdapter
    implements FeedItemView.OnCommentListener
  {
    private HashMap<Integer, FeedItemView> mCachedView = new HashMap();
    private HashMap<Integer, FeedItem> mReviewItems = new HashMap();

    public FeedListAdapter(Context arg2)
    {
      super();
    }

    private void cacheView(int paramInt, FeedItemView paramFeedItemView)
    {
      Iterator localIterator = this.mCachedView.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if ((localEntry.getValue() != paramFeedItemView) || (paramInt == ((Integer)localEntry.getKey()).intValue()))
          continue;
        this.mCachedView.remove(localEntry.getKey());
      }
      this.mCachedView.put(Integer.valueOf(paramInt), paramFeedItemView);
    }

    private int findPositionById(String paramString)
    {
      int j = -1;
      Iterator localIterator = this.mReviewItems.entrySet().iterator();
      int i;
      while (true)
      {
        i = j;
        if (!localIterator.hasNext())
          break;
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (!((FeedItem)localEntry.getValue()).feedId.equals(paramString))
          continue;
        i = ((Integer)localEntry.getKey()).intValue();
      }
      return i;
    }

    private FeedItem updateReviewItem(FeedItem paramFeedItem, UGCReviewItem paramUGCReviewItem)
    {
      if ("0".equals(paramUGCReviewItem.averagePrice))
      {
        localObject = "";
        paramFeedItem.avgPrice = ((String)localObject);
        paramFeedItem.shopPower = Integer.parseInt(paramUGCReviewItem.star);
        paramFeedItem.content = Html.fromHtml(paramUGCReviewItem.comment);
        if (!TextUtils.isEmpty(paramUGCReviewItem.shopdishtags))
          break label121;
      }
      label121: for (Object localObject = null; ; localObject = FeedItem.buildRecommends(paramUGCReviewItem.shopdishtags.split("、")))
      {
        paramFeedItem.recommends = ((Spanned)localObject);
        localObject = new String[paramUGCReviewItem.mPhotos.size()];
        int i = 0;
        while (i < paramUGCReviewItem.mPhotos.size())
        {
          localObject[i] = ((UploadPhotoData)paramUGCReviewItem.mPhotos.get(i)).photoPath;
          i += 1;
        }
        localObject = paramUGCReviewItem.averagePrice;
        break;
      }
      paramFeedItem.thumbnailsPhotos = ((String)localObject);
      paramFeedItem.photos = ((String)localObject);
      return (FeedItem)paramFeedItem;
    }

    public void appendComment(String paramString1, FeedUser paramFeedUser, String paramString2)
    {
      int i = findPositionById(paramString1);
      if (i != -1)
      {
        paramString1 = (FeedItemView)this.mCachedView.get(Integer.valueOf(i));
        if (paramString1 != null)
          paramString1.addComment(paramFeedUser, paramString2);
      }
    }

    public MApiRequest createRequest(int paramInt)
    {
      String str1 = "";
      String str2 = "";
      Location localLocation = FriendsFeedAgent.this.location();
      if (localLocation != null)
      {
        str1 = localLocation.latitude() + "";
        str2 = localLocation.longitude() + "";
      }
      return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/friendship/friendfeedlist.bin").buildUpon().appendQueryParameter("cityid", FriendsFeedAgent.this.cityId() + "").appendQueryParameter("start", String.valueOf(paramInt)).appendQueryParameter("lng", str2).appendQueryParameter("lat", str1).toString(), CacheType.DISABLED);
    }

    protected TextView getEmptyView(String paramString1, String paramString2, ViewGroup paramViewGroup, View paramView)
    {
      paramString1 = null;
      if (paramView == null);
      while (true)
      {
        paramString2 = paramString1;
        if (paramString1 == null)
        {
          paramString2 = (TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.simple_list_item_18, paramViewGroup, false);
          paramString2.setTag(EMPTY);
          paramString2.setGravity(1);
          paramString2.setPadding(0, ViewUtils.dip2px(FriendsFeedAgent.this.getContext(), 50.0F), 0, 0);
          paramString2.setText("暂未留下任何内容");
          paramString2.setTextColor(FriendsFeedAgent.this.getResources().getColor(R.color.light_gray));
          paramString2.setTextSize(0, FriendsFeedAgent.this.getResources().getDimensionPixelSize(R.dimen.text_medium));
        }
        return paramString2;
        if (paramView.getTag() != EMPTY)
          continue;
        paramString1 = (TextView)paramView;
      }
    }

    public int getViewTypeCount()
    {
      return 5;
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof FeedItemView));
      for (paramView = (FeedItemView)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
        {
          localObject = (FeedItemView)FriendsFeedAgent.this.res.inflate(FriendsFeedAgent.this.getContext(), R.layout.ugc_reviewlist_item, paramViewGroup, false);
          ((FeedItemView)localObject).setPhotoStyle(FeedGridPhotoView.Style.SQUARED);
        }
        paramViewGroup = (FeedItem)this.mReviewItems.get(Integer.valueOf(paramInt));
        paramView = paramViewGroup;
        if (paramViewGroup == null)
        {
          paramView = new FeedItem(paramDPObject);
          this.mReviewItems.put(Integer.valueOf(paramInt), paramView);
        }
        ((FeedItemView)localObject).setOnCommentListener(this);
        ((FeedItemView)localObject).setIndex(paramInt);
        ((FeedItemView)localObject).setData(paramView);
        ((FeedItemView)localObject).enableExtraOpts(false);
        cacheView(paramInt, (FeedItemView)localObject);
        return localObject;
      }
    }

    public void onComment(int paramInt, View paramView, String paramString1, String paramString2, String paramString3, FeedUser paramFeedUser)
    {
      Object localObject = new int[2];
      paramView.getLocationOnScreen(localObject);
      FriendsFeedAgent.access$102(FriendsFeedAgent.this, localObject[1] + paramView.getHeight());
      localObject = new CommentInputView(FriendsFeedAgent.this.getContext());
      ((CommentInputView)localObject).setEnableRemoveIsSelf(true);
      if (paramFeedUser == null);
      for (paramView = ""; ; paramView = "回复" + paramFeedUser.username)
      {
        ((CommentInputView)localObject).setCommentInputHint(paramView);
        ((CommentInputView)localObject).setOnCommentInputListener(new CommentInputView.OnCommentSendListener(paramInt, paramString1, paramString2, paramString3, paramFeedUser)
        {
          public void onCommentSend(String paramString)
          {
            FeedItemView localFeedItemView = (FeedItemView)FriendsFeedAgent.FeedListAdapter.this.mCachedView.get(Integer.valueOf(this.val$position));
            if (localFeedItemView != null)
              localFeedItemView.addComment(this.val$feedId, this.val$commentId, this.val$feedType, this.val$toUser, paramString);
          }
        });
        ((CommentInputView)localObject).setOnViewRemovedListener(new CommentInputView.OnViewRemovedListener()
        {
          public void OnViewRemoved()
          {
            ((FrameLayout.LayoutParams)((FriendsGoWhereFragment)FriendsFeedAgent.this.getFragment()).getFragmentListView().getLayoutParams()).bottomMargin = 0;
          }
        });
        ((FrameLayout.LayoutParams)((FriendsGoWhereFragment)FriendsFeedAgent.this.getFragment()).getFragmentListView().getLayoutParams()).bottomMargin = ViewUtils.dip2px(FriendsFeedAgent.this.getContext(), 48.0F);
        FriendsFeedAgent.this.getParentView().addView((View)localObject);
        return;
      }
    }

    public void removeReviewById(String paramString)
    {
      int j = -1;
      Iterator localIterator = this.mReviewItems.entrySet().iterator();
      int i;
      while (true)
      {
        i = j;
        if (!localIterator.hasNext())
          break;
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (!((FeedItem)localEntry.getValue()).feedId.equals(paramString))
          continue;
        i = ((Integer)localEntry.getKey()).intValue();
      }
      if (i != -1)
      {
        this.mReviewItems.clear();
        this.mCachedView.clear();
        this.mData.remove(i);
        notifyDataSetChanged();
      }
    }

    public void updateEditedReview(UGCReviewItem paramUGCReviewItem)
    {
      FeedItemView localFeedItemView = null;
      int j = 0;
      Iterator localIterator = this.mReviewItems.entrySet().iterator();
      Object localObject;
      int i;
      while (true)
      {
        localObject = localFeedItemView;
        i = j;
        if (!localIterator.hasNext())
          break;
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (!((FeedItem)localEntry.getValue()).feedId.equals(String.valueOf(paramUGCReviewItem.reviewId)))
          continue;
        localObject = (FeedItem)localEntry.getValue();
        i = ((Integer)localEntry.getKey()).intValue();
      }
      localFeedItemView = (FeedItemView)this.mCachedView.get(Integer.valueOf(i));
      if ((localFeedItemView != null) && (localObject != null))
        localFeedItemView.update(updateReviewItem((FeedItem)localObject, paramUGCReviewItem));
    }

    public void updateLike(String paramString, boolean paramBoolean, int paramInt)
    {
      int i = findPositionById(paramString);
      if (i != -1)
      {
        paramString = (FeedItemView)this.mCachedView.get(Integer.valueOf(i));
        if (paramString != null)
          paramString.addLike(paramBoolean, paramInt);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.friendsgowhere.FriendsFeedAgent
 * JD-Core Version:    0.6.0
 */
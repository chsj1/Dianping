package com.dianping.hotel.ugc;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.NovaFragment;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.base.widget.tagflow.FlowLayout;
import com.dianping.base.widget.tagflow.TagAdapter;
import com.dianping.base.widget.tagflow.TagFlowLayout;
import com.dianping.base.widget.tagflow.TagFlowLayout.OnItemCheckedStateChangedListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.UserProfile;
import com.dianping.ugc.model.UGCReviewItem;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class ReviewListFragment extends NovaFragment
  implements AccountListener
{
  static final DPObject DEFAULT_SORT;
  static final DPObject DEFAULT_STAR;
  private static final int NOTIFICATION_ID = "ReplyReviewAgent_Notification".hashCode();
  public static final String TAG = "ReviewListFragment";
  static final DPObject TIME_SORT;
  private static final String URL_ADD_COMMENT = "http://m.api.dianping.com/review/addugccomment.bin";
  private boolean isHotelReview;
  private ReviewListAdapter mAdapter;
  private View mCommentView;
  DPObject mCurrentSort = DEFAULT_SORT;
  DPObject mCurrentStar = DEFAULT_STAR;
  private String mDealId;
  private EditText mEditTextView;
  private int mFilterId;
  private boolean mHeaderAdded = false;
  private int mHotelLabelId;
  private String mKeyword;
  int mLastSelectedTagPosition = 0;
  private boolean mNeedFilter = true;
  private final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.REVIEWREFRESH".equals(paramIntent.getAction()))
      {
        paramContext = (UGCReviewItem)paramIntent.getParcelableExtra("updatedugcreviewitem");
        ReviewListFragment.this.mAdapter.updateEditedReview(paramContext);
      }
    }
  };
  private ListView mReviewList;
  private LinearLayout mReviewListHeader;
  private String mSelectedReviewId;
  private int mShopId;
  private int mUserId = 0;

  static
  {
    DEFAULT_SORT = new DPObject("ReviewFilter").edit().putInt("ID", 400).putString("Name", "默认排序").generate();
    TIME_SORT = new DPObject("ReviewFilter").edit().putInt("ID", 300).putString("Name", "时间倒序").generate();
    DEFAULT_STAR = new DPObject("ReviewFilter").edit().putInt("ID", 400).putString("Name", "全部星级").generate();
  }

  private void showCommentView(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this.mReviewList.setSelection(this.mReviewList.getHeaderViewsCount() + paramInt);
    KeyboardUtils.popupKeyboard(this.mEditTextView);
    this.mEditTextView.setHint(getContext().getResources().getString(R.string.ugc_review_comment_tip) + paramString4);
    this.mCommentView.setVisibility(0);
    this.mCommentView.findViewById(R.id.issue_comment).setOnClickListener(new View.OnClickListener(paramInt, paramString3, paramString4, paramString2, paramString1)
    {
      public void onClick(View paramView)
      {
        ReviewListFragment.this.updateNotification(0);
        String str = ReviewListFragment.this.mEditTextView.getText().toString();
        if (TextUtils.isEmpty(str))
          Toast.makeText(DPApplication.instance(), ReviewListFragment.this.getContext().getResources().getString(R.string.ugc_review_comment_empty_tip), 0).show();
        while (true)
        {
          paramView = new ArrayList();
          paramView.add("originuserid");
          paramView.add(String.valueOf(ReviewListFragment.this.accountService().id()));
          paramView.add("noteid");
          paramView.add(this.val$noteId);
          paramView.add("notetype");
          paramView.add(this.val$noteType);
          paramView.add("feedtype");
          paramView.add("1");
          paramView.add("mainid");
          paramView.add(this.val$reviewId);
          paramView.add("content");
          paramView.add(str);
          paramView = (BasicMApiRequest)BasicMApiRequest.mapiPost("http://m.api.dianping.com/review/addugccomment.bin", (String[])paramView.toArray(new String[paramView.size()]));
          ReviewListFragment.this.mapiService().exec(paramView, new RequestHandler()
          {
            public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
            {
              ReviewListFragment.this.updateNotification(2);
            }

            public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
            {
              ReviewListFragment.this.updateNotification(1);
            }
          });
          return;
          ReviewListFragment.this.hideCommentView();
          ReviewListFragment.this.mEditTextView.setText("");
          paramView = null;
          UserProfile localUserProfile = ReviewListFragment.this.getAccount();
          if (localUserProfile != null)
            paramView = localUserProfile.nickName();
          ReviewListFragment.this.mAdapter.appendComment(this.val$position, this.val$noteType, paramView, this.val$repliedUsername, str);
        }
      }
    });
  }

  private void updateNotification(int paramInt)
  {
    NotificationManager localNotificationManager = (NotificationManager)((Activity)getContext()).getSystemService("notification");
    if (localNotificationManager == null)
      return;
    PendingIntent localPendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(), 0);
    Object localObject;
    switch (paramInt)
    {
    default:
      localObject = "";
      label72: localObject = new NotificationCompat.Builder(getContext()).setContentTitle("大众点评").setContentText((CharSequence)localObject).setContentIntent(localPendingIntent).build();
      ((Notification)localObject).flags = 16;
      ((Notification)localObject).when = System.currentTimeMillis();
      ((Notification)localObject).sound = null;
      ((Notification)localObject).vibrate = null;
      switch (paramInt)
      {
      default:
      case 0:
      case 1:
      case 2:
      }
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      localNotificationManager.notify(NOTIFICATION_ID, (Notification)localObject);
      if (paramInt != 1)
        break;
      new Handler().postDelayed(new Runnable(localNotificationManager)
      {
        public void run()
        {
          if (this.val$nm != null)
            this.val$nm.cancel(ReviewListFragment.NOTIFICATION_ID);
        }
      }
      , 3000L);
      return;
      localObject = "正在提交评论";
      break label72;
      localObject = "评论提交成功";
      break label72;
      localObject = "评论提交失败";
      break label72;
      localNotificationManager.cancel(NOTIFICATION_ID);
      ((Notification)localObject).icon = R.drawable.upload_notification_anim;
      ((Notification)localObject).flags |= 2;
      continue;
      localNotificationManager.cancel(NOTIFICATION_ID);
      ((Notification)localObject).icon = R.drawable.upload_success;
      continue;
      localNotificationManager.cancel(NOTIFICATION_ID);
      ((Notification)localObject).icon = R.drawable.upload_failed;
    }
  }

  public boolean hideCommentView()
  {
    if (this.mCommentView.getVisibility() != 0)
      return false;
    KeyboardUtils.hideKeyboard(this.mEditTextView);
    this.mCommentView.setVisibility(8);
    ((ViewGroup)this.mCommentView.getParent()).setOnTouchListener(null);
    return true;
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    reset();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    String str = getActivity().getIntent().getData().getHost();
    if (("hotelreview".equals(str)) || ("hoteltuanreview".equals(str)) || ("hoteloverseareview".equals(str)));
    for (boolean bool = true; ; bool = false)
    {
      this.isHotelReview = bool;
      if ("hoteloverseareview".equals(str))
        setNeedFilter(false);
      if (paramBundle != null)
      {
        this.mShopId = paramBundle.getInt("shopId");
        this.mUserId = paramBundle.getInt("userId");
        this.mKeyword = paramBundle.getString("keyword");
        this.mDealId = paramBundle.getString("dealId");
        this.mFilterId = paramBundle.getInt("filterId");
        this.mHotelLabelId = paramBundle.getInt("hotelLabelId");
      }
      this.mAdapter = new ReviewListAdapter(getActivity());
      this.mReviewList.setAdapter(this.mAdapter);
      return;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    accountService().addListener(this);
    paramBundle = new IntentFilter("com.dianping.REVIEWREFRESH");
    LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mReceiver, paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramBundle = new FrameLayout(paramLayoutInflater.getContext());
    paramBundle.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
    this.mReviewList = new ListView(paramLayoutInflater.getContext());
    this.mReviewList.setCacheColorHint(getResources().getColor(R.color.transparent));
    this.mReviewList.setSelector(new ColorDrawable(getResources().getColor(R.color.transparent)));
    this.mReviewList.setHeaderDividersEnabled(false);
    this.mReviewListHeader = new LinearLayout(paramLayoutInflater.getContext());
    this.mReviewListHeader.setGravity(17);
    this.mReviewListHeader.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
    this.mReviewList.addHeaderView(this.mReviewListHeader);
    paramBundle.addView(this.mReviewList, new FrameLayout.LayoutParams(-1, -1));
    this.mCommentView = paramLayoutInflater.inflate(R.layout.hotel_temp_ugc_reviewlist_add_reply, paramViewGroup, false);
    this.mEditTextView = ((EditText)this.mCommentView.findViewById(R.id.review_comment_edit));
    paramLayoutInflater = this.mCommentView.findViewById(R.id.review_comment_outside);
    paramLayoutInflater.setSoundEffectsEnabled(false);
    paramLayoutInflater.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ReviewListFragment.this.hideCommentView();
      }
    });
    this.mCommentView.setVisibility(8);
    paramBundle.addView(this.mCommentView, new FrameLayout.LayoutParams(-1, -2, 80));
    return paramBundle;
  }

  public void onDestroy()
  {
    super.onDestroy();
    accountService().removeListener(this);
    LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mReceiver);
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("shopId", this.mShopId);
    paramBundle.putInt("userId", this.mUserId);
    paramBundle.putString("keyword", this.mKeyword);
    paramBundle.putString("dealId", this.mDealId);
    paramBundle.putInt("filterId", this.mFilterId);
    paramBundle.putInt("hotelLabelId", this.mHotelLabelId);
  }

  public void reset()
  {
    this.mAdapter.reset();
  }

  public void setDealId(String paramString)
  {
    this.mDealId = paramString;
  }

  public void setFilterId(int paramInt)
  {
    this.mFilterId = paramInt;
  }

  public void setHotelLabelId(int paramInt)
  {
    this.mHotelLabelId = paramInt;
  }

  public void setKeyword(String paramString)
  {
    this.mKeyword = paramString;
  }

  public void setNeedFilter(boolean paramBoolean)
  {
    this.mNeedFilter = paramBoolean;
    if (this.mReviewListHeader != null)
      this.mReviewListHeader.removeAllViews();
    this.mHeaderAdded = false;
  }

  public void setSelectedReviewId(String paramString)
  {
    this.mSelectedReviewId = paramString;
  }

  public void setShopId(int paramInt)
  {
    this.mShopId = paramInt;
  }

  public void setUserId(int paramInt)
  {
    this.mUserId = paramInt;
  }

  private class ReviewListAdapter extends BasicLoadAdapter
    implements ReviewItemView.OnCommentListener, ReviewItemView.OnDeleteOwnReviewListener, ReviewItemView.OnExpandFriendsListener, ReviewItemView.OnNotifyUpdateListener
  {
    private final int MAX_FRIENDS_REVIEWS = 1;
    private boolean loadFirstPage = false;
    private HashMap<Integer, ReviewItemView> mCachedView = new HashMap();
    private int mFriendReviewCount = 0;
    private ArrayList<DPObject> mHiddenFriendReviews = new ArrayList();
    private int mNextFriendReviewPosition = 0;
    private int mOwnReviewCount = 0;
    private HashMap<Integer, ReviewItem> mReviewItems = new HashMap();

    public ReviewListAdapter(Context arg2)
    {
      super();
    }

    private void cacheView(int paramInt, ReviewItemView paramReviewItemView)
    {
      Iterator localIterator = this.mCachedView.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if ((localEntry.getValue() != paramReviewItemView) || (paramInt == ((Integer)localEntry.getKey()).intValue()))
          continue;
        this.mCachedView.remove(localEntry.getKey());
      }
      this.mCachedView.put(Integer.valueOf(paramInt), paramReviewItemView);
    }

    private void expandFriendReviews()
    {
      int j = this.mHiddenFriendReviews.size();
      if (j > 0)
      {
        this.mReviewItems.clear();
        this.mCachedView.clear();
        this.mData.set(this.mNextFriendReviewPosition - 1, ((DPObject)this.mData.get(this.mNextFriendReviewPosition - 1)).edit().putInt("FriendCount", 0).generate());
        int i = 0;
        while (i < j)
        {
          this.mData.add(this.mNextFriendReviewPosition + i, this.mHiddenFriendReviews.get(i));
          this.mNextFriendReviewPosition += 1;
          i += 1;
        }
        this.mHiddenFriendReviews.clear();
        notifyDataSetChanged();
      }
    }

    private void refreshReviewByPosition(int paramInt, String paramString)
    {
      Object localObject1 = (ReviewItemView)this.mCachedView.get(Integer.valueOf(paramInt));
      if (localObject1 == null)
      {
        Log.d("ReviewListFragment", "refreshReviewByPosition itemview is empty, id: " + paramString + ", position: " + paramInt);
        return;
      }
      Object localObject2 = (ReviewItem)this.mReviewItems.get(Integer.valueOf(paramInt));
      if ((localObject1 != null) && (localObject2 != null))
      {
        if ((paramString.equals(((ReviewItemView)localObject1).mID)) && (paramString.equals(((ReviewItem)localObject2).ID)))
        {
          Log.d("ReviewListFragment", "refreshReviewByPosition id matches, id: " + paramString + ", position: " + paramInt);
          ((ReviewItemView)localObject1).setData((ReviewItem)localObject2);
          return;
        }
        localObject1 = this.mReviewItems.entrySet().iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject2 = (Map.Entry)((Iterator)localObject1).next();
          Log.d("ReviewListFragment", "refreshReviewByPosition id does not match, id: " + paramString + "entry.ID: " + ((ReviewItem)((Map.Entry)localObject2).getValue()).ID + ", position: " + paramInt + ", entry.position: " + ((Map.Entry)localObject2).getKey());
          if (!((ReviewItem)((Map.Entry)localObject2).getValue()).ID.equals(paramString))
            continue;
          int i = ((Integer)((Map.Entry)localObject2).getKey()).intValue();
          ReviewItemView localReviewItemView = (ReviewItemView)this.mCachedView.get(Integer.valueOf(i));
          if (localReviewItemView == null)
            continue;
          Log.d("ReviewListFragment", "refreshReviewByPosition id does not match, id: " + paramString + ", position: " + paramInt);
          localReviewItemView.setData((ReviewItem)((Map.Entry)localObject2).getValue());
          return;
        }
      }
      Log.d("ReviewListFragment", "refreshReviewByPosition refresh all, id: " + paramString + ", position: " + paramInt);
      notifyDataSetChanged();
    }

    private void removeOwnReviewByPosition(int paramInt)
    {
      this.mReviewItems.clear();
      this.mCachedView.clear();
      this.mData.remove(paramInt);
      this.mOwnReviewCount -= 1;
      if (this.mOwnReviewCount == 0)
        this.mData.remove(paramInt - 1);
      if ((this.mOwnReviewCount == 0) && (this.mFriendReviewCount == 0) && (this.mData.size() > 0))
        this.mData.remove(0);
      if (this.mData.size() == 0)
        reset();
      while (true)
      {
        LocalBroadcastManager.getInstance(DPApplication.instance()).sendBroadcast(new Intent("com.dianping.REVIEWDELETE"));
        ReviewListFragment.this.getContext().sendBroadcast(new Intent("com.dianping.action.HONEY_CHANGED"));
        return;
        notifyDataSetChanged();
      }
    }

    public void appendComment(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4)
    {
      ReviewItemView localReviewItemView = (ReviewItemView)this.mCachedView.get(Integer.valueOf(paramInt));
      if (localReviewItemView != null)
        localReviewItemView.addComment(paramString1, paramString2, paramString3, paramString4);
    }

    public MApiRequest createRequest(int paramInt)
    {
      Uri.Builder localBuilder;
      if (ReviewListFragment.this.isHotelReview)
      {
        localBuilder = Uri.parse("http://m.api.dianping.com/hotel/hotelreview.hotel").buildUpon();
        localBuilder.appendQueryParameter("shopid", String.valueOf(ReviewListFragment.this.mShopId));
        if (ReviewListFragment.this.mHotelLabelId > 0)
          localBuilder.appendQueryParameter("labelid", String.valueOf(ReviewListFragment.this.mHotelLabelId));
        localBuilder.appendQueryParameter("filterid", String.valueOf(ReviewListFragment.this.mFilterId));
        localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
        localBuilder.appendQueryParameter("tab", String.valueOf(1));
        if ((ReviewListFragment.this.getAccount() != null) && (!TextUtils.isEmpty(ReviewListFragment.this.accountService().token())))
          localBuilder.appendQueryParameter("token", ReviewListFragment.this.accountService().token());
        if (ReviewListFragment.this.mNeedFilter);
        for (localObject = "1"; ; localObject = "0")
        {
          localBuilder.appendQueryParameter("needfilter", (String)localObject);
          return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
        }
      }
      if (ReviewListFragment.this.mUserId == 0)
      {
        localBuilder = Uri.parse("http://m.api.dianping.com/review.bin").buildUpon();
        localBuilder.appendQueryParameter("shopid", String.valueOf(ReviewListFragment.this.mShopId));
        localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
        if ((ReviewListFragment.this.getAccount() != null) && (!TextUtils.isEmpty(ReviewListFragment.this.accountService().token())))
          localBuilder.appendQueryParameter("token", ReviewListFragment.this.accountService().token());
        if (ReviewListFragment.this.mDealId != null)
          localBuilder.appendQueryParameter("dealgroupid", ReviewListFragment.this.mDealId);
        localBuilder.appendQueryParameter("filterid", String.valueOf(ReviewListFragment.this.mFilterId));
        if (ReviewListFragment.this.mKeyword != null)
          localBuilder.appendQueryParameter("keyword", ReviewListFragment.this.mKeyword);
        if (ReviewListFragment.this.mNeedFilter);
        for (localObject = "1"; ; localObject = "0")
        {
          localBuilder.appendQueryParameter("needfilter", (String)localObject);
          return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
        }
      }
      Object localObject = Uri.parse("http://m.api.dianping.com/addtionalreview.bin").buildUpon();
      ((Uri.Builder)localObject).appendQueryParameter("shopid", String.valueOf(ReviewListFragment.this.mShopId));
      ((Uri.Builder)localObject).appendQueryParameter("start", String.valueOf(paramInt));
      ((Uri.Builder)localObject).appendQueryParameter("userid", String.valueOf(ReviewListFragment.this.mUserId));
      return (MApiRequest)BasicMApiRequest.mapiGet(((Uri.Builder)localObject).toString(), CacheType.DISABLED);
    }

    protected String emptyMessage()
    {
      return ReviewListFragment.this.getContext().getString(R.string.ugc_review_empty);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      long l1 = System.currentTimeMillis();
      DPObject localDPObject = (DPObject)getItem(paramInt);
      boolean bool = localDPObject.isClass("Title");
      if (((!bool) && ((paramView instanceof ReviewItemView))) || ((bool) && ((paramView instanceof ReviewTitleView))))
      {
        paramDPObject = paramView;
        if (!(paramDPObject instanceof ReviewItemView))
          break label268;
        paramViewGroup = (ReviewItem)this.mReviewItems.get(Integer.valueOf(paramInt));
        paramView = paramViewGroup;
        if (paramViewGroup == null)
        {
          paramView = new ReviewItem(localDPObject);
          paramView.setKeyword(ReviewListFragment.this.mKeyword);
          this.mReviewItems.put(Integer.valueOf(paramInt), paramView);
          Log.d("ReviewListFragment", String.valueOf(this.mReviewItems.size()));
        }
        ((ReviewItemView)paramDPObject).setOnCommentListener(this);
        ((ReviewItemView)paramDPObject).setOnDeleteOwnReviewListener(this);
        ((ReviewItemView)paramDPObject).setOnExpandFriendsListener(this);
        ((ReviewItemView)paramDPObject).setOnNotifyUpdateListener(this);
        ((ReviewItemView)paramDPObject).setIndex(paramInt);
        ((ReviewItemView)paramDPObject).setData(paramView);
        cacheView(paramInt, (ReviewItemView)paramDPObject);
      }
      while (true)
      {
        long l2 = System.currentTimeMillis();
        Log.d("ReviewListFragment", "all cost time=" + (l2 - l1));
        return paramDPObject;
        if (bool)
        {
          paramDPObject = new ReviewTitleView(ReviewListFragment.this.getContext());
          break;
        }
        paramDPObject = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.hotel_temp_ugc_reviewlist_item, paramViewGroup, false);
        break;
        label268: if (!(paramDPObject instanceof ReviewTitleView))
          continue;
        paramView = new ReviewItem(localDPObject);
        ((ReviewTitleView)paramDPObject).setData(paramView);
      }
    }

    public void onComment(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4)
    {
      ReviewListFragment.this.showCommentView(paramInt, paramString1, paramString2, paramString3, paramString4);
    }

    public void onDelete(int paramInt)
    {
      removeOwnReviewByPosition(paramInt);
    }

    public void onExpand()
    {
      expandFriendReviews();
    }

    public void onNotify(int paramInt, String paramString)
    {
      refreshReviewByPosition(paramInt, paramString);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      Object localObject1;
      Object localObject2;
      int j;
      int k;
      int i;
      if ((!this.loadFirstPage) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        localObject1 = (DPObject)paramMApiResponse.result();
        localObject2 = ((DPObject)localObject1).getObject("OwnerReview");
        DPObject localDPObject;
        if (localObject2 != null)
        {
          localDPObject = new DPObject("Title").edit().putString("Title", ReviewListFragment.this.getString(R.string.ugc_review_own_review)).generate();
          this.mData.add(localDPObject);
          this.mData.add(((DPObject)localObject2).edit().putInt("BelongType", 1).generate());
          this.mOwnReviewCount = 1;
          this.mNextFriendReviewPosition += 2;
        }
        localObject2 = ((DPObject)localObject1).getArray("FriendReviewList");
        if ((localObject2 != null) && (localObject2.length > 0))
        {
          localDPObject = new DPObject("Title").edit().putString("Title", ReviewListFragment.this.getString(R.string.ugc_review_friend_review)).generate();
          this.mData.add(localDPObject);
          j = Math.min(1, localObject2.length);
          k = localObject2.length - j;
          i = 0;
          if (i < j)
          {
            if ((i == j - 1) && (k > 0))
              this.mData.add(localObject2[i].edit().putInt("FriendCount", k).putInt("BelongType", 2).generate());
            while (true)
            {
              i += 1;
              break;
              this.mData.add(localObject2[i].edit().putInt("BelongType", 2).generate());
            }
          }
          if (k > 0)
          {
            i = 0;
            while (i < k)
            {
              this.mHiddenFriendReviews.add(localObject2[(i + j)]);
              i += 1;
            }
          }
          this.mFriendReviewCount = localObject2.length;
          this.mNextFriendReviewPosition += j + 1;
        }
        localObject1 = ((DPObject)localObject1).getArray("List");
        if ((localObject1 == null) || (localObject1.length <= 0))
          break label1067;
        i = 1;
        if (((this.mOwnReviewCount > 0) || (this.mFriendReviewCount > 0)) && (i != 0))
        {
          localObject1 = new DPObject("Title").edit().putString("Title", ReviewListFragment.this.getString(R.string.ugc_review_review)).generate();
          this.mData.add(localObject1);
        }
      }
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
      if ((!this.loadFirstPage) && (ReviewListFragment.this.mSelectedReviewId != null))
      {
        i = 0;
        paramMApiRequest = this.mData.iterator();
        label502: if (paramMApiRequest.hasNext())
        {
          localObject1 = (DPObject)paramMApiRequest.next();
          if (((DPObject)localObject1).isClass("Title"))
            break label1073;
          localObject1 = new ReviewItem((DPObject)localObject1);
          this.mReviewItems.put(Integer.valueOf(i), localObject1);
          if (!ReviewListFragment.this.mSelectedReviewId.equals(((ReviewItem)localObject1).reviewId))
            break label1073;
          ReviewListFragment.this.mReviewList.setSelection(ReviewListFragment.this.mReviewList.getHeaderViewsCount() + i);
        }
      }
      if ((!this.loadFirstPage) && (!ReviewListFragment.this.mHeaderAdded))
      {
        ReviewListFragment.access$1102(ReviewListFragment.this, true);
        if (ReviewListFragment.this.getContext() != null)
        {
          localObject1 = null;
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          paramMApiResponse = paramMApiRequest.getArray("ReviewAbstractList");
          localObject2 = paramMApiRequest.getArray("ReviewFilterNavs");
          if ((paramMApiResponse == null) || (paramMApiResponse.length <= 0))
            break label1169;
          paramMApiRequest = new LinearLayout(ReviewListFragment.this.getContext());
          paramMApiRequest.setBackgroundResource(R.color.gray_background);
          paramMApiRequest.setPadding(0, ViewUtils.dip2px(ReviewListFragment.this.getContext(), 10.0F), 0, ViewUtils.dip2px(ReviewListFragment.this.getContext(), 10.0F));
          paramMApiRequest.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
          localObject1 = new TagFlowLayout(ReviewListFragment.this.getContext());
          ((TagFlowLayout)localObject1).setBackgroundResource(R.drawable.ugc_tag_background_line);
          ((TagFlowLayout)localObject1).setPadding(ViewUtils.dip2px(ReviewListFragment.this.getContext(), 3.0F), ViewUtils.dip2px(ReviewListFragment.this.getContext(), 6.0F), ViewUtils.dip2px(ReviewListFragment.this.getContext(), 3.0F), ViewUtils.dip2px(ReviewListFragment.this.getContext(), 6.0F));
          ((TagFlowLayout)localObject1).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
          ((TagFlowLayout)localObject1).setNumLine(3);
          ((TagFlowLayout)localObject1).setChoiceMode(1);
          paramMApiRequest.addView((View)localObject1);
          int m = -1;
          i = 0;
          int n = paramMApiResponse.length;
          k = 0;
          label856: j = m;
          if (k < n)
          {
            localObject2 = paramMApiResponse[k];
            Log.d("ReviewListFragment", "tag name=" + ((DPObject)localObject2).getString("Name") + " id=" + ((DPObject)localObject2).getInt("RankType") + " hotel id=" + ((DPObject)localObject2).getInt("HotelLabelId"));
            j = ((DPObject)localObject2).getInt("RankType");
            if (ReviewListFragment.this.mHotelLabelId <= 0)
              break label1082;
            if ((((DPObject)localObject2).getInt("HotelLabelId") != ReviewListFragment.this.mHotelLabelId) || (j != ReviewListFragment.this.mFilterId))
              break label1154;
            j = i;
          }
          label993: ((TagFlowLayout)localObject1).setAdapter(new TagAdapter(paramMApiResponse)
          {
            public View getView(FlowLayout paramFlowLayout, int paramInt, DPObject paramDPObject)
            {
              paramFlowLayout = (TextView)LayoutInflater.from(ReviewListFragment.this.getContext()).inflate(R.layout.common_tag_item, paramFlowLayout, false);
              Object localObject = paramDPObject.getString("Name");
              paramInt = paramDPObject.getInt("Count");
              localObject = new StringBuilder((String)localObject);
              if (paramInt > 0)
                ((StringBuilder)localObject).append("(").append(paramInt).append(")");
              paramFlowLayout.setText(((StringBuilder)localObject).toString());
              paramFlowLayout.setTag(paramDPObject);
              return (View)paramFlowLayout;
            }
          });
          if (j != -1)
            ((TagFlowLayout)localObject1).setItemChecked(j);
          ReviewListFragment.this.mLastSelectedTagPosition = j;
          ((TagFlowLayout)localObject1).setOnItemCheckedStateChangedListener(new TagFlowLayout.OnItemCheckedStateChangedListener()
          {
            public void onItemCheckedStateChanged(View paramView, int paramInt, boolean paramBoolean)
            {
              if ((paramView.getTag() instanceof DPObject))
              {
                if ((ReviewListFragment.this.mLastSelectedTagPosition != paramInt) && (paramBoolean))
                {
                  paramView = (DPObject)paramView.getTag();
                  String str = paramView.getString("Name") + "_" + paramView.getInt("Affection");
                  ReviewListFragment.this.setHotelLabelId(paramView.getInt("HotelLabelId"));
                  ReviewListFragment.this.setFilterId(paramView.getInt("RankType"));
                  ReviewListFragment.this.setKeyword(str);
                  ReviewListFragment.ReviewListAdapter.this.reset();
                }
                ReviewListFragment.this.mLastSelectedTagPosition = paramInt;
              }
            }
          });
        }
      }
      while (true)
      {
        if (paramMApiRequest != null)
          ReviewListFragment.this.mReviewListHeader.addView(paramMApiRequest);
        if (!this.loadFirstPage)
          this.loadFirstPage = true;
        return;
        label1067: i = 0;
        break;
        label1073: i += 1;
        break label502;
        label1082: if (((j == ReviewListFragment.this.mFilterId) && (ReviewListFragment.this.mKeyword == null)) || ((j == ReviewListFragment.this.mFilterId) && (ReviewListFragment.this.mKeyword != null) && (ReviewListFragment.this.mKeyword.startsWith(((DPObject)localObject2).getString("Name")))))
        {
          j = i;
          break label993;
        }
        label1154: i += 1;
        k += 1;
        break label856;
        label1169: paramMApiRequest = (MApiRequest)localObject1;
        if (localObject2 == null)
          continue;
        paramMApiRequest = (MApiRequest)localObject1;
        if (localObject2.length <= 0)
          continue;
        paramMApiRequest = (FilterBar)LayoutInflater.from(ReviewListFragment.this.getContext()).inflate(R.layout.filter_layout, ReviewListFragment.this.mReviewListHeader, false);
        paramMApiResponse = new ArrayList();
        i = 0;
        while (i < localObject2.length)
        {
          if (localObject2[i].getInt("ParentID") == 4)
            paramMApiResponse.add(localObject2[i]);
          i += 1;
        }
        paramMApiResponse = (DPObject[])paramMApiResponse.toArray(new DPObject[0]);
        paramMApiRequest.addItem("star", "全部星级");
        paramMApiRequest.addItem("sort", "默认排序");
        paramMApiRequest.setOnItemClickListener(new FilterBar.OnItemClickListener(paramMApiResponse, paramMApiRequest)
        {
          public void onClickItem(Object paramObject, View paramView)
          {
            ListFilterDialog localListFilterDialog = new ListFilterDialog((NovaActivity)ReviewListFragment.this.getContext());
            if ("star".equals(paramObject))
            {
              localListFilterDialog.setTag(paramObject);
              localListFilterDialog.setItems(this.val$mStarList);
              localListFilterDialog.setSelectedItem(ReviewListFragment.this.mCurrentStar);
              localListFilterDialog.show(paramView);
            }
            while (true)
            {
              localListFilterDialog.setOnFilterListener(new FilterDialog.OnFilterListener()
              {
                public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
                {
                  if ("star".equals(paramFilterDialog.getTag()))
                  {
                    ReviewListFragment.this.mCurrentStar = ((DPObject)paramObject);
                    paramObject = ReviewListFragment.this.mCurrentStar.getString("Name");
                    ReviewListFragment.ReviewListAdapter.3.this.val$filterView.setItem("star", paramObject);
                  }
                  while (true)
                  {
                    Log.d("ReviewListFragment", "filter star id=" + ReviewListFragment.this.mCurrentStar.getInt("ID") + " filter sort id=" + ReviewListFragment.this.mCurrentSort.getInt("ID"));
                    ReviewListFragment.this.setFilterId(ReviewListFragment.this.mCurrentStar.getInt("ID") + ReviewListFragment.this.mCurrentSort.getInt("ID") - 400);
                    ReviewListFragment.ReviewListAdapter.this.reset();
                    paramFilterDialog.dismiss();
                    return;
                    if (!"sort".equals(paramFilterDialog.getTag()))
                      continue;
                    ReviewListFragment.this.mCurrentSort = ((DPObject)paramObject);
                    ReviewListFragment.ReviewListAdapter.3.this.val$filterView.setItem("sort", ReviewListFragment.this.mCurrentSort.getString("Name"));
                  }
                }
              });
              return;
              if (!"sort".equals(paramObject))
                continue;
              localListFilterDialog.setTag(paramObject);
              localListFilterDialog.setItems(new DPObject[] { ReviewListFragment.DEFAULT_SORT, ReviewListFragment.TIME_SORT });
              localListFilterDialog.setSelectedItem(ReviewListFragment.this.mCurrentSort);
              localListFilterDialog.show(paramView);
            }
          }
        });
      }
    }

    public void reset()
    {
      this.loadFirstPage = false;
      this.mReviewItems.clear();
      this.mCachedView.clear();
      this.mHiddenFriendReviews.clear();
      this.mOwnReviewCount = 0;
      this.mFriendReviewCount = 0;
      this.mNextFriendReviewPosition = 0;
      super.reset();
    }

    public void updateEditedReview(UGCReviewItem paramUGCReviewItem)
    {
      ReviewItemView localReviewItemView = null;
      int j = 0;
      Iterator localIterator = this.mReviewItems.entrySet().iterator();
      Object localObject;
      int i;
      while (true)
      {
        localObject = localReviewItemView;
        i = j;
        if (!localIterator.hasNext())
          break;
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (!((ReviewItem)localEntry.getValue()).reviewId.equals(String.valueOf(paramUGCReviewItem.reviewId)))
          continue;
        localObject = (ReviewItem)localEntry.getValue();
        i = ((Integer)localEntry.getKey()).intValue();
      }
      localReviewItemView = (ReviewItemView)this.mCachedView.get(Integer.valueOf(i));
      if ((localReviewItemView != null) && (localObject != null))
        localReviewItemView.update(ReviewItemHelper.updateReviewItem((ReviewItem)localObject, paramUGCReviewItem));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.ugc.ReviewListFragment
 * JD-Core Version:    0.6.0
 */
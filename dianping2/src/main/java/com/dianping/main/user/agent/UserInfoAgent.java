package com.dianping.main.user.agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.ugc.utils.UGCBaseDraftManager;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.loader.MyResources;
import com.dianping.main.find.pictureplaza.FeedBasicAdapter;
import com.dianping.main.user.UserFeedItem;
import com.dianping.model.Location;
import com.dianping.model.UserProfile;
import com.dianping.ugc.model.UGCReviewItem;
import com.dianping.ugc.model.UploadPhotoData;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class UserInfoAgent extends AdapterCellAgent
{
  private static final String USER_INFO_LIST_TAG = "50UserInfoList.";
  private static final String USER_INFO_TAB_TAG = "40UserInfoTab.";
  private int DATA_TYPE_ALL = 0;
  private int DATA_TYPE_CHECKIN = 3;
  private int DATA_TYPE_COMMENT = 1;
  private int DATA_TYPE_PICTURE = 2;
  private FeedListAdapter allFeedAdapter;
  private FeedListAdapter checkinFeedAdapter;
  private FeedListAdapter commentFeedAdapter;
  private int mDataType = -1;
  private View mLayoutDraft;
  private View mLayoutUgcAll;
  private View mLayoutUgcCheckin;
  private View mLayoutUgcComment;
  private View mLayoutUgcPic;
  private View.OnClickListener mTabClickListener;
  private View mTabView;
  private TextView mTvCountCheckin;
  private TextView mTvCountComment;
  private TextView mTvCountPic;
  private TextView mTvDraftCount;
  private FeedListAdapter picFeedAdapter;
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      if ("com.dianping.user.UPDATE_COUNT".equals(paramContext))
      {
        paramContext = (DPObject)UserInfoAgent.this.getSharedObject("user");
        if (paramContext != null)
        {
          paramContext = paramContext.getArray("FeedTypeList");
          if ((paramContext != null) && (paramContext.length != 0))
            break label48;
        }
      }
      label48: label55: label119: 
      do
        while (true)
        {
          return;
          int j = paramContext.length;
          int i = 0;
          String str;
          if (i < j)
          {
            paramIntent = paramContext[i];
            str = paramIntent.getString("ID");
            if (!String.valueOf(UserInfoAgent.this.DATA_TYPE_COMMENT).equals(str))
              break label119;
            UserInfoAgent.this.mTvCountComment.setText(String.valueOf(paramIntent.getInt("Type")));
          }
          while (true)
          {
            i += 1;
            break label55;
            break;
            if (String.valueOf(UserInfoAgent.this.DATA_TYPE_PICTURE).equals(str))
            {
              UserInfoAgent.this.mTvCountPic.setText(String.valueOf(paramIntent.getInt("Type")));
              continue;
            }
            if (!String.valueOf(UserInfoAgent.this.DATA_TYPE_CHECKIN).equals(str))
              continue;
            UserInfoAgent.this.mTvCountCheckin.setText(String.valueOf(paramIntent.getInt("Type")));
          }
          if (("com.dianping.action.USER_EDIT".equals(paramContext)) || ("daren:badgeupdate".equals(paramContext)))
          {
            UserInfoAgent.this.allFeedAdapter.reset();
            UserInfoAgent.this.commentFeedAdapter.reset();
            return;
          }
          if (!"com.dianping.action.PlazaFeedLikeStateChange".equals(paramContext))
            break;
          i = paramIntent.getIntExtra("position", 0);
          paramContext = paramIntent.getStringExtra("feedid");
          boolean bool = paramIntent.getBooleanExtra("islike", false);
          if (UserInfoAgent.this.mDataType == UserInfoAgent.this.DATA_TYPE_ALL)
          {
            UserInfoAgent.this.allFeedAdapter.updateLikeStatus(i, paramContext, bool);
            return;
          }
          if (UserInfoAgent.this.mDataType == UserInfoAgent.this.DATA_TYPE_COMMENT)
          {
            UserInfoAgent.this.commentFeedAdapter.updateLikeStatus(i, paramContext, bool);
            return;
          }
          if (UserInfoAgent.this.mDataType == UserInfoAgent.this.DATA_TYPE_CHECKIN)
          {
            UserInfoAgent.this.checkinFeedAdapter.updateLikeStatus(i, paramContext, bool);
            return;
          }
          if (UserInfoAgent.this.mDataType != UserInfoAgent.this.DATA_TYPE_PICTURE)
            continue;
          UserInfoAgent.this.picFeedAdapter.updateLikeStatus(i, paramContext, bool);
          return;
        }
      while ((!"com.dianping.REVIEWREFRESH".equals(paramContext)) || (UserInfoAgent.this.allFeedAdapter == null));
      paramContext = (UGCReviewItem)paramIntent.getParcelableExtra("updatedugcreviewitem");
      UserInfoAgent.this.allFeedAdapter.updateEditItem(paramContext);
    }
  };
  private UserInfoTabAdapter userInfoTabAdapter;

  public UserInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createTabView(ViewGroup paramViewGroup)
  {
    this.mTabClickListener = new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        UserInfoAgent.this.resetTabViews();
        paramView.setSelected(true);
        if ((paramView.getTag() != null) && ((paramView.getTag() instanceof Integer)))
          UserInfoAgent.this.setCurrentTab(((Integer)paramView.getTag()).intValue());
      }
    };
    this.mTabView = this.res.inflate(getContext(), R.layout.user_info_tab_layout, paramViewGroup, false);
    this.mLayoutUgcAll = this.mTabView.findViewById(R.id.all_list);
    this.mLayoutUgcAll.setOnClickListener(this.mTabClickListener);
    this.mLayoutUgcAll.setTag(Integer.valueOf(this.DATA_TYPE_ALL));
    this.mLayoutUgcComment = this.mTabView.findViewById(R.id.review_list);
    this.mTvCountComment = ((TextView)this.mLayoutUgcComment.findViewById(R.id.tv_comment_count));
    this.mLayoutUgcComment.setTag(Integer.valueOf(this.DATA_TYPE_COMMENT));
    this.mLayoutUgcComment.setOnClickListener(this.mTabClickListener);
    this.mLayoutUgcPic = this.mTabView.findViewById(R.id.picture_list);
    this.mTvCountPic = ((TextView)this.mLayoutUgcPic.findViewById(R.id.tv_pic_count));
    this.mLayoutUgcPic.setOnClickListener(this.mTabClickListener);
    this.mLayoutUgcPic.setTag(Integer.valueOf(this.DATA_TYPE_PICTURE));
    this.mLayoutUgcCheckin = this.mTabView.findViewById(R.id.checkin_list);
    this.mTvCountCheckin = ((TextView)this.mLayoutUgcCheckin.findViewById(R.id.tv_checkin_count));
    this.mLayoutUgcCheckin.setOnClickListener(this.mTabClickListener);
    this.mLayoutUgcCheckin.setTag(Integer.valueOf(this.DATA_TYPE_CHECKIN));
    this.mLayoutDraft = this.mTabView.findViewById(R.id.drafts);
    this.mLayoutDraft.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://drafts"));
        UserInfoAgent.this.getContext().startActivity(paramView);
      }
    });
    this.mTvDraftCount = ((TextView)this.mTabView.findViewById(R.id.tv_draft_count));
    paramViewGroup = UGCBaseDraftManager.getInstance().getDraftCount();
    TextView localTextView = this.mTvDraftCount;
    if (paramViewGroup == null);
    for (paramViewGroup = "0"; ; paramViewGroup = String.valueOf(paramViewGroup))
    {
      localTextView.setText(paramViewGroup);
      if ((getContext() instanceof DPActivity))
      {
        ((DPActivity)getContext()).addGAView(this.mLayoutUgcAll, -1);
        ((DPActivity)getContext()).addGAView(this.mLayoutUgcComment, -1);
        ((DPActivity)getContext()).addGAView(this.mTvCountPic, -1);
        ((DPActivity)getContext()).addGAView(this.mTvCountCheckin, -1);
      }
      this.mLayoutUgcAll.performClick();
      return this.mTabView;
    }
  }

  private boolean isMyself()
  {
    UserProfile localUserProfile = getAccount();
    return (localUserProfile != null) && (localUserProfile.id() == userId());
  }

  private void resetTabViews()
  {
    this.mLayoutUgcAll.setSelected(false);
    this.mLayoutUgcComment.setSelected(false);
    this.mLayoutUgcPic.setSelected(false);
    this.mLayoutUgcCheckin.setSelected(false);
  }

  private void setCurrentTab(int paramInt)
  {
    if (paramInt == this.mDataType)
      return;
    if (paramInt == this.DATA_TYPE_ALL)
      if ((isMyself()) && (UGCBaseDraftManager.getInstance().getDraftCount() != null) && (UGCBaseDraftManager.getInstance().getDraftCount().intValue() > 0))
      {
        this.mLayoutDraft.setVisibility(0);
        if ((getContext() instanceof DPActivity))
          ((DPActivity)getContext()).addGAView(this.mLayoutDraft, -1);
        addCell("50UserInfoList.", this.allFeedAdapter);
      }
    while (true)
    {
      this.mDataType = paramInt;
      return;
      this.mLayoutDraft.setVisibility(8);
      break;
      if (paramInt == this.DATA_TYPE_COMMENT)
      {
        this.mLayoutDraft.setVisibility(8);
        addCell("50UserInfoList.", this.commentFeedAdapter);
        continue;
      }
      if (paramInt == this.DATA_TYPE_PICTURE)
      {
        this.mLayoutDraft.setVisibility(8);
        addCell("50UserInfoList.", this.picFeedAdapter);
        continue;
      }
      if (paramInt != this.DATA_TYPE_CHECKIN)
        continue;
      this.mLayoutDraft.setVisibility(8);
      addCell("50UserInfoList.", this.checkinFeedAdapter);
    }
  }

  private int userId()
  {
    if ((getSharedObject("user") instanceof DPObject))
      return ((DPObject)getSharedObject("user")).getInt("UserID");
    return ((Integer)getSharedObject("memberId")).intValue();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.userInfoTabAdapter = new UserInfoTabAdapter(null);
    addCell("40UserInfoTab.", this.userInfoTabAdapter);
    this.allFeedAdapter = new FeedListAdapter(getContext(), this.DATA_TYPE_ALL);
    this.commentFeedAdapter = new FeedListAdapter(getContext(), this.DATA_TYPE_COMMENT);
    this.picFeedAdapter = new FeedListAdapter(getContext(), this.DATA_TYPE_PICTURE);
    this.checkinFeedAdapter = new FeedListAdapter(getContext(), this.DATA_TYPE_CHECKIN);
    addCell("50UserInfoList.", this.allFeedAdapter);
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.user.UPDATE_COUNT");
    paramBundle.addAction("com.dianping.action.USER_EDIT");
    paramBundle.addAction("com.dianping.action.PlazaFeedLikeStateChange");
    paramBundle.addAction("com.dianping.REVIEWREFRESH");
    paramBundle.addAction("daren:badgeupdate");
    getContext().registerReceiver(this.receiver, paramBundle);
    LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.receiver, paramBundle);
  }

  public void onDestroy()
  {
    getContext().unregisterReceiver(this.receiver);
    LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  class FeedListAdapter extends FeedBasicAdapter
  {
    private int mType;

    public FeedListAdapter(Context paramInt, int arg3)
    {
      super();
      int i;
      this.mType = i;
    }

    public MApiRequest createRequest(int paramInt)
    {
      String str1 = "";
      String str2 = "";
      Object localObject = UserInfoAgent.this.location();
      if (localObject != null)
      {
        str1 = ((Location)localObject).latitude() + "";
        str2 = ((Location)localObject).longitude() + "";
      }
      localObject = Uri.parse("http://m.api.dianping.com/review/getprofilefeedlist.bin").buildUpon();
      ((Uri.Builder)localObject).appendQueryParameter("start", String.valueOf(paramInt)).appendQueryParameter("userid", String.valueOf(UserInfoAgent.this.getSharedObject("memberId"))).appendQueryParameter("lat", str1).appendQueryParameter("lng", str2).appendQueryParameter("type", String.valueOf(this.mType));
      return (MApiRequest)BasicMApiRequest.mapiGet(((Uri.Builder)localObject).toString(), CacheType.DISABLED);
    }

    protected void dismissFeedProgressDialog()
    {
      UserInfoAgent.this.getFragment().dismissDialog();
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
          paramString2.setPadding(0, ViewUtils.dip2px(UserInfoAgent.this.getContext(), 50.0F), 0, 0);
          paramString2.setText("暂未留下任何内容");
          paramString2.setTextColor(UserInfoAgent.this.getResources().getColor(R.color.light_gray));
          paramString2.setTextSize(0, UserInfoAgent.this.getResources().getDimensionPixelSize(R.dimen.text_medium));
        }
        return paramString2;
        if (paramView.getTag() != EMPTY)
          continue;
        paramString1 = (TextView)paramView;
      }
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof UserFeedItem));
      for (paramView = (UserFeedItem)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
          localObject = (UserFeedItem)UserInfoAgent.this.res.inflate(UserInfoAgent.this.getContext(), R.layout.plaza_home_feed_item, paramViewGroup, false);
        ((UserFeedItem)localObject).setFeedUgc(paramDPObject, paramInt, this.expandMaps, this.imageIndexMaps, true);
        ((UserFeedItem)localObject).setFeedItemListener(this.feedItemListener);
        return localObject;
      }
    }

    protected void showFeedProgressDialog(String paramString)
    {
      UserInfoAgent.this.getFragment().showProgressDialog(paramString);
    }

    public void updateEditItem(UGCReviewItem paramUGCReviewItem)
    {
      if (paramUGCReviewItem == null);
      DPObject localDPObject;
      do
      {
        do
          return;
        while (!(getItem(this.curPosition) instanceof DPObject));
        localDPObject = (DPObject)getItem(this.curPosition);
      }
      while (!localDPObject.getString("MainId").equals(this.curFeedId));
      try
      {
        i = Integer.parseInt(paramUGCReviewItem.star);
        arrayOfDPObject = null;
        ArrayList localArrayList = paramUGCReviewItem.mPhotos;
        localObject = arrayOfDPObject;
        if (localArrayList != null)
        {
          localObject = arrayOfDPObject;
          if (localArrayList.size() > 0)
          {
            arrayOfDPObject = new DPObject[localArrayList.size()];
            j = 0;
            localObject = arrayOfDPObject;
            if (j < localArrayList.size())
              localObject = (UploadPhotoData)localArrayList.get(j);
          }
        }
      }
      catch (NumberFormatException localNumberFormatException1)
      {
        int i;
        try
        {
          while (true)
          {
            DPObject[] arrayOfDPObject;
            Object localObject;
            int j;
            k = Integer.parseInt(((UploadPhotoData)localObject).poiShopId);
            arrayOfDPObject[j] = new DPObject().edit().putInt("ShopId", k).putInt("ContainerHeight", ((UploadPhotoData)localObject).containerHeight).putInt("ContainerWidth", ((UploadPhotoData)localObject).containerWidth).putString("ShopName", ((UploadPhotoData)localObject).poiShopName).putString("PicUrl", ((UploadPhotoData)localObject).photoPath).putString("PicSmallUrl", ((UploadPhotoData)localObject).photoPath).generate();
            j += 1;
          }
          localNumberFormatException1 = localNumberFormatException1;
          i = 0;
        }
        catch (NumberFormatException localNumberFormatException2)
        {
          while (true)
            int k = 0;
        }
        this.mData.set(this.curPosition, localDPObject.edit().putInt("Star", i).putArray("PlazaPics", localNumberFormatException1).putString("Content", paramUGCReviewItem.comment).generate());
        notifyDataSetChanged();
      }
    }
  }

  private class UserInfoTabAdapter extends BaseAdapter
  {
    private UserInfoTabAdapter()
    {
    }

    public int getCount()
    {
      return 1;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (UserInfoAgent.this.mTabView == null)
        return UserInfoAgent.this.createTabView(paramViewGroup);
      return UserInfoAgent.this.mTabView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.agent.UserInfoAgent
 * JD-Core Version:    0.6.0
 */
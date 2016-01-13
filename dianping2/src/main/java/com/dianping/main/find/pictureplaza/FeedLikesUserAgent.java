package com.dianping.main.find.pictureplaza;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AdapterCellAgent.BasicCellAgentAdapter;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaImageView;
import java.util.ArrayList;
import java.util.Iterator;

public class FeedLikesUserAgent extends PlazaAdapterCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String FEED_LIKES_HEADER_TAG = "030LikesHeader";
  private static final String FEED_LIKES_USER_TAG = "031LikesUser";
  private final int IS_LIKE_ACTION_TYPE_NO = 0;
  private final int IS_LIKE_ACTION_TYPE_YES = 1;
  private final int USER_DISPLAY_PLACE_COUNT = 6;
  private Adapter mAdapter;
  private View mAgentView;
  private DPObject mDPObj;
  private DPObject mError;
  private int mFeedAuthorId = 0;
  private String mFeedId;
  private int mFeedType;
  private boolean mIsLike;
  private boolean mIsRegister = false;
  private NovaImageView mLikeBtn;
  private int mLikeCount = -1;
  private TextView mLikeCountView;
  final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      int i;
      if ("com.dianping.action.PlazaFeedIsLike".equals(paramIntent.getAction()))
      {
        FeedLikesUserAgent.access$002(FeedLikesUserAgent.this, paramIntent.getBooleanExtra("IsLike", false));
        if (FeedLikesUserAgent.this.mLikeBtn != null)
        {
          paramContext = FeedLikesUserAgent.this.mLikeBtn;
          if (!FeedLikesUserAgent.this.mIsLike)
            break label65;
          i = R.drawable.icon_like_pressed;
          paramContext.setImageResource(i);
        }
      }
      label65: 
      do
      {
        return;
        i = R.drawable.icon_like_normal;
        break;
      }
      while (!"com.dianping.action.PlazaFeedAuthorId".equals(paramIntent.getAction()));
      FeedLikesUserAgent.access$202(FeedLikesUserAgent.this, paramIntent.getIntExtra("FeedAuthorId", 0));
    }
  };
  private MApiRequest mReqLike;
  private MApiRequest mRequest;

  public FeedLikesUserAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendLikeCountBroadcast()
  {
    Intent localIntent = new Intent("com.dianping.action.PlazaFeedLikeCount");
    localIntent.putExtra("likecount", this.mLikeCount);
    LocalBroadcastManager.getInstance(DPApplication.instance()).sendBroadcast(localIntent);
  }

  private void sendLikeReq(int paramInt)
  {
    if (this.mReqLike != null)
    {
      getFragment().mapiService().abort(this.mReqLike, this, true);
      this.mReqLike = null;
    }
    String str = Uri.parse("http://m.api.dianping.com/review/ugcfavor.bin").toString();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("actiontype");
    localArrayList.add(String.valueOf(paramInt));
    localArrayList.add("mainid");
    localArrayList.add(this.mFeedId);
    localArrayList.add("feedtype");
    localArrayList.add(String.valueOf(this.mFeedType));
    localArrayList.add("originuserid");
    localArrayList.add(String.valueOf(this.mFeedAuthorId));
    this.mReqLike = BasicMApiRequest.mapiPost(str, (String[])localArrayList.toArray(new String[0]));
    getFragment().mapiService().exec(this.mReqLike, this);
  }

  private void sendRequest()
  {
    if (this.mRequest != null)
      return;
    this.mDPObj = null;
    this.mError = null;
    this.mRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/review/getfavorlist.bin").buildUpon().appendQueryParameter("start", String.valueOf(0)).appendQueryParameter("limit", String.valueOf(50)).appendQueryParameter("mainid", this.mFeedId).appendQueryParameter("feedtype", String.valueOf(this.mFeedType)).build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this);
  }

  private void updateLikeState(boolean paramBoolean)
  {
    Object localObject = getAccount();
    if (localObject == null)
    {
      Toast.makeText(getContext(), "您还没有登陆哦～", 0).show();
      return;
    }
    if (paramBoolean)
    {
      localObject = new DPObject("PlazaUser").edit().putString("Avatar", ((UserProfile)localObject).avatar()).putString("Nick", ((UserProfile)localObject).nickName()).putInt("UserID", ((UserProfile)localObject).id()).generate();
      localObject = new DPObject("FavorUser").edit().putString("FavTime", "刚刚").putObject("User", (DPObject)localObject).generate();
      this.mAdapter.addCurrentUser((DPObject)localObject);
      this.mLikeBtn.setImageResource(R.drawable.icon_like_pressed);
    }
    while (true)
    {
      sendLikeCountBroadcast();
      return;
      this.mAdapter.removeCurrentUser(((UserProfile)localObject).id());
      this.mLikeBtn.setImageResource(R.drawable.icon_like_normal);
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.mAdapter.setData();
    sendLikeCountBroadcast();
    this.mAdapter.notifyDataSetChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mFeedType = getFragment().getIntParam("feedtype", 0);
    this.mFeedId = getFragment().getStringParam("feedid");
    boolean bool;
    if (getFragment().getIntParam("islike", 0) == 1)
    {
      bool = true;
      this.mIsLike = bool;
      this.mFeedAuthorId = getFragment().getIntParam("feedauthorid", 0);
      if (getFragment().getIntParam("isfromnotificationcenter", 0) == 1)
      {
        paramBundle = new IntentFilter();
        paramBundle.addAction("com.dianping.action.PlazaFeedIsLike");
        paramBundle.addAction("com.dianping.action.PlazaFeedAuthorId");
        LocalBroadcastManager.getInstance(DPApplication.instance()).registerReceiver(this.mReceiver, paramBundle);
        this.mIsRegister = true;
      }
      this.mLikeBtn = ((NovaImageView)((FeedDetailFragment)getFragment()).getFragmentView().findViewById(R.id.do_like));
      paramBundle = this.mLikeBtn;
      if (!this.mIsLike)
        break label233;
    }
    label233: for (int i = R.drawable.icon_like_pressed; ; i = R.drawable.icon_like_normal)
    {
      paramBundle.setImageResource(i);
      this.mLikeBtn.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          boolean bool = true;
          if (FeedLikesUserAgent.this.accountService().token() == null)
          {
            FeedLikesUserAgent.this.accountService().login(new LoginResultListener()
            {
              public void onLoginCancel(AccountService paramAccountService)
              {
              }

              public void onLoginSuccess(AccountService paramAccountService)
              {
                FeedLikesUserAgent.2.this.onClick(null);
              }
            });
            return;
          }
          if (!FeedLikesUserAgent.this.mIsLike)
          {
            FeedLikesUserAgent.this.sendLikeReq(1);
            paramView = FeedLikesUserAgent.this;
            if (FeedLikesUserAgent.this.mIsLike)
              break label103;
          }
          while (true)
          {
            FeedLikesUserAgent.access$002(paramView, bool);
            FeedLikesUserAgent.this.updateLikeState(FeedLikesUserAgent.this.mIsLike);
            return;
            FeedLikesUserAgent.this.sendLikeReq(0);
            break;
            label103: bool = false;
          }
        }
      });
      this.mAdapter = new Adapter(null);
      addCell("030LikesHeader", new HeaderAdapter(null));
      addCell("031LikesUser", this.mAdapter);
      sendRequest();
      return;
      bool = false;
      break;
    }
  }

  public void onDestroy()
  {
    boolean bool = false;
    super.onDestroy();
    int i;
    if (getFragment().getIntParam("isfromnotificationcenter", 0) == 0)
    {
      if (getFragment().getIntParam("islike", 0) != 1)
        break label167;
      i = 1;
    }
    while (true)
    {
      if ((i ^ this.mIsLike) != 0)
      {
        Intent localIntent = new Intent("com.dianping.action.PlazaFeedLikeStateChange");
        localIntent.putExtra("feedid", this.mFeedId);
        if (!this.mIsLike)
          bool = true;
        localIntent.putExtra("islike", bool);
        localIntent.putExtra("position", getFragment().getIntParam("position"));
        LocalBroadcastManager.getInstance(DPApplication.instance()).sendBroadcast(localIntent);
      }
      if (this.mRequest != null)
      {
        getFragment().mapiService().abort(this.mRequest, this, true);
        this.mRequest = null;
        label146: if (!this.mIsRegister);
      }
      try
      {
        LocalBroadcastManager.getInstance(DPApplication.instance()).unregisterReceiver(this.mReceiver);
        return;
        label167: i = 0;
        continue;
        if (this.mReqLike == null)
          break label146;
        getFragment().mapiService().abort(this.mReqLike, this, true);
        this.mReqLike = null;
      }
      catch (Exception localException)
      {
      }
    }
  }

  protected void onRefresh()
  {
    super.onRefresh();
    sendRequest();
    onRefreshStart();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mRequest == paramMApiRequest)
    {
      this.mDPObj = null;
      this.mRequest = null;
      onRefreshComplete();
    }
    while ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (!TextUtils.isEmpty(paramMApiResponse.message().content())))
    {
      showToast(paramMApiResponse.message().content());
      return;
      if (this.mReqLike != paramMApiRequest)
        continue;
      this.mReqLike = null;
    }
    showToast("请求失败，请稍后再试");
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != null) && (this.mRequest == paramMApiRequest))
    {
      this.mRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mDPObj = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
      onRefreshComplete();
    }
    do
      return;
    while ((paramMApiRequest == null) || (this.mReqLike != paramMApiRequest));
    this.mReqLike = null;
  }

  private class Adapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    private ArrayList<DPObject> userList = new ArrayList();

    private Adapter()
    {
      super();
    }

    public void addCurrentUser(DPObject paramDPObject)
    {
      DPObject localDPObject1;
      if (paramDPObject != null)
      {
        localDPObject1 = paramDPObject.getObject("User");
        if (localDPObject1 != null);
      }
      else
      {
        return;
      }
      if (this.userList.size() > 0)
      {
        Iterator localIterator = this.userList.iterator();
        while (localIterator.hasNext())
        {
          DPObject localDPObject2 = ((DPObject)localIterator.next()).getObject("User");
          if ((localDPObject2 != null) && (localDPObject2.getInt("UserID") == localDPObject1.getInt("UserID")))
            return;
        }
      }
      FeedLikesUserAgent.access$702(FeedLikesUserAgent.this, Math.max(FeedLikesUserAgent.this.mLikeCount, 0));
      FeedLikesUserAgent.access$708(FeedLikesUserAgent.this);
      this.userList.add(0, paramDPObject);
      FeedLikesUserAgent.access$1002(FeedLikesUserAgent.this, null);
      notifyDataSetChanged();
    }

    public int getCount()
    {
      if (this.userList.size() > 0)
        return 1;
      return 0;
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
      if (FeedLikesUserAgent.this.mAgentView == null)
      {
        FeedLikesUserAgent.access$1002(FeedLikesUserAgent.this, FeedLikesUserAgent.this.res.inflate(FeedLikesUserAgent.this.getContext(), R.layout.find_plaza_feed_likes_user, paramViewGroup, false));
        LinearLayout localLinearLayout = (LinearLayout)FeedLikesUserAgent.this.mAgentView.findViewById(R.id.avatar_layout);
        paramView = new PlazaLikesAvatarItem[6];
        LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(0, ViewUtils.dip2px(FeedLikesUserAgent.this.getContext(), 45.0F), 1.0F);
        localLayoutParams1.rightMargin = ViewUtils.dip2px(FeedLikesUserAgent.this.getContext(), 4.0F);
        LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(0, ViewUtils.dip2px(FeedLikesUserAgent.this.getContext(), 45.0F), 1.0F);
        paramInt = 0;
        if (paramInt < 6)
        {
          paramView[paramInt] = ((PlazaLikesAvatarItem)LayoutInflater.from(FeedLikesUserAgent.this.getContext()).inflate(R.layout.find_plaza_likes_avatar_item, paramViewGroup, false));
          if (paramInt < 5)
            paramView[paramInt].setLayoutParams(localLayoutParams1);
          while (true)
          {
            localLinearLayout.addView(paramView[paramInt]);
            paramInt += 1;
            break;
            paramView[paramInt].setLayoutParams(localLayoutParams2);
          }
        }
        if (this.userList.size() > 6);
        for (paramInt = 6; ; paramInt = this.userList.size())
        {
          int i = 0;
          while (i < paramInt)
          {
            paramViewGroup = ((DPObject)this.userList.get(i)).getObject("User");
            if (paramViewGroup != null)
            {
              paramView[i].setAvatar(paramViewGroup.getString("Avatar"));
              int j = paramViewGroup.getInt("UserID");
              if (j != 0)
                paramView[i].setOnClickListener(new View.OnClickListener(j)
                {
                  public void onClick(View paramView)
                  {
                    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user").buildUpon().appendQueryParameter("userid", String.valueOf(this.val$userId)).build());
                    FeedLikesUserAgent.this.startActivity(paramView);
                  }
                });
              paramView[i].setGAString("like_profile", null, i);
            }
            i += 1;
          }
        }
        if (this.userList.size() > 6)
        {
          paramView[5].setCoverVisibility(0);
          paramView[5].setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://likesuser").buildUpon().appendQueryParameter("feedid", FeedLikesUserAgent.this.mFeedId).appendQueryParameter("feedtype", String.valueOf(FeedLikesUserAgent.this.getFragment().getIntParam("feedtype", 0))).build());
              FeedLikesUserAgent.this.startActivity(paramView);
            }
          });
          paramView[5].setGAString("like_more");
        }
      }
      return FeedLikesUserAgent.this.mAgentView;
    }

    public void removeCurrentUser(int paramInt)
    {
      if (this.userList.size() > 0)
      {
        Iterator localIterator = this.userList.iterator();
        while (localIterator.hasNext())
        {
          DPObject localDPObject1 = (DPObject)localIterator.next();
          DPObject localDPObject2 = localDPObject1.getObject("User");
          if ((localDPObject2 == null) || (localDPObject2.getInt("UserID") != paramInt))
            continue;
          if (FeedLikesUserAgent.this.mLikeCount > 0)
            FeedLikesUserAgent.access$710(FeedLikesUserAgent.this);
          this.userList.remove(localDPObject1);
          FeedLikesUserAgent.access$1002(FeedLikesUserAgent.this, null);
          notifyDataSetChanged();
        }
      }
    }

    public void setData()
    {
      if (FeedLikesUserAgent.this.mDPObj != null)
      {
        DPObject[] arrayOfDPObject = FeedLikesUserAgent.this.mDPObj.getArray("List");
        int i = FeedLikesUserAgent.this.mDPObj.getInt("RecordCount");
        FeedLikesUserAgent.access$702(FeedLikesUserAgent.this, Math.max(i, 0));
        this.userList.clear();
        FeedLikesUserAgent.access$1002(FeedLikesUserAgent.this, null);
        if (arrayOfDPObject != null)
        {
          i = 0;
          while (i < arrayOfDPObject.length)
          {
            this.userList.add(arrayOfDPObject[i]);
            i += 1;
          }
        }
      }
    }
  }

  private class HeaderAdapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    private HeaderAdapter()
    {
      super();
    }

    public int getCount()
    {
      if (FeedLikesUserAgent.this.mLikeCount > 0)
        return 1;
      return 0;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (FeedLikesUserAgent.this.mLikeCountView == null)
      {
        FeedLikesUserAgent.access$802(FeedLikesUserAgent.this, new TextView(FeedLikesUserAgent.this.getContext()));
        FeedLikesUserAgent.this.mLikeCountView.setLayoutParams(new AbsListView.LayoutParams(-2, -2));
        FeedLikesUserAgent.this.mLikeCountView.setPadding(ViewUtils.dip2px(FeedLikesUserAgent.this.getContext(), 18.0F), ViewUtils.dip2px(FeedLikesUserAgent.this.getContext(), 18.0F), 0, 0);
        FeedLikesUserAgent.this.mLikeCountView.setTextSize(0, FeedLikesUserAgent.this.getResources().getDimensionPixelSize(R.dimen.text_size_15));
        FeedLikesUserAgent.this.mLikeCountView.setTextColor(FeedLikesUserAgent.this.getResources().getColor(R.color.light_gray));
        FeedLikesUserAgent.this.mLikeCountView.setSingleLine(true);
      }
      FeedLikesUserAgent.this.mLikeCountView.setText(FeedLikesUserAgent.this.mLikeCount + "个赞");
      return FeedLikesUserAgent.this.mLikeCountView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.FeedLikesUserAgent
 * JD-Core Version:    0.6.0
 */
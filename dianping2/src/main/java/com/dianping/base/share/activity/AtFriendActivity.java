package com.dianping.base.share.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.EditSearchBar;
import com.dianping.base.widget.EditSearchBar.OnKeywordChangeListner;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AtFriendActivity extends NovaActivity
  implements EditSearchBar.OnKeywordChangeListner, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String K_AVATAR = "Avatar";
  private static final String K_FRIEND_OBJ_LIST = "friendObjList";
  private static final String K_NAME = "Name";
  private static final String K_PLATFORM = "platform";
  private static final String K_TOKEN = "token";
  public static final String TAG = AtFriendActivity.class.getSimpleName();
  private FriendAdapter mFriendAdapter;
  private ArrayList<DPObject> mFriendObjList = new ArrayList();
  private ListView mLvFriend;
  private String mPlatform;
  private EditSearchBar mSearchBar;
  private MApiRequest mSnsFriendsReq;

  private void initView()
  {
    this.mSearchBar = ((EditSearchBar)findViewById(R.id.search_bar));
    this.mSearchBar.setHint("请输入好友名字");
    this.mSearchBar.setOnKeywordChangeListner(this);
    this.mLvFriend = ((ListView)findViewById(R.id.lv_friend));
    this.mFriendAdapter = new FriendAdapter(this, this.mFriendObjList);
    this.mLvFriend.setAdapter(this.mFriendAdapter);
    this.mLvFriend.setOnItemClickListener(this.mFriendAdapter);
    this.mLvFriend.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        AtFriendActivity.this.hideKeyboard();
        return false;
      }
    });
  }

  private void onSnsFriendsSuccess(List<DPObject> paramList)
  {
    this.mFriendObjList.clear();
    this.mFriendObjList.addAll(paramList);
    this.mFriendAdapter.setDataAndNotify(this.mFriendObjList);
  }

  private void sendSnsFriendsReq()
  {
    if (this.mSnsFriendsReq != null)
    {
      mapiService().abort(this.mSnsFriendsReq, this, true);
      this.mSnsFriendsReq = null;
    }
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/snsfriends.bin").buildUpon();
    localBuilder.appendQueryParameter("token", accountService().token());
    localBuilder.appendQueryParameter("platform", this.mPlatform);
    this.mSnsFriendsReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.NORMAL);
    mapiService().exec(this.mSnsFriendsReq, this);
  }

  void hideKeyboard()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
    if (localInputMethodManager.isActive())
      localInputMethodManager.hideSoftInputFromWindow(this.mSearchBar.getWindowToken(), 0);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.activity_at_friend);
    initView();
    if (paramBundle == null)
    {
      this.mPlatform = getIntent().getData().getQueryParameter("platform");
      sendSnsFriendsReq();
      return;
    }
    this.mPlatform = paramBundle.getString("platform");
    this.mFriendObjList = paramBundle.getParcelableArrayList("friendObjList");
  }

  public void onKeywordChanged(String paramString)
  {
    if (!this.mFriendObjList.isEmpty())
    {
      if (TextUtils.isEmpty(paramString))
        this.mFriendAdapter.setDataAndNotify(this.mFriendObjList);
    }
    else
      return;
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.mFriendObjList.iterator();
    while (localIterator.hasNext())
    {
      DPObject localDPObject = (DPObject)localIterator.next();
      if (!localDPObject.getString("Name").contains(paramString))
        continue;
      localArrayList.add(localDPObject);
    }
    this.mFriendAdapter.setDataAndNotify(localArrayList);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest.equals(this.mSnsFriendsReq))
    {
      this.mSnsFriendsReq = null;
      showToast("获取好友列表失败");
      finish();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest.equals(this.mSnsFriendsReq))
    {
      this.mSnsFriendsReq = null;
      if ((paramMApiResponse instanceof DPObject[]))
        onSnsFriendsSuccess(Arrays.asList((DPObject[])(DPObject[])paramMApiResponse));
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("platform", this.mPlatform);
    paramBundle.putParcelableArrayList("friendObjList", this.mFriendObjList);
  }

  class FriendAdapter extends BasicAdapter
    implements AdapterView.OnItemClickListener
  {
    private final Context mContext;
    private final ArrayList<DPObject> mDataObjList = new ArrayList();

    public FriendAdapter(ArrayList<DPObject> arg2)
    {
      Object localObject;
      this.mContext = localObject;
      ArrayList localArrayList;
      setDataWithoutNotify(localArrayList);
    }

    public int getCount()
    {
      return this.mDataObjList.size();
    }

    public Object getItem(int paramInt)
    {
      return this.mDataObjList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = (DPObject)getItem(paramInt);
      if (paramView != null)
      {
        paramViewGroup = (LinearLayout)paramView;
        paramView = (FriendHolder)paramViewGroup.getTag();
      }
      while (true)
      {
        paramView.setItem(localDPObject);
        paramViewGroup.setTag(paramView);
        return paramViewGroup;
        paramViewGroup = (LinearLayout)LayoutInflater.from(this.mContext).inflate(R.layout.sns_friend_item, paramViewGroup, false);
        paramView = new FriendHolder();
        paramView.mIvAvatar = ((NetworkImageView)paramViewGroup.findViewById(R.id.iv_avatar));
        paramView.mTvName = ((TextView)paramViewGroup.findViewById(R.id.tv_name));
      }
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      paramAdapterView = (DPObject)this.mDataObjList.get(paramInt);
      paramView = new Intent();
      paramView.putExtra("Name", paramAdapterView.getString("Name"));
      AtFriendActivity.this.setResult(-1, paramView);
      AtFriendActivity.this.finish();
    }

    public void setDataAndNotify(ArrayList<DPObject> paramArrayList)
    {
      setDataWithoutNotify(paramArrayList);
      notifyDataSetChanged();
    }

    public void setDataWithoutNotify(ArrayList<DPObject> paramArrayList)
    {
      this.mDataObjList.clear();
      if (paramArrayList != null)
        this.mDataObjList.addAll(paramArrayList);
    }

    class FriendHolder
    {
      NetworkImageView mIvAvatar;
      TextView mTvName;

      FriendHolder()
      {
      }

      public void setItem(DPObject paramDPObject)
      {
        this.mIvAvatar.setImage(paramDPObject.getString("Avatar"));
        this.mTvName.setText(paramDPObject.getString("Name"));
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.activity.AtFriendActivity
 * JD-Core Version:    0.6.0
 */
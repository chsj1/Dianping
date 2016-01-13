package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.v1.R.color;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaListView;

public class LikesUserActivity extends NovaActivity
{
  private UserListAdapter mAdapter;
  private String mFeedId;
  private int mFeedType;
  private boolean mHasTitle = false;
  private NovaListView mListView;

  public String getPageName()
  {
    return "moments_feed_like";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getTitleBar().setTitle("");
    this.mFeedId = getStringParam("feedid");
    this.mFeedType = getIntParam("feedtype", 0);
    this.mListView = new NovaListView(this);
    this.mListView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    this.mListView.setBackgroundColor(getResources().getColor(R.color.white));
    this.mListView.setOnItemClickListener(null);
    this.mListView.setDivider(null);
    setContentView(this.mListView);
    this.mAdapter = new UserListAdapter(this);
    this.mListView.setAdapter(this.mAdapter);
  }

  class UserListAdapter extends BasicLoadAdapter
  {
    private Context context;

    public UserListAdapter(Context arg2)
    {
      super();
      this.context = localContext;
    }

    public MApiRequest createRequest(int paramInt)
    {
      return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/review/getfavorlist.bin").buildUpon().appendQueryParameter("start", String.valueOf(paramInt)).appendQueryParameter("limit", String.valueOf(50)).appendQueryParameter("mainid", LikesUserActivity.this.mFeedId).appendQueryParameter("feedtype", String.valueOf(LikesUserActivity.this.mFeedType)).build().toString(), CacheType.DISABLED);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof PlazaUserProfile));
      for (paramView = (PlazaUserProfile)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
          localObject = (PlazaUserProfile)LayoutInflater.from(this.context).inflate(R.layout.plaza_userprofile_layout, paramViewGroup, false);
        ((PlazaUserProfile)localObject).setPlazaUserWithGrayLine(paramDPObject.getObject("User"), paramDPObject.getString("FavTime"), true);
        paramDPObject = paramDPObject.getObject("User");
        if (paramDPObject != null)
        {
          int i = paramDPObject.getInt("UserID");
          if (i != 0)
            ((PlazaUserProfile)localObject).setOnClickListener(new View.OnClickListener(i)
            {
              public void onClick(View paramView)
              {
                paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user").buildUpon().appendQueryParameter("userid", String.valueOf(this.val$userId)).build());
                LikesUserActivity.this.startActivity(paramView);
              }
            });
        }
        ((PlazaUserProfile)localObject).setGAString("like_profile", null, paramInt);
        LikesUserActivity.this.addGAView((View)localObject, paramInt);
        return localObject;
      }
    }

    protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestComplete(paramBoolean, paramMApiRequest, paramMApiResponse);
      if ((!LikesUserActivity.this.mHasTitle) && (paramBoolean) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        int i = ((DPObject)paramMApiResponse.result()).getInt("RecordCount");
        if (i > 0)
          LikesUserActivity.this.getTitleBar().setTitle(i + "个赞");
        LikesUserActivity.access$202(LikesUserActivity.this, true);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.LikesUserActivity
 * JD-Core Version:    0.6.0
 */
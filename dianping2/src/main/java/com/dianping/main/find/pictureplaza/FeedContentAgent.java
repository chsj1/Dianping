package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AdapterCellAgent.BasicCellAgentAdapter;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class FeedContentAgent extends AdapterCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String FEED_PICTURE_LIST_TAG = "012PictureList";
  private static final String FEED_TEXT_CONTENT_TAG = "015TextContent";
  private static final String FEED_USER_INFO_TAG = "010UserInfo";
  private LinearLayout contLayout;
  private DPObject mDPObj;
  private DPObject mError;
  private String mFeedId;
  private int mFeedType;
  private boolean mIsLike;
  private PictureListAdapter mPictureListAdapter;
  private MApiRequest mRequest;
  private TextContentAdapter mTextContentAdapter;
  private UserInfoAdapter mUserInfoAdapter;
  private PlazaUserProfile mUserInfoView;

  public FeedContentAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequest()
  {
    if (this.mRequest != null)
      return;
    this.mDPObj = null;
    this.mError = null;
    String str1 = "";
    String str2 = "";
    Location localLocation = location();
    if (localLocation != null)
    {
      str1 = localLocation.latitude() + "";
      str2 = localLocation.longitude() + "";
    }
    this.mRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/review/getugcfeed.bin").buildUpon().appendQueryParameter("mainid", this.mFeedId).appendQueryParameter("feedtype", String.valueOf(this.mFeedType)).appendQueryParameter("lng", str2).appendQueryParameter("lat", str1).build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.mDPObj != null)
    {
      paramBundle = new Intent("com.dianping.action.PlazaFeedIsLike");
      paramBundle.putExtra("IsLike", this.mDPObj.getBoolean("IsLike"));
      LocalBroadcastManager.getInstance(DPApplication.instance()).sendBroadcast(paramBundle);
      if (this.mDPObj.getObject("User") != null)
      {
        paramBundle = new Intent("com.dianping.action.PlazaFeedAuthorId");
        paramBundle.putExtra("FeedAuthorId", this.mDPObj.getObject("User").getInt("UserID"));
        LocalBroadcastManager.getInstance(DPApplication.instance()).sendBroadcast(paramBundle);
      }
      this.mUserInfoAdapter.setData(this.mDPObj.getObject("User"), this.mDPObj.getString("Time"));
      this.mPictureListAdapter.setData(this.mDPObj.getArray("PlazaPics"));
      this.mPictureListAdapter.notifyDataSetChanged();
      this.mTextContentAdapter.setData(this.mDPObj.getString("Content"));
      this.mTextContentAdapter.notifyDataSetChanged();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mFeedId = getFragment().getStringParam("feedid");
    this.mFeedType = getFragment().getIntParam("feedtype", 0);
    this.mIsLike = getFragment().getBooleanParam("islike", false);
    this.mUserInfoAdapter = new UserInfoAdapter(null);
    this.mPictureListAdapter = new PictureListAdapter(null);
    this.mTextContentAdapter = new TextContentAdapter(null);
    addCell("010UserInfo", this.mUserInfoAdapter);
    addCell("012PictureList", this.mPictureListAdapter);
    addCell("015TextContent", this.mTextContentAdapter);
    sendRequest();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mRequest != null)
    {
      getFragment().mapiService().abort(this.mRequest, this, true);
      this.mRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mRequest == paramMApiRequest)
    {
      this.mDPObj = null;
      this.mRequest = null;
      if ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (!TextUtils.isEmpty(paramMApiResponse.message().content())))
        Toast.makeText(getContext(), paramMApiResponse.message().content(), 0).show();
    }
    else
    {
      return;
    }
    Toast.makeText(getContext(), "请求失败，请稍后再试", 0).show();
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
    }
  }

  private class PictureListAdapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    ArrayList<DPObject> list = new ArrayList();

    private PictureListAdapter()
    {
      super();
    }

    private void setData(DPObject[] paramArrayOfDPObject)
    {
      if ((paramArrayOfDPObject != null) && (paramArrayOfDPObject.length > 0))
      {
        int j = paramArrayOfDPObject.length;
        int i = 0;
        while (i < j)
        {
          DPObject localDPObject = paramArrayOfDPObject[i];
          this.list.add(localDPObject);
          i += 1;
        }
      }
    }

    public int getCount()
    {
      return this.list.size();
    }

    public Object getItem(int paramInt)
    {
      return this.list.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof PoiLargeImageView));
      for (paramView = (PoiLargeImageView)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
        {
          localObject = (PoiLargeImageView)FeedContentAgent.this.res.inflate(FeedContentAgent.this.getContext(), R.layout.poi_large_imageview_layout, paramViewGroup, false);
          ((PoiLargeImageView)localObject).setLayoutParams(new AbsListView.LayoutParams(-1, -2));
          ((PoiLargeImageView)localObject).setPadding(ViewUtils.dip2px(FeedContentAgent.this.getContext(), 10.0F), ViewUtils.dip2px(FeedContentAgent.this.getContext(), 5.0F), ViewUtils.dip2px(FeedContentAgent.this.getContext(), 10.0F), ViewUtils.dip2px(FeedContentAgent.this.getContext(), 5.0F));
        }
        ((PoiLargeImageView)localObject).setPoiLargeImageSource((DPObject)this.list.get(paramInt), 0, FeedContentAgent.this.mFeedId, false, 0);
        ((PoiLargeImageView)localObject).setPoiImageListener(new PoiLargeImageView.PoiImageListener(paramInt)
        {
          public void onLargeImageClick(int paramInt, Drawable paramDrawable)
          {
          }

          public void onPoiClick(int paramInt)
          {
            Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + ((DPObject)FeedContentAgent.PictureListAdapter.this.list.get(this.val$position)).getInt("ShopId")));
            FeedContentAgent.this.getContext().startActivity(localIntent);
          }
        });
        ((PoiLargeImageView)localObject).setCanClick(false);
        return localObject;
      }
    }
  }

  private class TextContentAdapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    private String content;

    private TextContentAdapter()
    {
      super();
    }

    public int getCount()
    {
      if (!TextUtils.isEmpty(this.content))
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
      if (FeedContentAgent.this.contLayout == null)
      {
        FeedContentAgent.access$702(FeedContentAgent.this, new LinearLayout(FeedContentAgent.this.getContext()));
        FeedContentAgent.this.contLayout.setOrientation(1);
        paramView = new TextView(FeedContentAgent.this.getContext());
        paramView.setTextSize(0, FeedContentAgent.this.getResources().getDimensionPixelSize(R.dimen.text_size_16));
        paramView.setPadding(ViewUtils.dip2px(FeedContentAgent.this.getContext(), 10.0F), ViewUtils.dip2px(FeedContentAgent.this.getContext(), 5.0F), ViewUtils.dip2px(FeedContentAgent.this.getContext(), 10.0F), ViewUtils.dip2px(FeedContentAgent.this.getContext(), 10.0F));
        if (!TextUtils.isEmpty(this.content))
          paramView.setText(this.content);
        FeedContentAgent.this.contLayout.addView(paramView);
        paramView = new LinearLayout(paramViewGroup.getContext());
        paramViewGroup = new LinearLayout.LayoutParams(-1, 1);
        paramViewGroup.setMargins(ViewUtils.dip2px(FeedContentAgent.this.getContext(), 10.0F), ViewUtils.dip2px(FeedContentAgent.this.getContext(), 15.0F), 0, 0);
        paramView.setLayoutParams(paramViewGroup);
        paramView.setBackgroundResource(R.color.line_gray);
        FeedContentAgent.this.contLayout.addView(paramView);
      }
      return FeedContentAgent.this.contLayout;
    }

    public void setData(String paramString)
    {
      if (!TextUtils.isEmpty(paramString))
        this.content = paramString;
    }
  }

  private class UserInfoAdapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    private DPObject user;

    private UserInfoAdapter()
    {
      super();
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
      if (FeedContentAgent.this.mUserInfoView == null)
      {
        FeedContentAgent.access$402(FeedContentAgent.this, (PlazaUserProfile)FeedContentAgent.this.res.inflate(FeedContentAgent.this.getContext(), R.layout.plaza_userprofile_layout, paramViewGroup, false));
        FeedContentAgent.this.mUserInfoView.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            if (FeedContentAgent.UserInfoAdapter.this.user == null)
              return;
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user").buildUpon().appendQueryParameter("userid", FeedContentAgent.UserInfoAdapter.this.user.getInt("UserID") + "").build());
            FeedContentAgent.this.getContext().startActivity(paramView);
          }
        });
      }
      return FeedContentAgent.this.mUserInfoView;
    }

    public void setData(DPObject paramDPObject, String paramString)
    {
      if (paramDPObject != null)
      {
        this.user = paramDPObject;
        FeedContentAgent.this.mUserInfoView.setPlazaUserInfo(paramDPObject, paramString, false, false);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.FeedContentAgent
 * JD-Core Version:    0.6.0
 */
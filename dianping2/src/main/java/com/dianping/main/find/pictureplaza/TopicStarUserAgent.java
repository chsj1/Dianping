package com.dianping.main.find.pictureplaza;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterCellAgent.BasicCellAgentAdapter;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;

public class TopicStarUserAgent extends PlazaAdapterCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String TOPIC_STAR_USER_TAG = "020StarUser";
  private final int USER_DISPLAY_MIN_COUNT = 4;
  private final int USER_DISPLAY_PLACE_COUNT = 5;
  private Adapter mAdapter;
  private View mAgentView;
  private DPObject mDPObj;
  private DPObject mError;
  private Button mJoinButton;
  private MApiRequest mRequest;
  private int mTopicId;

  public TopicStarUserAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequest()
  {
    if (this.mRequest != null)
      return;
    this.mError = null;
    this.mRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/plaza/getplazastaruser.bin").buildUpon().appendQueryParameter("topicid", String.valueOf(this.mTopicId)).appendQueryParameter("type", String.valueOf(1)).build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.mAdapter.notifyDataSetChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mTopicId = getFragment().getIntParam("topicid", 1);
    this.mAdapter = new Adapter(null);
    addCell("020StarUser", this.mAdapter);
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
  }

  private class Adapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    PlazaStarUserAvatarItem[] avatarViews;

    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      if ((TopicStarUserAgent.this.mDPObj != null) && (TopicStarUserAgent.this.mDPObj.getArray("List") != null) && (TopicStarUserAgent.this.mDPObj.getArray("List").length >= 4))
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
      if (TopicStarUserAgent.this.mAgentView == null)
      {
        TopicStarUserAgent.access$302(TopicStarUserAgent.this, TopicStarUserAgent.this.res.inflate(TopicStarUserAgent.this.getContext(), R.layout.find_plaza_topic_star_user, paramViewGroup, false));
        paramView = (LinearLayout)TopicStarUserAgent.this.mAgentView.findViewById(R.id.avatar_layout);
        this.avatarViews = new PlazaStarUserAvatarItem[5];
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, -2, 1.0F);
        localLayoutParams.rightMargin = ViewUtils.dip2px(TopicStarUserAgent.this.getContext(), 2.0F);
        localLayoutParams.leftMargin = localLayoutParams.rightMargin;
        paramInt = 0;
        while (paramInt < 5)
        {
          this.avatarViews[paramInt] = ((PlazaStarUserAvatarItem)LayoutInflater.from(TopicStarUserAgent.this.getContext()).inflate(R.layout.find_plaza_staruser_avatar_item, paramViewGroup, false));
          this.avatarViews[paramInt].setLayoutParams(localLayoutParams);
          paramView.addView(this.avatarViews[paramInt]);
          paramInt += 1;
        }
      }
      setViewData();
      return TopicStarUserAgent.this.mAgentView;
    }

    public void setViewData()
    {
      ArrayList localArrayList = new ArrayList(Arrays.asList(TopicStarUserAgent.this.mDPObj.getArray("List")));
      if (localArrayList.size() > 5);
      for (int i = 5; ; i = localArrayList.size())
      {
        int j = 0;
        while (j < i)
        {
          DPObject localDPObject = ((DPObject)localArrayList.get(j)).getObject("User");
          if (localDPObject != null)
          {
            this.avatarViews[j].setAvatar(localDPObject.getString("Avatar"));
            this.avatarViews[j].setCoverVisibility(4);
            this.avatarViews[j].setNickName(localDPObject.getString("Nick"));
            int k = localDPObject.getInt("UserID");
            if (k != 0)
              this.avatarViews[j].setOnClickListener(new View.OnClickListener(k)
              {
                public void onClick(View paramView)
                {
                  paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user").buildUpon().appendQueryParameter("userid", String.valueOf(this.val$userId)).build());
                  TopicStarUserAgent.this.startActivity(paramView);
                }
              });
            this.avatarViews[j].setGAString("prouser_profile", null, j);
            this.avatarViews[j].getGAUserInfo().biz_id = String.valueOf(k);
          }
          j += 1;
        }
      }
      if (localArrayList.size() > 5)
      {
        this.avatarViews[4].setNickName("");
        this.avatarViews[4].setCoverVisibility(0);
        this.avatarViews[4].setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://plazastaruser").buildUpon().appendQueryParameter("topicid", String.valueOf(TopicStarUserAgent.this.mTopicId)).build());
            TopicStarUserAgent.this.startActivity(paramView);
          }
        });
        this.avatarViews[4].setGAString("prouser_more");
      }
      while (true)
      {
        return;
        while (i < 5)
        {
          this.avatarViews[i].setAvatar("");
          this.avatarViews[i].setNickName("");
          this.avatarViews[i].setCoverVisibility(4);
          this.avatarViews[i].setOnClickListener(null);
          i += 1;
        }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.TopicStarUserAgent
 * JD-Core Version:    0.6.0
 */
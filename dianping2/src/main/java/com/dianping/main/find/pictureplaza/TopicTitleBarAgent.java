package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterCellAgent.BasicCellAgentAdapter;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.SimpleMsg;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaTextView;

public class TopicTitleBarAgent extends PlazaAdapterCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String TOPIC_TITLE_BAR_TAG = "010TitleBar";
  private final int TOPIC_IS_FOLLOWED_NO = 0;
  private final int TOPIC_IS_FOLLOWED_YES = 1;
  private Adapter mAdapter;
  private NetworkImageView mAgentBackground;
  private TextView mAgentSubtitleView;
  private TextView mAgentTitleView;
  private View mAgentView;
  private CustomImageButton mBackButtonOrange;
  private CustomImageButton mBackButtonWhite;
  private double mColorChannelDelta;
  private int mColorChannelMax = 255;
  private int mColorChannelMin = 51;
  private DPObject mDPObj;
  private int mIsFollowed;
  private ListView mListView;
  private int mMaxTextSize = getResources().getDimensionPixelSize(R.dimen.text_size_27);
  private int mMinTextSize = getResources().getDimensionPixelSize(R.dimen.text_size_18);
  private MApiRequest mRequest;
  private MApiRequest mRequestFollow;
  private double mTextDelta;
  private int mTitleAlphaDist;
  private ViewGroup mTitleBar;
  private NovaTextView mTitleBarButton;
  private ViewGroup mTitleBarLayout;
  private View mTitleBarShadow;
  private TextView mTitleBarText;
  private int mTitleDist = 2147483647;
  private int mTopicId;

  public TopicTitleBarAgent(Object paramObject)
  {
    super(paramObject);
  }

  private int getTextHeight(Context paramContext, CharSequence paramCharSequence, int paramInt)
  {
    int i = getFragment().getActivity().getWindowManager().getDefaultDisplay().getWidth();
    paramContext = new TextView(paramContext);
    paramContext.setText(paramCharSequence);
    paramContext.setTextSize(0, paramInt);
    paramContext.measure(View.MeasureSpec.makeMeasureSpec(i, -2147483648), View.MeasureSpec.makeMeasureSpec(0, 0));
    return paramContext.getMeasuredHeight();
  }

  private void sendRequest()
  {
    if (this.mRequest != null)
      return;
    this.mDPObj = null;
    this.mRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/plaza/getplazatopic.bin").buildUpon().appendQueryParameter("topicid", String.valueOf(this.mTopicId)).build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this);
  }

  private void sendRequestFollow()
  {
    int i = 1;
    if (this.mRequestFollow != null)
      return;
    if (this.mIsFollowed == 1)
      i = 0;
    this.mRequestFollow = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/plaza/followplazatopic.bin").buildUpon().appendQueryParameter("topicid", String.valueOf(this.mTopicId)).appendQueryParameter("actiontype", String.valueOf(i)).build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequestFollow, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((this.mDPObj != null) && (this.mAgentView != null))
    {
      paramBundle = this.mDPObj.getString("Title");
      if (!TextUtils.isEmpty(paramBundle))
      {
        this.mTitleBarText.setText(paramBundle);
        this.mAgentTitleView.setText(paramBundle);
        ((PlazaTopicFragment)getFragment()).mTopicName = paramBundle;
      }
      boolean bool = this.mDPObj.getBoolean("IsFollowed");
      this.mTitleBarButton.setVisibility(0);
      if (!bool)
        break label220;
      this.mIsFollowed = 1;
      this.mTitleBarButton.setText("已关注");
    }
    while (true)
    {
      this.mTitleBarButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = TopicTitleBarAgent.this.getFragment().accountService();
          if (paramView.token() == null)
          {
            paramView.login(new LoginResultListener()
            {
              public void onLoginCancel(AccountService paramAccountService)
              {
              }

              public void onLoginSuccess(AccountService paramAccountService)
              {
                TopicTitleBarAgent.1.this.onClick(null);
              }
            });
            return;
          }
          TopicTitleBarAgent.this.sendRequestFollow();
        }
      });
      paramBundle = this.mDPObj.getInt("ContentCount") + "条内容 • " + this.mDPObj.getInt("FollowCount") + "人关注 • " + this.mDPObj.getInt("ViewCount") + "人浏览";
      this.mAgentSubtitleView.setText(paramBundle);
      paramBundle = this.mDPObj.getString("BannerPicUrl");
      this.mAgentBackground.setImage(paramBundle);
      this.mAdapter.notifyDataSetChanged();
      return;
      label220: this.mIsFollowed = 0;
      this.mTitleBarButton.setText("+关注");
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mTitleAlphaDist = ViewUtils.dip2px(getContext(), 100.0F);
    this.mTitleDist = (ViewUtils.dip2px(getContext(), 50.0F) / 2 + getTextHeight(getContext(), "话题详情", this.mMinTextSize) / 2);
    this.mTextDelta = ((this.mMaxTextSize - this.mMinTextSize) / this.mTitleDist);
    this.mColorChannelDelta = ((this.mColorChannelMax - this.mColorChannelMin) / this.mTitleDist);
    this.mTopicId = getFragment().getIntParam("topicid", 1);
    this.mAdapter = new Adapter(null);
    addCell("010TitleBar", this.mAdapter);
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
    if (this.mRequestFollow != null)
    {
      getFragment().mapiService().abort(this.mRequestFollow, this, true);
      this.mRequestFollow = null;
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
      this.mRequest = null;
      this.mDPObj = null;
      onRefreshComplete();
    }
    while ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (!TextUtils.isEmpty(paramMApiResponse.message().content())))
    {
      Toast.makeText(getContext(), paramMApiResponse.message().content(), 0).show();
      return;
      if (this.mRequestFollow != paramMApiRequest)
        continue;
      this.mRequestFollow = null;
    }
    Toast.makeText(getContext(), "请求失败，请稍后再试", 0).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest != null)
    {
      if (this.mRequest != paramMApiRequest)
        break label52;
      this.mRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mDPObj = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
        onRefreshComplete();
      }
    }
    label52: 
    do
    {
      do
        return;
      while (this.mRequestFollow != paramMApiRequest);
      this.mRequestFollow = null;
      if (this.mIsFollowed != 1)
        continue;
      this.mIsFollowed = 0;
      this.mTitleBarButton.setText("+关注");
      showToast("已取消～");
      return;
    }
    while (this.mIsFollowed != 0);
    this.mIsFollowed = 1;
    this.mTitleBarButton.setText("已关注");
    showToast("关注成功！");
  }

  private class Adapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    private Adapter()
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
      if (TopicTitleBarAgent.this.mAgentView == null)
      {
        TopicTitleBarAgent.access$302(TopicTitleBarAgent.this, ((PlazaTopicFragment)TopicTitleBarAgent.this.getFragment()).getTitleBarLayout());
        TopicTitleBarAgent.access$402(TopicTitleBarAgent.this, (ViewGroup)TopicTitleBarAgent.this.mTitleBarLayout.findViewById(R.id.topic_title_bar));
        TopicTitleBarAgent.access$502(TopicTitleBarAgent.this, (CustomImageButton)TopicTitleBarAgent.this.mTitleBarLayout.findViewById(R.id.back_orange));
        TopicTitleBarAgent.access$602(TopicTitleBarAgent.this, (CustomImageButton)TopicTitleBarAgent.this.mTitleBarLayout.findViewById(R.id.back));
        TopicTitleBarAgent.access$702(TopicTitleBarAgent.this, (TextView)TopicTitleBarAgent.this.mTitleBarLayout.findViewById(R.id.topic_title_text));
        TopicTitleBarAgent.access$802(TopicTitleBarAgent.this, (NovaTextView)TopicTitleBarAgent.this.mTitleBarLayout.findViewById(R.id.topic_follow));
        TopicTitleBarAgent.this.mTitleBarButton.getGAUserInfo().biz_id = String.valueOf(TopicTitleBarAgent.this.mTopicId);
        ((DPActivity)TopicTitleBarAgent.this.getContext()).addGAView(TopicTitleBarAgent.this.mTitleBarButton, -1);
        TopicTitleBarAgent.access$1002(TopicTitleBarAgent.this, TopicTitleBarAgent.this.mTitleBarLayout.findViewById(R.id.topic_title_shadow));
        TopicTitleBarAgent.this.mTitleBar.setBackgroundColor(Color.argb(0, 252, 252, 252));
        TopicTitleBarAgent.this.mBackButtonOrange.setAlpha(0);
        TopicTitleBarAgent.this.mBackButtonWhite.setAlpha(255);
        TopicTitleBarAgent.access$1102(TopicTitleBarAgent.this, ((PlazaTopicFragment)TopicTitleBarAgent.this.getFragment()).getListView());
        TopicTitleBarAgent.this.mListView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
          private int getAlpha(int paramInt)
          {
            if (paramInt > TopicTitleBarAgent.this.mTitleAlphaDist)
            {
              TopicTitleBarAgent.this.mTitleBarShadow.setVisibility(0);
              return 255;
            }
            TopicTitleBarAgent.this.mTitleBarShadow.setVisibility(4);
            return (int)(255.0D / TopicTitleBarAgent.this.mTitleAlphaDist * paramInt);
          }

          private void setText(int paramInt)
          {
            if (paramInt >= TopicTitleBarAgent.this.mTitleDist)
            {
              TopicTitleBarAgent.this.mAgentTitleView.setVisibility(4);
              TopicTitleBarAgent.this.mAgentTitleView.setTextColor(Color.rgb(TopicTitleBarAgent.this.mColorChannelMin, TopicTitleBarAgent.this.mColorChannelMin, TopicTitleBarAgent.this.mColorChannelMin));
              TopicTitleBarAgent.this.mTitleBarText.setVisibility(0);
              return;
            }
            if (paramInt <= 0)
            {
              TopicTitleBarAgent.this.mAgentTitleView.setVisibility(0);
              TopicTitleBarAgent.this.mTitleBarText.setVisibility(4);
              TopicTitleBarAgent.this.mAgentTitleView.setTextSize(0, TopicTitleBarAgent.this.mMaxTextSize);
              TopicTitleBarAgent.this.mAgentTitleView.setTextColor(Color.rgb(TopicTitleBarAgent.this.mColorChannelMax, TopicTitleBarAgent.this.mColorChannelMax, TopicTitleBarAgent.this.mColorChannelMax));
              return;
            }
            TopicTitleBarAgent.this.mAgentTitleView.setVisibility(0);
            TopicTitleBarAgent.this.mTitleBarText.setVisibility(4);
            TopicTitleBarAgent.this.mAgentTitleView.setTextSize(0, (int)(TopicTitleBarAgent.this.mMaxTextSize - TopicTitleBarAgent.this.mTextDelta * paramInt));
            paramInt = (int)(TopicTitleBarAgent.this.mColorChannelMax - TopicTitleBarAgent.this.mColorChannelDelta * paramInt);
            TopicTitleBarAgent.this.mAgentTitleView.setTextColor(Color.rgb(paramInt, paramInt, paramInt));
          }

          public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
          {
            if ((paramInt1 == 0) || (paramInt1 == 1))
            {
              if (TopicTitleBarAgent.this.mListView.getChildAt(0) == null)
                return;
              paramInt1 = getAlpha(Math.abs(TopicTitleBarAgent.this.mListView.getChildAt(0).getTop()));
              TopicTitleBarAgent.this.mTitleBar.setBackgroundColor(Color.argb(paramInt1, 252, 252, 252));
              TopicTitleBarAgent.this.mBackButtonOrange.setAlpha(paramInt1);
              TopicTitleBarAgent.this.mBackButtonWhite.setAlpha(255 - paramInt1);
              setText(-TopicTitleBarAgent.this.mListView.getChildAt(0).getTop());
              return;
            }
            TopicTitleBarAgent.this.mTitleBar.setBackgroundColor(Color.argb(255, 252, 252, 252));
            TopicTitleBarAgent.this.mTitleBarShadow.setVisibility(0);
            TopicTitleBarAgent.this.mBackButtonOrange.setAlpha(255);
            TopicTitleBarAgent.this.mBackButtonWhite.setAlpha(0);
            setText(TopicTitleBarAgent.this.mTitleDist);
          }

          public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
          {
            onScroll(paramAbsListView, TopicTitleBarAgent.this.mListView.getFirstVisiblePosition(), 0, 0);
          }
        });
        TopicTitleBarAgent.access$202(TopicTitleBarAgent.this, TopicTitleBarAgent.this.res.inflate(TopicTitleBarAgent.this.getContext(), R.layout.find_plaza_topic_header, paramViewGroup, false));
        TopicTitleBarAgent.access$1402(TopicTitleBarAgent.this, (TextView)TopicTitleBarAgent.this.mAgentView.findViewById(R.id.header_title_text));
        TopicTitleBarAgent.access$2002(TopicTitleBarAgent.this, (TextView)TopicTitleBarAgent.this.mAgentView.findViewById(R.id.header_subtitle_text));
        TopicTitleBarAgent.access$2102(TopicTitleBarAgent.this, (NetworkImageView)TopicTitleBarAgent.this.mAgentView.findViewById(R.id.topic_background));
      }
      return TopicTitleBarAgent.this.mAgentView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.TopicTitleBarAgent
 * JD-Core Version:    0.6.0
 */
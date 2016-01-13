package com.dianping.shopinfo.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.widget.RichTextView;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.base.widget.tagflow.FlowLayout;
import com.dianping.base.widget.tagflow.TagAdapter;
import com.dianping.base.widget.tagflow.TagFlowLayout;
import com.dianping.base.widget.tagflow.TagFlowLayout.OnTagClickListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.activity.ShopInfoActivity;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.ugc.feed.model.FeedItem;
import com.dianping.ugc.feed.model.FeedUser;
import com.dianping.ugc.feed.view.FeedItemView;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String ACTION_DEL_REVIEW = "com.dianping.REVIEWDELETE";
  private static final String ACTION_REFRESH_FEED_COMMENT = "com.dianping.ADDCOMMENT";
  private static final String ACTION_REFRESH_FEED_LIKE = "com.dianping.REFRESHLIKE";
  private static final String ACTION_REFRESH_REVIEW = "com.dianping.REVIEWREFRESH";
  private static final String CELL_REVIEW = "3000Reivew.";
  private DPObject mError;
  private DPObject[] mFeedArray;
  final BroadcastReceiver mFeedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (ReviewAgent.this.mReviewCell == null);
      Object localObject;
      do
        while (true)
        {
          return;
          paramContext = paramIntent.getStringExtra("feedId");
          localObject = (NovaRelativeLayout)ReviewAgent.this.mReviewCell.findViewWithTag(paramContext);
          if (localObject == null)
            continue;
          localObject = (FeedItemView)((NovaRelativeLayout)localObject).findViewById(R.id.review_item);
          if (localObject == null)
            continue;
          if (!"com.dianping.ADDCOMMENT".equalsIgnoreCase(paramIntent.getAction()))
            break;
          if (TextUtils.isEmpty(paramContext))
            continue;
          ((FeedItemView)localObject).addComment((FeedUser)paramIntent.getParcelableExtra("toUser"), paramIntent.getStringExtra("content"));
          return;
        }
      while ((!"com.dianping.REFRESHLIKE".equalsIgnoreCase(paramIntent.getAction())) || (TextUtils.isEmpty(paramContext)));
      int i = paramIntent.getIntExtra("likeCount", 0);
      ((FeedItemView)localObject).addLike(paramIntent.getBooleanExtra("isLiked", false), i);
    }
  };
  private LocalBroadcastManager mLocalBroadcastManager;
  final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (("com.dianping.REVIEWREFRESH".equalsIgnoreCase(paramIntent.getAction())) || ("com.dianping.REVIEWDELETE".equalsIgnoreCase(paramIntent.getAction())))
        ReviewAgent.this.sendRequest();
    }
  };
  private MApiRequest mRequest;
  private View mReviewCell;
  protected List<DPObject> mReviewTags = new ArrayList();
  protected DPObject mShopReviewFeedList;

  public ReviewAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createDefaultReviewAgent()
  {
    Object localObject = this.res.inflate(getContext(), R.layout.shop_review_empty, getParentView(), false);
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.setTitle("网友点评");
    localShopinfoCommonCell.hideArrow();
    localShopinfoCommonCell.addContent((View)localObject, false, null);
    LinearLayout localLinearLayout = (LinearLayout)((View)localObject).findViewById(R.id.review_add);
    localObject = (RichTextView)((View)localObject).findViewById(R.id.review_notice);
    if (this.mShopReviewFeedList != null)
    {
      String str = this.mShopReviewFeedList.getString("Notice");
      if (TextUtils.isEmpty(str))
        break label129;
      ((RichTextView)localObject).setRichText(str);
    }
    while (true)
    {
      localLinearLayout.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          ReviewAgent.this.addReview();
        }
      });
      return localShopinfoCommonCell;
      label129: ((RichTextView)localObject).setRichText("上传第1条点评");
    }
  }

  private void sendRequest()
  {
    ShopInfoActivity.speedTest(getContext(), 1021);
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("http://m.api.dianping.com/review/shopreviewlist.bin?");
    localStringBuffer.append("shopid=").append(shopId());
    this.mRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this);
  }

  public void addReview()
  {
    DPObject localDPObject1 = getShop();
    if (localDPObject1 == null)
      return;
    switch (localDPObject1.getInt("Status"))
    {
    case 2:
    case 3:
    default:
      Bundle localBundle = new Bundle();
      localBundle.putParcelable("shop", localDPObject1);
      if (super.isBeautyHairType())
      {
        DPObject localDPObject2 = (DPObject)super.getSharedObject("beautyShopBasicInfo");
        if (localDPObject2 != null)
          localBundle.putParcelable("beautyShopBasicInfo", localDPObject2);
      }
      AddReviewUtil.addReview(getContext(), localDPObject1.getInt("ID"), localDPObject1.getString("Name"), localBundle);
      return;
    case 1:
    case 4:
    }
    Toast.makeText(getContext(), "暂停收录点评", 0).show();
  }

  protected void clickReviewItem(int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("dianping://review?id=");
    ((StringBuilder)localObject).append(shopId());
    if (getShop() != null)
    {
      ((StringBuilder)localObject).append("&shopname=").append(getShop().getString("Name"));
      String str = getShop().getString("BranchName");
      if (!TextUtils.isEmpty(str))
        ((StringBuilder)localObject).append("(").append(str).append(")");
      ((StringBuilder)localObject).append("&shopstatus=").append(getShop().getInt("Status"));
    }
    if (paramInt > 0)
      ((StringBuilder)localObject).append("&selectedreviewid=").append(paramInt);
    localObject = new Intent("android.intent.action.VIEW", Uri.parse(((StringBuilder)localObject).toString()));
    ((Intent)localObject).putExtra("shop", getShop());
    getFragment().startActivity((Intent)localObject);
  }

  protected View createAgentFooter()
  {
    View localView = this.res.inflate(getContext(), R.layout.shopinfo_common_cell_footer, getParentView(), false);
    ((TextView)localView.findViewById(R.id.title)).setText("查看全部网友点评");
    localView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ReviewAgent.this.clickReviewItem(0);
      }
    });
    ((NovaRelativeLayout)localView).setGAString("reviewall");
    GAHelper.instance().contextStatisticsEvent(getContext(), "reviewall", getGAExtra(), "view");
    return localView;
  }

  protected ShopinfoCommonCell createAgentHeader()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.setTitle("网友点评", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (ReviewAgent.this.mError != null)
        {
          ReviewAgent.access$202(ReviewAgent.this, null);
          ReviewAgent.this.sendRequest();
          ReviewAgent.this.dispatchAgentChanged(false);
          return;
        }
        ReviewAgent.this.clickReviewItem(0);
      }
    });
    localShopinfoCommonCell.setSubTitle("(" + this.mShopReviewFeedList.getInt("RecordCount") + ")");
    localShopinfoCommonCell.titleLay.setGAString("reviewlist");
    GAHelper.instance().contextStatisticsEvent(getContext(), "reviewlist", getGAExtra(), "view");
    return localShopinfoCommonCell;
  }

  protected View createReviewAgent()
  {
    ShopinfoCommonCell localShopinfoCommonCell = createAgentHeader();
    Object localObject1;
    if (localShopinfoCommonCell == null)
      localObject1 = null;
    Object localObject2;
    do
    {
      do
      {
        do
        {
          return localObject1;
          if ((this.mReviewTags != null) && (this.mReviewTags.size() > 0))
          {
            localObject1 = (TagFlowLayout)this.res.inflate(getContext(), R.layout.tag_flow_layout, getParentView(), false);
            ((TagFlowLayout)localObject1).setNumLine(2);
            ((TagFlowLayout)localObject1).setPadding(ViewUtils.dip2px(getContext(), 15.0F), ViewUtils.dip2px(getContext(), 1.0F), 0, ViewUtils.dip2px(getContext(), 4.0F));
            ((TagFlowLayout)localObject1).setAdapter(new PraiseTagAdapter(getContext(), this.mReviewTags));
            ((TagFlowLayout)localObject1).setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
            {
              public boolean onTagClick(View paramView, int paramInt, FlowLayout paramFlowLayout)
              {
                ((NovaTextView)paramView).setGAString("reviewfilter");
                paramView = (DPObject)paramView.getTag();
                if (paramView == null)
                  return false;
                paramFlowLayout = new StringBuilder();
                paramFlowLayout.append("dianping://review?id=");
                paramFlowLayout.append(ReviewAgent.this.shopId());
                if (ReviewAgent.this.getShop() != null)
                {
                  paramFlowLayout.append("&shopname=").append(ReviewAgent.this.getShop().getString("Name"));
                  str = ReviewAgent.this.getShop().getString("BranchName");
                  if (!TextUtils.isEmpty(str))
                    paramFlowLayout.append("(").append(str).append(")");
                  paramFlowLayout.append("&shopstatus=").append(ReviewAgent.this.getShop().getInt("Status"));
                }
                String str = paramView.getString("Name") + "_" + paramView.getInt("Affection");
                paramFlowLayout.append("&selecttagname=").append(str);
                paramInt = paramView.getInt("RankType");
                if (paramInt > 0)
                  paramFlowLayout.append("&tagtype=").append(paramInt);
                paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramFlowLayout.toString()));
                ReviewAgent.this.getFragment().startActivity(paramView);
                return true;
              }
            });
            localShopinfoCommonCell.addContent((View)localObject1, false);
            localObject1 = localShopinfoCommonCell.findViewById(R.id.middle_divder_line);
            if (localObject1 != null)
              ((View)localObject1).setVisibility(8);
          }
          localObject1 = localShopinfoCommonCell;
        }
        while (this.mFeedArray == null);
        int i = 0;
        while (i < this.mFeedArray.length)
        {
          localObject1 = new FeedItem(this.mFeedArray[i]);
          localObject2 = (FeedItemView)LayoutInflater.from(getContext()).inflate(R.layout.ugc_reviewlist_item, getParentView(), false);
          ((FeedItemView)localObject2).setTag(((FeedItem)localObject1).feedId);
          ((FeedItemView)localObject2).enableCommentSummary(true);
          ((FeedItemView)localObject2).enableExpandContent(false);
          ((FeedItemView)localObject2).showCommentList(false);
          ((FeedItemView)localObject2).setContentMaxLines(2);
          ((FeedItemView)localObject2).setMaxPhotoCount(3);
          ((FeedItemView)localObject2).setData((FeedItem)localObject1);
          localShopinfoCommonCell.addContent((View)localObject2, false, null);
          i += 1;
        }
        localObject1 = localShopinfoCommonCell;
      }
      while (this.mFeedArray.length < 2);
      localObject2 = createAgentFooter();
      localObject1 = localShopinfoCommonCell;
    }
    while (localObject2 == null);
    localShopinfoCommonCell.addView((View)localObject2);
    return (View)(View)localShopinfoCommonCell;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    paramBundle = getShop();
    removeAllCells();
    if (paramBundle == null)
      return;
    if ((this.mFeedArray != null) && (this.mFeedArray.length > 0))
    {
      this.mReviewCell = createReviewAgent();
      addCell("3000Reivew.", this.mReviewCell, 0);
    }
    while (true)
    {
      ShopInfoActivity.speedTest(getContext(), 1023);
      return;
      if (this.mShopReviewFeedList == null)
      {
        if (this.mError == null)
        {
          addCell("3000Reivew.", createLoadingCell(), 16);
          continue;
        }
        paramBundle = createErrorCell(new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            ReviewAgent.access$202(ReviewAgent.this, null);
            ReviewAgent.this.sendRequest();
            ReviewAgent.this.dispatchAgentChanged(false);
          }
        });
        paramBundle.setTag("RETRY");
        addCell("3000Reivew.", paramBundle, 0);
        continue;
      }
      if ((this.mShopReviewFeedList == null) || (this.mShopReviewFeedList.getArray("List") == null) || (this.mShopReviewFeedList.getArray("List").length != 0))
        continue;
      paramBundle = createDefaultReviewAgent();
      paramBundle.setTag("DEFAULT");
      addCell("3000Reivew.", paramBundle, 0);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(getContext());
    if (paramBundle != null)
    {
      this.mShopReviewFeedList = ((DPObject)paramBundle.getParcelable("reviewList"));
      this.mError = ((DPObject)paramBundle.getParcelable("error"));
    }
    if ((this.mShopReviewFeedList == null) && (this.mError == null))
      sendRequest();
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.REVIEWREFRESH");
    paramBundle.addAction("com.dianping.REVIEWDELETE");
    this.mLocalBroadcastManager.registerReceiver(this.mReceiver, paramBundle);
  }

  public void onDestroy()
  {
    if (this.mRequest != null)
    {
      getFragment().mapiService().abort(this.mRequest, this, true);
      this.mRequest = null;
    }
    if (this.mReceiver != null)
      this.mLocalBroadcastManager.unregisterReceiver(this.mReceiver);
    if (this.mFeedReceiver != null)
      this.mLocalBroadcastManager.unregisterReceiver(this.mFeedReceiver);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mRequest = null;
    if ((paramMApiResponse.error() instanceof DPObject));
    for (this.mError = ((DPObject)paramMApiResponse.error()); ; this.mError = new DPObject())
    {
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mRequest = null;
    this.mError = null;
    if (!(paramMApiResponse.result() instanceof DPObject));
    DPObject localDPObject;
    do
    {
      return;
      this.mShopReviewFeedList = ((DPObject)paramMApiResponse.result());
      paramMApiRequest = this.mShopReviewFeedList.getArray("ReviewAbstractList");
      if (paramMApiRequest != null)
      {
        int i = 0;
        while (i < paramMApiRequest.length)
        {
          paramMApiResponse = paramMApiRequest[i];
          if (paramMApiResponse.getInt("RankType") != 0)
            this.mReviewTags.add(paramMApiResponse);
          i += 1;
        }
      }
      this.mFeedArray = this.mShopReviewFeedList.getArray("List");
      setSharedObject("reviewList", this.mShopReviewFeedList);
      ShopInfoActivity.speedTest(getContext(), 1022);
      dispatchAgentChanged(false);
      paramMApiRequest = new ArrayList(Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(2) }));
      paramMApiResponse = new ArrayList(Arrays.asList(new Integer[] { Integer.valueOf(158), Integer.valueOf(159) }));
      localDPObject = getShop();
    }
    while ((localDPObject == null) || (!paramMApiResponse.contains(Integer.valueOf(localDPObject.getInt("CategoryID")))) || (!paramMApiRequest.contains(Integer.valueOf(localDPObject.getInt("CityID")))));
    dispatchAgentChanged("shopinfo/beautyheader", null);
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

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("reviewList", this.mShopReviewFeedList);
    localBundle.putParcelable("error", this.mError);
    return localBundle;
  }

  protected class PraiseTagAdapter extends TagAdapter<DPObject>
  {
    private List<DPObject> shortList = new ArrayList();

    public PraiseTagAdapter(List<DPObject> arg2)
    {
      super();
      this.shortList = localList;
    }

    public PraiseTagAdapter()
    {
      super();
    }

    public DPObject getItem(int paramInt)
    {
      return (DPObject)this.shortList.get(paramInt);
    }

    public View getView(FlowLayout paramFlowLayout, int paramInt, DPObject paramDPObject)
    {
      paramDPObject = getItem(paramInt);
      Object localObject = paramDPObject.getString("Name");
      paramInt = paramDPObject.getInt("Count");
      localObject = new StringBuilder((String)localObject);
      if (paramInt > 0)
        ((StringBuilder)localObject).append("(").append(paramInt).append(")");
      paramFlowLayout = (NovaTextView)LayoutInflater.from(ReviewAgent.this.getContext()).inflate(R.layout.review_tag_item, paramFlowLayout, false);
      paramFlowLayout.setText(((StringBuilder)localObject).toString());
      paramFlowLayout.setTag(paramDPObject);
      return (View)paramFlowLayout;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.ReviewAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.takeaway.fragment;

import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CustomLinearLayout;
import com.dianping.base.widget.CustomLinearLayout.OnItemClickListener;
import com.dianping.base.widget.NovaFragment;
import com.dianping.takeaway.activity.TakeawayMenuActivity;
import com.dianping.takeaway.entity.TakeawayCommentsDataSource;
import com.dianping.takeaway.entity.TakeawayCommentsDataSource.DataLoadListener;
import com.dianping.takeaway.entity.TakeawayCommentsDataSource.TakeawayCommentsPageStatus;
import com.dianping.takeaway.entity.TakeawayNetLoadStatus;
import com.dianping.takeaway.view.TAOperateItem;
import com.dianping.takeaway.view.TAOperateItem.BORDER;
import com.dianping.takeaway.view.TAStarView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TakeawayCommentsFragment extends NovaFragment
  implements TakeawayCommentsDataSource.DataLoadListener
{
  TakeawayMenuActivity activity;
  private TagAdapter cateAdapter;
  private CustomLinearLayout cateView;
  private TakeawayCommentAdapter commentAdapter;
  TakeawayCommentsDataSource commentDataSource;
  private ListView commentsListView;
  private TAOperateItem footerContentView;
  private View footerView;
  private View hasLikedView;
  private View headerView;
  LayoutInflater inflater;
  private ImageView optionCheckbox;
  private RelativeLayout statusView;
  View wholeView;

  private void addHeaderView()
  {
    this.headerView = this.inflater.inflate(R.layout.takeaway_comment_header, null);
    this.cateView = ((CustomLinearLayout)this.headerView.findViewById(R.id.comment_cate));
    this.cateView.init();
    this.cateAdapter = new TagAdapter(this.commentDataSource.reviewCateList);
    this.cateView.setAdapter(this.cateAdapter);
    this.cateView.setOnItemClickListener(new CustomLinearLayout.OnItemClickListener()
    {
      public void onItemClick(LinearLayout paramLinearLayout, View paramView, int paramInt, long paramLong)
      {
        if ((paramView != null) && ((paramView.getTag() instanceof Integer)))
        {
          paramInt = Integer.parseInt(paramView.getTag().toString());
          paramLinearLayout = ((TextView)paramView).getText().toString();
          TakeawayCommentsFragment.this.statisticsEvent("takeaway6", "takeaway6_dish_choosecomment_clk", paramLinearLayout, 0);
          paramView = TakeawayCommentsFragment.this.commentDataSource.getGAUserInfo();
          paramView.title = paramLinearLayout;
          GAHelper.instance().contextStatisticsEvent(TakeawayCommentsFragment.this.activity, "choosecomment", paramView, "tap");
          if (paramInt != TakeawayCommentsFragment.this.commentDataSource.curCommentsType)
          {
            TakeawayCommentsFragment.this.commentDataSource.curCommentsType = paramInt;
            TakeawayCommentsFragment.this.commentDataSource.reset(false);
            TakeawayCommentsFragment.this.commentAdapter.notifyDataSetChanged();
            TakeawayCommentsFragment.this.setupStatusView(TakeawayCommentsDataSource.TakeawayCommentsPageStatus.INITIAL_LOADING);
            TakeawayCommentsFragment.this.commentDataSource.loadComments();
          }
        }
      }
    });
    this.hasLikedView = this.headerView.findViewById(R.id.haslike_view);
    this.optionCheckbox = ((ImageView)this.headerView.findViewById(R.id.option_checkbox));
    this.optionCheckbox.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = TakeawayCommentsFragment.this.commentDataSource;
        if (!TakeawayCommentsFragment.this.commentDataSource.isLiked);
        for (boolean bool = true; ; bool = false)
        {
          paramView.isLiked = bool;
          TakeawayCommentsFragment.this.commentDataSource.reset(false);
          TakeawayCommentsFragment.this.commentAdapter.notifyDataSetChanged();
          TakeawayCommentsFragment.this.setupStatusView(TakeawayCommentsDataSource.TakeawayCommentsPageStatus.INITIAL_LOADING);
          TakeawayCommentsFragment.this.commentDataSource.loadComments();
          TakeawayCommentsFragment.this.statisticsEvent("takeaway6", "takeaway6_dish_likecomment_clk", TakeawayCommentsFragment.this.commentDataSource.shopId, 0);
          paramView = TakeawayCommentsFragment.this.commentDataSource.getGAUserInfo();
          paramView.title = "只看赞过菜的点评";
          GAHelper.instance().contextStatisticsEvent(TakeawayCommentsFragment.this.activity, "choosecomment", paramView, "tap");
          return;
        }
      }
    });
    this.commentsListView.addHeaderView(this.headerView);
  }

  private void initFooterView()
  {
    this.footerView = this.inflater.inflate(R.layout.takeaway_comment_footer, null);
    this.footerContentView = ((TAOperateItem)this.footerView.findViewById(R.id.content_view));
    this.footerContentView.iconView.setVisibility(8);
    this.footerContentView.contentText.setText("外卖点评太少？去看到店点评");
    this.footerContentView.expandView.setVisibility(0);
    GAUserInfo localGAUserInfo = this.commentDataSource.getGAUserInfo();
    GAHelper.instance().contextStatisticsEvent(this.activity, "shopcomment", localGAUserInfo, "view");
    this.footerContentView.setBorder(TAOperateItem.BORDER.BOTH);
    this.footerContentView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = Uri.parse("dianping://review").buildUpon().appendQueryParameter("id", TakeawayCommentsFragment.this.commentDataSource.shopId).appendQueryParameter("shopname", TakeawayCommentsFragment.this.commentDataSource.shopName).toString();
        TakeawayCommentsFragment.this.startActivity(paramView);
        TakeawayCommentsFragment.this.statisticsEvent("takeaway6", "takeaway6_dish_shopcomment_clk", TakeawayCommentsFragment.this.commentDataSource.shopId, 0);
        paramView = TakeawayCommentsFragment.this.commentDataSource.getGAUserInfo();
        GAHelper.instance().contextStatisticsEvent(TakeawayCommentsFragment.this.activity, "shopcomment", paramView, "tap");
      }
    });
  }

  public static TakeawayCommentsFragment newInstance(String paramString1, String paramString2)
  {
    TakeawayCommentsFragment localTakeawayCommentsFragment = new TakeawayCommentsFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("shopid", paramString1);
    localBundle.putString("shopname", paramString2);
    localTakeawayCommentsFragment.setArguments(localBundle);
    return localTakeawayCommentsFragment;
  }

  private void setupStatusView(TakeawayCommentsDataSource.TakeawayCommentsPageStatus paramTakeawayCommentsPageStatus)
  {
    this.statusView.removeAllViews();
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
    switch (7.$SwitchMap$com$dianping$takeaway$entity$TakeawayCommentsDataSource$TakeawayCommentsPageStatus[paramTakeawayCommentsPageStatus.ordinal()])
    {
    default:
    case 3:
    case 4:
      while (true)
      {
        this.commentsListView.setVisibility(8);
        this.statusView.setVisibility(0);
        return;
        paramTakeawayCommentsPageStatus = this.inflater.inflate(R.layout.loading_item_fullscreen, null);
        this.statusView.addView(paramTakeawayCommentsPageStatus, localLayoutParams);
        continue;
        paramTakeawayCommentsPageStatus = this.inflater.inflate(R.layout.takeaway_empty_item, null);
        ((TextView)paramTakeawayCommentsPageStatus.findViewById(16908308)).setText("网络不给力哦");
        localObject = (Button)paramTakeawayCommentsPageStatus.findViewById(R.id.btn_change);
        ((Button)localObject).setText("重新加载");
        ((Button)localObject).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            TakeawayCommentsFragment.this.setupStatusView(TakeawayCommentsDataSource.TakeawayCommentsPageStatus.INITIAL_LOADING);
            TakeawayCommentsFragment.this.commentDataSource.loadComments();
          }
        });
        this.statusView.addView(paramTakeawayCommentsPageStatus, localLayoutParams);
      }
    case 2:
    }
    paramTakeawayCommentsPageStatus = this.inflater.inflate(R.layout.takeaway_comment_empty, null);
    Object localObject = (NovaButton)paramTakeawayCommentsPageStatus.findViewById(R.id.shop_review_entr);
    ((NovaButton)localObject).setText("去看到店点评(" + this.commentDataSource.dpReviewCount + ")");
    if (this.commentDataSource.showEntrance);
    for (int i = 0; ; i = 8)
    {
      ((NovaButton)localObject).setVisibility(i);
      if (this.commentDataSource.showEntrance)
      {
        GAUserInfo localGAUserInfo = this.commentDataSource.getGAUserInfo();
        GAHelper.instance().contextStatisticsEvent(this.activity, "shopcomment", localGAUserInfo, "view");
      }
      ((NovaButton)localObject).setGAString("shopcomment");
      ((NovaButton)localObject).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          ((NovaButton)paramView).gaUserInfo.shop_id = Integer.valueOf(Integer.parseInt(TakeawayCommentsFragment.this.commentDataSource.shopId));
          paramView = Uri.parse("dianping://review").buildUpon().appendQueryParameter("id", TakeawayCommentsFragment.this.commentDataSource.shopId).appendQueryParameter("shopname", TakeawayCommentsFragment.this.commentDataSource.shopName).toString();
          TakeawayCommentsFragment.this.startActivity(paramView);
          TakeawayCommentsFragment.this.statisticsEvent("takeaway6", "takeaway6_dish_shopcomment_clk", TakeawayCommentsFragment.this.commentDataSource.shopId, 0);
        }
      });
      if (((NovaButton)localObject).getVisibility() == 0)
        statisticsEvent("takeaway6", "takeaway6_dish_shopcomment_show", this.commentDataSource.shopId, 0);
      this.statusView.addView(paramTakeawayCommentsPageStatus, localLayoutParams);
      break;
    }
  }

  private void updateHeaderFooterView()
  {
    int j = 8;
    this.cateAdapter.notifyDataSetChanged();
    Object localObject = this.cateView;
    if (this.commentDataSource.reviewCateList.isEmpty())
    {
      i = 8;
      ((CustomLinearLayout)localObject).setVisibility(i);
      this.optionCheckbox.setSelected(this.commentDataSource.isLiked);
      localObject = this.hasLikedView;
      i = j;
      if (this.commentDataSource.hasLikedChoice)
        i = 0;
      ((View)localObject).setVisibility(i);
      ViewUtils.setVisibilityAndContent(this.footerContentView.expandText, this.commentDataSource.dpReviewCount);
      if ((!this.commentDataSource.showEntrance) || (this.commentDataSource.noComment))
        break label164;
    }
    label164: for (int i = 1; ; i = 0)
    {
      this.commentsListView.removeFooterView(this.footerView);
      if (i != 0)
      {
        this.commentsListView.addFooterView(this.footerView);
        statisticsEvent("takeaway6", "takeaway6_dish_shopcomment_show", this.commentDataSource.shopId, 0);
      }
      return;
      i = 0;
      break;
    }
  }

  public void loadCommentsListFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, TakeawayCommentsDataSource.TakeawayCommentsPageStatus paramTakeawayCommentsPageStatus)
  {
    if (paramTakeawayNetLoadStatus == TakeawayNetLoadStatus.STATUS_SUCCESS)
      switch (7.$SwitchMap$com$dianping$takeaway$entity$TakeawayCommentsDataSource$TakeawayCommentsPageStatus[paramTakeawayCommentsPageStatus.ordinal()])
      {
      default:
      case 1:
      case 2:
      }
    do
    {
      return;
      updateHeaderFooterView();
      this.commentAdapter.notifyDataSetChanged();
      this.commentsListView.setVisibility(0);
      this.statusView.setVisibility(8);
      return;
      this.commentDataSource.reset(true);
      this.commentAdapter.notifyDataSetChanged();
      setupStatusView(paramTakeawayCommentsPageStatus);
      return;
    }
    while (paramTakeawayNetLoadStatus != TakeawayNetLoadStatus.STATUS_FAILED);
    this.commentDataSource.reset(true);
    this.commentAdapter.notifyDataSetChanged();
    setupStatusView(paramTakeawayCommentsPageStatus);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    setupStatusView(TakeawayCommentsDataSource.TakeawayCommentsPageStatus.INITIAL_LOADING);
    this.commentDataSource.loadComments();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.commentDataSource = new TakeawayCommentsDataSource(this);
    this.commentDataSource.setDataLoadListener(this);
    if (paramBundle != null)
      this.commentDataSource.shopId = paramBundle.getString("shopid");
    for (this.commentDataSource.shopName = paramBundle.getString("shopname"); ; this.commentDataSource.shopName = paramBundle.getString("shopname"))
    {
      this.activity = ((TakeawayMenuActivity)getActivity());
      this.inflater = LayoutInflater.from(this.activity);
      return;
      paramBundle = getArguments();
      this.commentDataSource.shopId = paramBundle.getString("shopid");
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.wholeView = paramLayoutInflater.inflate(R.layout.takeaway_comment_fragment, null);
    this.statusView = ((RelativeLayout)this.wholeView.findViewById(R.id.status_view));
    this.commentsListView = ((ListView)this.wholeView.findViewById(R.id.comment_list_view));
    this.commentsListView.setOnScrollListener(new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
      {
        if ((paramInt == 0) && (TakeawayCommentsFragment.this.activity.closeCartBtn != null))
          TakeawayCommentsFragment.this.activity.closeCartBtn.invalidate();
      }
    });
    addHeaderView();
    initFooterView();
    this.commentAdapter = new TakeawayCommentAdapter();
    this.commentsListView.setAdapter(this.commentAdapter);
    return this.wholeView;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.commentDataSource != null)
      this.commentDataSource.onDestroy();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("shopid", this.commentDataSource.shopId);
    paramBundle.putString("shopname", this.commentDataSource.shopName);
    super.onSaveInstanceState(paramBundle);
  }

  class CommentViewHolder
  {
    TextView commentTimeView;
    TextView contentView;
    TextView deliTimeView;
    LinearLayout recomDishsView;
    View recommendView;
    TAStarView taStarView;
    TextView userNameView;

    CommentViewHolder(View arg2)
    {
      Object localObject;
      this.userNameView = ((TextView)localObject.findViewById(R.id.user_name));
      this.taStarView = ((TAStarView)localObject.findViewById(R.id.ta_star_view));
      this.deliTimeView = ((TextView)localObject.findViewById(R.id.delivery_time));
      this.commentTimeView = ((TextView)localObject.findViewById(R.id.comment_time));
      this.contentView = ((TextView)localObject.findViewById(R.id.comment_content));
      this.recommendView = localObject.findViewById(R.id.recommend_view);
      this.recomDishsView = ((LinearLayout)localObject.findViewById(R.id.dish_list));
    }
  }

  class TagAdapter extends BasicAdapter
  {
    private int availableWidth;
    private int intervalWidth = ViewUtils.dip2px(TakeawayCommentsFragment.this.activity, 5.0F);
    private int originalWidth = ViewUtils.getScreenWidthPixels(TakeawayCommentsFragment.this.activity) - ViewUtils.dip2px(TakeawayCommentsFragment.this.activity, 12.0F) * 2;
    private List<DPObject> tagData;
    private int tagNum = 0;

    TagAdapter()
    {
      Object localObject;
      this.tagData = localObject;
      this.availableWidth = this.originalWidth;
    }

    public int getCount()
    {
      return this.tagData.size();
    }

    public Object getItem(int paramInt)
    {
      return this.tagData.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      boolean bool = false;
      paramView = (DPObject)this.tagData.get(paramInt);
      int i = paramView.getInt("Type");
      String str = paramView.getString("Message");
      paramViewGroup = TakeawayCommentsFragment.this.inflater.inflate(R.layout.takeaway_short_tag, ((CustomLinearLayout)paramViewGroup).getCurSubLinearLayout(), false);
      paramView = (TextView)paramViewGroup;
      int j = ViewUtils.getTextViewWidth(paramView, str) + this.intervalWidth;
      if ((paramInt == 0) || ((j > this.availableWidth) && (this.tagNum >= 2)))
      {
        this.availableWidth = (this.originalWidth - j);
        this.tagNum = 0;
        paramViewGroup = new LinearLayout(TakeawayCommentsFragment.this.activity);
        ((LinearLayout)paramViewGroup).setOrientation(0);
        paramView = (TextView)TakeawayCommentsFragment.this.inflater.inflate(R.layout.takeaway_short_tag, (LinearLayout)paramViewGroup, false);
        ((LinearLayout)paramViewGroup).addView(paramView);
        this.tagNum += 1;
        paramView.setText(str);
        paramView.setTag(Integer.valueOf(i));
        if (i == TakeawayCommentsFragment.this.commentDataSource.curCommentsType)
          bool = true;
        paramView.setSelected(bool);
        if (!bool)
          break label252;
      }
      label252: for (paramInt = TakeawayCommentsFragment.this.getResources().getColor(R.color.light_red); ; paramInt = TakeawayCommentsFragment.this.getResources().getColor(R.color.text_gray_color))
      {
        paramView.setTextColor(paramInt);
        return paramViewGroup;
        this.availableWidth -= j;
        break;
      }
    }
  }

  class TakeawayCommentAdapter extends BasicAdapter
  {
    int height = 0;
    int intervalWidth = ViewUtils.dip2px(TakeawayCommentsFragment.this.activity, 5.0F);
    int originalWidth = ViewUtils.getScreenWidthPixels(TakeawayCommentsFragment.this.activity) - ViewUtils.dip2px(TakeawayCommentsFragment.this.activity, 12.0F) * 2 - 27;

    TakeawayCommentAdapter()
    {
    }

    private TextView createRecomDishView(DPObject paramDPObject, ViewGroup paramViewGroup)
    {
      boolean bool = true;
      paramViewGroup = (TextView)TakeawayCommentsFragment.this.inflater.inflate(R.layout.takeaway_short_recomdish, paramViewGroup, false);
      paramViewGroup.setMaxWidth(this.originalWidth / 2 - this.intervalWidth);
      paramViewGroup.setText(paramDPObject.getString("Title"));
      if (paramDPObject.getInt("Type") == 1)
      {
        paramViewGroup.setEnabled(bool);
        if (!bool)
          break label121;
      }
      label121: for (int i = TakeawayCommentsFragment.this.getResources().getColor(R.color.light_red); ; i = TakeawayCommentsFragment.this.getResources().getColor(R.color.wm_tag_disable))
      {
        paramViewGroup.setTextColor(i);
        paramViewGroup.setTag(Integer.valueOf(paramDPObject.getInt("DishId")));
        paramViewGroup.setOnClickListener(new View.OnClickListener(paramViewGroup)
        {
          public void onClick(View paramView)
          {
            TakeawayCommentsFragment.this.activity.addClickListener.onClick(this.val$recomDishView);
          }
        });
        return paramViewGroup;
        bool = false;
        break;
      }
    }

    public int getCount()
    {
      int i = TakeawayCommentsFragment.this.commentDataSource.commentsList.size();
      if (TakeawayCommentsFragment.this.commentDataSource.isEnd)
      {
        if (!TakeawayCommentsFragment.this.commentDataSource.noComment)
          return i;
        return i + 1;
      }
      return i + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < TakeawayCommentsFragment.this.commentDataSource.commentsList.size())
        return TakeawayCommentsFragment.this.commentDataSource.commentsList.get(paramInt);
      if (TakeawayCommentsFragment.this.commentDataSource.noComment)
        return EMPTY;
      return LOADING;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        localObject = (DPObject)localObject;
        int i;
        label219: DPObject localDPObject;
        int j;
        if ((paramView == null) || (paramView.getTag() == null))
        {
          paramView = TakeawayCommentsFragment.this.inflater.inflate(R.layout.takeaway_comment_item, null);
          paramViewGroup = new TakeawayCommentsFragment.CommentViewHolder(TakeawayCommentsFragment.this, paramView);
          ViewUtils.setVisibilityAndContent(paramViewGroup.userNameView, ((DPObject)localObject).getString("UserName"));
          paramViewGroup.taStarView.setScore(((DPObject)localObject).getInt("ShopPower"));
          ViewUtils.setVisibilityAndContent(paramViewGroup.deliTimeView, ((DPObject)localObject).getString("Speed"));
          ViewUtils.setVisibilityAndContent(paramViewGroup.commentTimeView, ((DPObject)localObject).getString("Time"));
          ViewUtils.setVisibilityAndContent(paramViewGroup.contentView, ((DPObject)localObject).getString("ReviewBody"));
          paramViewGroup.recomDishsView.removeAllViews();
          localObject = ((DPObject)localObject).getArray("LikedDishList");
          if ((localObject == null) || (localObject.length <= 0))
            break label386;
          ArrayList localArrayList = new ArrayList();
          localArrayList.addAll(Arrays.asList(localObject));
          localObject = new LinearLayout(TakeawayCommentsFragment.this.activity);
          ((LinearLayout)localObject).setOrientation(0);
          paramViewGroup.recomDishsView.addView((View)localObject);
          paramInt = this.originalWidth;
          i = 0;
          if (i >= localArrayList.size())
            break label376;
          localDPObject = (DPObject)localArrayList.get(i);
          TextView localTextView = createRecomDishView(localDPObject, (ViewGroup)localObject);
          j = ViewUtils.getTextViewWidth(localTextView, localDPObject.getString("Title")) + this.intervalWidth;
          if ((j >= paramInt) && (((LinearLayout)localObject).getChildCount() > 1))
            break label321;
          ((LinearLayout)localObject).addView(localTextView);
          paramInt -= j;
        }
        while (true)
        {
          i += 1;
          break label219;
          paramViewGroup = (TakeawayCommentsFragment.CommentViewHolder)paramView.getTag();
          break;
          label321: localObject = new LinearLayout(TakeawayCommentsFragment.this.activity);
          ((LinearLayout)localObject).setOrientation(0);
          paramViewGroup.recomDishsView.addView((View)localObject);
          ((LinearLayout)localObject).addView(createRecomDishView(localDPObject, (ViewGroup)localObject));
          paramInt = this.originalWidth - j;
        }
        label376: paramViewGroup.recommendView.setVisibility(0);
        while (true)
        {
          return paramView;
          label386: paramViewGroup.recommendView.setVisibility(8);
        }
      }
      if (localObject == LOADING)
      {
        TakeawayCommentsFragment.this.commentDataSource.loadComments();
        return TakeawayCommentsFragment.this.inflater.inflate(R.layout.loading_item, paramViewGroup, false);
      }
      if (this.height <= 0)
        this.height = (TakeawayCommentsFragment.this.wholeView.getHeight() - TakeawayCommentsFragment.this.headerView.getHeight());
      paramView = TakeawayCommentsFragment.this.inflater.inflate(R.layout.takeaway_comment_empty_partial, paramViewGroup, false);
      paramView.setLayoutParams(new AbsListView.LayoutParams(-1, this.height));
      return (View)paramView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.fragment.TakeawayCommentsFragment
 * JD-Core Version:    0.6.0
 */
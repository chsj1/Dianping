package com.dianping.ugc.review.add;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.widget.RecommendDealView;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.ugc.widget.BannerView;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class RecommendDealReviewActivity extends NovaListActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String COMMON_REVIEW_TIP = "待点评的消费";
  private static final int DEFAULT_COMMON_SHOW_SIZE = 4;
  private static final int DEFAULT_RECOMMENDS_SIZE = 5;
  private static final String DELRECOMMENDURL = "http://m.api.dianping.com/review/delrecommend.bin";
  private static final String MORE_REVIEW_TIP = "更多待点评的消费";
  private static final String RECOMMEND_REVIEW_TIP = "这些店家等您评一评";
  private static final String URL = "http://m.api.dianping.com/review/reviewrecommend.bin";
  private HashMap<Integer, Set<Integer>> checkMap = new HashMap();
  private FrameLayout headerView;
  private boolean isCommonListEmpyty = false;
  private boolean isCommonReqReturnFail = false;
  private boolean isEdit;
  private boolean isNeedReSendRequest = false;
  private boolean isRecommendReqReturnFail = false;
  private Adapter mAdapter;
  private MApiRequest mCommonListReq;
  private Button mDeleteButton;
  private MApiRequest mDeleteReviewReq;
  private View mFooterView;
  private String mHeadButtonText;
  private String mHeadButtonUrl;
  List<Object> mHideCommonList = new ArrayList();
  private List<Object> mHideRecommendList = new ArrayList();
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      int i;
      if ("tuan:add_review_successfully".equals(paramIntent.getAction()))
      {
        i = paramIntent.getIntExtra("shopId", 0);
        if (i == 0)
          break label149;
        paramContext = RecommendDealReviewActivity.this.mResultList.iterator();
        while (paramContext.hasNext())
        {
          paramIntent = paramContext.next();
          if ((!(paramIntent instanceof DPObject)) || (((DPObject)paramIntent).getInt("ID") != i))
            continue;
          RecommendDealReviewActivity.this.mResultList.remove(paramIntent);
          if (RecommendDealReviewActivity.this.mHideRecommendList.size() > 0)
          {
            paramContext = RecommendDealReviewActivity.this.mHideRecommendList.get(0);
            RecommendDealReviewActivity.this.mResultList.add(paramContext);
            RecommendDealReviewActivity.this.mHideRecommendList.remove(paramContext);
          }
          RecommendDealReviewActivity.this.mAdapter.notifyDataSetChanged();
        }
      }
      label149: 
      do
      {
        return;
        while (!paramContext.hasNext())
        {
          do
            i = paramIntent.getIntExtra("orderId", 0);
          while (i == 0);
          paramContext = RecommendDealReviewActivity.this.mResultList.iterator();
        }
        paramIntent = paramContext.next();
      }
      while ((!(paramIntent instanceof DPObject)) || (((DPObject)paramIntent).getInt("RateId") != i));
      RecommendDealReviewActivity.this.mResultList.remove(paramIntent);
      if (RecommendDealReviewActivity.this.mHideRecommendList.size() > 0)
      {
        paramContext = RecommendDealReviewActivity.this.mHideRecommendList.get(0);
        RecommendDealReviewActivity.this.mResultList.add(paramContext);
        RecommendDealReviewActivity.this.mHideRecommendList.remove(paramContext);
      }
      RecommendDealReviewActivity.this.mAdapter.notifyDataSetChanged();
    }
  };
  private MApiRequest mRecommendListReq;
  private ArrayList<Object> mResultList = new ArrayList();
  ArrayList<Object> showedCommonList = new ArrayList();
  ArrayList<Object> showedRecommendList = new ArrayList();

  private void checkAllRequestEmptyOrError()
  {
    if ((this.mCommonListReq != null) || (this.mRecommendListReq != null));
    do
      return;
    while ((this.showedCommonList.size() != 0) || (this.showedRecommendList.size() != 0));
    if ((this.isCommonReqReturnFail) && (this.isRecommendReqReturnFail))
    {
      this.mResultList.clear();
      localArrayList = this.mResultList;
      localAdapter = this.mAdapter;
      localArrayList.add(Adapter.ERROR);
      this.mAdapter.notifyDataSetChanged();
      return;
    }
    this.mResultList.clear();
    ArrayList localArrayList = this.mResultList;
    Adapter localAdapter = this.mAdapter;
    localArrayList.add(Adapter.EMPTY);
    this.mAdapter.notifyDataSetChanged();
  }

  private void cleanChecked()
  {
    this.checkMap.clear();
    setButtonView();
  }

  private void initFooterView()
  {
    this.mFooterView = findViewById(R.id.delete_view);
    this.mDeleteButton = ((Button)this.mFooterView.findViewById(R.id.delete_btn));
    this.mFooterView.setVisibility(8);
    this.mDeleteButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = Uri.parse("http://m.api.dianping.com/review/delrecommend.bin").buildUpon();
        paramView.appendQueryParameter("ids", RecommendDealReviewActivity.this.parseMap2Json());
        if (RecommendDealReviewActivity.this.accountService().token() != null)
          paramView.appendQueryParameter("token", RecommendDealReviewActivity.this.accountService().token());
        RecommendDealReviewActivity.access$402(RecommendDealReviewActivity.this, BasicMApiRequest.mapiGet(paramView.toString(), CacheType.DISABLED));
        RecommendDealReviewActivity.this.mapiService().exec(RecommendDealReviewActivity.this.mDeleteReviewReq, RecommendDealReviewActivity.this);
        RecommendDealReviewActivity.this.showProgressDialog("请稍候...");
      }
    });
  }

  private void initViews()
  {
    this.headerView = new FrameLayout(this);
    this.mAdapter = new Adapter();
    if (this.listView != null)
    {
      this.listView.setBackgroundDrawable(null);
      this.listView.setDivider(null);
      this.listView.addHeaderView(this.headerView);
      this.listView.setAdapter(this.mAdapter);
    }
  }

  private String parseMap2Json()
  {
    JSONObject localJSONObject = new JSONObject();
    Object localObject1 = this.checkMap.keySet();
    if (localObject1 != null)
      try
      {
        localObject1 = ((Set)localObject1).iterator();
        while (((Iterator)localObject1).hasNext())
        {
          int i = ((Integer)((Iterator)localObject1).next()).intValue();
          Object localObject2 = (Set)this.checkMap.get(Integer.valueOf(i));
          if ((localObject2 == null) || (((Set)localObject2).size() <= 0))
            continue;
          localObject2 = new JSONArray((Collection)localObject2);
          localJSONObject.put("" + i, localObject2);
        }
      }
      catch (Exception localException)
      {
      }
    return (String)(String)localJSONObject.toString();
  }

  private void requestList()
  {
    if (this.mCommonListReq != null)
    {
      mapiService().abort(this.mCommonListReq, this, true);
      this.mCommonListReq = null;
    }
    if (this.mRecommendListReq != null)
    {
      mapiService().abort(this.mRecommendListReq, this, true);
      this.mRecommendListReq = null;
    }
    this.isCommonReqReturnFail = false;
    this.isRecommendReqReturnFail = false;
    this.showedRecommendList.clear();
    this.showedCommonList.clear();
    this.mHideCommonList.clear();
    this.mHideRecommendList.clear();
    this.mResultList.clear();
    ArrayList localArrayList = this.mResultList;
    Adapter localAdapter = this.mAdapter;
    localArrayList.add(Adapter.LOADING);
    this.mAdapter.notifyDataSetChanged();
    if (getAccount() == null)
      return;
    this.mCommonListReq = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/review/reviewrecommend.bin").buildUpon().appendQueryParameter("token", getAccount().token()).appendQueryParameter("cityid", String.valueOf(cityId())).appendQueryParameter("type", "1").toString(), CacheType.DISABLED);
    mapiService().exec(this.mCommonListReq, this);
    this.mRecommendListReq = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/review/reviewrecommend.bin").buildUpon().appendQueryParameter("token", getAccount().token()).appendQueryParameter("cityid", String.valueOf(cityId())).appendQueryParameter("type", "0").toString(), CacheType.DISABLED);
    mapiService().exec(this.mRecommendListReq, this);
  }

  private void setIsEdit(boolean paramBoolean)
  {
    this.isEdit = paramBoolean;
    if (paramBoolean)
      showEditTitleBar();
    while (true)
    {
      this.mAdapter.notifyDataSetChanged();
      setButtonView();
      return;
      showDefaultTitleBar();
    }
  }

  private void setlistData()
  {
    this.mResultList.clear();
    this.mResultList.addAll(this.showedCommonList);
    this.mResultList.addAll(this.showedRecommendList);
    this.mAdapter.notifyDataSetChanged();
  }

  private void showBannerView(String paramString1, String paramString2)
  {
    BannerView localBannerView = new BannerView(this);
    localBannerView.setLayoutParams(new FrameLayout.LayoutParams(-1, ViewUtils.dip2px(this, 38.0F)));
    if (BannerView.shouldShow(paramString1))
    {
      localBannerView.setVisibility(0);
      localBannerView.setText(paramString1);
      localBannerView.setUrl(paramString2);
      this.headerView.addView(localBannerView);
    }
  }

  private void showDefaultTitleBar()
  {
    getTitleBar().setLeftView(0, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        RecommendDealReviewActivity.this.onLeftTitleButtonClicked();
      }
    });
    View localView = LayoutInflater.from(this).inflate(R.layout.recommend_deal_right_edit_btn, null, false);
    getTitleBar().addRightViewItem(localView, "remove", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        RecommendDealReviewActivity.this.setIsEdit(true);
      }
    });
    if (this.mResultList.size() <= 1)
      localView.setVisibility(8);
    while (true)
    {
      this.mFooterView.setVisibility(8);
      return;
      localView.setVisibility(0);
    }
  }

  private void showEditTitleBar()
  {
    getTitleBar().setLeftView(-1, null);
    View localView = LayoutInflater.from(this).inflate(R.layout.recommend_deal_right_cencel_btn, null, false);
    getTitleBar().addRightViewItem(localView, "remove", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        RecommendDealReviewActivity.this.cleanChecked();
        RecommendDealReviewActivity.this.setIsEdit(false);
      }
    });
    this.mFooterView.setVisibility(0);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initFooterView();
    setTitle(getString(R.string.ugc_recommendreview));
    initViews();
    paramBundle = new IntentFilter("tuan:add_review_successfully");
    registerReceiver(this.mReceiver, paramBundle);
    if ((getAccount() != null) && (getAccount().token() != null))
    {
      requestList();
      return;
    }
    gotoLogin();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.mCommonListReq != null)
    {
      mapiService().abort(this.mCommonListReq, this, true);
      this.mCommonListReq = null;
    }
    if (this.mRecommendListReq != null)
    {
      mapiService().abort(this.mRecommendListReq, this, true);
      this.mRecommendListReq = null;
    }
    if (this.mDeleteReviewReq != null)
    {
      mapiService().abort(this.mDeleteReviewReq, this, true);
      this.mDeleteReviewReq = null;
    }
    unregisterReceiver(this.mReceiver);
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
      requestList();
    return super.onLogin(paramBoolean);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.mCommonListReq)
    {
      this.mCommonListReq = null;
      this.isCommonReqReturnFail = true;
      checkAllRequestEmptyOrError();
      if ((paramMApiResponse != null) && (paramMApiResponse.message() != null))
        this.mAdapter.mErrMsg = paramMApiResponse.message().content();
    }
    do
    {
      return;
      if (paramMApiRequest != this.mRecommendListReq)
        continue;
      this.mRecommendListReq = null;
      this.isRecommendReqReturnFail = true;
      checkAllRequestEmptyOrError();
      return;
    }
    while (paramMApiRequest != this.mDeleteReviewReq);
    this.mDeleteReviewReq = null;
    Toast.makeText(this, "请求失败，请稍后重试", 0).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    Object localObject;
    int j;
    if (paramMApiRequest == this.mCommonListReq)
    {
      this.mCommonListReq = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        paramMApiResponse = paramMApiRequest.getString("HeadBannerInfo");
        localObject = paramMApiRequest.getString("HeadBannerUrl");
        this.mHeadButtonUrl = paramMApiRequest.getString("HeadButtonUrl");
        this.mHeadButtonText = paramMApiRequest.getString("HeadButtonText");
        if (!TextUtils.isEmpty(paramMApiResponse))
          showBannerView(paramMApiResponse, (String)localObject);
        paramMApiResponse = paramMApiRequest.getArray("List");
        j = paramMApiRequest.getInt("ShowLimit");
        if ((paramMApiResponse != null) && (paramMApiResponse.length != 0))
          break label130;
        this.isCommonListEmpyty = true;
        checkAllRequestEmptyOrError();
      }
    }
    while (true)
    {
      showDefaultTitleBar();
      return;
      label130: int i = j;
      if (j <= 0)
        i = 4;
      this.showedCommonList.clear();
      this.showedCommonList.add("待点评的消费");
      if (paramMApiResponse.length > i)
      {
        this.mHideCommonList.clear();
        j = i;
        while (j < paramMApiResponse.length)
        {
          this.mHideCommonList.add(paramMApiResponse[j]);
          j += 1;
        }
        this.showedCommonList.addAll(Arrays.asList(Arrays.copyOf(paramMApiResponse, i)));
        this.showedCommonList.add("更多待点评的消费");
      }
      while (true)
      {
        setlistData();
        break;
        this.showedCommonList.addAll(Arrays.asList(paramMApiResponse));
      }
      if (paramMApiRequest == this.mRecommendListReq)
      {
        this.mRecommendListReq = null;
        if (!(paramMApiResponse.result() instanceof DPObject))
          continue;
        paramMApiRequest = ((DPObject)paramMApiResponse.result()).getArray("List");
        if ((paramMApiRequest == null) || (paramMApiRequest.length == 0))
        {
          checkAllRequestEmptyOrError();
          continue;
        }
        this.showedRecommendList.clear();
        this.showedRecommendList.add("这些店家等您评一评");
        paramMApiResponse = Arrays.asList(paramMApiRequest);
        paramMApiRequest = paramMApiResponse;
        if (paramMApiResponse.size() > 5)
        {
          paramMApiRequest = paramMApiResponse.subList(5, paramMApiResponse.size()).iterator();
          while (paramMApiRequest.hasNext())
          {
            localObject = (DPObject)paramMApiRequest.next();
            this.mHideRecommendList.add(localObject);
          }
          paramMApiRequest = paramMApiResponse.subList(0, 5);
        }
        paramMApiResponse = new ArrayList();
        paramMApiRequest = paramMApiRequest.iterator();
        while (paramMApiRequest.hasNext())
        {
          localObject = (DPObject)paramMApiRequest.next();
          if (paramMApiResponse.contains(Integer.valueOf(((DPObject)localObject).getInt("ID"))))
            continue;
          this.showedRecommendList.add(localObject);
          paramMApiResponse.add(Integer.valueOf(((DPObject)localObject).getInt("ID")));
        }
        setlistData();
        continue;
      }
      if (paramMApiRequest != this.mDeleteReviewReq)
        continue;
      this.mDeleteReviewReq = null;
      dismissDialog();
      Toast.makeText(this, "删除成功", 0).show();
      cleanChecked();
      setIsEdit(false);
      requestList();
      this.mAdapter.notifyDataSetChanged();
    }
  }

  public void onStart()
  {
    super.onStart();
    if (this.isNeedReSendRequest)
    {
      this.isNeedReSendRequest = false;
      requestList();
    }
  }

  void setButtonView()
  {
    int j = 0;
    int i = 0;
    Object localObject = this.checkMap.keySet();
    if (localObject != null)
    {
      localObject = ((Set)localObject).iterator();
      while (true)
      {
        j = i;
        if (!((Iterator)localObject).hasNext())
          break;
        Set localSet = (Set)this.checkMap.get(((Iterator)localObject).next());
        if ((localSet == null) || (localSet.size() <= 0))
          continue;
        i += localSet.size();
      }
    }
    if (j > 0)
    {
      this.mDeleteButton.setText("删除(" + j + ")");
      this.mDeleteButton.setClickable(true);
      this.mDeleteButton.setBackgroundResource(R.color.review_event_text_color);
      return;
    }
    this.mDeleteButton.setText("删除");
    this.mDeleteButton.setClickable(false);
    this.mDeleteButton.setBackgroundResource(R.color.review_delete_gray_color);
  }

  protected void setupView()
  {
    super.setContentView(R.layout.recommend_deal_review_list);
  }

  void startActivity(String paramString1, String paramString2, boolean paramBoolean)
  {
    if (!TextUtils.isEmpty(paramString1))
    {
      if (paramBoolean)
        this.isNeedReSendRequest = true;
      paramString1 = new Intent("android.intent.action.VIEW", Uri.parse(paramString1).buildUpon().appendQueryParameter("checkdraft", "true").build());
      paramString1.putExtra("referToken", paramString2);
      startActivity(paramString1);
    }
  }

  class Adapter extends BasicAdapter
  {
    boolean isShowListImage = DPActivity.preferences().getBoolean("isShowListImage", true);
    String mErrMsg;

    public Adapter()
    {
    }

    private LinearLayout generateTipLayout(TextView paramTextView)
    {
      LinearLayout localLinearLayout = new LinearLayout(RecommendDealReviewActivity.this);
      localLinearLayout.setBackgroundResource(R.drawable.list_cell_bottom);
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, -2);
      localLayoutParams.weight = 1.0F;
      localLinearLayout.addView(paramTextView, localLayoutParams);
      paramTextView = generateTipView();
      paramTextView.setBackgroundResource(0);
      paramTextView.setText(RecommendDealReviewActivity.this.mHeadButtonText);
      paramTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow, 0);
      paramTextView.setOnClickListener(new RecommendDealReviewActivity.Adapter.5(this));
      localLinearLayout.addView(paramTextView, new LinearLayout.LayoutParams(-2, -2));
      return localLinearLayout;
    }

    private TextView generateTipView()
    {
      TextView localTextView = new TextView(RecommendDealReviewActivity.this);
      localTextView.setTextColor(RecommendDealReviewActivity.this.getResources().getColor(R.color.light_gray));
      localTextView.setTextSize(0, RecommendDealReviewActivity.this.getResources().getDimensionPixelOffset(R.dimen.text_large));
      localTextView.setBackgroundResource(R.drawable.list_cell_bottom);
      localTextView.setFocusable(false);
      localTextView.setSingleLine(true);
      localTextView.setEllipsize(TextUtils.TruncateAt.END);
      int i = ViewUtils.dip2px(RecommendDealReviewActivity.this, 13.0F);
      int j = ViewUtils.dip2px(RecommendDealReviewActivity.this, 8.0F);
      localTextView.setPadding(i, j, i, j);
      return localTextView;
    }

    public int getCount()
    {
      return RecommendDealReviewActivity.this.mResultList.size();
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < RecommendDealReviewActivity.this.mResultList.size())
        return RecommendDealReviewActivity.this.mResultList.get(paramInt);
      return null;
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
        if ((paramView == null) || ((paramView != null) && (!(paramView instanceof RecommendDealView))))
        {
          paramView = (RecommendDealView)LayoutInflater.from(RecommendDealReviewActivity.this).inflate(R.layout.recommend_deal_view, paramViewGroup, false);
          paramView.setReviewItem((DPObject)localObject, RecommendDealReviewActivity.this.isEdit);
          paramView.getReviewBtn().setOnClickListener(new RecommendDealReviewActivity.Adapter.1(this, (DPObject)localObject));
          paramInt = ((DPObject)localObject).getInt("ID");
          i = ((DPObject)localObject).getInt("RecommendType");
          if (RecommendDealReviewActivity.this.isEdit)
          {
            paramViewGroup = (Set)RecommendDealReviewActivity.this.checkMap.get(Integer.valueOf(i));
            if ((paramViewGroup == null) || (!paramViewGroup.contains(Integer.valueOf(paramInt))))
              break label205;
            paramView.setCheckStatus(true);
          }
          label154: paramViewGroup = (DPNetworkImageView)paramView.findViewById(R.id.shop_pic_thumb);
          if (!this.isShowListImage)
            break label213;
          paramViewGroup.setVisibility(0);
        }
        while (true)
        {
          paramView.setOnClickListener(new RecommendDealReviewActivity.Adapter.2(this, i, paramView, paramInt, (DPObject)localObject));
          return paramView;
          paramView = (RecommendDealView)paramView;
          break;
          label205: paramView.setCheckStatus(false);
          break label154;
          label213: paramViewGroup.setVisibility(8);
        }
      }
      if (localObject == ERROR)
      {
        if (!TextUtils.isEmpty(RecommendDealReviewActivity.this.mAdapter.mErrMsg));
        for (localObject = RecommendDealReviewActivity.this.mAdapter.mErrMsg; ; localObject = "请重试")
          return getFailedView((String)localObject, new RecommendDealReviewActivity.Adapter.3(this), paramViewGroup, paramView);
      }
      if (localObject == EMPTY)
        return getEmptyView("没有待评价消费", "", paramViewGroup, paramView);
      if (localObject == LOADING)
        return getLoadingView(paramViewGroup, paramView);
      if ((localObject instanceof String))
      {
        paramView = (String)localObject;
        if ("更多待点评的消费".equalsIgnoreCase(paramView))
        {
          paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.recommend_deal_tg_more_view, paramViewGroup, false);
          paramViewGroup.setClickable(true);
          ((TextView)paramViewGroup.findViewById(R.id.recommend_tg_more)).setText(paramView);
          paramViewGroup.setOnClickListener(new RecommendDealReviewActivity.Adapter.4(this, paramView));
          return paramViewGroup;
        }
        paramViewGroup = generateTipView();
        if ("待点评的消费".equalsIgnoreCase(paramView))
        {
          paramViewGroup.setBackgroundResource(0);
          paramViewGroup.setText("待点评的消费");
          return generateTipLayout(paramViewGroup);
        }
        if ("这些店家等您评一评".equalsIgnoreCase(paramView))
        {
          if (RecommendDealReviewActivity.this.isCommonListEmpyty)
          {
            paramViewGroup.setBackgroundResource(0);
            paramViewGroup.setText("这些店家等您评一评");
            return generateTipLayout(paramViewGroup);
          }
          paramViewGroup.setText("这些店家等您评一评");
        }
        return paramViewGroup;
      }
      return (View)getEmptyView("没有待评价消费", "", paramViewGroup, paramView);
    }

    public boolean isEnabled(int paramInt)
    {
      return paramInt != 0;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.add.RecommendDealReviewActivity
 * JD-Core Version:    0.6.0
 */
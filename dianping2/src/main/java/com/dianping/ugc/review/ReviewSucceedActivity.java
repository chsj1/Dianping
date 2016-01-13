package com.dianping.ugc.review;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.ugc.widget.RecommendDealView;
import com.dianping.base.widget.MeasuredListView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;

public class ReviewSucceedActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, AdapterView.OnItemClickListener
{
  private static final String TAG = ReviewSucceedActivity.class.getName().toString();
  Adapter adapter;
  int orderId = 0;
  private MApiRequest recommendReviewRequest;
  private MApiRequest reviewEncourageRequest;
  int reviewId = 0;
  ShareHolder share = null;
  int shopId = 0;
  private String shopName;
  private String source;

  private void initBottomAd(Intent paramIntent)
  {
    try
    {
      Object localObject = (DPObject)paramIntent.getParcelableExtra("OperationInfo");
      if (localObject != null)
      {
        paramIntent = (RelativeLayout)findViewById(R.id.review_success_ad);
        paramIntent.setVisibility(0);
        localObject = URLDecoder.decode(((DPObject)localObject).getString("Link"), "utf-8");
        if (localObject != null)
          paramIntent.setOnClickListener(new View.OnClickListener((String)localObject)
          {
            public void onClick(View paramView)
            {
              ReviewSucceedActivity.this.startActivity(this.val$link);
            }
          });
      }
      return;
    }
    catch (java.lang.Throwable paramIntent)
    {
    }
  }

  private void initCommendShops(DPObject paramDPObject)
  {
    paramDPObject = paramDPObject.getArray("List");
    if ((paramDPObject != null) && (paramDPObject.length > 0))
    {
      DPObject[] arrayOfDPObject = new DPObject[paramDPObject.length];
      int i = 0;
      while (i < paramDPObject.length)
      {
        arrayOfDPObject[i] = paramDPObject[i];
        i += 1;
      }
      this.adapter.appendReview(arrayOfDPObject);
    }
  }

  private void initData(Intent paramIntent)
  {
    this.reviewId = paramIntent.getIntExtra("reviewId", 0);
    updateEncourageView((DPObject)paramIntent.getParcelableExtra("ReviewEncourage"));
    if ((paramIntent.getStringExtra("Title") != null) && (paramIntent.getStringExtra("Url") != null))
    {
      this.share = new ShareHolder();
      this.share.title = paramIntent.getStringExtra("Title");
      this.share.imageUrl = paramIntent.getStringExtra("IconUrl");
      this.share.webUrl = paramIntent.getStringExtra("Url");
    }
  }

  private void requestEncourageData()
  {
    String str = accountService().token();
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/getreviewresult.bin").buildUpon();
    localBuilder.appendQueryParameter("token", str);
    localBuilder.appendQueryParameter("reviewid", String.valueOf(this.reviewId));
    this.reviewEncourageRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.reviewEncourageRequest, this);
  }

  private void requestRecommendReview()
  {
    String str = accountService().token();
    int i = cityId();
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/reviewrecommend.bin").buildUpon();
    localBuilder.appendQueryParameter("token", str);
    localBuilder.appendQueryParameter("cityid", String.valueOf(i));
    localBuilder.appendQueryParameter("type", "2");
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    localBuilder.appendQueryParameter("orderid", String.valueOf(this.orderId));
    this.recommendReviewRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.recommendReviewRequest, this);
  }

  private void setupView()
  {
    super.setContentView(R.layout.review_succeed_view);
    super.setTitleButton("关闭", this);
    MeasuredListView localMeasuredListView = (MeasuredListView)findViewById(R.id.commend_shops);
    localMeasuredListView.setAdapter(this.adapter);
    localMeasuredListView.setOnItemClickListener(this);
  }

  private void startAddReviewActivity(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    String str;
    do
    {
      return;
      str = paramDPObject.getString("ActionURL");
    }
    while (TextUtils.isEmpty(str));
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str).buildUpon().appendQueryParameter("checkdraft", "true").build()));
    statisticsEvent("addreview5", "addreview5_submit_recommend_item", paramDPObject.getString("CommendReason") + "|" + paramDPObject.getInt("ID"), 0);
  }

  private void updateEncourageView(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    Object localObject = paramDPObject.getStringArray("EncourageTips");
    TextView localTextView = (TextView)findViewById(R.id.review_success_title);
    localObject = TextUtils.jsonParseText(localObject[0]);
    findViewById(R.id.review_success_encourage_subtitle_lay).setVisibility(8);
    if (paramDPObject.getInt("ScoreState") == 1)
    {
      localTextView.setText((CharSequence)localObject);
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          ReviewSucceedActivity.this.requestEncourageData();
        }
      }
      , 2000L);
      return;
    }
    if (paramDPObject.getInt("EncourageStyle") == 1)
    {
      localTextView.setText((CharSequence)localObject);
      return;
    }
    findViewById(R.id.review_success_encourage_subtitle_lay).setVisibility(0);
    localTextView.setText("感谢您的点评!");
    ((TextView)findViewById(R.id.review_success_sub_title)).setText((CharSequence)localObject);
    localTextView = (TextView)findViewById(R.id.review_success_link_text);
    localTextView.setText(paramDPObject.getString("LinkText"));
    localTextView.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        ReviewSucceedActivity.this.startActivity(this.val$reviewEncourage.getString("Link"));
      }
    });
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.title_button)
      finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent();
    try
    {
      this.orderId = Integer.parseInt(paramBundle.getStringExtra("orderId"));
      this.shopName = paramBundle.getStringExtra("shopName");
    }
    catch (NumberFormatException localNumberFormatException2)
    {
      try
      {
        this.shopId = Integer.parseInt(paramBundle.getStringExtra("shopId"));
        setTitle(this.shopName);
        this.adapter = new Adapter(this);
        setupView();
        initData(paramBundle);
        initBottomAd(paramBundle);
        this.source = getIntent().getData().getQueryParameter("source");
        paramBundle = new Intent("tuan:add_review_successfully");
        paramBundle.putExtra("shopId", this.shopId);
        paramBundle.putExtra("orderId", this.orderId);
        sendBroadcast(paramBundle);
        requestRecommendReview();
        return;
        localNumberFormatException1 = localNumberFormatException1;
        localNumberFormatException1.printStackTrace();
      }
      catch (NumberFormatException localNumberFormatException2)
      {
        while (true)
          localNumberFormatException2.printStackTrace();
      }
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.recommendReviewRequest != null)
    {
      mapiService().abort(this.recommendReviewRequest, null, true);
      this.recommendReviewRequest = null;
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramAdapterView.getItemAtPosition(paramInt) == BasicAdapter.FOOT)
    {
      paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://recommenddealreview"));
      paramAdapterView.setFlags(67108864);
      statisticsEvent("addreview5", "addreview5_submit_recommend_more", "", 0);
      startActivity(paramAdapterView);
      finish();
    }
    do
      return;
    while (!(paramAdapterView.getItemAtPosition(paramInt) instanceof DPObject));
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(((DPObject)paramAdapterView.getItemAtPosition(paramInt)).getString("DetailURL"))));
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.recommendReviewRequest)
      this.recommendReviewRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.recommendReviewRequest)
    {
      this.recommendReviewRequest = null;
      findViewById(R.id.commend_shops).setVisibility(0);
      initCommendShops((DPObject)paramMApiResponse.result());
    }
    do
    {
      do
        return;
      while (paramMApiRequest != this.reviewEncourageRequest);
      this.reviewEncourageRequest = null;
    }
    while (!(paramMApiResponse.result() instanceof DPObject));
    updateEncourageView((DPObject)paramMApiResponse.result());
  }

  class Adapter extends BasicAdapter
  {
    ArrayList<DPObject> commendShops = new ArrayList();
    private final int dip10;

    public Adapter(Context arg2)
    {
      Context localContext;
      this.dip10 = ViewUtils.dip2px(localContext, 10.0F);
    }

    public void appendReview(DPObject[] paramArrayOfDPObject)
    {
      this.commendShops.clear();
      this.commendShops.addAll(Arrays.asList(paramArrayOfDPObject));
      notifyDataSetChanged();
    }

    public int getCount()
    {
      int i = this.commendShops.size();
      if (i == 0)
        return 0;
      return i + 2;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt == 0)
        return HEAD;
      if (paramInt == getCount() - 1)
        return FOOT;
      if (this.commendShops.size() != 0)
        return this.commendShops.get(paramInt - 1);
      return null;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject2 = null;
      Object localObject1 = null;
      Object localObject3 = getItem(paramInt);
      if (localObject3 == HEAD)
      {
        if (paramView == null);
        while (true)
        {
          localObject1 = (NovaTextView)(NovaTextView)localObject1;
          paramView = (View)localObject1;
          if (localObject1 == null)
          {
            paramView = new NovaTextView(paramViewGroup.getContext());
            paramView.setPadding(this.dip10, this.dip10, this.dip10, this.dip10);
            paramView.setGravity(19);
            paramView.setTextColor(ReviewSucceedActivity.this.getResources().getColor(R.color.light_gray));
            paramView.setTextSize(0, ReviewSucceedActivity.this.getResources().getDimension(R.dimen.text_medium));
            paramView.setText("你可能还想点评");
            paramView.setTag(HEAD);
          }
          return paramView;
          if (paramView.getTag() != HEAD)
            continue;
          localObject1 = paramView;
        }
      }
      if (localObject3 == FOOT)
      {
        if (paramView == null)
          localObject1 = localObject2;
        while (true)
        {
          localObject1 = (NovaTextView)(NovaTextView)localObject1;
          paramView = (View)localObject1;
          if (localObject1 != null)
            break;
          paramView = new NovaTextView(paramViewGroup.getContext());
          paramView.setText("更多待点评");
          paramView.setBackgroundResource(R.drawable.list_item);
          paramView.setTextColor(ReviewSucceedActivity.this.getResources().getColor(R.color.deep_gray));
          paramView.setTextSize(0, ReviewSucceedActivity.this.getResources().getDimension(R.dimen.text_medium_1));
          paramView.setGravity(17);
          paramView.setPadding(this.dip10, this.dip10, this.dip10, this.dip10);
          paramView.setTag(FOOT);
          return paramView;
          localObject1 = localObject2;
          if (paramView.getTag() != FOOT)
            continue;
          localObject1 = paramView;
        }
      }
      if ((localObject3 instanceof DPObject))
      {
        localObject1 = (DPObject)localObject3;
        if ((paramView == null) || ((paramView != null) && (!(paramView instanceof RecommendDealView))));
        for (paramView = (RecommendDealView)LayoutInflater.from(ReviewSucceedActivity.this).inflate(R.layout.recommend_deal_view, paramViewGroup, false); ; paramView = (RecommendDealView)paramView)
        {
          paramView.findViewById(R.id.shop_pic_thumb).setVisibility(0);
          paramView.setReviewItem((DPObject)localObject1, false);
          paramView.getReviewBtn().setOnClickListener(new ReviewSucceedActivity.Adapter.1(this, (DPObject)localObject1));
          return paramView;
        }
      }
      return (View)null;
    }

    public int getViewTypeCount()
    {
      return 5;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.ReviewSucceedActivity
 * JD-Core Version:    0.6.0
 */
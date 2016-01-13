package com.dianping.shopinfo.hotel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.widget.CustomLinearLayout;
import com.dianping.base.widget.CustomLinearLayout.OnItemClickListener;
import com.dianping.base.widget.ReviewItem;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HotelReviewAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, CustomLinearLayout.OnItemClickListener
{
  private static final String CELL_REVIEW = "3000Reivew.";
  DPObject booking_price;
  DPObject error_reviews;
  boolean isOverseaCity = true;
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      String str;
      if (HotelReviewAgent.this.isOverseaCity)
      {
        paramView = new StringBuilder();
        paramView.append("dianping://hoteloverseareview?id=");
        paramView.append(HotelReviewAgent.this.shopId());
        if (HotelReviewAgent.this.getShop() != null)
        {
          paramView.append("&shopname=").append(HotelReviewAgent.this.getShop().getString("Name"));
          str = HotelReviewAgent.this.getShop().getString("BranchName");
          if (!TextUtils.isEmpty(str))
            paramView.append("(").append(str).append(")");
          paramView.append("&shopstatus=").append(HotelReviewAgent.this.getShop().getInt("Status"));
        }
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView.toString()));
        paramView.putExtra("shop", HotelReviewAgent.this.getShop());
        paramView.putExtra("reviewList", HotelReviewAgent.this.reviewList);
        paramView.putExtra("booking_price", HotelReviewAgent.this.booking_price);
        HotelReviewAgent.this.getFragment().startActivity(paramView);
      }
      label575: label591: 
      do
        while (true)
        {
          return;
          if (HotelReviewAgent.this.reviewList == null)
            break;
          if ((HotelReviewAgent.this.reviewList.getArray("List") != null) && (HotelReviewAgent.this.reviewList.getArray("List").length == 0))
          {
            HotelReviewAgent.this.addReview();
            return;
          }
          paramView = new StringBuilder();
          if (HotelReviewAgent.this.isHotelType())
          {
            paramView.append("dianping://hotelreview?id=");
            paramView.append(HotelReviewAgent.this.shopId());
            if (HotelReviewAgent.this.getShop() != null)
            {
              paramView.append("&shopname=").append(HotelReviewAgent.this.getShop().getString("Name"));
              str = HotelReviewAgent.this.getShop().getString("BranchName");
              if (!TextUtils.isEmpty(str))
                paramView.append("(").append(str).append(")");
              paramView.append("&shopstatus=").append(HotelReviewAgent.this.getShop().getInt("Status"));
            }
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView.toString()));
            paramView.putExtra("shop", HotelReviewAgent.this.getShop());
            HotelReviewAgent.this.getFragment().startActivity(paramView);
            paramView = new ArrayList();
            paramView.add(new BasicNameValuePair("shopid", HotelReviewAgent.this.shopId() + ""));
            if (!HotelReviewAgent.this.isTravelType())
              break label575;
            HotelReviewAgent.this.statisticsEvent("shopinfo5", "shopinfo5_viewreview2", "", 0, paramView);
          }
          while (true)
          {
            if (!HotelReviewAgent.this.isWeddingType())
              break label591;
            paramView = new ArrayList();
            paramView.add(new BasicNameValuePair("shopid", HotelReviewAgent.this.shopId() + ""));
            HotelReviewAgent.this.statisticsEvent("shopinfow", "shopinfow_viewreview", "", 0, paramView);
            return;
            paramView.append("dianping://review?id=");
            break;
            HotelReviewAgent.this.statisticsEvent("shopinfo5", "shopinfo5_viewreview", "", 0, paramView);
          }
        }
      while (HotelReviewAgent.this.error_reviews == null);
      HotelReviewAgent.this.error_reviews = null;
      HotelReviewAgent.this.sendRequest_reviews();
      HotelReviewAgent.this.dispatchAgentChanged(false);
    }
  };
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      HotelReviewAgent.this.sendRequest_reviews();
    }
  };
  private View.OnClickListener reivewListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      HotelReviewAgent.this.mListener.onClick(paramView);
      paramView = new GAUserInfo();
      paramView.shop_id = Integer.valueOf(HotelReviewAgent.this.shopId());
      paramView.query_id = HotelReviewAgent.this.getFragment().getStringParam("query_id");
      DPObject localDPObject = HotelReviewAgent.this.getShop();
      if (localDPObject != null)
        paramView.category_id = Integer.valueOf(localDPObject.getInt("CategoryID"));
      GAHelper.instance().contextStatisticsEvent(HotelReviewAgent.this.getContext(), "hotel_review", paramView, "tap");
    }
  };
  MApiRequest request_reviews;
  DPObject reviewList;
  private int screenWidth;
  List<DPObject> shortReviewObjects = new ArrayList();
  private View.OnClickListener titleListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      HotelReviewAgent.this.mListener.onClick(paramView);
      paramView = new GAUserInfo();
      paramView.shop_id = Integer.valueOf(HotelReviewAgent.this.shopId());
      paramView.query_id = HotelReviewAgent.this.getFragment().getStringParam("query_id");
      DPObject localDPObject = HotelReviewAgent.this.getShop();
      if (localDPObject != null)
        paramView.category_id = Integer.valueOf(localDPObject.getInt("CategoryID"));
      GAHelper.instance().contextStatisticsEvent(HotelReviewAgent.this.getContext(), "hotel_review_all", paramView, "tap");
    }
  };
  private TextView tv_aveScore;
  private TextView tv_ota;
  private TextView tv_title;

  public HotelReviewAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createDefaultReviewAgent()
  {
    View localView = this.res.inflate(getContext(), R.layout.review_empty_in_shopinfo, getParentView(), false);
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    if (this.isOverseaCity)
    {
      int i = this.reviewList.getInt("GlobalReviewCount");
      localShopinfoCommonCell.hideTitle();
      RelativeLayout localRelativeLayout = (RelativeLayout)this.res.inflate(getContext(), R.layout.hotel_oversea_review_title, null, false);
      this.tv_aveScore = ((TextView)localRelativeLayout.findViewById(R.id.score_avg));
      this.tv_ota = ((TextView)localRelativeLayout.findViewById(R.id.score_ota));
      this.tv_title = ((TextView)localRelativeLayout.findViewById(R.id.title));
      boolean bool;
      if (i == 0)
      {
        bool = false;
        if (i != 0)
          break label220;
      }
      label220: for (View.OnClickListener localOnClickListener = null; ; localOnClickListener = this.titleListener)
      {
        localShopinfoCommonCell.addContent(localRelativeLayout, bool, localOnClickListener);
        if ((this.reviewList != null) && (this.isOverseaCity))
          updateView(this.reviewList);
        if (i == 0)
        {
          localShopinfoCommonCell.addContent(localView, false, null);
          ((Button)localView.findViewById(R.id.review_add)).setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              HotelReviewAgent.this.addReview();
            }
          });
        }
        return localShopinfoCommonCell;
        bool = true;
        break;
      }
    }
    localShopinfoCommonCell.setTitle("网友点评");
    localShopinfoCommonCell.hideArrow();
    localShopinfoCommonCell.addContent(localView, false, null);
    ((Button)localView.findViewById(R.id.review_add)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        HotelReviewAgent.this.mListener.onClick(paramView);
      }
    });
    return localShopinfoCommonCell;
  }

  private View createHotelPraiseAgent()
  {
    if ((this.shortReviewObjects == null) || (this.shortReviewObjects.size() <= 0))
      return null;
    CustomLinearLayout localCustomLinearLayout = new CustomLinearLayout(getContext());
    localCustomLinearLayout.setOrientation(1);
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-1, -2);
    localLayoutParams.topMargin = ViewUtils.dip2px(getContext(), 0.0F);
    localLayoutParams.bottomMargin = ViewUtils.dip2px(getContext(), 12.0F);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 10.0F);
    localCustomLinearLayout.setLayoutParams(localLayoutParams);
    localCustomLinearLayout.init();
    localCustomLinearLayout.setMaxLine(2);
    localCustomLinearLayout.setAdapter(new ReviewTagAdapter(this.shortReviewObjects));
    localCustomLinearLayout.setOnItemClickListener(this);
    return localCustomLinearLayout;
  }

  private View createReviewAgent(DPObject[] paramArrayOfDPObject)
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    Object localObject1;
    int i;
    if (!this.isOverseaCity)
    {
      localObject1 = getShop().getString("ScoreText");
      if (!TextUtils.isEmpty((CharSequence)localObject1))
        if (TextUtils.hasDigitNumber((String)localObject1))
        {
          Object localObject2 = "网友点评" + "(" + this.reviewList.getInt("RecordCount") + ")";
          i = ((String)localObject2).length();
          localObject1 = (String)localObject2 + "\n" + (String)localObject1;
          localShopinfoCommonCell.setHotelSubTitleStyle();
          localObject2 = new SpannableString((CharSequence)localObject1);
          ((SpannableString)localObject2).setSpan(new RelativeSizeSpan(0.7F), i, ((String)localObject1).length(), 33);
          ((SpannableString)localObject2).setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.light_gray)), i, ((String)localObject1).length(), 33);
          localShopinfoCommonCell.setTitleSpannable((SpannableString)localObject2, this.titleListener);
          if (isWeddingShopType())
          {
            localObject1 = new ArrayList();
            ((List)localObject1).add(new BasicNameValuePair("shopid", shopId() + ""));
            statisticsEvent("shopinfoq", "shopinfoq_viewreview", "", 0, (List)localObject1);
          }
        }
    }
    while (true)
    {
      if (!this.isOverseaCity)
      {
        localObject1 = createHotelPraiseAgent();
        if (localObject1 != null)
        {
          localShopinfoCommonCell.addContent((View)localObject1, false, null);
          localObject1 = localShopinfoCommonCell.findViewById(R.id.middle_divder_line);
          if (localObject1 != null)
            ((View)localObject1).setVisibility(8);
        }
      }
      if (paramArrayOfDPObject == null)
        break label590;
      i = 0;
      while ((i < paramArrayOfDPObject.length) && (i < 2))
      {
        localObject1 = (ReviewItem)this.res.inflate(getContext(), R.layout.review_item_in_shopinfo, getParentView(), false);
        ((ReviewItem)localObject1).setReview(paramArrayOfDPObject[i]);
        ((ReviewItem)localObject1).setReviewCountVisible(8);
        ((ReviewItem)localObject1).setTag("hotel_review");
        localShopinfoCommonCell.addContent((View)localObject1, false, this.reivewListener);
        i += 1;
      }
      localShopinfoCommonCell.setTitle("网友点评", this.titleListener);
      localShopinfoCommonCell.setSubTitle("(" + this.reviewList.getInt("RecordCount") + ")");
      break;
      localShopinfoCommonCell.setTitle("网友点评", this.titleListener);
      localShopinfoCommonCell.setSubTitle("(" + this.reviewList.getInt("RecordCount") + ")");
      continue;
      localShopinfoCommonCell.hideTitle();
      localObject1 = (RelativeLayout)this.res.inflate(getContext(), R.layout.hotel_oversea_review_title, null, false);
      this.tv_aveScore = ((TextView)((RelativeLayout)localObject1).findViewById(R.id.score_avg));
      this.tv_ota = ((TextView)((RelativeLayout)localObject1).findViewById(R.id.score_ota));
      this.tv_title = ((TextView)((RelativeLayout)localObject1).findViewById(R.id.title));
      localShopinfoCommonCell.addContent((View)localObject1, true, this.titleListener);
    }
    label590: return (View)(View)localShopinfoCommonCell;
  }

  private void sendRequest_reviews()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("http://m.api.dianping.com/hotel/hotelreview.hotel?");
    localStringBuffer.append("shopid=").append(shopId());
    localStringBuffer.append("&tab=1");
    localStringBuffer.append("&start=0");
    localStringBuffer.append("&limit=2");
    localStringBuffer.append("&filterid=").append(0);
    localStringBuffer.append("&needfilter=").append(1);
    AccountService localAccountService = getFragment().accountService();
    if (localAccountService.token() != null)
      localStringBuffer.append("&token=").append(localAccountService.token());
    this.request_reviews = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request_reviews, this);
  }

  private void updateView(DPObject paramDPObject)
  {
    double d = paramDPObject.getDouble("AvgScore");
    this.tv_aveScore.setText(String.valueOf(d));
    DPObject[] arrayOfDPObject = paramDPObject.getArray("OTAScoreList");
    String str = "";
    Object localObject = str;
    int i;
    if (arrayOfDPObject != null)
    {
      localObject = str;
      if (arrayOfDPObject.length > 0)
      {
        int j = arrayOfDPObject.length;
        i = 0;
        while (true)
        {
          localObject = str;
          if (i >= j)
            break;
          localObject = arrayOfDPObject[i];
          str = str + ((DPObject)localObject).getString("Name") + " " + ((DPObject)localObject).getDouble("Score") + "分  ";
          i += 1;
        }
      }
    }
    if (TextUtils.isEmpty((CharSequence)localObject))
      this.tv_ota.setVisibility(8);
    while (true)
    {
      i = paramDPObject.getInt("GlobalReviewCount");
      this.tv_title.setText("网友点评(" + i + ")");
      return;
      this.tv_ota.setText((CharSequence)localObject);
    }
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

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if (paramAgentMessage.what.equals("com.dianping.shopinfo.hotel.HotelBookingAgent.HOTEL_BOOKING_SEND_BOOK_PRICE"))
      onAgentChanged(paramAgentMessage.body);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((paramBundle != null) && (paramBundle.getParcelable("booking_price") != null))
      this.booking_price = ((DPObject)paramBundle.getParcelable("booking_price"));
    label281: 
    do
    {
      do
      {
        return;
        paramBundle = getShop();
        removeAllCells();
      }
      while (paramBundle == null);
      Object localObject = null;
      if ((this.reviewList != null) && (this.reviewList.getArray("List") != null) && (this.reviewList.getArray("List").length > 0))
        if ((this.reviewList.getObject("OwnerReview") != null) && (new Date(this.reviewList.getArray("List")[0].getTime("Time")).before(new Date(this.reviewList.getObject("OwnerReview").getTime("Time")))))
        {
          paramBundle = new DPObject[2];
          paramBundle[0] = this.reviewList.getObject("OwnerReview");
          paramBundle[1] = this.reviewList.getArray("List")[0];
        }
      while (true)
      {
        if (paramBundle == null)
          break label281;
        addCell("3000Reivew.", createReviewAgent(paramBundle), 0);
        if ((this.reviewList == null) || (!this.isOverseaCity))
          break;
        updateView(this.reviewList);
        return;
        paramBundle = this.reviewList.getArray("List");
        continue;
        paramBundle = localObject;
        if (this.reviewList == null)
          continue;
        paramBundle = localObject;
        if (this.reviewList.getObject("OwnerReview") == null)
          continue;
        paramBundle = new DPObject[1];
        paramBundle[0] = this.reviewList.getObject("OwnerReview");
      }
      if (this.reviewList != null)
        continue;
      if (this.error_reviews == null)
      {
        addCell("3000Reivew.", createLoadingCell(), 16);
        return;
      }
      paramBundle = createErrorCell(new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          HotelReviewAgent.this.error_reviews = null;
          HotelReviewAgent.this.sendRequest_reviews();
          HotelReviewAgent.this.dispatchAgentChanged(false);
        }
      });
      paramBundle.setTag("RETRY");
      addCell("3000Reivew.", paramBundle, 0);
      return;
    }
    while ((this.reviewList == null) || (this.reviewList.getArray("List") == null) || (this.reviewList.getArray("List").length != 0));
    paramBundle = createDefaultReviewAgent();
    paramBundle.setTag("DEFAULT");
    addCell("3000Reivew.", paramBundle, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.screenWidth = ViewUtils.getScreenWidthPixels(getContext());
    if (paramBundle != null)
    {
      this.reviewList = ((DPObject)paramBundle.getParcelable("reviewList"));
      this.error_reviews = ((DPObject)paramBundle.getParcelable("error"));
    }
    if ((this.reviewList == null) && (this.error_reviews == null))
      sendRequest_reviews();
    paramBundle = new IntentFilter("com.dianping.REVIEWREFRESH");
    getContext().registerReceiver(this.receiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.REVIEWDELETE");
    getContext().registerReceiver(this.receiver, paramBundle);
    new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        HotelReviewAgent.this.addReview();
      }
    };
  }

  public void onDestroy()
  {
    if (this.request_reviews != null)
    {
      getFragment().mapiService().abort(this.request_reviews, this, true);
      this.request_reviews = null;
    }
    getContext().unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onItemClick(LinearLayout paramLinearLayout, View paramView, int paramInt, long paramLong)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    paramLinearLayout = (DPObject)paramView.getTag();
    if (isHotelType())
      localStringBuilder.append("dianping://hotelreview?id=");
    while (true)
    {
      localStringBuilder.append(shopId());
      if (getShop() != null)
      {
        localStringBuilder.append("&shopname=").append(getShop().getString("Name"));
        paramView = getShop().getString("BranchName");
        if (!TextUtils.isEmpty(paramView))
          localStringBuilder.append("(").append(paramView).append(")");
        if (paramLinearLayout != null)
        {
          localStringBuilder.append("&labelid=").append(paramLinearLayout.getInt("HotelLabelId"));
          localStringBuilder.append("&filterid=").append(paramLinearLayout.getInt("RankType"));
        }
        localStringBuilder.append("&shopstatus=").append(getShop().getInt("Status"));
      }
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(localStringBuilder.toString()));
      paramView.putExtra("shop", getShop());
      getFragment().startActivity(paramView);
      paramView = new GAUserInfo();
      paramView.shop_id = Integer.valueOf(shopId());
      paramView.query_id = getFragment().getStringParam("query_id");
      if (paramLinearLayout != null)
        paramView.title = paramLinearLayout.getString("Name");
      paramLinearLayout = getShop();
      if (paramLinearLayout != null)
        paramView.category_id = Integer.valueOf(paramLinearLayout.getInt("CategoryID"));
      GAHelper.instance().contextStatisticsEvent(getContext(), "hotel_shortreview", paramView, "tap");
      return;
      localStringBuilder.append("dianping://review?id=");
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request_reviews)
    {
      this.request_reviews = null;
      if (!(paramMApiResponse.error() instanceof DPObject))
        break label44;
    }
    label44: for (this.error_reviews = ((DPObject)paramMApiResponse.error()); ; this.error_reviews = new DPObject())
    {
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request_reviews)
    {
      this.request_reviews = null;
      this.reviewList = ((DPObject)paramMApiResponse.result());
      paramMApiRequest = this.reviewList.getArray("ReviewAbstractList");
      if (paramMApiRequest != null)
      {
        int i = 0;
        while (i < paramMApiRequest.length)
        {
          paramMApiResponse = paramMApiRequest[i];
          if (paramMApiResponse.getInt("RankType") != 0)
            this.shortReviewObjects.add(paramMApiResponse);
          i += 1;
        }
      }
      this.isOverseaCity = this.reviewList.getBoolean("IsOverseaCity");
      setSharedObject("reviewList", this.reviewList);
      this.error_reviews = null;
      dispatchAgentChanged(false);
      if (isHotelType())
      {
        if ((this.reviewList.contains("GlobalReviewCount")) && (this.reviewList.getInt("GlobalReviewCount") > 0))
        {
          this.reviewList = this.reviewList.edit().putInt("RecordCount", this.reviewList.getInt("GlobalReviewCount")).generate();
          setSharedObject("reviewList", this.reviewList);
        }
        handleMessage(new AgentMessage("com.dianping.shopinfo.hotel.HotelReviewAgent.HOTEL_REVIEW_LOAD_DATA_HOTEL_REVIEW"));
      }
      paramMApiRequest = new ArrayList(Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(2) }));
      paramMApiResponse = new ArrayList(Arrays.asList(new Integer[] { Integer.valueOf(158), Integer.valueOf(159) }));
      DPObject localDPObject = getShop();
      if ((localDPObject != null) && (paramMApiResponse.contains(Integer.valueOf(localDPObject.getInt("CategoryID")))) && (paramMApiRequest.contains(Integer.valueOf(localDPObject.getInt("CityID")))))
        dispatchAgentChanged("shopinfo/beautyheader", null);
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("reviewList", this.reviewList);
    localBundle.putParcelable("error", this.error_reviews);
    return localBundle;
  }

  class ReviewTagAdapter extends BasicAdapter
  {
    private int availableWith = this.originalWith;
    private int originalWith = HotelReviewAgent.this.screenWidth - ViewUtils.dip2px(HotelReviewAgent.this.getContext(), 15.0F) * 2;
    private List<DPObject> shortList = new ArrayList();

    public ReviewTagAdapter()
    {
      this.shortList.clear();
      Collection localCollection;
      this.shortList.addAll(localCollection);
    }

    public int getCount()
    {
      return this.shortList.size();
    }

    public Object getItem(int paramInt)
    {
      return this.shortList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return ((DPObject)this.shortList.get(paramInt)).getInt("RankType");
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = (DPObject)getItem(paramInt);
      Object localObject1 = paramView.getString("Name");
      int i = paramView.getInt("Count");
      localObject1 = new StringBuilder((String)localObject1);
      if (i > 0)
        ((StringBuilder)localObject1).append("(").append(i).append(")");
      int k = ViewUtils.getTextViewWidth((TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.review_short_tag, null, false), ((StringBuilder)localObject1).toString(), 12) + ViewUtils.dip2px(HotelReviewAgent.this.getContext(), 12.0F);
      int j = 0;
      i = j;
      if (paramInt > 0)
      {
        i = j;
        if (((DPObject)getItem(paramInt - 1)).getInt("RankType") == 0)
        {
          i = j;
          if (paramView.getInt("RankType") != 0)
            i = 1;
        }
      }
      if ((paramInt == 0) || (k > this.availableWith) || (i != 0))
      {
        this.availableWith = (this.originalWith - k);
        localObject2 = new LinearLayout(HotelReviewAgent.this.getContext());
        ((LinearLayout)localObject2).setOrientation(0);
        paramViewGroup = (NovaTextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.review_short_tag, (LinearLayout)localObject2, false);
        paramViewGroup.setText(((StringBuilder)localObject1).toString());
        ((LinearLayout)localObject2).addView(paramViewGroup);
        paramViewGroup.setTag(paramView);
        return localObject2;
      }
      this.availableWith -= k;
      paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.review_short_tag, ((CustomLinearLayout)paramViewGroup).getCurSubLinearLayout(), false);
      Object localObject2 = (NovaTextView)paramViewGroup;
      ((NovaTextView)localObject2).setText(((StringBuilder)localObject1).toString());
      ((NovaTextView)localObject2).setTag(paramView);
      return (View)(View)paramViewGroup;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelReviewAgent
 * JD-Core Version:    0.6.0
 */
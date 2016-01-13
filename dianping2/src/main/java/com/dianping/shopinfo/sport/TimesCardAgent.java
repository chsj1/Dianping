package com.dianping.shopinfo.sport;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.shopinfo.widget.TuanTicketCell;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;

public class TimesCardAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_TIMES_CARD = "0495timescard.";
  protected static final int MAX_SHOW_NUM = 2;
  private View.OnClickListener expandClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = TimesCardAgent.this;
      if (!TimesCardAgent.this.isExpand);
      for (boolean bool = true; ; bool = false)
      {
        paramView.isExpand = bool;
        TimesCardAgent.this.setExpandAction();
        TimesCardAgent.this.scrollToCenter();
        return;
      }
    }
  };
  protected LinearLayout expandLayout;
  protected NovaRelativeLayout expandView;
  protected boolean isExpand = false;
  private View.OnClickListener itemClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = paramView.getTag();
      if (paramView == null);
      do
      {
        do
          return;
        while (!(paramView instanceof DPObject));
        paramView = ((DPObject)paramView).getString("LinkUrl");
      }
      while (TextUtils.isEmpty(paramView));
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
      TimesCardAgent.this.startActivity(paramView);
    }
  };
  protected DPObject mCardInfo;
  protected NovaLinearLayout mCardLinearLayout;
  protected MApiRequest mRequest;
  protected String moreText = "";

  public TimesCardAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void scrollToCenter()
  {
    if (this.isExpand)
      this.mCardLinearLayout.postDelayed(new Runnable()
      {
        public void run()
        {
          ScrollView localScrollView = TimesCardAgent.this.getFragment().getScrollView();
          localScrollView.setSmoothScrollingEnabled(true);
          try
          {
            localScrollView.requestChildFocus(TimesCardAgent.this.mCardLinearLayout, TimesCardAgent.this.mCardLinearLayout);
            return;
          }
          catch (Exception localException)
          {
          }
        }
      }
      , 200L);
  }

  private void setExpandAction()
  {
    if (this.expandLayout == null)
      return;
    this.expandLayout.postDelayed(new Runnable()
    {
      public void run()
      {
        if (TimesCardAgent.this.isExpand)
          TimesCardAgent.this.expandLayout.setVisibility(0);
        while (true)
        {
          TimesCardAgent.this.setExpandState();
          return;
          TimesCardAgent.this.expandLayout.setVisibility(8);
        }
      }
    }
    , 100L);
  }

  public CommonCell createCardCell(DPObject paramDPObject, int paramInt)
  {
    TuanTicketCell localTuanTicketCell = (TuanTicketCell)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.tuan_cell_shopinfo_icon_v2, getParentView(), false);
    Object localObject1 = (NetworkImageView)localTuanTicketCell.findViewById(16908295);
    if (paramInt == 0)
    {
      localTuanTicketCell.setLeftIcon(R.drawable.detail_cardicon);
      ((NetworkImageView)localObject1).setVisibility(0);
    }
    while (true)
    {
      localTuanTicketCell.setSubTitle(paramDPObject.getString("Title"));
      localTuanTicketCell.setClickable(true);
      localTuanTicketCell.setTag(paramDPObject);
      localTuanTicketCell.setOnClickListener(this.itemClickListener);
      Object localObject3 = paramDPObject.getString("Price");
      Object localObject2 = paramDPObject.getString("OriginPrice");
      localObject1 = new SpannableStringBuilder();
      localObject3 = new SpannableString("￥" + (String)localObject3);
      ((SpannableString)localObject3).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
      ((SpannableString)localObject3).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_title)), 1, ((SpannableString)localObject3).length(), 33);
      ((SpannableString)localObject3).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, ((SpannableString)localObject3).length(), 33);
      ((SpannableStringBuilder)localObject1).append((CharSequence)localObject3);
      ((SpannableStringBuilder)localObject1).append(" ");
      localObject2 = new SpannableString("￥" + (String)localObject2);
      ((SpannableString)localObject2).setSpan(new StrikethroughSpan(), 1, ((SpannableString)localObject2).length(), 33);
      ((SpannableString)localObject2).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_hint)), 0, ((SpannableString)localObject2).length(), 33);
      ((SpannableString)localObject2).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_gray)), 0, ((SpannableString)localObject2).length(), 33);
      ((SpannableStringBuilder)localObject1).append((CharSequence)localObject2);
      localTuanTicketCell.setTitle((CharSequence)localObject1);
      if (paramDPObject.getString("PicUrl") != null)
        ((NetworkImageView)localTuanTicketCell.findViewById(R.id.icon)).setImage(paramDPObject.getString("PicUrl"));
      localObject1 = paramDPObject.getStringArray("PromoList");
      if ((localObject1 != null) && (localObject1.length > 0))
        localTuanTicketCell.setRightText(localObject1[0]);
      paramDPObject = paramDPObject.getString("TimeRange");
      if (!TextUtils.isEmpty(paramDPObject))
        localTuanTicketCell.setSaleCount(paramDPObject);
      localTuanTicketCell.setGAString("fitness_passcard");
      localTuanTicketCell.gaUserInfo.shop_id = Integer.valueOf(shopId());
      localTuanTicketCell.gaUserInfo.index = Integer.valueOf(paramInt);
      return localTuanTicketCell;
      ((NetworkImageView)localObject1).setVisibility(4);
    }
  }

  protected View line()
  {
    View localView = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 47.0F);
    localView.setLayoutParams(localLayoutParams);
    localView.setBackgroundResource(R.color.background_gray);
    return localView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (this.mCardLinearLayout != null)
      return;
    super.onAgentChanged(paramBundle);
    setupView();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null);
    for (boolean bool = false; ; bool = paramBundle.getBoolean("isExpand"))
    {
      this.isExpand = bool;
      if (shopId() > 0)
        break;
      return;
    }
    sendRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mRequest != paramMApiRequest);
    do
    {
      return;
      this.mCardInfo = ((DPObject)paramMApiResponse.result());
    }
    while (this.mCardInfo == null);
    dispatchAgentChanged(false);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putBoolean("isExpand", this.isExpand);
    return localBundle;
  }

  protected void sendRequest()
  {
    this.mRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/fitness/gettimescard.bin").buildUpon().appendQueryParameter("shopid", Integer.toString(shopId())).toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this);
  }

  protected void setExpandState()
  {
    if (this.expandView == null)
      return;
    if (this.isExpand)
    {
      ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_up);
      ((TextView)this.expandView.findViewById(16908308)).setText("收起");
      return;
    }
    ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_down);
    ((TextView)this.expandView.findViewById(16908308)).setText(this.moreText);
    this.expandView.findViewById(16908308).setVisibility(0);
  }

  protected void setupView()
  {
    if (this.mCardInfo == null);
    DPObject[] arrayOfDPObject;
    int j;
    do
    {
      do
      {
        return;
        arrayOfDPObject = this.mCardInfo.getArray("List");
      }
      while (arrayOfDPObject == null);
      j = arrayOfDPObject.length;
    }
    while (j <= 0);
    this.mCardLinearLayout = new NovaLinearLayout(getContext());
    this.mCardLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.mCardLinearLayout.setOrientation(1);
    this.mCardLinearLayout.addView(createCardCell(arrayOfDPObject[0], 0));
    if (j > 1)
    {
      this.mCardLinearLayout.addView(line());
      this.mCardLinearLayout.addView(createCardCell(arrayOfDPObject[1], 1));
    }
    if (j > 2)
    {
      this.expandLayout = new LinearLayout(getContext());
      this.expandLayout.setOrientation(1);
      if (!this.isExpand)
        this.expandLayout.setVisibility(8);
      int i = 2;
      while (i < j)
      {
        this.expandLayout.addView(line());
        this.expandLayout.addView(createCardCell(arrayOfDPObject[i], i));
        i += 1;
      }
      this.mCardLinearLayout.addView(this.expandLayout);
      this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.expand, getParentView(), false));
      this.expandView.setPadding(ViewUtils.dip2px(getContext(), 47.0F), 0, 0, 0);
      this.moreText = ("更多" + (j - 2) + "张次卡");
      ((TextView)this.expandView.findViewById(16908308)).setText(this.moreText);
      this.expandView.setClickable(true);
      this.expandView.setOnClickListener(this.expandClickListener);
      this.mCardLinearLayout.addView(this.expandView);
      setExpandState();
    }
    this.mCardLinearLayout.setGAString("fitness_passcard");
    this.mCardLinearLayout.gaUserInfo.shop_id = Integer.valueOf(shopId());
    ((DPActivity)getFragment().getActivity()).addGAView(this.mCardLinearLayout, -1);
    addCell("0495timescard.", this.mCardLinearLayout, 64);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.sport.TimesCardAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.sport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.RadioGroup;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.ExpandView;
import com.dianping.shopinfo.widget.ScheduleBlockView;
import com.dianping.shopinfo.widget.ScheduleBlockView.ScheduleBlockInterface;
import com.dianping.shopinfo.widget.ScheduleListView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;

public class FitnessVenuesAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_COURSE_SCHEDULE = "0360Venues.";
  private int defaultScheduleMaxShowNumber = 3;
  private DPObject dpResult;
  private DPObject[] dpitnessVenuesDatas;
  private HorizontalScrollView fitnessDateScroll;
  private MApiRequest request;
  private ScheduleBlockView.ScheduleBlockInterface scheduleBlockInterface = new ScheduleBlockView.ScheduleBlockInterface()
  {
    private DPObject selectDate = null;

    public View getDateItemView(DPObject paramDPObject, int paramInt, RadioGroup paramRadioGroup)
    {
      if ((paramDPObject != null) && (paramDPObject.getString("Name") != null))
      {
        paramRadioGroup = (NovaButton)LayoutInflater.from(FitnessVenuesAgent.this.getContext()).inflate(R.layout.shop_fitness_style_item, paramRadioGroup, false);
        paramRadioGroup.setId(paramInt);
        paramRadioGroup.setText(paramDPObject.getString("Name"));
        paramRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.getScreenWidthPixels(FitnessVenuesAgent.this.getContext()) / 2, -2));
        if (paramInt == 0)
        {
          paramRadioGroup.setSelected(true);
          this.selectDate = paramDPObject;
        }
        paramRadioGroup.setOnClickListener(new View.OnClickListener(paramDPObject, paramInt)
        {
          public void onClick(View paramView)
          {
            FitnessVenuesAgent.1.access$002(FitnessVenuesAgent.1.this, this.val$date);
            FitnessVenuesAgent.this.scheduleBlockView.sendDateChangeMsg(this.val$index);
            int i = (paramView.getLeft() + paramView.getRight()) / 2;
            int j = FitnessVenuesAgent.this.fitnessDateScroll.getScrollX();
            int k = FitnessVenuesAgent.this.fitnessDateScroll.getWidth() / 2;
            FitnessVenuesAgent.this.fitnessDateScroll.smoothScrollBy(i - j - k, 0);
          }
        });
        return paramRadioGroup;
      }
      return null;
    }

    public DPObject[] getScheduleListData()
    {
      return this.selectDate.getArray("FitnessVenues");
    }

    public View getScheduleListItemView(DPObject paramDPObject, ScheduleListView paramScheduleListView)
    {
      FitnessVenuesItemView localFitnessVenuesItemView = (FitnessVenuesItemView)LayoutInflater.from(FitnessVenuesAgent.this.getContext()).inflate(R.layout.shop_fitness_venues_item_view, paramScheduleListView, false);
      paramScheduleListView.setDefaultScheduleMaxShowNumber(FitnessVenuesAgent.this.defaultScheduleMaxShowNumber);
      ExpandView localExpandView = (ExpandView)LayoutInflater.from(FitnessVenuesAgent.this.getContext()).inflate(R.layout.shop_expand_view, paramScheduleListView, false);
      localExpandView.setExpandTextTitle("更多" + (this.selectDate.getArray("FitnessVenues").length - FitnessVenuesAgent.this.defaultScheduleMaxShowNumber) + "个场次");
      paramScheduleListView.setExpandView(localExpandView);
      localFitnessVenuesItemView.setFitnessVenuesItemView(paramDPObject);
      localFitnessVenuesItemView.setGAString("fitness_book");
      localFitnessVenuesItemView.gaUserInfo.shop_id = Integer.valueOf(FitnessVenuesAgent.this.shopId());
      localFitnessVenuesItemView.gaUserInfo.title = paramDPObject.getString("StartTime");
      localFitnessVenuesItemView.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$dpListItem.getString("Url");
          if (!TextUtils.isEmpty(paramView))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            FitnessVenuesAgent.this.getContext().startActivity(paramView);
          }
        }
      });
      return localFitnessVenuesItemView;
    }

    public View getSecondLevelView(DPObject paramDPObject)
    {
      return null;
    }

    public String getTips()
    {
      return "暂无可预订的场地哦~";
    }
  };
  private ScheduleBlockView scheduleBlockView;

  public FitnessVenuesAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getShop() == null);
    do
    {
      return;
      paramBundle = "";
      if (this.dpResult != null)
        paramBundle = this.dpResult.getString("Name");
      Object localObject = paramBundle;
      if (TextUtils.isEmpty(paramBundle))
        localObject = "场馆预订";
      this.scheduleBlockView = ((ScheduleBlockView)LayoutInflater.from(getContext()).inflate(R.layout.schedule_block_view, null));
      this.fitnessDateScroll = ((HorizontalScrollView)this.scheduleBlockView.findViewById(R.id.schedule_showdates_scroll));
      this.scheduleBlockView.setAgentHeaderTitle((String)localObject);
      this.scheduleBlockView.setViewShowAtScollView(getFragment().getScrollView(), this.scheduleBlockView);
      this.scheduleBlockView.setScheduleBlockInterface(this.scheduleBlockInterface);
      if ((this.dpitnessVenuesDatas == null) || (this.dpitnessVenuesDatas.length != 1))
        continue;
      this.fitnessDateScroll.setVisibility(8);
    }
    while ((this.dpitnessVenuesDatas == null) || (this.dpitnessVenuesDatas.length <= 0));
    this.scheduleBlockView.setScheduleBlockDate(this.dpitnessVenuesDatas);
    this.scheduleBlockView.setGAString("fitness_book");
    ((DPActivity)getFragment().getActivity()).addGAView(this.scheduleBlockView, -1);
    addCell("0360Venues.", this.scheduleBlockView, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.request != null)
    {
      super.getFragment().mapiService().abort(this.request, this, true);
      this.request = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
      this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
    {
      this.dpResult = ((DPObject)paramMApiResponse.result());
      if (this.dpResult != null)
        this.dpitnessVenuesDatas = this.dpResult.getArray("FitnessCategoryVenues");
      dispatchAgentChanged(false);
    }
  }

  public void sendRequest()
  {
    this.request = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/fitness/getfitnessvenues.bin").buildUpon().appendQueryParameter("shopid", String.valueOf(shopId())).build().toString(), CacheType.DISABLED);
    super.getFragment().mapiService().exec(this.request, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.sport.FitnessVenuesAgent
 * JD-Core Version:    0.6.0
 */
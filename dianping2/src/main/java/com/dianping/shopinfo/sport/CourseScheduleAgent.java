package com.dianping.shopinfo.sport;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
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
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.shopinfo.widget.ExpandView;
import com.dianping.shopinfo.widget.ScheduleBlockView;
import com.dianping.shopinfo.widget.ScheduleBlockView.ScheduleBlockInterface;
import com.dianping.shopinfo.widget.ScheduleListView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;

public class CourseScheduleAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_COURSE_SCHEDULE = "0350Course.";
  private HorizontalScrollView courseDateScroll;
  private int defaultScheduleMaxShowNumber = 3;
  private DPObject[] dpCourseDatas;
  private DPObject dpResult;
  private MApiRequest request;
  private ScheduleBlockView.ScheduleBlockInterface scheduleBlockInterface = new ScheduleBlockView.ScheduleBlockInterface()
  {
    private DPObject selectDate = null;

    public View getDateItemView(DPObject paramDPObject, int paramInt, RadioGroup paramRadioGroup)
    {
      if ((paramDPObject != null) && (paramDPObject.getString("CourseDay") != null))
      {
        paramRadioGroup = (NovaButton)LayoutInflater.from(CourseScheduleAgent.this.getContext()).inflate(R.layout.course_date_item, paramRadioGroup, false);
        paramRadioGroup.setId(paramInt);
        String str = paramDPObject.getString("CourseDay");
        paramRadioGroup.setText(str);
        paramRadioGroup.setGAString("fitness_class_date");
        paramRadioGroup.gaUserInfo.shop_id = Integer.valueOf(CourseScheduleAgent.this.shopId());
        paramRadioGroup.gaUserInfo.index = Integer.valueOf(paramInt);
        paramRadioGroup.gaUserInfo.title = str;
        if (paramInt == 0)
        {
          paramRadioGroup.setSelected(true);
          this.selectDate = paramDPObject;
        }
        paramRadioGroup.setOnClickListener(new View.OnClickListener(paramDPObject, paramInt)
        {
          public void onClick(View paramView)
          {
            CourseScheduleAgent.1.access$002(CourseScheduleAgent.1.this, this.val$date);
            CourseScheduleAgent.this.scheduleBlockView.sendDateChangeMsg(this.val$index);
            int i = (paramView.getLeft() + paramView.getRight()) / 2;
            int j = CourseScheduleAgent.this.courseDateScroll.getScrollX();
            int k = CourseScheduleAgent.this.courseDateScroll.getWidth() / 2;
            CourseScheduleAgent.this.courseDateScroll.smoothScrollBy(i - j - k, 0);
          }
        });
        return paramRadioGroup;
      }
      return null;
    }

    public DPObject[] getScheduleListData()
    {
      return this.selectDate.getArray("Courses");
    }

    public View getScheduleListItemView(DPObject paramDPObject, ScheduleListView paramScheduleListView)
    {
      CourseScheduleListItemView localCourseScheduleListItemView = (CourseScheduleListItemView)LayoutInflater.from(CourseScheduleAgent.this.getContext()).inflate(R.layout.schedule_list_item_view, paramScheduleListView, false);
      paramScheduleListView.setDefaultScheduleMaxShowNumber(CourseScheduleAgent.this.defaultScheduleMaxShowNumber);
      ExpandView localExpandView = (ExpandView)LayoutInflater.from(CourseScheduleAgent.this.getContext()).inflate(R.layout.shop_expand_view, paramScheduleListView, false);
      localExpandView.setExpandTextTitle("更多" + (this.selectDate.getArray("Courses").length - CourseScheduleAgent.this.defaultScheduleMaxShowNumber) + "个课程");
      paramScheduleListView.setExpandView(localExpandView);
      localCourseScheduleListItemView.setScheduleListItemView(paramDPObject);
      localCourseScheduleListItemView.setGAString("fitness_class_item");
      localCourseScheduleListItemView.gaUserInfo.shop_id = Integer.valueOf(CourseScheduleAgent.this.shopId());
      localCourseScheduleListItemView.gaUserInfo.title = paramDPObject.getString("Name");
      localCourseScheduleListItemView.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$dpListItem.getString("Url");
          if (!TextUtils.isEmpty(paramView))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            CourseScheduleAgent.this.getContext().startActivity(paramView);
          }
        }
      });
      return localCourseScheduleListItemView;
    }

    public View getSecondLevelView(DPObject paramDPObject)
    {
      return null;
    }

    public String getTips()
    {
      String str2 = this.selectDate.getString("Tip");
      String str1 = str2;
      if (TextUtils.isEmpty(str2))
        str1 = "暂无课程哦~";
      return str1;
    }
  };
  private ScheduleBlockView scheduleBlockView;

  public CourseScheduleAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createHeader(String paramString)
  {
    CommonCell localCommonCell = createCommonCell();
    setHeaderStyle(localCommonCell);
    localCommonCell.setLeftIcon(R.drawable.icon_reservation);
    localCommonCell.setTitle(paramString);
    localCommonCell.setRightIcon(R.drawable.tuan_refund_icon_check);
    localCommonCell.setRightText("在线预订");
    return localCommonCell;
  }

  private void setHeaderStyle(View paramView)
  {
    paramView.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 45.0F)));
    View localView = paramView.findViewById(16908295);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 22.0F), ViewUtils.dip2px(getContext(), 22.0F));
    localLayoutParams.setMargins(0, 0, 14, 0);
    localView.setLayoutParams(localLayoutParams);
    ((TextView)paramView.findViewById(16908308)).setTextSize(2, 15.0F);
    localView = paramView.findViewById(16908296);
    localLayoutParams = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 12.0F), ViewUtils.dip2px(getContext(), 12.0F));
    localLayoutParams.setMargins(0, 0, 5, 0);
    localView.setLayoutParams(localLayoutParams);
    paramView = (TextView)paramView.findViewById(R.id.text3);
    paramView.setTextSize(2, 12.0F);
    paramView.setTextColor(getContext().getResources().getColor(R.color.light_gray));
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getShop() == null);
    LinearLayout localLinearLayout;
    do
    {
      return;
      paramBundle = "";
      if (this.dpResult != null)
        paramBundle = this.dpResult.getString("Title");
      Object localObject = paramBundle;
      if (TextUtils.isEmpty(paramBundle))
        localObject = "课程";
      paramBundle = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
      paramBundle.setGAString("fitness_class");
      ((DPActivity)getFragment().getActivity()).addGAView(paramBundle, -1);
      paramBundle.findViewById(R.id.title_layout).setVisibility(8);
      localLinearLayout = (LinearLayout)paramBundle.findViewById(R.id.content);
      localLinearLayout.addView(createHeader((String)localObject));
      this.scheduleBlockView = ((ScheduleBlockView)LayoutInflater.from(getContext()).inflate(R.layout.schedule_block_view, null));
      this.courseDateScroll = ((HorizontalScrollView)this.scheduleBlockView.findViewById(R.id.schedule_showdates_scroll));
      this.scheduleBlockView.setAgentHeaderTitle(null);
      this.scheduleBlockView.setViewShowAtScollView(getFragment().getScrollView(), this.scheduleBlockView);
      this.scheduleBlockView.setScheduleBlockInterface(this.scheduleBlockInterface);
    }
    while ((this.dpCourseDatas == null) || (this.dpCourseDatas.length <= 0));
    this.scheduleBlockView.setScheduleBlockDate(this.dpCourseDatas);
    localLinearLayout.addView(this.scheduleBlockView);
    addCell("0350Course.", paramBundle, 0);
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
    {
      this.request = null;
      dispatchAgentChanged(false);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
    {
      this.dpResult = ((DPObject)paramMApiResponse.result());
      if (this.dpResult != null)
        this.dpCourseDatas = this.dpResult.getArray("DailyCourses");
      dispatchAgentChanged(false);
    }
  }

  public void sendRequest()
  {
    this.request = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/fitness/getfitnesscurriculum.bin").buildUpon().appendQueryParameter("shopid", String.valueOf(shopId())).build().toString(), CacheType.DISABLED);
    super.getFragment().mapiService().exec(this.request, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.sport.CourseScheduleAgent
 * JD-Core Version:    0.6.0
 */
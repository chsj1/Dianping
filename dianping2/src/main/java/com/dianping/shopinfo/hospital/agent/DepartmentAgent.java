package com.dianping.shopinfo.hospital.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CustomLinearLayout;
import com.dianping.base.widget.CustomLinearLayout.OnItemClickListener;
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
import com.dianping.shopinfo.hospital.widget.DepartmentAdpter;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;

public class DepartmentAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_DEPARMENT = "0310department.";
  DPObject[] departmentItems;
  DPObject departmentsInfo;
  MApiRequest infoRequest;
  private int screenWidth;
  private View.OnClickListener titleClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if ((DepartmentAgent.this.departmentsInfo == null) || (DepartmentAgent.this.departmentsInfo.getString("JumpUrl") == null) || (DepartmentAgent.this.departmentsInfo.getString("JumpUrl").length() <= 0))
        return;
      try
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(DepartmentAgent.this.departmentsInfo.getString("JumpUrl")));
        DepartmentAgent.this.getFragment().startActivity(paramView);
        return;
      }
      catch (Exception paramView)
      {
        paramView.printStackTrace();
      }
    }
  };

  public DepartmentAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createDepartmentCell()
  {
    if (this.departmentsInfo == null)
      return null;
    if ((this.departmentItems == null) || (this.departmentItems.length <= 0))
      return null;
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_clothes_recommend_layout, getParentView(), false);
    GAHelper.instance().contextStatisticsEvent(getContext(), "depart", getGAExtra(), "view");
    localShopinfoCommonCell.setTitle(this.departmentsInfo.getString("Title") + "(" + this.departmentsInfo.getInt("Count") + ") ", this.titleClickListener);
    localShopinfoCommonCell.getTitleLay().setGAString("depart_more", getGAExtra());
    localShopinfoCommonCell.addContent(createDepartmentItemView(), false, null);
    ((ImageView)localShopinfoCommonCell.findViewById(R.id.indicator)).setVisibility(0);
    return localShopinfoCommonCell;
  }

  private View createDepartmentItemView()
  {
    if ((this.departmentItems == null) || (this.departmentItems.length <= 0))
      return null;
    CustomLinearLayout localCustomLinearLayout = new CustomLinearLayout(getContext());
    localCustomLinearLayout.setOrientation(1);
    localCustomLinearLayout.setMaxLine(2);
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-1, -2);
    localLayoutParams.topMargin = ViewUtils.dip2px(getContext(), 15.0F);
    localLayoutParams.bottomMargin = ViewUtils.dip2px(getContext(), 15.0F);
    localCustomLinearLayout.setLayoutParams(localLayoutParams);
    localCustomLinearLayout.init();
    localCustomLinearLayout.setOnItemClickListener(new CustomLinearLayout.OnItemClickListener()
    {
      public void onItemClick(LinearLayout paramLinearLayout, View paramView, int paramInt, long paramLong)
      {
        if (TextUtils.isEmpty(DepartmentAgent.this.departmentItems[paramInt].getString("JumpUrl")))
          return;
        try
        {
          paramLinearLayout = new Intent("android.intent.action.VIEW", Uri.parse(DepartmentAgent.this.departmentItems[paramInt].getString("JumpUrl")));
          DepartmentAgent.this.getFragment().startActivity(paramLinearLayout);
          GAHelper.instance().contextStatisticsEvent(DepartmentAgent.this.getContext(), "depart", DepartmentAgent.this.getGAExtra(), "tap");
          return;
        }
        catch (Exception paramLinearLayout)
        {
          while (true)
            paramLinearLayout.printStackTrace();
        }
      }
    });
    localCustomLinearLayout.setAdapter(new DepartmentAdpter(getContext(), this.departmentItems, this.screenWidth));
    return localCustomLinearLayout;
  }

  private void sendInfoRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/medicine/getdepartmentinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.infoRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.infoRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      return;
      if ((this.departmentsInfo == null) && (this.infoRequest == null))
        sendInfoRequest();
      paramBundle = createDepartmentCell();
    }
    while (paramBundle == null);
    addCell("0310department.", paramBundle, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.screenWidth = ViewUtils.getScreenWidthPixels(getContext());
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.infoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.infoRequest = null;
    if ((paramMApiResponse == null) || (paramMApiResponse.result() == null));
    do
    {
      return;
      this.departmentsInfo = ((DPObject)paramMApiResponse.result());
    }
    while ((this.departmentsInfo == null) || (!this.departmentsInfo.getBoolean("IsShow")));
    this.departmentItems = this.departmentsInfo.getArray("Departments");
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hospital.agent.DepartmentAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
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
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class SchoolMajorsAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  protected static final String CELL_Major = "0400Major.01";
  private DPObject eduSchoolMajorInfo;
  MApiRequest request;

  public SchoolMajorsAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createMajorCell(DPObject paramDPObject)
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    Object localObject = paramDPObject.getStringArray("MajorList");
    paramDPObject = new StringBuffer();
    int j = localObject.length;
    int i = 0;
    while (i < j)
    {
      paramDPObject.append(localObject[i]);
      paramDPObject.append("   ");
      i += 1;
    }
    localObject = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.education_major_tag, null, false);
    ((TextView)localObject).setLineSpacing(ViewUtils.dip2px(getContext(), 7.4F), 1.0F);
    ((TextView)localObject).setText(paramDPObject);
    localShopinfoCommonCell.addContent((View)localObject, false, this);
    localShopinfoCommonCell.setTitle("重点专业", this);
    localShopinfoCommonCell.getTitleLay().setGAString("edu_schooldepartment", getGAExtra());
    return (View)localShopinfoCommonCell;
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/edu/schoolmajorinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopId", shopId() + "");
    this.request = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((this.eduSchoolMajorInfo == null) && (this.request == null))
      sendRequest();
    if (this.eduSchoolMajorInfo != null)
    {
      paramBundle = this.eduSchoolMajorInfo.getStringArray("MajorList");
      if ((paramBundle != null) && (paramBundle.length >= 1));
    }
    else
    {
      return;
    }
    removeAllCells();
    addCell("0400Major.01", createMajorCell(this.eduSchoolMajorInfo));
  }

  public void onClick(View paramView)
  {
    paramView = this.eduSchoolMajorInfo.getString("DetailLink");
    if (!TextUtils.isEmpty(paramView))
    {
      Intent localIntent = new Intent("android.intent.action.VIEW");
      localIntent.setData(Uri.parse(paramView));
      getFragment().startActivity(localIntent);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getShop() == null);
    do
      return;
    while (shopId() > 0);
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
      this.request = null;
      this.eduSchoolMajorInfo = ((DPObject)paramMApiResponse.result());
      if (this.eduSchoolMajorInfo != null)
        dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.SchoolMajorsAgent
 * JD-Core Version:    0.6.0
 */
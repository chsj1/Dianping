package com.dianping.shopinfo.education.agent;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.education.widget.EducationFeatureAdapter;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;

public class EducationShopDetailInfoAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, AdapterView.OnItemClickListener
{
  private static final String CELL_DETAIL_INFO = "2000EduDetail.10";
  DPObject mShopInfo;
  private MApiRequest mShopInfoRequest;

  public EducationShopDetailInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequest()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.setLength(0);
    localStringBuilder.append("http://mapi.dianping.com/edu/shopdetailinfo.bin?").append("shopid=").append(shopId());
    this.mShopInfoRequest = BasicMApiRequest.mapiGet(Uri.parse(localStringBuilder.toString()).buildUpon().build().toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.mShopInfoRequest, this);
  }

  private View setupShopDetailInfoView()
  {
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.edu_shopinfo_layout, getParentView(), false);
    Object localObject1 = (NovaRelativeLayout)localView.findViewById(R.id.edu_shopinfo_detail);
    ((NovaRelativeLayout)localObject1).setGAString("edu_brief", getGAExtra());
    Object localObject2 = this.mShopInfo.getString("Title");
    Object localObject3 = (TextView)localView.findViewById(R.id.edu_shopinfo_title);
    if (TextUtils.isEmpty((CharSequence)localObject2))
    {
      localObject1 = null;
      return localObject1;
    }
    ((TextView)localObject3).setText((CharSequence)localObject2);
    if (TextUtils.isEmpty(this.mShopInfo.getString("DetailLink")))
      return null;
    ((NovaRelativeLayout)localObject1).setOnClickListener(this);
    localObject1 = this.mShopInfo.getString("BrandStory");
    localObject2 = (NovaLinearLayout)localView.findViewById(R.id.edu_linear_brandstory);
    if (TextUtils.isEmpty((CharSequence)localObject1))
    {
      ((NovaLinearLayout)localObject2).setVisibility(8);
      label140: ((NovaLinearLayout)localObject2).setGAString("edu_brandstory", getGAExtra());
      localObject1 = (MeasuredGridView)localView.findViewById(R.id.edu_gridview_features);
      localObject2 = this.mShopInfo.getStringArray("Characteristics");
      if ((localObject2 != null) && (localObject2.length != 0))
        break label338;
      localView.findViewById(R.id.edu_textview_features).setVisibility(8);
      ((MeasuredGridView)localObject1).setVisibility(8);
      localView.findViewById(R.id.view_edu_line_features).setVisibility(8);
      label214: localObject1 = (NovaLinearLayout)localView.findViewById(R.id.edu_linear_environment);
      ((NovaLinearLayout)localObject1).setGAString("edu_brief", getGAExtra());
      localObject3 = this.mShopInfo.getStringArray("EnvPics");
      if ((localObject3 != null) && (localObject3.length != 0))
        break label393;
      localView.findViewById(R.id.edu_textview_envs).setVisibility(8);
      ((NovaLinearLayout)localObject1).setVisibility(8);
    }
    while (true)
    {
      if (localObject2 != null)
      {
        localObject1 = localView;
        if (localObject2.length != 0)
          break;
      }
      if (localObject3 != null)
      {
        localObject1 = localView;
        if (localObject3.length != 0)
          break;
      }
      localView.findViewById(R.id.view_edu_line_detail).setVisibility(8);
      return localView;
      ((TextView)localView.findViewById(R.id.edu_brandstory_title)).setText((CharSequence)localObject1);
      ((NovaLinearLayout)localObject2).setOnClickListener(this);
      break label140;
      label338: ((MeasuredGridView)localObject1).setAdapter(new EducationFeatureAdapter(getContext(), localObject2));
      ((MeasuredGridView)localObject1).setOnItemClickListener(this);
      ((NovaTextView)localView.findViewById(R.id.edu_textview_features)).setGAString("edu_brief", getGAExtra());
      localView.findViewById(R.id.edu_textview_features).setOnClickListener(this);
      break label214;
      label393: int m = (getContext().getResources().getDisplayMetrics().widthPixels - ViewUtils.dip2px(getContext(), 50.0F)) / 3;
      int j = this.mShopInfo.getInt("EnvPicWidth");
      int k = this.mShopInfo.getInt("EnvPicHeight");
      int i = j;
      if (j <= 0)
        i = 320;
      j = k;
      if (k <= 0)
        j = 240;
      float f = j * 1.0F / i;
      j = (int)(m * f);
      i = 0;
      while ((i < localObject3.length) && (i <= 2))
      {
        NetworkImageView localNetworkImageView = (NetworkImageView)this.res.inflate(getContext(), R.layout.edu_shopinfo_environment, (ViewGroup)localObject1, false);
        localNetworkImageView.setImage(localObject3[i]);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(m, j);
        if (i != 0)
          localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 10.0F);
        ((NovaLinearLayout)localObject1).addView(localNetworkImageView, localLayoutParams);
        i += 1;
      }
      ((NovaLinearLayout)localObject1).setOnClickListener(this);
      ((NovaTextView)localView.findViewById(R.id.edu_textview_envs)).setGAString("edu_brief", getGAExtra());
      localView.findViewById(R.id.edu_textview_envs).setOnClickListener(this);
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (this.mShopInfo == null);
    do
    {
      do
        return;
      while (getShopStatus() != 0);
      paramBundle = setupShopDetailInfoView();
    }
    while (paramBundle == null);
    addCell("2000EduDetail.10", paramBundle, 0);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (((i == R.id.edu_shopinfo_detail) || (i == R.id.edu_linear_environment) || (i == R.id.edu_textview_envs) || (i == R.id.edu_textview_features) || (i == R.id.edu_linear_brandstory)) && (!TextUtils.isEmpty(this.mShopInfo.getString("DetailLink"))))
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.mShopInfo.getString("DetailLink"))));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onDestroy()
  {
    if (this.mShopInfoRequest != null)
    {
      getFragment().mapiService().abort(this.mShopInfoRequest, this, true);
      this.mShopInfoRequest = null;
    }
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (!TextUtils.isEmpty(this.mShopInfo.getString("DetailLink")))
    {
      paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse(this.mShopInfo.getString("DetailLink")));
      getFragment().startActivity(paramAdapterView);
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopInfoRequest)
      this.mShopInfoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopInfoRequest)
    {
      this.mShopInfoRequest = null;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        this.mShopInfo = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.EducationShopDetailInfoAgent
 * JD-Core Version:    0.6.0
 */
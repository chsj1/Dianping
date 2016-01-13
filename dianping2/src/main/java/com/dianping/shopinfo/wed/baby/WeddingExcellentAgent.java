package com.dianping.shopinfo.wed.baby;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.base.widget.wed.WedBaseAdapter;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaTextView;

public class WeddingExcellentAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, AdapterView.OnItemClickListener
{
  DPObject excellentObject;
  MApiRequest excellentRequest;

  public WeddingExcellentAgent(Object paramObject)
  {
    super(paramObject);
    sendExcellentRequest();
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.excellentObject == null)
    {
      removeAllCells();
      return;
    }
    removeAllCells();
    paramBundle = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_wed_excellent_agent, getParentView(), false);
    Object localObject = (NovaLinearLayout)paramBundle.findViewById(R.id.wed_excellent_agent_title);
    ((NovaLinearLayout)localObject).setGAString("highquality_booking");
    ((NovaLinearLayout)localObject).setOnClickListener(this);
    localObject = (TextView)paramBundle.findViewById(R.id.wed_excellent_top_title);
    if (!TextUtils.isEmpty(this.excellentObject.getString("Title")))
      ((TextView)localObject).setText(this.excellentObject.getString("Title"));
    localObject = this.excellentObject.getArray("Properties");
    MeasuredGridView localMeasuredGridView = (MeasuredGridView)paramBundle.findViewById(R.id.wed_excellent_gridview);
    if ((localObject == null) || (localObject.length == 0))
    {
      localMeasuredGridView.setVisibility(8);
      removeAllCells();
      return;
    }
    localMeasuredGridView.setAdapter(new WedExcellentAdapter(getContext(), localObject));
    localMeasuredGridView.setOnItemClickListener(this);
    addCell("", paramBundle);
  }

  public void onClick(View paramView)
  {
    if (TextUtils.isEmpty(this.excellentObject.getString("DefaultLink")))
      return;
    paramView = Uri.parse("dianping://web").buildUpon();
    paramView.appendQueryParameter("url", this.excellentObject.getString("DefaultLink"));
    startActivity(new Intent("android.intent.action.VIEW", paramView.build()));
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (TextUtils.isEmpty(this.excellentObject.getString("DefaultLink")))
      return;
    paramAdapterView = Uri.parse("dianping://web").buildUpon();
    paramAdapterView.appendQueryParameter("url", this.excellentObject.getString("DefaultLink"));
    startActivity(new Intent("android.intent.action.VIEW", paramAdapterView.build()));
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.excellentRequest)
      this.excellentRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.excellentRequest)
    {
      this.excellentObject = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
      paramMApiRequest = new Bundle();
      paramMApiRequest.putParcelable("excellent", this.excellentObject);
      dispatchAgentChanged("shopinfo/wed_toolbar", paramMApiRequest);
    }
  }

  void sendExcellentRequest()
  {
    if (this.excellentRequest != null);
    do
      return;
    while (shopId() <= 0);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/selectiveshop.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.excellentRequest = mapiGet(this, localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.excellentRequest, this);
  }

  class WedExcellentAdapter extends WedBaseAdapter<DPObject>
  {
    public WedExcellentAdapter(Context paramArrayOfDPObject, DPObject[] arg3)
    {
      this.context = paramArrayOfDPObject;
      Object localObject;
      this.adapterData = localObject;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = new NovaTextView(WeddingExcellentAgent.this.getContext());
      paramView = (NovaTextView)paramViewGroup;
      paramView.setGravity(16);
      paramView.setTextColor(WeddingExcellentAgent.this.getContext().getResources().getColor(R.color.deep_gray));
      paramView.setCompoundDrawablePadding(ViewUtils.dip2px(WeddingExcellentAgent.this.getContext(), 5.0F));
      paramView.setTextSize(0, WeddingExcellentAgent.this.getContext().getResources().getDimensionPixelSize(R.dimen.text_size_13));
      paramView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wed_icon_gou, 0, 0, 0);
      paramView.setText(((DPObject[])this.adapterData)[paramInt].getString("Name"));
      paramView.setGAString("highquality_booking");
      return paramViewGroup;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingExcellentAgent
 * JD-Core Version:    0.6.0
 */
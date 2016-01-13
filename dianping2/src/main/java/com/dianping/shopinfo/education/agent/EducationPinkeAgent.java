package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.education.widget.PinkeListAdapter;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaListView;

public class EducationPinkeAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_EDUCATION_PINKE = "0250Basic.01pinke";
  private static final String TAG = EducationPinkeAgent.class.getSimpleName();
  private PinkeListAdapter pinkeListAdapter;
  private DPObject pinkeObject;
  private MApiRequest pinkeReq;

  public EducationPinkeAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void fixListViewHeight(NovaListView paramNovaListView)
  {
    PinkeListAdapter localPinkeListAdapter = (PinkeListAdapter)paramNovaListView.getAdapter();
    int j = 0;
    if (localPinkeListAdapter == null)
      return;
    int i = 0;
    int k = localPinkeListAdapter.getCount();
    while (i < k)
    {
      localObject = localPinkeListAdapter.getView(i, null, paramNovaListView);
      ((View)localObject).measure(0, 0);
      j += ((View)localObject).getMeasuredHeight();
      i += 1;
    }
    Object localObject = paramNovaListView.getLayoutParams();
    ((ViewGroup.LayoutParams)localObject).height = (paramNovaListView.getDividerHeight() * (localPinkeListAdapter.getCount() - 1) + j);
    paramNovaListView.setLayoutParams((ViewGroup.LayoutParams)localObject);
  }

  private void initViewCell(View paramView)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)paramView.findViewById(R.id.pinkecountLL);
    TextView localTextView1 = (TextView)paramView.findViewById(R.id.pinkecountTV);
    TextView localTextView2 = (TextView)paramView.findViewById(R.id.pinketitleTV);
    NovaListView localNovaListView = (NovaListView)paramView.findViewById(R.id.pinkelistLV);
    View localView = paramView.findViewById(R.id.pinkelistLV_line);
    LinearLayout localLinearLayout = (LinearLayout)paramView.findViewById(R.id.pinketitleLL);
    String str = this.pinkeObject.getString("Title");
    int i = this.pinkeObject.getInt("Count");
    paramView = this.pinkeObject.getArray("PinkeInfoList");
    if ((localTextView2 != null) && (localLinearLayout != null) && (!TextUtils.isEmpty(str)))
    {
      localLinearLayout.setVisibility(0);
      localTextView2.setText(str);
    }
    if (paramView.length > 2)
    {
      localTextView2 = paramView[0];
      localLinearLayout = paramView[1];
    }
    for (this.pinkeListAdapter = new PinkeListAdapter(getContext(), new DPObject[] { localTextView2, localLinearLayout }); ; this.pinkeListAdapter = new PinkeListAdapter(getContext(), paramView))
    {
      if ((localNovaLinearLayout != null) && (localTextView1 != null) && (i > 2))
      {
        localNovaLinearLayout.setVisibility(0);
        localNovaLinearLayout.setGAString("edu_pinke_more", getGAExtra());
        localView.setVisibility(0);
        localTextView1.setText(i - 2 + "");
        localNovaLinearLayout.setOnClickListener(new View.OnClickListener(paramView, localNovaListView, localNovaLinearLayout, localView)
        {
          public void onClick(View paramView)
          {
            EducationPinkeAgent.this.pinkeListAdapter.pinkeitems = this.val$pinkeitems;
            EducationPinkeAgent.this.pinkeListAdapter.notifyDataSetChanged();
            EducationPinkeAgent.this.fixListViewHeight(this.val$pinkelistLV);
            this.val$pinkecountLL.setVisibility(8);
            this.val$pinkelistLV_line.setVisibility(8);
          }
        });
      }
      localNovaListView.setAdapter(this.pinkeListAdapter);
      localNovaListView.setOnItemClickListener(new AdapterView.OnItemClickListener(paramView)
      {
        public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$pinkeitems[paramInt].getString("DetailLink")));
          EducationPinkeAgent.this.getFragment().startActivity(paramAdapterView);
        }
      });
      fixListViewHeight(localNovaListView);
      return;
    }
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/edu/shoppinkeinfo.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.pinkeReq = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.pinkeReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if ((this.pinkeObject == null) || (this.pinkeObject.getInt("Count") <= 0) || (this.pinkeObject.getArray("PinkeInfoList") == null) || (this.pinkeObject.getArray("PinkeInfoList").length <= 0))
      return;
    try
    {
      paramBundle = this.res.inflate(getContext(), R.layout.shop_education_pinkeproductall, getParentView(), false);
      initViewCell(paramBundle);
      addCell("0250Basic.01pinke", paramBundle, 0);
      return;
    }
    catch (java.lang.Exception paramBundle)
    {
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getShop();
    if (paramBundle == null)
    {
      Log.e(TAG, "Null shop data.");
      return;
    }
    if (paramBundle.getInt("ID") <= 0)
    {
      Log.e(TAG, "Invalid shop id.");
      return;
    }
    sendRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.pinkeReq == paramMApiRequest)
      this.pinkeReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.pinkeReq)
    {
      this.pinkeReq = null;
      this.pinkeObject = ((DPObject)paramMApiResponse.result());
      if (this.pinkeObject != null)
        dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.EducationPinkeAgent
 * JD-Core Version:    0.6.0
 */
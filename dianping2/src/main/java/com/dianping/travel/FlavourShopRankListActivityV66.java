package com.dianping.travel;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.travel.view.FlavourShopListItem;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;

public class FlavourShopRankListActivityV66 extends NovaListActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  private Adapter adapter;
  private String experienceid;
  private String guidetype;
  private ViewGroup headerIntroView;
  private MApiRequest mReq;
  private String name;
  private String rankid;
  private boolean shouldShowImage;
  private TextView tipsView;

  private void handlerResult(DPObject paramDPObject)
  {
    if (this.headerIntroView == null)
    {
      this.headerIntroView = ((ViewGroup)LayoutInflater.from(this).inflate(R.layout.flavour_shop_rank_head_intro_view_v66, this.listView, false));
      this.listView.addHeaderView(this.headerIntroView, null, false);
    }
    ((TextView)this.headerIntroView.findViewById(16908308)).setText(paramDPObject.getString("Intro"));
    if (this.tipsView == null)
    {
      this.tipsView = new TextView(this);
      this.tipsView.setBackgroundColor(-460552);
      this.tipsView.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(this, 40.0F)));
      this.tipsView.setGravity(80);
      this.tipsView.setPadding(ViewUtils.dip2px(this, 10.0F), 0, 0, ViewUtils.dip2px(this, 8.0F));
      this.listView.addHeaderView(this.tipsView, null, false);
    }
    this.tipsView.setText(paramDPObject.getString("Tips"));
    this.tipsView.setTextColor(getResources().getColor(R.color.light_gray));
    paramDPObject = paramDPObject.getArray("List");
    if ((paramDPObject != null) && (paramDPObject.length > 0))
    {
      this.adapter = new Adapter(paramDPObject);
      this.listView.setAdapter(this.adapter);
    }
  }

  private void loadRankList()
  {
    this.emptyView.removeAllViews();
    this.emptyView.addView(getLoadingView());
    if (this.mReq != null)
      mapiService().abort(this.mReq, this, true);
    Object localObject2 = "http://m.api.dianping.com/getrankshopv66.hotel?cityid=" + cityId() + "&name=" + Uri.encode(this.name) + "&guidetype=" + this.guidetype;
    Object localObject1 = localObject2;
    if (!TextUtils.isEmpty(this.experienceid))
      localObject1 = (String)localObject2 + "&experienceid=" + this.experienceid;
    localObject2 = localObject1;
    if (!TextUtils.isEmpty(this.rankid))
      localObject2 = (String)localObject1 + "&rankid=" + this.rankid;
    this.mReq = BasicMApiRequest.mapiGet((String)localObject2, CacheType.NORMAL);
    mapiService().exec(this.mReq, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getData();
    if (paramBundle != null)
    {
      this.name = paramBundle.getQueryParameter("name");
      super.setTitle(this.name);
      this.guidetype = paramBundle.getQueryParameter("guidetype");
      this.rankid = paramBundle.getQueryParameter("rankid");
      this.experienceid = paramBundle.getQueryParameter("experienceid");
    }
    this.listView.setOnItemClickListener(this);
    loadRankList();
  }

  protected void onDestroy()
  {
    if (this.mReq != null)
      mapiService().abort(this.mReq, this, true);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.listView.getItemAtPosition(paramInt);
    if ((paramAdapterView instanceof DPObject))
    {
      paramAdapterView = (DPObject)paramAdapterView;
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramAdapterView.getInt("ID")));
      paramView.putExtra("shopId", paramAdapterView.getInt("ID"));
      paramView.putExtra("shop", paramAdapterView);
      statisticsEvent("travel5_hotel", "click", paramAdapterView.getInt("ID") + "", 0);
      startActivity(paramView);
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.emptyView.removeAllViews();
    this.emptyView.addView(getFailedView(paramMApiResponse.message().content(), new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        FlavourShopRankListActivityV66.this.loadRankList();
      }
    }));
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.emptyView.removeAllViews();
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      handlerResult((DPObject)paramMApiResponse.result());
      return;
    }
    this.emptyView.addView(getFailedView("未知错误", new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        FlavourShopRankListActivityV66.this.loadRankList();
      }
    }));
  }

  protected void onResume()
  {
    super.onResume();
    boolean bool = NovaConfigUtils.isShowImageInMobileNetwork();
    if (this.shouldShowImage != bool)
      this.shouldShowImage = bool;
    if (this.adapter != null)
      this.adapter.notifyDataSetChanged();
  }

  protected void setupView()
  {
    super.setContentView(R.layout.flavour_shop_rank_list_frame);
  }

  private class Adapter extends BasicAdapter
  {
    private DPObject[] objs;

    public Adapter(DPObject[] arg2)
    {
      Object localObject;
      this.objs = localObject;
    }

    public int getCount()
    {
      return this.objs.length;
    }

    public Object getItem(int paramInt)
    {
      return this.objs[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject2 = null;
      DPObject localDPObject = null;
      Object localObject1 = localDPObject;
      if (paramViewGroup != null)
      {
        if (paramViewGroup.getContext() != null)
          break label28;
        localObject1 = localDPObject;
      }
      while (true)
      {
        return localObject1;
        label28: localObject1 = getItem(paramInt);
        if (!(localObject1 instanceof DPObject))
          break;
        localDPObject = (DPObject)localObject1;
        localObject1 = localObject2;
        if ((paramView instanceof FlavourShopListItem))
          localObject1 = (FlavourShopListItem)paramView;
        paramView = (View)localObject1;
        if (localObject1 == null)
          paramView = (FlavourShopListItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.flavourshop_rank_item_v66, paramViewGroup, false);
        localObject1 = paramView;
        if (paramView == null)
          continue;
        paramView.setShop(localDPObject, -1, 0.0D, 0.0D, FlavourShopRankListActivityV66.this.shouldShowImage);
        paramViewGroup = paramView.findViewById(R.id.flavour_divider);
        if (paramInt == 0)
        {
          paramViewGroup.setVisibility(8);
          return paramView;
        }
        paramViewGroup.setVisibility(0);
        return paramView;
      }
      return (View)new TextView(paramViewGroup.getContext());
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.FlavourShopRankListActivityV66
 * JD-Core Version:    0.6.0
 */
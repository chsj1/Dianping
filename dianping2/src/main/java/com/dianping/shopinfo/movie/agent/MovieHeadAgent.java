package com.dianping.shopinfo.movie.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.ShopPower;
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
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;

public class MovieHeadAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String CELL_TOP = "0200Basic.05Info";
  private MApiRequest cinemaInfoRequest;
  private DPObject mResultObj;

  public MovieHeadAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void requestCinemaInfo()
  {
    if (getShop() == null);
    do
      return;
    while (this.cinemaInfoRequest != null);
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/cinemainfomv.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(shopId()));
    this.cinemaInfoRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.cinemaInfoRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getFragment() == null);
    Object localObject1;
    do
    {
      do
        return;
      while (this.mResultObj == null);
      localObject1 = getShop();
    }
    while (localObject1 == null);
    paramBundle = this.res.inflate(getContext(), R.layout.shopinfo_movie_head_layout, getParentView(), false);
    paramBundle.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://cinemainfo"));
        paramView.putExtra("cinema", MovieHeadAgent.this.mResultObj);
        paramView.putExtra("main_shop", MovieHeadAgent.this.getShop());
        MovieHeadAgent.this.startActivity(paramView);
      }
    });
    LinearLayout localLinearLayout = (LinearLayout)paramBundle.findViewById(R.id.service_item_layout);
    Object localObject2 = (TextView)paramBundle.findViewById(R.id.cinema_name);
    Object localObject3 = (ShopPower)paramBundle.findViewById(R.id.cinema_shop_power);
    TextView localTextView = (TextView)paramBundle.findViewById(R.id.region_name_tv);
    if (TextUtils.isEmpty(((DPObject)localObject1).getString("BranchName")))
    {
      ((TextView)localObject2).setText(((DPObject)localObject1).getString("Name"));
      ((ShopPower)localObject3).setPower(((DPObject)localObject1).getInt("ShopPower"));
      localTextView.setText(((DPObject)localObject1).getString("RegionName"));
      if ((this.mResultObj.getArray("ServiceItems") == null) || (this.mResultObj.getArray("ServiceItems").length <= 0))
        break label364;
      localObject1 = this.mResultObj.getArray("ServiceItems");
      if (localObject1.length <= 6)
        break label357;
    }
    label357: for (int i = 6; ; i = localObject1.length)
    {
      int j = 0;
      while (j < i)
      {
        localObject2 = new NetworkImageView(getContext());
        ((NetworkImageView)localObject2).setScaleType(ImageView.ScaleType.FIT_XY);
        localObject3 = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 30.0F), ViewUtils.dip2px(getContext(), 30.0F));
        ((LinearLayout.LayoutParams)localObject3).rightMargin = ViewUtils.dip2px(getContext(), 6.0F);
        ((NetworkImageView)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject3);
        ((NetworkImageView)localObject2).setImage(localObject1[j].getString("ImageUrl"));
        localLinearLayout.addView((View)localObject2);
        j += 1;
      }
      ((TextView)localObject2).setText(((DPObject)localObject1).getString("Name") + "(" + ((DPObject)localObject1).getString("BranchName") + ")");
      break;
    }
    label364: addCell("0200Basic.05Info", paramBundle, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestCinemaInfo();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.cinemaInfoRequest != null)
    {
      getFragment().mapiService().abort(this.cinemaInfoRequest, this, true);
      this.cinemaInfoRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.cinemaInfoRequest = null;
    dispatchAgentChanged(false);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.cinemaInfoRequest)
    {
      if (DPObjectUtils.isDPObjectof(paramMApiResponse.result(), "Cinema"))
      {
        this.mResultObj = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
      this.cinemaInfoRequest = null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.movie.agent.MovieHeadAgent
 * JD-Core Version:    0.6.0
 */
package com.dianping.shopinfo.dish;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.GroupCellAgent;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import java.text.DecimalFormat;

public class DishDetailInfoAgent extends GroupCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_DISH_DETAIL_TOP = "0100Dish.0100Top";
  View cell;
  String defaultPic;
  String desc;
  private MApiRequest dishDetailInfoRequest;
  String dishshopid = "";
  String latitude = "";
  DPObject location;
  String longitude = "";
  String name = "";
  DPObject rank;
  DPObject rankResult;

  public DishDetailInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/mshop/dishshop.bin").buildUpon();
    this.location = getFragment().locationService().location();
    double d2;
    if (this.location != null)
    {
      double d1 = this.location.getDouble("Lat");
      d2 = this.location.getDouble("Lng");
      if ((d1 != 0.0D) && (d2 != 0.0D) && (d1 != (-1.0D / 0.0D)) && (d1 != (1.0D / 0.0D)) && (d2 != (-1.0D / 0.0D)) && (d2 != (1.0D / 0.0D)))
        this.latitude = (Location.FMT.format(d1) + "");
    }
    for (this.longitude = (Location.FMT.format(d2) + ""); ; this.longitude = (Location.FMT.format(0L) + ""))
    {
      this.dishshopid = ((DishDetailInfoFragment)getFragment()).dishshopid;
      localBuilder.appendQueryParameter("cityid", "" + cityId());
      localBuilder.appendQueryParameter("dishshopid", this.dishshopid);
      localBuilder.appendQueryParameter("latitude", this.latitude);
      localBuilder.appendQueryParameter("longitude", this.longitude);
      this.dishDetailInfoRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.NORMAL);
      getFragment().mapiService().exec(this.dishDetailInfoRequest, this);
      return;
      this.latitude = (Location.FMT.format(0L) + "");
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    NovaLinearLayout localNovaLinearLayout;
    if ((this.rankResult != null) && (!android.text.TextUtils.isEmpty(this.rankResult.getString("Name"))))
    {
      this.cell = this.res.inflate(getContext(), R.layout.dish_detail_agent_layout, getParentView(), false);
      paramBundle = (TextView)this.cell.findViewById(R.id.dish_name);
      Object localObject = (TextView)this.cell.findViewById(R.id.desc);
      paramBundle.setText(this.rankResult.getString("Name"));
      ((TextView)localObject).setText(this.rankResult.getString("Desc"));
      paramBundle = (NetworkImageView)this.cell.findViewById(R.id.info);
      localObject = (NetworkImageView)this.cell.findViewById(R.id.rank_icon);
      TextView localTextView1 = (TextView)this.cell.findViewById(R.id.rank_content);
      TextView localTextView2 = (TextView)this.cell.findViewById(R.id.rank_subtittle_tv);
      localNovaLinearLayout = (NovaLinearLayout)this.cell.findViewById(R.id.ranking_list);
      localNovaLinearLayout.setGAString("ranking_list");
      this.rank = this.rankResult.getObject("Rank");
      if (this.rank == null)
        break label398;
      if (!android.text.TextUtils.isEmpty(this.rank.getString("Icon")))
        ((NetworkImageView)localObject).setImage(this.rank.getString("Icon"));
      if (!android.text.TextUtils.isEmpty(this.rank.getString("Title")))
        localTextView1.setText(com.dianping.util.TextUtils.jsonParseText(this.rank.getString("Title")));
      if (!android.text.TextUtils.isEmpty(this.rank.getString("SubTitle")))
        localTextView2.setText(this.rank.getString("SubTitle"));
      localNovaLinearLayout.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (!android.text.TextUtils.isEmpty(DishDetailInfoAgent.this.rank.getString("Scheme")))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(DishDetailInfoAgent.this.rank.getString("Scheme")));
            DishDetailInfoAgent.this.getFragment().getActivity().startActivity(paramView);
          }
        }
      });
      ((DPActivity)getContext()).addGAView(localNovaLinearLayout, -1);
    }
    while (true)
    {
      this.defaultPic = this.rankResult.getString("DefaultPic");
      if (!android.text.TextUtils.isEmpty(this.defaultPic))
        paramBundle.setImage(this.defaultPic);
      paramBundle.setGAString("info");
      ((DPActivity)getContext()).addGAView(paramBundle, -1);
      this.name = this.rankResult.getString("Name");
      addCell("0100Dish.0100Top", this.cell);
      return;
      label398: localNovaLinearLayout.setVisibility(8);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onDestroy()
  {
    if ((this.dishDetailInfoRequest != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.dishDetailInfoRequest, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.dishDetailInfoRequest == paramMApiRequest)
    {
      this.rankResult = null;
      this.rank = null;
      this.defaultPic = null;
      this.desc = null;
      this.name = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.dishDetailInfoRequest == paramMApiRequest)
    {
      this.rankResult = ((DPObject)paramMApiResponse.result());
      ((DPActivity)getContext()).gaExtra.shop_id = Integer.valueOf(Integer.parseInt(this.dishshopid));
      dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.dish.DishDetailInfoAgent
 * JD-Core Version:    0.6.0
 */
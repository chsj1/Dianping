package com.dianping.shopinfo.clothes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.base.widget.wed.WedBaseAdapter;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.clothes.view.PicItemView;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.util.Arrays;

public class RecommendAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_RECOMMEND = "2000Clothes.Recommend";
  ShopinfoCommonCell commCell;
  private String gaString = "shareorder";
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (RecommendAgent.this.mRecommend != null)
      {
        paramView = RecommendAgent.this.mRecommend.getString("Url");
        if ((paramView != null) && (!paramView.equals("")))
        {
          Intent localIntent = new Intent("android.intent.action.VIEW");
          localIntent.setData(Uri.parse(paramView));
          RecommendAgent.this.startActivity(localIntent);
        }
      }
    }
  };
  private DPObject mRecommend;
  private DPObject[] recommendPics;
  private String recommendTags;
  private MApiRequest request;

  public RecommendAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createRecommendCell()
  {
    if (this.mRecommend == null);
    View localView;
    do
    {
      do
        return null;
      while ((this.recommendTags == null) && (this.recommendPics == null));
      localView = getRecommendView();
    }
    while (localView == null);
    if (this.commCell == null)
      GAHelper.instance().contextStatisticsEvent(getContext(), this.gaString, getGAExtra(), "view");
    this.commCell = ((ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_clothes_recommend_layout, getParentView(), false));
    this.commCell.setTitle(this.mRecommend.getString("Title"));
    if ((this.mRecommend.getString("Url") != null) && (this.mRecommend.getString("Url").length() > 0))
    {
      TextView localTextView = (TextView)this.commCell.findViewById(R.id.sub_title);
      localTextView.setText(this.mRecommend.getString("PromoTitle"));
      localTextView.setTextColor(Color.parseColor(this.mRecommend.getString("PromoColor")));
      ((ImageView)this.commCell.findViewById(R.id.indicator)).setVisibility(0);
      this.commCell.setOnClickListener(this.mListener);
    }
    this.commCell.setGAString(this.gaString);
    this.commCell.addContent(localView, false, this.mListener);
    return this.commCell;
  }

  private View getRecommendView()
  {
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_clothes_recommend_content, null, false);
    Object localObject;
    if ((this.recommendTags != null) && (this.recommendTags.length() > 0))
    {
      localObject = (TextView)localView.findViewById(R.id.recommend_tags);
      ((TextView)localObject).setText(this.recommendTags.trim());
      ((TextView)localObject).setLineSpacing(ViewUtils.dip2px(getContext(), 7.4F), 1.0F);
      ((TextView)localObject).setVisibility(0);
    }
    if ((this.recommendPics != null) && (this.recommendPics.length > 0))
    {
      if (this.recommendPics.length > 3)
        this.recommendPics = ((DPObject[])Arrays.copyOfRange(this.recommendPics, 0, 3));
      localObject = new ClothesGridProductAdapter(getContext(), this.recommendPics);
      MeasuredGridView localMeasuredGridView = (MeasuredGridView)localView.findViewById(R.id.gallery_gridview);
      localMeasuredGridView.setVisibility(0);
      localMeasuredGridView.setAdapter((ListAdapter)localObject);
      ((ClothesGridProductAdapter)localObject).notifyDataSetChanged();
    }
    localView.setOnClickListener(this.mListener);
    return (View)localView;
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/getnetfriendrec.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopId", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.request = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      return;
      if ((this.mRecommend == null) && (this.request == null))
        sendRequest();
      paramBundle = createRecommendCell();
    }
    while (paramBundle == null);
    addCell("2000Clothes.Recommend", paramBundle, this.gaString, 0);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.mRecommend = ((DPObject)paramMApiResponse.result());
    if (this.mRecommend == null);
    do
    {
      return;
      if ((this.mRecommend.getString("RecommendTags") != null) && (this.mRecommend.getString("RecommendTags").length() > 0))
        this.recommendTags = this.mRecommend.getString("RecommendTags");
      if ((this.mRecommend.getArray("RecommendPics") == null) || (this.mRecommend.getArray("RecommendPics").length <= 0))
        continue;
      this.recommendPics = this.mRecommend.getArray("RecommendPics");
    }
    while ((this.recommendTags == null) && (this.recommendPics == null));
    dispatchAgentChanged(false);
  }

  public class ClothesGridProductAdapter extends WedBaseAdapter
  {
    public ClothesGridProductAdapter(Context paramArrayOfDPObject, DPObject[] arg3)
    {
      this.context = paramArrayOfDPObject;
      Object localObject;
      this.adapterData = localObject;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = (DPObject)getItem(paramInt);
      if (paramView == null)
        return null;
      paramViewGroup = PicItemView.createView(RecommendAgent.this.getContext(), paramViewGroup);
      paramViewGroup.init(paramView.getString("Url"));
      paramViewGroup.setOnClickListener(RecommendAgent.this.mListener);
      paramViewGroup.setGAString(RecommendAgent.this.gaString);
      return paramViewGroup;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.clothes.RecommendAgent
 * JD-Core Version:    0.6.0
 */
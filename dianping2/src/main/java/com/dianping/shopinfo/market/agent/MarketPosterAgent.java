package com.dianping.shopinfo.market.agent;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.HorizontalImageGallery;
import com.dianping.base.widget.HorizontalImageGallery.OnGalleryImageClickListener;
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
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;

public class MarketPosterAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_POSTER = "0400Event.01Poster";
  ArrayList<DPObject> bigImageUrls = new ArrayList();
  private ShopinfoCommonCell commCell;
  private HorizontalImageGallery imageGallery;
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (MarketPosterAgent.this.mPoster != null)
      {
        paramView = MarketPosterAgent.this.mPoster.getString("Url");
        if ((paramView != null) && (!paramView.equals("")))
        {
          Intent localIntent = new Intent("android.intent.action.VIEW");
          localIntent.setData(Uri.parse(paramView));
          MarketPosterAgent.this.startActivity(localIntent);
        }
      }
    }
  };
  private DPObject mPoster;
  private DPObject[] posterPics;
  private MApiRequest request;

  public MarketPosterAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void buildGallery()
  {
    this.imageGallery.removeAllImages();
    this.bigImageUrls.clear();
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if (i < this.posterPics.length)
    {
      if ((TextUtils.isEmpty(this.posterPics[i].getString("SmallImageUrl"))) || (TextUtils.isEmpty(this.posterPics[i].getString("BigImageUrl"))));
      while (true)
      {
        i += 1;
        break;
        String str2 = this.posterPics[i].getString("OnlineTime");
        String str1 = str2;
        if (TextUtils.isEmpty(str2))
          str1 = "";
        localArrayList.add(this.posterPics[i].getString("SmallImageUrl"));
        this.bigImageUrls.add(new DPObject().edit().putString("Url", this.posterPics[i].getString("BigImageUrl")).putString("OnlineTime", str1).generate());
      }
    }
    this.imageGallery.setElementName("martpromo");
    this.imageGallery.addImages((String[])localArrayList.toArray(new String[0]), false);
    this.imageGallery.setOnGalleryImageClickListener(new HorizontalImageGallery.OnGalleryImageClickListener()
    {
      public void onGalleryImageClick(int paramInt1, int paramInt2, Drawable paramDrawable)
      {
        paramDrawable = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showmarketposter"));
        paramDrawable.putExtra("position", paramInt1);
        paramDrawable.putParcelableArrayListExtra("pageList", MarketPosterAgent.this.bigImageUrls);
        MarketPosterAgent.this.getFragment().startActivity(paramDrawable);
      }
    });
    this.imageGallery.setVisibility(0);
  }

  private View createPosterCell()
  {
    if (this.commCell == null)
      GAHelper.instance().contextStatisticsEvent(getContext(), "martpromo", getGAExtra(), "view");
    this.commCell = ((ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_market_poster_layout, getParentView(), false));
    String str2 = this.mPoster.getString("Title");
    String str1 = str2;
    if (this.mPoster.getInt("PosterCount") > 0)
      str1 = str2 + "(" + this.mPoster.getInt("PosterCount") + ")";
    this.commCell.setTitle(str1, this.mListener);
    this.imageGallery = ((HorizontalImageGallery)this.commCell.findViewById(R.id.poster_image_gallery));
    this.commCell.setSubTitle(this.mPoster.getString("SubTitle"));
    buildGallery();
    this.commCell.getTitleLay().setGAString("martpromo");
    return this.commCell;
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/getmarketposterinfo.bin?").buildUpon();
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
      do
      {
        return;
        if ((this.mPoster != null) || (this.request != null))
          continue;
        sendRequest();
      }
      while ((this.mPoster == null) || (this.posterPics == null) || (this.posterPics.length <= 0));
      paramBundle = createPosterCell();
    }
    while (paramBundle == null);
    addCell("0400Event.01Poster", paramBundle, 0);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    if ((paramMApiResponse == null) || (paramMApiResponse.result() == null));
    do
    {
      do
      {
        return;
        this.mPoster = ((DPObject)paramMApiResponse.result());
      }
      while (this.mPoster == null);
      if ((this.mPoster.getArray("MarketPosterImages") == null) || (this.mPoster.getArray("MarketPosterImages").length <= 0))
        continue;
      this.posterPics = this.mPoster.getArray("MarketPosterImages");
    }
    while ((this.posterPics == null) || (this.posterPics.length <= 0));
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.market.agent.MarketPosterAgent
 * JD-Core Version:    0.6.0
 */
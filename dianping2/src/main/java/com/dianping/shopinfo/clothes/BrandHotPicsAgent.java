package com.dianping.shopinfo.clothes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
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
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.Arrays;

public class BrandHotPicsAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_BRANDPIC = "1990Clothes.BrandHotPic";
  private DPObject[] barndPics;
  ArrayList<DPObject> bigImageUrls = new ArrayList();
  private DPObject[] brandCategorys;
  private ShopinfoCommonCell commCell;
  private String gaString = "brandposter";
  private DPObject mBrandPicInfos;
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (BrandHotPicsAgent.this.mBrandPicInfos != null)
      {
        paramView = BrandHotPicsAgent.this.mBrandPicInfos.getString("Url");
        if (!TextUtils.isEmpty(paramView))
        {
          Intent localIntent = new Intent("android.intent.action.VIEW");
          localIntent.setData(Uri.parse(paramView));
          BrandHotPicsAgent.this.startActivity(localIntent);
        }
      }
    }
  };
  private MApiRequest request;

  public BrandHotPicsAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createBrandPicsCell()
  {
    if ((this.mBrandPicInfos == null) || (this.barndPics == null) || (this.barndPics.length < 5))
      return null;
    if (this.commCell == null)
      GAHelper.instance().contextStatisticsEvent(getContext(), this.gaString, getGAExtra(), "view");
    this.commCell = ((ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false));
    this.commCell.setTitle(this.mBrandPicInfos.getString("Title") + "(" + this.mBrandPicInfos.getInt("Count") + ")", this.mListener);
    this.commCell.titleLay.setGAString(this.gaString);
    this.commCell.addContent(getBrandHotPicView(), false, null);
    return this.commCell;
  }

  private View getBrandHotPicView()
  {
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.clothes_brand_hotpic_layout, getParentView(), false);
    this.bigImageUrls.clear();
    int i = 0;
    while (i < this.barndPics.length)
    {
      this.bigImageUrls.add(new DPObject().edit().putString("Url", this.barndPics[i].getString("BigImageUrl")).putString("OnlineTime", this.barndPics[i].getString("OnlineTime")).generate());
      i += 1;
    }
    int j = R.id.pic1;
    int k = R.id.pic2;
    int m = R.id.pic3;
    int n = R.id.pic4;
    int i1 = R.id.pic5;
    i = 0;
    while (true)
    {
      if ((i >= this.barndPics.length) || (i >= 5))
      {
        if ((this.brandCategorys != null) && (this.brandCategorys.length > 0))
        {
          if (this.brandCategorys.length > 3)
            this.brandCategorys = ((DPObject[])Arrays.copyOfRange(this.brandCategorys, 0, 3));
          localObject = new BrandGridAdapter(getContext(), this.brandCategorys);
          MeasuredGridView localMeasuredGridView = (MeasuredGridView)localView.findViewById(R.id.category_gridview);
          localMeasuredGridView.setVisibility(0);
          localMeasuredGridView.setAdapter((ListAdapter)localObject);
          ((BrandGridAdapter)localObject).notifyDataSetChanged();
        }
        return localView;
      }
      Object localObject = (NetworkImageView)localView.findViewById(new int[] { j, k, m, n, i1 }[i]);
      ((NetworkImageView)localObject).setImage(this.barndPics[i].getString("SmallImageUrl"));
      ((NetworkImageView)localObject).setTag(Integer.valueOf(i));
      ((NetworkImageView)localObject).setGAString(this.gaString);
      ((NetworkImageView)localObject).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showmarketposter"));
          localIntent.putExtra("position", (Integer)paramView.getTag());
          localIntent.putParcelableArrayListExtra("pageList", BrandHotPicsAgent.this.bigImageUrls);
          BrandHotPicsAgent.this.getFragment().startActivity(localIntent);
        }
      });
      i += 1;
    }
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/getbrandposter.bin?").buildUpon();
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
      if ((this.mBrandPicInfos == null) && (this.request == null))
        sendRequest();
      paramBundle = createBrandPicsCell();
    }
    while (paramBundle == null);
    addCell("1990Clothes.BrandHotPic", paramBundle, 0);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.mBrandPicInfos = ((DPObject)paramMApiResponse.result());
    if (this.mBrandPicInfos == null);
    do
    {
      return;
      if ((this.mBrandPicInfos.getArray("Category") != null) && (this.mBrandPicInfos.getArray("Category").length > 0))
        this.brandCategorys = this.mBrandPicInfos.getArray("Category");
      if ((this.mBrandPicInfos.getArray("ImageList") == null) || (this.mBrandPicInfos.getArray("ImageList").length <= 0))
        continue;
      this.barndPics = this.mBrandPicInfos.getArray("ImageList");
    }
    while (this.barndPics == null);
    dispatchAgentChanged(false);
  }

  public class BrandGridAdapter extends WedBaseAdapter
  {
    public BrandGridAdapter(Context paramArrayOfDPObject, DPObject[] arg3)
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
      paramViewGroup = (NovaTextView)LayoutInflater.from(this.context).inflate(R.layout.clothes_brand_hotpic_category_item, paramViewGroup, false);
      paramViewGroup.setText(paramView.getString("Name"));
      paramViewGroup.setOnClickListener(new View.OnClickListener(paramView)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$object.getString("Url");
          if (!TextUtils.isEmpty(paramView))
          {
            Intent localIntent = new Intent("android.intent.action.VIEW");
            localIntent.setData(Uri.parse(paramView));
            BrandHotPicsAgent.this.startActivity(localIntent);
          }
        }
      });
      paramViewGroup.setGAString("brandposterfilter");
      return paramViewGroup;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.clothes.BrandHotPicsAgent
 * JD-Core Version:    0.6.0
 */
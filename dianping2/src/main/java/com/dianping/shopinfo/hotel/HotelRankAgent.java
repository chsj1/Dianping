package com.dianping.shopinfo.hotel;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;

public class HotelRankAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_RANK = "0250Rank.";
  private DPObject[] rankList;
  private MApiRequest request;

  public HotelRankAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createRankView(DPObject[] paramArrayOfDPObject)
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.hideTitle();
    int j = paramArrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      if ((localDPObject != null) && (!com.dianping.util.TextUtils.isEmpty(localDPObject.getString("Title"))))
      {
        LinearLayout localLinearLayout = new LinearLayout(getContext());
        localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 50.0F)));
        localLinearLayout.setOrientation(0);
        Object localObject1 = new ImageView(getContext());
        Object localObject2 = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 20.0F), ViewUtils.dip2px(getContext(), 20.0F));
        ((LinearLayout.LayoutParams)localObject2).gravity = 16;
        ((LinearLayout.LayoutParams)localObject2).leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
        ((ImageView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
        ((ImageView)localObject1).setImageResource(R.drawable.hotel_shop_info_rank_icon);
        localLinearLayout.addView((View)localObject1);
        localObject1 = new TextView(getContext());
        localObject2 = new LinearLayout.LayoutParams(-2, -1);
        ((LinearLayout.LayoutParams)localObject2).leftMargin = ViewUtils.dip2px(getContext(), 10.0F);
        ((LinearLayout.LayoutParams)localObject2).topMargin = ViewUtils.dip2px(getContext(), 8.0F);
        ((LinearLayout.LayoutParams)localObject2).bottomMargin = ViewUtils.dip2px(getContext(), 8.0F);
        ((LinearLayout.LayoutParams)localObject2).gravity = 16;
        ((TextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
        ((TextView)localObject1).setGravity(16);
        ((TextView)localObject1).setText(localDPObject.getString("Title"));
        localLinearLayout.addView((View)localObject1);
        localObject1 = new TextView(getContext());
        ((TextView)localObject1).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
        ((TextView)localObject1).setTextColor(getResources().getColor(R.color.text_color_light_gray));
        localObject2 = new LinearLayout.LayoutParams(-2, -1);
        ((LinearLayout.LayoutParams)localObject2).rightMargin = ViewUtils.dip2px(getContext(), 15.0F);
        ((LinearLayout.LayoutParams)localObject2).topMargin = ViewUtils.dip2px(getContext(), 8.0F);
        ((LinearLayout.LayoutParams)localObject2).bottomMargin = ViewUtils.dip2px(getContext(), 8.0F);
        ((LinearLayout.LayoutParams)localObject2).gravity = 16;
        ((TextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
        ((TextView)localObject1).setGravity(16);
        ((TextView)localObject1).setText(localDPObject.getString("TitleTag"));
        localObject2 = new LinearLayout(getContext());
        ((LinearLayout)localObject2).setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        ((LinearLayout)localObject2).setGravity(5);
        ((LinearLayout)localObject2).addView((View)localObject1);
        localLinearLayout.addView((View)localObject2);
        localShopinfoCommonCell.addContent(localLinearLayout, true, null);
        localLinearLayout.setOnClickListener(new View.OnClickListener(localDPObject)
        {
          public void onClick(View paramView)
          {
            Object localObject = this.val$rank.getString("Url");
            if (!android.text.TextUtils.isEmpty((CharSequence)localObject))
            {
              if (!((String)localObject).startsWith("http"))
                break label169;
              paramView = Uri.parse("dianping://hotelbookingweb");
              localObject = Uri.parse((String)localObject);
              paramView = new Intent("android.intent.action.VIEW", paramView.buildUpon().appendQueryParameter("url", ((Uri)localObject).toString()).build());
              HotelRankAgent.this.getFragment().startActivity(paramView);
            }
            while (true)
            {
              paramView = new GAUserInfo();
              paramView.title = this.val$rank.getString("Title");
              paramView.shop_id = Integer.valueOf(HotelRankAgent.this.shopId());
              paramView.query_id = HotelRankAgent.this.getFragment().getStringParam("query_id");
              localObject = HotelRankAgent.this.getShop();
              if (localObject != null)
                paramView.category_id = Integer.valueOf(((DPObject)localObject).getInt("CategoryID"));
              GAHelper.instance().contextStatisticsEvent(HotelRankAgent.this.getContext(), "hotel_phb", paramView, "tap");
              return;
              label169: if (!((String)localObject).startsWith("dianping"))
                continue;
              paramView = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
              HotelRankAgent.this.getFragment().startActivity(paramView);
            }
          }
        });
      }
      i += 1;
    }
    return (View)(View)localShopinfoCommonCell;
  }

  private void sendRequest()
  {
    this.request = BasicMApiRequest.mapiGet("http://m.api.dianping.com/hotel/hotelranklist.hotel?shopid=" + shopId(), CacheType.DISABLED);
    if (this.request != null)
      getFragment().mapiService().exec(this.request, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((this.rankList != null) && (this.rankList.length > 0))
      addCell("0250Rank.", createRankView(this.rankList));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onDestroy()
  {
    if (this.request != null)
    {
      getFragment().mapiService().abort(this.request, this, true);
      this.request = null;
    }
    super.onDestroy();
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
      this.rankList = ((DPObject)paramMApiResponse.result()).getArray("HotelRankLists");
      dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelRankAgent
 * JD-Core Version:    0.6.0
 */
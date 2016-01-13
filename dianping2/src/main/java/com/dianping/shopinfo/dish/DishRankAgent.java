package com.dianping.shopinfo.dish;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.GroupCellAgent;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;

public class DishRankAgent extends GroupCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final String CELLISH_RANK = "0200Dish.0200Rank";
  private DPObject mItem1Object;
  private DPObject mItem2Object;
  private DPObject mItem3Object;
  private MApiRequest mRankRequest;
  private DPObject mRankResult;

  public DishRankAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void requestData()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/mshop/generalrank.bin").buildUpon();
    String str1 = "";
    String str2 = "";
    if ((getFragment() instanceof DishDetailInfoFragment))
    {
      str1 = ((DishDetailInfoFragment)getFragment()).dishshopid;
      str2 = "1";
    }
    while (true)
    {
      localBuilder.appendQueryParameter("type", str2);
      localBuilder.appendQueryParameter("generalid", str1);
      localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
      this.mRankRequest = mapiGet(this, localBuilder.build().toString(), CacheType.NORMAL);
      mapiService().exec(this.mRankRequest, this);
      return;
      if (!(getFragment() instanceof ShopInfoFragment))
        continue;
      str1 = String.valueOf(((ShopInfoFragment)getFragment()).shopId);
      str2 = "0";
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    Object localObject1;
    Object localObject2;
    NovaLinearLayout localNovaLinearLayout1;
    NovaLinearLayout localNovaLinearLayout2;
    if (this.mRankResult != null)
    {
      paramBundle = LayoutInflater.from(getContext()).inflate(R.layout.dish_rank_layout, null);
      localObject1 = paramBundle.findViewById(R.id.bangdan_more);
      localObject2 = (NovaLinearLayout)paramBundle.findViewById(R.id.rank_item1);
      localNovaLinearLayout1 = (NovaLinearLayout)paramBundle.findViewById(R.id.rank_item2);
      localNovaLinearLayout2 = (NovaLinearLayout)paramBundle.findViewById(R.id.rank_item3);
      ((View)localObject1).setOnClickListener(this);
      ((NovaLinearLayout)localObject2).setOnClickListener(this);
      localNovaLinearLayout1.setOnClickListener(this);
      localNovaLinearLayout2.setOnClickListener(this);
      ((NovaLinearLayout)localObject2).gaUserInfo.index = Integer.valueOf(1);
      localNovaLinearLayout1.gaUserInfo.index = Integer.valueOf(2);
      localNovaLinearLayout2.gaUserInfo.index = Integer.valueOf(3);
      ((NovaLinearLayout)localObject2).setGAString("bangdan");
      localNovaLinearLayout1.setGAString("bangdan");
      localNovaLinearLayout2.setGAString("bangdan");
      localObject2 = this.mRankResult.getArray("GeneralRanks");
    }
    try
    {
      this.mItem1Object = localObject2[0];
      this.mItem2Object = localObject2[1];
      this.mItem3Object = localObject2[2];
      label180: if (this.mItem1Object == null)
        return;
      ((TextView)paramBundle.findViewById(R.id.title_content)).setText(this.mRankResult.getString("Name"));
      if ((TextUtils.isEmpty(this.mRankResult.getString("Url"))) || (localObject2.length <= 3))
      {
        paramBundle.findViewById(R.id.title_subtitle).setVisibility(8);
        paramBundle.findViewById(R.id.title_arrow).setVisibility(8);
        ((View)localObject1).setClickable(false);
      }
      ((NetworkImageView)paramBundle.findViewById(R.id.item1_img)).setImage(this.mItem1Object.getString("Pic"));
      localObject1 = (TextView)paramBundle.findViewById(R.id.item1_title);
      localObject2 = (TextView)paramBundle.findViewById(R.id.item1_disc);
      ((TextView)localObject1).setText(this.mItem1Object.getString("Titile"));
      ((TextView)localObject2).setText(this.mItem1Object.getString("Desc"));
      String str = this.mItem1Object.getString("Hot");
      if (TextUtils.isEmpty(str))
        paramBundle.findViewById(R.id.item1_hot).setVisibility(4);
      while (true)
      {
        ((TextView)paramBundle.findViewById(R.id.item1_subtitle)).setText(this.mItem1Object.getString("SubTitle"));
        if (this.mItem2Object != null)
        {
          paramBundle.findViewById(R.id.divider12).setVisibility(0);
          localNovaLinearLayout1.setVisibility(0);
          ((TextView)paramBundle.findViewById(R.id.item2_title)).setText(this.mItem2Object.getString("Titile"));
          ((TextView)paramBundle.findViewById(R.id.item2_subtitle)).setText(this.mItem2Object.getString("SubTitle"));
        }
        if (this.mItem3Object != null)
        {
          paramBundle.findViewById(R.id.divider23).setVisibility(0);
          localNovaLinearLayout2.setVisibility(0);
          ((TextView)paramBundle.findViewById(R.id.item3_title)).setText(this.mItem3Object.getString("Titile"));
          ((TextView)paramBundle.findViewById(R.id.item3_subtitle)).setText(this.mItem3Object.getString("SubTitle"));
        }
        GAHelper.instance().contextStatisticsEvent(getContext(), "榜单", getGAExtra(), "view");
        addCell("0200Dish.0200Rank", paramBundle);
        ((TextView)localObject1).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener((TextView)localObject1, (TextView)localObject2)
        {
          public void onGlobalLayout()
          {
            if (this.val$tvTitle.getLineCount() > 1)
              this.val$tvDisc.setVisibility(8);
          }
        });
        return;
        ((TextView)paramBundle.findViewById(R.id.item1_hot)).setText(str);
      }
    }
    catch (Exception localException)
    {
      break label180;
    }
  }

  public void onClick(View paramView)
  {
    String str = "";
    int i = paramView.getId();
    if (i == R.id.bangdan_more)
      paramView = this.mRankResult.getString("Url");
    while (true)
    {
      if (!TextUtils.isEmpty(paramView))
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView)));
      return;
      if (i == R.id.rank_item1)
      {
        paramView = this.mItem1Object.getString("Url");
        continue;
      }
      if (i == R.id.rank_item2)
      {
        paramView = this.mItem2Object.getString("Url");
        continue;
      }
      paramView = str;
      if (i != R.id.rank_item3)
        continue;
      paramView = this.mItem3Object.getString("Url");
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestData();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mRankRequest == paramMApiRequest)
    {
      this.mRankResult = null;
      this.mItem1Object = null;
      this.mItem2Object = null;
      this.mItem3Object = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mRankRequest == paramMApiRequest)
    {
      this.mRankResult = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.dish.DishRankAgent
 * JD-Core Version:    0.6.0
 */
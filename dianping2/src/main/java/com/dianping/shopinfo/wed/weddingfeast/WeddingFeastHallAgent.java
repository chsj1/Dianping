package com.dianping.shopinfo.wed.weddingfeast;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
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
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaRelativeLayout;

public class WeddingFeastHallAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_WEDDING_FEAST_HALL = "0300Hall.0001";
  ShopinfoCommonCell commCell;
  LinearLayout expandLayout;
  NovaRelativeLayout expandView;
  private boolean hasRequestSend = false;
  boolean isExpand;
  private boolean isHezuo;
  LinearLayout linearLayout;
  DPObject[] mHallList;
  MApiRequest mHallListRequest;

  public WeddingFeastHallAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createHallDealCell(DPObject paramDPObject, int paramInt)
  {
    if (paramDPObject == null)
      return null;
    this.isHezuo = paramDPObject.getBoolean(41528);
    if (isBanquetType())
      this.isHezuo = true;
    if (this.isHezuo);
    for (NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.wedding_feast_hall_item_has_choose_date, getParentView(), false); localNovaRelativeLayout == null; localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.wedding_feast_hall_item_none_choose_date, getParentView(), false))
      return null;
    Object localObject3 = (TextView)localNovaRelativeLayout.findViewById(R.id.name);
    Object localObject2 = (TextView)localNovaRelativeLayout.findViewById(R.id.table);
    Object localObject1 = (TextView)localNovaRelativeLayout.findViewById(R.id.extra);
    Object localObject4 = (NetworkImageView)localNovaRelativeLayout.findViewById(R.id.img);
    if ((localObject4 != null) && (paramDPObject.getString("Img") != null))
      ((NetworkImageView)localObject4).setImage(paramDPObject.getString("Img"));
    int j;
    int i;
    if (this.isHezuo)
    {
      j = 0;
      i = j;
      if (localObject3 != null)
      {
        localObject4 = paramDPObject.getString("Name");
        if ((localObject4 == null) || (((String)localObject4).equals("")))
        {
          ((TextView)localObject3).setVisibility(8);
          i = j;
        }
      }
      else
      {
        j = i;
        if (localObject2 != null)
        {
          localObject4 = paramDPObject.getString("Table");
          if ((localObject4 != null) && (!((String)localObject4).equals("")))
            break label455;
          ((TextView)localObject2).setVisibility(8);
          j = i;
        }
        label259: i = j;
        if (localObject1 != null)
        {
          localObject4 = paramDPObject.getString("Extra");
          if ((localObject4 != null) && (!((String)localObject4).equals("")))
            break label477;
          ((TextView)localObject1).setVisibility(8);
          i = j;
        }
        label302: if (i == 2)
        {
          if (((TextView)localObject3).getVisibility() != 0)
            break label499;
          ((TextView)localObject3).setPadding(((TextView)localObject3).getPaddingLeft(), 25, ((TextView)localObject3).getPaddingRight(), ((TextView)localObject3).getPaddingBottom());
          label338: if (((TextView)localObject1).getVisibility() != 0)
            break label524;
          ((TextView)localObject1).setPadding(((TextView)localObject1).getPaddingLeft(), ((TextView)localObject1).getPaddingTop(), ((TextView)localObject1).getPaddingRight(), 25);
        }
        label368: if (this.isHezuo)
        {
          localObject1 = (NovaButton)localNovaRelativeLayout.findViewById(R.id.btn);
          ((NovaButton)localObject1).setGAString("banquet_schedule");
          if (localObject1 != null)
          {
            if (!isBanquetType())
              break label702;
            localNovaRelativeLayout.removeView((View)localObject1);
          }
        }
      }
    }
    while (true)
    {
      if (!isBanquetType())
        break label719;
      localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$deal.getString("LinkUrl");
          if (!TextUtils.isEmpty(paramView))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + Uri.encode(paramView)));
            WeddingFeastHallAgent.this.getFragment().startActivity(paramView);
          }
        }
      });
      return localNovaRelativeLayout;
      ((TextView)localObject3).setText((CharSequence)localObject4);
      ((TextView)localObject3).setVisibility(0);
      i = 0 + 1;
      break;
      label455: ((TextView)localObject2).setText((CharSequence)localObject4);
      ((TextView)localObject2).setVisibility(0);
      j = i + 1;
      break label259;
      label477: ((TextView)localObject1).setText((CharSequence)localObject4);
      ((TextView)localObject1).setVisibility(0);
      i = j + 1;
      break label302;
      label499: ((TextView)localObject2).setPadding(((TextView)localObject2).getPaddingLeft(), 25, ((TextView)localObject2).getPaddingRight(), ((TextView)localObject2).getPaddingBottom());
      break label338;
      label524: ((TextView)localObject2).setPadding(((TextView)localObject2).getPaddingLeft(), ((TextView)localObject2).getPaddingTop(), ((TextView)localObject2).getPaddingRight(), 25);
      break label368;
      if (localObject3 != null)
      {
        localObject4 = paramDPObject.getString("Name");
        if ((localObject4 == null) || (((String)localObject4).equals("")))
          ((TextView)localObject3).setVisibility(0);
      }
      else
      {
        label583: if (localObject2 != null)
        {
          localObject3 = paramDPObject.getString("Table");
          if ((localObject3 != null) && (!((String)localObject3).equals("")))
            break label670;
          ((TextView)localObject2).setVisibility(0);
        }
      }
      while (true)
      {
        if (localObject1 == null)
          break label684;
        localObject2 = paramDPObject.getString("Extra");
        if ((localObject2 != null) && (!((String)localObject2).equals("")))
          break label686;
        ((TextView)localObject1).setVisibility(0);
        break;
        ((TextView)localObject3).setText((CharSequence)localObject4);
        ((TextView)localObject3).setVisibility(0);
        break label583;
        label670: ((TextView)localObject2).setText((CharSequence)localObject3);
        ((TextView)localObject2).setVisibility(0);
      }
      label684: break label368;
      label686: ((TextView)localObject1).setText((CharSequence)localObject2);
      ((TextView)localObject1).setVisibility(0);
      break label368;
      label702: ((NovaButton)localObject1).setOnClickListener(new View.OnClickListener(paramInt)
      {
        public void onClick(View paramView)
        {
          if (WeddingFeastHallAgent.this.getShop() == null)
            return;
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://weddinghotelbookingdate").buildUpon().appendQueryParameter("shopid", String.valueOf(WeddingFeastHallAgent.this.getShop().getInt("ID"))).appendQueryParameter("shopname", DPObjectUtils.getShopFullName(WeddingFeastHallAgent.this.getShop())).build().toString()));
          paramView.putExtra("objShop", WeddingFeastHallAgent.this.getShop());
          if (WeddingFeastHallAgent.this.getSharedObject("WeddingHotelExtra") != null)
            paramView.putExtra("extraWeddingShop", (DPObject)WeddingFeastHallAgent.this.getSharedObject("WeddingHotelExtra"));
          if ((WeddingFeastHallAgent.this.mHallList != null) && (WeddingFeastHallAgent.this.mHallList.length > this.val$index))
            paramView.putExtra("hallItem", WeddingFeastHallAgent.this.mHallList[this.val$index]);
          WeddingFeastHallAgent.this.getFragment().startActivity(paramView);
        }
      });
    }
    label719: localNovaRelativeLayout.setGAString("banquet_info");
    localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramInt)
    {
      public void onClick(View paramView)
      {
        if (WeddingFeastHallAgent.this.getShop() == null)
          return;
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://weddinghotelphoto"));
        paramView.putExtra("objShop", WeddingFeastHallAgent.this.getShop());
        if (WeddingFeastHallAgent.this.getSharedObject("WeddingHotelExtra") != null)
          paramView.putExtra("extraWeddingShop", (DPObject)WeddingFeastHallAgent.this.getSharedObject("WeddingHotelExtra"));
        if ((WeddingFeastHallAgent.this.mHallList != null) && (WeddingFeastHallAgent.this.mHallList.length > this.val$index))
          paramView.putExtra("hallItem", WeddingFeastHallAgent.this.mHallList[this.val$index]);
        WeddingFeastHallAgent.this.getFragment().startActivity(paramView);
      }
    });
    return (View)(View)(View)(View)localNovaRelativeLayout;
  }

  private void sendRequest()
  {
    this.hasRequestSend = true;
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/wedding/weddinghotelhall.bin?");
    localStringBuffer.append("shopid=").append(shopId());
    this.mHallListRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mHallListRequest, this);
  }

  private void setExpandState()
  {
    if ((this.expandView == null) || (this.expandLayout == null))
      return;
    if (this.isExpand)
    {
      ((TextView)this.expandView.findViewById(16908308)).setText("收起");
      ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.arrow_up_tuan);
      this.expandLayout.setVisibility(0);
      return;
    }
    if (isBanquetType())
      ((TextView)this.expandView.findViewById(16908308)).setText("更多" + (this.mHallList.length - 2) + "个会议场地");
    while (true)
    {
      ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.arrow_down_tuan);
      this.expandView.findViewById(16908308).setVisibility(0);
      this.expandLayout.setVisibility(8);
      return;
      ((TextView)this.expandView.findViewById(16908308)).setText("更多宴会厅");
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((!isWeddingType()) && (!isBanquetType()));
    do
    {
      do
      {
        return;
        paramBundle = (DPObject)getSharedObject("WeddingHotelExtra");
      }
      while ((!isBanquetType()) && ((paramBundle == null) || (paramBundle.getInt("HallCount") == 0)));
      if ((this.mHallList != null) || (this.hasRequestSend))
        continue;
      sendRequest();
      return;
    }
    while (this.mHallList == null);
    removeAllCells();
    this.commCell = ((ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false));
    if (isBanquetType())
    {
      this.commCell.setTitle("会议场地");
      this.commCell.setSubTitle("");
    }
    while (true)
    {
      this.commCell.hideArrow();
      this.linearLayout = new LinearLayout(getContext());
      this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      this.linearLayout.setOrientation(1);
      this.linearLayout.addView(createHallDealCell(this.mHallList[0], 0));
      if (this.mHallList.length > 1)
        this.linearLayout.addView(createHallDealCell(this.mHallList[1], 1));
      if (this.mHallList.length <= 2)
        break;
      this.expandLayout = new LinearLayout(getContext());
      this.expandLayout.setOrientation(1);
      this.expandLayout.removeAllViews();
      paramBundle = new LinearLayout.LayoutParams(-1, -2);
      if (!this.isExpand)
        this.expandLayout.setVisibility(8);
      this.expandLayout.setLayoutParams(paramBundle);
      int i = 2;
      while (true)
        if (i < this.mHallList.length)
        {
          this.expandLayout.addView(createHallDealCell(this.mHallList[i], i));
          i += 1;
          continue;
          this.commCell.setTitle("宴会厅");
          this.commCell.setSubTitle("(" + paramBundle.getInt("HallCount") + ")");
          break;
        }
      this.linearLayout.addView(this.expandLayout);
      this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.expand_wedding_feast, getParentView(), false));
      if (!this.isExpand)
        break label527;
      this.expandView.setGAString("banquet_less");
    }
    while (true)
    {
      if (this.expandView != null)
      {
        ((TextView)this.expandView.findViewById(16908308)).setText("更多宴会厅");
        this.expandView.setClickable(true);
        this.expandView.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = WeddingFeastHallAgent.this;
            if (!WeddingFeastHallAgent.this.isExpand);
            for (boolean bool = true; ; bool = false)
            {
              paramView.isExpand = bool;
              WeddingFeastHallAgent.this.setExpandState();
              return;
            }
          }
        });
        this.linearLayout.addView(this.expandView);
      }
      this.commCell.addContent(this.linearLayout, false);
      setExpandState();
      addCell("0300Hall.0001", this.commCell);
      return;
      label527: this.expandView.setGAString("banquet_more");
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null);
    for (boolean bool = false; ; bool = paramBundle.getBoolean("isExpand"))
    {
      this.isExpand = bool;
      return;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiResponse == null)
      return;
    this.mHallList = ((DPObject[])(DPObject[])paramMApiResponse.result());
    dispatchAgentChanged(false);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putBoolean("isExpand", this.isExpand);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.weddingfeast.WeddingFeastHallAgent
 * JD-Core Version:    0.6.0
 */
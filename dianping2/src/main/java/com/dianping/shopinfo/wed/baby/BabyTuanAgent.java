package com.dianping.shopinfo.wed.baby;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.wed.baby.widget.GridTuanAdapter;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.shopinfo.widget.TuanTicketCell;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class BabyTuanAgent extends ShopCellAgent
  implements View.OnClickListener, AdapterView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  final DecimalFormat PRICE_DF = new DecimalFormat("#.###");
  DPObject babyTuanObject;
  MApiRequest babyTuanRequest;
  LinearLayout expandLayout;
  NovaRelativeLayout expandView;
  GridTuanAdapter gridTuanAdapter;
  boolean isExpand;
  MeasuredGridView measuredGridView;
  String moreText = "";

  public BabyTuanAgent(Object paramObject)
  {
    super(paramObject);
    sendBabyTuanRequest();
  }

  private void setExpandAction()
  {
    if (this.expandLayout == null)
      return;
    this.expandLayout.postDelayed(new Runnable()
    {
      public void run()
      {
        if (BabyTuanAgent.this.isExpand)
          BabyTuanAgent.this.expandLayout.setVisibility(0);
        while (true)
        {
          BabyTuanAgent.this.setExpandState();
          return;
          BabyTuanAgent.this.expandLayout.setVisibility(8);
        }
      }
    }
    , 100L);
  }

  public CommonCell createDefaultDealCell(DPObject paramDPObject, boolean paramBoolean)
  {
    TuanTicketCell localTuanTicketCell = (TuanTicketCell)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.tuan_cell_shopinfo_icon, getParentView(), false);
    localTuanTicketCell.setGAString("tuan", getGAExtra());
    localTuanTicketCell.findViewById(R.id.arrow).setVisibility(4);
    localTuanTicketCell.setSubTitle(paramDPObject.getString("ContentTitle"));
    localTuanTicketCell.setClickable(true);
    localTuanTicketCell.setOnClickListener(this);
    double d1 = paramDPObject.getDouble("Price");
    double d2 = paramDPObject.getDouble("OriginalPrice");
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    SpannableString localSpannableString = new SpannableString("￥" + this.PRICE_DF.format(d1));
    localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
    localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_title)), 1, localSpannableString.length(), 33);
    localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, localSpannableString.length(), 33);
    localSpannableStringBuilder.append(localSpannableString);
    localSpannableStringBuilder.append(" ");
    localSpannableString = new SpannableString("￥" + this.PRICE_DF.format(d2));
    localSpannableString.setSpan(new StrikethroughSpan(), 1, localSpannableString.length(), 33);
    localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_hint)), 0, localSpannableString.length(), 33);
    localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_gray)), 0, localSpannableString.length(), 33);
    localSpannableStringBuilder.append(localSpannableString);
    localTuanTicketCell.setTitle(localSpannableStringBuilder);
    localTuanTicketCell.setTag(paramDPObject);
    if (paramDPObject.getString("Photo") != null)
      ((NetworkImageView)localTuanTicketCell.findViewById(R.id.icon)).setImage(paramDPObject.getString("Photo"));
    localTuanTicketCell.findViewById(R.id.layout).setBackgroundResource(0);
    if (!TextUtils.isEmpty(paramDPObject.getString("DiscountDesc")))
      localTuanTicketCell.setRightText(paramDPObject.getString("DiscountDesc"));
    return localTuanTicketCell;
  }

  void createTuanView()
  {
    boolean bool = this.babyTuanObject.getBoolean("HasHrefFlag");
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localLinearLayout.setOrientation(1);
    Object localObject = LayoutInflater.from(getContext()).inflate(R.layout.baby_shopinfo_tuan_header, getParentView(), false);
    if (bool)
    {
      ((View)localObject).findViewById(R.id.babytuan_layout_list_header).setVisibility(0);
      ((View)localObject).findViewById(R.id.babytuan_layout_normal_header).setVisibility(8);
      ((TextView)((View)localObject).findViewById(R.id.babytuan_shopinfo_textview)).setText(this.babyTuanObject.getString("HrefContent"));
      ((View)localObject).setClickable(true);
      ((View)localObject).setTag("HEADER");
      ((View)localObject).setOnClickListener(this);
    }
    while (true)
    {
      localLinearLayout.addView((View)localObject);
      localLinearLayout.addView(line());
      bool = this.babyTuanObject.getBoolean("RectangleOrSquareFlag");
      localObject = this.babyTuanObject.getArray("List");
      if ((localObject != null) && (localObject.length != 0))
        break;
      removeAllCells();
      return;
      ((View)localObject).findViewById(R.id.babytuan_layout_list_header).setVisibility(8);
      ((View)localObject).findViewById(R.id.babytuan_layout_normal_header).setVisibility(0);
      bool = this.babyTuanObject.getBoolean("OverdueAutoRefund");
      if (!this.babyTuanObject.getBoolean("IsAutoRefund"))
      {
        ((View)localObject).findViewById(R.id.icon_refund).setVisibility(8);
        ((View)localObject).findViewById(R.id.refund_support_day).setVisibility(8);
      }
      while (true)
      {
        if (bool)
          break label309;
        ((View)localObject).findViewById(R.id.icon_expire).setVisibility(8);
        ((View)localObject).findViewById(R.id.refund_support_expired).setVisibility(8);
        break;
        ((View)localObject).findViewById(R.id.icon_refund).setVisibility(0);
        ((View)localObject).findViewById(R.id.refund_support_day).setVisibility(0);
      }
      label309: ((View)localObject).findViewById(R.id.icon_expire).setVisibility(0);
      ((View)localObject).findViewById(R.id.refund_support_expired).setVisibility(0);
    }
    if (bool)
    {
      localLinearLayout.addView(createDefaultDealCell(localObject[0], true));
      int j = getDisplayCount();
      int i;
      if (localObject.length <= j)
      {
        i = 1;
        int k = localObject.length;
        while (i < k)
        {
          localLinearLayout.addView(line());
          localLinearLayout.addView(createDefaultDealCell(localObject[i], false));
          i += 1;
        }
      }
      if (localObject.length > j)
      {
        i = 1;
        while (i < j)
        {
          localLinearLayout.addView(line());
          localLinearLayout.addView(createDefaultDealCell(localObject[i], false));
          i += 1;
        }
        this.expandLayout = new LinearLayout(getContext());
        this.expandLayout.setOrientation(1);
        if (!this.isExpand)
          this.expandLayout.setVisibility(8);
        i = j;
        j = localObject.length;
        while (i < j)
        {
          this.expandLayout.addView(line());
          this.expandLayout.addView(createDefaultDealCell(localObject[i], false));
          i += 1;
        }
        localLinearLayout.addView(this.expandLayout);
        this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.expand, getParentView(), false));
        this.expandView.setTag("EXPAND");
        this.expandView.setPadding(ViewUtils.dip2px(getContext(), 15.0F), 0, 0, 0);
        this.moreText = ("全部" + localObject.length + "个团购");
        ((TextView)this.expandView.findViewById(16908308)).setText(this.moreText);
        this.expandView.setClickable(true);
        this.expandView.setOnClickListener(this);
        localLinearLayout.addView(this.expandView);
        setExpandState();
      }
    }
    while (true)
    {
      addCell("", localLinearLayout);
      return;
      View localView1 = LayoutInflater.from(getContext()).inflate(R.layout.wedding_product_recommend, getParentView(), false);
      this.measuredGridView = ((MeasuredGridView)localView1.findViewById(R.id.gallery_gridview));
      this.gridTuanAdapter = new GridTuanAdapter(getContext(), localObject);
      this.measuredGridView.setAdapter(this.gridTuanAdapter);
      this.measuredGridView.setOnItemClickListener(this);
      View localView2 = new View(getContext());
      localView2.setBackgroundColor(getResources().getColor(R.color.line_gray));
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
      localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
      localLinearLayout.addView(localView2, localLayoutParams);
      localLinearLayout.addView(localView1);
      if ((localObject.length != 3) && (localObject.length <= 4))
        continue;
      localView1 = new View(getContext());
      localView1.setBackgroundColor(getResources().getColor(R.color.line_gray));
      localLinearLayout.addView(localView1, localLayoutParams);
      this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.expand, getParentView(), false));
      this.expandView.setTag("EXPANDBABY");
      this.expandView.setPadding(ViewUtils.dip2px(getContext(), 15.0F), 0, 0, 0);
      this.moreText = ("全部" + localObject.length + "个团购");
      ((TextView)this.expandView.findViewById(16908308)).setText(this.moreText);
      this.expandView.setClickable(true);
      this.expandView.setOnClickListener(this);
      this.expandView.setGAString("tuan_more");
      localLinearLayout.addView(this.expandView);
    }
  }

  protected int getDisplayCount()
  {
    return 2;
  }

  protected View line()
  {
    View localView = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
    localView.setLayoutParams(localLayoutParams);
    localView.setBackgroundResource(R.color.background_gray);
    return localView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.babyTuanObject == null)
    {
      removeAllCells();
      return;
    }
    removeAllCells();
    createTuanView();
  }

  public void onClick(View paramView)
  {
    boolean bool2 = true;
    boolean bool1 = true;
    if ((paramView.getTag() instanceof DPObject));
    label304: 
    do
    {
      try
      {
        paramView = (DPObject)paramView.getTag();
        Uri.Builder localBuilder = Uri.parse("dianping://tuandeal").buildUpon();
        localBuilder.appendQueryParameter("shopid", shopId() + "");
        localBuilder.appendQueryParameter("id", paramView.getString("TuanGouId"));
        paramView = new Intent("android.intent.action.VIEW", localBuilder.build());
        getContext().startActivity(paramView);
        return;
      }
      catch (java.lang.Exception paramView)
      {
        Log.e("shop", "fail to launch deal", paramView);
        return;
      }
      if (paramView.getTag() == "EXPAND")
      {
        if (!this.isExpand)
        {
          this.isExpand = bool1;
          if (!this.isExpand)
            break label304;
          statisticsEvent("shopinfo5", "shopinfo5_tuan_more", "展开", 0);
          if (isWeddingShopType())
          {
            paramView = new ArrayList();
            paramView.add(new BasicNameValuePair("shopid", shopId() + ""));
            statisticsEvent("shopinfoq", "shopinfoq_tuan_more", "展开", 0, paramView);
          }
        }
        while (true)
        {
          if (isWeddingShopType())
          {
            paramView = new ArrayList();
            paramView.add(new BasicNameValuePair("shopid", shopId() + ""));
            statisticsEvent("shopinfoq", "shopinfoq_tuan_more", "", 0, paramView);
          }
          setExpandAction();
          return;
          bool1 = false;
          break;
          statisticsEvent("shopinfo5", "shopinfo5_tuan_more", "收起", 0);
          if (!isWeddingShopType())
            continue;
          paramView = new ArrayList();
          paramView.add(new BasicNameValuePair("shopid", shopId() + ""));
          statisticsEvent("shopinfoq", "shopinfoq_tuan_more", "收起", 0, paramView);
        }
      }
      if (paramView.getTag() != "EXPANDBABY")
        continue;
      paramView = this.gridTuanAdapter;
      if (!this.gridTuanAdapter.isExpand());
      for (bool1 = bool2; ; bool1 = false)
      {
        paramView.setExpand(bool1);
        this.gridTuanAdapter.notifyDataSetChanged();
        if (!this.gridTuanAdapter.isExpand())
          break;
        this.expandView.findViewById(16908308).setVisibility(0);
        ((TextView)this.expandView.findViewById(16908308)).setText("收起");
        ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_up);
        return;
      }
      this.expandView.findViewById(16908308).setVisibility(0);
      ((TextView)this.expandView.findViewById(16908308)).setText(this.moreText);
      ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_down);
      return;
    }
    while (paramView.getTag() != "HEADER");
    paramView = Uri.parse("dianping://babytuanlist").buildUpon();
    paramView.appendQueryParameter("shopid", shopId() + "");
    paramView = new Intent("android.intent.action.VIEW", paramView.build());
    paramView.putExtra("shop", getShop());
    startActivity(paramView);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = (DPObject)paramAdapterView.getAdapter().getItem(paramInt);
    paramView = Uri.parse("dianping://tuandeal").buildUpon();
    paramView.appendQueryParameter("shopid", shopId() + "");
    paramView.appendQueryParameter("id", paramAdapterView.getString("TuanGouId"));
    paramAdapterView = new Intent("android.intent.action.VIEW", paramView.build());
    getContext().startActivity(paramAdapterView);
    GAHelper.instance().contextStatisticsEvent(getContext(), "tuan_detail", getGAExtra(), "tap");
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.babyTuanRequest)
      this.babyTuanRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.babyTuanRequest)
    {
      this.babyTuanObject = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
    }
  }

  void sendBabyTuanRequest()
  {
    if (this.babyTuanRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/tuangoulist.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("page", "1");
    localBuilder.appendQueryParameter("pagename", "shop");
    this.babyTuanRequest = mapiGet(this, localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.babyTuanRequest, this);
  }

  protected void setExpandState()
  {
    if (this.expandView == null)
      return;
    if (this.isExpand)
    {
      ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_up);
      ((TextView)this.expandView.findViewById(16908308)).setText("收起");
      return;
    }
    ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_down);
    ((TextView)this.expandView.findViewById(16908308)).setText(this.moreText);
    this.expandView.findViewById(16908308).setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.BabyTuanAgent
 * JD-Core Version:    0.6.0
 */
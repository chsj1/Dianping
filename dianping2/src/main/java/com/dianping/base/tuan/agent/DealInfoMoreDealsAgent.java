package com.dianping.base.tuan.agent;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils.TruncateAt;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.base.widget.AutoHideTextView;
import com.dianping.base.widget.ColorBorderTextView;
import com.dianping.base.widget.TableView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.Arrays;

public class DealInfoMoreDealsAgent extends TuanGroupCellAgent
  implements TuanGroupCellAgent.OnCellRefreshListener, RequestHandler<MApiRequest, MApiResponse>
{
  protected static final int ACTION_TYPE_OTHER_SHOP_MORE_DEAL = 1001;
  protected static final int ACTION_TYPE_SAME_SHOP_MORE_DEAL = 1000;
  private static final int MAX_DEAL_COUNT = 6;
  protected final String OTHER_SHOP_CAN_USE = "其他分店可用";
  private DealInfoCommonCell commCell;
  protected int dealId;
  private DPObject dpDeal;
  protected DPObject[] dpMoreDeals;
  private DPObject[] dpOtherShopDeals;
  protected TextView header;
  double latitude;
  double longitude;
  int maxCount;
  protected TableView moreDealsLayout;
  protected boolean moreDealsLoaded;
  protected MApiRequest moreDealsReq;
  TableView otherShopDealsLayout;
  protected int shopId = 0;
  private String shopName;
  protected int status;

  public DealInfoMoreDealsAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void addDealList(TableView paramTableView, DPObject[] paramArrayOfDPObject, int paramInt1, String paramString1, String paramString2, int paramInt2)
  {
    paramTableView.removeAllViews();
    int i = 0;
    if (i < paramArrayOfDPObject.length)
    {
      if ((i == 0) && (!android.text.TextUtils.isEmpty(paramString1)))
      {
        paramString2 = this.res.inflate(getContext(), R.layout.other_deal_header, this.moreDealsLayout, false);
        ((TextView)paramString2.findViewById(R.id.title)).setText(paramString1);
        paramTableView.addView(paramString2);
      }
      if ((paramInt2 <= 0) || (i != paramInt2));
    }
    else
    {
      return;
    }
    DPObject localDPObject = paramArrayOfDPObject[i];
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.other_deal_item, this.moreDealsLayout, false);
    ((TextView)localNovaRelativeLayout.findViewById(R.id.price)).setText("¥" + PriceFormatUtils.formatPrice(localDPObject.getDouble("Price")));
    paramString2 = new SpannableString("¥" + PriceFormatUtils.formatPrice(localDPObject.getDouble("OriginalPrice")));
    paramString2.setSpan(new StrikethroughSpan(), 0, paramString2.length(), 33);
    ((AutoHideTextView)localNovaRelativeLayout.findViewById(R.id.original_price)).setText(paramString2);
    Object localObject2 = localDPObject.getString("SalesDesc");
    String str1 = localDPObject.getString("SalesTag");
    Object localObject1 = (TextView)localNovaRelativeLayout.findViewById(R.id.other_deal_count);
    paramString2 = null;
    if (!com.dianping.util.TextUtils.isEmpty(str1))
      paramString2 = com.dianping.util.TextUtils.jsonParseText(str1);
    while (true)
    {
      if (!com.dianping.util.TextUtils.isEmpty(paramString2))
      {
        ((TextView)localObject1).setText(paramString2);
        ((TextView)localObject1).setVisibility(0);
      }
      paramString2 = (LinearLayout)localNovaRelativeLayout.findViewById(R.id.other_deal_events_ll);
      localObject1 = localDPObject.getArray("EventList");
      if (!DPObjectUtils.isArrayEmpty(localObject1))
      {
        paramString2.removeAllViews();
        paramString2.setVisibility(0);
        int k = Math.min(2, localObject1.length);
        localObject2 = new LinearLayout.LayoutParams(-2, -2);
        ((LinearLayout.LayoutParams)localObject2).setMargins(10, 0, 0, 0);
        int j = 0;
        while (j < k)
        {
          str1 = localObject1[j].getString("ShortTitle");
          if (!android.text.TextUtils.isEmpty(str1))
          {
            ColorBorderTextView localColorBorderTextView = new ColorBorderTextView(getContext());
            String str2 = localObject1[j].getString("Color");
            localColorBorderTextView.setTextColor(str2);
            localColorBorderTextView.setBorderColor("#C8" + str2.substring(1));
            localColorBorderTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
            localColorBorderTextView.setSingleLine();
            localColorBorderTextView.setEllipsize(TextUtils.TruncateAt.END);
            localColorBorderTextView.setPadding(ViewUtils.dip2px(getContext(), 4.0F), 0, ViewUtils.dip2px(getContext(), 4.0F), 0);
            localColorBorderTextView.setText(str1);
            paramString2.addView(localColorBorderTextView, (ViewGroup.LayoutParams)localObject2);
          }
          j += 1;
        }
        if (com.dianping.util.TextUtils.isEmpty((CharSequence)localObject2))
          continue;
        paramString2 = com.dianping.util.TextUtils.jsonParseText((String)localObject2);
        continue;
      }
      paramString2.setVisibility(8);
    }
    localObject1 = localDPObject.getString("DealTitle");
    paramString2 = (String)localObject1;
    if (android.text.TextUtils.isEmpty((CharSequence)localObject1))
      paramString2 = localDPObject.getString("ContentTitle");
    ((TextView)localNovaRelativeLayout.findViewById(R.id.title)).setText(paramString2);
    localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramInt1, localDPObject)
    {
      public void onClick(View paramView)
      {
        if (this.val$actionType == 1000)
          if (!DealInfoMoreDealsAgent.this.handleAction(1000))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
            paramView.putExtra("deal", this.val$deal);
            DealInfoMoreDealsAgent.this.getContext().startActivity(paramView);
          }
        do
          return;
        while ((this.val$actionType != 1001) || (DealInfoMoreDealsAgent.this.handleAction(1001)));
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
        paramView.putExtra("deal", this.val$deal);
        DealInfoMoreDealsAgent.this.getContext().startActivity(paramView);
      }
    });
    if (paramInt1 == 1000)
      localNovaRelativeLayout.setGAString("other_deal");
    while (true)
    {
      ((NovaActivity)getContext()).addGAView(localNovaRelativeLayout, i, "tuandeal", "tuandeal".equals(((NovaActivity)getContext()).getPageName()));
      paramTableView.addView(localNovaRelativeLayout);
      i += 1;
      break;
      if (paramInt1 != 1001)
        continue;
      localNovaRelativeLayout.setGAString("othershop_deal");
    }
  }

  private void setupView()
  {
    this.header = createGroupHeaderCell();
    this.moreDealsLayout = ((TableView)this.res.inflate(getContext(), R.layout.tuan_deal_more, getParentView(), false));
    this.commCell = new DealInfoCommonCell(getContext());
    this.commCell.setTitleSize(0, getResources().getDimension(R.dimen.deal_info_agent_title_text_size));
    this.commCell.setArrowPreSize(0, getResources().getDimension(R.dimen.deal_info_agent_subtitle_text_size));
    this.commCell.setPaddingLeft((int)getResources().getDimension(R.dimen.deal_info_padding_left));
    this.commCell.setPaddingRight((int)getResources().getDimension(R.dimen.deal_info_padding_right));
    this.commCell.addContent(this.moreDealsLayout, false);
    this.moreDealsLayout.setDividerOfGroupEnd(getResources().getDrawable(R.drawable.gray_horizontal_line));
    this.moreDealsLayout.setDivider(new ColorDrawable(0));
  }

  protected String getHeaderTitle()
  {
    if (this.dpDeal != null)
      return this.dpDeal.getString("ShortTitle") + "的其他团购";
    return "其他团购";
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ((paramAgentMessage != null) && ("dealInfoShop".equals(paramAgentMessage.what)))
    {
      if ((paramAgentMessage.body == null) || (paramAgentMessage.body.getParcelable("shop") == null))
        break label71;
      this.shopId = ((DPObject)paramAgentMessage.body.getParcelable("shop")).getInt("ID");
    }
    while (true)
    {
      dispatchAgentChanged(false);
      return;
      label71: if (this.shopId != 0)
        continue;
      this.shopId = -1;
    }
  }

  protected void loadMoreDeals()
  {
    if ((this.moreDealsReq != null) || (this.moreDealsLoaded))
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("moredealslistgn.bin");
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    localUrlBuilder.addParam("dealgroupid", Integer.valueOf(this.dealId));
    localUrlBuilder.addParam("shopid", Integer.valueOf(this.shopId));
    String str = accountService().token();
    if (!android.text.TextUtils.isEmpty(str))
      localUrlBuilder.addParam("token", str);
    if (location() != null)
    {
      localUrlBuilder.addParam("lat", Double.valueOf(location().latitude()));
      localUrlBuilder.addParam("lng", Double.valueOf(location().longitude()));
    }
    this.moreDealsReq = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.moreDealsReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null)
      return;
    if (paramBundle != null)
    {
      this.dealId = paramBundle.getInt("dealid");
      this.shopId = paramBundle.getInt("shopid", 0);
      this.status = paramBundle.getInt("status");
      paramBundle = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal != paramBundle)
        this.dpDeal = paramBundle;
    }
    if ((this.status == 1) && (this.shopId != 0))
      loadMoreDeals();
    if ((this.header == null) || (this.moreDealsLayout == null))
      setupView();
    updateView();
  }

  public void onCreate(Bundle paramBundle)
  {
    Object localObject2 = null;
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      localObject1 = paramBundle.getParcelableArrayList("MoreDeals");
      if (localObject1 != null)
        break label61;
      localObject1 = null;
      this.dpMoreDeals = ((DPObject)localObject1);
      localObject1 = paramBundle.getParcelableArrayList("OtherShopDeals");
      if (localObject1 != null)
        break label79;
    }
    label61: label79: for (Object localObject1 = localObject2; ; localObject1 = (DPObject[])((ArrayList)localObject1).toArray(new DPObject[((ArrayList)localObject1).size()]))
    {
      this.dpMoreDeals = ((DPObject)localObject1);
      this.moreDealsLoaded = paramBundle.getBoolean("moreDealsLoaded");
      return;
      localObject1 = (DPObject[])((ArrayList)localObject1).toArray(new DPObject[((ArrayList)localObject1).size()]);
      break;
    }
  }

  public void onRefresh()
  {
    if (this.moreDealsReq != null)
    {
      mapiService().abort(this.moreDealsReq, this, true);
      this.moreDealsReq = null;
    }
    this.moreDealsLoaded = false;
    loadMoreDeals();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.moreDealsReq == paramMApiRequest)
      this.moreDealsReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.moreDealsReq == paramMApiRequest)
    {
      this.moreDealsReq = null;
      this.moreDealsLoaded = true;
      this.maxCount = 6;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (DPObjectUtils.isDPObjectof(paramMApiRequest, "MoreDealsList"))
      {
        this.shopName = paramMApiRequest.getString("BestShopName");
        if (paramMApiRequest.getObject("SameShopDeals") != null)
          this.dpMoreDeals = paramMApiRequest.getObject("SameShopDeals").getArray("List");
        if (paramMApiRequest.getObject("OtherShopDeals") != null)
          this.dpOtherShopDeals = paramMApiRequest.getObject("OtherShopDeals").getArray("List");
      }
      dispatchAgentChanged(false);
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = new Bundle();
    if (this.dpMoreDeals != null)
      localBundle.putParcelableArrayList("MoreDeals", new ArrayList(Arrays.asList(this.dpMoreDeals)));
    if (this.dpOtherShopDeals != null)
      localBundle.putParcelableArrayList("OtherShopDeals", new ArrayList(Arrays.asList(this.dpOtherShopDeals)));
    localBundle.putBoolean("moreDealsLoader", this.moreDealsLoaded);
    return localBundle;
  }

  protected void updateCells()
  {
    removeAllCells();
    this.commCell.hideArrow();
    this.commCell.setTitle(getHeaderTitle());
    if ((this.fragment instanceof GroupAgentFragment))
    {
      addCell("060MoreDeals.01MoreDeals0", this.commCell);
      return;
    }
    addCell("060MoreDeals.01MoreDeals0", this.commCell);
    addEmptyCell("060MoreDeals.01MoreDeals1");
  }

  protected void updateView()
  {
    int i;
    int j;
    label20: Object localObject2;
    Object localObject3;
    if (this.dpMoreDeals == null)
    {
      i = 0;
      if (this.dpOtherShopDeals != null)
        break label363;
      j = 0;
      if ((i > 0) || (j > 0))
      {
        localObject1 = null;
        this.moreDealsLayout.removeAllViews();
        if (i > 0)
        {
          localObject2 = createCellContainer();
          ((TableView)localObject2).setDividerOfGroupEnd(new ColorDrawable(0));
          localObject3 = this.dpMoreDeals;
          if (!android.text.TextUtils.isEmpty(this.shopName))
            break label373;
        }
      }
    }
    label363: label373: for (Object localObject1 = null; ; localObject1 = this.shopName + "可用")
    {
      addDealList((TableView)localObject2, localObject3, 1000, (String)localObject1, "tuan5_detail_otherdeal01", this.maxCount);
      this.moreDealsLayout.addView((View)localObject2);
      localObject1 = localObject2;
      localObject2 = localObject1;
      if (this.maxCount - i > 0)
      {
        localObject2 = localObject1;
        if (j > 0)
        {
          localObject1 = new View(getContext());
          ((View)localObject1).setBackgroundResource(R.drawable.gray_horizontal_line);
          localObject3 = new LinearLayout.LayoutParams(-1, -2);
          ((LinearLayout.LayoutParams)localObject3).leftMargin = ViewUtils.dip2px(getContext(), 10.0F);
          localObject2 = createCellContainer();
          ((TableView)localObject2).setDividerOfGroupEnd(new ColorDrawable(0));
          addDealList((TableView)localObject2, this.dpOtherShopDeals, 1001, "其他分店可用", "tuan5_detail_otherdeal02", this.maxCount - i);
          if (i > 0)
            this.moreDealsLayout.addView((View)localObject1, (ViewGroup.LayoutParams)localObject3);
          this.moreDealsLayout.addView((View)localObject2);
        }
      }
      if ((i + j > this.maxCount) && (localObject2 != null))
      {
        localObject1 = this.res.inflate(getContext(), R.layout.table_more_item, this.moreDealsLayout, false);
        localObject3 = (TextView)((View)localObject1).findViewById(R.id.more);
        ((TextView)localObject3).setTextColor(this.res.getColor(R.color.text_color_link));
        ((TextView)localObject3).setText("更多" + (i + j - this.maxCount) + "个团购");
        ((TableView)localObject2).addView((View)localObject1);
        ((TextView)localObject3).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            DealInfoMoreDealsAgent.this.maxCount = 2147483647;
            DealInfoMoreDealsAgent.this.dispatchAgentChanged(false);
          }
        });
      }
      updateCells();
      return;
      i = this.dpMoreDeals.length;
      break;
      j = this.dpOtherShopDeals.length;
      break label20;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoMoreDealsAgent
 * JD-Core Version:    0.6.0
 */
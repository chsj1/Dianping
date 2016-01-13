package com.dianping.movie.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.ExpandAnimation;
import com.dianping.base.widget.ExpandAnimation.OnExpendActionListener;
import com.dianping.base.widget.ExpandAnimation.OnExpendAnimationListener;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class CinemaDealView extends NovaLinearLayout
  implements View.OnClickListener, ExpandAnimation.OnExpendActionListener, ExpandAnimation.OnExpendAnimationListener
{
  private static final int EXPAND_STATUS_ANIMATE = 1;
  private static final int EXPAND_STATUS_NORMAL = 0;
  private final int DEFAULT_MAX_SHOW_NUMBER = 4;
  private Context context;
  private DPObject[] dpDeals;
  private String eventCategory;
  private int expandStatus = 0;
  private NovaLinearLayout expandView;
  private ImageView icon;
  private boolean isExpand = false;
  private LinearLayout layerDeallist;
  private LinearLayout layerExpand;
  private String moreHint;
  private DPObject[] movieDeals;
  private SparseArray<CinemaDealItem> tuanCells = new SparseArray();
  private TextView tvTitle;

  public CinemaDealView(Context paramContext)
  {
    this(paramContext, null);
  }

  public CinemaDealView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
  }

  private View createDealItem(DPObject paramDPObject, boolean paramBoolean, int paramInt, String paramString)
  {
    CinemaDealItem localCinemaDealItem = (CinemaDealItem)LayoutInflater.from(getContext()).inflate(R.layout.cinema_deal_item, this.layerDeallist, false);
    localCinemaDealItem.setDealItem(paramDPObject, paramBoolean);
    localCinemaDealItem.setTag(paramDPObject);
    localCinemaDealItem.setOnClickListener(this);
    this.tuanCells.append(paramDPObject.getInt("ID"), localCinemaDealItem);
    localCinemaDealItem.setGAString(paramString, "", paramInt);
    return localCinemaDealItem;
  }

  private void setBackground(View paramView, int paramInt)
  {
    int i = paramView.getPaddingBottom();
    int j = paramView.getPaddingTop();
    int k = paramView.getPaddingRight();
    int m = paramView.getPaddingLeft();
    paramView.setBackgroundResource(paramInt);
    paramView.setPadding(m, j, k, i);
  }

  private void setExpandAction()
  {
    if ((this.layerExpand == null) || (this.expandStatus == 1))
      return;
    ExpandAnimation localExpandAnimation = new ExpandAnimation(this.layerExpand, 500);
    localExpandAnimation.setOnAnimationListener(this);
    localExpandAnimation.setOnExpendActionListener(this);
    this.layerExpand.startAnimation(localExpandAnimation);
  }

  private void setExpandState()
  {
    if (this.expandView == null)
      return;
    if (this.isExpand)
    {
      ((ImageView)this.expandView.findViewById(R.id.expand_arrow)).setImageResource(R.drawable.arrow_up_shop);
      ((TextView)this.expandView.findViewById(R.id.expand_hint)).setText("收起");
      return;
    }
    ((ImageView)this.expandView.findViewById(R.id.expand_arrow)).setImageResource(R.drawable.arrow_down_shop);
    ((TextView)this.expandView.findViewById(R.id.expand_hint)).setText(this.moreHint);
  }

  public void onAnimationEnd()
  {
    super.onAnimationEnd();
    this.expandStatus = 0;
    setExpandState();
  }

  public void onAnimationStart()
  {
    super.onAnimationStart();
    this.expandStatus = 1;
  }

  public void onClick(View paramView)
  {
    boolean bool;
    if (paramView.getTag() == "EXPAND")
    {
      if (!this.isExpand)
        ((DPActivity)this.context).statisticsEvent("movie5", "movie5_cinemainfo_alldeals", "", 0);
      if (!this.isExpand)
      {
        bool = true;
        this.isExpand = bool;
        setExpandAction();
      }
    }
    do
    {
      return;
      bool = false;
      break;
      if (!DPObjectUtils.isDPObjectof(paramView.getTag(), "Deal"))
        continue;
      paramView = (DPObject)paramView.getTag();
      localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
      localIntent.putExtra("deal", paramView);
      getContext().startActivity(localIntent);
      ((DPActivity)this.context).statisticsEvent("movie5", "movie5_cinemainfo_dealinfo", "" + paramView.getInt("ID"), 0);
      return;
    }
    while (!DPObjectUtils.isDPObjectof(paramView.getTag(), "MovieDeal"));
    paramView = (DPObject)paramView.getTag();
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramView.getString("BuyLink")));
    getContext().startActivity(localIntent);
    ((DPActivity)this.context).statisticsEvent("movie5", this.eventCategory, "" + paramView.getInt("ID"), 0);
  }

  public void onExpendAction(View paramView)
  {
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.icon = ((ImageView)findViewById(R.id.icon));
    this.tvTitle = ((TextView)findViewById(R.id.title));
  }

  public void setCinemaDeal(DPObject[] paramArrayOfDPObject)
  {
    this.tuanCells.clear();
    this.dpDeals = paramArrayOfDPObject;
    if ((this.dpDeals == null) || (this.dpDeals.length == 0))
      return;
    this.layerDeallist = new LinearLayout(getContext());
    this.layerDeallist.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.layerDeallist.setOrientation(1);
    int i = 0;
    Object localObject;
    boolean bool;
    if ((i < this.dpDeals.length) && (i < 4))
    {
      paramArrayOfDPObject = this.layerDeallist;
      localObject = this.dpDeals[i];
      if (i == 3);
      for (bool = true; ; bool = false)
      {
        paramArrayOfDPObject.addView(createDealItem((DPObject)localObject, bool, i, "dealinfo"));
        i += 1;
        break;
      }
    }
    if (this.dpDeals.length > 4)
    {
      this.layerExpand = new LinearLayout(getContext());
      this.layerExpand.setOrientation(1);
      paramArrayOfDPObject = new LinearLayout.LayoutParams(-1, -2);
      if (!this.isExpand)
      {
        paramArrayOfDPObject.bottomMargin = (-(ViewUtils.dip2px(getContext(), 69.0F) * (this.dpDeals.length - 4)));
        this.layerExpand.setVisibility(8);
      }
      this.layerExpand.setLayoutParams(paramArrayOfDPObject);
      paramArrayOfDPObject = new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 1.0F));
      localObject = new View(getContext());
      ((View)localObject).setLayoutParams(paramArrayOfDPObject);
      ((View)localObject).setBackgroundResource(R.drawable.list_divider_right_inset);
      this.layerExpand.addView((View)localObject);
      i = 4;
      if (i < this.dpDeals.length)
      {
        paramArrayOfDPObject = this.layerExpand;
        localObject = this.dpDeals[i];
        if (i == this.dpDeals.length - 1);
        for (bool = true; ; bool = false)
        {
          paramArrayOfDPObject.addView(createDealItem((DPObject)localObject, bool, i, "dealinfo"));
          i += 1;
          break;
        }
      }
      this.layerDeallist.addView(this.layerExpand);
      this.expandView = ((NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.movie_info_expand_view, this.layerDeallist, false));
      this.expandView.setTag("EXPAND");
      this.moreHint = ("查看全部" + this.dpDeals.length + "条团购");
      ((TextView)this.expandView.findViewById(R.id.expand_hint)).setText(this.moreHint);
      setBackground(this.expandView, R.drawable.cell_item_white);
      this.expandView.setClickable(true);
      this.expandView.setOnClickListener(this);
      this.expandView.setGAString("alldeals");
      this.layerDeallist.addView(this.expandView);
      setExpandState();
    }
    addView(this.layerDeallist);
  }

  public void setDeals(DPObject paramDPObject, String paramString, int paramInt)
  {
    this.tuanCells.clear();
    this.eventCategory = paramString;
    if (paramInt == 0)
      this.icon.setVisibility(8);
    while (true)
    {
      this.tvTitle.setText(paramDPObject.getString("Title"));
      this.movieDeals = paramDPObject.getArray("List");
      if ((this.movieDeals != null) && (this.movieDeals.length != 0))
        break;
      return;
      this.icon.setImageResource(paramInt);
      this.icon.setVisibility(0);
    }
    this.layerDeallist = new LinearLayout(getContext());
    this.layerDeallist.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.layerDeallist.setOrientation(1);
    paramInt = 0;
    if (paramInt < this.movieDeals.length)
    {
      paramDPObject = this.layerDeallist;
      paramString = this.movieDeals[paramInt];
      if (paramInt == this.movieDeals.length - 1);
      for (boolean bool = true; ; bool = false)
      {
        paramDPObject.addView(createDealItem(paramString, bool, paramInt, "success_snackdeal"));
        paramInt += 1;
        break;
      }
    }
    addView(this.layerDeallist);
  }

  public void updatePromoTag(DPObject[] paramArrayOfDPObject)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0))
      return;
    SparseArray localSparseArray = new SparseArray();
    int i = 0;
    while (i < paramArrayOfDPObject.length)
    {
      localSparseArray.append(paramArrayOfDPObject[i].getInt("DealId"), paramArrayOfDPObject[i].getString("DiscountDesc"));
      i += 1;
    }
    i = 0;
    label57: int j;
    if (i < this.tuanCells.size())
    {
      j = this.tuanCells.keyAt(i);
      if (localSparseArray.get(j) != null)
        break label94;
    }
    while (true)
    {
      i += 1;
      break label57;
      break;
      label94: ((CinemaDealItem)this.tuanCells.valueAt(i)).setPromoLable((String)localSparseArray.get(j));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.CinemaDealView
 * JD-Core Version:    0.6.0
 */
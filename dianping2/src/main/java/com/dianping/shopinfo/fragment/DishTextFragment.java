package com.dianping.shopinfo.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class DishTextFragment extends NovaFragment
  implements AdapterView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final DecimalFormat PRICE_DF = new DecimalFormat("￥#.###");
  MyAdapter adapter;
  protected String defEmptyMsg;
  MApiRequest dishListRequest;
  protected TextView emptyTV;
  protected FrameLayout emptyView;
  ListView listView;
  private DPObject objShop;
  View rootView;
  MApiRequest shopRequest;

  private void dishListTask(int paramInt1, int paramInt2)
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/shopdish.bin?");
    localStringBuffer.append("shopid=").append(paramInt2);
    localStringBuffer.append("&cityid=").append(cityId());
    localStringBuffer.append("&start=").append(paramInt1);
    localStringBuffer.append("&").append(getScreenInfo());
    this.dishListRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
    localMApiService.exec(this.dishListRequest, this);
  }

  private String getScreenInfo()
  {
    DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
    return "screenwidth=" + localDisplayMetrics.widthPixels + "&screenheight=" + localDisplayMetrics.heightPixels + "&screendensity=" + localDisplayMetrics.density;
  }

  private void initGridView()
  {
    this.adapter = new MyAdapter();
    this.listView = ((ListView)this.rootView.findViewById(R.id.list));
    this.listView.setAdapter(this.adapter);
    this.listView.setOnItemClickListener(this);
    setEmptyView();
  }

  private void showCouponInformation(DPObject paramDPObject)
  {
    if (!isAdded());
    while (true)
    {
      return;
      int i = paramDPObject.getInt("DealID");
      if (i <= 0)
        break;
      Object localObject = "人均" + this.objShop.getInt("AvgPrice") + "元，推荐使用代金券";
      double d1 = paramDPObject.getDouble("CouponOriPrice");
      double d2 = paramDPObject.getDouble("CouponCurPrice");
      paramDPObject = this.rootView.findViewById(R.id.recommend_coupon);
      if (paramDPObject == null)
        continue;
      if (localObject != null)
        ((TextView)paramDPObject.findViewById(R.id.coupon)).setText((CharSequence)localObject);
      localObject = new SpannableStringBuilder(PRICE_DF.format(d2) + " ");
      SpannableString localSpannableString = new SpannableString(PRICE_DF.format(d1));
      localSpannableString.setSpan(new StrikethroughSpan(), 1, localSpannableString.length(), 33);
      localSpannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_hint)), 0, localSpannableString.length(), 33);
      localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_gray)), 0, localSpannableString.length(), 33);
      ((SpannableStringBuilder)localObject).append(localSpannableString);
      ((TextView)paramDPObject.findViewById(R.id.coupon_price)).setText((CharSequence)localObject);
      paramDPObject.setOnClickListener(new View.OnClickListener(i)
      {
        public void onClick(View paramView)
        {
          DishTextFragment.this.statisticsEvent("shopinfo5", "shopinfo5_dish_voucher", "代金券的点击次数", this.val$dealID);
          paramView = "dianping://tuandeal?id=" + this.val$dealID;
          DishTextFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView)));
        }
      });
      statisticsEvent("shopinfo5", "shopinfo5_dish_vouchershow", "代金券的展示次数", i);
      paramDPObject.setVisibility(0);
      return;
    }
    this.rootView.findViewById(R.id.recommend_coupon).setVisibility(8);
  }

  public View getView()
  {
    return this.rootView;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    this.rootView = paramLayoutInflater.inflate(R.layout.dish_list, paramViewGroup, false);
    paramLayoutInflater = getActivity().getIntent();
    int i;
    if (paramBundle == null)
    {
      this.objShop = ((DPObject)paramLayoutInflater.getParcelableExtra("objShop"));
      if ((this.objShop == null) && (paramLayoutInflater.getData() != null))
      {
        i = paramLayoutInflater.getIntExtra("shopId", 0);
        if (i <= 0)
          break label131;
        sendShopRequest(i);
      }
    }
    while (true)
    {
      initGridView();
      if (paramBundle != null)
      {
        this.adapter.onRestoreInstanceState(paramBundle);
        this.defEmptyMsg = paramBundle.getString("defEmptyMsg");
      }
      return this.rootView;
      this.objShop = ((DPObject)paramBundle.getParcelable("objShop"));
      break;
      label131: paramLayoutInflater = paramLayoutInflater.getData().getQueryParameter("id");
      try
      {
        i = Integer.parseInt(paramLayoutInflater);
        if (i <= 0)
          break label173;
        sendShopRequest(i);
      }
      catch (java.lang.NumberFormatException paramLayoutInflater)
      {
        getActivity().finish();
      }
      continue;
      label173: getActivity().finish();
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.adapter.dishs;
    if ((paramAdapterView != null) && (paramAdapterView.size() > paramInt))
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://dishalbumdetail"));
      paramView.putExtra("objShop", this.objShop);
      paramView.putExtra("dishId", ((DPObject)paramAdapterView.get(paramInt)).getInt("ID"));
      paramView.putExtra("dishName", ((DPObject)paramAdapterView.get(paramInt)).getString("Name"));
      startActivity(paramView);
      statisticsEvent("shopinfo5", "shopinfo5_dish_item", "", 0);
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.dishListRequest)
    {
      this.adapter.setError(paramMApiResponse.message().content());
      this.rootView.findViewById(R.id.recommend_coupon).setVisibility(8);
      this.dishListRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.dishListRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
      {
        this.adapter.appendCoupons(paramMApiRequest);
        showCouponInformation(paramMApiRequest);
      }
      this.dishListRequest = null;
    }
    do
      return;
    while (paramMApiRequest != this.shopRequest);
    try
    {
      this.objShop = ((DPObject)paramMApiResponse.result());
      if (this.objShop == null)
      {
        Toast.makeText(getActivity(), "暂时无法获取商户图片数据", 1).show();
        getActivity().finish();
        return;
      }
    }
    catch (Exception paramMApiRequest)
    {
      while (true)
        paramMApiRequest.printStackTrace();
      initGridView();
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    this.adapter.onSaveInstanceState(paramBundle);
    paramBundle.putString("defEmptyMsg", this.defEmptyMsg);
    paramBundle.putParcelable("objShop", this.objShop);
    super.onSaveInstanceState(paramBundle);
  }

  public void sendShopRequest(int paramInt)
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/shop.bin?");
    localStringBuffer.append("shopid=").append(paramInt);
    this.shopRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
    localMApiService.exec(this.shopRequest, this);
  }

  protected void setEmptyMsg(String paramString)
  {
    if (getActivity() == null)
      return;
    if (this.emptyTV == null)
    {
      this.emptyTV = ((TextView)getActivity().getLayoutInflater().inflate(R.layout.simple_list_item_18, this.emptyView, false));
      this.emptyTV.setMovementMethod(LinkMovementMethod.getInstance());
    }
    Drawable localDrawable = getResources().getDrawable(R.drawable.empty_page_nothing);
    localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
    this.emptyTV.setCompoundDrawablePadding(8);
    this.emptyTV.setCompoundDrawables(localDrawable, null, null, null);
    if (!TextUtils.isEmpty(paramString));
    for (this.defEmptyMsg = paramString; ; this.defEmptyMsg = "暂时没有推荐菜哦")
    {
      if (!TextUtils.isEmpty(this.defEmptyMsg))
        this.emptyTV.setText(Html.fromHtml(this.defEmptyMsg));
      if (this.emptyView.getChildAt(0) == this.emptyTV)
        break;
      this.emptyView.removeAllViews();
      this.emptyView.addView(this.emptyTV);
      return;
    }
  }

  protected void setEmptyView()
  {
    this.listView = ((ListView)this.rootView.findViewById(R.id.list));
    this.emptyView = ((FrameLayout)this.rootView.findViewById(R.id.empty));
    this.listView.setEmptyView(this.emptyView);
  }

  class MyAdapter extends BasicAdapter
  {
    ArrayList<DPObject> dishs = new ArrayList();
    String errorMsg;
    boolean isEnd;
    int nextStartIndex;
    int recordCount;

    MyAdapter()
    {
    }

    public void appendCoupons(DPObject paramDPObject)
    {
      if (paramDPObject.getInt("StartIndex") == this.nextStartIndex)
      {
        DishTextFragment.this.setEmptyMsg(paramDPObject.getString("EmptyMsg"));
        this.dishs.addAll(Arrays.asList(paramDPObject.getArray("List")));
        this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
        this.isEnd = paramDPObject.getBoolean("IsEnd");
        this.recordCount = paramDPObject.getInt("RecordCount");
        notifyDataSetChanged();
      }
    }

    public int getCount()
    {
      if (this.isEnd)
        return this.dishs.size();
      return this.dishs.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.dishs.size())
        return this.dishs.get(paramInt);
      if (this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
        return ((DPObject)localObject).getInt("ID");
      if (localObject == LOADING)
        return -paramInt;
      return 2147483647L;
    }

    public int getItemViewType(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
        return 0;
      if (localObject == LOADING)
        return 1;
      return 2;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        DPObject localDPObject;
        NetworkImageView localNetworkImageView;
        if ((paramView != null) && (paramView.getTag() == this))
        {
          paramViewGroup = (DPObject)localObject;
          localObject = (TextView)paramView.findViewById(R.id.dish_name);
          ((TextView)localObject).setText(paramViewGroup.getString("Name"));
          ((TextView)localObject).setTextColor(-16777216);
          ((TextView)paramView.findViewById(R.id.recommend_num)).setText(paramViewGroup.getInt("Count") + "人推荐");
          localObject = (TextView)paramView.findViewById(R.id.price_text);
          localDPObject = paramViewGroup.getObject("DefaultPic");
          localNetworkImageView = (NetworkImageView)paramView.findViewById(R.id.icon);
          if (localDPObject != null)
            break label217;
        }
        label217: for (paramViewGroup = ""; ; paramViewGroup = localDPObject.getString("ThumbUrl"))
        {
          localNetworkImageView.setImage(paramViewGroup);
          if (localDPObject == null)
            break label279;
          if (TextUtils.isEmpty(localDPObject.getString("PriceText")))
            break label228;
          ((TextView)localObject).setText(localDPObject.getString("PriceText"));
          return paramView;
          paramView = DishTextFragment.this.getActivity().getLayoutInflater().inflate(R.layout.simple_list_item_3, paramViewGroup, false);
          paramView.setPadding(10, 20, 10, 20);
          paramView.setTag(this);
          break;
        }
        label228: if (localDPObject.getInt("Price") > 0)
        {
          ((TextView)localObject).setText("￥" + localDPObject.getInt("Price"));
          return paramView;
        }
        ((TextView)localObject).setText("");
        return paramView;
        label279: ((TextView)localObject).setText("");
        return paramView;
      }
      if (localObject == LOADING)
      {
        if (this.errorMsg == null)
          loadNewPage();
        return getLoadingView(paramViewGroup, paramView);
      }
      return (View)getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          DishTextFragment.MyAdapter.this.loadNewPage();
        }
      }
      , paramViewGroup, paramView);
    }

    public int getViewTypeCount()
    {
      return 3;
    }

    public boolean loadNewPage()
    {
      if (this.isEnd);
      do
        return false;
      while ((DishTextFragment.this.dishListRequest != null) || (DishTextFragment.this.objShop == null));
      this.errorMsg = null;
      DishTextFragment.this.dishListTask(this.nextStartIndex, DishTextFragment.this.objShop.getInt("ID"));
      notifyDataSetChanged();
      return true;
    }

    public void onRestoreInstanceState(Bundle paramBundle)
    {
      this.dishs = paramBundle.getParcelableArrayList("dishs");
      this.nextStartIndex = paramBundle.getInt("nextStartIndex");
      this.isEnd = paramBundle.getBoolean("isEnd");
      this.recordCount = paramBundle.getInt("recordCount");
      this.errorMsg = paramBundle.getString("error");
      notifyDataSetChanged();
    }

    public void onSaveInstanceState(Bundle paramBundle)
    {
      paramBundle.putParcelableArrayList("dishs", this.dishs);
      paramBundle.putInt("nextStartIndex", this.nextStartIndex);
      paramBundle.putBoolean("isEnd", this.isEnd);
      paramBundle.putInt("recordCount", this.recordCount);
      paramBundle.putString("error", this.errorMsg);
    }

    public void setError(String paramString)
    {
      this.errorMsg = paramString;
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.fragment.DishTextFragment
 * JD-Core Version:    0.6.0
 */
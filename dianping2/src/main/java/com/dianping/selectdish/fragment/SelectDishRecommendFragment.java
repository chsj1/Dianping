package com.dianping.selectdish.fragment;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.NewCartManager.CartChangedListener;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaFrameLayout;
import java.math.BigDecimal;
import java.util.ArrayList;

public class SelectDishRecommendFragment extends NovaFragment
  implements NewCartManager.CartChangedListener
{
  private ImageView allChoose;
  private int changeDishId;
  private DPObject data;
  private String desc;
  private ArrayList<DishInfo> dishes = new ArrayList();
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private ListView listView;
  private NewCartManager mCartManager = NewCartManager.getInstance();
  private ProgressDialog mChangeDialog;
  private MApiRequest mRequestChangeRecommend;
  private FullRequestHandle<MApiRequest, MApiResponse> mapiHandler = new FullRequestHandle()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if ((SelectDishRecommendFragment.this.mChangeDialog != null) && (SelectDishRecommendFragment.this.mChangeDialog.isShowing()))
        SelectDishRecommendFragment.this.mChangeDialog.dismiss();
      if (paramMApiResponse.message() != null);
      for (paramMApiRequest = paramMApiResponse.message(); ; paramMApiRequest = new SimpleMsg("错误", "网络错误,请重试", 0, 0))
      {
        Toast.makeText(SelectDishRecommendFragment.this.getContext(), paramMApiRequest.content(), 0).show();
        return;
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if ((SelectDishRecommendFragment.this.mChangeDialog != null) && (SelectDishRecommendFragment.this.mChangeDialog.isShowing()))
        SelectDishRecommendFragment.this.mChangeDialog.dismiss();
      paramMApiRequest = paramMApiResponse.result();
      if ((paramMApiRequest instanceof DPObject))
      {
        paramMApiRequest = new DishInfo((DPObject)paramMApiRequest);
        SelectDishRecommendFragment.this.updateSingleRow(SelectDishRecommendFragment.this.changeDishId, paramMApiRequest);
        SelectDishRecommendFragment.this.initAllChoose();
        SelectDishRecommendFragment.this.updateTotalPrice();
      }
    }

    public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
    {
    }

    public void onRequestStart(MApiRequest paramMApiRequest)
    {
      if ((SelectDishRecommendFragment.this.mChangeDialog != null) && (SelectDishRecommendFragment.this.mChangeDialog.isShowing()))
        SelectDishRecommendFragment.this.mChangeDialog.dismiss();
      SelectDishRecommendFragment.access$002(SelectDishRecommendFragment.this, new ProgressDialog(SelectDishRecommendFragment.this.getContext()));
      SelectDishRecommendFragment.this.mChangeDialog.setMessage("");
      SelectDishRecommendFragment.this.mChangeDialog.setCancelable(false);
      SelectDishRecommendFragment.this.mChangeDialog.show();
    }
  };
  private DPObject[] menus;
  private BigDecimal originPrice = new BigDecimal(0);
  private BigDecimal price = new BigDecimal(0);
  private RecommendAdapter recommendAdapter;
  private TextView recommendDesc;
  private int shopId;
  private RMBLabelItem totalPrice;
  private int type;

  private void initAllChoose()
  {
    boolean bool2 = true;
    int j = this.dishes.size();
    int i = 0;
    while (true)
    {
      boolean bool1 = bool2;
      if (i < j)
      {
        if (this.mCartManager.getDishCountByDishId(((DishInfo)this.dishes.get(i)).dishId) == 0)
          bool1 = false;
      }
      else
      {
        this.allChoose.setSelected(bool1);
        return;
      }
      i += 1;
    }
  }

  private void initView(View paramView)
  {
    this.allChoose = ((ImageView)paramView.findViewById(R.id.selectdish_all_choose));
    this.recommendDesc = ((TextView)paramView.findViewById(R.id.recommend_desc));
    this.recommendDesc.setText(this.desc);
    this.totalPrice = ((RMBLabelItem)paramView.findViewById(R.id.price));
    updateTotalPrice();
    this.listView = ((ListView)paramView.findViewById(R.id.recommend_list));
    this.recommendAdapter = new RecommendAdapter();
    this.listView.setAdapter(this.recommendAdapter);
    initAllChoose();
    int i = this.dishes.size();
    this.allChoose.setOnClickListener(new View.OnClickListener(i)
    {
      public void onClick(View paramView)
      {
        GAHelper.instance().contextStatisticsEvent(SelectDishRecommendFragment.this.getActivity(), "selectall", SelectDishRecommendFragment.this.gaUserInfo, "tap");
        if (SelectDishRecommendFragment.this.allChoose.isSelected())
        {
          SelectDishRecommendFragment.this.allChoose.setSelected(false);
          i = 0;
          while (i < this.val$dishSize)
          {
            SelectDishRecommendFragment.this.mCartManager.reduceDish((DishInfo)SelectDishRecommendFragment.this.dishes.get(i));
            i += 1;
          }
        }
        SelectDishRecommendFragment.this.allChoose.setSelected(true);
        int i = 0;
        while (i < this.val$dishSize)
        {
          if (SelectDishRecommendFragment.this.mCartManager.getDishCountByDishId(((DishInfo)SelectDishRecommendFragment.this.dishes.get(i)).dishId) == 0)
            SelectDishRecommendFragment.this.mCartManager.addDish((DishInfo)SelectDishRecommendFragment.this.dishes.get(i));
          i += 1;
        }
      }
    });
  }

  public static SelectDishRecommendFragment newInstance(DPObject paramDPObject, int paramInt)
  {
    SelectDishRecommendFragment localSelectDishRecommendFragment = new SelectDishRecommendFragment();
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("data", paramDPObject);
    localBundle.putInt("shopid", paramInt);
    localSelectDishRecommendFragment.setArguments(localBundle);
    return localSelectDishRecommendFragment;
  }

  public void onCountChanged()
  {
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null)
      this.data = ((DPObject)getArguments().getParcelable("data"));
    for (this.shopId = getArguments().getInt("shopid"); ; this.shopId = paramBundle.getInt("shopid"))
    {
      this.type = this.data.getInt("Type");
      this.menus = this.data.getArray("Menus");
      this.desc = this.data.getString("Desc");
      int i = 0;
      while (i < this.menus.length)
      {
        paramBundle = new DishInfo(this.menus[i]);
        this.dishes.add(paramBundle);
        i += 1;
      }
      this.data = ((DPObject)paramBundle.getParcelable("data"));
    }
    this.mCartManager.addCartChangedListener(this);
  }

  @Nullable
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.selectdish_recommend_fragment, null);
    initView(paramLayoutInflater);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mRequestChangeRecommend != null)
      mapiService().abort(this.mRequestChangeRecommend, this.mapiHandler, true);
    this.mCartManager.removeCartChangedListener(this);
  }

  public void onDishChanged(CartItem paramCartItem)
  {
    if (paramCartItem == null)
      this.recommendAdapter.notifyDataSetChanged();
    while (true)
    {
      initAllChoose();
      return;
      updateSingleRow(paramCartItem.dishInfo.dishId, paramCartItem.dishInfo);
    }
  }

  public void onFreeDishChanged(ArrayList<CartFreeItem> paramArrayList)
  {
  }

  public void onGroupOnOrSetChanged()
  {
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("data", this.data);
    paramBundle.putInt("shopid", this.shopId);
  }

  public void reqChangeRecommend(int paramInt1, int paramInt2)
  {
    if (this.mRequestChangeRecommend != null)
      mapiService().abort(this.mRequestChangeRecommend, this.mapiHandler, true);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/changerecommenddish.hbt").buildUpon();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("shopid");
    localArrayList.add(String.valueOf(this.shopId));
    localArrayList.add("type");
    localArrayList.add(String.valueOf(paramInt2));
    localArrayList.add("dishid");
    localArrayList.add(String.valueOf(paramInt1));
    localArrayList.add("currentdishids");
    StringBuilder localStringBuilder = new StringBuilder();
    paramInt2 = 0;
    while (paramInt2 < this.dishes.size())
    {
      localStringBuilder.append(((DishInfo)this.dishes.get(paramInt2)).dishId).append(",");
      paramInt2 += 1;
    }
    if (localStringBuilder.length() > 0)
      localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    localArrayList.add(localStringBuilder.toString());
    this.changeDishId = paramInt1;
    this.mRequestChangeRecommend = BasicMApiRequest.mapiPost(localBuilder.toString(), (String[])localArrayList.toArray(new String[localArrayList.size()]));
    mapiService().exec(this.mRequestChangeRecommend, this.mapiHandler);
  }

  public void updateSingleRow(int paramInt, DishInfo paramDishInfo)
  {
    if (this.listView != null)
    {
      int j = this.listView.getFirstVisiblePosition();
      int k = this.listView.getLastVisiblePosition();
      int i = j;
      while (i <= k)
      {
        if (((this.listView.getItemAtPosition(i) instanceof DishInfo)) && (paramInt == ((DishInfo)this.listView.getItemAtPosition(i)).dishId))
        {
          View localView = this.listView.getChildAt(i - j);
          this.dishes.set(i, paramDishInfo);
          this.recommendAdapter.getView(i, localView, this.listView);
        }
        i += 1;
      }
    }
  }

  public void updateTotalPrice()
  {
    int j = this.dishes.size();
    this.price = new BigDecimal(0);
    this.originPrice = new BigDecimal(0);
    int i = 0;
    while (i < j)
    {
      this.price = this.price.add(new BigDecimal(((DishInfo)this.dishes.get(i)).currentPrice));
      this.originPrice = this.originPrice.add(new BigDecimal(((DishInfo)this.dishes.get(i)).oldPrice));
      i += 1;
    }
    this.price = this.price.setScale(2, 4);
    this.originPrice = this.originPrice.setScale(2, 4);
    if (this.price.doubleValue() >= this.originPrice.doubleValue())
    {
      this.totalPrice.setRMBLabelStyle(2, 2, false, getResources().getColor(R.color.light_red));
      this.totalPrice.setRMBLabelValue(this.price.doubleValue());
      return;
    }
    this.totalPrice.setRMBLabelStyle(2, 3, false, getResources().getColor(R.color.light_red));
    this.totalPrice.setRMBLabelValue(this.price.doubleValue(), this.originPrice.doubleValue());
  }

  public class RecommendAdapter extends BaseAdapter
  {
    public RecommendAdapter()
    {
    }

    public int getCount()
    {
      return SelectDishRecommendFragment.this.dishes.size();
    }

    public DishInfo getItem(int paramInt)
    {
      return (DishInfo)SelectDishRecommendFragment.this.dishes.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DishInfo localDishInfo;
      if (paramView == null)
      {
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.selectdish_recommend_item, paramViewGroup, false);
        paramViewGroup = new SelectDishRecommendFragment.ViewHolder(null);
        paramViewGroup.photo = ((NetworkImageView)paramView.findViewById(R.id.dishphoto));
        paramViewGroup.dishName = ((TextView)paramView.findViewById(R.id.dishname));
        paramViewGroup.addCart = ((NovaFrameLayout)paramView.findViewById(R.id.addcart));
        paramViewGroup.recommendCart = ((ImageView)paramView.findViewById(R.id.recommend_item_cart));
        paramViewGroup.recommendCount = ((TextView)paramView.findViewById(R.id.recommend_item_count));
        paramViewGroup.recommendStatus = ((TextView)paramView.findViewById(R.id.recommend_item_status));
        paramViewGroup.refresh = ((FrameLayout)paramView.findViewById(R.id.refresh));
        paramViewGroup.dishPrice = ((RMBLabelItem)paramView.findViewById(R.id.recommend_price));
        paramView.setTag(paramViewGroup);
        localDishInfo = getItem(paramInt);
        if (localDishInfo != null)
        {
          paramInt = SelectDishRecommendFragment.this.mCartManager.getDishCountByDishId(localDishInfo.dishId);
          paramViewGroup.photo.setImage(localDishInfo.url);
          paramViewGroup.dishName.setText(localDishInfo.name);
          if (!localDishInfo.soldout)
            break label309;
          paramViewGroup.recommendStatus.setVisibility(0);
          paramViewGroup.recommendCart.setVisibility(4);
          paramViewGroup.recommendCount.setVisibility(4);
          if (localDishInfo.currentPrice < localDishInfo.oldPrice)
            break label379;
          paramViewGroup.dishPrice.setRMBLabelStyle(2, 2, false, SelectDishRecommendFragment.this.getResources().getColor(R.color.light_red));
          paramViewGroup.dishPrice.setRMBLabelValue(localDishInfo.currentPrice);
        }
      }
      while (true)
      {
        paramViewGroup.refresh.setOnClickListener(new View.OnClickListener(localDishInfo)
        {
          public void onClick(View paramView)
          {
            GAHelper.instance().contextStatisticsEvent(SelectDishRecommendFragment.this.getActivity(), "replace", SelectDishRecommendFragment.this.gaUserInfo, "tap");
            SelectDishRecommendFragment.this.reqChangeRecommend(this.val$dishInfo.dishId, SelectDishRecommendFragment.this.type);
          }
        });
        return paramView;
        paramViewGroup = (SelectDishRecommendFragment.ViewHolder)paramView.getTag();
        break;
        label309: paramViewGroup.recommendStatus.setVisibility(4);
        paramViewGroup.recommendCart.setVisibility(0);
        if (paramInt <= 0)
          paramViewGroup.recommendCount.setVisibility(4);
        while (true)
        {
          paramViewGroup.recommendCount.setText(String.valueOf(paramInt));
          paramViewGroup.addCart.setOnClickListener(new View.OnClickListener(localDishInfo)
          {
            public void onClick(View paramView)
            {
              GAHelper.instance().contextStatisticsEvent(SelectDishRecommendFragment.this.getActivity(), "addcart", SelectDishRecommendFragment.this.gaUserInfo, "tap");
              SelectDishRecommendFragment.this.mCartManager.addDish(this.val$dishInfo);
            }
          });
          break;
          paramViewGroup.recommendCount.setVisibility(0);
        }
        label379: paramViewGroup.dishPrice.setRMBLabelStyle(2, 3, false, SelectDishRecommendFragment.this.getResources().getColor(R.color.light_red));
        paramViewGroup.dishPrice.setRMBLabelValue(localDishInfo.currentPrice, localDishInfo.oldPrice);
      }
    }
  }

  private static class ViewHolder
  {
    NovaFrameLayout addCart;
    TextView dishName;
    RMBLabelItem dishPrice;
    NetworkImageView photo;
    ImageView recommendCart;
    TextView recommendCount;
    TextView recommendStatus;
    FrameLayout refresh;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.fragment.SelectDishRecommendFragment
 * JD-Core Version:    0.6.0
 */
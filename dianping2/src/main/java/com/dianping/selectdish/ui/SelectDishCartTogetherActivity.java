package com.dianping.selectdish.ui;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.selectdish.TogetherCartManager;
import com.dianping.selectdish.TogetherCartManager.TogetherCartChangedListener;
import com.dianping.selectdish.TogetherSubmitOrderManager;
import com.dianping.selectdish.entity.SelectDishCartAdapter;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.view.SelectDishAlertDialog;
import com.dianping.selectdish.view.SelectDishAlertDialog.OnDialogClickListener;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.Iterator;

public class SelectDishCartTogetherActivity extends NovaActivity
  implements PullToRefreshListView.OnRefreshListener, RequestHandler<MApiRequest, MApiResponse>
{
  private final TogetherCartManager.TogetherCartChangedListener dishCartListener = new TogetherCartManager.TogetherCartChangedListener()
  {
    public void onCountChanged()
    {
      boolean bool = true;
      SelectDishCartTogetherActivity localSelectDishCartTogetherActivity;
      if (SelectDishCartTogetherActivity.this.mCartManager.isOwner == 1)
      {
        SelectDishCartTogetherActivity.this.updateCartInfos(SelectDishCartTogetherActivity.this.mCartManager.getTotalDishCount() + SelectDishCartTogetherActivity.this.mCartManager.getTotalSelectFreeDishCount(), SelectDishCartTogetherActivity.this.mCartManager.getTotalDishCount() + SelectDishCartTogetherActivity.this.mCartManager.getTotalFreeDishCount(), SelectDishCartTogetherActivity.this.mCartManager.getTotalSingleDishCount() + SelectDishCartTogetherActivity.this.mCartManager.getTotalSelectFreeDishCount());
        localSelectDishCartTogetherActivity = SelectDishCartTogetherActivity.this;
        if (SelectDishCartTogetherActivity.this.mCartManager.getTotalDishCount() <= 0)
          break label172;
      }
      while (true)
      {
        localSelectDishCartTogetherActivity.setCartClearButtonEnabled(bool);
        return;
        SelectDishCartTogetherActivity.this.updateCartInfos(SelectDishCartTogetherActivity.this.mCartManager.getTotalDishCount() + SelectDishCartTogetherActivity.this.mCartManager.getOtherTotalCount(), SelectDishCartTogetherActivity.this.mCartManager.getTotalDishCount() + SelectDishCartTogetherActivity.this.mCartManager.getOtherTotalCount(), SelectDishCartTogetherActivity.this.mCartManager.getTotalSingleDishCount());
        break;
        label172: bool = false;
      }
    }

    public void onDishChanged(CartItem paramCartItem)
    {
      SelectDishCartTogetherActivity.this.mCartAdapter.notifyDataSetChanged();
    }

    public void onFreeDishChanged(ArrayList<CartFreeItem> paramArrayList)
    {
      SelectDishCartTogetherActivity.this.mCartAdapter.notifyDataSetChanged();
    }

    public void onGroupOnOrSetChanged()
    {
    }

    public void onManulRefreshComplete()
    {
      SelectDishCartTogetherActivity.this.mListView.onRefreshComplete();
    }

    public void onMistakeRecieved(int paramInt, String paramString)
    {
      if ((SelectDishCartTogetherActivity.this.isResumed) && (!TextUtils.isEmpty(paramString)))
        Toast.makeText(SelectDishCartTogetherActivity.this, paramString, 0).show();
      if (paramInt == 100)
        SelectDishCartTogetherActivity.this.finish();
    }

    public void onSyncComplete()
    {
      boolean bool = true;
      SelectDishCartTogetherActivity.this.mListView.onRefreshComplete();
      SelectDishCartTogetherActivity.this.mCartAdapter.notifyDataSetChanged();
      SelectDishCartTogetherActivity localSelectDishCartTogetherActivity;
      if (SelectDishCartTogetherActivity.this.mCartManager.isOwner == 1)
      {
        SelectDishCartTogetherActivity.this.updateCartInfos(SelectDishCartTogetherActivity.this.mCartManager.getTotalDishCount() + SelectDishCartTogetherActivity.this.mCartManager.getTotalSelectFreeDishCount(), SelectDishCartTogetherActivity.this.mCartManager.getTotalDishCount() + SelectDishCartTogetherActivity.this.mCartManager.getTotalFreeDishCount(), SelectDishCartTogetherActivity.this.mCartManager.getTotalSingleDishCount() + SelectDishCartTogetherActivity.this.mCartManager.getTotalSelectFreeDishCount());
        localSelectDishCartTogetherActivity = SelectDishCartTogetherActivity.this;
        if (SelectDishCartTogetherActivity.this.mCartManager.getTotalDishCount() <= 0)
          break label192;
      }
      while (true)
      {
        localSelectDishCartTogetherActivity.setCartClearButtonEnabled(bool);
        return;
        SelectDishCartTogetherActivity.this.updateCartInfos(SelectDishCartTogetherActivity.this.mCartManager.getTotalDishCount() + SelectDishCartTogetherActivity.this.mCartManager.getOtherTotalCount(), SelectDishCartTogetherActivity.this.mCartManager.getTotalDishCount() + SelectDishCartTogetherActivity.this.mCartManager.getOtherTotalCount(), SelectDishCartTogetherActivity.this.mCartManager.getTotalSingleDishCount());
        break;
        label192: bool = false;
      }
    }
  };
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private boolean isEstimate;
  private double mAverage;
  private View mBottomLayout;
  private SelectDishCartAdapter mCartAdapter;
  private TogetherCartManager mCartManager = TogetherCartManager.getInstance();
  private PullToRefreshListView mListView;
  private int mShopId;
  private NovaButton mSubmit;
  private TextView mSummary1;
  private TextView mSummary2;
  private RMBLabelItem mTotalPrice;
  private MApiRequest requestQuitRoom;
  private RelativeLayout tipBarLayout;
  private ImageView tipCloseImg;
  private View tipDivider;

  private void clearDishCartWithoutUserHistoryGifts()
  {
    Object localObject;
    try
    {
      Iterator localIterator1 = new ArrayList(this.mCartManager.getAllDishesinTotalDish()).iterator();
      while (localIterator1.hasNext())
      {
        localObject = (CartItem)localIterator1.next();
        this.mCartManager.deleteDish(((CartItem)localObject).dishInfo);
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      showShortToast("菜品清空失败");
      return;
    }
    Iterator localIterator2 = new ArrayList(this.mCartManager.getAllUsedFreeDished()).iterator();
    while (localIterator2.hasNext())
    {
      localObject = (CartFreeItem)localIterator2.next();
      if (!((CartFreeItem)localObject).use)
        continue;
      this.mCartManager.operateFreeDish((CartFreeItem)localObject);
    }
    showShortToast("菜品已清空");
  }

  private void initViews()
  {
    boolean bool2 = false;
    this.tipBarLayout = ((RelativeLayout)findViewById(R.id.tip_bar));
    this.tipCloseImg = ((ImageView)findViewById(R.id.tip_close));
    this.tipDivider = findViewById(R.id.tip_divider);
    Object localObject = getSharedPreferences("selectdish_together_tipbar", 0);
    label196: int i;
    if ((this.mCartManager.isOwner == 0) && (((SharedPreferences)localObject).getInt("closed", 0) == 0))
    {
      this.tipBarLayout.setVisibility(0);
      this.tipDivider.setVisibility(0);
      this.tipCloseImg.setOnClickListener(new View.OnClickListener((SharedPreferences)localObject)
      {
        public void onClick(View paramView)
        {
          SelectDishCartTogetherActivity.this.tipBarLayout.setVisibility(8);
          SelectDishCartTogetherActivity.this.tipDivider.setVisibility(8);
          paramView = this.val$prefs.edit();
          paramView.putInt("closed", 1);
          paramView.apply();
        }
      });
      this.mListView = ((PullToRefreshListView)findViewById(R.id.sd_cart_list));
      localObject = findViewById(R.id.sd_cart_empty);
      this.mListView.setEmptyView((View)localObject);
      findViewById(R.id.backlist).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectDishCartTogetherActivity.this.finish();
        }
      });
      this.mBottomLayout = findViewById(R.id.sd_cart_bottom);
      this.mSubmit = ((NovaButton)findViewById(R.id.submit));
      NovaButton localNovaButton = this.mSubmit;
      if (!this.mCartManager.supportPrepay)
        break label495;
      localObject = "确认支付";
      localNovaButton.setText((CharSequence)localObject);
      this.mSubmit.setGAString("submit", this.gaUserInfo);
      this.mSubmit.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (SelectDishCartTogetherActivity.this.accountService().token() != null)
          {
            SelectDishCartTogetherActivity.this.mCartManager.stopPollingRequeset();
            SelectDishCartTogetherActivity.this.requestQuitOrderDishRoom();
            new TogetherSubmitOrderManager().submit(SelectDishCartTogetherActivity.this);
            SelectDishCartTogetherActivity.this.mListView.setMode(PullToRefreshBase.Mode.DISABLED);
            return;
          }
          SelectDishCartTogetherActivity.this.accountService().login(SelectDishCartTogetherActivity.this);
        }
      });
      localObject = (TextView)findViewById(R.id.submit_button_subtitle);
      ((TextView)localObject).setText(this.mCartManager.orderButtonSubTitle);
      if (!TextUtils.isEmpty(this.mCartManager.orderButtonSubTitle))
        break label502;
      i = 8;
      label268: ((TextView)localObject).setVisibility(i);
      this.mTotalPrice = ((RMBLabelItem)findViewById(R.id.sd_cart_price));
      this.mSummary1 = ((TextView)findViewById(R.id.sd_cart_summary1));
      this.mSummary2 = ((TextView)findViewById(R.id.sd_cart_summary2));
      this.mCartAdapter = new SelectDishCartAdapter(null, this.mCartManager, this);
      this.mListView.setAdapter(this.mCartAdapter);
      this.mListView.setOnRefreshListener(this);
      if (this.mCartManager.isOwner != 1)
        break label507;
      super.getTitleBar().addRightViewItem("清空", "clearcart", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectDishCartTogetherActivity.this.showClearConfirmAlert();
        }
      });
      updateCartInfos(this.mCartManager.getTotalDishCount() + this.mCartManager.getTotalSelectFreeDishCount(), this.mCartManager.getTotalDishCount() + this.mCartManager.getTotalFreeDishCount(), this.mCartManager.getTotalSingleDishCount() + this.mCartManager.getTotalSelectFreeDishCount());
    }
    while (true)
    {
      boolean bool1 = bool2;
      if (this.mCartManager.getAllDishesinTotalDish() != null)
      {
        bool1 = bool2;
        if (this.mCartManager.getAllDishesinTotalDish().size() > 0)
          bool1 = true;
      }
      setCartClearButtonEnabled(bool1);
      return;
      this.tipBarLayout.setVisibility(8);
      this.tipDivider.setVisibility(8);
      break;
      label495: localObject = "去下单";
      break label196;
      label502: i = 0;
      break label268;
      label507: updateCartInfos(this.mCartManager.getTotalDishCount() + this.mCartManager.getOtherTotalCount(), this.mCartManager.getTotalDishCount() + this.mCartManager.getOtherTotalCount(), this.mCartManager.getTotalSingleDishCount());
    }
  }

  private void setCartClearButtonEnabled(boolean paramBoolean)
  {
    Object localObject = super.getTitleBar().findRightViewItemByTag("clearcart");
    if ((localObject != null) && (((View)localObject).isEnabled() != paramBoolean))
    {
      if (this.mCartManager.isOwner != 1)
        break label91;
      ((View)localObject).setVisibility(0);
      ((View)localObject).setEnabled(paramBoolean);
      if ((localObject instanceof NovaTextView))
      {
        localObject = (NovaTextView)localObject;
        if (!paramBoolean)
          break label77;
      }
    }
    label77: for (int i = getResources().getColor(R.color.light_red); ; i = getResources().getColor(R.color.click_gray))
    {
      ((NovaTextView)localObject).setTextColor(i);
      return;
    }
    label91: ((View)localObject).setVisibility(8);
  }

  private void showClearConfirmAlert()
  {
    SelectDishAlertDialog localSelectDishAlertDialog = new SelectDishAlertDialog(this, "确定清空购物车里的菜品吗？", null, "取消", "确定");
    localSelectDishAlertDialog.setCanceledOnTouchOutside(true);
    localSelectDishAlertDialog.setDialogClickListener(new SelectDishAlertDialog.OnDialogClickListener()
    {
      public void onCancelClick(Dialog paramDialog)
      {
        paramDialog.dismiss();
      }

      public void onConfirmClick(Dialog paramDialog)
      {
        paramDialog.dismiss();
        SelectDishCartTogetherActivity.this.clearDishCartWithoutUserHistoryGifts();
        GAHelper.instance().contextStatisticsEvent(SelectDishCartTogetherActivity.this, "clearcart_confirm", SelectDishCartTogetherActivity.this.gaUserInfo, "tap");
      }
    });
    localSelectDishAlertDialog.show();
  }

  private void updateCartInfos(int paramInt1, int paramInt2, int paramInt3)
  {
    setTitle(getString(R.string.sd_title_cart).replace("%s", String.valueOf(paramInt1)));
    this.mTotalPrice.setRMBLabelStyle(3, 2, false, getResources().getColor(R.color.light_red));
    this.mTotalPrice.setRMBLabelValue(this.mCartAdapter.getTotalPrice());
    this.mSummary1.setText(getString(R.string.sd_dish_count).replace("%s", String.valueOf(paramInt3)));
    double d;
    Object localObject;
    boolean bool;
    if (this.mAverage != 0.0D)
    {
      d = this.mCartAdapter.getTotalPrice() / this.mAverage;
      if ((this.isEstimate) && (d > 1.0D) && (paramInt3 != 0) && (this.mCartAdapter.getTotalPrice() / paramInt3 >= 20.0D))
        break label203;
      this.mSummary2.setVisibility(8);
      localObject = this.mSubmit;
      if (paramInt1 <= 0)
        break label292;
      bool = true;
      label159: ((NovaButton)localObject).setEnabled(bool);
      if (this.mCartManager.isOwner != 1)
        break label303;
      localObject = this.mBottomLayout;
      if (paramInt2 > 0)
        break label298;
    }
    label292: label298: for (paramInt1 = 8; ; paramInt1 = 0)
    {
      ((View)localObject).setVisibility(paramInt1);
      return;
      d = 0.0D;
      break;
      label203: if (d % 1.0D != 0.0D)
        this.mSummary2.setText(getString(R.string.sd_advice_2).replace("%a", String.valueOf(()d)).replace("%b", String.valueOf(()d + 1L)));
      while (true)
      {
        this.mSummary2.setVisibility(0);
        break;
        this.mSummary2.setText(getString(R.string.sd_advice_1).replace("%s", String.valueOf(()d)));
      }
      bool = false;
      break label159;
    }
    label303: this.mBottomLayout.setVisibility(8);
  }

  public String getPageName()
  {
    return "menuorder_cart";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.selectdish_together_cart_layout);
    this.mShopId = this.mCartManager.getShopId();
    GAHelper.instance().setGAPageName(getPageName());
    this.gaUserInfo.shop_id = Integer.valueOf(this.mShopId);
    GAHelper.instance().contextStatisticsEvent(this, "cart", this.gaUserInfo, "view");
    this.mAverage = this.mCartManager.average;
    this.isEstimate = this.mCartManager.isEstimate;
    initViews();
    this.mCartManager.sortDishes();
    this.mCartManager.addCartChangedListener(this.dishCartListener);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.mCartManager.removeCartChangedListener(this.dishCartListener);
    if (this.requestQuitRoom != null)
    {
      mapiService().abort(this.requestQuitRoom, this, true);
      this.requestQuitRoom = null;
    }
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    this.mCartManager.stopPollingRequeset();
    this.mCartManager.syncCart();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  protected void requestQuitOrderDishRoom()
  {
    if (this.requestQuitRoom != null)
      mapiService().abort(this.requestQuitRoom, this, true);
    this.requestQuitRoom = BasicMApiRequest.mapiPost(Uri.parse("http://m.api.dianping.com/orderdish/quitorderdishroom.hbt").buildUpon().toString(), new String[] { "roomid", String.valueOf(this.mCartManager.roomId) });
    mapiService().exec(this.requestQuitRoom, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.ui.SelectDishCartTogetherActivity
 * JD-Core Version:    0.6.0
 */
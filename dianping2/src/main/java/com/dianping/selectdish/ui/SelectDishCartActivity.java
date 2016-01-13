package com.dianping.selectdish.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.base.widget.TitleBar;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.NewCartManager.CartChangedListener;
import com.dianping.selectdish.SubmitOrderManager;
import com.dianping.selectdish.entity.SelectDishCartAdapter;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.util.GetHistoryUtil;
import com.dianping.selectdish.util.GetHistoryUtil.GetHistoryListener;
import com.dianping.selectdish.view.SelectDishAlertDialog;
import com.dianping.selectdish.view.SelectDishAlertDialog.OnDialogClickListener;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.Iterator;

public class SelectDishCartActivity extends NovaActivity
{
  private final NewCartManager.CartChangedListener dishCartListener = new NewCartManager.CartChangedListener()
  {
    public void onCountChanged()
    {
      SelectDishCartActivity.this.updateCartInfos(SelectDishCartActivity.this.mCartManager.getTotalDishCount() + SelectDishCartActivity.this.mCartManager.getTotalSelectFreeDishCount(), SelectDishCartActivity.this.mCartManager.getTotalDishCount() + SelectDishCartActivity.this.mCartManager.getTotalFreeDishCount(), SelectDishCartActivity.this.mCartManager.getTotalSingleDishCount() + SelectDishCartActivity.this.mCartManager.getTotalSelectFreeDishCount());
      SelectDishCartActivity localSelectDishCartActivity = SelectDishCartActivity.this;
      if (SelectDishCartActivity.this.mCartManager.getTotalDishCount() > 0);
      for (boolean bool = true; ; bool = false)
      {
        localSelectDishCartActivity.setCartClearButtonEnabled(bool);
        return;
      }
    }

    public void onDishChanged(CartItem paramCartItem)
    {
      SelectDishCartActivity.this.mCartAdapter.notifyDataSetChanged();
    }

    public void onFreeDishChanged(ArrayList<CartFreeItem> paramArrayList)
    {
      SelectDishCartActivity.this.mCartAdapter.notifyDataSetChanged();
    }

    public void onGroupOnOrSetChanged()
    {
    }
  };
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private GetHistoryUtil getHistoryUtil = new GetHistoryUtil(this, false);
  private boolean isEstimate;
  private double mAverage;
  private View mBottomLayout;
  private SelectDishCartAdapter mCartAdapter;
  private NewCartManager mCartManager = NewCartManager.getInstance();
  private View mEmptyContentView;
  private ProgressDialog mProgressDialog;
  private int mShopId;
  private NovaButton mSubmit;
  private TextView mSummary1;
  private TextView mSummary2;
  private String mTableId;
  private RMBLabelItem mTotalPrice;
  private RelativeLayout tipBarLayout;
  private ImageView tipCloseImg;
  private View tipDivider;

  private void clearDishCartWithoutUserHistoryGifts()
  {
    Object localObject;
    try
    {
      Iterator localIterator1 = new ArrayList(this.mCartManager.getAllDishes()).iterator();
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

  private void getUserHistory()
  {
    this.getHistoryUtil.setGetHistoryListener(new GetHistoryUtil.GetHistoryListener()
    {
      public void onFail()
      {
        SelectDishCartActivity.this.mEmptyContentView.setVisibility(0);
        if ((SelectDishCartActivity.this.mProgressDialog != null) && (SelectDishCartActivity.this.mProgressDialog.isShowing()))
        {
          SelectDishCartActivity.this.mProgressDialog.dismiss();
          SelectDishCartActivity.access$402(SelectDishCartActivity.this, null);
        }
      }

      public void onFinish()
      {
        SelectDishCartActivity.this.mEmptyContentView.setVisibility(0);
        if ((SelectDishCartActivity.this.mProgressDialog != null) && (SelectDishCartActivity.this.mProgressDialog.isShowing()))
        {
          SelectDishCartActivity.this.mProgressDialog.dismiss();
          SelectDishCartActivity.access$402(SelectDishCartActivity.this, null);
        }
      }

      public void onProgress()
      {
      }

      public void onStart()
      {
        SelectDishCartActivity.this.mEmptyContentView.setVisibility(4);
        if ((SelectDishCartActivity.this.mProgressDialog != null) && (SelectDishCartActivity.this.mProgressDialog.isShowing()))
          SelectDishCartActivity.this.mProgressDialog.dismiss();
        SelectDishCartActivity.access$402(SelectDishCartActivity.this, new ProgressDialog(SelectDishCartActivity.this));
        SelectDishCartActivity.this.mProgressDialog.setMessage(SelectDishCartActivity.this.getResources().getString(R.string.sd_loading_userhistory));
        SelectDishCartActivity.this.mProgressDialog.setCancelable(false);
        SelectDishCartActivity.this.mProgressDialog.show();
      }
    });
    this.getHistoryUtil.getUserHistory();
  }

  private void initViews()
  {
    boolean bool2 = false;
    this.tipBarLayout = ((RelativeLayout)findViewById(R.id.tip_bar));
    this.tipCloseImg = ((ImageView)findViewById(R.id.tip_close));
    this.tipDivider = findViewById(R.id.tip_divider);
    Object localObject = getSharedPreferences("selectdishtipbar", 0);
    ListView localListView;
    if (((SharedPreferences)localObject).getInt("closed", 0) == 0)
    {
      this.tipBarLayout.setVisibility(0);
      this.tipDivider.setVisibility(0);
      this.tipCloseImg.setOnClickListener(new View.OnClickListener((SharedPreferences)localObject)
      {
        public void onClick(View paramView)
        {
          SelectDishCartActivity.this.tipBarLayout.setVisibility(8);
          SelectDishCartActivity.this.tipDivider.setVisibility(8);
          paramView = this.val$prefs.edit();
          paramView.putInt("closed", 1);
          paramView.commit();
        }
      });
      localListView = (ListView)findViewById(R.id.sd_cart_list);
      localListView.setEmptyView(findViewById(R.id.sd_cart_empty));
      this.mEmptyContentView = findViewById(R.id.sd_cart_empty_content);
      findViewById(R.id.backlist).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectDishCartActivity.this.finish();
        }
      });
      this.mBottomLayout = findViewById(R.id.sd_cart_bottom);
      this.mSubmit = ((NovaButton)findViewById(R.id.submit));
      NovaButton localNovaButton = this.mSubmit;
      if (!this.mCartManager.supportPrepay)
        break label468;
      localObject = "确认支付";
      label189: localNovaButton.setText((CharSequence)localObject);
      this.mSubmit.setGAString("submit", this.gaUserInfo);
      this.mSubmit.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (SelectDishCartActivity.this.accountService().token() != null)
          {
            new SubmitOrderManager().submit(SelectDishCartActivity.this);
            return;
          }
          SelectDishCartActivity.this.accountService().login(SelectDishCartActivity.this);
        }
      });
      localObject = (TextView)findViewById(R.id.submit_button_subtitle);
      ((TextView)localObject).setText(this.mCartManager.orderButtonSubTitle);
      if (!TextUtils.isEmpty(this.mCartManager.orderButtonSubTitle))
        break label475;
    }
    label468: label475: for (int i = 8; ; i = 0)
    {
      ((TextView)localObject).setVisibility(i);
      this.mTotalPrice = ((RMBLabelItem)findViewById(R.id.sd_cart_price));
      this.mSummary1 = ((TextView)findViewById(R.id.sd_cart_summary1));
      this.mSummary2 = ((TextView)findViewById(R.id.sd_cart_summary2));
      this.mCartAdapter = new SelectDishCartAdapter(this.mCartManager, null, this);
      localListView.setAdapter(this.mCartAdapter);
      super.getTitleBar().addRightViewItem("清空", "clearcart", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectDishCartActivity.this.showClearConfirmAlert();
        }
      });
      updateCartInfos(this.mCartManager.getTotalDishCount() + this.mCartManager.getTotalSelectFreeDishCount(), this.mCartManager.getTotalDishCount() + this.mCartManager.getTotalFreeDishCount(), this.mCartManager.getTotalSingleDishCount() + this.mCartManager.getTotalSelectFreeDishCount());
      boolean bool1 = bool2;
      if (this.mCartManager.getAllDishes() != null)
      {
        bool1 = bool2;
        if (this.mCartManager.getAllDishes().size() > 0)
          bool1 = true;
      }
      setCartClearButtonEnabled(bool1);
      return;
      this.tipBarLayout.setVisibility(8);
      this.tipDivider.setVisibility(8);
      break;
      localObject = "去下单";
      break label189;
    }
  }

  private void setCartClearButtonEnabled(boolean paramBoolean)
  {
    Object localObject = super.getTitleBar().findRightViewItemByTag("clearcart");
    if ((localObject != null) && (((View)localObject).isEnabled() != paramBoolean))
    {
      ((View)localObject).setEnabled(paramBoolean);
      if ((localObject instanceof NovaTextView))
      {
        localObject = (NovaTextView)localObject;
        if (!paramBoolean)
          break label61;
      }
    }
    label61: for (int i = getResources().getColor(R.color.light_red); ; i = getResources().getColor(R.color.click_gray))
    {
      ((NovaTextView)localObject).setTextColor(i);
      return;
    }
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
        SelectDishCartActivity.this.clearDishCartWithoutUserHistoryGifts();
        GAHelper.instance().contextStatisticsEvent(SelectDishCartActivity.this, "clearcart_confirm", SelectDishCartActivity.this.gaUserInfo, "tap");
      }
    });
    localSelectDishAlertDialog.show();
  }

  private void updateCartInfos(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    setTitle(getString(R.string.sd_title_cart).replace("%s", String.valueOf(paramInt1)));
    this.mTotalPrice.setRMBLabelStyle(3, 2, false, getResources().getColor(R.color.light_red));
    this.mTotalPrice.setRMBLabelValue(this.mCartAdapter.getTotalPrice());
    this.mSummary1.setText(getString(R.string.sd_dish_count).replace("%s", String.valueOf(paramInt3)));
    double d;
    Object localObject;
    if (this.mAverage != 0.0D)
    {
      d = this.mCartAdapter.getTotalPrice() / this.mAverage;
      if ((this.isEstimate) && (d > 1.0D) && (paramInt3 != 0) && (this.mCartAdapter.getTotalPrice() / paramInt3 >= 20.0D))
        break label210;
      this.mSummary2.setVisibility(8);
      localObject = this.mSubmit;
      if (paramInt1 <= 0)
        break label299;
    }
    label299: for (boolean bool = true; ; bool = false)
    {
      ((NovaButton)localObject).setEnabled(bool);
      localObject = this.mBottomLayout;
      paramInt1 = i;
      if (paramInt2 <= 0)
      {
        paramInt1 = i;
        if (this.mCartManager.getGroupOnInfo() == null)
          paramInt1 = 4;
      }
      ((View)localObject).setVisibility(paramInt1);
      return;
      d = 0.0D;
      break;
      label210: if (d % 1.0D != 0.0D)
        this.mSummary2.setText(getString(R.string.sd_advice_2).replace("%a", String.valueOf(()d)).replace("%b", String.valueOf(()d + 1L)));
      while (true)
      {
        this.mSummary2.setVisibility(0);
        break;
        this.mSummary2.setText(getString(R.string.sd_advice_1).replace("%s", String.valueOf(()d)));
      }
    }
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
    setContentView(R.layout.selectdish_cart_layout);
    this.mShopId = this.mCartManager.getShopId();
    GAHelper.instance().setGAPageName(getPageName());
    this.gaUserInfo.shop_id = Integer.valueOf(this.mShopId);
    GAHelper.instance().contextStatisticsEvent(this, "cart", this.gaUserInfo, "view");
    this.mAverage = this.mCartManager.average;
    this.isEstimate = this.mCartManager.isEstimate;
    this.mTableId = this.mCartManager.tableId;
    initViews();
    this.mCartManager.sortDishes();
    this.mCartManager.addCartChangedListener(this.dishCartListener);
    if ((accountService().token() != null) && (!this.mCartManager.hasHistoryFreeDish()))
      getUserHistory();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.mCartManager.removeCartChangedListener(this.dishCartListener);
    this.mCartManager.storeCart();
    this.getHistoryUtil.releaseHistoryRequest();
    this.getHistoryUtil.removeGetHistoryListener();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if ((paramBoolean) && (!this.mCartManager.hasHistoryFreeDish()))
      getUserHistory();
    return paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.ui.SelectDishCartActivity
 * JD-Core Version:    0.6.0
 */
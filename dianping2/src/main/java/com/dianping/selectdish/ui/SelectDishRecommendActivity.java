package com.dianping.selectdish.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.NewCartManager.CartChangedListener;
import com.dianping.selectdish.fragment.SelectDishRecommendFragment;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.view.HorizontalNumberPicker;
import com.dianping.selectdish.view.HorizontalNumberPicker.NumberPickerListener;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import java.util.ArrayList;

public class SelectDishRecommendActivity extends NovaActivity
  implements NewCartManager.CartChangedListener
{
  private static final int MSG_DELAY_REQUST = 1;
  private NovaButton addDishBtn;
  private boolean firstRequest = true;
  private ArrayList<Fragment> fragments = new ArrayList();
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private NovaButton goOrderBtn;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
      }
      SelectDishRecommendActivity.this.reqRecommend();
    }
  };
  private TextView hasOrderTextView;
  private HorizontalNumberPicker horizontalNP;
  private NewCartManager mCartManager = NewCartManager.getInstance();
  private Context mContext;
  private MApiRequest mRequestRecommend;
  private FullRequestHandle<MApiRequest, MApiResponse> mapiHandler = new FullRequestHandle()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      SelectDishRecommendActivity.this.dismissDialog();
      if (paramMApiResponse.message() != null);
      for (paramMApiRequest = paramMApiResponse.message(); ; paramMApiRequest = new SimpleMsg("错误", "网络错误,请重试", 0, 0))
      {
        Toast.makeText(SelectDishRecommendActivity.this.mContext, paramMApiRequest.content(), 0).show();
        SelectDishRecommendActivity.this.setupFail();
        return;
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      boolean bool = false;
      SelectDishRecommendActivity.this.dismissDialog();
      if (paramMApiRequest == SelectDishRecommendActivity.this.mRequestRecommend)
      {
        SelectDishRecommendActivity.this.displayLoadingView(false);
        paramMApiRequest = paramMApiResponse.result();
        if ((paramMApiRequest instanceof DPObject))
        {
          paramMApiRequest = (DPObject)paramMApiRequest;
          paramMApiResponse = paramMApiRequest.getArray("Menus");
          SelectDishRecommendActivity.this.tabs.clear();
          int i = 0;
          while (i < paramMApiResponse.length)
          {
            SelectDishRecommendActivity.this.tabs.add(paramMApiResponse[i]);
            i += 1;
          }
          if (SelectDishRecommendActivity.this.firstRequest)
          {
            SelectDishRecommendActivity.this.setupPeopleNum(paramMApiRequest.getInt("MaxNum"), paramMApiRequest.getInt("DefaultNum"));
            paramMApiRequest = SelectDishRecommendActivity.this;
            if (!SelectDishRecommendActivity.this.firstRequest)
              bool = true;
            SelectDishRecommendActivity.access$002(paramMApiRequest, bool);
          }
          if (SelectDishRecommendActivity.this.tabs.size() != 0)
          {
            SelectDishRecommendActivity.this.setupTitle();
            SelectDishRecommendActivity.this.setupViewPager();
          }
        }
      }
    }

    public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
    {
    }

    public void onRequestStart(MApiRequest paramMApiRequest)
    {
      if (!SelectDishRecommendActivity.this.firstRequest)
        SelectDishRecommendActivity.this.showProgressDialog("点小评正在给您配置菜品，请稍等！");
    }
  };
  private int peopleNum = 0;
  private RecommendFragmentPagerAdapter recommendFragmentPagerAdapter;
  private FrameLayout selectDishRecommendError;
  private View selectDishRecommendLoadedLayout;
  private View selectDishRecommendLoadingLayout;
  private int shopId;
  private int startNum;
  private ArrayList<DPObject> tabs = new ArrayList();
  private ArrayList<View> titelViews = new ArrayList();
  private LinearLayout titleLayout;
  private RMBLabelItem totalPrice;
  private ViewPager viewPager;

  private void displayLoadingView(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.selectDishRecommendLoadingLayout.setVisibility(0);
      this.selectDishRecommendError.setVisibility(8);
      this.selectDishRecommendLoadedLayout.setVisibility(8);
      return;
    }
    this.selectDishRecommendLoadingLayout.setVisibility(8);
    this.selectDishRecommendError.setVisibility(8);
    this.selectDishRecommendLoadedLayout.setVisibility(0);
  }

  private void initView()
  {
    this.selectDishRecommendLoadingLayout = super.findViewById(R.id.selectdish_recommend_loading);
    this.selectDishRecommendLoadedLayout = super.findViewById(R.id.selectdish_recommend_loaded);
    this.selectDishRecommendError = ((FrameLayout)super.findViewById(R.id.selectdish_recommend_error));
    this.horizontalNP = ((HorizontalNumberPicker)findViewById(R.id.recommend_people_num));
    this.horizontalNP.setNumberPickerListener(new HorizontalNumberPicker.NumberPickerListener()
    {
      public void onPick(int paramInt)
      {
        GAHelper.instance().contextStatisticsEvent(SelectDishRecommendActivity.this, "numbers_" + SelectDishRecommendActivity.this.peopleNum, SelectDishRecommendActivity.this.gaUserInfo, "slide");
        SelectDishRecommendActivity.access$902(SelectDishRecommendActivity.this, paramInt);
        SelectDishRecommendActivity.this.handler.removeMessages(1);
        SelectDishRecommendActivity.this.handler.sendEmptyMessageDelayed(1, 1500L);
      }
    });
    this.goOrderBtn = ((NovaButton)super.findViewById(R.id.recommend_goorder));
    this.addDishBtn = ((NovaButton)super.findViewById(R.id.recommend_adddish));
    this.hasOrderTextView = ((TextView)super.findViewById(R.id.recommend_hasorder));
    this.totalPrice = ((RMBLabelItem)super.findViewById(R.id.recommend_price));
    updateStatus();
    this.goOrderBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        GAHelper.instance().contextStatisticsEvent(SelectDishRecommendActivity.this, "topay", SelectDishRecommendActivity.this.gaUserInfo, "tap");
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishcart"));
        SelectDishRecommendActivity.this.startActivity(paramView);
      }
    });
    this.addDishBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        GAHelper.instance().contextStatisticsEvent(SelectDishRecommendActivity.this, "backmenu", SelectDishRecommendActivity.this.gaUserInfo, "tap");
        int i = SelectDishRecommendActivity.this.mCartManager.getTotalDishCount();
        if (i > 0)
          Toast.makeText(SelectDishRecommendActivity.this.mContext, "您的购物车中现共有" + i + "道菜咯！", 0).show();
        while (true)
        {
          SelectDishRecommendActivity.this.finish();
          return;
          if (SelectDishRecommendActivity.this.startNum <= 0)
            continue;
          Toast.makeText(SelectDishRecommendActivity.this.mContext, "您的购物车中现共有0道菜咯！", 0).show();
        }
      }
    });
    this.titleLayout = ((LinearLayout)super.findViewById(R.id.recommend_title));
    this.viewPager = ((ViewPager)super.findViewById(R.id.recommend_viewpager));
  }

  private void setupFail()
  {
    this.selectDishRecommendError.removeAllViews();
    this.selectDishRecommendError.addView(getFailedView("网络连接失败 点击重新加载", new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        SelectDishRecommendActivity.this.displayLoadingView(true);
        SelectDishRecommendActivity.this.reqRecommend();
      }
    }));
    this.selectDishRecommendLoadingLayout.setVisibility(8);
    this.selectDishRecommendLoadedLayout.setVisibility(8);
    this.selectDishRecommendError.setVisibility(0);
  }

  private void setupPeopleNum(int paramInt1, int paramInt2)
  {
    this.horizontalNP.setMaxNum(paramInt1, paramInt2);
  }

  private void setupTitle()
  {
    int j = this.tabs.size();
    this.titleLayout.removeAllViews();
    this.titelViews.clear();
    int i = 0;
    while (i < j)
    {
      View localView = LayoutInflater.from(this).inflate(R.layout.selectdish_tab_indicator, null, false);
      ((TextView)localView.findViewById(R.id.title)).setText(((DPObject)this.tabs.get(i)).getString("TabTitle"));
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, -1, 1.0F);
      localView.setOnClickListener(new TabClickListener(i));
      this.titelViews.add(localView);
      this.titleLayout.addView(localView, localLayoutParams);
      i += 1;
    }
    ((View)this.titelViews.get(0)).setSelected(true);
  }

  private void setupViewPager()
  {
    this.viewPager.removeAllViews();
    this.viewPager.setOffscreenPageLimit(5);
    this.fragments.clear();
    int j = this.tabs.size();
    int i = 0;
    while (i < j)
    {
      this.fragments.add(SelectDishRecommendFragment.newInstance((DPObject)this.tabs.get(i), this.shopId));
      i += 1;
    }
    this.recommendFragmentPagerAdapter = new RecommendFragmentPagerAdapter(getSupportFragmentManager(), this.fragments);
    this.viewPager.setAdapter(this.recommendFragmentPagerAdapter);
    this.viewPager.setCurrentItem(0);
    this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
    {
      public void onPageScrollStateChanged(int paramInt)
      {
      }

      public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
      {
      }

      public void onPageSelected(int paramInt)
      {
        GAHelper.instance().contextStatisticsEvent(SelectDishRecommendActivity.this, "switchgroup_" + paramInt, SelectDishRecommendActivity.this.gaUserInfo, "tap");
        int j = SelectDishRecommendActivity.this.titelViews.size();
        int i = 0;
        while (i < j)
        {
          ((View)SelectDishRecommendActivity.this.titelViews.get(i)).setSelected(false);
          i += 1;
        }
        ((View)SelectDishRecommendActivity.this.titelViews.get(paramInt)).setSelected(true);
      }
    });
  }

  private void updateStatus()
  {
    this.hasOrderTextView.setText("已选" + this.mCartManager.getTotalDishCount() + "道");
    Double localDouble1 = Double.valueOf(this.mCartManager.getTotalPrice());
    Double localDouble2 = Double.valueOf(this.mCartManager.getTotalOriginPrice());
    if (localDouble1.doubleValue() >= localDouble2.doubleValue())
    {
      this.totalPrice.setRMBLabelStyle(2, 2, false, getResources().getColor(R.color.light_red));
      this.totalPrice.setRMBLabelValue(localDouble2.doubleValue());
      return;
    }
    this.totalPrice.setRMBLabelStyle(2, 3, false, getResources().getColor(R.color.light_red));
    this.totalPrice.setRMBLabelValue(localDouble1.doubleValue(), localDouble2.doubleValue());
  }

  public String getPageName()
  {
    return "menuorder_serveorder";
  }

  public void onCountChanged()
  {
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.selectdish_recommend_layout);
    this.mContext = this;
    if (paramBundle == null);
    for (this.shopId = getIntParam("shopid"); ; this.shopId = paramBundle.getInt("shopid"))
    {
      GAHelper.instance().setGAPageName(getPageName());
      initView();
      displayLoadingView(true);
      this.mCartManager.addCartChangedListener(this);
      this.startNum = this.mCartManager.getTotalDishCount();
      reqRecommend();
      return;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.handler.removeMessages(1);
    this.mCartManager.removeCartChangedListener(this);
    this.horizontalNP.removeNumberPickerListener();
    if (this.mRequestRecommend != null)
      mapiService().abort(this.mRequestRecommend, this.mapiHandler, true);
  }

  public void onDishChanged(CartItem paramCartItem)
  {
    updateStatus();
  }

  public void onFreeDishChanged(ArrayList<CartFreeItem> paramArrayList)
  {
  }

  public void onGroupOnOrSetChanged()
  {
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.shopId = paramBundle.getInt("shopid");
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("shopid", this.shopId);
  }

  public void reqRecommend()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/dishmenurecommend.hbt").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    if (this.peopleNum != 0)
      localBuilder.appendQueryParameter("peoplenum", String.valueOf(this.peopleNum));
    this.mRequestRecommend = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.mRequestRecommend, this.mapiHandler);
  }

  public class RecommendFragmentPagerAdapter extends FragmentStatePagerAdapter
  {
    private ArrayList<Fragment> list;

    public RecommendFragmentPagerAdapter(ArrayList<Fragment> arg2)
    {
      super();
      Object localObject;
      this.list = localObject;
    }

    public int getCount()
    {
      return this.list.size();
    }

    public Fragment getItem(int paramInt)
    {
      return (Fragment)this.list.get(paramInt);
    }

    public int getItemPosition(Object paramObject)
    {
      return -2;
    }
  }

  public class TabClickListener
    implements View.OnClickListener
  {
    private int index = 0;

    public TabClickListener(int arg2)
    {
      int i;
      this.index = i;
    }

    public void onClick(View paramView)
    {
      SelectDishRecommendActivity.this.viewPager.setCurrentItem(this.index, true);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.ui.SelectDishRecommendActivity
 * JD-Core Version:    0.6.0
 */
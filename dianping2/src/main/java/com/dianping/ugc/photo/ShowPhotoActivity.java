package com.dianping.ugc.photo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.ScreenSlidePagerActivity;
import com.dianping.base.basic.ScreenSlidePagerAdapter;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.TitleBar;
import com.dianping.base.widget.loading.LoadingLayout;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Iterator;

public class ShowPhotoActivity extends ScreenSlidePagerActivity
  implements View.OnClickListener
{
  protected ArrayList<DPObject> arrShopObjs;
  protected int currentPos = 0;
  private ViewPager flipperView;
  String ga;
  boolean isRecommend = false;
  boolean isShowInfo = true;
  boolean isUserPhotoMode = false;
  public SparseArray<DPObject> mapShop = new SparseArray();
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      int i = 0;
      int j;
      if ("com.dianping.action.UPLOAD_PHOTO".equals(paramIntent.getAction()))
        if (paramIntent.getBooleanExtra("photo_delete", false))
        {
          j = paramIntent.getIntExtra("photo_delete_id", 0);
          paramIntent = null;
          if (j > 0)
          {
            Iterator localIterator = ShowPhotoActivity.this.shopPhotos.iterator();
            do
            {
              paramContext = paramIntent;
              if (!localIterator.hasNext())
                break;
              paramContext = (DPObject)localIterator.next();
            }
            while (paramContext.getInt("ID") != j);
            if (paramContext != null)
            {
              ShowPhotoActivity.this.shopPhotos.remove(paramContext);
              j = ShowPhotoActivity.this.viewPager().getCurrentItem();
              if ((ShowPhotoActivity.this.shopPhotos != null) && (ShowPhotoActivity.this.shopPhotos.size() != 0))
                break label143;
              ShowPhotoActivity.this.finish();
            }
          }
        }
      while (true)
      {
        return;
        label143: ShowPhotoActivity.this.viewPager().setAdapter(ShowPhotoActivity.this.pagerAdapter());
        paramContext = ShowPhotoActivity.this.viewPager();
        if (j > 0)
          i = j - 1;
        paramContext.setCurrentItem(i);
        if (ShowPhotoActivity.this.flipperView.getCurrentItem() == ShowPhotoActivity.this.shopPhotos.size())
          ShowPhotoActivity.this.flipperView.setCurrentItem(ShowPhotoActivity.this.shopPhotos.size() - 1);
        paramContext = (DPObject)ShowPhotoActivity.this.shopPhotos.get(ShowPhotoActivity.this.currentPage());
        ShowPhotoActivity.this.updateTitle(paramContext);
        return;
        if ((!"com.dianping.action.UPDATE_PHOTO".equals(paramIntent.getAction())) || (paramIntent.getParcelableExtra("photo") == null))
          continue;
        paramContext = (DPObject)paramIntent.getParcelableExtra("photo");
        i = 0;
        while (i < ShowPhotoActivity.this.shopPhotos.size())
        {
          if (((DPObject)ShowPhotoActivity.this.shopPhotos.get(i)).getInt("ID") == paramContext.getInt("ID"))
          {
            ShowPhotoActivity.this.shopPhotos.remove(i);
            ShowPhotoActivity.this.shopPhotos.add(i, paramContext);
            ShowPhotoActivity.this.viewPager().setAdapter(ShowPhotoActivity.this.pagerAdapter());
            ShowPhotoActivity.this.viewPager().setCurrentItem(i);
            return;
          }
          i += 1;
        }
      }
    }
  };
  public ArrayList<DPObject> shopPhotos;
  private TextView titleText;
  private String url;

  private void updateTitle(DPObject paramDPObject)
  {
    if ((paramDPObject != null) && (paramDPObject.getObject("User") != null) && (this.isUserPhotoMode))
    {
      paramDPObject = (DPObject)this.mapShop.get(Integer.valueOf(((DPObject)this.shopPhotos.get(currentPage())).getInt("ShopID")).intValue());
      if (paramDPObject != null)
        this.titleText.setText(DPObjectUtils.getShopFullName(paramDPObject));
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void onClick(View paramView)
  {
    if ((paramView != this.titleText) || (this.shopPhotos.get(currentPage()) == null))
      return;
    int i = ((DPObject)this.shopPhotos.get(currentPage())).getInt("ShopID");
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + i));
    paramView.putExtra("shop", (Parcelable)this.mapShop.get(i));
    statisticsEvent("profile5", "profile5_photo_shop", "", 0);
    startActivity(paramView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Object localObject = new IntentFilter();
    ((IntentFilter)localObject).addAction("com.dianping.action.UPLOAD_PHOTO");
    ((IntentFilter)localObject).addAction("com.dianping.action.UPDATE_PHOTO");
    registerReceiver(this.receiver, (IntentFilter)localObject);
    this.flipperView = viewPager();
    if (paramBundle == null)
    {
      paramBundle = getIntent();
      this.url = paramBundle.getStringExtra("url");
      this.shopPhotos = pageList();
      this.arrShopObjs = paramBundle.getParcelableArrayListExtra("arrShopObjs");
      if ((this.shopPhotos == null) && (this.url == null))
        finish();
    }
    do
    {
      do
      {
        return;
        this.isUserPhotoMode = paramBundle.getBooleanExtra("isUserPhotoMode", false);
        this.isRecommend = paramBundle.getBooleanExtra("isRecommend", false);
        this.ga = paramBundle.getData().getQueryParameter("ga");
        if (TextUtils.isEmpty(this.ga))
          this.ga = "shopinfo5_photo_user";
        while (this.arrShopObjs != null)
        {
          paramBundle = this.arrShopObjs.iterator();
          while (paramBundle.hasNext())
          {
            localObject = (DPObject)paramBundle.next();
            this.mapShop.put(Integer.valueOf(((DPObject)localObject).getInt("ID")).intValue(), localObject);
          }
          this.shopPhotos = paramBundle.getParcelableArrayList("paths");
          this.currentPos = paramBundle.getInt("currentPos");
          this.url = paramBundle.getString("url");
          this.arrShopObjs = paramBundle.getParcelableArrayList("arrShopObjs");
          this.isUserPhotoMode = paramBundle.getBoolean("isUserPhotoMode");
          this.isRecommend = paramBundle.getBoolean("isRecommend");
        }
      }
      while (!this.isUserPhotoMode);
      paramBundle = (FrameLayout)findViewById(16908290);
      localObject = getLayoutInflater().inflate(R.layout.double_line_title_bar_black, paramBundle, false);
      this.titleText = ((TextView)((View)localObject).findViewById(16908310));
      this.titleText.setMaxEms(7);
      this.titleText.setEllipsize(TextUtils.TruncateAt.END);
      this.titleText.setOnClickListener(this);
      paramBundle.addView((View)localObject);
      paramBundle = (DPObject)this.mapShop.get(Integer.valueOf(((DPObject)this.shopPhotos.get(currentPage())).getInt("ShopID")).intValue());
    }
    while (paramBundle == null);
    this.titleText.setText(DPObjectUtils.getShopFullName(paramBundle));
  }

  protected void onDestroy()
  {
    unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onPageScrollStateChanged(int paramInt)
  {
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
  }

  public void onPageSelected(int paramInt)
  {
    this.currentPos = paramInt;
    this.currentPage = this.currentPos;
    updateTitle((DPObject)this.shopPhotos.get(paramInt));
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    this.currentPos = paramBundle.getInt("currentPos");
    this.shopPhotos = paramBundle.getParcelableArrayList("paths");
    super.onRestoreInstanceState(paramBundle);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("currentPos", this.currentPos);
    paramBundle.putParcelableArrayList("paths", this.shopPhotos);
    paramBundle.putString("url", this.url);
    paramBundle.putParcelableArrayList("arrShopObjs", this.arrShopObjs);
    paramBundle.putBoolean("isRecommend", this.isRecommend);
    paramBundle.putBoolean("isUserPhotoMode", this.isUserPhotoMode);
  }

  protected void onStart()
  {
    super.onStart();
    ArrayList localArrayList;
    if ((this.shopPhotos != null) && (this.shopPhotos.size() > 0))
    {
      localArrayList = this.shopPhotos;
      if (this.currentPos <= this.shopPhotos.size() - 1)
        break label65;
    }
    label65: for (int i = this.shopPhotos.size() - 1; ; i = this.currentPos)
    {
      updateTitle((DPObject)localArrayList.get(i));
      return;
    }
  }

  protected PagerAdapter pagerAdapter()
  {
    if ((this.shopPhotos != null) && (this.shopPhotos.size() > 0))
      return new ScreenSlidePagerAdapter(getSupportFragmentManager(), this.currentPage, this.pageList, this.currentBitmap, ShowPhotoFragment.class, "showPhoto");
    if (this.url != null)
      return new SingleViewPagerAdapter(this.url);
    return super.pagerAdapter();
  }

  private class SingleViewPagerAdapter extends PagerAdapter
  {
    private String url;

    public SingleViewPagerAdapter(String arg2)
    {
      Object localObject;
      this.url = localObject;
    }

    public void destroyItem(View paramView, int paramInt, Object paramObject)
    {
      ((ViewPager)paramView).removeView((View)paramObject);
    }

    public int getCount()
    {
      return 1;
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      paramViewGroup = new LoadingLayout(paramViewGroup.getContext());
      paramViewGroup.creatLoadingLayout(true, true, true);
      paramViewGroup.setImageUrl(this.url);
      paramViewGroup.setLoadingBackgruond(ShowPhotoActivity.this.currentBitmap);
      return paramViewGroup;
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.ShowPhotoActivity
 * JD-Core Version:    0.6.0
 */
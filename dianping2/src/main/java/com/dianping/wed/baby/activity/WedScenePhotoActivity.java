package com.dianping.wed.baby.activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.base.widget.loading.LoadingLayout;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.List;

public class WedScenePhotoActivity extends NovaActivity
  implements View.OnClickListener
{
  String bookingbtntext;
  DPObject dpObject;
  DPObject dpProduct;
  DPObject dpShop;
  ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener()
  {
    public void onPageScrollStateChanged(int paramInt)
    {
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
    }

    public void onPageSelected(int paramInt)
    {
      WedScenePhotoActivity.this.viewIndex.setText(paramInt + 1 + "/" + WedScenePhotoActivity.this.photoObjs.length);
      WedScenePhotoActivity.this.viewName.setText(WedScenePhotoActivity.this.photoObjs[paramInt].getString("Title"));
    }
  };
  PagerAdapter pagerAdapter = new PagerAdapter()
  {
    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      paramViewGroup.removeView((View)WedScenePhotoActivity.this.photoViews.get(paramInt));
    }

    public int getCount()
    {
      return WedScenePhotoActivity.this.photoViews.size();
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      paramViewGroup.addView((View)WedScenePhotoActivity.this.photoViews.get(paramInt));
      return WedScenePhotoActivity.this.photoViews.get(paramInt);
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }
  };
  int photoIndex;
  DPObject[] photoObjs;
  List<View> photoViews;
  String productId;
  int shopId;
  TextView viewDes;
  TextView viewIndex;
  TextView viewName;
  ViewPager viewPager;

  void initView()
  {
    if (this.dpObject != null)
    {
      this.photoObjs = this.dpObject.getArray("WeddingSceneInfoList");
      if ((this.photoObjs != null) && (this.photoObjs.length > 0))
      {
        if ((this.photoIndex < 0) || (this.photoIndex >= this.photoObjs.length))
          this.photoIndex = 0;
        if (this.photoViews == null)
        {
          this.photoViews = new ArrayList();
          this.viewPager.setAdapter(this.pagerAdapter);
          this.viewPager.setOnPageChangeListener(this.onPageChangeListener);
        }
        while (true)
        {
          int i = 0;
          while (i < this.photoObjs.length)
          {
            LoadingLayout localLoadingLayout = new LoadingLayout(this);
            localLoadingLayout.creatLoadingLayout(false, true, true);
            localLoadingLayout.setImageUrl(this.photoObjs[i].getString("LargePicUrl"));
            this.photoViews.add(localLoadingLayout);
            i += 1;
          }
          this.photoViews.clear();
          this.pagerAdapter.notifyDataSetChanged();
        }
        this.pagerAdapter.notifyDataSetChanged();
        this.viewPager.setCurrentItem(this.photoIndex);
        this.viewIndex.setText(this.photoIndex + 1 + "/" + this.photoObjs.length);
        this.viewName.setText(this.photoObjs[this.photoIndex].getString("Title"));
      }
    }
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.imagebutton_babyphoto_back)
      onBackPressed();
    do
    {
      do
      {
        return;
        if (paramView.getId() != R.id.button_wedding_booking)
          continue;
        paramView = Uri.parse("dianping://weddingbabybooking").buildUpon();
        paramView.appendQueryParameter("shopid", this.shopId + "").appendQueryParameter("productid", this.productId + "").appendQueryParameter("bookingBtnText", this.bookingbtntext);
        if (!TextUtils.isEmpty(getStringParam("shopName")))
          paramView.appendQueryParameter("shopname", getStringParam("shopName"));
        if (!TextUtils.isEmpty(getStringParam("productCategoryID")))
          paramView.appendQueryParameter("productcategoryid", getStringParam("productCategoryID"));
        startActivity(new Intent("android.intent.action.VIEW", paramView.build()));
        return;
      }
      while ((paramView.getId() != R.id.textview_wedding_booking) || (this.dpShop == null));
      paramView = this.dpShop.getStringArray("PhoneNos");
    }
    while ((paramView == null) || (paramView.length <= 0));
    if (paramView.length == 1)
    {
      TelephoneUtils.dial(this, this.dpShop, paramView[0]);
      return;
    }
    new AlertDialog.Builder(this).setTitle("联系商户").setAdapter(new ArrayAdapter(this, R.layout.simple_list_item_1, 16908308, paramView, paramView)
    {
      public String getItem(int paramInt)
      {
        return "拨打电话：" + this.val$phoneNos[paramInt];
      }
    }
    , new DialogInterface.OnClickListener(paramView)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        TelephoneUtils.dial(WedScenePhotoActivity.this, WedScenePhotoActivity.this.dpShop, this.val$phoneNos[paramInt]);
      }
    }).show();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.wed_activity_wedphoto);
    if ((getIntent() != null) && (getIntent().hasExtra("dpobject")))
    {
      this.dpObject = getObjectParam("dpobject");
      this.shopId = getIntParam("shopid");
      this.photoIndex = getIntParam("index");
      this.productId = getStringParam("productid");
      this.bookingbtntext = getStringParam("bookingbtntext");
      this.dpShop = getObjectParam("shop");
    }
    for (this.dpProduct = getObjectParam("product"); ; this.dpProduct = ((DPObject)paramBundle.getParcelable("product")))
    {
      do
      {
        this.viewPager = ((ViewPager)findViewById(R.id.viewpager_babyphoto));
        this.viewName = ((TextView)findViewById(R.id.textview_babyphoto));
        this.viewDes = ((TextView)findViewById(R.id.textview_babydes));
        this.viewIndex = ((TextView)findViewById(R.id.textview_babyphoto_index));
        ((ImageButton)findViewById(R.id.imagebutton_babyphoto_back)).setOnClickListener(this);
        Object localObject = (NovaButton)findViewById(R.id.button_wedding_booking);
        ((NovaButton)localObject).setOnClickListener(this);
        if (!TextUtils.isEmpty(this.bookingbtntext))
          ((NovaButton)localObject).setText(this.bookingbtntext);
        paramBundle = getCloneUserInfo();
        paramBundle.shop_id = Integer.valueOf(this.shopId);
        paramBundle.biz_id = (this.productId + "");
        ((NovaButton)localObject).setGAString("actionbar_wedbooking", paramBundle);
        localObject = (NovaTextView)findViewById(R.id.textview_wedding_booking);
        ((NovaTextView)localObject).setOnClickListener(this);
        ((NovaTextView)localObject).setGAString("actionbar_tel", paramBundle);
        hideTitleBar();
        initView();
        return;
      }
      while ((paramBundle == null) || (!paramBundle.containsKey("dpobject")));
      this.dpObject = ((DPObject)paramBundle.getParcelable("dpobject"));
      this.shopId = paramBundle.getInt("shopid");
      this.productId = paramBundle.getString("productid");
      this.photoIndex = paramBundle.getInt("index");
      this.bookingbtntext = paramBundle.getString("bookingbtntext");
      this.dpShop = ((DPObject)paramBundle.getParcelable("shop"));
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("shopid", this.shopId);
    paramBundle.putString("productid", this.productId);
    paramBundle.putInt("index", this.photoIndex);
    paramBundle.putString("bookingbtntext", this.bookingbtntext);
    paramBundle.putParcelable("dpobject", this.dpObject);
    paramBundle.putParcelable("shop", this.dpShop);
    paramBundle.putParcelable("product", this.dpProduct);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.WedScenePhotoActivity
 * JD-Core Version:    0.6.0
 */
package com.dianping.hotel.shopinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HotelRoomIntroductionActivity extends NovaActivity
{
  private static final boolean DEBUG = false;
  private static final int REQUEST_CODE_BOOKROOM = 3;
  private static final SimpleDateFormat SDF2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
  private static final String TAG = "HotelRoomIntroductionActivity";
  private long endtime;
  private HotelImagePageAdapter mAdapter = new HotelImagePageAdapter();
  private RoomInfoAdapter mRoomInfoAdapter = null;
  private ViewPager mViewPager;
  private String otaid;
  private DPObject roomdetail;
  private String shopid;
  private long starttime;

  private void initBar()
  {
    ((TextView)findViewById(R.id.hotel_roomdetail_text_price)).setText(this.roomdetail.getString("PriceText"));
    Object localObject = (TextView)findViewById(R.id.hotel_roomdetail_text_tax);
    if (TextUtils.isEmpty(this.roomdetail.getString("Tax")))
      ((TextView)localObject).setVisibility(4);
    while (true)
    {
      ((TextView)findViewById(R.id.hotel_roomdetail_text_refund)).setText(this.roomdetail.getString("ReFund"));
      localObject = (Button)findViewById(R.id.hotel_roomdetail_button_book);
      if (this.roomdetail.getInt("Status") != 1)
        break;
      ((Button)localObject).setText("预订");
      ((Button)localObject).setEnabled(true);
      ((Button)localObject).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = Uri.parse("dianping://hotelbookingweb");
          Uri localUri = Uri.parse(HotelRoomIntroductionActivity.this.roomdetail.getString("BookingUrl")).buildUpon().appendQueryParameter("shopId", HotelRoomIntroductionActivity.this.shopid).appendQueryParameter("startDate", HotelRoomIntroductionActivity.SDF2.format(Long.valueOf(HotelRoomIntroductionActivity.this.starttime))).appendQueryParameter("endDate", HotelRoomIntroductionActivity.SDF2.format(Long.valueOf(HotelRoomIntroductionActivity.this.endtime))).build();
          paramView = new Intent("android.intent.action.VIEW", paramView.buildUpon().appendQueryParameter("url", localUri.toString()).build());
          paramView.putExtra("name", null);
          HotelRoomIntroductionActivity.this.startActivityForResult(paramView, 3);
          try
          {
            HotelRoomIntroductionActivity.this.statisticsEvent("roominfo5", "roominfo5_detail_booking", HotelRoomIntroductionActivity.this.roomdetail.getString("PayPolicy"), Integer.valueOf(HotelRoomIntroductionActivity.this.otaid).intValue());
            return;
          }
          catch (java.lang.NumberFormatException paramView)
          {
          }
        }
      });
      return;
      ((TextView)localObject).setVisibility(0);
      ((TextView)localObject).setText(this.roomdetail.getString("Tax"));
    }
    ((Button)localObject).setText("满房");
    ((Button)localObject).setEnabled(false);
  }

  private void initViews()
  {
    FrameLayout localFrameLayout = new FrameLayout(getApplicationContext());
    localFrameLayout.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
    this.mViewPager = new ViewPager(getApplicationContext());
    this.mViewPager.setLayoutParams(new FrameLayout.LayoutParams(-1, ViewUtils.dip2px(getApplicationContext(), 240.0F), 1));
    localFrameLayout.addView(this.mViewPager);
    Object localObject1 = new TextView(getApplicationContext());
    ((TextView)localObject1).setGravity(17);
    ((TextView)localObject1).setTextColor(-16777216);
    ((TextView)localObject1).setBackgroundResource(R.drawable.detail_indicator_icon_rest);
    ((TextView)localObject1).setPadding(ViewUtils.dip2px(getApplicationContext(), 5.0F), 0, ViewUtils.dip2px(getApplicationContext(), 5.0F), 0);
    Object localObject2 = new FrameLayout.LayoutParams(-2, -2, 81);
    ((FrameLayout.LayoutParams)localObject2).setMargins(0, 0, 0, ViewUtils.dip2px(getApplicationContext(), 10.0F));
    ((TextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
    this.mViewPager.setAdapter(this.mAdapter);
    localObject2 = this.roomdetail.getStringArray("ImageList");
    int i;
    label264: Object localObject3;
    if (localObject2 == null)
    {
      i = 0;
      if (i <= 0)
        break label461;
      localFrameLayout.addView((View)localObject1);
      this.mAdapter.setData(Arrays.asList(localObject2));
      this.mViewPager.setCurrentItem(0);
      ((TextView)localObject1).setText("1/" + i);
      this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener((TextView)localObject1, i)
      {
        public void onPageScrollStateChanged(int paramInt)
        {
        }

        public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
        {
        }

        public void onPageSelected(int paramInt)
        {
          this.val$index.setText(paramInt + 1 + "/" + this.val$urlamount);
          HotelRoomIntroductionActivity.this.statisticsEvent("roominfo5", "roominfo5_detail_photoslide", null, 0);
        }
      });
      localObject1 = LayoutInflater.from(this).inflate(R.layout.hotel_roominfo_layout, null);
      localObject2 = (TextView)((View)localObject1).findViewById(R.id.hotel_roominfo);
      localObject3 = (TextView)((View)localObject1).findViewById(R.id.hotel_roomremains);
      ((TextView)localObject2).setText(this.roomdetail.getString("RoomTypeName"));
      i = this.roomdetail.getInt("Remains");
      if (i != -1)
        break label482;
      ((TextView)localObject3).setText("紧张");
    }
    while (true)
    {
      localObject2 = new View(getApplicationContext());
      ((View)localObject2).setLayoutParams(new AbsListView.LayoutParams(-1, 1));
      ((View)localObject2).setBackgroundColor(Color.parseColor("#c8c8c8"));
      localObject3 = (ListView)findViewById(R.id.hotel_roomdetail_list);
      ((ListView)localObject3).addHeaderView(localFrameLayout, null, false);
      ((ListView)localObject3).addHeaderView((View)localObject1, null, false);
      ((ListView)localObject3).addHeaderView((View)localObject2, null, false);
      this.mRoomInfoAdapter = new RoomInfoAdapter(getApplicationContext(), R.layout.hotel_roomdetail_listitem);
      ((ListView)localObject3).setAdapter(this.mRoomInfoAdapter);
      this.mRoomInfoAdapter.setData(this.roomdetail);
      initBar();
      return;
      i = localObject2.length;
      break;
      label461: this.mAdapter.setData(Arrays.asList(new String[] { null }));
      break label264;
      label482: if ((i >= 1) && (i <= 5))
      {
        ((TextView)localObject3).setText("剩" + i + "间");
        continue;
      }
      ((TextView)localObject3).setVisibility(8);
    }
  }

  private void showEmpty(String paramString)
  {
    View localView = findViewById(R.id.hotel_roomdetail_empty);
    localView.setVisibility(0);
    ((TextView)localView.findViewById(R.id.hotel_roomtype_text_msg1)).setText(paramString);
    localView.findViewById(R.id.hotel_roomtype_text_msg2).setVisibility(8);
    findViewById(R.id.hotel_roomdetail_content).setVisibility(8);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.hotel_roomdetail);
    if (paramBundle == null)
    {
      this.roomdetail = ((DPObject)getIntent().getParcelableExtra("roomdetail"));
      this.starttime = getIntent().getLongExtra("starttime", System.currentTimeMillis());
      this.endtime = getIntent().getLongExtra("endtime", System.currentTimeMillis() + 86400000L);
      this.shopid = getIntent().getStringExtra("shopid");
    }
    for (this.otaid = getIntent().getStringExtra("otaid"); this.roomdetail != null; this.otaid = paramBundle.getString("otaid"))
    {
      initViews();
      return;
      this.roomdetail = ((DPObject)paramBundle.getParcelable("roomdetail"));
      this.starttime = paramBundle.getLong("starttime", System.currentTimeMillis());
      this.endtime = paramBundle.getLong("endtime", System.currentTimeMillis() + 86400000L);
      this.shopid = paramBundle.getString("shopid");
    }
    showEmpty("暂无房型信息喔");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("roomdetail", this.roomdetail);
    paramBundle.putLong("starttime", this.starttime);
    paramBundle.putLong("endtime", this.endtime);
    paramBundle.putString("shopid", this.shopid);
    paramBundle.putString("otaid", this.otaid);
  }

  private class AdaptiveNetworkImageView extends NetworkImageView
  {
    public AdaptiveNetworkImageView(Context arg2)
    {
      this(localContext, null);
    }

    public AdaptiveNetworkImageView(Context paramAttributeSet, AttributeSet arg3)
    {
      this(paramAttributeSet, localAttributeSet, 0);
    }

    public AdaptiveNetworkImageView(Context paramAttributeSet, AttributeSet paramInt, int arg4)
    {
      super(paramInt, i);
    }

    public void setImageBitmap(Bitmap paramBitmap)
    {
      super.setImageBitmap(paramBitmap);
    }
  }

  private class HotelImagePageAdapter extends PagerAdapter
  {
    private List<String> urls = new ArrayList();
    private View[] views = null;

    public HotelImagePageAdapter()
    {
    }

    private View getViewItem(String paramString)
    {
      HotelRoomIntroductionActivity.AdaptiveNetworkImageView localAdaptiveNetworkImageView = new HotelRoomIntroductionActivity.AdaptiveNetworkImageView(HotelRoomIntroductionActivity.this, HotelRoomIntroductionActivity.this.getApplicationContext());
      localAdaptiveNetworkImageView.placeholderLoading = R.drawable.placeholder_loading;
      localAdaptiveNetworkImageView.placeholderEmpty = R.drawable.placeholder_empty;
      localAdaptiveNetworkImageView.placeholderError = R.drawable.placeholder_error;
      localAdaptiveNetworkImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      if (!TextUtils.isEmpty(paramString))
        localAdaptiveNetworkImageView.setImage(paramString);
      return localAdaptiveNetworkImageView;
    }

    public void destroyItem(View paramView, int paramInt, Object paramObject)
    {
      ((ViewPager)paramView).removeView(this.views[paramInt]);
      this.views[paramInt] = null;
    }

    public void finishUpdate(View paramView)
    {
    }

    public int getCount()
    {
      return this.urls.size();
    }

    public Object instantiateItem(View paramView, int paramInt)
    {
      this.views[paramInt] = getViewItem((String)this.urls.get(paramInt));
      ((ViewPager)paramView).addView(this.views[paramInt], 0);
      return this.views[paramInt];
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }

    public void setData(List<String> paramList)
    {
      this.urls.clear();
      this.urls.addAll(paramList);
      this.views = new View[paramList.size()];
      notifyDataSetChanged();
    }
  }

  private static class RoomInfo
  {
    public static final int TYPE_CANCEL = 2;
    public static final int TYPE_INFO = 0;
    public static final int TYPE_PROMO = 1;
    public final String content;
    public final String iconurl;
    public final int type;

    public RoomInfo(int paramInt, String paramString1, String paramString2)
    {
      this.type = paramInt;
      this.iconurl = paramString1;
      this.content = paramString2;
    }
  }

  private class RoomInfoAdapter extends BaseAdapter
  {
    private LayoutInflater inflater;
    List<HotelRoomIntroductionActivity.RoomInfo> items = new ArrayList();
    private int resid;

    public RoomInfoAdapter(Context paramInt, int arg3)
    {
      this.inflater = LayoutInflater.from(paramInt);
      int i;
      this.resid = i;
    }

    private void bindView(View paramView, int paramInt)
    {
      NetworkImageView localNetworkImageView = (NetworkImageView)paramView.findViewById(R.id.hotel_roomdetail_listitem_icon);
      paramView = (TextView)paramView.findViewById(R.id.hotel_roomdetail_listitem_content);
      HotelRoomIntroductionActivity.RoomInfo localRoomInfo = (HotelRoomIntroductionActivity.RoomInfo)this.items.get(paramInt);
      if (localRoomInfo.type == 0)
      {
        localNetworkImageView.setVisibility(8);
        paramView.setText(localRoomInfo.content);
      }
      do
      {
        return;
        if (localRoomInfo.type != 1)
          continue;
        localNetworkImageView.setVisibility(0);
        paramView.setText(localRoomInfo.content);
        localNetworkImageView.setImage(localRoomInfo.iconurl);
        return;
      }
      while (localRoomInfo.type != 2);
      localNetworkImageView.setVisibility(8);
      paramView.setText(Html.fromHtml(localRoomInfo.content));
    }

    public boolean areAllItemsEnabled()
    {
      return false;
    }

    public int getCount()
    {
      return this.items.size();
    }

    public Object getItem(int paramInt)
    {
      return Integer.valueOf(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
        paramView = this.inflater.inflate(this.resid, paramViewGroup, false);
      while (true)
      {
        bindView(paramView, paramInt);
        return paramView;
      }
    }

    public boolean isEnabled(int paramInt)
    {
      return false;
    }

    public void setData(DPObject paramDPObject)
    {
      int i = 0;
      this.items.clear();
      Object localObject1 = paramDPObject.getString("PayPolicy");
      Object localObject2 = paramDPObject.getString("RoomInfo");
      String str = paramDPObject.getString("RoomDetailInfo");
      if (!TextUtils.isEmpty(str))
      {
        localObject1 = new HotelRoomIntroductionActivity.RoomInfo(0, null, (String)localObject1 + " " + (String)localObject2 + "\n" + str);
        this.items.add(localObject1);
      }
      localObject1 = paramDPObject.getArray("PromoInfoList");
      if (localObject1 == null);
      while (true)
      {
        int j = 0;
        while (j < i)
        {
          localObject2 = localObject1[j].getString("Icon");
          str = localObject1[j].getString("Content");
          if (!TextUtils.isEmpty(str))
          {
            localObject2 = new HotelRoomIntroductionActivity.RoomInfo(1, (String)localObject2, str);
            this.items.add(localObject2);
          }
          j += 1;
        }
        i = localObject1.length;
      }
      localObject2 = paramDPObject.getString("CancelTitle");
      localObject1 = paramDPObject.getString("CancelContent");
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        paramDPObject = (DPObject)localObject1;
        if (!TextUtils.isEmpty((CharSequence)localObject2))
          paramDPObject = "<font color=#323232>" + (String)localObject2 + "</font><br/>" + (String)localObject1;
        paramDPObject = new HotelRoomIntroductionActivity.RoomInfo(2, null, paramDPObject);
        this.items.add(paramDPObject);
      }
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shopinfo.activity.HotelRoomIntroductionActivity
 * JD-Core Version:    0.6.0
 */
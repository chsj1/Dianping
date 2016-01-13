package com.dianping.hotel.shopinfo.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.Flipper;
import com.dianping.base.widget.FlipperAdapter;
import com.dianping.base.widget.NetworkPhotoView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NavigationDot;
import java.util.HashMap;
import java.util.Map;

public class HotelOverseaGuideActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int REGION_RESULT = 2;
  private GuideAdapter adapter;
  private FrameLayout flipperContainer;
  private GuideFlipper flipperView;
  private DPObject[] guideList;
  private boolean isFromList = false;
  private boolean locked = false;
  private NetworkPhotoView map;
  private int mapHeight;
  private Map<Integer, Integer> offsetMap = new HashMap();
  private MApiRequest request;
  private int titleBarHeight;
  private int toolBarHeight;
  private int windowHeight;

  private void sendRequest()
  {
    if (this.request != null)
      mapiService().abort(this.request, this, true);
    this.request = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/hotel/accommodationguidedetail.hotel").buildUpon().appendQueryParameter("cityid", String.valueOf(city().id())).toString(), CacheType.DISABLED);
    mapiService().exec(this.request, this);
  }

  private void setupView()
  {
    this.map = ((NetworkPhotoView)findViewById(R.id.map));
    this.flipperContainer = ((FrameLayout)findViewById(R.id.flipper));
    this.flipperView = new GuideFlipper(this);
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-1, -1);
    localLayoutParams.gravity = 80;
    this.flipperView.setLayoutParams(localLayoutParams);
    this.adapter = new GuideAdapter(this);
    this.flipperView.setAdapter(this.adapter);
    this.flipperContainer.addView(this.flipperView);
    this.isFromList = getIntent().getBooleanExtra("fromList", false);
  }

  public void onClick(View paramView)
  {
    int i = ((Integer)paramView.getTag()).intValue();
    if ((this.guideList != null) && (this.guideList.length > 0) && (i < this.guideList.length))
    {
      if (!(paramView instanceof Button))
        break label191;
      statisticsEvent("hotelpro5", "hotelpro5_regionhotel", this.guideList[i].getString("RegionName"), 0);
    }
    while (true)
    {
      Object localObject2 = this.guideList[i];
      paramView = new StringBuilder();
      paramView.append("dianping://newshoplist");
      Object localObject1 = ((DPObject)localObject2).getString("RegionID");
      localObject2 = ((DPObject)localObject2).getString("RegionName");
      i = Integer.valueOf((String)localObject1).intValue();
      if (!this.isFromList)
        break;
      paramView = new DPObject().edit().putInt("ID", i).putString("Name", (String)localObject2).generate();
      localObject1 = new Intent();
      ((Intent)localObject1).putExtra("result", paramView);
      ((Intent)localObject1).putExtra("type", 2);
      setResult(-1, (Intent)localObject1);
      finish();
      return;
      label191: statisticsEvent("hotelpro5", "hotelpro5_regionname", this.guideList[i].getString("RegionName"), 0);
    }
    paramView.append("?categoryid=60&regionid=" + i + "&placetype=2");
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView.toString())));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.hotel_oversea_guide);
    setupView();
    sendRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
      this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
    {
      this.request = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        this.guideList = paramMApiRequest.getArray("GuideDetailList");
        this.map.setImage(paramMApiRequest.getString("BannerUrl"));
        this.flipperView.setCurrentItem(Integer.valueOf(0));
        this.flipperView.enableNavigationDotView(this.guideList.length);
        this.flipperView.update();
        paramMApiRequest = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(paramMApiRequest);
        this.windowHeight = paramMApiRequest.heightPixels;
        paramMApiRequest = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(paramMApiRequest);
        this.toolBarHeight = paramMApiRequest.top;
        this.map.measure(0, this.windowHeight);
        this.mapHeight = this.map.getMeasuredHeight();
        this.titleBarHeight = ViewUtils.dip2px(this, 50.0F);
      }
    }
  }

  public class GuiNavigationDot extends NavigationDot
  {
    public GuiNavigationDot(Context arg2)
    {
      super();
    }

    protected void onDraw(Canvas paramCanvas)
    {
      paramCanvas.translate(getPaddingLeft(), getPaddingTop());
      int i = this.dot_width;
      int j = this.totalDot;
      int k = this.padding;
      int m = this.totalDot;
      j = (this.width - (i * j + k * (m - 1))) / 2;
      i = 0;
      if (i < this.totalDot)
      {
        if (this.currentDot == i);
        for (Bitmap localBitmap = this.dotPressed; ; localBitmap = this.dotNormal)
        {
          paramCanvas.drawBitmap(localBitmap, (this.dot_width + this.padding) * i + j, (getHeight() - this.dot_height) / 2, paint);
          i += 1;
          break;
        }
      }
    }
  }

  protected class GuideAdapter
    implements FlipperAdapter<Integer>
  {
    Context context;

    public GuideAdapter(Context arg2)
    {
      Object localObject;
      this.context = localObject;
    }

    private void addToCommentList(LinearLayout paramLinearLayout, String paramString, boolean paramBoolean)
    {
      LinearLayout localLinearLayout = new LinearLayout(this.context);
      localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
      localLinearLayout.setOrientation(0);
      TextView localTextView = new TextView(this.context);
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
      localTextView.setGravity(17);
      localLayoutParams.leftMargin = 45;
      localLayoutParams.topMargin = 25;
      localLayoutParams.gravity = 48;
      localTextView.setLayoutParams(localLayoutParams);
      localTextView.setText("●");
      localTextView.setTextColor(HotelOverseaGuideActivity.this.getResources().getColor(R.color.hotel_oversea_guide_point));
      localTextView.setTextSize(12.0F);
      localLinearLayout.addView(localTextView);
      localTextView = new TextView(this.context);
      localTextView.setGravity(48);
      localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
      localLayoutParams.leftMargin = 35;
      localLayoutParams.rightMargin = ViewUtils.dip2px(HotelOverseaGuideActivity.this, 15.0F);
      localLayoutParams.topMargin = 25;
      localTextView.setLayoutParams(localLayoutParams);
      if (!TextUtils.isEmpty(paramString))
        localTextView.setText(paramString);
      if (paramBoolean)
        localTextView.setTextColor(HotelOverseaGuideActivity.this.getResources().getColor(R.color.hotel_oversea_guide_good_comment));
      while (true)
      {
        localLinearLayout.addView(localTextView);
        paramLinearLayout.addView(localLinearLayout);
        return;
        localTextView.setTextColor(HotelOverseaGuideActivity.this.getResources().getColor(R.color.hotel_oversea_guide_bad_comment));
      }
    }

    private int calculateOffset(int paramInt)
    {
      int i = paramInt - (HotelOverseaGuideActivity.this.windowHeight - HotelOverseaGuideActivity.this.titleBarHeight - HotelOverseaGuideActivity.this.toolBarHeight - HotelOverseaGuideActivity.this.mapHeight + ViewUtils.dip2px(this.context, 10.0F));
      paramInt = i;
      if (i <= 0)
        paramInt = 0;
      return paramInt;
    }

    private void setupView(Integer paramInteger, View paramView, DPObject paramDPObject)
    {
      LinearLayout localLinearLayout = (LinearLayout)paramView.findViewById(R.id.flipper_content);
      localLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(localLinearLayout, paramInteger)
      {
        @TargetApi(16)
        public void onGlobalLayout()
        {
          if (this.val$flipperContent.getHeight() > 0)
          {
            int i = HotelOverseaGuideActivity.GuideAdapter.this.calculateOffset(this.val$flipperContent.getHeight());
            HotelOverseaGuideActivity.this.offsetMap.put(this.val$item, Integer.valueOf(i));
            FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)this.val$flipperContent.getLayoutParams();
            localLayoutParams.bottomMargin = (i * -1);
            this.val$flipperContent.setLayoutParams(localLayoutParams);
            if (Build.VERSION.SDK_INT < 16)
              this.val$flipperContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
          }
          else
          {
            return;
          }
          this.val$flipperContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
      });
      Object localObject = paramDPObject.getString("RegionNO");
      if (!TextUtils.isEmpty((CharSequence)localObject))
        ((TextView)localLinearLayout.findViewById(R.id.letter)).setText((CharSequence)localObject);
      localObject = paramDPObject.getString("RegionName");
      if (!TextUtils.isEmpty((CharSequence)localObject))
        ((TextView)localLinearLayout.findViewById(R.id.region_name)).setText((CharSequence)localObject);
      localObject = paramDPObject.getString("HotRate");
      if (!TextUtils.isEmpty((CharSequence)localObject))
        ((TextView)localLinearLayout.findViewById(R.id.hot_rate)).setText((String)localObject + "%");
      localObject = paramDPObject.getArray("LandMarks");
      StringBuilder localStringBuilder = new StringBuilder();
      int j = localObject.length;
      int i = 0;
      while (i < j)
      {
        String str = localObject[i].getString("Name").replace(";", "  ");
        localStringBuilder.append(str + " ");
        i += 1;
      }
      if (!TextUtils.isEmpty(localStringBuilder.toString()))
      {
        localLinearLayout.findViewById(R.id.landmark_content).setVisibility(0);
        localLinearLayout.findViewById(R.id.landmark_divider).setVisibility(0);
        ((TextView)localLinearLayout.findViewById(R.id.landmark)).setText(localStringBuilder.toString());
      }
      while (true)
      {
        paramView = (LinearLayout)paramView.findViewById(R.id.review_list);
        paramView.removeAllViews();
        localObject = paramDPObject.getStringArray("Advantage");
        if ((localObject == null) || (localObject.length <= 0))
          break;
        j = localObject.length;
        i = 0;
        while (i < j)
        {
          addToCommentList(paramView, localObject[i], true);
          i += 1;
        }
        localLinearLayout.findViewById(R.id.landmark_content).setVisibility(8);
        localLinearLayout.findViewById(R.id.landmark_divider).setVisibility(8);
      }
      localObject = paramDPObject.getStringArray("Disadvantage");
      if ((localObject != null) && (localObject.length > 0))
      {
        j = localObject.length;
        i = 0;
        while (i < j)
        {
          addToCommentList(paramView, localObject[i], false);
          i += 1;
        }
      }
      paramView = paramDPObject.getObject("Url");
      if ((paramView != null) && (!TextUtils.isEmpty(paramView.getString("Name"))))
      {
        paramView = paramView.getString("Name").replace("查看", "");
        ((TextView)localLinearLayout.findViewById(R.id.nearby)).setText(paramView);
      }
      paramView = (Button)localLinearLayout.findViewById(R.id.button);
      paramView.setTag(paramInteger);
      paramView.setOnClickListener(HotelOverseaGuideActivity.this);
      paramView = localLinearLayout.findViewById(R.id.title);
      paramView.setTag(paramInteger);
      paramView.setOnClickListener(HotelOverseaGuideActivity.this);
    }

    public Integer getNextItem(Integer paramInteger)
    {
      if (HotelOverseaGuideActivity.this.guideList == null);
      do
        return null;
      while (paramInteger.intValue() + 1 >= HotelOverseaGuideActivity.this.guideList.length);
      return Integer.valueOf(paramInteger.intValue() + 1);
    }

    public Integer getPreviousItem(Integer paramInteger)
    {
      if (paramInteger.intValue() > 0)
        return Integer.valueOf(paramInteger.intValue() - 1);
      return null;
    }

    @TargetApi(11)
    public View getView(Integer paramInteger, View paramView)
    {
      if ((paramInteger == null) || (paramInteger.intValue() < 0) || (HotelOverseaGuideActivity.this.guideList == null) || (HotelOverseaGuideActivity.this.guideList.length == 0))
        return null;
      paramView = HotelOverseaGuideActivity.this.guideList[paramInteger.intValue()];
      if (paramView == null)
        return null;
      View localView = HotelOverseaGuideActivity.this.getLayoutInflater().inflate(R.layout.hotel_oversea_guide_item, null, false);
      if (Build.VERSION.SDK_INT > 13)
      {
        localView.findViewById(R.id.divider_2).setLayerType(1, null);
        localView.findViewById(R.id.divider_1).setLayerType(1, null);
      }
      setupView(paramInteger, localView, paramView);
      return localView;
    }

    public void onMoved(Integer paramInteger1, Integer paramInteger2)
    {
    }

    public void onMoving(Integer paramInteger1, Integer paramInteger2)
    {
    }

    public void onTap(Integer paramInteger)
    {
    }

    public void recycleView(View paramView)
    {
    }
  }

  public class GuideFlipper extends Flipper<Integer>
  {
    public GuideFlipper(Context arg2)
    {
      super();
      this.gestureListener = new MyGestureListener();
      this.gestureDetector = new GestureDetector(this.gestureListener);
    }

    private void show(View paramView)
    {
      if (this.currentItem == null)
        break label7;
      label7: 
      do
        return;
      while (HotelOverseaGuideActivity.this.locked);
      HotelOverseaGuideActivity.access$702(HotelOverseaGuideActivity.this, true);
      LinearLayout localLinearLayout = (LinearLayout)paramView.findViewById(R.id.flipper_content);
      paramView = (String)paramView.getTag();
      int i = ((Integer)HotelOverseaGuideActivity.this.offsetMap.get(this.currentItem)).intValue();
      int j = ViewUtils.dip2px(getContext(), 25.0F);
      float f;
      if (paramView.equals("showAll"))
        f = (i + j) * -1;
      while (true)
      {
        TranslateAnimation localTranslateAnimation = new TranslateAnimation(0.0F, 0.0F, 0.0F, f);
        localTranslateAnimation.setDuration(200L);
        AnimationSet localAnimationSet = new AnimationSet(true);
        localAnimationSet.addAnimation(localTranslateAnimation);
        localAnimationSet.setAnimationListener(new Animation.AnimationListener(localLinearLayout, paramView, j, i)
        {
          public void onAnimationEnd(Animation paramAnimation)
          {
            this.val$v.clearAnimation();
            paramAnimation = (FrameLayout.LayoutParams)this.val$v.getLayoutParams();
            if (this.val$code.equals("showAll"));
            for (int i = this.val$dotPanelHeight; ; i = this.val$offSet * -1)
            {
              paramAnimation.bottomMargin = i;
              this.val$v.setLayoutParams(paramAnimation);
              HotelOverseaGuideActivity.access$702(HotelOverseaGuideActivity.this, false);
              return;
            }
          }

          public void onAnimationRepeat(Animation paramAnimation)
          {
          }

          public void onAnimationStart(Animation paramAnimation)
          {
          }
        });
        localLinearLayout.startAnimation(localAnimationSet);
        if (!paramView.equals("showAll"))
          break;
        HotelOverseaGuideActivity.this.statisticsEvent("hotelpro5", "hotelpro5_up", HotelOverseaGuideActivity.this.guideList[((Integer)this.currentItem).intValue()].getString("RegionName"), 0);
        return;
        f = i + j;
      }
    }

    public void enableNavigationDotView(int paramInt)
    {
      if ((paramInt > 1) && (paramInt > 1))
      {
        if (this.navigationDot == null)
        {
          this.navigationDot = new HotelOverseaGuideActivity.GuiNavigationDot(HotelOverseaGuideActivity.this, getContext());
          FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 25.0F));
          localLayoutParams.bottomMargin = 0;
          localLayoutParams.gravity = 81;
          this.navigationDot.setLayoutParams(localLayoutParams);
          this.navigationDot.setBackgroundColor(getResources().getColor(R.color.hotel_oversea_guide_dot_bg));
          addView(this.navigationDot);
        }
        this.navigationDot.setTotalDot(paramInt);
      }
    }

    public boolean moveToNext(boolean paramBoolean)
    {
      HotelOverseaGuideActivity.this.statisticsEvent("hotelpro5", "hotelpro5_slide", "", 0);
      return super.moveToNext(paramBoolean);
    }

    public boolean moveToPrevious(boolean paramBoolean)
    {
      HotelOverseaGuideActivity.this.statisticsEvent("hotelpro5", "hotelpro5_slide", "", 0);
      return super.moveToPrevious(paramBoolean);
    }

    protected void onScrollDown()
    {
      if ((this.currentView.getTag() != null) && (this.currentView.getTag().equals("showAll")))
      {
        this.currentView.setTag("showTop");
        show(this.currentView);
      }
    }

    protected void onScrollUp()
    {
      if ((this.currentView.getTag() == null) || (this.currentView.getTag().equals("showTop")))
      {
        this.currentView.setTag("showAll");
        show(this.currentView);
      }
    }

    protected boolean onScrollY(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      if ((paramFloat2 > 0.0F) && (Math.abs(paramFloat2) > Math.abs(paramFloat1) * 0.5D))
        onScrollUp();
      do
      {
        return true;
        if ((paramFloat2 >= 0.0F) || (Math.abs(paramFloat2) <= Math.abs(paramFloat1) * 0.5D))
          continue;
        onScrollDown();
        return true;
      }
      while (Math.abs(paramFloat1) <= 50.0F);
      return false;
    }

    protected void onTap()
    {
      if (this.currentView == null)
        return;
      if ((this.currentView.getTag() == null) || (this.currentView.getTag().equals("showTop")))
        this.currentView.setTag("showAll");
      while (true)
      {
        show(this.currentView);
        return;
        this.currentView.setTag("showTop");
      }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener
    {
      MyGestureListener()
      {
      }

      private boolean isCanFling(float paramFloat1, float paramFloat2)
      {
        if ((paramFloat2 > 0.0F) && (Math.abs(paramFloat2) > Math.abs(paramFloat1) * 0.5D));
        do
          return false;
        while ((paramFloat2 < 0.0F) && (Math.abs(paramFloat2) > Math.abs(paramFloat1) * 0.5D));
        return true;
      }

      public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
      {
        if (isCanFling(paramFloat1, paramFloat2))
          HotelOverseaGuideActivity.GuideFlipper.this.onFling(paramFloat1);
        return true;
      }

      public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
      {
        if ((HotelOverseaGuideActivity.GuideFlipper.this.previousItem == null) && (HotelOverseaGuideActivity.GuideFlipper.this.nextItem == null))
          HotelOverseaGuideActivity.GuideFlipper.access$1002(HotelOverseaGuideActivity.GuideFlipper.this, false);
        do
        {
          return true;
          HotelOverseaGuideActivity.GuideFlipper.access$1102(HotelOverseaGuideActivity.GuideFlipper.this, true);
        }
        while (HotelOverseaGuideActivity.GuideFlipper.this.onScrollY(paramMotionEvent1, paramMotionEvent2, paramFloat1, paramFloat2));
        HotelOverseaGuideActivity.GuideFlipper.this.onScrollX(paramMotionEvent1, paramMotionEvent2, paramFloat1);
        return true;
      }

      public void onShowPress(MotionEvent paramMotionEvent)
      {
        HotelOverseaGuideActivity.GuideFlipper.access$1302(HotelOverseaGuideActivity.GuideFlipper.this, 0);
        super.onShowPress(paramMotionEvent);
      }

      public boolean onSingleTapUp(MotionEvent paramMotionEvent)
      {
        HotelOverseaGuideActivity.GuideFlipper.this.onTap();
        return true;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shopinfo.activity.HotelOverseaGuideActivity
 * JD-Core Version:    0.6.0
 */
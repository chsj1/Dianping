package com.dianping.hotel.shopinfo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaLoadActivity;
import com.dianping.base.shoplist.data.AbstractDataSource;
import com.dianping.base.shoplist.data.DataSource.OnDataChangeListener;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.List<Lcom.dianping.hotel.shopinfo.activity.HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo;>;
import java.util.List<Lcom.dianping.hotel.shopinfo.activity.HotelTogetherRoomTypeListActivity.RoomTypeInfo;>;

public class HotelTogetherRoomTypeListActivity extends NovaLoadActivity
  implements DataSource.OnDataChangeListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final boolean DEBUG = false;
  private static final int REQUEST_CODE_BOOKROOM = 3;
  private static final int REQUEST_CODE_ROOMINTRO = 2;
  private static final int REQUEST_CODE_TIME = 1;
  private static final SimpleDateFormat SDF = new SimpleDateFormat("MM-dd");
  private static final SimpleDateFormat SDF2 = new SimpleDateFormat("yyyyMMdd");
  private static final String TAG = "HotelTogetherRoomTypeListActivity";
  private int MAX_DEAL_COUNT = 3;
  RoomTableViewAdapter adapter;
  private RoomTypeDataSource ds = new RoomTypeDataSource();
  private long endtime = this.starttime + 86400000L;
  private View generalPromoInfoView;
  private RelativeLayout hotel_layout_time;
  private TextView hotel_tv_pick_time;
  ArrayList<LinkedHashMap<String, ArrayList<RoomTypeInfo>>> lists = new ArrayList();
  private View mAnnounceView;
  private View mDivider;
  private RoomTypeListAdapter mListAdapter;
  private MApiRequest mRequest;
  private ListView mRoomTypeList;
  private TextView mSource;
  private TextView mTip;
  public String otaid;
  public String shopid;
  private long starttime = System.currentTimeMillis();
  View viewLayoutFooter;
  View viewLayoutHeader;

  private String getDate(long paramLong)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String[] arrayOfString = SDF.format(Long.valueOf(paramLong)).split("-");
    localStringBuilder.append(Integer.valueOf(arrayOfString[0]).intValue()).append("月");
    localStringBuilder.append(Integer.valueOf(arrayOfString[1]).intValue()).append("日");
    return localStringBuilder.toString();
  }

  private void requestHotelRoomTypeList(boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
    localStringBuilder.append("roomprice.hotel?");
    localStringBuilder.append("shopid=").append(this.shopid);
    localStringBuilder.append("&otaid=").append(this.otaid);
    localStringBuilder.append("&checkindate=").append(this.starttime);
    localStringBuilder.append("&checkoutdate=").append(this.endtime);
    long l = getLongParam("trace_id");
    localStringBuilder.append("&traceid=").append(l);
    GAUserInfo localGAUserInfo = new GAUserInfo();
    localGAUserInfo.query_id = getStringParam("query_id");
    localGAUserInfo.title = (l + "");
    GAHelper.instance().setGAPageName("roomprice");
    GAHelper.instance().contextStatisticsEvent(this, "hotel_order_trace", localGAUserInfo, "view");
    this.mRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    if (paramBoolean)
      mapiCacheService().remove(this.mRequest);
    mapiService().exec(this.mRequest, this);
  }

  private void setupRightViewItem(DPObject paramDPObject)
  {
    getTitleBar().removeAllRightViewItem();
    if ((paramDPObject == null) || (TextUtils.isEmpty(paramDPObject.getString("OrderListUrl"))))
      return;
    getTitleBar().addRightViewItem("我的订单", "roomlist_myorder", new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        HotelTogetherRoomTypeListActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.val$obj.getString("OrderListUrl"))));
      }
    });
  }

  private void updateDateView()
  {
    this.hotel_tv_pick_time = ((TextView)this.viewLayoutHeader.findViewById(R.id.hotel_tv_pick_time));
    this.hotel_tv_pick_time.setText("入住 " + getDate(this.starttime) + " - 退房" + getDate(this.endtime));
  }

  // ERROR //
  private void updateViews(DPObject paramDPObject)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull +50 -> 51
    //   4: aload_0
    //   5: invokevirtual 303	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:getTitleBar	()Lcom/dianping/base/widget/TitleBar;
    //   8: ldc_w 364
    //   11: invokevirtual 367	com/dianping/base/widget/TitleBar:setTitle	(Ljava/lang/CharSequence;)V
    //   14: aload_0
    //   15: getfield 369	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mAnnounceView	Landroid/view/View;
    //   18: bipush 8
    //   20: invokevirtual 373	android/view/View:setVisibility	(I)V
    //   23: aload_0
    //   24: getfield 375	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mTip	Landroid/widget/TextView;
    //   27: bipush 8
    //   29: invokevirtual 376	android/widget/TextView:setVisibility	(I)V
    //   32: aload_0
    //   33: getfield 378	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mDivider	Landroid/view/View;
    //   36: bipush 8
    //   38: invokevirtual 373	android/view/View:setVisibility	(I)V
    //   41: aload_0
    //   42: getfield 380	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mSource	Landroid/widget/TextView;
    //   45: bipush 8
    //   47: invokevirtual 376	android/widget/TextView:setVisibility	(I)V
    //   50: return
    //   51: iconst_1
    //   52: istore 7
    //   54: aload_1
    //   55: ldc_w 382
    //   58: invokevirtual 315	com/dianping/archive/DPObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   61: astore_2
    //   62: aload_2
    //   63: invokestatic 321	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   66: ifne +382 -> 448
    //   69: new 384	org/json/JSONArray
    //   72: dup
    //   73: aload_2
    //   74: invokespecial 385	org/json/JSONArray:<init>	(Ljava/lang/String;)V
    //   77: astore_2
    //   78: aload_2
    //   79: invokevirtual 388	org/json/JSONArray:length	()I
    //   82: ifle +353 -> 435
    //   85: aload_2
    //   86: iconst_0
    //   87: invokevirtual 392	org/json/JSONArray:getJSONObject	(I)Lorg/json/JSONObject;
    //   90: astore_2
    //   91: aload_2
    //   92: ldc_w 393
    //   95: invokevirtual 398	org/json/JSONObject:optString	(Ljava/lang/String;)Ljava/lang/String;
    //   98: astore_3
    //   99: aload_2
    //   100: ldc_w 400
    //   103: invokevirtual 398	org/json/JSONObject:optString	(Ljava/lang/String;)Ljava/lang/String;
    //   106: astore 4
    //   108: aload_2
    //   109: ldc_w 402
    //   112: invokevirtual 405	org/json/JSONObject:optLong	(Ljava/lang/String;)J
    //   115: lstore 9
    //   117: aload_2
    //   118: ldc_w 407
    //   121: invokevirtual 405	org/json/JSONObject:optLong	(Ljava/lang/String;)J
    //   124: lstore 11
    //   126: invokestatic 410	com/dianping/util/DateUtil:currentTimeMillis	()J
    //   129: lstore 13
    //   131: ldc2_w 411
    //   134: lload 9
    //   136: lmul
    //   137: lload 13
    //   139: lcmp
    //   140: ifgt +289 -> 429
    //   143: ldc2_w 411
    //   146: lload 11
    //   148: lmul
    //   149: lload 13
    //   151: lcmp
    //   152: ifle +277 -> 429
    //   155: aload_0
    //   156: getfield 369	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mAnnounceView	Landroid/view/View;
    //   159: getstatic 415	com/dianping/v1/R$id:tv_announce	I
    //   162: invokevirtual 345	android/view/View:findViewById	(I)Landroid/view/View;
    //   165: checkcast 347	android/widget/TextView
    //   168: aload_3
    //   169: invokevirtual 359	android/widget/TextView:setText	(Ljava/lang/CharSequence;)V
    //   172: aload_0
    //   173: getfield 369	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mAnnounceView	Landroid/view/View;
    //   176: iconst_2
    //   177: anewarray 172	java/lang/String
    //   180: dup
    //   181: iconst_0
    //   182: aload 4
    //   184: aastore
    //   185: dup
    //   186: iconst_1
    //   187: aload_3
    //   188: aastore
    //   189: invokevirtual 419	android/view/View:setTag	(Ljava/lang/Object;)V
    //   192: aload_0
    //   193: ldc_w 421
    //   196: ldc_w 423
    //   199: aload_3
    //   200: iconst_0
    //   201: invokevirtual 427	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:statisticsEvent	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V
    //   204: iload 7
    //   206: ifne +248 -> 454
    //   209: aload_0
    //   210: getfield 369	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mAnnounceView	Landroid/view/View;
    //   213: bipush 8
    //   215: invokevirtual 373	android/view/View:setVisibility	(I)V
    //   218: aload_1
    //   219: ldc_w 429
    //   222: invokevirtual 315	com/dianping/archive/DPObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   225: astore_2
    //   226: aload_2
    //   227: invokestatic 321	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   230: ifne +235 -> 465
    //   233: aload_0
    //   234: getfield 375	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mTip	Landroid/widget/TextView;
    //   237: iconst_0
    //   238: invokevirtual 376	android/widget/TextView:setVisibility	(I)V
    //   241: aload_0
    //   242: getfield 375	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mTip	Landroid/widget/TextView;
    //   245: aload_2
    //   246: invokevirtual 359	android/widget/TextView:setText	(Ljava/lang/CharSequence;)V
    //   249: aload_1
    //   250: ldc_w 431
    //   253: invokevirtual 435	com/dianping/archive/DPObject:getArray	(Ljava/lang/String;)[Lcom/dianping/archive/DPObject;
    //   256: astore_3
    //   257: aload_3
    //   258: ifnull +10 -> 268
    //   261: aload_3
    //   262: astore_2
    //   263: aload_3
    //   264: arraylength
    //   265: ifgt +18 -> 283
    //   268: iconst_1
    //   269: anewarray 312	com/dianping/archive/DPObject
    //   272: astore_2
    //   273: aload_2
    //   274: iconst_0
    //   275: aload_1
    //   276: ldc_w 437
    //   279: invokevirtual 441	com/dianping/archive/DPObject:getObject	(Ljava/lang/String;)Lcom/dianping/archive/DPObject;
    //   282: aastore
    //   283: aload_0
    //   284: getstatic 444	com/dianping/v1/R$id:promo_list	I
    //   287: invokevirtual 445	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:findViewById	(I)Landroid/view/View;
    //   290: checkcast 447	android/widget/LinearLayout
    //   293: astore_3
    //   294: aload_3
    //   295: invokevirtual 450	android/widget/LinearLayout:removeAllViews	()V
    //   298: aload_2
    //   299: ifnull +178 -> 477
    //   302: aload_2
    //   303: arraylength
    //   304: ifle +173 -> 477
    //   307: aload_2
    //   308: arraylength
    //   309: istore 8
    //   311: iconst_0
    //   312: istore 7
    //   314: iload 7
    //   316: iload 8
    //   318: if_icmpge +159 -> 477
    //   321: aload_2
    //   322: iload 7
    //   324: aaload
    //   325: astore 4
    //   327: aload 4
    //   329: ifnull +91 -> 420
    //   332: aload_0
    //   333: invokevirtual 454	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:getLayoutInflater	()Landroid/view/LayoutInflater;
    //   336: getstatic 459	com/dianping/v1/R$layout:hotel_promo_info_explain	I
    //   339: aconst_null
    //   340: invokevirtual 465	android/view/LayoutInflater:inflate	(ILandroid/view/ViewGroup;)Landroid/view/View;
    //   343: astore 5
    //   345: aload 5
    //   347: getstatic 468	com/dianping/v1/R$id:general_promo_info_content	I
    //   350: invokevirtual 345	android/view/View:findViewById	(I)Landroid/view/View;
    //   353: astore 6
    //   355: aload 6
    //   357: getstatic 471	com/dianping/v1/R$id:ota_promo	I
    //   360: invokevirtual 345	android/view/View:findViewById	(I)Landroid/view/View;
    //   363: checkcast 347	android/widget/TextView
    //   366: aload 4
    //   368: ldc_w 473
    //   371: invokevirtual 315	com/dianping/archive/DPObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   374: invokevirtual 359	android/widget/TextView:setText	(Ljava/lang/CharSequence;)V
    //   377: aload 6
    //   379: getstatic 476	com/dianping/v1/R$id:ota_promo_explain	I
    //   382: invokevirtual 345	android/view/View:findViewById	(I)Landroid/view/View;
    //   385: checkcast 347	android/widget/TextView
    //   388: aload 4
    //   390: ldc_w 478
    //   393: invokevirtual 315	com/dianping/archive/DPObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   396: invokevirtual 359	android/widget/TextView:setText	(Ljava/lang/CharSequence;)V
    //   399: aload 5
    //   401: new 11	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity$1
    //   404: dup
    //   405: aload_0
    //   406: aload 4
    //   408: invokespecial 479	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity$1:<init>	(Lcom/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity;Lcom/dianping/archive/DPObject;)V
    //   411: invokevirtual 483	android/view/View:setOnClickListener	(Landroid/view/View$OnClickListener;)V
    //   414: aload_3
    //   415: aload 5
    //   417: invokevirtual 487	android/widget/LinearLayout:addView	(Landroid/view/View;)V
    //   420: iload 7
    //   422: iconst_1
    //   423: iadd
    //   424: istore 7
    //   426: goto -112 -> 314
    //   429: iconst_0
    //   430: istore 7
    //   432: goto -228 -> 204
    //   435: iconst_0
    //   436: istore 7
    //   438: goto -234 -> 204
    //   441: astore_2
    //   442: iconst_0
    //   443: istore 7
    //   445: goto -241 -> 204
    //   448: iconst_0
    //   449: istore 7
    //   451: goto -247 -> 204
    //   454: aload_0
    //   455: getfield 369	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mAnnounceView	Landroid/view/View;
    //   458: iconst_0
    //   459: invokevirtual 373	android/view/View:setVisibility	(I)V
    //   462: goto -244 -> 218
    //   465: aload_0
    //   466: getfield 375	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mTip	Landroid/widget/TextView;
    //   469: bipush 8
    //   471: invokevirtual 376	android/widget/TextView:setVisibility	(I)V
    //   474: goto -225 -> 249
    //   477: new 157	java/lang/StringBuilder
    //   480: dup
    //   481: invokespecial 158	java/lang/StringBuilder:<init>	()V
    //   484: astore_2
    //   485: aload_1
    //   486: ldc_w 489
    //   489: invokevirtual 315	com/dianping/archive/DPObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   492: astore_3
    //   493: aload_1
    //   494: ldc_w 491
    //   497: invokevirtual 315	com/dianping/archive/DPObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   500: astore 4
    //   502: aload_3
    //   503: invokestatic 321	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   506: istore 15
    //   508: aload 4
    //   510: invokestatic 321	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   513: istore 16
    //   515: iload 15
    //   517: ifne +22 -> 539
    //   520: aload_2
    //   521: aload_3
    //   522: invokevirtual 194	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   525: pop
    //   526: iload 16
    //   528: ifne +11 -> 539
    //   531: aload_2
    //   532: ldc_w 493
    //   535: invokevirtual 194	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   538: pop
    //   539: iload 16
    //   541: ifne +35 -> 576
    //   544: aload_2
    //   545: new 157	java/lang/StringBuilder
    //   548: dup
    //   549: invokespecial 158	java/lang/StringBuilder:<init>	()V
    //   552: ldc_w 495
    //   555: invokevirtual 194	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   558: aload 4
    //   560: invokevirtual 194	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   563: ldc_w 497
    //   566: invokevirtual 194	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   569: invokevirtual 200	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   572: invokevirtual 194	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   575: pop
    //   576: iload 15
    //   578: ifeq +46 -> 624
    //   581: iload 16
    //   583: ifeq +41 -> 624
    //   586: aload_0
    //   587: getfield 378	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mDivider	Landroid/view/View;
    //   590: bipush 8
    //   592: invokevirtual 373	android/view/View:setVisibility	(I)V
    //   595: aload_0
    //   596: getfield 380	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mSource	Landroid/widget/TextView;
    //   599: bipush 8
    //   601: invokevirtual 376	android/widget/TextView:setVisibility	(I)V
    //   604: iload 16
    //   606: ifne +12 -> 618
    //   609: aload_0
    //   610: invokespecial 498	com/dianping/base/app/NovaLoadActivity:getTitleBar	()Lcom/dianping/base/widget/TitleBar;
    //   613: aload 4
    //   615: invokevirtual 501	com/dianping/base/widget/TitleBar:setSubTitle	(Ljava/lang/CharSequence;)V
    //   618: aload_0
    //   619: aload_1
    //   620: invokespecial 503	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:setupRightViewItem	(Lcom/dianping/archive/DPObject;)V
    //   623: return
    //   624: aload_0
    //   625: getfield 378	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mDivider	Landroid/view/View;
    //   628: iconst_0
    //   629: invokevirtual 373	android/view/View:setVisibility	(I)V
    //   632: aload_0
    //   633: getfield 380	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mSource	Landroid/widget/TextView;
    //   636: iconst_0
    //   637: invokevirtual 376	android/widget/TextView:setVisibility	(I)V
    //   640: aload_0
    //   641: getfield 380	com/dianping/hotel/shopinfo/activity/HotelTogetherRoomTypeListActivity:mSource	Landroid/widget/TextView;
    //   644: aload_2
    //   645: invokevirtual 200	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   648: invokestatic 509	android/text/Html:fromHtml	(Ljava/lang/String;)Landroid/text/Spanned;
    //   651: invokevirtual 359	android/widget/TextView:setText	(Ljava/lang/CharSequence;)V
    //   654: goto -50 -> 604
    //   657: astore_2
    //   658: goto -216 -> 442
    //
    // Exception table:
    //   from	to	target	type
    //   69	78	441	org/json/JSONException
    //   78	131	657	org/json/JSONException
    //   155	204	657	org/json/JSONException
  }

  protected View getLoadingView()
  {
    return getLayoutInflater().inflate(R.layout.loading_item_fullscreen, null);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (1 == paramInt1)
      if (paramIntent != null)
      {
        long l1 = paramIntent.getLongExtra("checkin_time", this.starttime);
        long l2 = paramIntent.getLongExtra("checkout_time", this.endtime);
        if ((l1 != this.starttime) || (l2 != this.endtime))
        {
          this.starttime = l1;
          this.endtime = l2;
          updateDateView();
          this.ds.reset(true);
          this.ds.reload(true);
          paramIntent = new Intent();
          paramIntent.putExtra("checkin_time", this.starttime);
          paramIntent.putExtra("checkout_time", this.endtime);
          setResult(-1, paramIntent);
        }
      }
    do
      while (true)
      {
        return;
        if (2 != paramInt1)
          break;
        if (paramInt2 == -1)
          return;
      }
    while (3 != paramInt1);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null)
      paramBundle = getIntent().getData();
    try
    {
      String str = paramBundle.getQueryParameter("checkindate");
      if (!TextUtils.isEmpty(str))
        this.starttime = Long.valueOf(str).longValue();
      str = paramBundle.getQueryParameter("checkoutdate");
      if (!TextUtils.isEmpty(str))
        this.endtime = Long.valueOf(str).longValue();
      label69: this.shopid = paramBundle.getQueryParameter("shopid");
      for (this.otaid = paramBundle.getQueryParameter("otaid"); ; this.otaid = paramBundle.getString("otaid"))
      {
        updateViews(null);
        updateDateView();
        this.ds.addDataChangeListener(this);
        return;
        this.starttime = paramBundle.getLong("checkindate", this.starttime);
        this.endtime = paramBundle.getLong("checkoutdate", this.endtime);
        this.shopid = paramBundle.getString("shopid");
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      break label69;
    }
  }

  public void onDataChanged(int paramInt)
  {
    if (paramInt == 1)
    {
      this.mListAdapter.setData(this.ds);
      if (this.ds.status() != 3)
        break label33;
      showError(null);
    }
    label33: 
    do
      return;
    while ((this.ds.status() == 1) || ((this.ds.status() != 2) && (this.ds.status() != 0)));
    showContent();
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.ds.removeDataChangeListener(this);
    if (this.mRequest != null)
    {
      mapiService().abort(this.mRequest, this, true);
      this.mRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    String str;
    if (this.mRequest == paramMApiRequest)
    {
      this.mRequest = null;
      str = "错误";
      if ((paramMApiResponse == null) || (!(paramMApiResponse.error() instanceof SimpleMsg)))
        break label65;
      paramMApiResponse = paramMApiResponse.message();
      paramMApiRequest = str;
      if (paramMApiResponse != null)
      {
        paramMApiRequest = paramMApiResponse.toString();
        paramMApiResponse.flag();
      }
    }
    while (true)
    {
      this.ds.setError(paramMApiRequest);
      return;
      label65: paramMApiRequest = str;
      if (paramMApiResponse == null)
        continue;
      paramMApiRequest = str;
      if (!(paramMApiResponse.error() instanceof UnknownHostException))
        continue;
      paramMApiRequest = "网络错误，请重试";
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mRequest == paramMApiRequest)
    {
      this.mRequest = null;
      String str = "错误";
      paramMApiResponse = paramMApiResponse.result();
      paramMApiRequest = str;
      if (paramMApiResponse != null)
      {
        paramMApiRequest = str;
        if ((paramMApiResponse instanceof DPObject))
        {
          paramMApiRequest = ((DPObject)paramMApiResponse).getString("ErrorMsg");
          if (paramMApiRequest == null)
            break label63;
        }
      }
      this.ds.setError(paramMApiRequest);
    }
    else
    {
      return;
    }
    label63: this.ds.result = ((DPObject)paramMApiResponse);
    if (this.ds.getRoomTypeCount() <= 0)
      this.ds.setEmpty("所选日期暂无报价|你可以修改入住时间试试");
    while (true)
    {
      updateViews((DPObject)paramMApiResponse);
      return;
      this.ds.setSuccess();
    }
  }

  public void onResume()
  {
    super.onResume();
    if (this.ds.status() == 3)
      showError(null);
    do
      return;
    while ((this.ds.status() == 1) || (this.ds.status() == 2) || (this.ds.status() != 0));
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putLong("checkindate", this.starttime);
    paramBundle.putLong("checkoutdate", this.endtime);
    paramBundle.putString("shopid", this.shopid);
    paramBundle.putString("otaid", this.otaid);
  }

  protected void reloadContent()
  {
    showLoading(null);
    this.ds.reload(true);
  }

  protected void setupView()
  {
    super.setContentView(R.layout.hotel_together_roomtype_list);
    this.viewLayoutHeader = LayoutInflater.from(this).inflate(R.layout.hotel_roomlist_layout_header, null);
    this.mAnnounceView = this.viewLayoutHeader.findViewById(R.id.hotel_roomtype_list_announce);
    this.mAnnounceView.findViewById(R.id.arrow).setVisibility(8);
    this.mAnnounceView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = paramView.getTag();
        if ((paramView != null) && ((paramView instanceof String[])) && (((String[])(String[])paramView).length == 2));
        try
        {
          HotelTogetherRoomTypeListActivity.this.startActivity(((String[])(String[])paramView)[0]);
          HotelTogetherRoomTypeListActivity.this.statisticsEvent("roominfo5", "roominfo5_banner_click", ((String[])(String[])paramView)[1], 0);
          return;
        }
        catch (android.content.ActivityNotFoundException paramView)
        {
        }
      }
    });
    this.hotel_layout_time = ((RelativeLayout)this.viewLayoutHeader.findViewById(R.id.hotel_layout_time));
    4 local4 = new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        HotelTogetherRoomTypeListActivity.this.startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://hotelbookingpicktime?checkin_time=" + HotelTogetherRoomTypeListActivity.this.starttime + "&checkout_time=" + HotelTogetherRoomTypeListActivity.this.endtime)), 1);
        HotelTogetherRoomTypeListActivity.this.statisticsEvent("roominfo5", "roominfo5_hoteldate", null, 0);
      }
    };
    this.hotel_layout_time.setOnClickListener(local4);
    this.generalPromoInfoView = this.viewLayoutHeader.findViewById(R.id.hotel_general_promo_info);
    this.generalPromoInfoView.setPadding(ViewUtils.dip2px(getApplicationContext(), 12.0F), 0, 0, 0);
    this.viewLayoutFooter = LayoutInflater.from(this).inflate(R.layout.hotel_roomlist_layout_footer, null);
    this.mTip = ((TextView)this.viewLayoutFooter.findViewById(R.id.hotel_roomtype_list_tips_tip));
    this.mDivider = this.viewLayoutFooter.findViewById(R.id.hotel_roomtype_list_tips_divider);
    this.mSource = ((TextView)this.viewLayoutFooter.findViewById(R.id.hotel_roomtype_list_tips_source));
    this.mRoomTypeList = ((ListView)findViewById(R.id.hotel_roomtype_list_typelist));
    this.mListAdapter = new RoomTypeListAdapter(this, R.layout.hotel_together_roomtype_list_item);
    this.mRoomTypeList.addHeaderView(this.viewLayoutHeader, null, false);
    this.mRoomTypeList.addFooterView(this.viewLayoutFooter, null, false);
    this.mRoomTypeList.setAdapter(this.mListAdapter);
    this.mRoomTypeList.setDescendantFocusability(393216);
  }

  private static class GroupedRoomTypeDo
  {
    public final int DisplayCount;
    public final int[] RoomIndexList;
    public final String RoomTypeName;

    public GroupedRoomTypeDo(String paramString, int paramInt, int[] paramArrayOfInt)
    {
      this.RoomTypeName = paramString;
      this.DisplayCount = paramInt;
      this.RoomIndexList = paramArrayOfInt;
    }
  }

  private static class PromoInfoDo
  {
    String Content;
    String Icon;

    public PromoInfoDo(String paramString1, String paramString2)
    {
      this.Icon = paramString1;
      this.Content = paramString2;
    }
  }

  class RoomTableViewAdapter extends BasicAdapter
  {
    private int displayCount = 0;
    int groupIndex = 0;
    private String groupName;
    private ArrayList<HotelTogetherRoomTypeListActivity.RoomTypeInfo> infos;
    private int intAddCount = 0;
    private boolean isExpand = false;
    private int totalCount = 0;

    RoomTableViewAdapter()
    {
    }

    public int getCount()
    {
      if (this.infos != null)
      {
        int i = this.infos.size();
        if ((i > HotelTogetherRoomTypeListActivity.this.MAX_DEAL_COUNT) || (this.intAddCount > this.displayCount))
        {
          if (this.isExpand)
            return i + 1;
          return this.displayCount + 1;
        }
        return i;
      }
      return 0;
    }

    public Object getItem(int paramInt)
    {
      return this.infos.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((this.isExpand) && (paramInt == this.totalCount))
      {
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.tuan_shop_display_more, paramViewGroup, false);
        paramView.setClickable(true);
        paramViewGroup = (TextView)paramView.findViewById(R.id.display_deal_count);
        paramViewGroup.setText("收起报价");
        localObject = HotelTogetherRoomTypeListActivity.this.getResources().getDrawable(R.drawable.ic_arrow_up_black);
        ((Drawable)localObject).setBounds(0, 0, ((Drawable)localObject).getMinimumWidth(), ((Drawable)localObject).getMinimumHeight());
        paramViewGroup.setCompoundDrawables(null, null, (Drawable)localObject, null);
        paramView.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            HotelTogetherRoomTypeListActivity.RoomTableViewAdapter.access$702(HotelTogetherRoomTypeListActivity.RoomTableViewAdapter.this, false);
            HotelTogetherRoomTypeListActivity.RoomTableViewAdapter.access$802(HotelTogetherRoomTypeListActivity.RoomTableViewAdapter.this, 0);
            HotelTogetherRoomTypeListActivity.RoomTableViewAdapter.this.notifyDataSetChanged();
          }
        });
        return paramView;
      }
      Object localObject = (HotelTogetherRoomTypeListActivity.RoomTypeInfo)this.infos.get(paramInt);
      if ((!this.isExpand) && (this.intAddCount == this.displayCount))
      {
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.tuan_shop_display_more, paramViewGroup, false);
        paramView.setClickable(true);
        this.intAddCount += 1;
        paramViewGroup = (TextView)paramView.findViewById(R.id.display_deal_count);
        paramViewGroup.setText("查看该房型全部报价");
        localObject = HotelTogetherRoomTypeListActivity.this.getResources().getDrawable(R.drawable.ic_arrow_down_black);
        ((Drawable)localObject).setBounds(0, 0, ((Drawable)localObject).getMinimumWidth(), ((Drawable)localObject).getMinimumHeight());
        paramViewGroup.setCompoundDrawables(null, null, (Drawable)localObject, null);
        paramView.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            HotelTogetherRoomTypeListActivity.RoomTableViewAdapter.access$702(HotelTogetherRoomTypeListActivity.RoomTableViewAdapter.this, true);
            HotelTogetherRoomTypeListActivity.RoomTableViewAdapter.access$802(HotelTogetherRoomTypeListActivity.RoomTableViewAdapter.this, 0);
            HotelTogetherRoomTypeListActivity.RoomTableViewAdapter.this.notifyDataSetChanged();
          }
        });
        return paramView;
      }
      if (localObject != null)
      {
        if ((paramView != null) && ((paramView instanceof TogetherRoomTypeListItem)))
        {
          paramView = (TogetherRoomTypeListItem)paramView;
          paramView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
          paramView.setParams(HotelTogetherRoomTypeListActivity.this.shopid, HotelTogetherRoomTypeListActivity.this.starttime, HotelTogetherRoomTypeListActivity.this.endtime, HotelTogetherRoomTypeListActivity.this.otaid);
          paramView.setRoomTypeInfo((HotelTogetherRoomTypeListActivity.RoomTypeInfo)localObject, this.groupName);
          paramView.hotel_roomtype_listitem_icon.setOnClickListener(new View.OnClickListener(paramInt)
          {
            public void onClick(View paramView)
            {
              Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hotelroomintroduction"));
              DPObject localDPObject = new DPObject();
              paramView = HotelTogetherRoomTypeListActivity.this.ds.getGroupedRoomTypeById(HotelTogetherRoomTypeListActivity.RoomTableViewAdapter.this.groupIndex).RoomIndexList;
              HotelTogetherRoomTypeListActivity.RoomTypeInfo localRoomTypeInfo = (HotelTogetherRoomTypeListActivity.RoomTypeInfo)HotelTogetherRoomTypeListActivity.RoomTypeDataSource.access$900(HotelTogetherRoomTypeListActivity.this.ds).get(paramView[this.val$position]);
              String str = localRoomTypeInfo.roomInfo;
              paramView = str;
              if (str == null)
                paramView = "";
              localIntent.putExtra("roomdetail", localDPObject.edit().putString("RoomTypeName", localRoomTypeInfo.roomTypeName).putString("RoomInfo", paramView).putInt("Remains", localRoomTypeInfo.remains).putInt("Status", localRoomTypeInfo.status).putString("PriceText", localRoomTypeInfo.priceText).putString("Tax", localRoomTypeInfo.tax).putString("ReFund", localRoomTypeInfo.reFund).putString("BookingUrl", localRoomTypeInfo.bookingUrl).putString("PayPolicy", localRoomTypeInfo.payPolicy).putStringArray("ImageList", localRoomTypeInfo.imageList).putString("CancelTitle", localRoomTypeInfo.cancelTitle).putString("CancelContent", localRoomTypeInfo.cancelContent).putString("RoomDetailInfo", localRoomTypeInfo.roomDetailInfo).generate());
              localIntent.putExtra("starttime", HotelTogetherRoomTypeListActivity.this.starttime);
              localIntent.putExtra("endtime", HotelTogetherRoomTypeListActivity.this.endtime);
              localIntent.putExtra("shopid", HotelTogetherRoomTypeListActivity.this.shopid);
              localIntent.putExtra("otaid", HotelTogetherRoomTypeListActivity.this.otaid);
              HotelTogetherRoomTypeListActivity.this.startActivityForResult(localIntent, 2);
              HotelTogetherRoomTypeListActivity.this.statisticsEvent("roominfo5", "roominfo5_detail", HotelTogetherRoomTypeListActivity.this.otaid, 0);
            }
          });
          paramView.activity = ((Activity)paramViewGroup.getContext());
          if (((HotelTogetherRoomTypeListActivity.RoomTypeInfo)localObject).status == 0)
            break label408;
          paramView.setClickable(true);
          paramView.setOnClickListener(new View.OnClickListener((HotelTogetherRoomTypeListActivity.RoomTypeInfo)localObject)
          {
            public void onClick(View paramView)
            {
              paramView = Uri.parse("dianping://hotelbookingweb");
              Uri localUri = Uri.parse(this.val$info.bookingUrl).buildUpon().appendQueryParameter("shopId", HotelTogetherRoomTypeListActivity.this.shopid).appendQueryParameter("startDate", HotelTogetherRoomTypeListActivity.SDF2.format(Long.valueOf(HotelTogetherRoomTypeListActivity.this.starttime))).appendQueryParameter("endDate", HotelTogetherRoomTypeListActivity.SDF2.format(Long.valueOf(HotelTogetherRoomTypeListActivity.this.endtime))).build();
              paramView = new Intent("android.intent.action.VIEW", paramView.buildUpon().appendQueryParameter("url", localUri.toString()).build());
              paramView.putExtra("name", "");
              HotelTogetherRoomTypeListActivity.this.startActivityForResult(paramView, 3);
              try
              {
                HotelTogetherRoomTypeListActivity.this.statisticsEvent("roominfo5", "roominfo5_booking", this.val$info.payPolicy, Integer.valueOf(HotelTogetherRoomTypeListActivity.this.otaid).intValue());
                return;
              }
              catch (NumberFormatException paramView)
              {
              }
            }
          });
        }
        while (true)
        {
          this.intAddCount += 1;
          return paramView;
          paramView = (TogetherRoomTypeListItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.hotel_roomtype_list_item, paramViewGroup, false);
          break;
          label408: paramView.setClickable(false);
          paramView.setOnClickListener(null);
        }
      }
      return (View)null;
    }

    public void setData(ArrayList<HotelTogetherRoomTypeListActivity.RoomTypeInfo> paramArrayList, int paramInt1, String paramString, int paramInt2)
    {
      this.infos = paramArrayList;
      if (paramArrayList != null)
        this.totalCount = paramArrayList.size();
      this.displayCount = paramInt2;
      this.intAddCount = 0;
      this.groupIndex = paramInt1;
      this.groupName = paramString;
      notifyDataSetChanged();
    }
  }

  public class RoomTypeDataSource extends AbstractDataSource
  {
    private String emptyMsg;
    private String errorMsg;
    private List<HotelTogetherRoomTypeListActivity.RoomTypeInfo> items = new ArrayList();
    private List<HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo> items_group = new ArrayList();
    DPObject result;

    public RoomTypeDataSource()
    {
    }

    public HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo getGroupedRoomTypeById(int paramInt)
    {
      Object localObject2 = null;
      Object localObject1 = localObject2;
      if (HotelTogetherRoomTypeListActivity.this.ds != null)
      {
        localObject1 = localObject2;
        if (HotelTogetherRoomTypeListActivity.this.ds.getRoomTogetherTypeInfo() != null)
        {
          localObject1 = localObject2;
          if (HotelTogetherRoomTypeListActivity.this.ds.getRoomTogetherTypeInfo().size() > 0)
            localObject1 = (HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo)HotelTogetherRoomTypeListActivity.this.ds.getRoomTogetherTypeInfo().get(paramInt);
        }
      }
      return (HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo)localObject1;
    }

    public List<HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo> getRoomTogetherTypeInfo()
    {
      if (this.result != null)
      {
        if (this.items_group != null)
          this.items_group.clear();
        DPObject[] arrayOfDPObject = this.result.getArray("GroupedRoomTypeList");
        if (arrayOfDPObject == null);
        for (int i = 0; ; i = arrayOfDPObject.length)
        {
          int j = 0;
          while (j < i)
          {
            Object localObject = arrayOfDPObject[j];
            localObject = new HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo(((DPObject)localObject).getString("RoomTypeName"), ((DPObject)localObject).getInt("DisplayCount"), ((DPObject)localObject).getIntArray("RoomIndexList"));
            this.items_group.add(localObject);
            j += 1;
          }
        }
      }
      return (List<HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo>)this.items_group;
    }

    public int getRoomTypeCount()
    {
      DPObject[] arrayOfDPObject;
      if (this.result != null)
      {
        arrayOfDPObject = this.result.getArray("HotelRoomDetailList");
        if (arrayOfDPObject != null);
      }
      else
      {
        return 0;
      }
      return arrayOfDPObject.length;
    }

    public List<HotelTogetherRoomTypeListActivity.RoomTypeInfo> getRoomTypeInfo()
    {
      if (this.result != null)
      {
        if (this.items != null)
          this.items.clear();
        DPObject[] arrayOfDPObject = this.result.getArray("HotelRoomDetailList");
        if (arrayOfDPObject == null);
        for (int i = 0; ; i = arrayOfDPObject.length)
        {
          int j = 0;
          while (j < i)
          {
            Object localObject = arrayOfDPObject[j];
            localObject = new HotelTogetherRoomTypeListActivity.RoomTypeInfo(((DPObject)localObject).getString("RoomTypeName"), ((DPObject)localObject).getString("RoomTypeExtInfo"), ((DPObject)localObject).getString("RoomInfo"), ((DPObject)localObject).getString("PayPolicy"), ((DPObject)localObject).getString("ReFund"), ((DPObject)localObject).getInt("Remains"), ((DPObject)localObject).getInt("Status"), ((DPObject)localObject).getString("BookingUrl"), ((DPObject)localObject).getString("RoomImage"), ((DPObject)localObject).getStringArray("specialActivaties"), ((DPObject)localObject).getString("PriceText"), ((DPObject)localObject).getStringArray("ImageList"), ((DPObject)localObject).getString("CancelTitle"), ((DPObject)localObject).getString("cancelContent"), ((DPObject)localObject).getString("RoomDetailInfo"));
            this.items.add(localObject);
            j += 1;
          }
        }
      }
      return (List<HotelTogetherRoomTypeListActivity.RoomTypeInfo>)this.items;
    }

    public HotelTogetherRoomTypeListActivity.RoomTypeInfo getRoomTypeInfoById(int paramInt)
    {
      Object localObject2 = null;
      Object localObject1 = localObject2;
      if (this.items != null)
      {
        localObject1 = localObject2;
        if (this.items.size() > 0)
          localObject1 = (HotelTogetherRoomTypeListActivity.RoomTypeInfo)this.items.get(paramInt);
      }
      return (HotelTogetherRoomTypeListActivity.RoomTypeInfo)localObject1;
    }

    public void reload(boolean paramBoolean)
    {
      changeStatus(1);
      HotelTogetherRoomTypeListActivity.this.requestHotelRoomTypeList(paramBoolean);
    }

    public void reset(boolean paramBoolean)
    {
      if (paramBoolean)
      {
        this.result = null;
        this.items.clear();
        this.items_group.clear();
        this.emptyMsg = null;
        this.errorMsg = null;
      }
    }

    public void setEmpty(String paramString)
    {
      this.emptyMsg = paramString;
      changeStatus(0);
    }

    public void setError(String paramString)
    {
      this.errorMsg = paramString;
      changeStatus(3);
    }

    public void setSuccess()
    {
      changeStatus(2);
    }
  }

  public static class RoomTypeInfo
  {
    String bookingUrl = "";
    String cancelContent = "";
    String cancelTitle = "";
    String[] imageList;
    String payPolicy = "";
    String phone = "";
    String priceText = "";
    HotelTogetherRoomTypeListActivity.PromoInfoDo[] promoInfoList;
    String reFund = "";
    int remains = 0;
    String roomDetailInfo = "";
    String roomImage = "";
    String roomInfo = "";
    String roomTypeExtInfo = "";
    String roomTypeName = "";
    String[] specialActivaties;
    int status = 0;
    String tax = "";

    public RoomTypeInfo(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, int paramInt1, int paramInt2, String paramString6, String paramString7, String[] paramArrayOfString1, String paramString8, String[] paramArrayOfString2, String paramString9, String paramString10, String paramString11)
    {
      this.roomTypeName = paramString1;
      this.roomTypeExtInfo = paramString2;
      this.roomInfo = paramString3;
      this.payPolicy = paramString4;
      this.reFund = paramString5;
      this.remains = paramInt1;
      this.status = paramInt2;
      this.bookingUrl = paramString6;
      this.roomImage = paramString7;
      this.specialActivaties = paramArrayOfString1;
      this.priceText = paramString8;
      this.imageList = paramArrayOfString2;
      this.cancelTitle = paramString9;
      this.cancelContent = paramString10;
      this.roomDetailInfo = paramString11;
    }
  }

  private class RoomTypeListAdapter extends BasicAdapter
  {
    private String emptyMsg1;
    private String emptyMsg2;
    private String errorMsg;
    private LayoutInflater inflater;
    private List<HotelTogetherRoomTypeListActivity.RoomTypeInfo> items = new ArrayList();
    private List<HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo> items_group = new ArrayList();
    private int resid;
    private int status = 3;

    public RoomTypeListAdapter(Context paramInt, int arg3)
    {
      this.inflater = LayoutInflater.from(paramInt);
      int i;
      this.resid = i;
      this$1 = this.inflater.inflate(this.resid, null, false);
      HotelTogetherRoomTypeListActivity.this.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
      HotelTogetherRoomTypeListActivity.this.measure(0, 0);
    }

    private void bindView(View paramView, LinkedHashMap<String, ArrayList<HotelTogetherRoomTypeListActivity.RoomTypeInfo>> paramLinkedHashMap, int paramInt)
    {
      Object localObject1 = (TableView)paramView.findViewById(R.id.ROOM_PRICE_TABLE);
      paramLinkedHashMap = null;
      if (localObject1 != null)
      {
        if (((TableView)localObject1).getAdapter() != null)
          break label174;
        paramLinkedHashMap = new HotelTogetherRoomTypeListActivity.RoomTableViewAdapter(HotelTogetherRoomTypeListActivity.this);
        ((TableView)localObject1).setAdapter(paramLinkedHashMap);
      }
      while (true)
      {
        Object localObject2 = (TextView)paramView.findViewById(R.id.hotel_roomtype_title);
        paramView = null;
        if (localObject2 != null)
        {
          paramView = ((HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo)this.items_group.get(paramInt)).RoomTypeName;
          ((TextView)localObject2).setText(paramView);
          ((TextView)localObject2).setClickable(false);
        }
        if (localObject1 == null)
          break;
        localObject1 = new ArrayList();
        localObject2 = ((HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo)this.items_group.get(paramInt)).RoomIndexList;
        int j = localObject2.length;
        int i = 0;
        while (true)
          if (i < j)
          {
            int k = localObject2[i];
            ((ArrayList)localObject1).add(HotelTogetherRoomTypeListActivity.this.ds.getRoomTypeInfoById(k));
            i += 1;
            continue;
            label174: paramLinkedHashMap = (HotelTogetherRoomTypeListActivity.RoomTableViewAdapter)((TableView)localObject1).getAdapter();
            break;
          }
        paramLinkedHashMap.setData((ArrayList)localObject1, paramInt, paramView, ((HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo)this.items_group.get(paramInt)).DisplayCount);
      }
    }

    private View getEmptyViewWithTwoLines(String paramString1, String paramString2, ViewGroup paramViewGroup, View paramView)
    {
      View localView = null;
      if (paramView == null);
      while (true)
      {
        paramView = localView;
        if (localView == null)
        {
          paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.hotel_roomtype_empty, paramViewGroup, false);
          paramView.setTag(EMPTY);
        }
        ((TextView)paramView.findViewById(R.id.hotel_roomtype_text_msg1)).setText(paramString1);
        ((TextView)paramView.findViewById(R.id.hotel_roomtype_text_msg2)).setText(paramString2);
        return paramView;
        if (paramView.getTag() != EMPTY)
          continue;
        localView = paramView;
      }
    }

    public boolean areAllItemsEnabled()
    {
      return false;
    }

    public int getCount()
    {
      if ((this.status == 1) || (this.status == 3) || (this.status == 0))
        return 1;
      return this.items_group.size();
    }

    public Object getItem(int paramInt)
    {
      if (this.status == 1)
        return LOADING;
      if (this.status == 3)
        return ERROR;
      if (this.status == 0)
        return EMPTY;
      if ((this.status == 2) && (paramInt < getCount()))
        return HotelTogetherRoomTypeListActivity.this.lists.get(paramInt);
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      if (((getItem(paramInt) instanceof LinkedHashMap)) && (HotelTogetherRoomTypeListActivity.this.lists != null) && (HotelTogetherRoomTypeListActivity.this.lists.size() > 0))
      {
        ArrayList localArrayList = new ArrayList();
        int[] arrayOfInt = ((HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo)this.items_group.get(paramInt)).RoomIndexList;
        paramInt = 0;
        while (paramInt < arrayOfInt.length)
        {
          localArrayList.add(this.items.get(arrayOfInt[paramInt]));
          paramInt += 1;
        }
        if ((localArrayList == null) || (localArrayList.size() == 0))
          return -1;
        paramInt = localArrayList.size();
        if (paramInt == 1)
          return 0;
        if (paramInt == 2)
          return 1;
        if (paramInt == 3)
          return 2;
        if (paramInt > 3)
          return 3;
      }
      return -1;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof LinkedHashMap))
      {
        if ((HotelTogetherRoomTypeListActivity.this.lists != null) && (HotelTogetherRoomTypeListActivity.this.lists.size() > 0))
        {
          LinkedHashMap localLinkedHashMap = (LinkedHashMap)HotelTogetherRoomTypeListActivity.this.lists.get(paramInt);
          localObject = paramView;
          if (paramView == null)
            localObject = LayoutInflater.from(HotelTogetherRoomTypeListActivity.this).inflate(R.layout.hotel_together_roomtype_list_item, paramViewGroup, false);
          bindView((View)localObject, localLinkedHashMap, paramInt);
          return localObject;
        }
        return getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            HotelTogetherRoomTypeListActivity.this.ds.reset(true);
            HotelTogetherRoomTypeListActivity.this.ds.reload(true);
          }
        }
        , paramViewGroup, paramView);
      }
      if (localObject == LOADING)
        return getLoadingView(paramViewGroup, paramView);
      if (localObject == EMPTY)
      {
        paramView = getEmptyViewWithTwoLines(this.emptyMsg1, this.emptyMsg2, paramViewGroup, paramView);
        paramViewGroup = paramView.getLayoutParams();
        paramViewGroup.width = -2;
        paramViewGroup.height = -2;
        paramView.requestLayout();
        return paramView;
      }
      if (localObject == ERROR)
        return getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            HotelTogetherRoomTypeListActivity.this.ds.reset(true);
            HotelTogetherRoomTypeListActivity.this.ds.reload(true);
          }
        }
        , paramViewGroup, paramView);
      return (View)null;
    }

    public int getViewTypeCount()
    {
      return 4;
    }

    public boolean isEnabled(int paramInt)
    {
      int i = 1;
      if ((this.status == 1) || (this.status == 3) || (this.status == 0))
        i = 0;
      return i;
    }

    public void setData(HotelTogetherRoomTypeListActivity.RoomTypeDataSource paramRoomTypeDataSource)
    {
      this.items.clear();
      this.items = paramRoomTypeDataSource.getRoomTypeInfo();
      this.items_group = paramRoomTypeDataSource.getRoomTogetherTypeInfo();
      int i = 0;
      while (i < this.items_group.size())
      {
        Object localObject = ((HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo)this.items_group.get(i)).RoomIndexList;
        ArrayList localArrayList = new ArrayList();
        int j = 0;
        while (j < localObject.length)
        {
          localArrayList.add(paramRoomTypeDataSource.getRoomTypeInfoById(j));
          j += 1;
        }
        if (HotelTogetherRoomTypeListActivity.this.lists != null)
        {
          localObject = new LinkedHashMap();
          ((LinkedHashMap)localObject).put(((HotelTogetherRoomTypeListActivity.GroupedRoomTypeDo)this.items_group.get(i)).RoomTypeName, localArrayList);
          HotelTogetherRoomTypeListActivity.this.lists.add(localObject);
        }
        i += 1;
      }
      this.status = paramRoomTypeDataSource.status();
      this.errorMsg = paramRoomTypeDataSource.errorMsg;
      if (!TextUtils.isEmpty(paramRoomTypeDataSource.emptyMsg))
      {
        paramRoomTypeDataSource = paramRoomTypeDataSource.emptyMsg.split("\\|");
        if ((paramRoomTypeDataSource != null) && (paramRoomTypeDataSource.length == 2))
        {
          this.emptyMsg1 = paramRoomTypeDataSource[0];
          this.emptyMsg2 = paramRoomTypeDataSource[1];
        }
      }
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shopinfo.activity.HotelTogetherRoomTypeListActivity
 * JD-Core Version:    0.6.0
 */
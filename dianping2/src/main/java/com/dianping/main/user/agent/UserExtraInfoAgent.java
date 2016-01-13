package com.dianping.main.user.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.update.UpdateManager;
import com.dianping.base.util.FavoriteHelper;
import com.dianping.base.util.RedAlertManager;
import com.dianping.base.widget.CustomGridView;
import com.dianping.base.widget.CustomGridView.OnItemClickListener;
import com.dianping.base.widget.ImageCachedUserProfileItem;
import com.dianping.base.widget.TableHeader;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.UserProfileBaseItem;
import com.dianping.base.widget.UserProfileItem;
import com.dianping.base.widget.UserProfileVerticalItem;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.main.user.agent.app.UserAgent;
import com.dianping.util.Daemon;
import com.dianping.util.LoginUtils;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class UserExtraInfoAgent extends UserAgent
  implements View.OnClickListener, CustomGridView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String BINARY_USER_EXTRA_INFO_ELEMENTS = "myuserextrainfo";
  private static final String CELLNAME = "30UserExtraInfoAgent.";
  private static final String TAG = UserExtraInfoAgent.class.getSimpleName();
  private static final int UPDATE_USER_EXTRA_INFO = 1;
  private static final String URL = "http://m.api.dianping.com/framework/getelements.api";
  Comparator<DPObject> comparator = new Comparator()
  {
    public int compare(DPObject paramDPObject1, DPObject paramDPObject2)
    {
      int j = paramDPObject1.getInt("Group") - paramDPObject2.getInt("Group");
      int i = j;
      if (j == 0)
        i = paramDPObject1.getInt("Order") - paramDPObject2.getInt("Order");
      return i;
    }
  };
  Handler handler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 1)
        UserExtraInfoAgent.this.updateExtraInfoView();
    }
  };
  private boolean hasNewUpdate;
  private View mContainerView;
  private List<DPObject> mElements = new ArrayList();
  private MApiRequest mUserExtraInfoReq;
  private ArrayList<UserProfileBaseItem> myViewGroup = new ArrayList();
  private TableView userExtraInfoContainerView;

  public UserExtraInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createElementItem(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return null;
    ImageCachedUserProfileItem localImageCachedUserProfileItem = new ImageCachedUserProfileItem(getContext());
    localImageCachedUserProfileItem.setLayoutParams(new LinearLayout.LayoutParams(-1, getResources().getDimensionPixelSize(R.dimen.single_line_height)));
    localImageCachedUserProfileItem.setGravity(16);
    int i = (int)getResources().getDimension(R.dimen.table_item_padding);
    localImageCachedUserProfileItem.setPadding(i, 0, i, 0);
    localImageCachedUserProfileItem.setBackgroundResource(R.drawable.table_view_item);
    String str = paramDPObject.getString("Title");
    if ((TextUtils.isEmpty(str)) && (TextUtils.isEmpty(paramDPObject.getString("Url"))))
      return null;
    SpannableStringBuilder localSpannableStringBuilder = TextUtils.jsonParseText(paramDPObject.getString("SubTitle"));
    if (!TextUtils.isEmpty(str))
      localImageCachedUserProfileItem.setTitle(str);
    localImageCachedUserProfileItem.setImageSize(20, 20);
    localImageCachedUserProfileItem.setItemImage(paramDPObject.getString("IconUrl"));
    localImageCachedUserProfileItem.setTag(paramDPObject);
    localImageCachedUserProfileItem.setOnClickListener(this);
    localImageCachedUserProfileItem.setGAString(paramDPObject.getString("ElementId"));
    if (localSpannableStringBuilder != null)
      localImageCachedUserProfileItem.setSubtitle(localSpannableStringBuilder);
    showRedAlertInfo(localImageCachedUserProfileItem);
    this.myViewGroup.add(localImageCachedUserProfileItem);
    return localImageCachedUserProfileItem;
  }

  private TableHeader createTabHeader()
  {
    TableHeader localTableHeader = new TableHeader(getContext());
    localTableHeader.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 12.0F)));
    return localTableHeader;
  }

  // ERROR //
  private boolean loadFromFile()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 5
    //   3: aconst_null
    //   4: astore_1
    //   5: aconst_null
    //   6: astore_3
    //   7: aload 5
    //   9: astore_2
    //   10: aload_0
    //   11: invokevirtual 107	com/dianping/main/user/agent/UserExtraInfoAgent:getContext	()Landroid/content/Context;
    //   14: ldc 25
    //   16: invokevirtual 240	android/content/Context:openFileInput	(Ljava/lang/String;)Ljava/io/FileInputStream;
    //   19: astore 4
    //   21: aload 5
    //   23: astore_2
    //   24: aload 4
    //   26: astore_3
    //   27: aload 4
    //   29: astore_1
    //   30: aload 4
    //   32: invokevirtual 246	java/io/FileInputStream:available	()I
    //   35: newarray byte
    //   37: astore 5
    //   39: aload 5
    //   41: astore_2
    //   42: aload 4
    //   44: astore_3
    //   45: aload 4
    //   47: astore_1
    //   48: aload 4
    //   50: aload 5
    //   52: invokevirtual 250	java/io/FileInputStream:read	([B)I
    //   55: pop
    //   56: aload 5
    //   58: astore_1
    //   59: aload 4
    //   61: ifnull +11 -> 72
    //   64: aload 4
    //   66: invokevirtual 253	java/io/FileInputStream:close	()V
    //   69: aload 5
    //   71: astore_1
    //   72: aload_1
    //   73: astore_2
    //   74: aload_1
    //   75: ifnonnull +38 -> 113
    //   78: aload_0
    //   79: invokevirtual 107	com/dianping/main/user/agent/UserExtraInfoAgent:getContext	()Landroid/content/Context;
    //   82: invokevirtual 256	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   85: getstatic 260	com/dianping/v1/R$raw:myuserextrainfo	I
    //   88: invokevirtual 266	android/content/res/Resources:openRawResource	(I)Ljava/io/InputStream;
    //   91: astore_3
    //   92: aload_3
    //   93: invokevirtual 269	java/io/InputStream:available	()I
    //   96: newarray byte
    //   98: astore_2
    //   99: aload_2
    //   100: astore_1
    //   101: aload_3
    //   102: aload_2
    //   103: invokevirtual 270	java/io/InputStream:read	([B)I
    //   106: pop
    //   107: aload_2
    //   108: astore_1
    //   109: aload_3
    //   110: invokevirtual 271	java/io/InputStream:close	()V
    //   113: aconst_null
    //   114: astore_3
    //   115: new 273	com/dianping/archive/Unarchiver
    //   118: dup
    //   119: aload_2
    //   120: invokespecial 276	com/dianping/archive/Unarchiver:<init>	([B)V
    //   123: astore_2
    //   124: aconst_null
    //   125: astore_1
    //   126: aload_2
    //   127: invokevirtual 280	com/dianping/archive/Unarchiver:readDPObject	()Lcom/dianping/archive/DPObject;
    //   130: astore_2
    //   131: aload_2
    //   132: astore_1
    //   133: aload_3
    //   134: astore_2
    //   135: aload_1
    //   136: ifnull +11 -> 147
    //   139: aload_1
    //   140: ldc_w 282
    //   143: invokevirtual 286	com/dianping/archive/DPObject:getArray	(Ljava/lang/String;)[Lcom/dianping/archive/DPObject;
    //   146: astore_2
    //   147: aload_0
    //   148: getfield 75	com/dianping/main/user/agent/UserExtraInfoAgent:mElements	Ljava/util/List;
    //   151: astore_1
    //   152: aload_1
    //   153: monitorenter
    //   154: aload_0
    //   155: getfield 75	com/dianping/main/user/agent/UserExtraInfoAgent:mElements	Ljava/util/List;
    //   158: invokeinterface 291 1 0
    //   163: ifne +134 -> 297
    //   166: aload_2
    //   167: ifnull +8 -> 175
    //   170: aload_2
    //   171: arraylength
    //   172: ifne +93 -> 265
    //   175: aload_1
    //   176: monitorexit
    //   177: iconst_0
    //   178: ireturn
    //   179: astore_1
    //   180: aload_1
    //   181: invokevirtual 294	java/io/IOException:printStackTrace	()V
    //   184: aload 5
    //   186: astore_1
    //   187: goto -115 -> 72
    //   190: astore_1
    //   191: aload_2
    //   192: astore_1
    //   193: aload_3
    //   194: ifnull -122 -> 72
    //   197: aload_3
    //   198: invokevirtual 253	java/io/FileInputStream:close	()V
    //   201: aload_2
    //   202: astore_1
    //   203: goto -131 -> 72
    //   206: astore_1
    //   207: aload_1
    //   208: invokevirtual 294	java/io/IOException:printStackTrace	()V
    //   211: aload_2
    //   212: astore_1
    //   213: goto -141 -> 72
    //   216: astore_2
    //   217: aload_1
    //   218: ifnull +7 -> 225
    //   221: aload_1
    //   222: invokevirtual 253	java/io/FileInputStream:close	()V
    //   225: aload_2
    //   226: athrow
    //   227: astore_1
    //   228: aload_1
    //   229: invokevirtual 294	java/io/IOException:printStackTrace	()V
    //   232: goto -7 -> 225
    //   235: astore_2
    //   236: getstatic 64	com/dianping/main/user/agent/UserExtraInfoAgent:TAG	Ljava/lang/String;
    //   239: ldc_w 296
    //   242: aload_2
    //   243: invokestatic 302	com/dianping/util/Log:i	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   246: aload_1
    //   247: astore_2
    //   248: goto -135 -> 113
    //   251: astore_2
    //   252: getstatic 64	com/dianping/main/user/agent/UserExtraInfoAgent:TAG	Ljava/lang/String;
    //   255: ldc_w 296
    //   258: aload_2
    //   259: invokestatic 305	com/dianping/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   262: goto -129 -> 133
    //   265: aload_0
    //   266: getfield 75	com/dianping/main/user/agent/UserExtraInfoAgent:mElements	Ljava/util/List;
    //   269: invokeinterface 308 1 0
    //   274: aload_0
    //   275: getfield 75	com/dianping/main/user/agent/UserExtraInfoAgent:mElements	Ljava/util/List;
    //   278: aload_2
    //   279: invokestatic 314	java/util/Arrays:asList	([Ljava/lang/Object;)Ljava/util/List;
    //   282: invokeinterface 318 2 0
    //   287: pop
    //   288: aload_1
    //   289: monitorexit
    //   290: iconst_1
    //   291: ireturn
    //   292: astore_2
    //   293: aload_1
    //   294: monitorexit
    //   295: aload_2
    //   296: athrow
    //   297: aload_1
    //   298: monitorexit
    //   299: iconst_0
    //   300: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   64	69	179	java/io/IOException
    //   10	21	190	java/lang/Exception
    //   30	39	190	java/lang/Exception
    //   48	56	190	java/lang/Exception
    //   197	201	206	java/io/IOException
    //   10	21	216	finally
    //   30	39	216	finally
    //   48	56	216	finally
    //   221	225	227	java/io/IOException
    //   92	99	235	java/lang/Exception
    //   101	107	235	java/lang/Exception
    //   109	113	235	java/lang/Exception
    //   126	131	251	java/lang/Exception
    //   154	166	292	finally
    //   170	175	292	finally
    //   175	177	292	finally
    //   265	290	292	finally
    //   293	295	292	finally
    //   297	299	292	finally
  }

  private void saveToFile(byte[] paramArrayOfByte)
  {
    Handler localHandler = new Handler(Daemon.looper());
    if (localHandler != null)
      localHandler.post(new Runnable(paramArrayOfByte)
      {
        // ERROR //
        public void run()
        {
          // Byte code:
          //   0: aconst_null
          //   1: astore_2
          //   2: aconst_null
          //   3: astore_1
          //   4: aload_0
          //   5: getfield 19	com/dianping/main/user/agent/UserExtraInfoAgent$5:this$0	Lcom/dianping/main/user/agent/UserExtraInfoAgent;
          //   8: invokevirtual 34	com/dianping/main/user/agent/UserExtraInfoAgent:getContext	()Landroid/content/Context;
          //   11: ldc 36
          //   13: iconst_0
          //   14: invokevirtual 42	android/content/Context:openFileOutput	(Ljava/lang/String;I)Ljava/io/FileOutputStream;
          //   17: astore_3
          //   18: aload_3
          //   19: astore_1
          //   20: aload_3
          //   21: astore_2
          //   22: aload_3
          //   23: aload_0
          //   24: getfield 21	com/dianping/main/user/agent/UserExtraInfoAgent$5:val$bytes	[B
          //   27: invokevirtual 47	java/io/FileOutputStream:write	([B)V
          //   30: aload_3
          //   31: ifnull +7 -> 38
          //   34: aload_3
          //   35: invokevirtual 50	java/io/FileOutputStream:close	()V
          //   38: return
          //   39: astore_2
          //   40: aload_1
          //   41: ifnull -3 -> 38
          //   44: aload_1
          //   45: invokevirtual 50	java/io/FileOutputStream:close	()V
          //   48: return
          //   49: astore_1
          //   50: return
          //   51: astore_1
          //   52: aload_2
          //   53: ifnull +7 -> 60
          //   56: aload_2
          //   57: invokevirtual 50	java/io/FileOutputStream:close	()V
          //   60: aload_1
          //   61: athrow
          //   62: astore_1
          //   63: return
          //   64: astore_2
          //   65: goto -5 -> 60
          //
          // Exception table:
          //   from	to	target	type
          //   4	18	39	java/lang/Exception
          //   22	30	39	java/lang/Exception
          //   44	48	49	java/io/IOException
          //   4	18	51	finally
          //   22	30	51	finally
          //   34	38	62	java/io/IOException
          //   56	60	64	java/io/IOException
        }
      });
  }

  private void showRedAlertInfo(UserProfileBaseItem paramUserProfileBaseItem)
  {
    if (paramUserProfileBaseItem == null)
      return;
    if (!TextUtils.isEmpty(paramUserProfileBaseItem.getGAString()));
    for (String str1 = ((DPObject)paramUserProfileBaseItem.getTag()).getString("TagId"); TextUtils.isEmpty(str1); str1 = (String)paramUserProfileBaseItem.getTag())
    {
      paramUserProfileBaseItem.setRedAlert(false, "");
      return;
    }
    if (("me.setting".equals(str1)) && (this.hasNewUpdate))
    {
      paramUserProfileBaseItem.setRedAlert(this.hasNewUpdate, "发现新版本");
      return;
    }
    String str2 = RedAlertManager.getInstance().checkRedAlertByTag(str1);
    if (str2 == null)
    {
      paramUserProfileBaseItem.setRedAlert(false, "");
      return;
    }
    if ("me.toreview".equals(str1))
    {
      ((UserProfileItem)paramUserProfileBaseItem).setSpecialRedAlert(true, str2);
      return;
    }
    paramUserProfileBaseItem.setRedAlert(true, str2);
  }

  private void showRedAlertInfo(ArrayList<UserProfileBaseItem> paramArrayList)
  {
    if ((paramArrayList == null) || (paramArrayList.size() == 0));
    while (true)
    {
      return;
      paramArrayList = paramArrayList.iterator();
      while (paramArrayList.hasNext())
        showRedAlertInfo((UserProfileBaseItem)paramArrayList.next());
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle == null)
      updateCell();
    showRedAlertInfo(this.myViewGroup);
  }

  public void onClick(View paramView)
  {
    DPObject localDPObject;
    String str;
    if ((paramView.getTag() != null) && ((paramView.getTag() instanceof DPObject)))
    {
      localDPObject = (DPObject)paramView.getTag();
      str = localDPObject.getString("Url");
      boolean bool = localDPObject.getBoolean("NeedLogin");
      if (!TextUtils.isEmpty(str))
      {
        if (!bool)
          break label115;
        if (!isLogin())
          LoginUtils.setLoginGASource(getContext(), "ac_other");
        if (!needGotoLogin(paramView))
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(str));
          getFragment().startActivity(paramView);
          RedAlertManager.getInstance().updateRedAlert(localDPObject.getString("TagId"));
        }
      }
    }
    label115: 
    do
    {
      int i;
      do
        while (true)
        {
          return;
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(str));
          getFragment().startActivity(paramView);
          if (("me.setting".equals(localDPObject.getString("TagId"))) && (this.hasNewUpdate))
            continue;
          RedAlertManager.getInstance().updateRedAlert(localDPObject.getString("TagId"));
          return;
          i = paramView.getId();
          if (i == R.id.review)
          {
            if (!isLogin())
              LoginUtils.setLoginGASource(getContext(), "ac_other");
            if (needGotoLogin(paramView))
              continue;
            if (((UserProfileVerticalItem)paramView).isRedMarkVisible())
            {
              ((UserProfileVerticalItem)paramView).setRedAlert(false);
              RedAlertManager.getInstance().updateRedAlert(((UserProfileVerticalItem)paramView).getTag());
            }
            startActivity("dianping://user");
            return;
          }
          if (i != R.id.favour)
            break;
          if (!isLogin())
            LoginUtils.setLoginGASource(getContext(), "ac_other");
          if (needGotoLogin(paramView))
            continue;
          if (((UserProfileVerticalItem)paramView).isRedMarkVisible())
          {
            ((UserProfileVerticalItem)paramView).setRedAlert(false);
            RedAlertManager.getInstance().updateRedAlert(((UserProfileVerticalItem)paramView).getTag());
          }
          new FavoriteHelper(null).refresh(0, true);
          try
          {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + URLEncoder.encode("http://m.dianping.com/follow/index?token=!&cityid=*&latitude=*&longitude=*&product=fromapp", "utf-8"))));
            return;
          }
          catch (Exception paramView)
          {
            paramView.printStackTrace();
            return;
          }
        }
      while (i != R.id.tuan);
      if (isLogin())
        continue;
      LoginUtils.setLoginGASource(getContext(), "ac_other");
    }
    while (needGotoLogin(paramView));
    if (((UserProfileVerticalItem)paramView).isRedMarkVisible())
    {
      ((UserProfileVerticalItem)paramView).setRedAlert(false);
      RedAlertManager.getInstance().updateRedAlert(((UserProfileVerticalItem)paramView).getTag());
    }
    startActivity("dianping://mycoupon");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    new Thread(new Runnable()
    {
      public void run()
      {
        if (UserExtraInfoAgent.this.loadFromFile())
          UserExtraInfoAgent.this.handler.sendEmptyMessage(1);
      }
    }).start();
    this.hasNewUpdate = UpdateManager.instance(getContext()).checkNewVersion();
    requestUserExtraInfo();
    accountService().addListener(new AccountListener()
    {
      public void onAccountChanged(AccountService paramAccountService)
      {
        UserExtraInfoAgent.this.requestUserExtraInfo();
      }

      public void onProfileChanged(AccountService paramAccountService)
      {
      }
    });
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mUserExtraInfoReq != null)
    {
      getFragment().mapiService().abort(this.mUserExtraInfoReq, this, true);
      this.mUserExtraInfoReq = null;
    }
  }

  public void onItemClick(CustomGridView paramCustomGridView, View paramView, int paramInt, long paramLong)
  {
    onClick(paramView);
  }

  public void onRefresh()
  {
    onRefreshRequestStart();
    requestUserExtraInfo();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    onRefreshRequestFinish();
    this.mUserExtraInfoReq = null;
  }

  public void onRequestFinish(MApiRequest arg1, MApiResponse paramMApiResponse)
  {
    if (??? == this.mUserExtraInfoReq)
    {
      onRefreshRequestFinish();
      if (getContext() == null)
        this.mUserExtraInfoReq = null;
    }
    else
    {
      return;
    }
    DPObject[] arrayOfDPObject;
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      arrayOfDPObject = ((DPObject)paramMApiResponse.result()).getArray("List");
      if (arrayOfDPObject.length <= 0);
    }
    synchronized (this.mElements)
    {
      this.mElements.clear();
      this.mElements.addAll(Arrays.asList(arrayOfDPObject));
      updateExtraInfoView();
      saveToFile(paramMApiResponse.rawData());
      this.mUserExtraInfoReq = null;
      return;
    }
  }

  public void onResume()
  {
    super.onResume();
    showRedAlertInfo(this.myViewGroup);
  }

  public void requestUserExtraInfo()
  {
    if (this.mUserExtraInfoReq != null)
      return;
    this.mUserExtraInfoReq = new BasicMApiRequest(Uri.parse("http://m.api.dianping.com/framework/getelements.api").buildUpon().appendQueryParameter("scene", "my").appendQueryParameter("city", String.valueOf(cityId())).build().toString(), "GET", null, CacheType.DISABLED, false, null);
    getFragment().mapiService().exec(this.mUserExtraInfoReq, this);
  }

  void updateCell()
  {
    if (this.mContainerView == null)
    {
      this.mContainerView = getResources().inflate(getContext(), R.layout.my_zone_extra, getParentView(), false);
      View localView1 = this.mContainerView.findViewById(R.id.review);
      localView1.setOnClickListener(this);
      View localView2 = this.mContainerView.findViewById(R.id.favour);
      localView2.setOnClickListener(this);
      View localView3 = this.mContainerView.findViewById(R.id.tuan);
      localView3.setOnClickListener(this);
      this.userExtraInfoContainerView = ((TableView)this.mContainerView.findViewById(R.id.extra_info_container));
      addCell("30UserExtraInfoAgent.", this.mContainerView);
      this.myViewGroup.add((UserProfileVerticalItem)localView1);
      this.myViewGroup.add((UserProfileVerticalItem)localView2);
      this.myViewGroup.add((UserProfileVerticalItem)localView3);
    }
  }

  public void updateExtraInfoView()
  {
    if ((this.userExtraInfoContainerView == null) || (this.mElements.size() == 0) || (getContext() == null));
    while (true)
    {
      return;
      Collections.sort(this.mElements, this.comparator);
      ArrayList localArrayList1 = new ArrayList();
      int k = -1;
      int j = 0;
      int i = 0;
      ArrayList localArrayList2;
      if (i < this.mElements.size())
      {
        int n = j;
        int m = k;
        if (this.mElements.get(i) != null)
        {
          if (((DPObject)this.mElements.get(i)).getInt("Group") == k)
            break label192;
          m = ((DPObject)this.mElements.get(i)).getInt("Group");
          localArrayList2 = new ArrayList();
          localArrayList2.add(this.mElements.get(i));
          localArrayList1.add(j, localArrayList2);
          n = j + 1;
        }
        while (true)
        {
          i += 1;
          j = n;
          k = m;
          break;
          label192: ((ArrayList)localArrayList1.get(j - 1)).add(this.mElements.get(i));
          n = j;
          m = k;
        }
      }
      this.userExtraInfoContainerView.removeAllViews();
      i = 0;
      while (i < localArrayList1.size())
      {
        localArrayList2 = (ArrayList)localArrayList1.get(i);
        if ((localArrayList2 != null) && (localArrayList2.size() > 0))
        {
          this.userExtraInfoContainerView.addView(createTabHeader());
          j = 0;
          while (j < localArrayList2.size())
          {
            View localView = createElementItem((DPObject)localArrayList2.get(j));
            if (localView != null)
              this.userExtraInfoContainerView.addView(localView);
            j += 1;
          }
        }
        i += 1;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.agent.UserExtraInfoAgent
 * JD-Core Version:    0.6.0
 */
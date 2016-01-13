package com.dianping.main.user.activity;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.impl.BaseAccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.ugc.photo.SelectPhotoUtil;
import com.dianping.base.util.FavoriteHelper;
import com.dianping.base.util.RedAlertManager;
import com.dianping.base.widget.CircleImageView;
import com.dianping.base.widget.NovaFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.base.widget.TitleBar.OnDoubleClickListener;
import com.dianping.content.CityUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.StringInputStream;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiFormInputStream;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.AccountBind;
import com.dianping.model.AccountBindResult;
import com.dianping.model.City;
import com.dianping.model.UserLevel;
import com.dianping.model.UserProfile;
import com.dianping.util.ChainInputStream;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.DPBasicItem;
import com.dianping.widget.NetworkImageView;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  public static final String ACTION_UPDATE_AVATAR = "com.dianping.main.user.UPDATE_AVATAR";
  public static final String DEFAULT_TUAN_API_DOMAIN = "http://app.t.dianping.com/";
  public static final int JPEG_QUALITY = 80;
  public static final int MIN_IMAGE_SIZE = 140;
  public static final int PHOTO_CROP_WITH_DATA = 3025;
  private static final int REQUESTCODE_ADDRESS = 1;
  public static final int SCALE_IMAGE_SIZE = 700;
  private TextView address;
  private String bindPhoneNo;
  private AccountBind[] bindlist;
  int genderid;
  private CircleImageView icon;
  private File image;
  private boolean isCityChange;
  private boolean isPhoneChanged;
  boolean isSexChange;
  private Button mBtnLogout;
  private String mNewCityName;
  private int newCityId;
  TextView nickName;
  private DPBasicItem nickNameItem;
  private int oldCityId;
  int oldGenderid;
  private TextView phoneNum;
  private DPBasicItem phoneNumberItem;
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      if ("com.dianping.action.PROFILE_EDIT".equals(paramContext))
      {
        paramContext = paramIntent.getStringExtra("newUserName");
        if (!TextUtils.isEmpty(paramContext))
        {
          EditProfileActivity.this.setTextGray(EditProfileActivity.this.nickName, paramContext);
          if (EditProfileActivity.this.accountService().token() != null)
          {
            paramContext = EditProfileActivity.this.accountService().profile().edit().putString("NickName", paramContext).generate();
            EditProfileActivity.this.accountService().update(paramContext);
          }
        }
      }
      do
      {
        return;
        if (!"phoneChanged".equals(paramContext))
          continue;
        try
        {
          paramContext = new JSONObject(paramIntent.getExtras().getString("data"));
          EditProfileActivity.this.setProtectPhoneNum(paramContext.optString("mobile"));
          EditProfileActivity.access$102(EditProfileActivity.this, true);
          return;
        }
        catch (Exception paramContext)
        {
          paramContext.printStackTrace();
          return;
        }
      }
      while ((!"AccountBindChange".equals(paramContext)) || (paramIntent.getExtras() == null));
      paramContext = paramIntent.getExtras().getString("data");
      EditProfileActivity.this.updateThirdBind(paramContext);
    }
  };
  private TextView sex;
  private Bitmap showBitmap;
  private DPBasicItem thirdBindItem;
  DPObject tuanProfile;
  private TextView tv_icon_setting;
  private MApiRequest updateProfileReq;
  private MApiRequest uploadPhotoRequest;
  private TextView userGrade;
  private String userGradeUrl = "";
  private TextView userLevel;
  private DPBasicItem userLevelItem;
  private String userLevelUrl = "";

  private void gotoThirdPartyBind()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse("dianping://thirdpartybind"));
    localIntent.putExtra("url", "http://m.dianping.com/account/thirdbind/setup/app?token=" + ((BaseAccountService)accountService()).newToken() + "&version=" + Environment.versionName());
    startActivity(localIntent);
  }

  private void initView()
  {
    super.getTitleBar().setLeftView(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        KeyboardUtils.hideKeyboard(paramView);
        if (EditProfileActivity.this.getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
          paramView = EditProfileActivity.this.getSupportFragmentManager().findFragmentById(16908290);
          if ((paramView instanceof NovaFragment))
          {
            if (((NovaFragment)paramView).onGoBack())
              EditProfileActivity.this.getSupportFragmentManager().popBackStackImmediate();
            return;
          }
          EditProfileActivity.this.getSupportFragmentManager().popBackStackImmediate();
          return;
        }
        EditProfileActivity.this.finish();
      }
    });
    super.getTitleBar().setOnDoubleClickListener(new TitleBar.OnDoubleClickListener()
    {
      public void onDoubleClick()
      {
        View localView = EditProfileActivity.this.findViewById(R.id.scroll_view);
        if ((localView instanceof ScrollView))
          ((ScrollView)localView).smoothScrollTo(0, 0);
      }
    });
    findViewById(R.id.img).setOnClickListener(this);
    this.icon = ((CircleImageView)findViewById(R.id.icon));
    this.tv_icon_setting = ((TextView)findViewById(R.id.icon_setting));
    this.address = setDPbasicItemProfileView(R.id.residence);
    this.sex = setDPbasicItemProfileView(R.id.sex);
    this.nickName = setDPbasicItemProfileView(R.id.name);
    this.phoneNum = setDPbasicItemProfileView(R.id.telephone);
    this.userLevel = setDPbasicItemProfileView(R.id.vip);
    this.userGrade = setDPbasicItemProfileView(R.id.grade);
    setDPbasicItemProfileView(R.id.address);
    setDPbasicItemProfileView(R.id.openid);
    setDPbasicItemProfileView(R.id.password);
    this.mBtnLogout = ((Button)findViewById(R.id.exitid));
    this.mBtnLogout.setOnClickListener(this);
    this.thirdBindItem = ((DPBasicItem)findViewById(R.id.openid));
    this.userLevelItem = ((DPBasicItem)findViewById(R.id.vip));
    this.nickNameItem = ((DPBasicItem)findViewById(R.id.name));
    this.phoneNumberItem = ((DPBasicItem)findViewById(R.id.telephone));
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.userLevelItem.getItemLeft1stPic().getLayoutParams();
    localLayoutParams.width = ViewUtils.dip2px(this, 24.0F);
    localLayoutParams.height = ViewUtils.dip2px(this, 24.0F);
  }

  // ERROR //
  private File makeUploadPhoto(Bitmap paramBitmap)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 306	android/graphics/Bitmap:getWidth	()I
    //   4: aload_1
    //   5: invokevirtual 309	android/graphics/Bitmap:getHeight	()I
    //   8: if_icmple +120 -> 128
    //   11: aload_1
    //   12: invokevirtual 306	android/graphics/Bitmap:getWidth	()I
    //   15: i2f
    //   16: fstore_2
    //   17: ldc_w 310
    //   20: fload_2
    //   21: fdiv
    //   22: fstore_3
    //   23: aload_1
    //   24: invokevirtual 306	android/graphics/Bitmap:getWidth	()I
    //   27: aload_1
    //   28: invokevirtual 309	android/graphics/Bitmap:getHeight	()I
    //   31: if_icmple +106 -> 137
    //   34: aload_1
    //   35: invokevirtual 309	android/graphics/Bitmap:getHeight	()I
    //   38: i2f
    //   39: fstore_2
    //   40: fload_2
    //   41: ldc_w 311
    //   44: fcmpg
    //   45: ifge +9 -> 54
    //   48: ldc_w 311
    //   51: fload_2
    //   52: fdiv
    //   53: fstore_3
    //   54: aload_1
    //   55: aload_1
    //   56: invokevirtual 306	android/graphics/Bitmap:getWidth	()I
    //   59: i2f
    //   60: fload_3
    //   61: fmul
    //   62: f2i
    //   63: aload_1
    //   64: invokevirtual 309	android/graphics/Bitmap:getHeight	()I
    //   67: i2f
    //   68: fload_3
    //   69: fmul
    //   70: f2i
    //   71: iconst_1
    //   72: invokestatic 315	android/graphics/Bitmap:createScaledBitmap	(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
    //   75: astore_1
    //   76: new 317	java/io/File
    //   79: dup
    //   80: aload_0
    //   81: invokevirtual 321	com/dianping/main/user/activity/EditProfileActivity:getFilesDir	()Ljava/io/File;
    //   84: ldc_w 323
    //   87: invokespecial 326	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   90: astore 4
    //   92: new 328	java/io/FileOutputStream
    //   95: dup
    //   96: aload 4
    //   98: invokespecial 331	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   101: astore 5
    //   103: aload_1
    //   104: getstatic 337	android/graphics/Bitmap$CompressFormat:JPEG	Landroid/graphics/Bitmap$CompressFormat;
    //   107: bipush 80
    //   109: aload 5
    //   111: invokevirtual 341	android/graphics/Bitmap:compress	(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   114: pop
    //   115: aload 5
    //   117: invokevirtual 344	java/io/FileOutputStream:flush	()V
    //   120: aload 5
    //   122: invokevirtual 347	java/io/FileOutputStream:close	()V
    //   125: aload 4
    //   127: areturn
    //   128: aload_1
    //   129: invokevirtual 309	android/graphics/Bitmap:getHeight	()I
    //   132: i2f
    //   133: fstore_2
    //   134: goto -117 -> 17
    //   137: aload_1
    //   138: invokevirtual 306	android/graphics/Bitmap:getWidth	()I
    //   141: istore 6
    //   143: iload 6
    //   145: i2f
    //   146: fstore_2
    //   147: goto -107 -> 40
    //   150: astore_1
    //   151: aload_1
    //   152: invokevirtual 350	java/lang/Exception:printStackTrace	()V
    //   155: aconst_null
    //   156: areturn
    //   157: astore_1
    //   158: goto -7 -> 151
    //
    // Exception table:
    //   from	to	target	type
    //   0	17	150	java/lang/Exception
    //   17	40	150	java/lang/Exception
    //   48	54	150	java/lang/Exception
    //   54	92	150	java/lang/Exception
    //   128	134	150	java/lang/Exception
    //   137	143	150	java/lang/Exception
    //   92	125	157	java/lang/Exception
  }

  private TextView setDPbasicItemProfileView(int paramInt)
  {
    DPBasicItem localDPBasicItem = (DPBasicItem)findViewById(paramInt);
    localDPBasicItem.setOnClickListener(this);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams.weight = 1.0F;
    localDPBasicItem.getItemSubtitle().setLayoutParams(localLayoutParams);
    localDPBasicItem.getItemSubtitle().setGravity(5);
    localDPBasicItem.itemTitle().setTextColor(getResources().getColor(R.color.deep_gray));
    localDPBasicItem.getItemSubtitle().setTextColor(getResources().getColor(R.color.editprofile_blue));
    localDPBasicItem.itemTitle().setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_16));
    localDPBasicItem.getItemSubtitle().setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
    localDPBasicItem.getItemCount().setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
    return localDPBasicItem.getItemSubtitle();
  }

  private void setTextBlue(TextView paramTextView, String paramString)
  {
    paramTextView.setText(paramString);
    paramTextView.setTextColor(getResources().getColor(R.color.editprofile_blue));
  }

  private void setTextGray(TextView paramTextView, String paramString)
  {
    paramTextView.setText(paramString);
    paramTextView.setTextColor(getResources().getColor(R.color.editprofile_gray));
  }

  private void setTextOrange(TextView paramTextView, String paramString)
  {
    paramTextView.setText(paramString);
    paramTextView.setTextColor(getResources().getColor(R.color.editprofile_orange));
  }

  private void updateProfile()
  {
    Object localObject1 = getAccount();
    try
    {
      Object localObject2 = ((UserProfile)localObject1).getUserLevel();
      this.userLevelUrl = ((UserLevel)localObject2).getUrl();
      this.userLevelItem.getItemLeft1stPic().setVisibility(0);
      this.userLevel.setText(((UserLevel)localObject2).getTitle());
      this.userLevelItem.getItemLeft1stPic().setImage(((UserLevel)localObject2).getPic());
      localObject2 = accountService().profile();
      boolean bool1 = ((DPObject)localObject2).getBoolean("NeedSetAvatar");
      boolean bool2 = ((DPObject)localObject2).getBoolean("NeedSetName");
      if (bool1)
        setTextBlue(this.tv_icon_setting, "设置头像");
      while (bool2)
      {
        ((LinearLayout.LayoutParams)(LinearLayout.LayoutParams)this.nickNameItem.getItemSubtitle().getLayoutParams()).weight = 0.0F;
        this.nickNameItem.getItemSubtitle().setGravity(3);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
        this.nickNameItem.getItemCount().setGravity(5);
        this.nickNameItem.getItemCount().setLayoutParams(localLayoutParams);
        this.nickNameItem.getItemCount().setVisibility(0);
        setTextBlue(this.nickNameItem.getItemCount(), "修改");
        localObject2 = ((DPObject)localObject2).getObject("UserGrade");
        setTextGray(this.userGrade, ((DPObject)localObject2).getString("Desc"));
        this.userGradeUrl = ((DPObject)localObject2).getString("Url");
        if (localObject1 != null)
        {
          this.icon.setImage(((UserProfile)localObject1).avatarBig());
          setTextGray(this.nickName, ((UserProfile)localObject1).nickName());
          localObject2 = CityUtils.getCityById(((UserProfile)localObject1).cityId());
          if (localObject2 != null)
            break label629;
          setTextBlue(this.address, "设置");
          if (!this.isCityChange)
          {
            this.newCityId = ((UserProfile)localObject1).cityId();
            this.oldCityId = this.newCityId;
          }
          if (((UserProfile)localObject1).gender() != 1)
            break label644;
          setTextGray(this.sex, "男");
          this.genderid = ((UserProfile)localObject1).gender();
          this.oldGenderid = ((UserProfile)localObject1).gender();
          if (!this.isPhoneChanged)
            setProtectPhoneNum(((UserProfile)localObject1).grouponPhone());
          localObject1 = ((UserProfile)localObject1).getAccountBindResult();
          if (localObject1 != null)
          {
            this.bindlist = ((AccountBindResult)localObject1).getBindList();
            int j = 2;
            int n = 2;
            int k = j;
            int m = n;
            if (this.bindlist != null)
            {
              int i = j;
              if (this.bindlist[0] != null)
              {
                i = j;
                if ("qq".equals("" + this.bindlist[0].getName()))
                  i = this.bindlist[0].getState();
              }
              k = i;
              m = n;
              if (this.bindlist[1] != null)
              {
                k = i;
                m = n;
                if ("weixin".equals("" + this.bindlist[1].getName()))
                {
                  k = i;
                  m = n;
                  if (this.bindlist[1].getState() == 1)
                  {
                    m = this.bindlist[1].getState();
                    k = i;
                  }
                }
              }
            }
            updateThirdIcons(k, m);
          }
        }
        return;
        setTextBlue(this.tv_icon_setting, "修改头像");
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        Log.e(localException.toString());
        continue;
        ((LinearLayout.LayoutParams)(LinearLayout.LayoutParams)this.nickNameItem.getItemSubtitle().getLayoutParams()).weight = 1.0F;
        this.nickNameItem.getItemSubtitle().setGravity(5);
        this.nickNameItem.getItemCount().setVisibility(8);
        continue;
        label629: setTextGray(this.address, localException.name());
        continue;
        label644: if (((UserProfile)localObject1).gender() == 2)
        {
          setTextGray(this.sex, "女");
          continue;
        }
        setTextBlue(this.sex, "设置");
      }
    }
  }

  private void updateThirdIcons(int paramInt1, int paramInt2)
  {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (getAccount() != null)
    {
      bool1 = bool2;
      if (getAccount().getAccountBindResult() != null)
        bool1 = getAccount().getAccountBindResult().getShouldShow().booleanValue();
    }
    if (bool1)
    {
      if ((paramInt1 != 2) || (paramInt2 != 2))
        break label147;
      this.thirdBindItem.getItemLeft1stPic().setVisibility(0);
      this.thirdBindItem.getItemLeft2ndPic().setVisibility(0);
      this.thirdBindItem.getItemLeft1stPic().setLocalDrawable(getResources().getDrawable(R.drawable.account_gray_weixin));
      this.thirdBindItem.getItemLeft2ndPic().setImageResource(R.drawable.account_gray_qq);
      this.thirdBindItem.getItemSubtitle().setText("立即绑定");
      this.thirdBindItem.getItemRight1stPic().setVisibility(8);
      this.thirdBindItem.getItemRight2ndPic().setVisibility(8);
    }
    label147: 
    do
    {
      return;
      this.thirdBindItem.getItemLeft1stPic().setVisibility(8);
      this.thirdBindItem.getItemLeft2ndPic().setVisibility(8);
      this.thirdBindItem.getItemSubtitle().setText("");
      this.thirdBindItem.getItemRight1stPic().setVisibility(0);
      this.thirdBindItem.getItemRight2ndPic().setVisibility(0);
      if (paramInt2 == 1)
        this.thirdBindItem.getItemRight1stPic().setImageResource(R.drawable.share_to_icon_wx);
      while (paramInt1 == 1)
      {
        this.thirdBindItem.getItemRight2ndPic().setImageResource(R.drawable.share_to_icon_qq);
        return;
        if (paramInt2 != 2)
          continue;
        this.thirdBindItem.getItemRight1stPic().setImageResource(R.drawable.account_gray_weixin);
      }
    }
    while (paramInt1 != 2);
    this.thirdBindItem.getItemRight2ndPic().setImageResource(R.drawable.account_gray_qq);
  }

  public void addAvatarRequest(String paramString, InputStream paramInputStream)
  {
    if (this.uploadPhotoRequest != null)
      mapiService().abort(this.uploadPhotoRequest, this, true);
    Object localObject1 = new Random(System.currentTimeMillis());
    localObject1 = "----------ANDRIOD_" + Long.toString(((Random)localObject1).nextLong());
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("connection", "keep-alive"));
    localArrayList.add(new BasicNameValuePair("Charsert", "UTF-8"));
    localArrayList.add(new BasicNameValuePair("Content-Type", "multipart/form-data; boundary=" + (String)localObject1));
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("--").append((String)localObject1).append("\r\n");
    ((StringBuilder)localObject2).append("Content-Disposition: form-data; name=\"UploadFile2\"; filename=\"dianping_upload.jpg\"").append("\r\n");
    ((StringBuilder)localObject2).append("Content-Type: image/jpeg\r\n");
    ((StringBuilder)localObject2).append("\r\n");
    localObject2 = new StringInputStream(((StringBuilder)localObject2).toString());
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("\r\n");
    ((StringBuilder)localObject3).append("--").append((String)localObject1).append("\r\n");
    ((StringBuilder)localObject3).append("Content-Disposition: form-data; name=\"be_data\"; filename=\"be_data\"").append("\r\n");
    ((StringBuilder)localObject3).append("Content-Type: application/octet-stream\r\n");
    ((StringBuilder)localObject3).append("Content-Transfer-Encoding: binary\r\n");
    ((StringBuilder)localObject3).append("\r\n");
    localObject3 = new StringInputStream(((StringBuilder)localObject3).toString());
    paramString = new MApiFormInputStream(new String[] { "token", paramString });
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("\r\n");
    localStringBuilder.append("--").append((String)localObject1).append("--").append("\r\n");
    this.uploadPhotoRequest = new BasicMApiRequest("http://m.api.dianping.com/addavatar.bin", "POST", new ChainInputStream(new InputStream[] { localObject2, paramInputStream, localObject3, paramString, new StringInputStream(localStringBuilder.toString()) }), CacheType.DISABLED, false, localArrayList);
    mapiService().exec(this.uploadPhotoRequest, this);
  }

  void doLogout()
  {
    FavoriteHelper.delAllFavoriteShops();
    statisticsEvent("profile5", "profile5_quit", "", 0);
    accountService().logout();
    finish();
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void logout()
  {
    new AlertDialog.Builder(this).setTitle("提示").setMessage("确认退出该账号吗？").setPositiveButton("退出", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        EditProfileActivity.this.doLogout();
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
      }
    }).show();
  }

  public void onAccountInfoChanged(UserProfile paramUserProfile)
  {
    super.onAccountInfoChanged(paramUserProfile);
  }

  // ERROR //
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    // Byte code:
    //   0: iload_2
    //   1: iconst_m1
    //   2: if_icmpne +38 -> 40
    //   5: iload_1
    //   6: lookupswitch	default:+34->40, 1:+42->48, 1000:+237->243, 3025:+145->151
    //   41: iload_1
    //   42: iload_2
    //   43: aload_3
    //   44: invokespecial 801	com/dianping/base/app/NovaActivity:onActivityResult	(IILandroid/content/Intent;)V
    //   47: return
    //   48: aload_3
    //   49: ifnull -9 -> 40
    //   52: aload_3
    //   53: ldc_w 803
    //   56: invokevirtual 807	android/content/Intent:getParcelableExtra	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   59: checkcast 571	com/dianping/model/City
    //   62: astore 4
    //   64: aload 4
    //   66: ifnull +63 -> 129
    //   69: aload 4
    //   71: invokevirtual 810	com/dianping/model/City:id	()I
    //   74: ifle +55 -> 129
    //   77: aload 4
    //   79: invokevirtual 810	com/dianping/model/City:id	()I
    //   82: istore 5
    //   84: aload 4
    //   86: invokevirtual 573	com/dianping/model/City:name	()Ljava/lang/String;
    //   89: astore 4
    //   91: iload 5
    //   93: ifle -53 -> 40
    //   96: iload 5
    //   98: aload_0
    //   99: getfield 513	com/dianping/main/user/activity/EditProfileActivity:oldCityId	I
    //   102: if_icmpeq -62 -> 40
    //   105: aload_0
    //   106: iload 5
    //   108: putfield 511	com/dianping/main/user/activity/EditProfileActivity:newCityId	I
    //   111: aload_0
    //   112: aload 4
    //   114: putfield 812	com/dianping/main/user/activity/EditProfileActivity:mNewCityName	Ljava/lang/String;
    //   117: aload_0
    //   118: iconst_1
    //   119: putfield 509	com/dianping/main/user/activity/EditProfileActivity:isCityChange	Z
    //   122: aload_0
    //   123: invokevirtual 815	com/dianping/main/user/activity/EditProfileActivity:updateUserProfile	()V
    //   126: goto -86 -> 40
    //   129: aload_3
    //   130: ldc_w 816
    //   133: iconst_0
    //   134: invokevirtual 820	android/content/Intent:getIntExtra	(Ljava/lang/String;I)I
    //   137: istore 5
    //   139: aload_3
    //   140: ldc_w 822
    //   143: invokevirtual 825	android/content/Intent:getStringExtra	(Ljava/lang/String;)Ljava/lang/String;
    //   146: astore 4
    //   148: goto -57 -> 91
    //   151: iload_2
    //   152: iconst_m1
    //   153: if_icmpne -113 -> 40
    //   156: aload_3
    //   157: ifnull -117 -> 40
    //   160: aload_0
    //   161: aload_3
    //   162: ldc_w 827
    //   165: invokevirtual 807	android/content/Intent:getParcelableExtra	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   168: checkcast 302	android/graphics/Bitmap
    //   171: putfield 829	com/dianping/main/user/activity/EditProfileActivity:showBitmap	Landroid/graphics/Bitmap;
    //   174: aload_0
    //   175: getfield 829	com/dianping/main/user/activity/EditProfileActivity:showBitmap	Landroid/graphics/Bitmap;
    //   178: ifnull -138 -> 40
    //   181: aload_0
    //   182: aload_0
    //   183: aload_0
    //   184: getfield 829	com/dianping/main/user/activity/EditProfileActivity:showBitmap	Landroid/graphics/Bitmap;
    //   187: invokespecial 831	com/dianping/main/user/activity/EditProfileActivity:makeUploadPhoto	(Landroid/graphics/Bitmap;)Ljava/io/File;
    //   190: putfield 833	com/dianping/main/user/activity/EditProfileActivity:image	Ljava/io/File;
    //   193: aload_0
    //   194: getfield 833	com/dianping/main/user/activity/EditProfileActivity:image	Ljava/io/File;
    //   197: ifnull -157 -> 40
    //   200: new 835	java/io/FileInputStream
    //   203: dup
    //   204: aload_0
    //   205: getfield 833	com/dianping/main/user/activity/EditProfileActivity:image	Ljava/io/File;
    //   208: invokespecial 836	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   211: astore 4
    //   213: aload_0
    //   214: ldc_w 838
    //   217: invokevirtual 841	com/dianping/main/user/activity/EditProfileActivity:showProgressDialog	(Ljava/lang/String;)V
    //   220: aload_0
    //   221: aload_0
    //   222: invokevirtual 843	com/dianping/main/user/activity/EditProfileActivity:token	()Ljava/lang/String;
    //   225: aload 4
    //   227: invokevirtual 845	com/dianping/main/user/activity/EditProfileActivity:addAvatarRequest	(Ljava/lang/String;Ljava/io/InputStream;)V
    //   230: goto -190 -> 40
    //   233: astore 4
    //   235: aload 4
    //   237: invokevirtual 846	java/io/FileNotFoundException:printStackTrace	()V
    //   240: goto -200 -> 40
    //   243: iload_2
    //   244: iconst_m1
    //   245: if_icmpne -205 -> 40
    //   248: aload_3
    //   249: ldc_w 848
    //   252: invokevirtual 852	android/content/Intent:getStringArrayListExtra	(Ljava/lang/String;)Ljava/util/ArrayList;
    //   255: astore 4
    //   257: aload 4
    //   259: invokevirtual 855	java/util/ArrayList:size	()I
    //   262: ifle -222 -> 40
    //   265: aload_0
    //   266: aload 4
    //   268: iconst_0
    //   269: invokevirtual 859	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   272: checkcast 549	java/lang/String
    //   275: invokestatic 865	com/dianping/base/util/ImageUtils:doCropPhoto	(Landroid/app/Activity;Ljava/lang/String;)V
    //   278: goto -238 -> 40
    //   281: astore 4
    //   283: goto -48 -> 235
    //
    // Exception table:
    //   from	to	target	type
    //   193	213	233	java/io/FileNotFoundException
    //   213	230	281	java/io/FileNotFoundException
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.img)
      SelectPhotoUtil.selectPhoto(this, 1);
    do
    {
      while (true)
      {
        return;
        if (i == R.id.residence)
        {
          this.oldCityId = this.newCityId;
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectcity"));
          paramView.putExtra("isGetCity", true);
          startActivityForResult(paramView, 1);
          return;
        }
        if (i == R.id.sex)
        {
          this.oldGenderid = this.genderid;
          paramView = new AlertDialog.Builder(this);
          paramView.setTitle("选择性别");
          2 local2 = new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              if (paramInt == 0)
                EditProfileActivity.this.genderid = 2;
              if (paramInt == 1)
                EditProfileActivity.this.genderid = 1;
              if (EditProfileActivity.this.oldGenderid != EditProfileActivity.this.genderid)
              {
                EditProfileActivity.this.isSexChange = true;
                EditProfileActivity.this.updateUserProfile();
              }
            }
          };
          paramView.setItems(new String[] { "女", "男" }, local2);
          paramView.show();
          return;
        }
        if (i == R.id.name)
        {
          startActivity("dianping://editnickname");
          return;
        }
        if (i == R.id.telephone)
        {
          startActivity("dianping://modifyphone?url=http://m.dianping.com/account/modifymobile?newtoken=!&version=*");
          RedAlertManager.getInstance().updateLocalRedAlert(EditProfileActivity.class.getName());
          return;
        }
        if (i == R.id.openid)
        {
          gotoThirdPartyBind();
          return;
        }
        if (i == R.id.password)
        {
          startActivity("dianping://complexweb?url=http://m.dianping.com/account/modifypassword?newtoken=!&version=*");
          return;
        }
        if (i == R.id.vip)
        {
          if (this.userLevelUrl.isEmpty())
            continue;
          startActivity(this.userLevelUrl);
          return;
        }
        if (i != R.id.grade)
          break;
        if (this.userGradeUrl.isEmpty())
          continue;
        startActivity(this.userGradeUrl);
        return;
      }
      if (i != R.id.exitid)
        continue;
      logout();
      return;
    }
    while (i != R.id.address);
    startActivity("dianping://deliverylist");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.edit_profile);
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.dianping.action.PROFILE_EDIT");
    localIntentFilter.addAction("phoneChanged");
    localIntentFilter.addAction("AccountBindChange");
    registerReceiver(this.receiver, localIntentFilter);
    initView();
    if (paramBundle != null)
    {
      this.tuanProfile = ((DPObject)paramBundle.getParcelable("tuanProfile"));
      onUpdateTuanProfile(this.tuanProfile);
      return;
    }
    updateTuanProfile();
  }

  protected void onDestroy()
  {
    unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.updateProfileReq)
    {
      this.updateProfileReq = null;
      dismissDialog();
      if (this.isSexChange)
      {
        this.isSexChange = false;
        this.genderid = this.oldGenderid;
      }
      if (this.isCityChange)
      {
        this.isCityChange = false;
        this.newCityId = this.oldCityId;
        this.mNewCityName = null;
      }
      if (paramMApiResponse.message() != null)
        showMessageDialog(paramMApiResponse.message());
    }
    do
    {
      return;
      showToast("网络不给力哦，修改失败");
      return;
    }
    while (paramMApiRequest != this.uploadPhotoRequest);
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest != null)
      showToast(paramMApiRequest.getString("Content"));
    while (true)
    {
      dismissDialog();
      this.uploadPhotoRequest = null;
      return;
      showToast("上传头像失败,请重试");
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.updateProfileReq) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      this.updateProfileReq = null;
      dismissDialog();
      if (this.isSexChange)
      {
        this.isSexChange = false;
        this.oldGenderid = this.genderid;
        paramMApiResponse = this.sex;
        if (this.genderid == 1)
        {
          paramMApiRequest = "男";
          setTextGray(paramMApiResponse, paramMApiRequest);
          showToast("性别修改成功");
          paramMApiRequest = new Intent("com.dianping.action.PROFILE_EDIT");
          paramMApiRequest.putExtra("newUserGenderid", this.genderid);
          sendBroadcast(paramMApiRequest);
        }
      }
      else if (this.isCityChange)
      {
        this.isCityChange = false;
        showToast("居住城市修改成功");
        paramMApiRequest = new Intent("com.dianping.action.RESIDENCE_CHANGE");
        if (!TextUtils.isEmpty(this.mNewCityName))
        {
          setTextGray(this.address, this.mNewCityName);
          paramMApiRequest.putExtra("cityId", this.newCityId);
          paramMApiRequest.putExtra("cityName", this.mNewCityName);
          paramMApiRequest.putExtra("oldCityId", this.oldCityId);
        }
        sendBroadcast(paramMApiRequest);
      }
    }
    do
    {
      return;
      paramMApiRequest = "女";
      break;
    }
    while (paramMApiRequest != this.uploadPhotoRequest);
    if (((paramMApiResponse.result() instanceof DPObject)) && (this.showBitmap != null))
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
      {
        statisticsEvent("profile5", "profile5_photo_save", "", 0);
        showToast(paramMApiRequest.getString("Content"));
      }
      this.icon.setLocalBitmap(this.showBitmap);
      sendBroadcast(new Intent("com.dianping.main.user.UPDATE_AVATAR"));
      sendBroadcast(new Intent("com.dianping.action.USER_EDIT"));
      this.showBitmap = null;
      if (this.image != null)
      {
        this.image.delete();
        this.image = null;
      }
    }
    dismissDialog();
    this.uploadPhotoRequest = null;
  }

  public void onResume()
  {
    super.onResume();
    updateProfile();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelable("tuanProfile", this.tuanProfile);
    super.onSaveInstanceState(paramBundle);
  }

  protected void onUpdateTuanProfile(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    if (!TextUtils.isEmpty(paramDPObject.getString("MobilePhone")))
    {
      this.bindPhoneNo = paramDPObject.getString("MobilePhone");
      setProtectPhoneNum(paramDPObject.getString("MobilePhoneMasked"));
      accountService().update(accountService().profile().edit().putString("GrouponPhone", this.bindPhoneNo).generate());
      return;
    }
    this.bindPhoneNo = null;
    setProtectPhoneNum(null);
  }

  void setProtectPhoneNum(String paramString)
  {
    this.bindPhoneNo = paramString;
    int i = 0;
    if (TextUtils.isEmpty(paramString))
    {
      if (RedAlertManager.getInstance().checkLocalRedAlertByTag(EditProfileActivity.class.getName()))
      {
        paramString = getResources().getDrawable(R.drawable.red_dot);
        paramString.setBounds(0, 0, ViewUtils.dip2px(this, 8.0F), ViewUtils.dip2px(this, 8.0F));
        this.phoneNum.setCompoundDrawablePadding(ViewUtils.dip2px(this, 5.0F));
        this.phoneNum.setCompoundDrawables(null, null, paramString, null);
        setTextGray(this.phoneNum, "为了您的安全请立即绑定");
        i = 1;
      }
      while (true)
      {
        if (i != 0)
        {
          ((LinearLayout.LayoutParams)(LinearLayout.LayoutParams)this.nickNameItem.getItemSubtitle().getLayoutParams()).weight = 1.0F;
          this.phoneNumberItem.getItemSubtitle().setGravity(5);
          this.phoneNumberItem.getItemCount().setVisibility(8);
        }
        return;
        this.phoneNum.setCompoundDrawables(null, null, null, null);
        setTextOrange(this.phoneNumberItem.getItemSubtitle(), "安全等级低");
        ((LinearLayout.LayoutParams)(LinearLayout.LayoutParams)this.phoneNumberItem.getItemSubtitle().getLayoutParams()).weight = 0.0F;
        this.phoneNumberItem.getItemSubtitle().setGravity(3);
        paramString = new LinearLayout.LayoutParams(-2, -2);
        this.phoneNumberItem.getItemCount().setGravity(5);
        this.phoneNumberItem.getItemCount().setLayoutParams(paramString);
        this.phoneNumberItem.getItemCount().setVisibility(0);
        setTextBlue(this.phoneNumberItem.getItemCount(), "立即绑定");
      }
    }
    this.phoneNum.setCompoundDrawables(null, null, null, null);
    if ((!TextUtils.isEmpty(paramString)) && (paramString.length() == 11))
      setTextGray(this.phoneNum, paramString.substring(0, 3) + "****" + paramString.substring(7));
    while (true)
    {
      i = 1;
      break;
      setTextGray(this.phoneNum, paramString);
    }
  }

  public String token()
  {
    if (accountService().token() == null)
      return "";
    return accountService().token();
  }

  void updateThirdBind(String paramString)
  {
    int k = 0;
    int i = 0;
    int n = 0;
    int j = k;
    try
    {
      Object localObject = new JSONObject(paramString).getJSONArray("BindList");
      j = k;
      paramString = ((JSONArray)localObject).getJSONObject(1);
      j = k;
      localObject = ((JSONArray)localObject).getJSONObject(0);
      j = k;
      if (paramString.getString("Name").equals("qq"))
      {
        j = k;
        i = paramString.getInt("State");
      }
      k = i;
      m = n;
      j = i;
      if (((JSONObject)localObject).getString("Name").equals("weixin"))
      {
        j = i;
        m = ((JSONObject)localObject).getInt("State");
        k = i;
      }
      updateThirdIcons(k, m);
      return;
    }
    catch (JSONException paramString)
    {
      while (true)
      {
        paramString.printStackTrace();
        k = j;
        int m = n;
      }
    }
  }

  protected void updateTuanProfile()
  {
    this.phoneNum.setText("正在获取绑定的手机号...");
    mapiService().exec(BasicMApiRequest.mapiGet("http://app.t.dianping.com/tuanprofilegn.bin?token=" + accountService().token(), CacheType.CRITICAL), new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        try
        {
          EditProfileActivity.this.tuanProfile = ((DPObject)paramMApiResponse.result());
          EditProfileActivity.this.onUpdateTuanProfile(EditProfileActivity.this.tuanProfile);
          return;
        }
        catch (Exception paramMApiRequest)
        {
        }
      }
    });
  }

  void updateUserProfile()
  {
    if (this.updateProfileReq != null)
      mapiService().abort(this.updateProfileReq, this, true);
    this.updateProfileReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/updateprofile.bin", new String[] { "token", accountService().token(), "gender", this.genderid + "", "cityid", this.newCityId + "", "nickname", this.nickName.getText().toString() });
    mapiService().exec(this.updateProfileReq, this);
    showProgressDialog("正在修改请稍后...");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.EditProfileActivity
 * JD-Core Version:    0.6.0
 */
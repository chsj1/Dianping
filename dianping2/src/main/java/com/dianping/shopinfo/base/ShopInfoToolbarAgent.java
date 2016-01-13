package com.dianping.shopinfo.base;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.widget.ToolbarButton;
import com.dianping.base.widget.ToolbarImageButton;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ShopInfoToolbarAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String ACTION_ADD_REVIEW = "com.dianping.ugc.addreview";
  private static final int REQUEST_SELECT_GPS = 30438;
  private ToolbarButton mAddReviewButton;
  private ToolbarButton mCheckInButton;
  private MApiRequest mCheckinReq;
  private Handler mHandler;
  private MApiRequest mLocationErrorReq;
  private BroadcastReceiver mReceiver;
  private SharedPreferences mSP = null;
  protected HashMap<String, ToolbarButton> mToolBarButtonOrder = new HashMap();
  private ToolbarButton uploadPhotoButton;

  public ShopInfoToolbarAgent(Object paramObject)
  {
    super(paramObject);
    getToolbarView().setVisibility(0);
    this.mHandler = new Handler();
  }

  private void addReviewClickAction()
  {
    DPObject localDPObject1 = getShop();
    if (localDPObject1 == null)
      return;
    switch (localDPObject1.getInt("Status"))
    {
    case 2:
    case 3:
    default:
      Bundle localBundle = new Bundle();
      localBundle.putParcelable("shop", localDPObject1);
      if (super.isBeautyHairType())
      {
        DPObject localDPObject2 = (DPObject)super.getSharedObject("beautyShopBasicInfo");
        if (localDPObject2 != null)
          localBundle.putParcelable("beautyShopBasicInfo", localDPObject2);
      }
      AddReviewUtil.addReview(getContext(), localDPObject1.getInt("ID"), localDPObject1.getString("Name"), localBundle);
      return;
    case 1:
    case 4:
    }
    Toast.makeText(getContext(), "暂停收录点评", 0).show();
  }

  private void chooseLocation()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://maperrroreporter"));
    Object localObject = getShop();
    localIntent.putExtra("shop", (Parcelable)localObject);
    localIntent.putExtra("latitude", ((DPObject)localObject).getDouble("Latitude"));
    localIntent.putExtra("longitude", ((DPObject)localObject).getDouble("Longitude"));
    localIntent.putExtra("enableSelect", true);
    String str1 = ((DPObject)localObject).getString("Name");
    String str2 = ((DPObject)localObject).getString("BranchName");
    localObject = new StringBuilder().append(str1);
    if ((str2 == null) || (str2.length() == 0));
    for (str1 = ""; ; str1 = "(" + str2 + ")")
    {
      localIntent.putExtra("name", str1);
      getFragment().startActivityForResult(localIntent, 30438);
      return;
    }
  }

  private void removeAddReviewMessage()
  {
    ViewGroup localViewGroup = getToolbarView();
    int i = 0;
    while (i < localViewGroup.getChildCount())
    {
      if ((localViewGroup.getChildAt(i) instanceof ToolbarButton))
      {
        Object localObject = (ToolbarButton)localViewGroup.getChildAt(i);
        TextView localTextView = (TextView)((ToolbarButton)localObject).findViewById(16908308);
        if ((localObject != null) && (TextUtils.equals("写点评", localTextView.getText())))
        {
          localObject = (TextView)((ToolbarButton)localObject).findViewById(R.id.message);
          if (localObject != null)
            ((TextView)localObject).setVisibility(8);
        }
      }
      i += 1;
    }
  }

  private void reportLocation()
  {
    GAHelper.instance().contextStatisticsEvent(getContext(), "distanceerror", getGAExtra(), "tap");
    sendReport(2, 0.0D, 0.0D);
  }

  private void sendCheckinRequest()
  {
    int i = shopId();
    double d1 = 0.0D;
    double d2 = 0.0D;
    DPObject localDPObject = getFragment().locationService().location();
    if (localDPObject != null)
    {
      d1 = localDPObject.getDouble("Lat");
      d2 = localDPObject.getDouble("Lng");
    }
    this.mCheckinReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/checkin/addnewcheckin.bin", new String[] { "shopid", String.valueOf(i), "lng", Location.FMT.format(d2), "lat", Location.FMT.format(d1) });
    super.getFragment().mapiService().exec(this.mCheckinReq, this);
  }

  private void sendReport(int paramInt, double paramDouble1, double paramDouble2)
  {
    String str = "http://m.api.dianping.com/addshopfeedback.bin?flag=" + paramInt + "&shopid=" + shopId();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("flag");
    localArrayList.add(String.valueOf(paramInt));
    localArrayList.add("shopid");
    localArrayList.add(String.valueOf(shopId()));
    AccountService localAccountService = getFragment().accountService();
    Object localObject1 = localAccountService.profile();
    if (localObject1 == null);
    for (localObject1 = null; ; localObject1 = ((DPObject)localObject1).getString("Email"))
    {
      localArrayList.add("email");
      Object localObject2 = localObject1;
      if (TextUtils.isEmpty((CharSequence)localObject1))
        localObject2 = Environment.imei();
      localArrayList.add(localObject2);
      if (!TextUtils.isEmpty(localAccountService.token()))
      {
        localArrayList.add("token");
        localArrayList.add(localAccountService.token());
      }
      localArrayList.add("lat");
      localArrayList.add(String.valueOf(paramDouble1));
      localArrayList.add("lng");
      localArrayList.add(String.valueOf(paramDouble2));
      localArrayList.add("callid");
      localArrayList.add(UUID.randomUUID().toString());
      this.mLocationErrorReq = BasicMApiRequest.mapiPost(str, (String[])localArrayList.toArray(new String[localArrayList.size()]));
      if ((getFragment() != null) && (getFragment().mapiService() != null))
        getFragment().mapiService().exec(this.mLocationErrorReq, this);
      return;
    }
  }

  private void sendToolbarMessage(DPObject[] paramArrayOfDPObject)
  {
    boolean bool = this.mSP.getBoolean("isFirstCheckin", true);
    ToolbarButton localToolbarButton = (ToolbarButton)this.mToolBarButtonOrder.get("4CheckIn");
    Object localObject1;
    if (localToolbarButton != null)
    {
      localObject1 = (TextView)localToolbarButton.findViewById(R.id.message);
      if (localObject1 != null)
      {
        if (bool != true)
          break label334;
        ((TextView)localObject1).setText("NEW");
        ((TextView)localObject1).setVisibility(0);
      }
    }
    int i = 0;
    while (true)
    {
      if ((paramArrayOfDPObject == null) || (i >= paramArrayOfDPObject.length))
        return;
      Object localObject2 = paramArrayOfDPObject[i];
      localObject1 = ((DPObject)localObject2).getString("Notice");
      String str = ((DPObject)localObject2).getString("Name");
      int j = ((DPObject)localObject2).getInt("Status");
      int k = ((DPObject)localObject2).getInt("Type");
      if ((k == 3) && (j == 1) && (localToolbarButton != null))
      {
        ((ToolbarImageButton)localToolbarButton.findViewById(16908294)).setImageDrawable(this.res.getDrawable(R.drawable.shop_footerbar_locate_checked));
        localObject2 = (TextView)localToolbarButton.findViewById(16908308);
        ((TextView)localObject2).setText("已签到");
        ((TextView)localObject2).setTextColor(this.res.getColor(R.color.review_event_text_color));
      }
      localObject2 = getToolbarView();
      j = 0;
      label205: if (j < ((ViewGroup)localObject2).getChildCount())
      {
        Object localObject3;
        if ((((ViewGroup)localObject2).getChildAt(j) instanceof ToolbarButton))
        {
          localObject3 = (ToolbarButton)((ViewGroup)localObject2).getChildAt(j);
          TextView localTextView = (TextView)((ToolbarButton)localObject3).findViewById(16908308);
          if ((localObject3 != null) && (str != null) && (str.equals(localTextView.getText())) && (!TextUtils.isEmpty((CharSequence)localObject1)) && ((k != 3) || (bool != true)))
          {
            localObject3 = (TextView)((ToolbarButton)localObject3).findViewById(R.id.message);
            if (localObject3 != null)
            {
              if (bool != true)
                break label343;
              ((TextView)localObject3).setVisibility(8);
            }
          }
        }
        while (true)
        {
          j += 1;
          break label205;
          label334: ((TextView)localObject1).setVisibility(8);
          break;
          label343: ((TextView)localObject3).setText((CharSequence)localObject1);
          ((TextView)localObject3).setVisibility(0);
        }
      }
      i += 1;
    }
  }

  private void showCheckinButtonClickAction(DPObject paramDPObject)
  {
    int i = 3;
    if (paramDPObject != null)
      i = paramDPObject.getInt("Status");
    if (i == 0)
    {
      localObject = (ToolbarButton)this.mToolBarButtonOrder.get("4CheckIn");
      if (localObject != null)
      {
        ((ToolbarImageButton)((ToolbarButton)localObject).findViewById(16908294)).setImageDrawable(this.res.getDrawable(R.drawable.shop_footerbar_locate_checked));
        TextView localTextView = (TextView)((ToolbarButton)localObject).findViewById(16908308);
        localTextView.setText("已签到");
        localTextView.setTextColor(this.res.getColor(R.color.review_event_text_color));
        ((ToolbarButton)localObject).findViewById(R.id.message).setVisibility(8);
      }
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://visitedlist"));
      ((Intent)localObject).putExtra("shopid", shopId());
      ((Intent)localObject).putExtra("checkinsuccmsg", paramDPObject);
      getContext().startActivity((Intent)localObject);
    }
    do
    {
      return;
      if (i == 1)
      {
        localObject = paramDPObject.getString("Notice");
        paramDPObject = (DPObject)localObject;
        if (TextUtils.isEmpty((CharSequence)localObject))
          paramDPObject = "您的位置离商户较远，请到店内再次尝试~";
        new AlertDialog.Builder(getContext()).setTitle("距离较远").setMessage(paramDPObject).setNegativeButton("定位报错", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            ShopInfoToolbarAgent.this.reportLocation();
          }
        }).setPositiveButton("好的", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            GAHelper.instance().contextStatisticsEvent(ShopInfoToolbarAgent.this.getContext(), "distancetip", ShopInfoToolbarAgent.this.getGAExtra(), "tap");
          }
        }).show();
        return;
      }
      if (i != 2)
        continue;
      localObject = paramDPObject.getString("Notice");
      paramDPObject = (DPObject)localObject;
      if (TextUtils.isEmpty((CharSequence)localObject))
        paramDPObject = "您刚签到过，十分钟后再来吧~";
      paramDPObject = Toast.makeText(getContext(), paramDPObject, 0);
      paramDPObject.setGravity(17, 0, 0);
      paramDPObject.show();
      return;
    }
    while (i != 3);
    Object localObject = paramDPObject.getString("Notice");
    paramDPObject = (DPObject)localObject;
    if (TextUtils.isEmpty((CharSequence)localObject))
      paramDPObject = "点小评头晕了~";
    paramDPObject = Toast.makeText(getContext(), paramDPObject, 0);
    paramDPObject.setGravity(17, 0, 0);
    paramDPObject.show();
  }

  public void addCheckinButton()
  {
    this.mCheckInButton = addToolbarButton("签到", this.res.getDrawable(R.drawable.shop_footerbar_locate), new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ShopInfoToolbarAgent.this.mSP.edit().putBoolean("isFirstCheckin", false).commit();
        paramView = (ToolbarButton)ShopInfoToolbarAgent.this.mToolBarButtonOrder.get("4CheckIn");
        if (paramView != null)
        {
          paramView = (TextView)paramView.findViewById(R.id.message);
          if ((paramView != null) && (TextUtils.equals(paramView.getText().toString(), "NEW")))
            paramView.setVisibility(8);
        }
        if (!ShopInfoToolbarAgent.this.isLogined())
        {
          ShopInfoToolbarAgent.this.accountService().login(new LoginResultListener()
          {
            public void onLoginCancel(AccountService paramAccountService)
            {
            }

            public void onLoginSuccess(AccountService paramAccountService)
            {
              ShopInfoToolbarAgent.this.sendCheckinRequest();
            }
          });
          return;
        }
        ShopInfoToolbarAgent.this.sendCheckinRequest();
      }
    }
    , "4CheckIn");
  }

  public void addReviewButton()
  {
    this.mAddReviewButton = addToolbarButton("写点评", this.res.getDrawable(R.drawable.detail_footerbar_icon_comment_u), new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ShopInfoToolbarAgent.this.addReviewClickAction();
      }
    }
    , "7Review");
    this.mAddReviewButton.setGAString("toreview", getGAExtra());
  }

  public ToolbarButton addToolbarButton(CharSequence paramCharSequence, Drawable paramDrawable, View.OnClickListener paramOnClickListener, String paramString)
  {
    if (this.mToolBarButtonOrder.containsKey(paramString))
      getToolbarView().removeView((View)this.mToolBarButtonOrder.get(paramString));
    ToolbarButton localToolbarButton = createToolbarItem();
    localToolbarButton.setTitle(paramCharSequence);
    localToolbarButton.setIcon(paramDrawable);
    localToolbarButton.setOnClickListener(paramOnClickListener);
    addToolbarButton(localToolbarButton, paramString);
    this.mToolBarButtonOrder.put(paramString, localToolbarButton);
    return localToolbarButton;
  }

  public void addToolbarButton(View paramView, String paramString)
  {
    paramView.setTag(paramString);
    int k = -1;
    int i = 0;
    int m = getToolbarView().getChildCount();
    int j;
    while (true)
    {
      j = k;
      View localView;
      if (paramString != null)
      {
        j = k;
        if (i < m)
        {
          localView = getToolbarView().getChildAt(i);
          if ((localView.getTag() instanceof String))
            break label77;
        }
      }
      for (j = i; ; j = i)
      {
        if (j >= 0)
          break label107;
        getToolbarView().addView(paramView);
        return;
        label77: if (paramString.compareTo((String)localView.getTag()) >= 0)
          break;
      }
      i += 1;
    }
    label107: getToolbarView().addView(paramView, j);
  }

  public void addUploadPhotoButton()
  {
    1 local1 = new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = ShopInfoToolbarAgent.this.getShop();
        UploadPhotoUtil.uploadShopPhoto(ShopInfoToolbarAgent.this.getContext(), paramView);
      }
    };
    this.uploadPhotoButton = addToolbarButton("传图片", this.res.getDrawable(R.drawable.detail_footerbar_icon_camera_u), local1, "6Photo");
    this.uploadPhotoButton.setGAString("toupload", getGAExtra());
  }

  public ToolbarButton createToolbarItem()
  {
    return (ToolbarButton)MyResources.getResource(CellAgent.class).inflate(getContext(), R.layout.toolbar_button, getToolbarView(), false);
  }

  public ViewGroup getToolbarView()
  {
    return this.shopInfoFragment.toolbarView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
      sendToolbarMessage((DPObject[])(DPObject[])paramBundle.getParcelableArray("dpActionList"));
    paramBundle = getShop();
    if (paramBundle != null);
    switch (paramBundle.getInt("Status"))
    {
    case 2:
    case 3:
    default:
      this.mAddReviewButton.setEnabled(true);
      this.uploadPhotoButton.setEnabled(true);
      return;
    case 1:
    case 4:
    }
    this.mAddReviewButton.setEnabled(false);
    this.uploadPhotoButton.setEnabled(false);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getToolbarView() != null)
      getToolbarView().removeAllViews();
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.ugc.addreview");
    this.mReceiver = new BroadcastReceiver()
    {
      public void onReceive(Context paramContext, Intent paramIntent)
      {
        if ((paramIntent.getAction().equals("com.dianping.ugc.addreview")) && (paramIntent.getParcelableExtra("success") != null))
          ShopInfoToolbarAgent.this.removeAddReviewMessage();
      }
    };
    LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mReceiver, paramBundle);
    this.mSP = getContext().getSharedPreferences("shop_checkin_firstshow", 0);
    addUploadPhotoButton();
    addReviewButton();
    addCheckinButton();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mCheckinReq != null)
    {
      getFragment().mapiService().abort(this.mCheckinReq, this, true);
      this.mCheckinReq = null;
    }
    if (this.mLocationErrorReq != null)
    {
      getFragment().mapiService().abort(this.mLocationErrorReq, this, true);
      this.mLocationErrorReq = null;
    }
    LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mReceiver);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mCheckinReq)
      this.mCheckinReq = null;
    if (paramMApiRequest == this.mLocationErrorReq)
      this.mLocationErrorReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mCheckinReq)
    {
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      if (paramMApiResponse != null)
      {
        showCheckinButtonClickAction(paramMApiResponse);
        this.mCheckinReq = null;
      }
    }
    if (paramMApiRequest == this.mLocationErrorReq)
    {
      paramMApiRequest = new AlertDialog.Builder(getContext());
      paramMApiRequest.setTitle("提示");
      paramMApiRequest.setMessage("已收到您的报错信息，是否要标注商户正确位置？");
      paramMApiRequest.setNegativeButton("确认", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          ShopInfoToolbarAgent.this.chooseLocation();
        }
      });
      paramMApiRequest.setPositiveButton("取消", null);
      paramMApiRequest.show();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.base.ShopInfoToolbarAgent
 * JD-Core Version:    0.6.0
 */
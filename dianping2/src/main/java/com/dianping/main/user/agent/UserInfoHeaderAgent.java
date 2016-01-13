package com.dianping.main.user.agent;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.util.RedAlertManager;
import com.dianping.base.widget.CircleImageView;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.content.CityUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.main.user.agent.app.UserAgent;
import com.dianping.model.City;
import com.dianping.model.UserProfile;
import com.dianping.util.Log;
import com.dianping.util.LoginUtils;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import java.util.ArrayList;

public class UserInfoHeaderAgent extends UserAgent
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>, AccountListener
{
  private static final String CELLNAME = "20UserInfoHeader.";
  private static final int REQUESTCODE_HOME_CITY = 1;
  private static final String REVIEW_TAG = "点评";
  private TextView mLoginTipTextView;
  private int mNewCityId;
  private String mNewCityName;
  private int mOldCityId;
  private String mResidenceHeaderStr;
  private TextView mResidenceTextView;
  private TextView mTvSetting;
  private NetworkThumbView mUserGradeView;
  private CircleImageView mUserImageView;
  private NetworkThumbView mUserLevelView;
  private TextView mUserNameTextView;
  private String userGradePic = "";
  private MApiRequest userHomeReq;
  private MApiRequest userHomeUpdateReq;
  private String userLevelPic = "";

  public UserInfoHeaderAgent(Object paramObject)
  {
    super(paramObject);
  }

  private String avatarBig()
  {
    String str2 = getProfile().getString("Avatar");
    String str1 = str2;
    if (str2 != null)
    {
      str1 = str2;
      if (str2.endsWith("_s.jpg"))
        str1 = str2.substring(0, str2.length() - 6) + "_b.jpg";
    }
    return str1;
  }

  private void initResidence()
  {
    City localCity = CityUtils.getCityById(getProfile().getInt("CityID"));
    if ((localCity != null) && (localCity.id() > 0))
    {
      this.mResidenceTextView.setEnabled(true);
      this.mResidenceTextView.setText(this.mResidenceHeaderStr + localCity.name());
      this.mOldCityId = localCity.id();
      return;
    }
    this.mResidenceTextView.setEnabled(true);
    this.mResidenceTextView.setText(R.string.residence_notice);
    this.mOldCityId = 0;
  }

  private void onAgentChangCome(Bundle paramBundle)
  {
    if (paramBundle.containsKey("newUserName"))
      this.mUserNameTextView.setText(paramBundle.getString("newUserName"));
    do
      return;
    while (!paramBundle.containsKey("cityName"));
    this.mResidenceTextView.setText(this.mResidenceHeaderStr + paramBundle.getString("cityName"));
  }

  private void requestResidence()
  {
    if (this.userHomeReq != null)
      mapiService().abort(this.userHomeReq, this, true);
    this.mResidenceTextView.setEnabled(false);
    this.mResidenceTextView.setText(R.string.residence_reading);
    this.userHomeReq = BasicMApiRequest.mapiGet("http://m.api.dianping.com/residence.bin?token=" + token(), CacheType.DISABLED);
    mapiService().exec(this.userHomeReq, this);
  }

  private void updateHeaderView()
  {
    if (isLogin())
    {
      updateLoginView(0);
      this.mUserImageView.setImage(avatarBig());
      try
      {
        DPObject localDPObject = getProfile().getObject("UserLevel");
        this.userLevelPic = localDPObject.getString("Pic");
        if (localDPObject.getInt("Pow") < 0)
          this.userLevelPic = "";
        if (!this.userLevelPic.isEmpty())
          this.mUserLevelView.setImage(this.userLevelPic);
      }
      catch (Exception localException2)
      {
        try
        {
          while (true)
          {
            this.userGradePic = getProfile().getObject("UserGrade").getString("Image");
            if (this.userGradePic.isEmpty())
              break;
            this.mUserGradeView.setImage(this.userGradePic);
            this.mUserNameTextView.setText(getProfile().getString("NickName"));
            if ((!TextUtils.isEmpty(getAccount().gruponPhoneMasked())) || (!RedAlertManager.getInstance().checkLocalRedAlertByTag(UserInfoHeaderAgent.class.getName())))
              break label232;
            this.mTvSetting.setVisibility(0);
            initResidence();
            return;
            localException1 = localException1;
            this.userLevelPic = "";
            Log.e(localException1.toString());
            continue;
            this.mUserLevelView.setVisibility(8);
          }
        }
        catch (Exception localException2)
        {
          while (true)
          {
            this.userGradePic = "";
            Log.e(localException2.toString());
            continue;
            this.mUserGradeView.setVisibility(8);
            continue;
            label232: this.mTvSetting.setVisibility(8);
          }
        }
      }
    }
    updateLoginView(8);
    this.mUserImageView.setLocalBitmap(((BitmapDrawable)getResources().getDrawable(R.drawable.portrait_def)).getBitmap());
    requestResidence();
  }

  private void updateLoginView(int paramInt)
  {
    if (paramInt == 0)
    {
      this.mUserNameTextView.setVisibility(0);
      this.mUserLevelView.setVisibility(0);
      this.mUserGradeView.setVisibility(0);
      this.mLoginTipTextView.setVisibility(8);
      this.mResidenceTextView.setBackgroundResource(0);
      this.mResidenceTextView.setPadding(0, ViewUtils.dip2px(getContext(), 3.0F), ViewUtils.dip2px(getContext(), 4.0F), 0);
      this.mResidenceTextView.setClickable(false);
      this.mResidenceTextView.setTextColor(getResources().getColor(R.color.my_home_light_gray));
      return;
    }
    this.mUserNameTextView.setVisibility(8);
    this.mLoginTipTextView.setVisibility(0);
    this.mUserLevelView.setVisibility(8);
    this.mUserGradeView.setVisibility(8);
    this.mResidenceTextView.setBackgroundResource(R.drawable.residence_round_corner_background);
    this.mResidenceTextView.setTextColor(getResources().getColor(R.color.my_home_light_gray));
    this.mResidenceTextView.setPadding(0, ViewUtils.dip2px(getContext(), 3.0F), ViewUtils.dip2px(getContext(), 4.0F), ViewUtils.dip2px(getContext(), 2.0F));
    this.mResidenceTextView.setClickable(true);
    this.mTvSetting.setVisibility(8);
  }

  private void updateResidence(int paramInt)
  {
    if (this.userHomeUpdateReq != null)
      mapiService().abort(this.userHomeUpdateReq, this, true);
    ArrayList localArrayList = new ArrayList();
    if (token() != null)
    {
      localArrayList.add("token");
      localArrayList.add(token());
    }
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(paramInt));
    mapiService().exec(BasicMApiRequest.mapiPost("http://m.api.dianping.com/updateresidence.bin", (String[])localArrayList.toArray(new String[0])), null);
    this.userHomeUpdateReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/updateresidence.bin", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.userHomeUpdateReq, this);
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    dispatchAgentChanged(true);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    switch (paramInt1)
    {
    default:
    case 1:
    }
    label169: 
    while (true)
    {
      super.onActivityResult(paramInt1, paramInt2, paramIntent);
      return;
      if (paramIntent == null)
        continue;
      Object localObject = (City)paramIntent.getParcelableExtra("city");
      int i;
      if ((localObject != null) && (((City)localObject).id() > 0))
        i = ((City)localObject).id();
      for (localObject = ((City)localObject).name(); ; localObject = paramIntent.getStringExtra("cityName"))
      {
        if ((i <= 0) || (i == this.mOldCityId))
          break label169;
        this.mResidenceTextView.setText(this.mResidenceHeaderStr + (String)localObject);
        statisticsEvent("profile5", "profile5_changecity_submit", (String)localObject, 0);
        this.mNewCityId = i;
        this.mNewCityName = ((String)localObject);
        updateResidence(this.mNewCityId);
        break;
        i = paramIntent.getIntExtra("cityId", 0);
      }
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      onAgentChangCome(paramBundle);
      return;
    }
    updateHeaderView();
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.account)
      if (isLogin())
      {
        startActivity("dianping://editprofile");
        this.mTvSetting.setVisibility(8);
        RedAlertManager.getInstance().updateLocalRedAlert(UserInfoHeaderAgent.class.getName());
      }
    do
    {
      return;
      LoginUtils.setLoginGASource(getContext(), "ac_login");
      gotoLogin();
      return;
    }
    while ((i != R.id.myhome) || (isLogin()));
    statisticsEvent("profile5", "profile5_changecity", "", 0);
    if (this.mResidenceTextView.getText().equals(getContext().getString(R.string.residence_failed)))
    {
      requestResidence();
      return;
    }
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectcity"));
    paramView.putExtra("isGetCity", true);
    startActivityForResult(paramView, 1);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = (LinearLayout)getResources().inflate(getContext(), R.layout.checkin_my_profile_item, getParentView(), false);
    paramBundle.findViewById(R.id.account).setOnClickListener(this);
    this.mUserNameTextView = ((TextView)paramBundle.findViewById(R.id.user_name));
    this.mUserImageView = ((CircleImageView)paramBundle.findViewById(16908294));
    this.mUserLevelView = ((NetworkThumbView)paramBundle.findViewById(R.id.user_level_logo));
    this.mUserGradeView = ((NetworkThumbView)paramBundle.findViewById(R.id.user_grade_logo));
    this.mResidenceTextView = ((TextView)paramBundle.findViewById(R.id.myhome));
    this.mResidenceTextView.setOnClickListener(this);
    this.mLoginTipTextView = ((TextView)paramBundle.findViewById(R.id.login_tip));
    this.mResidenceHeaderStr = this.res.getString(R.string.residence_header);
    this.mTvSetting = ((TextView)paramBundle.findViewById(R.id.tv_set_info));
    this.mTvSetting.setCompoundDrawablePadding(ViewUtils.dip2px(getContext(), 5.0F));
    accountService().addListener(this);
    addCell("20UserInfoHeader.", paramBundle);
  }

  public void onDestroy()
  {
    if (this.userHomeReq != null)
      mapiService().abort(this.userHomeReq, this, true);
    accountService().removeListener(this);
    super.onDestroy();
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
    dispatchAgentChanged(true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.userHomeReq == paramMApiRequest)
    {
      this.mResidenceTextView.setText(R.string.residence_failed);
      this.mResidenceTextView.setEnabled(true);
      this.userHomeReq = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.userHomeReq == paramMApiRequest)
      if (!(paramMApiResponse.result() instanceof DPObject));
    do
      try
      {
        paramMApiRequest = (City)((DPObject)paramMApiResponse.result()).decodeToObject(City.DECODER);
        this.mResidenceTextView.setEnabled(true);
        if ((paramMApiRequest != null) && (paramMApiRequest.id() > 0))
          this.mResidenceTextView.setText(this.mResidenceHeaderStr + paramMApiRequest.name());
        for (this.mOldCityId = paramMApiRequest.id(); ; this.mOldCityId = 0)
        {
          this.userHomeReq = null;
          return;
          this.mResidenceTextView.setText(R.string.residence_notice);
        }
      }
      catch (ArchiveException paramMApiRequest)
      {
        while (true)
          paramMApiRequest.printStackTrace();
      }
      finally
      {
      }
    while (this.userHomeUpdateReq != paramMApiRequest);
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      if (accountService().token() != null)
      {
        paramMApiRequest = accountService().profile().edit().putInt("CityID", this.mNewCityId).generate();
        accountService().update(paramMApiRequest);
      }
      paramMApiRequest = new Intent("com.dianping.action.RESIDENCE_CHANGE");
      paramMApiRequest.putExtra("cityName", this.mNewCityName);
      paramMApiRequest.putExtra("cityId", this.mNewCityId);
      if (this.mOldCityId != 0)
        paramMApiRequest.putExtra("oldCityId", this.mOldCityId);
      if (getContext() != null)
        getContext().sendBroadcast(paramMApiRequest);
      this.mOldCityId = this.mNewCityId;
    }
    this.userHomeUpdateReq = null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.agent.UserInfoHeaderAgent
 * JD-Core Version:    0.6.0
 */
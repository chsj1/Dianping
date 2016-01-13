package com.dianping.main.user.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.UserProfile;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.DPBasicItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.message.BasicNameValuePair;

public class ShopEditActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int REQUEST_SELECT_CATEGORY = 37;
  private static final int REQUEST_SELECT_REGION = 38;
  private static final String TAG = ShopEditActivity.class.getSimpleName();
  private static final Pattern rfc2822 = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
  MApiRequest addShopRequest;
  EditText address;
  DPBasicItem btnShopRegion;
  DPBasicItem btnShopType;
  String callId;
  DPObject[] categories;
  EditText email;
  boolean hasChanged = false;
  private String initCategory;
  private String initRegion;
  boolean isClicked = false;
  DPObject mainCategory;
  DPObject mainRegion;
  EditText near;
  EditText phone;
  MApiRequest prefillCategory;
  MApiRequest prefillRegion;
  DPObject[] regions;
  DPObject shop;
  EditText shopName;
  DPObject subCategory;
  DPObject subRegion;
  Button submitButton;
  private TextWatcher textWatcher = new TextWatcher()
  {
    public void afterTextChanged(Editable paramEditable)
    {
      if (!ShopEditActivity.this.hasChanged)
      {
        ShopEditActivity.this.hasChanged = true;
        ShopEditActivity.this.submitButton.setEnabled(true);
      }
    }

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
    }
  };
  boolean waiting4Categorys;
  boolean waiting4Regions;

  private void doPrefill()
  {
    if ((this.regions != null) && (this.categories != null))
      updateShow();
  }

  private ShopInfo getUserInput()
  {
    ShopInfo localShopInfo = new ShopInfo(null);
    if (this.subRegion != null)
    {
      localShopInfo.regionId = this.subRegion.getInt("ID");
      if (this.subCategory == null)
        break label170;
      localShopInfo.categoryId = this.subCategory.getInt("ID");
    }
    while (true)
    {
      localShopInfo.shopName = this.shopName.getText().toString().trim();
      localShopInfo.address = this.address.getText().toString().trim();
      localShopInfo.crossRoad = this.near.getText().toString().trim();
      localShopInfo.phone = this.phone.getText().toString().trim();
      if (this.shop != null)
        localShopInfo.shopId = this.shop.getInt("ID");
      return localShopInfo;
      if (this.mainRegion != null)
      {
        localShopInfo.regionId = this.mainRegion.getInt("ID");
        break;
      }
      localShopInfo.regionId = 0;
      break;
      label170: if (this.mainCategory != null)
      {
        localShopInfo.categoryId = this.mainCategory.getInt("ID");
        continue;
      }
      if (this.shop == null)
        continue;
      localShopInfo.categoryId = this.shop.getInt("CategoryID");
    }
  }

  private boolean isEmailValidate(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    while (true)
    {
      return true;
      Object localObject = null;
      try
      {
        paramString = rfc2822.matcher(paramString);
        if ((paramString == null) || (paramString.matches()))
          continue;
        Toast.makeText(this, "请填写正确的邮箱地址", 0).show();
        return false;
      }
      catch (Exception paramString)
      {
        while (true)
        {
          Log.e(paramString.getLocalizedMessage());
          paramString = localObject;
        }
      }
    }
  }

  private boolean isInfoChanged(ShopInfo paramShopInfo)
  {
    if (paramShopInfo == null);
    do
    {
      return false;
      if (this.shop == null)
        return true;
      if (paramShopInfo.regionId != this.shop.getInt("RegionID"))
        return true;
      if (paramShopInfo.categoryId != this.shop.getInt("CategoryID"))
        return true;
      if (!paramShopInfo.shopName.equals(this.shop.getString("Name")))
        return true;
      if (!paramShopInfo.address.equals(this.shop.getString("Address")))
        return true;
      if (!paramShopInfo.crossRoad.equals(this.shop.getString("CrossRoad")))
        return true;
    }
    while (paramShopInfo.phone.equals(this.shop.getString("PhoneNo")));
    return true;
  }

  public static DPObject[] mainRegionList(DPObject[] paramArrayOfDPObject)
  {
    return subRegionList(paramArrayOfDPObject, 0);
  }

  private void requestCategories()
  {
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
    localStringBuilder.append("category.bin?");
    localStringBuilder.append("cityid=").append(this.shop.getInt("CityID"));
    this.prefillCategory = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.prefillCategory, this);
  }

  private void requestRegions()
  {
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
    localStringBuilder.append("region.bin?");
    localStringBuilder.append("cityid=").append(this.shop.getInt("CityID"));
    this.prefillRegion = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.prefillRegion, this);
  }

  private void setupView()
  {
    this.shopName = ((EditText)((DPBasicItem)findViewById(R.id.shopName_item)).findViewById(R.id.itemInput));
    this.btnShopRegion = ((DPBasicItem)findViewById(R.id.region_item));
    this.btnShopRegion.setOnClickListener(this);
    this.btnShopType = ((DPBasicItem)findViewById(R.id.channel_item));
    this.btnShopType.setOnClickListener(this);
    this.address = ((EditText)((DPBasicItem)findViewById(R.id.address_item)).findViewById(R.id.itemInput));
    this.near = ((EditText)((DPBasicItem)findViewById(R.id.near_item)).findViewById(R.id.itemInput));
    this.phone = ((EditText)((DPBasicItem)findViewById(R.id.phone_item)).findViewById(R.id.itemInput));
    this.phone.setText(city().areaCode() + "-");
    this.email = ((EditText)((DPBasicItem)findViewById(R.id.email_item)).findViewById(R.id.itemInput));
    this.submitButton = ((Button)findViewById(R.id.btn_add));
    this.submitButton.setOnClickListener(this);
    this.submitButton.setEnabled(false);
  }

  public static DPObject[] subRegionList(DPObject[] paramArrayOfDPObject, int paramInt)
  {
    if (paramArrayOfDPObject == null)
      return null;
    ArrayList localArrayList = new ArrayList();
    int j = paramArrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      if (localDPObject.getInt("ParentID") == paramInt)
        localArrayList.add(localDPObject);
      i += 1;
    }
    return (DPObject[])localArrayList.toArray(new DPObject[localArrayList.size()]);
  }

  private void triggerPrefill()
  {
    requestRegions();
    requestCategories();
  }

  private void updateShow()
  {
    dismissDialog();
    updateView(this.shop);
    if (this.waiting4Regions)
    {
      onClick(this.btnShopRegion);
      this.waiting4Regions = false;
    }
    do
      return;
    while (!this.waiting4Categorys);
    onClick(this.btnShopType);
    this.waiting4Categorys = false;
  }

  private void updateView(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.shopName.setText(paramDPObject.getString("Name"));
    this.address.setText(paramDPObject.getString("Address"));
    this.phone.setText(paramDPObject.getString("PhoneNo"));
    this.near.setText(paramDPObject.getString("CrossRoad"));
    this.shopName.addTextChangedListener(this.textWatcher);
    this.address.addTextChangedListener(this.textWatcher);
    this.phone.addTextChangedListener(this.textWatcher);
    this.near.addTextChangedListener(this.textWatcher);
    Object localObject1 = getAccount();
    if ((localObject1 != null) && (((UserProfile)localObject1).email() != null))
      this.email.setText(((UserProfile)localObject1).email());
    localObject1 = mainRegionList(this.regions);
    int j = 0;
    int n = localObject1.length;
    int i = 0;
    label211: DPObject[] arrayOfDPObject;
    int m;
    int k;
    if (i < n)
    {
      localObject2 = localObject1[i];
      if (((DPObject)localObject2).getInt("ID") == paramDPObject.getInt("RegionID"))
      {
        this.btnShopRegion.setSubTitle(((DPObject)localObject2).getString("Name"));
        this.btnShopRegion.setCount(null);
        this.btnShopRegion.build();
        this.mainRegion = ((DPObject)localObject2);
        this.subRegion = null;
      }
    }
    else
    {
      this.initRegion = this.btnShopRegion.getSubTitle();
      localObject2 = null;
      localObject1 = null;
      i = 0;
      arrayOfDPObject = this.categories;
      m = arrayOfDPObject.length;
      k = 0;
    }
    Object localObject4;
    Object localObject3;
    while (true)
    {
      localObject4 = localObject1;
      localObject3 = localObject2;
      if (k < m)
      {
        localObject3 = arrayOfDPObject[k];
        j = i;
        if (((DPObject)localObject3).getInt("ID") == paramDPObject.getInt("ShopType"))
        {
          localObject2 = localObject3;
          j = i + 1;
        }
        i = j;
        if (((DPObject)localObject3).getInt("ID") == paramDPObject.getInt("CategoryID"))
        {
          localObject1 = localObject3;
          i = j + 1;
        }
        if (i >= 2)
        {
          localObject3 = localObject2;
          localObject4 = localObject1;
        }
      }
      else
      {
        Log.i(TAG, "shopTypeMatch: " + localObject3);
        Log.i(TAG, "categoryIdMatch: " + localObject4);
        if (localObject4 != null)
          break label611;
        if (localObject3 != null)
        {
          this.mainCategory = ((DPObject)localObject3);
          this.btnShopType.setSubTitle(this.mainCategory.getString("Name"));
          this.btnShopType.setCount(null);
          this.btnShopType.build();
        }
        this.initCategory = this.btnShopType.getSubTitle();
        return;
        localObject3 = subRegionList(this.regions, ((DPObject)localObject2).getInt("ID"));
        int i1 = localObject3.length;
        m = 0;
        while (true)
        {
          k = j;
          if (m < i1)
          {
            localObject4 = localObject3[m];
            if (localObject4.getInt("ID") == paramDPObject.getInt("RegionID"))
            {
              this.btnShopRegion.setSubTitle(((DPObject)localObject2).getString("Name") + " - " + localObject4.getString("Name"));
              this.btnShopRegion.setCount(null);
              this.btnShopRegion.build();
              this.mainRegion = ((DPObject)localObject2);
              this.subRegion = localObject4;
              k = 1;
            }
          }
          else
          {
            if (k != 0)
              break label211;
            i += 1;
            j = k;
            break;
          }
          m += 1;
        }
      }
      k += 1;
    }
    label611: localObject1 = null;
    Object localObject2 = this.categories;
    j = localObject2.length;
    i = 0;
    while (true)
    {
      paramDPObject = (DPObject)localObject1;
      if (i < j)
      {
        paramDPObject = localObject2[i];
        if (localObject4.getInt("ParentID") != paramDPObject.getInt("ID"));
      }
      else
      {
        Log.i(TAG, "categoryIdMatchParent: " + paramDPObject);
        if (paramDPObject == null)
          break;
        this.mainCategory = paramDPObject;
        this.subCategory = localObject4;
        this.btnShopType.setSubTitle(this.mainCategory.getString("Name") + " - " + this.subCategory.getString("Name"));
        this.btnShopType.setCount(null);
        this.btnShopType.build();
        this.initCategory = this.btnShopType.getSubTitle();
        return;
      }
      i += 1;
    }
    this.mainCategory = ((DPObject)localObject3);
    this.subCategory = localObject4;
    if (this.mainCategory != null)
      this.btnShopType.setSubTitle(this.mainCategory.getString("Name") + " - " + this.subCategory.getString("Name"));
    while (true)
    {
      this.btnShopType.setCount(null);
      this.btnShopType.build();
      break;
      this.btnShopType.setSubTitle(this.subCategory.getString("Name"));
    }
  }

  boolean checkShouldShowAlertDialog()
  {
    if (isInfoChanged(getUserInput()))
    {
      new AlertDialog.Builder(this).setTitle("提示").setMessage("尚未提交，确定放弃修改吗？").setPositiveButton("确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          ShopEditActivity.this.finish();
        }
      }).setNegativeButton("取消", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      }).show();
      return true;
    }
    return false;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 6);
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
    if (paramUserProfile == null)
      finish();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 37)
      if (paramInt2 == -1)
      {
        this.mainCategory = ((DPObject)paramIntent.getParcelableExtra("resultExtra"));
        this.subCategory = ((DPObject)paramIntent.getParcelableExtra("result"));
        update();
      }
    do
      return;
    while ((paramInt1 != 38) || (paramInt2 != -1));
    this.mainRegion = ((DPObject)paramIntent.getParcelableExtra("resultExtra"));
    this.subRegion = ((DPObject)paramIntent.getParcelableExtra("result"));
    update();
  }

  public void onClick(View paramView)
  {
    if (!this.isClicked)
    {
      if (!paramView.equals(this.btnShopRegion))
        break label82;
      if (this.regions != null)
        break label47;
      this.isClicked = true;
      triggerPrefill();
      showProgressDialog("载入中...");
      this.waiting4Regions = true;
    }
    label47: label82: 
    do
    {
      return;
      startActivityForResult("dianping://regionfilter?source=addshop&cityid=" + this.shop.getInt("CityID"), 38);
      return;
      if (!paramView.equals(this.btnShopType))
        continue;
      if (this.categories == null)
      {
        this.isClicked = true;
        triggerPrefill();
        showProgressDialog("载入中...");
        this.waiting4Categorys = true;
        return;
      }
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://choosecategory?=false&cityid="));
      paramView.putExtra("hastopcategory", false);
      paramView.putExtra("hashotcategory", false);
      paramView.putExtra("hasAllSubCategory", false);
      paramView.putExtra("cityid", this.shop.getInt("CityID"));
      startActivityForResult(paramView, 37);
      return;
    }
    while (paramView.getId() != R.id.btn_add);
    submit();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.shop_edit_frame);
    super.setTitle("修改错误信息");
    this.callId = UUID.randomUUID().toString();
    StringBuilder localStringBuilder;
    if (paramBundle == null)
    {
      this.shop = ((DPObject)getIntent().getParcelableExtra("shop"));
      paramBundle = this.shop.getString("BranchName");
      localStringBuilder = new StringBuilder().append(this.shop.getString("Name"));
      if ((paramBundle != null) && (paramBundle.length() != 0))
        break label145;
    }
    label145: for (paramBundle = ""; ; paramBundle = "(" + paramBundle + ")")
    {
      super.setSubtitle(paramBundle);
      setupView();
      this.leftTitleButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (!ShopEditActivity.this.checkShouldShowAlertDialog())
            ShopEditActivity.this.finish();
        }
      });
      return;
      this.shop = ((DPObject)paramBundle.getParcelable("shop"));
      break;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.prefillRegion != null)
      mapiService().abort(this.prefillRegion, this, true);
    if (this.addShopRequest != null)
      mapiService().abort(this.addShopRequest, this, true);
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 4) && (checkShouldShowAlertDialog()))
      return true;
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
    if (this.addShopRequest != null)
      mapiService().abort(this.addShopRequest, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.prefillRegion)
      this.prefillRegion = null;
    if (paramMApiRequest == this.prefillCategory)
      this.prefillCategory = null;
    if (paramMApiResponse.message() != null)
      showMessageDialog(paramMApiResponse.message());
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.prefillRegion)
      this.prefillRegion = null;
    try
    {
      this.regions = ((DPObject[])(DPObject[])paramMApiResponse.result());
      doPrefill();
      label33: if (paramMApiRequest == this.prefillCategory)
        this.prefillCategory = null;
      try
      {
        this.categories = ((DPObject[])(DPObject[])paramMApiResponse.result());
        doPrefill();
        label66: if (paramMApiRequest == this.addShopRequest)
        {
          this.addShopRequest = null;
          if (paramMApiResponse.result() == null);
        }
        try
        {
          Toast.makeText(this, ((DPObject)paramMApiResponse.result()).getString("Content"), 0).show();
          paramMApiRequest = new ArrayList();
          if (this.shop != null)
            paramMApiRequest.add(new BasicNameValuePair("shopid", String.valueOf(this.shop.getInt("ID"))));
          statisticsEvent("addfeedback5", "addfeedback5_modify", "", 0, paramMApiRequest);
          finish();
          return;
        }
        catch (Exception paramMApiRequest)
        {
          paramMApiRequest.printStackTrace();
          return;
        }
      }
      catch (Exception localException1)
      {
        break label66;
      }
    }
    catch (Exception localException2)
    {
      break label33;
    }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    Object localObject2 = null;
    super.onRestoreInstanceState(paramBundle);
    Object localObject1 = paramBundle.getParcelableArrayList("regions");
    if (localObject1 == null)
    {
      localObject1 = null;
      this.regions = ((DPObject)localObject1);
      localObject1 = paramBundle.getParcelableArrayList("categorys");
      if (localObject1 != null)
        break label154;
    }
    label154: for (localObject1 = localObject2; ; localObject1 = (DPObject[])((ArrayList)localObject1).toArray(new DPObject[0]))
    {
      this.categories = ((DPObject)localObject1);
      this.mainRegion = ((DPObject)paramBundle.getParcelable("mainRegion"));
      this.subRegion = ((DPObject)paramBundle.getParcelable("subRegion"));
      this.mainCategory = ((DPObject)paramBundle.getParcelable("mainCategory"));
      this.subCategory = ((DPObject)paramBundle.getParcelable("subCategory"));
      this.callId = paramBundle.getString("callId");
      this.initRegion = paramBundle.getString("initRegion");
      this.initCategory = paramBundle.getString("initCategory");
      update();
      return;
      localObject1 = (DPObject[])((ArrayList)localObject1).toArray(new DPObject[0]);
      break;
    }
  }

  protected void onResume()
  {
    super.onResume();
    if ((this.regions == null) || (this.categories == null))
    {
      triggerPrefill();
      showProgressDialog("载入中...");
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    Object localObject2 = null;
    if (this.regions == null)
    {
      localObject1 = null;
      paramBundle.putParcelableArrayList("regions", (ArrayList)localObject1);
      if (this.categories != null)
        break label148;
    }
    label148: for (Object localObject1 = localObject2; ; localObject1 = new ArrayList(Arrays.asList(this.categories)))
    {
      paramBundle.putParcelableArrayList("categorys", (ArrayList)localObject1);
      paramBundle.putParcelable("shop", this.shop);
      paramBundle.putParcelable("mainRegion", this.mainRegion);
      paramBundle.putParcelable("subRegion", this.subRegion);
      paramBundle.putParcelable("mainCategory", this.mainCategory);
      paramBundle.putParcelable("subCategory", this.subCategory);
      paramBundle.putString("callId", this.callId);
      paramBundle.putString("initRegion", this.initRegion);
      paramBundle.putString("initCategory", this.initCategory);
      super.onSaveInstanceState(paramBundle);
      return;
      localObject1 = new ArrayList(Arrays.asList(this.regions));
      break;
    }
  }

  public void submit()
  {
    if (this.shopName.getText().toString().trim().length() == 0)
    {
      this.shopName.setText(null);
      this.shopName.setError(Html.fromHtml("<font color=#ff0000>请填写商户名</font>"));
      this.shopName.requestFocus();
      return;
    }
    if (this.mainRegion == null)
    {
      Toast.makeText(this, "请选择行政区", 0).show();
      return;
    }
    if (this.mainCategory == null)
    {
      Toast.makeText(this, "请选择频道", 0).show();
      return;
    }
    if (this.address.getText().toString().trim().length() == 0)
    {
      this.address.setText(null);
      this.address.setError(Html.fromHtml("<font color=#ff0000>请填写地址</font>"));
      this.address.requestFocus();
      return;
    }
    ShopInfo localShopInfo = getUserInput();
    if (!isInfoChanged(localShopInfo))
    {
      finish();
      return;
    }
    label172: Object localObject;
    String str2;
    int i;
    if (getAccount() == null)
    {
      str1 = this.email.getText().toString();
      if (!isEmailValidate(str1))
        break label462;
      localObject = new StringBuilder("http://m.api.dianping.com/addshopfeedback.bin?");
      ((StringBuilder)localObject).append("flag=").append(3);
      ((StringBuilder)localObject).append("&shopid=").append(localShopInfo.shopId);
      str2 = ((StringBuilder)localObject).toString();
      i = localShopInfo.shopId;
      localObject = str1;
      if ("".equals(str1))
        localObject = Environment.deviceId();
      if (getAccount() != null)
        break label464;
    }
    label462: label464: for (String str1 = ""; ; str1 = accountService().token())
    {
      this.addShopRequest = BasicMApiRequest.mapiPost(str2, new String[] { "flag", String.valueOf(3), "shopId", String.valueOf(i), "email", localObject, "token", str1, "categoryid", String.valueOf(localShopInfo.categoryId), "regionid", String.valueOf(localShopInfo.regionId), "shopname", localShopInfo.shopName, "address", localShopInfo.address, "crossroad", localShopInfo.crossRoad, "phone", localShopInfo.phone, "callid", this.callId });
      mapiService().exec(this.addShopRequest, this);
      showProgressDialog("正在提交商户信息...");
      return;
      str1 = getAccount().email();
      break label172;
      break;
    }
  }

  public void update()
  {
    String str2 = null;
    if (this.mainRegion != null)
      str2 = this.mainRegion.getString("Name");
    String str1 = str2;
    label99: String str3;
    if (this.subRegion != null)
    {
      if ((str2 != null) && (str2.length() > 0))
        str1 = str2 + " - " + this.subRegion.getString("Name");
    }
    else
    {
      if ((str1 == null) || (str1.length() <= 0))
        break label265;
      this.btnShopRegion.setSubTitle(str1);
      this.btnShopRegion.setCount(null);
      this.btnShopRegion.build();
      str2 = null;
      if (this.mainCategory != null)
        str2 = this.mainCategory.getString("Name");
      str3 = str2;
      if (this.subCategory != null)
      {
        if ((str2 == null) || (str2.length() <= 0))
          break label286;
        str3 = str2 + " - " + this.subCategory.getString("Name");
      }
      label178: if ((str3 == null) || (str3.length() <= 0))
        break label299;
      this.btnShopType.setSubTitle(str3);
      this.btnShopType.setCount(null);
      label205: this.btnShopType.build();
      if (!this.hasChanged)
      {
        if ((this.initRegion != null) && (this.initCategory != null))
          break label320;
        this.hasChanged = true;
        this.submitButton.setEnabled(true);
      }
    }
    while (true)
    {
      this.isClicked = false;
      return;
      str1 = this.subRegion.getString("Name");
      break;
      label265: this.btnShopRegion.setSubTitle(null);
      this.btnShopRegion.setCount("请选择地区");
      break label99;
      label286: str3 = this.subCategory.getString("Name");
      break label178;
      label299: this.btnShopType.setSubTitle(null);
      this.btnShopType.setCount("请选择频道");
      break label205;
      label320: if ((this.initRegion.equals(str1)) && (this.initCategory.equals(str3)))
        continue;
      this.hasChanged = true;
      this.submitButton.setEnabled(true);
    }
  }

  private static class ShopInfo
  {
    String address;
    int categoryId;
    String crossRoad;
    String phone;
    int regionId;
    int shopId;
    String shopName;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.ShopEditActivity
 * JD-Core Version:    0.6.0
 */
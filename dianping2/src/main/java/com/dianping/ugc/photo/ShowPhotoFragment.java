package com.dianping.ugc.photo;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.ScreenSlidePageFragment;
import com.dianping.base.widget.ShopPhotoItem;
import com.dianping.base.widget.loading.LoadingLayout;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.ugc.model.UploadPhotoData;
import com.dianping.util.DeviceUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class ShowPhotoFragment extends ScreenSlidePageFragment
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final int REQUEST_CODE_EDIT_CATEGORY = 1000;
  private DPObject delObjShopPhoto;
  private MApiRequest deleteRequest;
  private View shopInfoView;
  private ShowPhotoActivity showPhotoActivity;

  private void deleteTask(int paramInt1, int paramInt2, String paramString)
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    this.deleteRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/deletephoto.bin", new String[] { "token", paramString, "photoid", String.valueOf(paramInt1), "shopid", String.valueOf(paramInt2) });
    localMApiService.exec(this.deleteRequest, this);
  }

  private String getScreenInfo()
  {
    DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
    return "screenwidth=" + localDisplayMetrics.widthPixels + "&screenheight=" + localDisplayMetrics.heightPixels + "&screendensity=" + localDisplayMetrics.density;
  }

  protected ShopPhotoItem inflaterShopPhotoItem(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if (this.showPhotoActivity.isUserPhotoMode)
      return (ShopPhotoItem)paramLayoutInflater.inflate(R.layout.user_photo_item, paramViewGroup, false);
    return (ShopPhotoItem)paramLayoutInflater.inflate(R.layout.shop_photo_item, paramViewGroup, false);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 1000) && (paramInt2 == -1))
    {
      paramIntent = paramIntent.getParcelableArrayListExtra("photos");
      if ((paramIntent != null) && (paramIntent.size() > 0))
      {
        paramIntent = (UploadPhotoData)paramIntent.get(0);
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("shopid");
        localArrayList.add(paramIntent.poiShopId);
        localArrayList.add("photoid");
        localArrayList.add(paramIntent.photoId);
        localArrayList.add("token");
        localArrayList.add(accountService().token());
        localArrayList.add("name");
        localArrayList.add(paramIntent.title);
        if (paramIntent.price != null)
        {
          localArrayList.add("price");
          localArrayList.add(paramIntent.price);
        }
        localArrayList.add("tagname");
        localArrayList.add(paramIntent.categoryName);
        localArrayList.add("cx");
        localArrayList.add(DeviceUtils.cxInfo("shopphoto"));
        paramIntent = (BasicMApiRequest)BasicMApiRequest.mapiPost("http://m.api.dianping.com/updatephoto.bin", (String[])localArrayList.toArray(new String[localArrayList.size()]));
        mapiService().exec(paramIntent, new ShowPhotoFragment.4(this));
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.showPhotoActivity = ((ShowPhotoActivity)getActivity());
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    int i = 0;
    paramLayoutInflater = inflaterShopPhotoItem(paramLayoutInflater, paramViewGroup);
    paramLayoutInflater.setId(R.id.shop_photo_item);
    paramViewGroup = paramLayoutInflater.findViewById(R.id.edit_btn);
    paramBundle = paramLayoutInflater.findViewById(R.id.delete_btn);
    if ((this.pageObj != null) && (this.pageObj.getObject("User") != null) && (this.pageObj.getObject("User").getInt("UserID") == accountService().id()) && (!this.showPhotoActivity.isRecommend))
    {
      paramLayoutInflater.findViewById(R.id.save_btn).setVisibility(8);
      if ((paramViewGroup != null) && (paramBundle != null))
      {
        paramViewGroup.setVisibility(0);
        paramViewGroup.setOnClickListener(new ShowPhotoFragment.1(this));
      }
      paramBundle.setVisibility(0);
      paramBundle.setOnClickListener(new ShowPhotoFragment.2(this));
      paramViewGroup = (LoadingLayout)paramLayoutInflater.findViewById(R.id.loadinglayout);
      paramViewGroup.creatLoadingLayout(this.isBackground, true, true);
      if (this.isBackground)
        paramViewGroup.setLoadingBackgruond(this.bitmap);
      this.shopInfoView = paramLayoutInflater.findViewById(R.id.shop_info_cont);
      paramLayoutInflater.setPhoto(this.pageObj, this.showPhotoActivity.ga);
      paramViewGroup = this.shopInfoView;
      if (!this.showPhotoActivity.isShowInfo)
        break label361;
    }
    while (true)
    {
      paramViewGroup.setVisibility(i);
      this.shopInfoView.setClickable(true);
      if ((this.showPhotoActivity.isUserPhotoMode) && (this.pageObj != null))
        paramLayoutInflater.setShop((DPObject)this.showPhotoActivity.mapShop.get(Integer.valueOf(this.pageObj.getInt("ShopID")).intValue()), this.showPhotoActivity.ga);
      return paramLayoutInflater;
      if ((paramViewGroup != null) && (paramBundle != null))
      {
        paramViewGroup.setVisibility(8);
        paramBundle.setVisibility(8);
      }
      paramLayoutInflater.findViewById(R.id.save_btn).setVisibility(0);
      paramLayoutInflater.findViewById(R.id.save_btn).setBackgroundResource(R.drawable.icon_btn_save);
      paramLayoutInflater.findViewById(R.id.save_btn).setOnClickListener(new ShowPhotoFragment.3(this, paramLayoutInflater));
      break;
      label361: i = 8;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.deleteRequest)
    {
      if ((getActivity() instanceof NovaActivity))
      {
        paramMApiRequest = (NovaActivity)getActivity();
        if ((paramMApiResponse != null) && (paramMApiResponse.message() != null))
          Toast.makeText(paramMApiRequest, paramMApiResponse.message().content(), 0).show();
        paramMApiRequest.dismissDialog();
      }
      this.deleteRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    NovaActivity localNovaActivity;
    StringBuilder localStringBuilder;
    if ((paramMApiRequest == this.deleteRequest) && ((getActivity() instanceof NovaActivity)))
    {
      localNovaActivity = (NovaActivity)getActivity();
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiResponse = (DPObject)paramMApiResponse.result();
        localStringBuilder = new StringBuilder().append("http://m.api.dianping.com/userphoto.bin?userid=0&start=0");
        if (accountService().token() != null)
          break label224;
      }
    }
    label224: for (paramMApiRequest = ""; ; paramMApiRequest = "&token=" + accountService().token())
    {
      paramMApiRequest = paramMApiRequest + "&" + getScreenInfo();
      ((CacheService)getService("mapi_cache")).remove(BasicMApiRequest.mapiGet(paramMApiRequest, CacheType.NORMAL));
      paramMApiRequest = new Intent("com.dianping.action.UPLOAD_PHOTO");
      paramMApiRequest.putExtra("photo_delete", true);
      paramMApiRequest.putExtra("photo_delete_id", this.delObjShopPhoto.getInt("ID"));
      localNovaActivity.sendBroadcast(paramMApiRequest);
      localNovaActivity.sendBroadcast(new Intent("com.dianping.action.HONEY_CHANGED"));
      statisticsEvent("profile5", "profile5_photo_delete", "", 0);
      Toast.makeText(localNovaActivity, paramMApiResponse.getString("Content"), 0).show();
      localNovaActivity.dismissDialog();
      this.deleteRequest = null;
      localNovaActivity.finish();
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.ShowPhotoFragment
 * JD-Core Version:    0.6.0
 */
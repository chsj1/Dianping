package com.dianping.shopinfo.wed.weddingfeast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.speed.SpeedMonitorHelper;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.shopinfo.activity.ShopInfoActivity;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.shopinfo.widget.WeddingShopInfoHeaderView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class WedhotelTopAgent extends ShopCellAgent
{
  protected static final String CELL_TOP = "0200Basic.05Info";
  protected final View.OnClickListener iconClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = WedhotelTopAgent.this.getShop();
      if (paramView == null)
        return;
      if ((paramView.contains("PicCount")) && (paramView.getInt("PicCount") == 0) && (TextUtils.isEmpty(paramView.getString("DefaultPic"))))
      {
        UploadPhotoUtil.uploadShopPhoto(WedhotelTopAgent.this.getContext(), WedhotelTopAgent.this.getShop());
        return;
      }
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://weddinghotelphoto"));
      localIntent.putExtra("objShop", paramView);
      if (WedhotelTopAgent.this.getSharedObject("WeddingHotelExtra") != null)
        localIntent.putExtra("extraWeddingShop", (DPObject)WedhotelTopAgent.this.getSharedObject("WeddingHotelExtra"));
      if ((paramView.getInt("Status") == 1) || (paramView.getInt("Status") == 4));
      for (boolean bool = false; ; bool = true)
      {
        localIntent.putExtra("enableUpload", bool);
        WedhotelTopAgent.this.getFragment().startActivity(localIntent);
        new ArrayList().add(new BasicNameValuePair("shopid", String.valueOf(paramView.getInt("ID"))));
        GAHelper.instance().contextStatisticsEvent(WedhotelTopAgent.this.getContext(), "topphoto", WedhotelTopAgent.this.getGAExtra(), "tap");
        return;
      }
    }
  };
  protected ShopInfoHeaderView topView;

  public WedhotelTopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initHeaderView()
  {
    if (this.topView == null)
      this.topView = ((WeddingShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_wedding_panel, getParentView(), false));
  }

  private void resetHeaderView()
  {
    if ((this.topView != null) && (!this.topView.getClass().equals(WeddingShopInfoHeaderView.class)))
      this.topView = null;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    paramBundle = getShop();
    removeAllCells();
    if (paramBundle == null)
      break label18;
    label18: 
    do
      return;
    while (getFragment() == null);
    resetHeaderView();
    initHeaderView();
    if ((getSharedObject("WeddingHotelExtra") != null) && ((this.topView instanceof WeddingShopInfoHeaderView)))
      ((WeddingShopInfoHeaderView)this.topView).setExtraInfo((DPObject)getSharedObject("WeddingHotelExtra"));
    if (getShopStatus() == 0)
      this.topView.setShop(paramBundle, 0);
    while (true)
    {
      this.topView.setIconClickListen(this.iconClickListener);
      addCell("0200Basic.05Info", this.topView, 0);
      paramBundle = this.topView.findViewById(R.id.shopinfo_layout);
      if (paramBundle != null)
        paramBundle.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = new ArrayList();
            paramView.add(new BasicNameValuePair("shopid", WedhotelTopAgent.this.shopId() + ""));
            WedhotelTopAgent.this.statisticsEvent("shopinfow", "shopinfow_profile", "", 0, paramView);
          }
        });
      if (!(getContext() instanceof ShopInfoActivity))
        break;
      ((ShopInfoActivity)getContext()).getSpeedMonitorHelper().setResponseTime(1, System.currentTimeMillis());
      return;
      this.topView.setShop(paramBundle);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.weddingfeast.WedhotelTopAgent
 * JD-Core Version:    0.6.0
 */
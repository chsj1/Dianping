package com.dianping.shopinfo.hotel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.hotel.widget.HotelShopInfoHeaderView;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HotelTopAgent extends ShopCellAgent
{
  protected static final String CELL_TOP = "0200Basic.05Info";
  protected final View.OnClickListener iconClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      boolean bool = true;
      paramView = HotelTopAgent.this.getShop();
      if (paramView == null)
        return;
      if ((paramView.contains("PicCount")) && (paramView.getInt("PicCount") == 0) && (TextUtils.isEmpty(paramView.getString("DefaultPic"))))
      {
        GAHelper.instance().contextStatisticsEvent(HotelTopAgent.this.getContext(), "toupload", null, "tap");
        UploadPhotoUtil.uploadShopPhoto(HotelTopAgent.this.getContext(), HotelTopAgent.this.getShop());
        return;
      }
      Object localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hotelphoto"));
      if (paramView.getArray("ShopPhotoCategory") == null)
      {
        ((Intent)localObject).putExtra("shopId", paramView.getInt("ID"));
        if ((paramView.getInt("Status") == 1) || (paramView.getInt("Status") == 4))
          break label217;
      }
      while (true)
      {
        ((Intent)localObject).putExtra("enableUpload", bool);
        HotelTopAgent.this.getFragment().startActivity((Intent)localObject);
        localObject = new ArrayList();
        ((List)localObject).add(new BasicNameValuePair("shopid", String.valueOf(paramView.getInt("ID"))));
        HotelTopAgent.this.statisticsEvent("shopinfo5", "shopinfo5_viewphoto", "", 0, (List)localObject);
        return;
        ((Intent)localObject).putExtra("objShop", paramView);
        break;
        label217: bool = false;
      }
    }
  };
  protected ShopInfoHeaderView topView;

  public HotelTopAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if (paramAgentMessage.what.equals("com.dianping.shopinfo.hotel.HotelReviewAgent.HOTEL_REVIEW_LOAD_DATA_HOTEL_REVIEW"))
      onAgentChanged(paramAgentMessage.body);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    paramBundle = getShop();
    removeAllCells();
    if (paramBundle == null);
    do
      return;
    while (getFragment() == null);
    DPObject localDPObject = (DPObject)getSharedObject("reviewList");
    if ((this.topView != null) && (!this.topView.getClass().equals(HotelShopInfoHeaderView.class)))
      this.topView = null;
    if (this.topView == null)
    {
      this.topView = ((HotelShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_hotel_panel, getParentView(), false));
      if (this.topView != null)
        ((HotelShopInfoHeaderView)this.topView).setUploadClickListen(this.iconClickListener);
    }
    if (this.topView != null)
    {
      if (getShopStatus() != 0)
        break label173;
      this.topView.setShop(paramBundle, 0);
    }
    while (true)
    {
      if (localDPObject != null)
        ((HotelShopInfoHeaderView)this.topView).setTotalReviewTextView(localDPObject.getInt("RecordCount"));
      addCell("0200Basic.05Info", this.topView, 1024);
      return;
      label173: this.topView.setShop(paramBundle);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        UploadPhotoUtil.uploadShopPhoto(HotelTopAgent.this.getContext(), HotelTopAgent.this.getShop());
      }
    };
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelTopAgent
 * JD-Core Version:    0.6.0
 */
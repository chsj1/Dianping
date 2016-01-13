package com.dianping.membercard.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.membercard.constant.MURelationshipProductCode;
import com.dianping.model.Location;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaImageView;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MerchantUserRelationshipView extends FrameLayout
{
  private final int CONCERNTYPE_FANS = 20;
  private final int CONCERNTYPE_MEMBERCARD = 10;
  private NovaImageView checkBox;
  private int concernType;
  private TextView descTextView;
  private View iconView;
  private MApiRequest ifShowRequest;
  private boolean isChecked = false;
  private RequestHandler<MApiRequest, MApiResponse> mApiResponseRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == MerchantUserRelationshipView.this.ifShowRequest)
      {
        MerchantUserRelationshipView.access$102(MerchantUserRelationshipView.this, null);
        MerchantUserRelationshipView.this.hideMUViewSelf();
      }
      do
        return;
      while (paramMApiRequest != MerchantUserRelationshipView.this.opRequest);
      MerchantUserRelationshipView.access$002(MerchantUserRelationshipView.this, null);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      boolean bool2 = true;
      if (paramMApiRequest == MerchantUserRelationshipView.this.opRequest);
      do
        return;
      while (paramMApiRequest != MerchantUserRelationshipView.this.ifShowRequest);
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (!paramMApiRequest.isClass("ShopConcernResult"))
      {
        MerchantUserRelationshipView.this.hideMUViewSelf();
        return;
      }
      paramMApiRequest = paramMApiRequest.getArray("ShopConcerns");
      if ((paramMApiRequest == null) || (paramMApiRequest.length <= 0))
      {
        MerchantUserRelationshipView.this.hideMUViewSelf();
        return;
      }
      paramMApiRequest = paramMApiRequest[0];
      if (paramMApiRequest.getInt("IsNeedShow") == 1);
      for (boolean bool1 = true; !bool1; bool1 = false)
      {
        MerchantUserRelationshipView.this.hideMUViewSelf();
        return;
      }
      paramMApiResponse = MerchantUserRelationshipView.this;
      if (paramMApiRequest.getInt("IsChecked") == 1);
      String str;
      while (true)
      {
        MerchantUserRelationshipView.access$302(paramMApiResponse, bool2);
        paramMApiResponse = paramMApiRequest.getString("DisplayTitle");
        str = paramMApiRequest.getString("DisplayDesc");
        MerchantUserRelationshipView.access$402(MerchantUserRelationshipView.this, paramMApiRequest.getInt("ConcernType"));
        if ((!TextUtils.isEmpty(paramMApiResponse)) && (!TextUtils.isEmpty(str)))
          break;
        MerchantUserRelationshipView.this.hideMUViewSelf();
        return;
        bool2 = false;
      }
      if (MerchantUserRelationshipView.this.concernType == 20)
        MerchantUserRelationshipView.this.iconView.setVisibility(8);
      MerchantUserRelationshipView.this.titleTextView.setText(paramMApiResponse);
      MerchantUserRelationshipView.this.descTextView.setText(str);
      MerchantUserRelationshipView.this.refreshCheckBoxView();
      if (MerchantUserRelationshipView.this.muViewShowListener != null)
        MerchantUserRelationshipView.this.muViewShowListener.showOrHideMUView(bool1);
      GAHelper.instance().statisticsEvent(MerchantUserRelationshipView.this.checkBox, "view");
      MerchantUserRelationshipView.access$102(MerchantUserRelationshipView.this, null);
    }
  };
  private MUViewShowListener muViewShowListener;
  private MApiRequest opRequest;
  private MURelationshipProductCode productCode = MURelationshipProductCode.HUI;
  private int selectCityId;
  private int shopId;
  private TextView titleTextView;

  public MerchantUserRelationshipView(Context paramContext)
  {
    super(paramContext);
    initView(paramContext);
  }

  public MerchantUserRelationshipView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  private void addOrRemoveRelationship(boolean paramBoolean)
  {
    int i = 1;
    if (((this.shopId <= 0) || (this.selectCityId <= 0) || (this.productCode == null)) && (Environment.isDebug()))
      throw new IllegalStateException("You must call setShopId && setSelectCityId && setProductCode to ensure the params are legal.");
    if (this.opRequest != null)
      getMapiService().abort(this.opRequest, this.mApiResponseRequestHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("concerncommand");
    if (paramBoolean);
    while (true)
    {
      localArrayList.add(String.valueOf(i));
      localArrayList.add("productcode");
      localArrayList.add(String.valueOf(this.productCode.getCodeValue()));
      localArrayList.add("concerntype");
      localArrayList.add(String.valueOf(this.concernType));
      localArrayList.add("shopid");
      localArrayList.add(String.valueOf(this.shopId));
      Location localLocation = getLocation();
      if (localLocation != null)
      {
        DecimalFormat localDecimalFormat = Location.FMT;
        localArrayList.add("lat");
        localArrayList.add(localDecimalFormat.format(localLocation.latitude()));
        localArrayList.add("lng");
        localArrayList.add(localDecimalFormat.format(localLocation.longitude()));
      }
      localArrayList.add("cityid");
      localArrayList.add(String.valueOf(this.selectCityId));
      this.opRequest = BasicMApiRequest.mapiPost("http://mapi.dianping.com/mapi/fans/concernchange.fans", (String[])localArrayList.toArray(new String[0]));
      getMapiService().exec(this.opRequest, this.mApiResponseRequestHandler);
      return;
      i = 0;
    }
  }

  private Location getLocation()
  {
    Object localObject = (LocationService)(LocationService)DPApplication.instance().getService("location");
    Location localLocation = null;
    try
    {
      localObject = ((LocationService)localObject).location();
      if (localObject != null)
        localLocation = (Location)((DPObject)localObject).decodeToObject(Location.DECODER);
      return localLocation;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      Log.e(localException.getMessage());
    }
    return (Location)null;
  }

  private MApiService getMapiService()
  {
    return (MApiService)(MApiService)DPApplication.instance().getService("mapi");
  }

  private void hideMUViewSelf()
  {
    setVisibility(8);
    if (this.muViewShowListener != null)
      this.muViewShowListener.showOrHideMUView(false);
  }

  private void initView(Context paramContext)
  {
    LayoutInflater.from(paramContext).inflate(R.layout.view_membercard_opitem, this, true);
    this.iconView = findViewById(R.id.membercard_icon);
    this.titleTextView = ((TextView)findViewById(R.id.textview_title));
    this.descTextView = ((TextView)findViewById(R.id.textview_desc));
    this.checkBox = ((NovaImageView)findViewById(R.id.checkbox_membercard_op));
    this.checkBox.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = MerchantUserRelationshipView.this;
        if (!MerchantUserRelationshipView.this.isChecked);
        for (boolean bool = true; ; bool = false)
        {
          MerchantUserRelationshipView.access$302(paramView, bool);
          MerchantUserRelationshipView.this.refreshCheckBoxView();
          MerchantUserRelationshipView.this.addOrRemoveRelationship(MerchantUserRelationshipView.this.isChecked);
          return;
        }
      }
    });
    this.checkBox.setGAString("concern");
  }

  private void refreshCheckBoxView()
  {
    if (this.isChecked)
    {
      this.checkBox.setImageResource(R.drawable.mc_cbx_checked);
      return;
    }
    this.checkBox.setImageResource(R.drawable.mc_cbx_rest);
  }

  public void askIfMUVShowed()
  {
    if (((this.shopId <= 0) || (this.productCode == null)) && (Environment.isDebug()))
      throw new IllegalStateException("You must call setShopId && setProductCode to ensure the params are legal.");
    if (this.ifShowRequest != null)
    {
      getMapiService().abort(this.ifShowRequest, this.mApiResponseRequestHandler, true);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder("http://mapi.dianping.com/mapi/fans/shopconcern.fans?");
    localStringBuilder.append("shopid=").append(this.shopId).append("&productcode=").append(this.productCode.getCodeValue());
    Location localLocation = getLocation();
    if (localLocation != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      localStringBuilder.append("&lat=").append(localDecimalFormat.format(localLocation.latitude())).append("&lng=").append(localDecimalFormat.format(localLocation.longitude()));
    }
    localStringBuilder.append("&cityid=").append(this.selectCityId);
    this.ifShowRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    getMapiService().exec(this.ifShowRequest, this.mApiResponseRequestHandler);
  }

  public void destroyView()
  {
    this.muViewShowListener = null;
    if (this.muViewShowListener == null)
    {
      if (this.ifShowRequest != null)
      {
        getMapiService().abort(this.ifShowRequest, this.mApiResponseRequestHandler, true);
        this.ifShowRequest = null;
      }
      if (this.opRequest != null)
      {
        getMapiService().abort(this.opRequest, this.mApiResponseRequestHandler, true);
        this.opRequest = null;
      }
    }
  }

  public MerchantUserRelationshipView setMUShowListener(MUViewShowListener paramMUViewShowListener)
  {
    this.muViewShowListener = paramMUViewShowListener;
    return this;
  }

  public MerchantUserRelationshipView setProductCode(MURelationshipProductCode paramMURelationshipProductCode)
  {
    this.productCode = paramMURelationshipProductCode;
    return this;
  }

  public MerchantUserRelationshipView setSelectCityId(int paramInt)
  {
    this.selectCityId = paramInt;
    return this;
  }

  public MerchantUserRelationshipView setShopId(int paramInt)
  {
    this.shopId = paramInt;
    return this;
  }

  public static abstract interface MUViewShowListener
  {
    public abstract void showOrHideMUView(boolean paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.MerchantUserRelationshipView
 * JD-Core Version:    0.6.0
 */
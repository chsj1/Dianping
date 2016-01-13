package com.dianping.map.elements;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import com.dianping.app.DPApplication;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.GreatChinaBounds;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.util.DateUtils;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import java.util.HashMap;

public class ElementsController
  implements OnRequestFinishListener
{
  private static final int DURATION_LIMIT = 900;
  private static ElementsController ourInstance = new ElementsController();
  private Context mContext;
  private Location mLocation;
  private DPObject mShop;
  private TencentMap mTencentMap;
  private OnRouteChangeListener routeChangeListener;

  public static ElementsController getInstance()
  {
    return ourInstance;
  }

  public void destroyRequest()
  {
    WalkElements.getInstance().destroyRequest();
    BusElements.getInstance().destroyRequest();
    CarElements.getInstance().destroyRequest();
  }

  public HashMap<View, FrameLayout.LayoutParams> getCustomView(int paramInt)
  {
    if (paramInt == 3);
    while (true)
    {
      return null;
      if (paramInt == 1)
        return BusElements.getInstance().getCustomView();
      if (paramInt != 2)
        continue;
    }
  }

  public boolean haveCustomView(int paramInt)
  {
    if (paramInt == 3)
      return WalkElements.getInstance().haveCustomView();
    if (paramInt == 1)
      return BusElements.getInstance().haveCustomView();
    if (paramInt == 2)
      return CarElements.getInstance().haveCustomView();
    return false;
  }

  public void init(Context paramContext, TencentMap paramTencentMap, DPObject paramDPObject)
  {
    if ((paramContext == null) || ((paramContext instanceof DPApplication)) || (paramTencentMap == null) || (paramDPObject == null))
      return;
    this.mContext = paramContext;
    this.mTencentMap = paramTencentMap;
    this.mShop = paramDPObject;
    Object localObject1 = null;
    Double localDouble2 = Double.valueOf(31.2303122784D);
    Double localDouble3 = Double.valueOf(121.473562309D);
    Double localDouble1 = localDouble2;
    paramContext = (Context)localObject1;
    try
    {
      Object localObject2;
      if (((NovaActivity)this.mContext).locationService() != null)
      {
        localDouble1 = localDouble2;
        paramContext = (Context)localObject1;
        localObject2 = ((NovaActivity)this.mContext).locationService().location();
        if (localObject2 != null);
      }
      else
      {
        paramContext = null;
        localObject1 = localDouble3;
        localDouble1 = localDouble2;
        this.mLocation = paramContext;
        GreatChinaBounds.setLocation(((Double)localObject1).doubleValue(), localDouble1.doubleValue());
        CarElements.getInstance().setOnRequestFinishListener(this);
        WalkElements.getInstance().setOnRequestFinishListener(this);
        BusElements.getInstance().setOnRequestFinishListener(this);
        WalkElements.getInstance().init(this.mContext, paramTencentMap, paramDPObject, paramContext);
        CarElements.getInstance().init(this.mContext, paramTencentMap, paramDPObject, paramContext);
        BusElements.getInstance().init(this.mContext, paramTencentMap, paramDPObject, paramContext);
        if ((paramContext != null) && (!paramDPObject.getBoolean("IsForeignShop")) && ((!GreatChinaBounds.isForeignerOutChina()) || (paramDPObject.getBoolean("IsForeignShop"))))
          break label379;
        show(2);
        this.routeChangeListener.OnFinalRouteModeSelected(2, false);
        return;
      }
      localDouble1 = localDouble2;
      paramContext = (Context)localObject1;
      localObject1 = (Location)((NovaActivity)this.mContext).locationService().location().decodeToObject(Location.DECODER);
      double d;
      if (localObject1 == null)
      {
        d = -1.0D;
        label279: localDouble1 = localDouble2;
        paramContext = (Context)localObject1;
        localDouble2 = Double.valueOf(d);
        if (localObject1 != null)
          break label348;
        d = -1.0D;
      }
      while (true)
      {
        localDouble1 = localDouble2;
        paramContext = (Context)localObject1;
        localObject2 = Double.valueOf(d);
        localDouble1 = localDouble2;
        paramContext = (Context)localObject1;
        localObject1 = localObject2;
        break;
        localDouble1 = localDouble2;
        paramContext = (Context)localObject1;
        d = ((Location)localObject1).offsetLatitude();
        break label279;
        label348: localDouble1 = localDouble2;
        paramContext = (Context)localObject1;
        d = ((Location)localObject1).offsetLongitude();
      }
    }
    catch (ArchiveException localArchiveException)
    {
      while (true)
      {
        localArchiveException.printStackTrace();
        localObject1 = localDouble3;
      }
      label379: sendRequest();
    }
  }

  public void onRequestFinish(int paramInt1, boolean paramBoolean, int paramInt2)
  {
    if (this.routeChangeListener == null)
      return;
    OnRouteChangeListener localOnRouteChangeListener;
    boolean bool;
    HashMap localHashMap;
    String str;
    if (paramBoolean)
    {
      localOnRouteChangeListener = this.routeChangeListener;
      bool = haveCustomView(paramInt1);
      localHashMap = getCustomView(paramInt1);
      if (paramInt2 >= 0)
        break label87;
      str = "--";
    }
    while (true)
    {
      localOnRouteChangeListener.OnRouteChanged(paramInt1, bool, localHashMap, str);
      if (paramInt1 != 3)
        break;
      if (!paramBoolean)
        break label128;
      if (paramInt2 > 900)
        break label111;
      show(3);
      this.routeChangeListener.OnFinalRouteModeSelected(3, paramBoolean);
      return;
      label87: if (paramInt1 == 3)
      {
        str = DateUtils.aboutHour(paramInt2, 3);
        continue;
      }
      str = DateUtils.secToMinutes(paramInt2);
    }
    label111: show(2);
    this.routeChangeListener.OnFinalRouteModeSelected(2, paramBoolean);
    return;
    label128: show(2);
    this.routeChangeListener.OnFinalRouteModeSelected(2, paramBoolean);
  }

  public void sendRequest()
  {
    WalkElements.getInstance().sendRequest();
    BusElements.getInstance().sendRequest();
    CarElements.getInstance().sendRequest();
  }

  public void setRouteChangeListener(OnRouteChangeListener paramOnRouteChangeListener)
  {
    this.routeChangeListener = paramOnRouteChangeListener;
  }

  public void show(int paramInt)
  {
    if (this.mTencentMap == null);
    do
    {
      return;
      this.mTencentMap.clear();
      if (paramInt == 3)
      {
        WalkElements.getInstance().show();
        return;
      }
      if (paramInt != 1)
        continue;
      BusElements.getInstance().show();
      return;
    }
    while (paramInt != 2);
    CarElements.getInstance().show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.elements.ElementsController
 * JD-Core Version:    0.6.0
 */
package com.dianping.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.util.Daemon;
import com.dianping.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class CityConfig
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CITY_CONFIG_FILE = "UIO90DIGDKLLG";
  private static final int CMD_SAVE = 1;
  MApiRequest getCityInfoReq;
  private Context mContext;
  private City mCurrentCity;
  private Handler mListenerHandler = new Handler(Looper.getMainLooper());
  private ArrayList<SwitchListener> mListeners = new ArrayList();
  private Handler mSaveHandler = new Handler(Daemon.looper())
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 1)
        CityConfig.this.doSave();
    }
  };

  public CityConfig(Context paramContext)
  {
    this.mContext = paramContext;
  }

  private void doLoad()
  {
    try
    {
      SharedPreferences localSharedPreferences = this.mContext.getSharedPreferences("UIO90DIGDKLLG", 0);
      int i = localSharedPreferences.getInt("id", -1);
      String str = localSharedPreferences.getString("name", null);
      if ((i > 0) && (str != null))
        this.mCurrentCity = new City(i, str, localSharedPreferences.getString("areaCode", null), localSharedPreferences.getBoolean("isPromo", false), localSharedPreferences.getBoolean("isTuan", false), localSharedPreferences.getFloat("latitude", 0.0F), localSharedPreferences.getFloat("longitude", 0.0F), localSharedPreferences.getInt("firstChar", 0), localSharedPreferences.getBoolean("isTop", false), localSharedPreferences.getBoolean("isLocalPromoCity", false), localSharedPreferences.getBoolean("isRankIndexCity", false), localSharedPreferences.getBoolean("isLocalDish", false), localSharedPreferences.getInt("flag", 0));
      return;
    }
    catch (Exception localException)
    {
    }
  }

  private void doSave()
  {
    if (this.mCurrentCity == null)
      return;
    this.mContext.getSharedPreferences("UIO90DIGDKLLG", 0).edit().putInt("id", this.mCurrentCity.id()).putString("name", this.mCurrentCity.name()).putString("areaCode", this.mCurrentCity.areaCode()).putBoolean("isPromo", this.mCurrentCity.isPromo()).putBoolean("isTuan", this.mCurrentCity.isTuan()).putFloat("latitude", (float)this.mCurrentCity.latitude()).putFloat("longitude", (float)this.mCurrentCity.longitude()).putInt("firstChar", this.mCurrentCity.firstChar().charAt(0)).putBoolean("isLocalPromoCity", this.mCurrentCity.isLocalPromo()).putBoolean("isRankIndexCity", this.mCurrentCity.isRankIndexCity()).putBoolean("isLocalDish", this.mCurrentCity.isLocalDish()).putInt("flag", this.mCurrentCity.flag()).commit();
  }

  private void notifySwitch(City paramCity1, City paramCity2)
  {
    this.mListenerHandler.post(new Runnable(paramCity1, paramCity2)
    {
      public void run()
      {
        synchronized (CityConfig.this.mListeners)
        {
          Iterator localIterator = CityConfig.this.mListeners.iterator();
          if (localIterator.hasNext())
            ((CityConfig.SwitchListener)localIterator.next()).onCitySwitched(this.val$from, this.val$to);
        }
        monitorexit;
      }
    });
  }

  private void save()
  {
    this.mSaveHandler.sendEmptyMessage(1);
  }

  public void addListener(SwitchListener paramSwitchListener)
  {
    this.mListenerHandler.post(new Runnable(paramSwitchListener)
    {
      public void run()
      {
        if (!CityConfig.this.mListeners.contains(this.val$l))
          CityConfig.this.mListeners.add(this.val$l);
      }
    });
  }

  public City currentCity()
  {
    if (this.mCurrentCity == null)
      doLoad();
    if (this.mCurrentCity == null)
      this.mCurrentCity = City.DEFAULT;
    return this.mCurrentCity;
  }

  protected void getCityInfoRequest(int paramInt)
  {
    if (this.getCityInfoReq != null)
      DPApplication.instance().mapiService().abort(this.getCityInfoReq, this, true);
    this.getCityInfoReq = BasicMApiRequest.mapiGet("http://m.api.dianping.com/common/cityinfo.bin?cityid=" + paramInt, CacheType.DISABLED);
    DPApplication.instance().mapiService().exec(this.getCityInfoReq, this);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getCityInfoReq)
      this.getCityInfoReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != this.getCityInfoReq) || ((paramMApiResponse.result() instanceof DPObject)));
    try
    {
      paramMApiRequest = (City)((DPObject)paramMApiResponse.result()).decodeToObject(City.DECODER);
      if (!paramMApiRequest.equals(this.mCurrentCity))
      {
        paramMApiResponse = this.mCurrentCity;
        this.mCurrentCity = paramMApiRequest;
        save();
        notifySwitch(paramMApiResponse, paramMApiRequest);
      }
      this.getCityInfoReq = null;
      return;
    }
    catch (com.dianping.archive.ArchiveException paramMApiRequest)
    {
      while (true)
        Log.e("city decodeToObject error");
    }
  }

  public void removeListener(SwitchListener paramSwitchListener)
  {
    this.mListenerHandler.post(new Runnable(paramSwitchListener)
    {
      public void run()
      {
        if (CityConfig.this.mListeners.contains(this.val$l))
          CityConfig.this.mListeners.remove(this.val$l);
      }
    });
  }

  public void switchCity(City paramCity)
  {
    if (paramCity == null)
      return;
    City localCity = this.mCurrentCity;
    this.mCurrentCity = paramCity;
    save();
    notifySwitch(localCity, paramCity);
    getCityInfoRequest(paramCity.id());
  }

  public void updateCurrentCity(City paramCity)
  {
    if ((this.mCurrentCity != null) && (this.mCurrentCity.id() != paramCity.id()))
      return;
    this.mCurrentCity = paramCity;
    save();
  }

  public static abstract interface SwitchListener
  {
    public abstract void onCitySwitched(City paramCity1, City paramCity2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.app.CityConfig
 * JD-Core Version:    0.6.0
 */
package com.dianping.booking;

import android.content.Context;
import android.util.SparseArray;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import java.util.ArrayList;
import java.util.Calendar;

public class GrouponBookingDataManager
  implements FullRequestHandle<MApiRequest, MApiResponse>
{
  private static final int HALLBUSYMASK;
  private static final int HALLMASK = (int)Math.pow(2.0D, 20.0D);
  private static GrouponBookingDataManager grouponBookingDataManager;
  private static Context mContext;
  private ArrayList<Calendar> calendarList = new ArrayList();
  public Calendar defaultBookingTime = Calendar.getInstance();
  public int defaultPeopleCount;
  private MApiRequest getGrouponBookingConfigRequest;
  private ArrayList<GrouponBookingDataManager.BookingBitMap> grouponBookingBitMapList = new ArrayList();
  public DPObject grouponBookingConfig;
  public DPObject[] grouponLists;
  public RoomStatus hallRoom;
  private ArrayList<BookingDateItem> hourList = new ArrayList();
  private OnGrouponBookingRequestHandlerListener mListener;
  private SparseArray<ArrayList<BookingDateItem>> minuteMap = new SparseArray();
  public int peopleMaxCount;
  public int peopleMinCount;
  private DPObject[] rooms;
  private Calendar selectDate = Calendar.getInstance();
  private GrouponBookingDataManager.BookingBitMap selectDayBitMap;
  private int timeInterval = 15;

  static
  {
    HALLBUSYMASK = (int)(Math.pow(2.0D, 10.0D) - 1.0D);
  }

  private void caculateHall()
  {
    this.hallRoom = null;
    doCaculate(HALLMASK, HALLBUSYMASK);
  }

  private void caculateRoom(int paramInt1, int paramInt2)
  {
    this.hallRoom = null;
    int i = 0;
    if (i < this.rooms.length)
    {
      int j;
      RoomStatus localRoomStatus;
      if (this.rooms[i].getInt("RoomType") == 10)
      {
        j = this.rooms[i].getInt("MinPeople");
        int k = this.rooms[i].getInt("MaxPeople");
        if ((paramInt2 >= j) && (paramInt2 <= k))
        {
          localRoomStatus = new RoomStatus();
          localRoomStatus.room = this.rooms[i];
          j = this.selectDayBitMap.bitmap[getPosition()];
          if ((j & (int)Math.pow(2.0D, 20.0D)) != 0)
            break label144;
          localRoomStatus.status = -1;
        }
      }
      while (true)
      {
        this.hallRoom = localRoomStatus;
        i += 1;
        break;
        label144: if ((j & this.rooms[i].getInt("Index")) > 0)
        {
          localRoomStatus.status = 1;
          continue;
        }
        localRoomStatus.status = -2;
      }
    }
    if (paramInt1 == 10)
      doCaculate(HALLMASK, getHallValue(paramInt2));
  }

  private void doCaculate(int paramInt1, int paramInt2)
  {
    int i = 0;
    if (i < this.selectDayBitMap.bitmap.length)
    {
      int j = 0;
      ArrayList localArrayList = new ArrayList();
      int k = 0;
      if (j < 60)
      {
        if ((this.selectDayBitMap.bitmap[i] & paramInt1) == 0)
          localArrayList.add(new BookingDateItem(String.valueOf(j), -1));
        while (true)
        {
          j += this.timeInterval;
          i += 1;
          break;
          if ((this.selectDayBitMap.bitmap[i] & paramInt2) == 0)
          {
            localArrayList.add(new BookingDateItem(String.valueOf(j), -2));
            continue;
          }
          localArrayList.add(new BookingDateItem(String.valueOf(j), 1));
          k = 1;
        }
      }
      j = (i - 1) / (60 / this.timeInterval);
      BookingDateItem localBookingDateItem;
      if (k != 0)
      {
        localBookingDateItem = new BookingDateItem(j + "", 1);
        this.hourList.add(localBookingDateItem);
      }
      while (true)
      {
        this.minuteMap.put(j, localArrayList);
        break;
        localBookingDateItem = new BookingDateItem(j + "", -1);
        this.hourList.add(localBookingDateItem);
      }
    }
  }

  private int getHallValue(int paramInt)
  {
    int i = paramInt;
    paramInt = i;
    if (i <= 2)
      paramInt = 2;
    i = paramInt;
    if (paramInt > 10)
      i = 11;
    return (int)Math.pow(2.0D, i - 2);
  }

  public static GrouponBookingDataManager getInstance(Context paramContext)
  {
    if (grouponBookingDataManager == null)
      grouponBookingDataManager = new GrouponBookingDataManager();
    mContext = paramContext;
    return grouponBookingDataManager;
  }

  private int getPosition()
  {
    int i = this.selectDate.get(11);
    int j = this.selectDate.get(12);
    return 60 / this.timeInterval * i + j / this.timeInterval;
  }

  private boolean isTheSameDay(Calendar paramCalendar1, Calendar paramCalendar2)
  {
    return (paramCalendar1.get(1) == paramCalendar2.get(1)) && (paramCalendar1.get(6) == paramCalendar2.get(6));
  }

  private void setGrouponBookingBitMap()
  {
    this.grouponBookingBitMapList.clear();
    this.calendarList.clear();
    DPObject[] arrayOfDPObject = this.grouponBookingConfig.getArray("BookingBitMapList");
    if (arrayOfDPObject == null);
    while (true)
    {
      return;
      int i = 0;
      while (i < arrayOfDPObject.length)
      {
        long l = arrayOfDPObject[i].getTime("Date");
        Object localObject = arrayOfDPObject[i].getIntArray("BitMap");
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(l);
        localObject = new GrouponBookingDataManager.BookingBitMap(this, localCalendar, localObject);
        this.grouponBookingBitMapList.add(localObject);
        this.calendarList.add(localCalendar);
        i += 1;
      }
    }
  }

  private void setGrouponBookingConfig()
  {
    this.rooms = this.grouponBookingConfig.getArray("RoomList");
    this.grouponLists = this.grouponBookingConfig.getArray("GrouponList");
    this.defaultPeopleCount = this.grouponBookingConfig.getInt("DefaultPeopleCount");
    this.peopleMinCount = this.grouponBookingConfig.getInt("PeopleMinCount");
    this.peopleMaxCount = this.grouponBookingConfig.getInt("PeopleMaxCount");
    long l = this.grouponBookingConfig.getTime("DefaultBookingTime");
    if (l > 0L)
      this.defaultBookingTime.setTimeInMillis(l);
    int i = this.grouponBookingConfig.getInt("TimeInterval");
    if ((i > 0) && (i < 60))
      this.timeInterval = i;
    setGrouponBookingBitMap();
  }

  public void abortGrouponBookingConfigTask()
  {
    if (this.getGrouponBookingConfigRequest != null)
      ((DPActivity)mContext).mapiService().abort(this.getGrouponBookingConfigRequest, null, true);
    this.getGrouponBookingConfigRequest = null;
  }

  public void getGrouponBookingConfigTask(int paramInt)
  {
    if (this.getGrouponBookingConfigRequest != null)
      return;
    this.getGrouponBookingConfigRequest = BasicMApiRequest.mapiGet("http://rs.api.dianping.com/getgrouponbookingconfig.yy?shopID=" + paramInt, CacheType.DISABLED);
    ((NovaActivity)mContext).mapiService().exec(this.getGrouponBookingConfigRequest, this);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getGrouponBookingConfigRequest)
    {
      if (this.mListener != null)
        this.mListener.onFinish(false);
      this.getGrouponBookingConfigRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getGrouponBookingConfigRequest)
    {
      this.grouponBookingConfig = ((DPObject)paramMApiResponse.result());
      setGrouponBookingConfig();
      if (this.mListener != null)
      {
        if (this.grouponBookingConfig != null)
          break label55;
        this.mListener.onFinish(false);
      }
    }
    while (true)
    {
      this.getGrouponBookingConfigRequest = null;
      return;
      label55: this.mListener.onFinish(true);
    }
  }

  public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
  {
  }

  public void onRequestStart(MApiRequest paramMApiRequest)
  {
    if (this.mListener != null)
      this.mListener.onStart();
  }

  public void setGrouponBookingConfigData(DPObject paramDPObject)
  {
    this.grouponBookingConfig = paramDPObject;
    setGrouponBookingConfig();
  }

  public void setGrouponBookingDate(Calendar paramCalendar, int paramInt1, int paramInt2)
  {
    this.selectDate.setTimeInMillis(paramCalendar.getTimeInMillis());
    this.hourList.clear();
    this.minuteMap.clear();
    this.selectDayBitMap = null;
    int i = 0;
    while (true)
    {
      if (i < this.grouponBookingBitMapList.size())
      {
        if (isTheSameDay(this.selectDate, ((GrouponBookingDataManager.BookingBitMap)this.grouponBookingBitMapList.get(i)).calendar))
          this.selectDayBitMap = ((GrouponBookingDataManager.BookingBitMap)this.grouponBookingBitMapList.get(i));
      }
      else
      {
        if (this.selectDayBitMap != null)
          break;
        return;
      }
      i += 1;
    }
    if (paramInt2 == 0)
    {
      caculateHall();
      return;
    }
    caculateRoom(paramInt1, paramInt2);
  }

  public void setOnGrouponBookingRequestHandlerListener(OnGrouponBookingRequestHandlerListener paramOnGrouponBookingRequestHandlerListener)
  {
    this.mListener = paramOnGrouponBookingRequestHandlerListener;
  }

  public static abstract interface OnGrouponBookingRequestHandlerListener
  {
    public abstract void onFinish(boolean paramBoolean);

    public abstract void onStart();
  }

  class RoomStatus
  {
    DPObject room;
    int status;

    RoomStatus()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.GrouponBookingDataManager
 * JD-Core Version:    0.6.0
 */
package com.dianping.ugc.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;

public class UGCUploadPhotoItem extends UGCContentItem
{
  public static final Parcelable.Creator<UGCUploadPhotoItem> CREATOR = new Parcelable.Creator()
  {
    public UGCUploadPhotoItem createFromParcel(Parcel paramParcel)
    {
      return new UGCUploadPhotoItem(paramParcel);
    }

    public UGCUploadPhotoItem[] newArray(int paramInt)
    {
      return new UGCUploadPhotoItem[paramInt];
    }
  };
  public static final String TYPE = "uploadphoto";
  public String batchId;
  public int feed;
  public boolean isEdit = false;
  public final ArrayList<UploadPhotoData> mPhotos = new ArrayList();
  public String text;

  public UGCUploadPhotoItem()
  {
  }

  public UGCUploadPhotoItem(Parcel paramParcel)
  {
    super(paramParcel);
    this.batchId = paramParcel.readString();
    this.text = paramParcel.readString();
    this.feed = paramParcel.readInt();
    paramParcel.readTypedList(this.mPhotos, UploadPhotoData.CREATOR);
  }

  public void addUploadPhoto(UploadPhotoData paramUploadPhotoData)
  {
    this.mPhotos.add(paramUploadPhotoData);
  }

  public int getFailedPhotoCount()
  {
    int i = 0;
    Iterator localIterator = this.mPhotos.iterator();
    while (localIterator.hasNext())
    {
      if (((UploadPhotoData)localIterator.next()).success)
        continue;
      i += 1;
    }
    return i;
  }

  public String getFirstPOIShopId()
  {
    Iterator localIterator = this.mPhotos.iterator();
    while (localIterator.hasNext())
    {
      UploadPhotoData localUploadPhotoData = (UploadPhotoData)localIterator.next();
      if (localUploadPhotoData.poiShopId != null)
        return localUploadPhotoData.poiShopId;
    }
    return null;
  }

  public String getType()
  {
    return "uploadphoto";
  }

  public int getUploadPhotoCount()
  {
    return this.mPhotos.size();
  }

  public void setUploadPhotos(ArrayList<UploadPhotoData> paramArrayList)
  {
    this.mPhotos.clear();
    this.mPhotos.addAll(paramArrayList);
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(this.batchId);
    paramParcel.writeString(this.text);
    paramParcel.writeInt(this.feed);
    paramParcel.writeTypedList(this.mPhotos);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.model.UGCUploadPhotoItem
 * JD-Core Version:    0.6.0
 */
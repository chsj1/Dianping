package com.dianping.ugc.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UploadPhotoData extends PhotoData
{
  public static final Parcelable.Creator<UploadPhotoData> CREATOR = new UploadPhotoData.1();
  public final int[] adjustPosition = new int[2];
  public int adjustSize = 0;
  public String categoryName;
  public int containerHeight;
  public int containerWidth;
  public ArrayList<UploadPhotoDecalData> decals = new ArrayList();
  public int direction;
  public int height = 0;
  public String photoId;
  public String poiShopId;
  public String poiShopName;
  public String price;
  public boolean success = false;
  public ArrayList<UploadPhotoTagData> tags = new ArrayList();
  public String title;
  public int width = 0;

  public UploadPhotoData()
  {
  }

  public UploadPhotoData(Parcel paramParcel)
  {
    super(paramParcel);
    this.photoId = paramParcel.readString();
    if (paramParcel.readInt() == 1);
    while (true)
    {
      this.success = bool;
      this.title = paramParcel.readString();
      this.direction = paramParcel.readInt();
      this.width = paramParcel.readInt();
      this.height = paramParcel.readInt();
      this.containerHeight = paramParcel.readInt();
      this.containerWidth = paramParcel.readInt();
      paramParcel.readIntArray(this.adjustPosition);
      this.adjustSize = paramParcel.readInt();
      this.categoryName = paramParcel.readString();
      this.price = paramParcel.readString();
      this.poiShopId = paramParcel.readString();
      this.poiShopName = paramParcel.readString();
      paramParcel.readTypedList(this.tags, UploadPhotoTagData.CREATOR);
      paramParcel.readTypedList(this.decals, UploadPhotoDecalData.CREATOR);
      return;
      bool = false;
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public void setAdjust(int paramInt1, int paramInt2, int paramInt3)
  {
    this.adjustPosition[0] = paramInt1;
    this.adjustPosition[1] = paramInt2;
    this.adjustSize = paramInt3;
  }

  public void setPhotoId(String paramString)
  {
    this.photoId = paramString;
  }

  public void setSize(int paramInt1, int paramInt2)
  {
    this.width = paramInt1;
    this.height = paramInt2;
  }

  public JSONObject toJSONObject()
  {
    JSONObject localJSONObject = super.toJSONObject();
    try
    {
      if (this.photoId != null)
        localJSONObject.put("picId", this.photoId);
      localJSONObject.put("success", this.success);
      localJSONObject.put("width", this.width);
      localJSONObject.put("height", this.height);
      if (this.containerHeight != 0)
        localJSONObject.put("containerHeight", this.containerHeight);
      if (this.containerWidth != 0)
        localJSONObject.put("containerWidth", this.containerWidth);
      localJSONObject.put("title", this.title);
      localJSONObject.put("tagName", this.categoryName);
      if (this.price != null)
        localJSONObject.put("price", this.price);
      if (this.poiShopId != null)
        localJSONObject.put("shopId", this.poiShopId);
      if (this.tags.size() > 0)
      {
        JSONArray localJSONArray = new JSONArray();
        Iterator localIterator = this.tags.iterator();
        while (localIterator.hasNext())
          localJSONArray.put(((UploadPhotoTagData)localIterator.next()).toJSONObject());
      }
    }
    catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
    return localJSONObject;
    localJSONObject.put("tagList", localJSONException);
    return localJSONObject;
  }

  public String toString()
  {
    return "photoInfo:picId=" + this.photoId + " title=" + this.title + " photoKey=" + this.photoKey + " filepath=" + this.photoPath + " direction=" + this.direction + " (" + this.width + "*" + this.height + ")" + " adjust x=" + this.adjustPosition[0] + " adjust y=" + this.adjustPosition[1] + " adjust size=" + this.adjustSize;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(this.photoId);
    if (this.success);
    for (paramInt = 1; ; paramInt = 0)
    {
      paramParcel.writeInt(paramInt);
      paramParcel.writeString(this.title);
      paramParcel.writeInt(this.direction);
      paramParcel.writeInt(this.width);
      paramParcel.writeInt(this.height);
      paramParcel.writeInt(this.containerHeight);
      paramParcel.writeInt(this.containerWidth);
      paramParcel.writeIntArray(this.adjustPosition);
      paramParcel.writeInt(this.adjustSize);
      paramParcel.writeString(this.categoryName);
      paramParcel.writeString(this.price);
      paramParcel.writeString(this.poiShopId);
      paramParcel.writeString(this.poiShopName);
      paramParcel.writeTypedList(this.tags);
      paramParcel.writeTypedList(this.decals);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.model.UploadPhotoData
 * JD-Core Version:    0.6.0
 */
package com.dianping.ugc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotoData
  implements Parcelable
{
  public static final Parcelable.Creator<PhotoData> CREATOR = new PhotoData.1();
  public String photoKey;
  public String photoPath;

  public PhotoData()
  {
  }

  public PhotoData(Parcel paramParcel)
  {
    this.photoPath = paramParcel.readString();
    this.photoKey = paramParcel.readString();
  }

  public int describeContents()
  {
    return 0;
  }

  public void setPhotoKey(String paramString)
  {
    this.photoKey = paramString;
  }

  public JSONObject toJSONObject()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("picKey", this.photoKey);
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
    return localJSONObject;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.photoPath);
    paramParcel.writeString(this.photoKey);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.model.PhotoData
 * JD-Core Version:    0.6.0
 */
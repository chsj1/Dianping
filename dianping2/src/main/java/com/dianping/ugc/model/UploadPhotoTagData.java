package com.dianping.ugc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.json.JSONException;
import org.json.JSONObject;

public class UploadPhotoTagData
  implements Parcelable
{
  public static final Parcelable.Creator<UploadPhotoTagData> CREATOR = new UploadPhotoTagData.1();
  public static final int TYPE_INTEREST = 1;
  public static final int TYPE_POI = 2;
  public String content;
  public boolean isRight = true;
  public int tagId;
  public int tagType;
  public double xPosition;
  public double yPosition;

  public UploadPhotoTagData(int paramInt, double paramDouble1, double paramDouble2)
  {
    this.tagId = paramInt;
    this.xPosition = paramDouble1;
    this.yPosition = paramDouble2;
  }

  public UploadPhotoTagData(int paramInt, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    this(paramInt, paramDouble1, paramDouble2);
    this.isRight = paramBoolean;
  }

  public UploadPhotoTagData(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this(paramInt1, paramInt3, paramInt4);
    this.tagType = paramInt2;
  }

  public UploadPhotoTagData(Parcel paramParcel)
  {
    this.tagType = paramParcel.readInt();
    this.tagId = paramParcel.readInt();
    if (paramParcel.readInt() == 1);
    while (true)
    {
      this.isRight = bool;
      this.yPosition = paramParcel.readDouble();
      this.xPosition = paramParcel.readDouble();
      this.content = paramParcel.readString();
      return;
      bool = false;
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public void setTagType(int paramInt)
  {
    this.tagType = paramInt;
  }

  public JSONObject toJSONObject()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("isRight", this.isRight);
      localJSONObject.put("yPosition", this.yPosition);
      localJSONObject.put("xPosition", this.xPosition);
      localJSONObject.put("content", this.content);
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
    paramParcel.writeInt(this.tagType);
    paramParcel.writeInt(this.tagId);
    if (this.isRight);
    for (paramInt = 1; ; paramInt = 0)
    {
      paramParcel.writeInt(paramInt);
      paramParcel.writeDouble(this.yPosition);
      paramParcel.writeDouble(this.xPosition);
      paramParcel.writeString(this.content);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.model.UploadPhotoTagData
 * JD-Core Version:    0.6.0
 */
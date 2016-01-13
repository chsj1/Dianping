package com.dianping.ugc.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public class UGCUploadCommunityPhotoItem extends UGCUploadPhotoItem
{
  public static final Parcelable.Creator<UGCUploadCommunityPhotoItem> CREATOR = new Parcelable.Creator()
  {
    public UGCUploadCommunityPhotoItem createFromParcel(Parcel paramParcel)
    {
      return new UGCUploadCommunityPhotoItem(paramParcel);
    }

    public UGCUploadCommunityPhotoItem[] newArray(int paramInt)
    {
      return new UGCUploadCommunityPhotoItem[paramInt];
    }
  };
  public static final String TYPE = "uploadcommunityphoto";
  public String topicId;

  public UGCUploadCommunityPhotoItem()
  {
  }

  public UGCUploadCommunityPhotoItem(Parcel paramParcel)
  {
    super(paramParcel);
    this.topicId = paramParcel.readString();
  }

  public String getType()
  {
    return "uploadcommunityphoto";
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(this.topicId);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.model.UGCUploadCommunityPhotoItem
 * JD-Core Version:    0.6.0
 */
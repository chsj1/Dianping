package com.dianping.ugc.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public class UGCReviewItem extends UGCContentItem
{
  public static final Parcelable.Creator<UGCReviewItem> CREATOR = new UGCReviewItem.1();
  public static final String TYPE = "review";
  public String averagePrice;
  public String comment;
  public int feed;
  public double lat;
  public double lng;
  public final ArrayList<UploadPhotoData> mPhotos = new ArrayList();
  public String orderid;
  public String referToken;
  public int reviewId;
  public int reviewType;
  public final String[] scoreNames = new String[3];
  public final String[] scores = new String[3];
  public String shopdishtags;
  public String shoptags = "";
  public String star;
  public String tuanTitle = "";
  public String userToken;

  public UGCReviewItem()
  {
  }

  public UGCReviewItem(Parcel paramParcel)
  {
    super(paramParcel);
    this.referToken = paramParcel.readString();
    this.tuanTitle = paramParcel.readString();
    this.orderid = paramParcel.readString();
    this.reviewId = paramParcel.readInt();
    this.star = paramParcel.readString();
    paramParcel.readStringArray(this.scores);
    paramParcel.readStringArray(this.scoreNames);
    this.averagePrice = paramParcel.readString();
    this.comment = paramParcel.readString();
    this.shoptags = paramParcel.readString();
    this.shopdishtags = paramParcel.readString();
    this.lat = paramParcel.readDouble();
    this.lng = paramParcel.readDouble();
    this.feed = paramParcel.readInt();
    paramParcel.readTypedList(this.mPhotos, UploadPhotoData.CREATOR);
    this.userToken = paramParcel.readString();
  }

  public boolean equals(Object paramObject)
  {
    int j;
    if (!(paramObject instanceof UGCReviewItem))
      j = 0;
    int k;
    while (true)
    {
      return j;
      k = 1;
      paramObject = (UGCReviewItem)paramObject;
      j = k;
      if (1 != 0)
      {
        j = k;
        if (this.reviewId != paramObject.reviewId)
          j = 0;
      }
      k = j;
      if (j != 0)
      {
        k = j;
        if (this.star != null)
        {
          k = j;
          if (!this.star.equals(paramObject.star))
            k = 0;
        }
      }
      j = k;
      if (k != 0)
      {
        if (this.scores.length == paramObject.scores.length)
          break;
        j = 0;
      }
      else
      {
        k = j;
        if (j != 0)
        {
          k = j;
          if (this.averagePrice != null)
          {
            k = j;
            if (!this.averagePrice.equals(paramObject.averagePrice))
              k = 0;
          }
        }
        j = k;
        if (k != 0)
        {
          j = k;
          if (this.comment != null)
          {
            j = k;
            if (!this.comment.equals(paramObject.comment))
              j = 0;
          }
        }
        k = j;
        if (j != 0)
        {
          k = j;
          if (this.shoptags != null)
          {
            k = j;
            if (!this.shoptags.equals(paramObject.shoptags))
              k = 0;
          }
        }
        j = k;
        if (k == 0)
          continue;
        j = k;
        if (this.shopdishtags == null)
          continue;
        j = k;
        if (!this.shopdishtags.equals(paramObject.shopdishtags))
          return false;
      }
    }
    int i = 0;
    while (true)
    {
      j = k;
      if (i >= this.scores.length)
        break;
      if ((this.scores[i] != null) && (!this.scores[i].equals(paramObject.scores[i])))
      {
        j = 0;
        break;
      }
      i += 1;
    }
  }

  public String getType()
  {
    return "review";
  }

  public boolean hasScore()
  {
    int i = 0;
    while (i < this.scoreNames.length)
    {
      if (this.scoreNames[i] != null)
        return true;
      i += 1;
    }
    return false;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(this.referToken);
    paramParcel.writeString(this.tuanTitle);
    paramParcel.writeString(this.orderid);
    paramParcel.writeInt(this.reviewId);
    paramParcel.writeString(this.star);
    paramParcel.writeStringArray(this.scores);
    paramParcel.writeStringArray(this.scoreNames);
    paramParcel.writeString(this.averagePrice);
    paramParcel.writeString(this.comment);
    paramParcel.writeString(this.shoptags);
    paramParcel.writeString(this.shopdishtags);
    paramParcel.writeDouble(this.lat);
    paramParcel.writeDouble(this.lng);
    paramParcel.writeInt(this.feed);
    paramParcel.writeTypedList(this.mPhotos);
    paramParcel.writeString(this.userToken);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.model.UGCReviewItem
 * JD-Core Version:    0.6.0
 */
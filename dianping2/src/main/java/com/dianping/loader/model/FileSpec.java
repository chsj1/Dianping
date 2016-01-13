package com.dianping.loader.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FileSpec
  implements Parcelable
{
  public static final Parcelable.Creator<FileSpec> CREATOR = new Parcelable.Creator()
  {
    public FileSpec createFromParcel(Parcel paramParcel)
    {
      return new FileSpec(paramParcel);
    }

    public FileSpec[] newArray(int paramInt)
    {
      return new FileSpec[paramInt];
    }
  };
  public static final int DOWN_3G = 2;
  public static final int DOWN_ALWAYS = 5;
  public static final int DOWN_NONE = 0;
  public static final int DOWN_WIFI = 1;
  private String[] deps;
  private int down;
  private String id;
  private int length;
  private String md5;
  private String url;

  protected FileSpec(Parcel paramParcel)
  {
    this.id = paramParcel.readString();
    this.url = paramParcel.readString();
    this.md5 = paramParcel.readString();
    this.down = paramParcel.readInt();
    this.length = paramParcel.readInt();
    this.deps = paramParcel.createStringArray();
  }

  public FileSpec(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, String[] paramArrayOfString)
  {
    this.id = paramString1;
    this.url = paramString2;
    this.md5 = paramString3;
    this.down = paramInt1;
    this.length = paramInt2;
    this.deps = paramArrayOfString;
  }

  public FileSpec(JSONObject paramJSONObject)
    throws JSONException
  {
    this.id = paramJSONObject.getString("id");
    this.url = paramJSONObject.getString("url");
    this.md5 = paramJSONObject.optString("md5");
    this.down = paramJSONObject.optInt("down", 0);
    this.length = paramJSONObject.optInt("length", 0);
    paramJSONObject = paramJSONObject.optJSONArray("deps");
    if (paramJSONObject != null)
    {
      this.deps = new String[paramJSONObject.length()];
      int i = 0;
      while (i < this.deps.length)
      {
        this.deps[i] = paramJSONObject.getString(i);
        i += 1;
      }
    }
  }

  public String[] deps()
  {
    return this.deps;
  }

  public int describeContents()
  {
    return 0;
  }

  public int down()
  {
    return this.down;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this)
      return true;
    if (!(paramObject instanceof FileSpec))
      return false;
    return this.id.equals(((FileSpec)paramObject).id);
  }

  public int hashCode()
  {
    return this.id.hashCode();
  }

  public String id()
  {
    return this.id;
  }

  public int length()
  {
    return this.length;
  }

  public String md5()
  {
    return this.md5;
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.id);
    if ((this.deps != null) && (this.deps.length > 0))
    {
      localStringBuilder.append(':');
      localStringBuilder.append(this.deps[0]);
      int i = 1;
      while (i < this.deps.length)
      {
        localStringBuilder.append(',').append(this.deps[i]);
        i += 1;
      }
    }
    return localStringBuilder.toString();
  }

  public String url()
  {
    return this.url;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.id);
    paramParcel.writeString(this.url);
    paramParcel.writeString(this.md5);
    paramParcel.writeInt(this.down);
    paramParcel.writeInt(this.length);
    paramParcel.writeStringArray(this.deps);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.model.FileSpec
 * JD-Core Version:    0.6.0
 */
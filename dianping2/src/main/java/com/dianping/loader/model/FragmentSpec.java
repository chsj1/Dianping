package com.dianping.loader.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentSpec
  implements Parcelable
{
  public static final Parcelable.Creator<FragmentSpec> CREATOR = new Parcelable.Creator()
  {
    public FragmentSpec createFromParcel(Parcel paramParcel)
    {
      return new FragmentSpec(paramParcel);
    }

    public FragmentSpec[] newArray(int paramInt)
    {
      return new FragmentSpec[paramInt];
    }
  };
  public static final int SKIP_2G = 1;
  public static final int SKIP_3G = 2;
  public static final int SKIP_NONE = 0;
  private String code;
  private String host;
  private int maxVersion;
  private int minVersion;
  private String name;
  private String parameter;
  private int skip;
  private int timeout;

  protected FragmentSpec(Parcel paramParcel)
  {
    this.host = paramParcel.readString();
    this.code = paramParcel.readString();
    this.name = paramParcel.readString();
    this.minVersion = paramParcel.readInt();
    this.maxVersion = paramParcel.readInt();
    this.skip = paramParcel.readInt();
    this.timeout = paramParcel.readInt();
    this.parameter = paramParcel.readString();
  }

  public FragmentSpec(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString4)
  {
    this.host = paramString1;
    this.code = paramString2;
    this.name = paramString3;
    this.minVersion = paramInt1;
    this.maxVersion = paramInt2;
    this.skip = paramInt3;
    this.timeout = paramInt4;
    this.parameter = paramString4;
  }

  public FragmentSpec(JSONObject paramJSONObject)
    throws JSONException
  {
    this.host = paramJSONObject.getString("host");
    this.code = paramJSONObject.optString("code");
    this.name = paramJSONObject.getString("name");
    this.minVersion = paramJSONObject.optInt("minVersion");
    this.maxVersion = paramJSONObject.optInt("maxVersion");
    this.skip = paramJSONObject.optInt("skip");
    this.timeout = paramJSONObject.optInt("timeout");
    this.parameter = paramJSONObject.optString("parameter");
  }

  public String code()
  {
    return this.code;
  }

  public int describeContents()
  {
    return 0;
  }

  public String host()
  {
    return this.host;
  }

  public int maxVersion()
  {
    return this.maxVersion;
  }

  public int minVersion()
  {
    return this.minVersion;
  }

  public String name()
  {
    return this.name;
  }

  public String parameter()
  {
    return this.parameter;
  }

  public int skip()
  {
    return this.skip;
  }

  public int timeout()
  {
    return this.timeout;
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("xxx://").append(this.host).append(':');
    localStringBuilder.append(this.name);
    localStringBuilder.append('(');
    if (this.code == null)
      localStringBuilder.append('.');
    while (true)
    {
      if (this.minVersion != 0)
      {
        localStringBuilder.append(";v").append(this.minVersion);
        if (this.maxVersion != 0)
          localStringBuilder.append('~').append(this.maxVersion);
      }
      if (this.parameter != null)
      {
        localStringBuilder.append(";param=");
        localStringBuilder.append(this.parameter);
      }
      localStringBuilder.append(')');
      return localStringBuilder.toString();
      localStringBuilder.append(this.code);
    }
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.host);
    paramParcel.writeString(this.code);
    paramParcel.writeString(this.name);
    paramParcel.writeInt(this.minVersion);
    paramParcel.writeInt(this.maxVersion);
    paramParcel.writeInt(this.skip);
    paramParcel.writeInt(this.timeout);
    paramParcel.writeString(this.parameter);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.model.FragmentSpec
 * JD-Core Version:    0.6.0
 */
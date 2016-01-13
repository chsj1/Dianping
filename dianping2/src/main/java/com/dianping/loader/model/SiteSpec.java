package com.dianping.loader.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SiteSpec
  implements Parcelable
{
  public static final Parcelable.Creator<SiteSpec> CREATOR = new Parcelable.Creator()
  {
    public SiteSpec createFromParcel(Parcel paramParcel)
    {
      return new SiteSpec(paramParcel);
    }

    public SiteSpec[] newArray(int paramInt)
    {
      return new SiteSpec[paramInt];
    }
  };
  private FileSpec[] files;
  private FragmentSpec[] fragments;
  private String id;
  private String version;

  protected SiteSpec(Parcel paramParcel)
  {
    this.id = paramParcel.readString();
    this.version = paramParcel.readString();
    this.files = ((FileSpec[])paramParcel.createTypedArray(FileSpec.CREATOR));
    this.fragments = ((FragmentSpec[])paramParcel.createTypedArray(FragmentSpec.CREATOR));
  }

  public SiteSpec(String paramString1, String paramString2, FileSpec[] paramArrayOfFileSpec, FragmentSpec[] paramArrayOfFragmentSpec)
  {
    this.id = paramString1;
    this.version = paramString2;
    this.files = paramArrayOfFileSpec;
    this.fragments = paramArrayOfFragmentSpec;
  }

  public SiteSpec(JSONObject paramJSONObject)
    throws JSONException
  {
    this.id = paramJSONObject.getString("id");
    this.version = paramJSONObject.getString("version");
    Object localObject = paramJSONObject.getJSONArray("files");
    this.files = new FileSpec[((JSONArray)localObject).length()];
    int i = 0;
    while (i < this.files.length)
    {
      FileSpec localFileSpec = new FileSpec(((JSONArray)localObject).getJSONObject(i));
      this.files[i] = localFileSpec;
      i += 1;
    }
    paramJSONObject = paramJSONObject.getJSONArray("fragments");
    this.fragments = new FragmentSpec[paramJSONObject.length()];
    i = 0;
    while (i < this.fragments.length)
    {
      localObject = new FragmentSpec(paramJSONObject.getJSONObject(i));
      this.fragments[i] = localObject;
      i += 1;
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public FileSpec[] files()
  {
    return this.files;
  }

  public FragmentSpec[] fragments()
  {
    return this.fragments;
  }

  public FragmentSpec[] fragments(int paramInt)
  {
    ArrayList localArrayList = new ArrayList(this.fragments.length);
    FragmentSpec[] arrayOfFragmentSpec = this.fragments;
    int j = arrayOfFragmentSpec.length;
    int i = 0;
    if (i < j)
    {
      FragmentSpec localFragmentSpec = arrayOfFragmentSpec[i];
      if (paramInt > 0)
        if ((paramInt >= localFragmentSpec.minVersion()) && ((localFragmentSpec.maxVersion() == 0) || (paramInt <= localFragmentSpec.maxVersion())))
          localArrayList.add(localFragmentSpec);
      while (true)
      {
        i += 1;
        break;
        localArrayList.add(localFragmentSpec);
      }
    }
    if (localArrayList.size() == this.fragments.length)
      return this.fragments;
    return (FragmentSpec[])localArrayList.toArray(new FragmentSpec[localArrayList.size()]);
  }

  public FileSpec getFile(String paramString)
  {
    FileSpec[] arrayOfFileSpec = this.files;
    int j = arrayOfFileSpec.length;
    int i = 0;
    while (i < j)
    {
      FileSpec localFileSpec = arrayOfFileSpec[i];
      if (paramString.equals(localFileSpec.id()))
        return localFileSpec;
      i += 1;
    }
    return null;
  }

  @Deprecated
  public FragmentSpec getFragment(String paramString)
  {
    FragmentSpec[] arrayOfFragmentSpec = this.fragments;
    int j = arrayOfFragmentSpec.length;
    int i = 0;
    while (i < j)
    {
      FragmentSpec localFragmentSpec = arrayOfFragmentSpec[i];
      if (paramString.equalsIgnoreCase(localFragmentSpec.host()))
        return localFragmentSpec;
      i += 1;
    }
    return null;
  }

  public FragmentSpec getFragment(String paramString, int paramInt)
  {
    FragmentSpec[] arrayOfFragmentSpec = this.fragments;
    int j = arrayOfFragmentSpec.length;
    int i = 0;
    while (i < j)
    {
      FragmentSpec localFragmentSpec = arrayOfFragmentSpec[i];
      if ((paramString.equalsIgnoreCase(localFragmentSpec.host())) && ((paramInt <= 0) || ((paramInt >= localFragmentSpec.minVersion()) && ((localFragmentSpec.maxVersion() == 0) || (paramInt <= localFragmentSpec.maxVersion())))))
        return localFragmentSpec;
      i += 1;
    }
    return null;
  }

  public String id()
  {
    return this.id;
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.id);
    if ((this.version != null) && (!this.id.contains(this.version)))
      localStringBuilder.append(" v").append(this.version);
    localStringBuilder.append(" (").append(this.files.length).append(" files, ").append(this.fragments.length).append(" fragments)");
    return localStringBuilder.toString();
  }

  public String version()
  {
    return this.version;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.id);
    paramParcel.writeString(this.version);
    paramParcel.writeTypedArray(this.files, 0);
    paramParcel.writeTypedArray(this.fragments, 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.model.SiteSpec
 * JD-Core Version:    0.6.0
 */
package com.dianping.base.basic;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public abstract class ClassLoaderSavedState
  implements Parcelable
{
  public static final Parcelable.Creator<ClassLoaderSavedState> CREATOR;
  public static final ClassLoaderSavedState EMPTY_STATE = new ClassLoaderSavedState()
  {
  };
  private ClassLoader mClassLoader = null;
  private Parcelable mSuperState = EMPTY_STATE;

  static
  {
    CREATOR = new Parcelable.Creator()
    {
      public ClassLoaderSavedState createFromParcel(Parcel paramParcel)
      {
        if (paramParcel.readParcelable(null) != null)
          throw new IllegalStateException("superState must be null");
        return ClassLoaderSavedState.EMPTY_STATE;
      }

      public ClassLoaderSavedState[] newArray(int paramInt)
      {
        return new ClassLoaderSavedState[paramInt];
      }
    };
  }

  private ClassLoaderSavedState()
  {
    this.mSuperState = null;
    this.mClassLoader = null;
  }

  protected ClassLoaderSavedState(Parcel paramParcel)
  {
    paramParcel = paramParcel.readParcelable(this.mClassLoader);
    if (paramParcel != null);
    while (true)
    {
      this.mSuperState = paramParcel;
      return;
      paramParcel = EMPTY_STATE;
    }
  }

  protected ClassLoaderSavedState(Parcelable paramParcelable, ClassLoader paramClassLoader)
  {
    this.mClassLoader = paramClassLoader;
    if (paramParcelable == null)
      throw new IllegalArgumentException("superState must not be null");
    if (paramParcelable != EMPTY_STATE);
    while (true)
    {
      this.mSuperState = paramParcelable;
      return;
      paramParcelable = null;
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public final Parcelable getSuperState()
  {
    return this.mSuperState;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(this.mSuperState, paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.ClassLoaderSavedState
 * JD-Core Version:    0.6.0
 */
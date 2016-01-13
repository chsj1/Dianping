package com.dianping.base.widget;

import android.widget.SectionIndexer;
import java.util.Arrays;

public class AlphabetIndexer
  implements SectionIndexer
{
  private final int mCount;
  private final int[] mPositions;
  private final String[] mSections;

  public AlphabetIndexer(String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    if ((paramArrayOfString == null) || (paramArrayOfInt == null))
      throw new NullPointerException();
    if (paramArrayOfString.length != paramArrayOfInt.length)
      throw new IllegalArgumentException("The sections and counts arrays must have the same length");
    this.mSections = paramArrayOfString;
    this.mPositions = new int[paramArrayOfInt.length];
    int j = 0;
    int i = 0;
    if (i < paramArrayOfInt.length)
    {
      if (this.mSections[i] == null)
        this.mSections[i] = "";
      while (true)
      {
        this.mPositions[i] = j;
        j += paramArrayOfInt[i];
        i += 1;
        break;
        this.mSections[i] = this.mSections[i].trim();
      }
    }
    this.mCount = j;
  }

  public int getPositionForSection(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.mSections.length))
      return -1;
    return this.mPositions[paramInt];
  }

  public int getSectionForPosition(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.mCount))
      paramInt = -1;
    int i;
    do
    {
      return paramInt;
      i = Arrays.binarySearch(this.mPositions, paramInt);
      paramInt = i;
    }
    while (i >= 0);
    return -i - 2;
  }

  public Object[] getSections()
  {
    return this.mSections;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.AlphabetIndexer
 * JD-Core Version:    0.6.0
 */
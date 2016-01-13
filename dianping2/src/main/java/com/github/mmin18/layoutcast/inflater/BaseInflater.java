package com.github.mmin18.layoutcast.inflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class BaseInflater extends LayoutInflater
{
  private static final String[] sClassPrefixList = { "android.widget.", "android.webkit." };

  public BaseInflater(Context paramContext)
  {
    super(paramContext);
  }

  protected BaseInflater(LayoutInflater paramLayoutInflater, Context paramContext)
  {
    super(paramLayoutInflater, paramContext);
  }

  public LayoutInflater cloneInContext(Context paramContext)
  {
    return new BaseInflater(this, paramContext);
  }

  protected View onCreateView(String paramString, AttributeSet paramAttributeSet)
    throws ClassNotFoundException
  {
    String[] arrayOfString = sClassPrefixList;
    int j = arrayOfString.length;
    int i = 0;
    while (i < j)
    {
      Object localObject = arrayOfString[i];
      try
      {
        localObject = createView(paramString, (String)localObject, paramAttributeSet);
        if (localObject != null)
          return localObject;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        i += 1;
      }
    }
    return (View)super.onCreateView(paramString, paramAttributeSet);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.github.mmin18.layoutcast.inflater.BaseInflater
 * JD-Core Version:    0.6.0
 */
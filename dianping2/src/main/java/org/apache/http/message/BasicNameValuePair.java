package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.NameValuePair;

public class BasicNameValuePair
  implements NameValuePair, Cloneable, Serializable
{
  public static final int HASH_OFFSET = 37;
  public static final int HASH_SEED = 17;
  private static final long serialVersionUID = -6437800749411518984L;
  private final String name;
  private final String value;

  public BasicNameValuePair(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      throw new IllegalArgumentException("Name may not be null");
    this.name = paramString1;
    this.value = paramString2;
  }

  private int hashCode(int paramInt1, int paramInt2)
  {
    return paramInt1 * 37 + paramInt2;
  }

  private int hashCode(int paramInt, Object paramObject)
  {
    if (paramObject != null);
    for (int i = paramObject.hashCode(); ; i = 0)
      return hashCode(paramInt, i);
  }

  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject);
    while (true)
    {
      return true;
      if (!(paramObject instanceof Serializable))
        break;
      paramObject = (BasicNameValuePair)paramObject;
      if ((!this.name.equals(paramObject.name)) || (!equals(this.value, paramObject.value)))
        return false;
    }
    return false;
  }

  public boolean equals(Object paramObject1, Object paramObject2)
  {
    if (paramObject1 == null)
      return paramObject2 == null;
    return paramObject1.equals(paramObject2);
  }

  public String getName()
  {
    return this.name;
  }

  public String getValue()
  {
    return this.value;
  }

  public int hashCode()
  {
    return hashCode(hashCode(17, this.name), this.value);
  }

  public String toString()
  {
    if (this.value == null)
      return this.name;
    StringBuilder localStringBuilder = new StringBuilder(this.name.length() + 1 + this.value.length());
    localStringBuilder.append(this.name);
    localStringBuilder.append("=");
    localStringBuilder.append(this.value);
    return localStringBuilder.toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicNameValuePair
 * JD-Core Version:    0.6.0
 */
package com.google.zxing.client.result;

public abstract class ParsedResult
{
  private final ParsedResultType type;

  protected ParsedResult(ParsedResultType paramParsedResultType)
  {
    this.type = paramParsedResultType;
  }

  public static void maybeAppend(String paramString, StringBuilder paramStringBuilder)
  {
    if ((paramString != null) && (!paramString.isEmpty()))
    {
      if (paramStringBuilder.length() > 0)
        paramStringBuilder.append('\n');
      paramStringBuilder.append(paramString);
    }
  }

  public static void maybeAppend(String[] paramArrayOfString, StringBuilder paramStringBuilder)
  {
    if (paramArrayOfString != null)
    {
      int j = paramArrayOfString.length;
      int i = 0;
      while (i < j)
      {
        maybeAppend(paramArrayOfString[i], paramStringBuilder);
        i += 1;
      }
    }
  }

  public abstract String getDisplayResult();

  public final ParsedResultType getType()
  {
    return this.type;
  }

  public final String toString()
  {
    return getDisplayResult();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.result.ParsedResult
 * JD-Core Version:    0.6.0
 */
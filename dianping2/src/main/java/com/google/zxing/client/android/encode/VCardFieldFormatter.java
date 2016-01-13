package com.google.zxing.client.android.encode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class VCardFieldFormatter
  implements Formatter
{
  private static final Pattern NEWLINE;
  private static final Pattern RESERVED_VCARD_CHARS = Pattern.compile("([\\\\,;])");
  private final List<Map<String, Set<String>>> metadataForIndex;

  static
  {
    NEWLINE = Pattern.compile("\\n");
  }

  VCardFieldFormatter()
  {
    this(null);
  }

  VCardFieldFormatter(List<Map<String, Set<String>>> paramList)
  {
    this.metadataForIndex = paramList;
  }

  private static CharSequence formatMetadata(CharSequence paramCharSequence, Map<String, Set<String>> paramMap)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramMap != null)
    {
      paramMap = paramMap.entrySet().iterator();
      while (paramMap.hasNext())
      {
        Object localObject = (Map.Entry)paramMap.next();
        Set localSet = (Set)((Map.Entry)localObject).getValue();
        if ((localSet == null) || (localSet.isEmpty()))
          continue;
        localStringBuilder.append(';').append((String)((Map.Entry)localObject).getKey()).append('=');
        if (localSet.size() > 1)
          localStringBuilder.append('"');
        localObject = localSet.iterator();
        localStringBuilder.append((String)((Iterator)localObject).next());
        while (((Iterator)localObject).hasNext())
          localStringBuilder.append(',').append((String)((Iterator)localObject).next());
        if (localSet.size() <= 1)
          continue;
        localStringBuilder.append('"');
      }
    }
    localStringBuilder.append(':').append(paramCharSequence);
    return (CharSequence)localStringBuilder;
  }

  public CharSequence format(CharSequence paramCharSequence, int paramInt)
  {
    paramCharSequence = RESERVED_VCARD_CHARS.matcher(paramCharSequence).replaceAll("\\\\$1");
    String str = NEWLINE.matcher(paramCharSequence).replaceAll("");
    if ((this.metadataForIndex == null) || (this.metadataForIndex.size() <= paramInt));
    for (paramCharSequence = null; ; paramCharSequence = (Map)this.metadataForIndex.get(paramInt))
      return formatMetadata(str, paramCharSequence);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.encode.VCardFieldFormatter
 * JD-Core Version:    0.6.0
 */
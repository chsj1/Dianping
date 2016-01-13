package com.google.zxing.client.android.encode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class VCardContactEncoder extends ContactEncoder
{
  private static final char TERMINATOR = '\n';

  static List<Map<String, Set<String>>> buildPhoneMetadata(Collection<String> paramCollection, List<String> paramList)
  {
    Object localObject1;
    if ((paramList == null) || (paramList.isEmpty()))
      localObject1 = null;
    ArrayList localArrayList;
    int i;
    do
    {
      return localObject1;
      localArrayList = new ArrayList();
      i = 0;
      localObject1 = localArrayList;
    }
    while (i >= paramCollection.size());
    if (paramList.size() <= i)
      localArrayList.add(null);
    while (true)
    {
      i += 1;
      break;
      Object localObject2 = new HashMap();
      localArrayList.add(localObject2);
      localObject1 = new HashSet();
      ((Map)localObject2).put("TYPE", localObject1);
      String str = (String)paramList.get(i);
      localObject2 = maybeIntValue(str);
      if (localObject2 == null)
      {
        ((Set)localObject1).add(str);
        continue;
      }
      str = vCardPurposeLabelForAndroidType(((Integer)localObject2).intValue());
      localObject2 = vCardContextLabelForAndroidType(((Integer)localObject2).intValue());
      if (str != null)
        ((Set)localObject1).add(str);
      if (localObject2 == null)
        continue;
      ((Set)localObject1).add(localObject2);
    }
  }

  private static Integer maybeIntValue(String paramString)
  {
    try
    {
      paramString = Integer.valueOf(paramString);
      return paramString;
    }
    catch (java.lang.NumberFormatException paramString)
    {
    }
    return null;
  }

  private static String vCardContextLabelForAndroidType(int paramInt)
  {
    switch (paramInt)
    {
    case 7:
    case 8:
    case 9:
    case 11:
    case 12:
    case 13:
    case 14:
    case 15:
    case 16:
    default:
      return null;
    case 1:
    case 2:
    case 5:
    case 6:
      return "home";
    case 3:
    case 4:
    case 10:
    case 17:
    case 18:
    }
    return "work";
  }

  private static String vCardPurposeLabelForAndroidType(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return null;
    case 4:
    case 5:
    case 13:
      return "fax";
    case 6:
    case 18:
      return "pager";
    case 16:
      return "textphone";
    case 20:
    }
    return "text";
  }

  public String[] encode(List<String> paramList1, String paramString1, List<String> paramList2, List<String> paramList3, List<String> paramList4, List<String> paramList5, List<String> paramList6, String paramString2)
  {
    StringBuilder localStringBuilder1 = new StringBuilder(100);
    localStringBuilder1.append("BEGIN:VCARD").append('\n');
    localStringBuilder1.append("VERSION:3.0").append('\n');
    StringBuilder localStringBuilder2 = new StringBuilder(100);
    VCardFieldFormatter localVCardFieldFormatter = new VCardFieldFormatter();
    appendUpToUnique(localStringBuilder1, localStringBuilder2, "N", paramList1, 1, null, localVCardFieldFormatter, '\n');
    append(localStringBuilder1, localStringBuilder2, "ORG", paramString1, localVCardFieldFormatter, '\n');
    appendUpToUnique(localStringBuilder1, localStringBuilder2, "ADR", paramList2, 1, null, localVCardFieldFormatter, '\n');
    paramList1 = buildPhoneMetadata(paramList3, paramList4);
    appendUpToUnique(localStringBuilder1, localStringBuilder2, "TEL", paramList3, 2147483647, new VCardTelDisplayFormatter(paramList1), new VCardFieldFormatter(paramList1), '\n');
    appendUpToUnique(localStringBuilder1, localStringBuilder2, "EMAIL", paramList5, 2147483647, null, localVCardFieldFormatter, '\n');
    appendUpToUnique(localStringBuilder1, localStringBuilder2, "URL", paramList6, 2147483647, null, localVCardFieldFormatter, '\n');
    append(localStringBuilder1, localStringBuilder2, "NOTE", paramString2, localVCardFieldFormatter, '\n');
    localStringBuilder1.append("END:VCARD").append('\n');
    return new String[] { localStringBuilder1.toString(), localStringBuilder2.toString() };
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.encode.VCardContactEncoder
 * JD-Core Version:    0.6.0
 */
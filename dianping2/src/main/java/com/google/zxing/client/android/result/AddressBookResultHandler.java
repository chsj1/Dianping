package com.google.zxing.client.android.result;

import android.app.Activity;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import com.google.zxing.client.android.R.string;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class AddressBookResultHandler extends ResultHandler
{
  private static final int[] BUTTON_TEXTS;
  private static final DateFormat[] DATE_FORMATS = { new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH), new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH) };
  private int buttonCount;
  private final boolean[] fields;

  static
  {
    DateFormat[] arrayOfDateFormat = DATE_FORMATS;
    int j = arrayOfDateFormat.length;
    int i = 0;
    while (i < j)
    {
      arrayOfDateFormat[i].setLenient(false);
      i += 1;
    }
    BUTTON_TEXTS = new int[] { R.string.button_add_contact, R.string.button_show_map, R.string.button_dial, R.string.button_email };
  }

  public AddressBookResultHandler(Activity paramActivity, ParsedResult paramParsedResult)
  {
    super(paramActivity, paramParsedResult);
    paramActivity = (AddressBookParsedResult)paramParsedResult;
    paramParsedResult = paramActivity.getAddresses();
    int j;
    int k;
    if ((paramParsedResult != null) && (paramParsedResult.length > 0) && (paramParsedResult[0] != null) && (!paramParsedResult[0].isEmpty()))
    {
      j = 1;
      paramParsedResult = paramActivity.getPhoneNumbers();
      if ((paramParsedResult == null) || (paramParsedResult.length <= 0))
        break label159;
      k = 1;
      label60: paramActivity = paramActivity.getEmails();
      if ((paramActivity == null) || (paramActivity.length <= 0))
        break label165;
    }
    label159: label165: for (int m = 1; ; m = 0)
    {
      this.fields = new boolean[4];
      this.fields[0] = true;
      this.fields[1] = j;
      this.fields[2] = k;
      this.fields[3] = m;
      this.buttonCount = 0;
      int i = 0;
      while (i < 4)
      {
        if (this.fields[i] != 0)
          this.buttonCount += 1;
        i += 1;
      }
      j = 0;
      break;
      k = 0;
      break label60;
    }
  }

  private int mapIndexToAction(int paramInt)
  {
    if (paramInt < this.buttonCount)
    {
      int j = -1;
      int i = 0;
      while (i < 4)
      {
        int k = j;
        if (this.fields[i] != 0)
          k = j + 1;
        if (k == paramInt)
          return i;
        i += 1;
        j = k;
      }
    }
    return -1;
  }

  private static Date parseDate(String paramString)
  {
    DateFormat[] arrayOfDateFormat = DATE_FORMATS;
    int j = arrayOfDateFormat.length;
    int i = 0;
    while (i < j)
    {
      Object localObject = arrayOfDateFormat[i];
      try
      {
        localObject = ((DateFormat)localObject).parse(paramString);
        return localObject;
      }
      catch (ParseException localParseException)
      {
        i += 1;
      }
    }
    return (Date)null;
  }

  public int getButtonCount()
  {
    return this.buttonCount;
  }

  public int getButtonText(int paramInt)
  {
    return BUTTON_TEXTS[mapIndexToAction(paramInt)];
  }

  public CharSequence getDisplayContents()
  {
    Object localObject1 = (AddressBookParsedResult)getResult();
    StringBuilder localStringBuilder = new StringBuilder(100);
    ParsedResult.maybeAppend(((AddressBookParsedResult)localObject1).getNames(), localStringBuilder);
    int j = localStringBuilder.length();
    Object localObject2 = ((AddressBookParsedResult)localObject1).getPronunciation();
    if ((localObject2 != null) && (!((String)localObject2).isEmpty()))
    {
      localStringBuilder.append("\n(");
      localStringBuilder.append((String)localObject2);
      localStringBuilder.append(')');
    }
    ParsedResult.maybeAppend(((AddressBookParsedResult)localObject1).getTitle(), localStringBuilder);
    ParsedResult.maybeAppend(((AddressBookParsedResult)localObject1).getOrg(), localStringBuilder);
    ParsedResult.maybeAppend(((AddressBookParsedResult)localObject1).getAddresses(), localStringBuilder);
    localObject2 = ((AddressBookParsedResult)localObject1).getPhoneNumbers();
    if (localObject2 != null)
    {
      int k = localObject2.length;
      int i = 0;
      while (i < k)
      {
        String str = localObject2[i];
        if (str != null)
          ParsedResult.maybeAppend(PhoneNumberUtils.formatNumber(str), localStringBuilder);
        i += 1;
      }
    }
    ParsedResult.maybeAppend(((AddressBookParsedResult)localObject1).getEmails(), localStringBuilder);
    ParsedResult.maybeAppend(((AddressBookParsedResult)localObject1).getURLs(), localStringBuilder);
    localObject2 = ((AddressBookParsedResult)localObject1).getBirthday();
    if ((localObject2 != null) && (!((String)localObject2).isEmpty()))
    {
      localObject2 = parseDate((String)localObject2);
      if (localObject2 != null)
        ParsedResult.maybeAppend(DateFormat.getDateInstance(2).format(Long.valueOf(((Date)localObject2).getTime())), localStringBuilder);
    }
    ParsedResult.maybeAppend(((AddressBookParsedResult)localObject1).getNote(), localStringBuilder);
    if (j > 0)
    {
      localObject1 = new SpannableString(localStringBuilder.toString());
      ((Spannable)localObject1).setSpan(new StyleSpan(1), 0, j, 0);
      return localObject1;
    }
    return (CharSequence)(CharSequence)localStringBuilder.toString();
  }

  public int getDisplayTitle()
  {
    return R.string.result_address_book;
  }

  public void handleButtonPress(int paramInt)
  {
    AddressBookParsedResult localAddressBookParsedResult = (AddressBookParsedResult)getResult();
    Object localObject1 = localAddressBookParsedResult.getAddresses();
    if ((localObject1 == null) || (localObject1.length < 1))
    {
      localObject1 = null;
      label27: localObject2 = localAddressBookParsedResult.getAddressTypes();
      if ((localObject2 != null) && (localObject2.length >= 1))
        break label88;
    }
    label88: for (Object localObject2 = null; ; localObject2 = localObject2[0])
      switch (mapIndexToAction(paramInt))
      {
      default:
        return;
        localObject1 = localObject1[0];
        break label27;
      case 0:
      case 1:
      case 2:
      case 3:
      }
    addContact(localAddressBookParsedResult.getNames(), localAddressBookParsedResult.getNicknames(), localAddressBookParsedResult.getPronunciation(), localAddressBookParsedResult.getPhoneNumbers(), localAddressBookParsedResult.getPhoneTypes(), localAddressBookParsedResult.getEmails(), localAddressBookParsedResult.getEmailTypes(), localAddressBookParsedResult.getNote(), localAddressBookParsedResult.getInstantMessenger(), (String)localObject1, (String)localObject2, localAddressBookParsedResult.getOrg(), localAddressBookParsedResult.getTitle(), localAddressBookParsedResult.getURLs(), localAddressBookParsedResult.getBirthday(), localAddressBookParsedResult.getGeo());
    return;
    searchMap((String)localObject1);
    return;
    dialPhone(localAddressBookParsedResult.getPhoneNumbers()[0]);
    return;
    sendEmail(localAddressBookParsedResult.getEmails(), null, null, null, null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.AddressBookResultHandler
 * JD-Core Version:    0.6.0
 */
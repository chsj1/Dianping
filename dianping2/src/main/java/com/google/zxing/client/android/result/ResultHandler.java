package com.google.zxing.client.android.result;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.LocaleManager;
import com.google.zxing.client.android.R.string;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public abstract class ResultHandler
{
  private static final String[] ADDRESS_TYPE_STRINGS;
  private static final int[] ADDRESS_TYPE_VALUES;
  private static final String[] EMAIL_TYPE_STRINGS;
  private static final int[] EMAIL_TYPE_VALUES;
  public static final int MAX_BUTTON_COUNT = 4;
  private static final int NO_TYPE = -1;
  private static final String[] PHONE_TYPE_STRINGS;
  private static final int[] PHONE_TYPE_VALUES;
  private static final String TAG = ResultHandler.class.getSimpleName();
  private final Activity activity;
  private final String customProductSearch;
  private final Result rawResult;
  private final ParsedResult result;

  static
  {
    EMAIL_TYPE_STRINGS = new String[] { "home", "work", "mobile" };
    PHONE_TYPE_STRINGS = new String[] { "home", "work", "mobile", "fax", "pager", "main" };
    ADDRESS_TYPE_STRINGS = new String[] { "home", "work" };
    EMAIL_TYPE_VALUES = new int[] { 1, 2, 4 };
    PHONE_TYPE_VALUES = new int[] { 1, 3, 2, 4, 6, 12 };
    ADDRESS_TYPE_VALUES = new int[] { 1, 2 };
  }

  ResultHandler(Activity paramActivity, ParsedResult paramParsedResult)
  {
    this(paramActivity, paramParsedResult, null);
  }

  ResultHandler(Activity paramActivity, ParsedResult paramParsedResult, Result paramResult)
  {
    this.result = paramParsedResult;
    this.activity = paramActivity;
    this.rawResult = paramResult;
    this.customProductSearch = parseCustomSearchURL();
  }

  private static int doToContractType(String paramString, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    if (paramString == null);
    while (true)
    {
      return -1;
      int i = 0;
      while (i < paramArrayOfString.length)
      {
        String str = paramArrayOfString[i];
        if ((paramString.startsWith(str)) || (paramString.startsWith(str.toUpperCase(Locale.ENGLISH))))
          return paramArrayOfInt[i];
        i += 1;
      }
    }
  }

  private String parseCustomSearchURL()
  {
    String str2 = PreferenceManager.getDefaultSharedPreferences(this.activity).getString("preferences_custom_product_search", null);
    String str1 = str2;
    if (str2 != null)
    {
      str1 = str2;
      if (str2.trim().isEmpty())
        str1 = null;
    }
    return str1;
  }

  private static void putExtra(Intent paramIntent, String paramString1, String paramString2)
  {
    if ((paramString2 != null) && (!paramString2.isEmpty()))
      paramIntent.putExtra(paramString1, paramString2);
  }

  private static int toAddressContractType(String paramString)
  {
    return doToContractType(paramString, ADDRESS_TYPE_STRINGS, ADDRESS_TYPE_VALUES);
  }

  private static int toEmailContractType(String paramString)
  {
    return doToContractType(paramString, EMAIL_TYPE_STRINGS, EMAIL_TYPE_VALUES);
  }

  private static int toPhoneContractType(String paramString)
  {
    return doToContractType(paramString, PHONE_TYPE_STRINGS, PHONE_TYPE_VALUES);
  }

  final void addContact(String[] paramArrayOfString1, String[] paramArrayOfString2, String paramString1, String[] paramArrayOfString3, String[] paramArrayOfString4, String[] paramArrayOfString5, String[] paramArrayOfString6, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String[] paramArrayOfString7, String paramString8, String[] paramArrayOfString8)
  {
    Intent localIntent = new Intent("android.intent.action.INSERT_OR_EDIT", ContactsContract.Contacts.CONTENT_URI);
    localIntent.setType("vnd.android.cursor.item/contact");
    if (paramArrayOfString1 != null)
    {
      paramArrayOfString1 = paramArrayOfString1[0];
      putExtra(localIntent, "name", paramArrayOfString1);
      putExtra(localIntent, "phonetic_name", paramString1);
      if (paramArrayOfString3 == null)
        break label149;
    }
    int j;
    int k;
    label149: for (int i = paramArrayOfString3.length; ; i = 0)
    {
      j = Math.min(i, Contents.PHONE_KEYS.length);
      i = 0;
      while (i < j)
      {
        putExtra(localIntent, Contents.PHONE_KEYS[i], paramArrayOfString3[i]);
        if ((paramArrayOfString4 != null) && (i < paramArrayOfString4.length))
        {
          k = toPhoneContractType(paramArrayOfString4[i]);
          if (k >= 0)
            localIntent.putExtra(Contents.PHONE_TYPE_KEYS[i], k);
        }
        i += 1;
      }
      paramArrayOfString1 = null;
      break;
    }
    if (paramArrayOfString5 != null);
    for (i = paramArrayOfString5.length; ; i = 0)
    {
      j = Math.min(i, Contents.EMAIL_KEYS.length);
      i = 0;
      while (i < j)
      {
        putExtra(localIntent, Contents.EMAIL_KEYS[i], paramArrayOfString5[i]);
        if ((paramArrayOfString6 != null) && (i < paramArrayOfString6.length))
        {
          k = toEmailContractType(paramArrayOfString6[i]);
          if (k >= 0)
            localIntent.putExtra(Contents.EMAIL_TYPE_KEYS[i], k);
        }
        i += 1;
      }
    }
    paramArrayOfString1 = new ArrayList();
    if (paramArrayOfString7 != null)
    {
      j = paramArrayOfString7.length;
      i = 0;
      if (i < j)
      {
        paramString1 = paramArrayOfString7[i];
        if ((paramString1 == null) || (paramString1.isEmpty()))
          break label621;
        paramArrayOfString3 = new ContentValues(2);
        paramArrayOfString3.put("mimetype", "vnd.android.cursor.item/website");
        paramArrayOfString3.put("data1", paramString1);
        paramArrayOfString1.add(paramArrayOfString3);
      }
    }
    if (paramString8 != null)
    {
      paramString1 = new ContentValues(3);
      paramString1.put("mimetype", "vnd.android.cursor.item/contact_event");
      paramString1.put("data2", Integer.valueOf(3));
      paramString1.put("data1", paramString8);
      paramArrayOfString1.add(paramString1);
    }
    if (paramArrayOfString2 != null)
    {
      j = paramArrayOfString2.length;
      i = 0;
    }
    while (true)
    {
      if (i < j)
      {
        paramString1 = paramArrayOfString2[i];
        if ((paramString1 != null) && (!paramString1.isEmpty()))
        {
          paramArrayOfString2 = new ContentValues(3);
          paramArrayOfString2.put("mimetype", "vnd.android.cursor.item/nickname");
          paramArrayOfString2.put("data2", Integer.valueOf(1));
          paramArrayOfString2.put("data1", paramString1);
          paramArrayOfString1.add(paramArrayOfString2);
        }
      }
      else
      {
        if (!paramArrayOfString1.isEmpty())
          localIntent.putParcelableArrayListExtra("data", paramArrayOfString1);
        paramArrayOfString1 = new StringBuilder();
        if (paramString2 != null)
          paramArrayOfString1.append('\n').append(paramString2);
        if (paramArrayOfString8 != null)
          paramArrayOfString1.append('\n').append(paramArrayOfString8[0]).append(',').append(paramArrayOfString8[1]);
        if (paramArrayOfString1.length() > 0)
          putExtra(localIntent, "notes", paramArrayOfString1.substring(1));
        putExtra(localIntent, "im_handle", paramString3);
        putExtra(localIntent, "postal", paramString4);
        if (paramString5 != null)
        {
          i = toAddressContractType(paramString5);
          if (i >= 0)
            localIntent.putExtra("postal_type", i);
        }
        putExtra(localIntent, "company", paramString6);
        putExtra(localIntent, "job_title", paramString7);
        launchIntent(localIntent);
        return;
        label621: i += 1;
        break;
      }
      i += 1;
    }
  }

  final void addEmailOnlyContact(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    addContact(null, null, null, null, null, paramArrayOfString1, paramArrayOfString2, null, null, null, null, null, null, null, null, null);
  }

  final void addPhoneOnlyContact(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    addContact(null, null, null, paramArrayOfString1, paramArrayOfString2, null, null, null, null, null, null, null, null, null, null, null);
  }

  public boolean areContentsSecure()
  {
    return false;
  }

  final void dialPhone(String paramString)
  {
    launchIntent(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + paramString)));
  }

  final void dialPhoneFromUri(String paramString)
  {
    launchIntent(new Intent("android.intent.action.DIAL", Uri.parse(paramString)));
  }

  final String fillInCustomSearchURL(String paramString)
  {
    if (this.customProductSearch == null)
      return paramString;
    try
    {
      String str1 = URLEncoder.encode(paramString, "UTF-8");
      String str3 = this.customProductSearch;
      paramString = str3;
      if (this.rawResult != null)
      {
        str3 = str3.replaceFirst("%f(?![0-9a-f])", this.rawResult.getBarcodeFormat().toString());
        paramString = str3;
        if (str3.contains("%t"))
          paramString = str3.replace("%t", ResultParser.parseResult(this.rawResult).getType().toString());
      }
      return paramString.replace("%s", str1);
    }
    catch (UnsupportedEncodingException str2)
    {
      while (true)
        String str2 = paramString;
    }
  }

  final Activity getActivity()
  {
    return this.activity;
  }

  public abstract int getButtonCount();

  public abstract int getButtonText(int paramInt);

  public Integer getDefaultButtonID()
  {
    return null;
  }

  final void getDirections(double paramDouble1, double paramDouble2)
  {
    launchIntent(new Intent("android.intent.action.VIEW", Uri.parse("http://maps.google." + LocaleManager.getCountryTLD(this.activity) + "/maps?f=d&daddr=" + paramDouble1 + ',' + paramDouble2)));
  }

  public CharSequence getDisplayContents()
  {
    return this.result.getDisplayResult().replace("\r", "");
  }

  public abstract int getDisplayTitle();

  public final ParsedResult getResult()
  {
    return this.result;
  }

  public final ParsedResultType getType()
  {
    return this.result.getType();
  }

  public abstract void handleButtonPress(int paramInt);

  final boolean hasCustomProductSearch()
  {
    return this.customProductSearch != null;
  }

  final void launchIntent(Intent paramIntent)
  {
    try
    {
      rawLaunchIntent(paramIntent);
      return;
    }
    catch (ActivityNotFoundException paramIntent)
    {
      paramIntent = new AlertDialog.Builder(this.activity);
      paramIntent.setTitle(R.string.app_name);
      paramIntent.setMessage(R.string.msg_intent_failed);
      paramIntent.setPositiveButton(R.string.button_ok, null);
      paramIntent.show();
    }
  }

  final void openBookSearch(String paramString)
  {
    launchIntent(new Intent("android.intent.action.VIEW", Uri.parse("http://books.google." + LocaleManager.getBookSearchCountryTLD(this.activity) + "/books?vid=isbn" + paramString)));
  }

  final void openMap(String paramString)
  {
    launchIntent(new Intent("android.intent.action.VIEW", Uri.parse(paramString)));
  }

  final void openProductSearch(String paramString)
  {
    launchIntent(new Intent("android.intent.action.VIEW", Uri.parse("http://www.google." + LocaleManager.getProductSearchCountryTLD(this.activity) + "/m/products?q=" + paramString + "&source=zxing")));
  }

  final void openURL(String paramString)
  {
    String str;
    if (paramString.startsWith("HTTP://"))
      str = "http" + paramString.substring(4);
    while (true)
    {
      paramString = new Intent("android.intent.action.VIEW", Uri.parse(str));
      try
      {
        launchIntent(paramString);
        return;
        str = paramString;
        if (!paramString.startsWith("HTTPS://"))
          continue;
        str = "https" + paramString.substring(5);
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        Log.w(TAG, "Nothing available to handle " + paramString);
      }
    }
  }

  final void rawLaunchIntent(Intent paramIntent)
  {
    if (paramIntent != null)
    {
      paramIntent.addFlags(524288);
      Log.d(TAG, "Launching intent: " + paramIntent + " with extras: " + paramIntent.getExtras());
      this.activity.startActivity(paramIntent);
    }
  }

  final void searchMap(String paramString)
  {
    launchIntent(new Intent("android.intent.action.VIEW", Uri.parse("geo:0,0?q=" + Uri.encode(paramString))));
  }

  final void sendEmail(String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString1, String paramString2)
  {
    Intent localIntent = new Intent("android.intent.action.SEND", Uri.parse("mailto:"));
    if ((paramArrayOfString1 != null) && (paramArrayOfString1.length != 0))
      localIntent.putExtra("android.intent.extra.EMAIL", paramArrayOfString1);
    if ((paramArrayOfString2 != null) && (paramArrayOfString2.length != 0))
      localIntent.putExtra("android.intent.extra.CC", paramArrayOfString2);
    if ((paramArrayOfString3 != null) && (paramArrayOfString3.length != 0))
      localIntent.putExtra("android.intent.extra.BCC", paramArrayOfString3);
    putExtra(localIntent, "android.intent.extra.SUBJECT", paramString1);
    putExtra(localIntent, "android.intent.extra.TEXT", paramString2);
    localIntent.setType("text/plain");
    launchIntent(localIntent);
  }

  final void sendMMS(String paramString1, String paramString2, String paramString3)
  {
    sendMMSFromUri("mmsto:" + paramString1, paramString2, paramString3);
  }

  final void sendMMSFromUri(String paramString1, String paramString2, String paramString3)
  {
    paramString1 = new Intent("android.intent.action.SENDTO", Uri.parse(paramString1));
    if ((paramString2 == null) || (paramString2.isEmpty()))
      putExtra(paramString1, "subject", this.activity.getString(R.string.msg_default_mms_subject));
    while (true)
    {
      putExtra(paramString1, "sms_body", paramString3);
      paramString1.putExtra("compose_mode", true);
      launchIntent(paramString1);
      return;
      putExtra(paramString1, "subject", paramString2);
    }
  }

  final void sendSMS(String paramString1, String paramString2)
  {
    sendSMSFromUri("smsto:" + paramString1, paramString2);
  }

  final void sendSMSFromUri(String paramString1, String paramString2)
  {
    paramString1 = new Intent("android.intent.action.SENDTO", Uri.parse(paramString1));
    putExtra(paramString1, "sms_body", paramString2);
    paramString1.putExtra("compose_mode", true);
    launchIntent(paramString1);
  }

  final void shareByEmail(String paramString)
  {
    sendEmail(null, null, null, null, paramString);
  }

  final void shareBySMS(String paramString)
  {
    sendSMSFromUri("smsto:", paramString);
  }

  final void webSearch(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.WEB_SEARCH");
    localIntent.putExtra("query", paramString);
    launchIntent(localIntent);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.ResultHandler
 * JD-Core Version:    0.6.0
 */
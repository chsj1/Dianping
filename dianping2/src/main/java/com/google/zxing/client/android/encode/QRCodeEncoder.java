package com.google.zxing.client.android.encode;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.R.string;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.BitMatrix;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.List<Ljava.lang.String;>;
import java.util.Map;

public final class QRCodeEncoder
{
  private static final int BLACK = -16777216;
  private static final String TAG = QRCodeEncoder.class.getSimpleName();
  private static final int WHITE = -1;
  private final Context activity;
  private String contents;
  private String displayContents;
  private BarcodeFormat format;
  private final int height;
  private Map<EncodeHintType, Object> hints;
  private String title;
  private final boolean useVCard;
  private final int width;

  public QRCodeEncoder(Context paramContext, Intent paramIntent, int paramInt1, int paramInt2, boolean paramBoolean)
    throws WriterException
  {
    this.activity = paramContext;
    this.width = paramInt1;
    this.height = paramInt2;
    this.useVCard = paramBoolean;
    paramContext = paramIntent.getAction();
    if (paramContext.equals("com.google.zxing.client.android.ENCODE"))
      encodeContentsFromZXingIntent(paramIntent);
    do
      return;
    while (!paramContext.equals("android.intent.action.SEND"));
    encodeContentsFromShareIntent(paramIntent);
  }

  public QRCodeEncoder(Context paramContext, Intent paramIntent, int paramInt1, int paramInt2, boolean paramBoolean, Map<EncodeHintType, Object> paramMap)
    throws WriterException
  {
    this.activity = paramContext;
    this.width = paramInt1;
    this.height = paramInt2;
    this.useVCard = paramBoolean;
    this.hints = paramMap;
    paramContext = paramIntent.getAction();
    if (paramContext.equals("com.google.zxing.client.android.ENCODE"))
      encodeContentsFromZXingIntent(paramIntent);
    do
      return;
    while (!paramContext.equals("android.intent.action.SEND"));
    encodeContentsFromShareIntent(paramIntent);
  }

  private void encodeContentsFromShareIntent(Intent paramIntent)
    throws WriterException
  {
    if (paramIntent.hasExtra("android.intent.extra.STREAM"))
    {
      encodeFromStreamExtra(paramIntent);
      return;
    }
    encodeFromTextExtras(paramIntent);
  }

  private boolean encodeContentsFromZXingIntent(Intent paramIntent)
  {
    String str = paramIntent.getStringExtra("ENCODE_FORMAT");
    this.format = null;
    if (str != null);
    try
    {
      this.format = BarcodeFormat.valueOf(str);
      label24: if ((this.format == null) || (this.format == BarcodeFormat.QR_CODE))
      {
        str = paramIntent.getStringExtra("ENCODE_TYPE");
        if ((str != null) && (!str.isEmpty()));
      }
      while (true)
      {
        return false;
        this.format = BarcodeFormat.QR_CODE;
        encodeQRCodeContents(paramIntent, str);
        while ((this.contents != null) && (!this.contents.isEmpty()))
        {
          return true;
          paramIntent = paramIntent.getStringExtra("ENCODE_DATA");
          if ((paramIntent == null) || (paramIntent.isEmpty()))
            continue;
          this.contents = paramIntent;
          this.displayContents = paramIntent;
          this.title = this.activity.getString(R.string.contents_text);
        }
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label24;
    }
  }

  private void encodeFromStreamExtra(Intent paramIntent)
    throws WriterException
  {
    this.format = BarcodeFormat.QR_CODE;
    paramIntent = paramIntent.getExtras();
    if (paramIntent == null)
      throw new WriterException("No extras");
    Object localObject2 = (Uri)paramIntent.getParcelable("android.intent.extra.STREAM");
    if (localObject2 == null)
      throw new WriterException("No EXTRA_STREAM");
    Object localObject1 = null;
    paramIntent = null;
    try
    {
      InputStream localInputStream = this.activity.getContentResolver().openInputStream((Uri)localObject2);
      if (localInputStream == null)
      {
        paramIntent = localInputStream;
        localObject1 = localInputStream;
        throw new WriterException("Can't open stream for " + localObject2);
      }
    }
    catch (IOException localIOException2)
    {
      localObject1 = paramIntent;
      throw new WriterException(localIOException2);
    }
    finally
    {
      if (localObject1 != null);
      try
      {
        ((InputStream)localObject1).close();
        label126: throw paramIntent;
        paramIntent = localIOException2;
        localObject1 = localIOException2;
        localObject2 = new ByteArrayOutputStream();
        paramIntent = localIOException2;
        localObject1 = localIOException2;
        Object localObject3 = new byte[2048];
        while (true)
        {
          paramIntent = localIOException2;
          localObject1 = localIOException2;
          int i = localIOException2.read(localObject3);
          if (i <= 0)
            break;
          paramIntent = localIOException2;
          localObject1 = localIOException2;
          ((ByteArrayOutputStream)localObject2).write(localObject3, 0, i);
        }
        paramIntent = localIOException2;
        localObject1 = localIOException2;
        localObject2 = ((ByteArrayOutputStream)localObject2).toByteArray();
        paramIntent = localIOException2;
        localObject1 = localIOException2;
        localObject3 = new String(localObject2, 0, localObject2.length, "UTF-8");
        if (localIOException2 != null);
        try
        {
          localIOException2.close();
          label226: Log.d(TAG, "Encoding share intent content:");
          Log.d(TAG, (String)localObject3);
          paramIntent = ResultParser.parseResult(new Result((String)localObject3, localObject2, null, BarcodeFormat.QR_CODE));
          if (!(paramIntent instanceof AddressBookParsedResult))
            throw new WriterException("Result was not an address");
          encodeQRCodeContents((AddressBookParsedResult)paramIntent);
          if ((this.contents == null) || (this.contents.isEmpty()))
            throw new WriterException("No content to encode");
        }
        catch (IOException paramIntent)
        {
          break label226;
        }
      }
      catch (IOException localIOException1)
      {
        break label126;
      }
    }
  }

  private void encodeFromTextExtras(Intent paramIntent)
    throws WriterException
  {
    String str = ContactEncoder.trim(paramIntent.getStringExtra("android.intent.extra.TEXT"));
    Object localObject = str;
    if (str == null)
    {
      str = ContactEncoder.trim(paramIntent.getStringExtra("android.intent.extra.HTML_TEXT"));
      localObject = str;
      if (str == null)
      {
        str = ContactEncoder.trim(paramIntent.getStringExtra("android.intent.extra.SUBJECT"));
        localObject = str;
        if (str == null)
        {
          localObject = paramIntent.getStringArrayExtra("android.intent.extra.EMAIL");
          if (localObject == null)
            break label88;
        }
      }
    }
    label88: for (localObject = ContactEncoder.trim(localObject[0]); (localObject == null) || (((String)localObject).isEmpty()); localObject = "?")
      throw new WriterException("Empty EXTRA_TEXT");
    this.contents = ((String)localObject);
    this.format = BarcodeFormat.QR_CODE;
    if (paramIntent.hasExtra("android.intent.extra.SUBJECT"))
      this.displayContents = paramIntent.getStringExtra("android.intent.extra.SUBJECT");
    while (true)
    {
      this.title = this.activity.getString(R.string.contents_text);
      return;
      if (paramIntent.hasExtra("android.intent.extra.TITLE"))
      {
        this.displayContents = paramIntent.getStringExtra("android.intent.extra.TITLE");
        continue;
      }
      this.displayContents = this.contents;
    }
  }

  private void encodeQRCodeContents(Intent paramIntent, String paramString)
  {
    int i = -1;
    switch (paramString.hashCode())
    {
    default:
      switch (i)
      {
      default:
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      }
    case 1778595596:
    case 1833351709:
    case -1309271157:
    case 709220992:
    case -670199783:
    case 1349204356:
    }
    label503: float f1;
    label599: float f2;
    do
    {
      do
      {
        do
        {
          do
          {
            do
            {
              do
              {
                do
                {
                  return;
                  if (!paramString.equals("TEXT_TYPE"))
                    break;
                  i = 0;
                  break;
                  if (!paramString.equals("EMAIL_TYPE"))
                    break;
                  i = 1;
                  break;
                  if (!paramString.equals("PHONE_TYPE"))
                    break;
                  i = 2;
                  break;
                  if (!paramString.equals("SMS_TYPE"))
                    break;
                  i = 3;
                  break;
                  if (!paramString.equals("CONTACT_TYPE"))
                    break;
                  i = 4;
                  break;
                  if (!paramString.equals("LOCATION_TYPE"))
                    break;
                  i = 5;
                  break;
                  paramIntent = paramIntent.getStringExtra("ENCODE_DATA");
                }
                while ((paramIntent == null) || (paramIntent.isEmpty()));
                this.contents = paramIntent;
                this.displayContents = paramIntent;
                this.title = this.activity.getString(R.string.contents_text);
                return;
                paramIntent = ContactEncoder.trim(paramIntent.getStringExtra("ENCODE_DATA"));
              }
              while (paramIntent == null);
              this.contents = ("mailto:" + paramIntent);
              this.displayContents = paramIntent;
              this.title = this.activity.getString(R.string.contents_email);
              return;
              paramIntent = ContactEncoder.trim(paramIntent.getStringExtra("ENCODE_DATA"));
            }
            while (paramIntent == null);
            this.contents = ("tel:" + paramIntent);
            this.displayContents = PhoneNumberUtils.formatNumber(paramIntent);
            this.title = this.activity.getString(R.string.contents_phone);
            return;
            paramIntent = ContactEncoder.trim(paramIntent.getStringExtra("ENCODE_DATA"));
          }
          while (paramIntent == null);
          this.contents = ("sms:" + paramIntent);
          this.displayContents = PhoneNumberUtils.formatNumber(paramIntent);
          this.title = this.activity.getString(R.string.contents_sms);
          return;
          paramString = paramIntent.getBundleExtra("ENCODE_DATA");
        }
        while (paramString == null);
        String str1 = paramString.getString("name");
        String str2 = paramString.getString("company");
        String str3 = paramString.getString("postal");
        List localList1 = getAllBundleValues(paramString, Contents.PHONE_KEYS);
        List localList2 = getAllBundleValues(paramString, Contents.PHONE_TYPE_KEYS);
        List localList3 = getAllBundleValues(paramString, Contents.EMAIL_KEYS);
        paramIntent = paramString.getString("URL_KEY");
        String str4;
        if (paramIntent == null)
        {
          paramIntent = null;
          str4 = paramString.getString("NOTE_KEY");
          if (!this.useVCard)
            break label599;
        }
        for (paramString = new VCardContactEncoder(); ; paramString = new MECARDContactEncoder())
        {
          paramIntent = paramString.encode(Collections.singletonList(str1), str2, Collections.singletonList(str3), localList1, localList2, localList3, paramIntent, str4);
          if (paramIntent[1].isEmpty())
            break;
          this.contents = paramIntent[0];
          this.displayContents = paramIntent[1];
          this.title = this.activity.getString(R.string.contents_contact);
          return;
          paramIntent = Collections.singletonList(paramIntent);
          break label503;
        }
        paramIntent = paramIntent.getBundleExtra("ENCODE_DATA");
      }
      while (paramIntent == null);
      f1 = paramIntent.getFloat("LAT", 3.4028235E+38F);
      f2 = paramIntent.getFloat("LONG", 3.4028235E+38F);
    }
    while ((f1 == 3.4028235E+38F) || (f2 == 3.4028235E+38F));
    this.contents = ("geo:" + f1 + ',' + f2);
    this.displayContents = (f1 + "," + f2);
    this.title = this.activity.getString(R.string.contents_location);
  }

  private void encodeQRCodeContents(AddressBookParsedResult paramAddressBookParsedResult)
  {
    if (this.useVCard);
    for (Object localObject = new VCardContactEncoder(); ; localObject = new MECARDContactEncoder())
    {
      paramAddressBookParsedResult = ((ContactEncoder)localObject).encode(toList(paramAddressBookParsedResult.getNames()), paramAddressBookParsedResult.getOrg(), toList(paramAddressBookParsedResult.getAddresses()), toList(paramAddressBookParsedResult.getPhoneNumbers()), null, toList(paramAddressBookParsedResult.getEmails()), toList(paramAddressBookParsedResult.getURLs()), null);
      if (!paramAddressBookParsedResult[1].isEmpty())
      {
        this.contents = paramAddressBookParsedResult[0];
        this.displayContents = paramAddressBookParsedResult[1];
        this.title = this.activity.getString(R.string.contents_contact);
      }
      return;
    }
  }

  private static List<String> getAllBundleValues(Bundle paramBundle, String[] paramArrayOfString)
  {
    ArrayList localArrayList = new ArrayList(paramArrayOfString.length);
    int j = paramArrayOfString.length;
    int i = 0;
    if (i < j)
    {
      Object localObject = paramBundle.get(paramArrayOfString[i]);
      if (localObject == null);
      for (localObject = null; ; localObject = localObject.toString())
      {
        localArrayList.add(localObject);
        i += 1;
        break;
      }
    }
    return (List<String>)localArrayList;
  }

  private static String guessAppropriateEncoding(CharSequence paramCharSequence)
  {
    int i = 0;
    while (i < paramCharSequence.length())
    {
      if (paramCharSequence.charAt(i) > 'ÿ')
        return "UTF-8";
      i += 1;
    }
    return null;
  }

  private static List<String> toList(String[] paramArrayOfString)
  {
    if (paramArrayOfString == null)
      return null;
    return Arrays.asList(paramArrayOfString);
  }

  public Bitmap encodeAsBitmap()
    throws WriterException
  {
    Object localObject1 = this.contents;
    if (localObject1 == null)
      return null;
    Object localObject2 = guessAppropriateEncoding((CharSequence)localObject1);
    if ((this.hints == null) && (localObject2 != null))
    {
      this.hints = new EnumMap(EncodeHintType.class);
      this.hints.put(EncodeHintType.CHARACTER_SET, localObject2);
    }
    int m;
    int n;
    while (true)
    {
      int i;
      try
      {
        localObject2 = new MultiFormatWriter().encode((String)localObject1, this.format, this.width, this.height, this.hints);
        m = ((BitMatrix)localObject2).getWidth();
        n = ((BitMatrix)localObject2).getHeight();
        localObject1 = new int[m * n];
        i = 0;
        if (i >= n)
          break;
        int j = 0;
        if (j >= m)
          break label164;
        if (((BitMatrix)localObject2).get(j, i))
        {
          k = -16777216;
          localObject1[(i * m + j)] = k;
          j += 1;
          continue;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        return null;
      }
      int k = -1;
      continue;
      label164: i += 1;
    }
    localObject2 = Bitmap.createBitmap(m, n, Bitmap.Config.ARGB_8888);
    ((Bitmap)localObject2).setPixels(localIllegalArgumentException, 0, m, 0, 0, m, n);
    return (Bitmap)(Bitmap)localObject2;
  }

  String getContents()
  {
    return this.contents;
  }

  String getDisplayContents()
  {
    return this.displayContents;
  }

  String getTitle()
  {
    return this.title;
  }

  boolean isUseVCard()
  {
    return this.useVCard;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.encode.QRCodeEncoder
 * JD-Core Version:    0.6.0
 */
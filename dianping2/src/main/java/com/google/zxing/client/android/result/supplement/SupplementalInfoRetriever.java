package com.google.zxing.client.android.result.supplement;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.widget.TextView;
import com.google.zxing.client.android.common.executor.AsyncTaskExecInterface;
import com.google.zxing.client.android.common.executor.AsyncTaskExecManager;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class SupplementalInfoRetriever extends AsyncTask<Object, Object, Object>
{
  private static final String TAG = "SupplementalInfo";
  private final Collection<Spannable> newContents;
  private final WeakReference<TextView> textViewRef;

  SupplementalInfoRetriever(TextView paramTextView)
  {
    this.textViewRef = new WeakReference(paramTextView);
    this.newContents = new ArrayList();
  }

  static void maybeAddText(String paramString, Collection<String> paramCollection)
  {
    if ((paramString != null) && (!paramString.isEmpty()))
      paramCollection.add(paramString);
  }

  static void maybeAddTextSeries(Collection<String> paramCollection1, Collection<String> paramCollection2)
  {
    if ((paramCollection1 != null) && (!paramCollection1.isEmpty()))
    {
      int i = 1;
      StringBuilder localStringBuilder = new StringBuilder();
      paramCollection1 = paramCollection1.iterator();
      if (paramCollection1.hasNext())
      {
        String str = (String)paramCollection1.next();
        if (i != 0)
          i = 0;
        while (true)
        {
          localStringBuilder.append(str);
          break;
          localStringBuilder.append(", ");
        }
      }
      paramCollection2.add(localStringBuilder.toString());
    }
  }

  public static void maybeInvokeRetrieval(TextView paramTextView, ParsedResult paramParsedResult, Context paramContext)
  {
    AsyncTaskExecInterface localAsyncTaskExecInterface = (AsyncTaskExecInterface)new AsyncTaskExecManager().build();
    try
    {
      if ((paramParsedResult instanceof URIParsedResult))
      {
        localAsyncTaskExecInterface.execute(new URIResultInfoRetriever(paramTextView, (URIParsedResult)paramParsedResult, paramContext), new Object[0]);
        localAsyncTaskExecInterface.execute(new TitleRetriever(paramTextView, (URIParsedResult)paramParsedResult), new Object[0]);
        return;
      }
      if ((paramParsedResult instanceof ProductParsedResult))
      {
        localAsyncTaskExecInterface.execute(new ProductResultInfoRetriever(paramTextView, ((ProductParsedResult)paramParsedResult).getProductID(), paramContext), new Object[0]);
        return;
      }
      if ((paramParsedResult instanceof ISBNParsedResult))
      {
        paramParsedResult = ((ISBNParsedResult)paramParsedResult).getISBN();
        localAsyncTaskExecInterface.execute(new ProductResultInfoRetriever(paramTextView, paramParsedResult, paramContext), new Object[0]);
        localAsyncTaskExecInterface.execute(new BookResultInfoRetriever(paramTextView, paramParsedResult, paramContext), new Object[0]);
      }
      return;
    }
    catch (java.util.concurrent.RejectedExecutionException paramTextView)
    {
    }
  }

  final void append(String paramString1, String paramString2, String[] paramArrayOfString, String paramString3)
  {
    paramString1 = new StringBuilder();
    if (paramString2 != null)
      paramString1.append(paramString2).append(' ');
    int k = paramString1.length();
    int j = 1;
    int m = paramArrayOfString.length;
    int i = 0;
    if (i < m)
    {
      paramString2 = paramArrayOfString[i];
      if (j != 0)
      {
        paramString1.append(paramString2);
        j = 0;
      }
      while (true)
      {
        i += 1;
        break;
        paramString1.append(" [");
        paramString1.append(paramString2);
        paramString1.append(']');
      }
    }
    i = paramString1.length();
    paramString1 = paramString1.toString();
    paramString2 = new SpannableString(paramString1 + "\n\n");
    if (paramString3 != null)
    {
      if (!paramString3.startsWith("HTTP://"))
        break label207;
      paramString1 = "http" + paramString3.substring(4);
    }
    while (true)
    {
      paramString2.setSpan(new URLSpan(paramString1), k, i, 33);
      this.newContents.add(paramString2);
      return;
      label207: paramString1 = paramString3;
      if (!paramString3.startsWith("HTTPS://"))
        continue;
      paramString1 = "https" + paramString3.substring(5);
    }
  }

  public final Object doInBackground(Object[] paramArrayOfObject)
  {
    try
    {
      retrieveSupplementalInfo();
      return null;
    }
    catch (IOException paramArrayOfObject)
    {
      while (true)
        Log.w("SupplementalInfo", paramArrayOfObject);
    }
  }

  protected final void onPostExecute(Object paramObject)
  {
    paramObject = (TextView)this.textViewRef.get();
    if (paramObject != null)
    {
      Iterator localIterator = this.newContents.iterator();
      while (localIterator.hasNext())
        paramObject.append((CharSequence)localIterator.next());
      paramObject.setMovementMethod(LinkMovementMethod.getInstance());
    }
  }

  abstract void retrieveSupplementalInfo()
    throws IOException;
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.supplement.SupplementalInfoRetriever
 * JD-Core Version:    0.6.0
 */
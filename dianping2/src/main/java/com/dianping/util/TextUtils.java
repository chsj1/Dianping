package com.dianping.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class TextUtils
{
  private static final String TAG = TextUtils.class.getSimpleName();

  public static String dropParameter(String paramString)
  {
    String[] arrayOfString = paramString.split("[?]");
    if (arrayOfString.length > 0)
      paramString = arrayOfString[0];
    return paramString;
  }

  public static String getRealDialPhoneNum(String paramString)
  {
    if (android.text.TextUtils.isEmpty(paramString))
      return "";
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    while (i < paramString.length())
    {
      char c = paramString.charAt(i);
      if (PhoneNumberUtils.isReallyDialable(c))
        localStringBuilder.append(c);
      i += 1;
    }
    return localStringBuilder.toString();
  }

  public static boolean hasDigitNumber(String paramString)
  {
    return (!isEmpty(paramString)) && (paramString.replaceAll("\\d", "").length() != paramString.length());
  }

  public static SpannableStringBuilder highLightShow(Context paramContext, String paramString, int paramInt)
  {
    Object localObject;
    if (isEmpty(paramString))
    {
      localObject = null;
      return localObject;
    }
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    int j = paramString.indexOf("{");
    if (j < 0)
      return new SpannableStringBuilder(paramString);
    localArrayList1.add(Integer.valueOf(j));
    int i = 0;
    while (i == 0)
    {
      j = paramString.indexOf("{", j + 1);
      if (j < 0)
      {
        i = 1;
        continue;
      }
      localArrayList1.add(Integer.valueOf(j));
    }
    j = paramString.indexOf("}");
    localArrayList2.add(Integer.valueOf(j));
    i = 0;
    while (i == 0)
    {
      j = paramString.indexOf("}", j + 1);
      if (j < 0)
      {
        i = 1;
        continue;
      }
      localArrayList2.add(Integer.valueOf(j));
    }
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramString.replace("{", "").replace("}", ""));
    i = 0;
    while (true)
    {
      localObject = localSpannableStringBuilder;
      if (i >= Math.min(localArrayList1.size(), localArrayList2.size()))
        break;
      j = ((Integer)localArrayList1.get(i)).intValue() - i * 2;
      int k = ((Integer)localArrayList2.get(i)).intValue() - i * 2 - 1;
      if ((k < j) || (j < 0) || (k < 0))
        return new SpannableStringBuilder(paramString);
      localSpannableStringBuilder.setSpan(new ForegroundColorSpan(paramContext.getResources().getColor(paramInt)), j, k, 33);
      i += 1;
    }
  }

  public static boolean isEmpty(CharSequence paramCharSequence)
  {
    return (paramCharSequence == null) || (paramCharSequence.length() == 0);
  }

  public static boolean isValidColor(String paramString)
  {
    if (android.text.TextUtils.isEmpty(paramString))
      return false;
    try
    {
      Color.parseColor(paramString);
      return true;
    }
    catch (Exception paramString)
    {
      Log.e(TAG, paramString.getMessage());
    }
    return false;
  }

  public static SpannableStringBuilder jsonArrayParseText(JSONArray paramJSONArray)
  {
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    int i = 0;
    while (i < paramJSONArray.size())
    {
      Object localObject = paramJSONArray.get(i);
      if ((localObject instanceof JSONObject))
        localSpannableStringBuilder.append(jsonObjectParseText((JSONObject)localObject));
      i += 1;
    }
    return localSpannableStringBuilder;
  }

  public static SpannableString jsonObjectParseText(JSONObject paramJSONObject)
  {
    Object localObject1 = paramJSONObject.get("text");
    if ((localObject1 instanceof String))
      localObject1 = localObject1.toString();
    while (true)
    {
      localObject1 = new SpannableString((CharSequence)localObject1);
      Object localObject2 = paramJSONObject.get("textsize");
      if (((localObject2 instanceof Long)) || ((localObject2 instanceof String)));
      try
      {
        if (Integer.parseInt(localObject2.toString()) != 0)
          ((SpannableString)localObject1).setSpan(new AbsoluteSizeSpan(Integer.parseInt(localObject2.toString()), true), 0, ((SpannableString)localObject1).length(), 17);
        label85: localObject2 = paramJSONObject.get("textcolor");
        if (((localObject2 instanceof String)) && (isValidColor((String)localObject2)))
          ((SpannableString)localObject1).setSpan(new ForegroundColorSpan(Color.parseColor((String)localObject2)), 0, ((SpannableString)localObject1).length(), 17);
        localObject2 = paramJSONObject.get("backgroundcolor");
        if (((localObject2 instanceof String)) && (isValidColor((String)localObject2)))
          ((SpannableString)localObject1).setSpan(new BackgroundColorSpan(Color.parseColor((String)localObject2)), 0, ((SpannableString)localObject1).length(), 17);
        localObject2 = paramJSONObject.get("textstyle");
        if (((localObject2 instanceof String)) && (!android.text.TextUtils.isEmpty((String)localObject2)))
        {
          int i = 0;
          if (((String)localObject2).equalsIgnoreCase("Bold"))
            i = 1;
          if (((String)localObject2).equalsIgnoreCase("Italic"))
            i = 2;
          if (((String)localObject2).equalsIgnoreCase("Bold_Italic"))
            i = 3;
          ((SpannableString)localObject1).setSpan(new StyleSpan(i), 0, ((SpannableString)localObject1).length(), 17);
        }
        localObject2 = paramJSONObject.get("strikethrough");
        if (((localObject2 instanceof Boolean)) && (((Boolean)localObject2).booleanValue()))
          ((SpannableString)localObject1).setSpan(new StrikethroughSpan(), 0, ((SpannableString)localObject1).length(), 17);
        localObject2 = paramJSONObject.get("underline");
        if (((localObject2 instanceof Boolean)) && (((Boolean)localObject2).booleanValue()))
          ((SpannableString)localObject1).setSpan(new UnderlineSpan(), 0, ((SpannableString)localObject1).length(), 17);
        paramJSONObject = paramJSONObject.get("hyperlink");
        if (((paramJSONObject instanceof String)) && (!isEmpty((String)paramJSONObject)))
          ((SpannableString)localObject1).setSpan(new UrlSpanWithoutUnderLine((String)paramJSONObject), 0, ((SpannableString)localObject1).length(), 17);
        return localObject1;
        localObject1 = "";
      }
      catch (NumberFormatException localNumberFormatException)
      {
        break label85;
      }
    }
  }

  public static SpannableStringBuilder jsonParseText(String paramString)
  {
    if (android.text.TextUtils.isEmpty(paramString))
      return null;
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    Object localObject = JSONValue.parse(paramString);
    if ((localObject instanceof JSONArray))
      return jsonArrayParseText((JSONArray)localObject);
    if ((localObject instanceof JSONObject))
    {
      localSpannableStringBuilder.append(jsonObjectParseText((JSONObject)localObject));
      return localSpannableStringBuilder;
    }
    localSpannableStringBuilder.append(new SpannableString(paramString));
    return localSpannableStringBuilder;
  }

  public static void setJsonText(String paramString, TextView paramTextView)
  {
    if ((android.text.TextUtils.isEmpty(paramString)) || (paramTextView == null))
      return;
    Object localObject1 = JSONValue.parse(paramString);
    if ((localObject1 != null) && ((localObject1 instanceof JSONObject)))
    {
      localObject1 = (JSONObject)localObject1;
      Object localObject2 = ((JSONObject)localObject1).get("richtextlist");
      if (localObject2 != null)
      {
        if ((localObject2 instanceof JSONObject))
          paramTextView.setText(jsonObjectParseText((JSONObject)localObject2));
        while (true)
        {
          paramString = new GradientDrawable();
          localObject2 = ((JSONObject)localObject1).get("labelcolor");
          if (((localObject2 instanceof String)) && (isValidColor((String)localObject2)))
            paramString.setColor(Color.parseColor((String)localObject2));
          localObject2 = ((JSONObject)localObject1).get("cornerradius");
          if (((localObject2 instanceof Double)) || ((localObject2 instanceof Long)))
            paramString.setCornerRadius(ViewUtils.dip2px(paramTextView.getContext(), Float.parseFloat(localObject2.toString())));
          localObject2 = ((JSONObject)localObject1).get("bordercolor");
          localObject1 = ((JSONObject)localObject1).get("borderwidth");
          if (((localObject2 instanceof String)) && (isValidColor((String)localObject2)) && (((localObject1 instanceof Double)) || ((localObject1 instanceof Long))))
            paramString.setStroke(ViewUtils.dip2px(paramTextView.getContext(), Float.parseFloat(localObject1.toString())), Color.parseColor((String)localObject2));
          paramTextView.setBackgroundDrawable(paramString);
          return;
          paramTextView.setText(jsonArrayParseText((JSONArray)localObject2));
        }
      }
      paramTextView.setText(jsonParseText(paramString));
      return;
    }
    paramTextView.setText(jsonParseText(paramString));
  }

  public static float stringParseColor(String paramString)
  {
    if (android.text.TextUtils.isEmpty(paramString))
      return (0.0F / 0.0F);
    try
    {
      int i = Color.parseColor(paramString);
      return i;
    }
    catch (Exception paramString)
    {
      Log.e(TAG, paramString.getMessage());
    }
    return (0.0F / 0.0F);
  }

  public static String stripHeadAndTailQuotations(String paramString)
  {
    String str;
    if (isEmpty(paramString))
      str = "";
    do
    {
      do
      {
        do
        {
          return str;
          str = paramString;
        }
        while (!paramString.startsWith("\""));
        str = paramString;
      }
      while (!paramString.endsWith("\""));
      str = paramString;
    }
    while (paramString.length() <= 2);
    return paramString.substring(1, paramString.length() - 1);
  }

  static class UrlSpanWithoutUnderLine extends URLSpan
  {
    public UrlSpanWithoutUnderLine(String paramString)
    {
      super();
    }

    public void updateDrawState(TextPaint paramTextPaint)
    {
      paramTextPaint.setColor(Color.argb(255, 51, 136, 187));
      paramTextPaint.setUnderlineText(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.TextUtils
 * JD-Core Version:    0.6.0
 */
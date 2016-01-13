package com.google.zxing.client.android.result;

import android.app.Activity;
import com.google.zxing.client.android.R.string;
import com.google.zxing.client.result.GeoParsedResult;
import com.google.zxing.client.result.ParsedResult;

public final class GeoResultHandler extends ResultHandler
{
  private static final int[] buttons = { R.string.button_show_map, R.string.button_get_directions };

  public GeoResultHandler(Activity paramActivity, ParsedResult paramParsedResult)
  {
    super(paramActivity, paramParsedResult);
  }

  public int getButtonCount()
  {
    return buttons.length;
  }

  public int getButtonText(int paramInt)
  {
    return buttons[paramInt];
  }

  public int getDisplayTitle()
  {
    return R.string.result_geo;
  }

  public void handleButtonPress(int paramInt)
  {
    GeoParsedResult localGeoParsedResult = (GeoParsedResult)getResult();
    switch (paramInt)
    {
    default:
      return;
    case 0:
      openMap(localGeoParsedResult.getGeoURI());
      return;
    case 1:
    }
    getDirections(localGeoParsedResult.getLatitude(), localGeoParsedResult.getLongitude());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.GeoResultHandler
 * JD-Core Version:    0.6.0
 */
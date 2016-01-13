package com.dianping.main.home;

import android.text.TextUtils;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DecodingFactory;
import com.dianping.archive.Unarchiver;
import com.dianping.model.GuessLikeItem;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeGuessLikeItem extends GuessLikeItem
{
  public static final DecodingFactory<HomeGuessLikeItem> DECODER = new DecodingFactory()
  {
    public HomeGuessLikeItem[] createArray(int paramInt)
    {
      return new HomeGuessLikeItem[paramInt];
    }

    public HomeGuessLikeItem createInstance(int paramInt)
    {
      if (paramInt == 61484)
        return new HomeGuessLikeItem();
      return null;
    }
  };
  public JSONObject extContentJson;

  public void decode(Unarchiver paramUnarchiver)
    throws ArchiveException
  {
    super.decode(paramUnarchiver);
    if ((this.hasExtension) && (!TextUtils.isEmpty(this.extContent)))
      try
      {
        this.extContentJson = new JSONObject(this.extContent);
        return;
      }
      catch (JSONException paramUnarchiver)
      {
        this.hasExtension = false;
        paramUnarchiver.printStackTrace();
        return;
      }
    this.hasExtension = false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeGuessLikeItem
 * JD-Core Version:    0.6.0
 */
package com.dianping.search.shoplist.data.model;

import android.text.TextUtils;
import com.dianping.archive.DPObject;
import org.json.JSONObject;

public class SearchDirectZoneModel
{
  public static final int DISPLAY_TYPE_BIG_IMG = 3;
  public static final int DISPLAY_TYPE_MOVIES = 7;
  public static final int DISPLAY_TYPE_MOVIE_MORE = 8;
  public static final int DISPLAY_TYPE_NO_BTN = 2;
  public static final int DISPLAY_TYPE_ONE_MOVIE = 6;
  public static final int DISPLAY_TYPE_TEXT_LINK = 4;
  public static final int DISPLAY_TYPE_THREE_BTN = 1;
  public static final int DISPLAY_TYPE_THREE_IMG = 0;
  public String clickUrl;
  public DisplayContent[] displayContents;
  public int displayType;
  public String id;
  public int index;
  public String keyword;
  public DirectZoneItemModel leftBottomModel;
  public DirectZoneItemModel leftTopModel;
  public String mFeedback;
  public DirectZoneItemModel[] mLeftItemContents;
  public String mPrice;
  public DirectZoneItemModel[] mRightItemContents;
  public String mScore;
  public String picLabel;
  public String picUrl;
  public String property;
  public String queryId;
  public DirectZoneItemModel rightBottomModel;
  public DirectZoneItemModel rightTopModel;
  public String title;

  public static SearchDirectZoneModel fromDPObject(DPObject paramDPObject, int paramInt, String paramString1, String paramString2)
  {
    SearchDirectZoneModel localSearchDirectZoneModel = new SearchDirectZoneModel();
    localSearchDirectZoneModel.leftTopModel = DirectZoneItemModel.fromDPObject(paramDPObject.getObject("LeftTopItem"));
    localSearchDirectZoneModel.rightTopModel = DirectZoneItemModel.fromDPObject(paramDPObject.getObject("RightTopItem"));
    localSearchDirectZoneModel.leftBottomModel = DirectZoneItemModel.fromDPObject(paramDPObject.getObject("LeftBottomItem"));
    localSearchDirectZoneModel.rightBottomModel = DirectZoneItemModel.fromDPObject(paramDPObject.getObject("RightBottomItem"));
    localSearchDirectZoneModel.displayType = paramDPObject.getInt("DisplayType");
    localSearchDirectZoneModel.clickUrl = paramDPObject.getString("ClickUrl");
    localSearchDirectZoneModel.picLabel = paramDPObject.getString("PicLabel");
    localSearchDirectZoneModel.property = paramDPObject.getString("Property");
    localSearchDirectZoneModel.picUrl = paramDPObject.getString("PicUrl");
    localSearchDirectZoneModel.title = paramDPObject.getString("Title");
    localSearchDirectZoneModel.id = paramDPObject.getString("ID");
    DPObject[] arrayOfDPObject = paramDPObject.getArray("DisplayContent");
    if (arrayOfDPObject != null)
    {
      localSearchDirectZoneModel.displayContents = new DisplayContent[arrayOfDPObject.length];
      int i = 0;
      while (i < arrayOfDPObject.length)
      {
        localSearchDirectZoneModel.displayContents[i] = DisplayContent.fromDPObject(arrayOfDPObject[i]);
        i += 1;
      }
    }
    localSearchDirectZoneModel.displayContents = new DisplayContent[0];
    localSearchDirectZoneModel.index = paramInt;
    localSearchDirectZoneModel.keyword = paramString1;
    localSearchDirectZoneModel.queryId = paramString2;
    if (((localSearchDirectZoneModel.displayType == 6) || (localSearchDirectZoneModel.displayType == 7)) && (localSearchDirectZoneModel.displayType == 6))
    {
      paramString1 = paramDPObject.getArray("RightItemList");
      if (paramString1 != null)
      {
        localSearchDirectZoneModel.mRightItemContents = new DirectZoneItemModel[paramString1.length];
        paramInt = 0;
        while (paramInt < paramString1.length)
        {
          localSearchDirectZoneModel.mRightItemContents[paramInt] = DirectZoneItemModel.fromDPObject(paramString1[paramInt]);
          paramInt += 1;
        }
      }
      localSearchDirectZoneModel.mRightItemContents = new DirectZoneItemModel[0];
      paramString1 = paramDPObject.getArray("LeftItemList");
      if (paramString1 != null)
      {
        localSearchDirectZoneModel.mLeftItemContents = new DirectZoneItemModel[paramString1.length];
        paramInt = 0;
        while (paramInt < paramString1.length)
        {
          localSearchDirectZoneModel.mLeftItemContents[paramInt] = DirectZoneItemModel.fromDPObject(paramString1[paramInt]);
          paramInt += 1;
        }
      }
      localSearchDirectZoneModel.mLeftItemContents = new DirectZoneItemModel[0];
    }
    paramDPObject = paramDPObject.getString("ExtraInfo");
    if (!TextUtils.isEmpty(paramDPObject));
    try
    {
      paramDPObject = new JSONObject(paramDPObject);
      localSearchDirectZoneModel.mScore = paramDPObject.optString("Score", "");
      localSearchDirectZoneModel.mPrice = paramDPObject.optString("Price", "");
      localSearchDirectZoneModel.mFeedback = paramDPObject.optString("Feedback", "");
      return localSearchDirectZoneModel;
    }
    catch (Exception paramDPObject)
    {
      paramDPObject.printStackTrace();
    }
    return localSearchDirectZoneModel;
  }

  public static SearchDirectZoneModel getMorePlaceHolder(SearchDirectZoneModel paramSearchDirectZoneModel)
  {
    SearchDirectZoneModel localSearchDirectZoneModel = new SearchDirectZoneModel();
    localSearchDirectZoneModel.id = "moreDirectZone";
    localSearchDirectZoneModel.picLabel = paramSearchDirectZoneModel.picLabel;
    localSearchDirectZoneModel.displayType = paramSearchDirectZoneModel.displayType;
    return localSearchDirectZoneModel;
  }

  public int getMovieLabelType()
  {
    switch (this.displayType)
    {
    default:
    case 6:
    case 7:
    }
    while (true)
    {
      return 0;
      if (TextUtils.isEmpty(this.picLabel))
        continue;
      try
      {
        int i = Integer.parseInt(this.picLabel);
        return i;
        if (TextUtils.isEmpty(this.picLabel))
          continue;
        try
        {
          i = Integer.parseInt(this.picLabel);
          return i;
        }
        catch (NumberFormatException localNumberFormatException1)
        {
          return 0;
        }
      }
      catch (NumberFormatException localNumberFormatException2)
      {
      }
    }
    return 0;
  }

  public boolean isMorePlaceHolder()
  {
    return "moreDirectZone".equals(this.id);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.data.model.SearchDirectZoneModel
 * JD-Core Version:    0.6.0
 */
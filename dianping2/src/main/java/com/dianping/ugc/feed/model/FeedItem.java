package com.dianping.ugc.feed.model;

import android.content.res.Resources;
import android.text.Html;
import android.text.Spanned;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.color;
import com.dianping.v1.R.string;
import java.util.ArrayList;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class FeedItem
{
  public static final int REVIEW_TYPE_FRIENDS = 2;
  public static final int REVIEW_TYPE_OTHERS = 0;
  public static final int REVIEW_TYPE_OWNER = 1;
  public static final String TAG = "FeedItem";
  public static final int TYPE_DETAIL = 1;
  public static final int TYPE_TITLE = 0;
  public final String ID = UUID.randomUUID().toString();
  public String avgPrice;
  public int belongType;
  public Spanned cachedLikeUsers;
  public int commentCount;
  public ArrayList<FeedComment> commentsList = new ArrayList();
  public Spanned content;
  public String createdAt;
  public String detailUrl;
  public String editlUrl;
  public String feedId;
  public String feedSource;
  public String feedSourceUrl;
  public int feedType;
  public FeedUser feedUser;
  public int friendCount;
  public boolean hasOwnLike;
  private String[] highLightKeyword;
  public String honourUrl;
  public boolean isCommentListExpanded = false;
  public boolean isContentExpanded = false;
  public boolean isOriginalContent = true;
  public int likeCount;
  public ArrayList<FeedUser> likeUsersList = new ArrayList();
  public String[] photos;
  public String poiDistance;
  public String poiJumpUrl;
  public String poiName;
  public String poiPic;
  public String poiPrice;
  public String poiRegion;
  public Spanned recommends;
  public int reviewCount;
  public int reviewType;
  public String scoreText;
  public int shopId;
  public int shopPower;
  public String[] thumbnailsPhotos;
  public String title;
  public Spanned translatedContent;
  public int type;

  public FeedItem()
  {
  }

  public FeedItem(DPObject paramDPObject)
  {
    if (paramDPObject.isClass("Title"))
    {
      this.type = 0;
      this.title = paramDPObject.getString("Title");
      return;
    }
    this.type = 1;
    Object localObject = paramDPObject.getObject("FeedUser");
    if (localObject != null)
      this.feedUser = new FeedUser((DPObject)localObject);
    while (true)
    {
      this.feedId = paramDPObject.getString("MainId");
      this.feedType = paramDPObject.getInt("FeedType");
      this.reviewCount = paramDPObject.getInt("ReviewCount");
      this.friendCount = paramDPObject.getInt("FriendCount");
      this.honourUrl = paramDPObject.getString("Honour");
      this.shopPower = paramDPObject.getInt("Star");
      this.avgPrice = paramDPObject.getString("Price");
      this.feedSource = paramDPObject.getString("SourceName");
      this.createdAt = paramDPObject.getString("Time");
      this.scoreText = paramDPObject.getString("ScoreText");
      this.highLightKeyword = paramDPObject.getStringArray("AbstractList");
      this.detailUrl = paramDPObject.getString("DetailUrl");
      this.editlUrl = paramDPObject.getString("EditUrl");
      if (paramDPObject.getString("Content") == null)
        localObject = "";
      try
      {
        str = ((String)localObject).replace("\r\n", "<br>").replace("\n", "<br>");
        localObject = str;
        this.content = Html.fromHtml((String)localObject);
        if (paramDPObject.getString("TranslateContent") == null)
          localObject = "";
      }
      catch (Exception arrayOfString)
      {
        try
        {
          while (true)
          {
            String str = ((String)localObject).replace("\r\n", "<br>").replace("\n", "<br>");
            localObject = str;
            this.translatedContent = Html.fromHtml((String)localObject);
            this.recommends = buildRecommends(paramDPObject.getStringArray("Recommends"));
            localObject = paramDPObject.getArray("Pictures");
            if ((localObject == null) || (localObject.length <= 0))
              break label458;
            this.thumbnailsPhotos = new String[localObject.length];
            this.photos = new String[localObject.length];
            i = 0;
            while (i < localObject.length)
            {
              this.thumbnailsPhotos[i] = localObject[i].getString("SmallUrl");
              this.photos[i] = localObject[i].getString("BigUrl");
              i += 1;
            }
            this.feedUser = new FeedUser();
            break;
            localObject = paramDPObject.getString("Content");
            continue;
            localException1 = localException1;
            localException1.printStackTrace();
            continue;
            localObject = paramDPObject.getString("TranslateContent");
          }
        }
        catch (Exception arrayOfString)
        {
          while (true)
            localException2.printStackTrace();
          label458: localObject = paramDPObject.getObject("FeedPoi");
          String[] arrayOfString;
          if (localObject != null)
          {
            this.poiJumpUrl = ((DPObject)localObject).getString("JumpUrl");
            this.poiDistance = ((DPObject)localObject).getString("Distance");
            arrayOfString = ((DPObject)localObject).getStringArray("Region");
            if (arrayOfString != null)
            {
              StringBuilder localStringBuilder = new StringBuilder();
              i = 0;
              while (i < arrayOfString.length)
              {
                localStringBuilder.append(arrayOfString[i]).append(" ");
                i += 1;
              }
              this.poiRegion = localStringBuilder.toString();
            }
            this.poiPic = ((DPObject)localObject).getString("Picture");
            this.poiPrice = ((DPObject)localObject).getString("Price");
            this.poiName = ((DPObject)localObject).getString("Name");
          }
          this.likeCount = paramDPObject.getInt("LikeCount");
          this.hasOwnLike = paramDPObject.getBoolean("IsLike");
          localObject = paramDPObject.getArray("LikeUsers");
          int j;
          if ((localObject != null) && (localObject.length > 0))
          {
            j = localObject.length;
            i = 0;
            while (i < j)
            {
              arrayOfString = localObject[i];
              this.likeUsersList.add(new FeedUser(arrayOfString));
              i += 1;
            }
          }
          this.commentCount = paramDPObject.getInt("CommentCount");
          localObject = paramDPObject.getArray("Comments");
          if ((localObject != null) && (localObject.length > 0))
          {
            j = localObject.length;
            i = 0;
            while (i < j)
            {
              arrayOfString = localObject[i];
              this.commentsList.add(new FeedComment(arrayOfString));
              i += 1;
            }
          }
          if (!paramDPObject.contains("BelongType"))
            break label778;
        }
      }
    }
    int i = paramDPObject.getInt("BelongType");
    while (true)
    {
      this.belongType = i;
      this.reviewType = paramDPObject.getInt("ReviewType");
      return;
      label778: if (this.feedUser.userid.equals(String.valueOf(DPApplication.instance().accountService().id())))
      {
        i = 1;
        continue;
      }
      i = 0;
    }
  }

  public static Spanned buildRecommends(String[] paramArrayOfString)
  {
    Object localObject2 = null;
    Object localObject1 = localObject2;
    StringBuilder localStringBuilder;
    if (paramArrayOfString != null)
    {
      localObject1 = localObject2;
      if (paramArrayOfString.length > 0)
      {
        localStringBuilder = new StringBuilder(getResString(R.string.ugc_review_recommend_dishes) + "：");
        int j = paramArrayOfString.length;
        int i = 0;
        while (i < j)
        {
          localObject1 = paramArrayOfString[i];
          if (!android.text.TextUtils.isEmpty(((String)localObject1).trim()))
            localStringBuilder.append((String)localObject1).append(" ");
          i += 1;
        }
        localObject1 = localObject2;
        if (!android.text.TextUtils.isEmpty(localStringBuilder.toString().trim()))
        {
          localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
          paramArrayOfString = new JSONObject();
          localObject1 = new JSONObject();
        }
      }
    }
    try
    {
      paramArrayOfString.put("text", "  \n");
      paramArrayOfString.put("textsize", "5");
      ((JSONObject)localObject1).put("text", localStringBuilder);
      ((JSONObject)localObject1).put("textcolor", getResColor(R.color.text_color_light_gray));
      ((JSONObject)localObject1).put("textsize", "14");
      localObject1 = (Spanned)android.text.TextUtils.concat(new CharSequence[] { com.dianping.util.TextUtils.jsonParseText(paramArrayOfString.toString()), com.dianping.util.TextUtils.jsonParseText(((JSONObject)localObject1).toString()) });
      return localObject1;
    }
    catch (JSONException localJSONException)
    {
      while (true)
        localJSONException.printStackTrace();
    }
  }

  public static String getResColor(int paramInt)
  {
    return String.format("#%06X", new Object[] { Integer.valueOf(0xFFFFFF & DPApplication.instance().getResources().getColor(paramInt)) });
  }

  public static String getResString(int paramInt)
  {
    return DPApplication.instance().getResources().getString(paramInt);
  }

  public Spanned getContentWithRecommends()
  {
    if (this.recommends == null)
      return this.content;
    return (Spanned)android.text.TextUtils.concat(new CharSequence[] { this.content, Html.fromHtml("<br>"), this.recommends });
  }

  public boolean hasMoreFriendReviews()
  {
    return this.friendCount > 0;
  }

  public boolean hasMoreReviews()
  {
    return this.reviewCount > 1;
  }

  public void setContentExpanded(boolean paramBoolean)
  {
    this.isContentExpanded = paramBoolean;
  }

  public void setKeyword(String paramString)
  {
    String[] arrayOfString1;
    String[] arrayOfString2;
    int k;
    int m;
    int i;
    if (this.highLightKeyword == null)
    {
      arrayOfString1 = new String[0];
      arrayOfString2 = arrayOfString1;
      if (paramString != null)
      {
        k = 0;
        m = arrayOfString1.length;
        i = 0;
      }
    }
    while (true)
    {
      int j = k;
      if (i < m)
      {
        if (arrayOfString1[i].contains(paramString))
          j = 1;
      }
      else
      {
        arrayOfString2 = arrayOfString1;
        if (j == 0)
        {
          arrayOfString2 = new String[arrayOfString1.length + 1];
          System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, arrayOfString1.length);
          arrayOfString2[(arrayOfString2.length - 1)] = paramString;
        }
        paramString = this.content.toString();
        if ((arrayOfString2 == null) || (arrayOfString2.length <= 0))
          return;
        j = arrayOfString2.length;
        i = 0;
        while (i < j)
        {
          arrayOfString1 = arrayOfString2[i];
          paramString = paramString.replace(arrayOfString1, "<font color=" + getResColor(R.color.light_red) + ">" + arrayOfString1 + "</font>");
          i += 1;
        }
        arrayOfString1 = this.highLightKeyword;
        break;
      }
      i += 1;
    }
    this.content = Html.fromHtml(paramString);
  }

  public void setShopId(int paramInt)
  {
    this.shopId = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.feed.model.FeedItem
 * JD-Core Version:    0.6.0
 */
package com.dianping.hotel.ugc;

import android.content.res.Resources;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

public class ReviewItem
{
  public static final int REVIEW_TYPE_FRIENDS = 2;
  public static final int REVIEW_TYPE_OTHERS = 0;
  public static final int REVIEW_TYPE_OWNER = 1;
  public static final int SOURCE_DP = 0;
  public static final int SOURCE_THIRD_PARTY = 1;
  public static final int TYPE_DETAIL = 1;
  public static final int TYPE_TITLE = 0;
  public final String ID = UUID.randomUUID().toString();
  public int approveCount;
  public COMMENT_STATE approveState = COMMENT_STATE.COMMENT_IDLE;
  public String approveStr;
  private ArrayList<FavorUser> approveUsers = new ArrayList();
  public String avgPrice;
  public int belongType;
  public int commentCount;
  public COMMENT_STATE commentState = COMMENT_STATE.COMMENT_IDLE;
  public ArrayList<ReviewComment> comments = new ArrayList();
  public Spanned content;
  public String createdAt;
  public int friendCount;
  public boolean hasOwnApprove;
  private String[] highLightKeyword;
  public String honourUrl;
  public boolean isApproveExpanded = false;
  public boolean isCommentExpanded = false;
  public boolean isContentExpanded = false;
  public boolean isOriginalContent = true;
  private String keyword;
  public String[] photos;
  public int reviewCount;
  public String reviewId;
  public int reviewType;
  public String[] scores = new String[3];
  public int shopId;
  public String shopName;
  public int shopPower;
  public String sourceName;
  public int sourceType;
  public String sourceUrl;
  public String[] thumbnailsPhotos;
  public String title;
  public Spanned translatedContent;
  public int type;
  public int userId;
  public String[] userLabels;
  public String username;

  public ReviewItem()
  {
  }

  public ReviewItem(DPObject paramDPObject)
  {
    if (paramDPObject.isClass("Title"))
    {
      this.type = 0;
      this.title = paramDPObject.getString("Title");
      return;
    }
    this.type = 1;
    Object localObject = paramDPObject.getObject("User");
    if (localObject != null)
    {
      this.username = ((DPObject)localObject).getString("NickName");
      this.userId = ((DPObject)localObject).getInt("UserID");
      this.userLabels = ((DPObject)localObject).getStringArray("UserLabel");
    }
    this.reviewCount = paramDPObject.getInt("ReviewCount");
    this.friendCount = paramDPObject.getInt("FriendCount");
    this.reviewId = String.valueOf(paramDPObject.getInt("ID"));
    this.sourceType = paramDPObject.getInt("SourceType");
    if (this.sourceType == 1)
    {
      localObject = paramDPObject.getString("SourceName");
      this.sourceName = ((String)localObject);
      this.sourceUrl = paramDPObject.getString("SourceUrl");
      this.honourUrl = paramDPObject.getString("ReviewHonour");
      this.shopId = paramDPObject.getInt("ShopID");
      if (paramDPObject.getString("ShopName") != null)
        break label404;
      localObject = "default";
      label254: this.shopName = ((String)localObject);
      this.shopPower = paramDPObject.getInt("Star");
      this.avgPrice = paramDPObject.getString("PriceText");
      setCreatedAt(paramDPObject);
      this.highLightKeyword = paramDPObject.getStringArray("AbstractList");
      this.content = Html.fromHtml(paramDPObject.getString("ReviewBody"));
      if (paramDPObject.getString("TransReviewBody") != null)
        break label414;
    }
    label404: label414: for (localObject = ""; ; localObject = paramDPObject.getString("TransReviewBody"))
    {
      this.translatedContent = Html.fromHtml((String)localObject);
      this.thumbnailsPhotos = paramDPObject.getStringArray("Thumbnails");
      this.photos = paramDPObject.getStringArray("Images");
      this.approveCount = paramDPObject.getInt("LikesNum");
      this.hasOwnApprove = paramDPObject.getBoolean("IsLiked");
      this.commentCount = paramDPObject.getInt("CommentCount");
      this.belongType = paramDPObject.getInt("BelongType");
      this.reviewType = paramDPObject.getInt("Type");
      return;
      localObject = "";
      break;
      localObject = paramDPObject.getString("ShopName");
      break label254;
    }
  }

  private static String getDateYear(Date paramDate)
  {
    return new SimpleDateFormat("yy", Locale.getDefault()).format(paramDate);
  }

  private static Spanned highlightContent(String paramString, String[] paramArrayOfString)
  {
    int j = paramArrayOfString.length;
    int i = 0;
    while (i < j)
    {
      String str = paramArrayOfString[i];
      paramString = paramString.replace(str, "<font color=" + DPApplication.instance().getResources().getColor(R.color.light_red) + ">" + str + "</font>");
      i += 1;
    }
    return Html.fromHtml(paramString);
  }

  private void setCreatedAt(DPObject paramDPObject)
  {
    paramDPObject = new Date(paramDPObject.getTime("Time"));
    if (getDateYear(paramDPObject).equalsIgnoreCase(getDateYear(new Date())))
    {
      if (paramDPObject == null);
      for (paramDPObject = ""; ; paramDPObject = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(paramDPObject))
      {
        this.createdAt = paramDPObject;
        return;
      }
    }
    if (paramDPObject == null);
    for (paramDPObject = ""; ; paramDPObject = new SimpleDateFormat("yy-MM-dd", Locale.getDefault()).format(paramDPObject))
    {
      this.createdAt = paramDPObject;
      return;
    }
  }

  public void addApprove(String paramString)
  {
    this.approveCount += 1;
    FavorUser localFavorUser = new FavorUser();
    localFavorUser.username = paramString;
    this.approveUsers.add(0, localFavorUser);
    if (TextUtils.isEmpty(this.approveStr))
    {
      this.approveStr = paramString;
      return;
    }
    this.approveStr = (paramString + ", " + this.approveStr);
  }

  public void addComment(ReviewComment paramReviewComment)
  {
    this.commentCount += 1;
    this.comments.add(paramReviewComment);
  }

  public boolean hasApprove()
  {
    return this.approveCount != 0;
  }

  public boolean hasApproveContent()
  {
    return !TextUtils.isEmpty(this.approveStr);
  }

  public boolean hasCommentContent()
  {
    return this.comments.size() > 0;
  }

  public boolean hasComments()
  {
    return this.commentCount != 0;
  }

  public boolean hasMoreFriendReviews()
  {
    return this.friendCount > 0;
  }

  public boolean hasMoreReviews()
  {
    return this.reviewCount > 1;
  }

  public void setApprove(ArrayList<FavorUser> paramArrayList)
  {
    this.approveCount = paramArrayList.size();
    this.approveUsers.clear();
    this.approveUsers.addAll(paramArrayList);
    paramArrayList = new StringBuilder();
    Iterator localIterator = this.approveUsers.iterator();
    while (localIterator.hasNext())
      paramArrayList.append(((FavorUser)localIterator.next()).username).append(", ");
    if (paramArrayList.length() > 0)
      paramArrayList.delete(paramArrayList.length() - 2, paramArrayList.length() - 1);
    this.approveStr = paramArrayList.toString();
  }

  public void setComment(ArrayList<ReviewComment> paramArrayList)
  {
    this.commentCount = paramArrayList.size();
    this.comments.clear();
    this.comments.addAll(paramArrayList);
  }

  public void setKeyword(String paramString)
  {
    this.keyword = paramString;
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
        this.content = highlightContent(this.content.toString(), arrayOfString2);
        return;
        arrayOfString1 = this.highLightKeyword;
        break;
      }
      i += 1;
    }
  }

  public static enum COMMENT_STATE
  {
    static
    {
      COMMENT_ERROR = new COMMENT_STATE("COMMENT_ERROR", 1);
      COMMENT_IDLE = new COMMENT_STATE("COMMENT_IDLE", 2);
      $VALUES = new COMMENT_STATE[] { COMMENT_LOADING, COMMENT_ERROR, COMMENT_IDLE };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.ugc.ReviewItem
 * JD-Core Version:    0.6.0
 */
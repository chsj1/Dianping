package com.dianping.hotel.ugc;

import android.content.res.Resources;
import android.text.Spanned;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.string;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReviewComment
{
  public static final String TYPE_COMMENT_TO_COMMENT = "2";
  public static final String TYPE_COMMENT_TO_REVIEW = "1";
  public String commentId;
  private Spanned commentStr;
  public String commentUsername;
  public String content;
  private boolean isShopComment;
  public String type;

  public ReviewComment()
  {
  }

  public ReviewComment(DPObject paramDPObject)
  {
    this.commentId = String.valueOf(paramDPObject.getInt("CommentId"));
    this.type = String.valueOf(paramDPObject.getInt("NoteType"));
    this.isShopComment = paramDPObject.getBoolean("IsShopReply");
    this.content = paramDPObject.getString("Content");
    paramDPObject = paramDPObject.getObject("User");
    if (paramDPObject != null)
      this.commentUsername = paramDPObject.getString("Nick");
  }

  public Spanned getComment()
  {
    if (this.commentStr == null);
    try
    {
      JSONArray localJSONArray = new JSONArray();
      localObject2 = String.format("#%06X", new Object[] { Integer.valueOf(0xFFFFFF & DPApplication.instance().getResources().getColor(R.color.orange)) });
      String str = String.format("#%06X", new Object[] { Integer.valueOf(0xFFFFFF & DPApplication.instance().getResources().getColor(R.color.deep_gray)) });
      Object localObject1 = String.format("#%06X", new Object[] { Integer.valueOf(0xFFFFFF & DPApplication.instance().getResources().getColor(R.color.text_color_light_gray)) });
      JSONObject localJSONObject;
      if ("2".equals(this.type))
      {
        localJSONObject = new JSONObject();
        localJSONObject.put("text", this.commentUsername);
        if (this.isShopComment)
        {
          localJSONObject.put("textcolor", localObject2);
          localJSONArray.put(localJSONObject);
          localObject2 = new JSONObject();
          ((JSONObject)localObject2).put("text", DPApplication.instance().getResources().getString(R.string.ugc_review_comment_tip));
          ((JSONObject)localObject2).put("textcolor", str);
          localJSONArray.put(localObject2);
          localObject2 = new JSONArray(this.content);
          if (((JSONArray)localObject2).length() > 1)
          {
            localJSONObject = new JSONObject();
            localJSONObject.put("text", ((String)((JSONObject)((JSONArray)localObject2).get(0)).get("text")).replace("回复", ""));
            localJSONObject.put("textcolor", localObject1);
            localJSONArray.put(localJSONObject);
            localObject1 = new JSONObject();
            ((JSONObject)localObject1).put("text", (String)((JSONObject)((JSONArray)localObject2).get(1)).get("text"));
            ((JSONObject)localObject1).put("textcolor", str);
            localJSONArray.put(localObject1);
          }
        }
      }
      while (true)
      {
        this.commentStr = TextUtils.jsonParseText(localJSONArray.toString());
        return this.commentStr;
        localObject2 = localObject1;
        break;
        if (!"1".equals(this.type))
          continue;
        localJSONObject = new JSONObject();
        localJSONObject.put("text", this.commentUsername);
        if (!this.isShopComment)
          break label482;
        localJSONObject.put("textcolor", localObject2);
        localJSONArray.put(localJSONObject);
        localObject2 = new JSONObject();
        ((JSONObject)localObject2).put("text", "：");
        ((JSONObject)localObject2).put("textcolor", localObject1);
        localJSONArray.put(localObject2);
        localObject1 = new JSONObject();
        ((JSONObject)localObject1).put("text", this.content);
        ((JSONObject)localObject1).put("textcolor", str);
        localJSONArray.put(localObject1);
      }
    }
    catch (JSONException localJSONException)
    {
      while (true)
      {
        localJSONException.printStackTrace();
        continue;
        label482: Object localObject2 = localJSONException;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.ugc.ReviewComment
 * JD-Core Version:    0.6.0
 */
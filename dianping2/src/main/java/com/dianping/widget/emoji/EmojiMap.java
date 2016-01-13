package com.dianping.widget.emoji;

import com.dianping.nova.R.drawable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class EmojiMap
{
  private static final HashMap<String, String> EMOJIHASHMAP = new HashMap();
  private static final ArrayList<Emoji> EMOJILIST;
  private static final HashMap<String, Emoji> STRINGEMOJIHASHMAP = new HashMap();
  private static boolean isScaned;

  static
  {
    EMOJILIST = new ArrayList();
    EMOJIHASHMAP.put("emoji000", "[馋嘴]");
    EMOJIHASHMAP.put("emoji001", "[哈哈]");
    EMOJIHASHMAP.put("emoji002", "[微笑]");
    EMOJIHASHMAP.put("emoji003", "[嘻嘻]");
    EMOJIHASHMAP.put("emoji004", "[可爱]");
    EMOJIHASHMAP.put("emoji005", "[眨眼]");
    EMOJIHASHMAP.put("emoji006", "[花心]");
    EMOJIHASHMAP.put("emoji007", "[爱你]");
    EMOJIHASHMAP.put("emoji008", "[亲吻]");
    EMOJIHASHMAP.put("emoji009", "[发呆]");
    EMOJIHASHMAP.put("emoji010", "[闭眼]");
    EMOJIHASHMAP.put("emoji011", "[呵呵]");
    EMOJIHASHMAP.put("emoji012", "[鬼脸]");
    EMOJIHASHMAP.put("emoji013", "[吐舌]");
    EMOJIHASHMAP.put("emoji014", "[鄙视]");
    EMOJIHASHMAP.put("emoji015", "[阴险]");
    EMOJIHASHMAP.put("emoji016", "[汗]");
    EMOJIHASHMAP.put("emoji017", "[失望]");
    EMOJIHASHMAP.put("emoji018", "[无奈]");
    EMOJIHASHMAP.put("emoji019", "[皱眉]");
    EMOJIHASHMAP.put("emoji020", "[悲伤]");
    EMOJIHASHMAP.put("emoji021", "[泪]");
    EMOJIHASHMAP.put("emoji022", "[大哭]");
    EMOJIHASHMAP.put("emoji023", "[恐怖]");
    EMOJIHASHMAP.put("emoji024", "[生气]");
    EMOJIHASHMAP.put("emoji025", "[怒]");
    EMOJIHASHMAP.put("emoji026", "[睡觉]");
    EMOJIHASHMAP.put("emoji027", "[生病]");
    EMOJIHASHMAP.put("emoji101", "[good]");
    EMOJIHASHMAP.put("emoji102", "[弱]");
    EMOJIHASHMAP.put("emoji103", "[ok]");
    EMOJIHASHMAP.put("emoji104", "[耶]");
    EMOJIHASHMAP.put("emoji105", "[鼓掌]");
    EMOJIHASHMAP.put("emoji106", "[心]");
    EMOJIHASHMAP.put("emoji107", "[心碎]");
    EMOJIHASHMAP.put("emoji108", "[闪耀]");
    EMOJIHASHMAP.put("emoji109", "[困]");
    EMOJIHASHMAP.put("emoji110", "[亲嘴]");
    EMOJIHASHMAP.put("emoji201", "[汉堡]");
    EMOJIHASHMAP.put("emoji202", "[薯条]");
    EMOJIHASHMAP.put("emoji203", "[便当]");
    EMOJIHASHMAP.put("emoji204", "[寿司]");
    EMOJIHASHMAP.put("emoji205", "[饭团]");
    EMOJIHASHMAP.put("emoji206", "[饭]");
    EMOJIHASHMAP.put("emoji207", "[拉面]");
    EMOJIHASHMAP.put("emoji208", "[面包]");
    EMOJIHASHMAP.put("emoji209", "[蛋筒]");
    EMOJIHASHMAP.put("emoji210", "[沙冰]");
    EMOJIHASHMAP.put("emoji211", "[蛋糕]");
    EMOJIHASHMAP.put("emoji212", "[甜点]");
    EMOJIHASHMAP.put("emoji213", "[苹果]");
    EMOJIHASHMAP.put("emoji214", "[橙子]");
    EMOJIHASHMAP.put("emoji215", "[西瓜]");
    EMOJIHASHMAP.put("emoji216", "[草莓]");
    EMOJIHASHMAP.put("emoji217", "[咖啡]");
    EMOJIHASHMAP.put("emoji218", "[茶]");
    EMOJIHASHMAP.put("emoji219", "[啤酒]");
    EMOJIHASHMAP.put("emoji220", "[饮料]");
    EMOJIHASHMAP.put("emoji221", "[酒]");
    EMOJIHASHMAP.put("emoji222", "[刀叉]");
    EMOJIHASHMAP.put("emoji301", "[浪漫]");
    EMOJIHASHMAP.put("emoji302", "[情侣]");
    EMOJIHASHMAP.put("emoji303", "[按摩]");
    EMOJIHASHMAP.put("emoji304", "[美发]");
    EMOJIHASHMAP.put("emoji305", "[美甲]");
    EMOJIHASHMAP.put("emoji306", "[篮球]");
    EMOJIHASHMAP.put("emoji307", "[足球]");
    EMOJIHASHMAP.put("emoji308", "[网球]");
    EMOJIHASHMAP.put("emoji309", "[桌球]");
    EMOJIHASHMAP.put("emoji310", "[游泳]");
    EMOJIHASHMAP.put("emoji311", "[猫]");
    EMOJIHASHMAP.put("emoji312", "[狗]");
    EMOJIHASHMAP.put("emoji313", "[仓鼠]");
    EMOJIHASHMAP.put("emoji314", "[兔子]");
    EMOJIHASHMAP.put("emoji315", "[小猪]");
    EMOJIHASHMAP.put("emoji316", "[樱花]");
    isScaned = false;
  }

  public static ArrayList<Emoji> emojiList()
  {
    if (!isScaned)
      scanEmojiDrawable();
    return EMOJILIST;
  }

  public static HashMap<String, Emoji> emojiMap()
  {
    if (!isScaned)
      scanEmojiDrawable();
    return STRINGEMOJIHASHMAP;
  }

  private static void scanEmojiDrawable()
  {
    Field[] arrayOfField;
    int j;
    int i;
    if (!isScaned)
    {
      STRINGEMOJIHASHMAP.clear();
      EMOJILIST.clear();
      arrayOfField = R.drawable.class.getFields();
      j = arrayOfField.length;
      i = 0;
    }
    while (true)
    {
      if (i < j)
      {
        Field localField = arrayOfField[i];
        try
        {
          String str = localField.getName();
          if ((!str.startsWith("emoji")) || (!EMOJIHASHMAP.containsKey(str)))
            break label152;
          Emoji localEmoji = new Emoji();
          localEmoji.drawableName = str;
          localEmoji.name = ((String)EMOJIHASHMAP.get(str));
          localEmoji.drawableId = localField.getInt(localField);
          STRINGEMOJIHASHMAP.put(localEmoji.name, localEmoji);
          EMOJILIST.add(localEmoji);
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          localIllegalArgumentException.printStackTrace();
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          localIllegalAccessException.printStackTrace();
        }
      }
      else
      {
        isScaned = true;
        return;
      }
      label152: i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.emoji.EmojiMap
 * JD-Core Version:    0.6.0
 */
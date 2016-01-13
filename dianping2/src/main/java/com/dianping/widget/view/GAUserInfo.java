package com.dianping.widget.view;

public class GAUserInfo
  implements Cloneable
{
  public String ad_id = null;
  public String biz_id = null;
  public Integer book_id = null;
  public Integer butag = null;
  public Integer category_id = null;
  public Integer checkin_id = null;
  public Integer deal_id = null;
  public Integer dealgroup_id = null;
  public Integer index = null;
  public String keyword = null;
  public String marketing_source = null;
  public Integer member_card_id = null;
  public Integer order_id = null;
  public String prepay_info = null;
  public Integer promo_id = null;
  public String query_id = null;
  public Integer receipt_id = null;
  public Integer region_id = null;
  public Integer review_id = null;
  public Integer sectionIndex = null;
  public Integer shop_id = null;
  public Integer sort_id = null;
  public String title = null;
  public String url = null;
  public String utm = null;

  public Object clone()
  {
    try
    {
      Object localObject = super.clone();
      return localObject;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
    }
    return null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.view.GAUserInfo
 * JD-Core Version:    0.6.0
 */
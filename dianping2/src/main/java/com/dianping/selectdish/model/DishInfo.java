package com.dianping.selectdish.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.archive.DPObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;

public class DishInfo
  implements Parcelable, Serializable
{
  public static final Parcelable.Creator<DishInfo> CREATOR = new Parcelable.Creator()
  {
    public DishInfo createFromParcel(Parcel paramParcel)
    {
      return new DishInfo(paramParcel);
    }

    public DishInfo[] newArray(int paramInt)
    {
      return new DishInfo[paramInt];
    }
  };
  public static final int GENUS_TYPE_MEAT = 2;
  public static final int GENUS_TYPE_OTHER = -1;
  public static final int GENUS_TYPE_VEGETABLE = 3;
  public static final int SET_DISH = 1;
  public static final int SINGLE_DISH = 0;
  public int bought;
  public double currentPrice;
  public String desc;
  public String detailurl;
  public double discount;
  public int dishId;
  public int dishType = 0;
  public String eventDesc;
  public int freeCount;
  public GiftInfo freeItem;
  public int freeRuleType;
  public int genusType = -1;
  public int giftCount;
  public int hasbought = 0;
  public boolean isDiscountSetMeal;
  public boolean isDishLiked;
  public boolean isValidity;
  public String name;
  public double oldPrice;
  public double oldPriceForSubmit;
  public int recommend;
  public String saleTime;
  public String sales;
  public ArrayList<SetItem> setItems = new ArrayList();
  public int setSuggest;
  public boolean soldout;
  public int spicy;
  public int status;
  public String tag;
  public String[] tags;
  private int tagsCount = 0;
  public int targetCount;
  public String unit;
  public String url;
  public String validityDesc;

  public DishInfo(Parcel paramParcel)
  {
    this.dishId = paramParcel.readInt();
    this.name = paramParcel.readString();
    this.tag = paramParcel.readString();
    this.tagsCount = paramParcel.readInt();
    if (this.tagsCount > 0)
    {
      this.tags = new String[this.tagsCount];
      paramParcel.readStringArray(this.tags);
    }
    this.unit = paramParcel.readString();
    this.spicy = paramParcel.readInt();
    if (paramParcel.readInt() == 1)
    {
      bool1 = true;
      this.soldout = bool1;
      this.desc = paramParcel.readString();
      this.bought = paramParcel.readInt();
      this.giftCount = paramParcel.readInt();
      this.sales = paramParcel.readString();
      this.recommend = paramParcel.readInt();
      this.oldPrice = paramParcel.readDouble();
      this.oldPriceForSubmit = paramParcel.readDouble();
      this.currentPrice = paramParcel.readDouble();
      this.url = paramParcel.readString();
      this.detailurl = paramParcel.readString();
      this.desc = paramParcel.readString();
      this.freeRuleType = paramParcel.readInt();
      this.targetCount = paramParcel.readInt();
      this.freeCount = paramParcel.readInt();
      this.discount = paramParcel.readDouble();
      this.eventDesc = paramParcel.readString();
      this.freeItem = ((GiftInfo)paramParcel.readParcelable(getClass().getClassLoader()));
      this.dishType = paramParcel.readInt();
      if (paramParcel.readInt() != 1)
        break label382;
      bool1 = true;
      label290: this.isValidity = bool1;
      if (paramParcel.readInt() != 1)
        break label387;
      bool1 = true;
      label305: this.isDishLiked = bool1;
      this.validityDesc = paramParcel.readString();
      this.setSuggest = paramParcel.readInt();
      this.saleTime = paramParcel.readString();
      this.genusType = paramParcel.readInt();
      paramParcel.readTypedList(this.setItems, SetItem.CREATOR);
      this.hasbought = paramParcel.readInt();
      if (paramParcel.readInt() != 1)
        break label392;
    }
    label387: label392: for (boolean bool1 = bool2; ; bool1 = false)
    {
      this.isDiscountSetMeal = bool1;
      return;
      bool1 = false;
      break;
      label382: bool1 = false;
      break label290;
      bool1 = false;
      break label305;
    }
  }

  public DishInfo(DPObject paramDPObject)
  {
    this.status = paramDPObject.getInt("Status");
    this.dishId = paramDPObject.getInt("Id");
    this.name = paramDPObject.getString("Name");
    this.tags = paramDPObject.getStringArray("Tags");
    if ((this.tags != null) && (this.tags.length > 0))
    {
      this.tag = this.tags[0];
      this.tagsCount = this.tags.length;
    }
    this.unit = paramDPObject.getString("Unit");
    this.url = paramDPObject.getString("PicUrl");
    this.detailurl = paramDPObject.getString("DetailUrl");
    this.isValidity = paramDPObject.getBoolean("Validity");
    this.isDishLiked = paramDPObject.getBoolean("IsDishLiked");
    this.validityDesc = paramDPObject.getString("ValidityDesc");
    this.saleTime = paramDPObject.getString("SaleTime");
    this.genusType = paramDPObject.getInt("GenusType");
    if ((this.genusType != 2) && (this.genusType != 3))
      this.genusType = -1;
    try
    {
      this.oldPrice = Double.parseDouble(paramDPObject.getString("OriginPrice"));
      localObject1 = paramDPObject.getString("DishTaste");
    }
    catch (Exception localException5)
    {
      try
      {
        this.spicy = new JSONObject((String)localObject1).getInt("spicy");
        this.sales = paramDPObject.getString("DishSales");
        this.recommend = paramDPObject.getInt("RecomCount");
        this.soldout = paramDPObject.getBoolean("SoldOut");
        this.desc = paramDPObject.getString("Desc");
        this.dishType = paramDPObject.getInt("DishType");
        localObject1 = paramDPObject.getObject("MenuEvent");
        if (localObject1 != null)
        {
          this.eventDesc = ((DPObject)localObject1).getString("EventDesc");
          this.bought = ((DPObject)localObject1).getInt("Purchase");
          this.giftCount = ((DPObject)localObject1).getInt("GiftCount");
          this.freeRuleType = ((DPObject)localObject1).getInt("Type");
          this.targetCount = ((DPObject)localObject1).getInt("BuyCount");
          this.freeCount = ((DPObject)localObject1).getInt("FreeCount");
          localObject2 = ((DPObject)localObject1).getObject("MenuGift");
          if (localObject2 == null)
            break label577;
          this.freeItem = new GiftInfo((DPObject)localObject2);
        }
      }
      catch (Exception localException5)
      {
        try
        {
          this.discount = Double.parseDouble(((DPObject)localObject1).getString("Discount"));
          if (this.freeRuleType == 2)
            this.currentPrice = this.discount;
        }
        catch (Exception localException5)
        {
          try
          {
            while (true)
            {
              this.currentPrice = Double.parseDouble(paramDPObject.getString("Price"));
              if (localObject1 == null)
                this.oldPrice = this.currentPrice;
              this.oldPriceForSubmit = this.oldPrice;
              if (this.dishType != 1)
                break label655;
              Object localObject2 = paramDPObject.getArray("DishSet");
              Object localObject1 = localObject2;
              if (localObject2 == null)
                localObject1 = paramDPObject.getArray("DishesPackage");
              if (localObject1 != null)
                break;
              i = 0;
              int j = 0;
              while (j < i)
              {
                localObject2 = new SetItem(localObject1[j].getInt("Count"), localObject1[j].getObject("Dish"));
                this.setItems.add(localObject2);
                j += 1;
              }
              localException1 = localException1;
              localException1.printStackTrace();
              continue;
              localException2 = localException2;
              localException2.printStackTrace();
              continue;
              label577: this.freeItem = null;
              continue;
              localException5 = localException5;
              localException5.printStackTrace();
              continue;
              this.currentPrice = this.oldPrice;
            }
          }
          catch (Exception localException3)
          {
            while (true)
            {
              localException3.printStackTrace();
              continue;
              int i = localException3.length;
            }
            this.setSuggest = paramDPObject.getInt("PersonSuggest");
          }
        }
      }
    }
    try
    {
      this.oldPrice = Double.parseDouble(paramDPObject.getString("PackageDishPrice"));
      this.isDiscountSetMeal = paramDPObject.getBoolean("IsDiscountSetMeal");
      label655: return;
    }
    catch (Exception localException4)
    {
      while (true)
        localException4.printStackTrace();
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public int getSetDishesCount()
  {
    int i = 0;
    Iterator localIterator = this.setItems.iterator();
    while (localIterator.hasNext())
      i += ((SetItem)localIterator.next()).count;
    return i;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int j = 1;
    paramParcel.writeInt(this.dishId);
    paramParcel.writeString(this.name);
    paramParcel.writeString(this.tag);
    paramParcel.writeInt(this.tagsCount);
    if (this.tagsCount > 0)
      paramParcel.writeStringArray(this.tags);
    paramParcel.writeString(this.unit);
    paramParcel.writeInt(this.spicy);
    int i;
    if (this.soldout)
    {
      i = 1;
      paramParcel.writeInt(i);
      paramParcel.writeString(this.desc);
      paramParcel.writeInt(this.bought);
      paramParcel.writeInt(this.giftCount);
      paramParcel.writeString(this.sales);
      paramParcel.writeInt(this.recommend);
      paramParcel.writeDouble(this.oldPrice);
      paramParcel.writeDouble(this.oldPriceForSubmit);
      paramParcel.writeDouble(this.currentPrice);
      paramParcel.writeString(this.url);
      paramParcel.writeString(this.detailurl);
      paramParcel.writeString(this.desc);
      paramParcel.writeInt(this.freeRuleType);
      paramParcel.writeInt(this.targetCount);
      paramParcel.writeInt(this.freeCount);
      paramParcel.writeDouble(this.discount);
      paramParcel.writeString(this.eventDesc);
      paramParcel.writeParcelable(this.freeItem, paramInt);
      paramParcel.writeInt(this.dishType);
      if (!this.isValidity)
        break label322;
      paramInt = 1;
      label234: paramParcel.writeInt(paramInt);
      if (!this.isDishLiked)
        break label327;
      paramInt = 1;
      label248: paramParcel.writeInt(paramInt);
      paramParcel.writeString(this.validityDesc);
      paramParcel.writeInt(this.setSuggest);
      paramParcel.writeString(this.saleTime);
      paramParcel.writeInt(this.genusType);
      paramParcel.writeTypedList(this.setItems);
      paramParcel.writeInt(this.hasbought);
      if (!this.isDiscountSetMeal)
        break label332;
    }
    label322: label327: label332: for (paramInt = j; ; paramInt = 0)
    {
      paramParcel.writeInt(paramInt);
      return;
      i = 0;
      break;
      paramInt = 0;
      break label234;
      paramInt = 0;
      break label248;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.model.DishInfo
 * JD-Core Version:    0.6.0
 */
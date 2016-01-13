package com.dianping.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.archive.Decoding;
import com.dianping.archive.DecodingFactory;
import com.dianping.archive.Unarchiver;

public class UserProfile
  implements Parcelable, Decoding
{
  public static final Parcelable.Creator<UserProfile> CREATOR;
  public static final DecodingFactory<UserProfile> DECODER = new DecodingFactory()
  {
    public UserProfile[] createArray(int paramInt)
    {
      return new UserProfile[paramInt];
    }

    public UserProfile createInstance(int paramInt)
    {
      if (paramInt == 11716)
        return new UserProfile(null);
      return null;
    }
  };
  private AccountBindResult accountBindResult;
  private String avatar;
  private int badgeCount;
  private int checkinCount;
  private int cityId;
  private int couponCount;
  private String email;
  private int fansCount;
  private int favoCount;
  private int feedFlag;
  private int gender;
  private int grouponFavoCount;
  private boolean grouponIsLocked;
  private String grouponPhone;
  private int honeyCount;
  private int id;
  private boolean isFans;
  private boolean isHoney;
  private boolean isInvited;
  private int level;
  private int lotteryCount;
  private int mayorCount;
  private int memberCardCount;
  private int movieCount;
  private String nickName;
  private String phoneNo;
  private int photoCount;
  private int reviewCount;
  private int score;
  private int snsImportedFlags;
  private String snsNickName;
  private long time;
  private String token;
  private String tuanBalance;
  private UserLevel userLevel;
  private int userPower;

  static
  {
    CREATOR = new Parcelable.Creator()
    {
      public UserProfile createFromParcel(Parcel paramParcel)
      {
        return new UserProfile(paramParcel, null);
      }

      public UserProfile[] newArray(int paramInt)
      {
        return new UserProfile[paramInt];
      }
    };
  }

  private UserProfile()
  {
  }

  public UserProfile(int paramInt1, String paramString1, int paramInt2, String paramString2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, String paramString3, long paramLong)
  {
    this.id = paramInt1;
    this.nickName = paramString1;
    this.gender = paramInt2;
    this.avatar = paramString2;
    this.userPower = paramInt3;
    this.cityId = paramInt4;
    this.isHoney = paramBoolean1;
    this.isInvited = paramBoolean2;
    this.token = paramString3;
    this.time = paramLong;
  }

  public UserProfile(int paramInt1, String paramString1, int paramInt2, String paramString2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, String paramString3, long paramLong, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10)
  {
    this.id = paramInt1;
    this.nickName = paramString1;
    this.gender = paramInt2;
    this.avatar = paramString2;
    this.userPower = paramInt3;
    this.cityId = paramInt4;
    this.isHoney = paramBoolean1;
    this.isInvited = paramBoolean2;
    this.token = paramString3;
    this.time = paramLong;
    this.level = paramInt5;
    this.score = paramInt6;
    this.checkinCount = paramInt7;
    this.badgeCount = paramInt8;
    this.honeyCount = paramInt9;
    this.feedFlag = paramInt10;
  }

  public UserProfile(int paramInt1, String paramString1, int paramInt2, String paramString2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, String paramString3, long paramLong, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11)
  {
    this.id = paramInt1;
    this.nickName = paramString1;
    this.gender = paramInt2;
    this.avatar = paramString2;
    this.userPower = paramInt3;
    this.cityId = paramInt4;
    this.isHoney = paramBoolean1;
    this.isInvited = paramBoolean2;
    this.token = paramString3;
    this.time = paramLong;
    this.level = paramInt5;
    this.score = paramInt6;
    this.checkinCount = paramInt7;
    this.badgeCount = paramInt8;
    this.honeyCount = paramInt9;
    this.feedFlag = paramInt10;
    this.mayorCount = paramInt11;
  }

  public UserProfile(int paramInt1, String paramString1, int paramInt2, String paramString2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, String paramString3, long paramLong, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12)
  {
    this.id = paramInt1;
    this.nickName = paramString1;
    this.gender = paramInt2;
    this.avatar = paramString2;
    this.userPower = paramInt3;
    this.cityId = paramInt4;
    this.isHoney = paramBoolean1;
    this.isFans = paramBoolean2;
    this.token = paramString3;
    this.time = paramLong;
    this.level = paramInt5;
    this.score = paramInt6;
    this.checkinCount = paramInt7;
    this.badgeCount = paramInt8;
    this.honeyCount = paramInt9;
    this.fansCount = paramInt10;
    this.feedFlag = paramInt12;
    this.mayorCount = paramInt11;
  }

  public UserProfile(int paramInt1, String paramString1, int paramInt2, String paramString2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, String paramString3, long paramLong, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15)
  {
    this.id = paramInt1;
    this.nickName = paramString1;
    this.gender = paramInt2;
    this.avatar = paramString2;
    this.userPower = paramInt3;
    this.cityId = paramInt4;
    this.isHoney = paramBoolean1;
    this.isFans = paramBoolean2;
    this.token = paramString3;
    this.time = paramLong;
    this.level = paramInt5;
    this.score = paramInt6;
    this.checkinCount = paramInt7;
    this.badgeCount = paramInt8;
    this.honeyCount = paramInt9;
    this.fansCount = paramInt10;
    this.feedFlag = paramInt12;
    this.mayorCount = paramInt11;
    this.favoCount = paramInt13;
    this.reviewCount = paramInt14;
    this.photoCount = paramInt15;
  }

  private UserProfile(Parcel paramParcel)
  {
    this.id = paramParcel.readInt();
    this.nickName = paramParcel.readString();
    this.gender = paramParcel.readInt();
    this.avatar = paramParcel.readString();
    this.userPower = paramParcel.readInt();
    this.cityId = paramParcel.readInt();
    if (paramParcel.readInt() == 1)
    {
      bool1 = true;
      this.isHoney = bool1;
      if (paramParcel.readInt() != 1)
        break label324;
      bool1 = true;
      label79: this.isFans = bool1;
      if (paramParcel.readInt() != 1)
        break label329;
      bool1 = true;
      label94: this.isInvited = bool1;
      this.token = paramParcel.readString();
      this.time = paramParcel.readLong();
      this.level = paramParcel.readInt();
      this.score = paramParcel.readInt();
      this.checkinCount = paramParcel.readInt();
      this.badgeCount = paramParcel.readInt();
      this.honeyCount = paramParcel.readInt();
      this.fansCount = paramParcel.readInt();
      this.lotteryCount = paramParcel.readInt();
      this.movieCount = paramParcel.readInt();
      this.feedFlag = paramParcel.readInt();
      this.mayorCount = paramParcel.readInt();
      this.email = paramParcel.readString();
      this.favoCount = paramParcel.readInt();
      this.reviewCount = paramParcel.readInt();
      this.photoCount = paramParcel.readInt();
      this.couponCount = paramParcel.readInt();
      this.grouponFavoCount = paramParcel.readInt();
      this.memberCardCount = paramParcel.readInt();
      this.snsNickName = paramParcel.readString();
      this.snsImportedFlags = paramParcel.readInt();
      this.grouponPhone = paramParcel.readString();
      this.phoneNo = paramParcel.readString();
      if (paramParcel.readInt() != 1)
        break label334;
    }
    label324: label329: label334: for (boolean bool1 = bool2; ; bool1 = false)
    {
      this.grouponIsLocked = bool1;
      this.userLevel = ((UserLevel)paramParcel.readValue(new SingleClassLoader(UserLevel.class)));
      return;
      bool1 = false;
      break;
      bool1 = false;
      break label79;
      bool1 = false;
      break label94;
    }
  }

  public String avatar()
  {
    return this.avatar;
  }

  public String avatarBig()
  {
    if ((this.avatar != null) && (this.avatar.endsWith("_s.jpg")))
      this.avatar = (this.avatar.substring(0, this.avatar.length() - 6) + "_b.jpg");
    return this.avatar;
  }

  public int checkinCount()
  {
    return this.checkinCount;
  }

  public int cityId()
  {
    return this.cityId;
  }

  public void decode(Unarchiver paramUnarchiver)
    throws ArchiveException
  {
    while (true)
    {
      int i = paramUnarchiver.readMemberHash16();
      if (i <= 0)
        break;
      switch (i)
      {
      default:
        paramUnarchiver.skipAnyObject();
        break;
      case 36310:
        this.id = paramUnarchiver.readInt();
        break;
      case 34988:
        this.nickName = paramUnarchiver.readString();
        break;
      case 40971:
        this.gender = paramUnarchiver.readInt();
        break;
      case 55534:
        this.avatar = paramUnarchiver.readString();
        break;
      case 63747:
        this.userPower = paramUnarchiver.readInt();
        break;
      case 42932:
        this.cityId = paramUnarchiver.readInt();
        break;
      case 11807:
        this.isHoney = paramUnarchiver.readBoolean();
        break;
      case 9005:
        this.isFans = paramUnarchiver.readBoolean();
        break;
      case 53350:
        this.isInvited = paramUnarchiver.readBoolean();
        break;
      case 52490:
        this.token = paramUnarchiver.readString();
        break;
      case 50890:
        this.time = paramUnarchiver.readDate();
        break;
      case 44858:
        this.level = paramUnarchiver.readInt();
        break;
      case 19122:
        this.score = paramUnarchiver.readInt();
        break;
      case 59607:
        this.checkinCount = paramUnarchiver.readInt();
        break;
      case 56062:
        this.badgeCount = paramUnarchiver.readInt();
        break;
      case 3325:
        this.honeyCount = paramUnarchiver.readInt();
        break;
      case 60184:
        this.fansCount = paramUnarchiver.readInt();
        break;
      case 17961:
        this.feedFlag = paramUnarchiver.readInt();
        break;
      case 41908:
        this.mayorCount = paramUnarchiver.readInt();
        break;
      case 22659:
        this.email = paramUnarchiver.readString();
        break;
      case 62190:
        this.favoCount = paramUnarchiver.readInt();
        break;
      case 23196:
        this.reviewCount = paramUnarchiver.readInt();
        break;
      case 22809:
        this.photoCount = paramUnarchiver.readInt();
        break;
      case 31686:
        this.memberCardCount = paramUnarchiver.readInt();
        break;
      case 36995:
        this.couponCount = paramUnarchiver.readInt();
        break;
      case 57025:
        this.grouponFavoCount = paramUnarchiver.readInt();
        break;
      case 3698:
        this.snsNickName = paramUnarchiver.readString();
        break;
      case 23112:
        this.snsImportedFlags = paramUnarchiver.readInt();
        break;
      case 45647:
        this.grouponPhone = paramUnarchiver.readString();
        break;
      case 1045:
        this.phoneNo = paramUnarchiver.readString();
        break;
      case 15608:
        this.grouponIsLocked = paramUnarchiver.readBoolean();
        break;
      case 47801:
        this.lotteryCount = paramUnarchiver.readInt();
        break;
      case 29519:
        this.movieCount = paramUnarchiver.readInt();
        break;
      case 6181:
        this.tuanBalance = paramUnarchiver.readString();
        break;
      case 3269:
        this.userLevel = ((UserLevel)paramUnarchiver.readObject(UserLevel.DECODER));
        break;
      case 45308:
      }
      this.accountBindResult = ((AccountBindResult)paramUnarchiver.readObject(AccountBindResult.DECODER));
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public String email()
  {
    return this.email;
  }

  public boolean equals(Object paramObject)
  {
    return (paramObject == this) || (((paramObject instanceof UserProfile)) && (((UserProfile)paramObject).id == this.id));
  }

  public int fansCount()
  {
    return this.fansCount;
  }

  public int favoCount()
  {
    return this.favoCount;
  }

  public int feedFlag()
  {
    return this.feedFlag;
  }

  public int gender()
  {
    return this.gender;
  }

  public AccountBindResult getAccountBindResult()
  {
    return this.accountBindResult;
  }

  public String getPhoneNo()
  {
    return this.phoneNo;
  }

  public UserLevel getUserLevel()
  {
    return this.userLevel;
  }

  public boolean grouponIsLocked()
  {
    return this.grouponIsLocked;
  }

  public String grouponPhone()
  {
    return this.grouponPhone;
  }

  public String gruponPhoneMasked()
  {
    try
    {
      if (!TextUtils.isEmpty(this.grouponPhone))
      {
        String str = this.grouponPhone.substring(0, 3) + "****" + this.grouponPhone.substring(7);
        return str;
      }
    }
    catch (Exception localException)
    {
    }
    return this.grouponPhone;
  }

  public int hashCode()
  {
    return this.id;
  }

  public int honeyCount()
  {
    return this.honeyCount;
  }

  public int id()
  {
    return this.id;
  }

  public boolean isFans()
  {
    return this.isFans;
  }

  public boolean isHoney()
  {
    return this.isHoney;
  }

  public boolean isInvited()
  {
    return this.isInvited;
  }

  public String nickName()
  {
    return this.nickName;
  }

  public int photoCount()
  {
    return this.photoCount;
  }

  public int reviewCount()
  {
    return this.reviewCount;
  }

  public void setAccountBindResult(AccountBindResult paramAccountBindResult)
  {
    this.accountBindResult = paramAccountBindResult;
  }

  public void setCouponCount(int paramInt)
  {
    this.couponCount = paramInt;
  }

  public void setEmail(String paramString)
  {
    this.email = paramString;
  }

  public void setGrouponFavoCount(int paramInt)
  {
    this.grouponFavoCount = paramInt;
  }

  public void setHoney(boolean paramBoolean)
  {
    this.isHoney = paramBoolean;
  }

  public void setMenberCardCount(int paramInt)
  {
    this.memberCardCount = paramInt;
  }

  public String snsNickName()
  {
    return this.snsNickName;
  }

  public DPObject toDPObject()
  {
    return new DPObject("UserProfile").edit().putInt("UserID", this.id).putString("NickName", this.nickName).putString("Avatar", this.avatar).putInt("UserPower", this.userPower).putInt("CityID", this.cityId).putBoolean("IsHoney", this.isHoney).putBoolean("IsInvited", this.isInvited).putBoolean("IsFans", this.isFans).putString("Token", this.token).putTime("Time", this.time).putInt("Level", this.level).putInt("Score", this.score).putInt("CheckInCount", this.checkinCount).putInt("BadgeCount", this.badgeCount).putInt("HoneyCount", this.honeyCount).putInt("FansCount", this.fansCount).putInt("FeedFlag", this.feedFlag).putInt("MayorCount", this.mayorCount).putInt("FavoCount", this.favoCount).putInt("ReviewCount", this.reviewCount).putInt("MovieTicketCount", this.movieCount).putInt("PhotoCount", this.photoCount).putInt("CouponCount", this.couponCount).putString("Email", this.email).putString("SnsNickName", this.snsNickName).putInt("SnsImportedFlags", this.snsImportedFlags).putInt("MemberCardCount", this.memberCardCount).putString("GrouponPhone", this.grouponPhone).putBoolean("GrouponIsLocked", this.grouponIsLocked).putInt("GrouponFavoCount", this.grouponFavoCount).putInt("Gender", this.gender).putInt("LetteryCount", this.lotteryCount).putString("PhoneNo", this.phoneNo).putObject("UserLevel", this.userLevel).generate();
  }

  public String toString()
  {
    return this.id + " : " + this.nickName;
  }

  public String token()
  {
    return this.token;
  }

  public int userPower()
  {
    return this.userPower;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    paramParcel.writeInt(this.id);
    paramParcel.writeString(this.nickName);
    paramParcel.writeInt(this.gender);
    paramParcel.writeString(this.avatar);
    paramParcel.writeInt(this.userPower);
    paramParcel.writeInt(this.cityId);
    if (this.isHoney)
    {
      paramInt = 1;
      paramParcel.writeInt(paramInt);
      if (!this.isFans)
        break label304;
      paramInt = 1;
      label73: paramParcel.writeInt(paramInt);
      if (!this.isInvited)
        break label309;
      paramInt = 1;
      label87: paramParcel.writeInt(paramInt);
      paramParcel.writeString(this.token);
      paramParcel.writeLong(this.time);
      paramParcel.writeInt(this.level);
      paramParcel.writeInt(this.score);
      paramParcel.writeInt(this.checkinCount);
      paramParcel.writeInt(this.badgeCount);
      paramParcel.writeInt(this.honeyCount);
      paramParcel.writeInt(this.fansCount);
      paramParcel.writeInt(this.lotteryCount);
      paramParcel.writeInt(this.movieCount);
      paramParcel.writeInt(this.feedFlag);
      paramParcel.writeInt(this.mayorCount);
      paramParcel.writeString(this.email);
      paramParcel.writeInt(this.favoCount);
      paramParcel.writeInt(this.reviewCount);
      paramParcel.writeInt(this.photoCount);
      paramParcel.writeInt(this.couponCount);
      paramParcel.writeInt(this.grouponFavoCount);
      paramParcel.writeInt(this.memberCardCount);
      paramParcel.writeString(this.snsNickName);
      paramParcel.writeInt(this.snsImportedFlags);
      paramParcel.writeString(this.grouponPhone);
      paramParcel.writeString(this.phoneNo);
      if (!this.grouponIsLocked)
        break label314;
    }
    label304: label309: label314: for (paramInt = i; ; paramInt = 0)
    {
      paramParcel.writeInt(paramInt);
      paramParcel.writeValue(this.userLevel);
      return;
      paramInt = 0;
      break;
      paramInt = 0;
      break label73;
      paramInt = 0;
      break label87;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.UserProfile
 * JD-Core Version:    0.6.0
 */
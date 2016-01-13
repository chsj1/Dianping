package com.dianping.dataservice.mapi.impl;

import android.util.SparseArray;
import com.dianping.dataservice.http.impl.BasicHttpResponse;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.SimpleMsg;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;
import org.apache.http.NameValuePair;

public class BasicMApiResponse extends BasicHttpResponse
  implements MApiResponse
{
  public static final Object ERROR_MALFORMED;
  public static final Object ERROR_STATUS = "server status error";
  static SparseArray<String> statusCodeArray;
  private byte[] rawData;

  static
  {
    ERROR_MALFORMED = "malformed content";
    statusCodeArray = new SparseArray();
    statusCodeArray.put(-103, "点小评去吃糖醋排骨了");
    statusCodeArray.put(-100, "点小评去吃香辣五花肉了");
    statusCodeArray.put(-104, "点小评去吃烧子鹅了");
    statusCodeArray.put(-102, "点小评去吃烧花鸭了");
    statusCodeArray.put(-105, "点小评去吃松花小肚了");
    statusCodeArray.put(-106, "点小评去吃烩鸭条了");
    statusCodeArray.put(502, "点小评去吃香酥鸡了");
    statusCodeArray.put(-109, "点小评去吃熘蟹肉了");
    statusCodeArray.put(450, "点小评去吃炒腰花儿了");
    statusCodeArray.put(-108, "点小评去吃锅烧白菜了");
    statusCodeArray.put(403, "点小评去吃水晶肘子了");
    statusCodeArray.put(-107, "点小评去吃焖黄鳝了");
    statusCodeArray.put(504, "点小评去吃什锦豆腐了");
    statusCodeArray.put(500, "点小评去吃清蒸鸡了");
    statusCodeArray.put(404, "点小评去吃小肚儿了");
    statusCodeArray.put(-111, "点小评去吃烩蟹肉了");
    statusCodeArray.put(408, "点小评去吃蜜蜡肘子了");
    statusCodeArray.put(401, "点小评去吃炸子蟹了");
    statusCodeArray.put(503, "点小评去吃软炸里脊了");
  }

  public BasicMApiResponse(int paramInt, byte[] paramArrayOfByte, Object paramObject1, List<NameValuePair> paramList, Object paramObject2)
  {
    super(paramInt, paramObject1, paramList, paramObject2);
    this.rawData = paramArrayOfByte;
  }

  private String getErrorMessage(int paramInt)
  {
    return (String)statusCodeArray.get(paramInt, "点小评去吃满汉全席了");
  }

  public SimpleMsg message()
  {
    Object localObject = error();
    if ((localObject instanceof SimpleMsg))
      return (SimpleMsg)localObject;
    if (localObject == ERROR_MALFORMED)
      return new SimpleMsg("点小评醉了", getErrorMessage(statusCode()), 0, 0);
    if (localObject == ERROR_STATUS)
      return new SimpleMsg("出错了", getErrorMessage(statusCode()), 0, 0);
    if ((localObject instanceof Exception))
    {
      if (((localObject instanceof UnknownHostException)) || ((localObject instanceof ConnectException)))
        return new SimpleMsg("错误", "网络不给力哦", 0, 0);
      return new SimpleMsg("点小评晕了", getErrorMessage(statusCode()), 0, 0);
    }
    return new SimpleMsg("错误", getErrorMessage(statusCode()), 0, 0);
  }

  public byte[] rawData()
  {
    return this.rawData;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.impl.BasicMApiResponse
 * JD-Core Version:    0.6.0
 */
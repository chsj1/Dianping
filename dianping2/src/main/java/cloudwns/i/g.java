package cloudwns.i;

import cloudwns.l.c;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class g
{
  private static String c = "Settings";
  ConcurrentHashMap a = new ConcurrentHashMap();
  ConcurrentHashMap b = new ConcurrentHashMap();
  private String d = "80,443,8080,14000";
  private String e = "1440|1200|700";

  public g()
  {
    this.a.put("HeartbeatTimeIdle", Long.valueOf(1200000L));
    this.a.put("HeartbeatTime", Long.valueOf(180000L));
    this.a.put("ConnectionCount", Long.valueOf(2L));
    this.a.put("RecvTimeout", Long.valueOf(20000L));
    this.a.put("SendTimeout", Long.valueOf(20000L));
    this.a.put("RequestTimeout", Long.valueOf(60000L));
    this.a.put("MaxPacketSize", Long.valueOf(2097152L));
    this.a.put("ConnectTimeout", Long.valueOf(20000L));
    this.a.put("LogCacheTime", Long.valueOf(7L));
    this.a.put("MaxLogFileSize", Long.valueOf(6L));
    this.a.put("AccReportInterval", Long.valueOf(600L));
    this.a.put("AccReportSamples", Long.valueOf(10L));
    this.a.put("AccReportCount", Long.valueOf(50L));
    this.a.put("HandshakeTimeout", Long.valueOf(30000L));
    this.a.put("HeartbeatTimeout", Long.valueOf(60000L));
    this.a.put("DNSTimeout", Long.valueOf(20000L));
    this.a.put("WnsDiagnosisSamples", Long.valueOf(100L));
    this.a.put("UploaderDiagnosisSamples", Long.valueOf(300L));
    this.a.put("CdnPort", Long.valueOf(80L));
    this.a.put("TimeoutRetryThreshold", Long.valueOf(3L));
    this.a.put("TcpParallelConnCount", Long.valueOf(2L));
    this.a.put("HttpParallelConnCount", Long.valueOf(2L));
    this.a.put("EnableLog", Long.valueOf(1L));
    this.a.put("LogLevel", Long.valueOf(3L));
    this.a.put("TraceSucReportInterval", Long.valueOf(86400000L));
    this.a.put("TraceFailReportInterval", Long.valueOf(3600000L));
    this.a.put("TestSpeedReqTimeOut", Long.valueOf(20000L));
    this.a.put("TestSpeedConnTime", Long.valueOf(30000L));
    this.a.put("UpdateOptimumIpInterval", Long.valueOf(604800000L));
    this.a.put("NoneTcpRetryInterval", Long.valueOf(21600000L));
    this.a.put("EnableSessionId", Long.valueOf(1L));
    this.a.put("ip_no_pmtu_disc", Long.valueOf(1L));
    this.a.put("ClearExpireOperator", Long.valueOf(2592000000L));
    this.a.put("AccErrorReportSamples", Long.valueOf(1L));
    this.a.put("EnableWakeLockDelay", Long.valueOf(0L));
    this.a.put("IPScoreRequstNumberThreshold", Long.valueOf(100L));
    this.a.put("IPScoreUnqualifiedRequestRatio", Long.valueOf(50L));
    this.a.put("IPScoreEchoRequestSize", Long.valueOf(1000L));
    this.a.put("PingRequestTimeout", Long.valueOf(30000L));
    this.a.put("PingRequestInterval", Long.valueOf(60000L));
    this.a.put("TestModeReqInterval", Long.valueOf(600000L));
    this.b.put("accPort", this.d);
    this.b.put("SocketMaxSeg", this.e);
    this.b.put("ReportLogServer", "183.61.39.173");
    this.b.put("ExpireTimeMsg", "由于您长时间未登录，为保护账号安全请重新登录");
    this.b.put("ReLoginMsg", "由于您密码已修改或存在盗号风险，为保护账号安全请重新登录");
    this.b.put("IPScoreEnable", "0|0|5000");
  }

  @Deprecated
  public long a(String paramString)
  {
    return a(paramString, 0L);
  }

  public long a(String paramString, long paramLong)
  {
    if ((paramString == null) || (paramString.length() == 0));
    Object localObject1;
    do
    {
      return paramLong;
      Object localObject2 = null;
      localObject1 = localObject2;
      if (this.a == null)
        continue;
      localObject1 = localObject2;
      if (!this.a.containsKey(paramString))
        continue;
      localObject1 = (Long)this.a.get(paramString);
    }
    while (localObject1 == null);
    return ((Long)localObject1).longValue();
  }

  public Object a(String paramString, Object paramObject)
  {
    if ((paramString == null) || (paramString.length() == 0));
    do
      return paramObject;
    while ((this.b == null) || (!this.b.containsKey(paramString)));
    return this.b.get(paramString);
  }

  public void a(Map paramMap)
  {
    if ((paramMap == null) || (paramMap.isEmpty()))
      label13: return;
    while (true)
      try
      {
        if (!paramMap.containsKey("WNSSettting"))
          continue;
        Object localObject1 = (Map)paramMap.get("WNSSettting");
        if ((localObject1 == null) || (((Map)localObject1).isEmpty()))
          continue;
        Object localObject2 = (String)((Map)localObject1).get("WifiAuthDetectSwitch");
        if (localObject2 == null)
          continue;
        this.a.put("WifiAuthDetectSwitch", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("WakeLockOnRecvLife");
        if (localObject2 == null)
          continue;
        this.a.put("WakeLockOnRecvLife", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("WakeLockHandlePushLife");
        if (localObject2 == null)
          continue;
        this.a.put("WakeLockHandlePushLife", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("RequestTimeout");
        if (localObject2 == null)
          continue;
        this.a.put("RequestTimeout", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("MaxPacketSize");
        if (localObject2 == null)
          continue;
        this.a.put("MaxPacketSize", Long.valueOf(Long.parseLong((String)localObject2) * 1048576L));
        localObject2 = (String)((Map)localObject1).get("HeartbeatTime");
        if (localObject2 == null)
          continue;
        this.a.put("HeartbeatTime", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("HeartbeatTimeIdle");
        if (localObject2 == null)
          continue;
        this.a.put("HeartbeatTimeIdle", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("ConnectTimeout");
        if (localObject2 == null)
          continue;
        this.a.put("ConnectTimeout", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("RecvTimeout");
        if (localObject2 == null)
          continue;
        this.a.put("RecvTimeout", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("ConnectionCount");
        if (localObject2 == null)
          continue;
        this.a.put("ConnectionCount", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("SendTimeout");
        if (localObject2 == null)
          continue;
        this.a.put("SendTimeout", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("LogCacheTime");
        if (localObject2 == null)
          continue;
        long l = Long.parseLong((String)localObject2);
        this.a.put("LogCacheTime", Long.valueOf(l));
        c.setMaxKeepPeriod(l * 24L * 60L * 60L * 1000L);
        localObject2 = (String)((Map)localObject1).get("MaxLogFileSize");
        if (localObject2 == null)
          continue;
        l = Long.parseLong((String)localObject2);
        this.a.put("MaxLogFileSize", Long.valueOf(l));
        c.setMaxFolderSize(l * 1024L * 1024L);
        localObject2 = (String)((Map)localObject1).get("AccReportInterval");
        if (localObject2 == null)
          continue;
        l = Long.parseLong((String)localObject2);
        this.a.put("AccReportInterval", Long.valueOf(l));
        cloudwns.f.a.a(l * 1000L);
        localObject2 = (String)((Map)localObject1).get("AccReportSamples");
        if (localObject2 == null)
          continue;
        l = Long.parseLong((String)localObject2);
        this.a.put("AccReportSamples", Long.valueOf(l));
        cloudwns.f.a.b((int)l);
        localObject2 = (String)((Map)localObject1).get("AccReportCount");
        if (localObject2 == null)
          continue;
        l = Long.parseLong((String)localObject2);
        this.a.put("AccReportCount", Long.valueOf(Long.parseLong((String)localObject2)));
        cloudwns.f.a.a((int)l);
        localObject2 = (String)((Map)localObject1).get("AccErrorReportSamples");
        if (localObject2 == null)
          continue;
        l = Long.parseLong((String)localObject2);
        this.a.put("AccErrorReportSamples", Long.valueOf(l));
        localObject2 = (String)((Map)localObject1).get("HandshakeTimeout");
        if (localObject2 == null)
          continue;
        this.a.put("HandshakeTimeout", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("HeartbeatTimeout");
        if (localObject2 == null)
          continue;
        this.a.put("HeartbeatTimeout", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("DNSTimeout");
        if (localObject2 == null)
          continue;
        this.a.put("DNSTimeout", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("PingRequestTimeout");
        if (localObject2 == null)
          continue;
        this.a.put("PingRequestTimeout", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("PingRequestInterval");
        if (localObject2 == null)
          continue;
        this.a.put("PingRequestInterval", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("accPort");
        if (localObject2 == null)
          continue;
        this.b.put("accPort", localObject2);
        localObject2 = (String)((Map)localObject1).get("WnsDiagnosisSamples");
        if (localObject2 == null)
          continue;
        this.a.put("WnsDiagnosisSamples", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("UploaderDiagnosisSamples");
        if (localObject2 == null)
          continue;
        this.a.put("UploaderDiagnosisSamples", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("CdnPort");
        if (localObject2 == null)
          continue;
        this.a.put("CdnPort", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("TimeoutRetryThreshold");
        if (localObject2 == null)
          continue;
        this.a.put("TimeoutRetryThreshold", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("TestSpeedReqTimeOut");
        if (localObject2 == null)
          continue;
        this.a.put("TestSpeedReqTimeOut", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("TestSpeedConnTime");
        if (localObject2 == null)
          continue;
        this.a.put("TestSpeedConnTime", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("UpdateOptimumIpInterval");
        if (localObject2 == null)
          continue;
        this.a.put("UpdateOptimumIpInterval", Long.valueOf(Long.parseLong((String)localObject2) * 3600000L));
        localObject2 = (String)((Map)localObject1).get("NoneTcpRetryInterval");
        if (localObject2 == null)
          continue;
        this.a.put("NoneTcpRetryInterval", Long.valueOf(Long.parseLong((String)localObject2) * 3600000L));
        localObject2 = (String)((Map)localObject1).get("EnableSessionId");
        if (localObject2 == null)
          continue;
        this.a.put("EnableSessionId", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("ip_no_pmtu_disc");
        if (localObject2 == null)
          continue;
        this.a.put("ip_no_pmtu_disc", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("ClearExpireOperator");
        if (localObject2 == null)
          continue;
        this.a.put("ClearExpireOperator", Long.valueOf(Long.parseLong((String)localObject2) * 86400000L));
        localObject2 = (String)((Map)localObject1).get("SocketMaxSeg");
        if (localObject2 == null)
          continue;
        this.b.put("SocketMaxSeg", localObject2);
        localObject2 = (String)((Map)localObject1).get("EnableWakeLockDelay");
        if (localObject2 == null)
          continue;
        this.a.put("EnableWakeLockDelay", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("ReportLogServer");
        if (localObject2 == null)
          continue;
        this.b.put("ReportLogServer", localObject2);
        localObject2 = (String)((Map)localObject1).get("IPScoreEnable");
        if (localObject2 == null)
          continue;
        this.b.put("IPScoreEnable", localObject2);
        localObject2 = (String)((Map)localObject1).get("IPScoreRequstNumberThreshold");
        if (localObject2 == null)
          continue;
        this.a.put("IPScoreRequstNumberThreshold", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("IPScoreUnqualifiedRequestRatio");
        if (localObject2 == null)
          continue;
        this.a.put("IPScoreUnqualifiedRequestRatio", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("IPScoreEchoRequestSize");
        if (localObject2 == null)
          continue;
        this.a.put("IPScoreEchoRequestSize", Long.valueOf(Long.parseLong((String)localObject2)));
        localObject2 = (String)((Map)localObject1).get("BindWaitTimeMin");
        if (localObject2 == null)
          continue;
        this.a.put("BindWaitTimeMin", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("BindWaitTimeMax");
        if (localObject2 == null)
          continue;
        this.a.put("BindWaitTimeMax", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject2 = (String)((Map)localObject1).get("TestModeReqInterval");
        if (localObject2 == null)
          continue;
        this.a.put("TestModeReqInterval", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
        localObject1 = (String)((Map)localObject1).get("OverLoadInterval");
        if (localObject1 == null)
          continue;
        this.a.put("OverLoadInterval", Long.valueOf(Long.parseLong((String)localObject1) * 1000L));
        if ((paramMap == null) || (!paramMap.containsKey("TraceLog")))
          continue;
        localObject1 = (Map)paramMap.get("TraceLog");
        if ((localObject1 == null) || (((Map)localObject1).isEmpty()))
          continue;
        localObject2 = (String)((Map)localObject1).get("EnableLog");
        if (localObject2 == null)
          continue;
        localObject2 = Long.valueOf(Long.parseLong((String)localObject2));
        this.a.put("EnableLog", localObject2);
        if (1L != ((Long)localObject2).longValue())
          continue;
        l = Long.parseLong((String)((Map)localObject1).get("LogLevel"));
        this.a.put("LogLevel", Long.valueOf(l));
        if (cloudwns.l.a.a() == null)
          continue;
        cloudwns.l.a.a().setFileTracerEnabled(true);
        i = 63;
        switch ((int)l)
        {
        case 0:
          c.setFileTraceLevel(i);
          if ((paramMap == null) || (!paramMap.containsKey("ReportSetting")))
            continue;
          localObject1 = (Map)paramMap.get("ReportSetting");
          if ((localObject1 == null) || (((Map)localObject1).isEmpty()))
            continue;
          localObject2 = (String)((Map)localObject1).get("TraceSucReportInterval");
          if (localObject2 == null)
            continue;
          this.a.put("TraceSucReportInterval", Long.valueOf(Long.parseLong((String)localObject2) * 1000L));
          localObject1 = (String)((Map)localObject1).get("TraceFailReportInterval");
          if (localObject1 == null)
            continue;
          this.a.put("TraceFailReportInterval", Long.valueOf(Long.parseLong((String)localObject1) * 1000L));
          if ((paramMap == null) || (!paramMap.containsKey("LoginState")))
            break label13;
          paramMap = (Map)paramMap.get("LoginState");
          if ((paramMap == null) || (paramMap.isEmpty()))
            break label13;
          localObject1 = (String)paramMap.get("StateExpireTimeQQ");
          if (localObject1 == null)
            continue;
          this.a.put("StateExpireTimeQQ", Long.valueOf(Long.parseLong((String)localObject1) * 1000L));
          localObject1 = (String)paramMap.get("StateExpireTimeWechat");
          if (localObject1 == null)
            continue;
          this.a.put("StateExpireTimeWechat", Long.valueOf(Long.parseLong((String)localObject1) * 1000L));
          localObject1 = (String)paramMap.get("ExpireTimeMsg");
          if (localObject1 == null)
            continue;
          this.b.put("ExpireTimeMsg", localObject1);
          paramMap = (String)paramMap.get("由于您长时间未登录，为保护账号安全请重新登录");
          if (paramMap == null)
            break label13;
          this.b.put("由于您长时间未登录，为保护账号安全请重新登录", paramMap);
          return;
        case 1:
        case 2:
        case 3:
        }
      }
      catch (java.lang.NumberFormatException paramMap)
      {
        cloudwns.l.a.e(c, "NumberFormatException fail!");
        return;
        int i = 48;
        continue;
        i = 56;
        continue;
        i = 60;
        continue;
        i = 62;
        continue;
        cloudwns.l.a.a().setFileTracerEnabled(false);
        continue;
      }
      catch (java.lang.Exception paramMap)
      {
        cloudwns.l.a.e(c, "Exception fail!");
        return;
      }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     cloudwns.i.g
 * JD-Core Version:    0.6.0
 */
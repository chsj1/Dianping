package com.dianping.utn.client;

import com.dianping.utn.HttpRequest;
import com.dianping.utn.PingRequest;
import com.dianping.utn.ResponseBlock;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class UtnPingConnection extends UtnConnection
{
  private static final int RANDOM_SERVERS = 5;
  private static final int WAIT_EXTRA = 200;
  private final Runnable done = new Runnable()
  {
    public void run()
    {
      UtnPingConnection.PingSession[] arrayOfPingSession = UtnPingConnection.this.running;
      if (UtnPingConnection.this.running == null)
        return;
      Object localObject1 = null;
      long l1 = 9223372036854775807L;
      int j = arrayOfPingSession.length;
      int i = 0;
      while (i < j)
      {
        UtnPingConnection.PingSession localPingSession = arrayOfPingSession[i];
        localObject2 = localObject1;
        long l2 = l1;
        if (localPingSession.elapse > 0L)
        {
          localObject2 = localObject1;
          l2 = l1;
          if (localPingSession.elapse < l1)
          {
            l2 = localPingSession.elapse;
            localObject2 = localPingSession;
          }
        }
        i += 1;
        localObject1 = localObject2;
        l1 = l2;
      }
      Object localObject2 = UtnPingConnection.this;
      if (localObject1 == null);
      for (localObject1 = null; ; localObject1 = ((UtnPingConnection.PingSession)localObject1).server)
      {
        ((UtnPingConnection)localObject2).onPingResult((SocketAddress)localObject1, l1);
        return;
      }
    }
  };
  int lastPingNetwork = -1;
  SocketAddress lastPingResult;
  long lastPingTime;
  SocketAddress lastSuccessPingResult;
  private Random rnd = new Random(timestamp());
  PingSession[] running;
  SocketAddress server;
  private final Runnable timeout = new Runnable()
  {
    public void run()
    {
      if (UtnPingConnection.this.running != null)
        UtnPingConnection.this.onPingResult(null, 0L);
    }
  };

  private void donePing(PingSession paramPingSession)
  {
    PingSession[] arrayOfPingSession = this.running;
    int j;
    if (arrayOfPingSession != null)
    {
      j = 0;
      int k = 0;
      int n = arrayOfPingSession.length;
      int i = 0;
      while (i < n)
      {
        PingSession localPingSession = arrayOfPingSession[i];
        if (localPingSession == paramPingSession)
          k = 1;
        int m = j;
        if (localPingSession.elapse < 0L)
          m = j + 1;
        i += 1;
        j = m;
      }
      if ((k == 0) || (paramPingSession.elapse <= 0L))
        break label102;
      scheduleRun(this.done, paramPingSession.wait);
    }
    label102: 
    do
      return;
    while (j != arrayOfPingSession.length);
    onPingResult(null, 0L);
  }

  private static List<SocketAddress> random(List<SocketAddress> paramList, SocketAddress paramSocketAddress, int paramInt, Random paramRandom)
  {
    paramList = new ArrayList(paramList);
    Collections.shuffle(paramList, paramRandom);
    if (paramSocketAddress != null)
    {
      paramList.remove(paramSocketAddress);
      paramList.add(paramSocketAddress);
    }
    int i = paramList.size() - 1;
    if (i >= paramInt)
    {
      paramSocketAddress = (SocketAddress)paramList.get(i);
      if (!(paramSocketAddress instanceof InetSocketAddress));
      while (true)
      {
        i -= 1;
        break;
        paramRandom = ((InetSocketAddress)paramSocketAddress).getAddress().getHostAddress();
        int j = paramRandom.lastIndexOf('.');
        if (j <= 0)
          continue;
        paramRandom = paramRandom.substring(0, j + 1);
        int m = 0;
        j = 0;
        int k = m;
        if (j < paramInt)
        {
          SocketAddress localSocketAddress = (SocketAddress)paramList.get(j);
          if (!(localSocketAddress instanceof InetSocketAddress));
          do
          {
            j += 1;
            break;
          }
          while (!((InetSocketAddress)localSocketAddress).getAddress().getHostAddress().startsWith(paramRandom));
          k = 1;
        }
        paramList.remove(i);
        if (k != 0)
          continue;
        paramList.add(1, paramSocketAddress);
        paramList.remove(paramList.size() - 1);
      }
    }
    return paramList;
  }

  public void cancel()
  {
    monitorenter;
    try
    {
      PingSession[] arrayOfPingSession = this.running;
      this.running = null;
      if (arrayOfPingSession != null)
      {
        int j = arrayOfPingSession.length;
        int i = 0;
        while (i < j)
        {
          unscheduleRun(arrayOfPingSession[i]);
          i += 1;
        }
        unscheduleRun(this.done);
        unscheduleRun(this.timeout);
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  protected PingSession createPingSession(SocketAddress paramSocketAddress)
  {
    PingRequest localPingRequest = new PingRequest();
    localPingRequest.requestId = UtnUtils.generatePingRequestId();
    localPingRequest.network = getNetwork();
    return new PingSession(paramSocketAddress, localPingRequest);
  }

  protected UtnConnection.Session createSession(HttpRequest paramHttpRequest, Object paramObject)
  {
    if (paramHttpRequest.requestId == 0)
      paramHttpRequest.requestId = UtnUtils.generateHttpRequestId();
    paramHttpRequest.network = getNetwork();
    return new UtnConnection.Session(this, this.server, paramHttpRequest);
  }

  protected void dispatchDone(UtnConnection.Session paramSession)
  {
    if ((paramSession.status == -2) && (!isPinging()))
      pingServers();
  }

  protected abstract int getNetwork();

  public SocketAddress getServer()
  {
    return this.server;
  }

  protected abstract List<SocketAddress> getServers();

  public boolean isPinging()
  {
    return this.running != null;
  }

  public boolean isReady()
  {
    return (this.server != null) && (super.isReady());
  }

  protected void onPingResult(SocketAddress paramSocketAddress, long paramLong)
  {
    if (loggable())
      log("PING RESULT: " + paramSocketAddress);
    if (paramSocketAddress != null)
      this.lastSuccessPingResult = paramSocketAddress;
    this.lastPingResult = paramSocketAddress;
    cancel();
    setServer(paramSocketAddress);
  }

  public void pingIfNecessary()
  {
    if (!isPinging())
    {
      if ((this.lastPingNetwork == getNetwork()) && (this.lastPingResult != null))
        break label30;
      pingServers();
    }
    label30: 
    do
      return;
    while ((pingInterval() <= 0) || (timestamp() - this.lastPingTime + 1L < pingInterval()));
    pingServers();
  }

  public int pingInterval()
  {
    return 30000;
  }

  public void pingServers()
  {
    int i;
    switch (getNetwork())
    {
    default:
      i = 8000;
    case 2:
    case 1:
    case 3:
    }
    while (true)
    {
      pingServers((SocketAddress[])randomServers().toArray(new SocketAddress[0]), i);
      return;
      int j = 4000;
      continue;
      j = 6000;
      continue;
      j = 5000;
    }
  }

  public void pingServers(SocketAddress[] paramArrayOfSocketAddress, long paramLong)
  {
    monitorenter;
    int j;
    try
    {
      cancel();
      int k = paramArrayOfSocketAddress.length;
      PingSession[] arrayOfPingSession = new PingSession[k];
      int m = getNetwork();
      switch (m)
      {
      default:
        while (j < k)
        {
          PingSession localPingSession = createPingSession(paramArrayOfSocketAddress[j]);
          localPingSession.wait = ((k - j - 1) * i + 200);
          arrayOfPingSession[j] = localPingSession;
          j += 1;
        }
        this.running = arrayOfPingSession;
        this.lastPingTime = timestamp();
        this.lastPingNetwork = m;
        j = 0;
        while (j < k)
        {
          scheduleRun(arrayOfPingSession[j], j * i);
          j += 1;
        }
        if (paramLong > 0L)
          scheduleRun(this.timeout, paramLong);
        return;
      case 0:
      case 1:
      case 2:
      case 3:
      }
    }
    finally
    {
      monitorexit;
    }
    int i = 120;
    while (true)
    {
      j = 0;
      break;
      i = 150;
      continue;
      i = 120;
      continue;
      i = 60;
      continue;
      i = 80;
    }
  }

  protected void processResponseBlock(ResponseBlock paramResponseBlock)
    throws Exception
  {
    if (UtnUtils.isPingRequestId(paramResponseBlock.requestId))
    {
      PingSession[] arrayOfPingSession = this.running;
      int j;
      int i;
      if (arrayOfPingSession != null)
      {
        j = arrayOfPingSession.length;
        i = 0;
      }
      while (true)
      {
        if (i < j)
        {
          PingSession localPingSession = arrayOfPingSession[i];
          if (localPingSession.req.requestId == paramResponseBlock.requestId)
            localPingSession.recv(paramResponseBlock);
        }
        else
        {
          return;
        }
        i += 1;
      }
    }
    super.processResponseBlock(paramResponseBlock);
  }

  protected List<SocketAddress> randomServers()
  {
    return random(getServers(), this.lastSuccessPingResult, 5, this.rnd);
  }

  public int send(HttpRequest paramHttpRequest, Object paramObject)
  {
    monitorenter;
    try
    {
      int i = super.send(paramHttpRequest, paramObject);
      pingIfNecessary();
      monitorexit;
      return i;
    }
    finally
    {
      paramHttpRequest = finally;
      monitorexit;
    }
    throw paramHttpRequest;
  }

  public void setServer(SocketAddress paramSocketAddress)
  {
    this.server = paramSocketAddress;
  }

  private class PingSession
    implements Runnable
  {
    long elapse;
    final PingRequest req;
    final SocketAddress server;
    long startTime;
    int wait;

    public PingSession(SocketAddress paramPingRequest, PingRequest arg3)
    {
      this.server = paramPingRequest;
      Object localObject;
      this.req = localObject;
    }

    void recv(ResponseBlock paramResponseBlock)
      throws Exception
    {
      monitorenter;
      try
      {
        if (this.elapse == 0L)
          if (this.startTime != 0L)
            break label94;
        label94: for (this.elapse = -1L; ; this.elapse = (UtnPingConnection.this.timestamp() - this.startTime))
        {
          if (UtnPingConnection.this.loggable())
            UtnPingConnection.this.log("PING RECV: " + this.server + " " + this.elapse + "ms");
          UtnPingConnection.this.donePing(this);
          return;
        }
      }
      finally
      {
        monitorexit;
      }
      throw paramResponseBlock;
    }

    public void run()
    {
      if (this.startTime == 0L)
        this.startTime = UtnPingConnection.this.timestamp();
      try
      {
        send();
        return;
      }
      catch (Exception localException)
      {
        if (UtnPingConnection.this.loggable())
        {
          if (!(localException instanceof SocketException))
            break label88;
          UtnPingConnection.this.log("PING SOCK ERR: " + localException.getMessage());
        }
      }
      while (true)
      {
        this.elapse = -1L;
        UtnPingConnection.this.donePing(this);
        return;
        label88: UtnPingConnection.this.log("PING ERR: " + localException.getClass() + " " + localException.getMessage());
      }
    }

    void send()
      throws Exception
    {
      monitorenter;
      try
      {
        if (UtnPingConnection.this.loggable())
          UtnPingConnection.this.log("PING SEND: " + this.server);
        DatagramPacket localDatagramPacket = this.req.pack();
        localDatagramPacket.setSocketAddress(this.server);
        UtnPingConnection.this.socket.send(localDatagramPacket);
        return;
      }
      finally
      {
        monitorexit;
      }
      throw localObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.utn.client.UtnPingConnection
 * JD-Core Version:    0.6.0
 */
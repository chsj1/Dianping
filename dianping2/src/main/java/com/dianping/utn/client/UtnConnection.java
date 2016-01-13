package com.dianping.utn.client;

import com.dianping.utn.HttpAck;
import com.dianping.utn.HttpRequest;
import com.dianping.utn.ResponseBlock;
import com.dianping.utn.ResponseChain;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class UtnConnection
{
  static final int MAX_ACK_BITS = 16;
  static final int MAX_SEND = 4;
  static final int MIN_ACK_BITS = 3;
  static final int STATUS_CANCELED = -1;
  static final int STATUS_ERROR = -5;
  static final int STATUS_FINISHED = 4;
  static final int STATUS_IDLE = 0;
  static final int STATUS_RECEVING = 2;
  static final int STATUS_SENDING = 1;
  static final int STATUS_TIMEOUT_RECV = -3;
  static final int STATUS_TIMEOUT_SEND = -2;
  private boolean ready;
  final Map<Integer, Session> sessions = new ConcurrentHashMap();
  DatagramSocket socket;

  public UtnConnection()
  {
    try
    {
      this.socket = new DatagramSocket();
      this.ready = true;
      label31: if (this.ready)
        new Thread("utn_in")
        {
          public void run()
          {
            DatagramPacket localDatagramPacket = new DatagramPacket(new byte[1400], 1400);
            while (!UtnConnection.this.socket.isClosed())
            {
              try
              {
                UtnConnection.this.socket.receive(localDatagramPacket);
                localObject = new ResponseBlock();
                ((ResponseBlock)localObject).parse(localDatagramPacket);
                UtnConnection.this.processResponseBlock((ResponseBlock)localObject);
              }
              catch (Exception localException)
              {
              }
              if (!UtnConnection.this.loggable())
                continue;
              Object localObject = "";
              if (localDatagramPacket.getSocketAddress() != null)
                localObject = localDatagramPacket.getSocketAddress() + " ";
              if ((localException instanceof SocketException))
              {
                UtnConnection.this.log("SOCK ERR: " + (String)localObject + localException.getMessage());
                continue;
              }
              UtnConnection.this.log("R ERR: " + (String)localObject + localException.getClass() + " " + localException.getMessage());
            }
          }
        }
        .start();
      return;
    }
    catch (Exception localException)
    {
      break label31;
    }
  }

  private void done(Session paramSession)
  {
    monitorenter;
    try
    {
      this.sessions.remove(Integer.valueOf(paramSession.req.requestId));
      unscheduleRun(paramSession);
      dispatchDone(paramSession);
      monitorexit;
      return;
    }
    finally
    {
      paramSession = finally;
      monitorexit;
    }
    throw paramSession;
  }

  private static String getCommand(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      return "";
    int j = paramString.indexOf('?');
    int i = j;
    if (j < 0)
      i = paramString.length();
    int k = paramString.lastIndexOf('/', i);
    j = k;
    if (k < 0)
      j = -1;
    return paramString.substring(j + 1, i);
  }

  public void abort(int paramInt)
  {
    monitorenter;
    try
    {
      Session localSession = (Session)this.sessions.remove(Integer.valueOf(paramInt));
      if (localSession != null)
      {
        localSession.status = -1;
        unscheduleRun(localSession);
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void close()
  {
    monitorenter;
    try
    {
      boolean bool = this.ready;
      if (bool)
        try
        {
          if (this.socket != null)
            this.socket.close();
          Iterator localIterator = this.sessions.values().iterator();
          while (localIterator.hasNext())
          {
            Session localSession = (Session)localIterator.next();
            localSession.status = -1;
            unscheduleRun(localSession);
          }
        }
        catch (Exception localException)
        {
        }
      while (true)
      {
        this.ready = false;
        return;
        this.sessions.clear();
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  protected abstract Session createSession(HttpRequest paramHttpRequest, Object paramObject);

  protected abstract void dispatchDone(Session paramSession);

  public boolean isReady()
  {
    return this.ready;
  }

  protected void log(String paramString)
  {
  }

  protected boolean loggable()
  {
    return false;
  }

  protected void processResponseBlock(ResponseBlock paramResponseBlock)
    throws Exception
  {
    if (UtnUtils.isHttpRequestId(paramResponseBlock.requestId))
      monitorenter;
    try
    {
      Session localSession = (Session)this.sessions.get(Integer.valueOf(paramResponseBlock.requestId));
      monitorexit;
      if (localSession != null)
        localSession.recv(paramResponseBlock);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw paramResponseBlock;
  }

  protected abstract void scheduleRun(Runnable paramRunnable, long paramLong);

  public int send(HttpRequest paramHttpRequest, Object paramObject)
  {
    monitorenter;
    try
    {
      Session localSession;
      int i;
      if (isReady())
      {
        localSession = (Session)this.sessions.get(Integer.valueOf(paramHttpRequest.requestId));
        if (localSession == null)
        {
          paramObject = createSession(paramHttpRequest, paramObject);
          this.sessions.put(Integer.valueOf(paramHttpRequest.requestId), paramObject);
          scheduleRun(paramObject, 0L);
          i = paramObject.req.requestId;
        }
      }
      while (true)
      {
        return i;
        i = localSession.req.requestId;
        continue;
        i = 0;
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramHttpRequest;
  }

  protected long timestamp()
  {
    return System.nanoTime() / 1000000L;
  }

  protected abstract void unscheduleRun(Runnable paramRunnable);

  protected class Session
    implements Runnable
  {
    HttpAck ack;
    final int ackPeriod;
    long firstSendTime;
    int lastAckBits;
    long lastAckTime;
    long lastRecvTime;
    long lastSendTime;
    final int recvTimeout;
    final HttpRequest req;
    ResponseChain respChain;
    int sendCount;
    final int sendPeriod;
    final int sendTimeout;
    public final SocketAddress server;
    int status;

    public Session(SocketAddress paramHttpRequest, HttpRequest arg3)
    {
      this.server = paramHttpRequest;
      Object localObject;
      this.req = localObject;
      switch (localObject.network)
      {
      default:
        this.sendPeriod = 2000;
        this.recvTimeout = 3500;
        this.ackPeriod = 1000;
      case 3:
      case 0:
      case 1:
      case 2:
      }
      while (true)
      {
        this.sendTimeout = (this.sendPeriod * 6 + 500);
        return;
        this.sendPeriod = 1200;
        this.recvTimeout = 2000;
        this.ackPeriod = 500;
        continue;
        this.sendPeriod = 3000;
        this.recvTimeout = 5000;
        this.ackPeriod = 1000;
        continue;
        this.sendPeriod = 2000;
        this.recvTimeout = 3500;
        this.ackPeriod = 1000;
        continue;
        this.sendPeriod = 1000;
        this.recvTimeout = 1500;
        this.ackPeriod = 400;
      }
    }

    private String name()
    {
      return this.req.requestId + " " + UtnConnection.access$000(this.req.url);
    }

    // ERROR //
    void ack()
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield 36	com/dianping/utn/client/UtnConnection$Session:this$0	Lcom/dianping/utn/client/UtnConnection;
      //   6: invokevirtual 91	com/dianping/utn/client/UtnConnection:loggable	()Z
      //   9: ifeq +63 -> 72
      //   12: aload_0
      //   13: getfield 93	com/dianping/utn/client/UtnConnection$Session:respChain	Lcom/dianping/utn/ResponseChain;
      //   16: ifnonnull +153 -> 169
      //   19: aload_0
      //   20: getfield 95	com/dianping/utn/client/UtnConnection$Session:ack	Lcom/dianping/utn/HttpAck;
      //   23: invokevirtual 101	com/dianping/utn/HttpAck:countAll	()I
      //   26: istore_2
      //   27: aload_0
      //   28: getfield 36	com/dianping/utn/client/UtnConnection$Session:this$0	Lcom/dianping/utn/client/UtnConnection;
      //   31: new 61	java/lang/StringBuilder
      //   34: dup
      //   35: invokespecial 62	java/lang/StringBuilder:<init>	()V
      //   38: ldc 103
      //   40: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   43: aload_0
      //   44: invokespecial 105	com/dianping/utn/client/UtnConnection$Session:name	()Ljava/lang/String;
      //   47: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   50: ldc 71
      //   52: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   55: aload_0
      //   56: getfield 95	com/dianping/utn/client/UtnConnection$Session:ack	Lcom/dianping/utn/HttpAck;
      //   59: iload_2
      //   60: invokevirtual 109	com/dianping/utn/HttpAck:bitsString	(I)Ljava/lang/String;
      //   63: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   66: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   69: invokevirtual 113	com/dianping/utn/client/UtnConnection:log	(Ljava/lang/String;)V
      //   72: aload_0
      //   73: getfield 36	com/dianping/utn/client/UtnConnection$Session:this$0	Lcom/dianping/utn/client/UtnConnection;
      //   76: invokevirtual 117	com/dianping/utn/client/UtnConnection:timestamp	()J
      //   79: lstore_3
      //   80: lload_3
      //   81: aload_0
      //   82: getfield 119	com/dianping/utn/client/UtnConnection$Session:firstSendTime	J
      //   85: lsub
      //   86: lstore 5
      //   88: aload_0
      //   89: getfield 95	com/dianping/utn/client/UtnConnection$Session:ack	Lcom/dianping/utn/HttpAck;
      //   92: astore_1
      //   93: lload 5
      //   95: ldc2_w 120
      //   98: lcmp
      //   99: ifle +82 -> 181
      //   102: ldc 122
      //   104: istore_2
      //   105: aload_1
      //   106: iload_2
      //   107: putfield 125	com/dianping/utn/HttpAck:time	I
      //   110: aload_0
      //   111: getfield 95	com/dianping/utn/client/UtnConnection$Session:ack	Lcom/dianping/utn/HttpAck;
      //   114: invokevirtual 129	com/dianping/utn/HttpAck:pack	()Ljava/net/DatagramPacket;
      //   117: astore_1
      //   118: aload_1
      //   119: aload_0
      //   120: getfield 41	com/dianping/utn/client/UtnConnection$Session:server	Ljava/net/SocketAddress;
      //   123: invokevirtual 135	java/net/DatagramPacket:setSocketAddress	(Ljava/net/SocketAddress;)V
      //   126: aload_0
      //   127: getfield 36	com/dianping/utn/client/UtnConnection$Session:this$0	Lcom/dianping/utn/client/UtnConnection;
      //   130: getfield 139	com/dianping/utn/client/UtnConnection:socket	Ljava/net/DatagramSocket;
      //   133: aload_1
      //   134: invokevirtual 145	java/net/DatagramSocket:send	(Ljava/net/DatagramPacket;)V
      //   137: aload_0
      //   138: aload_0
      //   139: getfield 95	com/dianping/utn/client/UtnConnection$Session:ack	Lcom/dianping/utn/HttpAck;
      //   142: invokevirtual 101	com/dianping/utn/HttpAck:countAll	()I
      //   145: putfield 147	com/dianping/utn/client/UtnConnection$Session:lastAckBits	I
      //   148: aload_0
      //   149: lload_3
      //   150: putfield 149	com/dianping/utn/client/UtnConnection$Session:lastAckTime	J
      //   153: aload_0
      //   154: getfield 36	com/dianping/utn/client/UtnConnection$Session:this$0	Lcom/dianping/utn/client/UtnConnection;
      //   157: aload_0
      //   158: aload_0
      //   159: getfield 54	com/dianping/utn/client/UtnConnection$Session:ackPeriod	I
      //   162: i2l
      //   163: invokevirtual 153	com/dianping/utn/client/UtnConnection:scheduleRun	(Ljava/lang/Runnable;J)V
      //   166: aload_0
      //   167: monitorexit
      //   168: return
      //   169: aload_0
      //   170: getfield 93	com/dianping/utn/client/UtnConnection$Session:respChain	Lcom/dianping/utn/ResponseChain;
      //   173: getfield 159	com/dianping/utn/ResponseChain:blocks	[Lcom/dianping/utn/ResponseBlock;
      //   176: arraylength
      //   177: istore_2
      //   178: goto -151 -> 27
      //   181: lload 5
      //   183: l2i
      //   184: istore_2
      //   185: goto -80 -> 105
      //   188: astore_1
      //   189: aload_0
      //   190: monitorexit
      //   191: aload_1
      //   192: athrow
      //   193: astore_1
      //   194: goto -28 -> 166
      //
      // Exception table:
      //   from	to	target	type
      //   2	27	188	finally
      //   27	72	188	finally
      //   72	93	188	finally
      //   105	166	188	finally
      //   169	178	188	finally
      //   2	27	193	java/lang/Exception
      //   27	72	193	java/lang/Exception
      //   72	93	193	java/lang/Exception
      //   105	166	193	java/lang/Exception
      //   169	178	193	java/lang/Exception
    }

    void recv(ResponseBlock paramResponseBlock)
    {
      monitorenter;
      while (true)
      {
        long l;
        try
        {
          if (this.status != 1)
            continue;
          this.status = 2;
          this.respChain = new ResponseChain();
          this.ack = new HttpAck(this.req);
          if (this.status != 2)
            continue;
          this.respChain.append(paramResponseBlock);
          this.lastRecvTime = UtnConnection.this.timestamp();
          this.ack.set(paramResponseBlock.index);
          if (!UtnConnection.this.loggable())
            continue;
          UtnConnection.this.log("RECV: " + name() + " " + paramResponseBlock.index + "/" + paramResponseBlock.count + " " + this.ack.bitsString(paramResponseBlock.count));
          if (!this.respChain.isFinished())
            continue;
          ack();
          this.status = 4;
          UtnConnection.this.done(this);
          return;
          l = this.ack.countAll();
          if (l <= this.lastAckBits)
          {
            i = 0;
            if (i == 0)
              continue;
            ack();
            UtnConnection.this.scheduleRun(this, this.recvTimeout);
            continue;
          }
        }
        finally
        {
          monitorexit;
        }
        if (l - this.lastAckBits < 16L)
        {
          int m = 0;
          int k = 0;
          int j = 1;
          while (true)
          {
            i = m;
            if (j > 16)
              break;
            int n = (this.respChain.blocks.length + 1) / (1 << j);
            i = m;
            if (n < 3)
              break;
            k += n;
            i = this.lastAckBits;
            if ((i < k) && (l >= k))
            {
              i = 1;
              break;
            }
            j += 1;
          }
        }
        int i = 1;
      }
    }

    public void run()
    {
      monitorenter;
      while (true)
      {
        try
        {
          if (this.status != 0)
            continue;
          send();
          this.status = 1;
          return;
          if (this.status != 1)
            break label254;
          l = UtnConnection.this.timestamp() + 1L;
          if (l >= this.firstSendTime + this.sendTimeout)
          {
            if (!UtnConnection.this.loggable())
              continue;
            UtnConnection.this.log("SEND TIMEOUT: " + name());
            this.status = -2;
            UtnConnection.this.done(this);
            continue;
          }
        }
        catch (Exception localException)
        {
          this.status = -5;
          UtnConnection.this.done(this);
          if (!UtnConnection.this.loggable())
            continue;
          if (!(localException instanceof SocketException))
            break label386;
          UtnConnection.this.log("SOCK ERR: " + name() + " " + localException.getMessage());
          continue;
        }
        finally
        {
          monitorexit;
        }
        if (UtnConnection.this.timestamp() + 1L < this.lastSendTime + this.sendPeriod)
          continue;
        if (this.sendCount < 4)
        {
          send();
          continue;
        }
        UtnConnection.this.scheduleRun(this, this.sendTimeout - (l - 1L - this.firstSendTime));
        continue;
        label254: if (this.status != 2)
          continue;
        long l = UtnConnection.this.timestamp() + 1L;
        if (l >= this.lastRecvTime + this.recvTimeout)
        {
          if (UtnConnection.this.loggable())
            UtnConnection.this.log("RECV TIMEOUT: " + name());
          this.status = -3;
          UtnConnection.this.done(this);
          continue;
        }
        if ((l < this.lastAckTime + this.ackPeriod) || (this.ack == null) || (this.ack.countAll() <= this.lastAckBits))
          continue;
        ack();
        continue;
        label386: UtnConnection.this.log("O ERR: " + localObject.getClass() + " " + localObject.getMessage());
      }
    }

    void send()
      throws Exception
    {
      monitorenter;
      try
      {
        if (UtnConnection.this.loggable())
          UtnConnection.this.log("SEND: " + name());
        DatagramPacket localDatagramPacket = this.req.pack();
        localDatagramPacket.setSocketAddress(this.server);
        UtnConnection.this.socket.send(localDatagramPacket);
        this.lastSendTime = UtnConnection.this.timestamp();
        if (this.firstSendTime == 0L)
          this.firstSendTime = this.lastSendTime;
        this.sendCount += 1;
        UtnConnection.this.scheduleRun(this, this.sendPeriod);
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
 * Qualified Name:     com.dianping.utn.client.UtnConnection
 * JD-Core Version:    0.6.0
 */
package com.dianping.tunnel;

import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Tunnel
{
  public static final int CODE_BREAK = -152;
  public static final int CODE_EXIT = -150;
  public static final int CODE_SEND_FAIL = -154;
  public static final int CODE_SEND_FULL = -155;
  public static final int CODE_SERVER_ERROR = -160;
  public static final int CODE_SERVER_FULL = -167;
  public static final int CODE_SERVER_NOT_SUPPORTED = -168;
  public static final int CODE_TIMEOUT = -151;
  static final int CONNECT_MAX_FAIL = 5;
  static final int DEFAULT_TIMEOUT = 15000;
  static final int SEND_QUEUE_LIMIT = 16;
  BlackWhiteList blackWhiteList;
  long blackWhiteListTime;
  private final Comparator<TunnelConnection> connComp = new Comparator()
  {
    public int compare(TunnelConnection paramTunnelConnection1, TunnelConnection paramTunnelConnection2)
    {
      return paramTunnelConnection1.rtt() - paramTunnelConnection2.rtt();
    }
  };
  private final ConcurrentHashMap<SocketAddress, Boolean> connectingHosts = new ConcurrentHashMap();
  private final AtomicInteger connectingThreads = new AtomicInteger(0);
  private final ArrayList<TunnelConnection> connections = new ArrayList();
  final ConcurrentHashMap<String, Session> runningSessions = new ConcurrentHashMap();
  private final BlockingQueue<Session> sendQueue = new LinkedBlockingQueue(16);
  private Thread sendThread;

  private void done(Session paramSession)
  {
    this.runningSessions.remove(paramSession.request.id);
    this.sendQueue.remove(paramSession);
    unscheduleRun(paramSession);
    dispatchDone(paramSession);
  }

  public void abort(String paramString)
  {
    paramString = (Session)this.runningSessions.remove(paramString);
    if (paramString != null)
    {
      this.sendQueue.remove(paramString);
      unscheduleRun(paramString);
    }
  }

  protected void addConnection(List<SocketAddress> paramList)
  {
    if (this.connectingThreads.getAndIncrement() < maxConnectingThread());
    synchronized (this.connections)
    {
      if (this.connections.size() >= maxConnectionCount())
      {
        return;
        this.connectingThreads.decrementAndGet();
        return;
      }
      new Thread(new Runnable(new ArrayList(paramList))
      {
        public void run()
        {
          int i = 0;
          Iterator localIterator = this.val$addrs.iterator();
          while (true)
          {
            int k;
            if (localIterator.hasNext())
            {
              ??? = (SocketAddress)localIterator.next();
              k = 0;
              synchronized (Tunnel.this.connections)
              {
                if (Tunnel.this.connections.size() < Tunnel.this.maxConnectionCount());
              }
            }
            label65: synchronized (Tunnel.this.connections)
            {
              if (Tunnel.this.connections.isEmpty())
                Tunnel.this.connections.notifyAll();
              Tunnel.this.connectingThreads.decrementAndGet();
              return;
              Object localObject6 = Tunnel.this.connections.iterator();
              int j;
              while (true)
              {
                j = k;
                if (!((Iterator)localObject6).hasNext())
                  break;
                if (!???.equals(((TunnelConnection)((Iterator)localObject6).next()).socket.getRemoteSocketAddress()))
                  continue;
                j = 1;
              }
              monitorexit;
              if ((j != 0) || (Tunnel.this.connectingHosts.putIfAbsent(???, Boolean.TRUE) != null))
                continue;
              try
              {
                while (true)
                {
                  long l = Tunnel.this.timestamp();
                  ??? = new Socket();
                  ((Socket)???).connect((SocketAddress)???, Tunnel.this.defaultClientTimeout());
                  l = Tunnel.this.timestamp() - l;
                  if (Tunnel.this.loggable())
                    Tunnel.this.log(??? + " connected in " + l + "ms");
                  localObject6 = new TunnelConnection(Tunnel.this, (Socket)???);
                  if ((Tunnel.this.blackWhiteList == null) || (Tunnel.this.timestamp() - Tunnel.this.blackWhiteListTime > 900000L))
                    ((TunnelConnection)localObject6).register();
                  ((TunnelConnection)localObject6).ping();
                  Tunnel.this.onConnectResult((SocketAddress)???, l);
                  j = 0;
                  synchronized (Tunnel.this.connections)
                  {
                    if (Tunnel.this.connections.isEmpty())
                      j = 1;
                    Tunnel.this.connections.add(localObject6);
                    Tunnel.this.connections.notifyAll();
                    if (j != 0)
                      ((TunnelConnection)localObject6).loadbalance();
                    Tunnel.this.connectingHosts.remove(???);
                    break;
                    localObject2 = finally;
                    throw localObject2;
                  }
                }
              }
              catch (Exception localException)
              {
                if (Tunnel.this.loggable())
                  Tunnel.this.log("fail to connect to " + localObject2 + ", " + localException.getClass() + " " + localException.getMessage());
                Tunnel.this.onConnectResult(localObject2, -1L);
                i += 1;
                if (i >= 5)
                {
                  Tunnel.this.connectingHosts.remove(localObject2);
                  break label65;
                }
                Tunnel.this.connectingHosts.remove(localObject2);
                continue;
              }
              finally
              {
                Tunnel.this.connectingHosts.remove(localObject2);
              }
            }
          }
        }
      }
      , "tunnel_conn").start();
      return;
    }
  }

  protected void checkConnections(int paramInt)
  {
    if (paramInt == 0)
      addConnection(getServers());
  }

  public void clearConnections(boolean paramBoolean)
  {
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      synchronized (this.connections)
      {
        localArrayList.addAll(this.connections);
        this.connections.clear();
        prepareConnections();
        if (localArrayList.size() <= 0)
          continue;
        if (paramBoolean)
        {
          scheduleRun(new Runnable(localArrayList)
          {
            public void run()
            {
              Iterator localIterator = this.val$conns.iterator();
              while (localIterator.hasNext())
                ((TunnelConnection)localIterator.next()).close();
            }
          }
          , defaultClientTimeout());
          return;
        }
      }
      ??? = localObject2.iterator();
      while (((Iterator)???).hasNext())
        ((TunnelConnection)((Iterator)???).next()).close();
    }
  }

  public abstract Session createSession(TunnelRequest paramTunnelRequest, Object paramObject);

  public int defaultClientTimeout()
  {
    return 15000;
  }

  public int defaultServerTimeout()
  {
    return 0;
  }

  public abstract void dispatchDone(Session paramSession);

  public void getConnections(List<TunnelConnection> paramList)
  {
    synchronized (this.connections)
    {
      paramList.addAll(this.connections);
      return;
    }
  }

  public String getDpid()
  {
    return "0";
  }

  protected abstract List<SocketAddress> getServers();

  public String getToken()
  {
    return null;
  }

  public String getVersion()
  {
    return "7.2";
  }

  public boolean isBlocked(String paramString)
  {
    BlackWhiteList localBlackWhiteList = this.blackWhiteList;
    if (localBlackWhiteList == null)
      return false;
    return localBlackWhiteList.block(paramString);
  }

  public void log(String paramString)
  {
  }

  public boolean loggable()
  {
    return false;
  }

  protected int maxConnectingThread()
  {
    return 2;
  }

  protected int maxConnectionCount()
  {
    return 5;
  }

  protected void onConnectResult(SocketAddress paramSocketAddress, long paramLong)
  {
  }

  public int pingInterval()
  {
    return 30000;
  }

  void postBroke(TunnelConnection paramTunnelConnection)
  {
    Object localObject2;
    synchronized (this.connections)
    {
      this.connections.remove(paramTunnelConnection);
      ??? = new ArrayList();
      localObject2 = this.runningSessions.entrySet().iterator();
      if (((Iterator)localObject2).hasNext())
        ((ArrayList)???).add(((Map.Entry)((Iterator)localObject2).next()).getValue());
    }
    ??? = ((ArrayList)???).iterator();
    while (((Iterator)???).hasNext())
    {
      localObject2 = (Session)((Iterator)???).next();
      if (((Session)localObject2).connection != paramTunnelConnection)
        continue;
      if (((Session)localObject2).resp == null)
      {
        TunnelResponse localTunnelResponse = new TunnelResponse();
        localTunnelResponse.id = ((Session)localObject2).request.id;
        localTunnelResponse.statusCode = -152;
        ((Session)localObject2).resp = localTunnelResponse;
      }
      done((Session)localObject2);
    }
  }

  void postLoadbalance(TunnelConnection paramTunnelConnection, SocketAddress[] paramArrayOfSocketAddress)
  {
    paramTunnelConnection = new StringBuilder("loadbalance with ");
    int j = paramArrayOfSocketAddress.length;
    int i = 0;
    while (i < j)
    {
      paramTunnelConnection.append(paramArrayOfSocketAddress[i]);
      paramTunnelConnection.append(", ");
      i += 1;
    }
    if (loggable())
      log(paramTunnelConnection.toString());
    j = paramArrayOfSocketAddress.length;
    i = 0;
    while (i < j)
    {
      paramTunnelConnection = paramArrayOfSocketAddress[i];
      ArrayList localArrayList = new ArrayList(1);
      localArrayList.add(paramTunnelConnection);
      addConnection(localArrayList);
      i += 1;
    }
  }

  void postPing(TunnelConnection paramTunnelConnection)
  {
  }

  void postReged(TunnelConnection paramTunnelConnection, BlackWhiteList paramBlackWhiteList)
  {
    this.blackWhiteList = paramBlackWhiteList;
    this.blackWhiteListTime = timestamp();
  }

  void postResponse(TunnelResponse paramTunnelResponse)
  {
    Session localSession = (Session)this.runningSessions.get(paramTunnelResponse.id);
    if (localSession != null)
    {
      localSession.resp = paramTunnelResponse;
      done(localSession);
    }
  }

  public void prepareConnections()
  {
    synchronized (this.connections)
    {
      int i = this.connections.size();
      checkConnections(i);
      return;
    }
  }

  public abstract void scheduleRun(Runnable paramRunnable, long paramLong);

  public void send(TunnelRequest paramTunnelRequest, int paramInt, Object arg3)
  {
    if (paramTunnelRequest.id == null)
      paramTunnelRequest.id = TunnelUtils.generateHttpRequestId();
    Session localSession = createSession(paramTunnelRequest, ???);
    localSession.timeout = paramInt;
    synchronized (this.runningSessions)
    {
      this.runningSessions.put(paramTunnelRequest.id, localSession);
      scheduleRun(localSession, 0L);
      return;
    }
  }

  public long timestamp()
  {
    return System.nanoTime() / 1000000L;
  }

  public abstract void unscheduleRun(Runnable paramRunnable);

  private class SendThread extends Thread
  {
    public SendThread()
    {
      super();
    }

    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: new 27	java/util/ArrayList
      //   3: dup
      //   4: invokespecial 29	java/util/ArrayList:<init>	()V
      //   7: astore_1
      //   8: new 27	java/util/ArrayList
      //   11: dup
      //   12: invokespecial 29	java/util/ArrayList:<init>	()V
      //   15: astore_2
      //   16: lconst_0
      //   17: lstore 8
      //   19: aload_0
      //   20: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   23: invokestatic 33	com/dianping/tunnel/Tunnel:access$300	(Lcom/dianping/tunnel/Tunnel;)Ljava/util/concurrent/BlockingQueue;
      //   26: invokeinterface 39 1 0
      //   31: checkcast 41	com/dianping/tunnel/Tunnel$Session
      //   34: astore_3
      //   35: aload_0
      //   36: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   39: invokestatic 45	com/dianping/tunnel/Tunnel:access$000	(Lcom/dianping/tunnel/Tunnel;)Ljava/util/ArrayList;
      //   42: astore 4
      //   44: aload 4
      //   46: monitorenter
      //   47: aload_1
      //   48: invokevirtual 48	java/util/ArrayList:clear	()V
      //   51: lload 8
      //   53: lstore 10
      //   55: aload_0
      //   56: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   59: invokestatic 45	com/dianping/tunnel/Tunnel:access$000	(Lcom/dianping/tunnel/Tunnel;)Ljava/util/ArrayList;
      //   62: invokevirtual 52	java/util/ArrayList:isEmpty	()Z
      //   65: ifeq +50 -> 115
      //   68: ldc2_w 53
      //   71: lload 8
      //   73: ladd
      //   74: aload_0
      //   75: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   78: invokevirtual 58	com/dianping/tunnel/Tunnel:timestamp	()J
      //   81: lcmp
      //   82: ifge +24 -> 106
      //   85: aload_0
      //   86: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   89: iconst_0
      //   90: invokevirtual 62	com/dianping/tunnel/Tunnel:checkConnections	(I)V
      //   93: aload_0
      //   94: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   97: invokestatic 45	com/dianping/tunnel/Tunnel:access$000	(Lcom/dianping/tunnel/Tunnel;)Ljava/util/ArrayList;
      //   100: ldc2_w 63
      //   103: invokevirtual 70	java/lang/Object:wait	(J)V
      //   106: aload_0
      //   107: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   110: invokevirtual 58	com/dianping/tunnel/Tunnel:timestamp	()J
      //   113: lstore 10
      //   115: aload_1
      //   116: aload_0
      //   117: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   120: invokestatic 45	com/dianping/tunnel/Tunnel:access$000	(Lcom/dianping/tunnel/Tunnel;)Ljava/util/ArrayList;
      //   123: invokevirtual 74	java/util/ArrayList:addAll	(Ljava/util/Collection;)Z
      //   126: pop
      //   127: aload 4
      //   129: monitorexit
      //   130: aload_0
      //   131: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   134: aload_0
      //   135: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   138: invokestatic 45	com/dianping/tunnel/Tunnel:access$000	(Lcom/dianping/tunnel/Tunnel;)Ljava/util/ArrayList;
      //   141: invokevirtual 78	java/util/ArrayList:size	()I
      //   144: invokevirtual 62	com/dianping/tunnel/Tunnel:checkConnections	(I)V
      //   147: aload_1
      //   148: aload_0
      //   149: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   152: invokestatic 82	com/dianping/tunnel/Tunnel:access$600	(Lcom/dianping/tunnel/Tunnel;)Ljava/util/Comparator;
      //   155: invokestatic 88	java/util/Collections:sort	(Ljava/util/List;Ljava/util/Comparator;)V
      //   158: aload_2
      //   159: invokevirtual 48	java/util/ArrayList:clear	()V
      //   162: iconst_0
      //   163: istore 7
      //   165: aload_1
      //   166: invokevirtual 92	java/util/ArrayList:iterator	()Ljava/util/Iterator;
      //   169: astore 4
      //   171: aload 4
      //   173: invokeinterface 97 1 0
      //   178: ifeq +245 -> 423
      //   181: aload 4
      //   183: invokeinterface 100 1 0
      //   188: checkcast 102	com/dianping/tunnel/TunnelConnection
      //   191: astore 5
      //   193: aload 5
      //   195: getfield 106	com/dianping/tunnel/TunnelConnection:lastPingSendTime	J
      //   198: aload 5
      //   200: getfield 109	com/dianping/tunnel/TunnelConnection:lastPingRespTime	J
      //   203: lcmp
      //   204: ifle +88 -> 292
      //   207: aload_0
      //   208: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   211: invokevirtual 112	com/dianping/tunnel/Tunnel:defaultClientTimeout	()I
      //   214: ifle +110 -> 324
      //   217: aload_0
      //   218: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   221: invokevirtual 58	com/dianping/tunnel/Tunnel:timestamp	()J
      //   224: aload 5
      //   226: getfield 106	com/dianping/tunnel/TunnelConnection:lastPingSendTime	J
      //   229: lsub
      //   230: aload_0
      //   231: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   234: invokevirtual 112	com/dianping/tunnel/Tunnel:defaultClientTimeout	()I
      //   237: i2l
      //   238: lcmp
      //   239: iflt +85 -> 324
      //   242: aload 5
      //   244: invokevirtual 115	com/dianping/tunnel/TunnelConnection:close	()V
      //   247: goto -76 -> 171
      //   250: astore_1
      //   251: aload_0
      //   252: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   255: astore_1
      //   256: aload_1
      //   257: monitorenter
      //   258: aload_0
      //   259: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   262: invokestatic 119	com/dianping/tunnel/Tunnel:access$500	(Lcom/dianping/tunnel/Tunnel;)Ljava/lang/Thread;
      //   265: aload_0
      //   266: if_acmpne +12 -> 278
      //   269: aload_0
      //   270: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   273: aconst_null
      //   274: invokestatic 123	com/dianping/tunnel/Tunnel:access$502	(Lcom/dianping/tunnel/Tunnel;Ljava/lang/Thread;)Ljava/lang/Thread;
      //   277: pop
      //   278: aload_1
      //   279: monitorexit
      //   280: return
      //   281: astore_2
      //   282: aload_1
      //   283: monitorexit
      //   284: aload_2
      //   285: athrow
      //   286: astore_1
      //   287: aload 4
      //   289: monitorexit
      //   290: aload_1
      //   291: athrow
      //   292: aload_0
      //   293: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   296: invokevirtual 58	com/dianping/tunnel/Tunnel:timestamp	()J
      //   299: aload 5
      //   301: getfield 106	com/dianping/tunnel/TunnelConnection:lastPingSendTime	J
      //   304: lsub
      //   305: aload_0
      //   306: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   309: invokevirtual 126	com/dianping/tunnel/Tunnel:pingInterval	()I
      //   312: i2l
      //   313: lcmp
      //   314: iflt +10 -> 324
      //   317: aload_2
      //   318: aload 5
      //   320: invokevirtual 130	java/util/ArrayList:add	(Ljava/lang/Object;)Z
      //   323: pop
      //   324: iload 7
      //   326: ifne -155 -> 171
      //   329: aload 5
      //   331: aload_3
      //   332: getfield 134	com/dianping/tunnel/Tunnel$Session:request	Lcom/dianping/tunnel/TunnelRequest;
      //   335: invokevirtual 138	com/dianping/tunnel/TunnelConnection:send	(Lcom/dianping/tunnel/TunnelRequest;)V
      //   338: aload_3
      //   339: aload 5
      //   341: putfield 142	com/dianping/tunnel/Tunnel$Session:connection	Lcom/dianping/tunnel/TunnelConnection;
      //   344: iconst_1
      //   345: istore 7
      //   347: goto -176 -> 171
      //   350: astore 6
      //   352: aload_0
      //   353: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   356: invokevirtual 145	com/dianping/tunnel/Tunnel:loggable	()Z
      //   359: ifeq +56 -> 415
      //   362: aload_0
      //   363: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   366: new 147	java/lang/StringBuilder
      //   369: dup
      //   370: invokespecial 148	java/lang/StringBuilder:<init>	()V
      //   373: ldc 150
      //   375: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   378: aload 5
      //   380: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   383: ldc 159
      //   385: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   388: aload 6
      //   390: invokevirtual 163	java/lang/Object:getClass	()Ljava/lang/Class;
      //   393: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   396: ldc 159
      //   398: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   401: aload 6
      //   403: invokevirtual 167	java/lang/Exception:getMessage	()Ljava/lang/String;
      //   406: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   409: invokevirtual 170	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   412: invokevirtual 173	com/dianping/tunnel/Tunnel:log	(Ljava/lang/String;)V
      //   415: aload 5
      //   417: invokevirtual 115	com/dianping/tunnel/TunnelConnection:close	()V
      //   420: goto -249 -> 171
      //   423: iload 7
      //   425: ifne +52 -> 477
      //   428: new 175	com/dianping/tunnel/TunnelResponse
      //   431: dup
      //   432: invokespecial 176	com/dianping/tunnel/TunnelResponse:<init>	()V
      //   435: astore 4
      //   437: aload 4
      //   439: aload_3
      //   440: getfield 134	com/dianping/tunnel/Tunnel$Session:request	Lcom/dianping/tunnel/TunnelRequest;
      //   443: getfield 182	com/dianping/tunnel/TunnelRequest:id	Ljava/lang/String;
      //   446: putfield 183	com/dianping/tunnel/TunnelResponse:id	Ljava/lang/String;
      //   449: aload_1
      //   450: invokevirtual 52	java/util/ArrayList:isEmpty	()Z
      //   453: ifeq +134 -> 587
      //   456: sipush -150
      //   459: istore 7
      //   461: aload 4
      //   463: iload 7
      //   465: putfield 187	com/dianping/tunnel/TunnelResponse:statusCode	I
      //   468: aload_0
      //   469: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   472: aload 4
      //   474: invokevirtual 191	com/dianping/tunnel/Tunnel:postResponse	(Lcom/dianping/tunnel/TunnelResponse;)V
      //   477: aload_2
      //   478: invokevirtual 92	java/util/ArrayList:iterator	()Ljava/util/Iterator;
      //   481: astore_3
      //   482: lload 10
      //   484: lstore 8
      //   486: aload_3
      //   487: invokeinterface 97 1 0
      //   492: ifeq -473 -> 19
      //   495: aload_3
      //   496: invokeinterface 100 1 0
      //   501: checkcast 102	com/dianping/tunnel/TunnelConnection
      //   504: astore 4
      //   506: aload 4
      //   508: invokevirtual 194	com/dianping/tunnel/TunnelConnection:ping	()V
      //   511: goto -29 -> 482
      //   514: astore 5
      //   516: aload_0
      //   517: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   520: invokevirtual 145	com/dianping/tunnel/Tunnel:loggable	()Z
      //   523: ifeq +56 -> 579
      //   526: aload_0
      //   527: getfield 13	com/dianping/tunnel/Tunnel$SendThread:this$0	Lcom/dianping/tunnel/Tunnel;
      //   530: new 147	java/lang/StringBuilder
      //   533: dup
      //   534: invokespecial 148	java/lang/StringBuilder:<init>	()V
      //   537: ldc 196
      //   539: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   542: aload 4
      //   544: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   547: ldc 198
      //   549: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   552: aload 5
      //   554: invokevirtual 163	java/lang/Object:getClass	()Ljava/lang/Class;
      //   557: invokevirtual 157	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   560: ldc 159
      //   562: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   565: aload 5
      //   567: invokevirtual 167	java/lang/Exception:getMessage	()Ljava/lang/String;
      //   570: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   573: invokevirtual 170	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   576: invokevirtual 173	com/dianping/tunnel/Tunnel:log	(Ljava/lang/String;)V
      //   579: aload 4
      //   581: invokevirtual 115	com/dianping/tunnel/TunnelConnection:close	()V
      //   584: goto -102 -> 482
      //   587: sipush -154
      //   590: istore 7
      //   592: goto -131 -> 461
      //   595: astore 5
      //   597: goto -491 -> 106
      //
      // Exception table:
      //   from	to	target	type
      //   19	35	250	java/lang/InterruptedException
      //   258	278	281	finally
      //   278	280	281	finally
      //   282	284	281	finally
      //   47	51	286	finally
      //   55	93	286	finally
      //   93	106	286	finally
      //   106	115	286	finally
      //   115	130	286	finally
      //   287	290	286	finally
      //   329	344	350	java/lang/Exception
      //   506	511	514	java/lang/Exception
      //   93	106	595	java/lang/InterruptedException
    }
  }

  protected class Session
    implements Runnable
  {
    TunnelConnection connection;
    TunnelRequest request;
    TunnelResponse resp;
    long startTime;
    long timeout;

    protected Session()
    {
    }

    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 33	com/dianping/tunnel/Tunnel$Session:startTime	J
      //   4: lconst_0
      //   5: lcmp
      //   6: ifne +144 -> 150
      //   9: aload_0
      //   10: aload_0
      //   11: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   14: invokevirtual 37	com/dianping/tunnel/Tunnel:timestamp	()J
      //   17: putfield 33	com/dianping/tunnel/Tunnel$Session:startTime	J
      //   20: aload_0
      //   21: getfield 39	com/dianping/tunnel/Tunnel$Session:timeout	J
      //   24: lconst_0
      //   25: lcmp
      //   26: ifle +15 -> 41
      //   29: aload_0
      //   30: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   33: aload_0
      //   34: aload_0
      //   35: getfield 39	com/dianping/tunnel/Tunnel$Session:timeout	J
      //   38: invokevirtual 43	com/dianping/tunnel/Tunnel:scheduleRun	(Ljava/lang/Runnable;J)V
      //   41: aload_0
      //   42: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   45: invokestatic 47	com/dianping/tunnel/Tunnel:access$300	(Lcom/dianping/tunnel/Tunnel;)Ljava/util/concurrent/BlockingQueue;
      //   48: aload_0
      //   49: invokeinterface 53 2 0
      //   54: pop
      //   55: aload_0
      //   56: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   59: astore_1
      //   60: aload_1
      //   61: monitorenter
      //   62: aload_0
      //   63: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   66: invokestatic 57	com/dianping/tunnel/Tunnel:access$500	(Lcom/dianping/tunnel/Tunnel;)Ljava/lang/Thread;
      //   69: ifnonnull +32 -> 101
      //   72: aload_0
      //   73: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   76: new 59	com/dianping/tunnel/Tunnel$SendThread
      //   79: dup
      //   80: aload_0
      //   81: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   84: invokespecial 61	com/dianping/tunnel/Tunnel$SendThread:<init>	(Lcom/dianping/tunnel/Tunnel;)V
      //   87: invokestatic 65	com/dianping/tunnel/Tunnel:access$502	(Lcom/dianping/tunnel/Tunnel;Ljava/lang/Thread;)Ljava/lang/Thread;
      //   90: pop
      //   91: aload_0
      //   92: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   95: invokestatic 57	com/dianping/tunnel/Tunnel:access$500	(Lcom/dianping/tunnel/Tunnel;)Ljava/lang/Thread;
      //   98: invokevirtual 70	java/lang/Thread:start	()V
      //   101: aload_1
      //   102: monitorexit
      //   103: return
      //   104: astore_1
      //   105: new 72	com/dianping/tunnel/TunnelResponse
      //   108: dup
      //   109: invokespecial 73	com/dianping/tunnel/TunnelResponse:<init>	()V
      //   112: astore_1
      //   113: aload_1
      //   114: aload_0
      //   115: getfield 75	com/dianping/tunnel/Tunnel$Session:request	Lcom/dianping/tunnel/TunnelRequest;
      //   118: getfield 81	com/dianping/tunnel/TunnelRequest:id	Ljava/lang/String;
      //   121: putfield 82	com/dianping/tunnel/TunnelResponse:id	Ljava/lang/String;
      //   124: aload_1
      //   125: sipush -155
      //   128: putfield 86	com/dianping/tunnel/TunnelResponse:statusCode	I
      //   131: aload_0
      //   132: aload_1
      //   133: putfield 88	com/dianping/tunnel/Tunnel$Session:resp	Lcom/dianping/tunnel/TunnelResponse;
      //   136: aload_0
      //   137: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   140: aload_0
      //   141: invokestatic 92	com/dianping/tunnel/Tunnel:access$400	(Lcom/dianping/tunnel/Tunnel;Lcom/dianping/tunnel/Tunnel$Session;)V
      //   144: return
      //   145: astore_2
      //   146: aload_1
      //   147: monitorexit
      //   148: aload_2
      //   149: athrow
      //   150: aload_0
      //   151: getfield 39	com/dianping/tunnel/Tunnel$Session:timeout	J
      //   154: lconst_0
      //   155: lcmp
      //   156: ifle -53 -> 103
      //   159: aload_0
      //   160: getfield 88	com/dianping/tunnel/Tunnel$Session:resp	Lcom/dianping/tunnel/TunnelResponse;
      //   163: ifnonnull -60 -> 103
      //   166: aload_0
      //   167: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   170: getfield 96	com/dianping/tunnel/Tunnel:runningSessions	Ljava/util/concurrent/ConcurrentHashMap;
      //   173: aload_0
      //   174: getfield 75	com/dianping/tunnel/Tunnel$Session:request	Lcom/dianping/tunnel/TunnelRequest;
      //   177: getfield 81	com/dianping/tunnel/TunnelRequest:id	Ljava/lang/String;
      //   180: invokevirtual 102	java/util/concurrent/ConcurrentHashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
      //   183: aload_0
      //   184: if_acmpne -81 -> 103
      //   187: aload_0
      //   188: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   191: invokevirtual 37	com/dianping/tunnel/Tunnel:timestamp	()J
      //   194: aload_0
      //   195: getfield 33	com/dianping/tunnel/Tunnel$Session:startTime	J
      //   198: lsub
      //   199: lconst_1
      //   200: ladd
      //   201: aload_0
      //   202: getfield 39	com/dianping/tunnel/Tunnel$Session:timeout	J
      //   205: lcmp
      //   206: iflt -103 -> 103
      //   209: aload_0
      //   210: new 72	com/dianping/tunnel/TunnelResponse
      //   213: dup
      //   214: invokespecial 73	com/dianping/tunnel/TunnelResponse:<init>	()V
      //   217: putfield 88	com/dianping/tunnel/Tunnel$Session:resp	Lcom/dianping/tunnel/TunnelResponse;
      //   220: aload_0
      //   221: getfield 88	com/dianping/tunnel/Tunnel$Session:resp	Lcom/dianping/tunnel/TunnelResponse;
      //   224: aload_0
      //   225: getfield 75	com/dianping/tunnel/Tunnel$Session:request	Lcom/dianping/tunnel/TunnelRequest;
      //   228: getfield 81	com/dianping/tunnel/TunnelRequest:id	Ljava/lang/String;
      //   231: putfield 82	com/dianping/tunnel/TunnelResponse:id	Ljava/lang/String;
      //   234: aload_0
      //   235: getfield 88	com/dianping/tunnel/Tunnel$Session:resp	Lcom/dianping/tunnel/TunnelResponse;
      //   238: sipush -151
      //   241: putfield 86	com/dianping/tunnel/TunnelResponse:statusCode	I
      //   244: aload_0
      //   245: getfield 24	com/dianping/tunnel/Tunnel$Session:this$0	Lcom/dianping/tunnel/Tunnel;
      //   248: aload_0
      //   249: invokestatic 92	com/dianping/tunnel/Tunnel:access$400	(Lcom/dianping/tunnel/Tunnel;Lcom/dianping/tunnel/Tunnel$Session;)V
      //   252: return
      //
      // Exception table:
      //   from	to	target	type
      //   41	55	104	java/lang/Exception
      //   62	101	145	finally
      //   101	103	145	finally
      //   146	148	145	finally
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tunnel.Tunnel
 * JD-Core Version:    0.6.0
 */
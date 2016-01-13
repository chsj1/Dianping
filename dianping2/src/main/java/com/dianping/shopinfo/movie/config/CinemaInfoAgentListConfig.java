package com.dianping.shopinfo.movie.config;

import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.shopinfo.common.AddressAgent;
import com.dianping.shopinfo.common.PhoneAgent;
import com.dianping.shopinfo.common.TopAgent;
import com.dianping.shopinfo.movie.agent.CinemaServiceAgent;
import java.util.HashMap;
import java.util.Map;

public class CinemaInfoAgentListConfig
  implements AgentListConfig
{
  public Map<String, AgentInfo> getAgentInfoList()
  {
    return null;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("cinemainfo/cinemahead", TopAgent.class);
    localHashMap.put("cinemainfo/cinemaaddress", AddressAgent.class);
    localHashMap.put("cinemainfo/cinemaphone", PhoneAgent.class);
    localHashMap.put("cinemainfo/cinemaservice", CinemaServiceAgent.class);
    return localHashMap;
  }

  public boolean shouldShow()
  {
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.movie.config.CinemaInfoAgentListConfig
 * JD-Core Version:    0.6.0
 */
package com.dianping.base.ugc.utils;

import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.util.Log;
import com.dianping.util.encrypt.Md5;
import java.io.File;

public class UGCBaseDraftManager
{
  public static final String ACTION_DRAFT_ITEM_ADDED = "com.dianping.action.draftitem.added";
  public static final String ACTION_DRAFT_ITEM_REMOVED = "com.dianping.action.draftitem.removed";
  protected static final String[] DRAFT_TYPES;
  protected static final String DRAFT_TYPE_REVIEW = "review";
  protected static final String DRAFT_TYPE_UPLOADCOMMUNITYPHOTO = "uploadcommunityphoto";
  protected static final String DRAFT_TYPE_UPLOADPHOTO = "uploadphoto";
  public static final String TAG = "UGCBaseDraftManager";
  private static final String VERSION = "102";
  private static final File rootFolder = new File(DPApplication.instance().getFilesDir(), "ugcdraft102");
  protected File userDraftFolder = null;
  private int userid = 0;

  static
  {
    if (!rootFolder.exists())
      rootFolder.mkdirs();
    DRAFT_TYPES = new String[] { "review", "uploadphoto", "uploadcommunityphoto" };
  }

  protected static String generateDraftFileName(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("filename:");
    if (paramString1 != null);
    for (String str = paramString1 + "-" + paramString2; ; str = paramString2)
    {
      Log.d("UGCBaseDraftManager", str);
      str = paramString2;
      if (paramString1 != null)
        str = paramString1 + "-" + paramString2;
      return str;
    }
  }

  public static UGCBaseDraftManager getInstance()
  {
    return UGCBaseDraftManager.UGCBaseDraftManagerInner.access$000();
  }

  private void initUser()
  {
    this.userid = DPApplication.instance().accountService().id();
    if (this.userid != 0)
      this.userDraftFolder = new File(rootFolder, Md5.md5(String.valueOf(this.userid)));
  }

  public Integer getDraftCount()
  {
    if (!isValidUser());
    int j;
    do
    {
      return null;
      j = 0;
      String[] arrayOfString = DRAFT_TYPES;
      int m = arrayOfString.length;
      int i = 0;
      while (i < m)
      {
        Object localObject = arrayOfString[i];
        localObject = new File(this.userDraftFolder, (String)localObject);
        int k = j;
        if (((File)localObject).exists())
        {
          k = j;
          if (((File)localObject).isDirectory())
          {
            localObject = ((File)localObject).listFiles();
            k = j;
            if (localObject != null)
              k = j + localObject.length;
          }
        }
        i += 1;
        j = k;
      }
    }
    while (j <= 0);
    return (Integer)Integer.valueOf(j);
  }

  protected boolean isValidUser()
  {
    initUser();
    return this.userid != 0;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.ugc.utils.UGCBaseDraftManager
 * JD-Core Version:    0.6.0
 */
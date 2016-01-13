package com.dianping.ugc.draft;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.dianping.app.DPApplication;
import com.dianping.base.ugc.utils.UGCBaseDraftManager;
import com.dianping.ugc.model.UGCContentItem;
import com.dianping.ugc.model.UGCReviewItem;
import com.dianping.ugc.model.UGCUploadCommunityPhotoItem;
import com.dianping.ugc.model.UGCUploadPhotoItem;
import com.dianping.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UGCDraftManager extends UGCBaseDraftManager
{
  public static final String TAG = "UGCDraftManager";

  public static UGCDraftManager getInstance()
  {
    return UGCDraftManager.UGCDraftManagerInner.access$100();
  }

  public boolean addDraft(UGCContentItem paramUGCContentItem)
  {
    int i = 0;
    if (!isValidUser());
    while (true)
    {
      return i;
      if (paramUGCContentItem == null)
        continue;
      Object localObject = new File(this.userDraftFolder, paramUGCContentItem.getType());
      if (!((File)localObject).exists())
        ((File)localObject).mkdirs();
      localObject = new File((File)localObject, generateDraftFileName(paramUGCContentItem.draftId, paramUGCContentItem.id));
      if (!((File)localObject).exists());
      try
      {
        ((File)localObject).createNewFile();
        paramUGCContentItem.updateEditTime();
        boolean bool = FileUtils.put((File)localObject, paramUGCContentItem);
        i = bool;
        if (!bool)
          continue;
        localObject = new Intent("com.dianping.action.draftitem.added");
        ((Intent)localObject).putExtra("item", paramUGCContentItem);
        LocalBroadcastManager.getInstance(DPApplication.instance()).sendBroadcast((Intent)localObject);
        return bool;
      }
      catch (IOException localIOException)
      {
        while (true)
          localIOException.printStackTrace();
      }
    }
  }

  public List<UGCContentItem> getAllDrafts()
  {
    if (!isValidUser())
      return null;
    ArrayList localArrayList = new ArrayList();
    String[] arrayOfString = DRAFT_TYPES;
    int k = arrayOfString.length;
    int i = 0;
    while (i < k)
    {
      String str = arrayOfString[i];
      Object localObject = new File(this.userDraftFolder, str);
      if ((((File)localObject).exists()) && (((File)localObject).isDirectory()))
      {
        File[] arrayOfFile = ((File)localObject).listFiles();
        if (arrayOfFile != null)
        {
          int m = arrayOfFile.length;
          int j = 0;
          if (j < m)
          {
            File localFile = arrayOfFile[j];
            localObject = null;
            if ("review".equals(str))
              localObject = (UGCContentItem)FileUtils.getParcelable(localFile, UGCReviewItem.CREATOR);
            while (true)
            {
              if (localObject != null)
                localArrayList.add(localObject);
              j += 1;
              break;
              if ("uploadphoto".equals(str))
              {
                localObject = (UGCContentItem)FileUtils.getParcelable(localFile, UGCUploadPhotoItem.CREATOR);
                continue;
              }
              if (!"uploadcommunityphoto".equals(str))
                continue;
              localObject = (UGCContentItem)FileUtils.getParcelable(localFile, UGCUploadCommunityPhotoItem.CREATOR);
            }
          }
        }
      }
      i += 1;
    }
    Collections.sort(localArrayList);
    return (List<UGCContentItem>)localArrayList;
  }

  public ArrayList<UGCContentItem> getDraftsById(String paramString1, String paramString2)
  {
    return getDraftsById(paramString1, paramString2, true);
  }

  public ArrayList<UGCContentItem> getDraftsById(String paramString1, String paramString2, boolean paramBoolean)
  {
    if (!isValidUser())
      return null;
    ArrayList localArrayList = new ArrayList();
    Object localObject = new File(this.userDraftFolder, paramString1);
    if ((((File)localObject).exists()) && (((File)localObject).isDirectory()))
    {
      File[] arrayOfFile = ((File)localObject).listFiles();
      if (arrayOfFile != null)
      {
        int j = arrayOfFile.length;
        int i = 0;
        if (i < j)
        {
          File localFile = arrayOfFile[i];
          if (localFile.getName().startsWith(paramString2))
          {
            localObject = null;
            if (!"review".equals(paramString1))
              break label157;
            localObject = (UGCContentItem)FileUtils.getParcelable(localFile, UGCReviewItem.CREATOR);
          }
          while (true)
          {
            if ((localObject != null) && ((paramBoolean) || ((!paramBoolean) && (((UGCContentItem)localObject).isEditable()))))
              localArrayList.add(localObject);
            i += 1;
            break;
            label157: if ("uploadphoto".equals(paramString1))
            {
              localObject = (UGCContentItem)FileUtils.getParcelable(localFile, UGCUploadPhotoItem.CREATOR);
              continue;
            }
            if (!"uploadcommunityphoto".equals(paramString1))
              continue;
            localObject = (UGCContentItem)FileUtils.getParcelable(localFile, UGCUploadCommunityPhotoItem.CREATOR);
          }
        }
      }
    }
    Collections.sort(localArrayList);
    return (ArrayList<UGCContentItem>)localArrayList;
  }

  public boolean removeDraft(UGCContentItem paramUGCContentItem)
  {
    return removeDraft(paramUGCContentItem, false);
  }

  public boolean removeDraft(UGCContentItem paramUGCContentItem, boolean paramBoolean)
  {
    boolean bool = false;
    int i = 0;
    if (!isValidUser());
    do
    {
      do
        return i;
      while (paramUGCContentItem == null);
      localObject = new File(new File(this.userDraftFolder, paramUGCContentItem.getType()), generateDraftFileName(paramUGCContentItem.draftId, paramUGCContentItem.id));
      if (((File)localObject).exists())
        bool = ((File)localObject).delete();
      i = bool;
    }
    while (!paramBoolean);
    Object localObject = new Intent("com.dianping.action.draftitem.removed");
    ((Intent)localObject).putExtra("id", paramUGCContentItem.id);
    LocalBroadcastManager.getInstance(DPApplication.instance()).sendBroadcast((Intent)localObject);
    return bool;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.draft.UGCDraftManager
 * JD-Core Version:    0.6.0
 */
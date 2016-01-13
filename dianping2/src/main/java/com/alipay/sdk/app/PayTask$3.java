package com.alipay.sdk.app;

import android.os.Handler;
import com.alipay.sdk.util.FileDownloader.IDownloadProgress;
import com.alipay.sdk.widget.Loading;

class PayTask$3
  implements FileDownloader.IDownloadProgress
{
  public final void a()
  {
    this.a.c();
    PayTask.e(this.b).removeCallbacks(PayTask.d(this.b));
    PayTask.f(this.b);
  }

  public final void b()
  {
  }

  public final void c()
  {
    this.a.c();
    PayTask.e(this.b).post(PayTask.d(this.b));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.app.PayTask.3
 * JD-Core Version:    0.6.0
 */
package com.google.zxing.client.result;

public enum ParsedResultType
{
  static
  {
    TEXT = new ParsedResultType("TEXT", 4);
    GEO = new ParsedResultType("GEO", 5);
    TEL = new ParsedResultType("TEL", 6);
    SMS = new ParsedResultType("SMS", 7);
    CALENDAR = new ParsedResultType("CALENDAR", 8);
    WIFI = new ParsedResultType("WIFI", 9);
    ISBN = new ParsedResultType("ISBN", 10);
    VIN = new ParsedResultType("VIN", 11);
    $VALUES = new ParsedResultType[] { ADDRESSBOOK, EMAIL_ADDRESS, PRODUCT, URI, TEXT, GEO, TEL, SMS, CALENDAR, WIFI, ISBN, VIN };
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.result.ParsedResultType
 * JD-Core Version:    0.6.0
 */
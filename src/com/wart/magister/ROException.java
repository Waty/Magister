package com.wart.magister;

public class ROException extends Exception
{

  public ROException()
  {
    super("ROException");
  }

  public ROException(String paramString)
  {
    super(paramString);
  }

  public ROException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }

  public ROException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}
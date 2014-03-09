package de.tum.pssif.sysml4mechatronics.sfb768;

public class SFB768Exception extends RuntimeException {

  private static final long serialVersionUID = -600065388454020295L;

  public SFB768Exception(String msg) {
    super(msg);
  }

  public SFB768Exception(String msg, Throwable rootCouase) {
    super(msg, rootCouase);
  }

}

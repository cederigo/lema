package ch.unibe.lema;

import android.util.AndroidException;

public class ILMException extends AndroidException {

  private static final long serialVersionUID = -2211400708550607009L;

  public ILMException(String name) {
    super(name);
  }

  public ILMException(Exception cause) {
    super(cause);
  }

}

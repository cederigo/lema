package ch.unibe.lema;

import android.util.AndroidException;

public class LemaException extends AndroidException {

    private static final long serialVersionUID = -2211400708550607009L;

    public LemaException(String name) {
        super(name);
    }

    public LemaException(Exception cause) {
        super(cause);
    }

}

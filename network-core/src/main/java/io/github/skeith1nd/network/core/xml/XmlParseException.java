package io.github.skeith1nd.network.core.xml;

/**
 *
 */
public class XmlParseException extends Exception{

    private static final long serialVersionUID = 1906896722521922104L;

    public XmlParseException(String msg, int pos)
    {
        super(msg + " at " + pos);

        this.pos = pos;
    }

    public int GetPos()
    {
        return pos;
    }

    private int pos;

}
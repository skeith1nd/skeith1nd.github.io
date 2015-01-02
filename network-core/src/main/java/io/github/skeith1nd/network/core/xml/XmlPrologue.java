package io.github.skeith1nd.network.core.xml;

/**
 * XML prologue description
 */
public class XmlPrologue {
    /**
     * XML version
     */
    public String version;

    /**
     * Character encoding
     */
    public String encoding;

    @Override
    public String toString() {
        String res = "<?xml version=\""+version+ "\" encoding=\"" + encoding +"\"?>";

        return res;
    }
}
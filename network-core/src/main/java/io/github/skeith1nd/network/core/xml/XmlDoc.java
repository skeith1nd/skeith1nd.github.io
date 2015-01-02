package io.github.skeith1nd.network.core.xml;

/**
 * Main object that describes an XML document
 */
public class XmlDoc {
    /**
     * Has the XML prologue, that is the initial '<?xml' tag with its version
     * and encoding
     */
    public XmlPrologue prologue = new XmlPrologue();

    /**
     * Has the root tag for the XML document
     */
    public XmlTag root = new XmlTag();

    @Override
    public String toString() {
        String res = prologue.toString() + root.toString();

        return res;
    }
}
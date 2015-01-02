package io.github.skeith1nd.network.core.xml;

/**
 * Generic utilities for the XML parser and the JSON converter
 */
public class Utils {

    /**
     * Simple replace string function, String.ReplaceAll uses regular expressions internally and might not
     * transcompile correctly to all platforms
     * @param input
     * @param toReplace
     * @param replacement
     * @return
     */
    public static String ReplaceStr(String input, String toReplace, String replacement)
    {
        String res = input;

        int replacePos = input.indexOf(toReplace);
        while (replacePos != -1)
        {
            res = res.substring(0, replacePos)+ replacement+
                    res.substring(replacePos + toReplace.length());

            replacePos = res.indexOf(toReplace, replacePos + replacement.length());
        }

        return res;
    }

}
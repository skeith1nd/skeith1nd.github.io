package io.github.skeith1nd.core.util;

import playn.core.*;

public class TextUtil {
    public static Image createMessageText(String text, int fontSize, Integer fontColor) {
        Font font = PlayN.graphics().createFont("Helvetica", Font.Style.PLAIN, fontSize);
        TextLayout layout = PlayN.graphics().layoutText(text, new TextFormat().withFont(font).withAntialias(true));
        Image textImage = createTextImage(layout, fontColor);
        return textImage;
    }

    public static Image createTextImage(TextLayout layout, Integer fontColor) {
        CanvasImage image = PlayN.graphics().createImage((int)Math.ceil(layout.width()),
                (int)Math.ceil(layout.height()));
        if (fontColor != null) image.canvas().setFillColor(fontColor);
        image.canvas().fillText(layout, 0, 0);
        return image;
    }
}

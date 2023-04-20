package com.nucleus.floracestore.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class ImageUtils {

    public static BufferedImage cropImg(int targetWidth, int targetHeight, InputStream in) throws IOException {
        BufferedImage originalImage = ImageIO.read(in);
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        // Coordinates of the image's middle
        int xc = (width - targetWidth) / 2;
        int yc = (height - targetHeight) / 2;

        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                xc,
                yc,
                targetWidth, // Width
                targetHeight // Height
        );
        return croppedImage;
    }


public static void storedImageFile(BufferedImage bufferedImageResult, URI location) throws IOException {
    File outputFile = new File(location);

    String formatName = location.toString().substring(
            location.toString().lastIndexOf(".") + 1
    );
    ImageIO.write(
            bufferedImageResult,
            formatName,
            outputFile);
}
}
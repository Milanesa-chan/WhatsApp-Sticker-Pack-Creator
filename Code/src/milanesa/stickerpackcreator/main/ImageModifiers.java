package milanesa.stickerpackcreator.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageModifiers {

    static BufferedImage[] resizeAllImages(BufferedImage[] originalImages){
        BufferedImage[] resizedImages = new BufferedImage[originalImages.length];

        for(int imageIndex=0; imageIndex<resizedImages.length; imageIndex++){
            resizedImages[imageIndex] = resizeImage(originalImages[imageIndex], false);
            System.out.println("[resizeAllImages] Images resized: "+(imageIndex+1)+"/"+originalImages.length);
        }

        return resizedImages;
    }

    static BufferedImage resizeTray(BufferedImage originalTrayImage){
        return resizeImage(originalTrayImage, true);
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, boolean isTray){
        BufferedImage resizedImage;
        Dimension boundary;

        if(isTray){
            resizedImage = new BufferedImage(96, 96, BufferedImage.TYPE_INT_ARGB);
            boundary = new Dimension(96, 96);
        }else{
            resizedImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
            boundary = new Dimension(512, 512);
        }

        Dimension originalDimension = new Dimension(originalImage.getWidth(), originalImage.getHeight());

        Dimension resizedDimension = getNewImageDimensions(
                originalDimension,
                boundary
        );


        Point centeredImagePoint;
        if(isTray){
            centeredImagePoint = new Point(
                    (96-resizedDimension.width)/2,
                    (96-resizedDimension.height)/2
            );
        }else {
            centeredImagePoint = new Point(
                    (512 - resizedDimension.width) / 2,
                    (512 - resizedDimension.height) / 2
            );
        }

        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(
                originalImage,
                centeredImagePoint.x,
                centeredImagePoint.y,
                resizedDimension.width,
                resizedDimension.height,
                null
        );
        g.dispose();
        return resizedImage;
    }

    private static Dimension getNewImageDimensions(Dimension imgSize, Dimension boundary){
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        if (original_width > bound_width) {
            new_width = bound_width;
            new_height = (new_width * original_height) / original_width;
        }

        if (new_height > bound_height) {
            new_height = bound_height;
            new_width = (new_height * original_width) / original_height;
        }

        if(original_width < bound_width){
            if(original_height < bound_height){
                new_height = bound_height;
                new_width = (new_height * original_width) / original_height;
            }
        }

        if(original_height < bound_height){
            if(original_width < bound_width){
                new_height = bound_height;
                new_width = (new_height * original_width) / original_height;
            }
        }

        return new Dimension(new_width, new_height);
    }

    static void convertImagesToWebp(String mainDirPath){
        try{
            Process process = new ProcessBuilder(mainDirPath.concat("\\cwebp.bat")).start();
            process.waitFor();
            System.out.println("[convertImagesToWebp] Images have been converted to webp.");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    static void moveImagesToAssets(String assetsDirPath, String mainDirPath){
        File inputFolder = new File(mainDirPath.concat("/converted"));
        String outputPath = assetsDirPath.concat("/1");

        for(File file : inputFolder.listFiles()){
            file.renameTo(new File(outputPath.concat("/"+file.getName())));
        }

        System.out.println("[moveImagesToAssets] All images moved to assets folder.");
    }

}

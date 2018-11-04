package milanesa.stickerpackcreator.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URLDecoder;
import java.nio.Buffer;

public class Main {
    private static String jarPath;

    private static FilenameFilter imageFilenameFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            if(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")){
                return true;
            }else {
                return false;
            }
        }
    };

    public static void main(String[] args){
        jarPath = getJarPath();
        System.out.println(jarPath);
        outputResizedImages(jarPath, resizeAllImages(getImagesFromFolder(jarPath)));
    }

    private static String getJarPath(){
        try {
            File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String encodedJarPath = jarFile.getParentFile().getAbsolutePath();
            return URLDecoder.decode(encodedJarPath, "UTF-8");
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return "Error";
    }


    private static BufferedImage[] getImagesFromFolder(String mainDirPath) {
        try {
            File inputDir = new File(mainDirPath.concat("/input"));

            if (inputDir.exists() && inputDir.isDirectory()) {
                File[] allImageFiles = inputDir.listFiles(imageFilenameFilter);

                if (allImageFiles.length >= 3 && allImageFiles.length <=30) {
                    BufferedImage[] images = new BufferedImage[allImageFiles.length];
                    for (int fileIndex = 0; fileIndex < allImageFiles.length; fileIndex++) {
                        images[fileIndex] = ImageIO.read(allImageFiles[fileIndex]);
                        System.out.println("[getImagesFromFolder] Loaded image file: " + allImageFiles[fileIndex].getName());
                    }
                    return images;
                } else if (allImageFiles.length > 30){
                    System.out.println("[Error][getImagesFromFolder] Max amount of images is 30. Remove some images!");
                } else if (allImageFiles.length < 3){
                    System.out.println("[Error][getImagesFromFolder] Minimum amount of images is 3. Add some images!");
                } else {
                    System.out.println("[getImagesFromFolder] No valid files found in input folder.");
                    return null;
                }
            } else {
                return null;
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private static BufferedImage[] resizeAllImages(BufferedImage[] originalImages){
        BufferedImage[] resizedImages = new BufferedImage[originalImages.length];

        for(int imageIndex=0; imageIndex<resizedImages.length; imageIndex++){
            resizedImages[imageIndex] = resizeImage(originalImages[imageIndex]);
            System.out.println("[resizeAllImages] Images resized: "+(imageIndex+1)+"/"+originalImages.length);
        }

        return resizedImages;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage){
        BufferedImage resizedImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);

        Dimension originalDimension = new Dimension(originalImage.getWidth(), originalImage.getHeight());

        Dimension resizedDimension = getNewImageDimensions(
                originalDimension,
                new Dimension(512, 512)
        );

        Point centeredImagePoint = new Point(
                (512-resizedDimension.width)/2,
                (512-resizedDimension.height)/2
        );

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

        return new Dimension(new_width, new_height);
    }

    private static void outputResizedImages(String mainDirPath, BufferedImage[] imagesToOutput){
        File outputFolder = new File(mainDirPath.concat("/resized"));

        if(!outputFolder.exists()){
            outputFolder.mkdir();
        }

        try {
            if (outputFolder.isDirectory()) {
                for (int imageIndex = 0; imageIndex < imagesToOutput.length; imageIndex++) {
                    File imageFile = new File(outputFolder.getAbsolutePath().concat("/" + imageIndex + ".png"));
                    ImageIO.write(imagesToOutput[imageIndex], "png", imageFile);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}

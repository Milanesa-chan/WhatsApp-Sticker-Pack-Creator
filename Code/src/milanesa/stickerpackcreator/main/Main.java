package milanesa.stickerpackcreator.main;

import javax.imageio.ImageIO;
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
        getImagesFromFolder(jarPath);
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
                if(inputDir.listFiles().length > 0) {
                    File[] allImageFiles = inputDir.listFiles();
                    BufferedImage[] images = new BufferedImage[allImageFiles.length];
                    for (int fileIndex = 0; fileIndex < allImageFiles.length; fileIndex++) {
                        images[fileIndex] = ImageIO.read(allImageFiles[fileIndex]);
                        System.out.println("[getImagesFromFolder] Loaded image file: " + allImageFiles[fileIndex].getName());
                    }
                    return images;
                }else{
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
}

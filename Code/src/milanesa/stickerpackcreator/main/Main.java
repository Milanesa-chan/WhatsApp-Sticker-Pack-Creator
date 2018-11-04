package milanesa.stickerpackcreator.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLDecoder;

public class Main {
    private static String jarPath;

    public static void main(String[] args){
        jarPath = getJarPath();
        System.out.println(jarPath);
    }

    private static String getJarPath(){
        try {
            File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            return jarFile.getParentFile().getAbsolutePath();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return "Error";
    }


    private static BufferedImage[] getImagesFromFolder(){
        return null;
    }
}

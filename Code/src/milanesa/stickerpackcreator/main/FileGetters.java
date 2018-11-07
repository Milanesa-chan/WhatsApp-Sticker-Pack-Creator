package milanesa.stickerpackcreator.main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class FileGetters {

    private static FilenameFilter imageFilenameFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            if(name.equals("tray.jpg") || name.equals("tray.jpeg") || name.equals("tray.png")) {
                return false;
            }else if(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")){
                return true;
            }else{
                return false;
            }
        }
    };
    private static FilenameFilter trayImageFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            if(name.equals("tray.png") || name.equals("tray.jpg") || name.equals("tray.jpeg")){
                return true;
            }else {
                return false;
            }
        }
    };

    static String getJarPath(){
        try {
            File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String encodedJarPath = jarFile.getParentFile().getAbsolutePath();
            return URLDecoder.decode(encodedJarPath, "UTF-8");
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return "Error";
    }

    static BufferedImage[] getImagesFromFolder(String mainDirPath) {
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
                    Runtime.getRuntime().exit(0);
                } else if (allImageFiles.length < 3){
                    System.out.println("[Error][getImagesFromFolder] Minimum amount of images is 3. Add some images!");
                    Runtime.getRuntime().exit(0);
                } else {
                    System.out.println("[getImagesFromFolder] No valid files found in input folder.");
                    Runtime.getRuntime().exit(0);
                }
            } else {
                return null;
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    static File getModelJson(String mainDirPath){
        String methodId = "[getModelJson]";
        String methodError = "[Error]"+methodId;

        File modelFile = new File(mainDirPath.concat("/model.json"));
        if(modelFile.exists()){
            return modelFile;
        }else{
            System.out.println(methodError+" Couldn't find model json. Reinstall the application or ask the developer.");
            Runtime.getRuntime().exit(1);
            return null;
        }
    }

    static List<String> getModelData(File modelJson){
        try {
            FileReader fileReader = new FileReader(modelJson);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            List<String> data = new ArrayList<>();

            String currentData = bufferedReader.readLine();
            while(currentData != null){
                data.add(currentData);
                currentData = bufferedReader.readLine();
            }

            return data;

        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    static BufferedImage getTrayImage(String mainDirPath){
        try {
            File inputDir = new File(mainDirPath.concat("/input"));
            if(!inputDir.exists() || !inputDir.isDirectory()){
                System.out.println("[Error][getTrayImage] Problem finding input directory. Reinstall de application or ask the developer.");
                Runtime.getRuntime().exit(1);
            }else{
                File[] possibleTrayImages = inputDir.listFiles(trayImageFilter);

                if(possibleTrayImages.length==0){
                    System.out.println("[Error][getTrayImage] No tray image found! Input folder must have an image called \"tray\".");
                    Runtime.getRuntime().exit(1);
                }else if(possibleTrayImages.length>1){
                    System.out.println("[Error][getTrayImage] Multiple tray images found. Input directory must have only one image called \"tray\".");
                    Runtime.getRuntime().exit(1);
                }else{
                    BufferedImage trayImageFile = ImageIO.read(possibleTrayImages[0]);
                    System.out.println("[getTrayImage] Tray image found and loaded.");
                    return trayImageFile;
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}

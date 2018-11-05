package milanesa.stickerpackcreator.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URLDecoder;
import java.nio.Buffer;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static String jarPath, packName, assetsFolderPath, jsonData;

    public static void main(String[] args){
        System.out.println("Developer: Milanesa-chan (@lucc22221)");
        System.out.print("Set a name for your pack: ");
        Scanner inputScanner = new Scanner(System.in);
        packName = inputScanner.next();
        jarPath = FileGetters.getJarPath();
        assetsFolderPath = jarPath.concat("/android/app/src/main/assets");
        checkFolderConditions(jarPath);
        outputResizedImages(jarPath, ImageModifiers.resizeAllImages(FileGetters.getImagesFromFolder(jarPath)));
        ImageModifiers.convertImagesToWebp(jarPath);
        ImageModifiers.moveImagesToAssets(assetsFolderPath, jarPath);
        jsonData = FileGetters.getModelData(FileGetters.getModelJson(jarPath));
        String customJsonData = FileModifiers.customizeDataForPack(jsonData, packName, "asd.jpg", amountOfConvertedImages(jarPath));
        FileModifiers.writeContentsJson(assetsFolderPath, customJsonData);
    }

    private static void checkFolderConditions(String mainDirPath){
        File inputFolder = new File(mainDirPath.concat("/input"));
        File resizedFolder = new File(mainDirPath.concat("/resized"));
        File convertedFolder = new File(mainDirPath.concat("/converted"));

        if(!inputFolder.exists() || !inputFolder.isDirectory()){
            inputFolder.mkdirs();
            System.out.println("[Error][checkFolderConditions] No input folder found!");
            System.out.println("[checkFolderConditions] Created input folder. Put your images there.");
            Runtime.getRuntime().exit(1);
        }

        if(resizedFolder.exists() && resizedFolder.listFiles().length>0){
            for(File file : resizedFolder.listFiles()){
                file.delete();
            }
            System.out.println("[checkFolderConditions] All files in \"resized\" have been deleted.");
        }

        if(!convertedFolder.exists()){
            System.out.println("[checkFolderConditions] Creating \"converted\" folder.");
            convertedFolder.mkdirs();
        }
        if(convertedFolder.exists() && convertedFolder.listFiles().length>0){
            for(File file : convertedFolder.listFiles()){
                file.delete();
            }
            System.out.println("[checkFolderConditions] All files in \"converted\" have been deleted.");
        }

        FileModifiers.prepareAssetsFolder(assetsFolderPath);
    }

    private static void outputResizedImages(String mainDirPath, BufferedImage[] imagesToOutput){
        File outputFolder = new File(mainDirPath.concat("/resized"));

        if(!outputFolder.exists()){
            outputFolder.mkdir();
        }

        try {
            if (outputFolder.isDirectory()) {
                for (int imageIndex = 0; imageIndex < imagesToOutput.length; imageIndex++) {
                    File imageFile = new File(outputFolder.getAbsolutePath().concat("/" + (imageIndex+1) + ".png"));
                    ImageIO.write(imagesToOutput[imageIndex], "png", imageFile);

                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static int amountOfConvertedImages(String mainDirPath){
        File contentsDir = new File(mainDirPath.concat("/converted"));
        return contentsDir.listFiles().length;
    }
}

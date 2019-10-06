package milanesa.stickerpackcreator.main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URLDecoder;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
//WhatsApp Sticker Pack Creator (WASPC).
//Milanesa-chan (@github) made this.

public class Main {
    private static String packName, assetsFolderPath;
    public static String jarPath;
    private static boolean warmupGradle;

    public static void main(String[] args){
        System.out.println("Developer: Milanesa-chan (@DevMilanesa)");

        CheckArguments(args);

        //Initialization, the packName variable holds the name to put later on on the "contents.json" file
        packName = getPackName(args);

        //Get the path of folders accessed frequently. The absolute path of the jar is needed
        //since many instances of this app will be executing at the same time.
        jarPath = FileGetters.getJarPath();
        assetsFolderPath = jarPath.concat("/android/app/src/main/assets");

        //This makes sure every directory needed is created and is empty.
        //Beware everything from the input/resized/converted folders and also the assets folder of the
        //android app will be deleted, so don't put your non-backup'd family photos in there!
        checkFolderConditions(jarPath, false);

        //Sticker images from the input folder are resized and put into "resized". I hope i've been clear they are resized.
        //"amountOfImages" will be used to create the custom "contents.json" further below.
        int amountOfImages = outputResizedImages(jarPath, ImageModifiers.resizeAllImages(FileGetters.getImagesFromFolder(jarPath)));

        //Everything from the resized folder is put into the converted folder in webp format.
        //Don't put your hand in there or else it will be compressed into webp.
        //Worry not, webp is open source, you can get it back.
        ImageModifiers.convertImagesToWebp(jarPath);
        ImageModifiers.moveImagesToAssets(assetsFolderPath, jarPath);

        //The tray image has a different size and is not compressed as the sticker ones,
        //So it has to be done on a different process.
        BufferedImage trayImage = FileGetters.getTrayImage(jarPath);
        trayImage = ImageModifiers.resizeTray(trayImage);
        outputTrayImage(assetsFolderPath, trayImage);

        //We get the data from a model json in the same folder as the jar. No, don't modify the model.
        //Yes I could have put it inside the jar but me don't like that.
        List<String> jsonData = FileGetters.getModelData(FileGetters.getModelJson(jarPath));

        //It's a weird name but the "custom" json is the model json with the data specifically needed for
        //this pack regex'd into it. "amountOfImages" is used here.
        List<String> customJsonData = FileModifiers.customizeDataForPack(jsonData, packName, "tray.png", amountOfImages);
        FileModifiers.writeContentsJson(assetsFolderPath, customJsonData);

        //Start the build of the apk. It uses the gradle "assembleDebug" I plan on making this better in the future,
        //but for now this will work. Also the output apk gets moved into the "output" folder.
        FileModifiers.startGradleBuild(jarPath);
        String apkOutputPath = jarPath.concat("/android/app/build/outputs/apk/debug/app-debug.apk");
        moveApkToOutput(jarPath, apkOutputPath);
        System.out.println("[Main] Process finished.");
    }

    //Receives the execution args. If the args contain "/packname" it will return the string after that.
    //If it doesn't it promps the user to choose the name and returns the sanitized input.
    private static String getPackName(String[] args){
        List<String> argsList = Arrays.asList(args);
        if(argsList.contains("-packname")){
            int indexOfPackName = argsList.indexOf("-packname") + 1;
            String potPackName = argsList.get(indexOfPackName);
            int potPackNameLen = potPackName.length();
            if(potPackNameLen < 3 || potPackNameLen > 32){
                System.out.println("[Error][getPackName] Pack name must be between 3 and 32 characters long.");
                Runtime.getRuntime().exit(1);
                return null;
            }else{
                return potPackName;
            }
        }else{
            System.out.print("Set a name for your pack: ");
            Scanner inputScanner = new Scanner(System.in);
            String potPackName = inputScanner.next();
            int potPackNameLen = potPackName.length();
            while(potPackNameLen < 3 || potPackNameLen > 32){
                System.out.println("Pack name must be between 3 and 32 characters long:");
                potPackName = inputScanner.next();
                potPackNameLen = potPackName.length();
            }
            return potPackName;
        }
    }

    private static void checkFolderConditions(String mainDirPath, boolean isWarmup){
        File inputFolder = new File(mainDirPath.concat("/input"));
        File resizedFolder = new File(mainDirPath.concat("/resized"));
        File convertedFolder = new File(mainDirPath.concat("/converted"));
        File outputFolder = new File(mainDirPath.concat("/output"));
        File logsFolder = new File(mainDirPath.concat("/logs"));

        if(!inputFolder.exists() || !inputFolder.isDirectory() && !isWarmup){
            inputFolder.mkdirs();
            System.out.println("[Error][checkFolderConditions] No input folder found!");
            System.out.println("[checkFolderConditions] Created input folder. Put your images there.");
            Runtime.getRuntime().exit(1);
        }

        if(resizedFolder.exists() && resizedFolder.listFiles().length>0 && !isWarmup){
            for(File file : resizedFolder.listFiles()){
                file.delete();
            }
            System.out.println("[checkFolderConditions] All files in \"resized\" have been deleted.");
        }

        if(!convertedFolder.exists() && !isWarmup){
            System.out.println("[checkFolderConditions] Creating \"converted\" folder.");
            convertedFolder.mkdirs();
        }
        if(convertedFolder.exists() && convertedFolder.listFiles().length>0 && !isWarmup){
            for(File file : convertedFolder.listFiles()){
                file.delete();
            }
            System.out.println("[checkFolderConditions] All files in \"converted\" have been deleted.");
        }

        if(!outputFolder.exists() && !isWarmup){
            System.out.println("[checkFolderConditions] Creating \"output\" folder.");
            outputFolder.mkdirs();
        }
        if(outputFolder.exists() && outputFolder.listFiles().length>0 && !isWarmup){
            for(File file : outputFolder.listFiles()){
                file.delete();
            }
            System.out.println("[checkFolderConditions] All files in \"output\" have been deleted.");
        }

        if(!logsFolder.exists()){
            logsFolder.mkdirs();
        }else{
            if(!logsFolder.isDirectory()){
                System.out.println("[checkFolderConditions] Error when accessing logs directory. Aborting.");
                Runtime.getRuntime().exit(1);
            }else{
                System.out.println("[checkFolderConditions] Cleaning all existing logs...");
                for(File file : logsFolder.listFiles()){
                    file.delete();
                }
                System.out.println("[checkFolderConditions] Logs deleted.");
            }
        }

        if(!isWarmup) FileModifiers.prepareAssetsFolder(assetsFolderPath);
    }

    private static int outputResizedImages(String mainDirPath, BufferedImage[] imagesToOutput){
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
        }finally{
            return imagesToOutput.length;
        }
    }

    private static void outputTrayImage(String assetsFolderPath, BufferedImage trayImage){
        try {
            File outputImageFile = new File(assetsFolderPath.concat("/1/tray.png"));
            ImageIO.write(trayImage, "png", outputImageFile);
            System.out.println("[outputTrayImage] Tray image written into assets folder.");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static void moveApkToOutput(String mainDirPath, String apkOutputPath){
        File apkFile = new File(apkOutputPath);
        apkFile.renameTo(new File(mainDirPath.concat("/output/CustomStickerPack.apk")));
        System.out.println("[moveApkToOutput] Apk moved to output folder. Main process finished successfully!");
    }

    private static void CheckArguments(String[] args){
        List<String> argsList = Arrays.asList(args);

        if(argsList.contains("-warmup")){
            System.out.println("[CheckArguments] Warming up Gradle. This execution will not create a sticker pack.");
            jarPath = FileGetters.getJarPath();
            checkFolderConditions(jarPath, true);
            FileModifiers.startGradleBuild(jarPath);
            System.out.println("[CheckArguments] Gradle warmup finished. Next builds will be faster.");
            Runtime.getRuntime().exit(0);
        }
    }
}

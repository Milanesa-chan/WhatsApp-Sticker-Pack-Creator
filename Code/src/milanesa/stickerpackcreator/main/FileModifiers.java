package milanesa.stickerpackcreator.main;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileModifiers {

    static void prepareAssetsFolder(String assetsFolderPath){
        String methodId = "[prepareAssetsFolder]";
        String methodError = "[Error]"+methodId;

        File assetsFolder = new File(assetsFolderPath);
        if(!assetsFolder.exists()){
            System.out.println(methodError+" Assets folder not found. Reinstall the application or ask the developer.");
            Runtime.getRuntime().exit(1);
        }else{
            if(!assetsFolder.isDirectory()){
                System.out.println(methodError+" Conflict in assets folder. Reinstall the application or ask the developer.");
                Runtime.getRuntime().exit(1);
            }else{
                for(File asset : assetsFolder.listFiles()){
                    if(asset.isDirectory()){
                        for(File subAsset : asset.listFiles()){
                            subAsset.delete();
                        }
                    }else {
                        asset.delete();
                    }
                }
                System.out.println(methodId+" Cleared assets folder.");
                File imageFolder = new File(assetsFolder.getAbsolutePath().concat("/1"));
                imageFolder.mkdirs();
                System.out.println(methodId+" Created image folder. Finished preparing assets folder.");
            }
        }
    }

    static List<String> customizeDataForPack(List<String> modelData, String packName, String trayName, int amountOfImages){
        List<String> customData = new ArrayList<>();
        List<String> customImageFiles = customImageFileLines(amountOfImages);

        for(int line=0; line<modelData.size(); line++){
            String lineData = modelData.get(line);

            if(lineData.contains("[imagefiles]")){
                for(int customLine=0; customLine<customImageFiles.size(); customLine++){
                    customData.add(customImageFiles.get(customLine));
                }
            }else if(lineData.contains("[packname]")) {
                    customData.add(lineData.replaceFirst("\\[packname]", packName));
            }else if(lineData.contains("[trayname]")) {
                customData.add(lineData.replaceFirst("\\[trayname]", trayName));
            }else{
                customData.add(lineData);
            }
        }

        return customData;
    }

    static List<String> customImageFileLines(int amountOfImages){
        List<String> customLines = new ArrayList<>();
        String[] imageFileModel = {"{", "\"image_file\": \"[imagenumber].webp\"", "}"};

        General.addStringArrayIntoList(customLines, General.replaceInArray(imageFileModel, "\\[imagenumber]", "1"));
        for(int imageNum=2; imageNum<=amountOfImages; imageNum++){
            customLines.add(",");
            General.addStringArrayIntoList(customLines, General.replaceInArray(imageFileModel, "\\[imagenumber]", String.valueOf(imageNum)));
        }

        System.out.println("[customImageFileLines] Json data customized.");

        return customLines;
    }

    static void writeContentsJson(String assetsDirPath, List<String> jsonData){
        try {
            File contentsJson = new File(assetsDirPath.concat("/contents.json"));
            contentsJson.createNewFile();
            FileWriter fileWriter = new FileWriter(contentsJson);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for(String lineData : jsonData){
                bufferedWriter.write(lineData);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            System.out.println("[writeContentsJson] Custom Json written.");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    static void startGradleBuild(String mainDirPath){
        try {
            System.out.println("[startGradleBuild] Starting build, this could take some time, go grab some coffee...");
            ProcessBuilder gradlewProcessBuilder = new ProcessBuilder(mainDirPath.concat("\\android\\gradlew.bat"), "assembleDebug");
            gradlewProcessBuilder.directory(new File(mainDirPath.concat("/android")));
            //gradlewProcessBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT); //If I delete this line the process won't finish.
            Process gradlewProcess = gradlewProcessBuilder.start();

            createLoggingThread(mainDirPath, "Gradle", false, gradlewProcess.getInputStream());

            gradlewProcess.waitFor();
            System.out.println("[startGradleBuild] Gradle process finished.");

            if(gradlewProcess.exitValue()!=0){
                System.out.println("[startGradleBuild] BEGIN of gradle error output:");
                BufferedReader errorStreamReader = new BufferedReader(new InputStreamReader(gradlewProcess.getErrorStream()));
                String errorStreamLine = errorStreamReader.readLine();
                while(errorStreamLine != null){
                    System.out.println(errorStreamLine);
                    errorStreamLine = errorStreamReader.readLine();
                }
                errorStreamReader.close();
                System.out.println("[startGradleBuild] END of gradle error output.");
                System.out.println("[startGradleBuild] Gradle build exit code: "+gradlewProcess.exitValue()+" aborting.");
                Runtime.getRuntime().exit(1);
            }else{
                System.out.println("[startGradleBuild] Gradle build finished successfully.");
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    static void createLoggingThread(String mainDirPath, String appToLog, boolean isError, InputStream streamToLog){
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date currentDate = Date.from(Instant.now());
        String logFilePath = mainDirPath.concat("/logs/"+appToLog+" "+ simpleDateFormat.format(currentDate) + ((isError) ? " Error" : "") + ".txt");

        new Thread(() -> {
            try {
                File logFile = new File(logFilePath);
                FileWriter fw = new FileWriter(logFile);
                BufferedWriter writer = new BufferedWriter(fw);
                BufferedReader reader = new BufferedReader(new InputStreamReader(streamToLog));

                String nextLine;
                while((nextLine = reader.readLine()) != null){
                    writer.write(nextLine);
                    writer.newLine();
                }

                writer.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }).start();
    }
}

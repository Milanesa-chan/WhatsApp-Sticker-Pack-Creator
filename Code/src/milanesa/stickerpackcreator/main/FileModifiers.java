package milanesa.stickerpackcreator.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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

    static String customizeDataForPack(String modelData, String packName, String trayName, int amountOfImages){
        String customData = modelData;
        String imageFileModel = "{" +
                "\"image_file\": \"[imagenumber].webp\"" +
                "}";

        customData = customData.replaceFirst("\\[packname]", packName);
        customData = customData.replaceFirst("\\[trayname]", trayName);

        String[] dataArray = customData.split("\\[imagefiles]");

        for(int imageNum=1; imageNum<=amountOfImages; imageNum++){
            dataArray[0] = dataArray[0].concat(imageFileModel.replace("\\[imageNumber]", String.valueOf(imageNum)));
        }

        customData = dataArray[0].concat(dataArray[1]);
        return customData;
    }

    static void writeContentsJson(String assetsDirPath, String jsonData){
        try {
            File contentsJson = new File(assetsDirPath.concat("/contents.json"));
            contentsJson.createNewFile();
            FileWriter fileWriter = new FileWriter(contentsJson);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonData);
            bufferedWriter.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}

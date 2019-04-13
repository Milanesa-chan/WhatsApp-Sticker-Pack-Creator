package milanesa.stickerpackcreator.main;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.List;

public class General {

    static List<String> addStringArrayIntoList(List<String> list, String[] array){
        for(String string : array){
            list.add(string);
        }
        return list;
    }

    static String[] replaceInArray(String[] array, String toReplace, String toPut){
        String[] newArray = new String[array.length];

        for(int index=0; index<array.length; index++){
            newArray[index] = array[index].replaceFirst(toReplace, toPut);
        }
        return newArray;
    }
}

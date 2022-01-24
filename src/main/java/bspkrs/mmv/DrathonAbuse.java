package bspkrs.mmv;

import bspkrs.mmv.gui.MappingGui;

import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created to just override this insane app's bullshit. I don't want to bother messing with this code holy shit.
 */
public class DrathonAbuse {
    public static void convertThatBitch(){
        McpMappingLoader instance = MappingGui.currentLoader;
        // Search Methods
        String header = "addMapping(" + '"';
        String end = '"' + ");";
        StringWriter buf = new StringWriter();
        for (Map.Entry<ClassSrgData, Set<MethodSrgData>> entry : instance.srgFileData.class2MethodDataSet.entrySet())
        {
            String srgclass = entry.getKey().getSrgName();
            if(srgclass.contains("$")) continue;
            for (MethodSrgData methodData : entry.getValue())
            {
                CsvData csv = instance.srgMethodData2CsvData.get(methodData);
                if(csv == null) continue;
                String c1 = '"' + ", " + srgclass + ".class, " + '"';
                buf.append(header).append(csv.getMcpName()).append(c1).append(csv.getSrgName()).append(end).append("\n");
            }
        }

        // Search Fields
        for (Map.Entry<ClassSrgData, Set<FieldSrgData>> entry : instance.srgFileData.class2FieldDataSet.entrySet())
        {
            String srgclass = entry.getKey().getSrgName();
            if(srgclass.contains("$")) continue;
            for (FieldSrgData fieldData : entry.getValue())
            {
                CsvData csv = instance.srgFieldData2CsvData.get(fieldData);
                if(csv == null) continue;
                String c1 = '"' + ", " + srgclass + ".class, " + '"';
                buf.append(header).append(csv.getMcpName()).append(c1).append(csv.getSrgName()).append(end).append("\n");
            }
        }
        System.out.println(buf);
        if(!Files.exists(Paths.get("outputall.txt"))){
            try {
                Files.createFile(Paths.get("outputall.txt"));
            } catch(Exception ignored){}
        }
        try {
            Files.write(Paths.get("outputall.txt"), buf.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch(Exception ignored){}
    }
    public static void convertThatBitch(String classIn){
        if(classIn.isEmpty()) convertThatBitch();
        McpMappingLoader instance = MappingGui.currentLoader;
        // Search Methods
        String header = "addMapping(" + '"';
        String end = '"' + ");";
        StringWriter buf = new StringWriter();
        StringWriter bufcli = new StringWriter();
        for (Map.Entry<ClassSrgData, Set<MethodSrgData>> entry : instance.srgFileData.class2MethodDataSet.entrySet())
        {
            String srgclass = entry.getKey().getSrgName();
            if(srgclass.contains("$")) continue;
            if(!srgclass.equals(classIn)) continue;
            for (MethodSrgData methodData : entry.getValue())
            {
                CsvData csv = instance.srgMethodData2CsvData.get(methodData);
                if(csv == null) continue;
                String c1 = '"' + ", " + srgclass + ".class, " + '"';
                if(csv.getSide() != 0) buf.append(header).append(csv.getMcpName()).append(c1).append(csv.getSrgName()).append(end).append("\n");
                else bufcli.append(header).append(csv.getMcpName()).append(c1).append(csv.getSrgName()).append(end).append("\n");
            }
        }

        // Search Fields
        for (Map.Entry<ClassSrgData, Set<FieldSrgData>> entry : instance.srgFileData.class2FieldDataSet.entrySet())
        {
            String srgclass = entry.getKey().getSrgName();
            if(srgclass.contains("$")) continue;
            if(!srgclass.equals(classIn)) continue;
            for (FieldSrgData fieldData : entry.getValue())
            {
                CsvData csv = instance.srgFieldData2CsvData.get(fieldData);
                if(csv == null) continue;
                String c1 = '"' + ", " + srgclass + ".class, " + '"';
                if(csv.getSide() != 0) buf.append(header).append(csv.getMcpName()).append(c1).append(csv.getSrgName()).append(end).append("\n");
                else bufcli.append(header).append(csv.getMcpName()).append(c1).append(csv.getSrgName()).append(end).append("\n");
            }
        }
        write(buf.toString().getBytes(),bufcli.toString().getBytes());
    }
    public static void write(byte[] bytes, byte[] bytesClient){
        if(!Files.exists(Paths.get("common.txt"))){
            try {
                Files.createFile(Paths.get("common.txt"));
            } catch(Exception ignored){}
        }
        try {
            Files.write(Paths.get("common.txt"), bytes, StandardOpenOption.APPEND);
        } catch(Exception ignored){}

        if(!Files.exists(Paths.get("client.txt"))){
            try {
                Files.createFile(Paths.get("client.txt"));
            } catch(Exception ignored){}
        }
        try {
            Files.write(Paths.get("client.txt"), bytesClient, StandardOpenOption.APPEND);
        } catch(Exception ignored){}
    }

    public static String executeSearchCommand(String input) {
        input = input.replaceFirst("!","");
        if(input.contains(".class")){
            input = input.replace(".class","");
            convertThatBitch(input);
        }
        if(input.contains(".fromCF")){
            input = input.replaceFirst(".fromCF","");
            try {
                Scanner scan = new Scanner(new File("classFile.txt"));
                while (scan.hasNext()) {
                    String cls = scan.next();
                    convertThatBitch(cls);
                }
            } catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return input;
    }

}

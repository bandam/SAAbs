package com.amirab.layerdetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileWalker {

    ArrayList<Class> classes;

    public FileWalker() {
        classes = new ArrayList<>();
    }

    public ArrayList<Class> getData(String path) throws IOException {
        walk(path);
        //System.out.println(classes.size());
        //System.out.println(classes.get(28).getImports().get(1));
        return classes;
    }

    // Recursively go through file
    public void walk(String path) throws IOException {
        File root = new File(path);
        File[] list = root.listFiles();
        Class currentClass = new Class();

        if (list == null) {
            return;
        }

        // Iterate through the folder structure if its not empty
        for (File f : list) {
            if (f.isDirectory()) {
                walk(f.getAbsolutePath());
                //System.out.println( "Dir:" + f.getAbsoluteFile() );
            } else {

                if (isJavaFile(f.getName())) {
                    currentClass.setName(/*new File(f.getParent()).getName() + "/" + */ f.getName().replace(".java", ""));
                    readFile(currentClass, f.getAbsolutePath());
                    classes.add(currentClass);
                    currentClass = new Class();
                }
            }
        }

    }

    public boolean isJavaFile(String path) {
        String extension = "";

        int i = path.lastIndexOf('.');
        if (i >= 0) {
            extension = path.substring(i + 1);
        }
        
        if(extension.equals("java")){
            //System.out.println(extension);
            return true;
        }
        
        //System.out.println(extension);
        return false;
    }

//    public static void main(String[] args) throws IOException {
//        FileWalker fw = new FileWalker();
//        fw.getData("C:\\Users\\Andam\\Desktop\\Projects\\SideProjects\\Android\\ContainerHub\\src");
//    }
    // Read a class file and save imports to current class
    public void readFile(Class currentClass, String s) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            String line = br.readLine();

            while (line != null) {
                String keyword = getKeyWordInLine(line);
                //System.out.println(keyword);
                if (isImport(keyword)) {
                    currentClass.getImports().add(line);
                }

                if (isEndImports(keyword)) {
                    //System.out.println("End of classfile!! \t" + keyword);

                    break;
                }

                line = br.readLine();
            }

            //System.out.println(currentClass.getName());
            //System.out.println(currentClass.getImports().size() + "\n");
        }
    }

    // Check if  a line is an import
    public boolean isImport(String keyword) {
        return keyword.equals("import");
    }

    public boolean isEndImports(String keyword) {
        return (keyword.equals("public")) || (keyword.equals("private")) || (keyword.equals("class"));
    }

    public String getKeyWordInLine(String line) {
        String[] lineParts = line.split(" ");
        //System.out.println(lineParts.length);
        if (lineParts.length != 0) {
            return lineParts[0];
        }
        return " ";
    }
}

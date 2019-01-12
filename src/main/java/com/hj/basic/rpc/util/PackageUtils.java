package com.hj.basic.rpc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * @author tangj
 * @description
 * @since 2019/1/12 11:06
 */
@Slf4j
public class PackageUtils {

    private static final String FILE = "file";
    private static final String JAR = "jar";
    private static final String END_CLASS = ".class";

    public static List<Class> scanPackage(String packageName) {
        List<Class> classes = new ArrayList<>();
        String packageDirName = packageName.replace(".", "/");

        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();

                if (FILE.equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    int index = packageName.lastIndexOf(".");
                    String substring = packageName.substring(0, index);

                    doScanFile(new File(filePath), classes, substring);
                } else if (JAR.equals(protocol)) {
                    log.info("this is a jar.url:{}", url);
                }
            }
        } catch (IOException e) {
            log.info("packageDirName:{} is not a resource url", packageDirName);
            e.printStackTrace();
        }

        return classes;
    }

    private static void doScanFile(File rootFile, List<Class> classes, String packageName) {
        if (Objects.isNull(rootFile)) {
            return;
        }

        if (rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            packageName = packageName + "." + rootFile.getName();
            for (File file : files) {
                doScanFile(file, classes, packageName);
            }
        } else if (rootFile.isFile() && rootFile.getName().endsWith(END_CLASS)) {
            String className = rootFile.getName().replaceAll(END_CLASS, "");
            try {
                String packageClassName = packageName + '.' + className;
                classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageClassName));
            } catch (ClassNotFoundException e) {
                log.error("class not find.className:{}", className);
                e.printStackTrace();
            }
        }
    }
}

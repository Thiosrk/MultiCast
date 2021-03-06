package com.MultiCast.util;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultipartFileUtil {

    private static final String PATH = "tmp";

    private static List<File> fileList = new ArrayList<>();

    public static void toFiles(MultipartFile[] multipartFiles) {
        for (MultipartFile multipartFile : multipartFiles) {
            String name = PATH + "/" + multipartFile.getOriginalFilename();
            File file = new File(name);

            try {
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileList.add(file);
        }
    }

    public static List<File> getFileList() {
        return fileList;
    }

    public static void empty() {
        fileList = new ArrayList<>();
        File file = new File(PATH);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

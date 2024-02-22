package id.ineal.util.File;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import id.ineal.util.File.FileOperation.setFileFilter;

public class FileFunction {
    
    /*===========================
    *			FIND
    *============================*/
    static List<String> doFind(Path path, String opt, boolean recursive) {
        List<String> out = new ArrayList<>();
        Map<String,String> map = (opt != null) ? _File.getOption(opt) : null;
        
        File directory = new File(path.toString());
        if (directory.exists() && directory.isDirectory()) {
            if (recursive) {
                doFindInnerRecursive(directory, map, out);
            } else {
                doFindInner(directory, map, out);
            }
        }
        return out;
    }

    private static void doFindInner(File direktori, Map<String, String> map, List<String> out) {
        File[] files = (map != null) ? direktori.listFiles(new setFileFilter(map)) : direktori.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    out.add(file.getAbsolutePath().replace("\\", "/"));
                }
            }
        }
    }

    private static void doFindInnerRecursive(File direktori, Map<String, String> map, List<String> out) {
        File[] files = (map != null) ? direktori.listFiles(new setFileFilter(map)) : direktori.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    out.add(file.getAbsolutePath().replace("\\","/"));
                } else if (file.isDirectory()) {
                    doFindInnerRecursive(file, map, out);
                }
            }
        }
    }

    /*===========================
    *			WATCH
    *============================*/
    @SuppressWarnings("unchecked")
    static <T> T watchFile(String path,String opt,Class<?> ret) {
        Map<String, String> map = null;
        if(opt != null) {
            map = _File.getOption(opt);
        }
        File directory = new File(path);
        File[] files =(opt != null) ? directory.listFiles(new setFileFilter(map)) : directory.listFiles();
        StringBuilder builder = new StringBuilder();
        List<String> listResult = new ArrayList<>();
        
        if(files != null && files.length > 0) {
            for (File file : files) {
                builder.append(file.getName());
                builder.append(System.lineSeparator());
            }
            if(ret.isAssignableFrom(String.class)){
                return (T) builder.toString();
            } else {
                if (builder.length() > 0) {
                    listResult = Arrays.asList(builder.toString().split("\n"));
                }
                return (T)(List<String>)listResult;
            }
        }
        return null;
    }

    /*====================
     *      Zip
     *=====================*/
    @SuppressWarnings("unchecked")
    static void zip(Object src, Path output) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(output.toString()));
        if(src instanceof String) {
            File f = new File(src.toString());
            zipString(f, f, zipOut);
        } else {
            List<String> sources = (List<String>) src;
            for (String srcFile : sources) {
                File fileToZip = new File(srcFile);
                zipList(fileToZip, fileToZip.getName(), zipOut);
            }
        }
        zipOut.close();
    }

    private static void zipString(File rootDir, File dir, ZipOutputStream zipOut) throws IOException {
        byte[] tmpBuf = new byte[1024];

        if(!dir.isFile()) {
            File[] files = dir.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    zipString(rootDir, files[i], zipOut);
                    continue;
                }
                FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
                String relativePath = files[i].getAbsolutePath().substring(rootDir.getAbsolutePath().length() + 1);
                
                zipOut.putNextEntry(new ZipEntry(relativePath));
                int len;
                while ((len = in.read(tmpBuf)) > 0) {
                    zipOut.write(tmpBuf, 0, len);
                }
                zipOut.closeEntry();
                in.close();
            }
        } else {
            FileInputStream in = new FileInputStream(dir.getAbsolutePath());
            String relativePath = dir.getAbsolutePath().substring(rootDir.getParentFile().getAbsolutePath().length() + 1);

            zipOut.putNextEntry(new ZipEntry(relativePath));
            int len;
            while ((len = in.read(tmpBuf)) > 0) {
                zipOut.write(tmpBuf, 0, len);
            }
            zipOut.closeEntry();
            in.close();
        }
    }

    private static void zipList(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            // Membuat entri direktori
            if (!fileName.endsWith("/")) {
                fileName += "/";
            }
            zipOut.putNextEntry(new ZipEntry(fileName));
    
            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    zipList(childFile, fileName + childFile.getName(), zipOut);
                }
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
    /*====================
     *      Unzip
     *=====================*/
    static void unzip(String src, String dest) throws IOException {
        File destDir = new File(dest);
    
        // Validasi direktori tujuan
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(src))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = unzipThis(destDir, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Gagal Membuat Directory " + newFile);
                    }
                } else {
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Gagal Membuat Directory " + parent);
                    }
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
        }
    }
    
    private static File unzipThis(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
    
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
    
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Data diluar directory target : " + zipEntry.getName());
        }
    
        return destFile;
    }
}

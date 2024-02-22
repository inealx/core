package id.ineal.util.File;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import id.ineal.util.__;

public class FileOperation {

    /*=================================/
    *           CREATE
    *================================*/
    static File create(Path filePath,Object content,String mode){
        File f = new File(filePath.toString());
        if(!f.getName().contains(".")) {
            if(!f.exists()) {
                f.mkdirs();
            }
        } else {
            if(content == null) {
                return createFile(f);
            } else {
                return createFile(f, content,mode);
            }
        }
        return f;
    }

    static File createFile(Object f) {
        File x = (f instanceof File) ? (File) f : new File(f.toString());
        if(!x.exists()) {
            if(x.getParentFile() != null) {
                if(!x.getParentFile().exists()) {
                    x.getParentFile().mkdirs(); 
                }
            } 
            try {
                x.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return x;
    }

    static File createFile(Object f,Object content,String mode) {
        File x = createFile(f);
        String data = content.toString();

        try (RandomAccessFile raf = new RandomAccessFile(x, "rw")) {
            if (mode.equals("prepend")) {
                // Jika prepend true, pindahkan semua data ke bawah
                byte[] existingData = new byte[(int) raf.length()];
                raf.read(existingData);
                if(raf.length() > 0) {
                    data = data+"\r\n"+new String(existingData,StandardCharsets.UTF_8);
                }
                raf.setLength(0);
                raf.seek(0);
                raf.write(data.getBytes(StandardCharsets.UTF_8));
            } else if(mode.equals("append")) {
                // Jika prepend false, tambahkan data di akhir file
                raf.seek(raf.length());
                if (raf.length() > 0) {
                    data = "\r\n" + data;
                }
                raf.write(data.getBytes(StandardCharsets.UTF_8));
            } else {
                raf.setLength(0);
                raf.write(data.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating/replacing file: " + e.getMessage());
        }
        return x;
    }

    /*=================================/
     *           READ
     *================================*/
    @SuppressWarnings("unchecked")
    static <T> T read(Path filePath, Class<T> clz){
        if(clz == null) {
            return (T)(String)readFile(filePath,"string");
        } else {
            if(List.class.isAssignableFrom(clz)) {
                return clz.cast(readFile(filePath, "list"));
            } else if (Map.class.isAssignableFrom(clz)) {
                try {
                    return (T)__.getMapper().readValue(readFile(filePath, "string").toString(),new TypeReference<Map<String,Object>>() {});
                } catch (JsonProcessingException e) {
                   return null;
                }
            }
        }
        return null;
    }

    private static Object readFile(Path filePath,String mode) {
        File f = new File(filePath.toString());
        
        StringBuilder builder = new StringBuilder();
        List<String> listResult = new ArrayList<>();

        if(f.exists() && f.isFile()) {
            try (RandomAccessFile raf = new RandomAccessFile(f, "r")){
                int bufferSize = (int)calculateBufferSize(raf.length());    
                byte[] bytes = new byte[bufferSize];
                int bytesRead;

                while((bytesRead = raf.read(bytes)) != -1) {
                    if(mode.equals("list")) {
                        for(int i = 0; i < bytesRead;i++) {
                            char currentChar = (char) bytes[i];
                            if (currentChar == '\n' || currentChar == '\r') {
                                if(!builder.toString().trim().isEmpty()) {
                                    listResult.add(builder.toString().trim());
                                    builder = new StringBuilder(); // Reset StringBuilder
                                }  
                            } else {
                                builder.append(currentChar);
                            }
                        }
                    } else {
                        String data = new String(bytes, 0, bytesRead, StandardCharsets.UTF_8);
                        builder.append(data);
                    }
                }
                if(mode.equals("list")) {
                    if (builder.length() > 0) {
                        listResult.add(builder.toString());
                    }
                    return (List<String>)listResult;
                } else {
                    return builder.toString();
                }
            } catch(IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return "";
    }

    private static long calculateBufferSize(long fileSize) {
        // Sesuaikan logika penghitungan ukuran buffer berdasarkan ukuran file
        if (fileSize <= 1024 * 1024) {
            // Jika file kecil (<= 1MB), gunakan buffer kecil (contoh: 4KB)
            return 4 * 1024;
        } else if (fileSize <= 1024 * 1024 * 1024) {
            // Jika file medium (<= 1GB), gunakan buffer sedang (contoh: 256KB)
            return 256 * 1024;
        } else {
            // Jika file besar (> 1GB), gunakan buffer besar (contoh: 4MB)
            return 4 * 1024 * 1024;
        }
    }

    /*====================
     *      Delete
     *=====================*/
    static boolean delete(Path filePath,boolean selfDelete) {
        File f = new File(filePath.toString());
        if(f.exists()) {
            if(f.isDirectory()) {
                return deleteFiles(f,selfDelete);
            } else {
                return delete(filePath, null,true);
            }
        }
        return false;
    }

    static boolean delete(Path filePath,String opt,boolean selfDelete) {
        File f = new File(filePath.toString());
        if(f.exists()) {
            if(opt == null) {
                return deleteFiles(f,selfDelete);
            } else {
                if(f.isDirectory()) {
                    Map<String,String> map = _File.getOption(opt);
                    File[] files = f.listFiles(new setFileFilter(map));
                    if(files != null && files.length > 0) {
                        for(File file : files) {
                            if(file != null) file.delete();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean deleteFiles(File file,boolean selfDelete) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null) {
                for (File f : files) {
                    deleteFiles(f,true);
                }
            }
            if(selfDelete) {
                file.delete();
            }
        } else {
            file.delete();
        }
        
        return true;
    }

    static class setFileFilter implements FileFilter {
        private final Map<String,String> map;

        public setFileFilter(Map<String,String> filter) {
            System.out.println("filter =>"+filter);
            this.map = filter;
        }

        @Override
        public boolean accept(File files) {
            if(map.get("type") != null) {
                if(map.get("type").toString().equalsIgnoreCase("dir")) {
                    if(files.isDirectory()) {
                        return filter(files);
                    }
                } else if (map.get("type").toString().equalsIgnoreCase("file")) {
                    if(files.isFile()) {
                        return filter(files);
                    }
                }
            } else {
                return filter(files);
            }
            return false;
        }

        private boolean filter(File files) {
            boolean out = false;
            String fname = files.getName();

            if(map.get("regex") != null) {
                out = fname.matches(map.get("regex"));
            }

            if(map.get("start") != null) {
                String start = map.get("start");
                out = fname.toLowerCase().startsWith(start.toLowerCase());
            }

            if(map.get("end") != null) {
                String end = map.get("end");
                out = fname.toLowerCase().endsWith(end.toLowerCase());
            }

            if(map.get("name") != null) {
                return fname.equalsIgnoreCase(map.get("name"));
            }

            return out;
        }
    }
    
    /*====================
     *      CopyDir & CopyFile
     *=====================*/
    static void copy(Path source,Path destination,boolean cut) {
        File src = new File(source.toString());
        File dest = new File(destination.toString());

        if(src.isFile()) {
            if(dest.isAbsolute()) {
                if(!dest.getName().contains(".")) {
                    Path p = Paths.get(dest+"/"+src.getName());
                    dest = new File(p.toString());  
                } 
                if(!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                copyFile(src, dest, cut);
            } else {
                Path p = Paths.get(src.getParentFile()+"/"+dest.getName());
                dest = new File(p.toString());
                copyFile(src,dest,cut);
            }
        } else {
            copyDirectory(src, dest,cut);
        }
    }
    
    private static void copyDirectory(File src, File dest, Boolean cut) {
        if(!dest.exists()) dest.mkdir();

        String[] files = src.list();
        if(files != null) {
            for (String file : files) {
                File sourceFile = new File(src, file);
                File destinationFile = new File(dest, file);

                if (sourceFile.isDirectory()) {
                    copyDirectory(sourceFile, destinationFile, cut);
                } else {
                    copyFile(sourceFile, destinationFile, cut);
                }
            }
            if (cut) {
                delete(src.toPath(),null,true);
            }
        }
    }

    private static void copyFile(File src, File dest, Boolean cut) {
        InputStream input = null;
        OutputStream output = null;

        try {
            input = new FileInputStream(src);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if(input != null) input.close();
                if(output != null) output.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (cut) {
            delete(src.toPath(),null,true);
        }
    }

    /*===========================
    *			INTERNAL HELPER
    *============================*/ 
    static String getExt(Path fileName, String token) {
        String fname = fileName.getFileName().toString();
        int pos = fname.lastIndexOf(token);
        return fname.substring(pos + 1).toLowerCase();
    }
    /*===========================
    *			ATTRIBUTE
    *============================*/
    /**
     * Pengecekan zide dari directory maupun file
     * @param path
     * @return boolean
     */
    static boolean isEmpty(Path path) {
        File f = new File(path.toString());
        if(f.exists()) {
            if(f.isDirectory()) {
                File[] listFiles = f.listFiles();
                return (listFiles == null ||  listFiles.length == 0)  ? true : false; 
            } else {
                return f.length() <= 0;
            }
        }
        return true;
    }

    private static void setPermission(File file) {
        file.setExecutable(true, false);
        file.setReadable(true, false);
        file.setWritable(true, false);
    }
    
}
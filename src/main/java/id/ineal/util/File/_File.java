package id.ineal.util.File;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class _File 
{

    private static Path convertToPath(Object target) {
        return (target instanceof String) ? Paths.get(target.toString()) : (Path)target;
    }

    /**
     * @param target : Nama File yang akan dibuat
     * @throws RuntimeException
     */
    public static File create(Object target) {
        return FileOperation.create(convertToPath(target), null,"replace");
    }

    /**
     * @param target : Nama File yang akan dibuat
     * @param content : Isi Dari file
     * @throws RuntimeException
     */
    public static File create(Object target,Object content) throws RuntimeException {
        return FileOperation.create(convertToPath(target),content,"replace");
    }

    /**
     * @param target : Nama File yang akan dibuat
     * @param content : Isi Dari file
     * @param append : apakah data baru bisa digabungkan? (default true)
     * @throws RuntimeException
     */
    public static File create(Object target,Object content,boolean prepend) throws RuntimeException {
        String mode = (prepend) ? "prepend" : "append";
        return FileOperation.create(convertToPath(target),content,mode);
    }

    public static File createFile(Object target) {
        return FileOperation.createFile(convertToPath(target));
    }
    
    public static File createFile(Object target,Object content) {
        return FileOperation.createFile(convertToPath(target),content,"replace");
    }

    public static File createFile(Object target,Object content,boolean prepend) {
        String mode = (prepend) ? "prepend" : "append";
        return FileOperation.createFile(convertToPath(target),content,mode);
    }

    public static File createDirectory(Object target) {
        File f = new File(target.toString());
        if(!f.exists()) {
            f.mkdirs();
        }
        return f;
    }

    /*=================================/
     *           READ
     *================================*/
    public static String read(Object target) {
        return FileOperation.read(convertToPath(target),null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T read(Object target,Class<?> clz) {
        return (T)FileOperation.read(convertToPath(target),clz);
    }

    /**
     * @param path : lokasi directory file ex: D:/temp
     * @param opt : Bisa dengan nama file dan extnya atau dengan regex<p></p>
     * <b>Usage</b> 
     * <ul>
     *      <li>start:namefile</li>
     *      <li>end:txt</li>
     *      <li>regex:.*20230307.*</li>
     *      <li>regex:errno.*20230307.*</li>
     * </ul>
     * @return List<String>
     */
    public static List<String> find(Object path,String opt) {
        return FileFunction.doFind(convertToPath(path),opt,false);
    }
 
    public static boolean isWritable(String path) {
        File f = new File(path);
        return f.canWrite();
    }

    public static boolean isExecutable(String path) {
        File f = new File(path);
        return f.canExecute();
    }

    public static boolean isReadable(String path) {
        File f = new File(path);
        return f.canRead();
    }

    /**
     * Generate filename windows like,jika nama file exists
     * maka akan dibuat suffix  1 ex : filename-1
     * @param path ex : "d:/data"
     * @param fname ex : "ineal.txt"
     * @return File object
     */
    public static File generateName(String path, String fname) {
        Path p = Paths.get(path, fname);
        File f = new File(p.toString());

        if (!f.exists()) {
            return f;
        }

        if (f.isDirectory()) {
            String name = fname+" - Copy";
            p = Paths.get(path,name);
            f = new File(p.toString());
            if(f.exists()) {
                int i = 2;
                while (f.exists()) {
                    p = Paths.get(path, name + " (" + i + ")");
                    f = new File(p.toString());
                    i++;
                }
            }
        } else {
            int dotChar = fname.lastIndexOf(".");
            if(dotChar < 0) {
                String name = fname+" - Copy";
                p = Paths.get(path,name);
                f = new File(p.toString());
                if(f.exists()) {
                    int i = 2;
                    while (f.exists()) {
                        p = Paths.get(path, name + " (" + i + ")");
                        f = new File(p.toString());
                        i++;
                    }
                }
            } else {
                String name = fname.replaceFirst("[.][^.]+$", "")+" - Copy";
                String ext = fname.substring(dotChar + 1);
                p = Paths.get(path,name+"."+ext);
                f = new File(p.toString());

                if(f.exists()) {
                    int i = 2;
                    while (f.exists() && f.isFile()) {
                        p = Paths.get(path, name + " (" + i + ")." + ext);
                        f = new File(p.toString());
                        i++;
                    }
                }
            }
        }

        return f;
    }
    /*===========================
    *			DELETE
    *============================*/
    public static boolean delete(Object path) {
        return FileOperation.delete(convertToPath(path),null,true);
    }

    public static boolean delete(Object path,boolean selfDelete) {
        return FileOperation.delete(convertToPath(path), selfDelete);
    }

    public static boolean delete(Object path,String opt) {
        return FileOperation.delete(convertToPath(path), opt,true);
    }

    public static boolean delete(Object path,String opt,boolean selfDelete) {
        return FileOperation.delete(convertToPath(path), opt,selfDelete);
    }

    public static boolean delete(List<String> targets) {
        int i = 0;
        for(String target : targets) {
            if(FileOperation.delete(convertToPath(target),null,true)) {
                i++;
            }
        }
        return (i > 0) ? true : false;
    }

    public static boolean delete(List<String> targets,boolean selfDelete) {
        int i = 0;
        for(String target : targets) {
            if(FileOperation.delete(convertToPath(target),null,selfDelete)) {
                i++;
            }
        }
        return (i > 0) ? true : false;
    }
    /*===========================
    *			COPY
    *============================*/
    public static void copy(Object source,Object dest) {
        FileOperation.copy(convertToPath(source), convertToPath(dest),false);
    }

    public static void cut(Object source,Object dest) {
        FileOperation.copy(convertToPath(source), convertToPath(dest),true);
    }

   /**
     * Mengambil tipe Extensi file tanpa token
     * 
     * @param fileName {path} file yang akan dicek
     * @return String
     */
    public static String ext(Object fileName) {
        return FileOperation.getExt(convertToPath(fileName), ".");
    }

    /**
     * Mengambil tipe Extensi file
     * @param fileName {path} file yang akan dicek
     * @param token delimiter ext : [.,]
     * @return String
     */
    public static String ext(Object fileName, String token) {
        return FileOperation.getExt(convertToPath(fileName), token);
    }
    /*===========================
    *			SIZE
    *============================*/
    public static long size(Object fileName) {
        Path target = convertToPath(fileName);
        File f = new File(target.toString());
        return f.length();
    }

    public static String size(Object fileName,String mode) {
        if(mode.equals("readable")) {
            return (String)readableSize(size(fileName));
        }
        return null;
    }

    private static String readableSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    /*===========================
    *			EMPTY
    *============================*/
    public static boolean isEmpty(Object path) {
        return FileOperation.isEmpty(convertToPath(path));
    }

    public static boolean truncate(Object path) {
        try {
            Path fpath = convertToPath(path);
            File f = new File(fpath.toString());
            if(f.exists()) {
                FileWriter fw = new FileWriter(f, false);
                fw.flush();
                fw.close();
            }
            return f.length() <= 0 ? true : false;
        } catch (IOException ignored) { }

        return false;
        
    }

    public static boolean rename(Object name,Object newName) {
        File oldFile = new File(convertToPath(name).toString());
        return oldFile.renameTo(new File(convertToPath(newName).toString()));
    }

    public static boolean exists(Object path) {
        return new File(convertToPath(path).toString()).exists();
    }
    /*=================================/
     *           INFO
     *================================*/
    public static String attr(Object path,String key) {
        Path p = (path instanceof Path) ? (Path) path : Paths.get(path.toString());
        Map<String,Object> data = attr(p);
        if(data.get(key) != null) {
            return String.valueOf(data.get(key));
        }
        return null;
    }

    public static Map<String,Object> attr(Object path) throws RuntimeException {
        Path p = (path instanceof Path) ? (Path) path : Paths.get(path.toString());
        String type = Files.isDirectory(p) ? "dir" : "file";

        Map<String,Object> data = new HashMap<>();
        BasicFileAttributes attr;
        try {
            attr = Files.readAttributes(p, BasicFileAttributes.class);
            data.put("sizeLong",attr.size());
            data.put("size",readableSize(attr.size()));
            data.put("type",type);
            data.put("lastModified",attr.lastModifiedTime().toMillis());
            data.put("createAt",attr.creationTime().toMillis());
            data.put("lastAccess",attr.lastAccessTime().toMillis());
            data.put("symbolic",attr.isSymbolicLink());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return data;
    }

    /*=================================/
     *           Zip
     *================================*/
    public static void createZip(Object target,Object output){
        try {
            FileFunction.zip(target, convertToPath(output));
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void unzip(String target,String output) throws IOException {
        FileFunction.unzip(target, output);
    }

    public static boolean downloadFrom(String url,Object dest) {
        String destination = convertToPath(dest).toString();
        try (InputStream in = new URL(url).openStream();
            FileOutputStream fo = new FileOutputStream(destination)
        ){
            byte[] bytes = new byte[1024];
            int len;
            while((len = in.read(bytes)) != -1) {
                fo.write(bytes,0,len);
                fo.flush();
            }
        } catch(IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return exists(destination) ? true : false;
    }

    /*===========================
    *			HELPER
    *============================*/
    public static Map<String,String> getOption(String opt) {
        Map<String,String> map = new LinkedHashMap<>();
        if(opt.contains(",")) {
            String[] arr = opt.split(",");
            for(String x : arr) {
                String[] args = x.split(":");
                map.put(args[0], args[1]);
            }
        } else {
            if(opt.contains(":")) {
                String[] args = opt.split(":");
                if (args.length > 0) {
                    map.put(args[0], args[1]);
                }
            } else {
                map.put("name",opt);
            }
        }
        return map;
    }

    public static String watch(Object src) {
        try {
            return FileFunction.watchFile(convertToPath(src).toString(), null,String.class);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public static <T> T watch(Object src,Object... val) {
        try {
            if(val.length == 2) {
                if(val[0] instanceof String && val[1] instanceof Class<?>) {
                    Class<?> c = (Class<?>) val[1];
                    return FileFunction.watchFile(convertToPath(src).toString(),String.valueOf(val[0]),c);
                }
            } else if(val.length == 1) {
                 if(val[0] instanceof Class<?>) {
                    Class<?> c = (Class<?>) val[0];
                    return FileFunction.watchFile(convertToPath(src).toString(),null,c);
                } else {
                    return FileFunction.watchFile(convertToPath(src).toString(),String.valueOf(val[0]),String.class);
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
    
    public static File createObject(String path,Object obj_content) {
        if(path != null) {
            
            File f = new File(path);
            Path p = f.toPath();

            try(OutputStream stream = Files.newOutputStream(p);
                ObjectOutputStream obj = new ObjectOutputStream(stream)){
                obj.writeObject(obj_content);
                return f;
            } catch(IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T readObject(Object path,Class<?> clz) {
        if(path != null) {
            Path p = (path instanceof Path) ? (Path) path : Paths.get(path.toString());
            try (InputStream stream = Files.newInputStream(p);
                ObjectInputStream objectInputStream = new ObjectInputStream(stream);
            ) {
                return (T) clz.cast(objectInputStream.readObject());
            } catch(Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }
    
}

package id.ineal.util.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import id.ineal.util.__;
import id.ineal.util.Response.Response;

public class Cmd {
    
    /*===========================
    *			
    *============================*/
    public static Process run(String cmd) throws IOException {
        return _runner(null, cmd);
    }
    
    public static Process run(String path,String cmd) throws IOException {
        return _runner(path, cmd);
    }

    public static Response exec(String path,String cmd) {
        return _execute(path,cmd);
    }

    /*===========================
    *			
    *============================*/

    public static Response exec(String cmd) {
        return _execute(null,cmd);
    }

    private static Process _runner(String path,String cmd) throws IOException {
        String os = __.getOs();
        ProcessBuilder builder = new ProcessBuilder();
        if(path != null) {
            builder.directory(new File(path));
        }

        if(os.equals("window")) {
            builder.command("cmd", "/c", cmd);
        } else {
            builder.command("bash", "-c", cmd);
        }
        return builder.start();
    }

    private static Response _execute(String path,String cmd) {
        Response response = Response.createStatus(1)
                                .message("ok");

        String successString = "";
        String errorString = "";
        
        StringBuilder successMessage = new StringBuilder();
        StringBuilder errorMessage = new StringBuilder();

        try {
            Process p = run(path,cmd);

            BufferedReader successBuffer = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            
            while ((successString = successBuffer.readLine()) != null) {
                successMessage.append(successString).append(System.lineSeparator());
            }
            while ((errorString = errorBuffer.readLine()) != null) {
                errorMessage.append(errorString).append(System.lineSeparator());
            }

            int exitValue = p.exitValue();
            

            if(exitValue != 0) {
                response.setStatus(0)
                    .message(errorMessage.toString());
            } else {
                response.message(successMessage.toString());
            }
            successBuffer.close();
            errorBuffer.close();
        } catch (IOException e) {
            response.setStatus(0)
                       .message(e.getMessage());
        }
        return response;
    }
}

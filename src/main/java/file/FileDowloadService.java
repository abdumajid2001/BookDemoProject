package file;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileDowloadService {
    public void dowload(ServletContext context, String filePath, String fileName, HttpServletResponse resp) throws IOException {
        File dowloadFile = new File(filePath);
        try (
                FileInputStream fileInputStream = new FileInputStream(dowloadFile);
                ServletOutputStream outputStream = resp.getOutputStream();
        ) {
            String mimeType = context.getMimeType(filePath);
            System.out.println(mimeType);
            resp.setContentType(mimeType);
            resp.setContentLengthLong(dowloadFile.length());
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", fileName);
            resp.setHeader(headerKey, headerValue);

            byte[] buffer = new byte[(int) dowloadFile.length()];
            int bytesRead = -1;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}

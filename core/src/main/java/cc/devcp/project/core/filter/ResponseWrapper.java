package cc.devcp.project.core.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <pre>
 *     description:
 * </pre>
 *
 * @author deep.wu
 * @version 2020-04-04
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private PrintWriter printWriter = new PrintWriter(outputStream);

    @Override
    public PrintWriter getWriter() throws IOException {
        return printWriter;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            public void setWriteListener(WriteListener listener) {
            }
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
        };
    }
    public void flush(){
        try {
            printWriter.flush();
            printWriter.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ByteArrayOutputStream getByteArrayOutputStream(){
        return outputStream;
    }

    public String getTextContent() {
        flush();
        return outputStream.toString();
    }


}

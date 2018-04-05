package org.afc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class IOUtil {

	public static void close(InputStream is) {
		if (is != null) {
			try {
	            is.close();
            } catch (IOException e) {
	            throw new RuntimeException("Unable to close " + is.getClass().getSimpleName(), e);
            }
		}
	}

	public static void close(OutputStream os) {
		if (os != null) {
			try {
	            os.close();
            } catch (IOException e) {
	            throw new RuntimeException("Unable to close " + os.getClass().getSimpleName(), e);
            }
		}
	}

	
	public static void close(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
            } catch (IOException e) {
	            throw new RuntimeException("Unable to close " + socket.getClass().getSimpleName(), e);
            }
		}
	}
		
	
	public static void pipeInto(InputStream in, OutputStream out, int bufferSize) throws IOException {
    	byte[] buffer = new byte[bufferSize];
    	int len = -1;
    	while ((len = in.read(buffer)) != -1) {
    		out.write(buffer, 0, len);
    	}
	}
}

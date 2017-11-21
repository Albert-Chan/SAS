package connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HttpConnection {

	public static String getData(String url, String charset) throws IOException {
		URL u = new URL(url);
		try (InputStream in = u.openStream(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			int size;
			byte[] bytes = new byte[256];
			while ((size = in.read(bytes)) != -1) {
				out.write(bytes, 0, size);
			}
			String result = out.toString(charset);
			return result;
		}
	}

}

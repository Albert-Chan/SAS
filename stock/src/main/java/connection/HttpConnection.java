package connection;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class HttpConnection {

	public static String getData(String url, String charset) throws Exception {
		byte[] bytes = new byte[256];
		InputStream in = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			URL u = new URL(url);
			in = u.openStream();
			int size;
			while ((size = in.read(bytes)) != -1) {
				out.write(bytes, 0, size);
			}
			String result = out.toString(charset);

			out.close();
			return result;
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

}

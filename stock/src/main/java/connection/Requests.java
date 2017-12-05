package connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class Requests {

	public static String get(String url, String charset) throws IOException {
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

	public static String get(String url, String charset, int tryNum) throws MultipleTryFailedException {
		for (int i = 0; i < tryNum; i++) {
			try {
				return get(url, charset);
			} catch (IOException e) {
			}
		}
		throw new MultipleTryFailedException("Failed after trying " + tryNum + " times.");
	}

}

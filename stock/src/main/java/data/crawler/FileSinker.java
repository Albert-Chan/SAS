package data.crawler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

class FileSinker implements ICrawlerPostHandler {
	private File baseDir = null;

	public FileSinker(File base) {
		this.baseDir = base;
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
	}

	public void handle(String stockId, List<String> array) throws HandlerException {
		try (PrintWriter writer = new PrintWriter(new File(baseDir, stockId + ".txt"), "utf8");) {
			for (String object : array) {
				writer.println(object);
			}
		} catch (IOException e) {
			throw new HandlerException(e);
		}
	}
}
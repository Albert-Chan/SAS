package data.crawler;

import java.util.List;

public interface ICrawlerPostHandler {
	public void handle(String stockId, List<String> lines) throws HandlerException;
}
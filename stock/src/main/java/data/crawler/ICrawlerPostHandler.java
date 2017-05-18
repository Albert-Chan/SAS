package data.crawler;

import java.util.ArrayList;

import org.json.JSONObject;

public interface ICrawlerPostHandler {
	public void handle(String stockId, ArrayList<JSONObject> array ) throws Exception;
}

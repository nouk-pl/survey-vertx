package imie.survey.vertx.helpers;

import io.vertx.core.json.JsonObject;

public class ConfigurationHelper {

	private ConfigurationHelper() {}

	public static JsonObject build(String host, int port, String username, String password,
			String database) {
		
		JsonObject configuration = new JsonObject();
		configuration.put("host", host);
		configuration.put("port", port);
		configuration.put("username", username);
		configuration.put("password", password);
		configuration.put("database", database);
		
		return configuration;
	}

}

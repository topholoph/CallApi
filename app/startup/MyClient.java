package startup;

import play.mvc.*;
import play.libs.Json;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

public class MyClient implements WSBodyReadables, WSBodyWritables {
	private final WSClient ws;

	@Inject
	public MyClient(WSClient ws) {
		this.ws = ws;
		JsonNode json = Json.newObject().put("username", "adminPC").put("password", "adminPC").put("flag", "1");
		WSRequest request = ws.url("https://treskow.ml").setBody(json);
		CompletionStage<? extends WSResponse> responsePromise = request.post(json);
	}

}

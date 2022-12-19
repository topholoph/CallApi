package startup;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import controllers.CallRestApi;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;

public class Startup implements WSBodyReadables, WSBodyWritables {
	@Inject
	public Startup(WSClient ws) throws JsonProcessingException {
		System.out.println("+++++I run on Startup+++++");
		
		CallRestApi callRestApi = new CallRestApi();
		

		CompletableFuture<JsonNode> loginFuture = callRestApi.login("https://treskow.ml/manage/api/v1/login", ws);

		loginFuture.thenRun(() -> {
			try {
				System.out.println("CompletionStage Result Value= " + loginFuture.get());
				JsonNode accessToken = loginFuture.get().findValue("access_token");

				CompletableFuture<JsonNode> workspaceFuture = callRestApi.getWorkspaceInformation(
						"https://treskow.ml/manage/api/v1/workspaces/current", accessToken, ws);
				loginFuture.thenRun(() -> {

					try {
						System.out.println("Workspacec Information=  " + workspaceFuture.get());
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				});

			} catch (InterruptedException e) {
				System.out.println("InterruptedException");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				System.out.println("ExecutionException");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}
}

//
//@Inject
//public Startup (WSClient ws) {
//	System.out.println("+++++I run on Startup+++++");
//	
//	this.ws = ws;
//	JsonNode json = Json.newObject().put("username", "adminPC").put("password", "adminPC").put("flag", "1");
//	
//	System.out.println("json: " + json);
//
//	WSRequest request = ws.url("https://treskow.ml/manage/api/v1/login").setBody(json);
//	
//	System.out.println("Request URL= " + request.getUrl());
//	
//	CompletionStage<? extends WSResponse> responsePromise = request.post(json);
//	
//	System.out.println("CompletionStage= " + responsePromise);
//
//}

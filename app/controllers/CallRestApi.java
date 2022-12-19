package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

public class CallRestApi  implements WSBodyReadables, WSBodyWritables {
	
	public CompletableFuture<JsonNode> login(String url, WSClient webService) {

		JsonNode json = Json.newObject().put("username", "adminPC").put("password", "adminPC").put("flag", "1");

		System.out.println("json: " + json);

		WSRequest request = webService.url(url).setBody(json);

		System.out.println("Request URL= " + request.getUrl());
		CompletionStage<? extends WSResponse> responsePromise = request.post(json);

		return responsePromise.handle((result, error) -> {
			if (error != null) {

				System.out.println("Problem beim Aufruf folgender URL: " + url);
				return null;
			} else {
				System.out.println("Im else-block (kein Error bei login-Funktion)");
				return result;
			}
		}).<JsonNode>thenApply(r -> ((WSResponse) r).getBody(json())).toCompletableFuture();
	}

	public CompletableFuture<JsonNode> getWorkspaceInformation(String url, JsonNode access_token, WSClient webService) {

		WSRequest request = webService.url(url).addHeader("x-auth-token", access_token.toString());

		CompletionStage<? extends WSResponse> responsePromise = request.get();

		return responsePromise.handle((result, error) -> {
			if (error != null) {

				return null;
			} else {
				System.out.println("Im else-block (kein Error bei getWorkspaceInformation-Funktion)");
				return result;
			}
		}).<JsonNode>thenApply(r -> ((WSResponse) r).getBody(json())).toCompletableFuture();
	}
	
	
	
	
}

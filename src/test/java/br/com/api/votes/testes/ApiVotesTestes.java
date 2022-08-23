package br.com.api.votes.testes;

import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.*;

/**
 * @author Natalia Carnelos
 */

//GARANTINDO QUE A EXECUÇÃO DO TESTE SERA 1,2,3,4 E 5 PARA CRIAR, DEPOIS PROCURAR O ITEM CRIADO E POR ULTIMO DELETAR O ITEM CRIADO
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApiVotesTestes {

	private static final Logger LOG = LoggerFactory.getLogger(ApiVotesTestes.class);
	private static String ID_VOTE_CRIADO;

	//CENARIO 1
	@Test
	public void test1CreateVote() {
		
		JSONObject requestParams = new JSONObject();
		requestParams.put("image_id", "asf2");
		requestParams.put("sub_id", "my-user-1234");
		requestParams.put("value", 1);
		LOG.info("------------ Parametros POST" + requestParams.toString());
		
		Header first = new Header("Content-Type", "application/json");
		Header second = new Header("x-api-key", "live_Ln3MwKXoB0kSSrAd7SqEBJEoDQKyEmwfYIsyoPVuTkXjkteglhNp1JfVo2W6PczH");
		Headers headers = new Headers(first, second);
		LOG.info("------------ Headers POST" + headers.toString());
		
		ValidatableResponse response = 
		RestAssured.given().
		headers(headers).
		body(requestParams.toString()).
		when()
		        .post("https://api.thecatapi.com/v1/votes").
		then()
		        .statusCode(201)
		        .body("message", is("SUCCESS"))
		        .body("id", is(notNullValue()))
		        .body("image_id", is("asf2"))
		        .body("sub_id", is("my-user-1234"))
		        .body("value", is(1))
		        .body("country_code", is("BR"));
		
		//capturando o ID do VOTE criado
		ID_VOTE_CRIADO = response.extract().jsonPath().getString("id");
		LOG.info("ID_VOTE_CRIADO: " + ID_VOTE_CRIADO);
	}
	
	//CENARIO 2
	@Test
	public void test2SearchVote() {
		Header header = new Header("x-api-key", "live_Ln3MwKXoB0kSSrAd7SqEBJEoDQKyEmwfYIsyoPVuTkXjkteglhNp1JfVo2W6PczH");
		Headers headers = new Headers(header);

		RestAssured.given().
		headers(headers).
		pathParam("vote_id", ID_VOTE_CRIADO).	
		when()
		        .get("https://api.thecatapi.com/v1/votes/{vote_id}").
		        
		then()
		        .statusCode(200)
		        .body("id", is(Integer.parseInt(ID_VOTE_CRIADO)))
		        .body("user_id", is(notNullValue()))
		        .body("image_id", is("asf2"))
		        .body("sub_id", is("my-user-1234"))
		        .body("created_at", is(notNullValue()))
		        .body("value", is(1))
		        .body("country_code", is("BR"));
	}

	//CENARIO 3
	@Test
	public void test3SearchInexistentVote() {
		Header header = new Header("x-api-key", "live_Ln3MwKXoB0kSSrAd7SqEBJEoDQKyEmwfYIsyoPVuTkXjkteglhNp1JfVo2W6PczH");
		Headers headers = new Headers(header);
		
		RestAssured.given().
		headers(headers).
		pathParam("vote_id", "1").	
		when()
		        .get("https://api.thecatapi.com/v1/votes/{vote_id}").
		        
		then()
		        .statusCode(404);
	}

	//CENARIO 4
	@Test
	public void test4DeleteVote() {
		Header header = new Header("x-api-key", "live_Ln3MwKXoB0kSSrAd7SqEBJEoDQKyEmwfYIsyoPVuTkXjkteglhNp1JfVo2W6PczH");
		Headers headers = new Headers(header);
		
		RestAssured.given().
		headers(headers).
		pathParam("vote_id", ID_VOTE_CRIADO).	
		when()
		        .delete("https://api.thecatapi.com/v1/votes/{vote_id}").
		        
		then()
		        .statusCode(200)
		        .body("message", is("SUCCESS"));	
	}

	//CENARIO 5
	@Test
	public void test5DeleteInexistentVote() {
		Header header = new Header("x-api-key", "live_Ln3MwKXoB0kSSrAd7SqEBJEoDQKyEmwfYIsyoPVuTkXjkteglhNp1JfVo2W6PczH");
		Headers headers = new Headers(header);
		LOG.info("------------ Headers POST" + headers.toString());
		
		RestAssured.given().
		headers(headers).
		pathParam("vote_id", "1").	
		when()
		        .delete("https://api.thecatapi.com/v1/votes/{vote_id}").
		        
		then()
		        .statusCode(404);
	}
}
package exercises;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;


public class RestAssuredExercises2Test {

	private static RequestSpecification requestSpec;

	@BeforeAll
	public static void createRequestSpecification() {

		requestSpec = new RequestSpecBuilder().
			setBaseUri("http://localhost").
			setPort(9876).
			setBasePath("/api/f1").
			build();
	}
	
	/*******************************************************
	 * Use junit-jupiter-params for @ParameterizedTest that
	 * specifies in which country
	 * a specific circuit can be found (specify that Monza 
	 * is in Italy, for example) 
	 ******************************************************/

	//todo
	static Stream<Arguments> countryDataProvider() {
		return Stream.of(
				Arguments.of("monza")
		);
	}


	/*******************************************************
	 * Use junit-jupiter-params for @ParameterizedTest that specifies for all races
	 * (adding the first four suffices) in 2015 how many  
	 * pit stops Max Verstappen made
	 * (race 1 = 1 pitstop, 2 = 3, 3 = 2, 4 = 2)
	 ******************************************************/

	//todo
	static Stream<Arguments> raceDataProvider() {
		return Stream.of(
				Arguments.of("1","1"),
				Arguments.of("2","3"),
				Arguments.of("3","2"),
				Arguments.of("4","2")
		);
	}

	/*******************************************************
	 * Request data for a specific circuit (for Monza this 
	 * is /circuits/monza.json)
	 * and check the country this circuit can be found in
	 ******************************************************/
	

	@ParameterizedTest
	@MethodSource("countryDataProvider")
	public void checkCountryForCircuit(String country) {
		given().
				spec(requestSpec).
				pathParams("country",country).
				when().log().all().
				get(" /circuits/{country}.json").
				then().log().all().
				statusCode(HttpStatus.SC_OK).
				body(containsString("Italy"));
	}
	
	/*******************************************************
	 * Request the pitstop data for the first four races in
	 * 2015 for Max Verstappen (for race 1 this is
	 * /2015/1/drivers/max_verstappen/pitstops.json)
	 * and verify the number of pit stops made
	 ******************************************************/

	@ParameterizedTest
	@MethodSource("raceDataProvider")
	public void checkNumberOfPitstopsForMaxVerstappenIn2015(String path,String pitstops) {
		given().
				spec(requestSpec).
				pathParams("path",path).
				when().log().all().
				get("/2015/{path}/drivers/max_verstappen/pitstops.json").
				then().log().all()
				.statusCode(HttpStatus.SC_OK)
				.body("MRData.total",is(pitstops));
	}
}
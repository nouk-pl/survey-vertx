package imie.survey.vertx;

import imie.survey.vertx.base.SockJSEventBusVerticle;
import imie.survey.vertx.helpers.PermissionHelper;
import imie.survey.vertx.helpers.ConfigurationHelper;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.rxjava.ext.asyncsql.AsyncSQLClient;
import io.vertx.rxjava.ext.asyncsql.MySQLClient;
import io.vertx.rxjava.ext.sql.SQLConnection;
import rx.Emitter;
import rx.Observable;
import rx.Single;

public class SurveyStatsVerticle extends SockJSEventBusVerticle {

	private final static String ROUTE_PATTERN = "/eventbus/*";
	private final static short LISTENING_PORT = 9100;

	public SurveyStatsVerticle() {
		super(ROUTE_PATTERN, LISTENING_PORT,
				PermissionHelper.create().bothSide("survey.stats.notify", "survey.stats.respond"));
	}

	@Override
	public void start() throws Exception {
		super.start();
		
		JsonObject configuration = ConfigurationHelper.build("localhost", 3306, "root", "root", "survey_base_vertx");
		AsyncSQLClient client = MySQLClient.createShared(vertx, configuration);
		
		Observable<ResultSet> resultObs = Observable.create((Emitter<ResultSet> em) ->
        {
            client	.rxGetConnection().flatMap(conn -> queryTotalAnswersByAgeRange(conn, 1))
            		.subscribe(
	                    result -> {
	                        em.onNext(result);
	                        em.onCompleted();
	                    },
	                    err -> {
	                        logger.error(err.getMessage());
	                        em.onError(err);
	                        em.onCompleted();
	                    });
        }, Emitter.BackpressureMode.NONE);
		
		vertx.eventBus().consumer("survey.stats.notify", message -> {
			logger.info("Message reçu : {}", (JsonObject) message.body());
			
			resultObs.subscribe(r -> {
				vertx.eventBus().send("survey.stats.respond", r.getRows().toString());
			});
		});
		
	}
	
	
	
	@Override
	public void init(Vertx vertx, Context context) {
		// TODO Auto-generated method stub
		super.init(vertx, context);
	}

	private Single<ResultSet> queryTotalAnswersByAgeRange(SQLConnection connection, int surveyId) {
		final String query = "SELECT age_range, SUM(total) FROM survey_stats WHERE survey_ref = ? GROUP BY age_range";
		
		JsonArray params = new JsonArray().add(surveyId);
		return connection.rxQueryWithParams(query, params).doAfterTerminate(connection::close);
	}

}

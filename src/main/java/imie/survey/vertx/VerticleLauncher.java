package imie.survey.vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import imie.survey.vertx.utils.LoggingUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class VerticleLauncher {
	
	private static final Logger logger = LoggerFactory.getLogger(VerticleLauncher.class);


	public VerticleLauncher(Class<? extends AbstractVerticle> verticle) {
		logger.info("Launcher initialized.");
		final Vertx vertx = Vertx.vertx();
		
		logger.info("Deploying {} verticle ...", verticle);
		vertx.deployVerticle(verticle.getName());		
	}


	public static void main(String[] args) {
		LoggingUtils.configureLogging();
		new VerticleLauncher(SurveyStatsVerticle.class);
	}

}

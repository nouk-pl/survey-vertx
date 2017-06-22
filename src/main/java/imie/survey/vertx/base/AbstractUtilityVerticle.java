package imie.survey.vertx.base;


import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.AbstractVerticle;

public abstract class AbstractUtilityVerticle extends AbstractVerticle {
	
	protected final Logger logger;

	protected AbstractUtilityVerticle() {
		super();
		logger = LoggerFactory.getLogger(this.getClass());
	}

}

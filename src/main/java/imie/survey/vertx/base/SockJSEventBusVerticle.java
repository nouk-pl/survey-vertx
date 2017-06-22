package imie.survey.vertx.base;

import java.util.Map;

import imie.survey.vertx.helpers.PermissionHelper;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.sockjs.SockJSHandler;

/**
 * Classe abstraite pour le comportement de base d'un verticle pouvant
 *   dialoguer sur l'EventBus via SockJS (pour les client JavaScript)
 * @author Fabien
 *
 */
public abstract class SockJSEventBusVerticle extends AbstractUtilityVerticle {
	
	protected Router router;
	
	protected BridgeOptions bridgeOptions;
	
	protected short listenPort;
	
	protected String routePattern;

	private Map<String, PermissionHelper.Direction> messagesPermissions;

	protected SockJSEventBusVerticle(String routePattern, short listenPort, PermissionHelper permissions) {
		super();
		this.routePattern = routePattern;
		this.listenPort = listenPort;
		
		this.messagesPermissions = permissions.getPermissionsMap();
	}

	private void initSockJSBridge() {
		
		router = Router.router(vertx);
		
		SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
		sockJSHandler.bridge(this.getPermittedOptions());
		
		router.route(routePattern).handler(sockJSHandler);
		
		vertx.createHttpServer().requestHandler(router::accept).listen(listenPort);		
	}

	private BridgeOptions getPermittedOptions() {
		BridgeOptions options = new BridgeOptions();
		
		if (messagesPermissions != null) {
			messagesPermissions.entrySet().forEach(permission -> {
				if ((permission.getValue() == PermissionHelper.Direction.INBOUND)
						|| (permission.getValue() == PermissionHelper.Direction.BOTHSIDE)) {
					options.addInboundPermitted(new PermittedOptions().setAddress(permission.getKey()));
				}

				if ((permission.getValue() == PermissionHelper.Direction.OUTBOUND)
						|| (permission.getValue() == PermissionHelper.Direction.BOTHSIDE)) {
					options.addOutboundPermitted(new PermittedOptions().setAddress(permission.getKey()));
				}
			});
		}
		
		return options;
	}

	@Override
	public void start() throws Exception {
		this.initSockJSBridge();
	}


}

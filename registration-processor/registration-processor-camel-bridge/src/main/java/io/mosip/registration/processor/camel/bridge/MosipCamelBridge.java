package io.mosip.registration.processor.camel.bridge;

import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RoutesDefinition;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.registration.processor.core.abstractverticle.MessageBusAddress;
import io.mosip.registration.processor.core.logger.RegProcessorLogger;
import io.vertx.camel.CamelBridge;
import io.vertx.camel.CamelBridgeOptions;
import io.vertx.core.AbstractVerticle;
import io.mosip.registration.processor.core.constant.LoggerFileConstant;

/**
 * The Class MosipCamelBridge.
 * 
 * @author Mukul Puspam
 * @since 0.0.1
 */
public class MosipCamelBridge extends AbstractVerticle {

	/** The reg proc logger. */
	private static Logger regProcLogger = RegProcessorLogger.getLogger(MosipCamelBridge.class);

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		MosipBridgeFactory.getEventBus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.vertx.core.AbstractVerticle#start()
	 */
	@Override
	public void start() throws Exception {

		vertx.eventBus().consumer(MessageBusAddress.ERROR.getAddress(),
				message -> regProcLogger.error(LoggerFileConstant.SESSIONID.toString(),
						LoggerFileConstant.APPLICATIONID.toString(), "ERROR while doing operation >> : ",
						message.body().toString()));

		CamelContext camelContext = new DefaultCamelContext();
		InputStream is = MosipCamelBridge.class.getClassLoader().getResourceAsStream("camel-routes.xml");
		RoutesDefinition routes = camelContext.loadRoutesDefinition(is);
		camelContext.addRouteDefinitions(routes.getRoutes());
		camelContext.start();

		CamelBridge.create(vertx, new CamelBridgeOptions(camelContext)).start();
	}

}

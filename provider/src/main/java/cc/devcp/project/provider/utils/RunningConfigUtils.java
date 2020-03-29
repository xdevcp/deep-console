package cc.devcp.project.provider.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * Running config
 * @author nkorange
 */
@Component
public class RunningConfigUtils implements ApplicationListener<WebServerInitializedEvent> {

    private static int serverPort;

    private static String contextPath;

    private static String clusterName = "serverlist";

	@Autowired
    private ServletContext servletContext;

    @Override
	public void onApplicationEvent(WebServerInitializedEvent event) {

		setServerPort(event.getWebServer().getPort());
		setContextPath(servletContext.getContextPath());
	}

    public static int getServerPort() {
        return serverPort;
    }

    public static String getContextPath() {
        return contextPath;
    }

    public static String getClusterName() {
		return clusterName;
	}

	public static void setServerPort(int serverPort) {
		RunningConfigUtils.serverPort = serverPort;
	}

	public static void setContextPath(String contextPath) {
		RunningConfigUtils.contextPath = contextPath;
	}

}

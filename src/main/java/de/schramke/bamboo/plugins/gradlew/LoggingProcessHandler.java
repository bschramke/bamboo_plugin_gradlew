package de.schramke.bamboo.plugins.gradlew;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.utils.process.PluggableProcessHandler;
import com.atlassian.utils.process.StringInputHandler;

/**
 * Created with IntelliJ IDEA.
 * User: bschramke
 * Date: 30.09.13
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
public class LoggingProcessHandler extends PluggableProcessHandler {
    private static final String DEFAULT_ENCODING = "UTF-8";

    private LogOutputHandler outputHandler;
    private LogOutputHandler errorHandler;

    public LoggingProcessHandler(BuildLogger logger) {
        this(logger, null, DEFAULT_ENCODING);
    }

    public LoggingProcessHandler(BuildLogger logger, String input) {
        this(logger, input, DEFAULT_ENCODING);
    }

    public LoggingProcessHandler(BuildLogger logger, String input, String encoding) {
        if (input != null) {
            setInputHandler(new StringInputHandler(encoding, input));
        }

        outputHandler = new LogOutputHandler(logger);
        setOutputHandler(outputHandler);

        errorHandler = new LogOutputHandler(logger);
        setErrorHandler(errorHandler);
    }

}

package de.schramke.bamboo.plugins.gradlew;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.utils.process.*;

/**
 * ProcessMonitor that logs the results to the provided {@link BuildLogger} using the provided {@link Priority}. When
 * the external process does not succeed, the error messages are logged using the info priority.
 */
public class LoggingProcessMonitor implements ProcessMonitor {

    final private BuildLogger logger;
    private StringObfuscator obfuscator = null;

    public LoggingProcessMonitor(BuildLogger logger) {
        this(logger,null);
    }

    public LoggingProcessMonitor(BuildLogger logger, StringObfuscator obfuscator) {
        this.logger = logger;
        this.obfuscator = obfuscator;
    }

    protected String obfuscate(String value) {
        if (obfuscator != null) {
            return obfuscator.obfuscate(value);
        } else {
            return value;
        }
    }

    /**
     * @param process the external process.
     * @return the command line.
     */
    protected String getCommandLine(ExternalProcess process) {
        return obfuscate(process.getCommandLine());
    }

    @Override
    public void onBeforeStart(ExternalProcess externalProcess) {
        if (this.logger != null) {
            logger.addBuildLogEntry("Starting process: " + getCommandLine(externalProcess));
        }
    }

    @Override
    public void onAfterFinished(ExternalProcess externalProcess) {
        if (this.logger != null) {
            StringBuilder message = new StringBuilder();
            String commandLine = getCommandLine(externalProcess);
            message.append("Finished process: ").append(commandLine);
            Long startTime = externalProcess.getStartTime();
            if (startTime != null) {
                message.append(" took ").append(System.currentTimeMillis() - startTime.longValue()).append("ms");
            }
            logger.addBuildLogEntry(message.toString());

            // log errors if present
            ProcessHandler handler = externalProcess.getHandler();
            if (handler != null && !handler.succeeded()) {
                logger.addErrorLogEntry(getErrorMessage(externalProcess));
            }
        }
    }

    public String getErrorMessage(ExternalProcess process) {
        ProcessHandler handler = process.getHandler();
        String commandLine = getCommandLine(process);
        StringBuilder message = new StringBuilder();

        if (handler.getException() != null) {
            message.append("Exception executing command \"")
                    .append(commandLine).append("\" ")
                    .append(handler.getException().getMessage()).append("\n")
                    .append(handler.getException()).append("\n");
        }

        String reason = null;
        if (handler instanceof PluggableProcessHandler) {
            OutputHandler errorHandler = ((PluggableProcessHandler) handler).getErrorHandler();
            if (errorHandler instanceof StringOutputHandler) {
                StringOutputHandler errorStringHandler = (StringOutputHandler) errorHandler;
                if (errorStringHandler.getOutput() != null) {
                    reason = errorStringHandler.getOutput();
                }
            }
        }
        if (reason != null && reason.trim().length() > 0) {
            message.append("Error executing command \"").append(commandLine).append("\": ").append(reason);
        }
        return obfuscate(message.toString());
    }
}

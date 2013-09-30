package de.schramke.bamboo.plugins.gradlew;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.utils.process.BaseOutputHandler;
import com.atlassian.utils.process.IOUtils;
import com.atlassian.utils.process.ProcessException;

import java.io.*;

public class LogOutputHandler extends BaseOutputHandler {
    private final BuildLogger logger;

    public LogOutputHandler(BuildLogger logger) {
        this.logger = logger;
    }

    @Override
    public void complete() {
    }

    @Override
    public void process(InputStream output) throws ProcessException {
        InputStreamReader reader = null;
        StringWriter writer = new StringWriter();

        try {
            reader = new InputStreamReader(output);

            char buffer[] = new char[1024];
            int num;
            while ((num = reader.read(buffer)) != -1) {
                resetWatchdog();
                writer.write(buffer, 0, num);
            }
            logger.addBuildLogEntry(writer.toString());

        } catch (InterruptedIOException e) {
            // This means the process was asked to stop which can be normal so we just finish
        } catch (IOException e) {
            throw new ProcessException(e);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);

        }
    }
}

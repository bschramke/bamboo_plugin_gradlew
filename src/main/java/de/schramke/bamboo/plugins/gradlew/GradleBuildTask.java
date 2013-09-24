package de.schramke.bamboo.plugins.gradlew;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskType;
import org.jetbrains.annotations.NotNull;

public class GradleBuildTask implements TaskType {

    @NotNull
    @Override
    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {
        final BuildLogger buildLogger = taskContext.getBuildLogger();

        final String say = taskContext.getConfigurationMap().get("say");

        buildLogger.addBuildLogEntry(say);

        return TaskResultBuilder.newBuilder(taskContext).success().build();
    }
}

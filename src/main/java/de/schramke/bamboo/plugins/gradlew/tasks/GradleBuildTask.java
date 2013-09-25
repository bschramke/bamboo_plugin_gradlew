package de.schramke.bamboo.plugins.gradlew.tasks;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.bamboo.v2.build.CurrentBuildResult;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GradleBuildTask implements TaskType {
    private static final Logger log = LoggerFactory.getLogger(GradleBuildTask.class);
    // ------------------------------------------------------------------------------------------------------- Constants
    // ------------------------------------------------------------------------------------------------- Type Properties

    public static final String GRADLE_CAPABILITY_PREFIX = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + ".gradle";
    public static final String ENVIRONMENT = "environmentVariables";
    public static final String LABEL = "label";
    public static final String TASK = "gradleTask";

    // ---------------------------------------------------------------------------------------------------- Dependencies
    private final EnvironmentVariableAccessor environmentVariableAccessor;
    private final CapabilityContext capabilityContext;

    // ---------------------------------------------------------------------------------------------------- Constructors
    public GradleBuildTask(EnvironmentVariableAccessor environmentVariableAccessor, CapabilityContext capabilityContext) {
        this.environmentVariableAccessor = environmentVariableAccessor;
        this.capabilityContext = capabilityContext;
    }

    // ----------------------------------------------------------------------------------------------- Interface Methods
    @NotNull
    @Override
    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        final CurrentBuildResult currentBuildResult = taskContext.getBuildContext().getBuildResult();

        final String gradleTask = taskContext.getConfigurationMap().get("gradleTask");

        buildLogger.addBuildLogEntry(gradleTask);

        try{
            final ConfigurationMap configuration = taskContext.getConfigurationMap();

            final String label = configuration.get(LABEL);
            Preconditions.checkNotNull(label);

            return TaskResultBuilder.newBuilder(taskContext).success().build();

        }finally {
            currentBuildResult.addBuildErrors(Lists.newArrayList("Irgendwas lief hier falsch"));
        }

    }

    private Map<String, String> getEnvironment(final TaskContext taskContext)
    {
        final String environment = taskContext.getConfigurationMap().get(ENVIRONMENT);
        return environmentVariableAccessor.splitEnvironmentAssignments(environment);
    }

    // -------------------------------------------------------------------------------------------------- Action Methods
    // -------------------------------------------------------------------------------------------------- Public Methods
    // -------------------------------------------------------------------------------------- Basic Accessors / Mutators
}

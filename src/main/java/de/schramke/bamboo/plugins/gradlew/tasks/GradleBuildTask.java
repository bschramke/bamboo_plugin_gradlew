package de.schramke.bamboo.plugins.gradlew.tasks;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
//import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.bamboo.v2.build.CurrentBuildResult;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.utils.process.ExternalProcess;
import com.atlassian.utils.process.ExternalProcessBuilder;
import com.atlassian.utils.process.StringProcessHandler;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.schramke.bamboo.plugins.gradlew.GradlewExtractor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GradleBuildTask implements TaskType {
    private static final Logger log = LoggerFactory.getLogger(GradleBuildTask.class);
    // ------------------------------------------------------------------------------------------------------- Constants
    // ------------------------------------------------------------------------------------------------- Type Properties

    public static final String GRADLE_CAPABILITY_PREFIX = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + ".gradle";
    public static final String LABEL = "label";
    public static final String USE_WRAPPER = "useWrapper";
    public static final String TASK = "gradleTask";
    public static final String ENVIRONMENT = "environmentVariables";
    public static final String LOG_LEVEL = "logLevel";
    public static final String STACK_TRACE_OUTPUT = "stackTraceOutput";

    // ---------------------------------------------------------------------------------------------------- Dependencies
    private final ProcessService processService;
    private final EnvironmentVariableAccessor environmentVariableAccessor;
    private final CapabilityContext capabilityContext;

    // ---------------------------------------------------------------------------------------------------- Constructors
    public GradleBuildTask(final ProcessService processService, EnvironmentVariableAccessor environmentVariableAccessor, CapabilityContext capabilityContext) {
        this.processService = processService;
        this.environmentVariableAccessor = environmentVariableAccessor;
        this.capabilityContext = capabilityContext;
    }

    // ----------------------------------------------------------------------------------------------- Interface Methods
    @NotNull
    @Override
    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        final CurrentBuildResult currentBuildResult = taskContext.getBuildContext().getBuildResult();
        TaskResultBuilder builder = TaskResultBuilder.newBuilder(taskContext);

        try {
            final ConfigurationMap configuration = taskContext.getConfigurationMap();
            final Map<String, String> environment = getEnvironment(taskContext);


            final String gradleTask = configuration.get("gradleTask");
            Preconditions.checkNotNull(gradleTask);

            final File workingDirectory = taskContext.getWorkingDirectory();

            buildLogger.addBuildLogEntry(workingDirectory.getAbsolutePath());
            final String runnerPath = GradlewExtractor.getGradlewPath(workingDirectory.getAbsolutePath());
            buildLogger.addBuildLogEntry(runnerPath);

            final List<String> command = Lists.newArrayList(runnerPath, "tasks");

            final StringProcessHandler processHandler = new StringProcessHandler();
            final ExternalProcess process = new ExternalProcessBuilder()
                    .command(command, workingDirectory)
                    .handler(processHandler)
                    .env(environment).build();

            process.execute();
            buildLogger.addBuildLogEntry(processHandler.getOutput());

            return builder.checkReturnCode(process, 0).build();

        } finally {
            currentBuildResult.addBuildErrors(Lists.newArrayList("Irgendwas lief hier falsch"));
        }

    }

    private Map<String, String> getEnvironment(final TaskContext taskContext) {
        final String environment = taskContext.getConfigurationMap().get(ENVIRONMENT);
        return environmentVariableAccessor.splitEnvironmentAssignments(environment);
    }

    // -------------------------------------------------------------------------------------------------- Action Methods
    // -------------------------------------------------------------------------------------------------- Public Methods
    // -------------------------------------------------------------------------------------- Basic Accessors / Mutators

}

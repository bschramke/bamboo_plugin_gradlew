package de.schramke.bamboo.plugins.gradlew.tasks;

import com.atlassian.bamboo.build.Job;
import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskConfigConstants;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.utils.i18n.I18nBean;
import com.atlassian.bamboo.v2.build.agent.capability.Requirement;
import com.atlassian.bamboo.v2.build.agent.capability.RequirementImpl;
import com.atlassian.util.concurrent.Nullable;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class GradleTaskConfigurator extends AbstractTaskConfigurator {

    private static final Logger log = Logger.getLogger(GradleTaskConfigurator.class);

    // ------------------------------------------------------------------------------------------------------- Constants

    private static final Set<String> FIELDS_TO_COPY = Sets.newHashSet(
            GradleBuildTask.USE_WRAPPER,
            GradleBuildTask.LABEL,
            GradleBuildTask.TASK,
            GradleBuildTask.ENVIRONMENT,
            GradleBuildTask.LOG_LEVEL,
            GradleBuildTask.STACK_TRACE_OUTPUT,
            TaskConfigConstants.CFG_JDK_LABEL,
            TaskConfigConstants.CFG_WORKING_SUB_DIRECTORY
    );

    // ------------------------------------------------------------------------------------------------- Type Properties
    // ---------------------------------------------------------------------------------------------------- Dependencies
    // ---------------------------------------------------------------------------------------------------- Constructors
    // ----------------------------------------------------------------------------------------------- Interface Methods

    @Override
    @NotNull
    public Set<Requirement> calculateRequirements(@NotNull TaskDefinition taskDefinition, @NotNull Job buildable) {
        final String label = taskDefinition.getConfiguration().get(GradleBuildTask.LABEL);
        return Sets.<Requirement>newHashSet(new RequirementImpl(GradleBuildTask.GRADLE_CAPABILITY_PREFIX + "." + label, true, ".*"));
    }

    @Override
    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection) {
        super.validate(params, errorCollection);

//        TextProvider textprovider = TextProviderUtils.getTextProvider();
        I18nBean i18nBean = this.getI18nBean();

        final String gradleTaskValue = params.getString("gradleTask");
        if (StringUtils.isEmpty(gradleTaskValue)) {
//            errorCollection.addError("gradleTask", textprovider.getText("gradle.task.error"));
            errorCollection.addError("gradleTask", i18nBean.getText("gradle.task.error"));
        }
    }

    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition) {

        final Map<String, String> map = super.generateTaskConfigMap(params, previousTaskDefinition);
        taskConfiguratorHelper.populateTaskConfigMapWithActionParameters(map, params, FIELDS_TO_COPY);
        return map;

    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        super.populateContextForCreate(context);
        context.put("useWrapper", true);
        context.put("gradleTask", "build");
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);
        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS_TO_COPY);
    }

    @Override
    public void populateContextForView(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForView(context, taskDefinition);
        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS_TO_COPY);
    }

    // -------------------------------------------------------------------------------------------------- Action Methods
    // -------------------------------------------------------------------------------------------------- Public Methods
    // -------------------------------------------------------------------------------------- Basic Accessors / Mutators
}

package de.schramke.bamboo.plugins.gradlew.tasks;

import com.atlassian.bamboo.util.TextProviderUtils;
import com.opensymphony.xwork2.TextProvider;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;


import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.util.concurrent.Nullable;

import java.util.Map;

public class GradleTaskConfigurator extends AbstractTaskConfigurator {

    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition) {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);

        config.put("gradleTask", params.getString("gradleTask"));

        return config;
    }

    @Override
    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection) {
        super.validate(params, errorCollection);

        TextProvider textprovider = TextProviderUtils.getTextProvider();

        final String gradleTaskValue = params.getString("gradleTask");
        if (StringUtils.isEmpty(gradleTaskValue)) {
            errorCollection.addError("gradleTask", textprovider.getText("builder.gradle.task.error"));
        }
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        super.populateContextForCreate(context);

    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);

        context.put("gradleTask", taskDefinition.getConfiguration().get("gradleTask"));
    }

    @Override
    public void populateContextForView(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForView(context, taskDefinition);
        context.put("gradleTask", taskDefinition.getConfiguration().get("gradleTask"));
    }

}

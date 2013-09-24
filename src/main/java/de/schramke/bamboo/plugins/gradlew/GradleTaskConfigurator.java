package de.schramke.bamboo.plugins.gradlew;

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

public class GradleTaskConfigurator extends AbstractTaskConfigurator{

    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition)
    {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);

        config.put("say", params.getString("say"));

        return config;
    }

    @Override
    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection)
    {
        super.validate(params, errorCollection);

        final String sayValue = params.getString("say");
        if (StringUtils.isEmpty(sayValue))
        {
            TextProvider textprovider = TextProviderUtils.getTextProvider();
            errorCollection.addError("say", textprovider.getText("helloworld.say.error"));
        }
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context)
    {
        super.populateContextForCreate(context);

        context.put("say", "Hello, World!");
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition)
    {
        super.populateContextForEdit(context, taskDefinition);

        context.put("say", taskDefinition.getConfiguration().get("say"));
    }

    @Override
    public void populateContextForView(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition)
    {
        super.populateContextForView(context, taskDefinition);
        context.put("say", taskDefinition.getConfiguration().get("say"));
    }

}

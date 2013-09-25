package de.schramke.bamboo.plugins.gradlew;

import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
import de.schramke.bamboo.plugins.gradlew.tasks.GradleBuildTask;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GradleCapabilityDefaultsHelper implements CapabilityDefaultsHelper{
    private static final Logger log = LoggerFactory.getLogger(GradleCapabilityDefaultsHelper.class);

    // ------------------------------------------------------------------------------------------------------- Constants
    // ------------------------------------------------------------------------------------------------- Type Properties
    // ---------------------------------------------------------------------------------------------------- Dependencies
    // ---------------------------------------------------------------------------------------------------- Constructors
    // ----------------------------------------------------------------------------------------------- Interface Methods

    @NotNull
    @Override
    public CapabilitySet addDefaultCapabilities(@NotNull final CapabilitySet capabilitySet) {
        log.info("start addDefaultCapabilities");

        createCapabilityForGradle("Gradle 1.x",capabilitySet,"/usr/bin/");

        log.info("end addDefaultCapabilities");

        return capabilitySet;
    }

    // -------------------------------------------------------------------------------------------------- Action Methods
    // -------------------------------------------------------------------------------------------------- Public Methods
    // -------------------------------------------------------------------------------------- Basic Accessors / Mutators

    private void createCapabilityForGradle(@NotNull final String label, @NotNull final CapabilitySet capabilitySet, @Nullable final String path)
    {
        if (StringUtils.isNotEmpty(path))
        {
//            File file = new File(new File(path).getParentFile(), "IDE");
//            if (file.exists() && file.isDirectory())
//            {
                Capability capability = new CapabilityImpl(GradleBuildTask.GRADLE_CAPABILITY_PREFIX + "." + label, path);
                capabilitySet.addCapability(capability);
//            }
        }
    }
}

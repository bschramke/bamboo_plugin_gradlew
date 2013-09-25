[#-- @ftlvariable name="uiConfigSupport" type="com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport" --]

[@ww.checkbox labelKey='gradle.wrapper' name='useGradleWrapper' toggle='true'/]

[#assign addExecutableLink][@ui.displayAddExecutableInline executableKey='gradle'/][/#assign]
[@ww.select cssClass="builderSelectWidget" labelKey='executable.type' name='label'
            list=uiConfigSupport.getExecutableLabels('gradle')
            extraUtility=addExecutableLink require='true'/]

[@ww.textarea labelKey='gradle.task' name='gradleTask' rows='2' required='true' cssClass="long-field" /]

[#assign addJdkLink][@ui.displayAddJdkInline /][/#assign]
[@ww.select cssClass="jdkSelectWidget"
            labelKey='builder.common.jdk' name='buildJdk'
            list=uiConfigSupport.jdkLabels required='true'
            extraUtility=addJdkLink /]

[@ww.textfield labelKey='builder.common.env' name='environmentVariables' cssClass="long-field" /]
[@ww.textfield labelKey='builder.common.sub' name='workingSubDirectory' cssClass="long-field" /]
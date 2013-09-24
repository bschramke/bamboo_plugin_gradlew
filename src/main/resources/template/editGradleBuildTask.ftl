[#-- @ftlvariable name="uiConfigSupport" type="com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport" --]

[@ww.checkbox labelKey='builder.gradle.wrapper' name='useGradleWrapper' toggle='true'/]
[#assign addExecutableLink][@ui.displayAddExecutableInline executableKey='gradle1'/][/#assign]
[@ww.select cssClass="builderSelectWidget" labelKey='executable.type' name='label'
            list=uiConfigSupport.getExecutableLabels('gradle1')
            extraUtility=addExecutableLink /]

[@ww.textarea labelKey='builder.gradle.task' name='gradleTask' rows='2' required='true' cssClass="long-field" /]

[#assign addJdkLink][@ui.displayAddJdkInline /][/#assign]
[@ww.select cssClass="jdkSelectWidget"
            labelKey='builder.common.jdk' name='buildJdk'
            list=uiConfigSupport.jdkLabels required='true'
            extraUtility=addJdkLink /]

[@ww.textfield labelKey='builder.common.env' name='environmentVariables' cssClass="long-field" /]
[@ww.textfield labelKey='builder.common.sub' name='workingSubDirectory' cssClass="long-field" /]

[#if !deploymentMode]
    [@ui.bambooSection titleKey='builder.common.tests.directory.description']
        [@ww.checkbox labelKey='builder.common.tests.exists' name='testChecked' toggle='true'/]

        [@ui.bambooSection dependsOn='testChecked' showOn='true']
            [@ww.radio labelKey='builder.common.tests.directory' name='testDirectoryOption'
                       listKey='key' listValue='value' toggle='true'
                       list=testDirectoryTypes ]
            [/@ww.radio]
            [@ui.bambooSection dependsOn='testDirectoryOption' showOn='customTestDirectory']
                [@ww.textfield labelKey='builder.common.tests.directory.custom' name='testResultsDirectory' cssClass="long-field" /]
            [/@ui.bambooSection]
        [/@ui.bambooSection]
    [/@ui.bambooSection]
[#else]
    <p>[@ww.text name='builder.common.deployment.test.disabled' /]</p>
[/#if]

[@ui.bambooSection titleKey='repository.advanced.option' collapsible=true isCollapsed=!(useMavenReturnCode?has_content || projectFile?has_content)]
    [@ww.checkbox labelKey='builder.maven.return' descriptionKey='builder.maven.return.description' name='useMavenReturnCode' /]
    [@ww.textfield labelKey='builder.maven2.projectFile' name='projectFile' cssClass="long-field" /]
[/@ui.bambooSection]
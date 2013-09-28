[#-- @ftlvariable name="uiConfigSupport" type="com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport" --]

[@ww.checkbox labelKey='gradle.wrapper' descriptionKey='builder.maven.return.description' name='useWrapper' toggle='true'/]
[@ui.bambooSection dependsOn='useWrapper' showOn='false']
    [@ww.textfield labelKey="xcode.target" name="target" cssClass="long-field"/]
    [@ww.select cssClass="builderSelectWidget" labelKey='gradle.executable' name='label'
                list=uiConfigSupport.getExecutableLabels('gradle')
                extraUtility=addExecutableLink required='true' /]
[/@ui.bambooSection]

[@ww.textarea labelKey='gradle.task' name='gradleTask' rows='2' required='true' cssClass="long-field" /]

[@ww.textfield labelKey='builder.common.env' name='environmentVariables' cssClass="long-field" /]
[@ww.textfield labelKey='builder.common.sub' name='workingSubDirectory' cssClass="long-field" /]

[@ui.bambooSection titleKey='repository.advanced.option' collapsible=true isCollapsed=!(logLevel?has_content || stackTraceOutput?has_content)]
    [@ww.textfield labelKey='gradle.logLevel' name='logLevel' cssClass="long-field" /]
    [@ww.textfield labelKey='gradle.stacktrace' name='stackTraceOutput' cssClass="long-field" /]
[/@ui.bambooSection]

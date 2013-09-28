[#if useWrapper == 'true']
    [@ww.label labelKey="xcode.target" value='Using gradle wrapper' /]
[#else]
    [@ww.label labelKey="xcode.target" value='Using some other' /]
[/#if]

[@ww.label labelKey='gradle.task' name='gradleTask'/]
[@ww.label labelKey='builder.common.env' name='environmentVariables'/]
[@ww.label labelKey='builder.common.sub' name='workingSubDirectory'/]


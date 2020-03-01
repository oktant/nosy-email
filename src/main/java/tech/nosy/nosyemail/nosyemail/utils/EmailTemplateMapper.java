package tech.nosy.nosyemail.nosyemail.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.nosy.nosyemail.nosyemail.dto.EmailTemplateDto;
import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;

import java.util.List;

@Mapper(uses = EmailConfigMapper.class)
public abstract class EmailTemplateMapper {
    public static final EmailTemplateMapper INSTANCE = Mappers.getMapper(EmailTemplateMapper.class);

    @Mapping(source = "emailTemplateName", target = "name")
    @Mapping(source = "emailTemplateFromAddress", target = "fromAddress")
    @Mapping(source = "emailTemplateTo", target = "to")
    @Mapping(source = "emailTemplateCc", target = "cc")
    @Mapping(source = "emailTemplateSubject", target = "subject")
    @Mapping(source = "emailTemplateText", target = "text")
    @Mapping(source = "emailTemplateFromProvider", target = "fromProvider")
    @Mapping(source = "emailTemplatePriority", target = "priority")
    @Mapping(source = "emailTemplateRetryTimes", target = "retryTimes")
    @Mapping(source = "emailTemplateRetryPeriod", target = "retryPeriod")
    @Mapping(source = "emailConfig.emailConfigName", target = "configName")
    public abstract EmailTemplateDto toEmailTemplateDto(EmailTemplate emailTemplate);

    public abstract List<EmailTemplateDto> toEmailTemplateDtoList(List<EmailTemplate> emailTemplateList);

    @Mapping(source = "name", target = "emailTemplateName")
    @Mapping(source = "fromAddress", target = "emailTemplateFromAddress")
    @Mapping(source = "to", target = "emailTemplateTo")
    @Mapping(source = "cc", target = "emailTemplateCc")
    @Mapping(source = "text", target = "emailTemplateText")
    @Mapping(source = "subject", target = "emailTemplateSubject")
    @Mapping(source = "fromProvider", target = "emailTemplateFromProvider")
    @Mapping(source = "priority", target = "emailTemplatePriority")
    @Mapping(source = "retryTimes", target = "emailTemplateRetryTimes")
    @Mapping(source = "retryPeriod", target = "emailTemplateRetryPeriod")
    public abstract EmailTemplate toEmailTemplate(EmailTemplateDto emailTemplateDto);

}

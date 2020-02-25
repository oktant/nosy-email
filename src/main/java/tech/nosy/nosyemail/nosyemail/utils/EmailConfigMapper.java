package tech.nosy.nosyemail.nosyemail.utils;

import org.hibernate.annotations.Type;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.nosy.nosyemail.nosyemail.dto.EmailConfigDto;
import tech.nosy.nosyemail.nosyemail.dto.EmailServerPropertyDto;
import tech.nosy.nosyemail.nosyemail.dto.EmailTemplateDto;
import tech.nosy.nosyemail.nosyemail.model.EmailConfig;
import tech.nosy.nosyemail.nosyemail.model.EmailServerProperty;
import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Mapper(uses= {EmailServerPropertyMapper.class})
public abstract class EmailConfigMapper {
    public static final EmailConfigMapper INSTANCE = Mappers.getMapper(EmailConfigMapper.class);
    @Mapping(source = "emailConfigName", target = "name")
    public abstract EmailConfigDto toEmailConfigDto(EmailConfig emailConfig);

    @Mapping(source = "name", target = "emailConfigName")
    public abstract EmailConfig toEmailConfig(EmailConfigDto emailConfigDto);

    public abstract List<EmailConfigDto> toEmailConfigDtoList(List<EmailConfig> emailConfigList);
    public abstract List<EmailConfig> toEmailConfigList(List<EmailConfigDto> emailConfigDtoList);

}



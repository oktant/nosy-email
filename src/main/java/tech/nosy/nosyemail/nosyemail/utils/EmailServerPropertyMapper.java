package tech.nosy.nosyemail.nosyemail.utils;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.nosy.nosyemail.nosyemail.dto.EmailServerPropertyDto;
import tech.nosy.nosyemail.nosyemail.model.EmailServerProperty;

@Mapper
public abstract class EmailServerPropertyMapper {
    public static final EmailServerPropertyMapper INSTANCE = Mappers.getMapper(EmailServerPropertyMapper.class);

    public abstract EmailServerProperty toEmailServerProperty(EmailServerPropertyDto emailServerPropertyDto);
    public abstract EmailServerPropertyDto toEmailServerPropertyDto
            (EmailServerProperty emailServerProperty);
}

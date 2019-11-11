package tech.nosy.nosyemail.nosyemail.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.nosy.nosyemail.nosyemail.dto.EmailCollectionDto;
import tech.nosy.nosyemail.nosyemail.model.EmailCollection;

import java.util.List;

@Mapper
public abstract class EmailCollectionMapper {
    public static final EmailCollectionMapper INSTANCE = Mappers.getMapper(EmailCollectionMapper.class);

            @Mapping(source = "emailCollectionId", target = "id")
            @Mapping(source = "emailCollectionName", target = "name")
            @Mapping(source = "emailCollectionEmails", target = "emails")

    public abstract EmailCollectionDto toEmailCollectionDto(EmailCollection emailCollection);

            @Mapping(source = "id", target = "emailCollectionId")
            @Mapping(source = "name", target = "emailCollectionName")
            @Mapping(source = "emails", target = "emailCollectionEmails")

    public abstract EmailCollection toEmailCollection(EmailCollectionDto emailCollectionDto);

    public abstract List<EmailCollectionDto> toEmailCollectionDtoList(List<EmailCollection> emailCollectionList);
}

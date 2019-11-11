package tech.nosy.nosyemail.nosyemail.utils;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.nosy.nosyemail.nosyemail.config.security.TokenCollection;
import tech.nosy.nosyemail.nosyemail.dto.TokenCollectionDto;

@Mapper
public abstract class TokenCollectionMapper {
    public static final TokenCollectionMapper INSTANCE = Mappers.getMapper( TokenCollectionMapper.class );
    public abstract TokenCollectionDto toTokenCollectionDto(TokenCollection tokenCollection);
}

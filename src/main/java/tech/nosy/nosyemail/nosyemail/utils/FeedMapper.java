package tech.nosy.nosyemail.nosyemail.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.nosy.nosyemail.nosyemail.dto.FeedDto;
import tech.nosy.nosyemail.nosyemail.model.Feed;
import java.util.List;

@Mapper
public abstract class FeedMapper {

    public static final FeedMapper INSTANCE = Mappers.getMapper(FeedMapper.class);

    @Mapping(source = "feedId", target = "id")
    @Mapping(source = "feedName", target = "name")
    @Mapping(source = "feedSubscribers", target = "subscribers")
    public abstract FeedDto toFeedDto(Feed feed);

    @Mapping(source = "id", target = "feedId")
    @Mapping(source = "name", target = "feedName")
    @Mapping(source = "subscribers", target = "feedSubscribers")
    public abstract Feed toFeed(FeedDto feedDto);

    public abstract List<FeedDto> toFeedDtoList(List<Feed> feedList);

}

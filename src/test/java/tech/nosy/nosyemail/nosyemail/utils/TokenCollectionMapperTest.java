package tech.nosy.nosyemail.nosyemail.utils;

import org.junit.Test;
import tech.nosy.nosyemail.nosyemail.config.security.TokenCollection;
import tech.nosy.nosyemail.nosyemail.dto.TokenCollectionDto;

import static org.junit.Assert.assertEquals;

public class TokenCollectionMapperTest {

    private TokenCollection tokenCollection=new TokenCollection();
    @Test
    public void testToTokenCollection(){
        tokenCollection.setAccessToken("token");
        tokenCollection.setExpiresIn(1);
        tokenCollection.setNotBeforePolicy(2);
        tokenCollection.setRefreshExpiresIn(2);
        tokenCollection.setRefreshToken("refreshtoken");
        tokenCollection.setSessionState("dasdas");
        tokenCollection.setTokenType("type");
        TokenCollectionDto tokenCollectionDto=TokenCollectionMapper.INSTANCE.toTokenCollectionDto(tokenCollection);
        assertEquals(tokenCollectionDto.getAccessToken(), tokenCollection.getAccessToken());
        assertEquals(tokenCollectionDto.getExpiresIn(), tokenCollection.getExpiresIn());
        assertEquals(tokenCollectionDto.getRefreshToken(), tokenCollection.getRefreshToken());


    }

}

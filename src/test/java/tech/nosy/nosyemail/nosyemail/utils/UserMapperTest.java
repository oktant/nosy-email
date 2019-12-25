//package tech.nosy.nosyemail.nosyemail.utils;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//import tech.nosy.nosyemail.nosyemail.dto.UserDto;
//import tech.nosy.nosyemail.nosyemail.model.InputSystem;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(MockitoJUnitRunner.class)
//public class UserMapperTest {
//    private UserDto userDto=new UserDto();
//    private User user=new User();
//    @Before
//    public void setUp(){
//        InputSystem inputSystem=new InputSystem();
//        inputSystem.setInputSystemId("dadasdsa");
//        Set<InputSystem> inputSystemSet=new HashSet<>();
//        user.setEmail("TestUser");
//        user.setPassword("TestUserPassword");
//        user.setLastName("TestUserLastName");
//        user.setFirstName("TestUserFirstName");
//        user.setInputSystem(inputSystemSet);
//
//        userDto.setEmail("TestUserDto");
//        userDto.setPassword("TestUserDtoPassword");
//        userDto.setLastName("TestUserDtoLastName");
//        userDto.setFirstName("TestUserDtoFirstName");
//    }
//    @Test
//    public void toUserDto(){
//        Assert.assertEquals(user.getEmail(), UserMapper.INSTANCE.toUserDto(user).getEmail());
//        Assert.assertEquals(user.getPassword(), UserMapper.INSTANCE.toUserDto(user).getPassword());
//        Assert.assertEquals(user.getFirstName(), UserMapper.INSTANCE.toUserDto(user).getFirstName());
//        Assert.assertEquals(user.getLastName(), UserMapper.INSTANCE.toUserDto(user).getLastName());
//
//    }
//
//
//    @Test
//    public void toUser(){
//        Assert.assertEquals(userDto.getEmail(), UserMapper.INSTANCE.toUser(userDto).getEmail());
//        Assert.assertEquals(userDto.getPassword(), UserMapper.INSTANCE.toUser(userDto).getPassword());
//        Assert.assertEquals(userDto.getFirstName(), UserMapper.INSTANCE.toUser(userDto).getFirstName());
//        Assert.assertEquals(userDto.getLastName(), UserMapper.INSTANCE.toUser(userDto).getLastName());
//
//    }
//
//
//}
//
//
//

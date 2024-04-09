package blog_Application.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import blog_Application.Model.User;
import blog_Application.Paylaod.UserDto;

public interface UserService {
	
	
	public UserDto create(UserDto userDto);
	public UserDto getUser(long id);
	public List<User> getAllUser(int pageNumber ,int pageSize);
	public UserDto Update(UserDto userDto ,long id);
	public String Delete(long id);

}

package blog_Application.Paylaod;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import blog_Application.Model.User;
import lombok.Data;


@Data
public class PostDto {
	
	private  long postid;
	
	private String title;
	
	private String content;
	
	private Date addDate;
	
	private UserDto userdto;
	
	
	private String image;
	
	private CategoryDto categoryDto;
	
	@JsonBackReference
	private List<CommentDto> comments;

	
	
	
	public void setUserDtoId(long userid)
	{
		if(this.userdto==null)
		{
			this.userdto =new UserDto();
		}
	userdto.setId(userid);
	}
	
	public void setUserDtoName(String name)
	{
		if(this.userdto==null)
		{
			this.userdto =new UserDto();
		}
	userdto.setName(name);;
	}
	public void setUserDtoEmail(String email)
	{
		if(this.userdto==null)
		{
			this.userdto =new UserDto();
		}
	userdto.setEmail(email);;
	}
	public void setUserDtoPassword(String password)
	{
		if(this.userdto==null)
		{
			this.userdto =new UserDto();
		}
	userdto.setPassword(password);;
	}
	public void setUserDtoAbout(String about)
	{
		if(this.userdto==null)
		{
			this.userdto =new UserDto();
		}
	userdto.setAbout(about);
	}
	
	public void setCategoryDtoId(long id)
	{
		if(this.categoryDto==null)
		{
			this.categoryDto=new CategoryDto();
		}
		categoryDto.setCatid(id);
	}
	public void setCategoryDtoName(String name)
	{
		if(this.categoryDto==null)
		{
			this.categoryDto=new CategoryDto();
		}
		categoryDto.setCatname(name);;
	}
	public void setCategoryDtoAbout(String about)
	{
		if(this.categoryDto==null)
		{
			this.categoryDto=new CategoryDto();
		}
		categoryDto.setCatabout(about);;
	}
	
}

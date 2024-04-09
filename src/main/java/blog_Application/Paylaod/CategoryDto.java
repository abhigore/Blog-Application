package blog_Application.Paylaod;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CategoryDto {

	
	private long catid;

	@NotEmpty
	@Size(min=2,message ="Category name must be more than 2 characters")
	private String catname;

	private String catabout;


}

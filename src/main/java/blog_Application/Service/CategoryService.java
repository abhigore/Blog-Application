package blog_Application.Service;

import java.util.List;

import blog_Application.Model.Category;
import blog_Application.Paylaod.CategoryDto;

public interface CategoryService {
	
	public CategoryDto create(CategoryDto categoryDto);
	public CategoryDto getOne(long catid);
	public  String delete(long catid);
	public CategoryDto update(CategoryDto categoryDto ,long catid);
	public List<Category> getAll(int pageNumber ,int pageSize);

}

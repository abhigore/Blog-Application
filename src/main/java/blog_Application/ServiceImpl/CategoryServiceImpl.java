package blog_Application.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import blog_Application.Exception.ResourceNotFoundException;
import blog_Application.Model.Category;
import blog_Application.Paylaod.CategoryDto;
import blog_Application.Repository.CategoryRepo;
import blog_Application.Service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	@Lazy
	private CategoryRepo categoryRepo;
	
	private ModelMapper mapper =new ModelMapper();
	

	@Override
	public CategoryDto create(CategoryDto categoryDto) {
		
		Category category =catDtoToCategory(categoryDto);
		
		Category savecat = categoryRepo.save(category);
		 CategoryDto categoryDto2 = catToCategoryDto(savecat);
		// categoryDto2.setCatid(save.getCatid());
		 
		
		return categoryDto2;
	}

	@Override
	public CategoryDto getOne(long catid) {
		Category cat = categoryRepo.findByCatid(catid).orElseThrow(()-> new ResourceNotFoundException("Category", "Category id", catid));
		CategoryDto categoryDto = catToCategoryDto(cat);
		return categoryDto;
	}

	@Override
	public String delete(long catid) {
		Category cat =new Category();
		Optional<Category> cat1= categoryRepo.findByCatid(catid);
		
		if(cat1.isPresent())
		{
			cat =cat1.get();
		}
		else {
			throw new ResourceNotFoundException("Category", "category id ", catid);
		}
		
		
		categoryRepo.delete(cat);
		return "Category Deleted Successfully" + catid;
	}

	@Override
	public CategoryDto update(CategoryDto categoryDto ,long catid) {
		Category category = categoryRepo.findByCatid(catid).orElseThrow(()->new ResourceNotFoundException("Category", "Category Id", catid));
		if(categoryDto.getCatname()!=null)
		{
			category.setCatname(categoryDto.getCatname());
		}
		if(categoryDto.getCatabout()!=null)
		{
			category.setCatabout(categoryDto.getCatabout());
		}
		
		categoryRepo.saveAndFlush(category);
		CategoryDto categoryDto1 = catToCategoryDto(category);
		
		
		return categoryDto1;
	}

	@Override
	public List<Category> getAll(int pageNumber ,int pageSize) {
		Pageable r =PageRequest.of(pageNumber, pageSize);
		 Page<Category> categoryPerPage = categoryRepo.findAll(r);	
		 List<Category> list =categoryPerPage.getContent();
		
		return list;
	}
	
	public Category catDtoToCategory(CategoryDto categoryDto)
	{
		
		Category  cat = mapper.map(categoryDto, Category.class);
		
		
	
		return cat;
	}
	
	public CategoryDto catToCategoryDto(Category category)
	{
		CategoryDto categoryDto =mapper.map(category, CategoryDto.class);
		return categoryDto;
	}

}

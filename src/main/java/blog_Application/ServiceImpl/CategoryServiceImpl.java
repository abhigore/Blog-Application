package blog_Application.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
	private CategoryRepo catRepo;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public CategoryDto create(CategoryDto categoryDto) {
		
		Category category =catDtoToCategory(categoryDto);
		catRepo.save(category);
		 CategoryDto categoryDto2 = catToCategoryDto(category);
		 categoryDto2.setCatid(category.getCatid());
		return categoryDto2;
	}

	@Override
	public CategoryDto getOne(long catid) {
		Category cat = catRepo.findByCatid(catid).orElseThrow(()-> new ResourceNotFoundException("Category", "Category id", catid));
		CategoryDto categoryDto = catToCategoryDto(cat);
		return categoryDto;
	}

	@Override
	public String delete(long catid) {
		Category cat = catRepo.findByCatid(catid).orElseThrow(()-> new ResourceNotFoundException("Category", "Category id", catid));
		
		catRepo.delete(cat);
		return "Category Deleted Successfully" + catid;
	}

	@Override
	public CategoryDto update(CategoryDto categoryDto ,long catid) {
		Category category = catRepo.findByCatid(catid).orElseThrow(()->new ResourceNotFoundException("Category", "Category Id", catid));
		if(categoryDto.getCatname()!=null)
		{
			category.setCatname(categoryDto.getCatname());
		}
		if(categoryDto.getCatabout()!=null)
		{
			category.setCatabout(categoryDto.getCatabout());
		}
		
		catRepo.saveAndFlush(category);
		CategoryDto categoryDto1 = catToCategoryDto(category);
		
		
		return categoryDto1;
	}

	@Override
	public List<Category> getAll(int pageNumber ,int pageSize) {
		Pageable r =PageRequest.of(pageNumber, pageSize);
		 Page<Category> categoryPerPage = catRepo.findAll(r);	
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

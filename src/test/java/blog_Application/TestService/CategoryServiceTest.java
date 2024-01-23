package blog_Application.TestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import TestConfig.MyTestConfig;
import blog_Application.Model.Category;
import blog_Application.Paylaod.CategoryDto;
import blog_Application.Repository.CategoryRepo;
import blog_Application.ServiceImpl.CategoryServiceImpl;


@ExtendWith(MockitoExtension.class)

public class CategoryServiceTest {

	@Mock
	private CategoryRepo categoryRepo;

	@InjectMocks
	private CategoryServiceImpl catService;

	private ModelMapper mapper =new ModelMapper();

	Logger logger =LoggerFactory.getLogger(CategoryServiceTest.class);



	@Test
	public void createCategoryTest()
	{

		long catid =1L;
		CategoryDto catDto = new CategoryDto();
		catDto.setCatid(catid);
		catDto.setCatname("Sport");
		catDto.setCatabout("This category is about to display Sport related information");

		Category category = mapper.map(catDto, Category.class);


		when(categoryRepo.save(any(Category.class))).thenReturn(category);

		CategoryDto expectedResult = mapper.map(category ,CategoryDto.class);

		logger.info("create category expected_result =" +expectedResult.getCatname());

		CategoryDto result = catService.create(catDto);
		logger.info("create category result =" +result.getCatname());
		assertEquals(expectedResult, result);
		logger.info("----------------------------------------------------------------------------------------");
	}


	@Test
	public void getOneCategoryTest()
	{

		long catid =1L;
		CategoryDto catDto = new CategoryDto();
		catDto.setCatid(catid);
		catDto.setCatname("Sport related");
		catDto.setCatabout("This category is about to display Sport related information");


		ModelMapper mapper1 =Mockito.mock(ModelMapper.class);
		Category category =mapper.map(catDto,Category.class);


		when(categoryRepo.findByCatid(catid)).thenReturn(java.util.Optional.of(category));

		CategoryDto expectedResult =mapper.map(category ,CategoryDto.class);

		logger.info(" get_One_Category expected_result = " + expectedResult.getCatname());

		CategoryDto result = catService.getOne(catid);
		logger.info(" get_One_Category result = " + result.getCatname());


		assertEquals(expectedResult, result);
		logger.info("----------------------------------------------------------------------------------------");


	}

	@Test
	public void getAllCategoryTest()
	{
		long catid =1L;
		int pageNumber =0;
		int pageSize =1;
		CategoryDto catDto = new CategoryDto();
		catDto.setCatid(catid);
		catDto.setCatname("Sport related");
		catDto.setCatabout("This category is about to display Sport related information");
		Category category = mapper.map(catDto, Category.class);
		List<Category>categories =new ArrayList<>();
		categories.add(category);
		Pageable r =PageRequest.of(pageNumber, pageSize);
		when(categoryRepo.findAll(r)).thenReturn(new PageImpl<Category>(categories));

		logger.info("expected_result of get All categores =" + categories);

		List<Category> result = catService.getAll(pageNumber, pageSize);
		logger.info(" actual result of get All categores =" + result);

		assertEquals(categories,result);

		logger.info("----------------------------------------------------------------------------------------");

	}

	@Test
	public void updateCatregoryTest()
	{ 
		long catid=1L;
		CategoryDto catDto = new CategoryDto();
		catDto.setCatid(catid);
		catDto.setCatname("Sport related");
		catDto.setCatabout("This category is about to display Sport related information");

		Category category = mapper.map(catDto, Category.class);

		CategoryDto dto =new CategoryDto();

		dto.setCatname("Education related");

		when(categoryRepo.findByCatid(catid)).thenReturn(java.util.Optional.of(category));

		if(dto.getCatname()!=null)
		{
			category.setCatname(dto.getCatname());
		}

		when(categoryRepo.saveAndFlush(category)).thenReturn(category);

		CategoryDto expectedResult = mapper.map(category,CategoryDto.class);

		logger.info("expected_result from Update category = " + expectedResult.getCatname());

		CategoryDto result = catService.update(dto, catid);
		logger.info(" Actual Result from update category= " + result.getCatname());

		assertEquals(expectedResult,result);
		logger.info("----------------------------------------------------------------------------------------");
	}


	@Test
	public void deleteCategoryTest()
	{
		long catid=1L;
		CategoryDto catDto = new CategoryDto();
		catDto.setCatid(catid);
		catDto.setCatname("Sport related");
		catDto.setCatabout("This category is about to display Sport related information");

		Category category = mapper.map(catDto, Category.class);

		when(categoryRepo.findByCatid(catid)).thenReturn(java.util.Optional.of(category));

		catService.delete(catid);

		verify(categoryRepo,times(1)).delete(category);

		logger.info("----------------------------------------------------------------------------------------");
	}

}

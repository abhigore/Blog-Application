package blog_Application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog_Application.Model.Category;
import blog_Application.Paylaod.CategoryDto;
import blog_Application.ServiceImpl.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@SecurityRequirement(name="scheme1")
@RequestMapping("/category")
@Tag(name="Category Controller")
public class CategoryController {

	@Autowired
	private CategoryServiceImpl serviceImpl;
	
	@PostMapping
	public ResponseEntity<CategoryDto>create(@Valid @RequestBody CategoryDto categoryDto)
	{
		return new ResponseEntity<CategoryDto>(serviceImpl.create(categoryDto),HttpStatus.CREATED);
	}
	
	@GetMapping("/{catid}")
	public ResponseEntity<CategoryDto>getOne(@PathVariable long catid )
	{
		return new ResponseEntity<CategoryDto>(serviceImpl.getOne(catid),HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<Category>>getAll(
			@RequestParam(name ="pageNumber" ,defaultValue = "0" ,required = false) int pageNumber,
			@RequestParam(name="pageSize" ,defaultValue = "2",required = false) int pageSize)
	{
		return new ResponseEntity<>(serviceImpl.getAll(pageNumber,pageSize),HttpStatus.OK);
	}
	
	@PutMapping("/{catid}")
	public ResponseEntity<CategoryDto>update(@RequestBody CategoryDto categoryDto ,@PathVariable long catid)
	{
		return new ResponseEntity<CategoryDto>(serviceImpl.update(categoryDto,catid),HttpStatus.OK);
	}
	
	@DeleteMapping("/{catid}")
	public ResponseEntity<String> delete(@PathVariable long catid)
	{
		return new ResponseEntity<String>(serviceImpl.delete(catid),HttpStatus.OK);
	}
	
}

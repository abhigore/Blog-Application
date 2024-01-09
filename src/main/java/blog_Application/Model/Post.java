package blog_Application.Model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
 @Entity
 @Table(name = "Post")
 @Data
public class Post {

	 @Id 
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long postid;
	
	@Column(name = "post_title")
	private String title;
	
	private String content;
	
	private String image;
	

	private Date addDate;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	@JsonBackReference
	private Category category;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToMany(mappedBy ="post" ,cascade =  CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<Comment> comments;
	
	
	
}

package blog_Application.Model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name ="User" )
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private  String about;

	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<Post> posts = new ArrayList();
	

	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<Comment>user_comments =new ArrayList<>();
	
	
	@ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType. EAGER)
	@JsonManagedReference
	@JoinTable(
			joinColumns = @JoinColumn(name= "user_id",referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name="role_id" ,referencedColumnName = "roleid")
			)
	private Set<Role>roles =new HashSet<>();
	
	
}

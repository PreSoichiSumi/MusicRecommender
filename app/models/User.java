package models;

import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.*;

@Entity
public class User extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long user_id;
	
	public String name;
	

	@ManyToMany(cascade=CascadeType.ALL)
	public List<Music> sung;
	
	public User(){
		
	}

	public User(Long user_id, String name, List<Music> sung) {
		this.user_id = user_id;
		this.name = name;
		this.sung = sung;
	}
	
	
	public static Finder<Long, User> find = new Finder<>(User.class);
}

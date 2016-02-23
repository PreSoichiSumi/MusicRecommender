package models;

import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Account extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long account_id;
	
	public String name;
	

	@ManyToMany(cascade=CascadeType.ALL)
	@JsonManagedReference
	public List<Music> sung;
	
	public Account(){
		
	}

	public Account(Long user_id, String name, List<Music> sung) {
		this.account_id = user_id;
		this.name = name;
		this.sung = sung;
	}
	
	
	public static Finder<Long, Account> find = new Finder<>(Account.class);
}

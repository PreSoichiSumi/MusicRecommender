package models;

import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Account extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long account_id;
	
	public String name;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="account", fetch=FetchType.LAZY)
	@JsonIgnore
	public List<SungMusic> sung;
	
	public Account(){
		
	}

	public Account(Long user_id, String name) {
		this.account_id = user_id;
		this.name = name;
	}
	
	
	public static Finder<Long, Account> find = new Finder<>(Account.class);
}

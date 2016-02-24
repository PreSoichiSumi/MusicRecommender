package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;

@Entity
public class Account extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long account_id;

	public String name;

	public Long roomId;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="account", fetch=FetchType.LAZY)
	public List<SungMusic> sung;

	public Account(){

	}

	public Account(Long user_id, String name) {
		this.account_id = user_id;
		this.name = name;
	}


	public static Finder<Long, Account> find = new Finder<>(Account.class);
}

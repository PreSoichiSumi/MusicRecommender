package models;

import javax.persistence.*;

import com.avaje.ebean.Model;

@Entity
public class Room extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long room_id;
	
	public String name;
	
}

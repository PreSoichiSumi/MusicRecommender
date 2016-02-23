
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import play.libs.Json;
import play.libs.Yaml;
import play.mvc.Result;
import play.test.Helpers;

import static play.test.Helpers.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import models.Music;
import models.Account;

public class DatabaseTest {

	private Account u1;
	private Account u2;
	private Music music1;
	private Music music2;
	private Music music3;
	private Music music4;
	private Music music5;
	private ArrayList<Music> l1;
	private ArrayList<Music> l2;

	@Before
	public void setUp() throws Exception {
		start(fakeApplication(inMemoryDatabase()));

		music1 = new Music(1l,"A","a");
		music1.save();
		music2 = new Music(2l,"B","b");
		music2.save();
		music3 = new Music(3l,"C","c");
		music3.save();
		music4 = new Music(4l,"D","d");
		//music4.save();
		music5 = new Music(5l,"E","e");
		//music5.save();

		l1 = new ArrayList<>();
		l1.add(music1);
		l1.add(music2);

		l2 = new ArrayList<>();
		l2.add(music2);
		l2.add(music3);
	
		
		u1 = new Account(1l, "Alice", /*l1);//*/new ArrayList<>());
		u2 = new Account(2l, "Bob", /*l2);//*/ new ArrayList<>());
		u1.save();
		u2.save();
		
		
		u1.sung = l1;
		u1.update();
		
		u2.sung = l2;
		u2.update();
		//Ebean.save(list);
	}

	@Test
	public void test() {
		for(Account u : Account.find.all()){
			System.out.println(u.account_id);
		}
		
		Account uu = Account.find.byId(1l);
		assertThat(uu.account_id,
				is(u1.account_id));
		assertThat(uu.name, is(u1.name));
		assertThat(uu.sung.get(0).music_id, 
				is(u1.sung.get(0).music_id));
		assertThat(uu.sung.get(1).music_id,
				is(u1.sung.get(1).music_id));
		

		uu = Account.find.byId(2l);
		assertThat(uu.account_id, is(u2.account_id));
		assertThat(uu.name, is(u2.name));
		assertThat(uu.sung.get(0).music_id, is(u2.sung.get(0).music_id));
		assertThat(uu.sung.get(1).music_id, is(u2.sung.get(1).music_id));
	}

}

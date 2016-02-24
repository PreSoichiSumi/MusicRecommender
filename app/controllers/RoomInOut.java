package controllers;

import models.Account;
import models.NowRoom;
import models.Room;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.ExpressionList;

public class RoomInOut extends Controller{
	public Result in(Long account_id, String room_name){
		Account account = Account.find.byId(account_id);
		if(account == null){
			return badRequest();
		}
		
		leaveRoom(account);
		
		NowRoom nowroom = NowRoom.find.where().eq("room_name", room_name).findUnique();
		if(nowroom == null){
			return badRequest();
		}
		account.room_id = nowroom.room_id;
		account.update();
		
		return ok();
	}
	
	public Result out(Long account_id){
		Account account = Account.find.byId(account_id);
		if(account == null){
			return badRequest();
		}
		if(leaveRoom(account)){
			return ok();
		}else{
			return badRequest();
		}
	}
	
	public boolean leaveRoom(Account account){
		if(account.room_id == null || account.room_id == 0l){
			return false;
		}
		
		Long rid = account.room_id;
		account.room_id = null;
		account.update();
		
		
		if(Account.find.where().eq("room_id", rid).findRowCount() == 0){
			//NowRoomからroomidを削除
			NowRoom.find.where().eq("room_id", rid).findUnique().delete();
		}
		
		return true;
	}
	
}

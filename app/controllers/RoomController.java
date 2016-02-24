package controllers;

import models.Account;
import models.NowRoom;
import models.Room;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.ExpressionList;

public class RoomController extends Controller{
	public Result createRoom(String roomName, Long userID){
		int rowCnt = NowRoom.find.where().eq("room_name", roomName).findRowCount();
    	if(rowCnt != 0){
    		return badRequest();
    	}
    	Room roomTmp=new Room(0L, roomName);
    	roomTmp.save();
    	NowRoom nowRoomTmp=new NowRoom(roomTmp.room_id, roomName);
    	nowRoomTmp.save();
    	Account accountTmp = Account.find.byId(userID);
    	if(accountTmp==null)
    		return internalServerError("userId is missing when creating room");

    	accountTmp.room_id=roomTmp.room_id;
    	accountTmp.update();

        return ok();
	}
	
	public Result in(Long account_id, String room_name){
		Account account = Account.find.byId(account_id);
		if(account == null){
			return badRequest();
		}
		
		Long rid = account.room_id;
		if(rid != null && rid != 0L){
			account.room_id = null;
			account.update();
		}		
		
		
		NowRoom nowroom = NowRoom.find.where().eq("room_name", room_name).findUnique();
		if(nowroom == null){
			return badRequest();
		}
		account.room_id = nowroom.room_id;
		account.update();
		

		if(rid != null && rid != 0L && Account.find.where().eq("room_id", rid).findRowCount() == 0){
			//NowRoomからroomidを削除
			NowRoom.find.where().eq("room_id", rid).findUnique().delete();
		}
		
		
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
	
	private boolean leaveRoom(Account account){
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

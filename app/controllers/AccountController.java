package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Account;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;

public class AccountController extends Controller {
	public Result post(){
		DynamicForm param = new DynamicForm().bindFromRequest();
		String name = param.get("name");
		if(name == null || name.equals("")) badRequest();
		
		Account ac = new Account(0l, name);
		ac.save();
		ObjectMapper om = new ObjectMapper();
		JsonNode jn = om.valueToTree(ac);
		return ok(jn);
	}
	
	public Result get(Long id){
		Account ac = Account.find.byId(id);
		if(ac == null) notFound();
		ObjectMapper om = new ObjectMapper();
		JsonNode jn = om.valueToTree(ac);
		return ok(jn);
	}
}

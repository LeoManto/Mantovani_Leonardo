package it.unibo.parkingmanagerservice.test.utils

object Tester {
	
	var clientState = ClientState.initial

	var changed : Boolean = false;
	
	var slotnum 	= -1
	var tokenID		= 0
	
	@Override
	fun setSlot(s : Int) : Unit {
		 slotnum = s
	}
	
	@Override
	fun getSlot() : Int{
		return  slotnum
	}
	
	@Override
	fun setToken(t : Int){
		 tokenID = t
	}
	
	@Override
	fun getToken() : Int {
		return  tokenID
	}
	
	@Override
	fun setclientstate(state : ClientState) : Unit{
		 clientState = state
		 setchanged(true)
	}

	@Override
	fun getclientstate() : ClientState{
		return  clientState
}
	
	@Override
	fun setchanged(c : Boolean) : Unit{
		 changed = c
	}
	
	@Override
	fun getchanged() : Boolean{
		return  changed
	}
	
}


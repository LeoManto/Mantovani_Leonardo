package it.unibo.ParkManagerService_s2

import java.math.BigInteger
import java.security.MessageDigest
import java.io.File

object ParkingSlotsKb {
	
	private val charPool : List<Char> = ('0'..'5') + ('6'..'9')
	
	var indoorFree = true
	var numFreeSlots = 6
	var firstFreeSlot = 1
	var slotsFree: BooleanArray = booleanArrayOf(true, true, true, true, true, true)
	
	
	fun setSlot(slot : Int, v : Boolean) : Unit{  //[per ora] prende in ingresso il numero dello slot [1,6]
		slotsFree.set(slot-1, v)  //in futuro questa elaborazione Token->slot sarà più complessa 
	}
	
	//è la funzione che cerca il primo slot libero disponibile
	fun findSlot() : Int{
		var i = 1	//è il numero dello slot, ma non la posizione nell'array (quella è i-1)
		for (v in slotsFree){
			if(v){
				firstFreeSlot = i
				return i
			}
			i++
		}
		return 0  // non si verifica perchè quando si genera il TOKENID c'è almeno uno slot libero
	}
	
	
	//è la funzione che calcola il numero di slots liberi
	fun checkSlots() : Int{
		var i = 0	//è il numero dello slot, ma non la posizione nell'array (quella è i-1)
		for (v in slotsFree){
			if(v){
				i++
			}	
		}
		numFreeSlots = i
		return i
	}
	
	fun generateToken(slot : Int) : String {
		val psw = (1..4)
				.map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
				.map(charPool::get)
				.joinToString("");
		val Token = slot.toString() + psw
		write(Token)
		return Token
	}
	
	
	fun checkToken(token : String) : Int {
		val fileName = "./tokens.txt"
		val myfile = File(fileName).readText(Charsets.UTF_8)
		if(myfile.contains(token))
			return token.substring(0,1).toInt()
		else
			return 0
	}
	
	fun write(token : String){
		val fileName = "./tokens.txt"
		val myfile = File(fileName)
		myfile.printWriter().use { out ->
        	out.println(token)
		}
	}
	
}
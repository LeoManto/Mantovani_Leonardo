package it.unibo.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.io.File
import java.io.InputStream
import java.io.PrintWriter

object ParkingSlotsKb {
	
	private val charPool : List<Char> = ('0'..'5') + ('6'..'9')
	
	var indoorFree = true
	var outdoorFree = true
	var numFreeSlots = 6
	var firstFreeSlot = 1
	var slotsFree: BooleanArray = booleanArrayOf(true, true, true, true, true, true)
	
/*------------------------------------------------------------------------------*/ 
	
	init{
		val fileName = "./tokens.txt"
		if(File(fileName).exists())
			File(fileName).delete() 
		else
			File(fileName)
	}

/*------------------------------------------------------------------------------*/
		
	
	fun setArea(s1:Boolean,s2:Boolean,s3:Boolean,s4:Boolean,s5:Boolean,s6:Boolean){
		slotsFree = booleanArrayOf(s1,s2,s3,s4,s5,s6)
	}
	
/*
*	Prende in ingresso il numero dello slot e il nuovo valore relativo al suo stato (true/false)
*	e lo modifica all'interno dell'array
 */	
	fun setSlot(slot : Int, v : Boolean) : Unit{
		slotsFree.set(slot-1, v) 
	}
	
	
/*	
*	è la funzione che cerca il primo slot libero disponibile
*/	
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
	
/*
*	Calcola il numero di slots liberi
*/
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
	
/*
*	Genera il TOKEN a partire dallo SLOTNUM e lo aggiunge nel file txt
*/ 		
	fun generateToken(slot : Int) : String {
		val psw = (1..4)
				.map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
				.map(charPool::get)
				.joinToString("");
		val Token = slot.toString() + psw
		write(Token)
		return Token
	}
	
/*
*	Controlla se il token inserito per il pickup è autentico,
*	Se lo è, ritorna il SLOTCARNUM,
*   altrimenti ritorna il valore 0.
*	Inoltre crea un nuovo file txt con tutti i Token tranne quello passato (se autentico)
*/
	fun checkToken(token : String) : Int {
		var slotnum = -1
		val fileName = "./tokens.txt"
		var myfile = File(fileName)
		var newFile: String = ""
		val lines: List<String> = myfile.readLines()
		lines.forEach{		
				if(it==token)
					slotnum = token.substring(0,1).toInt()
				else
					newFile = newFile+ it +"\n"	
		}
		myfile.delete()
		myfile = File(fileName)
		myfile.appendText(newFile)
		return slotnum
	}
	
/*
*	
*/ 		
	fun write(token : String){
		val fileName = "./tokens.txt"
		val myfile = File(fileName)
		myfile.appendText(token+"\n")
		}
	}
	

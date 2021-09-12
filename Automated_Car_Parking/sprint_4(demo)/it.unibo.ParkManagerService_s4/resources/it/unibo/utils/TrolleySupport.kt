package it.unibo.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.io.File
import java.io.InputStream
import java.io.PrintWriter
import itunibo.planner.plannerUtil
import mapRoomKotlin.mapUtil.map
import mapRoomKotlin.Box

object TrolleySupport {
	
	init{
		println("TrolleySupport | init ... ")
		
	}
	
	//calcolare il percorso per andare in una zona della mappa
	
	fun getPathPlan(goal : String) : Unit{
		
		when(goal){
			"home" -> {
				return plannerUtil.planForGoal("0", "0")
			}
			"outdoor" -> {
				return plannerUtil.planForGoal("6", "4")
			}
			"indoor" -> {
				return plannerUtil.planForGoal("6", "0")
			}
			"p1" -> {
				return plannerUtil.planForGoal("1", "1")
			}
			"p2" -> {
				return plannerUtil.planForGoal("4", "1")
			}
			"p3" -> {
				return plannerUtil.planForGoal("1", "2")
			}
			"p4" -> {
				return plannerUtil.planForGoal("4", "2")
			}
			"p5" -> {
				return plannerUtil.planForGoal("1", "3")
			}
			"p6" -> {
				return plannerUtil.planForGoal("4", "3")
			}
		}
	}
	
	fun exactDir(goal : String) : Long {
		var time : Long  = 0 
		when(goal){
			"home" -> {
				while(plannerUtil.getDirection()!="downDir"){
					unibo.robot.robotSupport.move( "r" )
					plannerUtil.updateMap(  "r" )
					time = time + 100
				}
				return time
			}
			"outdoor" -> {
				while(plannerUtil.getDirection()!="upDir"){
					unibo.robot.robotSupport.move( "r" )
					plannerUtil.updateMap(  "r" )
					time = time + 100
				}
				return time
			}
			"indoor" -> {
				while(plannerUtil.getDirection()!="downDir"){
					unibo.robot.robotSupport.move( "r" )
					plannerUtil.updateMap(  "r" )
					time = time + 100
				}
				return time
			}
			"p1" -> {
				while(plannerUtil.getDirection()!="leftDir"){
					unibo.robot.robotSupport.move( "r" )
					plannerUtil.updateMap(  "r" )
					time = time + 100
				}
				return time
			}
			"p2" -> {
				while(plannerUtil.getDirection()!="rightDir"){
					unibo.robot.robotSupport.move( "r" )
					plannerUtil.updateMap(  "r" )
					time = time + 100
				}
				return time
			}
			"p3" -> {
				while(plannerUtil.getDirection()!="leftDir"){
					unibo.robot.robotSupport.move( "r" )
					plannerUtil.updateMap(  "r" )
					time = time + 100
				}
				return time
			}
			"p4" -> {
				while(plannerUtil.getDirection()!="rightDir"){
					unibo.robot.robotSupport.move( "r" )
					plannerUtil.updateMap(  "r" )
					time = time + 100
				}
				return time
			}
			"p5" -> {
				while(plannerUtil.getDirection()!="leftDir"){
					unibo.robot.robotSupport.move( "r" )
					plannerUtil.updateMap(  "r" )
					time = time + 100
				}
				return time
			}
			"p6" -> {
				while(plannerUtil.getDirection()!="rightDir"){
					unibo.robot.robotSupport.move( "r" )
					plannerUtil.updateMap(  "r" )
					time = time + 100
				}
				return time
			}
			
		}
		return time
		}
	
	
	//Mettere l'ostacolo dove la macchina è appena stata parcheggiata
	
	fun checkObstacle(x : Int, y : Int) : Boolean{
		
		var pos = getObstaclePos(x, y)
		
		if(!map.isObstacle(pos.first, pos.second)){
			map.put(pos.first, pos.second, Box(true, false, false))
			return true	
		}else{
			return false
		}	
		
	}
	
	fun getObstaclePos(x : Int, y : Int) : Pair<Int, Int>{
		when(plannerUtil.getDirection()){
			
			"upDir"		-> 	return Pair(x, y-1)
		
			"downDir"	-> return Pair(x, y+1)
			
			"leftDir"	-> return Pair(x-1, y)
			
			"righttDir"	-> return Pair(x+1, y)
				
		}
		return Pair(-1, -1)
	}

/*===========================================================
				
	fun mapConfiguring() : Boolean{
		
		var parkMap = "parkingMap"
		
		var i : Int
		var j : Int
		for (i in 0..6){
			for (j in 0..4){
 				if(i==0 && j==0){println("RRRRRRRRRRRRRRRRRRR")}
 				else
 					map.put(i, j, Box(false, true, false))
			}
		}	
		for(i in 0..7){
			map.put(i, 5, Box(true, false, false))
		}
		
		for(i in 0..4){
			map.put(7, i, Box(true, false, false))
		}
		
		
		map.put(2, 1, Box(true, false, false))
		
		map.put(3, 1, Box(true, false, false))
		
		map.put(2, 2, Box(true, false, false))
		
		map.put(3, 2, Box(true, false, false))
		
		map.put(2, 3, Box(true, false, false))
		
		map.put(3, 3, Box(true, false, false))
			
		plannerUtil.showMap()
		plannerUtil.saveRoomMap(parkMap)
		
		return true
			
	}
 
 ========================================================*/
	
}
	

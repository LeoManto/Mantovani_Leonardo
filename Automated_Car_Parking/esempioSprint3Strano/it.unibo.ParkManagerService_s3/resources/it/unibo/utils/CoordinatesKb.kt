package it.unibo.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.io.File
import java.io.InputStream
import java.io.PrintWriter
import itunibo.planner.plannerUtil
import mapRoomKotlin.mapUtil.map
import mapRoomKotlin.Box

object CoordinatesKb {
	
	init{
		println("CoordinatesKb | init ... ")
		
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
				return plannerUtil.planForGoal("2", "1")
			}
			"p3" -> {
				return plannerUtil.planForGoal("1", "2")
			}
			"p4" -> {
				return plannerUtil.planForGoal("2", "2")
			}
			"p5" -> {
				return plannerUtil.planForGoal("1", "3")
			}
			"p6" -> {
				return plannerUtil.planForGoal("2", "3")
			}
			
		}
		 
	}
	
	//Togliere l'ostacolo dove la macchina è appena stata rimossa
	
	fun removeObstacle(cleaner : String) : Unit{
		
		when(cleaner){
			"p1" -> {
				return map.cleanCell(2, 1)
			}
			"p2" -> {
				return map.cleanCell(3, 1)
			}
			"p3" -> {
				return map.cleanCell(2, 2)
			}
			"p4" -> {
				return map.cleanCell(3, 2)
			}
			"p5" -> {
				return map.cleanCell(2, 3)
			}
			"p6" -> {
				return map.cleanCell(3, 3)
			}
			
		}
		 
	}
	
	//Mettere l'ostacolo dove la macchina è appena stata parcheggiata
	
	fun setObstacle(obstacle : String) : Unit{
		
		when(obstacle){
			"p1" -> {
				return map.put(2, 1, Box(true, false, false))
			}
			"p2" -> {
				return map.put(3, 1, Box(true, false, false))
			}
			"p3" -> {
				return map.put(2, 2, Box(true, false, false))
			}
			"p4" -> {
				return map.put(3, 2, Box(true, false, false))
			}
			"p5" -> {
				return map.put(2, 3, Box(true, false, false) )
			}
			"p6" -> {
				return map.put(3, 3, Box(true, false, false))
			}
			
		}
	}
		
		fun isObstacle(obstacle : String) : Boolean{
		
			when(obstacle){
				"p1" -> {
					return map.isObstacle(2, 1)
				}
				"p2" -> {
					return map.isObstacle(3, 1)
				}
				"p3" -> {
					return map.isObstacle(2, 2)
				}
				"p4" -> {
					return map.isObstacle(3, 2)
				}
				"p5" -> {
					return map.isObstacle(2, 3)
				}
				"p6" -> {
					return map.isObstacle(3, 3)
				}
				
			}
		 return false
	}
	
}
	

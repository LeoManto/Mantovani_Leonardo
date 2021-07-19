package it.unibo.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.io.File
import java.io.InputStream
import java.io.PrintWriter
import itunibo.planner.plannerUtil

object CoordinatesKb {
	
	val planner = plannerUtil
	
	init{
		println("CoordinatesKb | init ... ")
		
	}
	
	fun getPathPlan(goal : String) : Unit{
		
		when(goal){
			"home" -> {
				return planner.planForGoal("0", "0")
			}
			"outdoor" -> {
				return planner.planForGoal("6", "4")
			}
			"indoor" -> {
				return planner.planForGoal("6", "0")
			}
			"p1" -> {
				return planner.planForGoal("2", "1")
			}
			"p2" -> {
				return planner.planForGoal("3", "1")
			}
			"p3" -> {
				return planner.planForGoal("2", "2")
			}
			"p4" -> {
				return planner.planForGoal("3", "2")
			}
			"p5" -> {
				return planner.planForGoal("2", "3")
			}
			"p6" -> {
				return planner.planForGoal("3", "3")
			}
			
		}
		 
	} 
	
}
	

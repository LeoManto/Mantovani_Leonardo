/* Generated by AN DISI Unibo */ 
package it.unibo.ctxParkingservice
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "parkingservice.pl", "sysRules.pl"
	)
}


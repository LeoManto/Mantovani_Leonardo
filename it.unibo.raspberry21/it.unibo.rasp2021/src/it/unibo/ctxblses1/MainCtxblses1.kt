/* Generated by AN DISI Unibo */ 
package it.unibo.ctxblses1
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "blses1_onoff.pl", "sysRules.pl"
	)
}

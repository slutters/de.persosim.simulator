package de.persosim.simulator.test.globaltester.perso;

import de.persosim.simulator.perso.DefaultPersoTestPkiTemplate06;
import de.persosim.simulator.perso.Personalization;

public class DefaultPersoTestPkiTemplate06Test extends TestPersoTest {

	@Override
	public Personalization getPersonalization() {
		if(persoCache == null) {
			persoCache = new DefaultPersoTestPkiTemplate06();
		}
		
		return persoCache;
	}
	
}
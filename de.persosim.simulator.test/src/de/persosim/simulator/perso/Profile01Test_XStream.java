package de.persosim.simulator.perso;

import org.junit.Before;

public class Profile01Test_XStream extends XmlPersonalizationTest {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		testPerso = new Profile01();
	}
}

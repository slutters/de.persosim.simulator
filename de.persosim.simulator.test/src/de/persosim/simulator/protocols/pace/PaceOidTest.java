package de.persosim.simulator.protocols.pace;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.persosim.simulator.protocols.ca.Ca;

public class PaceOidTest {

	/**
	 * Negative test: That null is returned for an Oid that is not an PaceOid
	 */
	@Test
	public void testGetStringRepresentation_nonPaceOid() {
		assertNull (new PaceOid().getStringRepresentation(Ca.id_CA_DH_3DES_CBC_CBC));
	}

}

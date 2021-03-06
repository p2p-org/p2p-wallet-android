package org.p2p.solanaj.programs;

import org.p2p.solanaj.model.core.PublicKey;
import org.p2p.solanaj.model.core.TransactionInstruction;

import org.junit.Test;
import static org.junit.Assert.*;

public class SystemProgramTest {

    @Test
    public void transferInstruction() {
        PublicKey fromPublicKey = new PublicKey("QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo");
        PublicKey toPublickKey = new PublicKey("GrDMoeqMLFjeXQ24H56S1RLgT4R76jsuWCd6SvXyGPQ5");
        int lamports = 3000;

        TransactionInstruction instruction = SystemProgram.transfer(fromPublicKey, toPublickKey, lamports);

        assertEquals(SystemProgram.PROGRAM_ID, instruction.getProgramId());
        assertEquals(2, instruction.getKeys().size());
        assertEquals(toPublickKey, instruction.getKeys().get(1).getPublicKey());

        assertArrayEquals(new byte[] { 2, 0, 0, 0, -72, 11, 0, 0, 0, 0, 0, 0 }, instruction.getData());
    }

}

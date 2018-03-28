package com.freerdp.freerdpcore.services.pcsc;

import com.freerdp.freerdpcore.services.pcsc.atr.PcscAtr;
import com.freerdp.freerdpcore.services.pcsc.atr.PcscAtrException;
import com.freerdp.freerdpcore.services.pcsc.atr.PcscRawAtrParser;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Local unit test, which will execute on the development machine (host).
 * Test classes naming: ClassNameTest
 * test cases naming: methodName[_negative][_caseName]
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PcscRawAtrParserTest {
    @Test
    public void parse_ecp20atr() throws Exception {
        byte[] atrData = {0x3B, (byte) 0x8B, 0x01, 0x52, 0x75, 0x74, 0x6F, 0x6B, 0x65, 0x6E, 0x20, 0x44, 0x53, 0x20, (byte) 0xC1};//ECP 2.0
        PcscRawAtrParser parser = new PcscRawAtrParser(atrData);
        PcscAtr atr = parser.parse();

        byte[] historicalBytes = {0x52, 0x75, 0x74, 0x6F, 0x6B, 0x65, 0x6E, 0x20, 0x44, 0x53, 0x20};
        assertArrayEquals(historicalBytes, atr.getHistoricalBytes());
    }

    @Test(expected = PcscAtrException.class)
    public void parse_negative_badTsT0() throws Exception {
        byte[] atrData = {0x3B};
        PcscRawAtrParser parser = new PcscRawAtrParser(atrData);
        parser.parse();
    }

    @Test(expected = PcscAtrException.class)
    public void parse_negative_badTsEncoding() throws Exception {
        byte[] atrData = {0x00, 0x00};
        PcscRawAtrParser parser = new PcscRawAtrParser(atrData);
        parser.parse();
    }

    @Test(expected = PcscAtrException.class)
    public void parse_negative_historicalBytesOffsetOutOfRange() throws Exception {
        byte[] atrData = {0x3B, (byte) 0xF0};
        PcscRawAtrParser parser = new PcscRawAtrParser(atrData);
        parser.parse();
    }

    @Test(expected = PcscAtrException.class)
    public void parse_negative_historicalBytesLengthOutOfRange() throws Exception {
        byte[] atrData = {0x3B, 0x01};
        PcscRawAtrParser parser = new PcscRawAtrParser(atrData);
        parser.parse();
    }
}
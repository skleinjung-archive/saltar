package com.thrashplay.saltar.physics;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class MeasurementTest {
    @Test
    public void construct_withValue_parsesComponentsCorrectly() {
        Measurement measurement = new Measurement("5a0dF");
        assertEquals(5, measurement.getBlocks());
        assertEquals(10, measurement.getPixels());
        assertEquals(0, measurement.getSubpixels());
        assertEquals(13, measurement.getSubsubpixels());
        assertEquals(15, measurement.getSubsubsubpixels());
    }
}

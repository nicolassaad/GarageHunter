package com.nothingsoft.nicolassaad.garagehunter.Models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by nicolassaad on 7/4/16.
 */
public class GarageSaleTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testToString() throws Exception {
        GarageSale garageSale = new GarageSale("Title", "Description", "Address", 12.1, 13.2, "Thursday", "Image1", "Image2", "Image3");
        assertEquals("GarageSale{title='Title', description='Description', address='Address', lat='12.1', lon='13.2', weekday='Thursday'}", garageSale.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitle() {
        GarageSale garageSale = new GarageSale("Title", "Description", "Address", 12.1, 13.2, "Thursday", "Image1", "Image2", "Image3");
        garageSale.setTitle("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitleConstructor() {
        new GarageSale("", "Description", "Address", 12.1, 13.2, "Thursday", "Image1", "Image2", "Image3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTitleConstructor() {
        new GarageSale(null, "Description", "Address", 12.1, 13.2, "Thursday", "Image1", "Image2", "Image3");
    }
}
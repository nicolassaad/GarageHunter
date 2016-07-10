package com.nothingsoft.nicolassaad.garagehunter.Fragments;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.nothingsoft.nicolassaad.garagehunter.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by nicolassaad on 7/4/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class PostFragmentTest {
    @Mock
    View mockView;

    @Mock
    ImageView imageHolder1;

    @Mock
    EditText title;

    @Mock
    EditText address;

    @Mock
    Editable addressEditable;

    @Mock
    Editable titleEditable;

    private PostFragment postFragment;


    @Before
    public void setUp() throws Exception {
        postFragment = new PostFragment();


        when(mockView.findViewById(R.id.post_image_holder1)).thenReturn(imageHolder1);
        when(mockView.findViewById(R.id.post_title)).thenReturn(title);
        when(mockView.findViewById(R.id.post_address)).thenReturn(address);

        when(title.getText()).thenReturn(titleEditable);
        when(address.getText()).thenReturn(addressEditable);

        postFragment.setViews(mockView);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCheckForNulls() {
        when(titleEditable.toString()).thenReturn("");
        when(addressEditable.toString()).thenReturn("");



        assertEquals(-1, postFragment.checkForNulls());

        //verify(title).setError(anyString());
    }
}
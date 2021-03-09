package pt.isel.ls;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class ExampleTest {

    @Test
    public void example() {
        // arrange | given
        int a = 1;
        int b = 2;

        // act | when
        int result = a + b;

        // assert | then
        assertEquals(3, result);
    }

    @Test(expected = FileNotFoundException.class)
    public void do_not_ignore_unexpected_exceptions_on_tests() throws FileNotFoundException {
        // test methods can have a non-empty `throws` exception list.
        new FileInputStream("does-not-exist");
    }
}

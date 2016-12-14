package com.github.drxaos.edu;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.util.Iterator;

public class SqliteAlternateListTest {
    @Test
    public void test1() throws Exception {

        String table1 = "list1";
        // create list
        SqliteSavedListAlternate<String> list = new SqliteSavedListAlternate<String>(table1);
        list.add("One");
        list.add("Two");
        list.add("Three");
        for (int i = 0; i < 100; i++) {
            list.add(1, "Hello " + i);
        }
        list.remove("Two");

        // check contents
        Assert.assertTrue(list.contains("Hello 50"));
        Assert.assertEquals(102, list.size());
        Assert.assertEquals("One", list.get(0));
        Assert.assertEquals("Hello 99", list.get(1));
        Assert.assertEquals("Hello 0", list.get(100));

        // create another list
        String table2 = "list2";

        SqliteSavedListAlternate<String> list2 = new SqliteSavedListAlternate<String>(table2);
        list2.add("One");
        list2.add("Two");
        list2.add("Three");

        // retain
        list.retainAll(list2);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void test2() throws Exception {

        String table3 = "list3";

        // create list
        SqliteSavedListAlternate<String> list = new SqliteSavedListAlternate<String>(table3);
        list.add("One");
        list.add("Two");
        list.add("Three");
        for (int i = 0; i < 100; i++) {
            list.add(1, "Hello " + i);
        }
        list.remove("Two");
        list = null;

        // load list
        SqliteSavedListAlternate<String> loadedList = new SqliteSavedListAlternate<String>(table3);

        Assert.assertTrue(DBAlternate.tableAndList.containsKey(table3));
        Assert.assertTrue(loadedList.contains("Hello 50"));
        Assert.assertEquals(102, loadedList.size());
        Assert.assertEquals("One", loadedList.get(0));
        Assert.assertEquals("Hello 99", loadedList.get(1));
        Assert.assertEquals("Hello 0", loadedList.get(100));
    }

    @Test
    public void test3() throws Exception {
        String table4 = "list4";

        // create list
        SqliteSavedListAlternate<String> list = new SqliteSavedListAlternate<String>(table4);
        list.add("One");
        list.add("Two");
        list.add("Three");
        for (int i = 0; i < 100; i++) {
            list.add(1, "Hello " + i);
        }
        list.remove("Two");

        // load list and remove elements
        SqliteSavedListAlternate<String> loadedList = new SqliteSavedListAlternate<String>(table4);
        for (Iterator<String> iterator = loadedList.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            if (next.contains("8")) {
                iterator.remove();
            }
        }
        Assert.assertEquals(83, loadedList.size());

        // reload
        Assert.assertFalse(list.isEmpty());
        list = new SqliteSavedListAlternate<String>(table4);
        Assert.assertEquals(83, list.size());

        // remove file
        DBAlternate.tableAndList.remove(table4);

        // reload
        list = new SqliteSavedListAlternate<String>(table4);
        Assert.assertTrue(list.isEmpty());
        Assert.assertEquals(0, list.size());
    }


    @Test
    public void test4() throws Exception {
        String table5 = "list5";

        // create list
        SqliteSavedListAlternate<Integer> list = new SqliteSavedListAlternate<Integer>(table5);
        list.add(1);
        list.add(2);
        list.add(3);
        list.set(0, 999);
        list = null;

        // load list
        SqliteSavedListAlternate<Integer> loadedList = new SqliteSavedListAlternate<Integer>(table5);

        Assert.assertTrue(DBAlternate.tableAndList.containsKey(table5));
        Assert.assertEquals(Integer.valueOf(999), loadedList.get(0));
        Assert.assertEquals(Integer.valueOf(2), loadedList.get(1));
        Assert.assertEquals(Integer.valueOf(3), loadedList.get(2));
        Assert.assertEquals(3, loadedList.size());
    }
}
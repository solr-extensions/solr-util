package com.s24.search.solr.util;

import static org.junit.Assert.assertEquals;

import org.apache.solr.common.util.NamedList;
import org.junit.Test;

public class NamedListBuilderTest {

   @Test
   public void testNamedListBuilder() throws Exception {
      NamedList<String> list = new NamedListBuilder<>()
            .add("foo", "bar")
            .add("greeting", "hello")
            .build();

      assertEquals(2, list.size());
      assertEquals("bar", list.get("foo"));
      assertEquals("hello", list.get("greeting"));
   }

   @Test
   public void testBuildEmptyList() throws Exception {
      NamedList<Object> list = new NamedListBuilder<>().build();
      assertEquals(0, list.size());
   }

   @Test(expected = IllegalStateException.class)
   public void testBuildCannotBeCalledMoreThanOnce() throws Exception {
      NamedListBuilder<String> builder = new NamedListBuilder<>();
      builder.build();
      builder.build();
   }

   @Test(expected = IllegalStateException.class)
   public void testAddCannotBeCalledAfterBuild() throws Exception {
      NamedListBuilder<String> builder = new NamedListBuilder<>();
      builder.build();
      builder.add("foo", "bar");
   }
}

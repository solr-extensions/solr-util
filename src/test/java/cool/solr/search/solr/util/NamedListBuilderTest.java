/*
 * Copyright 2015 Shopping24 GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.solr.search.solr.util;

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

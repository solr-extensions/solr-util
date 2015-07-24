package com.s24.search.solr.util;

import java.util.Arrays;

/**
 * Thrown if an entry in a {@code NamedList} cannot be cast to the expected type.
 */
public class NamedListEntryClassCastException extends ClassCastException {

   NamedListEntryClassCastException(String name, Class<?> actual, Class<?> expected) {
      super(String.format("Entry \"%s\" is an instance of %s and cannot be cast to %s", name, actual, expected));
   }

   NamedListEntryClassCastException(String[] names, Class<?> actual, Class<?> expected) {
      this(String.join(".", names), actual, expected);
   }
}

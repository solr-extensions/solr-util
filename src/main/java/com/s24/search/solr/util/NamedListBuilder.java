package com.s24.search.solr.util;

import org.apache.solr.common.util.NamedList;

/**
 * Builder for instances of {@link NamedList}.
 *
 * @param <T> the type of the elements of the returned list.
 */
public class NamedListBuilder<T> {

   NamedList<T> underConstruction = new NamedList<>();

   /**
    * Creates a new Builder instance.
    */
   public NamedListBuilder() {
   }

   /**
    * Adds the given name-value pair to the end of the named list constructed by this builder.
    */
   public NamedListBuilder add(String name, T value) {
      if (underConstruction == null) {
         throw new IllegalStateException("build() was already invoked on this builder instance");
      }

      underConstruction.add(name, value);
      return this;
   }

   /**
    * Returns the named list constructed by this builder.
    */
   public NamedList<T> build() {
      if (underConstruction == null) {
         throw new IllegalStateException("build() was already invoked on this builder instance");
      }

      NamedList<T> result = underConstruction;
      underConstruction = null;
      return result;
   }
}

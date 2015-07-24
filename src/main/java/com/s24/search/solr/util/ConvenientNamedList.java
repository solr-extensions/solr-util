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

package com.s24.search.solr.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.solr.common.util.NamedList;

/**
 * Provides more convenient read-only access to the elements of a {@link NamedList}. The {@code getNested} methods of
 * this class return a nested named list, wrapped in another instance of this class. If the nested list does not exist
 * or the element at the given name or index is {@code null}, the returned {@code ConvenientNamedList} is empty. All
 * other getter methods return {@code null} if the underlying named list does not contain a value for a given name, or
 * if the value for the given name is {@code null}. The underlying named list may itself be {@code null}, in this case,
 * the methods will also return {@code null}.
 */
public class ConvenientNamedList {

   private NamedList<?> namedList;

   /**
    * Creates a new read-only wrapper for the given named list.
    */
   public ConvenientNamedList(NamedList namedList) {
      this.namedList = namedList;
   }

   /**
    * Returns the size of the underlying named list.
    */
   public int size() {
      return namedList != null ? namedList.size() : 0;
   }

   /**
    * Returns the nested {@code NamedList} of the given name.
    */
   public ConvenientNamedList getNested(String name) {
      return new ConvenientNamedList(NamedLists.navigate(namedList, name));
   }

   /**
    * Returns the nested {@code NamedList} at the given path.
    */
   public ConvenientNamedList getNested(String... names) {
      return new ConvenientNamedList(NamedLists.navigate(namedList, names));
   }

   /**
    * Returns the nested {@code NamedList} at the given index, or returns an empty {@code ConvenientNamedList} if the
    * given index is invalid in the underlying list.
    */
   public ConvenientNamedList getNested(int index) {
      try {
         return new ConvenientNamedList(NamedLists.get(namedList, NamedList.class, index));
      } catch (IndexOutOfBoundsException e) {
         return new ConvenientNamedList(null);
      }
   }

   /**
    * Returns the set of the given name.
    * 
    * @param <T>
    *           the type of the elements in the set. This is not checked by this method. If any elements in the set are
    *           not of the expected type, accessing the elements of the set will result in a {@link ClassCastException}.
    */
   public <T> Set<T> getSet(String name) {
      return NamedLists.getSet(namedList, name);
   }

   /**
    * Returns the list of the given name.
    *
    * @param <T>
    *           the type of the elements in the list. This is not checked by this method. If any elements in the list
    *           are not of the expected type, accessing the elements of the list will result in a
    *           {@link ClassCastException}.
    */
   public <T> List<T> getList(String name) {
      return NamedLists.getList(namedList, name);
   }

   /**
    * Returns the collection of the given name.
    *
    * @param <T>
    *           the type of the elements in the set. This is not checked by this method. If any elements in the set are
    *           not of the expected type, accessing the elements of the set will result in a {@link ClassCastException}.
    */
   public <T> Collection<T> getCollection(String name) {
      return NamedLists.getCollection(namedList, name);
   }

   /**
    * Returns the string of the given name.
    */
   public String getString(String name) {
      return NamedLists.getString(namedList, name);
   }

   /**
    * Returns the underlying {@code NamedList}.
    */
   public NamedList<?> getNamedList() {
      return namedList;
   }

   @Override
   public String toString() {
      return namedList != null ? namedList.toString() : "{}";
   }
}

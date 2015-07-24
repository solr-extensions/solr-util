package com.s24.search.solr.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.solr.common.util.NamedList;

/**
 * Utility class for working with {@link NamedList}.
 * <p>
 * The convenience methods that take a number of path elements as an argument all work by first navigating to the given
 * nested {@code NamedList} and then returning the requested element of that list. Note that if any of the lists in the
 * given path contains a {@code null} value for the path element, or no value of that name, the methods return
 * {@code null}.
 * <p>
 * All methods of this class throw a {@link NamedListEntryClassCastException} if an element does not have the expected
 * type.
 */
public final class NamedLists {

   // private constructor to prevent instantiation
   private NamedLists() {
   }

   /**
    * Gets the nested {@code NamedList} stored under the given name.
    *
    * @param namedList
    *           the list.
    * @param name
    *           the name of the element in the list.
    * @return the nested {@code NamedList}, or {@code null} if no element with the given name exists in the list or the
    *         value is {@code null}.
    */
   public static NamedList<?> navigate(NamedList<?> namedList, String name) {
      return get(namedList, NamedList.class, name);
   }

   /**
    * Gets the nested {@code NamedList} stored under the given path. See the class documentation for notes on
    * convenience methods that take a path as their argument.
    */
   public static NamedList<?> navigate(NamedList<?> namedList, String... names) {
      return get(namedList, NamedList.class, names);
   }

   /**
    * Gets the {@code Set} of the specified name from the given list.
    * 
    * @param namedList
    *           the list.
    * @param name
    *           the name of the element in the list.
    * @param <T>
    *           the type of the elements in the returned set. Note that this type is <strong>not</strong> checked by
    *           this method, so a {@link ClassCastException} may occur when trying to actually access the elements of
    *           the set if they're not of the expected class.
    * @return the set, or {@code null} if the list does not contain an element with the given name or the value is
    *         {@code null}.
    */
   public static <T> Set<T> getSet(NamedList<?> namedList, String name) {
      return get(namedList, Set.class, name);
   }

   /**
    * Gets a set from the given list. See the class documentation for notes on convenience methods that take a path as
    * their argument.
    * 
    * @see #getSet(NamedList, String)
    */
   public static <T> Set<T> getSet(NamedList<?> namedList, String... names) {
      return get(namedList, Set.class, names);
   }

   /**
    * Gets the {@code List} of the specified name from the given list.
    *
    * @param namedList
    *           the list.
    * @param name
    *           the name of the element in the list.
    * @param <T>
    *           the type of the elements in the returned list. Note that this type is <strong>not</strong> checked by
    *           this method, so a {@link ClassCastException} may occur when trying to actually access the elements of
    *           the list if they're not of the expected class.
    * @return the list, or {@code null} if the list does not contain an element with the given name or the value is
    *         {@code null}.
    */
   public static <T> List<T> getList(NamedList<?> namedList, String name) {
      return get(namedList, List.class, name);
   }

   /**
    * Gets a list from the given list. See the class documentation for notes on convenience methods that take a path as
    * their argument.
    * 
    * @see #getList(NamedList, String)
    */
   public static <T> List<T> getList(NamedList<?> namedList, String... names) {
      return get(namedList, List.class, names);
   }

   /**
    * Gets the {@code Collection} of the specified name from the given list.
    *
    * @param namedList
    *           the list.
    * @param name
    *           the name of the element in the list.
    * @param <T>
    *           the type of the elements in the returned collection. Note that this type is <strong>not</strong> checked
    *           by this method, so a {@link ClassCastException} may occur when trying to actually access the elements of
    *           the collection if they're not of the expected class.
    * @return the collection, or {@code null} if the list does not contain an element with the given name or the value
    *         is {@code null}.
    */
   public static <T> Collection<T> getCollection(NamedList<?> namedList, String name) {
      return get(namedList, Collection.class, name);
   }

   /**
    * Gets a collection from the given list. See the class documentation for notes on convenience methods that take a
    * path as their argument.
    *
    * @see #getCollection(NamedList, String)
    */
   public static <T> Collection<T> getCollection(NamedList<?> namedList, String... names) {
      return get(namedList, Collection.class, names);
   }

   /**
    * Gets the string value of the specified name from the given list.
    *
    * @param namedList
    *           the list.
    * @param name
    *           the name of the element in the list.
    * @return the string, or {@code null} if the list does not contain an element with the given name or the value is
    *         {@code null}.
    */
   public static String getString(NamedList<?> namedList, String name) {
      return get(namedList, String.class, name);
   }

   /**
    * Gets a string value from the given list. See the class documentation for notes on convenience methods that take a
    * path as their argument.
    *
    * @see #getString(NamedList, String)
    */
   public static String getString(NamedList<?> namedList, String... names) {
      return get(namedList, String.class, names);
   }

   /**
    * Gets the element of the specified name from the given list.
    * 
    * @param namedList
    *           the list.
    * @param elementClass
    *           the class of the element expected in the list under the given name.
    * @param name
    *           the name of the list entry to return.
    * @return the first element found in the list under the given name, or {@code null} if no element with the given
    *         name exists in the list or the value if {@code null}.
    * @throws NamedListEntryClassCastException
    *            if the value stored in the list is not an instance of class {@code elementClass}.
    */
   public static <T> T get(NamedList<?> namedList, Class<T> elementClass, String name) {
      Object value = namedList.get(name);
      try {
         return elementClass.cast(value);
      } catch (ClassCastException e) {
         throw new NamedListEntryClassCastException(name, value.getClass(), elementClass);
      }
   }

   /**
    * Gets a nested element from the given list. See the class documentation for notes on convenience methods that take
    * a path as their argument.
    *
    * @see #get(NamedList, Class, String)
    */
   public static <T> T get(NamedList<?> namedList, Class<T> elementClass, String... names) {
      Object value = namedList.findRecursive(names);
      try {
         return elementClass.cast(value);
      } catch (ClassCastException e) {
         throw new NamedListEntryClassCastException(names, value.getClass(), elementClass);
      }
   }
}

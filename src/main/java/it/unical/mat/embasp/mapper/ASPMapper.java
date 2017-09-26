/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Davide Fusca'
 * Copyright (c) 2015 Stefano Germano
 * Copyright (c) 2015 Jessica Zangari
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package it.unical.mat.embasp.mapper;

import it.unical.mat.embasp.Manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Davide Fusca'
 * @author Stefano Germano
 * @author Jessica Zangari
 *
 */
public class ASPMapper {

	/**
	 *
	 */
	private static ASPMapper mapper;

	/**
	 * @return
	 */
	public static ASPMapper getInstance() {
		if (ASPMapper.mapper == null)
			ASPMapper.mapper = new ASPMapper();
		return ASPMapper.mapper;
	}

	/**
	 *
	 */
	private final Map<String, Class<?>> predicateClass;

	/**
	 *
	 */
	private final Map<Class<?>, Map<String, Method>> classSetterMethod;

	/**
	 *
	 */
	private ASPMapper() {
		predicateClass = new HashMap<>();
		classSetterMethod = new HashMap<>();
	}

	/**
	 * @param predicate
	 * @return
	 */
	public Class<?> getClass(final String predicate) {
		return predicateClass.get(predicate);
	}

	/**
	 * @param atom
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 */
	public Object getObject(final String atom) throws IllegalAccessException,
	IllegalArgumentException, InvocationTargetException,
	NoSuchMethodException, SecurityException, InstantiationException {

		// Manager.Log.info(atom);

		// FIXME specify a more precise pattern
		final String patternString = "(-?[a-z][A-Za-z0-9_]*)(\\((.+)?\\))?";

		final Pattern pattern = Pattern.compile(patternString);

		final Matcher matcher = pattern.matcher(atom);
		if (!matcher.matches())
			throw new IllegalArgumentException();

		// Manager.Log.info("matches: " + matcher.group(1) + "   "
		// + matcher.group(2));

		final String predicate = matcher.group(1);
		// final String predicate = atom.substring(0, atom.indexOf("("));
		final Class<?> cl = getClass(predicate);
		if (cl == null)
			return null;
		// Manager.Log.info("Class<?> " + cl.getName());
		Object obj;
		try {
			obj = cl.newInstance();
		} catch (final Exception e) {
			Manager.Log.severe("Class " + cl.getName()
					+ " cannot be instantiated,"
					+ " probably the default constructor is missing");
			e.printStackTrace();
			return null;
		}
		// Term with arity 0 return obj
		if (matcher.groupCount() == 1)
			return obj;
		// FIXME Doesn't work with atoms like "a("asd,"). we should fix the
		// split
		final String[] terms = matcher.group(3).replace("\"", "").split(",");
		// String[] paramiter=atom.substring(atom.indexOf("(")+1,
		// atom.lastIndexOf(")")).split(",");

		for (final Field field : cl.getDeclaredFields())
			if (field.isAnnotationPresent(Term.class)) {

				final int term = field.getAnnotation(Term.class).value() - 1;
				final String nameMethod = "set"
						+ Character.toUpperCase(field.getName().charAt(0))
						+ field.getName().substring(1);
				final Method method = classSetterMethod.get(cl).get(nameMethod);

				if (method.getParameterTypes()[0].equals(int.class)
						|| method.getParameterTypes()[0].equals(Integer.class))
					method.invoke(obj, Integer.valueOf(terms[term]));
				else
					method.invoke(obj, terms[term]);

			}

		return obj;
	}

	/**
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalTermException
	 */
	public String getString(final Object obj) throws IllegalAccessException,
	IllegalArgumentException, InvocationTargetException,
	NoSuchMethodException, SecurityException, IllegalTermException {
		final String predicate = registerClass(obj.getClass());
		String atom = predicate + "(";
		final HashMap<Integer, Object> mapTerm = new HashMap<>();
		for (final Field field : obj.getClass().getDeclaredFields())
			if (field.isAnnotationPresent(Term.class)) {
				final String methodName = "get"
						+ Character.toUpperCase(field.getName().charAt(0))
						+ field.getName().substring(1);
				final Object value = obj.getClass().getMethod(methodName)
						.invoke(obj);
				mapTerm.put(field.getAnnotation(Term.class).value() - 1, value);
			}
		for (int i = 0; i < mapTerm.size(); i++) {
			if (i != 0)
				atom += ",";
			final Object objectTerm = mapTerm.get(i);
			if (objectTerm == null)
				throw new IllegalTermException("Wrong term number of class "
						+ obj.getClass().getName());
			if (objectTerm instanceof Integer)
				atom += objectTerm.toString();
			else if (objectTerm instanceof Long)
				atom += objectTerm.toString();
			else
				atom += "\"" + objectTerm.toString() + "\"";

		}
		atom += ")";
		return atom;
	}

	/**
	 * @param cl
	 * @return
	 */
	public String registerClass(final Class<?> cl) {
		final String predicate = cl.getAnnotation(Predicate.class).value();
		predicateClass.put(predicate, cl);
		final Map<String, Method> namesMethods = new HashMap<>();
		for (final Method method : cl.getMethods())
			if (method.getName().startsWith("set"))
				namesMethods.put(method.getName(), method);
		classSetterMethod.put(cl, namesMethods);
		return predicate;
	}
}

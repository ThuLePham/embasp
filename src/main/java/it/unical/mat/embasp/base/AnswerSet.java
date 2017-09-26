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
package it.unical.mat.embasp.base;

import it.unical.mat.embasp.mapper.ASPMapper;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Davide Fusca'
 * @author Stefano Germano
 * @author Jessica Zangari
 *
 */
public class AnswerSet {

	/**
	 *
	 */
	protected final List<String> answerSet;
	/**
	 *
	 */
	protected final Map<Integer, Integer> weightMap;
	/**
	 *
	 */
	private Set<Object> answerObjects;

	/**
	 * @param outputString
	 */
	public AnswerSet(final List<String> outputString) {
		answerSet = outputString;
		weightMap = new HashMap<Integer, Integer>();
	}

	/**
	 * @param outputString
	 * @param outputWeightMap
	 */
	public AnswerSet(final List<String> outputString,
			final Map<Integer, Integer> outputWeightMap) {
		answerSet = outputString;
		weightMap = outputWeightMap;
	}

	/**
	 * @return
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Set<Object> getAnswerObjects() throws InvocationTargetException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException {

		if (answerObjects == null) {

			answerObjects = new HashSet<>();

			final ASPMapper mapper = ASPMapper.getInstance();

			for (final String atom : answerSet) {
				final Object object = mapper.getObject(atom);
				if (object != null)
					answerObjects.add(object);
				else
					System.err.println("Object not mapped: " + atom);
			}

		}

		return answerObjects;

	}

	/**
	 * @return
	 */
	public List<String> getAnswerSet() {
		return Collections.unmodifiableList(answerSet);
	}

	/**
	 * @return
	 */
	public Map<Integer, Integer> getWeightMap() {
		return Collections.unmodifiableMap(weightMap);
	}

	@Override
	public String toString() {
		return "AnswerSet [answerSet=" + answerSet + "]";
	}
	
}

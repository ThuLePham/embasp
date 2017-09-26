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
import it.unical.mat.embasp.mapper.IllegalTermException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Davide Fusca'
 * @author Stefano Germano
 * @author Jessica Zangari
 *
 */
public abstract class ASPHandler {

	/**
	 *
	 */
	protected StringBuilder options;
	/**
	 *
	 */
	protected StringBuilder program;
	/**
	 *
	 */
	protected List<String> filesPaths;
	/**
	 *
	 */
	protected StringBuilder filter;

	/**
	 *
	 */
	public ASPHandler() {
		options = new StringBuilder();
		program = new StringBuilder();
		filesPaths = new ArrayList<String>();
		filter = new StringBuilder();
	}

	/**
	 * @param filePath
	 * @throws FileNotFoundException
	 */
	public void addFileInput(final String filePath)
			throws FileNotFoundException {
		final File f = new File(filePath);
		if (f.exists() && f.isFile())
			filesPaths.add(filePath);
		else
			throw new FileNotFoundException();
	}

	/**
	 * @param filterClasses
	 */
	public void addFilter(final Class<?>... filterClasses) {
		final ASPMapper mapper = ASPMapper.getInstance();
		for (final Class<?> aClass : filterClasses) {
			if (filter.length() > 0)
				filter.append(',');
			filter.append(mapper.registerClass(aClass));
		}
	}

	/**
	 * @param filterPredicates
	 */
	public void addFilterPredicate(final String... filterPredicates) {
		for (final String filterPredicate : filterPredicates) {
			if (filter.length() > 0)
				filter.append(',');
			filter.append(filterPredicate);
		}
	}

	/**
	 * @param inputObj
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalTermException
	 */
	public void addInput(final Object inputObj) throws IllegalAccessException,
	IllegalArgumentException, InvocationTargetException,
	NoSuchMethodException, SecurityException, IllegalTermException {
		program.append(ASPMapper.getInstance().getString(inputObj)).append('.');
	}

	/**
	 * @param inputObjs
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalTermException
	 */
	public void addInput(final Set<Object> inputObjs)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, IllegalTermException {
		for (final Object inputObj : inputObjs)
			addInput(inputObj);
	}

	/**
	 * @param options
	 */
	public void addOption(final String options) {
		if (this.options.length() != 0)
			this.options.append(" ");
		this.options.append(options);
	}

	/**
	 * @param rawInput
	 */
	public void addRawInput(final String rawInput) {
		program.append(rawInput);
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public abstract AnswerSets reason() throws IOException,
	InterruptedException;

	/**
	 *
	 */
	public void resetFileInputs() {
		filesPaths = new ArrayList<String>();
	}

	/**
	 *
	 */
	public void resetFilter() {
		filter = new StringBuilder();
	}

	/**
	 *
	 */
	public void resetOptions() {
		options = new StringBuilder();
	}

	/**
	 *
	 */
	public void resetProgram() {
		program = new StringBuilder();
	}

	public String getProgram() {
		return program.toString();
	}

}

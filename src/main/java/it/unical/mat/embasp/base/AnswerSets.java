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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Davide Fusca'
 * @author Stefano Germano
 * @author Jessica Zangari
 *
 */
public abstract class AnswerSets {

	/**
	 *
	 */
	protected String answerSetsString;

	/**
	 *
	 */
	protected List<AnswerSet> answerSetsList;

	/**
	 * @param answerSets
	 */
	public AnswerSets(final String answerSets) {
		answerSetsList = new ArrayList<AnswerSet>();
		answerSetsString = answerSets;
		parse();
	}

	/**
	 * @return
	 */
	public List<AnswerSet> getAnswerSetsList() {
		return Collections.unmodifiableList(answerSetsList);
	}

	/**
	 * @return
	 */
	public String getAnswerSetsString() {
		return answerSetsString;
	}

	/**
	 *
	 */
	abstract protected void parse();

}

/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Stefano Germano - Insight Centre for Data Analytics NUIG
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
package it.unical.mat.embasp.dlvhex;

import it.unical.mat.embasp.base.AnswerSet;
import it.unical.mat.embasp.base.AnswerSets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stefano Germano
 *
 */
public class DLVHEXAnswerSets extends AnswerSets {

	/**
	 * @param answerSets
	 */
	public DLVHEXAnswerSets(final String answerSets) {
		super(answerSets);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.insight_centre.urq.citypulse.wp5.decision_support_system.asp_reasoning
	 * .base.AnswerSets#parse()
	 */
	@Override
	protected void parse() {

		final Pattern pattern = Pattern.compile("(\\{.*\\})( <.+>)?");
		final Matcher matcher = pattern.matcher(answerSetsString);
		while (matcher.find()) {

			final Pattern patternAnswerSet = Pattern
					.compile("-?[a-z][A-Za-z0-9_]*(\\(.*?\\))?");
			final Matcher matcherAnswerSet = patternAnswerSet.matcher(matcher
					.group(1));
			final List<String> answerSetList = new ArrayList<>();
			while (matcherAnswerSet.find())
				answerSetList.add(matcherAnswerSet.group());

			if (matcher.group(2) != null) {

				final Map<Integer, Integer> weightMap = new HashMap<>();
				final Pattern patternWL = Pattern
						.compile("\\[(\\d+):(\\d+)\\]+?");
				final Matcher matcherWL = patternWL.matcher(matcher.group(2));
				while (matcherWL.find())
					weightMap.put(Integer.parseInt(matcherWL.group(2)),
							Integer.parseInt(matcherWL.group(1)));

				answerSetsList.add(new AnswerSet(answerSetList, weightMap));

			} else
				answerSetsList.add(new AnswerSet(answerSetList));
		}

	}

}

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
package it.unical.mat.embasp.clingo;

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
public class ClingoAnswerSets extends AnswerSets {

	public static void main(final String[] args) {

		final ClingoAnswerSets clingoAnswerSets = new ClingoAnswerSets(
				"Solving...\r\n"
						+ "Answer: 1\r\n"
						+ "parking_space_selected(\"10.1442012 56.2136502\",13,100)\r\n"
						+ "Optimization: 100\r\n"
						+ "Answer: 1\r\n"
						+ "parking_space_selected(\"10.1442012 56.2136502\",13,100)\r\n"
						+ "Optimization: 100\r\n"
						+ "Answer: 2\n"
						+ "parking_space_selected(\"10.1171296 56.2261545\",13,100)  parking_space_selected(\"10.1442012 56.2136502\",13,100)\n"
						+ "Optimization: 100\r\n"
						+ "Answer: 3\r\n"
						+ "parking_space_selected(\"10.1442042 56.2136402\",13,100)\r\n"
						+ "Optimization: 100\r\n"
						+ "OPTIMUM FOUND\r\n"
						+ "\r\n"
						+ "Models       : 4\r\n"
						+ "  Optimum    : yes\r\n"
						+ "  Optimal    : 3\r\n"
						+ "Optimization : 100\r\n"
						+ "Calls        : 1\r\n"
						+ "Time         : 0.970s (Solving: 0.00s 1st Model: 0.00s Unsat: 0.00s)\r\n"
						+ "CPU Time     : 0.233s\r\n" + "");

		for (final AnswerSet answerSet : clingoAnswerSets.getAnswerSetsList())
			System.out.println(answerSet.getAnswerSet());

	}

	/**
	 * @param answerSets
	 */
	public ClingoAnswerSets(final String answerSets) {
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

		final Pattern pattern = Pattern
				.compile("Answer: (\\d+)\\r?\\n(.*)(\\r?\\nOptimization: (.+))?");
		final Matcher matcher = pattern.matcher(answerSetsString);
		while (matcher.find()) {

			try {
				if (matcher.group(1) == null
						|| Integer.parseInt(matcher.group(1)) <= answerSetsList
						.size())
					continue;
			} catch (final NumberFormatException e1) {
				e1.printStackTrace();
				break;
			}

			final Pattern patternAnswerSet = Pattern
					.compile("-?[a-z][A-Za-z0-9_]*(\\(.*?\\))?");
			final Matcher matcherAnswerSet = patternAnswerSet.matcher(matcher
					.group(2));
			final List<String> answerSetList = new ArrayList<>();
			while (matcherAnswerSet.find())
				answerSetList.add(matcherAnswerSet.group());

			if (matcher.group(4) != null) {

				final Map<Integer, Integer> weightMap = new HashMap<>();
				try {
					final String[] split = matcher.group(4).split(" ");
					int level = split.length;
					for (final String weight : split)
						weightMap.put(level--, Integer.parseInt(weight));
				} catch (final NumberFormatException e) {
					e.printStackTrace();
				}

				answerSetsList.add(new AnswerSet(answerSetList, weightMap));

			} else
				answerSetsList.add(new AnswerSet(answerSetList));
		}

	}
}

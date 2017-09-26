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

import it.unical.mat.embasp.Configuration;
import it.unical.mat.embasp.Manager;
import it.unical.mat.embasp.base.ASPHandler;
import it.unical.mat.embasp.base.AnswerSets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @author Stefano Germano
 *
 */
public class ClingoHandler extends ASPHandler {

	String clingoURI = null;

	/**
	 *
	 */
	public ClingoHandler() {
		super();
	}

	/**
	 *
	 */
	public ClingoHandler(String clingoUri) {
		super();
		this.clingoURI = clingoUri;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.insight_centre.urq.citypulse.wp5.decision_support_system.asp_reasoning
	 * .base.ASPHandler#reason()
	 */
	@Override
	public AnswerSets reason() throws IOException, InterruptedException {

//		Manager.Log.log(Level.INFO, "program: " + program.toString());

		if (this.clingoURI == null) {
			this.clingoURI = Configuration.getInstance().getClingoPath();
		}
//		Manager.Log.info("ClingoURI: " + this.clingoURI);

		final StringBuilder commandStringBuilder = new StringBuilder();

		commandStringBuilder.append(this.clingoURI).append(' ');
		commandStringBuilder.append(options).append(' ');
		for (final String string : filesPaths) {
			commandStringBuilder.append(' ').append(string);
		}
		commandStringBuilder.append(" -");

		Manager.Log.info("Calling Clingo: " + commandStringBuilder.toString());
		final Process process = Runtime.getRuntime().exec(
				commandStringBuilder.toString());

		final BufferedWriter bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(process.getOutputStream()));
		bufferedWriter.write(program.toString());
		bufferedWriter.close();

		final long start = System.currentTimeMillis();
		final int waitingTime = 300;
//		process.waitFor(waitingTime, TimeUnit.SECONDS);
//		process.waitFor();

		if ((System.currentTimeMillis() - start) >= waitingTime * 1000) {
			process.destroy();
			Manager.Log.info("Timeout (5 minutes)");
		}
//		process.waitFor();

		// read output of Clingo and store in AnswerSets
		final BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));
		final BufferedReader hexerr = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));

		String clingoOutput = "";
		String currentLine;
		String currentErrLine;
		while ((currentLine = bufferedReader.readLine()) != null) {
			clingoOutput += currentLine+"\n";
		}
//		Manager.Log.info(clingoOutput);
//		while ((currentErrLine = hexerr.readLine()) != null) {
//			Manager.Log.severe(currentErrLine);
//		}

		Manager.Log.info("Called Clingo");
		return new ClingoAnswerSets(clingoOutput);

	}
}

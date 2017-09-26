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

import it.unical.mat.embasp.Configuration;
import it.unical.mat.embasp.Manager;
import it.unical.mat.embasp.base.ASPHandler;
import it.unical.mat.embasp.base.AnswerSets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;

/**
 * @author Stefano Germano
 *
 */
public class DLVHEXHandler extends ASPHandler {

	/**
	 *
	 */
	public DLVHEXHandler() {
		super();
	}

	/**
	 * @return
	 */
	protected String getFactFilename() {
		return String.format("data.hex");
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

		// final StringBuilder filePathsString = new StringBuilder();
		// // filePathsString.append(programFile);
		// for (final String string : filesPaths)
		// filePathsString.append(' ').append(string);

		// Manager.Log.log(Level.INFO, "filePathsString: " + filePathsString);
		Manager.Log.log(Level.INFO, "program: " + program.toString());

		// final boolean isWindows = System.getProperty("os.name").toLowerCase()
		// .indexOf("win") >= 0;

		final StringBuilder commandStringBuilder = new StringBuilder();
		commandStringBuilder
				.append(Configuration.getInstance().getDLVHEXPath())
				.append(' ');
		commandStringBuilder.append(options).append(' ');
		if (filter.length() > 0)
			commandStringBuilder.append("--filter=").append(filter.toString());
		for (final String string : filesPaths)
			commandStringBuilder.append(' ').append(string);
		commandStringBuilder.append(" --");

		// final String command = new StringBuilder()
		// .append(Configuration.getDLVHEXPath())
		// .append(' ')
		// // .append(Configuration.getHexAdditionalArguments()).append(' ')
		// .append(options).append(' ')
		// .append(filter.length() > 0 ? "--filter=" : "")
		// .append(filter.toString()).append(filePathsString)
		// // .append(isWindows ? "" : " --").toString();
		// .append(" --").toString();

		// call dlvhex
		Manager.Log.info("Calling dlvhex2: " + commandStringBuilder.toString());
		final Process process = Runtime.getRuntime().exec(
				commandStringBuilder.toString());

		// if (!isWindows) {
		final BufferedWriter bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(process.getOutputStream()));
		bufferedWriter.write(program.toString());
		bufferedWriter.close();
		// }

		process.waitFor();
		Manager.Log.info("Called dlvhex2");

		// read output of dlvhex and store in AnswerSets
		final BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));
		final BufferedReader hexerr = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));

		String dlvhexOutput = "";
		String currentLine;
		String currentErrLine;
		while ((currentLine = bufferedReader.readLine()) != null)
			dlvhexOutput += currentLine + "\n";
		Manager.Log.info(dlvhexOutput);
		while ((currentErrLine = hexerr.readLine()) != null)
			Manager.Log.severe(currentErrLine);

		return new DLVHEXAnswerSets(dlvhexOutput);

	}
}

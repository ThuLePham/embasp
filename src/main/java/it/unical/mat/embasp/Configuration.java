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
package it.unical.mat.embasp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Stefano Germano
 *
 */
public class Configuration {

	/**
	 *
	 */
	private static final Configuration INSTANCE = new Configuration();

	/**
	 * @return
	 */
	public static Configuration getInstance() {
		return Configuration.INSTANCE;
	}

	/**
	 *
	 */
	private Properties prop = null;

	/**
	 *
	 */
	private Configuration() {
		initialize();
	}

	// public static String getHexAdditionalArguments() {
	// Configuration.initialize();
	// if (Configuration.prop == null)
	// return "";
	// return Configuration.prop.getProperty("hexaditionalarguments");
	// }

	/**
	 * @return
	 */
	public String getClingoPath() {
		if (prop == null)
			return "clingo";
		return prop.getProperty("clingopath");
	}

	// public static String getReasoningFilename() {
	// Configuration.initialize();
	// if (Configuration.prop == null)
	// return "";
	// return Configuration.prop.getProperty("reasoningfilename");
	// }

	// public static boolean getUseDlv() {
	// Configuration.initialize();
	// if (Configuration.prop == null)
	// return true;
	// return !Configuration.prop.getProperty("usedlv").equals("false")
	// && !Configuration.prop.getProperty("usedlv").equals("no");
	// }

	/**
	 * @return
	 */
	public String getDLVHEXPath() {
		if (prop == null)
			return "dlvhex2";
		return prop.getProperty("dlvhexpath");
	}

	/**
	 *
	 */
	private void initialize() {

		final String resourceName = "config.properties";

		if (prop == null)
			try {
				final ClassLoader loader = Thread.currentThread()
						.getContextClassLoader();
				prop = new Properties();
				final InputStream resourceStream = loader
						.getResourceAsStream(resourceName);
				prop.load(resourceStream);

			} catch (final IOException e) {
				System.err.println("Could not open configuration file.");
				System.err.println(e);
				System.err.println("Falling back to defaults.");
				prop = null;
			}

	}

	/**
	 * @return
	 */
	public boolean isDebugMode() {
		if (prop == null)
			return false;
		final String b = prop.getProperty("debug");
		final boolean retVal = !b.equals("false") && !b.equals("no")
				&& !b.equals("0");
		return retVal;
	}

}

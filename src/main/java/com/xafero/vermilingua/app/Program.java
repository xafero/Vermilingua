package com.xafero.vermilingua.app;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.xafero.vermilingua.core.Search;

public class Program {

	private static final String APP_SHORT_NAME = "vermilingua";
	private static final String APP_SRC_URL = "https://github.com/xafero/Vermilingua";

	private static final String S_HELP = "?";
	private static final String S_ROOT = "r";
	private static final String S_JARS = "f";
	private static final String S_BY_NAME = "k";
	private static final String S_BY_CLASS = "l";
	private static final String S_BY_MANIFEST = "m";
	private static final String S_BY_TEXT = "n";

	public static void main(String[] args) throws Exception {
		// Define options
		Option help = new Option(S_HELP, "help", false, "print this message");
		Option root = Option.builder(S_ROOT).desc("specify root").argName("dir").longOpt("root").hasArg().build();
		Option jars = Option.builder(S_JARS).desc("find JARs ...").longOpt("jars").build();
		Option byName = Option.builder(S_BY_NAME).desc("... by name").argName("term").longOpt("name").hasArg().build();
		Option byClass = Option.builder(S_BY_CLASS).desc("... by class").argName("term").longOpt("class").hasArg()
				.build();
		Option byManifest = Option.builder(S_BY_MANIFEST).desc("... by manifest").argName("term").longOpt("manifest")
				.hasArg().build();
		Option byText = Option.builder(S_BY_TEXT).desc("... by text").argName("term").longOpt("text").hasArg().build();
		// Collect them
		Options options = new Options();
		options.addOption(help);
		options.addOption(root);
		options.addOption(jars);
		options.addOption(byName);
		options.addOption(byClass);
		options.addOption(byManifest);
		options.addOption(byText);
		// If nothing given, nothing will happen
		if (args == null || args.length < 1) {
			printHelp(options);
			return;
		}
		// Parse command line
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine line = parser.parse(options, args);
			// Work on it
			process(line, options);
		} catch (Throwable e) {
			System.err.printf("Error occurred: %n " + e.getMessage());
		}
	}

	private static void printHelp(Options options) {
		String header = String.format("A tool to search for contents in Java resources%n%n");
		String footer = String.format("%nPlease report issues at " + APP_SRC_URL);
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(APP_SHORT_NAME, header, options, footer, true);
	}

	private static void process(CommandLine line, Options options) throws Exception {
		if (line.hasOption(S_HELP)) {
			printHelp(options);
			return;
		}
		if (line.hasOption(S_JARS)) {
			String dir = line.getOptionValue(S_ROOT);
			String name = line.getOptionValue(S_BY_NAME);
			String clazz = line.getOptionValue(S_BY_CLASS);
			String manifest = line.getOptionValue(S_BY_MANIFEST);
			String text = line.getOptionValue(S_BY_TEXT);
			Search.findJARs(dir, name, clazz, manifest, text);
			return;
		}
	}
}
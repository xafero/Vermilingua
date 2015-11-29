package com.xafero.vermilingua.api;

import java.io.InputStream;

public interface IParser {

	String parse(String name, InputStream input);

}
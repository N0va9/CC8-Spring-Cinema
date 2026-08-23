package com.dawan.cinema.tools;

import org.modelmapper.ModelMapper;

public class DtoTool {
	
	private static ModelMapper mapper = new ModelMapper();
	
	public static <TSource, TDestination> TDestination convert(TSource obj, Class<TDestination> clazz) {
		return mapper.map(obj, clazz);
	}

}

package com.enablix.state.change;

import java.lang.reflect.TypeVariable;

public class GenericsTest<A extends Input> {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		GenericsTest<AInput> input = new GenericsTest<>();
		TypeVariable<?>[] typeParameters = input.getClass().getTypeParameters();
		
	}
	
}

interface Input {
	
}

class AInput implements Input {
	
}

/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline.core;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PrimObjectClassTest {

    @Test
    public void shouldProvideAccessToInstanceSize() {
        PrimObjectMetaclass metaclass = PrimObjectMetaclass.basicSubclassOf(null);
        PrimObjectMetaclass aClass = metaclass.basicCreate("test", null, null, null, null, null);
        assertNotNull(aClass.instanceSize());
    }

	@Test (expected = IllegalStateException.class)
	public void shouldThrowIllegalArgumentExceptionWhenVariableAlreadyRegistered() {
		PrimObjectClass object = new PrimObjectClass();
		object.variableIndexes().put("var", 1);
		object.addVariableNamed("var");
	}

	@Test
	public void shouldAddVariables() {
		PrimObjectClass object = new PrimObjectClass();
		object.addVariableNamed("var");
		assertTrue(object.variableIndexes().containsKey("var"));
	}

    @Test
    public void shouldHaveRegistryOfVariableIndexesWhenConstructed() {
        PrimObjectClass object = new PrimObjectClass();
        assertNotNull(object.variableIndexes());
    }

	@Test
	public void shouldDelegatePackageLookupToClass() {
		PrimObject aClass = mock(PrimObject.class);
		PrimObjectClass primObjectClass = new PrimObjectClass();
		primObjectClass.cls(aClass);
		primObjectClass.packageFor("SomeClass");
		verify(aClass).packageFor("SomeClass");
	}

	@Test
	public void shouldProvideAccessToSuperclass() {
		PrimObjectClass object = new PrimObjectClass();
		assertEquals(object.superclass(), PrimObject.PRIM_NIL);
	}

	@Test
	public void shouldProvideMutateOfSuperclass() {
		PrimObjectClass object = new PrimObjectClass();
		PrimObjectClass superclass = new PrimObjectClass();
		object.superclass(superclass);
		assertEquals(object.superclass(), superclass);
	}

	@Test
	public void shouldProvideMethodDictionary() {
		PrimObjectClass object = new PrimObjectClass();
		assertNotNull(object.methods());
	}

	@Test
	public void shouldKnowIfIncludesSelector() {
		PrimObjectClass object = new PrimObjectClass();
		object.methods().put("foo", new PrimObject());
		assertTrue(object.includesSelector("foo"));
	}

	@Test
	public void shouldFindMethodForSelector() {
		PrimObject aMethod = new PrimObject();
		PrimObjectClass object = new PrimObjectClass();
		object.methods().put("aMethod", aMethod);
		assertEquals(object.methodFor("aMethod"), aMethod);
	}

	@Test
	public void shouldKnowIfNotIncludesSelector() {
		PrimObjectClass object = new PrimObjectClass();
		assertFalse(object.includesSelector("abscentMethod"));
	}

	@Test
	public void performShouldInvokeDNUWhenNoMethodsSet() {
		PrimObjectClass object = new PrimObjectClass();
		try {
			object.perform("aMethod");
		} catch (RedlineException e) {
			assertEquals(e.getMessage(), "Object '" + object.toString() + "' (" + object.cls().toString() + ") does not understand 'aMethod'.");
		}
	}

	@Test
	public void performShouldInvokeMethodWhenMethodSet() {
		final PrimObject result = new PrimObject();
		PrimObjectClass object = new PrimObjectClass();
		object.methods().put("existingMethod", new PrimObject() {
			public PrimObject invoke(PrimObject receiver, PrimContext primContext) {
				return result;
			}
		});
		assertEquals(object.perform0(object, "existingMethod"), result);
	}
}

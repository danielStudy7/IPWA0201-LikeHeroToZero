package fastTest.apiv1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.apiv1.mapper.AbstractMapper;

public abstract class AbstractMapperTest<S, T> {

	protected AbstractMapper<S, T> mapper;
	
	protected abstract AbstractMapper<S, T> createMapper();
	
	protected abstract S createValidSource();
	
	protected abstract void assertMapped(S source, T target);
	
	@BeforeEach
	void initMapper() {
		
		mapper = createMapper();
		
		assertNotNull(mapper, "Mapper konnte nicht erzeugt werden!");
	}
	
	@Test
	void testMappNullSource() {
		
		assertNull(mapper.map((S) null));
	}
	
	@Test
	void testMapCollection() {
		
		S first = createValidSource();
		S second = createValidSource();
		
		List<T> targets = mapper.map(Arrays.asList(first, second));
		
		assertEquals(2, targets.size());
        assertMapped(first, targets.get(0));
        assertMapped(second, targets.get(1));
	}
	
	@Test
	void testMapNullCollection() {
		
		List<S> nullList = null;
		List<T> targets = mapper.map(nullList);
		
		assertNotNull(targets);
		assertTrue(targets.isEmpty());
	}
	
	@Test
	void testMapEmptyCollection() {
		
		assertTrue(mapper.map(Collections.emptyList()).isEmpty());
	}
	
	@Test
	void testMapCollectionWithNullElement() {
		
		List<S> sources = Arrays.asList(createValidSource(), null);
		
		List<T> targets = mapper.map(sources);
		
		assertEquals(1, targets.size());
	}
	
	@Test
	void testMapOptionalPresent() {
		
		S source = createValidSource();
		
		Optional<T> result = mapper.mapOptional(source);
		
		assertTrue(result.isPresent());
		assertMapped(source, result.get());
	}
	
	@Test
	void testMapOptionalEmpty() {
		
		assertTrue(mapper.mapOptional(null).isEmpty());
	}
	
	@Test
	void testMapRequiredValidSource() {
		
		S source = createValidSource();
		
		assertMapped(source, mapper.mapRequired(source));
	}
	
	@Test
	void testMapRequiredNullSource() {
		
		assertThrows(NullPointerException.class, () -> mapper.mapRequired(null));
	}
	
	@Test
	void testMapCreatesNewInstance() {
		
		S source = createValidSource();
		
		T first = mapper.map(source);
		T second = mapper.map(source);
		
		assertNotNull(first);
		assertNotNull(second);
		assertTrue(first != second, "Mapper muss eine neue Instanz erzeugen.");
		assertSame(first.getClass(), second.getClass());
	}
}

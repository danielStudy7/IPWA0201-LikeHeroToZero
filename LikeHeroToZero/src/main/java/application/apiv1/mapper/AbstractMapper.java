package application.apiv1.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractMapper<S, T> {
	
	/**
	 * Mapping Methode zum Setzen der Felder von {@code source} nach {@code target}.
	 * 
	 * @param source carf nicht {@code null} sein
	 * @return das Zielobjekt, niemals {@code null}
	 */
	protected abstract T mapInterval(S source);
	
	/**
	 * Mapping eines einzelnen Objekts. {@code null} wird zu {@code null} gemappt.
	 * 
	 * @param source
	 * @return
	 */
	public final T map(S source) {
		
		if (source == null) {
			return null;
		}
		
		T target = mapInterval(source);
		
		return target;
	}
	
	/**
	 * Mapping von Listen. {@code null} wird zu einer leeren Liste.
	 * 
	 * @param sources
	 * @return
	 */
	public final List<T> map(Collection<? extends S> sources) {
		
		if (sources == null || sources.isEmpty()) {
			return Collections.emptyList();
		}
		
		return sources.stream()
				.filter(Objects::nonNull)
				.map(this::mapInterval)
				.toList();
	}
	
	public final Optional<T> mapOptional(S source) {
		
		return Optional.ofNullable(map(source));
	}
	
	public final T mapRequired(S source) {
		
		return map(Objects.requireNonNull(source, "Die Quelle darf nicht null sein."));
	}
}
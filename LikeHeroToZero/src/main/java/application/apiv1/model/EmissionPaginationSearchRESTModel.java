package application.apiv1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import model.Country;

public class EmissionPaginationSearchRESTModel {
	
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private int page;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private int entriesPerPage;
	private EmissionSortFieldRESTModel sortBy;
	@Schema(defaultValue = "false", description = "Für eine absteigende Sortierung 'true' angeben.")
	private boolean sortDescending;
	@Schema(description = "Optionales Feld, zum Filtern nach einem spezifischen Land.")
	private Country filterCountry;
	@Schema(description = "Optionales Feld, zum Filtern nach einem spezifischen Jahr.")
	private Integer filterYear;
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getEntriesPerPage() {
		return entriesPerPage;
	}
	
	public void setEntriesPerPage(int entriesPerPage) {
		this.entriesPerPage = entriesPerPage;
	}
	
	public EmissionSortFieldRESTModel getSortBy() {
		return sortBy;
	}
	
	public void setSortBy(EmissionSortFieldRESTModel sortBy) {
		this.sortBy = sortBy;
	}
	
	public boolean isSortDescending() {
		return sortDescending;
	}
	
	public void setSortDescending(boolean sortDescending) {
		this.sortDescending = sortDescending;
	}

	public Country getFilterCountry() {
		return filterCountry;
	}

	public void setFilterCountry(Country filterCountry) {
		this.filterCountry = filterCountry;
	}

	public Integer getFilterYear() {
		return filterYear;
	}

	public void setFilterYear(Integer filterYear) {
		this.filterYear = filterYear;
	}
}

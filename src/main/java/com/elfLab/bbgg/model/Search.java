package com.elfLab.bbgg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Search extends Pagination {

	private String searchType;
	
	private String keyword;	
			
	
}

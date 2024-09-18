package com.mlorenzo.reactiveproductservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlorenzo.reactiveproductservice.dto.ProductDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("product")
public class ProductStreamController {
	
	@Autowired
	private Sinks.Many<ProductDto> sink;
	
	@GetMapping(path = "stream/{maxPrice}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ProductDto> getProductUpdates(@PathVariable int maxPrice) {
		return sink.asFlux()
				.filter(pDto -> pDto.getPrice() <= maxPrice);
	}

}

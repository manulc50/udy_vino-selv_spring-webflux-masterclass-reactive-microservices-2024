package com.mlorenzo.webfluxdemo.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.mlorenzo.webfluxdemo.dto.Response;
import com.mlorenzo.webfluxdemo.util.SleepUtil;

@Service
public class MathService {

	public Response findSquare(int input) {
		return new Response(input * input);
	}
	
	public List<Response> multiplicationTable(int input) {
		return IntStream.rangeClosed(1, 10)
				.peek(i -> SleepUtil.sleepSeconds(1))
				.peek(i -> System.out.println("math-service procesing:  " + i))
				.mapToObj(i -> new Response(i * input))
				.collect(Collectors.toList());
	}
}

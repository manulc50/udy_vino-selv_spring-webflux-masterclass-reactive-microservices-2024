package com.mlorenzo.webfluxdemo.webclient;

import org.springframework.boot.test.context.SpringBootTest;

// Con el atributo webEnvironment puesto a SpringBootTest.WebEnvironment.DEFINED_PORT, el servidor de las pruebas utiliza el mismo puerto definido en esta aplicación RESTful
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BaseTest {


}

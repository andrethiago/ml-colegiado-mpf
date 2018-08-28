package br.mp.mpf.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"br.mp.mpf"})
@PropertySource("classpath:applicationScriptsDB.properties")
@Import(value = {PersistenceConfig.class})
public class AppConfig {

}

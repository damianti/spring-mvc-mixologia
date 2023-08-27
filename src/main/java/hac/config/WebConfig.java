/**
 WebConfig.java:
 The WebConfig class is a configuration class that sets up web-related configurations for the application.
 */
package hac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;


@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    /**
     * Creates a HiddenHttpMethodFilter bean for supporting hidden HTTP methods in forms.
     *
     * @return the HiddenHttpMethodFilter bean
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    /**
     * Configures resource handlers for serving static resources.
     *
     * @param registry the ResourceHandlerRegistry to configure
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/assets/**", "/css/**", "/js/**")
                .addResourceLocations("classpath:/static/assets/", "classpath:/static/css/", "classpath:/static/js/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

}
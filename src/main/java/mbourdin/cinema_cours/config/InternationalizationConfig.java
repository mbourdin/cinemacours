package mbourdin.cinema_cours.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;


@Configuration
public class InternationalizationConfig extends WebMvcConfigurerAdapter {

    /**
     * Set encoding and prefix file name of messages resources
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    private MessageSource messageSourceValidator() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/i18n/validationMessages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSourceValidator());
        return bean;
    }

    @Bean
    public LocaleResolver localeResolver() {
// stores locale info in the session
        SessionLocaleResolver resolver = new SessionLocaleResolver();
//default to US locale
        resolver.setDefaultLocale(Locale.FRENCH);
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
//the request param that we'll use to determine the locale
        interceptor.setParamName("lang");
        return interceptor;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}

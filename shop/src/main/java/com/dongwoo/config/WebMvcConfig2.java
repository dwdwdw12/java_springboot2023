package com.dongwoo.config;

//import org.springframework.context.annotation.Configuration;
//        import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
//        import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//        import org.thymeleaf.spring5.view.ThymeleafViewResolver;
//
//@Configuration //이 클래스는 설정정보파일임을 의미하는 Annotation
//public class WebMvcConfig2  implements WebMvcConfigurer {
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//        resolver.setContentType("text/html;charset=UTF-8");
//        // resolver.setCharset("UTF-8")
//        // resolver.setPrefix("classpath:/templates/")
//        // resolver.setSuffix(".html") ...
//        // 타임리프가 아닌 다른 템플릿 View를 사용할 경우 필요에 따라 설정해준다.
//
//        registry.viewResolver(resolver);
//    }
//}
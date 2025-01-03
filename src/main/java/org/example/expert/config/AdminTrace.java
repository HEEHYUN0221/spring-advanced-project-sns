package org.example.expert.config;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;


@Aspect
@Component
public class AdminTrace {

  private final Logger logger = LoggerFactory.getLogger(AdminTrace.class.getName());
  private final StringHttpMessageConverter stringHttpMessageConverter;

  public AdminTrace(StringHttpMessageConverter stringHttpMessageConverter) {
    this.stringHttpMessageConverter = stringHttpMessageConverter;
  }


  @Around("execution(public * org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))||"
      + "execution(public * org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
  public Object adminTracelog(ProceedingJoinPoint joinPoint) throws Throwable {

    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
    HttpServletResponse response = attributes.getResponse();
    //로깅 내용에 요청 사용자 ID, 요청 시각, 요청 URL, 요청 본문, 응답 본문 기록 +  Logger 클래스 이용
    //실행전
    logger.info("ADMIN 기능 실행");
    logger.info("요청 시각 : {}", LocalDateTime.now());
    logger.info("요청 URL : {}", request.getRequestURI());
    logger.info("요청 사용자 ID : {}", request.getAttribute("userId"));
    logger.info("요청 본문 : {} ", getRequestBody(request));
    //메서드 실행
    Object result = joinPoint.proceed();

    //실행후
    logger.info("응답 본문 : {} ", getResponseBody(response));
    logger.info("실행 완료");


    return result;
  }

  private String getRequestBody(HttpServletRequest request) throws IOException {
    ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
    byte[] contentAsByteArray = wrapper.getContentAsByteArray();
    return new String(contentAsByteArray, wrapper.getCharacterEncoding());
  }

  private String getResponseBody(HttpServletResponse response) throws UnsupportedEncodingException {
    ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
    byte[] contentAsByteArray = wrapper.getContentAsByteArray();
    return new String(contentAsByteArray, wrapper.getCharacterEncoding());
  }


}

package ru.practicum.shareit.gateway.config;

import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import io.swagger.v3.oas.models.parameters.Parameter;
import java.util.HashMap;

// https://github.com/springdoc/springdoc-openapi/issues/868

/**
 * Allows to use the default value for enum when using springdoc
 */
@Component
public class DefaultValueParameterCustomizer implements ParameterCustomizer {

    @Override
    public Parameter customize(Parameter parameterModel, MethodParameter methodParameter) {
        RequestParam requestParamAnnotation = methodParameter.getParameterAnnotation(RequestParam.class);
        if (requestParamAnnotation != null &&
                !ValueConstants.DEFAULT_NONE.equals(requestParamAnnotation.defaultValue())) {
            String defaultValue = requestParamAnnotation.defaultValue();
            parameterModel.setExtensions(new HashMap<>());
            parameterModel.getExtensions().put("default", defaultValue);
        }
        return parameterModel;
    }
}
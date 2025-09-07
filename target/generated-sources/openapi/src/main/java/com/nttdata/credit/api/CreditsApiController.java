package com.nttdata.credit.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
@Controller
@RequestMapping("${openapi.creditService.base-path:}")
public class CreditsApiController implements CreditsApi {

    private final CreditsApiDelegate delegate;

    public CreditsApiController(@org.springframework.beans.factory.annotation.Autowired(required = false) CreditsApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new CreditsApiDelegate() {});
    }

    @Override
    public CreditsApiDelegate getDelegate() {
        return delegate;
    }

}

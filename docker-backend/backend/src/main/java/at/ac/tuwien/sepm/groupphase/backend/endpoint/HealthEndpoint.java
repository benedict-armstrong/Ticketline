package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import javax.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * This endpoint is used for kubernetes health checks.
 */
@RestController
@RequestMapping("/health")
public class HealthEndpoint {

    private boolean healthy = true;

    @PermitAll
    @GetMapping
    public String getHealth() {
        if (healthy) {
            return "OK";
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Previously to a shutdown of a pod this url will be called. Afterwards the health probes fail. Therefore the pod
     * is removed from the healthy pods which are exposed. This way a zero downtime upgrade is possible.
     */
    @PermitAll
    @GetMapping("/prepareShutdown")
    public void preShutdown() {
        healthy = false;
        try {
            Thread.sleep(15_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

package net.taskwolf.manual.access;

import net.taskwolf.core.access.TaskwolfRequestBody;
import net.taskwolf.core.access.TaskwolfRestController;
import com.google.common.collect.Maps;
import jakarta.servlet.http.HttpServletResponse;
import net.taskwolf.core.user.UserDatabaseTable;
import net.taskwolf.workflow.WorkflowModule;
import net.taskwolf.workflow.structure.WorkflowDatabaseTable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class ManualTriggerController extends TaskwolfRestController {
  private final WorkflowModule workflowModule;
  private final WorkflowDatabaseTable workflowDatabaseTable;

  private ManualTriggerController(
    Key secretKey, UserDatabaseTable userDatabaseTable,
    WorkflowModule workflowModule, WorkflowDatabaseTable workflowDatabaseTable
  ) {
    super(secretKey, userDatabaseTable);
    this.workflowModule = workflowModule;
    this.workflowDatabaseTable = workflowDatabaseTable;
  }

  @RequestMapping(path = "/workflow/manual/execute/", method = RequestMethod.POST)
  public CompletableFuture<Map<String, Object>> executeWorkflow(
    @RequestBody String payload, HttpServletResponse response
  ) {
    var body = TaskwolfRequestBody.of(payload, response);
    return workflowDatabaseTable.findWorkflow(body.getUUID("workflow"))
      .thenCompose(entry -> workflowModule.createWorkflow(entry.triggerId())
        .thenCompose(workflow -> workflow.trigger(Maps.newHashMap())
          .thenApply(success -> Map.of("success", success))));
  }
}


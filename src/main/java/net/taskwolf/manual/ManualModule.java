package net.taskwolf.manual;

import com.google.inject.Injector;
import net.taskwolf.core.account.AccountLink;
import net.taskwolf.workflow.action.ActionRepository;
import net.taskwolf.core.database.DatabaseConnection;
import net.taskwolf.core.database.DatabaseKeyspace;
import net.taskwolf.core.log.Log;
import net.taskwolf.workflow.integration.Integration;
import net.taskwolf.core.module.ModuleDescription;
import net.taskwolf.core.module.ModuleInformation;
import net.taskwolf.core.module.ModuleLoadPriority;
import net.taskwolf.workflow.trigger.TriggerRepository;
import net.taskwolf.manual.trigger.ManualTrigger;

@ModuleDescription(name = "manual", version = "1.0.0-SNAPSHOT",
  priority = ModuleLoadPriority.NEUTRAL)
public final class ManualModule extends Integration {
  private Log log;
  private AccountLink accountLink;

  public ManualModule(Injector injector) {
    super(injector);
  }

  @Override
  public void enable() throws Exception {
    log = injector().getInstance(Log.class).subLog("Manual");
    accountLink = ManualAccountLink.create();
  }

  @Override
  public void disable() {

  }

  @Override
  public AccountLink accountLink() {
    return accountLink;
  }

  @Override
  public ModuleInformation moduleInformation() {
    return ModuleInformation.create("manual", "", "manual.png",
      ModuleInformation.Type.PUBLIC);
  }

  @Override
  public TriggerRepository triggerRepository() {
    var databaseConnection = injector().getInstance(DatabaseConnection.class);
    var databaseKeyspace = injector().getInstance(DatabaseKeyspace.class);
    var repository = TriggerRepository.create();
    repository.registerTrigger(ManualTrigger.create(
      databaseConnection, databaseKeyspace));
    return repository;
  }

  @Override
  public ActionRepository actionRepository() {
    return ActionRepository.create();
  }
}

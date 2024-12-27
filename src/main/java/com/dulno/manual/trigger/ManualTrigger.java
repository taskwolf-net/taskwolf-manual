package com.dulno.manual.trigger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.dulno.workflow.trigger.Trigger;
import com.dulno.workflow.trigger.TriggerContentDatabaseTable;
import com.dulno.workflow.trigger.TriggerInformation;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "create")
public final class ManualTrigger implements Trigger {
  public static ManualTrigger create(
    DatabaseConnection databaseConnection, DatabaseKeyspace databaseKeyspace
  ) {
    return new ManualTrigger(TriggerContentDatabaseTable.create(
      databaseConnection, databaseKeyspace, "trigger_manual",
      Lists.newArrayList()));
  }

  private final TriggerContentDatabaseTable contentDatabaseTable;

  @Override
  public String type() {
    return "manual-trigger";
  }

  @Override
  public TriggerInformation information() {
    return TriggerInformation.builder()
      .withName("manual.trigger.name")
      .withDescription("manual.trigger.description")
      .build();
  }

  @Override
  public void initialize() {
    contentDatabaseTable.createIfNotExists();
  }

  @Override
  public CompletableFuture<Void> insert(UUID triggerId, Map<String, Object> content) {
    return contentDatabaseTable.insertContent(triggerId, DatabaseRow.of());
  }

  @Override
  public CompletableFuture<Map<String, Object>> findContent(UUID triggerId) {
    return CompletableFuture.completedFuture(Maps.newHashMap());
  }

  @Override
  public CompletableFuture<List<UUID>> findEntries(DatabaseCondition condition) {
    return contentDatabaseTable.findContentByCondition(condition).thenApply(
      rows -> rows.stream().map(row -> row.findCell(0).uuidValue()).toList());
  }

  @Override
  public CompletableFuture<Void> delete(UUID triggerId) {
    return contentDatabaseTable.deleteContent(triggerId);
  }
}

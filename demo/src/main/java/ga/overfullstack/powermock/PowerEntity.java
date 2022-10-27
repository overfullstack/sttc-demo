package ga.overfullstack.powermock;

import ga.overfullstack.legacy.DBUtil.Powers;
import ga.overfullstack.legacy.Entity;
import org.jetbrains.exposed.sql.Table;

public class PowerEntity extends Entity {

  @Override
  public Table getTable() {
    return Powers.INSTANCE;
  }
}

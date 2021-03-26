import { Module, Global, DynamicModule } from '@nestjs/common';
import { DatabaseService } from './database.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { DATABASE } from '../constants';

const DatabaseOrmModule = (dbName: string): DynamicModule => {
  const config = new DatabaseService(dbName).read();

  const entities =
    config.TYPE == 'mariadb'
      ? DATABASE.MARIA_DB.ENTITIES
      : DATABASE.MONGO_DB.ENTITIES;

  return TypeOrmModule.forRoot({
    type: config.TYPE,
    host: config.HOST,
    port: config.PORT,
    username: config.USERNAME,
    password: config.PASSWORD,
    database: config.NAME,
    entities: entities,
    synchronize: false,
  });
};

@Global()
@Module({
  imports: [DatabaseOrmModule('mariadb')],
})
export class DatabaseModule {}

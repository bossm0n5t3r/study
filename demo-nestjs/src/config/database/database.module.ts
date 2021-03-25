import { Module, Global, DynamicModule } from '@nestjs/common';
import { DatabaseService } from './database.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import {
  mariaDatabaseEntities,
  mongoDatabaseEntities,
} from './database.entity';

const DatabaseOrmModule = (dbName: string): DynamicModule => {
  const config = new DatabaseService(dbName).read();

  const entities =
    config.TYPE == 'mariadb' ? mariaDatabaseEntities : mongoDatabaseEntities;

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

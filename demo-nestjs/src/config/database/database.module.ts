import { Module, Global, DynamicModule } from '@nestjs/common';
import { DatabaseService } from './database.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { DATABASE } from '../../constants';
import { MongooseModule } from '@nestjs/mongoose';

const DatabaseOrmModule = (dbName: string): DynamicModule => {
  const config = new DatabaseService(dbName).read();

  let resultDatabaseOrmModule;

  switch (dbName) {
    case 'mariadb': {
      const entities = DATABASE.MARIA_DB.ENTITIES;
      resultDatabaseOrmModule = TypeOrmModule.forRoot({
        type: config.TYPE,
        host: config.HOST,
        port: config.PORT,
        username: config.USERNAME,
        password: config.PASSWORD,
        database: config.NAME,
        entities: entities,
        synchronize: false,
      });
      break;
    }
    case 'mongodb': {
      const uri = `mongodb://${config.HOST}:${config.PORT}/${config.NAME}`;
      resultDatabaseOrmModule = MongooseModule.forRoot(uri);
    }
  }

  return resultDatabaseOrmModule;
};

@Global()
@Module({
  imports: [DatabaseOrmModule('mariadb'), DatabaseOrmModule('mongodb')],
})
export class DatabaseModule {}
